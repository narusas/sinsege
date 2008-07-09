/*
 * 
 */
package net.narusas.aceauction.ui;

import net.narusas.aceauction.data.updater.Undo;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;

// TODO: Auto-generated Javadoc
/**
 * The Class UndoTask.
 */
public class UndoTask implements Runnable {

	/** The f. */
	private final ConsoleFrame f;
	
	/** The list mode instance. */
	private final ����ListModel listModeInstance;

	/**
	 * Instantiates a new undo task.
	 * 
	 * @param f the f
	 * @param listModeInstance the list mode instance
	 */
	public UndoTask(ConsoleFrame f, ����ListModel listModeInstance) {
		this.f = f;
		this.listModeInstance = listModeInstance;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		int selectedCourt = f.get����List().getSelectedIndex();
		if (selectedCourt == -1 || selectedCourt >= ����.size()) {
			return;
		}
		���� court = ����.get(selectedCourt);
		int selectedCharge = f.get����List().getSelectedIndex();
		���� charge = listModeInstance.get����(selectedCharge);
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
