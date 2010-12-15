package net.narusas.aceauction.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import net.narusas.aceauction.data.builder.BuildProgressListener;

public class SwingBuildProgressListener extends JDialog implements BuildProgressListener {
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger("log");

	private JLabel chargeLabel = null;

	private int chargeProgress;

	private int chargeSize;

	private JPanel jContentPane = null;

	private JLabel jLabel = null;

	private JLabel jLabel2 = null;

	private JLabel jLabel4 = null;

	private JPanel jPanel = null;

	private JScrollPane jScrollPane = null;

	private JTextArea logTextArea = null;

	private JLabel mulgunLabel = null;

	private int mulgunProgress;

	private int mulgunSize;

	private JProgressBar progressBar = null;

	private JPanel progressPanel = null;

	private JLabel sagunLabel = null;

	private int sagunProgress;

	private int sagunSize;

	/**
	 * @param owner
	 */
	public SwingBuildProgressListener(Frame owner) {
		super(owner);
		initialize();
		logger.addHandler(new Handler() {

			@Override
			public void close() throws SecurityException {

			}

			@Override
			public void flush() {

			}

			@Override
			public void publish(LogRecord record) {
				if (record.getLevel().intValue() >= Level.FINE.intValue()) {
					log(record.getMessage());
				}
			}
		});
	}

	public void log(String message) {
		String temp = getLogTextArea().getText() + "\n" + message;
		if (temp.length() > 10000) {
			temp = temp.substring(0, 5000);
		}
		getLogTextArea().setText(temp);

		if (getLogTextArea().getText().length() > 1) {
			getLogTextArea().setCaretPosition(getLogTextArea().getText().length() - 1);
		}

		// logger.log(Level.INFO, message);
	}

	public void progress(int level) {
		logger.log(Level.FINE, "Progress:" + level);
		switch (level) {
		case LEVEL_담당계:
			chargeProgress++;
			break;
		case LEVEL_사건:
			sagunProgress++;
			break;
		case LEVEL_물건:
			mulgunProgress++;
			break;
		}
		update();
	}

	public void update담당계Size(int size) {
		logger.log(Level.FINE, "담당계 Size를 갱신:" + size);

		this.chargeSize = size;
		this.chargeProgress = 0;
		this.mulgunSize = 0;
		this.mulgunProgress = 0;
		this.sagunSize = 0;
		this.sagunProgress = 0;
		update();
	}

	public void update물건Size(int mulgunSize) {
		logger.log(Level.FINE, "물건 Size를 갱신:" + mulgunSize);
		this.mulgunSize = mulgunSize;
		mulgunProgress = 0;
		update();
	}

	public void update사건Size(int sagunSize) {
		logger.log(Level.FINE, "사건 Size를 갱신:" + sagunSize);

		this.sagunSize = sagunSize;
		sagunProgress = 0;
		this.mulgunSize = 0;
		mulgunProgress = 0;
		update();
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
			jContentPane.add(getProgressPanel(), BorderLayout.NORTH);
			jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
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
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.insets = new Insets(5, 5, 5, 5);
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 5;
			gridBagConstraints4.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints4.gridy = 0;
			mulgunLabel = new JLabel();
			mulgunLabel.setText("0/0");
			mulgunLabel.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 4;
			gridBagConstraints3.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints3.gridy = 0;
			jLabel4 = new JLabel();
			jLabel4.setText("물건");
			jLabel4.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 3;
			gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints2.gridy = 0;
			sagunLabel = new JLabel();
			sagunLabel.setText("0/0");
			sagunLabel.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 2;
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints1.gridy = 0;
			jLabel2 = new JLabel();
			jLabel2.setText("사건");
			jLabel2.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.gridy = 0;
			chargeLabel = new JLabel();
			chargeLabel.setText("0/0");
			chargeLabel.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
			jLabel = new JLabel();
			jLabel.setText("담당계");
			jLabel.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(jLabel, gridBagConstraints5);
			jPanel.add(chargeLabel, gridBagConstraints);
			jPanel.add(jLabel2, gridBagConstraints1);
			jPanel.add(sagunLabel, gridBagConstraints2);
			jPanel.add(jLabel4, gridBagConstraints3);
			jPanel.add(mulgunLabel, gridBagConstraints4);
		}
		return jPanel;
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
	 * This method initializes logTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getLogTextArea() {
		if (logTextArea == null) {
			logTextArea = new JTextArea();
		}
		return logTextArea;
	}

	/**
	 * This method initializes progressBar
	 * 
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getProgressBar() {
		if (progressBar == null) {
			progressBar = new JProgressBar();
			progressBar.setValue(0);
		}
		return progressBar;
	}

	/**
	 * This method initializes progressPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getProgressPanel() {
		if (progressPanel == null) {
			progressPanel = new JPanel();
			progressPanel.setLayout(new BorderLayout());
			progressPanel.add(getJPanel(), BorderLayout.NORTH);
			progressPanel.add(getProgressBar(), BorderLayout.CENTER);
		}
		return progressPanel;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(776, 499);
		this.setTitle("Build Progress Dialog");
		this.setContentPane(getJContentPane());
	}

	private void update() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chargeLabel.setText("" + chargeProgress + "/" + chargeSize);
				sagunLabel.setText("" + sagunProgress + "/" + sagunSize);
				mulgunLabel.setText("" + mulgunProgress + "/" + mulgunSize);

				if (chargeProgress == 0 && chargeSize == 0) {
					getProgressBar().setValue(0);
				} else {
					getProgressBar().setValue(100 * chargeProgress / chargeSize);
				}
			}
		});
	}

} // @jve:decl-index=0:visual-constraint="10,10"
