/*
 * 
 */
package net.narusas.aceauction.ui;

import net.narusas.aceauction.data.updater.Undo;
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.법원;

// TODO: Auto-generated Javadoc
/**
 * The Class UndoTask.
 */
public class UndoTask implements Runnable {

	/** The f. */
	private final ConsoleFrame f;
	
	/** The list mode instance. */
	private final 담당계ListModel listModeInstance;

	/**
	 * Instantiates a new undo task.
	 * 
	 * @param f the f
	 * @param listModeInstance the list mode instance
	 */
	public UndoTask(ConsoleFrame f, 담당계ListModel listModeInstance) {
		this.f = f;
		this.listModeInstance = listModeInstance;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		int selectedCourt = f.get법원List().getSelectedIndex();
		if (selectedCourt == -1 || selectedCourt >= 법원.size()) {
			return;
		}
		법원 court = 법원.get(selectedCourt);
		int selectedCharge = f.get담당계List().getSelectedIndex();
		담당계 charge = listModeInstance.get담당계(selectedCharge);
		if (charge == null) {
			return;
		}
		Undo undo = new Undo(court, charge);
		try {
			undo.start();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

}
