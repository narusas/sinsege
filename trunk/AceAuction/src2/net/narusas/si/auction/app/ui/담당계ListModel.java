package net.narusas.si.auction.app.ui;

import javax.swing.DefaultListModel;

import net.narusas.si.auction.model.담당계;

public class 담당계ListModel extends DefaultListModel {

	/**
	 * Gets the 담당계.
	 * 
	 * @param index
	 *            the index
	 * 
	 * @return the 담당계
	 */
	public 담당계 get담당계(int index) {
		return (담당계) getElementAt(index);
	}
}