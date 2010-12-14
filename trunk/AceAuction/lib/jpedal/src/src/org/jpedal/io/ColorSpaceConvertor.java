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
* ColorSpaceConvertor.java
* ---------------
* (C) Copyright 2005, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal.io;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import org.jpedal.color.ColorSpaces;
import org.jpedal.exception.PdfException;
import org.jpedal.utils.LogWriter;

/**
 * set of static methods to save/load objects to convert images between 
 * different colorspaces - 
 * 
 * Several methods are very similar and I should recode my code to use a common
 * method for the RGB conversion 
 * 
 * LogWriter is JPedal logging class
 * 
 */
public class ColorSpaceConvertor {

	/**holds CMYK colorspace for conversion*/
	private static ColorSpace cmykCS;

	/**defines rgb colorspace*/
	private static ColorSpace rgbCS;

	/**conversion Op for translating rasters or images*/
	private static ColorConvertOp CSToRGB = null;

    private static ColorConvertOp ccopWithHints = new ColorConvertOp(ColorSpaces.hints);

	/**rgb colormodel*/
	private static ColorModel rgbModel = null;
    public static boolean wasRemoved;

    /**initialise all the colorspaces when first needed */
	private static void initCMYKColorspace() throws PdfException {

		try {

		    /**create CMYK colorspace using icm profile*/
		    ICC_Profile p =ICC_Profile.getInstance(ColorSpaceConvertor.class.getResourceAsStream(
			"/org/jpedal/res/cmm/cmyk.icm"));
		    //ICC_ColorSpace cmykCS = new ICC_ColorSpace(p);
            cmykCS = new ICC_ColorSpace(p);
			/**create CMYK colorspace using icm profile*
		    ICC_ColorSpace
		    ICC_Profile p =
				ICC_Profile.getInstance(
					loader.getResourceAsStream("org/jpedal/res/cmm/cmyk.icm"));
			cmykCS = new ICC_ColorSpace(p);
*/
			/**create RGB colorspace and model*/
			ICC_Profile rgbProfile =
				ICC_Profile.getInstance(ColorSpace.CS_sRGB);
			rgbCS = new ICC_ColorSpace(rgbProfile);
			rgbModel =
				new ComponentColorModel(
					rgbCS,
					new int[] { 8, 8, 8 },
					false,
					false,
					ColorModel.OPAQUE,
					DataBuffer.TYPE_BYTE);

			/**define the conversion. PdfColor.hints can be replaced with null or some hints*/
			CSToRGB = new ColorConvertOp(cmykCS, rgbCS, ColorSpaces.hints);

		} catch (Exception e) {
			LogWriter.writeLog(
				"Exception " + e + " initialising color components");
			
			throw new PdfException("[PDF] Unable to create CMYK colorspace. Check cmyk.icm in jar file");

		}
	}

	/**
	 * save raw CMYK data by converting to RGB using algorithm method - 
	 * pdfsages supplied the C source and I have converted -  
	 * This works very well on most colours but not dark shades which are 
	 * all rolled into black - 
	 * 
	 * This is what xpdf seems to use - 
	 * 
	 * We pass it the name of the file we have previously stored in 
	 * CMYK dir (we just save the encoded DCT stream as xxx.jpg)
	 *
	 
	private static BufferedImage algorithmicConvertCMYKImageToRGBXX() {

		BufferedImage image = null;

		try {

			FileInputStream in =
				new FileInputStream(cmyk_dir + image_name + ".jpg");
			ImageReader currentImageReader =
				(ImageReader) ImageIO
					.getImageReadersByFormatName("JPEG")
					.next();
			ImageIO.setUseCache(false);
			ImageInputStream iin = ImageIO.createImageInputStream(in);
			currentImageReader.setInput(iin, true);

			Raster currentRaster = currentImageReader.readRaster(0, null);

			in.close();
			iin.close();
			currentImageReader.dispose();
			//VERY IMPORTANT - seems to be a memory bug in Java

			int width = currentRaster.getWidth();
			int height = currentRaster.getHeight();
			int pixelCount = width * height;

			int c[] = new int[pixelCount];
			currentRaster.getSamples(0, 0, width, height, 0, c);
			int m[] = new int[pixelCount];
			currentRaster.getSamples(0, 0, width, height, 1, m);
			int y[] = new int[pixelCount];
			currentRaster.getSamples(0, 0, width, height, 2, y);
			int k[] = new int[pixelCount];
			currentRaster.getSamples(0, 0, width, height, 3, k);

			int r, g, b;
			int max_r = 0, max_g = 0, max_b = 0;
			byte[] image_data = new byte[pixelCount * 3];
			for (int i = 0; i < pixelCount; i++) {

				//convert the colours
				r = (c[i] + k[i]);

				if (r > max_r)
					max_r = r;

				if (r > 256)
					r = 0;
				g = (m[i] + k[i]);

				if (g > max_g)
					max_g = g;

				if (g > 256)
					g = 0;
				b = (y[i] + k[i]);
				if (b > max_b)
					max_b = b;
				if (b > 256)
					b = 0;

				image_data[(i * 3)] = (byte) (-r);
				image_data[(i * 3) + 1] = (byte) (-g);
				image_data[(i * 3) + 2] = (byte) (-b);

			}

			DataBuffer db = new DataBufferByte(image_data, image_data.length);

			try {
				int[] bands = new int[3];
				bands[0] = 0;
				bands[1] = 1;
				bands[2] = 2;
				image =
					new BufferedImage(
						width,
						height,
						BufferedImage.TYPE_INT_RGB);
				Raster raster =
					Raster.createInterleavedRaster(
						db,
						width,
						height,
						width * 3,
						3,
						bands,
						null);
				image.setData(raster);
			} catch (Exception e) {
				LogWriter.writeLog("Exception " + e + " with 24 bit RGB image");
			}

		} catch (Exception ee) {
			image = null;
			LogWriter.writeLog("Couldn't read JPEG, not even raster: " + ee);
		}

		return image;
	}*/
	
	/**
	 * slightly contrived but very effective way to convert to RGB 
	 */
	public static BufferedImage convertFromICCCMYK(
		int width,
		int height,
		byte[] data,
		ColorSpace cs) {
	    
	    if(cs==null)
	        cs=rgbCS;

		BufferedImage image = null;
		try {

			/**make sure data big enough and pad out if not*/
			int size = width * height * 4;
			if (data.length < size) {
				byte[] newData = new byte[size];
				System.arraycopy(data, 0, newData, 0, data.length);
				data = newData;
			}

			/**turn it into a BufferedImage so we can filter*/
			DataBuffer db = new DataBufferByte(data, data.length);

			int[] bands = { 0, 1, 2, 3 };

			WritableRaster raster =
				Raster.createInterleavedRaster(
					db,
					width,
					height,
					width * 4,
					4,
					bands,
					null);

			ColorModel cmykModel =
				new ComponentColorModel(
					cs,
					new int[] { 8, 8, 8, 8 },
					false,
					false,
					ColorModel.OPAQUE,
					DataBuffer.TYPE_BYTE);

			image = new BufferedImage(cmykModel, raster, false, null);

		} catch (Exception ee) {
			LogWriter.writeLog(
				"Exception  " + ee + " converting from ICC colorspace");
		}

		return image;

	}

	/**
	 * slightly contrived but very effective way to convert  to RGB 
	 */
	public static BufferedImage convertFromICCCMYK(
		int width,
		int height,
		DataBuffer db,
		ColorSpace cs) {

		BufferedImage image = null;
		try {

			int[] bands = { 0, 1, 2, 3 };

			WritableRaster raster =
				Raster.createInterleavedRaster(
					db,
					width,
					height,
					width * 4,
					4,
					bands,
					null);

			ColorModel cmykModel =
				new ComponentColorModel(
					cs,
					new int[] { 8, 8, 8, 8 },
					false,
					false,
					ColorModel.OPAQUE,
					DataBuffer.TYPE_BYTE);

			image = new BufferedImage(cmykModel, raster, false, null);

		} catch (Exception ee) {
			LogWriter.writeLog(
				"Exception  " + ee + " converting from ICC colorspace");
		}

		return image;

	}
		
	/**
	 * slightly contrived but very effective way to convert CMYK CMAP to RGB -
	 * I've treated the CMAP as an image and converted the values
	 * 
	 * Default is CMYK, but I am trying to allow for other ColorSpaces
	 */
	public static byte[] convertIndexToRGB(byte[] data, ColorSpace cs) {

		try {

			/**turn it into a BufferedImage so we can convert then extract the data*/
			int width = data.length / 4;
			int height = 1;
			DataBuffer db = new DataBufferByte(data, data.length);

			int[] bands = { 0, 1, 2, 3 };
			WritableRaster raster =
				Raster.createInterleavedRaster(
					db,
					width,
					height,
					width * 4,
					4,
					bands,
					null);
			
			WritableRaster rgbRaster =
				rgbModel.createCompatibleWritableRaster(width, height);
			
			//init on first usage
			if(CSToRGB==null)
				initCMYKColorspace();
			CSToRGB.filter(raster, rgbRaster);

			/**put into byte array*/
			int size = width * height * 3;
			data = new byte[size];

			DataBuffer convertedData = rgbRaster.getDataBuffer();

			for (int ii = 0; ii < size; ii++)
				data[ii] = (byte) convertedData.getElem(ii);

		} catch (Exception ee) {
			LogWriter.writeLog("Exception  " + ee + " converting colorspace");
		}

		return data;

	}


	
	/**
	 * convert any BufferedImage to RGB colourspace
	 */
	final public static BufferedImage convertToRGB(BufferedImage image) {
		
		//don't bother if already rgb or ICC
		if ((image.getType() != BufferedImage.TYPE_INT_RGB)) {

			try{
			    /**/
				BufferedImage raw_image = image;
				image =
					new BufferedImage(
						image.getWidth(),
						image.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				//ColorConvertOp xformOp = new ColorConvertOp(ColorSpaces.hints);/**/

				//THIS VERSION IS AT LEAST 5 TIMES SLOWER!!!
				//ColorConvertOp colOp = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB), ColorSpaces.hints);
				//image=colOp.filter(image,null);

				//xformOp.filter(raw_image, image);
                ccopWithHints.filter(raw_image, image);
                //image = raw_image;
			} catch (Exception e) {
				LogWriter.writeLog(
					"Exception " + e.toString() + " removing alpha from JPEG image");
			}
		}
		
		return image;
	}
	
	/**
	 * convert a BufferedImage to RGB colourspace (used when I clip the image)
	 */
	final public static BufferedImage convertToARGB(BufferedImage image) {

		//don't bother if already rgb
		if (image.getType() != BufferedImage.TYPE_INT_ARGB) {
			try {
				BufferedImage raw_image = image;
				image =
					new BufferedImage(
						raw_image.getWidth(),
						raw_image.getHeight(),
						BufferedImage.TYPE_INT_ARGB);
				ColorConvertOp xformOp = new ColorConvertOp(null);
				xformOp.filter(raw_image, image);
			} catch (Exception e) {
				LogWriter.writeLog("Exception " + e + " creating argb image");
			}
		}
		return image;
	}

	/**
	 * save raw CMYK data by converting to RGB using algorithm method -  
	 * pdfsages supplied the C source and I have converted - 
	 * This works very well on most colours but not dark shades which are 
	 * all rolled into black 
	 * 
	 * This is what xpdf seems to use - 
	 * <b>Note</b> we store the output data in our input queue to reduce memory 
	 * usage - we have seen raw 2000 * 2000 images and having input and output 
	 * buffers is a LOT of memory - 
	 * I have kept the doubles in as I just rewrote Leonard's code -  
	 * I haven't really looked at optimisation beyond memory issues
	 */
	public static BufferedImage algorithmicConvertCMYKImageToRGB(
		byte[] buffer,
		int w,
		int h) {

		BufferedImage image = null;
		byte[] new_data = new byte[w * h * 3];

		int pixelCount = w * h*4;
			
		double lastC=-1,lastM=-1.12,lastY=-1.12,lastK=-1.21;
		double x=255;
		
		new_data = new byte[w * h * 3];
		
		double c, m, y, aw, ac, am, ay, ar, ag, ab;
		double outRed=0, outGreen=0, outBlue=0;
	
		int pixelReached = 0;
		for (int i = 0; i < pixelCount; i = i + 4) {
			
			double inCyan = (buffer[i]&0xff)/x ;
			double inMagenta = (buffer[i + 1]&0xff) / x;
			double inYellow = (buffer[i + 2]&0xff) / x;
			double inBlack = (buffer[i + 3]&0xff) / x;
		
			if((lastC==inCyan)&&(lastM==inMagenta)&&
			        (lastY==inYellow)&&(lastK==inBlack)){
				 //use existing values   
				}else{//work out new
					double k = 1;
					c = clip01(inCyan + inBlack);
					m = clip01(inMagenta + inBlack);
					y = clip01(inYellow + inBlack);
					aw = (k - c) * (k - m) * (k - y);
					ac = c * (k - m) * (k - y);
					am = (k - c) * m * (k - y);
					ay = (k - c) * (k - m) * y;
					ar = (k - c) * m * y;
					ag = c * (k - m) * y;
					ab = c * m * (k - y);
					outRed = x*clip01(aw + 0.9137 * am + 0.9961 * ay + 0.9882 * ar);
					outGreen = x*clip01(aw + 0.6196 * ac + ay + 0.5176 * ag);
					outBlue =
						x*clip01(
							aw
								+ 0.7804 * ac
								+ 0.5412 * am
								+ 0.0667 * ar
								+ 0.2118 * ag
								+ 0.4863 * ab);
					
					lastC=inCyan;
					lastM=inMagenta;
			        	lastY=inYellow;
			        	lastK=inBlack;
				}
			
				new_data[pixelReached++] =(byte)(outRed);
				new_data[pixelReached++] = (byte) (outGreen);
				new_data[pixelReached++] = (byte) (outBlue);

			}

			try {
				/***/
				int[] b = {0,1,2};
				
				DataBuffer db = new DataBufferByte(new_data, new_data.length);
				image =new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);

				Raster raster =Raster.createInterleavedRaster(db,w,h,w * 3,3,b,null);
				image.setData(raster);
			
			} catch (Exception e) {
				LogWriter.writeLog("Exception " + e + " with 24 bit RGB image");
			}

		return image;
	}	
	
	/**
	 * save raw CMYK data by converting to RGB using algorithm method -
	 * pdfsages supplied the C source and I have converted - 
	 * This works very well on most colours but not dark shades which are 
	 * all rolled into black 
	 *
	 */
	public static BufferedImage algorithmicConvertCMYKImageToRGB(
		DataBuffer buffer,
		int w,
		int h,boolean debug) {

        wasRemoved=false;

        BufferedImage image = null;
		byte[] new_data = new byte[w * h * 4];

		int pixelCount = w * h*4;

        boolean nonTransparent=false;

		int r=0,g=0,b=0;
		double lastC=-1,lastM=-1.12,lastY=-1.12,lastK=-1.21;
		int pixelReached = 0;
		
		for (int i = 0; i < pixelCount; i = i + 4) {
			
			double Y = ((buffer.getElemDouble(i)));
			double Cb = ((buffer.getElemDouble(1+i)));
			double Cr = ((buffer.getElemDouble(2+i)));
			double CENTER = ((buffer.getElemDouble(3+i)));

            double a=255;
            if((lastC==Y)&&(lastM==Cb)&&(lastY==Cr)&&(lastK==CENTER)){
			 //use existing values   
			}else{//work out new

                if (debug)
                    System.out.println(Y + " " + Cb + " " + Cr + " " + CENTER);

                r = checkRange(Y - CENTER + 1.402 * (Cr - 128));
                g = checkRange((Y - CENTER) - 0.34414 * (Cb - 128) - 0.71414 * (Cr - 128));
                b = checkRange((Y - CENTER) + 1.772 * (Cb - 128));
                //r = checkRange(Y + 0.000*(Cb-CENTER) + 1.371*(Cr-CENTER));
                //g = checkRange(Y - 0.336*(Cb-CENTER) - 0.698*(Cr-CENTER));
                //b = checkRange(Y + 1.732*(Cb-CENTER) + 0.000*(Cr-CENTER));


                if ((Y == 255.0) && (Cr == Cb) && (CENTER==0)|| (CENTER==255 && Cr==128)) {

                   // System.out.println(Y + " " + Cb + " " + Cr + " " + CENTER);

                    r = 255;
                    g = 255;
                    b = 255;

                   if(CENTER==255)
                    a =0;
                   else
                    nonTransparent=true;

                }else
                     nonTransparent=true;
                
                if (debug)
                    System.out.println(r+" "+g+" "+b);

				lastC=Y;
				lastM=Cb;
				lastY=Cr;
				lastK=CENTER;
				
			}

            new_data[pixelReached++] =(byte) (r);
			new_data[pixelReached++] = (byte) (g);
			new_data[pixelReached++] = (byte) (b);
            new_data[pixelReached++] = (byte) (a);
                        
			
/**
			buffer.setElemDouble(pixelReached++,x*outRed);
			buffer.setElemDouble(pixelReached++,x*outGreen);
			buffer.setElemDouble(pixelReached++,x*outBlue);
			*/
			}


            if(!nonTransparent){

               wasRemoved=true;
               return null;
            }
        
            try {
				/***/
				int[] bands = {0,1,2,3};
				
				DataBuffer db = new DataBufferByte(new_data, new_data.length);
				image =new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);

				Raster raster =Raster.createInterleavedRaster(db,w,h,w * 4,4,bands,null);
				image.setData(raster);

            } catch (Exception e) {
			    System.out.println(e);
			    e.printStackTrace();
				LogWriter.writeLog("Exception " + e + " with 24 bit RGB image");
			}

		return image;
	}	

	/**
	 * convert a BufferedImage to RGB colourspace
	 */
	final public static BufferedImage convertColorspace(
		BufferedImage image,
		int newType) {

		try {
			BufferedImage raw_image = image;
			image =
				new BufferedImage(
					raw_image.getWidth(),
					raw_image.getHeight(),
					newType);
			ColorConvertOp xformOp = new ColorConvertOp(null);
			xformOp.filter(raw_image, image);
		} catch (Exception e) {
			LogWriter.writeLog("Exception " + e + " converting image");

		}

		return image;
	}
	
	/**convenience method used to check value within bounds*/
	private static int checkRange(double d) {

	    int value=(int) d;
	
		if (value < 0)
			value = 0;

		if (value > 255)
			value = 255;

		return value;
	}	

	/**convenience method used to check value within bounds*/
	private static double clip01(double value) {

		if (value < 0)
			value = 0;

		if (value > 1)
			value = 1;

		return value;
	}	
}
