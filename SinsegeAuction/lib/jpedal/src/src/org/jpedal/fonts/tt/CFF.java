/**
 * ===========================================
 * Java Pdf Extraction Decoding Access Library
 * ===========================================
 *
 * Project Info:  http://www.jpedal.org
 * Project Lead:  Mark Stephens
 *
 * (C) Copyright 2007, IDRsolutions and Contributors.
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

  * CFF.java
  * ---------------
  * (C) Copyright 2007, by IDRsolutions and Contributors.
  *
  *
  * --------------------------
 */
package org.jpedal.fonts.tt;

import org.jpedal.utils.LogWriter;
import org.jpedal.fonts.Type1C;
import org.jpedal.fonts.glyph.PdfGlyph;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.glyph.T1Glyphs;
import org.jpedal.fonts.glyph.GlyphFactory;

public class CFF extends Table {

    Type1C cffData;

    PdfJavaGlyphs glyphs=new T1Glyphs(false);

    boolean hasCFFdata=false;

    public CFF(FontFile2 currentFontFile){

		LogWriter.writeMethod("{readCFFTable}", 0);

        //move to start and check exists
		int startPointer=currentFontFile.selectTable(FontFile2.CFF);

        //read 'cff' table
		if(startPointer!=0){

            int length=currentFontFile.getTableSize(FontFile2.CFF);

            byte[] data=currentFontFile.readBytes(startPointer, length) ;

            try {
                cffData=new Type1C(data,glyphs);

                hasCFFdata=true;
            } catch (Exception e) {
                e.printStackTrace(); 
            }
		}
    }

    public boolean hasCFFData() {
        return hasCFFdata;
    }


    public PdfGlyph getCFFGlyph(GlyphFactory factory,String glyph, float[][] Trm,int rawInt, String displayValue, float currentWidth,String key) {

        return glyphs.getEmbeddedGlyph(factory, glyph, Trm, rawInt, displayValue, currentWidth, key);

    }
}
