/**
 * ===========================================
 * Java Pdf Extraction Decoding Access Library
 * ===========================================
 *
 * Project Info:  http://www.jpedal.org
 * Project Lead:  Mark Stephens (mark@idrsolutions.com)
 *
 * (C) Copyright 2005, IDRsolutions and Contributors.
 * 
 * 	This file is part of JPedal
 *
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General Public License for more details.

    You should have received a copy of the GNU General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


 *
 * ---------------
 * GUICombo.java
 * ---------------
 * (C) Copyright 2006, by IDRsolutions and Contributors.
 *

 * Original Author:  Mark Stephens (mark@idrsolutions.com)
 * Contributor(s):
 *
 */
package org.jpedal.examples.simpleviewer.gui.generic;

import java.awt.Color;
import java.awt.Dimension;

/**abstract version of ComboBox*/
public interface GUICombo {

	public void setSelectedIndex(int defaultSelection);

	public void setBackground(Color white);

	public void setEditable(boolean b);

	public void setPreferredSize(Dimension dimension);

	public void setID(int id);

	public void setToolTipText(String tooltip);

	public void setEnabled(boolean value);

	public int getSelectedIndex();

	public void setSelectedItem(Object index);

	public Object getSelectedItem();
	
	public void setVisible(boolean visible);

}
