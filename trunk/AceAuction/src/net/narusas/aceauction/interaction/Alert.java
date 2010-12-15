package net.narusas.aceauction.interaction;

public abstract class Alert {
	static class DefaultAlert extends Alert {
		@Override
		public void alert(String msg) {
			System.err.println(msg);
		}

		@Override
		public void alert(String msg, Exception ex) {
			System.err.println(msg);
			ex.printStackTrace(System.err);
		}
	}

	private static Alert alert = new DefaultAlert();

	public abstract void alert(String msg);

	public abstract void alert(String msg, Exception ex);

	public static Alert getInstance() {
		return alert;
	}

	public static void setAlert(Alert alert) {
		Alert.alert = alert;
	}
}
