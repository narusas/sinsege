/*
 * 
 */
package net.narusas.aceauction.ui;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import net.narusas.aceauction.interaction.ProgressBar;

// TODO: Auto-generated Javadoc
/**
 * The Class SwingProgress.
 */
public class SwingProgress extends ProgressBar {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");
	
	/** The dialog. */
	private ProgressDialog dialog;

	/** The parent. */
	private final JFrame parent;

	/** The dp. */
	DefualtProgress dp = new DefualtProgress();

	/** The is running. */
	boolean isRunning = false;

	/** The p. */
	int p;

	/**
	 * Instantiates a new swing progress.
	 * 
	 * @param parent the parent
	 */
	SwingProgress(JFrame parent) {
		this.parent = parent;
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

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.interaction.ProgressBar#canceled(java.lang.String)
	 */
	@Override
	public void canceled(String msg) {
		close();
	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.interaction.ProgressBar#complete()
	 */
	@Override
	public void complete() {
		close();
	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.interaction.ProgressBar#getMaxProgress()
	 */
	@Override
	public int getMaxProgress() {
		return max;
	}

	/**
	 * Increse progress.
	 * 
	 * @param p the p
	 */
	public void increseProgress(final int p) {
		if (isWorkable()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					dialog.getJProgressBar().setValue(p);
				}
			});
		}

	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.interaction.ProgressBar#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Log.
	 * 
	 * @param msg the msg
	 */
	public void log(final String msg) {
		if (isWorkable()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JTextArea textArea = dialog.getJTextArea();
					textArea.setText(textArea.getText() + "\n" + msg);
					textArea.setCaretPosition(textArea.getText().length());
				}
			});
		}
	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.interaction.ProgressBar#progress(int, java.lang.String)
	 */
	@Override
	public void progress(final int p, final String msg) {
		this.p = p;
		if (isWorkable()) {
			increseProgress(p);
			log(msg);
		}
	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.interaction.ProgressBar#progress(java.lang.String)
	 */
	@Override
	public void progress(String msg) {
		progress(p + 1, msg);

	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.interaction.ProgressBar#setMaxProgress(int)
	 */
	@Override
	public void setMaxProgress(int max) {
		this.max = max;
	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.interaction.ProgressBar#start(int)
	 */
	@Override
	public void start(int max) {
		this.p = 0;
		this.max = max;
		isRunning = true;
		if (isWorkable()) {
			return;
		}
		dialog = new ProgressDialog(parent);
		dialog.setResizable(true);
		dialog.setLocationRelativeTo(parent);
		dialog.getJProgressBar().setMaximum(max);
		dialog.setModal(true);
		new Thread() {
			@Override
			public void run() {
				dialog.setVisible(true);
			}
		}.start();

	}

	/**
	 * Close.
	 */
	private void close() {
		isRunning = false;
		if (dialog != null) {
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					dialog.setVisible(false);
					dialog.dispose();
					dialog = null;
				}
			});

		}
	}

	/**
	 * Checks if is workable.
	 * 
	 * @return true, if is workable
	 */
	private boolean isWorkable() {
		return dialog != null && dialog.isVisible();
	}

}
