/*
 * ===========================================
 * Java Pdf Extraction Decoding Access Library
 * ===========================================
 *
 * Project Info:  http://www.jpedal.org
 * Project Lead:  Mark Stephens (mark@idrsolutions.com)
 * 
 * (C) Copyright 2005, IDRsolutions and Contributors.
 *
 * 	This file is part of JPedal
 * 
 * Created on 28-Apr-2005
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
 * FormXObject.java
 * ---------------
 *
 * Original Author:  Mark Stephens (mark@idrsolutions.com)
 * Contributor(s):
 *
 */
package org.jpedal.objects.acroforms;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.jpedal.gui.ShowGUIMessage;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Strip;

import org.jpedal.parser.PdfStreamDecoder;
import org.jpedal.render.DynamicVectorRenderer;

/**
 * @author chris
 * 
 * 
 */
public class FormXObject {

    private static final boolean debug = false;
    private static final boolean showImage = false;
    
    private int width = 20;
    private int height = 20;
    
    private String whenToScale=null;
    
    private Map formFieldValues=null;

    /** handle on object reader */
    private PdfObjectReader currentPdfFile;

    /**
     * fonts in file
      */
    private Map fonts=new HashMap();
    
    /**returns  Map containing all font objects*/
    public Map getFontMap() {
        return fonts;
    }
    
    /**
     * 
     */
    public FormXObject(PdfObjectReader currentPdfFile) {
        this.currentPdfFile = currentPdfFile;
    }
    
    private FormXObject() {
    }
    
    public BufferedImage decode(Map currentValues,String scale,Map formValues){
//        if(scale==null){
//            System.out.println("ERROR scale should be a value scale="+scale);
//            System.exit(1);
//        }
        formFieldValues = formValues;
        whenToScale = scale;
        return decode(currentValues);
    }
    
    /**
     * decode appearance stream and convert into VectorRenderObject we can redraw
     * */
    public BufferedImage decode(Map currentValues){
        	
        //set to false, NEXT appears, set to true and its gone
        boolean breaksButton=false; 
        
        if(debug)
        	System.out.println("XOBJEct BBox="+currentValues);
      
        try{
            
        
        /**
         * generate local object to decode the stream
         */
        PdfStreamDecoder glyphDecoder=new PdfStreamDecoder();
		ObjectStore localStore = new ObjectStore();
		glyphDecoder.setStore(localStore);
		
		/**
		 * create renderer object
		 */
		DynamicVectorRenderer glyphDisplay=new DynamicVectorRenderer(0,false,20,localStore);
		
		try{
			glyphDecoder.init(false,true,15,0,new PdfPageData(),0,glyphDisplay,currentPdfFile,new HashMap(),new HashMap());
			
		}catch(Exception e){
			LogWriter.writeLog("Font exception "+e);
		}
		
		/**read any resources*/
		try{
			Map resValue =(Map) currentPdfFile.resolveToMapOrString("Resources", currentValues.get("Resources"));	
			if(debug)
        		System.out.println("REsources="+resValue.keySet());


            //map out any refs
            Object fontValue=resValue.get("Font");
            if((fontValue!=null)&&(fontValue instanceof String)){
              String ref=(String)fontValue;
                if(ref.endsWith(" R")){
                    Map value=currentPdfFile.readObject(ref,false,null);
                    resValue.put("Font",value);
                }
            }

            if (resValue != null)
			    glyphDecoder.readResourcesForForm(resValue);
//			currentValues.remove("Resources");
      
			Map newFonts=glyphDecoder.getFontMap();
			Iterator keys=newFonts.keySet().iterator();
			while(keys.hasNext()){
			    Object fontKey=keys.next();
			    fonts.put(fontKey,newFonts.get(fontKey));
			    
			}
			
		}catch(Exception e){
		    e.printStackTrace();
		    System.out.println("Exception "+e+" reading resources in XForm");
		}
		
		/**decode the stream*/
		byte[] commands=(byte[]) currentPdfFile.resolveToMapOrString("DecodedStream", currentValues.get("DecodedStream"));
		if(commands!=null)
		    glyphDecoder.decodeStreamIntoObjects(commands);
//		currentValues.remove("DecodedStream");
		
		///////////////////////////////
		
		boolean ignoreColors=glyphDecoder.ignoreColors;
		
		glyphDecoder=null;

		localStore.flush();
		
		org.jpedal.fonts.glyph.T3Glyph form= new org.jpedal.fonts.glyph.T3Glyph(glyphDisplay, 0,0,ignoreColors,"");
		
//		System.out.println(currentValues.get("BBox"));
		
		String rect = (String)currentPdfFile.resolveToMapOrString("BBox", currentValues.get("BBox"));
		float rectX1=0,rectY1=0;
		if(rect!=null){
		    rect = Strip.removeArrayDeleminators(rect);
			
			StringTokenizer tok = new StringTokenizer(rect);
			float x1=Float.parseFloat(tok.nextToken()),
			y1=Float.parseFloat(tok.nextToken()),
			x2=Float.parseFloat(tok.nextToken()),
			y2=Float.parseFloat(tok.nextToken());
			
			rectX1 = (x1);
			rectY1 = (y1);
			width=(int) (x2-x1);
			height=(int) (y2-y1);
			if(debug)
				System.out.println("rectx="+rectX1+" recty="+rectY1+" w="+width+" h="+height);
			
			if(formFieldValues!=null){
				if(whenToScale==null || whenToScale.equals("A")){
			        //scale icon to fit BBox
			        Rectangle formRect = (Rectangle)formFieldValues.get("rect");
			        if(formRect.width!=width || formRect.height!=height){
//			            System.out.println("field - width="+formRect.width+" height="+formRect.height+
//			                    " App - width="+width+" height="+height);
			            
				        
			            /** default is A Always scale to fit Form BBox, look in org.jpedal.fonts.T3Glyph D1 D0 */
		                LogWriter.writeFormLog("{stream} XObject MK IF A command, the icon should be scaled to fit the BBox",false);
			        }
				}else if(whenToScale.equals("N")){
			        //do nothing as already does this
			    }else {
			        LogWriter.writeFormLog("{XObject} XObject MK IF Unimplemented command="+whenToScale,false);
			    }
			}
			
		}
		
		
		if(width<0)
			width=-width;
		if(height<0)
			height=-height;
		
		if(width==0 || height==0)
			return null;
		
		BufferedImage aa=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		
		
		Graphics2D g2=(Graphics2D) aa.getGraphics();
		
		AffineTransform flip=new AffineTransform();
		flip.translate(0, height);
		flip.scale(1, -1);
		g2.setTransform(flip);
		
		String matrix = (String) currentPdfFile.resolveToMapOrString("Matrix", currentValues.get("Matrix"));
		if(matrix!=null){
			StringTokenizer tok = new StringTokenizer(Strip.removeArrayDeleminators(matrix));
			float a = Float.parseFloat(tok.nextToken());
			float b = Float.parseFloat(tok.nextToken());
			float c = Float.parseFloat(tok.nextToken());
			float d = Float.parseFloat(tok.nextToken());
			float e = Float.parseFloat(tok.nextToken());
			float f = Float.parseFloat(tok.nextToken());
			
			//removed to fix display_error.pdf
			if(debug)
				System.out.println("matrix="+matrix);
			g2.transform(new AffineTransform(a,b,c,d,0,0));
			
			float x=0,y=0;
			//OLD routine
//			if(e!=0f && rectX1!=0){
//			x = e;
//			}
//			if(f!=0f && rectY1!=0){
//			y = f;
//			}
			
			/**
			x = (e*rectX1)/e;//possible fixes for future problems
			y = (f*rectY1)/f;
			*/
			
			if(e!=0f){
				x = -rectX1;
			}
			if(f!=0f){
				y = -rectY1;
			}
			g2.translate(x,y);
//			g2.translate(-rectX1,-rectY1);
			
			if(debug)
				System.out.println("rect x="+rectX1+" y="+rectY1+" w="+width+" h="+height);
		}
		
//		AffineTransformOp invert =new AffineTransformOp(flip,ColorSpaces.hints);
//		image=invert.filter(image,null);
		
		form.render(0,g2,false, 1f);
		
		
		g2.dispose();
		
		return aa;
		
        }catch(Exception e){
            e.printStackTrace();
            
            return null;
        }
        
       
        /**
        				String subtype = (String) currentValues.getxx("Subtype");
        		
        				if (subtype.equals("/Form")) {
        					
        				   
        					//reset operand
        					currentOp=0;
        					operandCount=0;
        					
        					if(objectData!=null)
        					processXForm(currentValues, objectData);
        					
        					if((!this.renderDirectly)&&(statusBar!=null))
        					statusBar.inSubroutine(false);
        					
        				} else {
        					LogWriter.writeLog("[PDF] " + subtype + " not supported");
        				}
        				*/
    }
    
}
