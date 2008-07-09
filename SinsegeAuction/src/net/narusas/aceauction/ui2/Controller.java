/*
 * 
 */
package net.narusas.aceauction.ui2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.narusas.aceauction.data.updater.Updater;
import net.narusas.aceauction.data.updater.UpdaterListener;

// TODO: Auto-generated Javadoc
/**
 * The Class Controller.
 */
public class Controller implements UpdaterListener {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The ui. */
	private final UpdaterUI ui;

	/**
	 * Instantiates a new controller.
	 * 
	 * @param ui the ui
	 */
	public Controller(UpdaterUI ui) {
		this.ui = ui;
	}

	/**
	 * Inits the.
	 */
	public void init() {
		logger.log(Level.INFO, "Updater를 초기화합니다.");
		Date d = new Date(System.currentTimeMillis());
		ui.getYearTextField().setText("" + (d.getYear() + 1900));
		ui.getMonthTextField().setText("" + (d.getMonth() + 1));
		ui.getDayTextField().setText("" + d.getDate());
		final UpdaterListener listener = this;
		ui.getUpdateButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ui.getUpdateButton().setEnabled(false);
				ui.getLogTextArea().setText("");
				new Thread() {
					@Override
					public void run() {
						Updater up = new Updater(getTimeStamp(), listener);
						try {
							up.update();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						ui.getUpdateButton().setEnabled(true);
					}
				}.start();

			}
		});

		ui.addWindowListener(new WindowListener() {

			public void windowActivated(WindowEvent e) {
			}

			public void windowClosed(WindowEvent e) {

			}

			public void windowClosing(WindowEvent e) {
				System.exit(-1);
			}

			public void windowDeactivated(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowOpened(WindowEvent e) {
			}
		});
		ui.setVisible(true);

	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.data.updater.UpdaterListener#log(java.lang.String)
	 */
	public void log(String msg) {
		logger.log(Level.INFO, msg);

		ui.getLogTextArea().setText(ui.getLogTextArea().getText() + "\n" + msg);
		ui.getLogTextArea().setCaretPosition(ui.getLogTextArea().getText().length() - 1);
	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.data.updater.UpdaterListener#progress(int)
	 */
	public void progress(int progress) {
	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.data.updater.UpdaterListener#updateWorkSize(int)
	 */
	public void updateWorkSize(int size) {
	}

	/**
	 * Gets the time stamp.
	 * 
	 * @return the time stamp
	 */
	private Date getTimeStamp() {
		int year = Integer.parseInt(ui.getYearTextField().getText());
		int month = Integer.parseInt(ui.getMonthTextField().getText());
		int day = Integer.parseInt(ui.getDayTextField().getText());
		return new Date(year - 1900, month - 1, day);
	}
}
