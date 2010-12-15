package net.narusas.aceauction.ui2;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
	static Logger logger = Logger.getLogger("log");
	static {
		try {
			FileHandler h = new FileHandler("update.log", true);
			h.setFormatter(new SimpleFormatter());
			logger.addHandler(h);
			logger.setLevel(Level.FINEST);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		UpdaterUI ui = new UpdaterUI();
		Controller controller = new Controller(ui);
		controller.init();
	}
}
