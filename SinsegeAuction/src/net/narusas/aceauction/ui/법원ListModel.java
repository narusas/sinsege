/*
 * 
 */
package net.narusas.aceauction.ui;

import javax.swing.AbstractListModel;

import net.narusas.aceauction.model.����;

// TODO: Auto-generated Javadoc
/**
 * The Class ����ListModel.
 */
public class ����ListModel extends AbstractListModel {

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	public Object getElementAt(int index) {
		return ����.get(index);
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getSize()
	 */
	public int getSize() {
		return ����.size();
	}

}
