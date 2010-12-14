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
  * TTGlyphs.java
  * ---------------
  * (C) Copyright 2005, by IDRsolutions and Contributors.
  *
  * 
  * --------------------------
 */
package org.jpedal.fonts.tt;

import org.jpedal.fonts.glyph.GlyphFactory;
import org.jpedal.fonts.glyph.PdfGlyph;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.StandardFonts;

//<start-jfl>
import org.jpedal.PdfDecoder;
//<end-jfl>

public class TTGlyphs extends PdfJavaGlyphs {

	protected boolean hasGIDtoCID;

	protected int[] CIDToGIDMap;

	private CMAP currentCMAP;
	private Post currentPost;
	private Glyf currentGlyf;
	private Hmtx currentHmtx;

	private FontFile2 currentFontFile;

	//private Head currentHead;
	//private Name currentName;
	//private Mapx currentMapx;
	//private Loca currentLoca;
	//private Cvt currentCvt;
	//private Fpgm currentFpgm;
	//private Hhea currentHhea;

    private CFF currentCFF;

    int glyphCount=0;


	private int unitsPerEm;

    private boolean hasCFF;


    /**
	 * used by  non type3 font
	 */
	public PdfGlyph getEmbeddedGlyph(GlyphFactory factory, String glyph, float[][]Trm, int rawInt, String displayValue, float currentWidth, String key) {

		int id=rawInt;
		if(hasGIDtoCID)
			rawInt=CIDToGIDMap[rawInt];

		/**flush cache if needed*/
		if((lastTrm[0][0]!=Trm[0][0])|(lastTrm[1][0]!=Trm[1][0])|
				(lastTrm[0][1]!=Trm[0][1])|(lastTrm[1][1]!=Trm[1][1])){
			lastTrm=Trm;
			cachedShapes = null;
		}

		//either calculate the glyph to draw or reuse if alreasy drawn
		PdfGlyph transformedGlyph2 = getEmbeddedCachedShape(id);

		if (transformedGlyph2 == null) {

            //use CMAP to get actual glyph ID
            int idx=rawInt;


            if((currentCMAP!=null))
                idx=currentCMAP.convertIndexToCharacterCode(glyph,rawInt,remapFont,isSubsetted);

            //if no value use post to lookup
            if(idx<1)
                idx=currentPost.convertGlyphToCharacterCode(glyph);

            //shape to draw onto
			try{
                if(hasCFF){
                  
                    transformedGlyph2=currentCFF.getCFFGlyph(factory,glyph,Trm,rawInt, displayValue,currentWidth,key);
                    
                    //set raw width to use for scaling
                    transformedGlyph2.setWidth(getUnscaledWidth(glyph, rawInt, displayValue,false));
                    
                    
                }else
                    transformedGlyph2=getTTGlyph(idx,glyph,rawInt, displayValue,currentWidth);
			}catch(Exception e){
				transformedGlyph2=null;

			}

			//save so we can reuse if it occurs again in this TJ command
			setEmbeddedCachedShape(id, transformedGlyph2);
		}

		return transformedGlyph2;
	}

/*
	 * creates glyph from truetype font commands
	 */
	public PdfGlyph getTTGlyph(int idx,String glyph,int rawInt, String displayValue, float currentWidth) {

		//System.out.println(glyph);

		PdfGlyph currentGlyph=null;
		/**
		if(rawInt>glyphCount){
			LogWriter.writeLog("Font index out of bounds using defaul t"+glyphCount);
			rawInt=0;

		}*/

		try{
			//final boolean debug=(rawInt==48);
			final boolean debug=false;


			if(idx!=-1){
				//move the pointer to the commands
				int p=currentGlyf.getCharString(idx);

				if(p!=-1){
					currentFontFile.setPointer(p);
					currentGlyph=new TTGlyph(glyph,debug,currentGlyf, currentFontFile,currentHmtx,idx,(unitsPerEm/1000f));
					if(debug)
						System.out.println(">>"+p+" "+rawInt+" "+displayValue+" "+baseFontName);	
				}
			}

		}catch(Exception ee){
			ee.printStackTrace();

		}

		//if(glyph.equals("fl"))

		return currentGlyph;
	}

	public void setEncodingToUse(boolean hasEncoding, int fontEncoding, boolean b, boolean isCIDFont) {

			if(currentCMAP!=null)
			currentCMAP.setEncodingToUse(hasEncoding,fontEncoding,b,isCIDFont);

	}

	/*
	 * creates glyph from truetype font commands
	 */
	public float getTTWidth(String glyph,int rawInt, String displayValue,boolean TTstreamisCID) {

        //use CMAP if not CID
		int idx=rawInt;

		float width=0;

		try{
			if((!TTstreamisCID))
				idx=currentCMAP.convertIndexToCharacterCode(glyph,rawInt,remapFont,isSubsetted);

			//if no value use post to lookup
			if(idx<1)
				idx=currentPost.convertGlyphToCharacterCode(glyph);

			//if(idx!=-1)
				width=currentHmtx.getWidth(idx);

		}catch(Exception e){

		}

        return width;
	}
	
	/*
	 * creates glyph from truetype font commands
	 */
	private float getUnscaledWidth(String glyph,int rawInt, String displayValue,boolean TTstreamisCID) {

        //use CMAP if not CID
		int idx=rawInt;

		float width=0;

		try{
			if((!TTstreamisCID))
				idx=currentCMAP.convertIndexToCharacterCode(glyph,rawInt,remapFont,isSubsetted);

			//if no value use post to lookup
			if(idx<1)
				idx=currentPost.convertGlyphToCharacterCode(glyph);

			//if(idx!=-1)
				width=currentHmtx.getUnscaledWidth(idx);

		}catch(Exception e){

		}

        return width;
	}


	public void setGIDtoCID(int[] cidToGIDMap) {

		hasGIDtoCID=true;
		this.CIDToGIDMap=cidToGIDMap;

	}

    /**
     * return name of font or all fonts if TTC
     * NAME will be LOWERCASE to avoid issues of capitalisation
     * when used for lookup - if no name, will be null
     */
    public String[] readPostScriptFontNames(byte[] fontData) {

        String[] postscriptNames=new String[0];

        /**setup read the table locations*/
        FontFile2 currentFontFile=new FontFile2(fontData);

        //get type
        int fontType=currentFontFile.getType();

        int fontCount=currentFontFile.getFontCount();
  
        postscriptNames=new String[fontCount];

        /**read tables for names*/
        for(int i=0;i<fontCount;i++){

            currentFontFile.setSelectedFontIndex(i);
            Name currentName=new Name(currentFontFile);

            String name=currentName.getString(Name.POSTSCRIPT_NAME);

            if(name==null)
                postscriptNames[i]=null;
            else
                postscriptNames[i]=currentName.getString(Name.POSTSCRIPT_NAME).toLowerCase();
        }

        return postscriptNames;
    }

    public int readEmbeddedFont(boolean TTstreamisCID,byte[] fontData) {

        //assume TT and set to OTF further down
        int type= StandardFonts.TRUETYPE;
        
        /**setup read the table locations*/
		currentFontFile=new FontFile2(fontData);

		//<start-jfl>
        //select font if TTC
        //does nothing if TT
		if(PdfDecoder.fontSubstitutionFontID==null){
			currentFontFile.setPointer(0);
		}else{
	        Integer fontID= (Integer) PdfDecoder.fontSubstitutionFontID.get(fontName.toLowerCase());
	
	        if(fontID!=null)
	            currentFontFile.setPointer(fontID.intValue());
	        else
	            currentFontFile.setPointer(0);
		}
		//<end-jfl>
		
        /**read tables*/
		Head currentHead=new Head(currentFontFile);

		currentPost=new Post(currentFontFile);

		//currentName=new Name(currentFontFile);
        
		Mapx currentMapx=new Mapx(currentFontFile);
		glyphCount=currentMapx.getGlyphCount();
		Loca currentLoca=new Loca(currentFontFile,glyphCount,currentHead.getFormat());
		currentGlyf=new Glyf(currentFontFile,glyphCount,currentLoca.getIndices());

        currentCFF=new CFF(currentFontFile);

        hasCFF=currentCFF.hasCFFData();
        if(hasCFF)
        	type= StandardFonts.OPENTYPE;

        //currentCvt=new Cvt(currentFontFile);
		//currentFpgm=new Fpgm(currentFontFile);
		Hhea currentHhea=new Hhea(currentFontFile);

        int[] matrix=currentHead.getMatrix();
        int width=matrix[3];
        currentHmtx=new Hmtx(currentFontFile,glyphCount,currentHhea.getNumberOfHMetrics(),width);

        //not all files have CMAPs
		if(!TTstreamisCID){
			int startPointer=currentFontFile.selectTable(FontFile2.CMAP);

			if(startPointer!=0)
			currentCMAP=new CMAP(currentFontFile,startPointer,currentGlyf);

		}
           
        unitsPerEm=currentHead.getUnitsPerEm();

        return type;
    }
}
