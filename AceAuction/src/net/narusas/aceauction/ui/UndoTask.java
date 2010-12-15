package net.narusas.aceauction.ui;

import net.narusas.aceauction.data.updater.Undo;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;

public class UndoTask implements Runnable {

	private final ConsoleFrame f;
	private final ����ListModel listModeInstance;

	public UndoTask(ConsoleFrame f, ����ListModel listModeInstance) {
		this.f = f;
		this.listModeInstance = listModeInstance;
	}

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
