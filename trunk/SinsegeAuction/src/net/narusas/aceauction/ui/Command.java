/*
 * 
 */
package net.narusas.aceauction.ui;

import net.narusas.aceauction.interaction.Alert;
import net.narusas.aceauction.interaction.ProgressBar;

// TODO: Auto-generated Javadoc
/**
 * The Class Command.
 */
public class Command extends Thread {
	
	/** The progress. */
	private final int progress;

	/** The task. */
	private final Runnable task;

	/**
	 * Instantiates a new command.
	 * 
	 * @param task the task
	 * @param progress the progress
	 */
	public Command(Runnable task, int progress) {
		this.task = task;
		this.progress = progress;
	}

	/**
	 * Execute.
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
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
