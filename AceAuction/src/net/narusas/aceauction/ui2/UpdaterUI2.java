package net.narusas.aceauction.ui2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class UpdaterUI2 extends JFrame {

	private static final long serialVersionUID = 1L;

	private JList chargeList = null;

	private JList courtList = null;

	private JTextField endDayTextField = null;

	private JTextField endMonthTextField = null;

	private JTextField endYearTextField = null;

	private JPanel jContentPane = null;

	private JLabel jLabel = null;

	private JLabel jLabel1 = null;

	private JLabel jLabel11 = null;

	private JLabel jLabel2 = null;

	private JLabel jLabel21 = null;

	private JLabel jLabel3 = null;

	private JPanel jPanel = null;

	private JPanel jPanel1 = null;

	private JPanel jPanel10 = null;

	private JPanel jPanel11 = null;

	private JPanel jPanel2 = null;

	private JPanel jPanel3 = null;

	private JPanel jPanel4 = null;

	private JPanel jPanel5 = null;

	private JPanel jPanel6 = null;

	private JPanel jPanel7 = null;

	private JPanel jPanel8 = null;

	private JPanel jPanel9 = null;

	private JScrollPane jScrollPane = null;

	private JScrollPane jScrollPane1 = null;

	private JScrollPane jScrollPane2 = null;

	private JScrollPane jScrollPane3 = null;

	private JSplitPane jSplitPane = null;

	private JTextArea logTextArea = null;

	private JList sagunList = null;

	private JTextField startDayTextField = null;

	private JTextField startMonthTextField = null;

	private JTextField startYearTextField = null;

	private JButton workAllButton = null;

	private JButton workChargeButton = null;

	private JButton workCourtButton = null;

	private JButton workSagunButton = null;

	/**
	 * This is the default constructor
	 */
	public UpdaterUI2() {
		super();
		initialize();
	}

	/**
	 * This method initializes chargeList
	 * 
	 * @return javax.swing.JList
	 */
	public JList getChargeList() {
		if (chargeList == null) {
			chargeList = new JList();
		}
		return chargeList;
	}

	/**
	 * This method initializes courtList
	 * 
	 * @return javax.swing.JList
	 */
	public JList getCourtList() {
		if (courtList == null) {
			courtList = new JList();
		}
		return courtList;
	}

	/**
	 * This method initializes endDayTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getEndDayTextField() {
		if (endDayTextField == null) {
			endDayTextField = new JTextField();
			endDayTextField.setPreferredSize(new Dimension(20, 22));
		}
		return endDayTextField;
	}

	/**
	 * This method initializes endMonthTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getEndMonthTextField() {
		if (endMonthTextField == null) {
			endMonthTextField = new JTextField();
			endMonthTextField.setPreferredSize(new Dimension(20, 22));
		}
		return endMonthTextField;
	}

	/**
	 * This method initializes endYearTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getEndYearTextField() {
		if (endYearTextField == null) {
			endYearTextField = new JTextField();
			endYearTextField.setPreferredSize(new Dimension(35, 22));
			endYearTextField.setText("9999");
		}
		return endYearTextField;
	}

	/**
	 * This method initializes logTextArea
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
	 * This method initializes sagunList
	 * 
	 * @return javax.swing.JList
	 */
	public JList getSagunList() {
		if (sagunList == null) {
			sagunList = new JList();
		}
		return sagunList;
	}

	/**
	 * This method initializes startDayTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getStartDayTextField() {
		if (startDayTextField == null) {
			startDayTextField = new JTextField();
			startDayTextField.setPreferredSize(new Dimension(20, 22));
			startDayTextField.setText("99");
		}
		return startDayTextField;
	}

	/**
	 * This method initializes startMonthTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getStartMonthTextField() {
		if (startMonthTextField == null) {
			startMonthTextField = new JTextField();
			startMonthTextField.setPreferredSize(new Dimension(20, 22));
			startMonthTextField.setText("99");
		}
		return startMonthTextField;
	}

	/**
	 * This method initializes startYearTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	public JTextField getStartYearTextField() {
		if (startYearTextField == null) {
			startYearTextField = new JTextField();
			startYearTextField.setText("9999");
			startYearTextField.setPreferredSize(new Dimension(35, 22));
		}
		return startYearTextField;
	}

	/**
	 * This method initializes workAllButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getWorkAllButton() {
		if (workAllButton == null) {
			workAllButton = new JButton();
			workAllButton.setText("전체작업");
		}
		return workAllButton;
	}

	/**
	 * This method initializes workChargeButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getWorkChargeButton() {
		if (workChargeButton == null) {
			workChargeButton = new JButton();
			workChargeButton.setText("작업");
		}
		return workChargeButton;
	}

	/**
	 * This method initializes workCourtButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getWorkCourtButton() {
		if (workCourtButton == null) {
			workCourtButton = new JButton();
			workCourtButton.setText("작업");
		}
		return workCourtButton;
	}

	/**
	 * This method initializes workSagunButton
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getWorkSagunButton() {
		if (workSagunButton == null) {
			workSagunButton = new JButton();
			workSagunButton.setText("작업");
		}
		return workSagunButton;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJSplitPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			gridLayout.setColumns(3);
			jPanel = new JPanel();
			jPanel.setLayout(gridLayout);
			jPanel.add(getJPanel2(), null);
			jPanel.add(getJPanel3(), null);
			jPanel.add(getJPanel4(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BorderLayout());
			jPanel1.add(getJScrollPane(), BorderLayout.CENTER);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel10
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel10() {
		if (jPanel10 == null) {
			jPanel10 = new JPanel();
			jPanel10.setLayout(new FlowLayout());
			jPanel10.add(getWorkSagunButton(), null);
		}
		return jPanel10;
	}

	/**
	 * This method initializes jPanel11
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel11() {
		if (jPanel11 == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 6;
			gridBagConstraints12.gridy = 1;
			jLabel21 = new JLabel();
			jLabel21.setText("일");
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.gridx = 5;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 4;
			gridBagConstraints10.gridy = 1;
			jLabel11 = new JLabel();
			jLabel11.setText("월");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.gridx = 3;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 2;
			gridBagConstraints8.gridy = 1;
			jLabel3 = new JLabel();
			jLabel3.setText("년");
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.weightx = 1.0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 4;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 2;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridx = 5;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 6;
			gridBagConstraints1.gridy = 0;
			jLabel2 = new JLabel();
			jLabel2.setText("일");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints3.gridx = 3;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			jLabel1 = new JLabel();
			jLabel1.setText("월");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0D;
			jLabel = new JLabel();
			jLabel.setText("년");
			jPanel11 = new JPanel();
			jPanel11.setLayout(new GridBagLayout());
			jPanel11.add(jLabel, gridBagConstraints5);
			jPanel11.add(getStartYearTextField(), gridBagConstraints2);
			jPanel11.add(jLabel1, gridBagConstraints6);
			jPanel11.add(getStartMonthTextField(), gridBagConstraints3);
			jPanel11.add(jLabel2, gridBagConstraints1);
			jPanel11.add(getStartDayTextField(), gridBagConstraints4);
			jPanel11.add(getEndYearTextField(), gridBagConstraints7);
			jPanel11.add(jLabel3, gridBagConstraints8);
			jPanel11.add(getEndMonthTextField(), gridBagConstraints9);
			jPanel11.add(jLabel11, gridBagConstraints10);
			jPanel11.add(getEndDayTextField(), gridBagConstraints11);
			jPanel11.add(jLabel21, gridBagConstraints12);
		}
		return jPanel11;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setLayout(new BorderLayout());
			jPanel2.setBorder(BorderFactory.createTitledBorder(null, "\ubc95\uc6d0",
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(
							"Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanel2.add(getJPanel5(), BorderLayout.CENTER);
			jPanel2.add(getJPanel6(), BorderLayout.SOUTH);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jPanel3
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jPanel3 = new JPanel();
			jPanel3.setLayout(new BorderLayout());
			jPanel3.setBorder(BorderFactory.createTitledBorder(null, "\ub2f4\ub2f9\uacc4",
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(
							"Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanel3.add(getJPanel7(), BorderLayout.CENTER);
			jPanel3.add(getJPanel8(), BorderLayout.SOUTH);
		}
		return jPanel3;
	}

	/**
	 * This method initializes jPanel4
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel4() {
		if (jPanel4 == null) {
			jPanel4 = new JPanel();
			jPanel4.setLayout(new BorderLayout());
			jPanel4.setBorder(BorderFactory.createTitledBorder(null, "\uc0ac\uac74",
					TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(
							"Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanel4.add(getJPanel9(), BorderLayout.CENTER);
			jPanel4.add(getJPanel10(), BorderLayout.SOUTH);
		}
		return jPanel4;
	}

	/**
	 * This method initializes jPanel5
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.weightx = 1.0;
			jPanel5 = new JPanel();
			jPanel5.setLayout(new GridBagLayout());
			jPanel5.add(getJScrollPane1(), gridBagConstraints);
		}
		return jPanel5;
	}

	/**
	 * This method initializes jPanel6
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel6() {
		if (jPanel6 == null) {
			jPanel6 = new JPanel();
			jPanel6.setLayout(new FlowLayout());
			jPanel6.add(getJPanel11(), null);
			jPanel6.add(getWorkCourtButton(), null);
			jPanel6.add(getWorkAllButton(), null);
		}
		return jPanel6;
	}

	/**
	 * This method initializes jPanel7
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel7() {
		if (jPanel7 == null) {
			jPanel7 = new JPanel();
			jPanel7.setLayout(new BorderLayout());
			jPanel7.add(getJScrollPane2(), BorderLayout.CENTER);
		}
		return jPanel7;
	}

	/**
	 * This method initializes jPanel8
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel8() {
		if (jPanel8 == null) {
			jPanel8 = new JPanel();
			jPanel8.setLayout(new FlowLayout());
			jPanel8.add(getWorkChargeButton(), null);
		}
		return jPanel8;
	}

	/**
	 * This method initializes jPanel9
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel9() {
		if (jPanel9 == null) {
			jPanel9 = new JPanel();
			jPanel9.setLayout(new BorderLayout());
			jPanel9.add(getJScrollPane3(), BorderLayout.CENTER);
		}
		return jPanel9;
	}

	/**
	 * This method initializes jScrollPane
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
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getCourtList());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jScrollPane2
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new JScrollPane();
			jScrollPane2.setViewportView(getChargeList());
		}
		return jScrollPane2;
	}

	/**
	 * This method initializes jScrollPane3
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane3() {
		if (jScrollPane3 == null) {
			jScrollPane3 = new JScrollPane();
			jScrollPane3.setViewportView(getSagunList());
		}
		return jScrollPane3;
	}

	/**
	 * This method initializes jSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPane.setDividerLocation(400);
			jSplitPane.setBottomComponent(getJPanel1());
			jSplitPane.setTopComponent(getJPanel());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(876, 551);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

} // @jve:decl-index=0:visual-constraint="10,10"
