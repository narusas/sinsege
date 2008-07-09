/*
 * 
 */
package net.narusas.aceauction.interaction;

// TODO: Auto-generated Javadoc
/**
 * The Class ProgressBar.
 */
public abstract class ProgressBar {
	
	/**
	 * The Class DefualtProgress.
	 */
	public static class DefualtProgress extends ProgressBar {
		
		/** The progress. */
		int progress = 0;

		/* (non-Javadoc)
		 * @see net.narusas.aceauction.interaction.ProgressBar#addMax(int)
		 */
		@Override
		public void addMax(int i) {
			max += i;
		}

		/* (non-Javadoc)
		 * @see net.narusas.aceauction.interaction.ProgressBar#canceled(java.lang.String)
		 */
		@Override
		public void canceled(String msg) {
			System.err.println("Progress Canceled:" + msg);
		}

		/* (non-Javadoc)
		 * @see net.narusas.aceauction.interaction.ProgressBar#complete()
		 */
		@Override
		public void complete() {
			System.out.println("모든 과정을 완료했습니다.");
		}

		/* (non-Javadoc)
		 * @see net.narusas.aceauction.interaction.ProgressBar#getMaxProgress()
		 */
		@Override
		public int getMaxProgress() {
			return max;
		}

		/* (non-Javadoc)
		 * @see net.narusas.aceauction.interaction.ProgressBar#isRunning()
		 */
		@Override
		public boolean isRunning() {
			return false;
		}

		/* (non-Javadoc)
		 * @see net.narusas.aceauction.interaction.ProgressBar#progress(int, java.lang.String)
		 */
		@Override
		public void progress(int p, String msg) {
			progress = p;
			System.err.println("Progressing " + p + ": " + msg);
		}

		/* (non-Javadoc)
		 * @see net.narusas.aceauction.interaction.ProgressBar#progress(java.lang.String)
		 */
		@Override
		public void progress(String msg) {
			progress(progress + 1, msg);
		}

		/* (non-Javadoc)
		 * @see net.narusas.aceauction.interaction.ProgressBar#setMaxProgress(int)
		 */
		@Override
		public void setMaxProgress(int max) {
			this.max = max;
		}

		/* (non-Javadoc)
		 * @see net.narusas.aceauction.interaction.ProgressBar#start(int)
		 */
		@Override
		public void start(int max) {
			this.max = max;
		}
	}

	/** The instance. */
	private static ProgressBar instance = new DefualtProgress();

	/** The max. */
	protected int max;

	/**
	 * Adds the max.
	 * 
	 * @param i the i
	 */
	public void addMax(int i) {
		max += i;
	}

	/**
	 * Canceled.
	 * 
	 * @param msg the msg
	 */
	public abstract void canceled(String msg);

	/**
	 * Complete.
	 */
	public abstract void complete();

	/**
	 * Gets the max progress.
	 * 
	 * @return the max progress
	 */
	public int getMaxProgress() {
		return max;
	}

	/**
	 * Checks if is running.
	 * 
	 * @return true, if is running
	 */
	public abstract boolean isRunning();

	/**
	 * Progress.
	 * 
	 * @param p the p
	 * @param msg the msg
	 */
	public abstract void progress(int p, String msg);

	/**
	 * Progress.
	 * 
	 * @param msg the msg
	 */
	public abstract void progress(String msg);

	/**
	 * Sets the max progress.
	 * 
	 * @param max the new max progress
	 */
	public void setMaxProgress(int max) {
		this.max = max;
	}

	/**
	 * Start.
	 * 
	 * @param max the max
	 */
	public abstract void start(int max);

	/**
	 * Gets the single instance of ProgressBar.
	 * 
	 * @return single instance of ProgressBar
	 */
	public static ProgressBar getInstance() {
		return instance;
	}

	/**
	 * Sets the progress.
	 * 
	 * @param progres the new progress
	 */
	public static void setProgress(ProgressBar progres) {
		ProgressBar.instance = progres;
	}
}
