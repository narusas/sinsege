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
* Head.java
* ---------------
* (C) Copyright 2004, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal.fonts.tt;

import org.jpedal.utils.LogWriter;


public class Head extends Table {
	
	/**format used*/
	private int format=0;
	
	/**bounds on font*/
	private int[] matrix=new int[4];

	private int flags;

	private int unitsPerEm=1;

	public Head(FontFile2 currentFontFile){
	
		LogWriter.writeMethod("{readHeadTable}", 0);
		
		//move to start and check exists
		int startPointer=currentFontFile.selectTable(FontFile2.HEAD);
		
		//read 'head' table
		if(startPointer==0)
			LogWriter.writeLog("No head table found");
		else{
			
			int id=currentFontFile.getNextUint32();
			
			//ignore values
			for(int i=0;i<3;i++)
				id=currentFontFile.getNextUint32();
			
			flags=currentFontFile.getNextUint16();
			unitsPerEm=currentFontFile.getNextUint16();
			
			//ignore dates
			for(int i=0;i<2;i++)
				id=currentFontFile.getNextUint64();
			
			//ignore bounds
			for(int i=0;i<4;i++)
                matrix[i]=currentFontFile.getNextSignedInt16();

			//ignore more flags
			for(int i=0;i<3;i++)
				id=currentFontFile.getNextUint16();
			
			//finally the bit we want indicating size of chunks in mapx
			format=currentFontFile.getNextUint16();
			
		}
	}
	
	public int getFormat(){
		return format;
	}

	public int[] getMatrix(){
		return this.matrix;
	}
	/**
	 * get flags in Head
	 */
	public int getFlags() {
		return flags;
	}

	/**
	 *  Returns the unitsPerEm.
	 */
	public int getUnitsPerEm() {
		return unitsPerEm;
	}

}
