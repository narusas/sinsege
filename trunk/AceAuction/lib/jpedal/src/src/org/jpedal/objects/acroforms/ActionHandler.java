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
 * ActionHandler.java
 * ---------------
 * (C) Copyright 2006, by IDRsolutions and Contributors.
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

public interface ActionHandler {

    /**when K called, do this*/
    public void K();

    /**
	 * creates and returns a mouseListener to perform the required command
	 * <br>
	 * commands set here are<br>
	 * togglenoview - set component visible on mouse over<br>
	 * ResetForm - reset the forms values to the Default values<br>
	 * Print - print this page<br>
	 * SaveAs<br>
	 * <br>their may be updates and additional actions sent through as per the pdf being read
	 */
	public MouseListener setupCommand(String string,AcroRenderer acrorend);

	/**
	 * creates and returns mouse listener to perform required actions
	 * <br>
	 * entered action map from pdf, contains<br>
	 * command - /Hide (to hide or show the listed fields)<br>
	 * fields - component by name to hide or show<br>
	 * hide - whether to hide or show (NOT hide) the field<br>
	 * <br>their may be updates and additional actions sent through as per the pdf being read
	 */
	public MouseListener setupEnteredAction(Map enteredAction, AcroRenderer acrorenderer);

	/**
	 * creates and returns a mouse listener that will perform the required actions
	 * <br>
	 * exited action map from pdf, contains<br>
	 * command - /Hide (to hide or show the listed fields)<br>
	 * fields - component by name to hide or show<br>
	 * hide - whether to hide or show (NOT hide) the field<br>
	 * <br>their may be updates and additional actions sent through as per the pdf being read
	 */
	public MouseListener setupExitedAction(Map exitedAction, AcroRenderer acrorenderer);

	/**
	 * creates and returns a mouse listener that will perform the required actions
	 * <br>
	 * a hide action map containing a fields array of names and corresponding hide boolean array
	 * <br>the field[index] should be hidden or shown based on hide[index]
	 */
	public MouseListener setupHideAction(Map hideMap, AcroRenderer acrorenderer);

	/**
	 * creates and returns a mouse listener that will perform the required actions
	 * <br>
	 * Popup - with a Component<br>
	 * URL - with a web address<br>
	 * <br>their may be updates and additional actions sent through as per the pdf being read 
	 */
	public MouseListener setupClickedAction(Map activateAction, AcroRenderer acrorenderer);

	/**
	 * creates a returns an action listener that will change the down icon for each click
	 * <br>
	 * 2 icons that need to be changed when the button is sellected and not selected,
	 * so that when the button is pressed the appropriate icon is shown correctly
	 */
	public ActionListener setupChangingDownIcon(BufferedImage downOff, BufferedImage downOn);

	/**
	 * setup action to read required forms data and perform submit action
	 * <br>
	 * SubmitForm action - submitURL is contained in F of dataMap 
	 * 
	 */
	public MouseListener setupSubmitAction(Map dataMap,AcroRenderer acrorend);

	/**
	 * setup mouse actions to allow the text of the button to change with the captions provided
	 * <br>
	 * should change the caption as the moouse actions occure on the field
	 */
	public MouseListener setupChangingCaption(String normalCaption,String rolloverCaption,String downCaption);

	/**
	 * set the combobox to show its options on entry to field
	 * <br>
	 * used to make comboboxes show their contents when the mouse scrolls over the field, 
	 * and hides it after the mouse leaves the field area
	 */
	public MouseListener setComboClickOnEntry();


	/**
	 * setup value change validate option
	 * <br>
	 * a validate command to be executed on the associated field,<br>
	 * primariy in javascript
	 */
	public PropertyChangeListener setupValidateAction(String validateValue);
}
