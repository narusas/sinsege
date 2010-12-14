/**
 * ===========================================
 * Java Pdf Extraction Decoding Access Library
 * ===========================================
 *
 * Project Info:  http://www.jpedal.org
 * Project Lead:  Mark Stephens (mark@idrsolutions.com)
 *
 * (C) Copyright 2006, IDRsolutions and Contributors.
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
  * PdfJavaGlyphs.java
  * ---------------
  * (C) Copyright 2005, by IDRsolutions and Contributors.
  *
  * 
  * --------------------------
 */
package org.jpedal.fonts.glyph;

//<start-jfl>
import org.jpedal.PdfDecoder;
//<end-jfl>

import java.awt.geom.Area;
import java.awt.geom.AffineTransform;
import java.awt.font.GlyphVector;
import java.awt.font.FontRenderContext;
import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PdfJavaGlyphs implements PdfGlyphs,Serializable{

	/**shapes we have already drawn to speed up plotting, or <code>null</code> if there are none*/
	public Area[] cachedShapes = null;

	boolean isFontInstalled = false;

	/**default font to use in display*/
	public String defaultFont = "Lucida Sans";

	/**lookup for font names less any + suffix*/
	public String fontName="default";
	
	Map chars=new HashMap();
	Map displayValues=new HashMap();
	Map embeddedChars=new HashMap();
	

	public String getBaseFontName() {
		return baseFontName;
	}

	public void setBaseFontName(String baseFontName) {
		this.baseFontName=baseFontName;
	}

	public String baseFontName="";

	public boolean isSubsetted;


	/**copy of Trm so we can choose if cache should be flushed*/
	public float[][] lastTrm=new float[3][3];

	/**current font to plot, or <code>null</code> if not used yet*/
	private Font unscaledFont = null;

	boolean isArialInstalledLocally;
	private int maxCharCount=255;
	private boolean isCIDFont;

	/**make 256 value fonts to f000 range if flag set*/
	public boolean remapFont=false;

	public String font_family_name;

	public int style;


	/**used to render page by drawing routines*/
	private static FontRenderContext frc =new FontRenderContext(null, true, true);

	/**list of installed fonts*/
	public static String[] fontList;


	/**
	 * used for standard non-substituted version
	 * @param Trm
	 * @param rawInt
	 * @param displayValue
	 * @param currentWidth
	 */
	public Area getStandardGlyph(float[][]Trm, int rawInt, String displayValue, float currentWidth) {

		/**flush cache if needed*/
		if((lastTrm[0][0]!=Trm[0][0])|(lastTrm[1][0]!=Trm[1][0])|
				(lastTrm[0][1]!=Trm[0][1])|(lastTrm[1][1]!=Trm[1][1])){
			lastTrm =Trm;
			cachedShapes = null;
		}

		//either calculate the glyph to draw or reuse if alreasy drawn
		Area transformedGlyph2 = getCachedShape(rawInt);

		double dY = -1,dX=1;

		//allow for text running up the page
		if (((Trm[1][0] < 0)&(Trm[0][1] >= 0))|((Trm[0][1] < 0)&(Trm[1][0] >= 0))) {
			dX=1f;
			dY =-1f;

		}

		/**else if ((Trm[1][0] < 0)|(Trm[0][1] < 0)) {
		 dX=-dX;
		 dY =-dY;
		 } */

		if (transformedGlyph2 == null) {

			//remap font if needed
//			String xx=displayValue;
//			if((remapFont)&&(getUnscaledFont().canDisplay(xx.charAt(0))==false))
//				xx=""+(char)(rawInt+ 0xf000);

			transformedGlyph2=getGlyph(rawInt, displayValue, currentWidth);

			if(transformedGlyph2!=null){

				//create shape for text using tranformation to make correct size

				double y=0;

				//hack to fix problem with Java Arial font
				if(rawInt==146 && isArialInstalledLocally){
					y=(dY*Trm[1][1])/2;
					y=-(transformedGlyph2.getBounds().height-transformedGlyph2.getBounds().y);
				}

				AffineTransform at =new AffineTransform(dX*Trm[0][0],dX*Trm[0][1],dY*Trm[1][0],dY*Trm[1][1] ,0,y);

				transformedGlyph2.transform( at);

			}

			//save so we can reuse if it occurs again in this TJ command
			setCachedShape(rawInt, transformedGlyph2);

		}

		return transformedGlyph2;
	}

	/**
	 * used by  non-embedded font where not standard
	 */
	public Area getApproximateGlyph(float[][]Trm, int rawInt, String displayValue, float currentWidth) {

		/**flush cache if needed*/
		if((lastTrm[0][0]!=Trm[0][0])|(lastTrm[1][0]!=Trm[1][0])|
				(lastTrm[0][1]!=Trm[0][1])|(lastTrm[1][1]!=Trm[1][1])){
			lastTrm=Trm;
			cachedShapes = null;
		}

		//either calculate the glyph to draw or reuse if alreasy drawn
		Area transformedGlyph2 = getCachedShape(rawInt);

		double dY = -1,dX=1;

		//allow for text running up the page
		if (((Trm[1][0] < 0)&(Trm[0][1] >= 0))|((Trm[0][1] < 0)&(Trm[1][0] >= 0))) {
			dX=1f;
			dY =-1f;

		}

		/**else
		 if ((Trm[1][0] < 0)|(Trm[0][1] < 0)) {
		 dX=-dX;
		 dY =-dY;
		 }
		 */

		//boolean fontMatched=true;
		if (transformedGlyph2 == null) {

			//remap font if needed
			String xx=displayValue;
			if((remapFont)&&(getUnscaledFont().canDisplay(xx.charAt(0))==false))
				xx=""+(char)(rawInt+ 0xf000);

			/**
			//if cannot display return to Lucida
			if(getUnscaledFont().canDisplay(xx.charAt(0))==false){
				xx=displayValue;
				fontMatched=false;
			}*/

			/**use default if cannot be displayed*
			GlyphVector gv1=null;
			if(fontMatched){
				gv1 =getUnscaledFont().createGlyphVector(frc, xx);
			}else{
				Font tempFont = new Font(defaultFont, 0, 1);
				gv1 =tempFont.createGlyphVector(frc, xx);
			}*/

			GlyphVector gv1 =null;

			//do not show CID fonts as Lucida unless match
			if((!isCIDFont)||(isFontInstalled))
			gv1=getUnscaledFont().createGlyphVector(frc, xx);

			if(gv1!=null){

				transformedGlyph2 = new Area(gv1.getOutline());

				//put glyph into display position
				double glyphX=gv1.getOutline().getBounds2D().getX();

				AffineTransform at;

				//ensure inside box
				if(glyphX<0){
					glyphX=-glyphX;
					at =AffineTransform.getTranslateInstance(glyphX*2,0);
					transformedGlyph2.transform(at);
				}

				//<start-13>
				double glyphWidth=gv1.getVisualBounds().getWidth()+(glyphX*2);
				double scaleFactor=currentWidth/glyphWidth;

				if(scaleFactor<1)	{
					at =AffineTransform.getScaleInstance(scaleFactor,1);
					transformedGlyph2.transform(at);

				}

				//create shape for text using tranformation to make correct size
				at =new AffineTransform(dX*Trm[0][0],dX*Trm[0][1],dY*Trm[1][0],dY*Trm[1][1] ,0,0);

				transformedGlyph2.transform( at);

				//save so we can reuse if it occurs again in this TJ command
				setCachedShape(rawInt, transformedGlyph2);
			}
		}

		return transformedGlyph2;
	}


	/**returns a generic glyph using inbuilt fonts*/
	protected Area getGlyph(int rawInt, String displayValue,float currentWidth){

		boolean fontMatched=true;

		/**use default if cannot be displayed*/
		GlyphVector gv1=null;

		//remap font if needed
		String xx=displayValue;

		if((remapFont)&&(getUnscaledFont().canDisplay(xx.charAt(0))==false))
			xx=""+(char)(rawInt+ 0xf000);
		                   
		/**commented out 18/8/04 when font code updated*/
		//if cannot display return to Lucida
		if(getUnscaledFont().canDisplay(xx.charAt(0))==false){
			xx=displayValue;
			fontMatched=false;
		}

		if((this.isCIDFont)&&(fontMatched)){
			gv1=null;
		}else if(fontMatched){
			gv1 =getUnscaledFont().createGlyphVector(frc, xx);
		}else{
			Font tempFont = new Font(defaultFont, 0, 1);
			if(tempFont.canDisplay(xx.charAt(0))==false)
				tempFont = new Font("lucida", 0, 1);
			if(tempFont.canDisplay(xx.charAt(0)))
			gv1 =tempFont.createGlyphVector(frc, xx);
		}

		//gv1 =getUnscaledFont().createGlyphVector(frc, xx);

		Area transformedGlyph2 = null;
		if(gv1!=null){
			transformedGlyph2=new Area(gv1.getOutline());

			//put glyph into display position
			double glyphX=gv1.getOutline().getBounds2D().getX();
			//double glyphY=gv1.getOutline().getBounds2D().getY();
			//double maxX=gv1.getOutline().getBounds2D().getMaxX()-gv1.getOutline().getBounds2D().getWidth();

			AffineTransform at;


			//ensure inside box
			if(glyphX<0){
				glyphX=-glyphX;
				at =AffineTransform.getTranslateInstance(glyphX,0);
				transformedGlyph2.transform(at);
			}


			//<start-13>
			//double glyphWidth=gv1.getVisualBounds().getWidth()+(glyphX*2);
			//double scaleFactor=currentWidth/glyphWidth;
			double scaleFactor=currentWidth/(transformedGlyph2.getBounds2D().getWidth());

			if(scaleFactor<1)	{
				at =AffineTransform.getScaleInstance(scaleFactor,1);
				transformedGlyph2.transform(at);

			}

		}

		return transformedGlyph2;
	}

	/**
		 * Caches the specified shape.
		 */
		protected final void setCachedShape(int idx, Area shape) {
			// using local variable instead of sync'ing
			Area[] cache = cachedShapes;
			if (cache == null)
				cachedShapes = cache = new Area[maxCharCount];
			if(shape==null)
				cache[idx] = cache[idx] =null;
			else
				cache[idx] = cache[idx] = (Area)shape.clone();
		}

	/**
	 * Returns the specified shape from the cache, or <code>null</code> if the shape
	 * is not in the cache.
	 */
	protected final Area getCachedShape(int idx) {
		// using local variable instead of sync'ing
		Area[] cache = cachedShapes;

		if(cache==null)
			return null;
		else{
			Area currentShape=cache[idx];

			if(currentShape==null)
				return null;
			else
				return  (Area)currentShape.clone();
		}
		//return cache == null ? null : (Area)cache[idx].clone();
		//return cache == null ? null : cache[idx];
	}

	public void init(int maxCharCount, boolean isCIDFont) {
		this.maxCharCount=maxCharCount;
		this.isCIDFont=isCIDFont;
	}

	/**set the font being used or try to approximate*/
		public final Font setFont(String name,int size) {

			if(name.equals("Helv"))
				name="Helvetica";
			else if(name.equals("HeBo"))
				name="Helvetica-BOLD";
			else if(name.equals("ZaDb"))
				name="ZapfDingbats";

			//set defaults
			this.font_family_name=name;
			this.style =Font.PLAIN;

			String weight =null,mappedName=null;

			if(font_family_name==null)
				font_family_name=this.fontName;

			String testFont=font_family_name;
			if(font_family_name!=null)
				testFont=font_family_name.toLowerCase();

			//pick up any weight in type 3 font or - standard font mapped to Java
			int pointer = font_family_name.indexOf(",");
			if ((pointer == -1))//&&(StandardFonts.javaFontList.get(font_family_name)!=null))
				pointer = font_family_name.indexOf("-");

            //<start-jfl>
            if (pointer != -1) {

				//see if present with ,
				mappedName=(String) PdfDecoder.fontSubstitutionAliasTable.get(testFont);


				weight =testFont.substring(pointer + 1, testFont.length());

				style = getWeight(weight);

				font_family_name = font_family_name.substring(0, pointer).toLowerCase();

				testFont=font_family_name;

				if(testFont.endsWith("mt"))
					testFont=testFont.substring(0,testFont.length()-2);

			}

			//remap if not type 3 match
			if(mappedName==null)
			mappedName=(String) PdfDecoder.fontSubstitutionAliasTable.get(testFont);
            //<end-jfl>    

            if((mappedName!=null)&&(mappedName.equals("arialbd")))
				mappedName="arial-bold";

			if(mappedName!=null){

				font_family_name=mappedName;

				pointer = font_family_name.indexOf("-");
				if(pointer!=-1){

					font_family_name=font_family_name.toLowerCase();

					weight =font_family_name.substring(pointer + 1, font_family_name.length());

					style = getWeight(weight);

					font_family_name = font_family_name.substring(0, pointer);
				}

				testFont=font_family_name.toLowerCase();

				if(testFont.endsWith("mt"))
					testFont=testFont.substring(0,testFont.length()-2);

			}

			//see if installed
			if(fontList!=null){
				int count = fontList.length;
				for (int i = 0; i < count; i++) {
					if ((fontList[i].equals(testFont))||((weight==null)&&(testFont.startsWith(fontList[i])))) {
						isFontInstalled = true;
						font_family_name=fontList[i];
						i = count;
					}
				}

				//hack for windows as some odd things going on
				if(isFontInstalled && font_family_name.equals("arial")){
					isArialInstalledLocally=true;
				}
			}



			/**approximate display if not installed*/
			if (isFontInstalled == false) {

				//try to approximate font
				if(weight==null){

					//pick up any weight
					String test = font_family_name.toLowerCase();
					style=getWeight(test);

				}

				font_family_name = defaultFont;
			}


			unscaledFont = new Font(font_family_name, style, size);

			//System.out.println(font_family_name+" "+style+" "+size+" "+unscaledFont);
//		if(font_family_name.indexOf("ucida")!=-1){
//			System.err.println(name +" maps to "+font_family_name+" "+unscaledFont+" "+testFont+" "+mappedName);
//			System.exit(1);
//		}

			return unscaledFont;
		}

	/**
	 * work out style (ITALIC, BOLD)
	 */
	private int getWeight(String weight) {

		int style=Font.PLAIN;

		if(weight.endsWith("mt"))
			weight=weight.substring(0,weight.length()-2);

		if (weight.indexOf("heavy") != -1)
			style = Font.BOLD;
		else if (weight.indexOf("bold") != -1)
			style = Font.BOLD;
		else if (weight.indexOf("roman") != -1)
			style = Font.ROMAN_BASELINE;

		if (weight.indexOf("italic") != -1)
			style = style+Font.ITALIC;
		else if (weight.indexOf("oblique") != -1)
			style = style+Font.ITALIC;

		return style;
	}


	/**
		 * Returns the unscaled font, initializing it first if it hasn't been used before.
		 */
		public final Font getUnscaledFont() {

			/**commenting out  this broke originaldoc.pdf*/
			if (unscaledFont == null)
				unscaledFont = new Font(defaultFont, Font.PLAIN, 1);

			return unscaledFont;
		}

	protected PdfGlyph[] cachedEmbeddedShapes=null;

	protected int localBias=0,globalBias=0;

	/**
	 * Caches the specified shape.
	 */
	public final void setEmbeddedCachedShape(int idx, PdfGlyph shape) {
		// using local variable instead of sync'ing
		PdfGlyph[] cache = cachedEmbeddedShapes;
		if (cache == null)
			cachedEmbeddedShapes = cache = new PdfGlyph[maxCharCount];

		cache[idx] = cache[idx] = shape;
	}

	/**
	 * Returns the specified shape from the cache, or <code>null</code> if the shape
	 * is not in the cache.
	 */
	public final PdfGlyph getEmbeddedCachedShape(int idx) {
		// using local variable instead of sync'ing
		PdfGlyph[] cache = cachedEmbeddedShapes;

		if(cache==null)
			return null;
		else{
			PdfGlyph currentShape=cache[idx];

			if(currentShape==null)
				return null;
			else
				return currentShape;
		}
		//return cache == null ? null : (Area)cache[idx].clone();
		//return cache == null ? null : cache[idx];
	}

	/**
	 * template used by t1/t3/tt fonts
	 */
	public PdfGlyph getEmbeddedGlyph(GlyphFactory factory, String glyph, float[][] trm, int rawInt, String displayValue, float currentWidth, String key) {
		return null;
	}

	public void setGIDtoCID(int[] cidToGIDMap) {
	}

	public void setEncodingToUse(boolean hasEncoding, int fontEncoding, boolean b, boolean isCIDFont) {

	}


	public int readEmbeddedFont(boolean TTstreamisCID, byte[] fontData) {
        return 0;
	}

	public void setIsSubsetted(boolean b) {
		isSubsetted=b;
	}

	public void setT3Glyph(String pKey, PdfGlyph glyph) {

	}


	public void setCharString(String s, byte[] bytes) {
	}

	public int getNumber(byte[] content, int p, double[] op, int i, boolean b) {

        return 0;
	}

	public boolean is1C() {
		return false;
	}

	public void setis1C(boolean b) {
	}

	
	public void setValuesForGlyph(int rawInt, String charGlyph, String displayValue, String embeddedChar) {
		Integer key=new Integer(rawInt);
		chars.put(key,charGlyph);
		displayValues.put(key,displayValue);
		embeddedChars.put(key,embeddedChar);
	}

	public String getDisplayValue(Integer key) {
		return (String) displayValues.get(key);
	}

	public String getCharGlyph(Integer key) {
		return (String) chars.get(key);
	}

	public String getEmbeddedEnc(Integer key) {
		
		return (String) embeddedChars.get(key);
	}
	
	public Map getDisplayValues() {
		return displayValues;
	}

	public Map getCharGlyphs() {
		return chars;
	}

	public Map getEmbeddedEncs() {
		
		return  embeddedChars;
	}
	
	public void setDisplayValues(Map displayValues) {
		this.displayValues=displayValues;
	}

	public void setCharGlyphs(Map chars) {
		this.chars=chars;
	}

	public void setEmbeddedEncs(Map embeddedChars) {
		
		this.embeddedChars=embeddedChars;
	}

	public void setLocalBias(int i) {
		localBias=i;
		
	}
	
	public void setGlobalBias(int i) {
		globalBias=i;
		
	}

    public float getTTWidth(String charGlyph, int rawInt, String displayValue, boolean b) {
        System.out.println("wrong!");
        System.exit(1);
        return 0;  //To change body of created methods use File | Settings | File Templates.
    }
}
