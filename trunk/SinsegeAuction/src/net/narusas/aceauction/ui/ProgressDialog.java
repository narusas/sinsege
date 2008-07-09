/*
 * 
 */
package net.narusas.aceauction.ui;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

// TODO: Auto-generated Javadoc
/**
 * The Class ProgressDialog.
 */
public class ProgressDialog extends JDialog {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The j content pane. */
	private JPanel jContentPane = null;

	/** The j progress bar. */
	private JProgressBar jProgressBar = null;

	/** The j scroll pane. */
	private JScrollPane jScrollPane = null;

	/** The j text area. */
	private JTextArea jTextArea = null;

	/** The parent2. */
	private final JFrame parent2;

	/**
	 * The Constructor.
	 * 
	 * @param parent the parent
	 */
	public ProgressDialog(JFrame parent) {
		super();
		parent2 = parent;
		initialize();
	}

	/**
	 * This method initializes jProgressBar.
	 * 
	 * @return javax.swing.JProgressBar
	 */
	public JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
			jProgressBar.setStringPainted(true);
		}
		return jProgressBar;
	}

	/**
	 * This method initializes jTextArea.
	 * 
	 * @return javax.swing.JTextArea
	 */
	public JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setEnabled(true);
			jTextArea.setWrapStyleWord(true);
			jTextArea.setEditable(false);
		}
		return jTextArea;
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
			jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
			jContentPane.add(getJProgressBar(), BorderLayout.NORTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jScrollPane.
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJTextArea());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes this.
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(750, 400);
		this.setName("ProgressDialog");
		this.setTitle("진행상황");
		this.setContentPane(getJContentPane());
		this.setResizable(true);
	}

} // @jve:decl-index=0:visual-constraint="71,31"
