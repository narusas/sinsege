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
* PdfPanel.java
* ---------------
* (C) Copyright 2005, by IDRsolutions and Contributors.
*

* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.dnd.DropTarget;

import java.awt.event.MouseEvent;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.util.Map;

import javax.swing.border.Border;

import org.jpedal.io.ObjectStore;

import org.jpedal.objects.PdfPageData;

//<start-jfl>
//<start-adobe>
import org.jpedal.gui.Hotspots;
import org.jpedal.objects.PageLines;
import org.jpedal.objects.PdfAnnots;
import org.jpedal.objects.PdfFormData;

//<end-adobe>

import org.jpedal.objects.PrinterOptions;

//<start-adobe>
import org.jpedal.objects.acroforms.AcroRenderer;
//<end-adobe>
//<end-jfl>
import org.jpedal.render.DynamicVectorRenderer;

import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.utils.repositories.Vector_Rectangle;
import org.jpedal.utils.repositories.Vector_Shape;
import org.jpedal.utils.repositories.Vector_String;


import javax.swing.*;


/**
 * Do not create an instance of this class - provides GUI functionality for
 * PdfDecoder class to extend
 */
public class PdfPanel extends JPanel{

    //<start-jfl>
    protected Display pages;

    //<start-adobe>
    /** holds the extracted AcroForm data */
    protected PdfFormData currentAcroFormData;

    /** holds the annotation data and functions */
    protected PdfAnnots annotsData;   

    /**holds page lines*/
    protected PageLines pageLines;

    /**default renderer for acroforms*/
    protected AcroRenderer currentFormRenderer;//=new DefaultAcroRenderer();

    /**default renderer for annots*/
    protected AcroRenderer currentAnnotRenderer;//=new DefaultAnnotRenderer();

    /**hotspots for display*/
    Hotspots displayHotspots;

    /**hotspots for printing*/
    Hotspots printHotspots;

    //<end-adobe>

    //<end-jfl>
    
    protected Rectangle[] alternateOutlines;
	String altName;

	/**tracks indent so changing to continuous does not disturb display*/
    private int lastIndent=-1;

    //<start-jfl>
    PageOffsets currentOffset;

    /**copy of flag to tell program whether to create
	 * (and possibly update) screen display
	 */
    protected boolean renderPage = false;

    /**type of printing*/
    protected boolean IsPrintAutoRotateAndCenter=false;

    /**flag to show we use PDF page size*/
    boolean usePDFPaperSize=false;

    /**page scaling mode to use for printing*/
    protected int pageScalingMode=PrinterOptions.PAGE_SCALING_REDUCE_TO_PRINTER_MARGINS;

    //<end-jfl>
    
    /**display mode (continuous, facing, single)*/
    protected int displayView=Display.SINGLE_PAGE;

     /**amount we scroll screen to make visible*/
    private int scrollInterval=10;

    /** count of how many pages loaded */
    protected int pageCount = 0;

    /**
    * if true
    * show the crop box as a box with cross on it
    * and remove the clip.
    */
    private boolean showCrop = false;

    /** when true setPageParameters draws the page rotated for use with scale to window */
    private boolean isNewRotationSet=false;

    /** displays the viewport border */
    protected boolean displayViewportBorder=false;

    /**flag to stop multiple attempts to decode*/
    protected boolean isDecoding=false,stopDecoding=false;

    protected int alignment=Display.DISPLAY_LEFT_ALIGNED;

    /** used by setPageParameters to draw rotated pages */
    protected int displayRotation=0;

    /**current cursor location*/
    private Point current_p;

    /**allows user to create viewport on page and scale to this*/
    protected Rectangle viewableArea=null;

    /**shows merging for debugging*/
    private Vector_Int merge_level ;
    private Vector_Shape merge_outline;
    private boolean[] showDebugLevel;
    private Color[] debugColors;
    private boolean showMerging=false;

    /**used to draw demo cross*/
    AffineTransform demoAf=null;

    /**flag to show if tooltips have been initialised*/
    private boolean tooltipsAreInitialised=false;

    /**repaint manager*/
    private RepaintManager currentManager=RepaintManager.currentManager(this);

    /**current page*/
    protected int pageNumber=1;

    /**used to reduce or increase image size*/
    protected AffineTransform displayScaling;

    /**
     * used to apply the imageable area to the displayscaling, used instead of
     * displayScaling, as to preserve displayScaling
     */
    protected AffineTransform viewScaling=null;

    public boolean showAnnotations=true;

    /**flag to show if forms available*/
    protected boolean formsAvailable=true;

    /** holds page information used in grouping*/
    protected PdfPageData pageData = new PdfPageData();

    /**used to track highlight*/
    private Rectangle lastHighlight=null;

    /**rectangle drawn on screen by user*/
    protected Rectangle cursorBoxOnScreen = null,lastCursorBoxOnScreen=null;

    /** whether the cross-hairs are drawn */
    private boolean drawCrossHairs = false;

    /** which box the cursor is currently positioned over */
    private int boxContained = -1;

    /** color to highlight selected handle */
    private Color selectedHandleColor = Color.red;

    /** the gap around each point of reference for cursorBox */
    private int handlesGap = 5;

    /**colour of highlighted rectangle*/
    private Color outlineColor;

    /**rectangle of object currently under cursor*/
    protected Rectangle currentHighlightedObject = null;

    /**colour of a shape we highlight on the page*/
    private Color outlineHighlightColor;

    /**preferred colour to highliht page*/
    private Color[] highlightColors;

    /**gap around object to repaint*/
    final private int strip=2;

    /**highlight around selected area*/
    private Rectangle2D[] outlineZone = null;

    private int[] processedByRegularExpression=null;

    /**allow for inset of display*/
    protected int insetW=0,insetH=0;

    /**flag to show if area selected*/
    private boolean[] highlightedZonesSelected = null;

    /**user defined viewport*/
    Rectangle userAnnot=null;

    /** default height width of bufferedimage in pixels */
    private int defaultSize = 100;

    /**height of the BufferedImage in pixels*/
    private int y_size = defaultSize;

    /**unscaled page width and height*/
    private int max_y,max_x;

    /**width of the BufferedImage in pixels*/
    private int x_size = defaultSize;

    /**used to plot selection*/
    int[] cx=null,cy=null;

    /**any scaling factor being used to convert co-ords into correct values
     * and to alter image size
     */
    protected float scaling=1;

    /**tooltip text for display over selected areas*/
    private Vector_String toolTipText;

    /**tooltip area for display over selected areas*/
    private Vector_Rectangle toolTipRectangle;

    /**tooltip area for display over selected areas*/
    private Vector_Int ttType;

    /**tooltip area for display over selected areas*/
    private Vector_Int toolTipPage;

    /**counter used for number of tooltips*/
    private int tooltipsCount = 0;

    /**mode for showing outlines*/
    private int highlightMode = 0;

    /**flag for showing all object outlines in PDFPanel*/
    public static final int SHOW_OBJECTS = 1;

    /**flag for showing all lines on page used for grouping */
    public static final int SHOW_LINES = 2;

    /**flag for showing all lines on page used for grouping */
    public static final int SHOW_BOXES = 4;

    /**size of font for selection order*/
    protected int size=20;

    /**font used to show selection order*/
    protected Font highlightFont=null;

    /**border for component*/
    protected Border myBorder=null;

    /**flag for TEXT type tooltip*/
    private static final int OUTLINE_CURRENT = 2;

    /**flag for showing all selected object outlines*/
    private static final int SHOW_SELECTED = 4;

    /**flag for TEXT type tooltip*/
    private static final int TEXT_TOOLTIP = 8;

    /**flag for MASTER type tooltip*/
    private static final int MASTER_TOOLTIP = 16;

    protected DropTarget dropTarget = null;

    /** the ObjectStore for this file */
    protected ObjectStore objectStoreRef = new ObjectStore();

    /**the actual display object*/
    protected DynamicVectorRenderer currentDisplay=new DynamicVectorRenderer(1,objectStoreRef,false); //

    /**set of individual icons to use for annotations on each page*/
    Map userAnnotIcons;

    /**flag to show if border appears on printed output*/
    protected boolean useBorder=true;

    private int[] selectionOrder;

    /**stores area of arrays in which text should be highlighted*/
    private Rectangle[] areas;

    /**stores area of arrays in which text found on search*/
    private Rectangle rectArea;
    private Rectangle[] rectAreas;

    private Object[] linkedItems;

    private int[] parents;

    protected boolean useAcceleration=true;

    /**all text blocks as a shape*/
    private Shape[] fragmentShapes;

    private int x_size_cropped;

    private int y_size_cropped;

    private AffineTransform cursorAf;

    private Rectangle actualBox;

    private boolean drawInteractively=false;

	protected int lastFormPage=-1,lastStart=-1,lastEnd=-1;
	
	private int pageUsedForTransform;

    protected int additionalPageCount=0,xOffset=0;

	//private GraphicsDevice currentGraphicsDevice = null;

    //<start-adobe>
    public void initNonPDF(PdfDecoder pdf){

        pages=new SingleDisplay(pageNumber,pageCount,currentDisplay);

        pages.setup(true,null, pdf);
    }

    /**workout combined area of shapes are in an area*/
	public  Rectangle getCombinedAreas(Rectangle targetRectangle,boolean justText){
		if(this.currentDisplay!=null)
			return currentDisplay.getCombinedAreas(targetRectangle, justText);
		return
			null;
	}

    /**reset tooltips*/
    final protected void flushToolTips() {
        toolTipText = new Vector_String(100);
        toolTipRectangle = new Vector_Rectangle(100);
        ttType = new Vector_Int(100);
        toolTipPage = new Vector_Int(100);
        tooltipsCount = 0;

    }

    /**
     * put debugging info for grouping onscreen
     * to aid in developing and debugging merging algorithms used by Storypad -
     * (NOT PART OF API and subject to change)
     */
    final public void addMergingDisplayForDebugging(Vector_Int merge_level,
                                                    Vector_Shape merge_outline,int count,Color[] colors) {

        this.merge_level=merge_level;
        this.merge_outline=merge_outline;
        this.showDebugLevel=new boolean[count];
        this.debugColors=colors;

	}


    /**
     * debug zone for Storypad - not part of API
     */
    final public void setDebugView(int level, boolean enabled){
        if(showDebugLevel!=null)
        showDebugLevel[level]=enabled;
    }
    //<end-adobe>

    /**
     * get pdf as Image of current page with height as required height in pixels -
     * Page must be decoded first - used to generate thubnails for display.
	 * Use decodePageAsImage forcreating images of pages
     */
    final public BufferedImage getPageAsThumbnail(int height,DynamicVectorRenderer currentDisplay) {

    	if(currentDisplay==null){

			currentDisplay=this.currentDisplay;

			/**
			 * save to disk
			 */
			ObjectStore.cachePage(new Integer(pageNumber), currentDisplay);
			
		}

		BufferedImage image = getImageFromRenderer(height,currentDisplay,pageNumber);


        return image;

    }

    /**
     */
    protected BufferedImage getImageFromRenderer(int height,DynamicVectorRenderer rend,int pageNumber) {

        //int mediaBoxW = pageData.getMediaBoxWidth(pageNumber);
        int mediaBoxH = pageData.getMediaBoxHeight(pageNumber);
        int mediaBoxX = pageData.getMediaBoxX(pageNumber);
        int mediaBoxY = pageData.getMediaBoxY(pageNumber);

        int crw=pageData.getCropBoxWidth(pageNumber);
        int crh=pageData.getCropBoxHeight(pageNumber);
        int crx=pageData.getCropBoxX(pageNumber);

        int cry=pageData.getCropBoxY(pageNumber);
        if(cry>0)
            cry=mediaBoxH-crh-cry;

        float scale=(float) height/(crh);

        int rotation=pageData.getRotation(pageNumber);

        /**allow for rotation*/
        int dr=-1;

        if((rotation==90)|(rotation==270)){
            int tmp=crw;
            crw=crh;
            crh=tmp;
            dr=1;

            tmp=crx;
            crx=cry;
            cry=tmp;
        }

        AffineTransform scaleAf = getScalingForImage(pageNumber,rotation,scale);//(int)(mediaBoxW*scale), (int)(mediaBoxH*scale),
        int cx=mediaBoxX-crx,cy=mediaBoxY-cry;

        scaleAf.translate(cx,dr*cy);
        return rend.getPageAsImage(scale,crx,cry,crw,crh,pageNumber,scaleAf,BufferedImage.TYPE_INT_RGB);

    }

    //<start-adobe>

    /**set zones we want highlighted onscreen
     * @deprecated
     * please look at  setFoundTextAreas(Rectangle areas),setHighlightedAreas(Rectangle[] areas)
     * <b>This is NOT part of the API</b> (used in Storypad)
     */
    final public void setHighlightedZones(
        int mode,
        int[] cx,int[] cy,
        Shape[] fragmentShapes,
        Object[] linkedItems,
        int[] parents,
        Rectangle2D[] outlineZone,
        boolean[] highlightedZonesSelected,Color[] highlightColors,int[] selectionOrder,int[] processedByRegularExpression) {

		this.cx=cx;
        this.cy=cy;
        this.fragmentShapes=fragmentShapes;
        this.linkedItems=linkedItems;
        this.parents=parents;
        this.outlineZone = outlineZone;
        this.processedByRegularExpression=processedByRegularExpression;

        this.highlightedZonesSelected = highlightedZonesSelected;
        this.highlightMode = mode;
        this.highlightColors=highlightColors;
        this.selectionOrder=selectionOrder;

		//and deselect alt highlights
		this.alternateOutlines=null;


	}

    /**
     * create the tooltip
     * <br><b>This is NOT part of the API</b>
     */
    private void createToolTip(
        int type,
        String message,
        Rectangle outline,
        int page) {

            //check if tooltips initialised and do so if needed
            if(!tooltipsAreInitialised){
                tooltipsAreInitialised=true;

                toolTipText = new Vector_String(100);
                toolTipRectangle = new Vector_Rectangle(100);
                ttType = new Vector_Int(100);
                toolTipPage = new Vector_Int(100);

            }

        //create the rectangle
        toolTipRectangle.addElement(outline);
        toolTipText.addElement(message);
        ttType.addElement(type);
        toolTipPage.addElement(page);
        tooltipsCount++;
    }


    /**set merging option for Storypad (not part of API)*/
    public void setDebugDisplay(boolean isEnabled){
        this.showMerging=isEnabled;
    }

    /**
     * set an inset display so that display will not touch edge of panel*/
    final public void setInset(int width,int height) {
        this.insetW=width;
        this.insetH=height;
    }

    /**
     * make screen scroll to ensure point is visible
     */
    public void ensurePointIsVisible(Point p){
        super.scrollRectToVisible(new Rectangle(p.x,y_size-p.y,scrollInterval,scrollInterval));
    }
    //<end-adobe>

    /**
     * get sizes of panel <BR>
     * This is the PDF pagesize (as set in the PDF from pagesize) -
     * It now includes any scaling factor you have set (ie a PDF size 800 * 600
     * with a scaling factor of 2 will return 1600 *1200)
     */
    final public Dimension getMaximumSize() {

        Dimension pageSize=null;

        if(displayView!=Display.SINGLE_PAGE)
        pageSize = pages.getPageSize(displayView);

        if(pageSize==null){
            if((displayRotation==90)|(displayRotation==270))
                pageSize= new Dimension((int)(y_size_cropped+insetW+insetW+(xOffset*scaling)+(additionalPageCount*(insetW+insetW))),x_size_cropped+insetH+insetH);
            else
                pageSize= new Dimension((int)(x_size_cropped+insetW+insetW+(xOffset*scaling)+(additionalPageCount*(insetW+insetW))),y_size_cropped+insetH+insetH);

        }

        return pageSize;

    }

    /**
     * get width*/
    final public Dimension getMinimumSize() {

        return new Dimension(100+insetW,100+insetH);
    }

    /**
     * get sizes of panel <BR>
     * This is the PDF pagesize (as set in the PDF from pagesize) -
     * It now includes any scaling factor you have set (ie a PDF size 800 * 600
     * with a scaling factor of 2 will return 1600 *1200)
     */
    final public Dimension getPreferredSize() {
        return getMaximumSize();
    }

    //<start-adobe>
    /**
     * highlight zone on page for text areas
     */
    public void setFoundTextArea(Rectangle area){

            this.rectArea =area;

            //flag screen for redraw

            //this one doesn't work
            //currentManager.addDirtyRegion(this,(int)x1-5,(int)y1-5,(int)(x2-x1)+10,(int)(y2-y1)+10);
            //currentManager.addDirtyRegion(this,0,0,x_size,y_size);
            //this.invalidate();

    }

    /**
     * highlight zones on page for text areas
     */
    public void setFoundTextAreas(Rectangle areas[]){

            this.rectAreas=areas;

            //flag screen for redraw

            //this one doesn't work
            //currentManager.addDirtyRegion(this,(int)x1-5,(int)y1-5,(int)(x2-x1)+10,(int)(y2-y1)+10);
            //currentManager.addDirtyRegion(this,0,0,x_size,y_size);
            //this.invalidate();

    }

    /**
     * allows definition of zones for highlighted text -
     * set to null for no selection
     * Uses PDF co-ordinate system and allows for inset automatically -
     * See SimpleViewer demo
     * (Only works when setHardwareAccelerationforScreen(boolean) is set to
     * <b>false</b>)
     */
    public void setHighlightedAreas(Rectangle[] areas){

    	if(this.displayView!=Display.SINGLE_PAGE){
    		this.areas=null;
    		return;
    	}
    	
        //if inset add in difference transparently
        if(areas!=null){
            Rectangle[] newAreas=new Rectangle[areas.length];
            for(int i=0;i<areas.length;i++){
                if(areas[i]!=null)
                    newAreas[i]= new Rectangle(areas[i].x,
                            areas[i].y,
                            areas[i].width,
                            areas[i].height);

            }

            this.areas=newAreas;

            //flag screen for redraw

            //this one doesn't work
            //currentManager.addDirtyRegion(this,(int)x1-5,(int)y1-5,(int)(x2-x1)+10,(int)(y2-y1)+10);
            currentManager.addDirtyRegion(this,0,0,x_size,y_size);

        }else
            this.areas=null;

    }


    //<start-adobe>
    /**
     * handle context sensitive tooltips -
     * replaces default java routine with our own which is context sensitive  -
     * Used for hotspots
     */
    final public String getToolTipText(MouseEvent e) {
        String result = null;

        Point raw_p = e.getPoint();

        //convert to our co-ords
        current_p = new Point((int) ((raw_p.getX()-insetW)/scaling), (int) (((y_size+insetH)-raw_p.getY())/scaling));

        if((displayHotspots!=null))
            result=displayHotspots.getTooltip(current_p,userAnnotIcons,pageNumber);

        //return default or ours
        return result;
    }

    /**
     * update rectangle we draw to highlight an area -
     * See SimpleViewer example for example code showing current usage.
     */
    final public void updateCursorBoxOnScreen(
        Rectangle newOutlineRectangle,
        Color outlineColor) {

    	if(this.displayView!=Display.SINGLE_PAGE)
    		return;
    	
    	
        //area to reapint
        int x_size=this.x_size;
        int y_size=this.y_size;

        if(newOutlineRectangle!=null){

            int x=newOutlineRectangle.x;
            int y=newOutlineRectangle.y;
            int w=newOutlineRectangle.width;
            int h=newOutlineRectangle.height;

            int cropX=pageData.getCropBoxX(pageNumber);
            int cropY=pageData.getCropBoxY(pageNumber);
            int cropW=pageData.getCropBoxWidth(pageNumber);
            int cropH=pageData.getCropBoxHeight(pageNumber);

            if(x<cropX){
                int diff=cropX-x;
                w=w-diff;
                x=cropX;
            }

            if(y<cropY){
                int diff=cropY-y;
                h=h-diff;
                y=y+diff;
            }
            if((x+w)>(cropW+cropX+xOffset))
                w=cropX+xOffset+cropW-x;
            if((y+h)>(cropY+cropH))
                h=cropY+cropH-y;

            cursorBoxOnScreen = new Rectangle(x,y,w,h);

        }else
            cursorBoxOnScreen=null;

        this.outlineColor = outlineColor;

        int strip=30;

        /**allow offset from page being centered*/
        int dx=0;
        //center if required
        if(alignment==Display.DISPLAY_CENTERED){
            int width=this.getBounds().width;
            int pdfWidth=this.getPDFWidth();

            if(displayView!=Display.SINGLE_PAGE)
                pdfWidth=(int)pages.getPageSize(displayView).getWidth();

            dx=((width-pdfWidth)/2);
        }

        if(lastCursorBoxOnScreen!=null){
            if(displayRotation==0 || displayRotation==180)
                currentManager.addDirtyRegion(this,insetW+dx,insetH,x_size+5+xOffset,y_size);
            else
                currentManager.addDirtyRegion(this,insetH+dx,insetW,y_size+5+xOffset,x_size);

//		    if((displayRotation==90)|(displayRotation==270))
//		        currentManager.addDirtyRegion(this,0,0,y_size+max_y,x_size);
//		    else if((displayRotation==180))
//		        currentManager.addDirtyRegion(this,0,0,x_size+1000,y_size+max_y);
//		    else
//	            currentManager.addDirtyRegion(this,
//	                    (int)(lastCursorBoxOnScreen.x*scaling)-strip,
//            			(int)(((max_y)-lastCursorBoxOnScreen.y-lastCursorBoxOnScreen.height)*scaling)-strip,
//						(int)(lastCursorBoxOnScreen.width*scaling)+strip+strip,
//						(int)(lastCursorBoxOnScreen.height*scaling)+strip+strip);
//
            lastCursorBoxOnScreen=null;
        }



        if(cursorBoxOnScreen!=null){
            currentManager.addDirtyRegion(this,(int)(cursorBoxOnScreen.x*scaling)-strip,
                                    (int)(((max_y)-cursorBoxOnScreen.y-cursorBoxOnScreen.height)*scaling)-strip,
                                    (int)(cursorBoxOnScreen.width*scaling)+strip+strip,
                                    (int)(cursorBoxOnScreen.height*scaling)+strip+strip);
        }

        if(this.viewScaling!=null)
        currentManager.markCompletelyDirty(this);
    }

    /**requests repaint of an area*/
    public void repaintArea(Rectangle screenBox,int maxY){

        int strip=10;

        if(strip<this.insetH)
            strip=insetH;

        if(strip<this.insetW)
            strip=insetW;

        currentManager.addDirtyRegion(this,(int)(screenBox.x*scaling)-strip,
                /**
                (int)(((pageData.getCropBoxHeight(pageNumber)*scaling)-screenBox.y-screenBox.height)*scaling)-strip,
                /*/
                (int)((maxY-screenBox.y-screenBox.height)*scaling)-strip,
                //*/
                (int)((screenBox.x+screenBox.width)*scaling)+strip+strip,
                (int)((screenBox.y+screenBox.height)*scaling)+strip+strip);

    }

    /**
     * update rectangle we draw to show area of object -
     * See org.jpedal.examples.contentextractor.contentExtractor
     */
    final public void removeHiglightedObject(){

        if(lastHighlight!=null){
            currentManager.addDirtyRegion(this,lastHighlight.x-strip,lastHighlight.y-strip,lastHighlight.width+strip+strip,lastHighlight.height+strip+strip);

            currentHighlightedObject=null;

        }

    }

    /**
     * update rectangle we draw to show area of object for Storypad -
     * (NOT PART OF API and subject to change)
     */
    final public void addHiglightedObject(
        Rectangle currentShape,
        Color outlineHighlightColor) {
    	
    	//over-ride in multipage mode
    	if(this.displayView!=Display.SINGLE_PAGE)
    		return;
    	
        this.currentHighlightedObject = currentShape;
        this.outlineHighlightColor = outlineHighlightColor;

        if((currentHighlightedObject!=null)&&(currentHighlightedObject!=lastHighlight)){

            currentManager.addDirtyRegion(this,currentHighlightedObject.x-strip,currentHighlightedObject.y-strip,currentHighlightedObject.width+strip+strip,currentHighlightedObject.height+strip+strip);

            lastHighlight=currentHighlightedObject;
        }
    }

    //<end-adobe>


    public void paint(Graphics g){
    	
		try{

		super.paint(g);

		if(!isDecoding){

            /**add any highlighted rectangle on screen*/
            if (cursorBoxOnScreen != null){
                Graphics2D g2=(Graphics2D) g;
                AffineTransform defaultAf=g2.getTransform();
                /**
                if(displayRotation==0 || displayRotation==180)
                    g2.setClip(insetW,insetH,(int)(pageData.getCropBoxWidth(pageNumber)*scaling),(int)(pageData.getCropBoxHeight(pageNumber)*scaling));
                else
                    g2.setClip(insetH,insetW,(int)(pageData.getCropBoxHeight(pageNumber)*scaling),(int)(pageData.getCropBoxWidth(pageNumber)*scaling));
                */
                if(cursorAf!=null){
                    g2.setTransform(cursorAf);

                    Shape clip=g2.getClip();

                    //remove clip for drawing outline
                    if((alignment==Display.DISPLAY_CENTERED)&&(clip!=null))
                        g2.setClip(null);

                    //<start-adobe>
                    paintRectangle(g2);
                    //<end-adobe>

                    g2.setClip(clip);

                    g2.setTransform(defaultAf);

                }
            }
        }

		}catch(Exception e){

			pages.stopGeneratingPage();
			pages.flushPageCaches();

		}catch(Error err){  //for tight memory

			pages.flushPageCaches();
			pages.stopGeneratingPage();


			paint(g);

		}


	}

    /**standard method to draw page and any highlights onto JPanel*/
    public void paintComponent(Graphics g) {

        //if(this.backgroundIsTransparent)
            //currentDisplay.setBackgroundColor(null);
		if(Display.debugLayout)
			System.out.println("PaintComponent called ");

        if (SwingUtilities.isEventDispatchThread())
            threadSafePaint(g);
        else {
            final Graphics g2 = g;
            final Runnable doPaintComponent = new Runnable() {
                public void run() {
                    threadSafePaint(g2);
                }
            };
            SwingUtilities.invokeLater(doPaintComponent);
        }

    }

    /**
     * update display
     */
    private void threadSafePaint(Graphics g) {

      //System.out.println("threadSafePaint1-------------------------------------------------------- "+displayRotation);

        super.paintComponent(g);

        if(displayScaling==null)
                   return;

		Graphics2D g2 = (Graphics2D) g;
		
/**		GraphicsDevice newGD = g2.getDeviceConfiguration().getDevice();
		if(currentGraphicsDevice ==null){
			currentGraphicsDevice = newGD;
		}else if(!currentGraphicsDevice.equals(newGD)){
			// REDECODE PAGE
			System.out.println("screen changed");
			ConvertToString.printStackTrace(1);
			//@chris TODO add method to call decode page for multi screen displays
			currentGraphicsDevice = newGD;
		}
*/
		
        //remember so we can ut it back
		final AffineTransform rawAf=g2.getTransform();

		if(Display.debugLayout)
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>START PAINT");

        //<start-adobe>
        /**
		 * lazy initialisation to add forms to screen
		 */

        if ((formsAvailable) && (renderPage) && (!stopDecoding)) {

            //track all changes
            int start=pageNumber;
            int end=pageNumber;

            if(displayView!=Display.SINGLE_PAGE){
                start=pages.getStartPage();
                end=pages.getEndPage();
                if(start==0 || end==0 || lastEnd!=end || lastStart!=start)
                    lastFormPage=-1;

                lastEnd=end;
                lastStart=start;

            }

            if(lastFormPage!=pageNumber){

                lastFormPage=pageNumber;

                if ((currentFormRenderer != null)
                        && (currentAcroFormData != null) && (!stopDecoding)) {
                    currentFormRenderer.displayComponentsOnscreen(start,end, this, scaling, displayRotation);

                }

                if ((showAnnotations) && (currentAnnotRenderer != null)
                        && (annotsData != null) && (!stopDecoding)) {
                    currentAnnotRenderer.displayComponentsOnscreen(start,end, this, scaling, displayRotation);
                }
            }

        }
        //<end-adobe>

        if(DynamicVectorRenderer.debugPaint)
            System.err.println("threadsafePaint called "+this.displayView);
		pages.init(scaling,pageCount,displayRotation,pageNumber,currentDisplay,false, pageData,insetW,insetH);
		int indent=0;

        //center if required
        if(alignment==Display.DISPLAY_CENTERED){
            int width=this.getBounds().width;
            int pdfWidth=this.getPDFWidth();

            if(displayView!=Display.SINGLE_PAGE)
                pdfWidth=(int)pages.getPageSize(displayView).getWidth();

            if(width>pdfWidth)  {
                indent=((width-pdfWidth)/2);

                if(displayView==Display.SINGLE_PAGE)
                	lastIndent=indent;
                else if(displayView==Display.CONTINUOUS && lastIndent!=-1){
                	indent=lastIndent;
                	lastIndent = -1;
                }else
                	lastIndent=-1;

                g2.translate(indent,0);
            }

            //<start-adobe>
            if(formsAvailable){


                if(currentFormRenderer!=null && currentAcroFormData!=null)
                    currentFormRenderer.resetScaledLocation(scaling,displayRotation,indent);

                if((showAnnotations)&&(currentAnnotRenderer!=null))
                    currentAnnotRenderer.resetScaledLocation(scaling,displayRotation,indent);
            }
            //<end-adobe>

        }

		/**we need to store the Affine and put it back at
         * the end otherwise we f**k up all the form components
         * on certain pages
         */
        Rectangle dirtyRegion=null;

		pages.initRenderer(areas,g2,myBorder,indent);

		if((!isDecoding)||(drawInteractively)){
			actualBox =pages.drawPage(viewScaling,displayScaling,pageUsedForTransform);
        }else //just fill the background
            currentDisplay.paintBackground(g2, dirtyRegion);

        //disabnled if not in Single PAGE
        if(displayView==Display.SINGLE_PAGE){

            /**
             * draw highlighted text boxes
             */

            //add highlights
            if(rectArea !=null){

                AffineTransform defaultScaling=null;
                if(viewScaling!=null){
                    defaultScaling=g2.getTransform();
                    g2.transform(viewScaling);
                }

                Composite opacity=g2.getComposite();
                g2.setColor(Color.blue);
                g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.7f ) );

                if(rectArea !=null)
                    g2.fillRect(rectArea.x, rectArea.y, rectArea.width, rectArea.height);

                g2.setComposite(opacity);

                if(viewScaling!=null)
                    g2.setTransform(defaultScaling);
            }

            if(rectAreas !=null){

                AffineTransform defaultScaling=null;
                if(viewScaling!=null){
                    defaultScaling=g2.getTransform();
                    g2.transform(viewScaling);
                }

                Composite opacity=g2.getComposite();
                g2.setColor(Color.blue);
                g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.7f ) );

                for(int jj=0;jj<rectAreas.length;jj++){

                    Rectangle rectArea=rectAreas[jj];

                    if(rectArea !=null)
                        g2.fillRect(rectArea.x, rectArea.y, rectArea.width, rectArea.height);

                }

                g2.setComposite(opacity);

                if(viewScaling!=null)
                    g2.setTransform(defaultScaling);
            }


            //reset after first clear
            if ((currentHighlightedObject==null)&&(lastHighlight!=null))
                lastHighlight=null;

            /**
             * add any viewport
             */
            AffineTransform beforeView=g2.getTransform();
            if(viewScaling!=null)
            g2.transform(viewScaling);

            //<start-adobe>

            /**set any highlighted zones*/
            if (highlightedZonesSelected != null)
                paintHighlights(g2);

			/**draw any annotations*/
            if(displayHotspots!=null)
                displayHotspots.addHotspotsToDisplay(g2,userAnnotIcons,pageNumber);

            /**shows merging for debugging*/
            if((merge_level!=null)&&(showMerging))
                paintMergingInfo(g2);

            //<end-adobe>

            //remove any viewport
    //			g2.setTransform(beforeView);

            /**add any highlighted rectangle on screen*/
            if (cursorBoxOnScreen != null)
                this.cursorAf=g2.getTransform();
            else
                this.actualBox=null;

            pages.resetToDefaultClip();

            //draw highlight underneath
            if (currentHighlightedObject != null) {
                g2.setColor(outlineHighlightColor);
                g2.draw(currentHighlightedObject);
            }


            if (showCrop) {
                g2.setColor(Color.orange);
                pages.completeForm(g2);

            }

        }

        /**
         * draw other pages if not in SINGLE mode
         **/
        pages.drawBorder();

        g2.setTransform(rawAf);

		if(Display.debugLayout)
				System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<END PAINT ");

	}


    //<start-adobe>


    /**
     * not part of API - used by Storypad
     */
    private void paintMergingInfo(Graphics2D g2) {
        int merge_count = merge_outline.size() - 1;
        for( int i = 0;i < merge_count;i++ ){
            Shape s = merge_outline.elementAt( i );
            int level = merge_level.elementAt( i );

            if( ( showDebugLevel[level]) & ( s != null ) ){
                g2.setColor( debugColors[level]);

                g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.1f ) );
                g2.draw( s.getBounds() );
                g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.3f ) );
                g2.fill( s );
            }
        }
    }

    /**
     * turn crossHairs on or off -
     * highlight <b>newBoxContained</b> handle with specified Color,<br>
     * if <b>newBoxContained</b> is -1 no handles are highlighted -
     * See org.jpedal.examples.contentextractor.ContentExtractor
     */
    public void setDrawCrossHairs(boolean newDrawCrossHairs,int newBoxContained,Color newColor){
        drawCrossHairs = newDrawCrossHairs;
        boxContained = newBoxContained;
        selectedHandleColor = newColor;
    }

    /**
     * draw cursorBox on screen with specified color,
     */
    private void paintRectangle(Graphics2D g2){

        Stroke oldStroke = g2.getStroke();//copy before to stop page border from being dotted        
        Stroke lineStroke;
        
        //allow for negative
        if(scaling<0)
        	lineStroke = new BasicStroke(1/-scaling);
        else
        	lineStroke = new BasicStroke(1/scaling);
        
        g2.setStroke(lineStroke);

        g2.setColor(outlineColor);
        g2.draw(cursorBoxOnScreen);

        if(drawCrossHairs){

            int x1 = cursorBoxOnScreen.x;
            int y1 = cursorBoxOnScreen.y;
            int x2 = x1+cursorBoxOnScreen.width;
            int y2 = y1+cursorBoxOnScreen.height;

            int mediaW = pageData.getMediaBoxWidth(pageNumber);
            int mediaH = pageData.getMediaBoxHeight(pageNumber);
            int mediaX = pageData.getMediaBoxX(pageNumber);
            int mediaY = pageData.getMediaBoxY(pageNumber);

            if(scaling>0)
	            g2.setStroke(new BasicStroke(3/scaling, BasicStroke.CAP_ROUND,
	                    BasicStroke.JOIN_ROUND, 0, new float[]{0,6/scaling,0,6/scaling}, 0));
            else
            	g2.setStroke(new BasicStroke(3/-scaling, BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND, 0, new float[]{0,6/-scaling,0,6/-scaling}, 0));

            //draw dotted lines to edges
            g2.drawLine(x1,y1,mediaX,y1);
            g2.drawLine(x1,y1,x1,mediaY);
            g2.drawLine(x2,y1,mediaW,y1);
            g2.drawLine(x2,y1,x2,mediaY);

            g2.drawLine(x1,y2,mediaX,y2);
            g2.drawLine(x1,y2,x1,mediaH);
            g2.drawLine(x2,y2,mediaW,y2);
            g2.drawLine(x2,y2,x2,mediaH);


            Rectangle[] cursorBoxHandles = new Rectangle[8];
            //*centre of line handles
            //left
            cursorBoxHandles[0] = new Rectangle(x1-handlesGap,(y1+(Math.abs(y2-y1))/2)-handlesGap,handlesGap*2,handlesGap*2);//0
            //bottom
            cursorBoxHandles[1] = new Rectangle((x1+(Math.abs(x2-x1))/2)-handlesGap,y1-handlesGap,handlesGap*2,handlesGap*2);//1
            //top
            cursorBoxHandles[2] = new Rectangle((x1+(Math.abs(x2-x1))/2)-handlesGap,y2-handlesGap,handlesGap*2,handlesGap*2);//2
            //right
            cursorBoxHandles[3] = new Rectangle(x2-handlesGap,(y1+(Math.abs(y2-y1))/2)-handlesGap,handlesGap*2,handlesGap*2);//3
            /**/

            //*corner handles
            //bottom left
            cursorBoxHandles[4] = new Rectangle(x1-handlesGap,y1-handlesGap,handlesGap*2,handlesGap*2);//4
            //top left
            cursorBoxHandles[5] = new Rectangle(x1-handlesGap,y2-handlesGap,handlesGap*2,handlesGap*2);//5
            //bottom right
            cursorBoxHandles[6] = new Rectangle(x2-handlesGap,y1-handlesGap,handlesGap*2,handlesGap*2);//6
            //top right
            cursorBoxHandles[7] = new Rectangle(x2-handlesGap,y2-handlesGap,handlesGap*2,handlesGap*2);//7

            /**/

            g2.setStroke(lineStroke);
            //draw handle box containing cursor
            if(boxContained!=-1 && boxContained<cursorBoxHandles.length){
                if(selectedHandleColor!=null){
                    Color old = g2.getColor();
                    g2.setColor(selectedHandleColor);
                    g2.fill(cursorBoxHandles[boxContained]);
                    g2.setColor(old);
                }else
                    g2.fill(cursorBoxHandles[boxContained]);
            }

            //draw other handles
            for(int i=0;i<cursorBoxHandles.length;i++){
                if(i!=boxContained)
                    g2.draw(cursorBoxHandles[i]);
            }
        }
        g2.setStroke(oldStroke);

        if(actualBox==null){
            lastCursorBoxOnScreen=cursorBoxOnScreen;

        }else{

            Rectangle b1=cursorBoxOnScreen.getBounds();
            int minX=(int)b1.getMinX();
            int minY=(int)b1.getMinY();
            int maxX=(int)b1.getMaxX();
            int maxY=(int)b1.getMaxY();

            Rectangle bounds=actualBox.getBounds();
            int tmp=(int)bounds.getMinX();
            if(tmp<minX)
                minX=tmp;
            tmp=(int) bounds.getMinY();
            if(tmp<minY)
                minY=tmp;
            tmp=(int) bounds.getMaxX();
            if(tmp>maxX)
                maxX=tmp;
            tmp=(int)bounds.getMaxY();
            if(tmp>maxY)
                maxY=tmp;

            lastCursorBoxOnScreen=new Rectangle(minX-5,minY-5,10+maxX-minX,10+(maxY-minY));
        }
    }

    /**
     * put selected areas onto screen display as highlights
     */
    private void paintHighlights(Graphics2D g2) {

		if(alternateOutlines!=null){

			int items=alternateOutlines.length;

			for (int i = 0; i < items; i++) {


				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float) 0.1));
				g2.setColor(Color.darkGray);

				g2.fill(alternateOutlines[i]);
				//g2.draw(alternateOutlines[i]);

				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float) 0.9));
				g2.draw(alternateOutlines[i]);

			}

			if(merge_outline!=null && (!altName.equals("Nothing") && !altName.equals("Lines"))){

				int merge_count = merge_outline.size() - 1;

				for( int i = 0;i < merge_count;i++ ){
					Shape s = merge_outline.elementAt( i );
					int level = merge_level.elementAt( i );

					if( level==2 & ( s != null ) ){
						g2.setColor( debugColors[level]);

						g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.1f ) );
						g2.draw( s.getBounds() );
						g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.3f ) );
						g2.fill( s );
					}
				}
			}

		}else{

            int items=highlightedZonesSelected.length;

			for (int i = 0; i < items; i++) {

				if (highlightedZonesSelected[i]){
					highlightStoryOnscreen(g2, i);
				}else if (((highlightMode & SHOW_OBJECTS) == SHOW_OBJECTS)&&(fragmentShapes[i]!=null)) {
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float) 0.1));
					if(highlightColors[i]==null)
						g2.setColor(Color.darkGray);
					else
						g2.setColor(highlightColors[i]);

					g2.fill(fragmentShapes[i]);
					g2.draw(outlineZone[i]);

					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float) 0.9));
					g2.draw(outlineZone[i]);

					if(linkedItems!=null){
						int[] ii=(int[]) linkedItems[i];
						if(ii!=null)
							numberItems(false,g2, i+"-",ii);
					}
				}
			 }


            //show page furniture
			if((pageLines!=null)&&((highlightMode & SHOW_LINES) == SHOW_LINES))
				pageLines.drawLines(g2);

			//show page furniture
			if((pageLines!=null)&&((highlightMode & SHOW_BOXES) == SHOW_BOXES))
				pageLines.drawBoxes(g2);



            /**add selection order for highlights*/
			if(selectionOrder!=null)
				numberItems(false,g2, "",selectionOrder);


        }
	}

    /**
     * draw any outline around story and any linked items
     */
    private void highlightStoryOnscreen(Graphics2D g2, int i) {

            if(fragmentShapes[i]!=null){

            if(highlightColors[i]==null)
                g2.setColor(Color.blue);
            else
                g2.setColor(highlightColors[i]);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float) 0.1));

            g2.fill(fragmentShapes[i]);
            g2.draw(outlineZone[i]);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float) 0.9));
            g2.draw(outlineZone[i]);

            int xs=outlineZone[i].getBounds().x;
            int ys=outlineZone[i].getBounds().y;
            g2.drawLine(xs,ys+(int)outlineZone[i].getBounds().getHeight(),
                    xs+(int)outlineZone[i].getBounds().getWidth(),ys);

            if(processedByRegularExpression[i]>0)
            g2.drawLine(xs+(int)outlineZone[i].getBounds().getWidth(),ys+(int)outlineZone[i].getBounds().getHeight(),
                                    xs,ys);
        }

    }
    /**
     * add numbers to selected items
     */
    private void numberItems(boolean isSublist,Graphics2D g2, String prefix,int[] selectionOrder) {

        int itemCount=selectionOrder.length;

        if(itemCount==0)
            return;
        int head=selectionOrder[0];
        int order=1;

        for(int ii=0;ii<itemCount;ii++){
            int i=selectionOrder[ii];

            if(i==-1){
                ii=itemCount;
            }else{

                String value=prefix+""+(order);

                //see if linked items and highlight
                if(linkedItems==null){

                    if(fragmentShapes[i]!=null)
                     numberItem(g2, i, value);

                  order++;
                }else{
                    int[] currentLinks=(int[])linkedItems[i];

                    if(currentLinks!=null){
                        order++;
                        int childCount=currentLinks.length;
                        int item=0;
                        for(int j=0;j<childCount;j++){
                            int childID=currentLinks[j];

                            item++;
                            if(childID==-1)
                                j=childCount;
                            else if(fragmentShapes[i]!=null)
                                numberItem(g2, childID, value+"."+(item));

                        }


                        //System.out.println("---recurse end");
                    }else if(this.parents[i]==-1){
                            if(fragmentShapes[i]!=null)
                                numberItem(g2, i, value);

                            order++;
                    }
                }
            }
        }
    }

    /**
     * @param g2
     * @param i
     * @param value
     */
    private void numberItem(Graphics2D g2, int i, String value) {

        AffineTransform af=new AffineTransform();
        GlyphVector gv;
        gv=highlightFont.createGlyphVector(g2.getFontRenderContext(),value);

        af.scale(1,-1);
        af.translate(cx[i],-cy[i]);

        Area a=new Area(gv.getOutline());
        a.transform(af);

        g2.setColor(Color.black);
        g2.fill(a.getBounds());

        g2.setColor(Color.white);
        g2.fill(a);
    }


    private void flagNumbersForRedraw(int max_y){

        /**add selection order*/
        if((selectionOrder!=null)&&(cx!=null)&&(cy!=null)){

            for(int ii=0;ii<this.selectionOrder.length;ii++){

                int i=selectionOrder[ii];

                if(i==-1){
                    ii=selectionOrder.length;
                }else{
                    currentManager.addDirtyRegion(this,(int)(cx[i]*scaling),(int)((max_y-cy[i]-25)*scaling),(int)(25*scaling),(int)(25*scaling));

                    int[] currentLinks=(int[])linkedItems[i];

                    if(currentLinks!=null){
                        for(int i2=0;i2<currentLinks.length;i2++){

                            int j=currentLinks[i2];

                            if(j==-1){
                                i2=currentLinks.length;
                            }else{
                                currentManager.addDirtyRegion(this,(int)(cx[j]*scaling),(int)((max_y-cy[j]-25)*scaling),
                                        (int)(25*scaling),(int)(25*scaling));
                            }
                        }
                    }

                }
            }
        }
    }

    //<end-adobe>


    /**
     * get sizes of panel <BR>
     * This is the PDF pagesize (as set in the PDF from pagesize) -
     * It now includes any scaling factor you have set
     */
    final public int getPDFWidth() {
        if((displayRotation==90)|(displayRotation==270))
            return y_size+insetW+insetW;
        else
            return x_size+insetW+insetW;

    }

    /**
     * get raw width for image
     */
    final public int getRawPDFWidth() {
        if((displayRotation==90)| (displayRotation==270))
            return y_size;
        else
            return x_size;

    }


    //<start-adobe>

    /**
     * allow user to set component for waring message in renderer to appear -
     * if unset no message will appear
     * @param frame
     */
    public void setMessageFrame(JFrame frame){
        currentDisplay.setMessageFrame(frame);
    }

    //<end-adobe>


    /**
     * get sizes of panel -
     * This is the PDF pagesize
     */
    final public int getPDFHeight() {
        if((displayRotation==90)|(displayRotation==270))
            return x_size+insetH+insetH;
        else
            return y_size+insetH+insetH;

    }

    /**
     * get sizes of page excluding any insets
     */
    final public int getRawPDFHeight() {
        if((displayRotation==90)|(displayRotation==270))
            return x_size;
        else
            return y_size;

    }

    //<start-adobe>

    /**return the hotspots areas for the page (ie regions of Annotations).
     * See org.jpedal.examples.simpleviewer.SimpleViewer for sample code*/
    public Rectangle[] getPageHotspots(){

        if(displayHotspots!=null)
            return displayHotspots.getAnnotationhotSpots();
        else
            return null;
    }
    //<end-adobe>


    /**do not display border when screen printed*/
    public void disableBorderForPrinting(){
        useBorder=false;
    }

    /**set border for screen and print which will be displayed<br>
     * Setting a new value will enable screen and border painting - disable
     * with disableBorderForPrinting() */
    final public void setPDFBorder(Border newBorder){
        this.myBorder=newBorder;

        //switch on as default
        useBorder=true;
    }

    /**
     * workout Transformation to use on image
     */
    protected final AffineTransform getScalingForImage(int pageNumber,int rotation,float scaling) {
        /**/
         //poss new code
        double mediaX = pageData.getMediaBoxX(pageNumber)*scaling;
        double mediaY = pageData.getMediaBoxY(pageNumber)*scaling;
        //double mediaW = pageData.getMediaBoxWidth(pageNumber)*scaling;
        double mediaH = pageData.getMediaBoxHeight(pageNumber)*scaling;

        double crw = pageData.getCropBoxWidth(pageNumber)*scaling;
        double crh = pageData.getCropBoxHeight(pageNumber)*scaling;
        double crx = pageData.getCropBoxX(pageNumber)*scaling;
        double cry = pageData.getCropBoxY(pageNumber)*scaling;

        //create scaling factor to use
        AffineTransform displayScaling = new AffineTransform();

        //** new x_size y_size declaration *
        int x_size=(int) (crw+(crx-mediaX));
        int y_size=(int) (crh+(cry-mediaY));

        if (rotation == 270) {

            displayScaling.rotate(-Math.PI / 2.0, x_size/ 2, y_size / 2);

            double x_change = (displayScaling.getTranslateX());
            double y_change = (displayScaling.getTranslateY());
            displayScaling.translate((y_size - y_change), -x_change);
            displayScaling.translate(0, y_size);
            displayScaling.scale(1, -1);
            displayScaling.translate(-(crx+mediaX), -(mediaH-crh-(cry-mediaY)));

        } else if (rotation == 180) {

            displayScaling.rotate(Math.PI, x_size / 2, y_size / 2);
            displayScaling.translate(-(crx+mediaX),y_size+(cry+mediaY)-(mediaH-crh-(cry-mediaY)));
            displayScaling.scale(1, -1);

        } else if (rotation == 90) {

            displayScaling.rotate(Math.PI / 2.0);
            displayScaling.translate(0,(cry+mediaY)-(mediaH-crh-(cry-mediaY)));
            displayScaling.scale(1, -1);

        }else{
            displayScaling.translate(0, y_size);
            displayScaling.scale(1, -1);
            displayScaling.translate(0, -(mediaH-crh-(cry-mediaY)));
        }

        displayScaling.scale(scaling,scaling);

        /*/
          //old code
          //create scaling factor to use
          AffineTransform displayScaling = new AffineTransform();

          if (raw_rotation == 270) {

              displayScaling.rotate(-Math.PI / 2.0, pageWidth/ 2, pageHeight / 2);

              double x_change = (displayScaling.getTranslateX());
              double y_change = (displayScaling.getTranslateY());
              displayScaling.translate((pageHeight - y_change), -x_change);

          } else if (raw_rotation == 180) {
              displayScaling.rotate(Math.PI, pageWidth / 2, pageHeight / 2);
          } else if (raw_rotation == 90) {

              displayScaling.rotate(Math.PI / 2.0,  pageWidth / 2,  pageHeight / 2);

              double x_change =(displayScaling.getTranslateX());
              double y_change = (displayScaling.getTranslateY());
              displayScaling.translate(-y_change, pageWidth - x_change);
          }

          displayScaling.translate(pageWidth, pageHeight);
          displayScaling.scale(1, -1);

          int mediaX = pageData.getMediaBoxX(pageNumber);
          int mediaY = pageData.getMediaBoxY(pageNumber);

          displayScaling.translate(-pageWidth-(mediaX*scaling), -(mediaY*scaling));
          displayScaling.scale(scaling,scaling);
          /**/
        /**
        if(raw_rotation == 90){
            displayScaling.translate(insetH/scaling,insetW/scaling);
        }else if(raw_rotation == 270){
            displayScaling.translate(-insetH/scaling,-insetW/scaling);
        }else if(raw_rotation == 180){
            displayScaling.translate(-insetW/scaling,insetH/(scaling));
        }else
            displayScaling.translate(insetW/scaling,-insetH/scaling);
        */
        return displayScaling;
    }

    /**
     * initialise panel and set size to display during updates
     * and update the AffineTransform to new values<br>
     * @param newRotation - sets display rotation to this value
     */
    final public void setPageRotation(int newRotation) {

        //DO NOT DO THIS!!!
        //This code is also called to alter scaling
        //if(newRotation==oldRotation)
            //return;

        displayRotation = newRotation;

        //assume unrotated for multiple views and rotate on a page basis
        if(displayView!=Display.SINGLE_PAGE)
        	newRotation=0;

        /**/
		pageUsedForTransform=pageNumber;
		displayScaling = getScalingForImage(pageNumber,newRotation,scaling);//(int)(pageData.getCropBoxWidth(pageNumber)*scaling),(int)(pageData.getCropBoxHeight(pageNumber)*scaling),

        if(newRotation == 90){
            displayScaling.translate(insetH/scaling,insetW/scaling);
        }else if(newRotation == 270){
            displayScaling.translate(-insetH/scaling,-insetW/scaling);
        }else if(newRotation == 180){
            displayScaling.translate(-insetW/scaling,insetH/(scaling));
        }else{
            displayScaling.translate(insetW/scaling,-insetH/scaling);
        }


        //force redraw if screen being cached
        pages.refreshDisplay();


        /**
         * now apply any viewport scaling
         */
        if(this.viewableArea!=null){

            viewScaling=new AffineTransform();

            /**workout scaling and choose larger*/
            double dx=(double)viewableArea.width/(double)pageData.getCropBoxWidth(pageNumber);
            double dy=(double)viewableArea.height/(double)pageData.getCropBoxHeight(pageNumber);
            double viewScale=dx;
            if(dy<dx)
                viewScale=dy;

            /**workout any translation*/
            double x=viewableArea.x;//left align
            double y=viewableArea.y+(viewableArea.height-(pageData.getCropBoxHeight(pageNumber)*viewScale));//top align
            //double x=viewableArea.x+(viewableArea.width-(pageData.getCropBoxWidth(pageNumber)*viewScale));//right align
            //double y=viewableArea.y;//bottom align

            //if(crx>0)
            //	x=(int) (x+crx);
            viewScaling.translate(x,y);
            viewScaling.scale(viewScale,viewScale);

        }else
            viewScaling=null;


    }

    /** calls setPageParameters(scaling,pageNumber) after setting rotation to draw page */
    final public void setPageParameters(float scaling, int pageNumber,int newRotation) {

    	if(pages!=null)
    		pages.setScaling(scaling);

    	isNewRotationSet=true;
        displayRotation=newRotation;

        setPageParameters(scaling, pageNumber);
    }

    /**
     * initialise panel and set size to fit PDF page<br>
     * intializes display with rotation set to the default, specified in the PDF document
     */
    final public void setPageParameters(float scaling, int pageNumber) {

        this.pageNumber = pageNumber;
        
        this.scaling=scaling;

        int mediaW = pageData.getMediaBoxWidth(pageNumber);
        max_y = pageData.getMediaBoxHeight(pageNumber);
        max_x = pageData.getMediaBoxWidth(pageNumber);
        //int mediaX = pageData.getMediaBoxX(pageNumber);
        //int mediaY = pageData.getMediaBoxY(pageNumber);

        int cropW = pageData.getCropBoxWidth(pageNumber);
        int cropH = pageData.getCropBoxHeight(pageNumber);
        //int cropX = pageData.getCropBoxX(pageNumber);
        //int cropY = pageData.getCropBoxY(pageNumber);

        x_size_cropped = (int)(cropW*scaling);
        y_size_cropped = (int)(cropH*scaling);

        this.x_size =(int) ((cropW)*scaling);
        this.y_size =(int) ((cropH)*scaling);
      
        if(!isNewRotationSet)
            displayRotation = pageData.getRotation(pageNumber);
        else
            isNewRotationSet=false;

        currentDisplay.init(mediaW,max_y,displayRotation);

        /**update the AffineTransform using the current rotation*/
        setPageRotation(displayRotation);

    }

    /**
     * initialise panel to display during updates<br>
     * pageScaling of 1 is 100%
     */
    final protected AffineTransform setPageParametersForImage(float scaling, int pageNumber) {

        //create scaling factor to use
        AffineTransform imageScaling = new AffineTransform();

        //int mediaW = pageData.getMediaBoxWidth(pageNumber);
        //int mediaH = pageData.getMediaBoxHeight(pageNumber);
        //int mediaX = pageData.getMediaBoxX(pageNumber);
        //int mediaY = pageData.getMediaBoxY(pageNumber);

        int crw = pageData.getCropBoxWidth(pageNumber);
        int crh = pageData.getCropBoxHeight(pageNumber);
        int crx = pageData.getCropBoxX(pageNumber);
        int cry = pageData.getCropBoxY(pageNumber);
        /**allow for rotation*/
        if((displayRotation==90)|(displayRotation==270)){
            int tmp=crw;
            crw=crh;
            crh=tmp;
        }

        int image_x_size =(int) ((crw)*scaling);
        int image_y_size =(int) ((crh)*scaling);

        int raw_rotation = pageData.getRotation(pageNumber);

        imageScaling.translate(-crx*scaling,+cry*scaling);

        if (raw_rotation == 270) {

            imageScaling.rotate(-Math.PI / 2.0, image_x_size/ 2, image_y_size / 2);

            double x_change = (imageScaling.getTranslateX());
            double y_change = (imageScaling.getTranslateY());
            imageScaling.translate((image_y_size - y_change), -x_change);

        } else if (raw_rotation == 180) {

            imageScaling.rotate(Math.PI, image_x_size / 2, image_y_size / 2);

        } else if (raw_rotation == 90) {

            imageScaling.rotate(Math.PI / 2.0,  image_x_size / 2,  image_y_size / 2);

            double x_change =(imageScaling.getTranslateX());
            double y_change = (imageScaling.getTranslateY());
            imageScaling.translate(-y_change, image_x_size - x_change);

        }

        if(scaling<1){
            imageScaling.translate(image_x_size, image_y_size);
            imageScaling.scale(1, -1);
            imageScaling.translate(-image_x_size, 0);

            imageScaling.scale(scaling,scaling);
        }else{
            imageScaling.translate(image_x_size, image_y_size);
            imageScaling.scale(1, -1);
            imageScaling.translate(-image_x_size, 0);

            imageScaling.scale(scaling,scaling);
        }

        //System.out.println("scale="+pageScale+" "+print_x_size+" "+print_y_size);

        return imageScaling;
    }


    /**
     * Enables/Disables hardware acceleration of screen rendering in 1.4 (default is on)
     */
    public void setHardwareAccelerationforScreen(boolean useAcceleration) {
        this.useAcceleration = useAcceleration;
    }

    //<start-adobe>

    /**return amount to scroll window by when scrolling (default is 10)*/
    public int getScrollInterval() {
        return scrollInterval;
    }

    /**set amount to scroll window by when scrolling*/
    public void setScrollInterval(int scrollInterval) {
        this.scrollInterval = scrollInterval;
    }

    /**allows user to switch on/off display (default is on)*/
    public void setAnnotationsVisible(boolean showAnnotations) {
        this.showAnnotations = showAnnotations;
    }
    
    /**
     * JPedal will now draw the screen only when fully decoded rather than on any paint
     * - to restore previous default behaviour (if required), call this
     * routine with true
     */
    public void setDrawInteractively(boolean drawInteractively) {
        this.drawInteractively = drawInteractively;
    }

    /**
     * returns view mode used in panel -
     * SINGLE_PAGE,CONTINUOUS,FACING,CONTINUOUS_FACING
     * (has no effect in OS versions)
     */
    public int getDisplayView() {
        return displayView;
    }

    //<end-adobe>


    /**read current Page scaling mode used for printing*/
    public int getPrintPageScalingMode() {
        return pageScalingMode;
    }

    /**
     * set page scaling mode to use - default setting is
     * PAGE_SCALING_REDUCE_TO_PRINTER_MARGINS
     * All values start PAGE_SCALING
     */
    public void setPrintPageScalingMode(int pageScalingMode) {
        this.pageScalingMode = pageScalingMode;
    }
    public void setUsePDFPaperSize(boolean usePDFPaperSize) {
        this.usePDFPaperSize = usePDFPaperSize;
    }
    

    //<start-adobe>

    //<end-adobe>

}
