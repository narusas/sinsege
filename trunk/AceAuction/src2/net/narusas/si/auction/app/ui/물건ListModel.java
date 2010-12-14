package net.narusas.si.auction.app.ui;

import javax.swing.DefaultListModel;

import net.narusas.si.auction.model.물건;

public class 물건ListModel extends DefaultListModel {

	public 물건 get물건(int index) {
		return (물건) getElementAt(index);
	}
}
