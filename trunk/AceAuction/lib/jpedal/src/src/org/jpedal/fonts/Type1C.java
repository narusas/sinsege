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
 * Type1C.java
 * ---------------
 * (C) Copyright 2004, by IDRsolutions and Contributors.
 *
  * 
 * Original Author: Mark Stephens (mark@idrsolutions.com) Contributor(s):
 *  
 */
package org.jpedal.fonts;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.jpedal.fonts.glyph.T1Glyphs;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;

//<start-jfl>
import org.jpedal.io.PdfObjectReader;
import org.jpedal.io.ObjectStore;
import org.jpedal.parser.PdfStreamDecoder;
//<end-jfl>

import org.jpedal.utils.LogWriter;



/**
 * handlestype1 specifics
 */
public class Type1C extends Type1{

	final boolean  debugFont=false;
	
	final boolean debugDictionary=false;
	
	int ROS=-1,CIDFontVersion=0,CIDFontRevision=0,CIDFontType=0,CIDcount=0,UIDBase=-1,FDArray=-1,FDSelect=-1;
	
	final String[] OneByteCCFDict={"version","Notice","FullName","FamilyName","Weight",
			"FontBBox","BlueValues","OtherBlues","FamilyBlues","FamilyOtherBlues",
			"StdHW","StdVW","Escape","UniqueID","XUID",
			"charset","Encoding","CharStrings","Private", "Subrs",
			"defaultWidthX","nominalWidthX","-reserved-","-reserved-","-reserved-",
			"-reserved-","-reserved-","-reserved-","shortint","longint",
			"BCD","-reserved-"};
	
	final String[] TwoByteCCFDict={"Copyright","isFixedPitch","ItalicAngle","UnderlinePosition","UnderlineThickness",
			"PaintType","CharstringType","FontMatrix","StrokeWidth","BlueScale",
			"BlueShift","BlueFuzz","StemSnapH","StemSnapV","ForceBold",
			"-reserved-","-reserved-","LanguageGroup","ExpansionFactor","initialRandomSeed",
			"SyntheticBase","PostScript","BaseFontName","BaseFontBlend","-reserved-",
			"-reserved-","-reserved-","-reserved-","-reserved-","-reserved-",
			"ROS","CIDFontVersion","CIDFontRevision","CIDFontType","CIDCount",
			"UIDBase","FDArray","FDSelect","FontName"};
	
	//current location in file
	private int top = 0;

	private int charset = 0;

	private int enc = 0;

	private int charstrings = 0;
	
	private int stringIdx;

	private int stringStart;

	private int stringOffSize;

	private int privateDict = -1,privateDictOffset=-1;

	/** decoding table for Expert */
	private static final int ExpertSubCharset[] = { // 87
																					  // elements
		0,
			1,
			231,
			232,
			235,
			236,
			237,
			238,
			13,
			14,
			15,
			99,
			239,
			240,
			241,
			242,
			243,
			244,
			245,
			246,
			247,
			248,
			27,
			28,
			249,
			250,
			251,
			253,
			254,
			255,
			256,
			257,
			258,
			259,
			260,
			261,
			262,
			263,
			264,
			265,
			266,
			109,
			110,
			267,
			268,
			269,
			270,
			272,
			300,
			301,
			302,
			305,
			314,
			315,
			158,
			155,
			163,
			320,
			321,
			322,
			323,
			324,
			325,
			326,
			150,
			164,
			169,
			327,
			328,
			329,
			330,
			331,
			332,
			333,
			334,
			335,
			336,
			337,
			338,
			339,
			340,
			341,
			342,
			343,
			344,
			345,
			346 };


	/** lookup table for names for type 1C glyphs */
	private static final String type1CStdStrings[] = { // 391
																					   // elements
		".notdef",
			"space",
			"exclam",
			"quotedbl",
			"numbersign",
			"dollar",
			"percent",
			"ampersand",
			"quoteright",
			"parenleft",
			"parenright",
			"asterisk",
			"plus",
			"comma",
			"hyphen",
			"period",
			"slash",
			"zero",
			"one",
			"two",
			"three",
			"four",
			"five",
			"six",
			"seven",
			"eight",
			"nine",
			"colon",
			"semicolon",
			"less",
			"equal",
			"greater",
			"question",
			"at",
			"A",
			"B",
			"C",
			"D",
			"E",
			"F",
			"G",
			"H",
			"I",
			"J",
			"K",
			"L",
			"M",
			"N",
			"O",
			"P",
			"Q",
			"R",
			"S",
			"T",
			"U",
			"V",
			"W",
			"X",
			"Y",
			"Z",
			"bracketleft",
			"backslash",
			"bracketright",
			"asciicircum",
			"underscore",
			"quoteleft",
			"a",
			"b",
			"c",
			"d",
			"e",
			"f",
			"g",
			"h",
			"i",
			"j",
			"k",
			"l",
			"m",
			"n",
			"o",
			"p",
			"q",
			"r",
			"s",
			"t",
			"u",
			"v",
			"w",
			"x",
			"y",
			"z",
			"braceleft",
			"bar",
			"braceright",
			"asciitilde",
			"exclamdown",
			"cent",
			"sterling",
			"fraction",
			"yen",
			"florin",
			"section",
			"currency",
			"quotesingle",
			"quotedblleft",
			"guillemotleft",
			"guilsinglleft",
			"guilsinglright",
			"fi",
			"fl",
			"endash",
			"dagger",
			"daggerdbl",
			"periodcentered",
			"paragraph",
			"bullet",
			"quotesinglbase",
			"quotedblbase",
			"quotedblright",
			"guillemotright",
			"ellipsis",
			"perthousand",
			"questiondown",
			"grave",
			"acute",
			"circumflex",
			"tilde",
			"macron",
			"breve",
			"dotaccent",
			"dieresis",
			"ring",
			"cedilla",
			"hungarumlaut",
			"ogonek",
			"caron",
			"emdash",
			"AE",
			"ordfeminine",
			"Lslash",
			"Oslash",
			"OE",
			"ordmasculine",
			"ae",
			"dotlessi",
			"lslash",
			"oslash",
			"oe",
			"germandbls",
			"onesuperior",
			"logicalnot",
			"mu",
			"trademark",
			"Eth",
			"onehalf",
			"plusminus",
			"Thorn",
			"onequarter",
			"divide",
			"brokenbar",
			"degree",
			"thorn",
			"threequarters",
			"twosuperior",
			"registered",
			"minus",
			"eth",
			"multiply",
			"threesuperior",
			"copyright",
			"Aacute",
			"Acircumflex",
			"Adieresis",
			"Agrave",
			"Aring",
			"Atilde",
			"Ccedilla",
			"Eacute",
			"Ecircumflex",
			"Edieresis",
			"Egrave",
			"Iacute",
			"Icircumflex",
			"Idieresis",
			"Igrave",
			"Ntilde",
			"Oacute",
			"Ocircumflex",
			"Odieresis",
			"Ograve",
			"Otilde",
			"Scaron",
			"Uacute",
			"Ucircumflex",
			"Udieresis",
			"Ugrave",
			"Yacute",
			"Ydieresis",
			"Zcaron",
			"aacute",
			"acircumflex",
			"adieresis",
			"agrave",
			"aring",
			"atilde",
			"ccedilla",
			"eacute",
			"ecircumflex",
			"edieresis",
			"egrave",
			"iacute",
			"icircumflex",
			"idieresis",
			"igrave",
			"ntilde",
			"oacute",
			"ocircumflex",
			"odieresis",
			"ograve",
			"otilde",
			"scaron",
			"uacute",
			"ucircumflex",
			"udieresis",
			"ugrave",
			"yacute",
			"ydieresis",
			"zcaron",
			"exclamsmall",
			"Hungarumlautsmall",
			"dollaroldstyle",
			"dollarsuperior",
			"ampersandsmall",
			"Acutesmall",
			"parenleftsuperior",
			"parenrightsuperior",
			"twodotenleader",
			"onedotenleader",
			"zerooldstyle",
			"oneoldstyle",
			"twooldstyle",
			"threeoldstyle",
			"fouroldstyle",
			"fiveoldstyle",
			"sixoldstyle",
			"sevenoldstyle",
			"eightoldstyle",
			"nineoldstyle",
			"commasuperior",
			"threequartersemdash",
			"periodsuperior",
			"questionsmall",
			"asuperior",
			"bsuperior",
			"centsuperior",
			"dsuperior",
			"esuperior",
			"isuperior",
			"lsuperior",
			"msuperior",
			"nsuperior",
			"osuperior",
			"rsuperior",
			"ssuperior",
			"tsuperior",
			"ff",
			"ffi",
			"ffl",
			"parenleftinferior",
			"parenrightinferior",
			"Circumflexsmall",
			"hyphensuperior",
			"Gravesmall",
			"Asmall",
			"Bsmall",
			"Csmall",
			"Dsmall",
			"Esmall",
			"Fsmall",
			"Gsmall",
			"Hsmall",
			"Ismall",
			"Jsmall",
			"Ksmall",
			"Lsmall",
			"Msmall",
			"Nsmall",
			"Osmall",
			"Psmall",
			"Qsmall",
			"Rsmall",
			"Ssmall",
			"Tsmall",
			"Usmall",
			"Vsmall",
			"Wsmall",
			"Xsmall",
			"Ysmall",
			"Zsmall",
			"colonmonetary",
			"onefitted",
			"rupiah",
			"Tildesmall",
			"exclamdownsmall",
			"centoldstyle",
			"Lslashsmall",
			"Scaronsmall",
			"Zcaronsmall",
			"Dieresissmall",
			"Brevesmall",
			"Caronsmall",
			"Dotaccentsmall",
			"Macronsmall",
			"figuredash",
			"hypheninferior",
			"Ogoneksmall",
			"Ringsmall",
			"Cedillasmall",
			"questiondownsmall",
			"oneeighth",
			"threeeighths",
			"fiveeighths",
			"seveneighths",
			"onethird",
			"twothirds",
			"zerosuperior",
			"foursuperior",
			"fivesuperior",
			"sixsuperior",
			"sevensuperior",
			"eightsuperior",
			"ninesuperior",
			"zeroinferior",
			"oneinferior",
			"twoinferior",
			"threeinferior",
			"fourinferior",
			"fiveinferior",
			"sixinferior",
			"seveninferior",
			"eightinferior",
			"nineinferior",
			"centinferior",
			"dollarinferior",
			"periodinferior",
			"commainferior",
			"Agravesmall",
			"Aacutesmall",
			"Acircumflexsmall",
			"Atildesmall",
			"Adieresissmall",
			"Aringsmall",
			"AEsmall",
			"Ccedillasmall",
			"Egravesmall",
			"Eacutesmall",
			"Ecircumflexsmall",
			"Edieresissmall",
			"Igravesmall",
			"Iacutesmall",
			"Icircumflexsmall",
			"Idieresissmall",
			"Ethsmall",
			"Ntildesmall",
			"Ogravesmall",
			"Oacutesmall",
			"Ocircumflexsmall",
			"Otildesmall",
			"Odieresissmall",
			"OEsmall",
			"Oslashsmall",
			"Ugravesmall",
			"Uacutesmall",
			"Ucircumflexsmall",
			"Udieresissmall",
			"Yacutesmall",
			"Thornsmall",
			"Ydieresissmall",
			"001.000",
			"001.001",
			"001.002",
			"001.003",
			"Black",
			"Bold",
			"Book",
			"Light",
			"Medium",
			"Regular",
			"Roman",
			"Semibold" };

	/** Lookup table to map values */
	private static final int ISOAdobeCharset[] = { // 229
																					  // elements
		0,
			1,
			2,
			3,
			4,
			5,
			6,
			7,
			8,
			9,
			10,
			11,
			12,
			13,
			14,
			15,
			16,
			17,
			18,
			19,
			20,
			21,
			22,
			23,
			24,
			25,
			26,
			27,
			28,
			29,
			30,
			31,
			32,
			33,
			34,
			35,
			36,
			37,
			38,
			39,
			40,
			41,
			42,
			43,
			44,
			45,
			46,
			47,
			48,
			49,
			50,
			51,
			52,
			53,
			54,
			55,
			56,
			57,
			58,
			59,
			60,
			61,
			62,
			63,
			64,
			65,
			66,
			67,
			68,
			69,
			70,
			71,
			72,
			73,
			74,
			75,
			76,
			77,
			78,
			79,
			80,
			81,
			82,
			83,
			84,
			85,
			86,
			87,
			88,
			89,
			90,
			91,
			92,
			93,
			94,
			95,
			96,
			97,
			98,
			99,
			100,
			101,
			102,
			103,
			104,
			105,
			106,
			107,
			108,
			109,
			110,
			111,
			112,
			113,
			114,
			115,
			116,
			117,
			118,
			119,
			120,
			121,
			122,
			123,
			124,
			125,
			126,
			127,
			128,
			129,
			130,
			131,
			132,
			133,
			134,
			135,
			136,
			137,
			138,
			139,
			140,
			141,
			142,
			143,
			144,
			145,
			146,
			147,
			148,
			149,
			150,
			151,
			152,
			153,
			154,
			155,
			156,
			157,
			158,
			159,
			160,
			161,
			162,
			163,
			164,
			165,
			166,
			167,
			168,
			169,
			170,
			171,
			172,
			173,
			174,
			175,
			176,
			177,
			178,
			179,
			180,
			181,
			182,
			183,
			184,
			185,
			186,
			187,
			188,
			189,
			190,
			191,
			192,
			193,
			194,
			195,
			196,
			197,
			198,
			199,
			200,
			201,
			202,
			203,
			204,
			205,
			206,
			207,
			208,
			209,
			210,
			211,
			212,
			213,
			214,
			215,
			216,
			217,
			218,
			219,
			220,
			221,
			222,
			223,
			224,
			225,
			226,
			227,
			228 };
	/** lookup data to convert Expert values */
	private static final int ExpertCharset[] = { // 166
																				// elements
		0,
			1,
			229,
			230,
			231,
			232,
			233,
			234,
			235,
			236,
			237,
			238,
			13,
			14,
			15,
			99,
			239,
			240,
			241,
			242,
			243,
			244,
			245,
			246,
			247,
			248,
			27,
			28,
			249,
			250,
			251,
			252,
			253,
			254,
			255,
			256,
			257,
			258,
			259,
			260,
			261,
			262,
			263,
			264,
			265,
			266,
			109,
			110,
			267,
			268,
			269,
			270,
			271,
			272,
			273,
			274,
			275,
			276,
			277,
			278,
			279,
			280,
			281,
			282,
			283,
			284,
			285,
			286,
			287,
			288,
			289,
			290,
			291,
			292,
			293,
			294,
			295,
			296,
			297,
			298,
			299,
			300,
			301,
			302,
			303,
			304,
			305,
			306,
			307,
			308,
			309,
			310,
			311,
			312,
			313,
			314,
			315,
			316,
			317,
			318,
			158,
			155,
			163,
			319,
			320,
			321,
			322,
			323,
			324,
			325,
			326,
			150,
			164,
			169,
			327,
			328,
			329,
			330,
			331,
			332,
			333,
			334,
			335,
			336,
			337,
			338,
			339,
			340,
			341,
			342,
			343,
			344,
			345,
			346,
			347,
			348,
			349,
			350,
			351,
			352,
			353,
			354,
			355,
			356,
			357,
			358,
			359,
			360,
			361,
			362,
			363,
			364,
			365,
			366,
			367,
			368,
			369,
			370,
			371,
			372,
			373,
			374,
			375,
			376,
			377,
			378 };


    /** needed so CIDFOnt0 can extend */
    public Type1C() {}


        //<start-jfl>

        /** get handles onto Reader so we can access the file */
		public Type1C(PdfObjectReader current_pdf_file,String substiuteFont) {

				glyphs=new T1Glyphs(false);

				init(current_pdf_file);

				this.substituteFont=substiuteFont;
		}

        /** read details of any embedded fontFile */
		protected void readEmbeddedFont(Map values, Map fontDescriptor)
			throws Exception {

			//System.out.println(fontDescriptor);

			//if(renderPage){
				if(substituteFont!=null){

					//read details
					BufferedInputStream from = new BufferedInputStream(loader.getResourceAsStream(substituteFont));
					//create streams
					ByteArrayOutputStream to = new ByteArrayOutputStream();

					//write
					byte[] buffer = new byte[65535];
					int bytes_read;
					while ((bytes_read = from.read(buffer)) != -1)
						to.write(buffer, 0, bytes_read);

					to.close();
					from.close();

					/**load the font*/
					try{
						if (substituteFont.indexOf("/t1/") != -1)
							readType1FontFile(to.toByteArray());
						else
							readType1CFontFile(to.toByteArray());

					} catch (Exception e) {
						LogWriter.writeLog("[PDF]Substitute font="+substituteFont+"Type 1 exception=" + e);
					}

				}else{
					/** try type 1 first then type 1c/0c */
					String fontFileRef =(String) fontDescriptor.get("FontFile");
					//String fontFileRef =null;//(String) fontDescriptor.get("FontFile");

					if (fontFileRef != null) {
						try {
							//System.out.println("t1 " + ObjectStore.getCurrentFilename());
							readType1FontFile(currentPdfFile.readStream(fontFileRef,true));
						} catch (Exception e) {
							System.out.println("Type 1 exception=" + e);

						}

					}else{
						Object objectFontFileRef =fontDescriptor.get("FontFile3");

						try{
						if (objectFontFileRef != null){
							byte[] stream;
							if(objectFontFileRef instanceof String)
								stream=currentPdfFile.readStream((String)objectFontFileRef,true);
							else
								stream=(byte[]) ((Map)objectFontFileRef).get("DecodedStream");

							if(stream!=null) //if it fails, null returned
							readType1CFontFile(stream);

						}
						}catch(Exception e){
						}
					}
				}
		}

        /** read in a font and its details from the pdf file */
		public Map createFont(
                Map values,
                String fontID,
                boolean renderPage,
                Map descFontValues, ObjectStore objectStore)
			throws Exception {

			LogWriter.writeMethod("{readType1Font}" + values, 0);

			fontTypes = StandardFonts.TYPE1;

			Map fontDescriptor =super.createFont(values, fontID, renderPage, descFontValues, objectStore);

			if (fontDescriptor != null)
				readEmbeddedFont(values, fontDescriptor);

			readWidths(values);

            if(embeddedFontName!=null && is1C()){

                if(cleanupFonts || PdfStreamDecoder.runningStoryPad){
                	embeddedFontName= cleanupFontName(embeddedFontName);
                 
                	this.setBaseFontName(embeddedFontName);
                	this.setFontName(embeddedFontName);
                }
            }

            //make sure a font set
			if (renderPage)
				setFont(getBaseFontName(), 1);
			
			return fontDescriptor;
		}
        //<end-jfl>

    /** Constructor for OTF fonts */
    public Type1C(byte[] content, PdfJavaGlyphs glyphs) throws Exception{

        this.glyphs=glyphs;
        
        readType1CFontFile(content);

    }


    /** Handle encoding for type1C fonts. Also used for CIDFontType0C */
    final private void readType1CFontFile(byte[] content) throws Exception{

        LogWriter.writeMethod("{readType1CFontFile}", 0);

        LogWriter.writeLog("Embedded Type1C font used");

        glyphs.setis1C(true);

        //debugFont=getBaseFontName().equals("CVSTHK+GroThesis55Vz-Regular");

        if(debugFont)
            System.out.println(getBaseFontName());


        /**
         try {
         java.io.FileOutputStream fos=new java.io.FileOutputStream(getBaseFontName()+".1c");
         fos.write(content);
         fos.close();
         } catch (Exception e1) {
         e1.printStackTrace();
         }/***/

        int start; //pointers within table
        int size=2;

        /**
         * read Header
         */
        int major = content[0];
        int minor = content[1];
        if ((major != 1) | (minor != 0))
            LogWriter.writeLog("1C  format "+ major+ ":"+ minor+ " not fully supported");

        if(debugFont)
            System.out.println("major="+major+" minor="+minor);

        // read header size to workout start of names index
        top = content[2];

        /**
         * read names index
         */
        // read name index for the first font
        int count = getWord(content, top, size);
        int offsize = content[top + size];

        /**
         * get last offset and use to move to top dict index
         */
        top += (size+1);  //move pointer to start of font names
        start = top + (count + 1) * offsize - 1; //move pointer to end of offsets
        top = start + getWord(content, top + count * offsize, offsize);


        /**
         * read the dict index
         */
        count = getWord(content, top, size);
        offsize = content[top + size];

        top += (size+1); //update pointer
        start = top + (count + 1) * offsize - 1;

        int dicStart = start + getWord(content, top, offsize);
        int dicEnd = start + getWord(content, top + offsize, offsize);

        /**
         * read string index
         */
        String[] strings=readStringIndex(content,start,offsize,count);

        /**
         * read global subroutines (top set by Strings code)
         */
        readGlobalSubRoutines(content);

        /**
         * decode the dictionary
         */
        decodeDictionary(content,dicStart,dicEnd,strings);

        /**
         * allow  for subdictionaries in CID  font
         */
        if(FDSelect!=-1 ){

            try{

                if(debugDictionary)
                    System.out.println("=============FDSelect===================="+getBaseFontName());

                int nextDic=FDArray;

                count = getWord(content, nextDic, size);
                offsize = content[nextDic + size];

                nextDic += (size+1); //update pointer
                start = nextDic + (count + 1) * offsize - 1;
                dicStart = start+getWord(content, nextDic, offsize);
                dicEnd =start+getWord(content, nextDic + offsize, offsize);

                decodeDictionary(content,dicStart,dicEnd,strings);

                if(debugDictionary)
                    System.out.println("================================="+getBaseFontName());


            }catch(Exception ee){
                ee.printStackTrace();
                System.exit(1);
            }
        }

        /**
         * get number of glyphs from charstrings index
         */
        top = charstrings;

        int nGlyphs = getWord(content, top, size); //start of glyph index

        if(debugFont)
            System.out.println("nGlyphs="+nGlyphs);

        int[] names =readCharset(charset, nGlyphs, charstrings, content);

        if(debugFont){
            System.out.println("=======charset===============");
            int count2=names.length;
            for(int jj=0;jj<count2;jj++){
                System.out.println(jj+" "+names[jj]);
            }

            System.out.println("=======Encoding===============");
        }

        /**
         * set encoding if not set
         */
        setEncoding(content,nGlyphs,names);

        /**
         * read glyph index
         */
        top = charstrings;
        readGlyphs(content,nGlyphs, names);

         /**/
        if(privateDict!=-1){

            try{
                top = privateDict + privateDictOffset;

                if(top+2<content.length){
                    int nSubrs = getWord(content, top, size);
                    if(nSubrs>0)
                        readSubrs(content, nSubrs);
                }else if(debugFont || debugDictionary){
                    System.out.println("Private subroutine out of range");
                }
            }catch(Exception ee){
                ee.printStackTrace();
            }
        }
         /**/
        /**
         * set flags to tell software to use these descritpions
         */
        isFontEmbedded = true;

    }

	/**pick up encoding from embedded font*/
	final private void setEncoding(byte[] content,int nGlyphs,int[] names){

		LogWriter.writeMethod("{setEncoding}", 0);

		if(debugFont)
			System.out.println("Enc="+enc);
		
		// read encoding (glyph -> code mapping)
		if (enc == 0){
			embeddedEnc=StandardFonts.STD;
			if (fontEnc == -1)
			putFontEncoding(StandardFonts.STD);
		}else if (enc == 1){
			embeddedEnc=StandardFonts.MACEXPERT;
			if (fontEnc == -1)
			putFontEncoding(StandardFonts.MACEXPERT);
		}else { //custom mapping

			if(debugFont)
				System.out.println("custom mapping");
			
			top = enc;
			int encFormat = (content[top++] & 0xff),c;
			String name;

			if ((encFormat & 0x7f) == 0) { //format 0

				int nCodes = 1 + (content[top++] & 0xff);
				if (nCodes > nGlyphs)
					nCodes = nGlyphs;
				for (int i = 1; i < nCodes; ++i) {
					c =content[top++] & 0xff;
					name =getString(content,names[i],stringIdx,stringStart,stringOffSize);
					putChar(c, name);

				}

			} else if ((encFormat & 0x7f) == 1) { //format 1

				int nRanges = (content[top++] & 0xff);
				int nCodes = 1;
				for (int i = 0; i < nRanges; ++i) {
					c = (content[top++] & 0xff);
					int nLeft = (content[top++] & 0xff);
					for (int j = 0; j <= nLeft && nCodes < nGlyphs; ++j) {
						name =getString(content,names[nCodes],stringIdx,stringStart,stringOffSize);
						putChar(c, name);

						nCodes++;
						c++;
					}
				}
			}

			if ((encFormat & 0x80) != 0) { //supplimentary encodings

				int nSups = (content[top++] & 0xff);
				for (int i = 0; i < nSups; ++i) {
					c = (content[top++] & 0xff);
					int sid = getWord(content, top, 2);
					top += 2;
					name =getString(content,sid,stringIdx,stringStart,stringOffSize);
					putChar(c, name);

				}
			}
		}
	}
	
	// LILYPONDTOOL
	private final void readSubrs(byte[] content, int nSubrs) throws Exception {
		
		int subrOffSize = content[top+2];
		top+=3;
		int subrIdx = top;
		int subrStart = top + (nSubrs + 1) * subrOffSize - 1;
		top = subrStart + getWord(content, top+nSubrs*subrOffSize, subrOffSize);
		int[] subrOffset = new int [nSubrs + 2];
		int ii = subrIdx;
		for (int jj = 0; jj<nSubrs+1; jj++) {
			subrOffset[jj] = subrStart + getWord(content, ii, subrOffSize);
			ii += subrOffSize;
		}
		subrOffset[nSubrs + 1] = top;

		glyphs.setLocalBias(calculateSubroutineBias(nSubrs));
		
		//read the glyphs and store
		int current = subrOffset[0];
		for (int jj = 1; jj < nSubrs+1; jj++) {	
			ByteArrayOutputStream nextSubr = new ByteArrayOutputStream();
			
			for (int c = current; c < subrOffset[jj]; c++)
				nextSubr.write(content[c]);
			nextSubr.close();
			
			glyphs.setCharString("subrs"+(jj-1),nextSubr.toByteArray());
			current = subrOffset[jj];

		}
	}

	
	private final void readGlyphs(byte[] content,int nGlyphs,int[] names) throws Exception{

		LogWriter.writeMethod("{readGlyphs}"+nGlyphs, 0);
		try{
		int glyphOffSize = content[top + 2];
		top += 3;
		int glyphIdx = top;
		int glyphStart = top + (nGlyphs + 1) * glyphOffSize - 1;
		top =glyphStart+ getWord(content,top + nGlyphs * glyphOffSize,glyphOffSize);
		int[] glyphoffset = new int[nGlyphs + 2];

		int ii = glyphIdx;

		//read the offsets
		for (int jj = 0; jj < nGlyphs + 1; jj++) {

			glyphoffset[jj] = glyphStart+getWord(content,ii,glyphOffSize);
			ii = ii + glyphOffSize;

		}

		glyphoffset[nGlyphs + 1] = top;

		//read the glyphs and store
		int current = glyphoffset[0];
		for (int jj = 1; jj < nGlyphs+1; jj++) {
			
			ByteArrayOutputStream nextGlyph = new ByteArrayOutputStream();

			for (int c = current; c < glyphoffset[jj]; c++)
				nextGlyph.write(content[c]);
			nextGlyph.close();

			if((isCID)){
				glyphs.setCharString(""+names[jj-1],nextGlyph.toByteArray());
				
				if(debugFont)
					System.out.println("CIDglyph= "+names[jj-1]+" start="+current+" length="+glyphoffset[jj]);
				
			}else{
				String name=getString(content,names[jj-1],stringIdx,stringStart,stringOffSize);
				glyphs.setCharString(name,nextGlyph.toByteArray());
				
				if(debugFont)
					System.out.println("glyph= "+name+" start="+current+" length="+glyphoffset[jj]);
				
			
			}
			current = glyphoffset[jj];

		}
		}catch(Exception e){
		}
	}
	
	// LILYPONDTOOL
	private final int calculateSubroutineBias(int subroutineCount) {
		int bias;
		if (subroutineCount < 1240) {
			bias = 107;
		} else if (subroutineCount < 33900) {
			bias = 1131;
		} else {
			bias = 32768;
		}
		return bias;
	}
	
	private final void readGlobalSubRoutines(byte[] content) throws Exception{

		LogWriter.writeMethod("{readGlobalSubRoutines}", 0);

		int subOffSize = (content[top + 2] & 0xff);
		int count= getWord(content, top, 2);
		top += 3;
		if(count>0){

			int idx = top;
			int start = top + (count + 1) * subOffSize - 1;
			top =start+ getWord(content,top + count * subOffSize,subOffSize);
			int[] offset = new int[count + 2];

			int ii = idx;

			//read the offsets
			for (int jj = 0; jj < count + 1; jj++) {

				offset[jj] = start + getWord(content,ii,subOffSize);
				ii = ii + subOffSize;

			}

			offset[count + 1] = top;

			// LILYPONDTOOL
			glyphs.setGlobalBias(calculateSubroutineBias(count));
			
			//read the subroutines and store
			int current = offset[0];
			for (int jj = 1; jj < count+1; jj++) {

				ByteArrayOutputStream nextStream = new ByteArrayOutputStream();
				for (int c = current; c < offset[jj]; c++)
					nextStream.write(content[c]);
				nextStream.close();

				//store
				glyphs.setCharString("global"+(jj-1),nextStream.toByteArray());
				
				//setGlobalSubroutine(new Integer(jj-1+bias),nextStream.toByteArray());
				current = offset[jj];

			}
		}
	}

	private void decodeDictionary(byte[] content,int dicStart,int dicEnd,String[] strings){

        boolean fdReset=false;

		LogWriter.writeMethod("{decodeDictionary}", 0);

		if(debugDictionary)
			System.out.println("=============Read dictionary===================="+getBaseFontName());
		
		int p = dicStart, nextVal,key;
		int i=0;
		double[] op = new double[48]; //current operand in dictionary

        while (p < dicEnd) {

            nextVal = content[p] & 0xFF;
			if (nextVal <= 27 || nextVal == 31) { // operator

				key = nextVal;

				p++;
				
				if(debugDictionary && key!=12)
					System.out.println(key +" (1) "+OneByteCCFDict[key]);
				
				if (key == 0x0c) { //handle escaped keys
					key = content[p] & 0xFF;

					if(debugDictionary)
						System.out.println(key +" (2) "+TwoByteCCFDict[key]);
					
					p++;

                    if(key!=36 && key!=37 && FDSelect!=-1){
                        if(debugDictionary){
							System.out.println("Ignored as part of FDArray ");

                            for(int ii=0;ii<6;ii++)
							System.out.println(op[ii]);
                        }
                    }else if (key == 2) { //italic
						
                    	italicAngle=(int)op[0];
						if(debugDictionary)
							System.out.println("Italic="+op[0]);
					
                    }else if (key == 7) { //fontMatrix
						System.arraycopy(op, 0, FontMatrix, 0, 6);
						
						if(debugDictionary){
							for(int ii=0;ii<6;ii++)
							System.out.println(ii+"="+op[ii]);
						}
					}else if (key == 30) { //ROS
						ROS=(int)op[0];
						isCID=true;
						if(debugDictionary)
							System.out.println(op[0]);
					}else if(key==31){ //CIDFontVersion
						CIDFontVersion=(int)op[0];
						if(debugDictionary)
							System.out.println(op[0]);
					}else if(key==32){ //CIDFontRevision
						CIDFontRevision=(int)op[0];
						if(debugDictionary)
							System.out.println(op[0]);		
					}else if(key==33){ //CIDFontType
						CIDFontType=(int)op[0];
						if(debugDictionary)
							System.out.println(op[0]);			
					}else if(key==34){ //CIDcount
						CIDcount=(int)op[0];
						if(debugDictionary)
							System.out.println(op[0]);		
					}else if(key==35){ //UIDBase
						UIDBase=(int)op[0];
						if(debugDictionary)
							System.out.println(op[0]);	
					}else if(key==36){ //FDArray
						FDArray=(int)op[0];
						if(debugDictionary)
							System.out.println(op[0]);

                    }else if(key==37){ //FDSelect
						FDSelect=(int)op[0];

                        fdReset=true;

                        if(debugDictionary)
							System.out.println(op[0]);
                    }else if(key==0){ //copyright

                        int id=(int)op[0];
                        if(id>390)
                        id=id-390;
                        copyright=strings[id];
                        if(debugDictionary)
                        System.out.println("copyright= "+copyright);
                    }else if(key==21){ //Postscript

                        int id=(int)op[0];
                        if(id>390)
                        id=id-390;
                        //postscriptFontName=strings[id];
                        if(debugDictionary)  {
                            System.out.println("Postscript= "+strings[id]);
                            System.out.println(TwoByteCCFDict[key]+" "+op[0]);
                        }
                    }else if(key==22){ //BaseFontname

                        int id=(int)op[0];
                        if(id>390)
                        id=id-390;
                        //baseFontName=strings[id];
                        if(debugDictionary){
                            System.out.println("BaseFontname= "+embeddedFontName);
                            System.out.println(TwoByteCCFDict[key]+" "+op[0]);
                        }
                    }else if(key==38){ //fullname

                        int id=(int)op[0];
                        if(id>390)
                        id=id-390;
                        //fullname=strings[id];
                        if(debugDictionary){
                            System.out.println("fullname= "+strings[id]);
                            System.out.println(TwoByteCCFDict[key]+" "+op[0]);
                        }

                    }else if(debugDictionary)
						System.out.println(op[0]);

				} else {

                    if(key==2){ //fullname

                        int id=(int)op[0];
                        if(id>390)
                        id=id-390;
                        embeddedFontName=strings[id];
                        if(debugDictionary){
                            System.out.println("name= "+embeddedFontName);
                            System.out.println(OneByteCCFDict[key]+" "+op[0]);
                        }

                    }else if(key==3){ //familyname

                        int id=(int)op[0];
                        if(id>390)
                        id=id-390;
                        //embeddedFamilyName=strings[id];
                        if(debugDictionary){
                            System.out.println("FamilyName= "+embeddedFamilyName);
                            System.out.println(OneByteCCFDict[key]+" "+op[0]);
                        }

                    }else if (key == 5) { //fontBBox
						if(debugDictionary){
							for(int ii=0;ii<4;ii++)
							System.out.println(op[ii]);
						}
						//for(int ii=0;ii<4;ii++){f
							//System.out.println(this.baseFontName+" "+ii+" "+op[ii]);
						//	this.FontBBox[ii]=(float) op[ii];
						//}
					}else if (key == 0x0f) { // charset
						charset = (int) op[0];
						
						if(debugDictionary)
							System.out.println(op[0]);
						
					} else if (key == 0x10) { // encoding
						enc = (int) op[0];
						
						if(debugDictionary)
							System.out.println(op[0]);
						
					} else if (key == 0x11) { // charstrings
						charstrings = (int) op[0];
						
						if(debugDictionary)
							System.out.println(op[0]);
						
						//System.out.println("charStrings="+charstrings);
					} else if (key == 18 && glyphs.is1C()) { // readPrivate
						privateDict = (int) op[1];
						privateDictOffset = (int) op[0];
						
						if(debugDictionary)
						System.out.println("privateDict="+op[0]+" Offset="+op[1]);

                    }else if(debugDictionary){
						
						// System.out.println(p+" "+key+" "+T1CcharCodes1Byte[key]+" <<<"+op);
						 
						System.out.println("Other value "+key);
						 /**if(op <type1CStdStrings.length)
						 System.out.println(type1CStdStrings[(int)op]);
						 else if((op-390) <strings.length)
						 System.out.println("interesting key:"+key);*/
					}
					//System.out.println(p+" "+key+" "+raw1ByteValues[key]+" <<<"+op);
				}

				i=0;

			}else{
                p=glyphs.getNumber(content, p,op,i,false);
                i++;
			}
		}
		
		if(debugDictionary)
			System.out.println("================================="+getBaseFontName());

        //reset
        if(!fdReset)
        FDSelect=-1;

    }

	private String[] readStringIndex(byte[] content,int start,int offsize,int count){

		LogWriter.writeMethod("{readStringIndex}", 0);

		top = start + getWord(content, top + count * offsize, offsize);
		//start of string index
		int nStrings = getWord(content, top, 2);
		stringOffSize = content[top + 2];

		top += 3;
		stringIdx = top;
		stringStart = top + (nStrings + 1) * stringOffSize - 1;
		top =stringStart+ getWord(content,top + nStrings * stringOffSize,stringOffSize);
		int[] offsets = new int[nStrings + 2];
		String[] strings = new String[nStrings + 2];

		int ii = stringIdx;
		//read the offsets
		for (int jj = 0; jj < nStrings + 1; jj++) {

			offsets[jj] = getWord(content,ii,stringOffSize); //content[ii] & 0xff;
			//getWord(content,ii,stringOffSize);
			ii = ii + stringOffSize;

		}

		offsets[nStrings + 1] = top - stringStart;

		//read the strings
		int current = 0;
		for (int jj = 0; jj < nStrings + 1; jj++) {

			StringBuffer nextString = new StringBuffer();
			for (int c = current; c < offsets[jj]; c++)
				nextString.append((char) content[stringStart + c]);

			if(debugFont)
				System.out.println("String "+jj+" ="+nextString);
			
			strings[jj] = nextString.toString();
			current = offsets[jj];

        }
		return strings;
	}

	/** Utility method used during processing of type1C files */
	final private String getString(byte[] content,int sid,int idx,int start,int offsize) {

		int len;
		String result = null;

		if (sid < 391)
			result = type1CStdStrings[sid];
		else {
			sid -= 391;
			int idx0 =start+ getWord(content,idx + sid * offsize,offsize);
			int idxPtr1 =start+ getWord(content,idx + (sid + 1) * offsize,offsize);
			//System.out.println(sid+" "+idx0+" "+idxPtr1);
			if ((len = idxPtr1 - idx0) > 255)
				len = 255;
			result = new String(content, idx0, len);
		}
		return result;
	}

	/** get standard charset or extract from type 1C font */
	final private int[] readCharset(int charset,int nGlyphs,int top,byte[] content) {

		LogWriter.writeMethod("{get1CCharset}+"+charset+" glyphs="+nGlyphs, 0);

		int glyphNames[] = null;
		int i, j;
		
		if(debugFont)
			System.out.println("charset="+charset);

		/**
		//handle CIDS first
		if(isCID){
			glyphNames = new int[nGlyphs];
			glyphNames[0] = 0;

			for (i = 1; i < nGlyphs; ++i) {
				glyphNames[i] = (int) getWord(content, top, 2);
				if(fontID.equals("C0_0")){
				System.out.println(i+" "+(int)glyphNames[i]);
				}
				top += 2;
			}


		// read appropriate non-CID charset
		}else */if (charset == 0)
			glyphNames = ISOAdobeCharset;
		else if (charset == 1)
			glyphNames = ExpertCharset;
		else if (charset == 2)
			glyphNames = ExpertSubCharset;
		else {
			glyphNames = new int[nGlyphs];
			glyphNames[0] = 0;
			top = charset;
			int charsetFormat = content[top++] & 0xff;

			if(debugFont)
				System.out.println("charsetFormat="+charsetFormat);
			
			if (charsetFormat == 0) {
				for (i = 1; i < nGlyphs; ++i) {
					glyphNames[i] = getWord(content, top, 2);
					top += 2;
				}

			} else if (charsetFormat == 1) {
				i = 1;
				while (i < nGlyphs) {

					int c = getWord(content, top, 2);
					top += 2;
					int nLeft = content[top++] & 0xff;

					for (j = 0; j <= nLeft; ++j){
						glyphNames[i++] =c++;
						
					}
				}
			} else if (charsetFormat == 2) {
				i = 1;
				while (i < nGlyphs) {
					int c = getWord(content, top, 2);
					top += 2;
					int nLeft = getWord(content, top, 2);
					top += 2;
					for (j = 0; j <= nLeft; ++j)
						glyphNames[i++] =c++;
				}
			}
		}

		return glyphNames;
	}

	/** Utility method used during processing of type1C files */
	final private int getWord(byte[] content, int index, int size) {
		int result = 0;
		for (int i = 0; i < size; i++) {
			result = (result << 8) + (content[index + i] & 0xff);

		}
		return result;
	}

}



