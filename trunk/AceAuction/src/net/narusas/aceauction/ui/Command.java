package net.narusas.aceauction.ui;

import net.narusas.aceauction.interaction.Alert;
import net.narusas.aceauction.interaction.ProgressBar;

public class Command extends Thread {
	private final int progress;

	private final Runnable task;

	public Command(Runnable task, int progress) {
		this.task = task;
		this.progress = progress;
	}

	public void execute() {
		try {
			task.run();
		} catch (Exception ex) {
			ex.printStackTrace();
			ProgressBar.getInstance().canceled(ex.getMessage());

			Alert.getInstance().alert("Command execution failed", ex);
			Controller.updating = false;
		}
	}

	@Override
	public void run() {
		// if (Controller.updating) {
		// return;
		// }
		Controller.updating = true;
		ProgressBar.getInstance().start(progress);
		execute();
		Controller.updating = false;
		ProgressBar.getInstance().complete();
	}
}
