package net.narusas.si.auction.app.update;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.narusas.si.auction.app.Controller;
import net.narusas.si.auction.app.Initializer;
import net.narusas.si.auction.app.build.BuildApp;
import net.narusas.si.auction.app.build.BuildController;

import com.jeta.forms.components.panel.FormPanel;

public class UpdateApp extends BuildApp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1902281946500833962L;

	public static void main(String[] args) {
		new UpdateApp().run();
	}

	@Override
	public FormPanel initModelView() {
		JPanel modelSelectionPanel = new JPanel();
		modelSelectionPanel.setLayout(new BorderLayout());

		FormPanel modelPanel = new FormPanel("UpdateModelForm.jfrm");

		modelSelectionPanel.add(modelPanel, BorderLayout.CENTER);
		getContentPane().add(modelSelectionPanel, BorderLayout.WEST);
		return modelPanel;
	}

	@Override
	public FormPanel initControlView() {
		FormPanel controlPanel = new FormPanel("UpdateControlForm.jfrm");
		getContentPane().add(controlPanel, BorderLayout.SOUTH);
		return controlPanel;
	}

	@Override
	public Controller createController() {
		return new UpdateController();
	}

	@Override
	public Initializer createInitializer() {
		return new UpdateInitializaer();
	}

	@Override
	public void bindController(FormPanel modelPanel, FormPanel controlPanel, FormPanel logPanel) {
		BuildController c = (BuildController) controller;
		c.set법원List((JList) modelPanel.getComponentByName("courtList"));
		c.set전체실행Button((JButton) controlPanel.getComponentByName("doAllBtn"));
		c.set선택실행Button((JButton) controlPanel.getComponentByName("doSelectedBtn"));
		
		c.set단일실행Button((JButton) controlPanel.getComponentByName("doSIngleEventBtn"));
		((UpdateController)c).set지정물건실행Button((JButton) controlPanel.getComponentByName("doSpecBtn"));
		c.set단일사건TextField((JTextField) controlPanel.getComponentByName("singleEventYearTextField"),
				(JTextField) controlPanel.getComponentByName("singleEventNoTextField"));

		c.set기간TextFields((JTextField) controlPanel.getComponentByName("startYear"),//
				(JTextField) controlPanel.getComponentByName("startMonth"),//
				(JTextField) controlPanel.getComponentByName("startDay"),//

				(JTextField) controlPanel.getComponentByName("endYear"),//
				(JTextField) controlPanel.getComponentByName("endMonth"),//
				(JTextField) controlPanel.getComponentByName("endDay")//

				);
		((UpdateController) controller).set종류Combo((JComboBox) controlPanel.getComponentByName("typeCombo"));

		((UpdateController) controller).setDoneCheckbox((JCheckBox) controlPanel.getComponentByName("doneCheckbox"));

		c.setClearLogButton((JButton) logPanel.getComponentByName("clearLogBtn"));
		c.setLogTetArea((JTextArea) logPanel.getComponentByName("logTextArea"));

	}

}
