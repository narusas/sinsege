package net.narusas.si.auction.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.jeta.forms.components.panel.FormPanel;

public abstract class App extends JFrame {
	private static final long serialVersionUID = -8677640340576937392L;
	public static ApplicationContext context;
	protected Controller controller;
	protected final Logger logger = LoggerFactory.getLogger("auction");

	public App(String title) {
		super(title);
		initLogger();

		getContentPane().setLayout(new BorderLayout());

		FormPanel modelPanel = initModelView();

		FormPanel controlPanel = initControlView();

		JPanel contentPanel = new JPanel();
		FormPanel statusPanel = initStatusView(contentPanel);
		FormPanel logPanel = initLogView(contentPanel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1024, 700);
		center();
		setVisible(true);

		init();
		bind(modelPanel, controlPanel, statusPanel, logPanel);
		if (controller != null) {
			controller.enableControl(true);
		}

	}

	protected void center() {
		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Determine the new location of the window
		int w = this.getSize().width;
		int h = this.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;

		// Move the window
		this.setLocation(x, y);
	}

	public void init() {
		createInitializer().init();
	}

	public abstract void bind(FormPanel modelPanel, FormPanel controlPanel, FormPanel statusPanel, FormPanel logPanel);

	public abstract void run();

	public abstract FormPanel initLogView(JPanel contentPanel);

	public abstract FormPanel initModelView();

	public abstract FormPanel initControlView();

	public abstract FormPanel initStatusView(JPanel contentPanel);

	public abstract Controller createController();

	public abstract Initializer createInitializer();

	public void initLogger() {
		java.util.logging.Logger.getLogger("auction").addHandler(LogConsoleThread.getInstance());

	}

	public abstract void bindController(FormPanel modelPanel, FormPanel controlPanel, FormPanel logPanel);
}
