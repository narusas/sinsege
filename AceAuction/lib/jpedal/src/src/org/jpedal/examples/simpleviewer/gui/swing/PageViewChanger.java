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
 *PageViewChanger.java
 * ---------------
 *
 * Original Author:  Mark Stephens (mark@idrsolutions.com)
 * Contributor(s):
 *
 */
package org.jpedal.examples.simpleviewer.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jpedal.PdfDecoder;
import org.jpedal.examples.simpleviewer.gui.SwingGUI;
import org.jpedal.examples.simpleviewer.utils.PropertiesFile;

/**used from 2.8 onwards in views with multiple pages to setup new view settings from menu when option choosen*/
public class PageViewChanger implements ActionListener{
	
	
	int id,alignment;
	private PdfDecoder decode_pdf;
	private PropertiesFile properties;
	private SwingGUI swingGUI;
	public PageViewChanger(int alignment,int i,PdfDecoder decode_pdf, PropertiesFile properties, SwingGUI swingGUI){
		id=i;
        this.alignment=alignment;
        this.decode_pdf=decode_pdf;
        this.properties = properties;
        this.swingGUI = swingGUI;
	}
	
	public void actionPerformed(ActionEvent e) {	
		
		/**/
		
	}
	
}

