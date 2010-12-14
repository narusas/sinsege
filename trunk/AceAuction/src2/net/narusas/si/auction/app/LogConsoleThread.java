package net.narusas.si.auction.app;

import java.util.logging.LogRecord;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class LogConsoleThread extends java.util.logging.Handler {
	private static LogConsoleThread instance;
	private JTextArea logTextArea;

	public LogConsoleThread() {
	}

	public JTextArea getLogTextArea() {
		return logTextArea;
	}

	public void setLogTextArea(JTextArea logTextArea) {
		this.logTextArea = logTextArea;
	}

	public static LogConsoleThread getInstance() {
		if (instance == null) {
			instance = new LogConsoleThread();
		}
		return instance;
	}

	@Override
	public void close() throws SecurityException {

	}

	@Override
	public void flush() {

	}

	@Override
	public void publish(final LogRecord record) {
		if (logTextArea == null) {
			return;
		}

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				String temp = logTextArea.getText() == null ? "" : logTextArea.getText();

				if (temp.length() > 100000) {
					temp = temp.substring(50000);
				}
				temp +="\n" + record.getMessage();
				logTextArea.setText(temp );
				if (temp.length() > 0) {
					int pos = temp.lastIndexOf("\n")+1;
					logTextArea.setCaretPosition(pos);
				}

			}
		});
	}
}