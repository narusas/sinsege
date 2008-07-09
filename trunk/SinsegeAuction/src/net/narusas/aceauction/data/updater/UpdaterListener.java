/*
 * 
 */
package net.narusas.aceauction.data.updater;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving updater events.
 * The class that is interested in processing a updater
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addUpdaterListener<code> method. When
 * the updater event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see UpdaterEvent
 */
public interface UpdaterListener {

	/**
	 * Log.
	 * 
	 * @param msg the msg
	 */
	void log(String msg);

	/**
	 * Progress.
	 * 
	 * @param progress the progress
	 */
	void progress(int progress);

	/**
	 * Update work size.
	 * 
	 * @param size the size
	 */
	void updateWorkSize(int size);

}
