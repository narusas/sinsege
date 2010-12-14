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
 * CustomPropertyChangeListener.java
 * ---------------
 * (C) Copyright 2006, by IDRsolutions and Contributors.
 *
 * 
 * --------------------------
 */
package org.jpedal.objects.acroforms.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jpedal.objects.Javascript;


/**
 * @author chris
 * 
 * used to implement actions from the interactive fields
 */
public class CustomPropertyChangeListener implements PropertyChangeListener {
	
	private String command;
	private String args;
	
	/**
	 * setup the string command, currently javascript validate
	 */
	public CustomPropertyChangeListener(String validateValue) {
		int index = validateValue.indexOf("\\(");
		command = validateValue.substring(1,index);
		args = validateValue.substring(index+2,validateValue.indexOf("\\)"));
	}

	public void propertyChange(PropertyChangeEvent evt) {
		Javascript.execute(command,args);
	}
}
