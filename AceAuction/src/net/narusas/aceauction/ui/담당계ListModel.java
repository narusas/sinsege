package net.narusas.aceauction.ui;

import javax.swing.DefaultListModel;

import net.narusas.aceauction.model.담당계;

public class 담당계ListModel extends DefaultListModel {
	public 담당계 get담당계(int index) {
		return (담당계) getElementAt(index);
	}
}
