/*
 * 
 */
package net.narusas.aceauction.data;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving fileUpload events.
 * The class that is interested in processing a fileUpload
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addFileUploadListener<code> method. When
 * the fileUpload event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see FileUploadEvent
 */
public interface FileUploadListener {
	
	/**
	 * Update.
	 * 
	 * @param count the count
	 * @param remains the remains
	 * @param currentWork the current work
	 */
	void update(int count, int remains, String currentWork);
}
