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
* 20040402	Added
*/
package org.jpedal.fonts.glyph;

import java.awt.Graphics2D;
import java.awt.geom.Area;

import org.jpedal.color.PdfPaint;

/**
 * base glyph used by T1 and Truetype fonts
 */
public interface PdfGlyph {
	
	
	/**draw the glyph*/
	public abstract void render(int text_fill_type, Graphics2D g2, boolean debug, float scaling);

	/**
	 * return max possible glyph width in absolute units
	 */
	public abstract float getmaxWidth();

	/**
	 * return max possible glyph width in absolute units
	 */
	public abstract int getmaxHeight();

	/**
	 * used by type3 glyphs to set colour if required
	 */
	public abstract void lockColors(PdfPaint strokeColor, PdfPaint nonstrokeColor);

	/**
	 * see if we ignore colours for type 3 font
	 */
	public abstract boolean ignoreColors();

	public abstract Area getShape();

    //used by Type3 fonts
    String getGlyphName();

	public abstract void setWidth(float width);
}