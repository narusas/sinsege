/*
 * 
 */
package net.narusas.aceauction.ui;

import java.util.Vector;

import javax.swing.DefaultListModel;

// TODO: Auto-generated Javadoc
/**
 * The Class 물건정보ListModel.
 */
public class 물건정보ListModel extends DefaultListModel {

	/**
	 * Update.
	 * 
	 * @param cmds the cmds
	 */
	public void update(Vector<String> cmds) {
		clear();
		for (String string : cmds) {
			addElement(string);
		}
	}
}
