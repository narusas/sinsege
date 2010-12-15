package net.narusas.aceauction.ui;

import javax.swing.AbstractListModel;

import net.narusas.aceauction.model.过盔;

public class 过盔ListModel extends AbstractListModel {

	public Object getElementAt(int index) {
		return 过盔.get(index);
	}

	public int getSize() {
		return 过盔.size();
	}

}
