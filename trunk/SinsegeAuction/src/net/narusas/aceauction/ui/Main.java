/*
 * 
 */
package net.narusas.aceauction.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class Main {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");
	static {
		try {
			FileHandler h = new FileHandler("build.log", true);
			h.setFormatter(new SimpleFormatter());
			logger.addHandler(h);
			logger.setLevel(Level.FINEST);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		Controller controller = new Controller();
		ConsoleFrame f = new ConsoleFrame();

		controller.setFrame(f);
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		f.setVisible(true);
	}
}
