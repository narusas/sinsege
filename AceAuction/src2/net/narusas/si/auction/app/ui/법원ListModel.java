package net.narusas.si.auction.app.ui;

import java.util.List;

import javax.swing.AbstractListModel;

import net.narusas.si.auction.model.법원;

public class 법원ListModel extends AbstractListModel {
	private final List<법원> courts;

	public 법원ListModel(List<법원> courts) {
		this.courts = courts;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	public Object getElementAt(int index) {
		return courts.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#getSize()
	 */
	public int getSize() {
		return courts.size();
	}

}