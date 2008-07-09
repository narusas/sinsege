package org.jpedal.parser;

import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.StandardFonts;
import org.jpedal.fonts.glyph.PdfGlyph;
//<start-jfl>
import org.jpedal.io.PdfObjectReader;
//<end-jfl>
import org.jpedal.objects.GraphicsState;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.utils.LogWriter;


public class FontFactory {

    //<start-jfl>
    public static PdfFont createFont(GraphicsState currentGraphicsState,String subtype,int fontType, PdfObjectReader currentPdfFile, String subFont) {
		
		if (fontType==StandardFonts.TYPE1) {
			return new org.jpedal.fonts.Type1C(currentPdfFile,subFont); 
		}else if (fontType==StandardFonts.TRUETYPE){
			return new org.jpedal.fonts.TrueType(currentPdfFile,subFont); 
		}else if (fontType==StandardFonts.TYPE3){
			return new org.jpedal.fonts.Type3(currentPdfFile,currentGraphicsState); 
		}else if (fontType==StandardFonts.CIDTYPE0){
			return new org.jpedal.fonts.CIDFontType0(currentPdfFile); 
		}else if(fontType==StandardFonts.CIDTYPE2) {
			return new org.jpedal.fonts.CIDFontType2(currentPdfFile); 
		} else{
			LogWriter.writeLog("Font type " + subtype + " not supported");
			return new PdfFont(currentPdfFile); 
		}
		
	}
    //<end-jfl>

    public static PdfGlyph chooseGlpyh(int glyphType, Object rawglyph) {
		
		if(glyphType==DynamicVectorRenderer.TYPE3){
			return (org.jpedal.fonts.glyph.T3Glyph)rawglyph;
		}else if(glyphType==DynamicVectorRenderer.TYPE1C){
			return (org.jpedal.fonts.glyph.T1Glyph)rawglyph;
		}else if(glyphType==DynamicVectorRenderer.TRUETYPE){
			return (org.jpedal.fonts.tt.TTGlyph)rawglyph;
		}else
			return null;
		
	}
}
