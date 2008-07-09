/*
* ===========================================
* Java Pdf Extraction Decoding Access Library
* ===========================================
*
* Project Info:  http://www.jpedal.org
* Project Lead:  Mark Stephens (mark@idrsolutions.com)
*
* (C) Copyright 2004, IDRsolutions and Contributors.
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
* Hhea.java
* ---------------
* (C) Copyright 2004, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal.fonts.tt;

import org.jpedal.utils.LogWriter;


public class Hhea extends Table {
		
	private int version;
	private short ascender;
	private short descender;
	private short lineGap;
	private short advanceWidthMax;
	private short minLeftSideBearing;
	private short minRightSideBearing;
	private short xMaxExtent;
	private short caretSlopeRise;
	private short caretSlopeRun;
	private short caretOffset;
	private short metricDataFormat;
	private int numberOfHMetrics;
	
	public Hhea(FontFile2 currentFontFile){
	
		LogWriter.writeMethod("{readHheaTable}", 0);
		
		//move to start and check exists
		int startPointer=currentFontFile.selectTable(FontFile2.HHEA);
		
		//read 'head' table
		if(startPointer!=0){
			
		version =currentFontFile.getNextUint32();
		ascender =currentFontFile.getFWord();
		descender = currentFontFile.getFWord();
		lineGap = currentFontFile.getFWord();
		advanceWidthMax = currentFontFile.readUFWord();
		minLeftSideBearing = currentFontFile.getFWord();
		minRightSideBearing =currentFontFile.getFWord();
		xMaxExtent =  currentFontFile.getFWord();
		caretSlopeRise = currentFontFile.getNextInt16();
		caretSlopeRun =currentFontFile.getNextInt16();
		caretOffset=currentFontFile.getFWord();
		
		//reserved values
		for( int i = 0; i < 4; i++ )
			currentFontFile.getNextUint16();
		
		metricDataFormat = currentFontFile.getNextInt16();
		numberOfHMetrics =currentFontFile.getNextUint16();
		
		
		}
	}
	
	public int getNumberOfHMetrics(){
		return numberOfHMetrics;
	}
	
}
