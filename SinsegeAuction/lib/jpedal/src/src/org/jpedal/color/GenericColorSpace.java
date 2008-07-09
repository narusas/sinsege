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
* PdfColor.java
* ---------------
* (C) Copyright 2002, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
* $Id: GenericColorSpace.java,v 1.11 2005/08/17 08:44:53 markee Exp $
*
* Changes (since 01-Jun-2002)
* --------------------------
*/
package org.jpedal.color;

//standard java
import java.awt.Color;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Iterator;

//<start-13>
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
//<end-13>

//<start-jfl>
import org.jpedal.PdfDecoder;
//<end-jfl>
import org.jpedal.exception.PdfException;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.objects.GraphicsState;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Strip;


/**
 * Provides Color functionality and conversion for pdf
 * decoding
 */
public class GenericColorSpace  implements Cloneable, Serializable {

	/**any intent*/
	protected String intent=null;
	
	/**any pattern maps*/
	protected Map patternValues=null;
	
	/**size for indexed colorspaces*/
	protected int size=0;

	/**holds cmyk values if present*/
	protected float c=-1,y=-1,m=-1,k=-1;
	
	/**matrices for calculating CIE XYZ colour*/
	protected float[] W, G, Ma, B, R;
	
	/**defines rgb colorspace*/
	static protected ColorSpace rgbCS;

	public static final String cb = "<color ";

	public static final String ce = "</color>";

	//ID of colorspace (ie DeviceRGB)
	protected int value = ColorSpaces.DeviceRGB;

	/**conversion Op for translating rasters or images*/
	private static ColorConvertOp CSToRGB = null;

	protected ColorSpace cs;

	protected PdfPaint currentColor = new PdfColor(0,0,0);

	/**rgb colormodel*/
	protected static ColorModel rgbModel = null;

    /**currently does nothing but added so we can introduce
     * profile matching
     */
    private static ICC_Profile ICCProfile=null;

	//flag to show problem with colors
	protected boolean failed=false;


	/**initialise all the colorspaces when first needed */
    private static void initCMYKColorspace() throws PdfException {

        try {

            if(ICCProfile==null){
            rgbModel =
                new ComponentColorModel(
                        rgbCS,
                    new int[] { 8, 8, 8 },
                    false,
                    false,
                    ColorModel.OPAQUE,
                    DataBuffer.TYPE_BYTE);
            }else{
                int compCount=rgbCS.getNumComponents();
                int[] values=new int[compCount];
                for(int i=0;i<compCount;i++)
                    values[i]=8;

                rgbModel =
                new ComponentColorModel( rgbCS,
                    values,
                    false,
                    false,
                    ColorModel.OPAQUE,
                    DataBuffer.TYPE_BYTE);
            }

            /**create CMYK colorspace using icm profile*/
            ICC_Profile p =ICC_Profile.getInstance(GenericColorSpace.class.getResourceAsStream(
            "/org/jpedal/res/cmm/cmyk.icm"));
            ICC_ColorSpace cmykCS = new ICC_ColorSpace(p);

            /**define the conversion. PdfColor.hints can be replaced with null or some hints*/
            CSToRGB = new ColorConvertOp(cmykCS, rgbCS, ColorSpaces.hints);

        } catch (Exception e) {
            LogWriter.writeLog(
                "Exception " + e.getMessage() + " initialising color components");
            throw new PdfException("[PDF] Unable to create CMYK colorspace. Check cmyk.icm in jar file");

        }
    }

	//show if problem and we should default to Alt�
	public boolean isInvalid(){
		return failed;
	}



    //allow user to replace sRGB colorspace
    static{

        if(ICCProfile!=null){
            System.out.println("setup "+ICCProfile);
            rgbCS=new ICC_ColorSpace(ICCProfile);

        }else
            rgbCS=ColorSpace.getInstance(ColorSpace.CS_sRGB);

    }

    /**
	 * get size
	 */
	public int getIndexSize(){
		return size;
	}
	
	/**
	 * get color
	 */
	public PdfPaint getColor()
	{
		return currentColor;
	}
	
	/**return the set Java colorspace*/
	public ColorSpace getColorSpace() {
		return cs;
	}

	public  GenericColorSpace() {
		
		cs=rgbCS;
	}
	
	/**
	 * clone graphicsState
	 */
	final public Object clone()
	{
		Object o = null;
		try
		{
			o = super.clone();
		}
		catch( Exception e ){
		System.out.println(e);
		}

		return o;
	}	
	
	/**any indexed colormap*/
	protected byte[] IndexedColorMap = null;

	/**pantone name if present*/
    public String pantoneName=null;

    /**number of colors*/
	protected int componentCount=3;

	/**handle to graphics state / only set and used by Pattern*/
	protected GraphicsState gs;

	protected int pageHeight;
   
	/**
	 * <p>Convert DCT encoded image bytestream to sRGB</p>
	 * <p>It uses the internal Java classes
	 * and the Adobe icm to convert CMYK and YCbCr-Alpha - the data is still DCT encoded.</p>
	 * <p>The Sun class JPEGDecodeParam.java is worth examining because it contains lots 
	 * of interesting comments</p>
	 * <p>I tried just using the new IOImage.read() but on type 3 images, all my clipping code 
	 * stopped working so I am still using 1.3</p>
	 */
	final protected BufferedImage nonRGBJPEGToRGBImage(
            byte[] data, int w, int h, String decodeArray) {

        boolean isProcessed=false;

        BufferedImage image = null;
		ByteArrayInputStream in = null;


        //<start-13>
		ImageReader iir=null;
		ImageInputStream iin=null;
		//<end-13>

		try {

            if(CSToRGB==null)
            initCMYKColorspace();
            CSToRGB = new ColorConvertOp(cs, rgbCS, ColorSpaces.hints);

            in = new ByteArrayInputStream(data);

            //<start-13>
            int cmykType=getJPEGTransform(data);

            //suggestion from Carol
            Iterator iterator = ImageIO.getImageReadersByFormatName("JPEG");

            while (iterator.hasNext())
            {
                Object o = iterator.next();
                iir = (ImageReader) o;
                if (iir.canReadRaster())
                    break;
            }

            //iir = (ImageReader)ImageIO.getImageReadersByFormatName("JPEG").next();
            ImageIO.setUseCache(false);

            iin = ImageIO.createImageInputStream((in));
            iir.setInput(iin, true);   //new MemoryCacheImageInputStream(in));

            Raster ras=iir.readRaster(0,null);

            //invert
            if(decodeArray!=null){

                if(decodeArray.indexOf("1 0 1 0 1 0 1 0")!=-1){

                        DataBuffer buf=ras.getDataBuffer();

                        int count=buf.getSize();

                        for(int ii=0;ii<count;ii++)
                           buf.setElem(ii,255-buf.getElem(ii));


                }else if(decodeArray.indexOf("0 1 0 1 0 1 0 1")!=-1){//identity
                }else if(decodeArray.indexOf("0.0 1.0 0.0 1.0 0.0 1.0 0.0 1.0")!=-1){//identity
                }else if(decodeArray.length()>0){
                }
            }

            if(cs.getNumComponents()==4){ //if 4 col CMYK of ICC translate

                isProcessed=true;

                try{

                    if(cmykType==2){
                        image = ColorSpaceConvertor.algorithmicConvertCMYKImageToRGB(ras.getDataBuffer(),w,h,false);
                    }else{
                        /**generate the rgb image*/
                        WritableRaster rgbRaster =rgbModel.createCompatibleWritableRaster(w, h);
                        // if(cmykType!=0)
                        CSToRGB.filter(ras, rgbRaster);
                        image =new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
                        image.setData(rgbRaster);

                        //slower in tests
                        //image=new BufferedImage(rgbModel,rgbRaster,false,null);

                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else if(cmykType!=0){

                image=iir.read(0);
                isProcessed=true;

            }


                //<end-13>
            if(!isProcessed){
            /**1.3 version or vanilla version*/
                WritableRaster rgbRaster;

                in = new ByteArrayInputStream(data);

                //access the file
                com.sun.image.codec.jpeg.JPEGImageDecoder decoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGDecoder(in);
                Raster currentRaster = decoder.decodeAsRaster();
                //we have to call regardless to get params

                int colorType = decoder.getJPEGDecodeParam().getEncodedColorID();
                int width = currentRaster.getWidth();
                int height = currentRaster.getHeight();
			 
                if (colorType == 4) { //CMYK
			 
                    rgbRaster =rgbModel.createCompatibleWritableRaster(width, height);
                    CSToRGB.filter(currentRaster, rgbRaster);
                    image =new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
                    image.setData(rgbRaster);

                } else { //type 7 - these seem to crash the new 1.4 IO routines as far as I can see

                    LogWriter.writeLog("COLOR_ID_YCbCrA image");

                    //I reread the image which is inefficient but I can't currently see any alternative...
                    in = new ByteArrayInputStream(data);
                    decoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGDecoder(in);
                    image = decoder.decodeAsBufferedImage();

                    image = ColorSpaceConvertor.convertToRGB(image);

                    // Convert from CMYK now - loses black
                    //image = CSToRGB.filter(image, null);

                }

                /**/
            }

        } catch (Exception ee) {
			image = null;
            ee.printStackTrace();
            LogWriter.writeLog("Couldn't read JPEG, not even raster: " + ee);
        }catch(Error err ){
        	//<start-13>
			//System.out.println("Error="+err);
			if(iir!=null)
				iir.dispose();
			if(iin!=null){
				try {
					iin.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//<end-13>
		}
		
		//<start-13>
		try {
			in.close();
			iir.dispose();
			iin.close();
		} catch (Exception ee) {
			LogWriter.writeLog("Problem closing  " + ee);
		}
		//<end-13>

		return image;

	}		

	
	/**Toms routien to read the image type - you can also use 
	 * int colorType = decoder.getJPEGDecodeParam().getEncodedColorID();
	 */
	final protected  int getJPEGTransform(byte[] data) {
		int xform = 0;

		for (int i=0,imax=data.length-2; i<imax; ) {
			
			int type = data[i+1] & 0xff;	// want unsigned bytes!
			//out_.println("+"+i+": "+Integer.toHexString(type)/*+", len="+len*/);
			i += 2;	// 0xff and type

			if (type==0x01 || (0xd0 <= type&&type <= 0xda)) {

			} else if (type==0xda) {
				i = i + ((data[i]&0xff)<<8) + (data[i+1]&0xff);
				while (true) {
					for ( ; i<imax; i++) if ((data[i]&0xff)==0xff && data[i+1]!=0) break;
					int rst = data[i+1]&0xff;
					if (0xd0 <= rst&&rst <= 0xd7) i+=2; else break;
				}

			} else {
				/*if (0xc0 <= type&&type <= 0xcf) {	// SOF
				 Nf = data[i+7] & 0xff;	// 1, 3=YCbCr, 4=YCCK or CMYK
				 } else*/ if (type == 0xee) {	// Adobe
				 	if (data[i+2]=='A' && data[i+3]=='d' && data[i+4]=='o' && data[i+5]=='b' && data[i+6]=='e') { xform = data[i+13]&0xff; break; }
				 }
				 i = i + ((data[i]&0xff)<<8) + (data[i+1]&0xff);
			}
		}

		return xform;
	}		

	
	public void setIndex(byte[] IndexedColorMap,int size) {

		//		set the data for an object
		this.IndexedColorMap = IndexedColorMap;
		this.size=size;
		
	}

	public void setIndex(String CMap,String name,int count) {

		StringBuffer rawValues = new StringBuffer();
		this.size=count;

        //see if hex or octal values and make a lisr
		if (CMap.startsWith("(\\")) {

			//get out the octal values to hex
			StringTokenizer octal_values =new StringTokenizer(CMap, "(\\)");

			while (octal_values.hasMoreTokens()) {
				int next_value = Integer.parseInt(octal_values.nextToken(), 8);
				String hex_value = Integer.toHexString(next_value);
				//pad with 0 if required
				if (hex_value.length() < 2)
					rawValues.append("0");

				rawValues.append(hex_value);
			}
		} else if (CMap.startsWith("(")) {
			
			//should never happen as remapped in ObjectReader
			
		} else {
			
			//get rest of hex data minus any <>
			if (CMap.startsWith("<"))
				CMap =CMap.substring(1, CMap.length() - 1).trim();
			rawValues = new StringBuffer(CMap);
			
		}
		
		//workout components size
		int total_components = 1;
		if ((name.indexOf("RGB") != -1)|(name.indexOf("ICC") != -1))
			total_components = 3;
		else if (name.indexOf("CMYK") != -1)
			total_components = 4;

		IndexedColorMap = new byte[(count + 1) * total_components];
		
		//make sure no spaces in array
		rawValues=Strip.stripAllSpaces(rawValues);

        //put into lookup array
		for (int entries = 0; entries < count + 1; entries++) {
			for (int comp = 0; comp < total_components; comp++) {
				int p = (entries * total_components * 2) + (comp * 2);

				int col_value =Integer.parseInt(rawValues.substring(p, p + 2),16);
				IndexedColorMap[(entries * total_components) + comp] =(byte) col_value;

			}
		}
    }

	/**
	 * lookup a component for index colorspace
	 */
	protected int getIndexedColorComponent(int count) {
		int value =  255;
		
		if(IndexedColorMap!=null){
			value=IndexedColorMap[count];
	
			if (value < 0)
				value = 256 + value;

		}
		return value;

	}

	/**return indexed COlorMap
		 */
	public byte[] getIndexedMap() {
		return IndexedColorMap;
	}
	
	/**
	 * convert color value to sRGB color
	 */
	public void setColor(String[] value,int operandCount){

	}

    //<start-jfl>
    /**
	 * convert byte[] datastream JPEG to an image in RGB
	 */
	public BufferedImage  JPEGToRGBImage(byte[] data,int w,int h,String decodeArray) {
		
		BufferedImage image = null;
		ByteArrayInputStream bis=null;
		
		try {
		
		
			//<start-13>
			/**1.4 code*/
			//
			bis=new ByteArrayInputStream(data);
			if(PdfDecoder.use13jPEGConversion){
				com.sun.image.codec.jpeg.JPEGImageDecoder decoder =com.sun.image.codec.jpeg.JPEGCodec.createJPEGDecoder(bis);
				image = decoder.decodeAsBufferedImage();
				decoder =null;
			}else{
				ImageIO.setUseCache(false);
				image =ImageIO.read(bis);
			}
			
			if(image!=null)
			image=ColorSpaceConvertor.convertToRGB(image);
		} catch (Exception ee) {
			image = null;
			LogWriter.writeLog("Problem reading JPEG: " + ee);
			ee.printStackTrace();
		}
		
		if(bis!=null){
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		//<end-13>

			com.sun.image.codec.jpeg.JPEGImageDecoder decoder =com.sun.image.codec.jpeg.JPEGCodec.createJPEGDecoder(new ByteArrayInputStream(data));
		
			image = decoder.decodeAsBufferedImage();
		}catch(Exception e){
			LogWriter.writeLog("Exception "+e+" with JPEG image");
		}
		/***/
		return image;
	}
    //<end-jfl>

    /**
	 * convert byte[] datastream JPEG to an image in RGB
	 * @throws PdfException 
	 */
	public BufferedImage  JPEG2000ToRGBImage(byte[] data) throws PdfException{
		
		BufferedImage image = null;
		
		//<start-13>
		ByteArrayInputStream in = null;
		
		try {
			in = new ByteArrayInputStream(data);
			
			/**1.4 code*/
			//standard java 1.4 IO
			
			ImageReader iir = (ImageReader)ImageIO.getImageReadersByFormatName("JPEG2000").next();
        //	ImageIO.setUseCache(false);
			ImageInputStream iin = ImageIO.createImageInputStream(in);
			try{	
				iir.setInput(iin, true);   //new MemoryCacheImageInputStream(in));		
				image = iir.read(0);
				iir.dispose();
				iin.close();
				in.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			image=ColorSpaceConvertor.convertToRGB(image);
		} catch (Exception ee) {
			image = null;
            LogWriter.writeLog("Problem reading JPEG 2000: " + ee);
			
			throw new PdfException("Exception with JPEG2000 image - please ensure imageio.jar (from JAI library) on classpath");
			//ee.printStackTrace();
		} catch (Error ee2) {
			image = null;
            ee2.printStackTrace();
            LogWriter.writeLog("Problem reading JPEG 2000: " + ee2);

			throw new PdfException("Error with JPEG2000 image - please ensure imageio.jar (from JAI library) on classpath");
			//ee.printStackTrace();
		}
		
		/**
		//<end-13>
		LogWriter.writeLog("JPEG 2000 requires Java 1.4");
		/***/
		return image;
	}	
	
	/**
	 * convert color content of data to sRGB data
	  */
	public BufferedImage dataToRGB(byte[] data,int w,int h){
		
			int[] bands = {0,1,2};
			DataBuffer db = new DataBufferByte(data, data.length);
			BufferedImage image =new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
			Raster raster =Raster.createInterleavedRaster(db,w,h,w * 3,3,bands,null);
			image.setData(raster);
			
			return image;
	}	
	
	/**
	 * convert image to sRGB image
	  */
	public BufferedImage BufferedImageToRGBImage(BufferedImage image){
		
			return image;
	}	
		
	/**get colorspace ID*/
	public int getID(){
		return value;
	}
		
	/**
	 * create a CIE values for conversion to RGB colorspace
	 */
	final public void setCIEValues(String whitepoint,String blackpoint,String range,String matrix,String gamma) {
		
		/**set to CIEXYZ colorspace*/
		cs = ColorSpace.getInstance(ColorSpace.CS_CIEXYZ);
		
		//set defaults
		float[] R = { -100f,100f, -100.0f, 100.0f };
		float[] W = { 0.0f, 1.0f, 0.0f };
		float[] B = { 0.0f, 0.0f, 0.0f };
		float[] G = { 1.0f, 1.0f, 1.0f };
		float[] Ma = { 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f };

		//whitepoint
		if (whitepoint != null) {
			StringTokenizer matrix_values =new StringTokenizer(whitepoint, "[] ");
			int i = 0;
			while (matrix_values.hasMoreTokens()) {
				W[i] = Float.parseFloat(matrix_values.nextToken());
				i++;
			}
		}

		//blackpoint
		if (blackpoint != null) {
			StringTokenizer matrix_values =new StringTokenizer(blackpoint, "[] ");
			int i = 0;
			while (matrix_values.hasMoreTokens()) {
				B[i] = Float.parseFloat(matrix_values.nextToken());
				i++;
			}
		}

		//range
		if (range != null) {
			StringTokenizer matrix_values =new StringTokenizer(range, "[] ");
			int i = 0;
			while (matrix_values.hasMoreTokens()) {
				R[i] = Float.parseFloat(matrix_values.nextToken());
				i++;
			}
		}
		
		//Matrix
		if (matrix != null) {
			StringTokenizer matrix_values =new StringTokenizer(matrix, "[] ");
			int i = 0;
			while (matrix_values.hasMoreTokens()) {
				Ma[i] = Float.parseFloat(matrix_values.nextToken());
				i++;
			}
		}
		
		//Gamma
		if (gamma != null) {
			StringTokenizer matrix_values =new StringTokenizer(gamma, "[] ");
			int i = 0;
			while (matrix_values.hasMoreTokens()) {
				G[i] = Float.parseFloat(matrix_values.nextToken());
				i++;
			}
		}

		//set values
		this.G = G;
		this.Ma = Ma;
		this.W = W;
		this.B = B;
		this.R = R;
		
	}
	
	/**
	 * convert 4 component index to 3
	  */
	final protected byte[] convert4Index(byte[] data){
		return convertIndex(data,4);
	}
	
	/**
	 * convert 4 component index to 3
	  */
	final protected byte[] convertIndex(byte[] data,int compCount){
		try {

			/**turn it into a BufferedImage so we can convert then extract the data*/
			int width = data.length / compCount;
			int height = 1;
			DataBuffer db = new DataBufferByte(data, data.length);
			int[] bands;
			WritableRaster raster;
			
			int[] bands4 = { 0, 1, 2, 3 };
			int[] bands3 = { 0, 1, 2};
			if(compCount==4)
				bands=bands4;
			else
				bands=bands3;
		
			raster =Raster.createInterleavedRaster(db,width,height,width * compCount,compCount,bands,null);
			
			if(CSToRGB==null)
				initCMYKColorspace();
			CSToRGB = new ColorConvertOp(cs, rgbCS, ColorSpaces.hints);
			
			WritableRaster rgbRaster =
				rgbModel.createCompatibleWritableRaster(width, height);
			
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
	 * convert Index to RGB
	  */
	public byte[] convertIndexToRGB(byte[] index){
		return index;
	}	
	
	/**
	 * get an xml string with the color info
	 */
	public String getXMLColorToken(){
		
		String colorToken="";
		
		//only cal if not set
		if(c==-1){ //approximate
			if(currentColor instanceof Color){
				Color col=(Color)currentColor;
				float c=(255-col.getRed())/255f;
				float m=(255-col.getGreen())/255f;
				float y=(255-col.getBlue())/255f;
				float k=c;
				if(k<m)
					k=m;
				if(k<y)
					k=y;
				
				if(pantoneName==null)
				    colorToken=GenericColorSpace.cb+"C='"+c+"' M='"+m+"' Y='"+y+"' K='"+k+"' >";
				else
				    colorToken=GenericColorSpace.cb+"C='"+c+"' M='"+m+"' Y='"+y+"' K='"+k+"' pantoneName='"+pantoneName+"' >";
			}else{
				colorToken=GenericColorSpace.cb+"type='shading'>";
			}
		}else{
		    if(pantoneName==null)
			    colorToken=GenericColorSpace.cb+"C='"+c+"' M='"+m+"' Y='"+y+"' K='"+k+"' >";
			else
			    colorToken=GenericColorSpace.cb+"C='"+c+"' M='"+m+"' Y='"+y+"' K='"+k+"' pantoneName='"+pantoneName+"' >";
		}
		
		return colorToken;
	}	

	/**
	 * pass in list of patterns
	 */
	public void setPattern(Map currentPatternValues,int pageHeight) {
		this.patternValues=currentPatternValues;
		this.pageHeight=pageHeight;
		//System.out.println("set pattern called");
	}

	/** used by generic decoder to asign color*/
	public void setColor(PdfPaint col) {
		this.currentColor=col;
	}

	/**return number of values used for color (ie 3 for rgb)*/
	public int getColorComponentCount() {
		
		return componentCount;
	}

	/**pattern colorspace needs access to graphicsState*/
	public void setGS(GraphicsState currentGraphicsState) {
		
		this.gs=currentGraphicsState;
		
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}
	
}
