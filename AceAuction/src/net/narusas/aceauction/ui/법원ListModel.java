package net.narusas.aceauction.ui;

import javax.swing.AbstractListModel;

import net.narusas.aceauction.model.법원;

public class 법원ListModel extends AbstractListModel {

	public Object getElementAt(int index) {
		return 법원.get(index);
	}

	public int getSize() {
		return 법원.size();
	}

}
