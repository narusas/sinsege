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
 * DefaultActionHandler.java
 * ---------------
 * (C) Copyright 2005, by IDRsolutions and Contributors.
 *
 * 
 * --------------------------
 */
package org.jpedal.objects.acroforms;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.util.Map;

import org.jpedal.objects.acroforms.listeners.CustomActionListener;
import org.jpedal.objects.acroforms.listeners.CustomMouseListener;
import org.jpedal.objects.acroforms.listeners.CustomPropertyChangeListener;


public class DefaultActionHandler implements ActionHandler{
	
	/** 
	 * creates and returns a mouseListener to perform the required command
	 */
	public MouseListener setupCommand(String command,AcroRenderer acrorend) {
		return new CustomMouseListener(command,acrorend);
	}

	/**
	 * creates and returns mouse listener to perform required actions
	 */
	public MouseListener setupEnteredAction(Map enteredAction, AcroRenderer acrorenderer) {
		return new CustomMouseListener(enteredAction,"entered",acrorenderer);
	}

	/**
	 * creates and returns a mouse listener that will perform the required actions
	 */
	public MouseListener setupExitedAction(Map exitedAction, AcroRenderer acrorenderer) {
		return new CustomMouseListener(exitedAction,"exited",acrorenderer);
	}

	/**
	 * creates and returns a mouse listener that will perform the required actions
	 */
	public MouseListener setupHideAction(Map hideAction, AcroRenderer acrorenderer) {
		return new CustomMouseListener(hideAction,"Hide",acrorenderer);
	}

	/**
	 * creates and returns a mouse listener that will perform the required actions
	 */
	public MouseListener setupClickedAction(Map activateAction, AcroRenderer acrorenderer) {
		return new CustomMouseListener(activateAction,"Clicked",acrorenderer);
	}

	/**
	 * creates a returns an action listener that will change the down icon for each click
	 */
	public ActionListener setupChangingDownIcon(BufferedImage downOff, BufferedImage downOn) {
		return new CustomActionListener(downOff,downOn);
	}
	
	/**
	 * setup action to read required forms data and perform submit action
	 */
	public MouseListener setupSubmitAction(Map aDataMap,AcroRenderer acrorend){
    	return new CustomMouseListener(aDataMap,acrorend);
	}

	/**
	 * sets up the captions to change as needed
	 */
	public MouseListener setupChangingCaption(String normalCaption,String rolloverCaption,String downCaption) {
		return new CustomMouseListener(normalCaption,rolloverCaption,downCaption);
	}

	/**
	 * set the combobox to show its options on entry to field
	 */
	public MouseListener setComboClickOnEntry() {
		return new CustomMouseListener("comboEntry",null);
	}


	/**
	 * setup value change validate option
	 */
	public PropertyChangeListener setupValidateAction(String validateValue) {
		return new CustomPropertyChangeListener(validateValue);
	}

	public void K() {
		// TODO Auto-generated method stub
		
	}
}

