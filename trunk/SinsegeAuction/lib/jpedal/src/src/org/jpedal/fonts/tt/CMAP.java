/*
* ===========================================
* Java Pdf Extraction Decoding Access Library
* ===========================================
*
* Project Info:  http://www.jpedal.org
* Project Lead:  Mark Stephens (mark@idrsolutions.com)
*
* (C) Copyright 2004, IDRsolutions and Contributors.
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
* CMAP.java
* ---------------
* (C) Copyright 2004, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal.fonts.tt;

import java.util.Hashtable;

import org.jpedal.fonts.StandardFonts;
import org.jpedal.utils.LogWriter;

public class CMAP extends Table {
	
	private int[][] charIndexToGlyph;
	
	/**used to 'guess' wrongly encoded fonts*/ 
	private int winScore=0,macScore=0;
	
	//used by format 4
	int segCount=0;
	
	/**which type of mapping to use*/
	int fontMapping=0;
	
	//used by format 4
	int[] endCode,startCode,idDelta,idRangeOffset,glyphIdArray,f6glyphIdArray,offset;
	
	/**CMap format used -1 shows not set*/
	private int[] CMAPformats;
	
	/**Platform-specific ID list*/
	private static String[] PlatformSpecificID={"Roman","Japanese","Traditional Chinese","Korean",
			"Arabic","Hebrew","Greek","Russian",
			"RSymbol","Devanagari","Gurmukhi","Gujarati",
			"Oriya","Bengali","Tamil","Telugu",
			"Kannada","Malayalam","Sinhalese","Burmese",
			"Khmer","Thai","Laotian","Georgian",
			"Armenian","Simplified Chinese","Tibetan","Mongolian",
			"Geez","Slavic","Vietnamese","Sindhi","(Uninterpreted)"};
	
	/**Platform-specific ID list*/
	private static String[] PlatformIDName={"Unicode","Macintosh","Reserved","Microsoft"};

	/**shows which encoding used*/
	int[] platformID;

	private static Hashtable exceptions;
	
	/**set up differences from Mac Roman*/
	static {
		
		exceptions=new Hashtable();
		
		String[] values={"notequal","173","infinity","176","lessequal","178","greaterequal","179",
				"partialdiff","182","summation","183","product","184","pi","185",
				"integral","186","Omega","189","radical","195","approxequal","197",
				"Delta","198","lozenge","215","Euro","219","apple","240"};
		for(int i=0;i<values.length;i=i+2)
			exceptions.put(values[i],values[i+1]);
		
	}
	
	/**which CMAP to use to decode the font*/
	private int formatToUse;

	/**encoding to use resolving tt font - should be MAC but not always*/
	private int encodingToUse;

	private boolean isSubstituted;

	private static boolean WINchecked;

	
	
	public CMAP(FontFile2 currentFontFile,int startPointer, Glyf currentGlyf){
		
		//LogWriter.writeMethod("{readCMAPTable}", 0);
		
		//read 'cmap' table
		if(startPointer==0)
			LogWriter.writeLog("No CMAP table found");
		else{
		
			int id=currentFontFile.getNextUint16();
			int numberSubtables=currentFontFile.getNextUint16();
			
			//read the subtables
			int[] CMAPsubtables=new int[numberSubtables];
			platformID=new int[numberSubtables];
			int[] platformSpecificID=new int[numberSubtables];
			CMAPformats=new int[numberSubtables];
			charIndexToGlyph=new int[numberSubtables][256];
			
			for(int i=0;i<numberSubtables;i++){
				
				platformID[i]=currentFontFile.getNextUint16();
				platformSpecificID[i]=currentFontFile.getNextUint16();
				CMAPsubtables[i]=currentFontFile.getNextUint32();
			//	System.out.println("IDs="+platformSpecificID[i]+" "+platformID[i]);
		//System.out.println(PlatformID[platformID[i]]+" "+PlatformSpecificID[platformSpecificID[i]]+CMAPsubtables[i]);
				
			}
			
			//now read each subtable
			for(int j=0;j<numberSubtables;j++){
				currentFontFile.selectTable(FontFile2.CMAP);
				currentFontFile.skip(CMAPsubtables[j]);
				//assume 16 bit format to start
				CMAPformats[j]=currentFontFile.getNextUint16();
				int length=currentFontFile.getNextUint16();
				int lang=currentFontFile.getNextUint16();

                if((CMAPformats[j]==0)&(length==262)){
					
					StandardFonts.checkLoaded(StandardFonts.WIN);
					StandardFonts.checkLoaded(StandardFonts.MAC);
				
					for(int glyphNum=0;glyphNum<256;glyphNum++){
						
						int index=currentFontFile.getNextUint8();
						charIndexToGlyph[j][glyphNum]=index;

                        /**count to try and guess if wrongly encoded*/
						if((index>0)&&(currentGlyf.isPresent(index))){
							/**/
							if(StandardFonts.isValidMacEncoding(glyphNum))
								macScore++;
                            if(StandardFonts.isValidWinEncoding(glyphNum))
								winScore++;
                        }
					}
					
				}else if(CMAPformats[j]==4){
					
					//read values
					segCount = currentFontFile.getNextUint16()/2;
					int searchRange = currentFontFile.getNextUint16();
					int entrySelector = currentFontFile.getNextUint16();
					int rangeShift = currentFontFile.getNextUint16();
				
					//read tables and initialise size of arrays
					endCode = new int[segCount];
					for (int i = 0; i < segCount; i++) 
						endCode[i] = currentFontFile.getNextUint16();
						
					int pad=currentFontFile.getNextUint16(); //reserved (should be zero)
					
					startCode = new int[segCount];
					for (int i = 0; i < segCount; i++) 
						startCode[i] =currentFontFile.getNextUint16();
					
					idDelta = new int[segCount];
					for (int i = 0; i < segCount; i++)
						idDelta[i] = currentFontFile.getNextUint16();
					
					
					idRangeOffset = new int[segCount];
					for (int i = 0; i < segCount; i++) 
						idRangeOffset[i] = currentFontFile.getNextUint16();
					
					/**create offsets*/
					offset = new int[segCount];
					int diff=0,cumulative=0;
					
					for (int i = 0; i < segCount; i++) {
						
						if(idDelta[i]==0){
							offset[i]=cumulative;
							diff=1+endCode[i]-startCode[i];
							cumulative=cumulative+diff;
						}
						
					}
					
					// glyphIdArray at end
					int count = (length -16-(segCount*8)) / 2;
					
					glyphIdArray = new int[count];
					for (int i = 0; i < count; i++){
						glyphIdArray[i] =currentFontFile.getNextUint16();
					}
					
				}else if(CMAPformats[j]==6){
					int firstCode=currentFontFile.getNextUint16();
					int entryCount=currentFontFile.getNextUint16();
					
					f6glyphIdArray = new int[firstCode+entryCount];
					for(int jj=0;jj<entryCount;jj++)
						f6glyphIdArray[jj+firstCode]=currentFontFile.getNextUint16();

				}else{
					//System.out.println("Unsupported Format "+CMAPformats[j]);
					//reset to avoid setting
					CMAPformats[j]=-1;
					
				}
				
				//System.out.println(" <> "+platformID[j]);
				//System.out.println(CMAPformats[j]+" "+platformID[j]+" "+platformSpecificID[j]+" "+PlatformIDName[platformID[j]]);
				
			}
		}
		
		/**validate format zero encoding*/
		//if(formatFour!=-1)
		//validateMacEncoding(formatZero,formatFour);
				
	}
	

		
	/**convert raw glyph number to Character code*/
	public int convertIndexToCharacterCode(String glyph,int index,boolean remapFont,boolean isSubsetted){
		
		int index2=-1;


		/**convert index if needed*/
		if((fontMapping==1 || (!remapFont &&fontMapping==4))&&(glyph!=null)&&(!glyph.equals("notdef"))){
			
			index=StandardFonts.getAdobeMap(glyph);
			
		}else if (fontMapping==2){
			
			StandardFonts.checkLoaded(encodingToUse);
			
			if(encodingToUse==StandardFonts.MAC){
			    
				Object exception=null;
				if(glyph!=null)
				    exception=exceptions.get(glyph);
				if(exception==null){
				    if(glyph!=null && !isSubsetted)
					index=StandardFonts.lookupCharacterIndex(glyph,encodingToUse);
				}else
					index=((Integer)exception).intValue();
				
				//win indexed just incase
				if(glyph!=null){
					if(!WINchecked){
						StandardFonts.checkLoaded(StandardFonts.WIN);
						WINchecked=true;
					}
					index2=StandardFonts.lookupCharacterIndex(glyph,StandardFonts.WIN);
				}
			}else if(glyph!=null)
				index=StandardFonts.lookupCharacterIndex(glyph,encodingToUse);
		}
		
		int value=-1;
		int format=CMAPformats[formatToUse];


		//remap if flag set
		if((remapFont)&(format>0)&&(format!=6))
			index=index + 0xf000;
		
		//if no cmap use identity
		if(format==0){

			//hack
			if(index>255)
				index=0;
			
			value=charIndexToGlyph[formatToUse][index];
			if((value==0)&(index2!=-1))
				value=charIndexToGlyph[formatToUse][index2];

		}else	if(format==4){
			
			for (int i = 0; i < segCount; i++) {
				
				//System.out.println("Segtable="+i);
				if ((endCode[i] >= index) && (startCode[i] <= index) ){
		
					int idx=0;	
					if (idRangeOffset[i] == 0) {
						//System.out.println("xxx="+(idDelta[i] + index));
						value= (idDelta[i] + index) % 65536;
						//System.out.println("value="+value);
					}else{
						
						idx= offset[i]+(index - startCode[i]);
						value=glyphIdArray[idx];
						
					} 
				}
			}
			
		}else if(format==6){
			if(index>=f6glyphIdArray.length)
				value=0;
			else
				value=f6glyphIdArray[index];
		}else{
			System.err.println("Cmap "+ format+"not supported");
			//LogWriter.writeLog("Cmap "+ format+"not supported");
			value=index;
			//System.out.println(glyph+" "+index+" "+encodingToUse+" "+StandardFonts.MAC+" "+fontMapping);
			
		}
		
	//	System.out.println(value+" format="+format);
		
		return value;
	}

	/**
	 * work out correct CMAP table to use.
	 */
	public void setEncodingToUse(boolean hasEncoding,int fontEncoding,boolean isSubstituted,boolean isCID) {
		
		formatToUse=-1;
		
		int count=platformID.length;
		
		this.isSubstituted=isSubstituted;
		
		//this code changes encoding to WIN if that appears to be encoding used
		if((!isSubstituted)&&(macScore<207)&&(count>0)&&((winScore)>(macScore)))
			this.encodingToUse=StandardFonts.WIN;

        /**case 1 */
        for(int i=0;i<count;i++){
            //System.out.println("Maps="+platformID[i]+" "+CMAPformats[i]);
            if((platformID[i]==3)&&(CMAPformats[i]==1)){
                formatToUse=i;
                this.fontMapping=1;
                i=count;
                //StandardFonts.loadAdobeMap();
            }
        }

        /**case 2*/
		if((formatToUse==-1)&&(!isCID)&&(!isSubstituted)){
			
			for(int i=0;i<count;i++){
				if((platformID[i]==1)&&(CMAPformats[i]==0)){
					formatToUse=i;
					if(hasEncoding)
						fontMapping=2;
					else if(fontEncoding==StandardFonts.WIN)
						fontMapping=2;
					else
						fontMapping=3;
						
					//System.out.println("====>"+i+" "+fontMapping);
					i=count;
				}
			}
		}

        /**case 3 - no MAC cmap in other ranges and substituting font */
        boolean wasCase3=false;
        if(formatToUse==-1){
			for(int i=0;i<count;i++){
				//if((platformID[i]==1)&&(CMAPformats[i]==6)){ Altered 20050921 to fix problem with Doucsign page
				if((CMAPformats[i]==6)){
					formatToUse=i;
					if((!hasEncoding)&&(fontEncoding==StandardFonts.WIN)){
						fontMapping=2;
						StandardFonts.checkLoaded(StandardFonts.MAC);
					}else
						fontMapping=6;

                    wasCase3=true;
                    i=count;
				}
			}
		}

        /**case 4 - no simple maps or prefer to last 1*/
		if((formatToUse==-1) || wasCase3){//&&((!isSubstituted)|(isCID))){
		//if((formatToUse==-1)){
			for(int i=0;i<count;i++){
				if((CMAPformats[i]==4)){
					formatToUse=i;
					fontMapping=4;
					i=count;
				}
			}
		}

		
		//System.out.println(formatToUse+" " +fontMapping);
		
		if(fontEncoding==StandardFonts.ZAPF)
			fontMapping=2;
	}
}
