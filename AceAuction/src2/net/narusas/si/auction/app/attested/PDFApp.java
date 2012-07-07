package net.narusas.si.auction.app.attested;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.app.Controller;
import net.narusas.si.auction.app.Initializer;
import net.narusas.si.auction.app.LogConsoleThread;

import com.jeta.forms.components.panel.FormPanel;

public class PDFApp extends App {

	public PDFApp() {
		super("대법원 등기부등본");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8673432719648289049L;

	public static void main(String[] args) {
		new PDFApp().run();
//		new Browser();
	}

	@Override
	public FormPanel initModelView() {
		JPanel modelSelectionPanel = new JPanel();
		modelSelectionPanel.setLayout(new BorderLayout());

		FormPanel modelPanel = new FormPanel("PDFModel.jfrm");

		modelSelectionPanel.add(modelPanel, BorderLayout.CENTER);
		getContentPane().add(modelSelectionPanel, BorderLayout.CENTER);
		return modelPanel;
	}

	@Override
	public FormPanel initControlView() {
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());

		FormPanel controlForm = new FormPanel("PDFControl.jfrm");

		controlPanel.add(controlForm, BorderLayout.CENTER);
		getContentPane().add(controlPanel, BorderLayout.SOUTH);
		LogConsoleThread.getInstance().setLogTextArea((JTextArea) controlForm.getComponentByName("logTextArea"));

		return controlForm;
	}

	@Override
	public Controller createController() {
		return new PDFController();
	}

	@Override
	public Initializer createInitializer() {
		return new Initializer();
	}

	public FormPanel initLogView(JPanel contentPanel) {

		return null;
	}

	@Override
	public void bind(FormPanel modelPanel, FormPanel controlPanel, FormPanel statusPanel, FormPanel logPanel) {
		PDFController c = (PDFController) createController();
		c.setFrame((JFrame)this);
		c.set법원List((JList) modelPanel.getComponentByName("courtList"));
		c.set담당계List((JList) modelPanel.getComponentByName("chargeList"));
		c.set사건List((JList) modelPanel.getComponentByName("eventList"));
		c.set물건List((JList) modelPanel.getComponentByName("goodsList"));
		c.set목록List((JList) modelPanel.getComponentByName("itemList"));

		c.set목록주소Label((JLabel) controlPanel.getComponentByName("addressLabel"));
		c.set목록구분Label((JLabel) controlPanel.getComponentByName("typeLabel"));
		c.set목록비고Label((JLabel) controlPanel.getComponentByName("commentLabel"));
		c.set실행Button((JButton)controlPanel.getComponentByName("executeButton"));
		
		c.set상태Label((JLabel)controlPanel.getComponentByName("statusLabel"));
	}

	@Override
	public FormPanel initStatusView(JPanel contentPanel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindController(FormPanel modelPanel, FormPanel controlPanel, FormPanel logPanel) {
		// TODO Auto-generated method stub

	}

}
