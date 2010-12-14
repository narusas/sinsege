/**
* ===========================================
* Storypad
* ===========================================
*
* Project Info:  http://www.idrsolutions.com
* Project Lead:  Mark Stephens (mark@idrsolutions.com)
*
* (C) Copyright 2004, IDRsolutions and Contributors.
*
* ---------------
* GlyphFactory.java.java
* ---------------
* (C) Copyright 2002, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal.fonts.glyph;


/**
 * template for glyph creation routines
 */
public interface GlyphFactory {
    
    /**
     * set fontMatrix and zero all arrays
     */
    void reinitialise(double[] fontMatrix);

    /**
     * @return
     */
    PdfGlyph getGlyph(boolean debug);

    /**
     * @param f
     * @param g
     * @param h
     * @param i
     * @param j
     * @param k
     */
    void curveTo(float f, float g, float h, float i, float j, float k);

    /**
     * 
     */
    void closePath();

    /**
     * @param f
     * @param g
     */
    void moveTo(float f, float g);

    /**
     * @param f
     * @param g
     */
    void lineTo(float f, float g);

    /**
     * @param f
     * @param g
     */
    void setYMin(float f, float g);

    int getLSB();
}
