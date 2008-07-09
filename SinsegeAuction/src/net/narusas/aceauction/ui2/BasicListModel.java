/*
 * 
 */
package net.narusas.aceauction.ui2;

import javax.swing.DefaultListModel;

// TODO: Auto-generated Javadoc
/**
 * The Class BasicListModel.
 */
public class BasicListModel extends DefaultListModel {

	/**
	 * Update.
	 */
	public void update() {
		fireContentsChanged(this, 0, size() - 1);
	}
}
