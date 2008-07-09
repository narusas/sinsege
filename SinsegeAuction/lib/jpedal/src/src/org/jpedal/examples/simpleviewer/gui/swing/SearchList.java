/**
* ===========================================
* Java Pdf Extraction Decoding Access Library
* ===========================================
*
* Project Info:  http://www.jpedal.org
* Project Lead:  Mark Stephens (mark@idrsolutions.com)
*
* (C) Copyright 2006, IDRsolutions and Contributors.
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
* SearchList.java
* ---------------
* (C) Copyright 2005, by IDRsolutions and Contributors.
*
* 
* --------------------------
*/
package org.jpedal.examples.simpleviewer.gui.swing;

import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.DefaultListModel;

import org.jpedal.utils.Messages;


/**used by search function ro provide page number as tooltip*/
public class SearchList extends javax.swing.JList {

	private Map textPages;
	
	private String pageStr="Page";
	
	public SearchList(DefaultListModel listModel,Map textPages) {
		super(listModel);

		this.textPages=textPages;
		pageStr=Messages.getMessage("PdfViewerSearch.Page")+" ";
	}

	public String getToolTipText(MouseEvent event){
	
		int index=this.locationToIndex(event.getPoint());
		
		Object page=textPages.get(new Integer(index));
		
		if(page!=null)
			return pageStr+page;
		else
			return null;
	}
}
