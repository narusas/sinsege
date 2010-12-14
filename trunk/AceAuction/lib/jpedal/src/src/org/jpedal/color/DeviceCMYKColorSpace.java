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
* DeviceCMYKColorSpace.java
* ---------------
* (C) Copyright 2003, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
* 
* Tom Phelps has helped with the CMYK image handling
*
*
*/
package org.jpedal.color;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import org.jpedal.utils.LogWriter;

/**
 * handle DeviceCMYKColorSpace
 */
public class DeviceCMYKColorSpace
	extends  GenericColorSpace{
		
	private float lastC = -1, lastM=-1, lastY=-1, lastK=-1;
	
	private  static ColorSpace CMYK=null;

    /**
	 * initialise CMYK profile
	 */
	private void initColorspace() {
	
		/**load the cmyk profile - I am using the Adobe version from the web. There are lots
		 * out there.*/
		try {

            InputStream stream=this.getClass().getResourceAsStream("/org/jpedal/res/cmm/cmyk.icm");
            ICC_Profile p =ICC_Profile.getInstance(stream);
            CMYK = new ICC_ColorSpace(p);
            stream.close();
            
        } catch (Exception e) {
		    LogWriter.writeLog("Exception "+e);
		    
		}
	}
	
	/**setup colorspaces*/
	public DeviceCMYKColorSpace(){
	
		componentCount=4;
		
		if(CMYK==null)
		initColorspace();
		
		cs = CMYK;
		
		value = ColorSpaces.DeviceCMYK;
	}

	/**
	 * convert CMYK to RGB as defined by Adobe
	 * (p354 Section 6.2.4 in Adobe 1.3 spec 2nd edition)
	 * and set value
	 */
	final public void setColor(String[] operand,int length) {
		
		boolean newVersion=true;
		
		//default of black
		c=1;
		y=1;
		m=1;
		k=1;
		
		if(length>3){
			//get values
			c= Float.parseFloat(operand[3]);
			// the cyan
			m= Float.parseFloat(operand[2]);
			// the magenta
			y = Float.parseFloat(operand[1]);
			// the yellow
			k = Float.parseFloat(operand[0]);
		}else{
			//get values
			if(length>3)
			c= Float.parseFloat(operand[3]);
			// the cyan
			if(length>2)
			m= Float.parseFloat(operand[2]);
			// the magenta
			if(length>1)
			y = Float.parseFloat(operand[1]);
			// the yellow
			if(length>0)
			k = Float.parseFloat(operand[0]);
			
		}
		
		
		float r, g, b;
		if ((lastC == c) && (lastM == m) && (lastY == y) && (lastK == k)) {
		} else {
			if(!newVersion){
				//convert the colours the old way
				r = (c + k);
				if (r > 1)
					r = 1;
				g = (m + k);
				if (g > 1)
					g = 1;
				b = (y + k);
				if (b > 1)
					b = 1;
		
				//set the colour
				this.currentColor= new PdfColor(
				(int) (255 * (1 - r)),
				(int) (255 * (1 - g)),
				(int) (255 * (1 - b)));
			}else if((c==0)&&(y==0)&&(m==0)&&(k==0)){
				this.currentColor=new PdfColor(1.0f,1.0f,1.0f);
				
			}else{
				if(c>.99)
					c=1.0f;
				else if(c<0.01)
					c=0.0f;
				if(m>.99)
					m=1.0f;
				else if(m<0.01)
					m=0.0f;
				if(y>.99)
					y=1.0f;
				else if(y<0.01)
					y=0.0f;
				if(k>.99)
					k=1.0f;
				else if(k<0.01)
					k=0.0f;
				float[] cmykValues = {c,m,y,k};
				float[] rgb=CMYK.toRGB(cmykValues);
				
				//check rounding
				for(int jj=0;jj<3;jj++){
					if(rgb[jj]>.99)
						rgb[jj]=1.0f;
					else if(rgb[jj]<0.01)
						rgb[jj]=0.0f;
				}
				currentColor=new PdfColor(rgb[0],rgb[1],rgb[2]);
				
			}
			lastC=c;
			lastM=m;
			lastY=y;
			lastK=k;
		}
	}


	

	/**
	 * <p>
	 * Convert DCT encoded image bytestream to sRGB
	 * </p>
	 * <p>
	 * It uses the internal Java classes and the Adobe icm to convert CMYK and
	 * YCbCr-Alpha - the data is still DCT encoded.
	 * </p>
	 * <p>
	 * The Sun class JPEGDecodeParam.java is worth examining because it contains
	 * lots of interesting comments
	 * </p>
	 * <p>
	 * I tried just using the new IOImage.read() but on type 3 images, all my
	 * clipping code stopped working so I am still using 1.3
	 * </p>
	 */
	final public BufferedImage JPEGToRGBImage(
		byte[] data,int w,int h,String decodeArray) {

		return nonRGBJPEGToRGBImage(data,w,h, decodeArray);

	}	
	
	/**
	 * convert Index to RGB
	  */
	final public byte[] convertIndexToRGB(byte[] index){
		return convert4Index(index);
	}	
}
