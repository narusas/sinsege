package net.narusas.aceauction.ui;

import java.util.Vector;

import javax.swing.DefaultListModel;

public class 물건정보ListModel extends DefaultListModel {

	public void update(Vector<String> cmds) {
		clear();
		for (String string : cmds) {
			addElement(string);
		}
	}
}
