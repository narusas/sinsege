package net.narusas.aceauction.ui2;

import javax.swing.DefaultListModel;

public class BasicListModel extends DefaultListModel {

	public void update() {
		fireContentsChanged(this, 0, size() - 1);
	}
}
