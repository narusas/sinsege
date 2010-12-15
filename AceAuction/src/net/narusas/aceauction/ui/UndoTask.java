package net.narusas.aceauction.ui;

import net.narusas.aceauction.data.updater.Undo;
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.법원;

public class UndoTask implements Runnable {

	private final ConsoleFrame f;
	private final 담당계ListModel listModeInstance;

	public UndoTask(ConsoleFrame f, 담당계ListModel listModeInstance) {
		this.f = f;
		this.listModeInstance = listModeInstance;
	}

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
