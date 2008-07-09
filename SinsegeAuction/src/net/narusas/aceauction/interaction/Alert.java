/*
 * 
 */
package net.narusas.aceauction.interaction;

// TODO: Auto-generated Javadoc
/**
 * The Class Alert.
 */
public abstract class Alert {
	
	/**
	 * The Class DefaultAlert.
	 */
	static class DefaultAlert extends Alert {
		
		/* (non-Javadoc)
		 * @see net.narusas.aceauction.interaction.Alert#alert(java.lang.String)
		 */
		@Override
		public void alert(String msg) {
			System.err.println(msg);
		}

		/* (non-Javadoc)
		 * @see net.narusas.aceauction.interaction.Alert#alert(java.lang.String, java.lang.Exception)
		 */
		@Override
		public void alert(String msg, Exception ex) {
			System.err.println(msg);
			ex.printStackTrace(System.err);
		}
	}

	/** The alert. */
	private static Alert alert = new DefaultAlert();

	/**
	 * Alert.
	 * 
	 * @param msg the msg
	 */
	public abstract void alert(String msg);

	/**
	 * Alert.
	 * 
	 * @param msg the msg
	 * @param ex the ex
	 */
	public abstract void alert(String msg, Exception ex);

	/**
	 * Gets the single instance of Alert.
	 * 
	 * @return single instance of Alert
	 */
	public static Alert getInstance() {
		return alert;
	}

	/**
	 * Sets the alert.
	 * 
	 * @param alert the new alert
	 */
	public static void setAlert(Alert alert) {
		Alert.alert = alert;
	}
}
