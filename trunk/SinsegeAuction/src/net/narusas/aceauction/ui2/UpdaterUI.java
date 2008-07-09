/*
 * 
 */
package net.narusas.aceauction.ui2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// TODO: Auto-generated Javadoc
/**
 * The Class UpdaterUI.
 */
public class UpdaterUI extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The day text field. */
	private JTextField dayTextField = null;

	/** The j content pane. */
	private JPanel jContentPane = null;

	/** The j label. */
	private JLabel jLabel = null;

	/** The j label1. */
	private JLabel jLabel1 = null;

	/** The j label2. */
	private JLabel jLabel2 = null;

	/** The j panel. */
	private JPanel jPanel = null;

	/** The j scroll pane. */
	private JScrollPane jScrollPane = null;

	/** The log text area. */
	private JTextArea logTextArea = null;

	/** The month text field. */
	private JTextField monthTextField = null;

	/** The update button. */
	private JButton updateButton = null;

	/** The year text field. */
	private JTextField yearTextField = null;

	/**
	 * This is the default constructor.
	 */
	public UpdaterUI() {
		super();
		initialize();
	}

	/**
	 * This method initializes dayTextField.
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getDayTextField() {
		if (dayTextField == null) {
			dayTextField = new JTextField();
			dayTextField.setPreferredSize(new Dimension(30, 22));
		}
		return dayTextField;
	}

	/**
	 * This method initializes logTextArea.
	 * 
	 * @return javax.swing.JTextArea
	 */
	public JTextArea getLogTextArea() {
		if (logTextArea == null) {
			logTextArea = new JTextArea();
		}
		return logTextArea;
	}

	/**
	 * This method initializes monthTextField.
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getMonthTextField() {
		if (monthTextField == null) {
			monthTextField = new JTextField();
			monthTextField.setPreferredSize(new Dimension(50, 22));
		}
		return monthTextField;
	}

	/**
	 * This method initializes updateButton.
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getUpdateButton() {
		if (updateButton == null) {
			updateButton = new JButton();
			updateButton.setText("Update");
		}
		return updateButton;
	}

	/**
	 * This method initializes yearTextField.
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getYearTextField() {
		if (yearTextField == null) {
			yearTextField = new JTextField();
			yearTextField.setPreferredSize(new Dimension(50, 22));
			yearTextField.setHorizontalAlignment(JTextField.LEFT);
		}
		return yearTextField;
	}

	/**
	 * This method initializes jContentPane.
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), BorderLayout.NORTH);
			jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel.
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel2 = new JLabel();
			jLabel2.setText("��");
			jLabel1 = new JLabel();
			jLabel1.setText("��");
			jLabel = new JLabel();
			jLabel.setText("����");
			jPanel = new JPanel();
			jPanel.setLayout(new FlowLayout());
			jPanel.add(jLabel, null);
			jPanel.add(getYearTextField(), null);
			jPanel.add(jLabel1, null);
			jPanel.add(getMonthTextField(), null);
			jPanel.add(jLabel2, null);
			jPanel.add(getDayTextField(), null);
			jPanel.add(getUpdateButton(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jScrollPane.
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getLogTextArea());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes this.
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(889, 464);
		this.setContentPane(getJContentPane());
		this.setTitle("Ace Updater");
	}

} // @jve:decl-index=0:visual-constraint="10,10"
