package net.narusas.aceauction.ui;

import javax.swing.AbstractListModel;

import net.narusas.aceauction.model.����;

public class ����ListModel extends AbstractListModel {

	public Object getElementAt(int index) {
		return ����.get(index);
	}

	public int getSize() {
		return ����.size();
	}

}
