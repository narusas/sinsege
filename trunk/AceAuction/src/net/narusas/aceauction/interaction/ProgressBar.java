package net.narusas.aceauction.interaction;

public abstract class ProgressBar {
	public static class DefualtProgress extends ProgressBar {
		int progress = 0;

		@Override
		public void addMax(int i) {
			max += i;
		}

		@Override
		public void canceled(String msg) {
			System.err.println("Progress Canceled:" + msg);
		}

		@Override
		public void complete() {
			System.out.println("모든 과정을 완료했습니다.");
		}

		@Override
		public int getMaxProgress() {
			return max;
		}

		@Override
		public boolean isRunning() {
			return false;
		}

		@Override
		public void progress(int p, String msg) {
			progress = p;
			System.err.println("Progressing " + p + ": " + msg);
		}

		@Override
		public void progress(String msg) {
			progress(progress + 1, msg);
		}

		@Override
		public void setMaxProgress(int max) {
			this.max = max;
		}

		@Override
		public void start(int max) {
			this.max = max;
		}
	}

	private static ProgressBar instance = new DefualtProgress();

	protected int max;

	public void addMax(int i) {
		max += i;
	}

	public abstract void canceled(String msg);

	public abstract void complete();

	public int getMaxProgress() {
		return max;
	}

	public abstract boolean isRunning();

	public abstract void progress(int p, String msg);

	public abstract void progress(String msg);

	public void setMaxProgress(int max) {
		this.max = max;
	}

	public abstract void start(int max);

	public static ProgressBar getInstance() {
		return instance;
	}

	public static void setProgress(ProgressBar progres) {
		ProgressBar.instance = progres;
	}
}
