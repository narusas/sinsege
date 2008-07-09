/*
 * =========================================== Java Pdf Extraction Decoding
 * Access Library ===========================================
 * 
 * Project Info: http://www.jpedal.org Project Lead: Mark Stephens
 * (mark@idrsolutions.com)
 * 
 * (C) Copyright 2003, IDRsolutions and Contributors.
 * 
 * This file is part of JPedal
 * 
 *     This library is free software; you can redistribute it and/or
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
 * --------------- SeparationColorSpace.java --------------- (C) Copyright
 * 2003, by IDRsolutions and Contributors.
 * 
 * Original Author: Mark Stephens (mark@idrsolutions.com) Contributor(s):
 * 
 *  
 */
package org.jpedal.color;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

import java.io.ByteArrayInputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

//<start-13>
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
//<end-13>

import org.jpedal.io.PdfObjectReader;
import org.jpedal.utils.LogWriter;

/**
 * handle Separation ColorSpace - thanks to Tom for some of the code -
 * reproduced with permissions
 */
public class SeparationColorSpace extends GenericColorSpace {

	protected GenericColorSpace altCS;
	
    protected ColorMapping colorMapper;

    private float[] domain;
	
    public SeparationColorSpace() {}

	public SeparationColorSpace(PdfObjectReader currentPdfFile,String currentColorspace,Map colorValues) {

        value = ColorSpaces.Separation;

		processColorToken(currentPdfFile, currentColorspace, colorValues);
    }

	protected void processColorToken(PdfObjectReader currentPdfFile, String currentColorspace, Map colorValues) {


        String altColorSpaceName = "", transformName = "";
		Map altColorSpace = new Hashtable();
		Map tintTransform = new Hashtable();
		byte[] stream = null;
		float[]  range = null;

        domain = null;
        
        StringTokenizer sep_tokens =new StringTokenizer(currentColorspace, " ");

        if (sep_tokens.countTokens() > 3) { //3 values
			
			String sepKeyWord = sep_tokens.nextToken();
			
			if(value==ColorSpaces.Separation){
				pantoneName = sep_tokens.nextToken().substring(1);
				componentCount=1;
			}else{ //read DeviceN array
				
				String colorantWord = sep_tokens.nextToken();
				
				//read the colorant values
				componentCount=0;
				
				while(true){
					colorantWord = sep_tokens.nextToken();
					
					if(colorantWord.equals("]"))
						break;
					componentCount++;
					
				}
			}
			
			altColorSpaceName = sep_tokens.nextToken();

            if(altColorSpaceName.equals("[")){
                while(altColorSpaceName.indexOf("]")==-1)
                   altColorSpaceName=altColorSpaceName+" "+sep_tokens.nextToken();
            }

            transformName = sep_tokens.nextToken();

			if (sep_tokens.hasMoreTokens()) {

				//allow for alt colorspace as object
				if (transformName.equals("0")) {

					altColorSpaceName = altColorSpaceName + " 0 R";
					altColorSpace =
						currentPdfFile.readObject(altColorSpaceName, false, null);
					transformName = "";
					sep_tokens.nextToken();
				} else
					transformName = transformName + " ";

				if(value==ColorSpaces.Separation){
					//read rest of transform
					while (sep_tokens.hasMoreTokens())
						transformName =
							transformName + sep_tokens.nextToken() + " ";
				}else{ //read DeviceN array
					//read rest of transform
					while (sep_tokens.hasMoreTokens()){
						String nextToken=sep_tokens.nextToken();
						transformName =transformName + nextToken  + " ";
						if(nextToken.equals("R")|(nextToken.equals("<<")))
							break;
					}
				}
				

				transformName = transformName.trim();

				if (transformName.endsWith("R"))
					tintTransform =currentPdfFile.readObject(transformName, false, null);
				
				if (altColorSpaceName.endsWith("R")){
					Map ColObj=currentPdfFile.readObject(altColorSpaceName, false, null);
					altColorSpaceName=(String) ColObj.get("rawValue");
				}

			}

			/** setup values */
			stream = currentPdfFile.readStream(transformName,true);

		} else {
		    //get the pantone
		    pantoneName=currentColorspace.substring(11).trim();
		    
		    int firstR=pantoneName.indexOf("R");
		    if(pantoneName.startsWith("/")){
		        pantoneName=pantoneName.substring(1);	 
		        int altIndex=pantoneName.indexOf("/");
		        if(altIndex!=-1){
		        		altColorSpaceName=pantoneName.substring(altIndex).trim();
		        		pantoneName=pantoneName.substring(0,altIndex).trim();
		        }
		    }else if(firstR!=-1){ //extract name and alt colorspace allowing for indirect as well
		    		altColorSpaceName=pantoneName.substring(firstR+1).trim();
		        pantoneName=currentPdfFile.getValue(pantoneName.substring(0,firstR));
		        
		        if(altColorSpaceName.endsWith("R")) 
		        	altColorSpaceName=currentPdfFile.getValue(altColorSpaceName);
		        
		    }
		    
		    	if(altColorSpaceName.length()==0){
				//allow for name in Map
				Iterator ii = colorValues.keySet().iterator();
				altColorSpaceName = "";
				while (ii.hasNext()) {
					String next = (String) ii.next();
					if (!next.equals("rawValue")){
						altColorSpaceName = altColorSpaceName + next;
					}
				}
		    }

			tintTransform = (Map) colorValues.get(altColorSpaceName);

			if(tintTransform==null)
				tintTransform=colorValues;
		}

        /**
		 * set alt colorspace 
		 **/
		altCS =ColorspaceDecoder.getColorSpaceInstance(false, null,
				altColorSpaceName,
				altColorSpace,
				currentPdfFile);

        //handle any chars embedded as # in PantoneName
		if((pantoneName!=null)&&(pantoneName.indexOf("#")!=-1)){
		    StringBuffer newValue=new StringBuffer();
		    int nameLength=pantoneName.length();
		    
		    for(int i=0;i<nameLength;i++){
		        char c=pantoneName.charAt(i);
		        
		        if(c=='#'){
		            String hexValue=pantoneName.substring(i+1,i+3);
		            newValue.append((char)Integer.parseInt(hexValue,16));
		            i=i+2;
		        }else{
		            newValue.append(c);
		        }
		    }
		    
		    pantoneName=newValue.toString();
		    
		}
		
		/**
		 * setup transformation
		 **/
		colorMapper=new ColorMapping(currentPdfFile, tintTransform, stream, range);

        domain=colorMapper.getDomain();

    }

	
	
	//<start-13>
	/**private method to do the calculation*/
	private void setColor(float value){
		try{

			//adjust size if needed
			int elements=1;

			if(domain!=null)
				elements=domain.length/2;

			float[] values = new float[elements];
			for(int j=0;j<elements;j++)
				values[j] = value;

			String[] operand =colorMapper.getOperand(values);

			altCS.setColor(operand,operand.length);

		}catch(Exception e){
		}
	}
	
	/** set color (translate and set in alt colorspace */
	public void setColor(String[] operand,int opCount) {

        setColor(Float.parseFloat(operand[0]));

	}
	//<end-13>
	
	//<start-13>
	/**
	 * convert data stream to srgb image
	 */
	public BufferedImage JPEGToRGBImage(
			byte[] data,int ww,int hh,String decodeArray) {
		
		BufferedImage image = null;
		ByteArrayInputStream in = null;
		
		ImageReader iir=null;
		ImageInputStream iin=null;
		
		try {
			
			//read the image data
			in = new ByteArrayInputStream(data);
			iir = (ImageReader)ImageIO.getImageReadersByFormatName("JPEG").next();
			ImageIO.setUseCache(false);
			iin = ImageIO.createImageInputStream((in));
			iir.setInput(iin, true);   
			Raster r=iir.readRaster(0, null);
			int w = r.getWidth(), h = r.getHeight();
			DataBufferByte rgb = (DataBufferByte) r.getDataBuffer();


            //convert the image
			image=createImage(w, h, rgb.getData());
			
		} catch (Exception ee) {
			image = null;
			LogWriter.writeLog("Couldn't read JPEG, not even raster: " + ee);
		}
		
		try {
			in.close();
			iir.dispose();
			iin.close();
		} catch (Exception ee) {
			LogWriter.writeLog("Problem closing  " + ee);
		}
		
		return image;
		
	}	
	
	/**
	 * convert separation stream to RGB and return as an image
	  */
	public BufferedImage  dataToRGB(byte[] data,int w,int h) {

		BufferedImage image=null;
		
		try {
			
			//convert data
			image=createImage(w, h, data);
			
		} catch (Exception ee) {
			image = null;
			LogWriter.writeLog("Couldn't convert Separation colorspace data: " + ee);
		}
		
		return image;

	}
	
	/**
	 * turn raw data into an image
	 */
	private BufferedImage createImage(int w, int h, byte[] rgb) {

        BufferedImage image;
		
		//convert data to RGB format
		int byteCount=rgb.length;
		float[] lookuptable=new float[256];
		for(int i=0;i<255;i++)
			lookuptable[i]=-1;

        for(int i=0;i<byteCount;i++){
			
			int value=(rgb[i] & 255);
			if(lookuptable[value]==-1){
				setColor(value/255f);
				lookuptable[value]=((Color)this.getColor()).getRed();
			}
			rgb[i]= (byte) lookuptable[value];
			
		}
		
		//create the RGB image
		int[] bands = {0};
        DataBuffer dataBuf=new DataBufferByte(rgb,rgb.length);
        image =new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
		Raster raster =Raster.createInterleavedRaster(dataBuf,w,h,w,1,bands,null);
		image.setData(raster);
		
		return image;
	}
	
	/**
	 * create rgb index for color conversion
	 */
	public byte[] convertIndexToRGB(byte[] data){
		
		byte[] newdata=new byte[3*256]; //converting to RGB so size known
		
		try {
			
			int outputReached=0;
			String[] opValues=new String[1];
			Color currentCol=null;
			String[] operand;
			int byteCount=data.length;
			float[] values = new float[componentCount];
			
			//scan each byte and convert
			for(int i=0;i<byteCount;i=i+componentCount){
				
				//turn into rgb and store
				if(this.componentCount==1){ //separation
					opValues=new String[1];
					opValues[1]=""+(data[i] & 255);
					setColor(opValues,1);
					currentCol=(Color)this.getColor();
				}else{ //convert deviceN
					
					for(int j=0;j<componentCount;j++)
						values[componentCount-1-j] = (data[i+j] & 255)/255f;
					
					operand = colorMapper.getOperand(values);
					
					altCS.setColor(operand,operand.length);
					currentCol=(Color)altCS.getColor();
						
				}
				
				newdata[outputReached]=(byte) currentCol.getBlue();
				outputReached++;
				newdata[outputReached]=(byte)currentCol.getGreen();
				outputReached++;
				newdata[outputReached]=(byte)currentCol.getRed();
				outputReached++;
				
			}
			
		} catch (Exception ee) {
			
			
			System.out.println(ee);
			LogWriter.writeLog("Exception  " + ee + " converting colorspace");
		}
		
		return newdata;		
	}
	//<end-13>
	
	/**
	 * get color
	 */
	public PdfPaint getColor() {
		//<start-13>
		/**
		//<end-13>
		return new PdfColor(255,255,255);
		//<start-13>
		*/
		return altCS.getColor();
		//<end-13>
		
	}
	
	/**
	 * get alt colorspace for separation colorspace
	 */
	public GenericColorSpace getAltColorSpace()
	{
		return altCS;
	}

}
