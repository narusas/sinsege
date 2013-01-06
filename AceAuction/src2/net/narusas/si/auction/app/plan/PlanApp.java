package net.narusas.si.auction.app.plan;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.slf4j.LoggerFactory;

import net.narusas.si.auction.app.LogConsoleThread;

public class PlanApp {
	public static void main(String[] args) {
		new PlanApp();
	}

	private PlanAppView view;
	private PlanAppController controller;
	private PlanInitiator initiator;

	public PlanApp() {
		initiator = new PlanInitiator();
		initiator.init();
		view = new PlanAppView();
		controller = new PlanAppController();
		controller.bind(view);
		view.bind(controller);
		view.setVisible(true);
		center();
		initLogger();
	}
	
	public void initLogger() {
		java.util.logging.Logger.getLogger("auction").addHandler(LogConsoleThread.getInstance());
		LogConsoleThread.getInstance().setLogTextArea(view.getLogTextArea());
	}

	protected void center() {
		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Determine the new location of the window
		int w = view.getSize().width;
		int h = view.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;

		// Move the window
		view.setLocation(x, y);
	}

}
