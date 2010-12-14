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
* CalGrayColorSpace.java
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
 * handle CalGrayColorSpace
 */
public class CalGrayColorSpace
	extends GenericColorSpace {
	
	public CalGrayColorSpace(String whitepoint,String blackpoint,String gamma) {
			
		componentCount=1;
		
		setCIEValues(whitepoint,blackpoint,null,null,gamma);
		value = ColorSpaces.CalGray;
		
	}

	  /**
	   * set CalGray color (in terms of rgb)
	   */
	  final public void setColor(String[] number_values,int opCount) {
		  float A = Float.parseFloat(number_values[0]);

		  //standard calc (see pdf spec 1.3 page 170)
		  float[] values = new float[3];
		  float AG = (float) Math.pow(A, G[0]);
		  
		  values[0] = ((W[0]) * AG);
		  values[1] = ((W[1]) * AG);
		  values[2] = (W[2] * AG);
		  
		  //convert to rgb
		  values = cs.toRGB(values);
		   
		  //set color
		  this.currentColor= new PdfColor(values[0],values[1],values[2]);
		
	  }
}
