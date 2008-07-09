/*
 * 
 */
package net.narusas.aceauction.ui;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.narusas.aceauction.interaction.Alert;

// TODO: Auto-generated Javadoc
/**
 * The Class SwingAlert.
 */
public class SwingAlert extends Alert {
	
	/** The parent. */
	private final JFrame parent;

	/**
	 * Instantiates a new swing alert.
	 * 
	 * @param parent the parent
	 */
	SwingAlert(JFrame parent) {
		this.parent = parent;

	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.interaction.Alert#alert(java.lang.String)
	 */
	@Override
	public void alert(String msg) {
		JOptionPane.showMessageDialog(parent, msg);
	}

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.interaction.Alert#alert(java.lang.String, java.lang.Exception)
	 */
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
