/**
* ===========================================
* Java Pdf Extraction Decoding Access Library
* ===========================================
*
* Project Info:  http://www.jpedal.org
* Project Lead:  Mark Stephens (mark@idrsolutions.com)
*
* (C) Copyright 2005, IDRsolutions and Contributors.
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
* TiffDecoder.java
* ---------------
* (C) Copyright 2005, by IDRsolutions and Contributors.
*
* 
* --------------------------
*/
package org.jpedal.io;

import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.jpedal.utils.LogWriter;


/**
 * converts CCITT stream into either an image of bytestream
 * 
 * Many thanks to Brian Burkhalter for all his help
 */
public class TiffDecoder {

	byte[] bytes;
	
	/**
	 * called with values from PDF
	 * Map contains values from PDF as stream pair
	 */
	public TiffDecoder(int w, int h,Map values,byte[] data){

        //return value
		bytes=null;
		
		/**
		 * get values from stream
		 */
		//flag to show if default is black or white
		boolean isBlack = false;
		//int columns = 1728; //in PDF spec
		int k = 0;
		//boolean isByteAligned=false;  //in PDF spec
		
		//get k (type of encoding)
		String value = (String) values.get("K");
		if (value != null)
			k = Integer.parseInt(value);
	
		/**
		//get flag for white/black as default
		value = (String) values.get("EncodedByteAlign");
		if (value != null)
		    isByteAligned = Boolean.valueOf(value).booleanValue();*/
		
		//get flag for white/black as default
		value = (String) values.get("BlackIs1");
		if (value != null){
			isBlack = Boolean.valueOf(value).booleanValue();
			
		}
		
		/**not used but in Map from PDF
		value = (String) values.get("Rows");
		if (value != null)
		rows = Integer.parseInt(value);	
		
		value = (String) values.get("Columns");
		if (value != null)
		columns= Integer.parseInt(value);	*/
		
		/**
		 * build the image
		 */
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
	    
		/**
		 * tiff header (id, version, offset)
		 * */
		final String[] headerValues={"4d","4d","00","2a","00","00","00","08"};
		for(int i=0;i<headerValues.length;i++)
			bos.write(Integer.parseInt(headerValues[i],16));
		
		int tagCount=9; //appears to be minimum needed
		int stripCount=1; //1 strip with all CCITT data
	    
	    /**
	     * write IFD image file directory
	     */
		writeWord(""+tagCount,bos); //num of directory entries  
	    writeTag("256", "04", "01", ""+w, bos);  /**image width*/  
	    writeTag("257", "04", "01", ""+h, bos); /**image length*/ 
	    writeTag("258", "03", "01", "00010000h", bos); /**BitsPerSample 258 - b&w 1 bit image*/
	    
	    if (k == 0){
	    		writeTag("259", "03", "01", "00030000h", bos); /**compression 259 */
	    }else if (k > 0)
			writeTag("259", "03", "01", "00020000h", bos); /**compression 259 */
		else if (k < 0)
			writeTag("259", "03", "01", "00040000h", bos); /**compression 259 */
		
	    if(!isBlack)
	    		writeTag("262", "03", "01", "00000000h", bos); /**photometricInterpretation 262 */
	    else
	    		writeTag("262", "03", "01", "00010000h", bos); /**photometricInterpretation 262 */
	    
	    writeTag("273", "04", "1","122", bos); /**stripOffsets 273 -start of data after tables */
	    writeTag("277", "03", "01", "00010000h", bos); /**samplesPerPixel 277 */
	    writeTag("278", "04", "01", ""+h, bos); /**rowsPerStrip 278 - uses height */
	    writeTag("279", "04", "1", ""+data.length,bos); /**stripByteCount  279 - 1 strip so all data */
	    writeDWord("0",bos); /** write next IOD offset  zero as no other table*/
	
	    /**
	     * write the CCITT image data at the end
	     */
	    try{
			
	    	bos.write(data);
			bos.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/**setup image */
		try {

			/**write out to debug*
        System.out.println("mac_"+data.length+".tiff");
            java.io.FileOutputStream fos=new java.io.FileOutputStream("mac_"+data.length+".tiff");
			fos.write(bos.toByteArray());
			fos.close();
			/***/
		
			JAIHelper.confirmJAIOnClasspath();

			com.sun.media.jai.codec.ByteArraySeekableStream fss=new com.sun.media.jai.codec.ByteArraySeekableStream(bos.toByteArray());//.wrapInputStream(bis,true);

			javax.media.jai.RenderedOp op = (javax.media.jai.JAI.create("stream",fss));

			Raster raster=op.getData();

			//Raster raster = img2.getData();
			DataBuffer db = raster.getDataBuffer();

			DataBufferByte dbb = (DataBufferByte) db;

			bytes=dbb.getData();

			if(!isBlack){ //invert if needed
				int bcount=bytes.length;
				for(int i=0;i<bcount;i++){
					bytes[i]= (byte) (255-(bytes[i]));
				}
			}

		} catch (Error err) {
			LogWriter.writeLog("[PDF] Tiff error "+err);
		} catch (Exception e1) {
		//	e1.printStackTrace();
		}
	}
	
	
	/**return raw bytes from image*/
	public byte[] getRawBytes(){
		
		
		return  bytes; 
		
	}
	
	/**write word (2 bytes to stream) */
	private void writeWord(String i, ByteArrayOutputStream bos) {
		
		int value=0;
		
		//allow decimal,octal or hex
		if(i.endsWith("h"))
			value=Integer.parseInt(i.substring(0,i.length()-1),16);
		else if(i.endsWith("o"))
			value=Integer.parseInt(i.substring(0,i.length()-1),8);
		else 
			value=Integer.parseInt(i);
		
		bos.write((value>>8)); //high byte
		bos.write(value & 0xFF); //low byte
			
	}
	
	/**write Dword (4 bytes to stream) */
	private void writeDWord(String i, ByteArrayOutputStream bos) {
		
		int value=0;
		
		//allow decimal,octal or hex
		if(i.endsWith("h"))
			value=Integer.parseInt(i.substring(0,i.length()-1),16);
		else if(i.endsWith("o"))
			value=Integer.parseInt(i.substring(0,i.length()-1),8);
		else 
			value=Integer.parseInt(i);
		
		bos.write((value>>24) & 0xff); //high byte
		bos.write((value>>16) & 0xff); 
		bos.write((value>>8) & 0xff); 
		bos.write(value & 0xFF); //low byte
		
	}
	
	/**write a tag to stream*/
	private void writeTag(String TagId, String dataType, String DataCount, String DataOffset, ByteArrayOutputStream bos) {
		
		writeWord(TagId,bos);
		writeWord(dataType,bos);
		writeDWord(DataCount,bos);
		writeDWord(DataOffset,bos);
		
	}
	
}
