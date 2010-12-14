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
* DeviceNColorSpace.java
* ---------------
* (C) Copyright 2003, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*
*/
package org.jpedal.color;

import java.util.Map;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBuffer;
import java.io.ByteArrayInputStream;

import org.jpedal.io.PdfObjectReader;
import org.jpedal.utils.LogWriter;

import javax.imageio.ImageReader;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

/**
 * handle Device ColorSpace
 */
public class DeviceNColorSpace
	extends SeparationColorSpace {
	
	public DeviceNColorSpace(){	
		
	}
	
	public DeviceNColorSpace(PdfObjectReader currentPdfFile,String currentColorspace,Map colorValues) {

		value = ColorSpaces.DeviceN;
		
		processColorToken(currentPdfFile, currentColorspace, colorValues);
	}
	
	//<start-13>
	/** set color (translate and set in alt colorspace */
	public void setColor(String[] operand,int opCount) {
        //in reverse order so turn around
//        String[] reversedOps=new String[opCount];
//        for(int jj=0;jj<opCount;jj++)
//        	reversedOps[jj]=operand[opCount-jj-1];
//
//        operand=reversedOps;
		
        try{
			
			float[] values = new float[opCount];
			for(int j=0;j<opCount;j++)
			values[j] = Float.parseFloat(operand[j]);
			
			operand =colorMapper.getOperand(values);

            altCS.setColor(operand,operand.length);

        }catch(Exception e){
		}

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
			LogWriter.writeLog("Couldn't convert DeviceN colorspace data: " + ee);
		}
		
		return image;

	}
	//<end-13>

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
                iir = (ImageReader) ImageIO.getImageReadersByFormatName("JPEG").next();
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

                ee.printStackTrace();

               // System.exit(1);
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
         * turn raw data into an image
         */
        private BufferedImage createImage(int w, int h, byte[] rawData) {

            BufferedImage image;

            byte[] rgb=new byte[w*h*3];

            //convert data to RGB format
            int byteCount= rawData.length/componentCount;
            
            String[] values=new String[componentCount];

            int j=0,j2=0;

            for(int i=0;i<byteCount;i++){

                if(j>=rawData.length)
                break;
                
//                rgb[j2]=(byte) (rawData[j] & 255);
//                rgb[j2+1]=(byte) (rawData[j+1] & 255);
//                rgb[j2+2]=(byte) (rawData[j+2] & 255);
//                j2=j2+3;
//                j=j+4;

                for(int comp=0;comp<componentCount;comp++){

                    float value=((rawData[j] & 255)/255f);

              //  System.out.println(comp+" "+value+" "+rawData[j]);
                    values[componentCount-comp-1]=""+value;
                    j++;

                }

                setColor(values,componentCount);

               

                //set values
                int foreground =altCS.currentColor.getRGB();

                //System.out.println(currentColor+"<<<<<<"+altCS+" "+altCS.currentColor);

                rgb[j2]=(byte) ((foreground>>16) & 0xFF);
                rgb[j2+1]=(byte) ((foreground>>8) & 0xFF);
                rgb[j2+2]=(byte) ((foreground) & 0xFF);
                
               // System.out.println(rgb[j2]+" "+rgb[j2+1]+" "+rgb[j2+2]+" ");
              //  System.exit(1);
                
                j2=j2+3;

            }

            //create the RGB image
            int[] bands = {0,1,2};
            DataBuffer dataBuf=new DataBufferByte(rgb, rgb.length);
            image =new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
            Raster raster =Raster.createInterleavedRaster(dataBuf,w,h,w*3,3,bands,null);
            image.setData(raster);

        //ShowGUIMessage.showGUIMessage("x",image,"x");
        
            return image;
        }


	
}
