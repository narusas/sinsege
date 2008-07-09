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
* CIDFontType2.java
* ---------------
* (C) Copyright 2004, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal.fonts;

import java.util.Map;

import org.jpedal.fonts.tt.TTGlyphs;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.io.ObjectStore;
import org.jpedal.utils.LogWriter;


/**
 * handles truetype specifics
 *  */
public class CIDFontType2 extends TrueType {

	/**get handles onto Reader so we can access the file*/
	public CIDFontType2(PdfObjectReader currentPdfFile) {

		isCIDFont=true;
		TTstreamisCID=true;

		glyphs=new TTGlyphs();

		init(currentPdfFile);

	}

	/**get handles onto Reader so we can access the file*/
	public CIDFontType2(PdfObjectReader currentPdfFile,boolean ttflag) {

		isCIDFont=true;
		TTstreamisCID=ttflag;

		glyphs=new TTGlyphs();
		
		init(currentPdfFile);

	}

	/**read in a font and its details from the pdf file*/
	public Map createFont(Map values, String fontID, boolean renderPage, Map descFontValues, ObjectStore objectStore) throws Exception{

		LogWriter.writeMethod("{readFontType0 "+fontID+"}", 0);

		Map fontDescriptor=null;

		fontTypes=StandardFonts.CIDTYPE2;
		this.fontID=fontID;

		fontDescriptor=createCIDFont(values,descFontValues);

		/**/
		//System.err.println(fontDescriptor);
		//System.err.println(descFontValues);
		//System.err.println(values);
		//System.exit(1);
		/***/

		if (fontDescriptor!= null) {

			String fontFileRef = (String) fontDescriptor.get("FontFile2");
			if (fontFileRef != null) {
				if(renderPage)
					readEmbeddedFont(currentPdfFile.readStream(fontFileRef,true),hasEncoding,false);


			}
		}

		//setup and substitute font
		if((renderPage)&&(!isFontEmbedded)&&(this.substituteFontFile!=null)){
			this.substituteFontUsed(substituteFontFile,substituteFontName);
			isFontSubstituted=true;
			this.isFontEmbedded=true;
		}

			//make sure a font set
			if (renderPage)
				setFont(getBaseFontName(), 1);
			
			if(!isFontEmbedded)
				selectDefaultFont();


		return fontDescriptor;
	}
}
