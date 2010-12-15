package net.narusas.aceauction.ui;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.narusas.aceauction.interaction.Alert;

public class SwingAlert extends Alert {
	private final JFrame parent;

	SwingAlert(JFrame parent) {
		this.parent = parent;

	}

	@Override
	public void alert(String msg) {
		JOptionPane.showMessageDialog(parent, msg);
	}

	@Override
	public void alert(String msg, Exception ex) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		// if (Progress.getInstance().isRunning()) {
		// Progress.getInstance().progress(
		// msg + "\n" + sw.getBuffer().toString());
		// } else {
		JOptionPane.showMessageDialog(parent, msg + "\n" + sw.getBuffer().toString());
		// }
	}
}
