package org.jpedal;

//<start-adobe>
//<start-thin>
import org.jpedal.examples.simpleviewer.gui.generic.GUIThumbnailPanel;
//<end-thin>
//<end-adobe>

import org.jpedal.objects.PdfPageData;

import javax.swing.border.Border;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.HashMap;

//<start-13>
import java.awt.image.VolatileImage;
//<end-13>

import java.awt.*;
import java.awt.geom.AffineTransform;

import org.jpedal.parser.PdfStreamDecoder;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.utils.repositories.Vector_Int;
/*
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
* SingleDisplay.java
* ---------------
* (C) Copyright 2006, by IDRsolutions and Contributors.
*

* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/

public class SingleDisplay implements Display {

    //used to flag we can decode multiple pages
    boolean isGeneratingOtherPages=false;

    Rectangle userAnnot= null;
    
    AffineTransform rawAf;
    Shape rawClip;

    DynamicVectorRenderer currentDisplay;

    // flag shows if running
    boolean running = false;

    protected PageOffsets currentOffset;

    /** used for creating the image size of volatileImage*/
    protected int volatileWidth, volatileHeight;

    int startViewPage=0;
    int endViewPage=0;

    Map pagesDrawn=new HashMap();

    Map cachedPageViews=new WeakHashMap();

    Map currentPageViews=new HashMap();

    boolean screenNeedsRedrawing;

    PdfDecoder pdf;

    protected Vector_Int offsets=new Vector_Int(3);
    protected Map newPages=new HashMap();
    protected int currentXOffset=0,additionalPageCount=0;


    /**any scaling factor being used to convert co-ords into correct values
     * and to alter image size
     */
    float oldScaling=-1,oldRotation=-1,oldVolatileWidth=-1,oldVolatileHeight=-1;

    /**centering on page*/
    int indent=0;

    /**used to draw pages offset if not in SINGLE_PAGE mode*/
    int[] xReached,yReached,pageW,pageH;

    boolean[] isRotated;

    /**optimise page redraws*/
    Map accleratedPagesAlreadyDrawn=new HashMap();

    /**flag to switch back to unaccelerate screen if no enough memory for scaling*/
    boolean overRideAcceleration=false;

	//to remind me to add feature back
    private boolean message=false;


    /**local copies*/
    int displayRotation,displayView=SINGLE_PAGE;

    int insetW,insetH;

    float scaling;
    
    int pageNumber;
    int pageCount=0;

    PdfPageData pageData=null;

    /**flag to optimse calculations*/
    private int lastPageChecked=-1,lastState=-1;

    Graphics2D g2;

    Rectangle[] areas;

    /**render screen using hardware acceleration*/
    boolean useAcceleration=true;

    boolean isInitialised;

    //rectangle onscreen
    int rx=0,ry=0,rw=0,rh=0;

    /**used to draw demo cross*/
    int crx,cry,crw,crh;

    AffineTransform current2=null;
    Shape currentClip=null;

    protected Border myBorder;

    public SingleDisplay(int pageNumber,int pageCount,DynamicVectorRenderer currentDisplay) {

        this.pageNumber=pageNumber;
        this.pageCount=pageCount;
        this.currentDisplay=currentDisplay;

    }

    public SingleDisplay(PdfDecoder pdf) {
        this.pdf=pdf;

    }

    /**
     * used by Storypad to display split spreads not aprt of API
     */
    public void clearAdditionalPages() {

        offsets.clear();
        newPages.clear();
        currentXOffset=0;
        additionalPageCount=0;
    }

    /**
     * used by Storypad to display split spreads not aprt of API
     */
    public void addAdditionalPage(DynamicVectorRenderer dynamicRenderer, int pageWidth, int origPageWidth) {

        //store
        offsets.addElement(currentXOffset+origPageWidth);
        newPages.put(new Integer(currentXOffset+origPageWidth),dynamicRenderer);
        additionalPageCount++;

        //work out new offset
        currentXOffset=currentXOffset+pageWidth;

        //force redraw
        this.oldScaling=-oldScaling;
        this.refreshDisplay();

    }


    //<start-13>
    VolatileImage backBuffer = null;

    protected void createBackBuffer() {

        if(debugLayout)
            System.out.println("createBackBuffer");

        if (backBuffer != null) {
            backBuffer.flush();
            backBuffer = null;
        }

        int width=0,height=0;

        if(displayView==SINGLE_PAGE){
            if((displayRotation==90)|(displayRotation==270)){
                width=(int) ((volatileHeight+currentXOffset)*scaling);
                height=(int) (volatileWidth*scaling);
            }else{
                width=(int) ((volatileWidth+currentXOffset)*scaling);
                height=(int) (volatileHeight*scaling);
            }
        }else if(currentOffset!=null){



        }

        try{
			//avoid huge pages as VERY slow
            if(height>15000){
                volatileHeight=0;
                height=0;
                overRideAcceleration=true;
            }
            if((width>0)&&(height>0)){
                backBuffer = pdf.createVolatileImage(width,height);
                oldVolatileWidth=volatileWidth;
                oldVolatileHeight=volatileHeight;
                
                Graphics2D gg=(Graphics2D) backBuffer.getGraphics();
                gg.setPaint(pdf.getBackground());
                gg.fillRect(0,0,width,height);
            }

		}catch(Error e){ //switch off if not enough memory
            overRideAcceleration=true;
            backBuffer=null;
        }catch(Exception e){
            e.printStackTrace();
            //System.exit(1);
        }
    }
    //<end-13>


    public boolean isAccelerated() {

    	//<start-13>
        if((!useAcceleration)||(overRideAcceleration)){
            return false;
        }else
            return true;
        /**
        //<end-13>
        return false;
        /**/
    }


    public void resetCachedValues() {

        //rest page views
        lastPageChecked = -1;
        lastState = -1;
    }

    public void stopGeneratingPage(){

    	//request any processes die
    	isGeneratingOtherPages=false;

        //wait to die
		while (running) {
			// System.out.println("Waiting to die");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// should never be called
				e.printStackTrace();
			}
		}

    }

    public void disableScreen(){
        isInitialised=false;
    }


    boolean testAcceleratedRendering() {


		//<start-13>
        boolean canDrawAccelerated=false;

        //force redraw if page rescaled
        if((oldScaling!=scaling)||(oldRotation!=displayRotation)||
        		(oldVolatileWidth!=volatileWidth)||(oldVolatileHeight!=volatileHeight)){
            backBuffer=null;
			overRideAcceleration=false;

		}

		if(DynamicVectorRenderer.debugPaint)
            System.err.println("acceleration called "+backBuffer);

        if (!overRideAcceleration &&(backBuffer == null)){//||(screenNeedsRedrawing)){

			createBackBuffer();
            accleratedPagesAlreadyDrawn.clear();

		}


        if (backBuffer != null){
            do {
                // First, we validate the back buffer
                int valCode = VolatileImage.IMAGE_INCOMPATIBLE;
                if(backBuffer!=null)
                    valCode = backBuffer.validate(pdf.getGraphicsConfiguration());

                if (valCode == VolatileImage.IMAGE_RESTORED) {
                    // This case is just here for illustration
                    // purposes.  Since we are
                    // recreating the contents of the back buffer
                    // every time through this loop, we actually
                    // do not need to do anything here to recreate
                    // the contents.  If our VImage was an image that
                    // we were going to be copying _from_, then we
                    // would need to restore the contents at this point
                } else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
                    if(!overRideAcceleration)
                    createBackBuffer();

				}

                // Now we've handled validation, get on with the rendering
                if((backBuffer!=null)){

                    canDrawAccelerated=true;


                }

                // Now we are done; or are we?  Check contentsLost() and loop as necessary
            } while ((backBuffer==null)||(backBuffer.contentsLost()));
        }
        return canDrawAccelerated;

        /**
        //<end-13>
        return false;
        /**/
    }

    public Dimension getPageSize(int displayView) {

        Dimension pageSize=null;

        //height for facing pages
        int biggestFacingHeight=0;

        if((displayView==FACING)&&(pageW!=null)){
            //get 2 facing page numbers
            int p1=pageNumber;
            if((p1 & 1)==1)
                p1--;
            int p2=p1+1;

            if((displayRotation==90)|(displayRotation==270)){
                biggestFacingHeight=pageW[p1];
                if(p2<pageW.length && biggestFacingHeight<pageW[p2])
                    biggestFacingHeight=pageW[p2];
            }else{
                biggestFacingHeight=pageH[p1];
                if(p2<pageH.length && biggestFacingHeight<pageH[p2])
                    biggestFacingHeight=pageH[p2];
            }
        }

        int gaps=currentOffset.gaps;
        int doubleGaps=currentOffset.doubleGaps;

        switch(displayView){

            case FACING:

                if((displayRotation==90)|(displayRotation==270))
                    pageSize= new Dimension((int)((currentOffset.doublePageHeight*scaling)+insetW+insetW),(int)((biggestFacingHeight*scaling)+insetH+insetH));
                else
                    pageSize= new Dimension((int)((currentOffset.doublePageWidth*scaling)+insetW+insetW),(int)((biggestFacingHeight*scaling)+insetH+insetH));

                break;


            case CONTINUOUS:

                if((displayRotation==90)|(displayRotation==270))
                    pageSize= new Dimension((int)((currentOffset.biggestHeight*scaling)+insetW+insetW),(int)((currentOffset.totalSingleWidth*scaling)+gaps+insetH+insetH));
                else
                    pageSize= new Dimension((int)((currentOffset.biggestWidth*scaling)+insetW+insetW),(int)((currentOffset.totalSingleHeight*scaling)+gaps+insetH+insetH));

                break;


            case CONTINUOUS_FACING:

                if((displayRotation==90)|(displayRotation==270)){
					if(pageCount == 2)
						pageSize= new Dimension((int)((currentOffset.doublePageHeight*scaling)+insetW+insetW),(int)((currentOffset.biggestWidth*scaling)+gaps+insetH+insetH));
					else
						pageSize= new Dimension((int)((currentOffset.doublePageHeight*scaling)+insetW+insetW),(int)((currentOffset.totalDoubleWidth*scaling)+doubleGaps+insetH+insetH));
				}else{
					if(pageCount == 2)
						pageSize= new Dimension((int)((currentOffset.doublePageWidth*scaling)+insetW+insetW),(int)((currentOffset.biggestHeight*scaling)+gaps+insetH+insetH));
					else
						pageSize= new Dimension((int)((currentOffset.doublePageWidth*scaling)+insetW+insetW),(int)((currentOffset.totalDoubleHeight*scaling)+doubleGaps+insetH+insetH));
				}
                break;

        }

        if(debugLayout)
            System.out.println("pageSize"+pageSize);

        return pageSize;
    }




	public void drawBorder() {

        if((crw >0)&&(crh >0)&&(myBorder!=null))
            myBorder.paintBorder(pdf,g2,crx -1, cry -1, crw +2, crh +2);

    }

    void setDisplacementOnG2(Graphics2D gBB) { //multi pages assumes all page 0 and rotates for each
        //translate the graphic to 0,0 on volatileImage java co-ords
        float cX=crx /scaling,cY=cry /scaling;
        if(displayRotation==0 || this.displayView!=Display.SINGLE_PAGE)
            gBB.translate(-cX,cY);
        else if(displayRotation==90)
            gBB.translate(-cY,-cX);
        else if(displayRotation==180)
            gBB.translate(cX,-cY);
        else if(displayRotation==270)
            gBB.translate(cY,cX);

    }


	public void refreshDisplay(){

        screenNeedsRedrawing = true;
        // reset over-ride which may have been enabled

        accleratedPagesAlreadyDrawn.clear();

        overRideAcceleration = false;
    }

    public void flushPageCaches() {
        currentPageViews.clear();
        cachedPageViews.clear();
    }

    public void init(float scaling, int pageCount, int displayRotation, int pageNumber,
    		DynamicVectorRenderer currentDisplay, boolean isInit, PdfPageData pageData,int insetW, int insetH){

        //if(debugLayout)
        //    System.out.println("init");

		this.currentDisplay=currentDisplay;
        this.scaling=scaling;
        this.pageCount=pageCount;
        this.displayRotation=displayRotation;
        this.pageNumber=pageNumber;
        this.pageData=pageData;

        this.insetW=insetW;
        this.insetH=insetH;

        //reset over-ride which may have been enabled
        volatileWidth = pageData.getCropBoxWidth(this.pageNumber);
        volatileHeight = pageData.getCropBoxHeight(this.pageNumber);

        if(isInit){
            lastPageChecked=-1;

            setPageOffsets(pageCount,this.pageNumber);
            isInitialised=true;
        }
    }

    /**
     * workout offsets so we  can draw pages
     * */
    public void setPageOffsets(int pageCount,int pageNumber) {
    	
    	if(debugLayout)
    		System.out.println("setPageOffsets "+pageNumber+" "+pageCount+" displayView="+displayView+" scaling="+scaling);
    	
    	if(displayView==1)
    		return ;
    	
    	lastPageChecked=pageNumber;
    	lastState=displayView;
    	
    	xReached=new int[pageCount+1];
    	yReached=new int[pageCount+1];
    	pageW=new int[pageCount+1];
    	pageH=new int[pageCount+1];
    	
    	int displayRotation = 0;
    	
    	int diff=-1;
    	boolean hasDoubleRotation=false,hasLeftRotation=false,hasRightRotation=false;
    	
    	isRotated=new boolean[pageCount+1];
    	int gap=(int) (currentOffset.pageGap);//set.pageGap*scaling);
    	
    	/**work out page sizes - need to do it first as we can look ahead*/
    	for(int i=1;i<pageCount+1;i++){
    		
    		/**
    		 * get unrotated page sizes
    		 */
    		pageW[i]=(int) (pageData.getCropBoxWidth(i)*scaling);
    		pageH[i]=(int) ((pageData.getCropBoxHeight(i)*scaling));
    		
    		displayRotation=pageData.getRotation(i)+this.displayRotation;
    		if(displayRotation>=360)
    			displayRotation=displayRotation-360;
    		
    		//swap if this page rotated and flag
    		if((displayRotation==90|| displayRotation==270)){
    			int tmp=pageW[i];
    			pageW[i]=pageH[i];
    			pageH[i]=tmp;
    			isRotated[i]=true; //flag page as rotated
    		}
    		
    	}
    	
    	//loop through all pages and work out positions
    	for(int i=1;i<pageCount+1;i++){  
    		
    		int Ydiff = 0;
    		
    		//System.out.println("pageCount =="+i+"< xReached =="+xReached[i-1]+"< yReached =="+yReached[i-1]+"<");
    		
    		if((pageCount==2)&&(displayView==FACING || displayView==CONTINUOUS_FACING)){ //special case
    			
    			if(i==1){
    				xReached[1]=0;
    				yReached[1]=0;
    			}else{
    				xReached[2]=xReached[1]+pageW[1]+gap;
    				yReached[2]=0;
    				//yReached[2]=pageH[1];
    			}
    			
    		}else if(i==1){  //first page is special case
    			
    			if(displayView==CONTINUOUS){
    				xReached[1]=0;
    				yReached[1]=0;
    			}else if((displayView==FACING)||(displayView==CONTINUOUS_FACING)){
    				if(isRotated[i] == true){
    					xReached[1]=pageW[2]+gap;
    				}else{
    					xReached[1]=pageW[2]+gap;
    					yReached[1]=0;
    				}
    			}
    			
    		}else if(displayView==CONTINUOUS){
    			
    			yReached[i]=yReached[i-1]+pageH[i-1]+gap;
    			xReached[i]=0;
    			
    		}else if((displayView==FACING)||(displayView==CONTINUOUS_FACING)){
    			
    			if((i & 1)==0){ //even pages on left
    				xReached[i]=0;
    				
    				if((displayView==CONTINUOUS_FACING)){
    					
    					if(i>2 && isRotated[i-1] && isRotated[i-2]) //one page rotated to get bigger height
    						hasDoubleRotation=true;
    					
    					if(i<pageCount && isRotated[i]!=isRotated[i+1]){ //one page rotated on left so indent on width
    						//choose max height if 2 differ
    						int newDiff=(pageW[i]-pageH[i]);
    						if(newDiff<0)
    							newDiff=-newDiff;
    						
    						if(newDiff>diff)
    							diff=newDiff;
    						
    					}
    					
    					if(i==2){ //second page is special case
    						yReached[i]=pageH[1]+gap;
    					}else if(i>2 && isRotated[i-1] !=isRotated[i-2]){ //one page rotated to get bigger height
    						//choose max height if 2 differ
    						if(displayRotation == 90 || displayRotation == 270){
    							int h=pageH[i-1];
    							if(h<pageH[i-2])
    								h=pageH[i-2];
    							
    							yReached[i]=(yReached[i-2]+h+gap);
    						}else{
    							int h=pageH[i-1];
    							if(h<pageH[i-2])
    								h=pageH[i-2];
    							yReached[i]=yReached[i-2]+h+gap;
    						}
    					}else{ //both same so use either width or height
    						yReached[i]=yReached[i-1]+pageH[i-1]+gap;
    					}
    					
    				}else
    					if(displayView==FACING){
    						if(isRotated[i] && isRotated.length>(i+1) && !isRotated[i+1]){
    							diff = ((pageH[i+1]-pageH[i])/2);
    							yReached[i]=+diff;
    						}
    						if(!isRotated[i] && isRotated.length>(i+1) && isRotated[i+1]){
    							diff = ((pageH[i]-pageH[i+1])/2);
    							yReached[i+1]=+diff;
    						}
    					}else
    						yReached[i]=0;
    				
    				if(displayView==CONTINUOUS_FACING){
    					//make sure centred against facing page
    					if(i<pageCount && !isRotated[i+1] && isRotated[i]){ //one page rotated to get bigger height
    						hasLeftRotation=true;
    						Ydiff = ((pageW[i]-pageH[i])/2);
    						yReached[i]=yReached[i]+Ydiff;
    					}
    					
    					if(isRotated[i-2] && !isRotated[i-1]){
    						//Ydiff = ((pageH[i]-pageW[i])/2);
    						yReached[i]=yReached[i-1]+pageH[i-1]+gap;
    					}
    				}
    				
    			}else{ //odd pages on right
    				xReached[i]=xReached[i-1]+pageW[i-1]+gap;
    				
    				if(displayView==CONTINUOUS_FACING){
    					if(isRotated[i-1] && pageNumber > 2){
    						yReached[i]=yReached[i-2]+pageH[i-2]+gap;
    					}else{
    						yReached[i]=yReached[i-1];
    					}
    				}else
    					if(displayView==FACING){
    						if(i<pageCount && !isRotated[i] && isRotated[i+1]){
    							diff = ((pageH[i]-pageH[i+1])/2);
    							yReached[i+1]=+diff;
    						}
    					}else
    						yReached[i]=0;
    				
    				if(displayView==CONTINUOUS_FACING){
//  					make sure centred against facing page
    					if(isRotated[i] && i>1 && !isRotated[i-1]){ //one page rotated to get bigger height
    						hasRightRotation=true;
    						Ydiff=(pageH[i-1]-pageH[i])/2;
    						yReached[i]=yReached[i]+Ydiff;
    					}
    					
    					if(isRotated[i-1] && !isRotated[i]){
    						yReached[i]=yReached[i-2]+pageH[i-2]+gap;
    					}
    					
    					if(i>3 && isRotated[i-2] && !isRotated[i-3]){
    						Ydiff = (pageH[i-3]-pageH[i-2])/2;
    						yReached[i] = yReached[i-2]+pageH[i-2]+gap+Ydiff;
    					}
    					
    					//cropping handling @kieran
    					if(displayView == CONTINUOUS_FACING){
    					if(displayRotation == 0 || displayRotation == 180){
    						if((i&1)==1 && i>1){
    							if(pageH[i]<pageH[i-1]){
    								diff = (pageH[i-1]-pageH[i])/2;
    								//System.out.println(pageW[i]+" "+pageW[i-1]+" page =="+i+" diff =="+diff+"<");
    								yReached[i] = yReached[i-1]+diff;
    							}
    						
    							if(pageH[i]>pageH[i-1]){
    								diff = (pageH[i]-pageH[i-1])/2;
    								//System.out.println(pageW[i]+" "+pageW[i-1]+" page =="+i+" diff =="+diff+"<");
    								yReached[i-1] = yReached[i]+diff;
    							}
    						}
    					}
    					if(displayRotation == 270 || displayRotation == 90){
    						if((i&1)==1 && i>1){
    							if(pageH[i]<pageH[i-1]){
    								diff = (pageH[i-1]-pageH[i])/2;
    								//System.out.println(pageW[i]+" "+pageW[i-1]+" page =="+i+" diff =="+diff+"<");
    								yReached[i] = yReached[i-1]+diff;
    							}
    						
    							if(pageH[i]>pageH[i-1]){
    								diff = (pageH[i]-pageH[i-1])/2;
    								//System.out.println(pageW[i]+" "+pageW[i-1]+" page =="+i+" diff =="+diff+"<");
    								yReached[i-1] = yReached[i]+diff;
    							}
    						}
    					}
    				}
    					
    					
    					
    					if(isRotated[i] && i>1 && isRotated[i-1]){
    						hasDoubleRotation=true;
    					}
    				}
    			}
    		}
    	}
    	int maxWidth=0;
    	boolean allRotated = true;//check for all pages having the same rotation
    	for(int i=2;i<pageCount+1;i++){
    		
    		if(pageW[i]>maxWidth)
    			maxWidth = pageW[i];
    		
    		if(isRotated[i] != isRotated[i-1])
    			allRotated = false;
    		
    		if(pageW[i]!=pageW[i-1])
    			allRotated = false;
    		
    	}
    	
    	for(int i=1;i<pageCount+1;i++){ //set pages to have the correct allignment
    		if(displayView==CONTINUOUS && allRotated){
    			if(displayRotation == 270){
    				diff = (pageW[i]-pageH[i])/2;
    				xReached[i]=-diff;
    			}
    			if(displayRotation == 90){
    				diff = (pageW[i]-pageH[i])/2;
    				xReached[i]=+diff;
    			}
    		}
    	}
    	
    	//add in offset for 1 rotated page on left or right
    	if(this.displayView==Display.CONTINUOUS_FACING){
    		//work out page sizes - need to do it second
    		if(!hasDoubleRotation && hasLeftRotation!=hasRightRotation){
    			if(diff>0 && hasLeftRotation){ //no movement required on right rotation
    				for(int i=1;i<pageCount+1;i++){
    					if(i==1) //page 1 special case
    						if(hasLeftRotation && isRotated[1]){
    							xReached[1]=pageW[1]+gap;
    						}else{
    							xReached[1]=pageH[1]+gap;
    							yReached[1]=0;
    						}
    					if(((i & 1)==0)&& !isRotated[i]){//even left pages
    						xReached[i]=xReached[i]+diff;
    						xReached[i+1]=xReached[i+1]+diff;
    					}
    				}
    			}
    		}else if(pageCount>2){ //ensure all centered if page sizes vary
    			//initial value for page 1
    			/*int scaledWidth=0;
    			 if(displayRotation==0 || displayRotation==180)
    			 scaledWidth=(int)(scaling*currentOffset.doublePageWidth);
    			 else
    			 scaledWidth=(int)(scaling*currentOffset.doublePageHeight);
    			 diff = (pageH[1] - pageW[1])/2;*/
    			
    			for(int i=1;i<pageCount+1;i++){
    				//begining of cropped and uncropped page handling
    				//handle files with rotation that is not picked up
    				if(displayView == CONTINUOUS_FACING && !allRotated && !hasLeftRotation && !hasRightRotation && !hasDoubleRotation){
    					//if(pageW[i]<maxWidth)
    					//	xReached[i] = xReached[i]+(maxWidth-pageW[i]);
    						
    					if(i<pageW.length-1 && pageW[i+1]<maxWidth)
    						xReached[i+1] = xReached[i+1]+(maxWidth-pageW[i+1]);
    				}
    				
    				
    				diff = (pageW[i]-pageH[i]);
    				if(i==2){ //page 1 special case
    					xReached[i]=xReached[i];
    					xReached[1]=xReached[2]+pageW[2]+gap;
    				}
    				else //ensure all left pages are alligned and right pages are moved accross
    					if((i & 1)==0 && !isRotated[i] && isRotated[i+1]){
    						xReached[i]=xReached[i]-diff;
    						xReached[i+1] = xReached[i] + pageW[i]+gap;
    					}else{
    						diff = (pageW[i]-pageH[i]);
    						xReached[i]=xReached[i];
    						}
    				
    				
    				//Handle ang PDF that has both left and right rotation but not double rotation
    				if(displayRotation==0 || displayRotation==180){	//handled correctly in the code above for 90 and 270
    					if(i<pageCount && hasLeftRotation && hasRightRotation && !((!isRotated[i] && isRotated[i+1])||(isRotated[i] && !isRotated[i+1]))){
    						if(!isRotated[1] && pageH[1] > pageW[1])
    							diff = (pageH[1]-pageW[1]); 
    						if(!isRotated[1] && pageH[1] < pageW[1])
    							diff = (pageW[1]-pageH[1]);
    						xReached[1]=xReached[2]+pageW[2]+gap;
    						xReached[i]=xReached[i]+diff;
    						if(i==pageCount-1){ //Handle last page
    							xReached[pageCount]=xReached[pageCount]+diff;
    						}
    					}else{//ignore page after a rotation as it is already correct
    						i++;
    					}
    				}
    				if(displayView == CONTINUOUS_FACING){
    					xReached[1] = xReached[2]+pageW[2]+gap;
    					if(i<xReached.length-1 && (i&1)==0){
    						xReached[i+1] = xReached[i]+pageW[i]+gap;
    					}
    				}
    				
    			}
    			
    		}
    	}

        //<start-adobe>    
        pdf.setMultiPageOffsets(xReached,yReached);
        //<end-adobe>

    }

    public void decodeOtherPages(int pageNumber, int pageCount) {
    }


    public void completeForm(Graphics2D g2) {
        g2.drawLine(crx, cry, crx +crw,cry +crh);
        g2.drawLine(crx, crh +cry, crw +crx,cry);
    }

    public void resetToDefaultClip() {

        if(current2!=null)
        g2.setTransform(current2);

        //reset transform and clip
        if(currentClip!=null)
        g2.setClip(currentClip);

    }


    public void initRenderer(Rectangle[] areas, Graphics2D g2,Border myBorder,int indent){

        //if(debugLayout)
        //    System.out.println("initRenderer");

        this.rawAf=g2.getTransform();
        this.rawClip=g2.getClip();

        this.areas=areas;
        this.g2=g2;

        this.myBorder=myBorder;

        this.indent=indent;
        pagesDrawn.clear();

        setPageSize(pageNumber, scaling);

    }

    void setPageSize(int pageNumber, float scaling) {

        /**
         *handle clip - crop box values
         */
        topW=pageData.getCropBoxWidth(pageNumber);
        topH=pageData.getCropBoxHeight(pageNumber);
        double mediaH=pageData.getMediaBoxHeight(pageNumber)*scaling;

        cropX=pageData.getCropBoxX(pageNumber);
        cropY=pageData.getCropBoxY(pageNumber);
        cropW=topW*scaling;
        cropH=topH*scaling;

        /**
         * actual clip values - for flipped page
         */
		if(displayView==Display.SINGLE_PAGE){
			crx =(int)(insetW+cropX*scaling);
			cry =(int)(insetH-cropY*scaling);
		}else{
			crx =insetW;
			cry =insetH;
		}
		//cry =(int)(insetH+(mediaH-cropH)-cropY);

        //amount needed to move cropped page into correct position
        //int offsetX=(int) (mediaW-cropW);
        int offsetY=(int) (mediaH-cropH);

        if((displayRotation==90||(displayRotation==270))){
            crw =(int)(cropH);
            crh =(int)(cropW);
            
			int tmp = crx;
			crx = cry;
			cry = tmp;

            crx=crx+offsetY;
        }else{
            crw =(int)(cropW);
            crh =(int)(cropH);

           cry=cry+offsetY;
        }
         /**/
        g2.translate(insetW-crx,insetH-cry);

        //save any transform and stroke
        current2 = g2.getTransform();

        //save global clip and set to our values
        currentClip =g2.getClip();

        //if(!showCrop)
        g2.clip(new Rectangle(crx,cry, (int) (crw+( (insetW*additionalPageCount)+currentXOffset*scaling)),crh));

    }

    int topW,topH;
    double cropX,cropY,cropW,cropH;

	boolean thumbnailsRunning;

    //<start-adobe>
    //<start-thin>
	protected GUIThumbnailPanel thumbnails=null;
	//<end-thin>
    //<end-adobe>




    public Rectangle drawPage(AffineTransform viewScaling, AffineTransform displayScaling,int pageUsedForTransform) {

        Rectangle actualBox=null;

        /**add in other elements*/
        if((displayScaling!=null)&&(currentDisplay!=null)){

            /**
             * pass in values as needed for patterns
             */
            currentDisplay.setScalingValues(cropX,cropH+cropY,scaling);//@kieran

            g2.transform(displayScaling);

            if(DynamicVectorRenderer.debugPaint)
                System.err.println("accelerate or redraw");

            boolean canDrawAccelerated=false;
            //use hardware acceleration - it sucks on large image so
            //we check scaling as well...
            if((useAcceleration)&&(!overRideAcceleration)&&(scaling<2))
                canDrawAccelerated= testAcceleratedRendering();

            if(canDrawAccelerated){
            	//<start-13>
                // rendering to the back buffer:
                Graphics2D gBB = (Graphics2D)backBuffer.getGraphics();

                if(screenNeedsRedrawing){
                    //fill background white to prevent memory overRun from previous graphics memory
                    gBB.setColor(currentDisplay.getBackgroundColor());
                    gBB.fill(new Rectangle(0,0,backBuffer.getWidth(),backBuffer.getHeight()));


                    gBB.setTransform(displayScaling);
                    setDisplacementOnG2(gBB);

					actualBox =currentDisplay.paint(gBB,null,viewScaling,userAnnot,false);

                    drawSpreadPages(gBB,viewScaling,userAnnot,false);

                    screenNeedsRedrawing =false;
                }

                gBB.dispose();

                if(backBuffer !=null){

                    //draw the buffer
                    AffineTransform affBefore=g2.getTransform();

                    g2.setTransform(rawAf);
                    g2.drawImage(backBuffer, insetW,insetH, pdf);
                    g2.setTransform(affBefore);

                    /**
                     * 	draw other page outlines and any decoded pages so visible
                     */
                    actualBox =currentDisplay.paint(g2,areas,viewScaling,userAnnot,true);

                    drawSpreadPages(g2,viewScaling,userAnnot,true);
                }
                //<end-13>
            }else{
                if(DynamicVectorRenderer.debugPaint)
                    System.err.println("standard paint called ");

                /**
                 * 	draw other page outlines and any decoded pages so visible
                 */
                //same rectangle works for any rotation so removed the rotation check
//                System.out.println("a");
                g2.clip(new Rectangle((int)cropX,(int)cropY,topW,topH));
                
                actualBox =currentDisplay.paint(g2,areas,viewScaling,userAnnot,false);

                drawSpreadPages(g2,viewScaling,userAnnot,false);
            }

            //track scaling so we can update dependent values
            oldScaling=scaling;
            oldRotation=displayRotation;


        }

        return actualBox;
    }

    private void drawSpreadPages(Graphics2D g2,AffineTransform viewScaling,Rectangle userAnnot,boolean flag){

        if(currentXOffset>0){
            int[] currentOffset=offsets.get();

            int count=currentOffset.length;

            int currentValue=0;
            for(int i=0;i<count;i++){

                currentValue=currentOffset[i];

                if(currentValue>0){
                    DynamicVectorRenderer renderer= (DynamicVectorRenderer) newPages.get(new Integer(currentValue));

                    g2.setClip(null);
                    g2.translate(currentValue,0);
                    if(renderer!=null)
                    renderer.paint(g2,areas,viewScaling,userAnnot,flag);

                    g2.translate(-currentValue,0);
                }
            }
        }
    }

    public void setup(boolean useAcceleration,PageOffsets currentOffset,PdfDecoder pdf){

        this.useAcceleration=useAcceleration;
        this.currentOffset=currentOffset;
        this.pdf=pdf;

		overRideAcceleration=false;

    }

    public int getXCordForPage(int page){
    	return xReached[page]+insetW;
    }

    public int getYCordForPage(int page){
        return yReached[page]+insetH;
    }

    public int getStartPage() {
        return this.startViewPage;
    }

    public int getEndPage() {
        return this.endViewPage;
    }

    /**show if decoding pages*/
	public boolean isDecoding() {
		return running;
	}

	public void setThumbnailsDrawing(boolean b) {
		thumbnailsRunning=b;

	}

    //<start-adobe>
    //<start-thin>
	public void setThumbnailPanel(GUIThumbnailPanel thumbnails) {
		this.thumbnails=thumbnails;

	}
	//<end-thin>
    //<end-adobe>

    public void setScaling(float scaling) {
		this.scaling=scaling;

}

}
