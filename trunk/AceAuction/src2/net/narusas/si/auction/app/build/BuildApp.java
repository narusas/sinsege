package net.narusas.si.auction.app.build;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.app.Controller;
import net.narusas.si.auction.app.Initializer;
import net.narusas.si.auction.app.LogConsoleThread;
import net.narusas.si.auction.builder.Mode;

import com.jeta.forms.components.panel.FormPanel;

public class BuildApp extends App {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8456984555811789604L;

	public static void main(String[] args) {
		new BuildApp().run();
	}

	public static Mode mode = Mode.신건;

	public BuildApp() {
		super("Auction Builder");
	}

	public FormPanel initLogView(JPanel contentPanel) {
		FormPanel logPanel = new FormPanel("LogForm.jfrm");
		contentPanel.add(logPanel, BorderLayout.CENTER);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		LogConsoleThread.getInstance().setLogTextArea((JTextArea) logPanel.getComponentByName("logTextArea"));
		return logPanel;
	}

	public FormPanel initStatusView(JPanel contentPanel) {
		contentPanel.setLayout(new BorderLayout());
		FormPanel statusPanel = new FormPanel("StatusForm.jfrm");
		contentPanel.add(statusPanel, BorderLayout.NORTH);
		return statusPanel;
	}

	public FormPanel initControlView() {
		FormPanel controlPanel = new FormPanel("ControlForm.jfrm");
		getContentPane().add(controlPanel, BorderLayout.SOUTH);
		return controlPanel;
	}

	public FormPanel initModelView() {
		JPanel modelSelectionPanel = new JPanel();
		modelSelectionPanel.setLayout(new BorderLayout());

		FormPanel modelPanel = new FormPanel("ModelForm.jfrm");

		modelSelectionPanel.add(modelPanel, BorderLayout.CENTER);
		getContentPane().add(modelSelectionPanel, BorderLayout.WEST);
		return modelPanel;
	}

	public void bind(FormPanel modelPanel, FormPanel controlPanel, FormPanel statusPanel, FormPanel logPanel) {
		controller = createController();
		bindController(modelPanel, controlPanel, logPanel);
		bindEventNotifier(statusPanel);

	}

	public Controller createController() {
		return new BuildController();
	}

	protected void bindEventNotifier(FormPanel statusPanel) {
		EventNotifier.set법원StatusLabel((JLabel) statusPanel.getComponentByName("courtStatus"));
		EventNotifier.set법원CurrentLabel((JLabel) statusPanel.getComponentByName("courtCurrent"));

		EventNotifier.set담당계StatusLabel((JLabel) statusPanel.getComponentByName("chargeStatus"));
		EventNotifier.set담당계CurrentLabel((JLabel) statusPanel.getComponentByName("chargeCurrent"));

		EventNotifier.set사건StatusLabel((JLabel) statusPanel.getComponentByName("eventStatus"));
		EventNotifier.set사건CurrentLabel((JLabel) statusPanel.getComponentByName("eventCurrent"));

		EventNotifier.set물건StatusLabel((JLabel) statusPanel.getComponentByName("goodsStatus"));
		EventNotifier.set물건CurrentLabel((JLabel) statusPanel.getComponentByName("goodsCurrent"));

		EventNotifier.set사진StatusLabel((JLabel) statusPanel.getComponentByName("picStatus"));
		EventNotifier.set업로드StatusLabel((JLabel) statusPanel.getComponentByName("uploadStatus"));
	}

	public void bindController(FormPanel modelPanel, FormPanel controlPanel, FormPanel logPanel) {
		BuildController c = (BuildController) controller;
		c.set법원List((JList) modelPanel.getComponentByName("courtList"));
		c.set담당계List((JList) modelPanel.getComponentByName("chargeList"));
		c.set전체실행Button((JButton) controlPanel.getComponentByName("doAllBtn"));
		c.set선택실행Button((JButton) controlPanel.getComponentByName("doSelectedBtn"));
		c.set단일실행Button((JButton) controlPanel.getComponentByName("doSIngleEventBtn"));
		c.set단일사건TextField((JTextField) controlPanel.getComponentByName("singleEventYearTextField"),
				(JTextField) controlPanel.getComponentByName("singleEventNoTextField"));
		c.set여기부터TextField((JTextField) controlPanel.getComponentByName("filterEventYearTextField"),
				(JTextField) controlPanel.getComponentByName("filterEventNoTextField"));

		c.set신건RadioBtn((JRadioButton) controlPanel.getComponentByName("newbieBtn"));
		c.set매각물건명세서RadioBtn((JRadioButton) controlPanel.getComponentByName("goodsStatementBtn"));
		c.set등기부등본RadioBtn((JRadioButton) controlPanel.getComponentByName("attestedBtn"));

		c.setClearLogButton((JButton) logPanel.getComponentByName("clearLogBtn"));
		c.setLogTetArea((JTextArea) logPanel.getComponentByName("logTextArea"));

	}

	

	public Initializer createInitializer() {
		return new Initializer();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
