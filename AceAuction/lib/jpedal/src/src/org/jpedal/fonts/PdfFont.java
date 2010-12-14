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
* PdfFontsData.java
* ---------------
* (C) Copyright 2004, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal.fonts;

//standard java
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.*;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

//<start-jfl>
import org.jpedal.PdfDecoder;
import org.jpedal.parser.PdfStreamDecoder;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.io.ObjectStore;
//<end-jfl>

import org.jpedal.utils.Strip;

import org.jpedal.exception.PdfFontException;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;

import org.jpedal.utils.LogWriter;

import org.jpedal.utils.ToInteger;

/**
 * contains all generic pdf font data for fonts.<P>
 *
  */
public class PdfFont implements Serializable {

	public Font javaFont=null;

    protected String embeddedFontName=null,embeddedFamilyName=null,copyright=null;

    private float missingWidth=-1f;

	public PdfJavaGlyphs glyphs=new PdfJavaGlyphs();

	/**cache for translate values*/
	private String[] cachedValue=new String[256];

    /**allow use to remove spaces and - from font names and ensure lowercase*/
    protected boolean cleanupFonts=false;

    //<start-jfl>
    static{
		setStandardFontMappings();
	}
    //<end-jfl>

    public PdfFont(){}

    /**read in a font and its details for generic usage*/
    public void createFont(String fontName) throws Exception{}

    //<start-jfl>
    /**get handles onto Reader so we can access the file*/
	public PdfFont(PdfObjectReader current_pdf_file) {

		init(current_pdf_file);

	}

    static public void setStandardFontMappings(){

        int count=StandardFonts.files_names.length;

        for(int i=0;i<count;i++){
        	String key=StandardFonts.files_names_bis[i].toLowerCase();
        	String value=StandardFonts.javaFonts[i].toLowerCase();

        	if((!key.equals(value))&&(!PdfDecoder.fontSubstitutionAliasTable.containsKey(key)))
        		PdfDecoder.fontSubstitutionAliasTable.put(key,value);

        }

        for(int i=0;i<count;i++){

        	String key=StandardFonts.files_names[i].toLowerCase();
        	String value=StandardFonts.javaFonts[i].toLowerCase();
        	if((!key.equals(value))&&(!PdfDecoder.fontSubstitutionAliasTable.containsKey(key)))
        		PdfDecoder.fontSubstitutionAliasTable.put(key,value);
            StandardFonts.javaFontList.put(StandardFonts.files_names[i],"x");
        }

        //add other common values
        PdfDecoder.fontSubstitutionAliasTable.put("arialmt","arial");
        PdfDecoder.fontSubstitutionAliasTable.put("arial-boldmt","arialbd");

	}
    //<end-jfl>


    protected String substituteFont=null;

	protected boolean renderPage=false;

	private final float xscale =(float)0.001;

	/**embedded encoding*/
	protected int embeddedEnc=StandardFonts.STD;

	/**holds lookup to map char differences in embedded font*/
	protected String[] diffs;

	/**flag to show if Font included embedded data*/
	public boolean isFontEmbedded=false;

	/*show if font stream CID*/
	protected boolean TTstreamisCID=false;

	/**String used to reference font (ie F1)*/
	protected String fontID="";

	/**number of glyphs - 65536 for CID fonts*/
	protected int maxCharCount=256;

	/**show if encoding set*/
	protected boolean hasEncoding=true;

	/**flag to show if double-byte*/
	private boolean isDoubleByte=false;


	/**font type*/
	protected int fontTypes;

	protected String substituteFontFile=null,substituteFontName=null;

	/**lookup to track which char is space. -1 means none set*/
	private int spaceChar = -1;

	/**holds lookup to map char differences*/
	private String[] diffTable;

	/**holds flags for font*/
	private int fontFlag=0;

	/**lookup for which of each char for embedded fonts which we can flush*/
	private float[] widthTable ;

	/**size to use for space if not defined (-1 is no setting)*/
	private float possibleSpaceWidth=-1;

    //<start-jfl>
    /**handle onto file access*/
	protected PdfObjectReader currentPdfFile;
    //<end-jfl>

    /**loader to load data from jar*/
	protected ClassLoader loader = this.getClass().getClassLoader();

	/**FontBBox for font*/
	public double[] FontMatrix={0.001d,0d,0d,0.001d,0,0};

	/**font bounding box*/
	public float[] FontBBox= { 0f, 0f, 1f, 1f };

	/**
	 * flag to show
	 * Gxxx, Bxxx, Cxxx.
	 */
	protected boolean isHex = false ;

	/**holds lookup to map char values*/
	private String[] unicodeMappings;

	/**encoding pattern used for font. -1 means not set*/
	protected int fontEnc = -1;

	/**flag to show type of font*/
	protected boolean isCIDFont=false;

	/**lookup CID index mappings*/
	protected String[] CMAP;

	/** CID font encoding*/
	protected String CIDfontEncoding;

	/**default width for font*/
	private float defaultWidth=1f;

	protected boolean isFontSubstituted=false;

	/**flag to show if font had explicit encoding - we need to use embedded in some ghostscript files*/
	protected boolean hasFontEncoding;

	protected int italicAngle=0;

	/**test if cid.jar present first time we need it*/
	private static boolean isCidJarPresent;

	/**
	 * used to show truetype used for type 0 CID
	 */
	public boolean isFontSubstituted() {
		return isFontSubstituted;
	}

	/**
	 * flag if double byte CID char
	 */
	public boolean isDoubleByte() {
		return isDoubleByte;
	}

	/**set the default width of a CID font*/
	final protected void setCIDFontDefaultWidth(String value) {

		/**convert to float value*/
		defaultWidth=Float.parseFloat(value)/1000f;

	}

	/**Method to add the widths of a CID font*/
	final protected void setCIDFontWidths(String values) {

		//remove first and last []
		values = values.substring(1, values.length() - 1).trim();

		widthTable=new float[65536];

		//set all values to -1 so I can spot ones with no value
		for(int ii=0;ii<65536;ii++)
			widthTable[ii]=-1;

		StringTokenizer widthValues = new StringTokenizer(values, " []",true);

		String nextValue;

		while (widthValues.hasMoreTokens()) {

			if(!widthValues.hasMoreTokens())
				break;

			while(true){
				nextValue = widthValues.nextToken();
				if(!nextValue.equals(" "))
					break;
			}

			int pointer = Integer.parseInt(nextValue);

			while(true){
				nextValue = widthValues.nextToken();
				if(!nextValue.equals(" "))
					break;
			}

			//process either range or []
			if (nextValue.equals("[")) {
				while(true){

					while(true){
						nextValue = widthValues.nextToken();
						if(!nextValue.equals(" "))
							break;
					}

					if(nextValue.equals("]"))
						break;

					widthTable[pointer]=Float.parseFloat(nextValue)/1000f;
					pointer++;

				}

			} else {

				int endPointer = 1 + Integer.parseInt(nextValue);

				while(true){
					nextValue = widthValues.nextToken();
					if(!nextValue.equals(" "))
						break;
				}

				for (int ii = pointer; ii < endPointer; ii++)
					widthTable[ii]=Float.parseFloat(nextValue)/1000f;

			}
		}
	}


    /**flag if CID font*/
	final public boolean isCIDFont() {

			return isCIDFont;
	}

    //<start-jfl>
    /**set number of glyphs to 256 or 65536*/
	final protected void init(PdfObjectReader current_pdf_file){

		this.currentPdfFile = current_pdf_file;

		//setup font size and initialise objects
		if(isCIDFont)
			maxCharCount=65536;

		glyphs.init(maxCharCount,isCIDFont);
	}
    //<end-jfl>


    /**return unicode value for this index value */
	final private String getUnicodeMapping(int char_int){
		if(unicodeMappings==null)
			return null;
		else
		return  unicodeMappings[char_int];
	}

	/**store encoding and load required mappings*/
	final protected void putFontEncoding(int enc) {

		/**save encoding */
		fontEnc=enc;

		StandardFonts.checkLoaded(enc);

	}


	/**return the mapped character*/
	final public String getUnicodeValue(String displayValue,int rawInt){

		String textValue=getUnicodeMapping(rawInt);

		if(textValue==null)
			textValue=displayValue;

		return textValue;
	}

	/**
	 * convert value read from TJ operand into correct glyph<br>Also check to
	 * see if mapped onto unicode value
	 */
	final public String getGlyphValue(int rawInt) {

		if(cachedValue[rawInt]!=null)
			return cachedValue[rawInt];

		String return_value = null;

		 /***/
		if(isCIDFont){

			//	test for unicode
			String unicodeMappings=getUnicodeMapping(rawInt);
			if(unicodeMappings!=null)
				return_value=unicodeMappings;

			if(return_value == null){

				//get font encoding
				String fontEncoding =CIDfontEncoding;

				if(diffTable!=null){
					return_value =diffTable[rawInt];
				}else if(fontEncoding!=null){
					if(fontEncoding.startsWith("Identity-")){
						return_value=""+(char)rawInt;
					}else if(CMAP!=null){
						String newChar=CMAP[rawInt];

						if(newChar!=null)
							return_value=newChar;
					}
				}

				if(return_value==null)
					return_value=""+((char)rawInt);
			}

		}else
			return_value=getStandardGlyphValue(rawInt);

		//save value for next time
		cachedValue[rawInt]=return_value;

		return return_value;

	}


	/**
	 * read translation table
	 * @throws PdfFontException
	 */
	final private void handleCIDEncoding(String encodingType) throws PdfFontException
	{
		String line = "";
		int begin, end, entry;
		boolean inDefinition = false;
		BufferedReader CIDstream=null;

		//lose the / on encoding type
		if(encodingType.startsWith("/"))
			encodingType=encodingType.substring(1);

		/**put encoding in lookup table*/
		CIDfontEncoding=encodingType;

		/** if not 2 standard encodings
		 * 	load CIDMAP
		 */

		if(encodingType.startsWith("Identity-")){

			//flag as 2 bytes
			isDoubleByte=true;

		}else{

			//test if cid.jar present on first time and throw exception if not
			if(!isCidJarPresent){
				isCidJarPresent=true;
				InputStream in=PdfFont.class.getResourceAsStream("/org/jpedal/res/cid/00_ReadMe.pdf");
		    		if(in==null)
		    			throw new PdfFontException("cid.jar not on classpath");
			}

			CMAP=new String[65536];

			//remap values
			if(encodingType.equals("ETenms-B5-H"))
				encodingType="ETen-B5-H";
			else if(encodingType.equals("ETenms-B5-V"))
				encodingType="ETen-B5-V";

			try{
				CIDstream =new BufferedReader
				(new InputStreamReader(loader.getResourceAsStream(
					"org/jpedal/res/cid/" + encodingType), "Cp1252"));


			} catch (Exception e) {
				LogWriter.writeLog("Problem reading encoding for CID font "+fontID+" "+encodingType+" Check CID.jar installed");
			}

			if(encodingType.equals("UniJIS-UCS2-H"))
			    isDoubleByte=true;

			//read values into lookup table
			if (CIDstream != null) {
				while (true) {

					try{
						line = CIDstream.readLine();

					} catch (Exception e) {}

					if (line == null)
						break;

					if (line.indexOf("endcidrange") != -1)
						inDefinition=false;

					if (inDefinition == true) {
						StringTokenizer CIDentry =new StringTokenizer(line, " <>[]");

						//flag if multiple values
						boolean multiple_values = false;
						if (line.indexOf("[") != -1)
							multiple_values = true;

						//first 2 values define start and end
						begin = Integer.parseInt(CIDentry.nextToken(), 16);
						end = Integer.parseInt(CIDentry.nextToken(), 16);
						entry = Integer.parseInt(CIDentry.nextToken(), 16);

						//put into array
						for (int i = begin; i < end + 1; i++) {
							if (multiple_values == true) {
								//put either single values or range
								entry =Integer.parseInt(CIDentry.nextToken(), 16);
								CMAP[i]= "" + (char)entry;
							} else {
								CMAP[i]="" + (char)entry;
								entry++;
							}
						}
					}
					if (line.indexOf("begincidrange") != -1)
						inDefinition = true;
				}
			}
		}

		if(CIDstream!=null){
			try{
				CIDstream.close();
			} catch (Exception e) {
				LogWriter.writeLog("Problem reading encoding for CID font "+fontID+" "+encodingType+" Check CID.jar installed");
			}
		}

	}

	/**
	 * convert value read from TJ operand into correct glyph<br> Also check to
	 * see if mapped onto unicode value
	 */
	final public String getStandardGlyphValue(int char_int) {

		//get possible unicode values
		String unicode_char = getUnicodeMapping(char_int);

		//handle if unicode
		if ((unicode_char != null))// & (mapped_char==null))
			return unicode_char;

		//not unicode so get mapped char
		String return_value = "", mapped_char;

		//get font encoding
		int font_encoding = getFontEncoding( true);

		mapped_char = getMappedChar(char_int,true);

		// handle if differences first then standard mappings
		if (mapped_char != null) { //convert name into character

			// First check if the char has been mapped specifically for this
			String char_mapping =StandardFonts.getUnicodeName(this.fontEnc +mapped_char);

			if (char_mapping != null)
				return_value = char_mapping;
			else {

				char_mapping =StandardFonts.getUnicodeName(mapped_char);

				if (char_mapping != null)
					return_value = char_mapping;
				else {
				    if(mapped_char.length()==1){
				        return_value = mapped_char;
				    }else if (mapped_char.length() > 1) {
						char c = mapped_char.charAt(0);
						char c2 = mapped_char.charAt(1);
						if (c == 'B' | c == 'C' | c == 'c' | c == 'G' ) {
							mapped_char = mapped_char.substring(1);
							try {
								int val =(isHex)
										? Integer.valueOf(mapped_char, 16).intValue() : Integer.parseInt(mapped_char);
								return_value = String.valueOf((char) val);
							} catch (Exception e) {
								return_value = "";
							}
						} else
							return_value = "";

						//allow for hex number
						boolean isHex=((c>=48 && c<=57)||(c>=97 && c<=102) || (c>=65 && c<=70))&&
						((c2>=48 && c2<=57)||(c2>=97 && c2<=102) || (c2>=65 && c2<=70));

						if(return_value.length()==0 && this.fontTypes ==StandardFonts.TYPE3 && mapped_char.length()==2 && isHex){

							try{
								return_value=""+(char)Integer.parseInt(mapped_char,16);
							}catch(Exception e){
								//hexValue=-1;
							}
						}
					} else
						return_value = "";
				}
			}
		} else if (font_encoding > -1) //handle encoding
			return_value=StandardFonts.getEncodedChar(font_encoding,char_int);

		return return_value;
	}


	/**set the font being used or try to approximate*/
	public final Font getJavaFont(int size) {

		int style =Font.PLAIN;
		boolean isJavaFontInstalled = false;
		String weight =null,mappedName=null,font_family_name=glyphs.fontName;

		String testFont=font_family_name;
		if(font_family_name!=null)
			testFont=font_family_name.toLowerCase();

		//System.out.print(testFont);
		if(testFont.equals("arialmt")){
			testFont="arial";
			font_family_name=testFont;
		}else if(testFont.equals("arial-boldmt")){
			testFont="arial Bold";
			font_family_name=testFont;
		}//else{
//			System.out.println(font_family_name);
//			System.exit(1);
//		}
	//	System.out.print(testFont+" "+font_family_name);
		//pick up any weight in type 3 font or - standard font mapped to Java
		//int pointer = font_family_name.indexOf(",");
		//if ((pointer == -1))//&&(StandardFonts.javaFontList.get(font_family_name)!=null))
		//	pointer = font_family_name.indexOf("-");

//		if (pointer != -1) {
//
//		    //see if present with ,
//			mappedName=(String) PdfDecoder.fontSubstitutionAliasTable.get(testFont);
//
//			weight =testFont.substring(pointer + 1, testFont.length());
//
//			if (weight.indexOf("bold") != -1)
//				style = Font.BOLD;
//			else if (weight.indexOf("roman") != -1)
//				style = Font.ROMAN_BASELINE;
//
//			if (weight.indexOf("italic") != -1)
//				style = style+Font.ITALIC;
//			else if (weight.indexOf("oblique") != -1)
//				style = style+Font.ITALIC;
//
//			font_family_name = font_family_name.substring(0, pointer);
//
//		}

		//remap if not type 3 match
		//if(mappedName==null)
		//mappedName=(String) PdfDecoder.fontSubstitutionAliasTable.get(testFont);

		if(mappedName!=null){
			font_family_name=mappedName;
			testFont=font_family_name.toLowerCase();
		}

		//see if installed
		if(PdfJavaGlyphs.fontList !=null){
			int count = PdfJavaGlyphs.fontList.length;
			for (int i = 0; i < count; i++) {
				System.out.println(PdfJavaGlyphs.fontList[i]+"<>"+testFont);
				if ((PdfJavaGlyphs.fontList[i].indexOf(testFont)!=-1)) {
					isJavaFontInstalled = true;
					font_family_name=PdfJavaGlyphs.fontList[i];
					i = count;
				}
			}
		}

		/**approximate display if not installed*/
		if (isJavaFontInstalled == false) {

		    //try to approximate font
			if(weight==null){

				//pick up any weight
				String test = font_family_name.toLowerCase();
				if (test.indexOf("heavy") != -1)
					style = Font.BOLD;
				else if (test.indexOf("bold") != -1)
					style = Font.BOLD;
				else if (test.indexOf("roman") != -1)
					style = Font.ROMAN_BASELINE;

				if (test.indexOf("italic") != -1)
					style = style+Font.ITALIC;
				else if (test.indexOf("oblique") != -1)
					style = style+Font.ITALIC;

			}

		//	font_family_name = defaultFont;
		}
//			System.out.println(font_family_name+" "+baseFontName+" "+" "+testFont+" "+weight+" "+style);
//			System.out.println(new Font(font_family_name, style, size));
//			if(baseFontName.toLowerCase().indexOf("bold")!=-1)
//				System.exit(1);
		if(isJavaFontInstalled)
			return new Font(font_family_name, style, size);
		else{
			System.out.println("No match with "+glyphs.getBaseFontName()+" "+" "+testFont+" "+weight+" "+style);
			System.exit(1);
			return null;
		}

	}

	/**set the font used for default from Java fonts on system
	 * - check it is a valid font (otherwise it will default to Lucida anyway)
	 */
	public final void setDefaultDisplayFont(String fontName) {

		glyphs.defaultFont=fontName;

	}

    //<start-jfl>
    /**
	 * Returns the java font, initializing it first if it hasn't been used before.
	 */
	public final Font getJavaFontX(int size) {

		return new Font(glyphs.font_family_name, glyphs.style, size);

	}

	/**
	 * reset font handle
	 *
	public final void unsetUnscaledFont() {
		unscaledFont=null;
	}*/

	/**read in generic font details from the pdf file*/
	final protected void readGenericFontMetadata(Map values) {

		LogWriter.writeMethod("{readGenericFontMetadata "+ fontID+"}", 0);

		// Get font size matrix
		String fontMatrix = (String) values.get("FontMatrix");
		if (fontMatrix != null) {
			StringTokenizer tokens = new StringTokenizer(fontMatrix, "[] ");

			for (int i = 0; i < 6; i++){
				FontMatrix[i] =(Float.parseFloat(tokens.nextToken()));
				//System.out.println(i+" "+fontMatricies[i]);
			}
		}

		//Get font size matrix for type 3 font
		String fontBounding = (String) values.get("FontBBox");

		if (fontBounding != null) {
			StringTokenizer tokens =new StringTokenizer(fontBounding, "[] ");

			for (int i = 0; i < 4; i++)
				FontBBox[i] = Float.parseFloat(tokens.nextToken());

		}

		// Get base font name
		String baseFontName =currentPdfFile.getValue((String) values.get("BaseFont"));

		//allow for CID name in fontname
		if(baseFontName==null)
			baseFontName =currentPdfFile.getValue((String) values.get("FontName"));

		//should have a name but some don't so use Name
		if (baseFontName == null)
			baseFontName = fontID;
		else
			baseFontName = baseFontName.substring(1);

        /**remove spaces and unwanted chars*/
        if(cleanupFonts || PdfStreamDecoder.runningStoryPad)
        baseFontName= cleanupFontName(baseFontName);

        /**save name less any suffix*/
		glyphs.fontName=baseFontName;
		int index=baseFontName.indexOf("+");
		if (index  == 6)
			glyphs.fontName=baseFontName.substring(index + 1);

		glyphs.setBaseFontName(baseFontName);
	}


    //<end-jfl>

    /**
	 * get font name as a string from ID (ie Tf /F1) and load if one of Adobe 14
	 */
	final public String getFontName() {

		//check if one of 14 standard fonts and load if needed
		StandardFonts.loadStandardFontWidth(glyphs.fontName);

		return glyphs.fontName;
	}

	/**
	 * get raw font name which may include +xxxxxx
	 */
	final public String getBaseFontName() {

		return glyphs.getBaseFontName();
	}

	/**
	 * set raw font name which may include +xxxxxx
	 */
	final public void setBaseFontName(String fontName) {

		glyphs.setBaseFontName(fontName);
	}

    /**
	 * set font name
	 */
	final public void setFontName(String fontName) {

		glyphs.fontName=fontName;
	}

    /**
	 * get width of a space
	 */
	final public float getCurrentFontSpaceWidth() {

		float width;

		//allow for space mapped onto other value
		int space_value =spaceChar;

		if (space_value !=-1)
			width = getWidth(space_value);
		else
			width=  possibleSpaceWidth; //use shortest width as a guess

		//allow for no value
		if (width ==-1)
		width = 0.2f;

		return width;
	}

	final protected int getFontEncoding( boolean notNull) {
		int result = fontEnc;

		if (result == -1 && notNull)
			result = StandardFonts.STD;

		return result;
	}

	/** Returns width of the specified character<br>
	 *  Allows for no value set*/
	final public float getWidth( int charInt) {
		//try embedded font first (indexed by number)
		float width =-1;

		if(widthTable!=null)
			width =  widthTable[charInt];

        if (width == -1) {

			if(isCIDFont){
				width= defaultWidth;

			}else{

				//try standard values which are indexed under NAME of char
				String charName = getMappedChar( charInt,false);

				if((charName!=null)&&(charName.equals(".notdef")))
					charName=StandardFonts.getUnicodeChar(getFontEncoding( true) , charInt);

				Float value =StandardFonts.getStandardWidth(glyphs.fontName , charName);

				if (value != null)
					width=value.floatValue();
				else{
                    if(missingWidth!=-1)
                        width=missingWidth*xscale;
                    else
                        width=0;
                }
			}
		}

		return width;
	}

    //<start-jfl>
    /**generic CID code
	 * @throws PdfFontException */
	public Map createCIDFont(Map values,Map descFontValues) throws PdfFontException{

		Map fontDescriptor=null;

		cachedValue=new String[65536];

		/**read encoding values*/
		String encoding =(String) values.get("Encoding");
		if(encoding!=null)
			handleCIDEncoding(encoding);

		/**unicode settings*/
		Object toUnicode= values.get("ToUnicode");
		if(toUnicode!=null){
		    if(toUnicode instanceof String)
		        this.readUnicode(currentPdfFile.readStream((String)toUnicode,true),fontID);
		    else
		        this.readUnicode((byte[])((Map)toUnicode).get("DecodedStream"),fontID);
		}
		//byte[] data=currentPdfFile.readStream(unicode_value);
		/**read widths*/
		String rawWidths = currentPdfFile.getValue((String) descFontValues.get("W"));
		if(rawWidths!=null)
			setCIDFontWidths(rawWidths);

		/**set default width*/
		String defaultWidth = currentPdfFile.getValue((String) descFontValues.get("DW"));
		if(defaultWidth!=null)
			setCIDFontDefaultWidth(defaultWidth);



		/**set CIDtoGIDMap*/
		Object  CIDtoGIDvalue = descFontValues.get("CIDToGIDMap");
		if(CIDtoGIDvalue!=null){
			if(CIDtoGIDvalue instanceof String){
				String CIDtoGIDMap=(String) CIDtoGIDvalue;

				//load a stream object - build table
				if(CIDtoGIDMap.endsWith(" R")){
					byte[] CIDMaps=currentPdfFile.readStream(CIDtoGIDMap,true);
					int j=0;
					int[] CIDToGIDMap=new int[CIDMaps.length/2];
					for(int i=0;i<CIDMaps.length;i=i+2){
						int val=(((CIDMaps[i] & 255)<<8)+(CIDMaps[i+1] & 255));
						CIDToGIDMap[j]= val;

						//System.out.println("CIDtoGIDMap "+j+" "+(int)val+" "+val);
						j++;
					}

					glyphs.setGIDtoCID(CIDToGIDMap);

				}else if(CIDtoGIDMap.equals("/Identity")){
					handleCIDEncoding("Identity-");
				}else{
					LogWriter.writeLog("not yet supported in demo.");
					System.err.println("not yet supported in demo.");
				}

			}else{

				LogWriter.writeLog("not yet supported in demo.");
				System.err.println("not yet supported in demo.");

			}
		}

		/**set default font - value is mandatory*/
		Object  Info = descFontValues.get("CIDSystemInfo");
		Map CIDSystemInfo=new Hashtable();
		if(Info instanceof Map)
			CIDSystemInfo=(Map) Info;
		else
			CIDSystemInfo=currentPdfFile.readObject((String)Info,false, null);

		/**handle ordering*/
		String ordering=(String) CIDSystemInfo.get("Ordering");

		if(ordering!=null){
			/**if(ordering.indexOf("Japan1")!=-1){
			}else */if(ordering.indexOf("Japan")!=-1){

				substituteFontFile="kochi-mincho.ttf";
				substituteFontName="Kochi Mincho";

				this.TTstreamisCID=false;
				//substituteFontFile="/home/markee/msgothic.ttf";
				//substituteFontName="msgothic";
				//substituteFontFile="/mnt/shared/KozGoPro-Medium-Acro.otf";
				//substituteFontName="kos";
			}else if(ordering.indexOf("Korean")!=-1){
				System.err.println("Unsupported font encoding "+ordering);

			}else if(ordering.indexOf("Chinese")!=-1){
				System.err.println("Chinese "+ordering);
			}else{
				//System.err.println("Unsupported font encoding "+ordering);
				//LogWriter.writeLog("Unsupported font encoding "+ordering);
			}

			if(substituteFontName!=null)
			LogWriter.writeLog("Using font "+substituteFontName+" for "+ordering);

		}

		/**get object describing fontdescriptor and read values*/
		Object fontDescriptorRef =descFontValues.get("FontDescriptor");

		if (fontDescriptorRef != null) {

			fontDescriptor=null;

			if(fontDescriptorRef instanceof String){
				String ref=(String)fontDescriptorRef;
				if(ref.length() > 1)
				fontDescriptor =currentPdfFile.readObject( ref,false, null);
			}else{
				fontDescriptor=(Map)fontDescriptorRef;
			}
			/**read other info*/
			if(fontDescriptor!=null)
			readGenericFontMetadata(fontDescriptor);
		}

		return fontDescriptor;

	}
    //<end-jfl>

    /**
	 *
	 */
	final protected void selectDefaultFont() {
		/**load fonts for specific encoding*
		 *
		//get list of fonts and see if installed
		String[] fontList =GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		System.out.println(substituteFontFile+" "+this.substituteFontName);
		Hiragino Kaku Gothic Pro
Hiragino Kaku Gothic Std
Hiragino Maru Gothic Pro
Hiragino Mincho Pro

defaultFont="Hiragino Mincho Pro";

		int count = fontList.length;
		for (int i = 0; i < count; i++) {
		    System.out.println(fontList[i]);
			//if (fontList[i].equals(font_family_name)) {
			//	isFontInstalled = true;
			//	i = count;
			//}
		}
		if(CIDfontEncoding.startsWith("GBpc-EUC")){
			substituteFontFile="kochi-mincho.ttf";
			substituteFontName="Kochi Mincho";
		}*/
	}

    //<start-jfl>
    /**read inwidth values*/
	public void readWidths(Map values) throws Exception{

		LogWriter.writeMethod("{readWidths}"+values, 0);

		// place font data in fonts array
		String firstChar = currentPdfFile.getValue((String) values.get("FirstChar"));
		int firstCharNumber = 1;
		if (firstChar != null)
			firstCharNumber = ToInteger.getInteger(firstChar);
		String lastChar = currentPdfFile.getValue((String) values.get("LastChar"));

		//variable to get shortest width as guess for space
		float shortestWidth=0;

		//read first,last char, widths and put last into array for fast access
		String width_value =currentPdfFile.getValue((String) values.get("Widths"));

        if (width_value != null) {

			widthTable = new float[maxCharCount];

			//set all values to -1 so I can spot ones with no value
			for(int ii=0;ii<maxCharCount;ii++)
				widthTable[ii]=-1;

			String rawWidths =width_value.substring(1, width_value.length() - 1).trim();

			StringTokenizer widthValues = new StringTokenizer(rawWidths);

			int lastCharNumber = ToInteger.getInteger(lastChar) ;
			float widthValue;

			//scaling factor to convert type 3 to 1000 spacing
			float ratio=(float) (1f/FontMatrix[0]);
			if(ratio<0)
			ratio=-ratio;
			for (int i = firstCharNumber; i < lastCharNumber+1; i++) {

				if(!widthValues.hasMoreTokens()){
					widthValue=0;
				}else{
					if(fontTypes==StandardFonts.TYPE3) //convert to 1000 scale unit
						widthValue =Float.parseFloat(widthValues.nextToken())/(ratio);
					else
						widthValue =Float.parseFloat(widthValues.nextToken())*xscale;

					widthTable[i]=widthValue;

					//track shortest width
					if((widthValue>0)){
						shortestWidth=shortestWidth+widthValue;
					}
				}
			}
		}
	}
    
    /**read in a font and its details from the pdf file*/
	public Map createFont(Map values, String fontID, boolean renderPage, Map descFontValues, ObjectStore objectStore) throws Exception{

		LogWriter.writeMethod("{readNonCIDFont}"+values+"{render="+renderPage, 0);


		if((PdfJavaGlyphs.fontList ==null)&(renderPage)){
		    PdfJavaGlyphs.fontList =GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		    //make sure lowercase
		    int count=PdfJavaGlyphs.fontList.length;
		    for (int i = 0; i < count; i++)
				PdfJavaGlyphs.fontList[i]=PdfJavaGlyphs.fontList[i].toLowerCase();
		}

		this.fontID=fontID;
		this.renderPage=renderPage;

		readGenericFontMetadata(values);

		// Get font type
		String fontType = (String) values.get("Subtype");
		if (fontType == null)
			fontType = "";

		//handle to unicode mapping
		Object toUnicode= values.get("ToUnicode");
		if(toUnicode!=null){
		    if(toUnicode instanceof String)
		        this.readUnicode(currentPdfFile.readStream((String)toUnicode,true),fontID);
		    else
		        this.readUnicode((byte[])((Map)toUnicode).get("DecodedStream"),fontID);
		}
//		byte[] data=currentPdfFile.readStream(unicode_value);

		// place font data in fonts array
		String firstChar = (String) values.get("FirstChar");
		int firstCharNumber = 1;
		if (firstChar != null)
			firstCharNumber = ToInteger.getInteger(firstChar);
		String lastChar = (String) values.get("LastChar");

		//variable to get shortest width as guess for space
		float shortestWidth=0;

		int count=0;

		//read first,last char, widths and put last into array for fast access
		String width_value =currentPdfFile.getValue((String) values.get("Widths"));

		if (width_value != null) {

			widthTable = new float[maxCharCount];

			//set all values to -1 so I can spot ones with no value
			for(int ii=0;ii<maxCharCount;ii++)
				widthTable[ii]=-1;

			String rawWidths =width_value.substring(1, width_value.length() - 1).trim();

			StringTokenizer widthValues = new StringTokenizer(rawWidths);

			int lastCharNumber = ToInteger.getInteger(lastChar) + 1;
			float widthValue;

			//scaling factor to convert type 3 to 1000 spacing
			float ratio=(float) (1f/FontMatrix[0]);
			ratio=-ratio;
			for (int i = firstCharNumber; i < lastCharNumber; i++) {

				if(widthValues.hasMoreTokens()==false)
					widthValue=0;
				else if(fontTypes==StandardFonts.TYPE3) //convert to 1000 scale unit
					widthValue =Float.parseFloat(widthValues.nextToken())/(ratio);
				else
					widthValue =Float.parseFloat(widthValues.nextToken())*xscale;

				widthTable[i]=widthValue;

				//track shortest width
				if((widthValue>0)){
					shortestWidth=shortestWidth+widthValue;
					count++;
				}
			}
		}

		//get encoding info or Unicode and put in lookup table
		Object encValue =values.get("Encoding");

		if (encValue != null) {
			if(encValue instanceof String)
				handleFontEncoding((String) encValue,null,fontType);
			else
				handleFontEncoding("",(Map) encValue,fontType);

		} else {
		    handleNoEncoding(0);
		}
		
		//save guess for space as half average char
		if( count>0)
			possibleSpaceWidth=shortestWidth/(2*count);

		Map fontDescriptor = null;

		//get object describing font descriptor
		Object rawFont=values.get("FontDescriptor");

		if(rawFont instanceof String){
			String fontDescriptorRef = (String) rawFont;
			if (fontDescriptorRef != null && fontDescriptorRef.length() > 1)
				fontDescriptor =currentPdfFile.readObject(fontDescriptorRef,false, null);
		}else
			fontDescriptor=(Map) rawFont;

		if(fontDescriptor!=null){
			/**handle flags and set local variables*/
			int flags=0;
			String value=(String) fontDescriptor.get("Flags");
			if(value!=null)
				flags=Integer.parseInt(value);

			fontFlag=flags;
			//System.err.println("fontFlag="+fontFlag);
			//reset to defaults
			glyphs.remapFont=false;

			int flag=fontFlag;
			if((flag & 4)==4)
				glyphs.remapFont=true;

			//fontDescriptor =currentPdfFile.readObject(fontDescriptorRef,false, null);

            //set missingWidth
            value =currentPdfFile.getValue((String) fontDescriptor.get("MissingWidth"));
            if(value!=null)
            missingWidth=Float.parseFloat(value);
            
            //get any italic (not used)
            //String italic=(String) fontDescriptor.get("ItalicAngle");
    		//if(italic!=null && !italic.equals("0"))
    			//System.out.println(italic+" "+fontDescriptor);
    		//	italicAngle=(int) Float.parseFloat(italic);
    		
        }

		return fontDescriptor ;
	}

    /**
     *
     */
    private int  handleNoEncoding(int encValue) {

		String font=getBaseFontName();
		
		if(font.equals("ZapfDingbats")||(font.equals("ZaDb"))){
	        	putFontEncoding(StandardFonts.ZAPF);
	        	glyphs.defaultFont="Zapf Dingbats";
	        	StandardFonts.checkLoaded(StandardFonts.ZAPF);
	        	
	        	encValue=StandardFonts.ZAPF;
	        	
        }else if(font.equals("Symbol")){
        		putFontEncoding(StandardFonts.SYMBOL);
        		
        		encValue=StandardFonts.SYMBOL;
        }else
        	putFontEncoding(StandardFonts.STD); //default to standard
        
        hasEncoding=false;
        
        return encValue;
    }

    ///////////////////////////////////////////////////////////////////////
	/**
	 * handle font encoding and store information
	 */
	final private void handleFontEncoding(String ref,Map values,String fontType){

		hasFontEncoding=true;

		//Read fonts encoding
		int encoding = getFontEncoding( false);

		String encName = "";
		int encValue = encoding;
		if (encValue == -1) {

			if (fontType.equals("/TrueType"))
				encValue = StandardFonts.MAC;
			else
				encValue = StandardFonts.STD;
		}

		//allow for object and stream of commands
		if( (ref.indexOf(" ") != -1)|(values!=null)){

			if(values==null)
			values =currentPdfFile.readObject(ref,false, null);

			String baseEncoding = currentPdfFile.getValue( (String) values.get("BaseEncoding"));
			String diffs=currentPdfFile.getValue((String) values.get("Differences"));

			if (baseEncoding != null) {
				if (baseEncoding.startsWith("/"))
					baseEncoding = baseEncoding.substring(1);
				encName = baseEncoding;
				hasEncoding=true;
			}else{
				encValue=handleNoEncoding(encValue);
			}
			
			if (diffs != null) {

				glyphs.setIsSubsetted(true);

				//remove [] and then split into elements
				diffs = Strip.removeArrayDeleminators(diffs);

				int pointer = 0;
				String ignoreValues=" \r\n";
				StringTokenizer eachValue =new StringTokenizer(diffs," /\r\n",true);
				while (eachValue.hasMoreTokens()) {

					String value = eachValue.nextToken();

					if(ignoreValues.indexOf(value)!=-1){
					}else if(value.equals("/")){ //next value

					    //get glyph name
					    value=eachValue.nextToken();
					    while(ignoreValues.indexOf(value)!=-1)
					    value=eachValue.nextToken();

						//store space marker
						if (value.startsWith("space"))
						spaceChar= pointer;
						
						putMappedChar( pointer, value);
						pointer++;

						char c=value.charAt(0);
						if (c=='B' | c=='c' | c=='C' | c=='G') {
							int i = 1,l=value.length();
							while (!isHex && i < l)
								isHex =Character.isLetter(value.charAt(i++));
						}
					}else if (Character.isDigit(value.charAt(0))){ //new pointer

					    pointer = Integer.parseInt(value);

					}
				}
			}
		} else
			encName = ref;

		if (encName.indexOf("MacRomanEncoding") != -1)
			encValue = StandardFonts.MAC;
		else if (encName.indexOf("WinAnsiEncoding") != -1)
			encValue = StandardFonts.WIN;
		else if (encName.indexOf("MacExpertEncoding") != -1)
			encValue = StandardFonts.MACEXPERT;
		else if ((encName.indexOf("STD") == -1) & (encValue == -1))
			LogWriter.writeLog("Encoding type " + encName + " not implemented");

		if (encValue > -1)
			putFontEncoding(encValue);
	}
    //<end-jfl>

    /** Insert a new mapped char in the name mapping table */
	final protected void putMappedChar(int charInt, String mappedChar) {

		if(diffTable==null)
			diffTable = new  String[maxCharCount];

		if(diffTable[charInt]==null)
		diffTable[charInt]= mappedChar;

	}



	/**
	 *holds amount of y displacement for embedded type 3font
	 */
	public double getType3Ydisplacement(int rawInt) {
		return 0;
	}

	/** Returns the char glyph corresponding to the specified code for the specified font. */
	public final String getMappedChar(int charInt,boolean remap) {

		String result =null;

		//check differences
		if(diffTable!=null)
		result =diffTable[charInt];

        if((remap)&&(result!=null)&&(result.equals(".notdef")))
			result=" ";

		//check standard encoding
		if (result == null)
			result =StandardFonts.getUnicodeChar(getFontEncoding( true) , charInt);

        //all unused win values over 40 map to bullet
        if(result==null &&  charInt>40 && getFontEncoding(true)==StandardFonts.WIN )
                result="bullet";

        //check embedded stream as not in baseFont encoding
		if((isFontEmbedded)&&(result==null)){

				//diffs from embedded 1C file
				if(diffs!=null)
					result =diffs[charInt];


				//Embedded encoding (which can be different from the encoding!)
				if ((result == null))
					result =StandardFonts.getUnicodeChar(this.embeddedEnc , charInt);

		}

		return result;
	}

	public final String getEmbeddedChar(int charInt) {

		String embeddedResult=null;

		//check embedded stream as not in baseFont encoding
		if(isFontEmbedded){

			//diffs from embedded 1C file
			if(diffs!=null)
				embeddedResult =diffs[charInt];

			//Embedded encoding (which can be different from the encoding!)
			if ((embeddedResult == null) && charInt<256)
				embeddedResult =StandardFonts.getUnicodeChar(this.embeddedEnc , charInt);

		}

		return embeddedResult;
	}

	/**
	 * read unicode translation table
	 */
	final protected void readUnicode(byte[] data, String font_ID){

		String line;
		int begin, end, entry,inDefinition = 0;

		BufferedReader unicode_mapping_stream =null;
		ByteArrayInputStream bis=null;
		//initialise unicode holder
		unicodeMappings = new String[maxCharCount];

		//get stream of data
		try {

			//get the data for an object
			bis=new ByteArrayInputStream(data);
			unicode_mapping_stream =new BufferedReader(new InputStreamReader(bis));

			//read values into lookup table
			if (unicode_mapping_stream != null) {
				while (true) {

					line = unicode_mapping_stream.readLine();

					if ((line == null))
						break;
					else if (line.indexOf("endbf") != -1)
						inDefinition = 0;

					if (inDefinition == 1) {

						StringTokenizer unicode_entry =new StringTokenizer(line, " <>[]");

						//first value defines start
						begin = Integer.parseInt(unicode_entry.nextToken(), 16);

						/**
						 * get raw entry and convert to string
						 */
						String rawEntry=unicode_entry.nextToken();
						String value="";
						if(rawEntry.length()<4){
							entry =Integer.parseInt(unicode_entry.nextToken(), 16);
							value=String.valueOf((char)entry);
						}else{
							for(int i=0;i<rawEntry.length();i=i+4){
								entry=Integer.parseInt(rawEntry.substring(i,i+4), 16);
								value=value+ String.valueOf((char)entry);
							}
						}

						//put into array
						unicodeMappings[begin]= value;

					}else if (inDefinition == 2) {
						StringTokenizer unicode_entry =
							new StringTokenizer(line, " <>[]");

						//first value defines start
						begin = Integer.parseInt(unicode_entry.nextToken(), 16);
						end = Integer.parseInt(unicode_entry.nextToken(), 16);

						//get values
						String rawEntry="";
						while (unicode_entry.hasMoreTokens())
							rawEntry=rawEntry+unicode_entry.nextToken();
						int offset=0;

						//put into array
						for (int i = begin; i < end + 1; i++) {

							String value="";
							int count=rawEntry.length();
							for(int ii=0;ii<count;ii=ii+4){
								entry=Integer.parseInt(rawEntry.substring(ii,ii+4), 16);

								//add offset to last value
								if(i+4>count)
									entry=entry+offset;

								value=value+ String.valueOf((char)entry);
							}
							unicodeMappings[i]=value;
							offset++;

						}
					}
					if (line.indexOf("beginbfchar") != -1)
						inDefinition = 1;
					else if (line.indexOf("beginbfrange") != -1)
						inDefinition = 2;

				}
			}

		} catch (Exception e) {
			LogWriter.writeLog("Exception setting up text object " + e);
		}

		if(unicode_mapping_stream!=null){
			try {
				bis.close();
				unicode_mapping_stream.close();
			} catch (IOException e1) {
				LogWriter.writeLog("Exception setting up text object " + e1);
			}
		}
	}


	/**
	 * gets type of font (ie 3 ) so we can call type
	 * specific code.
	 * @return int of type
	 */
	final public int getFontType() {
		return fontTypes;
	}




	/**
	 * name of font used to display
	 */
	public String getSubstituteFont() {
		return this.substituteFontName;
	}

	/**
	 * test if there is a valid value
	 */
	public boolean isValidCodeRange(int rawInt) {
		if(CMAP==null)
			return false;
		else{
			//System.out.println(CMAP[rawInt]+"<<"+rawInt);
			return (CMAP[rawInt]!=null);
		}
	}

	/**used in generic renderer*/
	public float getGlyphWidth(String charGlyph, int rawInt, String displayValue) {

        if(this.fontTypes==StandardFonts.TRUETYPE){
            return glyphs.getTTWidth(charGlyph,rawInt, displayValue,false);

        }else{
            return 0;
        }
    }


	/**set subtype (only used by generic font*/
	public void setSubtype(int fontType) {
		this.fontTypes=fontType;

	}

	/**used by JPedal internally for font substitution*/
	public void setSubstituted(boolean value) {
		this.isFontSubstituted=value;

	}

	public PdfJavaGlyphs getGlyphData() {
		return glyphs;
	}

	public Font setFont(String font, int textSize) {
		return glyphs.setFont(font,textSize);  //To change body of created methods use File | Settings | File Templates.
	}

	public boolean is1C() {
		return glyphs.is1C();  //To change body of created methods use File | Settings | File Templates.
	}

	public boolean isFontSubsetted() {
		return glyphs.isSubsetted;
	}

	public void setValuesForGlyph(int rawInt, String charGlyph, String displayValue, String embeddedChar) {
		glyphs.setValuesForGlyph(rawInt, charGlyph, displayValue, embeddedChar);
		
	}

    /**
     * remove unwanted chars from string name
     */
    String cleanupFontName(String baseFontName) {


       // baseFontName=baseFontName.toLowerCase();

        int length=baseFontName.length();

        StringBuffer cleanedName=new StringBuffer(length);

        for(int aa=0;aa<length;aa++){
            char c=baseFontName.charAt(aa);

            if(c==' ' || c=='-'){

            }else
                cleanedName.append(c);
        }

        return cleanedName.toString();
    }

	public int getItalicAngle() {
		
		return italicAngle;
	}
}
