package net.narusas.aceauction.ui;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import net.narusas.aceauction.interaction.ProgressBar;

public class SwingProgress extends ProgressBar {
	static Logger logger = Logger.getLogger("log");
	private ProgressDialog dialog;

	private final JFrame parent;

	DefualtProgress dp = new DefualtProgress();

	boolean isRunning = false;

	int p;

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

	@Override
	public void canceled(String msg) {
		close();
	}

	@Override
	public void complete() {
		close();
	}

	@Override
	public int getMaxProgress() {
		return max;
	}

	public void increseProgress(final int p) {
		if (isWorkable()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					dialog.getJProgressBar().setValue(p);
				}
			});
		}

	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}

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

	@Override
	public void progress(final int p, final String msg) {
		this.p = p;
		if (isWorkable()) {
			increseProgress(p);
			log(msg);
		}
	}

	@Override
	public void progress(String msg) {
		progress(p + 1, msg);

	}

	@Override
	public void setMaxProgress(int max) {
		this.max = max;
	}

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

	private boolean isWorkable() {
		return dialog != null && dialog.isVisible();
	}

}
