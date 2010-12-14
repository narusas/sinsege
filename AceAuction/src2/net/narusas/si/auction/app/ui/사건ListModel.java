package net.narusas.si.auction.app.ui;

import javax.swing.DefaultListModel;

import net.narusas.si.auction.model.사건;

public class 사건ListModel extends DefaultListModel {

	/**
	 * Gets the 담당계.
	 * 
	 * @param index
	 *            the index
	 * 
	 * @return the 담당계
	 */
	public 사건 get사건(int index) {
		return (사건) getElementAt(index);
	}
}