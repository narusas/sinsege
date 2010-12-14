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
* PdfObjectReader.java
* ---------------
* (C) Copyright 2002, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

//<start-13>
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

//<end-13>

import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.fonts.StandardFonts;
import org.jpedal.objects.PageLookup;
import org.jpedal.objects.PdfFileInformation;
import org.jpedal.objects.Javascript;
import org.jpedal.utils.LogWriter;

import org.jpedal.utils.Sorts;
import org.jpedal.utils.Strip;
import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.constants.PDFflags;

/**
 * extends PdfFileReader and PdfFilteredFileReader to
 * provide access at object level to data in pdf file
 */
public class PdfReader extends PdfFilteredReader implements PdfObjectReader {

	/**points to stream on disk*/
    private int startStreamPointer,endStreamPointer;
	
    /**used to cache last compressed object*/
    private byte[] lastCompressedStream=null;
    
    //text fields
	private Map fields=new HashMap();
	
    /**location of end ref*/
    private Vector_Int xref=new Vector_Int(100);
    
    /**used to cache last compressed object*/
    private Map lastOffsetStart,lastOffsetEnd;
    
    /**used to cache last compressed object*/
    private int lastFirst;
    
    /**current/last object read*/
    private Map objData=null;
    
    /**names lookup table*/
    private Map nameLookup=new HashMap();
    
    /**allows cache of data so not reread if requested consecutive times*/
    private String lastRef="";

	/**Information object holds information from file*/
	PdfFileInformation currentFileInformation = new PdfFileInformation();

	/**flag to show if extraction allowed*/
	private boolean extractionIsAllowed = true;
	
	private final static byte[] endPattern = { 101, 110, 100, 111, 98, 106 }; //pattern endobj
	
	private final static byte[] endObj = { 32, 111, 98, 106 }; //pattern endobj
	
    private final static byte[] lengthString = { 47, 76, 101, 110, 103, 116, 104}; //pattern /Length
	private final static byte[] startStream = { 115, 116, 114, 101, 97, 109};
	private final static byte[] endStream = { 101, 110, 100, 115, 116, 114, 101, 97,109 };
	
	/**flag to show data encrytped*/
	private boolean isEncrypted = false;
	
	/**flag to show provider read*/
	private boolean isInitialised=false;
	
	/**encryption password*/
	private byte[] encryptionPassword = new byte[ 0 ];
	
	/**info object*/
	private String infoObject=null;
	
	/**key used for encryption*/
	private byte[] encryptionKey=null;
		
	/**flag to show if user can view file*/
	private boolean isFileViewable=true;
	
	/** revision used for encryption*/
	private int rev=0;
	
	/**length of encryption key used*/
	private int keyLength=5;
	
	/**P value in encryption*/
	private int P=0;
	
	/**O value in encryption*/
	private byte[] O=new byte[0];
	
	/**U value in encryption*/
	private byte[] U=new byte[0];
	
	/**holds file ID*/
	private String ID="";
	
	/**flag if password supplied*/
	private boolean isPasswordSupplied=false;
	
	//<start-13>
	/**cipher used for decryption*/
	private Cipher cipher=null;
	//<end-13>
	
	/**encryption padding*/
	private String[] padding={"28","BF","4E","5E","4E","75","8A","41","64","00","4E","56","FF","FA","01","08",
											 "2E","2E","00","B6","D0","68","3E","80","2F","0C","A9","FE","64","53","69","7A"};

	/**length of each object*/
    private int[] ObjLengthTable;

	private boolean refTableInvalid=false;

	final private boolean testOp=false;

	/**size above which objects stored on disk (-1 is off)*/
	private int miniumumCacheSize;
    public boolean interruptRefReading=false;

    // additional values for V4 option
    Map CF;
    
    // additional values for V4 option
	String StrF,EFF,CFM;

    //tell user status on password
    private int passwordStatus=0;

    public PdfReader() {

        //setup a list of fields which are string values
        fields.put("T","x");
        fields.put("TM","x");
        fields.put("TU","x");
        fields.put("CA","x");
        fields.put("R","x");
        fields.put("V","x");
        fields.put("RC","x");
        fields.put("DA","x");
        fields.put("DV","x");
        fields.put("Contents","x");

    }
	/**
	 * read first start ref from last 1024 bytes
	 */
	final public int readFirstStartRef() throws PdfException {

		
		//reset flag
		refTableInvalid=false;
		int pointer = -1;
		int i = 1019;
		StringBuffer startRef = new StringBuffer();

        /**move to end of file and read last 1024 bytes*/
        int block=1024;
        byte[] lastBytes = new byte[block];
        
        try {

            //lose null bytes at end of file
            long end=pdf_datafile.length();
            byte[] buffer=new byte[1];
            movePointer(end-1);
            while(end>0 && buffer[0]==0){
                pdf_datafile.read(buffer);
                end--;
                movePointer(end-1);

            }

            //allow for very small file
			int count=(int)(end - block);

            if(count<0){
				count=0;
                int size=(int)pdf_datafile.length();
                lastBytes=new byte[size];
                i=size+3; //force reset below
            }

            movePointer(count);

			pdf_datafile.read(lastBytes);


        } catch (Exception e) {
			LogWriter.writeLog("Exception " + e + " reading last 1024 bytes");
			throw new PdfException( e + " reading last 1024 bytes");
		}

		//look for tref as end of startxref
		int fileSize=lastBytes.length;

        if(i>fileSize)
			i=fileSize-5;

		while ((i >-1)) {
            if ((lastBytes[i] == 116)
				&& (lastBytes[i + 1] == 120)
				&& (lastBytes[i + 2] == 114)
				&& (lastBytes[i + 3] == 101)
				&& (lastBytes[i + 4] == 102))
				break;


			i--;

		}

		/**trap buggy files*/
		if(i==-1){
			try {
				this.pdf_datafile.close();
			} catch (IOException e1) {
				LogWriter.writeLog("Exception "+e1+" closing file");
			}
			throw new PdfException( "No Startref found in last 1024 bytes!!");
		}

		i = i + 5; //allow for word length

		//move to start of value ignoring spaces or returns
		while ((i < 1024)
			&& ((lastBytes[i] == 10)
				| (lastBytes[i] == 32)
				| (lastBytes[i] == 13)))
			i++;

		//move to start of value ignoring spaces or returns
		while ((i < 1024)
			&& (lastBytes[i] != 10)
			&& (lastBytes[i] != 32)
			&& (lastBytes[i] != 13)) {
			startRef.append((char) lastBytes[i]);
			i++;
		}

		/**convert xref to string to get pointer*/
		if (startRef.length() > 0)
			pointer = Integer.parseInt(startRef.toString());

		if (pointer == -1){
			LogWriter.writeLog("No Startref found in last 1024 bytes!!");
			try {
                this.pdf_datafile.close();
            } catch (IOException e1) {
               LogWriter.writeLog("Exception "+e1+" closing file");
            }
			throw new PdfException( "No Startref found in last 1024 bytes!!");
		}

		return pointer;
	}
	/**set a password for encryption*/
	public void setEncryptionPassword(String password){
        this.encryptionPassword = password.getBytes();
	}

	/**
	 * convert direct values into Map
	 */
	public Map directValuesToMap(String value) {

		Map colorValues=new HashMap();

		int start=value.indexOf("<<");
		int end=value.indexOf(">>");

		String values=value.substring(start+2,end).trim();

		StringTokenizer vals=new StringTokenizer(values,"/");
		while(vals.hasMoreTokens()){
			String nextValue=vals.nextToken();

			int pointer=nextValue.indexOf(" ");
			String key="/"+nextValue.substring(0,pointer);
			String operand=nextValue.substring(pointer+1);
			colorValues.put(key,operand);
		}
		return colorValues;
	}

	/**
	 * turns any refs into String or Map
	 */
	public Object resolveToMapOrString(String command, Object field) {
		/**
		 * convert field into map or string
		 */

		if((fields!=null)&&(fields.get(command)!=null)&&(field instanceof byte[])){

				byte[] fieldBytes=getByteTextStringValue(field,fields);
				field=getTextString(fieldBytes);

		}else if((field instanceof String)&&(((String)field).endsWith(" R"))){
			Object newObj= getObjectValue(field);

			if(newObj instanceof Map){

				Map newField=(Map)newObj;
				/** removed to fix read obj recursive for obj=n=*/
				//newField.put("obj",field); //store name
				this.readStream((String)field,true); //force decode of any streams
				field=newField;

			}else
				field=newObj;

		}//else if(((Map)field).get(command)==null)
			//field=null;

		return field;
	}

    /**
	 * read object data
	  */
    private byte[] readObjectDataTEST(int bufSize,boolean lengthSet){

        //flag so I can setup in situ
        boolean debugCache=false;
        if(!debugCache)
            return readObjectData(bufSize);
        else{

            //recoded version to allow for uncompressed data in stream (ie Rogs file)
            //and fix performance issue

            //it now works, but we have a problem with some streams where there is a block of
            //'clear' data in the stream where Rog has embedded a PDF.
            //Becuase we cannot guarantee the Length setting for the stream is correct, we need to
            //do things the hard way and check all the values :-(

            //the work so far has not been wasted because it has allowed us to get the rest working and we can
            //use it to debug this new code.


            //call old routine which also sets values
            byte[] testData=readObjectData(bufSize);


            //read smaller of bufSize or miniumumCacheSize (if set) into array
            //scan from end to start looking at
            //1. All endobj have a matching obj
            //2. find first stream in data
            //3. find last endstream (or use obj if no endstream (which can happen)

            //if failed to pair on 1., scan forwards looking for match and adjust pointer.
            //alter blocksize from 256 and double if no match

            //if all in 1 stream, set values and validate

            //if not in stream and combined size less than miniumumCacheSize, append onto end


            //test result unchanged and exit if not - it will fail on bugged.pdf as that is where the problem is.

            return null;
        }
    }

    private byte[] readObjectData(int bufSize){

        int rawSize=bufSize,realPos=0;

		final boolean debug=false;

        final boolean debugFaster=false;

        boolean lengthSet=false; //start false and set to true if we find /Length in metadata
		boolean streamFound=false;

		if(debug)
		System.out.println("=============================");

		boolean isDefaultSize=false;
		if(bufSize<1){
			isDefaultSize=true;
			bufSize=128;
		}

		//make sure fits in memory
		if(miniumumCacheSize!=-1 && bufSize>miniumumCacheSize)
			bufSize=miniumumCacheSize;

		//rest cache flags
		startStreamPointer=-1;
		endStreamPointer=-1;

		int charReached = 0,charReached2=0, charReached3=0,startStreamCount=0, endStreamCount=0,bytesAddedToTempStream=0;

		int miniumumCacheSize=this.miniumumCacheSize;

		boolean cacheStream =((this.miniumumCacheSize!=-1)||debugCaching);

		if(debugCaching && miniumumCacheSize==-1){
			miniumumCacheSize=8192;
			this.miniumumCacheSize=miniumumCacheSize;
		}

		int start=0;

		byte[] tempStreamData=null;
		if(cacheStream)
			tempStreamData=new byte[miniumumCacheSize];

		byte[] array=null,buffer=null,dataRead=null;
		boolean inStream=false,inLimits=false,ignoreByte;

		/**adjust buffer if less than bytes left in file*/
		long pointer=0,lastEndStream=-1,objStart=-1;

		/**read in the bytes, using the startRef as our terminator*/
		ByteArrayOutputStream bis= new ByteArrayOutputStream();

		/**read the object or block*/
		try {

			byte currentByte=0,lastByte;

			int i=bufSize-1;
			int offset=-bufSize;

            int blocksRead=0;

            int lastEnd=-1,lastComment=-1;
            
            while (true) {

				i++;

				if(i==bufSize){

                    //cache data and update counter
                    if(!cacheStream){
                        if(blocksRead==1){
                            dataRead=buffer;
                        }else if(blocksRead>1){

                            int bytesRead=dataRead.length;
                            int newBytes=buffer.length;
                            byte[] tmp=new byte[bytesRead+newBytes];

                            //existing data into new array
                            for(int ii=0;ii<bytesRead;ii++)
                                tmp[ii]=dataRead[ii];

                            //data from current block
                            for(int ii=0;ii<newBytes;ii++)
                                tmp[ii+bytesRead]=buffer[ii];

                            dataRead=tmp;
                        }
                        blocksRead++;
                    }


                    //double size of default buffer if lots of reads
					//if((isDefaultSize)&&(bufSize<16384))
					//	bufSize=bufSize*2;

                    /**
                     * read the next block
                     */
                    pointer = this.getPointer();

					/**adjust buffer if less than bytes left in file*/
					if (pointer + bufSize > eof)
						bufSize = (int) (eof - pointer);

					bufSize += 6;
					buffer = new byte[bufSize];

					/**get bytes into buffer*/
					pdf_datafile.read(buffer);

					if(debug)
					System.out.println("--read block");

					offset=offset+i;
					i=0;

                }

				/**write out and look for endobj at end*/
				lastByte=currentByte;
				currentByte = buffer[i];
				ignoreByte=false;
				
				//track comments
				if(currentByte==(int)'%')
					lastComment=realPos;
				
				/**check for endobj at end - reset if not*/
					if (currentByte == endPattern[charReached] &&  !inStream)
						charReached++;
					else
						charReached = 0;
	
					//also scan for <SPACE>obj after endstream incase no endobj
					if(streamFound &&currentByte == endObj[charReached2] &&  !inStream)
						charReached2++;
					else
						charReached2 = 0;

                /**if length not set we go on endstream in data*/
                if(!lengthSet){

                    //also scan for /Length if it had a valid size
					if(rawSize!=-1){
	                    if(!streamFound &&currentByte == lengthString[charReached3] &&  !inStream){
	                        charReached3++;
	                        if(charReached3==6)
	                        lengthSet=true;
	                    }else
	                        charReached3 = 0;
					}

                }

				if(debug)
				System.out.println((pointer+i)+" i="+(i+offset)+" "+currentByte+" "+(char)currentByte);
				/**
				 * if stream can be cached, look for start and see if object outgrows size allowed
				 *
				 * track start and end of stream
				 */

				if((cacheStream)){

					if ((!inStream)&&(currentByte == 62) && ((lastByte == 62)))
						inLimits=false;

					if(!inStream){  //keep look out for start of stream

						//look for start of stream and set inStream true
						if (startStreamCount<6 && currentByte == startStream[startStreamCount] && !inLimits){
							startStreamCount++;
						}else
							startStreamCount=0;

						if((startStreamCount == 6)){ //stream start found so log
							inStream=true;

							streamFound=true;

							//add char which will otherwise be missed off
							if(bis!=null)
								bis.write(currentByte);

							/**
							 * allow for multiple starts in a stream, and only take account
							 * of the first start
							 */
							if(startStreamPointer == -1){
								ignoreByte=true;
	
								startStreamCount=0;
	
								start=i+1;
	
								if (buffer[start] == 13 && buffer[start+1] == 10) //allow for double linefeed
									start+=2;
								else if((buffer[start]==10)|(buffer[start]==13))
									start++;
	
								startStreamPointer=(int) (start+pointer);
							}
							//factor in offset
							start=start+offset;
						}
					}else{ //then end of stream

						//store fist miniumumCacheSize bytes so we can add back if not stored on disk
						if(debugCaching || (bytesAddedToTempStream < miniumumCacheSize)){

							if(debugCaching){//make sure its big enough
								if(bytesAddedToTempStream>=tempStreamData.length){

									byte[] newArray=new byte[bytesAddedToTempStream+1000];
									for(int ii=0;ii<tempStreamData.length;ii++)
										newArray[ii]=tempStreamData[ii];
									tempStreamData=newArray;
								}
							}

							tempStreamData[bytesAddedToTempStream]=currentByte;
							bytesAddedToTempStream++;
							ignoreByte=true;

						}

						//look for end and keep an eye on size
						if (currentByte == endStream[endStreamCount] && !inLimits)
							endStreamCount++;
						else{
							endStreamCount=0;

							//allow for eendstream
							if (currentByte == endStream[endStreamCount] && !inLimits)
								endStreamCount++;
						}
						if(debug)
						System.out.println(endStreamCount+" "+inLimits+" "+currentByte);

						//if end found and not too big, tag onto bis otherwise we just keep locations for later
						if(endStreamCount == 9){
							//inStream=false;
							endStreamCount=0;
							//tag end of stream

							int j=i-9;

							//add back if not being cached
							if((debugCaching || bytesAddedToTempStream < miniumumCacheSize)){

								for(int aa=0;aa<bytesAddedToTempStream;aa++)
								bis.write(tempStreamData[aa]);

								//not cached so reset flags
								if(!debugCaching)
									startStreamPointer=-1;
							}//else

							if(startStreamPointer!=-1)
								endStreamPointer=(int) (j+pointer);

							if(endStreamPointer<startStreamPointer){
								startStreamPointer=-1;
								endStreamPointer=-1;
							}
							inStream=false;
							ignoreByte=true;

						}
					}

					if ((!inStream) &&(currentByte == 60) && ((lastByte == 60)))
						inLimits=true;

				}

				if (charReached == 6 || charReached2==4){
				
					if(!lengthSet)
					break;
					
					charReached=0;
					charReached2=0;
					lastEnd=realPos;
					
				}
					
				if(lengthSet && realPos>=rawSize){
					//System.out.println(realPos+" "+rawSize);
					break;
				}
					
                if((debugFaster || !ignoreByte) && (debugCaching || !cacheStream || !inStream))//|| !inStream)
					bis.write(currentByte);

                realPos++;
			}

            //create byte array to return
            if(cacheStream){
            	bis.close();

                /**get bytes into buffer*/
                array=bis.toByteArray();

            }else{
            	
                if(blocksRead==1){ //scenario 1 - all in first block
                    array=new byte[i];
                    for(int ii=0;ii<i;ii++){
                        array[ii]=buffer[ii];
                    }
                }else{
                    int bytesRead=dataRead.length;

                    array=new byte[bytesRead+i];
                    //existing data
                    for(int ii=0;ii<bytesRead;ii++)
                        array[ii]=dataRead[ii];

                    //data from current block
                    for(int ii=0;ii<i;ii++)
                        array[ii+bytesRead]=buffer[ii];
                }

                if(lengthSet && lastEnd!=-1 && lastComment!=-1 && lastComment>lastEnd){
                	byte[] newArray = new byte[lastEnd];
                    System.arraycopy(array, 0, newArray, 0, (int) lastEnd);
                    array = newArray;
                }
                
                //if(!cacheStream || debugCaching){
                if(debugFaster){
                    bis.close();

                    /**get bytes into buffer*/
                    byte[] testArray=bis.toByteArray();

                    if(array.length!=testArray.length){
                        System.out.println("Different lengths "+array.length+" "+testArray.length);
                        System.exit(1);
                    }

                    int count=array.length;
                    for(int ii=0;ii<count;ii++){
                        if(array[ii]!=testArray[ii]){
                            System.out.println("Different values at "+ii+" >>"+array[ii]+" "+testArray[ii]);
                            System.exit(1);
                        }
                    }
                }
            }

            if(debug)
				System.out.println("cache="+(endStreamPointer-startStreamPointer)+" array="+array.length+" START="+start);

			//System.out.println(startStreamPointer+" "+endStreamPointer+" "+start);
			if((startStreamPointer!=-1)&&(debugCaching))
				verifyCachedData(debugCaching, cacheStream, array, start);

			if(!cacheStream && !lengthSet)
				array = checkEndObject(array, objStart, lastEndStream);

		} catch (Exception e) {
			e.printStackTrace();
			LogWriter.writeLog("Exception " + e + " reading object");
		}

		return array;
	}

    private byte[] checkEndObject(byte[] array, long objStart, long lastEndStream) {
        int ObjStartCount = 0;

        //check if mising endobj
        for (int i = 0; i < array.length - 8; i++) {

            //track endstream and first or second obj
            if ((ObjStartCount < 2) && (array[i] == 32) && (array[i + 1] == 111) &&
                    (array[i + 2] == 98) && (array[i + 3] == 106)) {
                ObjStartCount++;
                objStart = i;
            }
            if ((ObjStartCount < 2) && (array[i] == 101) && (array[i + 1] == 110) &&
                    (array[i + 2] == 100) && (array[i + 3] == 115) &&
                    (array[i + 4] == 116) && (array[i + 5] == 114) &&
                    (array[i + 6] == 101) && (array[i + 7] == 97) && (array[i + 8] == 109))
                lastEndStream = i + 9;
        }

        if ((lastEndStream > 0) && (objStart > lastEndStream)) {
            byte[] newArray = new byte[(int) lastEndStream];
            System.arraycopy(array, 0, newArray, 0, (int) lastEndStream);
            array = newArray;
        }
        return array;
    }
    private void verifyCachedData(boolean debugCaching, boolean cacheStream, byte[] fullArray,int start) throws IOException {

    	try{
        //store non-cached and read cached version from disk to test

        int arrayLength=endStreamPointer-startStreamPointer;

        if(arrayLength<0){

        	System.out.println("Array size negative "+arrayLength);

        	System.out.println(startStreamPointer+" "+endStreamPointer);
        	System.out.println(" ");
        	for(int jj=0;jj<startStreamPointer+30;jj++)
        		System.err.println(jj+" "+(char)fullArray[jj]);
        	System.exit(1);
        }

        byte[] array = new byte[arrayLength];
        this.movePointer(startStreamPointer);
        this.pdf_datafile.read(array);

        //System.out.println("end="+endStreamPointer+"<>"+(endStreamPointer-startStreamPointer));
        //System.out.println(arrayLength+"<>"+(fullArray.length)+" "+start);
        arrayLength=array.length;

        boolean failed=false;
        for(int ii=0;ii<arrayLength;ii++){
            if((fullArray[ii+start]!=array[ii])){
            	//System.out.println(startStreamPointer+"<>"+endStreamPointer);
                System.out.println("X1 Not same value at "+ii+" =="+fullArray[ii+start]+" "+array[ii]);
                failed=true;
                System.exit(1);
            }
        }
        if(failed)
        	System.exit(1);

    	}catch(Exception ee){
    		ee.printStackTrace();
    		System.exit(1);
    	}

    }

    /**
     * read a dictionary object
    */
    public int readDictionary(String objectRef,int level,Map rootObject,int i,byte[] raw,
    		boolean isEncryptionObject,Map textFields,int endPoint){

        boolean preserveTextString=false;

        final boolean debug=false;
        //debug=objectRef.equals("37004 0 R");
        StringBuffer operand=null,key;

        //setup array to hold commands and data
        if(testOp)
            operand=new StringBuffer(255);

        //if(testKey)
            key=new StringBuffer(20);               

        while(true){

            i++;

            if(i>=raw.length || (endPoint!=-1 && i>=endPoint))
                break;        

            //break at end
            if ((raw[i] == 62) && (raw[i + 1] == 62))
                break;

            if ((raw[i] == 101)&& (raw[i + 1] == 110)&& (raw[i + 2] == 100)&& (raw[i + 3] == 111))
                break;

            //handle recursion
            if ((raw[i] == 60) && ((raw[i + 1] == 60))){
                level++;
                i++;

                if(debug)
                System.err.println(level+" << found key="+key+"= nextchar="+raw[i + 1]);

                Map dataValues=new HashMap();
                rootObject.put(key.toString(),dataValues);
                i=readDictionary(objectRef,level,dataValues,i,raw,isEncryptionObject,textFields,endPoint);

                //i++;
                key=new StringBuffer(20);
                level--;
                //allow for >>>> with no spaces
                if ((raw[i] == 62) && (raw[i + 1] == 62))
                i++;

            }else  if ((raw[i] == 47)&&(raw[i+1] == 47)&&(key.length()==0)) { //allow for oddity of //DeviceGray in colorspace
                key=new StringBuffer(' ');
                i++;
            }else  if ((raw[i] == 47)&(key.length()==0)) { //everything from /

                key=new StringBuffer(20);
                i++;

                while (true) { //get key up to space or [ or / or ( or < or carriage return

                    if ((raw[i] == 32)|| (raw[i] == 13) || (raw[i] == 9) || (raw[i] == 10) ||(raw[i] == 91)||(raw[i]==47)||
                            (raw[i]==40)||(raw[i]==60))
                        break;

					if(raw[i]=='#' && 1==2){ //handle any hex values
						//convert  to value
						i++;
						StringBuffer hexStr=new StringBuffer();
						hexStr.append((char)raw[i]);
						i++;
						hexStr.append((char)raw[i]);
						int hexNum=Integer.parseInt(hexStr.toString(),16);

						/**if(hexNum==32)
							key.append('_');
						else*/
							key.append((char)hexNum);
						
					}else{
						key.append((char) raw[i]);
					}
					i++;
				}

				//allow for / /DeviceGray permutation
                if((raw[i] == 32)&&(raw[i-1] == 47))
                    key.append(' ');

                if(debug)
                System.err.println(level+" key="+key+"<");

                //set flag to extract raw text string


                if((textFields!=null)&&(key.length()>0)&&(textFields.containsKey(key.toString()))){
                    preserveTextString=true;
                }else
                    preserveTextString=false;

                if((raw[i]==47)|(raw[i]==40)|(raw[i]==60)| (raw[i] == 91)) //move back cursor
                i--;

            }else if((raw[i]==32)|(raw[i]==13)|(raw[i]==10)){
            }else if((raw[i]==60)&&(preserveTextString)){ //text string <00ff>

                final boolean debug2=true;

                byte[] streamData;
                i++;

                ByteArrayOutputStream bis=null;
                if(debug2)
                bis=new ByteArrayOutputStream();

                int count=0,i2=i;
                /**
                 * workout number of bytes
                 */
                while(true){

                    i2=i2+2;

                    count++;

                    //ignore returns
                    while((raw[i2]==13)|(raw[i2]==10))
                        i2++;

                    if((raw[i2]==62))
                        break;

                }
                streamData=new byte[count];
                count=0;

                /**
                 * convert to values
                 **/
                while(true){

                    StringBuffer newValue=new StringBuffer(2);
                    for(int j=0;j<2;j++){
                        newValue.append((char)raw[i]);
                        i++;
                    }

                    if(debug2)
                    bis.write(Integer.parseInt(newValue.toString(),16));

                    streamData[count]=(byte)Integer.parseInt(newValue.toString(),16);
                    count++;

                    //ignore returns
                    while((raw[i]==13)|(raw[i]==10))
                        i++;

                    if((raw[i]==62))
                        break;

                }

                try{

                    if(debug2)
                    bis.close();

                    if(debug2){
                        byte[] stream2=bis.toByteArray();

                        if(streamData.length!=stream2.length){
                            System.out.println("Different lengths "+streamData.length+" "+stream2.length);
                            System.exit(1);
                        }

                        for(int jj=0;jj<stream2.length;jj++){
                            if(stream2[jj]!=streamData[jj]){
                                System.out.println(jj+" Different values "+stream2[jj]+" "+streamData[jj]);
                                System.exit(1);
                            }
                        }
                    }
                       // streamData=decrypt(streamData,objectRef,null,false);
                    streamData=decrypt(streamData,objectRef, false,null);

                    rootObject.put(key.toString(),streamData); //save pair and reset
                }catch(Exception e){
                    LogWriter.writeLog("[PDF] Problem "+e+" writing text string"+key);
                    e.printStackTrace();
                }

                key = new StringBuffer(20);

                //ignore spaces and returns
            }else if(raw[i]==40){ //read in (value) excluding any returns

                if(preserveTextString){
                    ByteArrayOutputStream bis=new ByteArrayOutputStream();
                    try{
                        if(raw[i+1]!=41){ //trap empty field
                            while(true){

                                i++;
                                boolean isOctal=false;

                                //trap escape
                                if((raw[i]==92)){
                                    i++;

                                    if(raw[i]=='b')
                                        raw[i]='\b';
                                    else if(raw[i]=='n')
                                        raw[i]='\n';
                                    else if(raw[i]=='t')
                                        raw[i]='\t';
                                    else if(raw[i]=='r')
                                        raw[i]='\r';
                                    else if(raw[i]=='f')
                                        raw[i]='\f';
                                    else if(raw[i]=='\\')
                        				raw[i]='\\';
                                    else if(Character.isDigit((char) raw[i])){ //octal
                                        StringBuffer octal=new StringBuffer(3);
                                        for(int ii=0;ii<3;ii++){
                                            octal.append((char)raw[i]);
                                            i++;
                                        }
                                        //move back 1
                                        i--;
                                        isOctal=true;
                                        raw[i]=(byte) Integer.parseInt(octal.toString(),8);
                                    }
                                }

                                //exit at end
                                if((!isOctal)&&(raw[i]==41)&&((raw[i-1]!=92)|((raw[i-1]==92)&&(raw[i-2]==92))))
                                    break;

                                bis.write(raw[i]);

                            }
                        }

                        bis.close();

                        byte[] streamData=bis.toByteArray();

                        streamData=decrypt(streamData,objectRef, false, null);

                        //substitute dest key otherwise write through
                        if(key.toString().equals("Dest")){
                            String destKey=this.getTextString(streamData);
                            rootObject.put(key.toString(),nameLookup.get(destKey)); //save pair and reset
                        }else
                            rootObject.put(key.toString(),streamData); //save pair and reset

                    }catch(Exception e){
                        LogWriter.writeLog("[PDF] Problem "+e+" handling text string"+key);
                    }
                }else if((isEncryptionObject)&&(key.length()==1)&&((key.charAt(0)=='U')|(key.charAt(0)=='O'))){
                    int count=32;

                    ByteArrayOutputStream bis=new ByteArrayOutputStream();
                    while(true){

                        i++;

                        byte next=raw[i];
                        if(next==92){

                            i++;
                            next=raw[i];

                            //map chars correctly
                            if(next==114)
                            	next=13;
                            else if(next==110)
                            	next=10;
                            else if(next==116)
                            	next=9;
                            else if(next==102) // \f
                            	next=12;
                            else if(next==98) // \b
                            	next=8;
                            else if(next>47 && next<58){ //octal
                            	
                            	StringBuffer octal=new StringBuffer(3);
                            	for(int ii=0;ii<3;ii++)
                            		octal.append((char)raw[i+ii]);
                            	
                            	i=i+2; //roll on extra chars
                            	
                            	//substitute
                            	next=(byte)(Integer.parseInt(octal.toString(),8));
                            	
                            }

                        }

                        bis.write(next);

                        count--;

                        if(count==0)
                            break;

                    }
                    try{
                        bis.close();
                        rootObject.put(key.toString(),bis.toByteArray()); //save pair and reset
                    }catch(Exception e){
                        LogWriter.writeLog("[PDF] Problem "+e+" writing "+key);
                    }

                }else{
                    int startValue=i,opPointer=0;
                    boolean inComment=false;
                    while(true){

                        if(!inComment){
                            if (testOp){
                                if((raw[i]==13)|(raw[i]==10))
                                    operand.append(' ');
                                else
                                    operand.append((char)raw[i]);
                            }
                            opPointer++;
                        }
                        if(((raw[i-1]!=92)||(raw[i-2]==92))&&(raw[i]==41))
                            break;

                        i++;

                        if((raw[i]==37)&&(raw[i-1]!=92)) //ignore comments %
                            inComment=true;

                    }

                    inComment=false;
                    int p=0;
                    char[] value=new char[opPointer];
                    while(true){
                        if(!inComment){
                            if((raw[startValue]==13)|(raw[startValue]==10)){
                                value[p]=' ';
                                p++;
                            }else{
                                value[p]=(char)raw[startValue];
                                p++;
                            }
                        }

                        //avoid \\) where \\ causes problems and allow for \\\
                        if((raw[startValue]!=92)&&(raw[startValue-1]==92)&&(raw[startValue-2]==92))
                            raw[startValue-1]=0;

                        if((raw[startValue-1]!=92)&(raw[startValue]==41))
                            break;

                        startValue++;

                        if((raw[startValue]==37)&&(raw[startValue-1]!=92)) //ignore comments %
                            inComment=true;

                    }

                    //save pair and reset
                    String finalOp = String.copyValueOf(value,0,opPointer);
                    if((testOp)&&(!finalOp.equals(operand.toString()))){
                        System.out.println("Different\n"+finalOp+"<\n"+operand);
                        System.exit(1);
                    }
                    
                    if(!finalOp.equals("null"))
                        rootObject.put(key.toString(),finalOp);

                    if(debug)
                        System.err.println(level+" *0 "+key+"=="+finalOp+"<");

                    //reset values
                    if(testOp)
                        operand = new StringBuffer(255);

                }
                key = new StringBuffer(20);
			}else if(raw[i]==91 && isFDF){ //read in [value] excluding any returns


				Map fdfTable=new HashMap();

				//read paired values
				while(true){

					//find <<
					while (raw[i+1]!=60 && raw[i+2]!=60 && raw[i]!=93)
					i++;


					//find >>
					int end=i;
					while (raw[end+1]!=62 && raw[end+2]!=62 && raw[end]!=93)
					end++;

					if(raw[i]==93)
					break;

					Map ref=new HashMap();
					i=readDictionary(objectRef,1,ref,i+2,raw,isEncryptionObject,textFields,end);
				    i--;
					i--;

					String fdfkey=null,value="";
					byte[] pdfFile=getByteTextStringValue(ref.get("T"),ref);
					if(pdfFile!=null)
		    		fdfkey=getTextString(pdfFile);

					pdfFile=getByteTextStringValue(ref.get("V"),ref);
					if(pdfFile!=null)
		    		value=getTextString(pdfFile);

					if(fdfkey!=null)
					fdfTable.put(fdfkey,value);
				}

				rootObject.put(key.toString(),fdfTable);

				//if(debug)
				//System.err.println(level+" *1 "+key+"=="+finalOp+"<");

				key = new StringBuffer(20);

				//reset
				if(testOp)
					operand=new StringBuffer(255);

			}else if(raw[i]==91){ //read in [value] excluding any returns

					int startValue=i,opPointer=0;
                    boolean inComment=false,convertToHex=false;
                    int squareCount=0,count=0;
                    char next=' ',last=' ';
                    boolean containsIndexKeyword=false;

                    while(raw[i]==32){ //ignore any spaces
                        if (testOp) //add in char
                            operand.append((char)raw[i]);
                        opPointer++;
                        i++;
                    }

                    while(true){
                        if(raw[i]==92) //ignore any escapes
                            i++;

                        //check if it contains the word /Indexed
                        if((opPointer>7)&&(!containsIndexKeyword)&&(raw[i-7]=='/')&&(raw[i-6]=='I')&&
                                        (raw[i-5]=='n')&&(raw[i-4]=='d')&&(raw[i-3]=='e')&&(raw[i-2]=='x')&&
                                        (raw[i-1]=='e')&&(raw[i]=='d')){
                                    containsIndexKeyword=true;
                        }

                        //track [/Indexed /xx ()] with binary values in ()
                        if((raw[i]==40)&&(raw[i-1]!=92)&&(containsIndexKeyword)){
                            convertToHex=true;

                            if (testOp)
                                operand.append(" <");
                            opPointer=opPointer+2;
                        }else if(convertToHex){
                            if((raw[i]==41)&&(raw[i-1]!=92)){
                                if (testOp) //add in char
                                    operand.append(">");
                                opPointer++;

                                convertToHex=false;

                            }else{
                                String hex_value = Integer.toHexString((raw[i]) & 255);
                                //pad with 0 if required
                                if (hex_value.length() < 2){
                                    if (testOp) //add in char
                                        operand.append("0");
                                    opPointer++;
                                }

                                if (testOp) //add in char
                                    operand.append(hex_value);
                                opPointer=opPointer+hex_value.length();
                            }
                        }else if(!inComment){
                            if((raw[i]==13)||(raw[i]==10)){
                                if (testOp) //add in char
                                    operand.append(' ');
                                opPointer++;
                            }else{
                                next=(char)raw[i];

                                //put space in [/ASCII85Decode/FlateDecode]
                                if((next=='/')&&(last!=' ')){

                                    //add in char
                                    if (testOp)
                                        operand.append(' ');
                                    opPointer++;
                                }

                                if((next!=' ')&&(last==')')){ //put space in [()99 0 R]

                                    if (testOp) //add in char
                                        operand.append(' ');
                                    opPointer++;
                                }

                                if (testOp) //add in char
                                    operand.append(next);
                                opPointer++;
                                last=next;
                            }
                        }

                        if((raw[i-1]!=92)||((raw[i-1]==92)&&(raw[i-2]==92)&&(raw[i-3]!=92))){ //allow for escape and track [] and ()
                            if(raw[i]==40)
                                count++;
                            else if(raw[i]==41)
                                count--;
                            if(count==0){
                                if(raw[i]==91)
                                    squareCount++;
                                else if(raw[i]==93)
                                    squareCount--;
                                }
                            }

                        if((squareCount==0)&&(raw[i-1]!=92)&&(raw[i]==93))
                        break;

                        i++;

                        //System.err.println(count++);

                        if((raw[i]==37)&&(raw[i-1]!=92)&&(squareCount==0)) //ignore comments %
                        inComment=true;

                    }

                    /**
                     * now extract char array at correct size
                     */
                    char[] value=new char[opPointer*2];
                    int pt=0;
                    i=startValue; //move pointer back to start

                    //ignore any spaces
                    while(raw[i]==32){
                        value[pt]=(char)raw[i];
                        pt++;
                        i++;
                    }

                    //reset defaults
                    inComment=false;
                    convertToHex=false;
                    squareCount=0;
                    count=0;
                    next=' ';
                    last=' ';

                    while(true){
                    	
                    	//if(containsIndexKeyword)
                    	//System.out.println(i+" "+( raw[i] & 255)+" "+(char)(raw[i] & 255)+" "+Integer.toHexString((raw[i]) & 255));

//						check if it contains the word /Indexed
                        if((i>7)&&(!containsIndexKeyword)&&(raw[i-7]=='/')&&(raw[i-6]=='I')&&
                                        (raw[i-5]=='n')&&(raw[i-4]=='d')&&(raw[i-3]=='e')&&(raw[i-2]=='x')&&
                                        (raw[i-1]=='e')&&(raw[i]=='d')){
                                    containsIndexKeyword=true;                               
                        }

                        //track [/Indexed /xx ()] with binary values in ()
                        if((raw[i]==40)&(raw[i-1]!=92)&&(containsIndexKeyword)){
                        	
                        	//find end
                        	int start=i+1,end=i;
                        	while(end<raw.length){
                        		end++;
                        		if((raw[end]==')')&(raw[end-1]!=92))
                        			break;
                        	}

                            //handle escape chars
                            int length=end-start;
                        	byte[] fieldBytes=new byte[length];
                        	
                        	for(int a=0;a<length;a++){
                        		
                        		if(start==end)
                        			break;
                        		
                        		byte b=raw[start];
                                if(b!=92){
                        			fieldBytes[a]=b;
                                }else{

                                    start++;

                                    if(raw[start]=='b')
                                        fieldBytes[a]='\b';
                                    else if(raw[start]=='n')
                                        fieldBytes[a]='\n';
                                    else if(raw[start]=='t')
                                        fieldBytes[a]='\t';
                                    else if(raw[start]=='r')
                                        fieldBytes[a]='\r';
                                    else if(raw[start]=='f')
                                        fieldBytes[a]='\f';
                                    else if(raw[start]=='\\')
                        				fieldBytes[a]='\\';
                                    else if(Character.isDigit((char) raw[start])){ //octal
                                        
                                    	StringBuffer octal=new StringBuffer(3);
                                        for(int ii=0;ii<3;ii++){
                                        	
                                        	//allow for less than 3 digits
                                        	if(raw[start]==92 || raw[start]==')')
                                        		break;
                                        	
                                            octal.append((char)raw[start]);
                                            start++;
                                        }
                                        start--;
                                        //move back 1
                                        fieldBytes[a]=(byte) Integer.parseInt(octal.toString(),8);
                                        
                                    }else{
                                        //start--;
                                        fieldBytes[a]=raw[start];
                                    }
                                }
                        		
                        		start++;
                            }

                            //handle encryption
                            try {
                                fieldBytes=decrypt(fieldBytes,objectRef, false,null);
                            } catch (PdfSecurityException e) {
                                e.printStackTrace();
                            }

                            /**
                             * add to data as hex stream
                             */

                            //start
                            value[pt]=' ';
                            pt++;
                            value[pt]='<';
                            pt++;

                            //data
                            for(int jj=0;jj<length;jj++){
                            String hex_value = Integer.toHexString((fieldBytes[jj] & 255));

                                if (hex_value.length() < 2){ //pad with 0 if required
                                    value[pt]='0';
                                    pt++;
                                }

                                int hCount=hex_value.length();
                                for(int j=0; j<hCount;j++){
                                    value[pt]=hex_value.charAt(j);
                                    pt++;
                                }
                            }

                            //end
                            value[pt]='>';
                            pt++;

                        }else if(!inComment){
                            if((raw[i]==13)|(raw[i]==10)){
                                value[pt]=' ';
                                pt++;
                            }else{
                                next=(char)raw[i];
                                if((next=='/')&&(last!=' ')){ //put space in [/ASCII85Decode/FlateDecode]
                                    value[pt]=' ';
                                    pt++;
                                }
                                if((next!=' ')&&(last==')')){ //put space in [()99 0 R]
                                    value[pt]=' ';
                                    pt++;
                                }
                                value[pt]=next;
                                pt++;

                                last=next;
                            }
                        }

                        if((raw[i-1]!=92)|((raw[i-1]==92)&&(raw[i-2]==92)&&(raw[i-3]!=92))){ //allow for escape and track [] and ()
                            if(raw[i]==40)
                                count++;
                            else if(raw[i]==41)
                                count--;
                            if(count==0){
                                if(raw[i]==91)
                                    squareCount++;
                                else if(raw[i]==93)
                                    squareCount--;
                            }
                        }

                        if((squareCount==0)&&(raw[i-1]!=92)&(raw[i]==93))
                        break;

                        i++;

                        if((raw[i]==37)&&(raw[i-1]!=92)&&(squareCount==0)) //ignore comments %
                        inComment=true;

                    }


                    //save pair and reset
                    String finalOp= String.copyValueOf(value,0,pt);
                    if(testOp){
                        if((!finalOp.equals(operand.toString()))|(pt!=opPointer)){
                            System.out.println("Error"+pt+" "+opPointer);
                            System.out.println("final="+finalOp+"<");
                            System.out.println("="+operand+"<");
                            System.exit(1);
                        }
                    }
                    if(!finalOp.equals("null"))
                        rootObject.put(key.toString(),finalOp);

                    if(debug)
                    System.err.println(level+" *1 "+key+"=="+finalOp+"<");

                    key = new StringBuffer(20);

                    //reset
                    if(testOp)
                        operand=new StringBuffer(255);

                }else if ((raw[i] != 62)&&(raw[i] != 60)&&(key.length()>0)){

                        boolean inComment=false;
                        int startValue=i,opPointer=0;

                        //calculate size of next value
                        while(true){

                            if((raw[i]!=13)&&(raw[i]!=9)&&(raw[i]!=10)&&(!inComment)){

                                if (testOp) //add in char
                                    operand.append((char)raw[i]);
                                opPointer++;
                            }

                            if((raw[i+1]==47)||((raw[i]!=62)&&(raw[i+1]==62)))
                            break;

                            i++;

                            if((raw[i]==37)&&(raw[i-1]!=92)) //ignore comments %
                            inComment=true;
                        }

                        //lose spaces at end, save pair and reset
                        while((opPointer>0)&&((raw[startValue+opPointer-1]==32)||(raw[startValue+opPointer-1]==10)||(raw[startValue+opPointer-1]==13)||
                                (raw[startValue+opPointer-1]==9))){
                            opPointer--;
                        }

                        //get value
                        char[] value=new char[opPointer];
                        opPointer--;
                        int p=0;
                        while(true){

                            if((raw[startValue]!=13)&&(raw[startValue]!=9)&&(raw[startValue]!=10)){
                                //System.out.println(value.length+" "+p+" "+opPointer+" i="+i+" startPt="+startValue+" char="+(char)raw[startValue]+" "+raw[startValue]);
            					value[p]=(char)raw[startValue];
            					p++;
                            }

                            startValue++;
                            if(p>opPointer)
                                break;
                        }

                        String finalOp=String.copyValueOf(value,0,p);
                        
                        //test
                        if((testOp)&&(!finalOp.equals(operand.toString().trim()))){
                                System.out.println(finalOp+"<1>"+operand+"<");
                                System.exit(1);
                        }


/**						//if reference, subsitute with value
                        //System.out.println(key);//+" >"+finalOp+"<"+(opChars[opPointer-1]=='R')+" "+(opChars[opPointer-2]==' ')+" "+(!key.toString().equals("Parent"))+" "+(!key.toString().equals("Next"))+" "+(!key.toString().equals("Prev")));
                        if((convertRefs)&&(opPointer>4)&&(opChars[opPointer-1]=='R')&&(opChars[opPointer-2]==' ')&&
                                (!key.toString().equals("Parent"))&&
                                (!key.toString().equals("Next"))&&
                                (!key.toString().equals("Prev"))&&
                                (!key.toString().equals("Length"))){

                                this.opPointer=0;
                                //System.out.println(key+"="+finalOp);
                                //System.out.println(readObject(finalOp,false, null));
                                rootObject.put(key.toString(),readObject(finalOp,false, null));
                        }else*/ 
                       
                        //map out hex value
                        if(finalOp.indexOf("#")!=-1 && 1==2){
                        	
                        	// System.out.println("a="+finalOp+"<");
//                        	convert  to value
    						int count=finalOp.length();
    						int j=0;
    						char[] chars=finalOp.toCharArray();
    						
    						StringBuffer convertedOp=new StringBuffer();
    						
    						while(j<count){
    							
    							if(chars[j]=='#' && 1==2){
    								
    								StringBuffer hexStr=new StringBuffer();
    								
    								j++;
		    						hexStr.append((char)chars[j]);
		    						
		    						j++;
		    						hexStr.append((char)chars[j]);
		    						
		    						int hexNum=Integer.parseInt(hexStr.toString(),16);
		
		    						/**if(hexNum==32)
		    							convertedOp.append('_');
		    						else*/
		    							convertedOp.append((char)hexNum);
		    								
    							}else{
    								convertedOp.append(chars[j]);
    							}
    							j++;
    						}
    						//System.out.println(finalOp+" "+convertedOp);
    						finalOp=convertedOp.toString();
    						//System.exit(1);
                        }
                        
                        if(!finalOp.equals("null"))
                            rootObject.put(key.toString(),finalOp);
                        
                        
                        if(debug)
                        System.err.println(level+"*2 "+key+"=="+finalOp+"<");
                        key = new StringBuffer(20);

                        //rest to new value
                        if(testOp)
                            operand = new StringBuffer(255);

                }
            }

        if(debug)
            System.err.println("=====Dictionary read");

        return i;

    }

	/**read a stream*/
	final public byte[] readStream(Map objData,String objectRef,boolean cacheValue,boolean decompress,boolean keepRaw)  {

		Object data=objData.get("DecodedStream");
		BufferedOutputStream streamCache=null;
		byte[] stream;
		String cacheName=null;

		boolean isCachedOnDisk = false;

		//decompress first time
		if(data==null){
			stream=(byte[]) objData.get("Stream");

			isCachedOnDisk=objData.get("startStreamOnDisk")!=null &&
				endStreamPointer - startStreamPointer >= 0;

			if(isCachedOnDisk){
				try{
					/**write to disk raw data*/
					File tempFile=File.createTempFile("jpedal",".bin");
					cacheName=tempFile.getAbsolutePath();
					cachedObjects.put(cacheName,"x");
					streamCache=new BufferedOutputStream(new FileOutputStream(tempFile));

					int buffer=8192;
					byte[] bytes;
					int ptr=startStreamPointer,remainingBytes;
					while(true){

						//handle last n bytes of object correctly
						remainingBytes=1+endStreamPointer-ptr;

						if(remainingBytes<buffer)
							buffer=remainingBytes;
						bytes = new byte[buffer];

						//get bytes into buffer
						this.movePointer(ptr);
						this.pdf_datafile.read(bytes);


                        //spool to disk
						streamCache.write(bytes);

						ptr=ptr+buffer;
						if(ptr>=endStreamPointer)
							break;
					}
					streamCache.close();

					File tt=new File(cacheName);

				}
				catch(Exception e){
					e.printStackTrace();
				}

				//decrypt the stream
				try{
					decrypt(null,objectRef, false,cacheName);
				}catch(Exception e){
					e.printStackTrace();
					stream=null;
					LogWriter.writeLog("Exception "+e);
				}

				objData.put("CachedStream",cacheName);
			}

			if(stream!=null){ /**decode and save stream*/

				//decrypt the stream
				try{
					stream=decrypt(stream,objectRef, false,null);
				}catch(Exception e){
					e.printStackTrace();
					stream=null;
					LogWriter.writeLog("Exception "+e);
				}
			}

            if(keepRaw)
                objData.remove("Stream");

            int length=1;

            if((stream!=null)||(isCachedOnDisk)){

				//values for CCITTDecode
				int height=1,width=1;
				String value=(String) objData.get("Height");
				if(value!=null)
					height = Integer.parseInt(value);

				value=(String) objData.get("Width");
				if(value!=null)
					width = Integer.parseInt(value);

				value= getValue((String)objData.get("Length"));
				if(value!=null)
					length= Integer.parseInt(value);

				/**allow for no width or length*/
				if(height*width==1)
					width=length;

				String filter = this.getValue((String) objData.get("Filter"));

				boolean isImageMask=false;
				Object maskFlag=objData.get("ImageMask");
				if((maskFlag!=null)&&(maskFlag.equals("true"))){
					isImageMask=true;
				}

				if ((filter != null)&&(!filter.startsWith("/JPXDecode"))&&
						(!filter.startsWith("/DCT"))){

					try{

                        //ensure ref converted first
                        Object param = objData.get("DecodeParms");
                        if(param!=null && param instanceof String){
                            String ref=(String) param;
                            if(ref.endsWith(" R")){
                                Map paramObj=this.readObject(ref,false,null);
                                objData.put("DecodeParms",paramObj);
                            }
                        }
                        
                        stream =decodeFilters(stream, filter, objData,width,height,true,cacheName);

					}catch(Exception e){
						LogWriter.writeLog("[PDF] Problem "+e+" decompressing stream "+filter);
						stream=null;
						isCachedOnDisk=false; //make sure we return null, and not some bum values
					}

					//stop spurious match down below in caching code
					length=1;
				}else if(stream!=null&&(length!=1)&&(length<stream.length)){

					/**make sure length correct*/
					if(stream.length!=length){
						byte[] newStream=new byte[length];
                        System.arraycopy(stream, 0, newStream, 0, length);

						stream=newStream;
					}
				}
			}

            if((stream!=null)&&(cacheValue))
				objData.put("DecodedStream",stream);

			if((decompress)&&(isCachedOnDisk)){
				int streamLength = (int) new File(cacheName).length();

				byte[] bytes = new byte[streamLength];

				try {
					new BufferedInputStream(new FileInputStream(cacheName)).read(bytes);
				} catch (Exception e) {
					e.printStackTrace();
				}

				/**resize if length supplied*/
				if((length!=1)&&(length<streamLength)){

					/**make sure length correct*/
					byte[] newStream=new byte[length];
                    System.arraycopy(bytes, 0, newStream, 0, length);

                    bytes=newStream;

				}

				if(debugCaching){
					if(bytes.length!=stream.length){

						System.out.println("Problem with sizes in readStream "+bytes.length+" "+stream.length);
						System.exit(1);
					}
				}

				return bytes;
			}

		}else
			stream=(byte[]) data;

		return  stream;
	}

	/**read a stream*/
	final public byte[] readStream(String ref,boolean decompress)  {


		Map currentValues=readObject(ref,false, null);

		return readStream(currentValues,ref,true,decompress,false);
	}

	/**
	 * read an object in the pdf into a Map which can be an indirect or an object,
	 * used for compressed objects
	 *
	 */
	final public Map readObject(int objectRef)  {

	    boolean debug=false,preserveTextString=false;
		objData=new HashMap();

		//any stream
		byte[] stream=null;

		/**read raw object data*/
		try{
			movePointer(objectRef);
		}catch(Exception e){
			LogWriter.writeLog("Exception moving pointer to "+objectRef);
		}

		byte[] raw = readObjectData(-1);

		/**read the object name from the start*/
		StringBuffer pattern=new StringBuffer("obj");
		StringBuffer objectName=new StringBuffer();
		char current,last=' ';
		int matched=0,i=0;
		while(i<raw.length){
		    current=(char)raw[i];

		    //treat returns same as spaces
		    if((current==10)||(current==13))
		            current=' ';

		    if((current==' ')&&(last==' ')){//lose duplicate or spaces
		        matched=0;
		    }else if(current==pattern.charAt(matched)){ //looking for obj at end
		        matched++;
		    }else{
		        matched=0;
		        objectName.append(current);
		    }
		    if(matched==3)
		        break;
		    last=current;
		    i++;
		}
		//add end and put into Map
		objectName.append('R');
		objData.put("Reference",objectName.toString());
		//objData.put("obj",objectName.toString());

		convertObjectBytesToMap(objData,objectName.toString(),false, null, debug, preserveTextString, stream, raw,false);

		lastRef="-1";

		return objData;
	}

	/**
	 * stop cache of last object in readObject
	 *
	 */
	final public void flushObjectCache(){
		lastRef=null;
	}
	
	/**
	 * read an object in the pdf into a Map which can be an indirect or an object
	 *
	 */
	final synchronized public Map readObject(String objectRef,boolean isEncryptionObject, Map textFields)  {

		//System.out.println("Reading="+objectRef);

        /**return if last read otherwise read*/
	    if((lastRef!=null)&&(objectRef.equals(lastRef))){
	        return objData;
	    }else{
	        lastRef=objectRef;
	        
			boolean debug=false,preserveTextString=false;
			objData=new HashMap();

			//objData.put("obj",objectRef);

			//set flag to extract raw text string
			if((textFields!=null)){
				preserveTextString=true;
			}else
				preserveTextString=false;

			if(debug)
			System.err.println("reading objectRef="+objectRef+"<");

			/**allow for indirect*/
			if(objectRef.endsWith("]"))
			objectRef=Strip.removeArrayDeleminators(objectRef);

			if(debug)
				System.err.println("reading objectRef="+objectRef+"< isCompressed="+isCompressed(objectRef));

			boolean isCompressed=isCompressed(objectRef);

			if(objectRef.endsWith(" R")){

				//any stream
				byte[] stream=null,raw=null;

				/**read raw object data*/
				if(isCompressed){

				    int objectID=Integer.parseInt(objectRef.substring(0,objectRef.indexOf(" ")));
				    int compressedID=getCompressedStreamObject(objectRef);
				    String compressedRef=compressedID+" 0 R",startID=null;
				    Map compressedObject,offsetStart=lastOffsetStart,offsetEnd=lastOffsetEnd;
				    int First=lastFirst;
				    byte[] compressedStream;
				    boolean isCached=true; //assume cached

				    //see if we already have values
				    compressedStream=lastCompressedStream;
				    if(lastOffsetStart!=null)
				            startID=(String) lastOffsetStart.get(""+objectID);

				    //read 1 or more streams
				    while(startID==null){
				        isCached=false;
				        try{
							movePointer(compressedRef);
						}catch(Exception e){
							LogWriter.writeLog("Exception moving pointer to "+objectRef);
						}

						raw = readObjectData(this.ObjLengthTable[compressedID]);
						compressedObject=new HashMap();

						convertObjectBytesToMap(compressedObject,objectRef,isEncryptionObject, textFields, debug, preserveTextString, stream, raw,false);

						/**get offsets table see if in this stream*/
						offsetStart=new HashMap();
						offsetEnd=new HashMap();
						First=Integer.parseInt((String) compressedObject.get("First"));
						compressedStream=this.readStream(compressedObject,objectRef,true,true,false);

						extractCompressedObjectOffset(offsetStart, offsetEnd,First, compressedStream);

						startID=(String) offsetStart.get(""+objectID);

						compressedRef=(String) compressedObject.get("Extends");

				    }

				    if(!isCached){
				        lastCompressedStream=compressedStream;
				        lastOffsetStart=offsetStart;
				        lastOffsetEnd=offsetEnd;
				        lastFirst=First;
				    }

				    /**put bytes in stream*/
				    int start=First+Integer.parseInt(startID),end=compressedStream.length;

				    String endID=(String) offsetEnd.get(""+objectID);
				    if(endID!=null){
				        end=First+Integer.parseInt(endID);
				    }

				    //System.out.println(First+" "+startID+" "+start+" "+endID+" "+end);

				    int streamLength=end-start;
        				raw = new byte[streamLength];
        				System.arraycopy(compressedStream, start, raw, 0, streamLength);

				}else{
					try{
						movePointer(objectRef);
					}catch(Exception e){
						LogWriter.writeLog("Exception moving pointer to "+objectRef);
					}
					int pointer=objectRef.indexOf(" ");
					int id=Integer.parseInt(objectRef.substring(0,pointer));

					if((isEncryptionObject)|(refTableInvalid))
						raw=readObjectData(-1);
					else if(ObjLengthTable[id]==0){
						LogWriter.writeLog(objectRef+ " cannot have offset 0");
						raw=new byte[0];
					}else
						raw = readObjectData(ObjLengthTable[id]);

//					if(objectRef.equals("241 0 R"))
//						System.exit(1);
				}

				if(debug)
				System.out.println("convertObjectsToMap");

				if(startStreamPointer!=-1 || raw.length>1)
				convertObjectBytesToMap(objData,objectRef,isEncryptionObject, textFields, debug, preserveTextString, stream, raw,isCompressed);

				if(debug)
					System.out.println("converted");

			}else{

				try {
				//put direct value into array and read
				ByteArrayOutputStream bos=new ByteArrayOutputStream();

				for (int ii=0;ii<objectRef.length();ii++)
					bos.write((byte) objectRef.charAt(ii));

				if(debug)
				System.out.println("reading dictionary");


						bos.close();

				byte[] bytes=bos.toByteArray();

				if(bytes.length>0)
				readDictionary(objectRef,1,objData,0,bytes,isEncryptionObject,textFields,-1);

				//System.out.println("Direct object read "+objectRef+"<<");
				LogWriter.writeLog("Direct object read "+objectRef+"<<");

				} catch (IOException e) {
					e.printStackTrace();
			}


			}

			if(debug)
				System.out.println("object read");


		return objData;
	    }
	}

	/**
	 * read an object in the pdf into a Map which can be an indirect or an object
	 *
	 */
	final synchronized public byte[] readObjectAsByteArray(String objectRef,boolean isEncryptionObject)  {

		byte[] raw=null;

		/**allow for indirect*/
		if(objectRef.endsWith("]"))
		objectRef=Strip.removeArrayDeleminators(objectRef);

		boolean isCompressed=isCompressed(objectRef);

		if(objectRef.endsWith(" R")){

			//any stream
			byte[] stream=null;

			/**read raw object data*/
			if(isCompressed){

			    int objectID=Integer.parseInt(objectRef.substring(0,objectRef.indexOf(" ")));
			    int compressedID=getCompressedStreamObject(objectRef);
			    String compressedRef=compressedID+" 0 R",startID=null;
			    int compressedIndex=getOffsetInCompressedStream(objectRef);
			    Map compressedObject,offsetStart=lastOffsetStart,offsetEnd=lastOffsetEnd;
			    int First=lastFirst;
			    byte[] compressedStream;
			    boolean isCached=true; //assume cached

			    //see if we already have values
			    compressedStream=lastCompressedStream;
			    if(lastOffsetStart!=null)
			            startID=(String) lastOffsetStart.get(""+objectID);

			    //read 1 or more streams
			    while(startID==null){
			        isCached=false;
			        try{
						movePointer(compressedRef);
					}catch(Exception e){
						LogWriter.writeLog("Exception moving pointer to "+objectRef);
					}

					raw = readObjectData(this.ObjLengthTable[compressedID]);
					compressedObject=new HashMap();
					convertObjectBytesToMap(compressedObject,objectRef,isEncryptionObject, null, false, false, stream, raw,false);

					/**get offsets table see if in this stream*/
					offsetStart=new HashMap();
					offsetEnd=new HashMap();
					First=Integer.parseInt((String) compressedObject.get("First"));
					compressedStream=this.readStream(compressedObject,objectRef,true,true,false);

					extractCompressedObjectOffset(offsetStart, offsetEnd,First, compressedStream);

					startID=(String) offsetStart.get(""+objectID);

					compressedRef=(String) compressedObject.get("Extends");

			    }

			    if(!isCached){
			        lastCompressedStream=compressedStream;
			        lastOffsetStart=offsetStart;
			        lastOffsetEnd=offsetEnd;
			        lastFirst=First;
			    }

			    /**put bytes in stream*/
			    int start=First+Integer.parseInt(startID),end=compressedStream.length;
			    String endID=(String) offsetEnd.get(""+objectID);
			    if(endID!=null)
			        end=First+Integer.parseInt(endID);

			    int streamLength=end-start;
    				raw = new byte[streamLength];
    				System.arraycopy(compressedStream, start, raw, 0, streamLength);

			}else{
				try{
					movePointer(objectRef);
				}catch(Exception e){
					LogWriter.writeLog("Exception moving pointer to "+objectRef);
				}
				int pointer=objectRef.indexOf(" ");
				int id=Integer.parseInt(objectRef.substring(0,pointer));
				if((isEncryptionObject)|(refTableInvalid))
					raw=readObjectData(-1);
				else
					raw = readObjectData(ObjLengthTable[id]);
			}

		}else{

			//put direct value into array and read
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			for (int ii=0;ii<objectRef.length();ii++)
				bos.write((byte) objectRef.charAt(ii));

			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			raw=bos.toByteArray();

		}

		return raw;

	}

	/**
	 * @param First
	 * @param compressedStream
	 */
	private void extractCompressedObjectOffset(Map offsetStart, Map offsetEnd,int First, byte[] compressedStream) {

	    String lastKey=null,key=null,offset=null;

	    final boolean debug=true;
	    StringBuffer rawKey=null,rawOffset=null;
	    int startKey=0,endKey=0,startOff=0,endOff=0;

	    //read the offsets table
	    for(int ii=0;ii<First;ii++){

	    	if(debug){
	    		rawKey=new StringBuffer();
	    		rawOffset=new StringBuffer();
	    	}

	    	/**work out key size*/
	    	startKey=ii;
	        while(compressedStream[ii]!=32){
	        	if(debug)
	        		rawKey.append((char)compressedStream[ii]);
	            ii++;
	    	}
	        endKey=ii-1;

	        /**extract key*/
	        int length=endKey-startKey+1;
			char[] newCommand=new char[length];
			for(int i=0;i<length;i++)
				newCommand[i]=(char)compressedStream[startKey+i];

			key =new String(newCommand);

			/**test key if in debug*/
			if(debug){
				if(!key.equals(rawKey.toString())){
					System.out.println("Different="+key+"<>"+rawKey+"<");
					System.exit(1);
				}
			}

			/**move to offset*/
	        while(compressedStream[ii]==32)
	            ii++;

	        /**get size*/
	        startOff=ii;
	        while((compressedStream[ii]!=32)&&(ii<First)){

	        	if(debug)
	            rawOffset.append((char)compressedStream[ii]);

	            ii++;
	        }
	        endOff=ii-1;

	        /**extract offset*/
	        length=endOff-startOff+1;
			newCommand=new char[length];
			for(int i=0;i<length;i++)
				newCommand[i]=(char)compressedStream[startOff+i];

			offset =new String(newCommand);

			/**test key if in debug*/
			if(debug){
				if(!offset.equals(rawOffset.toString())){
					System.out.println("Different="+offset+"<>"+rawOffset+"<");
					System.exit(1);
				}
			}

	        /**
	         * save values
	         */
	        offsetStart.put(key,offset);

	        //save end as well
	        if(lastKey!=null)
	        	offsetEnd.put(lastKey,offset);

	        lastKey=key;

	    }
	}
    /**
     * converts the bytes from the File into a map object containing the data
     *
     * / =47
     (=40
     )=41
     \=92
     [=91
     ]=93

     */

    private void convertObjectBytesToMap(Map objData,String objectRef,boolean isEncryptionObject, Map textFields,
            boolean debug, boolean preserveTextString,
            byte[] stream, byte[] raw,boolean isCompressed) {

    	/**get values*/
        int i = 0;

        ByteArrayOutputStream rawStringAsBytes=new ByteArrayOutputStream();

    	char remainderLastChar=' ';

    	StringBuffer remainder=new StringBuffer();

        if(!isCompressed){

        	//remove the obj start
	        while (true) {

                if ((raw[i] == 111)&& (raw[i + 1] == 98)&& (raw[i + 2] == 106))
	        			break;
	        	i++;
	        }

	        i = i + 2;

	        //make sure no comment afterwards by rolling onto next CR or < or [ or /
	        while(true){

	           if(raw[i]==47) //allow for command right after obj
	           break;

		        	i++;
		        	//System.out.println(i+" "+(char)raw[i]+" "+raw[i]);
		        	if((raw[i]==10)|(raw[i]==13)|(raw[i]==60)|(raw[i]==91)|(raw[i]==32))
		        	break;
	        }
        }

        if(debug){
        		for(int j=i;j<raw.length - 7;j++)
        			System.err.print((char)raw[j]);

        		System.err.print("<===\n\n");
        }



        //avoid root dictionary object
        //if ((raw[i] == 60) && (raw[i + 1] == 60))
        //i++;

        //allow for immediate command
        if((raw[i]==47)|(raw[i]==91)) //allow for command or array right after obj
            i--;

        //look for trailer keyword
        while (i < raw.length - 7) {

        	i++;

        	if(debug)
        	    System.err.println((char)raw[i]);

            //trap for no endObj
            if(raw[i]=='o' && raw[i+1]=='b' && raw[i+2]=='j')
                    break;

            //read a subdictionary
        	if ((raw[i] == 60) && ((raw[i + 1] == 60)| (raw[i - 1] == 60))){

        		if(raw[i - 1] != 60)
        		i++;

        		if(debug)
        		System.err.println("Read dictionary");
        		i=readDictionary(objectRef,1,objData,i,raw,isEncryptionObject,textFields,-1);

        	/**handle a stream*/
        	}else if ((raw[i] == 115)&& (raw[i + 1] == 116)&& (raw[i + 2] == 114)&& (raw[i + 3] == 101)&& (raw[i + 4] == 97)&& (raw[i + 5] == 109)) {

        		if(debug)
            		System.err.println("Read stream");

        	    //ignore these characters and first return
        		i = i + 6;

        		if (raw[i] == 13 && raw[i+1] == 10) //allow for double linefeed
        			i=i+2;
        		else if((raw[i]==10)|(raw[i]==13))
        			i++;

        		int start = i;

        		i--; //move pointer back 1 to allow for zero length stream

        		int streamLength=0;
        		String setLength=(String)objData.get("Length");
        		if(setLength!=null){
        			//read indirect
        			if(setLength.indexOf(" R")!=-1){
        				/**read raw object data*/
        				try{
        					long currentPos=movePointer(setLength);
        					int buffSize=128;
        					if(currentPos+buffSize>eof)
        						buffSize=(int) (eof-currentPos-1);
        					StringBuffer rawChars=new StringBuffer();
        					byte[] buf=new byte[buffSize];
        					this.pdf_datafile.read(buf);

        					int ii=3;

        					//find start
        					while(true){
        						if((ii<buffSize)&&(buf[ii-3]==111)&&(buf[ii-2]==98)&&(buf[ii-1]==106))
        							break;
        						ii++;
        					}

        					//find first number
        					while(true){
        						if((ii<buffSize)&&(Character.isDigit((char)buf[ii])))
        							break;
        						ii++;
        					}

        					//read number
        					while(true){
        						if((ii<buffSize)&&(Character.isDigit((char)buf[ii]))){
        							rawChars.append((char)buf[ii]);
        							ii++;
        						}else
        							break;
        					}

        					movePointer(currentPos);
        					setLength=rawChars.toString();

        				}catch(Exception e){
        					LogWriter.writeLog("Exception moving pointer to "+objectRef);
        					setLength=null;
        				}
        			}

        			if(setLength!=null){
        				
        				streamLength=Integer.parseInt(setLength);
            			i=start+streamLength;

            			//check validity
            			if ((raw.length>(i+9))&&(raw[i] == 101)&& (raw[i + 1] == 110)&& (raw[i + 2] == 100)&& (raw[i + 3] == 115)&& (raw[i + 4] == 116)
    	        				&& (raw[i + 5] == 114)&& (raw[i + 6] == 101)&& (raw[i + 7] == 97)&& (raw[i + 8] == 109)){

            			}else{
            				boolean	isValid=false;
            				int current=i;
            				//check forwards
            				if(i<raw.length){
	            				while(true){
	            					i++;
	            					if((isValid)||(i==raw.length))
	        	        				break;

	            					if ((raw[i] == 101)&& (raw[i + 1] == 110)&& (raw[i + 2] == 100)&& (raw[i + 3] == 115)&& (raw[i + 4] == 116)
	            	        				&& (raw[i + 5] == 114)&& (raw[i + 6] == 101)&& (raw[i + 7] == 97)&& (raw[i + 8] == 109)){
	            						streamLength=i-start;
	            						isValid=true;
	            					}
	            				}
        					}
            				
            				if(!isValid){
            					i=current;
            					if(i>raw.length)
            						i=raw.length;
            					//check backwords
                				while(true){
                					i--;
                					if((isValid)||(i<0))
            	        				break;
                					if ((raw[i] == 101)&& (raw[i + 1] == 110)&& (raw[i + 2] == 100)&& (raw[i + 3] == 115)&& (raw[i + 4] == 116)
                	        				&& (raw[i + 5] == 114)&& (raw[i + 6] == 101)&& (raw[i + 7] == 97)&& (raw[i + 8] == 109)){
                						streamLength=i-start;
                						isValid=true;
                						
                					}
                				}
            				}

            				if(!isValid)
            					i=current;
            			}
        			}
        		}else{

	        		/**workout length and check if length set*/
	    			int end;

	        		while (true) { //find end

	        			i++;

	        			if(i==raw.length)
	        				break;
	        			if ((raw[i] == 101)&& (raw[i + 1] == 110)&& (raw[i + 2] == 100)&& (raw[i + 3] == 115)&& (raw[i + 4] == 116)
	        				&& (raw[i + 5] == 114)&& (raw[i + 6] == 101)&& (raw[i + 7] == 97)&& (raw[i + 8] == 109))
	        				break;

	        		}

	        		end=i-1;

	        		if((end>start))
	        			streamLength=end-start+1;
        		}
 /**
				if(streamLength>0){
	        		while((raw[start+streamLength-1]==10)||(raw[start+streamLength-1]==13)){
	        			//System.out.println(raw[start+streamLength]);
	        			streamLength--;
	        		}
				}
    */  
        		
        		/**
        		 * either read stream into object from memory or just save position in Map
        		 */
        		if((startStreamPointer==-1) ||(debugCaching)){
		        	
        			if(start+streamLength>raw.length)
		        		streamLength=raw.length-start;

	        		stream = new byte[streamLength];
	        		System.arraycopy(raw, start, stream, 0, streamLength);

        		}
        		
        		if(startStreamPointer!=-1){
        			
        			objData.put("startStreamOnDisk",  new Integer(this.startStreamPointer));
        			objData.put("endStreamOnDisk",  new Integer(this.endStreamPointer));

        			//debug code
        			if(debugCaching){
        				try{
		        			if(start+streamLength>raw.length)
				        		streamLength=raw.length-start;
		        			
			        		byte[] stream2 = new byte[streamLength];
			        		System.arraycopy(raw, start, stream2, 0, streamLength);

			        		int cacheLength=endStreamPointer-startStreamPointer+1;
			        		
			        		//check it matches
			        		int xx=0;
			        		for(int jj=this.startStreamPointer;jj<this.endStreamPointer;jj++){
			        			byte[] buffer = new byte[1];

			    				/**get bytes into buffer*/
			    				this.movePointer(jj);
			    				this.pdf_datafile.read(buffer);

			    				if(buffer[0]!=stream2[xx]){
			    					System.out.println("error here");
			    					System.exit(1);
			    				}
			    				
			    				xx++;
			        		}
			        		
		        			if((cacheLength!=streamLength)){//&& (setLength==null)){
		        				System.out.println("\n");
		        				System.out.println("lengths cache changed="+cacheLength+" array="+streamLength+" set="+setLength);
				
		        				//for(int ii=0;ii<stream2.length;ii++)
		        				//	System.out.println(ii+" "+stream2[ii]+" "+(char)stream2[ii]);
		        				System.exit(1);
		        			}
		        			
        				}catch(Exception e){
        					System.out.println("ERRor in debug code");
        					e.printStackTrace();
        					System.exit(1);
        				}
        			}
        		}

        		i = i + 9; //roll on pointer

        	}else if(raw[i]==91){ //handle just a raw array ie [ /Separation /CMAN /DeviceCMYK 8 0 R]
        		if(debug)
        			System.err.println("read array");

        		i=readArray(objectRef,i,objData,raw,isEncryptionObject,textFields);

        	}else if((raw[i]!=60)&(raw[i]!=62)){ //direct value

        		if(preserveTextString){

        			//allow for all escape combinations
        			if((raw[i-1]==92)&&(raw[i-2]==92)){
        					//stop match on //( or //)
        				rawStringAsBytes.write(raw[i]);
        			}else if(((raw[i]==40)|(raw[i]==41))&(raw[i-1]!=92)){
        						//ignore //
        			}else
        				rawStringAsBytes.write(raw[i]);
        		}

        		if(((raw[i]==10)|(raw[i]==13)|(raw[i]==32))){

        			if(remainder.length()>0)
        				remainder.append(' ');

        			remainderLastChar=' ';

        		}else{

        			/**allow for no spaces in /a/b/c*/
        			if((raw[i]==47)&&(remainderLastChar!=' '))
        				remainder.append(' ');

        			remainderLastChar=(char)raw[i];
        			remainder.append(remainderLastChar);
        		}
        	}

        	/**}else if((raw[i]!=60)&(raw[i]!=62)&(raw[i]!=10)&(raw[i]!=13)&(raw[i]!=32)){ //direct value
        			remainder.append((char)raw[i]);				}*/
        }

        /**strip any comment from remainder*/
        if(remainder.length()>0)	{
        	int ii=remainder.toString().indexOf("%");
        	if(ii>-1)
        		remainder.setLength(ii);
        }

        if(remainder.length()>0)	{
            String rawString=remainder.toString().trim();
        	if((preserveTextString)&&(rawString.startsWith("("))){
        		try {
        			rawStringAsBytes.close();

        		  	byte[] streamData=rawStringAsBytes.toByteArray();
        			streamData=decrypt(streamData,objectRef, false,null);
        	       	objData.put("rawValue",streamData); //save pair and reset

        		} catch (Exception e) {
        			LogWriter.writeLog("Exception "+e+" writing out text string");
        		}

        	}else{
        		if(debug)
        			System.err.println("Remainder value="+remainder+"<<");
        		objData.put("rawValue",rawString);
        	}
        }

        if(stream!=null)
        objData.put("Stream",  stream);

        if(debug)
        System.err.println(objData);

        try {
			rawStringAsBytes.close();
		} catch (IOException e) {
			e.printStackTrace();
    		}
    }
    /**
     * read an array
     */
	private int readArray(String objectRef,int i,Map objData,byte[] raw,boolean isEncryptionObject,Map textFields){


        final boolean debug=false;
        int start=0,end=0;
        boolean maybeKey=false,isSeparation=false;
        //<start-13>
        StringBuffer rawValue=new StringBuffer();
        StringBuffer possKey=new StringBuffer();
        /**<end-13>
        String rawValue="";
        String  possKey="";
        /**/

        boolean containsIndexKeyword=false,convertToHex=false;

        while(true){


            if(debug)
                System.out.println("Raw="+rawValue +"start="+start+" end="+end);

            if(maybeKey){

                int j=i;

                if(debug)
                    System.out.println("Poss key char="+(char)raw[j]);

                //find first valid char
                while((raw[j]==13)|(raw[j]==10)|(raw[j]==32))
                    j++;
 
                if(debug)
                    System.out.println("now="+(char)raw[j]);
                  if((raw[j]==60)&&(raw[j+1]==60)){
               
                    if(debug)
                        System.out.println("Poss key");
                   
                    i=j;
                  
                    if(isSeparation){
                     
                        if(debug)
                            System.out.println("Store in same level "+possKey);
                        
                        //<start-13>
                        rawValue.append(possKey);
                        rawValue.append(' ');
                        /**<end-13>
                        rawValue=rawValue+possKey+" ";
                        /**/
                        i=readDictionary(objectRef,1,objData,i,raw,isEncryptionObject,textFields,-1);

                    }else{
                        Map subDictionary=new HashMap();
                        objData.put(possKey.substring(1),subDictionary);
                        i=readDictionary(objectRef,1,subDictionary,i,raw,isEncryptionObject,textFields,-1);
                       
                        if(debug)
                            System.out.println("Sub dictionary="+subDictionary);
                    }

                    //roll on if needed
                    if(raw[i]==62)
                        i++;
                    //<start-13>
                    possKey=new StringBuffer();
                    /**<end-13>
                    possKey="";
                    /**/
                }else{

                    if(debug)
                        System.out.println("Get value");

                    if(rawValue.charAt(rawValue.length()-1)!=' '){
                    	//<start-13>
                    	rawValue.append(' ');
                    	/**<end-13>
                    	rawValue=rawValue+" ";
                    	/**/
                    }
                    
                    //<start-13>
                    rawValue.append(possKey);
                    rawValue.append(' ');
                    
                    possKey=new StringBuffer();
                    /**<end-13>
                    rawValue=rawValue+possKey+" ";
                    possKey="";
                    /**/
                    
                    maybeKey=false;
                    
                    i--;
                    
                    if(debug)
                        System.out.println("Value="+rawValue);
                  
                }

                //identify possible keys and read

            }else if(!convertToHex && raw[i]==47){

                if(debug)
                    System.out.println("Found /");

                maybeKey=true;
                while(true){
                	//<start-13>
                    possKey.append((char)raw[i]);
                    /**<end-13>
                    possKey=possKey+(char)raw[i];
                    /**/
                    i++;

                    if((raw[i]==47)||(raw[i]==13)||(raw[i]==10)||(raw[i]==32)||(raw[i]==60)||(raw[i]==91)||(raw[i]==93))
                        break;

                }

                //allow for no space as in
                if((raw[i]==47)||(raw[i]==91)||(raw[i]==93)||(raw[i]==60))
                    i--;
                if(debug)
                    System.out.println("Key="+possKey+"<");

                if(possKey.toString().equals("/Separation")){
                    isSeparation=true;
                }else if(possKey.toString().equals("/Indexed"))
                    containsIndexKeyword=true;

                //track [/Indexed /xx ()] with binary values in () and convert to hex string
            }else if((raw[i]==40)&&(raw[i-1]!=92)&&(containsIndexKeyword)){
                convertToHex=true;

                //<start-13>
                rawValue.append(" <");
                /**<end-13>
                rawValue=rawValue+" <";
                /**/

            }else if(convertToHex){ //end of stream
                if((raw[i]==41)&&(raw[i-1]!=92)){
                    //<start-13>
                    rawValue.append(">");
                    /**<end-13>
                    rawValue=rawValue+">";
                    /**/
                    
                    convertToHex=false;
                }else{ //values

                    String hex_value=null;

                    //allow for escaped octal up to 3 chars
                    if(raw[i]=='\\' && raw[i+1]!=13 && raw[i+1]!=10 && raw[i+1]!=114){
                        StringBuffer octal=new StringBuffer(3);
                        int count=0;
                        for(int ii=1;ii<4;ii++){

                            char c=(char)raw[i+1];

                            if(c<48 || c>57)
                            break;
                            
                            octal.append(c);
                            count++;

                            i++;
                        }

                        if(count>0)
                        hex_value=Integer.toHexString(Integer.parseInt(octal.toString(),8));
                    }
                    
                    if(hex_value==null)
                        hex_value = Integer.toHexString((raw[i]) & 255);
                    //pad with 0 if required
                    if (hex_value.length() < 2){
                        //<start-13>
                        rawValue.append('0');
                        /**<end-13>
                        rawValue=rawValue+"0";
                        /**/
                    }
                    //<start-13>
                    rawValue.append(hex_value);
                    /**<end-13>
                    rawValue=rawValue+hex_value;
                    /**/

                }
                //all other cases
            }else{

                //if(debug)
                //	System.out.println("Else"+" "+(char)raw[i]);

                if((i>0)&&(raw[i-1]==47)){ //needed for [/Indexed /DeviceCMYK 60 1 0 R] to get second /
                    
                    //<start-13>
                    rawValue.append('/');
                    /**<end-13>
                    rawValue=rawValue+"/";
                    /**/
                }
           
                if ((raw[i] == 13) | (raw[i] == 10)){ //added as lines split in ghostscript output
                    //<start-13>
                    rawValue.append(' ');
                    /**<end-13>
                    rawValue=rawValue+" ";
                    /**/
                }else {

                    if((raw[i]=='<')&&(raw[i-1]!=' ')){ //make sure < always has a space before it
                        //<start-13>
	                    rawValue.append(' ');
	                    /**<end-13>
	                    rawValue=rawValue+" ";
	                    /**/
                    }
                    if((i>0)&&(raw[i-1]==93)){ //make sure ] always has a space after it
                    	//<start-13>
	                    rawValue.append(' ');
	                    /**<end-13>
	                    rawValue=rawValue+" ";
	                    /**/
                    }

                    //<start-13>
                    rawValue.append((char) raw[i]);
                    /**<end-13>
                    rawValue=rawValue+(char) raw[i];
                    /**/
                }
                if((i==0)||((i>0)&&(raw[i-1]!=92))){
                    if(raw[i]==91)
                        start++;
                    else if(raw[i]==93)
                        end++;
                }
            }

            if((raw[i]==93)&(start==end))
            	break;
            i++;
            
        }
        objData.put("rawValue",rawValue.toString().trim());
        
        if(debug)
            System.out.println(rawValue+"<>"+objData);
        
        return i;
	}

	/**
	 * read FDF
	 */
	final public Map readFDF() throws PdfException{

		int eof=-1,start=-1,end=-1;

		Map objData=new HashMap();

		Map fields=new HashMap();
		fields.put("F","x");
		fields.put("T","x");
		fields.put("V","x");

		try{
	        eof = (int) pdf_datafile.length();

			pdf_datafile.readLine(); //lose first line with definition
			start=(int)pdf_datafile.getFilePointer();

			eof=eof-start;
			byte[] fileData=new byte[eof];
			this.pdf_datafile.read(fileData);

			this.convertObjectBytesToMap(objData,"1 0 R",false,fields,false,true,fileData,fileData,false);

			objData=(Map)objData.get("FDF");
		} catch (Exception e) {
	        try {
                this.pdf_datafile.close();
            } catch (IOException e1) {
               LogWriter.writeLog("Exception "+e+" closing file");
            }

            throw new PdfException("Exception " + e + " reading trailer");
		}

		return objData;
	}

    /**give user access to internal flags such as user permissions*/
    public int getPDFflag(Integer flag) {

        if(flag==PDFflags.USER_ACCESS_PERMISSIONS)
                return P;
        else if(flag==PDFflags.VALID_PASSWORD_SUPPLIED)
            return passwordStatus;
        else
                return -1;
        
    }

    /**flag to show images not displayed until we get JBIG support up and running*/
    public boolean containsJBIG() {
        return containsJBIG;
    }

    /**
	 * read reference table start to see if new 1.5 type or traditional xref
	 * @throws PdfException
	 */
	final public String readReferenceTable() throws PdfException {

	    int pointer = readFirstStartRef(),eof=-1;
	    xref.addElement(pointer);
	    try{
	        eof = (int) pdf_datafile.length();
	    } catch (Exception e) {
	        try {
                this.pdf_datafile.close();
            } catch (IOException e1) {
               LogWriter.writeLog("Exception "+e+" closing file");
            }

            throw new PdfException("Exception " + e + " reading trailer");
		}

        if(pointer>=eof){
			LogWriter.writeLog("Pointer not if file - trying to manually find startref");
			refTableInvalid=true;
			return findOffsets();
		}else if(isCompressedStream(pointer,eof)){
		    return readCompressedStream(pointer,eof);
		}else
		    return readLegacyReferenceTable(pointer,eof);

	}

    /** Utility method used during processing of type1C files */
	final private int getWord(byte[] content, int index, int size) {
		int result = 0;
		for (int i = 0; i < size; i++) {
			result = (result << 8) + (content[index + i] & 0xff);

		}
		return result;
	}

	/**
     * read 1.5 compression stream ref table
	 * @throws PdfException
     */
    private String readCompressedStream(int pointer, int eof)
            throws PdfException {

        String rootObject = "", encryptValue = null, value,Index;
        int current,numbEntries;

        while (pointer != -1) {

            if(interruptRefReading)
                return null;

            /**
             * get values to read stream ref
             */
            Map obj_values = readObject(pointer);
            String ref=(String) obj_values.get("Reference");

            value=(String) obj_values.get("Index");
            if(value==null){
                current=0;
                numbEntries=Integer.parseInt((String)obj_values.get("Size"));
            }else{
                Index=Strip.removeArrayDeleminators(value);
                StringTokenizer values=new StringTokenizer(Index);
                current=Integer.parseInt(values.nextToken());
                numbEntries=Integer.parseInt(values.nextToken());
            }

            //read the field sizes
            value=(String) obj_values.get("W");
            int[] fieldSizes=new int[3];
            StringTokenizer  values=new StringTokenizer(Strip.removeArrayDeleminators(value));
            for(int i=0;i<3;i++)
                fieldSizes[i]=Integer.parseInt(values.nextToken());

            //now read the xrefs stream
            byte[] xrefs=this.readStream(obj_values,ref,true,true,false);

            //now parse the stream and extract values
            int pntr=0;
            int[] defaultValue={1,0,0};
            for(int i=0;i<numbEntries;i++){

                //read the next 3 values
                int[] nextValue=new int[3];
                for(int ii=0;ii<3;ii++){
                		if(fieldSizes[ii]==0){
                			nextValue[ii]=defaultValue[ii];
                		}else{
                			nextValue[ii]=getWord(xrefs,pntr,fieldSizes[ii]);
                			pntr=pntr+fieldSizes[ii];
                		}

                }

                //System.out.println(nextValue[0]+" "+nextValue[1]+" "+nextValue[2]);

                //handle values appropriately
                int id=0,gen;
                switch(nextValue[0]){
                		case 0:
                			//current=nextValue[1];
                		   // gen=nextValue[2];

                            //System.out.println("0-Object="+current+" gen="+gen);
                            current++;

                		break;
                		case 1:
                		    id=nextValue[1];
                		    gen=nextValue[2];

                            //System.out.println("1-id="+current+" gen="+gen);

                            storeObjectOffset(current, id, gen,false);
                		    current++;
                		break;
                		case 2:
                		    id=nextValue[1];
                		    gen=nextValue[2];
                		    storeObjectOffset(current, id, gen,true);

                            //System.out.println("2-id="+current+" gen="+gen);

                            current++;

                		break;
                		default:
                		    throw new PdfException("Exception Unsupported Compression mode with value "+nextValue[0]);

                }
            }

            /**
             * now process trailer values - only first set of table values for
             * root, encryption and info
             */
            if (rootObject.length() == 0) {

                value = (String) obj_values.get("Root"); //get root
                if (value != null)
                    rootObject = value;

                /**
                 * handle encryption (currently just looks for Encryption
                 * object)
                 */
                encryptValue = (String) obj_values.get("Encrypt");

                if (encryptValue != null) {
                    ID = (String) obj_values.get("ID"); //get ID object
                    if (ID == null) {
                        ID = "";
                    } else {
                        ID = Strip.removeArrayDeleminators(ID);

                        if (ID.startsWith("<"))
                            ID = ID.substring(1, ID.indexOf(">"));
                    }
                }

                value = (String) obj_values.get("Info"); //get Info object
                if ((value != null)
                        && ((!this.isEncrypted()) | (this.isPasswordSupplied())))
                    infoObject = value;
                else
                    infoObject = null;

            }

            //make sure first values used if several tables and code for prev
            value = (String) obj_values.get("Prev");
            //see if other trailers
            if (value != null) {
                obj_values = new HashMap();
                pointer = Integer.parseInt(value);
            } else
                pointer = -1;
        }

        if(!interruptRefReading)
        calculateObjectLength();

        return rootObject;
    }

    /**
     * test first bytes to see if new 1.5 style table with obj or contains ref
     * @throws PdfException
     */
    private boolean isCompressedStream(int pointer,int eof) throws PdfException {

        boolean isCompressed=false;
        int bufSize = 50,charReached=0;

        final int UNSET=-1;
        final int COMPRESSED=1;
        final int LEGACY=2;
        int type=UNSET;
        byte[] newPattern = "obj".getBytes();
        byte[] oldPattern = "ref".getBytes();

        while (true) {

            /** adjust buffer if less than 1024 bytes left in file */
            if (pointer + bufSize > eof)
                bufSize = eof - pointer;

            if(bufSize<0)
            	bufSize=50;
            byte[] buffer = new byte[bufSize];

            /** get bytes into buffer */
            movePointer(pointer);
            try{
                pdf_datafile.read(buffer);
            } catch (Exception e) {
                try {
                    this.pdf_datafile.close();
                } catch (IOException e1) {
                   LogWriter.writeLog("Exception "+e+" closing file");
                }

                throw new PdfException("Exception " + e + " scanning trailer for ref or obj");
            }

            /**look for xref or obj */
            for (int i = 0; i < bufSize; i++) {

                byte currentByte = buffer[i];

                /** check for xref OR end - reset if not */
                if ((currentByte == oldPattern[charReached])&&(type!=COMPRESSED)){
                    charReached++;
                    type=LEGACY;
                }else if ((currentByte == newPattern[charReached])&&(type!=LEGACY)){
                    charReached++;
                    type=COMPRESSED;
                }else{
                    charReached = 0;
                    type=UNSET;

                }

                //update pointer
                pointer = pointer + bufSize;

                if (charReached == 2)
                    break;

            }

            if(charReached==2)
                break;
        }

        /**
         * throw exception if no match or tell user which type
         */
        if(type==UNSET){
            try {
                this.pdf_datafile.close();
            } catch (IOException e1) {
               LogWriter.writeLog("Exception "+1+" closing file");
            }
            throw new PdfException("Exception unable to find ref or obj in trailer");
        }

        if(type==COMPRESSED)
            return true;
        else
            return false;
    }

	/**
	 * read reference table from file so we can locate
	 * objects in pdf file and read the trailers
	 */
	final private String readLegacyReferenceTable(int pointer,int eof) throws PdfException {
		String rootObject = "", value = "", encryptValue=null;

        int current = 0; //current object number
		byte[] Bytes = null;
		int bufSize = 1024;

		int charReached = 0, endTable = 0;
		byte[] pattern = { 37, 37, 69, 79, 70 }; //pattern %%EOF

		Map obj_values = new HashMap();

		/**read and decode 1 or more trailers*/
		while (true) {

            if(interruptRefReading)
            return null;

            try {

				//allow for pointer outside file
				Bytes=readTrailer(bufSize, charReached, pattern, pointer, eof);

			} catch (Exception e) {
				Bytes=null;
				try {
                    this.pdf_datafile.close();
                } catch (IOException e1) {
                   LogWriter.writeLog("Exception "+e+" closing file");
                }
				throw new PdfException("Exception " + e + " reading trailer");
			}

			if (Bytes == null) //safety catch
				break;

			/**get trailer*/
			int i = 0;
			StringBuffer startRef = new StringBuffer();
			StringBuffer key = new StringBuffer();
			StringBuffer operand = new StringBuffer();

			int maxLen=Bytes.length;


			//for(int a=0;a<100;a++)
			//	System.out.println((char)Bytes[i+a]);
            while (i <maxLen) {//look for trailer keyword
				if ((Bytes[i] == 116)& (Bytes[i + 1] == 114)& (Bytes[i + 2] == 97)& (Bytes[i + 3] == 105)& (Bytes[i + 4] == 108)& (Bytes[i + 5] == 101)& (Bytes[i + 6] == 114))
					break;

				i++;
			}

			//save endtable position for later
			endTable = i;

			//move to beyond <<
			while ((Bytes[i] != 60) && (Bytes[i + 1] != 60))
				i++;

			i = readRef2(Bytes, obj_values, i, key, operand);

			//System.out.println(obj_values);

            //handle optional XRefStm
            value = (String) obj_values.get("XRefStm"); //get optional XRefStm
            if(value!=null){
                pointer=Integer.parseInt(value);
            }else{ //usual way

                boolean hasRef=true;

                //look for xref as end of startref
                while ((Bytes[i] != 116)&& (Bytes[i + 1] != 120)
                    && (Bytes[i + 2] != 114)&& (Bytes[i + 3] != 101)&& (Bytes[i + 4] != 102)){

                    if(Bytes[i]=='o' && Bytes[i+1]=='b' && Bytes[i+2]=='j'){
                        hasRef=false;
                        break;
                    }
                    i++;
                }

                if(hasRef){

                    i = i + 8;
                    //move to start of value ignoring spaces or returns
                    while ((i < maxLen)&& ((Bytes[i] == 10)| (Bytes[i] == 32)| (Bytes[i] == 13)))
                        i++;

                    //allow for characters between xref and startref
                    while ((i < maxLen)&& (Bytes[i] != 10)&& (Bytes[i] != 32)&& (Bytes[i] != 13)) {
                        startRef.append((char) Bytes[i]);
                        i++;
                    }

                    /**convert xref to string to get pointer*/
                    if (startRef.length() > 0)
                        pointer = Integer.parseInt(startRef.toString());
                }
            }
			if (pointer == -1) {
				LogWriter.writeLog("No startRef");

				/**now read the objects for the trailers*/
			} else if ((Bytes[0] == 120)& (Bytes[1] == 114)& (Bytes[2] == 101)& (Bytes[3] == 102)) { //make sure starts xref

				i = 5;

				//move to start of value ignoring spaces or returns
				while (((Bytes[i] == 10)| (Bytes[i] == 32)| (Bytes[i] == 13)))
					i++;

                current = readXRefs(current, Bytes, endTable, i);
                i=endTable;

				/**now process trailer values - only first set of table values for root, encryption and info*/
				if (rootObject.length() == 0) {

                    value = (String) obj_values.get("Root"); //get root
					if (value != null)
						rootObject = value;

					/**handle encryption (currently just looks for Encryption object)*/
					encryptValue = (String) obj_values.get("Encrypt");

					if (encryptValue != null){
						ID = (String) obj_values.get("ID"); //get ID object
						if(ID==null){
							ID="";
						}else{
							ID=Strip.removeArrayDeleminators(ID);

							if(ID.startsWith("<"))
							ID=ID.substring(1,ID.indexOf(">"));
						}

					}

					value = (String) obj_values.get("Info"); //get Info object
					if ((value != null)&&((!this.isEncrypted())|(this.isPasswordSupplied())))
					    infoObject=value;
					else
					    infoObject=null;

				}

				//make sure first values used if several tables and code for prev
				value = (String) obj_values.get("Prev");
                //see if other trailers
				if (value != null) {
					//reset values for loop
					bufSize = 1024;
					charReached = 0;
					obj_values = new HashMap();
					pointer = Integer.parseInt(value);

					//track ref table so we can work out object length
					xref.addElement(pointer);

				} else
					pointer = -1;

			} else{
				pointer=-1;
				rootObject = findOffsets();
				refTableInvalid=true;
			}
			if (pointer == -1)
				break;
		}

		/**
		 * check offsets
		 */
        if(!interruptRefReading){
            //checkOffsets(validOffsets);
            if(encryptValue!=null)
            readEncryptionObject(encryptValue);

            if(!refTableInvalid )
            calculateObjectLength();
        }

        return rootObject;
	}

	/**
     * precalculate sizes for each object
     */
    private void calculateObjectLength() {

        //add eol to refs as catchall
        try{
            this.xref.addElement( (int) pdf_datafile.length());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }


        //get order list of refs
        int[] xrefs=this.xref.get();
        int xrefCount=xrefs.length;
        int[] xrefID=new int[xrefCount];
        for(int i=0;i<xrefCount;i++)
            xrefID[i]=i;
        xrefID=Sorts.quicksort( xrefs, xrefID );

        //get ordered list of objects in offset order
        int objectCount=offset.getCapacity();
        ObjLengthTable=new int[objectCount];
        int[] id=new int[objectCount];
        int[] offsets=new int[objectCount];

        //read from local copies and pop lookup table
        int[] off=offset.get();
        boolean[] isComp=isCompressed.get();
        for(int i=0;i<objectCount;i++){
        		if(!isComp[i]){
        			offsets[i]=off[i];
        			id[i]=i;
        		}
        }

        id=Sorts.quicksort( offsets, id );

        int i=0;
        //ignore empty values
        while(true){
            try{
            if(offsets[id[i]]!=0)
                break;
            i++;
            }catch(Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }

        /**
         * loop to calc all object lengths
         * */
        int  start=offsets[id[i]],end;

        //find next xref
        int j=0;
        while(xrefs[xrefID[j]]<start+1)
            j++;

        while(i<objectCount-1){

            end=offsets[id[i+1]];
            int objLength=end-start-1;

            //adjust for any xref
            if(xrefs[xrefID[j]]<end){
                objLength=xrefs[xrefID[j]]-start-1;
                while(xrefs[xrefID[j]]<end+1)
                    j++;
            }
            ObjLengthTable[id[i]]=objLength;
            //System.out.println(id[i]+" "+objLength+" "+start+" "+end);
            start=end;
            while(xrefs[xrefID[j]]<start+1)
                j++;
            i++;
        }

        //special case - last object

        ObjLengthTable[id[i]]=xrefs[xrefID[j]]-start-1;
        //System.out.println("*"+id[i]+" "+start+" "+xref+" "+eof);
    }
    /**
     * read table of values
     */
    private int readXRefs( int current, byte[] Bytes, int endTable, int i) {

    	char flag='c';
		int id=0,tokenCount=0;
		int generation=0;
		int lineLen=0;
		int startLine,endLine;

    	int[] breaks=new int[5];
		int[] starts=new int[5];

    	// loop to read all references
    	while (i < endTable) { //exit end at trailer

    		startLine=i;
    		endLine=-1;

    		/**
    		 * read line locations
    		 */
    		//move to start of value ignoring spaces or returns
    		while ((Bytes[i] != 10) & (Bytes[i] != 13)) {
    			//scan for %
    			if((endLine==-1)&&(Bytes[i]==37))
				endLine=i-1;

    			i++;
    		}

    		//set end if no comment
    		if(endLine==-1)
    		endLine=i-1;

    		//strip any spaces
    		while(Bytes[startLine]==32)
    			startLine++;

    		//strip any spaces
    		while(Bytes[endLine]==32)
    			endLine--;

    		i++;

    		/**
    		 * decode the line
    		 */
    		tokenCount=0;
    		lineLen=endLine-startLine+1;

    		if(lineLen>0){

    			//decide if line is a section header or value

    			//first count tokens
    			int lastChar=1,currentChar;
    			for(int j=1;j<lineLen;j++){
    				currentChar=Bytes[startLine+j];

    				if((currentChar==32)&&(lastChar!=32)){
    					breaks[tokenCount]=j;
    					tokenCount++;
    				}else if((currentChar!=32)&&(lastChar==32)){
    					starts[tokenCount]=j;
    				}

    				lastChar=currentChar;
    			}

    			//update numbers so loops work
    			breaks[tokenCount]=lineLen;
    			tokenCount++;

    			if (tokenCount == 2){
    				current=parseInt(startLine,startLine+breaks[0],Bytes);
    			}else {

    				id = parseInt(startLine,startLine+breaks[0],Bytes);
    				generation=parseInt(startLine+starts[1],startLine+breaks[1],Bytes);

    				flag =(char)Bytes[startLine+starts[2]];

    				if ((flag=='n')) { // only add objects in use

    					/**
    					 * assume not valid and test to see if valid
    					 */
    					boolean isValid=false;

    					//get bytes
    					int bufSize=20;

    					//adjust buffer if less than 1024 bytes left in file
    					if (id + bufSize > eof)
    						bufSize = (int) (eof - id);

    					if(bufSize>0){
	    					byte[] buffer = new byte[bufSize];

	    					/** get bytes into buffer */
	    					movePointer(id);

	    					try {
	    						pdf_datafile.read(buffer);

	    						//look for space o b j
	    						for(int ii=4;ii<bufSize;ii++){
	    							if((buffer[ii-3]==32)&&(buffer[ii-2]==111)&&(buffer[ii-1]==98)&&(buffer[ii]==106)){
	    								isValid=true;
	    								ii=bufSize;
	    							}

	    						}

	    						if(isValid){
	    							storeObjectOffset(current, id, generation,false);
	    							xref.addElement( id);
	    						}else{
	    						}
	    					} catch (IOException e) {
	    						e.printStackTrace();
	    					}
    					}

    				}
    				current++; //update our pointer
    			}
    		}
    	}
    	return current;
    }

    private final static int[] powers={1,10,100,1000,10000,100000,1000000,10000000,100000000,
    		1000000000};

    /**
	 * turn stream of bytes into a number
	 */
	private int parseInt(int i, int j, byte[] bytes) {
		int finalValue=0;
		int power=0;
		i--; //decrement  pointer to speed up
		for(int current=j-1;current>i;current--){
			finalValue=finalValue+((bytes[current]-48)*powers[power]);
			//System.out.println(finalValue+" "+powers[power]+" "+current+" "+(char)bytes[current]+" "+bytes[current]);
			power++;
		}
		//System.exit(1);
		return finalValue;
	}

	/**
     * @param Bytes
     * @param obj_values
     * @param i
     * @param key
     * @param operand
    */
    private int readRef2(byte[] Bytes, Map obj_values, int i, StringBuffer key, StringBuffer operand) {
        boolean notID;
        int p;

        //now read key pairs until >>
        while (true) {

        	i++;

        	//exit at closing >>
        	if ((Bytes[i] == 62) && (Bytes[i + 1] == 62))
        		break;

        	if (Bytes[i] == 47) { //everything from /

        		i++;
        		//get key up to space or [
        		while (true) {
        			if ((Bytes[i] == 32) | (Bytes[i] == 91)|(Bytes[i] == 10)|(Bytes[i] == 13)|(Bytes[i]==60))
        				break;

        			key.append((char) Bytes[i]);
        			i++;
        		}

        		if((key.length()==2)&&(key.charAt(0)=='I')&&(key.charAt(1)=='D'))
        			notID=false;
        		else
        			notID=true;

        		//ignore spaces some pdf use ID[ so we trap for [ (char 91)
        		if ((Bytes[i] != 91)) {
        			while (Bytes[i] == 32)
        				i++;
        		}

        		int Oplen=0,brackets=0;

        		int dictCount=0;
        		//get operand up to end
        		while (true) {

        			char c = (char) Bytes[i];

        			if((Bytes[i-1]==60)&&(Bytes[i]==60))
        					dictCount++;

        			if((c=='(')&&(i>0)&&((Bytes[i-1]!=92)|((i>1)&&(Bytes[i-1]==92)&&(Bytes[i-2]!=92))))
        					brackets++;
        			else if((c==')')&&(i>0)&&((Bytes[i-1]!=92)|((i>1)&&(Bytes[i-1]==92)&&(Bytes[i-2]!=92))))
        					brackets++;

        			//System.out.print(c);
        			if((c == 47)&notID&&(dictCount==0)) //allow for no spacing
        			i--;

					//fix for no gap before any keys
					// ie <</ID [(\215Ga"\224\017\225D\264u\203:\007\2579\302) (\215Ga"\224\017\225D\264u\203:\007\2579\302)]/Info 10 0 R/Prev 34185/Root 13 0 R/Size 29>>
					if(!notID && c==']' && Bytes[i+1]==47)
							break;


					//allow for ID=[<3a4614ef17287563abfd439fbb4ad0ae><7d0c03d0767811d8b491000502832bed>]/Pr
        			//also allow for ID[(])()]
        			if((!notID)&&(c==']')&&(brackets==0))
        					break;

        			if ((dictCount==0)&&(((c == 10) | (c == 13) | ((c == 47)&&notID))&&(Oplen>0)))
        				break;

        			//exit at closing >>
        			if ((Bytes[i] == 62) && (Bytes[i + 1] == 62)){
        				if(dictCount==0)
        					break;
        				else
        					dictCount--;
        			}

        			//allow for escape
        			if (c == '\\') {
        				i++;
        				c = (char) Bytes[i];
        			}

        			//avoid returns at start
        			if ((( Bytes[i] == 10) || ( Bytes[i]== 13))&(Oplen==0)){
        			}else{
        				operand.append((char) Bytes[i]);
        				Oplen++;
        			}

        			i++;
        		}

        		operand = removeTrailingSpaces(operand);

        		//save pair and reset - allow for null value
        		String finalValue= operand.toString();
        		if(!finalValue.equals("null")){
        			p=finalValue.indexOf("%");
        			if((p!=-1)&(finalValue.indexOf("\\%")!=p-1))
        			finalValue=finalValue.substring(0,p).trim();

        			obj_values.put(key.toString(),finalValue);

        		}

        		key = new StringBuffer();
        		operand = new StringBuffer();

        	}

        	if ((Bytes[i] == 62) && (Bytes[i + 1] == 62))
        	break;

        }
        return i;
    }
    /**
     */
    private byte[] readTrailer(int bufSize, int charReached, byte[] pattern,
            int pointer, int eof) throws IOException {

    		/**read in the bytes, using the startRef as our terminator*/
		ByteArrayOutputStream bis = new ByteArrayOutputStream();
		
        while (true) {

            /** adjust buffer if less than 1024 bytes left in file */
            if (pointer + bufSize > eof)
                bufSize = eof - pointer;

            byte[] buffer = new byte[bufSize];

            /** get bytes into buffer */
            movePointer(pointer);
            pdf_datafile.read(buffer);
            
            boolean endFound=false;

            /** write out and lookf for startref at end */
            for (int i = 0; i < bufSize; i++) {

                byte currentByte = buffer[i];

                /** check for startref at end - reset if not */
                if (currentByte == pattern[charReached])
                    charReached++;
                else
                    charReached = 0;

                if (charReached == 5){ //located %%EOF and get last few bytes
                	
	                	for (int j = 0; j < i+1; j++) 
	                		bis.write(buffer[j]);
	                	
	                	i = bufSize;
	                	endFound=true;
                	
                }
            }
            
            //write out block if whole block used
            if(!endFound)
            		bis.write(buffer);
            
            //update pointer
            pointer = pointer + bufSize;

            if (charReached == 5)
                break;

        }
        
        bis.close();
        return bis.toByteArray();
		
    }
    /**
     * read the form data from the file
     */
    final public PdfFileInformation readPdfFileMetadata(String ref) {
    
        //read info object (may be defined and object set in different trailers so must be done at end)
    	if((infoObject!=null)&&((!isEncrypted)|(isPasswordSupplied)))
    		readInformationObject(infoObject);
    	
    	//read and set XML value
    	if(ref!=null)
		    currentFileInformation.setFileXMLMetaData(convertStreamToXML(ref).toString(),readStream(ref,true));
    	
    	return currentFileInformation;
    }

	
    /**
     * take object ref for XML in stream, read and return as StringBuffer
     */
    public  StringBuffer convertStreamToXML(String ref) {
        
        StringBuffer XMLObject = null;
        if(ref!=null){
            XMLObject = new StringBuffer();
            
            String line = "";
            BufferedReader mappingStream =null;
            ByteArrayInputStream bis=null;
            
            //get stream of data and read
            try {
                
                byte[] stream = readStream(ref,true);
                bis=new ByteArrayInputStream(stream);
                mappingStream =
                    new BufferedReader(
                            new InputStreamReader(bis));
                
                //read values into lookup table
                if (mappingStream != null) {
                    
                    while (true) {
                        line = mappingStream.readLine();
                        
                        if (line == null)
                            break;
                        
                        //append to XML data
                        if(!Strip.isEmptyLine(line)){
                        	XMLObject.append(line);
                        	XMLObject.append('\n');
                        }
                    }
                }
            } catch (Exception e) {
                LogWriter.writeLog(
                        "Exception " + e + " reading XML object " + ref);	
            }
            
            if(mappingStream!=null){
                try {
                    mappingStream.close();
                    bis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }

        return XMLObject;
    }
    //////////////////////////////////////////////////////////////////////////
	  /**
	   * get value which can be direct or object
	   */
	  final public String getValue(String value) {
		  
		  if ((value != null)&&(value.endsWith(" R"))){ //indirect
		
			  Map indirectObject=readObject(value,false, null);
			  //System.out.println(value+" "+indirectObject);
			  value=(String) indirectObject.get("rawValue");
			
		  }
		  
		  //allow for null as string
		  if(value!=null && value.equals("null"))
			  value=null;
		
		  return value;
	  }
	  
	  /**
	   * get value which can be direct or object
	   */
	  private Object getObjectValue(Object value) {
		
		  Map indirectObject=readObject((String)value,false, fields);
		  
		  //System.out.println(value+" "+indirectObject);
		  int keyCount=indirectObject.size();
		  if(keyCount==1){
			  Object stringValue= indirectObject.get("rawValue");
			  if(stringValue!=null){
				  if(stringValue instanceof String)
					  value=stringValue;
				  else
					  value=this.getTextString((byte[]) stringValue);
				  
			  }else 
				  value=indirectObject;
		  }else
			  value=indirectObject;
			
		
		  return value;
	  }

	  /**
	   * get text value as byte stream which can be direct or object
	   */
	  final public byte[] getByteTextStringValue(Object rawValue,Map fields) {
	  	
	  	if(rawValue instanceof String){
	  		String value=(String) rawValue;
	  		if ((value != null)&&(value.endsWith(" R"))){ //indirect
	  			
	  			Map indirectObject=readObject(value,false, fields);
	  			
	  			rawValue=indirectObject.get("rawValue");
	  			
	  		}else {
                return value.getBytes();
            }
	  	}
	  	
	  	return (byte[]) rawValue;
	  }
	  

		/**
		 * get sub dictionary value which can be direct or object
		 */
		final public Map getSubDictionary(Object  value) {
		
			if(value instanceof String){
				return readObject((String)value,false, null);
			}else{
				return (Map) value;
			}
			
		}
	
	/**remove any trailing spaces at end*/
	private StringBuffer removeTrailingSpaces(StringBuffer operand) {

		/*remove any trailing spaces on operand*/
		int l = operand.length();
		for (int ii = l - 1; ii > -1; ii--) {
			if (operand.charAt(ii) == ' ')
				operand.deleteCharAt(ii);
			else
				ii = -2;
		}

		return operand;

	}
	
	/**convert to String*/
		public static  String convertToString(byte[] byteStream) {

			StringBuffer operand=new StringBuffer();
			
			/*remove any trailing spaces on operand*/
			int l = byteStream.length;
			for (int ii =0; ii<l; ii++) {
				operand.append((char)byteStream[ii]);
			}

			return operand.toString();

		}

	/**
		 * return flag to show if encrypted
		 */
	final public boolean isEncrypted() {
		return isEncrypted;
	}
	
	/**
		 * return flag to show if valid password has been supplied
		 */
	final public boolean isPasswordSupplied() {
		return isPasswordSupplied;
	}

	/**
		 * return flag to show if encrypted
		 */
	final public boolean isExtractionAllowed() {
		return extractionIsAllowed;
	}
	
	/**show if file can be displayed*/
	public boolean isFileViewable() {
		
			return isFileViewable;
	}
	
		  /**
		  * reads the line/s from file which make up an object
		  * includes move
		  */
	final private byte[] decrypt(byte[] data, String ref,
                                 boolean isEncryption, String cacheName) throws PdfSecurityException{
		
		//System.out.println(((isEncrypted)|(isEncryption))+" "+cacheName+" "+streamLength);

		if((isEncrypted)|(isEncryption)){
			//System.err.println("entering decrypt");
			BufferedOutputStream streamCache= null;
			BufferedInputStream bis = null;
			int streamLength=0;
			if(cacheName!=null){
				//rename file
				try {

					streamLength = (int) new File(cacheName).length();
					
					File tempFile2 = File.createTempFile("jpedal",".raw");
					
					cachedObjects.put(tempFile2.getAbsolutePath(),"x");
					//System.out.println(">>>"+tempFile2.getAbsolutePath());
					ObjectStore.copy(cacheName,tempFile2.getAbsolutePath());
					File rawFile=new File(cacheName);
					rawFile.delete();
				
					
					//decrypt
					streamCache = new BufferedOutputStream(new FileOutputStream(cacheName));
					bis=new BufferedInputStream(new FileInputStream(tempFile2));
					
				} catch (IOException e1) {
					LogWriter.writeLog("Exception "+e1+" in decrypt");
				}
			}
			byte[] currentKey=new byte[keyLength];
			
			if(ref.length()>0)
				currentKey=new byte[keyLength+5];
			
			System.arraycopy(encryptionKey, 0, currentKey, 0, keyLength);
			
			try{
				
				if(ref.length()>0){
					int pointer=ref.indexOf(" ");
					int pointer2=ref.indexOf(" ",pointer+1);
					
					int obj=Integer.parseInt(ref.substring(0,pointer));
					int gen=Integer.parseInt(ref.substring(pointer+1,pointer2));
					
					currentKey[keyLength]=((byte)(obj & 0xff));
					currentKey[keyLength+1]=((byte)((obj>>8) & 0xff));
					currentKey[keyLength+2]=((byte)((obj>>16) & 0xff));
					currentKey[keyLength+3]=((byte)(gen & 0xff));
					currentKey[keyLength+4]=((byte)((gen>>8) & 0xff));
				}

                byte[] finalKey = new byte[Math.min(currentKey.length,16)];

                if(ref.length()>0){
					MessageDigest currentDigest =MessageDigest.getInstance("MD5"); 	
					currentDigest.update(currentKey);
					System.arraycopy(currentDigest.digest(),0, finalKey,0, finalKey.length);
				}else{
					System.arraycopy(currentKey,0, finalKey,0, finalKey.length);
				}	
				
				/**only initialise once - seems to take a long time*/
				if(cipher==null)	
					cipher = Cipher.getInstance("RC4");
				
				SecretKey testKey = new SecretKeySpec(finalKey, "RC4");
				
				if(isEncryption)
					cipher.init(Cipher.ENCRYPT_MODE, testKey);
				else
					cipher.init(Cipher.DECRYPT_MODE, testKey);
				
				//if data on disk read a byte at a time and write back
				if((data==null)||(debugCaching && streamCache!=null)){
					
					CipherInputStream cis=new CipherInputStream(bis,cipher);
					int nextByte;
					while(true){
						nextByte=cis.read();
						if(nextByte==-1)
							break;
						streamCache.write(nextByte);
					}
					cis.close();
					streamCache.close();
					bis.close();
					
				}
				if(data!=null)//all in memory
				data=cipher.doFinal(data);
				
			}catch(Exception e){
				
				throw new PdfSecurityException("Exception "+e+" decrypting content");
				
			}
			
		}
		
		return data;
	}	

	/**
	 * routine to create a padded key
	 */
	private byte[] getPaddedKey(byte[] password){
			
			/**get 32 bytes for  the key*/
			byte[] key=new byte[32];

			int passwordLength=password.length;
			if(passwordLength>32)
			    passwordLength=32;

        System.arraycopy(encryptionPassword, 0, key, 0, passwordLength);
		
			for(int ii=passwordLength;ii<32;ii++){
			
				key[ii]=(byte)Integer.parseInt(padding[ii-passwordLength],16);
				
				
			}
		
			return key;
	}
	
	/**see if valid for password*/
	private boolean testUserPassword() throws PdfSecurityException{
			
			int count=32;
			
			byte[] rawValue=new byte[32];
			
			byte[] keyValue=new byte[32];
		
			for(int i=0;i<32;i++)
			rawValue[i]=(byte)Integer.parseInt(padding[i],16);
			
			byte[] encryptedU=(byte[])rawValue.clone();

			if (rev==2) {
				encryptionKey=calculateKey(O,P,ID);
				encryptedU=decrypt(encryptedU,"", true,null);
				
			} else if(rev==3) {
				count=16;	
				encryptionKey=calculateKey(O,P,ID);
				byte[] originalKey=(byte[]) encryptionKey.clone(); 
				
				MessageDigest md = null;
				try { 
					md = MessageDigest.getInstance("MD5"); 
				} catch (Exception e) {
					LogWriter.writeLog("Exception "+e+" with digest");
				}
				
				md.update(encryptedU);
				
				//feed in ID
				keyValue=new byte[ID.length()/2];
				for(int ii=0;ii<ID.length();ii=ii+2){
					String nextValue=ID.substring(ii,ii+2);
					keyValue[ii/2]=(byte)Integer.parseInt(nextValue,16);
				}
				
				keyValue = md.digest(keyValue);
				keyValue=decrypt(keyValue,"", true,null);
					
				byte[] nextKey = new byte[keyLength];
				
				for (int i=1; i<=19; i++) {
		
					for (int j=0; j<keyLength; j++) 
					nextKey[j] = (byte)(originalKey[j] ^ i);
	
					encryptionKey=nextKey;
					keyValue=decrypt(keyValue,"", true,null);
	
				}
				
				encryptionKey=originalKey;

				encryptedU = new byte[32];
				System.arraycopy(keyValue,0, encryptedU,0, 16);
				System.arraycopy(rawValue,0, encryptedU,16, 16);
				
			}
			
			boolean isMatch=true;
			
			for(int i=0;i<count;i++){
				if(U[i]!=encryptedU[i]){
					isMatch=false;
					i=U.length;
				}
			}
		
			return isMatch;
	}
	
	/**set the key value*/
	private void setKey() throws PdfSecurityException{
		MessageDigest md=null;
		
					/**calculate key to use*/
					byte[] key=getPaddedKey(encryptionPassword);
				
					/**feed into Md5 function*/
					try{
	
						// Obtain a message digest object.
						md = MessageDigest.getInstance("MD5"); 
						encryptionKey=md.digest(key);
						
						/**rev 3 extra security*/
						if(rev==3){
							for (int ii=0; ii<50; ii++) 
							encryptionKey = md.digest(encryptionKey);
						}


					}catch(Exception e){
								throw new PdfSecurityException("Exception "+e+" generating encryption key");
					}
											
	}
	
	/**see if valid for password*/
	private boolean testOwnerPassword() throws PdfSecurityException{
		
		byte[] originalPassword=this.encryptionPassword;
			
		byte[] ownerValue=new byte[keyLength];
		byte[] inputValue=(byte[])O.clone();
		setKey();
		byte[] originalKey=(byte[])encryptionKey.clone();		
			
		if(rev==2)
				ownerValue=decrypt(inputValue,"", false,null);
		else if(rev==3){
				
				ownerValue=inputValue;		
				byte[] nextKey = new byte[keyLength];
					
				for (int i=19; i>=0; i--) {
						
					for (int j=0; j<keyLength; j++) 
					nextKey[j] = (byte)(originalKey[j] ^ i);
					
					encryptionKey=nextKey;
					ownerValue=decrypt(ownerValue,"", false,null);
					
				}
		}	
			
		/**use for password*/
		/**	
		StringBuffer newKey=new StringBuffer();
		for(int ii=0;ii<ownerValue.length;ii++){
			newKey.append((char)ownerValue[ii]);
			System.out.print(ownerValue[ii]+" ");
		}
		*/
	
		encryptionPassword = ownerValue; //newKey.toString();
		
		setKey();
		boolean isMatch=testUserPassword();
		
		//put back to original if not in fact correct
		if(isMatch==false){
			encryptionPassword=originalPassword;
			setKey();
		}else{
		}
			
			return isMatch;
		}
	
	/**
     * find a valid offset
     */
    final private String findOffsets() throws PdfSecurityException {
        LogWriter
                .writeLog("Corrupt xref table - trying to find objects manually");

        String root_id = "";
        try {
            movePointer(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            String line = null;

            int i = (int) this.getPointer();

            try {
                line = pdf_datafile.readLine();
            } catch (Exception e) {
                LogWriter.writeLog("Exception " + e + " reading line");
            }
            if (line == null)
                break;

            if (line.indexOf(" obj") != -1) {

                int pointer = line.indexOf(" ");
                if (pointer > -1) {
                    int current_number = Integer.parseInt(line.substring(0,
                            pointer));
                    storeObjectOffset(current_number, i, 1,false);
                }
                
            } else if (line.indexOf("Root") != -1) {

                int start = line.indexOf("Root") + 4;
                int pointer = line.indexOf("R", start);
                if (pointer > -1)
                    root_id = line.substring(start, pointer + 1).trim();
            } else if (line.indexOf("/Encrypt") != -1) {
                //too much risk on corrupt file
                throw new PdfSecurityException("Corrupted, encrypted file");
            }
        }

        return root_id;
    }

	/**extract  metadata for  encryption object
	 */
	final public void readEncryptionObject(String ref) throws PdfSecurityException {

		//<start-13>
        //<start-jfl>
		if (!isInitialised) {
			isInitialised = true;
			SetSecurity.init();
		}
        //<end-jfl>

        //get values
		Map encryptionValues = readObject(ref, true, null);
		
		//check type of filter and see if supported
		String filter = (String) encryptionValues.get("Filter");
		int v = 1;
		String value = (String) encryptionValues.get("V");
		if (value != null)
			v = Integer.parseInt(value);

		value = (String) encryptionValues.get("Length");
		if (value != null)
			keyLength = Integer.parseInt(value) / 8;

        if(v==3)
            throw new PdfSecurityException("Unsupported Custom Adobe Encryption method " + encryptionValues);

        //throw exception if we have an unknown encryption method
		if ((v > 4) && (filter.indexOf("Standard") == -1))
			throw new PdfSecurityException("Unsupported Encryption method " + encryptionValues);

		//get rest of the values (which are not optional)
		rev = Integer.parseInt((String) encryptionValues.get("R"));
		P = Integer.parseInt((String) encryptionValues.get("P"));

		Object OValue = encryptionValues.get("O");
		if (OValue instanceof String) {
			String hexString = (String) OValue;
			int keyLength = hexString.length() / 2;
			O = new byte[keyLength];

			for (int ii = 0; ii < keyLength; ii++) {
				int p = ii * 2;
				O[ii] =
					(byte) Integer.parseInt(hexString.substring(p, p + 2), 16);
			}

		} else {
			O = (byte[]) OValue;
		}

		Object UValue = encryptionValues.get("U");
		if (UValue instanceof String) {
			String hexString = (String) UValue;
			int keyLength = hexString.length() / 2;
			U = new byte[keyLength];

			for (int ii = 0; ii < keyLength; ii++) {
				int p = ii * 2;
				U[ii] =
					(byte) Integer.parseInt(hexString.substring(p, p + 2), 16);
			}

		} else {
			U = (byte[]) UValue;
		}
		
		//System.out.println(encryptionValues);
		
		//get additional AES values
		if(v==4){
			CF=(Map) encryptionValues.get("CF");
			StrF=(String) encryptionValues.get("StrF");
			EFF=(String) encryptionValues.get("EFF");
			CFM=(String) encryptionValues.get("CFM");
		}
		
		//<end-13>
		isEncrypted = true;
		isFileViewable = false;

		LogWriter.writeLog("File has encryption settings");

		try{
			verifyAccess();
		}catch(PdfSecurityException e){
			LogWriter.writeLog("File requires password");
		}
		
	}	
	
	/**test password and set access settings*/
	private void verifyAccess() throws PdfSecurityException{
		
		/**assume false*/
		isPasswordSupplied=false;
		extractionIsAllowed=false;

        passwordStatus=PDFflags.NO_VALID_PASSWORD;

        //<start-13>
		/**workout if user or owner password valid*/
		boolean isOwnerPassword =testOwnerPassword();
		
		if(!isOwnerPassword){
			boolean isUserPassword=testUserPassword();
			
			/**test if user first*/
			if(isUserPassword){
				//tell if not default value
				if(encryptionPassword.length>0)
				LogWriter.writeLog("Correct user password supplied ");
				
				isFileViewable=true;
				isPasswordSupplied=true;
				
				if((P & 16)==16)
				extractionIsAllowed=true;

                passwordStatus=PDFflags.VALID_USER_PASSWORD;

            }else
				throw new PdfSecurityException("No valid password supplied");
			
		}else{
			LogWriter.writeLog("Correct owner password supplied");
			isFileViewable=true;
			isPasswordSupplied=true;
			extractionIsAllowed=true;
            passwordStatus=PDFflags.VALID_OWNER_PASSWORD;
        }
		
		//<end-13>
	}
	
	/**
	 * calculate the key
	*/
	private byte[] calculateKey(byte[] O,int P,String ID) throws PdfSecurityException{
		
		MessageDigest md=null;
		
		byte[] keyValue=null;
		
		/**calculate key to use*/
		byte[] key=getPaddedKey(encryptionPassword);
		
		/**feed into Md5 function*/
		try{
			
			// Obtain a message digest object.
			md = MessageDigest.getInstance("MD5"); 
		
			md.update(key);
		
			//write in O
			md.update(O);
		
			byte[] PValue=new byte[4];
			PValue[0]=((byte)((P) & 0xff));
			PValue[1]=((byte)((P>>8) & 0xff));
			PValue[2]=((byte)((P>>16) & 0xff));
			PValue[3]=((byte)((P>>24) & 0xff));
		
			md.update(PValue);
		
			//feed in ID
			keyValue=new byte[ID.length()/2];
			for(int ii=0;ii<ID.length();ii=ii+2){
				String nextValue=ID.substring(ii,ii+2);
				keyValue[ii/2]=(byte)Integer.parseInt(nextValue,16);
			}
			
			keyValue = md.digest(keyValue);
			
			//for rev 3
			if(rev==3){
				for(int i=0;i<50;i++)
				keyValue = md.digest(keyValue);
			}
					
		}catch(Exception e){
	
			throw new PdfSecurityException("Exception "+e+" generating encryption key");
		}		
				
		/**put significant bytes into key*/
		byte[] returnKey = new byte[keyLength];
		System.arraycopy(keyValue,0, returnKey,0, keyLength);		
		
		return returnKey;
	}
	
	///////////////////////////////////////////////////////////////////////////
	/**
	 * read information object and return pointer to correct
	 * place
	 */
	final private void readInformationObject(String value) {

		try {

			//LogWriter.writeLog("Information object "+value+" present");

			Map fields=new HashMap();
			String[] names=currentFileInformation.getFieldNames();
			for(int ii=0;ii<names.length;ii++){
				fields.put(names[ii],"z");
			}
			
			//get info
			Map info_values = readObject(value,false, fields);
			
			//System.out.println(info_values);
			/**set the information values*/
					
					//put into fields so we can display
					for (int i = 0; i < names.length; i++){
						Object nextValue=info_values.get(names[i]);
						//System.out.println(names[i]);
						if(nextValue!=null){
							/**allow for stream value*/
							if(nextValue instanceof byte[]){
								//System.out.println(names[i]);
								String textValue=getTextString((byte[]) nextValue);
								//System.out.println(textValue+"<<<<<<<<<<<<<<<<<<<<<<<");
								currentFileInformation.setFieldValue( i,textValue);
							}else if(nextValue instanceof String ){
								String stringValue=(String)nextValue;
								if(stringValue.indexOf("False")!=-1){
									currentFileInformation.setFieldValue( i,"False");
								}else if(stringValue.indexOf("False")!=-1){
									currentFileInformation.setFieldValue( i,"True");
								}else{
									//System.out.println("TEXT value "+nextValue+" in file "+ObjectStore.getCurrentFilename());
									//System.exit(1);
									//currentFileInformation.setFieldValue( i,newValue.toString());
								}
							}else{
								//System.out.println("TEXT value "+nextValue+" in file "+ObjectStore.getCurrentFilename());
								//System.exit(1);
								//currentFileInformation.setFieldValue( i,newValue.toString());
							}
						}
					}
			
			
		} catch (Exception e) {
			System.out.println(" problem with info");
			LogWriter.writeLog(
				"Exception " + e + " reading information object "+value);
			//System.exit(1);
		}
	}
	/**
	 * return pdf data
	 */
	public byte[] getPdfBuffer() {
		return pdf_datafile.getPdfBuffer();
	}
	
	/**
	 * read a text String held in fieldName in string
	 */
	public String getTextString(byte[] rawText) {
		
		String returnText="";
		
		//make sure encoding loaded
		StandardFonts.checkLoaded(StandardFonts.PDF);
		
		String text="";
		
		//retest on false and true
		final boolean debug=false;
		
		char[] chars=null;
		if(rawText!=null)
			chars=new char[rawText.length];
		int ii=0;
		
		StringBuffer convertedText=null;
		if(debug)
		convertedText=new StringBuffer();
		
		char nextChar;
		
		TextTokens rawChars=new TextTokens(rawText);
		
		//test to see if unicode
		if(rawChars.isUnicode()){
			//its unicode
			while(rawChars.hasMoreTokens()){
				nextChar=rawChars.nextUnicodeToken();
				if(nextChar==9){
					if(debug)
					convertedText.append(' ');
					chars[ii]=32;
					ii++;
				}else if(nextChar>31){
					if(debug)
					convertedText.append(nextChar);
					chars[ii]=nextChar;
					ii++;
				}
			}
			
		}else{
			//pdfDoc encoding
			
			while(rawChars.hasMoreTokens()){
				nextChar=rawChars.nextToken();
				
				if(nextChar==9){
					if(debug)
					convertedText.append(' ');
					chars[ii]=32;
					ii++;
				}else if(nextChar>31){
					String c=StandardFonts.getEncodedChar(StandardFonts.PDF,nextChar);
						
					if(debug)
						convertedText.append(c);
					
					int len=c.length();
					
					//resize if needed
					if(ii+len>=chars.length){
						char[] tmp=new char[len+ii+10];
						for(int jj=0;jj<chars.length;jj++)
							tmp[jj]=chars[jj];
						chars=tmp;
					}
					
					//add values
					for(int i=0;i<len;i++){
						chars[ii]=c.charAt(i);
						ii++;
					}
				}
			}
		}
		
		
		if(chars!=null)
			returnText=String.copyValueOf(chars,0,ii);
		
		if(debug){
			if(!convertedText.toString().equals(returnText)){
				System.out.println("Different values >"+convertedText+"<>"+returnText+"<");
				System.exit(1);
			}
		}
		
		//System.exit(1);
		return returnText;
		
	}


    /**
	 * read any names
	 */
	public void readNames(Object nameObj, Javascript javascript){
		
		Map values=null;
		
		if(nameObj instanceof String)
			values=readObject((String)nameObj,false,null);
		else
			values=(Map) nameObj;

        Object dests= values.get("Dests");
		String names=getValue((String) values.get("Names"));
        String javaScriptRef=(String) values.get("JavaScript");

        if(names!=null){
			String nameList = Strip.removeArrayDeleminators(names); //get initial pages
			if(nameList.startsWith("<feff")){ //handle [<feff005f00500041004700450031> 1 0 R]
				StringTokenizer keyValues =new StringTokenizer(nameList);
				while(keyValues.hasMoreTokens()){
					String nextKey=keyValues.nextToken();
					nextKey=nextKey.substring(1,nextKey.length()-1);
					String value=keyValues.nextToken()+" "+keyValues.nextToken()+" "+keyValues.nextToken();
					nameLookup.put(nextKey,value);
				}
				
			}else if(nameList.indexOf("(")!=-1){ //its a binary list so we need to read from the raw bytes
				
				/**read the raw bytes so we can decode correctly*/
				byte[] raw=readObjectAsByteArray((String) nameObj,false);
				int dataLen=raw.length;
				int i=0;
				
				/**move to /Names*/
				while(true){
					if((raw[i]==47)&&(raw[i+1]==78)&&(raw[i+2]==97)&&(raw[i+3]==109)&&(raw[i+4]==101)&&(raw[i+5]==115))
					break;
					i++;
				}
						
				i=i+5;
				
				/**
				 * read all value pairs
				 */
				while(i<dataLen){
					
					/**
					 *move to first (
					 */
					while(raw[i]!=40){
						
						i++;
					}
					//i++;
					
					ByteArrayOutputStream bis=new ByteArrayOutputStream();
					try{
						if(raw[i+1]!=41){ //trap empty field
							
							/**
							 * read the bytes for the text string
							 */
							while(true){
								
								i++;
								boolean isOctal=false;
								
								//trap escape
								if((raw[i]==92)&&((raw[i-1]!=92)||((raw[i-1]==92)&&(raw[i-2]==92)))){
									
									i++;
									
									if(raw[i]=='b')
										raw[i]='\b';
									else if(raw[i]=='n')
										raw[i]='\n';
									else if(raw[i]=='t')
										raw[i]='\t';
									else if(raw[i]=='r')
										raw[i]='\r';
									else if(raw[i]=='f')
										raw[i]='\f';
                                    else if(raw[i]=='\\')
										raw[i]='\\';
                                    else if(Character.isDigit((char) raw[i])){ //octal
										StringBuffer octal=new StringBuffer(3);
										for(int ii=0;ii<3;ii++){
											octal.append((char)raw[i]);
											i++;
										}
										//move back 1
										i--;
										isOctal=true;
										raw[i]=(byte) Integer.parseInt(octal.toString(),8);
									}
										
								}
								
								//exit at end
								if((!isOctal)&&(raw[i]==41)&&((raw[i-1]!=92)||((raw[i-1]==92)&&(raw[i-2]==92))))
									break;
								
								bis.write(raw[i]);	
							}
						}
						
						//decrypt the text stream
						bis.close();
						byte[] streamData=bis.toByteArray();
						streamData=decrypt(streamData,(String)nameObj, false,null);
						
						/**
						 * read object  ref
						 */
						StringBuffer objectName=new StringBuffer();
						i++;
						
						//lose any dodgy chars
						while((raw[i]==32)|(raw[i]==10)|(raw[i]==13))
							i++;
						
						int end=i;
						
						//ignore any extra spaces
						while(raw[end]==32){
							end++;
						}
						
						//find next start ( or [
						boolean isEmbedded=false;
						while((raw[end]!=40)&(end+1<dataLen)){	
							if(raw[end]==91)
								isEmbedded=true;
							if(raw[end]==93) //lookout for ]
								break;
							end++;
						}
						
						//roll on to get ]
						if(isEmbedded)
							end=end+2;
						
						int nextStart=end;
						//safety catch for end
						if((!isEmbedded)&&(raw[end]==']')){
							nextStart=dataLen;
						}
						
						//ignore any extra spaces at end
						while(raw[end]==32)
							end--;
						int charLength=end-i;
						
							objectName=new StringBuffer(charLength);
							for(int ii=0;ii<charLength;ii++){
								objectName.append((char)raw[ii+i]);
								
								if(((!isEmbedded)&&(raw[ii+i]==82))|(raw[ii+i]==93))
									break;
								
							}
							/**
							System.out.println(getTextString(streamData)+" "+objectName+"<<");
							
							if(getTextString(streamData).toString().indexOf("G2.1375904")!=-1){
								
							System.exit(1);
							}*/
							//store
							nameLookup.put(getTextString(streamData),objectName.toString());
							
						i=nextStart; //next item or exit at end
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
			}else{
				LogWriter.writeLog("Name list format not supported");
				
			}
			
		}else if(dests!=null){
			Map destValues=null;
			if(dests instanceof String)
				destValues=readObject((String)dests,false,null);
			else
				destValues=(Map) dests;
			
			//handle any kids
			String kidsObj=(String)destValues.get("Kids");
			if(kidsObj!=null){
				String kids = Strip.removeArrayDeleminators(getValue((String) destValues.get("Kids"))); //get initial pages
				if (kids.length() > 0) {/**allow for empty value and put next pages in the queue*/				
					StringTokenizer initialValues =new StringTokenizer(kids, "R");
					while (initialValues.hasMoreTokens())
						readNames(initialValues.nextToken().trim() + " R",javascript);
				}
			}
        }else if(javaScriptRef!=null){

            
            //for the moment just flag Javascript as there
            if(javascript!=null)
            javascript.readJavascript();
        }else{
			//handle any kids
			String kidsObj=(String)values.get("Kids");
			if(kidsObj!=null){
				String kids = Strip.removeArrayDeleminators(getValue((String) values.get("Kids"))); //get initial pages
				if (kids.length() > 0) {/**allow for empty value and put next pages in the queue*/				
					StringTokenizer initialValues =new StringTokenizer(kids, "R");
					while (initialValues.hasMoreTokens())
						readNames(initialValues.nextToken().trim() + " R",javascript);
				}	
			}
		}
		}
	
    /**
     * convert name into object ref
     */
    public String convertNameToRef(String value) {
        
        return (String) nameLookup.get(value);
    }
    
    /**
	 * convert all object refs (ie 1 0 R) into actual data.
	 * Works recursively to cover all levels.
     * @param pageLookup
	 */
    public void flattenValuesInObject(boolean addPage,boolean keepKids,Map formData, Map newValues,Map fields, PageLookup pageLookup,String formObject) {
        final boolean debug =false;
    	
        if(debug)
            System.out.println(formData);
        
        		if(addPage)
			newValues.put("PageNumber","1");
        		
	    	Iterator keys=formData.keySet().iterator();
	    	while(keys.hasNext()){
	    		String currentKey=(String) keys.next();
	    		Object currentValue=null;
	    		
	    		if(debug)
	                System.out.println("currentKey="+currentKey);
	    		
	    		if(currentKey.equals("P")){
	    			//add page
	    			try{
	    				Object rawValue=formData.get("P");
	    				
	    				if(rawValue!=null && pageLookup!=null && rawValue instanceof String){
	    					int page = pageLookup.convertObjectToPageNumber((String) rawValue);
	    					newValues.put("PageNumber",""+page);
	    					//currentForm.remove("P");
	    				}
	    			}catch(Exception e){
	    				
	    			}
	    		}else if(currentKey.equals("Stream")){
	    		    /**read the stream*/
	    		    byte[] objectData =readStream(formData,formObject,false,true,false);
	    			
	    		    newValues.put("DecodedStream",objectData);
	    		}else if((!currentKey.equals("Kids"))&&(!currentKey.equals("Parent"))){
	    			currentValue=formData.get(currentKey);
	    		}else if((keepKids) &&(currentKey.equals("Kids"))){
	    			
	    			String kidsList=(String) formData.get("Kids");
	    			if(kidsList!=null){
	    				
	    				Map formObjects=new HashMap();
	    				
	    				//handle any square brackets (ie arrays)
					if (kidsList.startsWith("["))
						kidsList =kidsList.substring(1, kidsList.length() - 1).trim();
					
					//put kids in the queue
					StringTokenizer kidObjects =new StringTokenizer(kidsList, "R");
						
		    			while (kidObjects.hasMoreTokens()) {
							String next_value =kidObjects.nextToken().trim() + " R";
							
							Map stringValue=new HashMap();
	    						flattenValuesInObject(true,keepKids,readObject(next_value,false,fields), stringValue,fields,pageLookup,formObject);
	    						formObjects.put(next_value,stringValue);
					}
		    			
		    			newValues.put("Kids",formObjects);
		    			
	    			}
	    		}
	    		
	    		if(debug)
	    		    System.out.println("currentValue="+currentValue);
	    		
	    		if(currentValue!=null){
	    			if(currentKey.equals("rawValue")){
		    			
		    			if(currentValue instanceof byte[]){
		    				
		    				byte[] fieldBytes=getByteTextStringValue(currentValue,fields);
		    				
		    				if(fieldBytes!=null)
		    					currentValue=getTextString(fieldBytes);
		    			}
		    		}else  if((fields!=null)&&(fields.get(currentKey)!=null)&&(currentValue instanceof byte[])){
	    				
	    				byte[] fieldBytes=getByteTextStringValue(currentValue,fields);
	    				
	    				if(fieldBytes!=null)
	    					currentValue=getTextString(fieldBytes);
	    			}
	    			
	    			if(currentValue instanceof String){ //remap xx=[1 0 R] but not [1 0 R 2 0 R]
	    				String keyString=currentValue.toString();
	    				StringTokenizer tokens=new StringTokenizer(keyString);
	    				if(tokens.countTokens()==3){
		    				int i1=keyString.indexOf(" R");
		    				int i2=keyString.indexOf(" R",i1+1);
		    				
		    				if(debug)
		    				    System.out.println("i1="+i1+" i2="+i2);
		    				
		    				if((i2==-1)&&(keyString.endsWith("]")&&(keyString.indexOf(" R")!=-1))){
		    					keyString=Strip.removeArrayDeleminators(keyString);
		    					formObject=keyString;
		    					Map stringValue=new HashMap();
		    					flattenValuesInObject(addPage,keepKids,readObject(keyString,false,fields), stringValue,fields,pageLookup,formObject);
		    					newValues.put(currentKey,stringValue);
		    					
		    				}else if((i2==-1)&&(keyString.endsWith(" R"))){
		    					Map stringValue=new HashMap();
		    					formObject=keyString;
		    					flattenValuesInObject(addPage,keepKids,readObject(keyString,false,fields), stringValue,fields,pageLookup,formObject);
		    					newValues.put(currentKey,stringValue);
		    				}else
		    					newValues.put(currentKey,currentValue);
	    				}else //if /CalRGB 6 0 R just put back or the moment
	    					newValues.put(currentKey,currentValue);
	    			}else if(currentValue instanceof Map){ // rewmap {N=35 0 R}
	    				Map valueMap=(Map) currentValue;
	    				Map updatedValue=new HashMap();
	    				flattenValuesInObject(addPage,keepKids,valueMap, updatedValue,fields,pageLookup,formObject);
	    				newValues.put(currentKey,updatedValue);
	    			}else{
	    				newValues.put(currentKey,currentValue);
	    			}
	    		}
	    		}
	    	}
    
    /**
     * set size over which objects kept on disk
     */
	public void setCacheSize(int miniumumCacheSize) {
		this.miniumumCacheSize=miniumumCacheSize;
		
	}
	
	/**read data directly from PDF*/
	public byte[] readStreamFromPDF(int start, int end) {
		
		byte[] bytes=new byte[end-start+1];
		
		//get bytes into buffer
		try {
			movePointer(start);
			pdf_datafile.read(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bytes;
	}
	
	public void setInterruptRefReading(boolean value) {
		interruptRefReading=value;
		
	}
	
	public void readStreamIntoMemory(Map downField) {
		String cachedStream = ((String)downField.get("CachedStream"));

		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(cachedStream));
			
			int streamLength = (int)new File(cachedStream).length();
			
			byte[] bytes = new byte[streamLength];
			
			bis.read(bytes);
			bis.close();
			
			downField.put("DecodedStream", bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
