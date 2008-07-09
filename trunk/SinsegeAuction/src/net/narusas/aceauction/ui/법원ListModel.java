/*
 * 
 */
package net.narusas.aceauction.ui;

import javax.swing.AbstractListModel;

import net.narusas.aceauction.model.过盔;

// TODO: Auto-generated Javadoc
/**
 * The Class 过盔ListModel.
 */
public class 过盔ListModel extends AbstractListModel {

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	public Object getElementAt(int index) {
		return 过盔.get(index);
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getSize()
	 */
	public int getSize() {
		return 过盔.size();
	}

}
