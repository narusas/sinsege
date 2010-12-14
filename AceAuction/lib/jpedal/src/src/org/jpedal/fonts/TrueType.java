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
* TrueType.java
* ---------------
* (C) Copyright 2004, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal.fonts;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.jpedal.exception.PdfFontException;
import org.jpedal.fonts.tt.TTGlyphs;
//<start-jfl>
import org.jpedal.io.PdfObjectReader;
import org.jpedal.io.ObjectStore;
//<end-jfl>
import org.jpedal.utils.LogWriter;

/**
 * handles truetype specifics
 *  */
public class  TrueType extends PdfFont {

	public TrueType(){

	}




	public void readFontData(byte[] fontData){

		LogWriter.writeMethod("{readFontData}", 0);

		fontTypes=glyphs.readEmbeddedFont(TTstreamisCID,fontData);



	}

	/**allows us to substitute a font to use for display
	 * @throws PdfFontException */
	protected void substituteFontUsed(String substituteFontFile,String substituteFontName) throws PdfFontException{

		InputStream from=null;

		//process the font data
		try {

			from =loader.getResourceAsStream("org/jpedal/res/fonts/" + substituteFontFile);
			//from =new FileInputStream("/Applications/Adobe Reader 7.0.app/Contents/MacOS/Resource/CIDFont/KozMinProVI-Regular.otf");
		} catch (Exception e) {
			System.err.println("Exception " + e + " reading "+substituteFontFile+" Check cid  jar installed");
			LogWriter.writeLog("Exception " + e + " reading "+substituteFontFile+" Check cid  jar installed");
		}

		if(from==null)
			throw new PdfFontException("Unable to load font "+substituteFontFile);

		try{

			//create streams
			//BufferedInputStream from = new BufferedInputStream(new FileInputStream("/home/markee/workspace/jpedalDEV/ttfonts/"+substituteFontFile));
			ByteArrayOutputStream to = new ByteArrayOutputStream();

			//write
			byte[] buffer = new byte[65535];
			int bytes_read;
			while ((bytes_read = from.read(buffer)) != -1)
				to.write(buffer, 0, bytes_read);

			to.close();
			from.close();

			readFontData(to.toByteArray());
			glyphs.setEncodingToUse(hasEncoding,this.getFontEncoding(false),true,isCIDFont);

			isFontEmbedded=true;

		} catch (Exception e) {
			System.err.println("Exception " + e + " reading "+substituteFontFile+" Check cid  jar installed");
			LogWriter.writeLog("Exception " + e + " reading "+substituteFontFile+" Check cid  jar installed");
		}

	}

    /**entry point when using generic renderer*/
	public TrueType(String substituteFont) {

		glyphs=new TTGlyphs();

        //<start-jfl>
        init(null);
        /**
        //<end-jfl>
        //setup font size and initialise objects
		if(isCIDFont)
			maxCharCount=65536;

		glyphs.init(maxCharCount,isCIDFont);
        /**/

        this.substituteFont=substituteFont;

	}

    //<start-jfl>
	/**get handles onto Reader so we can access the file*/
	public TrueType(PdfObjectReader current_pdf_file,String substituteFont) {

			glyphs=new TTGlyphs();

			init(current_pdf_file);
			this.substituteFont=substituteFont;

	}

	/**read in a font and its details from the pdf file*/
	public Map createFont(Map values, String fontID, boolean renderPage, Map descFontValues, ObjectStore objectStore) throws Exception{

		LogWriter.writeMethod("{readTrueTypeFont}"+values, 0);

		fontTypes=StandardFonts.TRUETYPE;


		Map fontDescriptor =super.createFont(values, fontID,renderPage,descFontValues, objectStore);

		if(renderPage){
			if ((fontDescriptor!= null)) {

				Object fontFileRef =fontDescriptor.get("FontFile2");

				try{
				if (fontFileRef != null){
					byte[] stream;
					if(fontFileRef instanceof String)
						stream=currentPdfFile.readStream((String)fontFileRef,true);
					else
						stream=(byte[]) ((Map)fontFileRef).get("DecodedStream");

					readEmbeddedFont(stream,hasEncoding,false);

				}
				}catch(Exception e){
				}
			}

			if((!isFontEmbedded)&&(substituteFont!=null)){

				//over-ride font remapping if substituted
				if(glyphs.remapFont)
						glyphs.remapFont=false;

				//read details
				BufferedInputStream from;

				InputStream jarFile = loader.getResourceAsStream(substituteFont);
				if(jarFile==null)
					from=new BufferedInputStream(new FileInputStream(substituteFont));
				else
					from= new BufferedInputStream(jarFile);

				//create streams
				ByteArrayOutputStream to = new ByteArrayOutputStream();

				//write
				byte[] buffer = new byte[65535];
				int bytes_read;
				while ((bytes_read = from.read(buffer)) != -1)
					to.write(buffer, 0, bytes_read);

				to.close();
				from.close();
				readEmbeddedFont(to.toByteArray(),false,true);

				isFontSubstituted=true;

			}
		}

		readWidths(values);

		//make sure a font set
		if (renderPage)
			setFont(glyphs.fontName, 1);
		
		return fontDescriptor ;
	}
    //<end-jfl>

    /**read in a font and its details for generic usage*/
	public void createFont(String fontName) throws Exception{

		fontTypes=StandardFonts.TRUETYPE;

		setBaseFontName(fontName);

		//read details
		BufferedInputStream from;

		InputStream jarFile = loader.getResourceAsStream(substituteFont);
		if(jarFile==null)
			from=new BufferedInputStream(new FileInputStream(substituteFont));
		else
			from= new BufferedInputStream(jarFile);

		//create streams
		ByteArrayOutputStream to = new ByteArrayOutputStream();

		//write
		byte[] buffer = new byte[65535];
		int bytes_read;
		while ((bytes_read = from.read(buffer)) != -1)
			to.write(buffer, 0, bytes_read);

		to.close();
		from.close();
		readEmbeddedFont(to.toByteArray(),false,true);

		isFontSubstituted=true;

	}

	/**
	 * read truetype font data and also install font onto System
	 * so we can use
	 */
	final protected void readEmbeddedFont(byte[] font_data,boolean hasEncoding,boolean isSubstituted) {

		LogWriter.writeMethod("{readEmbeddedFont}", 0);

		/**
		try {
			java.io.FileOutputStream fos=new java.io.FileOutputStream(getBaseFontName().substring(7)+font_data.length+".bin");
			fos.write(font_data);
			fos.close();
			System.out.println(getBaseFontName().substring(7)+".ttf");

		} catch (Exception e1) {
			e1.printStackTrace();
		}/***/

		//process the font data
		try {

			LogWriter.writeLog("Embedded TrueType font used");

//			System.out.println("init font "+this.baseFontName);
//			javaFont=Font.createFont(Font.TRUETYPE_FONT,new ByteArrayInputStream(font_data));
//			System.out.println(javaFont);
//
			readFontData(font_data);

			isFontEmbedded=true;

			glyphs.setEncodingToUse(hasEncoding,this.getFontEncoding(false),isSubstituted,TTstreamisCID);

		} catch (Exception e) {
			LogWriter.writeLog("Exception " + e + " processing TrueType font");
			e.printStackTrace();
		}
	}

}
