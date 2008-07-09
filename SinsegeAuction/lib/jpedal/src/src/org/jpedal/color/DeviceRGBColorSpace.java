/*
* ===========================================
* Java Pdf Extraction Decoding Access Library
* ===========================================
*
* Project Info:  http://www.jpedal.org
* Project Lead:  Mark Stephens (mark@idrsolutions.com)
*
* (C) Copyright 2003, IDRsolutions and Contributors.
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
* DeviceRGBColorSpace.java
* ---------------
* (C) Copyright 2003, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*
*/
package org.jpedal.color;

/**
 * handle RGB ColorSpace
 */
public class DeviceRGBColorSpace
	extends  GenericColorSpace{
		
	public DeviceRGBColorSpace(){	
		value = ColorSpaces.DeviceRGB;
	}

	/**set color*/
	final public void setColor(String[] operand,int length) {
		
		int value;
		
		//note indexed also uses this
		if (length == 1) {

			//get indexed values
			String raw=operand[0];
			
			int[] val=new int[3];
			
			if(!raw.startsWith("/")){
				int id =(int)( Float.parseFloat(raw)*3);
		
				for(int i=0;i<3;i++){
					value =getIndexedColorComponent(id+i);
							
					val[i] = value;
				}
				
				//set color
				currentColor = new PdfColor(val[0],val[1],val[2]);
				
			}
	
		} else if (length > 2) {
	
			float[] val=new float[3];
			
			for(int i=0;i<3;i++){
									
				//red
				val[i]= Float.parseFloat(operand[2-i]);
				if(val[i]<0)
				val[i]=0;
				if(val[i]>1)
				val[i]=1;
	
			}
			
			//set color
			currentColor = new PdfColor(val[0],val[1],val[2]);
			
		}
	}		
	
}
