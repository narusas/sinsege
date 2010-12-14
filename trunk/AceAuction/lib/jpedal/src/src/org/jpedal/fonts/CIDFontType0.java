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
* CIDFontType0.java
* ---------------
* (C) Copyright 2004, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal.fonts;

import java.util.Map;

import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.glyph.T1Glyphs;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.io.ObjectStore;
import org.jpedal.utils.LogWriter;


/**
 * handles truetype specifics
 *  */
public class CIDFontType0 extends Type1C {

	/**used to display non-embedded fonts*/
	private CIDFontType2 subFont=null;

	/**get handles onto Reader so we can access the file*/
	public CIDFontType0(PdfObjectReader currentPdfFile) {

		glyphs=new T1Glyphs(true);

		isCIDFont=true;
		TTstreamisCID=true;
		init(currentPdfFile);
		this.currentPdfFile=currentPdfFile;

	}

	/**read in a font and its details from the pdf file*/
	public Map createFont(Map values, String fontID, boolean renderPage, Map descFontValues, ObjectStore objectStore) throws Exception{

		LogWriter.writeMethod("{readCIDFONT0 "+fontID+"}", 0);

		fontTypes=StandardFonts.CIDTYPE0;
		this.fontID=fontID;

		Map fontDescriptor=createCIDFont(values,descFontValues);

		if (fontDescriptor!= null)
			readEmbeddedFont(values,fontDescriptor);

		if((renderPage)&&(!isFontEmbedded)&&(this.substituteFontFile!=null)){

			isFontSubstituted=true;
			subFont=new CIDFontType2(currentPdfFile,TTstreamisCID);

			subFont.substituteFontUsed(substituteFontFile,substituteFontName);
			this.isFontEmbedded=true;

		}

		if(!isFontEmbedded)
			selectDefaultFont();

		//make sure a font set
		if (renderPage)
			setFont(getBaseFontName(), 1);
		
		return fontDescriptor ;
	}


	/**
	 * used by  non type3 font
	 */
	public PdfJavaGlyphs getGlyphData(){

		if(subFont!=null)
			return subFont.getGlyphData();
		else
			return glyphs;
		
	}

}
