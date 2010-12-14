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
* ColorspaceDecoder.java
* ---------------
* (C) Copyright 2003, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*
*/
package org.jpedal.color;

import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.jpedal.io.PdfObjectReader;
import org.jpedal.utils.Strip;

/**
 * @author markee
 *
 * returns the correct colorspace, decoding the values
 */
public class ColorspaceDecoder {

	private ColorspaceDecoder(){}
	
	/**
	 * used by commands which implicitly set colorspace
	 */
	final public static GenericColorSpace getColorSpaceInstance(boolean isPrinting, float[][] CTM,
			String currentColorspace,
			Map colorValues,PdfObjectReader currentPdfFile) {
		
		/**convert values*/
		if ((colorValues != null)&&(colorValues.size()>0)){
			
			String current =Strip.removeArrayDeleminators((String) colorValues.get("rawValue"));
			
			if(current.indexOf("/")!=-1){
				currentColorspace=current;
			}else{
				//allow for name in Map
				Iterator ii=colorValues.keySet().iterator();
				StringBuffer buf=new StringBuffer();
				String next;
				while(ii.hasNext()){
					next=(String)ii.next();
					if(!next.equals("rawValue"))
						buf.append(next);
				}
				currentColorspace=buf.toString();
				
			}
		}else
			currentColorspace = Strip.removeArrayDeleminators(currentColorspace);
		
		
		int ID=ColorSpaces.convertNameToID(currentColorspace);
		
		//if(ID==10)
		   // System.exit(1);
		
		if (ID==ColorSpaces.Separation)
			 return new SeparationColorSpace(currentPdfFile,currentColorspace,colorValues);
		else if ((ID==ColorSpaces.DeviceN)&&(ColorSpaces.useDeviceN))
			 return new DeviceNColorSpace(currentPdfFile,currentColorspace,colorValues);
		else
			return  getColorSpaceInstance(isPrinting,CTM,currentColorspace,ID,colorValues,currentPdfFile);
			
	}
	
	final public static GenericColorSpace getColorSpaceInstance(boolean isPrinting,
			float[][] CTM,
			String currentColorspace,
			int ID,
			Map colorValues,PdfObjectReader currentPdfFile) {
		
		String whitepoint=null,blackpoint=null,range=null,gamma=null,matrix=null;
		
		boolean isIndexed=false;
		String CMap="",hvalue="";
		
		//allow for a value
		if((colorValues==null)&&(currentColorspace.indexOf("<<")!=-1)){
			
			colorValues = currentPdfFile.directValuesToMap(currentColorspace);
		}
		
		//default value
		GenericColorSpace currentColorData=new DeviceRGBColorSpace();
		
		/**setup colorspaces which map onto others*/
		if (ID==ColorSpaces.Indexed){

            int pointer=currentColorspace.indexOf("/Indexed");
			String rawColorspace=currentColorspace.substring(pointer+8,currentColorspace.length()).trim();		
			StringTokenizer values =new StringTokenizer(rawColorspace, "] ");
			
			//get first 3 values assuming 3 are direct
			currentColorspace = values.nextToken();

            if(currentColorspace.equals("[")){
				
				//get colorspace
				Iterator ii=colorValues.keySet().iterator();
				
				StringBuffer buf=new StringBuffer();
				String next;
				while(ii.hasNext()){
					next=(String)ii.next();
					if(!next.equals("rawValue"))
						buf.append(next);
				}
				currentColorspace=buf.toString();

            }
			
			hvalue = values.nextToken();
			CMap = values.nextToken();

            //see if first item actually an object pointing to first and read
			if (CMap.equals("R")) {
				Map colorData =currentPdfFile.readObject(currentColorspace + " " + hvalue + " R",true, null);
                currentColorspace = Strip.removeArrayDeleminators((String) colorData.get("rawValue")).trim();
				
				if(currentColorspace.length()==0){
					
					colorValues=colorData;
					//get colorspace
					Iterator ii=colorData.keySet().iterator();
					StringBuffer buf=new StringBuffer();
					String next;
					while(ii.hasNext()){
						next=(String)ii.next();
						if(!next.equals("rawValue"))
							buf.append(next);
					}
					currentColorspace=buf.toString();

                }

                //reread other values
				hvalue = values.nextToken();
				CMap = values.nextToken();
			}
			
			//add any more values onto CMAP_descriptor
			StringBuffer buf=new StringBuffer(CMap);
			while (values.hasMoreTokens()){
				buf.append(' ');
				buf.append(values.nextToken());
			}
			CMap=buf.toString();

			isIndexed=true;	
			ID=ColorSpaces.convertNameToID(currentColorspace);

        }
		
		if (ID==ColorSpaces.Separation)
			currentColorData=new SeparationColorSpace(currentPdfFile,currentColorspace,colorValues);
		else if ((ID==ColorSpaces.DeviceN)&&(ColorSpaces.useDeviceN)){
			currentColorData=new DeviceNColorSpace(currentPdfFile,currentColorspace,colorValues);
		/**use alt for ICC if available as much faster*/
		}else if(ID==ColorSpaces.ICC){
			//remove /ICCBased & ] to get object ref
			int  pointer = currentColorspace.indexOf("/ICCBased");
			String object_ref = currentColorspace.substring(pointer + 9);
			pointer = object_ref.indexOf("]");
			if (pointer != -1)
				object_ref = object_ref.substring(0, pointer - 1);
			
			//read the ICC profile in the file
			/**String alt = "";
			if(object_ref.indexOf(" R")!=-1){
			Map colorData =currentPdfFile.readObject(object_ref.trim(),false, null);
				alt=(String)colorData.get("Alternate");
			}else
				alt=object_ref.trim();

			if((alt!=null)&&(alt.length()>1)){
				currentColorspace=alt;
				ID=ColorSpaces.convertNameToID(currentColorspace);				
			}*/
		}
		
		if (ID==ColorSpaces.DeviceGray){
			currentColorData=new DeviceGrayColorSpace();
		} else if (ID==ColorSpaces.DeviceRGB) {
			currentColorData=new DeviceRGBColorSpace();
		} else if (ID==ColorSpaces.DeviceCMYK) {
			currentColorData=new DeviceCMYKColorSpace();
		} else if (ID==ColorSpaces.CalGray) {
			Map calValues=(Map)colorValues.get("CalGray");
			if(calValues==null)
				calValues=colorValues;
			whitepoint=currentPdfFile.getValue((String) calValues.get("WhitePoint"));
			blackpoint=currentPdfFile.getValue((String)calValues.get("BlackPoint"));
			gamma =currentPdfFile.getValue((String) calValues.get("Gamma"));
			currentColorData=new CalGrayColorSpace(whitepoint,blackpoint,gamma);
			
		} else if (ID==ColorSpaces.CalRGB) {

            Map calValues=null;
            if(colorValues!=null)
                calValues=(Map)colorValues.get("CalRGB");

            if(calValues==null)
				calValues=colorValues;

            if(calValues!=null){
                whitepoint=currentPdfFile.getValue((String) calValues.get("WhitePoint"));
                blackpoint=currentPdfFile.getValue((String)calValues.get("BlackPoint"));
                matrix =currentPdfFile.getValue((String) calValues.get("Matrix"));
                gamma =currentPdfFile.getValue((String) calValues.get("Gamma"));
            }
            currentColorData=new CalRGBColorSpace(whitepoint,blackpoint,matrix,gamma);

        } else if (ID==ColorSpaces.Lab) {
			Map labValues=(Map)colorValues.get("Lab");
			whitepoint=currentPdfFile.getValue((String) labValues.get("WhitePoint"));
			blackpoint=currentPdfFile.getValue((String) labValues.get("BlackPoint"));
			range =currentPdfFile.getValue((String) labValues.get("Range"));
			
			currentColorData=new LabColorSpace(whitepoint,blackpoint,range);
		} else if (ID==ColorSpaces.ICC) {
			currentColorData=new ICCColorSpace(currentPdfFile,currentColorspace);

			//use alternative if not valid
			if(currentColorData.isInvalid()){

				int  pointer = currentColorspace.indexOf("/ICCBased");
				String object_ref = currentColorspace.substring(pointer + 9);
				pointer = object_ref.indexOf("]");
				if (pointer != -1)
					object_ref = object_ref.substring(0, pointer - 1);

				String alt = "";
				if(object_ref.indexOf(" R")!=-1){
				Map colorData =currentPdfFile.readObject(object_ref.trim(),false, null);
					alt=(String)colorData.get("Alternate");
				}else
					alt=object_ref.trim();

				if((alt!=null)&&(alt.length()>1)){
					currentColorspace=alt;
					ID=ColorSpaces.convertNameToID(currentColorspace);
				}
			}


		}
		
		/**handle CMAP as object or direct*/
		if(isIndexed){
			int size=Integer.parseInt(hvalue);
			if (CMap.endsWith(" R"))
				currentColorData.setIndex(currentPdfFile.readStream(CMap,true),size);
			else  //direct list of values
				currentColorData.setIndex(CMap,currentColorspace,size);
		}	
		
		return currentColorData;	
	}
}
