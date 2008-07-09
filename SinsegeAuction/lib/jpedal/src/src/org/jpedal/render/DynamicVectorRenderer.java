/*
 * Created on 02-Dec-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.jpedal.render;

import java.awt.*;

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


//<start-jfl>
import org.jpedal.PdfDecoder;
import org.jpedal.Display;
//<end-jfl>
import org.jpedal.color.ColorSpaces;
import org.jpedal.color.PdfColor;
import org.jpedal.color.PdfPaint;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.*;

import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.JAIHelper;
import org.jpedal.objects.GraphicsState;
import org.jpedal.utils.Matrix;
import org.jpedal.utils.Messages;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.repositories.Vector_Double;
import org.jpedal.utils.repositories.Vector_Float;
import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.utils.repositories.Vector_Object;
import org.jpedal.utils.repositories.Vector_Rectangle;
import org.jpedal.utils.repositories.Vector_Shape;

import org.jpedal.parser.FontFactory;
import org.jpedal.fonts.glyph.GlyphFactory;
import org.jpedal.fonts.glyph.MarkerGlyph;
import org.jpedal.fonts.glyph.PdfGlyph;
import org.jpedal.fonts.glyph.PdfGlyphs;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.glyph.UnrendererGlyph;

/**
 * @author markee
 */
public final class DynamicVectorRenderer {

    private int pageNumber=0;
    
    //used by type3 fonts as identifier
    private String rawKey=null;

    /**global colours if set*/
    private PdfPaint fillCol=null,strokeCol = null;

    /**used to enusre we get message once if problem*/
    private static boolean flagException=false;

    /**default array size*/
    int defaultSize=5000;

    //used to track end of PDF page in display
    int endItem=-1;

    /**flag to enable debugging of painting*/
    public static boolean debugPaint=false;

    /**use hi res images to produce better quality display*/
    private boolean useHiResImageForDisplay=false;

    /**hint for conversion ops*/
    private static RenderingHints hints = null;

    private ObjectStore objectStoreRef;

    private boolean isPrinting;

    private static  Map cachedWidths=new HashMap();

    private static Map cachedHeights=new HashMap();

    private Map fonts=new HashMap();

    private Map fontsUsed=new HashMap();
    
    protected GlyphFactory factory=null;

	private PdfGlyphs glyphs;

    //<start-jfl>
    private int displayMode=Display.SINGLE_PAGE;

    //<end-jfl>

    private boolean isType3Font=false;
    static private int glyphT3Count;
    private Map imageID=new HashMap();

//	static {
//		hints =
//			new RenderingHints(
//					RenderingHints.KEY_RENDERING,
//					RenderingHints.VALUE_RENDER_QUALITY);
//		hints.put(
//				RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
//	}

    static {
        hints =
            new RenderingHints(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

       //hints.put(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);


        hints.put(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
                //RenderingHints.VALUE_ANTIALIAS_OFF);

    }

    /**create instance and set flag to show if we draw white background*/
    public DynamicVectorRenderer(int pageNumber,boolean addBackground,int defaultSize,ObjectStore newObjectRef) {

        this.pageNumber=pageNumber;
        this.objectStoreRef = newObjectRef;
        this.addBackground=addBackground;

        setupArrays(defaultSize);

    }



    /**
     * @param defaultSize
     */
    private void setupArrays(int defaultSize) {
        x_coord=new float[defaultSize];
        y_coord=new float[defaultSize];
        text_color=new Vector_Object(defaultSize);
        textFillType=new Vector_Int(defaultSize);
        stroke_color=new Vector_Object(defaultSize);
        fill_color=new Vector_Object(defaultSize);
        stroke=new Vector_Object(defaultSize);
        pageObjects=new Vector_Object(defaultSize);
        javaObjects=new Vector_Object(defaultSize);
        shapeType=new Vector_Int(defaultSize);
        areas=new Vector_Rectangle(defaultSize);
        //TRvalues=new Vector_Int(defaultSize);
        af1=new Vector_Double(defaultSize);
        af2=new Vector_Double(defaultSize);
        af3=new Vector_Double(defaultSize);
        af4=new Vector_Double(defaultSize);
        clips=new Vector_Shape(defaultSize);
        objectType=new Vector_Int(defaultSize);
        //opacity=new Vector_Float(defaultSize);
    }

    public DynamicVectorRenderer(int pageNumber,ObjectStore newObjectRef,boolean isPrinting) {

        this.pageNumber=pageNumber;
        this.objectStoreRef = newObjectRef;
        this.isPrinting=isPrinting;

        setupArrays(defaultSize);

    }

    //<start-jfl>
    public void setDisplayView(int displayMode){
		this.displayMode=displayMode;
	}
    //<end-jfl>

    /**real size of pdf*/
    private int w=0,h=0;

    /**background color*/
    private Color backgroundColor=Color.white;

    /**store x*/
    private float[] x_coord;

    /**store y*/
    private float[] y_coord;

    /**cache for images*/
    private Map largeImages=new WeakHashMap();
    
    private Vector_Object text_color;
    private Vector_Object stroke_color;
    private Vector_Object fill_color;

    private Vector_Object stroke;

    /**initial Q & D object to hold data*/
    private Vector_Object pageObjects;

    private Vector_Int shapeType;

    /**holds rectangular outline to test in redraw*/
    private Vector_Rectangle areas;

    private Vector_Double af1;
    private Vector_Double af2;
    private Vector_Double af3;
    private Vector_Double af4;

    /**TR for text*/
    private Vector_Int TRvalues;
    
    /**font sizes for text*/
    private Vector_Int fs;

    /**line widths if not 0*/
    private Vector_Int lw;

    /**holds rectangular outline to test in redraw*/
    private Vector_Shape clips;

    /**holds object type*/
    private Vector_Int objectType;

    /**holds object type*/
    private Vector_Object javaObjects;

    /**holds fill type*/
    private Vector_Int textFillType;

    /**holds object type*/
    private Vector_Float opacity;


    final private static int TEXT=1;
    final private static int SHAPE=2;
    final private static int IMAGE=3;
    public final static int TRUETYPE=4;
    public final static int TYPE1C=5;
    public final static int TYPE3=6;
    public final static int CLIP=7;
    public final static int COLOR=8;
    public final static int AF=9;
    public final static int TEXTCOLOR=10;
    public final static int FILLCOLOR=11;
    public final static int STROKECOLOR=12;
    public final static int STROKE=14;
    public final static int TR=15;
    public final static int STRING=16;
    public final static int STROKEOPACITY=17;
    public final static int FILLOPACITY=18;

    public final static int STROKEDSHAPE=19;
    public final static int FILLEDSHAPE=20;
    
    public final static int FONTSIZE=21;
    public final static int LINEWIDTH=22;
    
    public final static int MARKER=200;
    public final static boolean debugStreams=false;
    
    /**set flag to show if we add a background*/
    private boolean addBackground=true;

    /**flag to stop race condition on redraw if flushed*/
    private boolean pageDrawing=false;

    /**current item added to queue*/
    private int currentItem=0;

    //used to track col changes
    private int lastFillTextCol,lastFillCol,lastStrokeCol;

    /**used to track strokes*/
    private Stroke lastStroke=null;

    //trakc affine transform changes
    private double[] lastAf=new double[4];

    /**used to minimise TR and font changes by ignoring duplicates*/
    private int lastTR=2,lastFS=-1,lastLW=-1;

    /**ensure colors reset if text*/
    private boolean resetTextColors=true;

    private boolean fillSet=false,strokeSet=false;

	
    public final void renderText(int type,Area transformedGlyph2,Graphics2D g2,
								 boolean isHighlighted,PdfPaint strokePaint,
								 PdfPaint textFillCol,float strokeOpacity,float fillOpacity,Rectangle currentArea){

        //add any highlight
        if((isHighlighted)&&(strokePaint instanceof Color)){

			//set the highlight
            Color currentCol=(Color) strokePaint;
            if(currentCol==null)
                currentCol=(Color) textFillCol;

			Color altCol=setHighlightedColor( currentCol,g2);

			g2.fill(currentArea);
			g2.setPaint(altCol);
		}else{

			//type of draw operation to use
			Composite comp=g2.getComposite();

            if((type & GraphicsState.FILL)==GraphicsState.FILL){

                textFillCol.setScaling(cropX,cropH,scaling);
                g2.setPaint(textFillCol);

				if(isHighlighted){
                    //set the highlight
                    Color currentCol=(Color) textFillCol;

					Color altCol=setHighlightedColor( currentCol,g2);

					g2.fill(currentArea);

					g2.setPaint(altCol);

				}else
                    g2.setPaint(textFillCol);

				if(fillOpacity!=1f)
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,fillOpacity));

				g2.fill(transformedGlyph2);

				//reset opacity
            	g2.setComposite(comp);

            }

            if((type & GraphicsState.STROKE)==GraphicsState.STROKE){
                if(strokePaint!=null)
                strokePaint.setScaling(cropX,cropH,scaling);
                g2.setPaint(strokePaint);

				if(strokeOpacity!=1f)
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,strokeOpacity));

				g2.draw(transformedGlyph2);

				//reset opacity
            	g2.setComposite(comp);
			}
        }

		//g2.setComposite(c);

        //g2.draw(transformedGlyph2.getBounds2D());
    }

	final public void renderEmbeddedText(int text_fill_type,Object rawglyph,int glyphType,
                                         Graphics2D g2,AffineTransform glyphAT,boolean isHighlighted,
                                         PdfPaint strokePaint,PdfPaint fillPaint,
										float strokeOpacity,float fillOpacity,
										Rectangle currentArea,int lineWidth){

        //get glyph to draw
        PdfGlyph glyph=FontFactory.chooseGlpyh(glyphType,rawglyph);

		AffineTransform at=g2.getTransform();

		if((glyph!=null)){
			
			Stroke currentStoke=g2.getStroke();
			
			if(lineWidth!=0)
			g2.setStroke(new BasicStroke(lineWidth));
			
            // set the highlight
            Color currentCol=null;

            if((strokePaint!=null) &&(strokePaint instanceof Color))
                currentCol=(Color) strokePaint;
            else if((strokePaint==null) &&(fillPaint instanceof Color))
                currentCol=(Color) fillPaint;

            if((currentCol!=null)&&(isHighlighted)&&(glyphType!=TYPE3)){

				Color altCol=setHighlightedColor(currentCol, g2);

				g2.fill(currentArea);

				g2.setPaint(altCol);

                //set transform
				g2.transform(glyphAT);

				try{
					glyph.render(text_fill_type,g2,false, scaling);
				}catch(Exception e){
					System.out.println("Exception "+e+" rendering glyph");
					e.printStackTrace();
				}

			}else{

                //set transform
				g2.transform(glyphAT);

				//type of draw operation to use
				Composite comp=g2.getComposite();

                //for a fill
                if ((text_fill_type & GraphicsState.FILL) == GraphicsState.FILL){
                    fillPaint.setScaling(cropX,cropH,scaling);
                    g2.setPaint(fillPaint);


					if(fillOpacity!=1f)
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,fillOpacity));

					glyph.render(GraphicsState.FILL,g2,false, scaling);

					//reset opacity
					g2.setComposite(comp);

                }else

                //and/or do a stroke
                if ((text_fill_type & GraphicsState.STROKE) == GraphicsState.STROKE){
                    if(strokePaint!=null)
                    strokePaint.setScaling(cropX,cropH,scaling);
                    g2.setPaint(strokePaint);

					if(strokeOpacity!=1f)
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,strokeOpacity));

                    try{
                    	glyph.render(GraphicsState.STROKE,g2,false, scaling);
                    }catch(Exception e){
                        System.out.println("Exception "+e+" rendering glyph");
                        e.printStackTrace();
                    }

                    //reset opacity
					g2.setComposite(comp);
                }
            }

            //restore transform
            g2.setTransform(at);
            
            if(lineWidth!=0)
            g2.setStroke(currentStoke);
			

            //g2.draw(transformedGlyph2.getBounds2D());
        }
    }

	private Color setHighlightedColor(Color currentCol, Graphics2D g2) {

		Color altCol=null;

		int r=255-currentCol.getRed();
		if(r<0)
			r=-r;
		int g=255-currentCol.getGreen();
		if(g<0)
			g=-g;
		int b=255-currentCol.getBlue();
		if(b<0)
			b=-b;

		//allow for a grey
		if((r>120)&&(r<136)&&(g>120)&&(g<136)&&(b>120)&(b<136)){
			g2.setPaint(Color.black);
			altCol=Color.white;
		}else{
			g2.setPaint(currentCol);
			altCol=new Color(r,g,b);
		}

		return altCol;
	}

	public final void renderImage(AffineTransform imageAf, BufferedImage image,float alpha,
			GraphicsState currentGraphicsState,Graphics2D g2,float x,float y){

		boolean renderDirect=(currentGraphicsState!=null);

		if(image==null)
			return;

		int w = image.getWidth();
		int h = image.getHeight();


		//plot image (needs to be flipped as well as co-ords upside down)
		//graphics less than 1 get swallowed if flipped
		AffineTransform upside_down = new AffineTransform();

        boolean applyTransform=false;

        /**
		 * setup for printing
		 */
		if((renderDirect)||(useHiResImageForDisplay)){
			if(renderDirect){

				float CTM[][]=currentGraphicsState.CTM;

				//Turn image around if needed
				image = invertImage(CTM, image);

				upside_down=new AffineTransform(CTM[0][0]/w,CTM[0][1]/w,
						CTM[1][0]/h,CTM[1][1]/h,CTM[2][0],CTM[2][1]);
				
			}else{
				upside_down=imageAf;
				
			}

            applyTransform=true;


        }else if (h > 1) {
			//upside_down.scale(1, -1);
			//upside_down.translate(0, -h);

			//<start-jfl>
			float dpi = PdfDecoder.dpi;
			if(dpi != 72){
				upside_down.scale(72 / dpi , 72 / dpi);
				upside_down.translate(0,h * ((dpi / 72) - 1));
				
                applyTransform=true;
            }
			//<end-jfl>

		}

		
		Composite c=g2.getComposite();

		Shape clip=g2.getClip();
		//<start-jfl>
		//bg_holiday and some Times bugs
		//if((PdfDecoder.isRunningOnMac)&&(clip!=null))
		//g2.setClip(null);
		//<end-jfl>

		//if(alpha!=1.0f)
		// g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));

        /**
         * color type3 glyphs if not black
         */
        if(colorsLocked){

            int[] maskCol =new int[4];
            int foreground =this.fillCol.getRGB();
            maskCol[0]=((foreground>>16) & 0xFF);
            maskCol[1]=((foreground>>8) & 0xFF);
            maskCol[2]= ((foreground) & 0xFF);
            maskCol[3]=255;
            
            if(maskCol[0]==0 && maskCol[1]==0 && maskCol[2]==0){
            	//System.out.println("black");
            }else{
	            BufferedImage img=new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
	
	            Raster src=image.getRaster();
	            WritableRaster dest=img.getRaster();
	            for(int yy=0;yy<image.getHeight();yy++){
	                for(int xx=0;xx<image.getWidth();xx++){
	
	                    int[] values=new int[4];
	
	                    //get raw color data
	                    src.getPixel(xx,yy,values);
	
	                    //if not transparent, fill with color
	                    if(values[3]>2)
	                        dest.setPixel(xx,yy,maskCol);
	
	                }
	            }
	            image=img;
            }
        }

        if(renderDirect || useHiResImageForDisplay){
			try{

				//fix for 1.3
				//<start-13>
				/**
				//<end-13>
				image=org.jpedal.io.ColorSpaceConvertor.convertToRGB(image);
				g2.drawImage(image,upside_down,null);
				/**/

				//<start-13>
				g2.drawImage(image,upside_down,null);

				//<end-13>

				//g2.drawImage(image,invert,0,0);//(int)currentGraphicsState.CTM[2][0],(int)currentGraphicsState.CTM[2][1]);
				//possible work around for mac
				//image=invert.filter(image,null);
				//g2.drawImage(image,x,y+h,w,-h,null,null);
			}catch(Exception e){
			}

		}else{

			AffineTransformOp invert =new AffineTransformOp(upside_down,ColorSpaces.hints);

			try{

				//<start-jfl>
				if(this.isType3Font && PdfDecoder.optimiseType3Rendering){
				/*
				//<end-jfl>
				if(this.isType3Font){
					/**/
					
					double[] matrix=new double[6];
					AffineTransform aff2=g2.getTransform();

					g2.translate(x,y);

					g2.getTransform().getMatrix(matrix);

					int ww=image.getWidth();
					int hh=image.getHeight();

					double w1=(ww*(matrix[0]));
					if(w1==0)
						w1=(int)(ww*(matrix[1]));

					if(w1<0)
						w1=-w1;

					if(w1>ww)
						w1=ww;

					double h1=(hh*(matrix[3]));
					if(h1==0)
						h1=(hh*(matrix[2]));
					if(h1<0)
						h1=-h1;

					//rounding
					float r=0;
					h1=h1+r;
					w1=w1+r;

					if(h1>=1 && w1>=1){

						g2.setTransform(new AffineTransform(1f,0,0,-1f,matrix[4],matrix[5]));

						image=invert.filter(image,null);

						double diff=(h1-(int)h1)*scaling;
						double diff2=(matrix[5]-(int)matrix[5]);
						double diff3=(w1-(int)w1)*scaling;
						if(diff3>1){
							int temp = (int)diff3;
							diff3 = diff3-temp;
							w1 = w1+temp;
						}
						if(diff>1){
							int temp = (int)diff;
							diff = diff-temp;
							h1 = h1+temp;
						}

						Image scaledImage= image.getScaledInstance((int)w1, (int)h1,BufferedImage.SCALE_SMOOTH);

						//if(glyphT3Count>12){
						if(diff2<.5d)							
							g2.translate(0,-1);

						//glyphT3Count++;

						//System.out.println(scaling+" "+matrix[4]+" "+matrix[5]+" width="+w1+" height="+h1+" diff="+diff+" "+diff2);

						//}
						g2.drawImage(scaledImage,0,0,null);

					}else{
						image=invert.filter(image,null);
						g2.drawImage(image,0,0,null);
					}


					g2.setTransform(aff2);

				}else{

                    if(applyTransform)
                        image=invert.filter(image,null);

                    g2.translate(x,y);
					g2.drawImage(image,0,0,null);
					g2.translate(-x,-y);
				}
				//Image scaledImage= current_image.getScaledInstance(w,h,BufferedImage.SCALE_SMOOTH);

				// ShowGUIMessage.showGUIMessage("x2",image,"x2");
			}catch(Exception ee){
			}
		}

		//<start-jfl>
		if((PdfDecoder.isRunningOnMac)&&(clip!=null))
			g2.setClip(clip);
		//<end-jfl>

		g2.setComposite(c);

	}

    public final void renderShape(int fillType,PdfPaint strokeCol,PdfPaint fillCol,
                                  Stroke shapeStroke,Shape currentShape,Graphics2D g2,float strokeOpacity,float fillOpacity) {

        Composite comp=g2.getComposite();

        //stroke and fill (do fill first so we don't overwrite Stroke)
        if ((fillType == GraphicsState.FILL) | (fillType == GraphicsState.FILLSTROKE)) {

            fillCol.setScaling(cropX,cropH,scaling);
            g2.setPaint(fillCol);

            if(fillOpacity!=1f)
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,fillOpacity));

            g2.fill(currentShape);

//      	reset opacity
            g2.setComposite(comp);

//      	System.out.println(fillCol);
//      	System.out.println(currentShape.getBounds());
//      	if(currentShape.getBounds().getY()==673)
//      	System.exit(1);

        }

        if ((fillType == GraphicsState.STROKE) | (fillType == GraphicsState.FILLSTROKE)) {

            //set values for drawing the shape
            Stroke currentStroke=g2.getStroke();

            //fix for using large width on point to draw line
            if(currentShape.getBounds2D().getWidth()<1.0f && ((BasicStroke)shapeStroke).getLineWidth()>10)
                g2.setStroke(new BasicStroke(1));
            else
            g2.setStroke(shapeStroke); //set stroke pattern

            strokeCol.setScaling(cropX,cropH,scaling);
            g2.setPaint(strokeCol);

            if(strokeOpacity!=1f)
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,strokeOpacity));

            Shape clip=g2.getClip();
            if(clip!=null && (clip.getBounds2D().getHeight()<1 || clip.getBounds2D().getWidth()<1))
            g2.setClip(null);

            g2.draw(currentShape);

            g2.setClip(clip);
            g2.setStroke(currentStroke);

//      	reset opacity
            g2.setComposite(comp);
        }

    }

    /* remove all page objects and flush queue */
    public void flush() {

        if(shapeType!=null){
            shapeType.clear();
            pageObjects.clear();
            objectType.clear();
            areas.clear();
            clips.clear();
            x_coord=new float[defaultSize];
            y_coord=new float[defaultSize];
            textFillType.clear();
            text_color.clear();
            fill_color.clear();
            stroke_color.clear();
            stroke.clear();

            if(TRvalues!=null)
            TRvalues.clear();

            if(fs!=null)
                fs.clear();
            
            if(lw!=null)
                lw.clear();

            af1.clear();
            af2.clear();
            af3.clear();
            af4.clear();

            if(opacity!=null)
            opacity.clear();

            if(isPrinting)
                largeImages.clear();


            endItem=-1;
        }

        //pointer we use to flag color change
        lastFillTextCol=0;
        lastFillCol=0;
        lastStrokeCol=0;

        lastClip=null;
        hasClips=false;

        //track strokes
        lastStroke=null;

        lastAf=new double[4];

        currentItem=0;

        fillSet=false;
        strokeSet=false;

        fonts.clear();
        fontsUsed.clear();

        imageID.clear();
    }

    /**set background colour - null is transparent*/
    public void setBackgroundColor(Color background){
        if(background==null)
            this.addBackground=false;
        else
            backgroundColor=background;
    }

    int xx=0;
    int yy=0;

    private double minX=-1;

    private double minY=-1;

    private double maxX=-1;

    private double maxY=-1;

    private AffineTransform aff=new AffineTransform();

    /**raw page rotation*/
    private int rotation=0;

    /**shows if colours over-ridden for type3 font*/
    private boolean colorsLocked;

    /**flag to show if we try and optimise painting*/
    private boolean optimiseDrawing;

    private boolean renderFailed;

    /**optional frame for user to pass in - if present, error warning will be displayed*/
    private JFrame frame=null;

    /**make sure user only gets 1 error message a session*/
    private static boolean userAlerted=false;

    public void paint(Graphics2D g2,Rectangle[] highlights,int myx,int myy){

        if(myx<0)
            this.xx=myx;
        else
            this.xx=0;
        if(myy<0)
            this.yy=myy;
        else
            this.yy=0;

        paint(g2,highlights,null,null,false);
    }

    /*renders all the objects onto the g2 surface*/
    public Rectangle paint(Graphics2D g2,Rectangle[] highlights,AffineTransform viewScaling,Rectangle userAnnot,boolean drawJustHighlights){

		final boolean debug=false;
		
        int paintedCount=0;

        if(!this.isType3Font)
        glyphT3Count=0;


        if(!pageDrawing){ //lock to stop race condition

            pageDrawing=true;

            Rectangle dirtyRegion=null;

            //local copies
            int[] objectTypes=objectType.get();
            int[] textFill=textFillType.get();
            int count=objectTypes.length;
            Area[] pageClips=clips.get();
            double[] afValues1=af1.get();
            int[] fsValues=null;
            if(fs!=null)
            	fsValues=fs.get();
            
            int[] lwValues=null;
            if(lw!=null)
            	lwValues=lw.get();
            //int[] TRvalues=this.TRvalues.get();
            double[] afValues2=af2.get();
            double[] afValues3=af3.get();
            double[] afValues4=af4.get();
            Object[] text_color=this.text_color.get();
            Object[] fill_color=this.fill_color.get();

            Object[] stroke_color=this.stroke_color.get();
            Object[] pageObjects=this.pageObjects.get();
            Object[] javaObjects=this.javaObjects.get();
            Object[] stroke=this.stroke.get();
            int[] fillType=this.shapeType.get();

            float[] opacity = null;
            if(this.opacity!=null)
            	opacity=this.opacity.get();

            int[] TRvalues = null;
            if(this.TRvalues!=null)
            	TRvalues=this.TRvalues.get();


            Shape rawClip=g2.getClip();
            if(rawClip!=null)
                dirtyRegion=rawClip.getBounds();//g2.getClipBounds();
                
            //g2.setClip(dirtyRegion);

            boolean isInitialised=false;

            Shape defaultClip=g2.getClip();

            //used to optimise clipping
            Area clipToUse=null;
            boolean newClip=false;

            /**/
            if(!drawJustHighlights)
            paintBackground(g2, dirtyRegion);/**/

            /**save raw scaling and apply any viewport*/
            AffineTransform rawScaling=g2.getTransform();
            if(viewScaling!=null){
                g2.transform(viewScaling);
                defaultClip=g2.getClip(); //not valid if viewport so disable
            }

            //reset tracking of box
            minX=-1;
            minY=-1;
            maxX=-1;
            maxY=-1;

            //set crop
            //if(currentClip!=null)
            //g2.setClip((Shape)currentClip);

            int type=0,textFillType=0,currentTR=GraphicsState.FILL;
            int lineWidth=0;
            float fillOpacity=1.0f;
            float strokeOpacity=1.0f;
            float x=0,y=0;
            int iCount=0,cCount=0,sCount=0,etCount=0,fsCount=-1,lwCount=0,afCount=-1,tCount=0,stCount=0,fillCount=0,strokeCount=0,trCount=0,opCount=0,stringCount=0;//note af is 1 behind!
            PdfPaint textStrokeCol=null,textFillCol=null,fillCol=null,strokeCol = null;
            Stroke currentStroke=null;

            if(colorsLocked){
                strokeCol=this.strokeCol;
                fillCol=this.fillCol;
            }

//			if(visbleRect!=null){
//				g2.setPaint(Color.MAGENTA);
//				g2.fill(visbleRect);
//				System.out.println("visbleRect="+visbleRect);
//			}

            for(int i=0;i<count;i++){

                boolean ignoreItem=false,isHighlighted=false;

                type=objectTypes[i];

                Rectangle currentArea=null;


				if(type>0){

					x=x_coord[i];
                    y=y_coord[i];

                    Object currentObject=null;

					currentObject=pageObjects[i];


					/**
                     * workout area occupied by glyf
                     */
                    if(currentArea!=null){
                    	//ignore as already done
                    }else if(afValues1!=null && type==IMAGE){

                    	BufferedImage img=((BufferedImage)pageObjects[i]);

                    	if(img!=null)
                    	currentArea=new Rectangle((int)x,(int)y,img.getWidth(),img.getHeight());

                    }else if(afValues1!=null && type==SHAPE){

                    	currentArea=((Shape)pageObjects[i]).getBounds();

                    	//System.out.println(currentArea);
                    }else if(type==TEXT && afCount>-1){

						int x1=((Area)currentObject).getBounds().x;
						int y1=((Area)currentObject).getBounds().y;
						currentArea=getAreaForGlyph(new float[][]{{(float)afValues1[afCount],(float)afValues2[afCount],0},
						{(float)afValues3[afCount],(float)afValues4[afCount],0},
						{x1,y1,1}});

						//currentArea=new Rectangle((int)x,(int)y,fsValues[fsCount],fsValues[fsCount]);
						//currentArea=(((Area)currentObject).getBounds());
					}else if(fsCount!=-1 && afValues1!=null){// && afCount>-1){
						currentArea=new Rectangle((int)x,(int)y,fsValues[fsCount],fsValues[fsCount]);
					}


                    //ignoreItem=false;

                    if(ignoreItem){

                        //keep local counts in sync
                        switch (type) {

                        case SHAPE:
                            sCount++;
                            break;
                        case IMAGE:
                            iCount++;
                            break;
                        case CLIP:
                            cCount++;
                        case TRUETYPE:
                            etCount++;
                            break;
                        case TYPE1C:
                            etCount++;
                            break;
                        case TYPE3:
                            etCount++;
                            break;
                        case AF:
                            afCount++;
                            break;
                        case FONTSIZE:
                            fsCount++;
                            break;
                        case LINEWIDTH:
                            lwCount++;
                            break;    
                        case TEXTCOLOR:
                            tCount++;
                            break;
                        case FILLCOLOR:
                            fillCount++;
                            break;
                        case STROKECOLOR:
                            strokeCount++;
                            break;
                        case STROKE:
                            stCount++;
                            break;
                        case TR:
                            trCount++;
                            break;
                        }

                    }else{

                        if(!isInitialised){

                            //set hints to produce high quality image
                            g2.setRenderingHints(hints);
                            isInitialised=true;
                        }

                        paintedCount++;


                        switch (type) {

                        case SHAPE:

                            if(debug)
                                System.out.println("Shape");

                            if(!drawJustHighlights){
	                            if(newClip){
	                               renderClip(clipToUse, dirtyRegion,defaultClip,g2);
	                               newClip=false;
	                            }

	                            renderShape(fillType[sCount],strokeCol,fillCol,
	                                    currentStroke,(Shape)currentObject,g2,strokeOpacity,fillOpacity);
                            }

                            sCount++;

                            break;
                            /**/
                        case TEXT:

                            if(debug)
                                System.out.println("Text");

                            if(newClip){
                                renderClip(clipToUse, dirtyRegion,defaultClip,g2);
                                newClip=false;
                            }

                            /** see if highlighted */
                            if(highlights!=null)
                                isHighlighted = setHighlightForGlyph(currentArea,objectTypes,highlights, i, isHighlighted);

							if(!drawJustHighlights || isHighlighted)
                            renderText(currentTR,(Area)currentObject,g2,isHighlighted,
									textStrokeCol,textFillCol,strokeOpacity,fillOpacity,currentArea);
                            break;

						case TRUETYPE:

                            if(debug)
                                System.out.println("Truetype");

                            if(newClip){
                                renderClip(clipToUse, dirtyRegion,defaultClip,g2);
                                newClip=false;
                            }

                            
                            /** see if highlighted */
                            if(highlights!=null)
                                isHighlighted = setHighlightForGlyph(currentArea,objectTypes,highlights, i, isHighlighted);

                            aff=new AffineTransform(afValues1[afCount],afValues2[afCount],
                                    afValues3[afCount],afValues4[afCount],x,y);

                            if(!drawJustHighlights || isHighlighted)
                            renderEmbeddedText(currentTR,currentObject,TRUETYPE,g2,aff,
                            		isHighlighted,textStrokeCol,textFillCol,strokeOpacity,
									fillOpacity,currentArea,lineWidth);

							etCount++;
                            break;

                        case TYPE1C:

                            if(debug)
                                System.out.println("Type1c");

                            if(newClip){
                                renderClip(clipToUse, dirtyRegion,defaultClip,g2);
                                newClip=false;
                            }

                            /** see if highlighted */
                            if(highlights!=null)
                                isHighlighted = setHighlightForGlyph(currentArea,objectTypes,highlights, i, isHighlighted);

                            aff=new AffineTransform(afValues1[afCount],afValues2[afCount],
                                    afValues3[afCount],afValues4[afCount],x,y);

                            if(!drawJustHighlights || isHighlighted)
                            renderEmbeddedText(currentTR,currentObject,TYPE1C,g2,aff,isHighlighted,
									textStrokeCol,textFillCol,strokeOpacity,
									fillOpacity,currentArea,lineWidth);

                            etCount++;
                            break;

                        case TYPE3:

                            if(debug)
                                System.out.println("Type3");

                            if(newClip){
                                renderClip(clipToUse, dirtyRegion,defaultClip,g2);
                                newClip=false;
                            }

                            /** see if highlighted */
                            if(highlights!=null)
                                isHighlighted = setHighlightForGlyph(currentArea,objectTypes,highlights, i, isHighlighted);

                            aff=new AffineTransform(afValues1[afCount],afValues2[afCount],
                                    afValues3[afCount],afValues4[afCount],x,y);

                            if(!drawJustHighlights || isHighlighted)
                            renderEmbeddedText(currentTR,currentObject,TYPE3,g2,aff,isHighlighted,
									textStrokeCol,textFillCol,strokeOpacity,
									fillOpacity,currentArea,lineWidth);

                            etCount++;
                            break;

						case IMAGE:

                            if(debug)
                                System.out.println("Image");

                            if(!drawJustHighlights){


                            	if(newClip){
                            		
                            		renderClip(clipToUse, dirtyRegion,defaultClip,g2);
                            		newClip=false;
                            	}

                            	if(this.useHiResImageForDisplay){
                            	}else{
                            		renderImage(null,(BufferedImage)currentObject,fillOpacity,null,g2,x,y);
                            	}
                            }
                            iCount++;

                            break;

                        case CLIP:
                            clipToUse=pageClips[cCount];
                            newClip=true;
                            cCount++;
                            break;

                        case AF:
                            afCount++;
                            break;
                        case FONTSIZE:
                            fsCount++;
                            break;
                        case LINEWIDTH:
                        	lineWidth=lwValues[lwCount];
                            lwCount++;
                            break;    
                        case TEXTCOLOR:

                            if(debug)
                                System.out.println("TextCOLOR");

                            textFillType=textFill[tCount];

                            if(textFillType==GraphicsState.STROKE)
                                textStrokeCol=(PdfPaint) text_color[tCount];
                            else
                                textFillCol=(PdfPaint) text_color[tCount];

                            tCount++;
                            break;
                        case FILLCOLOR:

                            if(debug)
                                System.out.println("FillCOLOR");

                            if(!colorsLocked)
                                fillCol=(PdfPaint) fill_color[fillCount];

                            fillCount++;

                            break;
                        case STROKECOLOR:

                            if(debug)
                                System.out.println("StrokeCOL");

                            if(!colorsLocked){
                                strokeCol=(PdfPaint)stroke_color[strokeCount];
                                strokeCol.setScaling(cropX,cropH,scaling);
                            }

                            strokeCount++;
                            break;
                        case STROKE:

                            currentStroke=(Stroke)stroke[stCount];

                            if(debug)
                                System.out.println("STROKE");

                            stCount++;
                            break;
                        case TR:

                            if(debug)
                                System.out.println("TR");

                            currentTR=TRvalues[trCount];
                            trCount++;
                            break;
                        case STROKEOPACITY:

                            if(debug)
                                System.out.println("Stroke Opacity");

                            strokeOpacity=opacity[opCount];
                            opCount++;
                            break;
                        case FILLOPACITY:

                            if(debug)
                                System.out.println("Fill Opacity");

                            fillOpacity=opacity[opCount];
                            opCount++;
                            break;

                        case STRING:

                        	if(!drawJustHighlights){
                        		try{

                        			AffineTransform defaultAf=g2.getTransform();
                        			String displayValue=(String)currentObject;

                        			double[] af=new double[6];

                        			g2.getTransform().getMatrix(af);

                        			if(af[2]!=0)
                        				af[2]=-af[2];
                        			if(af[3]!=0)
                        				af[3]=-af[3];
                        			g2.setTransform(new AffineTransform(af));

                        			Font javaFont=(Font) javaObjects[stringCount];

                        			g2.setFont(javaFont);

                        			if((currentTR & GraphicsState.FILL)==GraphicsState.FILL){

                        				if(textFillCol!=null)
                        					textFillCol.setScaling(cropX,cropH,scaling);
                        				g2.setPaint(textFillCol);

                        			}

                        			if((currentTR & GraphicsState.STROKE)==GraphicsState.STROKE){

                        				if(textStrokeCol!=null)
                        					textStrokeCol.setScaling(cropX,cropH,scaling);
                        				g2.setPaint(textStrokeCol);

                        			}

                                    g2.drawString(displayValue,x,y);

                        			g2.setTransform(defaultAf);

                        			stringCount++;

                        		}catch(Exception e){
                        			System.err.println(currentObject+"<>"+i);
                        			e.printStackTrace();
                        			System.exit(1);
                        		}
                        	}
                            break;

                        }
                    }
                }
            }

            //restore defaults
            g2.setClip(defaultClip);

            g2.setTransform(rawScaling);

        }

        if(DynamicVectorRenderer.debugPaint)
            System.err.println("Painted "+paintedCount);

        pageDrawing=false;

        //<start-jfl>
        //tell user if problem
        if((frame!=null)&&(renderFailed)&&(userAlerted==false)){

            userAlerted=true;

            if(PdfDecoder.showErrorMessages){
                String status = (Messages.getMessage("PdfViewer.ImageDisplayError")+
                        Messages.getMessage("PdfViewer.ImageDisplayError1")+
                        Messages.getMessage("PdfViewer.ImageDisplayError2")+
                        Messages.getMessage("PdfViewer.ImageDisplayError3")+
                        Messages.getMessage("PdfViewer.ImageDisplayError4")+
                        Messages.getMessage("PdfViewer.ImageDisplayError5")+
                        Messages.getMessage("PdfViewer.ImageDisplayError6")+
                        Messages.getMessage("PdfViewer.ImageDisplayError7"));

                JOptionPane.showMessageDialog(frame,status);

                frame.invalidate();
                frame.repaint();
            }

        }
        //<end-jfl>

        //if we highlighted text return oversized
        if(minX==-1)
            return null;
        else
            return new Rectangle((int)minX,(int)minY,(int)(maxX-minX),(int)(maxY-minY));
    }

	//work out size glyph occupies
	private Rectangle getAreaForGlyph(float[][] trm){

		//workout area
		int w=(int) Math.sqrt((trm[0][0]*trm[0][0])+(trm[1][0]*trm[1][0]));
		int h=(int) Math.sqrt((trm[1][1]*trm[1][1])+(trm[0][1]*trm[0][1]));

		return (new Rectangle((int)trm[2][0],(int)trm[2][1],w,h));

	}

	/**
	 * allow user to set component for waring message in renderer to appear -
	 * if unset no message will appear
	 * @param frame
	 */
	public void setMessageFrame(JFrame frame){
		this.frame=frame;
	}

    public void paintBackground(Graphics2D g2, Rectangle dirtyRegion) {
        if((addBackground)){
            g2.setColor(backgroundColor);
            if(dirtyRegion==null)
                g2.fill(new Rectangle(xx,yy,w,h));
            else
                g2.fill(dirtyRegion);

        }
    }

    /**
     * update clip
     * @param defaultClip
     */
    public void renderClip(Area clip,Rectangle dirtyRegion,  Shape defaultClip,Graphics2D g2) {

        /**/
    	
        if (clip != null){
            g2.setClip(clip);
            
            //can cause problems in Canoo so limit effect if Canoo running
            if(dirtyRegion!=null)// && (!isRunningOnRemoteClient || clip.intersects(dirtyRegion)))
                g2.clip(dirtyRegion);
        }else
            g2.setClip(defaultClip);

        /***/
    }

    /**
     * highlight a glyph by reversing the display. For white text, use black
     */
    private boolean setHighlightForGlyph(Rectangle area,int[] objectTypes,Rectangle[] highlights, int i, boolean isHighlighted) {

        int hcount=highlights.length;
        for(int i2=0;i2<hcount;i2++){
            int objectType=objectTypes[i];

            if((area!=null)&&((objectType==TEXT)|(objectType==TRUETYPE)|(objectType==TYPE1C)|(objectType==TYPE3))){

                if((highlights[i2]!=null)&&(highlights[i2].intersects(area))
                        &&(highlights[i2].getMinX()<=area.getMinX())&&(highlights[i2].getMinY()<=area.getMinY())){
                    i2=hcount;
                    isHighlighted=true;

                    Rectangle2D bounds = area.getBounds2D();

                    if(minX==-1){
                        minX=bounds.getMinX();
                        minY=bounds.getMinY();
                        maxX=bounds.getMaxX();
                        maxY=bounds.getMaxY();
                    }else{
                        double tmp=bounds.getMinX();
                        if(tmp<minX)
                            minX=tmp;
                        tmp=bounds.getMinY();
                        if(tmp<minY)
                            minY=tmp;
                        tmp=bounds.getMaxX();
                        if(tmp>maxX)
                            maxX=tmp;
                        tmp=bounds.getMaxY();
                        if(tmp>maxY)
                            maxY=tmp;
                    }

                }
            }
        }
        return isHighlighted;
    }

    /* saves text object with attributes for rendering*/
    final public void drawText(float[][] Trm,Object transformedGlyph2,GraphicsState currentGraphicsState) {

        /**
         * set color first
         */
        PdfPaint currentCol=null;

		if(Trm!=null){
			double[] nextAf=new double[]{Trm[0][0],Trm[0][1],Trm[1][0],Trm[1][1],Trm[2][0],Trm[2][1]};

			if((lastAf[0]==nextAf[0])&&(lastAf[1]==nextAf[1])&&
					(lastAf[2]==nextAf[2])&&(lastAf[3]==nextAf[3])){
			}else{

				this.drawAffine(nextAf);
				lastAf[0]=nextAf[0];
				lastAf[1]=nextAf[1];
				lastAf[2]=nextAf[2];
				lastAf[3]=nextAf[3];
			}
		}

		int text_fill_type = currentGraphicsState.getTextRenderType();

        //for a fill
        if ((text_fill_type & GraphicsState.FILL) == GraphicsState.FILL) {
            currentCol=currentGraphicsState.getNonstrokeColor();

            if(currentCol.isPattern()){
                drawColor(currentCol,GraphicsState.FILL);
                resetTextColors=true;
            }else{

                int newCol=(currentCol).getRGB();
                if((resetTextColors)||((lastFillTextCol!=newCol))){
                    lastFillTextCol=newCol;
                    drawColor(currentCol,GraphicsState.FILL);
                }
            }
        }

        //and/or do a stroke
        if ((text_fill_type & GraphicsState.STROKE) == GraphicsState.STROKE){
            currentCol=currentGraphicsState.getStrokeColor();

            if(currentCol.isPattern()){
                drawColor(currentCol,GraphicsState.STROKE);
                resetTextColors=true;
            }else{
                int newCol=currentCol.getRGB();
                if((resetTextColors)||(lastStrokeCol!=newCol)){
                    lastStrokeCol=newCol;
                    drawColor(currentCol,GraphicsState.STROKE);
                }
            }
        }

        //drawFontSize((int) ((Area)transformedGlyph2).getBounds().getWidth());

        if(transformedGlyph2 instanceof Area) {
			drawFontSize((int) ((Area)transformedGlyph2).getBounds().getWidth());
			pageObjects.addElement(transformedGlyph2);

			areas.addElement(((Area)transformedGlyph2).getBounds());
            objectType.addElement(TEXT);

        }else{
        	pageObjects.addElement(transformedGlyph2);
            objectType.addElement(-TEXT);
        }

        x_coord=checkSize(x_coord,currentItem);
        y_coord=checkSize(y_coord,currentItem);
        x_coord[currentItem]=Trm[2][0];
        y_coord[currentItem]=Trm[2][1];

		currentItem++;

        resetTextColors=false;

        //flag as dirty
        //if(currentManager!=null)
        //currentManager.addDirtyRegion(drawPanel,transformedGlyph2.getBounds().x,transformedGlyph2.getBounds().y,transformedGlyph2.getBounds().width,transformedGlyph2.getBounds().height);
    }

    /* saves text object with attributes for rendering*/
    final public void drawText(float[][] Trm,String text,GraphicsState currentGraphicsState,float x,float y,Font javaFont) {

        /**
         * set color first
         */
        PdfPaint currentCol=null;


		if(Trm!=null){
			double[] nextAf=new double[]{Trm[0][0],Trm[0][1],Trm[1][0],Trm[1][1],Trm[2][0],Trm[2][1]};

			if((lastAf[0]==nextAf[0])&&(lastAf[1]==nextAf[1])&&
					(lastAf[2]==nextAf[2])&&(lastAf[3]==nextAf[3])){
			}else{
				this.drawAffine(nextAf);
				lastAf[0]=nextAf[0];
				lastAf[1]=nextAf[1];
				lastAf[2]=nextAf[2];
				lastAf[3]=nextAf[3];
			}
		}

		int text_fill_type = currentGraphicsState.getTextRenderType();

        //for a fill
        if ((text_fill_type & GraphicsState.FILL) == GraphicsState.FILL) {
            currentCol=currentGraphicsState.getNonstrokeColor();

            if(currentCol.isPattern()){
                drawColor(currentCol,GraphicsState.FILL);
                resetTextColors=true;
            }else{

                int newCol=(currentCol).getRGB();
                if((resetTextColors)||((lastFillTextCol!=newCol))){
                    lastFillTextCol=newCol;
                    drawColor(currentCol,GraphicsState.FILL);
                }
            }
        }

        //and/or do a stroke
        if ((text_fill_type & GraphicsState.STROKE) == GraphicsState.STROKE){
            currentCol=currentGraphicsState.getStrokeColor();

            if(currentCol.isPattern()){
                drawColor(currentCol,GraphicsState.STROKE);
                resetTextColors=true;
            }else{

                int newCol=currentCol.getRGB();
                if((resetTextColors)||(lastStrokeCol!=newCol)){
                    lastStrokeCol=newCol;
                    drawColor(currentCol,GraphicsState.STROKE);
                }
            }
        }

        pageObjects.addElement(text);
        javaObjects.addElement(javaFont);

        objectType.addElement(STRING);

        x_coord=checkSize(x_coord,currentItem);
        y_coord=checkSize(y_coord,currentItem);
        x_coord[currentItem]=x;
        y_coord[currentItem]=y;

        currentItem++;

        resetTextColors=false;

        //flag as dirty
        //if(currentManager!=null)
        //currentManager.addDirtyRegion(drawPanel,transformedGlyph2.getBounds().x,transformedGlyph2.getBounds().y,transformedGlyph2.getBounds().width,transformedGlyph2.getBounds().height);
    }


    /**resize array*/
    private float[] checkSize(float[] array, int currentItem) {

        int size=array.length;
        if(size<=currentItem){
            int newSize=size*2;
            float[] newArray=new float[newSize];
            System.arraycopy( array, 0, newArray, 0, size );

            array=newArray;
        }


        return array;

    }

    /**workout combined area of shapes in an area*/
    public  Rectangle getCombinedAreas(Rectangle targetRectangle,boolean justText){

        Rectangle combinedRectangle=null;

        if(areas!=null){

            //set defaults for new area
            Rectangle target = targetRectangle.getBounds();
            int x2=target.x;
            int y2=target.y;
            int x1=x2+target.width;
            int y1=y2+target.height;

            boolean matchFound=false;

            Rectangle[] currentAreas=areas.get();
            int count=currentAreas.length;
            //find all items enclosed by this rectangle
            for(int i=0;i<count;i++){
                if((currentAreas[i]!=null)&&(targetRectangle.contains(currentAreas[i]))){
                    matchFound=true;

                    int newX=currentAreas[i].x;
                    if(x1>newX)
                        x1=newX;
                    newX=currentAreas[i].x+currentAreas[i].width;
                    if(x2<newX)
                        x2=newX;

                    int newY=currentAreas[i].y;
                    if(y1>newY)
                        y1=newY;
                    newY=currentAreas[i].y+currentAreas[i].height;
                    if(y2<newY)
                        y2=newY;
                }
            }

            //allow margin of 1 around object
            if(matchFound){
                combinedRectangle=new Rectangle(x1-1,y1+1,(x2-x1)+2,(y2-y1)+2);

            }

        }

        return combinedRectangle;
    }


    /*setup renderer*/
    final public void init(int x, int y,int rawRotation) {
        w=x;
        h=y;
        this.rotation=rawRotation;

    }

    /* get page as Image*/
    final public BufferedImage getPageAsImage(
            float scaling,
            int cropX,int cropY,
            int cropW,int cropH,
            int page,AffineTransform af_scaling,int type) {

        if(cropW<0){
            cropW=w;
            cropH=h;
        }else{
            cropW=(int) (cropW*scaling);
            cropH=(int) (cropH*scaling);
        }

        BufferedImage image=new BufferedImage(cropW, cropH, type);

        Graphics2D g2 = image.createGraphics();
        if(type==1){
            g2.setColor(Color.white);
            g2.fillRect(0,0,cropW,cropH);
        }

        AffineTransform af=g2.getTransform();


        if(af_scaling!=null)
            g2.transform(af_scaling);
        paint(g2,null,cropX,cropY);

        //ShowGUIMessage.showGUIMessage("w",image,"w");

        g2.setTransform(af);

        return image;
    }

    //<start-jfl>
    /* save image in array to draw */
    final public void drawImage(int pageNumber,BufferedImage image,
											 GraphicsState currentGraphicsState,
                                             boolean alreadyCached,String name) {

    	this.pageNumber=pageNumber;
        float CTM[][]=currentGraphicsState.CTM;

        float x=currentGraphicsState.x;
        float y=currentGraphicsState.y;

        //Turn image around if needed
        if(!alreadyCached && image.getHeight()>1)
        image = invertImage(CTM, image);

       if(useHiResImageForDisplay){


        }

        x_coord=checkSize(x_coord,currentItem);
        y_coord=checkSize(y_coord,currentItem);
        x_coord[currentItem]=x;
        y_coord[currentItem]=y;

        objectType.addElement(IMAGE);

        w=(int)CTM[0][0];
		if(w==0)
			w=(int)CTM[0][1];
		h=(int)CTM[1][1];
		if(h==0)
			h=(int)CTM[1][0];


        areas.addElement(new Rectangle((int)currentGraphicsState.x,(int)currentGraphicsState.y,w,h));

        if(useHiResImageForDisplay){
            pageObjects.addElement(null);
        }else
            pageObjects.addElement(image);

        //store id so we can get as low res image

        imageID.put(name,new Integer(currentItem));

        currentItem++;

	}
    //<end-jfl>

    private BufferedImage invertImage(float[][] CTM, BufferedImage image) {

    	boolean isInverted=((CTM[0][0]>0)&&(CTM[1][1]>0))||((CTM[0][0]<0)&&(CTM[1][1]<0))||
                ((CTM[0][0]*CTM[1][1])<0)||
                (((CTM[0][1]*CTM[1][0])!=0));

        //Matrix.show(CTM);

        if(isInverted){

            //turn upside down
            AffineTransform image_at2 =new AffineTransform();
            image_at2.scale(1,-1);
            image_at2.translate(0,-image.getHeight());

            AffineTransformOp invert3= new AffineTransformOp(image_at2,  ColorSpaces.hints);

            boolean imageProcessed=false;

            if(JAIHelper.isJAIused()){

                imageProcessed=true;

                try{
                    image = (javax.media.jai.JAI.create("affine", image, image_at2, new javax.media.jai.InterpolationNearest())).getAsBufferedImage();
                }catch(Exception ee){
                    imageProcessed=false;
                    ee.printStackTrace();
                }catch(Error err){
                    imageProcessed=false;
                     err.printStackTrace();
                }

                if(!imageProcessed) {
                    LogWriter.writeLog("Unable to use JAI for image inversion");
                }

            } /**/

            if(!imageProcessed){

                if(image.getType()==12){ //avoid turning into ARGB

                    BufferedImage source=image;
                    image =new BufferedImage(source.getWidth(),source.getHeight(),source.getType());

                    invert3.filter(source,image);
                }else
                    image=invert3.filter(image,null);

            }
        }
        return image;
    }

    //<start-jfl>
    /* save image in array to draw */
    final public void drawImage(BufferedImage image) {

        int h = image.getHeight();

        /**/
        //turn upside down
        if(h>1){
            AffineTransform flip=new AffineTransform();
            flip.translate(0, h);
            flip.scale(1, -1);
            AffineTransformOp invert =new AffineTransformOp(flip,ColorSpaces.hints);
            image=invert.filter(image,null);
        }

        if(useHiResImageForDisplay){

        }

        x_coord=checkSize(x_coord,currentItem);
        y_coord=checkSize(y_coord,currentItem);
        x_coord[currentItem]=0;
        y_coord[currentItem]=0;

        objectType.addElement(IMAGE);
        areas.addElement(new Rectangle(0,0,image.getWidth(),image.getHeight()));

        if(useHiResImageForDisplay){
            pageObjects.addElement(null);
        }else
            pageObjects.addElement(image);

		currentItem++;

    }
    //<end-jfl>

    /*save shape in array to draw*/
    final public void drawShape(Shape currentShape,GraphicsState currentGraphicsState) {

        int fillType=currentGraphicsState.getFillType();
        PdfPaint currentCol;

        int newCol;


        //check for 1 by 1 complex shape and replace with dot
        if( (currentShape.getBounds().getWidth()==1)&&
                (currentShape.getBounds().getHeight()==1))
            currentShape=new Rectangle(0,0,1,1);

        //stroke and fill (do fill first so we don't overwrite Stroke)
        if ((fillType == GraphicsState.FILL) | (fillType == GraphicsState.FILLSTROKE)) {

            currentCol=currentGraphicsState.getNonstrokeColor();

            if(currentCol.isPattern()){

                drawFillColor(currentCol);
                fillSet=true;
            }else{
                newCol=currentCol.getRGB();
                if((!fillSet) || (lastFillCol!=newCol)){
                    lastFillCol=newCol;
                    drawFillColor(currentCol);
                    fillSet=true;

                }
            }
        }

        if ((fillType == GraphicsState.STROKE) | (fillType == GraphicsState.FILLSTROKE)) {

            currentCol=currentGraphicsState.getStrokeColor();

            if(currentCol instanceof Color){
                newCol=(currentCol).getRGB();

                if((!strokeSet) || (lastStrokeCol!=newCol)){
                    lastStrokeCol=newCol;
                    drawStrokeColor(currentCol);
                    strokeSet=true;
                }
            }else{
                drawStrokeColor(currentCol);
                strokeSet=true;
            }
        }

        Stroke newStroke=currentGraphicsState.getStroke();
        if((lastStroke!=null)&&(lastStroke.equals(newStroke))){

        }else{
            lastStroke=newStroke;
            drawStroke((newStroke));
        }

        pageObjects.addElement(currentShape);
        objectType.addElement(SHAPE);
        areas.addElement(currentShape.getBounds());

        x_coord=checkSize(x_coord,currentItem);
        y_coord=checkSize(y_coord,currentItem);
        x_coord[currentItem]=currentGraphicsState.x;
        y_coord[currentItem]=currentGraphicsState.y;

        shapeType.addElement(fillType);
        currentItem++;

        resetTextColors=true;

      //  if(currentItem>581)
      //  	System.exit(1);
    }

    /*save text colour*/
    final public void drawColor(PdfPaint currentCol,int type) {

        pageObjects.addElement(null);
        objectType.addElement(TEXTCOLOR);
        textFillType.addElement(type); //used to flag which has changed

        text_color.addElement(currentCol);

        x_coord=checkSize(x_coord,currentItem);
        y_coord=checkSize(y_coord,currentItem);
        x_coord[currentItem]=0;
        y_coord[currentItem]=0;

        currentItem++;

        //ensure any shapes reset color
        strokeSet=false;
        fillSet=false;

    }


    /**reset on colorspace change to ensure cached data up to data*/
    public void resetOnColorspaceChange(){

        fillSet=false;
        strokeSet=false;

    }

    /*save shape colour*/
    final public void drawFillColor(PdfPaint currentCol) {

        pageObjects.addElement(null);
        objectType.addElement(FILLCOLOR);

        //fill_color.addElement(new Color (currentCol.getRed(),currentCol.getGreen(),currentCol.getBlue()));
        fill_color.addElement(currentCol);

        x_coord=checkSize(x_coord,currentItem);
        y_coord=checkSize(y_coord,currentItem);
        x_coord[currentItem]=0;
        y_coord[currentItem]=0;

        currentItem++;

        this.lastFillCol=currentCol.getRGB();

    }

    /*save opacity settings*/
    final public void setGraphicsState(int fillType,float value) {

    	if(value!=1.0f){

    		if(opacity==null)
    			opacity=new Vector_Float(defaultSize);

    		pageObjects.addElement(null);

	        if(fillType==GraphicsState.STROKE)
	        objectType.addElement(STROKEOPACITY);
	        else
	        objectType.addElement(FILLOPACITY);

	        opacity.addElement(value);

	        x_coord=checkSize(x_coord,currentItem);
	        y_coord=checkSize(y_coord,currentItem);
	        x_coord[currentItem]=0;
	        y_coord[currentItem]=0;

	        currentItem++;
    	}

    }

    /*Method to add Shape, Text or image to main display on page over PDF - will be flushed on redraw*/
    final public void drawAdditionalObjectsOverPage(int[] type, Color[] colors,Object[] obj) throws PdfException {

        /**
         * remember end of items from PDF page
         */
        if(endItem==-1){
            endItem=currentItem;
        }

        //reset and remove all from page
        if(obj==null){

            //reset pointer
            if(endItem!=-1)
                currentItem=endItem;

            endItem=-1;

            if(objectType.size()-1>currentItem)
                objectType.setSize(currentItem);

            if(shapeType.size()-1>currentItem)
                shapeType.setSize(currentItem);

            if(pageObjects.size()-1>currentItem)
                pageObjects.setSize(currentItem);

            if(areas.size()-1>currentItem)
                areas.setSize(currentItem);

            if(clips.size()-1>currentItem)
                clips.setSize(currentItem);

            if(textFillType.size()-1>currentItem)
                textFillType.setSize(currentItem);

            if(text_color.size()-1>currentItem)
                text_color.setSize(currentItem);

            if(fill_color.size()-1>currentItem)
                fill_color.setSize(currentItem);

            if(stroke_color.size()-1>currentItem)
                stroke_color.setSize(currentItem);

            if(stroke.size()-1>currentItem)
                stroke.setSize(currentItem);

            if(TRvalues!=null && TRvalues.size()-1>currentItem)
                TRvalues.setSize(currentItem);

            if(fs!=null && fs.size()-1>currentItem)
                fs.setSize(currentItem);

            if(lw!=null && lw.size()-1>currentItem)
                lw.setSize(currentItem);

            if(af1.size()-1>currentItem)
                af1.setSize(currentItem);

            if(af2.size()-1>currentItem)
                af2.setSize(currentItem);

            if(af3.size()-1>currentItem)
                af3.setSize(currentItem);

            if(af4.size()-1>currentItem)
                af4.setSize(currentItem);

            if(opacity!=null)
                opacity.clear();
            if(objectType.size()-1>currentItem)
                objectType.setSize(currentItem);

            endItem=-1;

            //reset pointers we use to flag color change
            lastFillTextCol=0;
            lastFillCol=0;
            lastStrokeCol=0;

            lastClip=null;
            hasClips=false;

            lastStroke=null;

            lastAf=new double[4];

            fillSet=false;
            strokeSet=false;

            // fonts.clear();
            // fontsUsed.clear();

            // imageID.clear();

            return ;
        }

        /**
         * cycle through items and add to display - throw exception if not valid
         */
        int count=type.length;

        int currentType;

        GraphicsState gs;

        for(int i=0;i<count;i++){

            currentType=type[i];

            switch(currentType){
                case STROKEDSHAPE:
                    gs=new GraphicsState();
                    gs.setFillType(GraphicsState.STROKE);
                    gs.setStrokeColor(new PdfColor(colors[i].getRed(),colors[i].getGreen(),colors[i].getBlue()));
                    drawShape( (Shape)obj[i],gs);

                    break;

                case FILLEDSHAPE:
                    gs=new GraphicsState();
                    gs.setFillType(GraphicsState.FILL);
                    gs.setNonstrokeColor(new PdfColor(colors[i].getRed(),colors[i].getGreen(),colors[i].getBlue()));
                    drawShape( (Shape)obj[i],gs);

                    break;

                case STRING:
                    TextObject textObj=(TextObject)obj[i];
                    gs=new GraphicsState();
                    float fontSize=textObj.font.getSize();
                    double[] afValues={fontSize,0f,0f,fontSize,0f,0f};
                    drawAffine(afValues);

                    drawTR(GraphicsState.FILL);
                    gs.setTextRenderType(GraphicsState.FILL);
                    gs.setNonstrokeColor(new PdfColor(colors[i].getRed(),colors[i].getGreen(),colors[i].getBlue()));
                    drawText(null,textObj.text,gs,textObj.x,-textObj.y,textObj.font); //note y is negative


                    break;

                default:
                    throw new PdfException("Unrecognised type "+currentType);
            }
        }
    }

    /*save shape colour*/
    final public void drawStrokeColor(Paint currentCol) {

        pageObjects.addElement(null);
        objectType.addElement(STROKECOLOR);
        
        //stroke_color.addElement(new Color (currentCol.getRed(),currentCol.getGreen(),currentCol.getBlue()));
        stroke_color.addElement(currentCol);

        x_coord=checkSize(x_coord,currentItem);
        y_coord=checkSize(y_coord,currentItem);
        x_coord[currentItem]=0;
        y_coord[currentItem]=0;

        currentItem++;

        strokeSet=false;
        fillSet=false;
        resetTextColors=true;

    }

    /*save shape stroke*/
    final public void drawTR(int value) {

        if(value!=lastTR){ //only cache if needed

        	if(TRvalues==null)
        		TRvalues=new Vector_Int(defaultSize);
	        
            lastTR=value;

            pageObjects.addElement(null);
            objectType.addElement(TR);

            this.TRvalues.addElement(value);

            x_coord=checkSize(x_coord,currentItem);
            y_coord=checkSize(y_coord,currentItem);
            x_coord[currentItem]=0;
            y_coord[currentItem]=0;
            
            
            currentItem++;
        }

    }


    /*save shape stroke*/
    final public void drawStroke(Stroke current) {

        pageObjects.addElement(null);
        objectType.addElement(STROKE);
        
        this.stroke.addElement((current));

        x_coord=checkSize(x_coord,currentItem);
        y_coord=checkSize(y_coord,currentItem);
        x_coord[currentItem]=0;
        y_coord[currentItem]=0;

        currentItem++;

    }

    boolean hasClips=false;
    Area lastClip=null;

    private double cropX;

    private double cropH;

    private float scaling;

    /*save clip in array to draw*/
    final public void drawClip(GraphicsState currentGraphicsState) {

        Area clip=currentGraphicsState.getClippingShape();

        if((hasClips)&&(lastClip==null)&&(clip==null)){
        }else{
        	
            pageObjects.addElement(null);
            objectType.addElement(CLIP);
            
            lastClip=clip;

            if(clip==null){
                clips.addElement(null);

                //System.out.println("======null clip");
            }else{

                clips.addElement((Area) clip.clone());
                //System.out.println("======"+clip.getBounds());
            }

            x_coord=checkSize(x_coord,currentItem);
            y_coord=checkSize(y_coord,currentItem);
            x_coord[currentItem]=currentGraphicsState.x;
            y_coord[currentItem]=currentGraphicsState.y;

            currentItem++;
        }

        hasClips=true;
    }

    /**
     * store glyph info
     */
    public void drawEmbeddedText(float[][] Trm,int fontSize,PdfGlyph glyph,int type, 
    		GraphicsState currentGraphicsState,AffineTransform at,float currentLineWidth) {

        /**
         * set color first
         */
        PdfPaint currentCol;

        int text_fill_type = currentGraphicsState.getTextRenderType();

        //for a fill
        if ((text_fill_type & GraphicsState.FILL) == GraphicsState.FILL){

            currentCol=currentGraphicsState.getNonstrokeColor();
            //drawColor(currentCol,GraphicsState.FILL);

            if(currentCol.isPattern()){
                drawColor(currentCol,GraphicsState.FILL);
            }else{

                int newCol=currentCol.getRGB();
                if(lastFillTextCol!=newCol){
                    lastFillTextCol=newCol;
                    drawColor(currentCol,GraphicsState.FILL);
                }
            }
        }

        //and/or do a stroke
        if ((text_fill_type & GraphicsState.STROKE) == GraphicsState.STROKE){
            currentCol=currentGraphicsState.getStrokeColor();

            int newCol=currentCol.getRGB();
            if(lastStrokeCol!=newCol){
                lastStrokeCol=newCol;
                drawColor(currentCol,GraphicsState.STROKE);
            }
        }

        drawFontSize(fontSize);
        
       // setLineWidth((int)(currentLineWidth+0.5f));
        
        double[] nextAf=new double[6];
        at.getMatrix(nextAf);
        if((lastAf[0]==nextAf[0])&&(lastAf[1]==nextAf[1])&&
                (lastAf[2]==nextAf[2])&&(lastAf[3]==nextAf[3])){
        }else{
            this.drawAffine(nextAf);
            lastAf[0]=nextAf[0];
            lastAf[1]=nextAf[1];
            lastAf[2]=nextAf[2];
            lastAf[3]=nextAf[3];
        }

        /**now text*/
        pageObjects.addElement(glyph);
        objectType.addElement(type);

        x_coord=checkSize(x_coord,currentItem);
        y_coord=checkSize(y_coord,currentItem);
        x_coord[currentItem]=(float) at.getTranslateX();
        y_coord[currentItem]=(float) at.getTranslateY();

        areas.addElement(new Rectangle((int)Trm[2][0],(int)Trm[2][1],fontSize,fontSize));

        
        currentItem++;

    }

    /**
     * store af info
     */
    public void drawAffine(double[] afValues) {

        pageObjects.addElement(null);
        //pageAT.add(null);
        objectType.addElement(AF);

        
        af1.addElement(afValues[0]);
        af2.addElement(afValues[1]);
        af3.addElement(afValues[2]);
        af4.addElement(afValues[3]);

        x_coord=checkSize(x_coord,currentItem);
        y_coord=checkSize(y_coord,currentItem);
        x_coord[currentItem]=(float)afValues[4];
        y_coord[currentItem]=(float)afValues[5];

        currentItem++;

    }
    
    /**
     * store af info
     */
    public void drawFontSize(int fontSize) {

    	if(fontSize!=lastFS){
	        pageObjects.addElement(null);
	        //pageAT.add(null);
	        objectType.addElement(FONTSIZE);
	        
	        if(fs==null)
        		fs=new Vector_Int(defaultSize);
	        
	        fs.addElement(fontSize);
	
	        x_coord=checkSize(x_coord,currentItem);
	        y_coord=checkSize(y_coord,currentItem);
	        x_coord[currentItem]=0;
	        y_coord[currentItem]=0;
	
	        currentItem++;
	        
	        lastFS=fontSize;
	        
	   }
    }
    
    /**
     * store line width info
     */
    public void setLineWidth(int lineWidth) {

    	if(lineWidth!=lastLW || 1==1){
	        
    		pageObjects.addElement(null);
	        //pageAT.add(null);
	        objectType.addElement(LINEWIDTH);
	        
	        if(lw==null)
        		lw=new Vector_Int(defaultSize);
	        
	        lw.addElement(lineWidth);
	
	        x_coord=checkSize(x_coord,currentItem);
	        y_coord=checkSize(y_coord,currentItem);
	        x_coord[currentItem]=0;
	        y_coord[currentItem]=0;
	
	        currentItem++;
	        
	        lastLW=lineWidth;
	        
	   }

    }

    /**
     * set affine transform
     */
    public void renderAffine(AffineTransform at) {
        aff=at;

    }

    /**
     * @return true if background needed to be added
     */
    public boolean addBackground() {

        return addBackground;
    }

    /**
     * @return background color
     */
    public Color getBackgroundColor() {

        return backgroundColor;
    }

    /**
     * used by type 3 glyphs to set colour
     */
    public void lockColors(PdfPaint strokePaint, PdfPaint nonstrokePaint) {

        colorsLocked=true;
        Color strokeColor=Color.white,nonstrokeColor=Color.white;

        if(!strokePaint.isPattern())
        strokeColor=(Color) strokePaint;
        strokeCol=new PdfColor (strokeColor.getRed(),strokeColor.getGreen(),strokeColor.getBlue());

        if(!nonstrokePaint.isPattern())
        nonstrokeColor=(Color) nonstrokePaint;
        fillCol=new PdfColor (nonstrokeColor.getRed(),nonstrokeColor.getGreen(),nonstrokeColor.getBlue());

    }

    /**
     * Screen drawing using hi res images and not down-sampled images but may be slower
     * and use more memory<br> Default setting is <b>false</b> and does nothing in
	 * OS version
     */
    public void setHiResImageForDisplayMode(boolean useHiResImageForDisplay) {
    }

    /**
     *
     *
    public void dumpImagesFromMemory() {

        if(tmpFile==null){
            try{

                //trash the images - program will reload
                int count=pageObjects.size();
                for(int i=0;i<count;i++){
                    Object nextObject=pageObjects.elementAt(i);

                    if((nextObject!=null)&&(nextObject instanceof BufferedImage))
                        pageObjects.setElementAt(null,i);

                }

            }catch( Exception e ){
                LogWriter.writeLog( "Exception " + e + " trying to save remove object." );
                System.out.println(e + " trying to save remove object." );
            }
        }

    }*/

    /**
     * @param optimiseDrawing The optimiseDrawing to set.
     */
    public void setOptimiseDrawing(boolean optimiseDrawing) {
        this.optimiseDrawing = optimiseDrawing;
    }

    public void setScalingValues(double cropX, double cropH, float scaling) {

        this.cropX=cropX;
        this.cropH=cropH;
        this.scaling=scaling;

    }


    public boolean isImageCached(int pageNumber) {

         if(rawKey==null)
        return objectStoreRef.isImageCached(pageNumber+"_HIRES_"+(currentItem+1));
        else
        return objectStoreRef.isImageCached(pageNumber+"_HIRES_"+(currentItem+1)+"_"+rawKey);
        //return false;
    }

    //<start-13>
    /**
     * rebuild serialised version
     *
     * NOT PART OF API and subject to change (DO NOT USE)
     * @param fonts 
     *
     */
    public DynamicVectorRenderer(byte[] stream, Map fonts){

    	//isRunningOnRemoteClient=true;
        //we use Cannoo to turn our stream back into a DynamicVectorRenderer
        try{
        	this.fonts = fonts;

            ByteArrayInputStream bis=new ByteArrayInputStream(stream);

            //read version and throw error is not correct version
            int version=bis.read();
            if(version!=1)
                throw new PdfException("Unknown version in serialised object "+version);

            int isHires=bis.read(); //0=no,1=yes
            if(isHires==1)
            	useHiResImageForDisplay=true;
            else
            	useHiResImageForDisplay=false;
            	
            pageNumber=bis.read();
            
            x_coord=(float[]) restoreFromStream(bis);
            y_coord=(float[]) restoreFromStream(bis);

            //read in arrays - opposite of serializeToByteArray();
            //we may need to throw an exception to allow for errors

            text_color = (Vector_Object) restoreFromStream(bis);

            textFillType = (Vector_Int) restoreFromStream(bis);

            //stroke_color = (Vector_Object) restoreFromStream(bis);
            stroke_color = new Vector_Object();
            stroke_color.restoreFromStream(bis);
            
            //fill_color=(Vector_Object) restoreFromStream(bis);
            fill_color = new Vector_Object();
            fill_color.restoreFromStream(bis);
            
            stroke = new Vector_Object();
            stroke.restoreFromStream(bis);

            pageObjects = new Vector_Object();
            pageObjects.restoreFromStream(bis);

            javaObjects=(Vector_Object) restoreFromStream(bis);

            shapeType = (Vector_Int) restoreFromStream(bis);

            af1 = (Vector_Double) restoreFromStream(bis);

            af2 = (Vector_Double) restoreFromStream(bis);

            af3 = (Vector_Double) restoreFromStream(bis);

            af4 = (Vector_Double) restoreFromStream(bis);

            clips = new Vector_Shape();
            clips.restoreFromStream(bis);

            objectType = (Vector_Int) restoreFromStream(bis);

            opacity=(Vector_Float) restoreFromStream(bis);

            TRvalues = (Vector_Int) restoreFromStream(bis);

            fs = (Vector_Int) restoreFromStream(bis);
            lw = (Vector_Int) restoreFromStream(bis);

            int fontCount=((Integer) restoreFromStream(bis)).intValue();
            for(int ii=0;ii<fontCount;ii++){

            	Object key=restoreFromStream(bis);
            	Object glyphs=restoreFromStream(bis);
            	fonts.put(key,glyphs);
            }
           
            int alteredFontCount=((Integer) restoreFromStream(bis)).intValue();
            for(int ii=0;ii<alteredFontCount;ii++){

            	Object key=restoreFromStream(bis);

				PdfJavaGlyphs updatedFont=(PdfJavaGlyphs) fonts.get(key);

				updatedFont.setDisplayValues((Map) restoreFromStream(bis));
            	updatedFont.setCharGlyphs((Map) restoreFromStream(bis));
				updatedFont.setEmbeddedEncs((Map) restoreFromStream(bis));
            	
            }

            bis.close();

        }catch(Exception e){
        	//JOptionPane.showMessageDialog(null, "exception deserializing in DVR "+e);
            e.printStackTrace();
        }

    }

    /**
     * turn object into byte[] so we can move across
     * this way should be much faster than the stadard Java serialise.
     *
     * NOT PART OF API and subject to change (DO NOT USE)
     *
     * @throws IOException
     */
    public byte[] serializeToByteArray(Set fontsAlreadyOnClient) throws IOException{

        ByteArrayOutputStream bos=new ByteArrayOutputStream();

        //add a version so we can flag later changes
        bos.write(1);
        
        //flag hires
        //0=no,1=yes
        if(useHiResImageForDisplay)
        	 bos.write(1);
        else
        	 bos.write(0);
        
        //save page
        bos.write(pageNumber);

        //the WeakHashMaps are local caches - we ignore

        //we do not copy across hires images

        //we need to copy these in order

        //if we write a count for each we can read the count back and know how many objects
        //to read back

        //write these values first
        //pageNumber;
        //objectStoreRef;
        //isPrinting;
        
        text_color.trim();
        stroke_color.trim();
        fill_color.trim();
        stroke.trim();
        pageObjects.trim();
        javaObjects.trim();
        stroke.trim();
        pageObjects.trim();
        javaObjects.trim();
        shapeType.trim();
        af1.trim();
        af2.trim();
        af3.trim();
        af4.trim();
        clips.trim();
        objectType.trim();
        if(opacity!=null)
        opacity.trim();
        if(TRvalues!=null)
        TRvalues.trim();
        
        if(fs!=null)
            fs.trim();
        
        if(lw!=null)
            lw.trim();
        
        writeToStream(bos,x_coord,"x_coord");
        writeToStream(bos,y_coord,"y_coord");
        writeToStream(bos,text_color,"text_color");
        writeToStream(bos,textFillType,"textFillType");
        //writeToStream(bos,stroke_color,"stroke_color");
        stroke_color.writeToStream(bos);
        //writeToStream(bos,fill_color,"fill_color");
        fill_color.writeToStream(bos);
        
        int start = bos.size();
        stroke.writeToStream(bos);
        int end = bos.size();
        
        if(debugStreams)
        	System.out.println("stroke = "+((end-start)));
        
        start = end;
        pageObjects.writeToStream(bos);
        end = bos.size();
        
        if(debugStreams)
        	System.out.println("pageObjects = "+((end-start)));
        
        writeToStream(bos,javaObjects,"javaObjects");
        writeToStream(bos,shapeType,"shapeType");
        
        writeToStream(bos,af1,"af1");
        writeToStream(bos,af2,"af2");
        writeToStream(bos,af3,"af3");
        writeToStream(bos,af4,"af4");
        
        start = bos.size();
        clips.writeToStream(bos);
        end = bos.size();
        
        if(debugStreams)
        	System.out.println("clips = "+((end-start)));
        
        writeToStream(bos,objectType,"objectType");
        writeToStream(bos,opacity,"opacity");
        writeToStream(bos,TRvalues,"TRvalues");
        
        writeToStream(bos,fs,"fs");
        writeToStream(bos,lw,"lw");

        int fontCount=0,updateCount=0;
        Map fontsAlreadySent=new HashMap();
        Map newFontsToSend=new HashMap();
        
        for (Iterator iter = fontsUsed.keySet().iterator(); iter.hasNext();) {
			Object fontUsed = iter.next();
			if(!fontsAlreadyOnClient.contains(fontUsed)){
				fontCount++;
				newFontsToSend.put(fontUsed, "x");
			}else{
				updateCount++;
				fontsAlreadySent.put(fontUsed, "x");
			}
		}
        
        /**
         * new fonts
         */
        writeToStream(bos,new Integer(fontCount),"new Integer(fontCount)");

		Iterator keys=newFontsToSend.keySet().iterator();
        while(keys.hasNext()){
        	Object key=keys.next();
        	
        	if(debugStreams)
        		System.out.println("sending font = "+key);
        	
        	writeToStream(bos,key,"key");
        	writeToStream(bos,fonts.get(key),"font");
        	
        	fontsAlreadyOnClient.add(key);
        }
        
        /**
         * new data on existing fonts
         */
        /**
         * new fonts
         */
        writeToStream(bos,new Integer(updateCount),"new Integer(existingfontCount)");

		keys=fontsAlreadySent.keySet().iterator();
        while(keys.hasNext()){
        	Object key=keys.next();
        	
        	if(debugStreams)
        		System.out.println("sending font = "+key);
        	
        	writeToStream(bos,key,"key");
        	PdfJavaGlyphs aa = (PdfJavaGlyphs) fonts.get(key);
        	writeToStream(bos,aa.getDisplayValues(),"display");
        	writeToStream(bos,aa.getCharGlyphs() ,"char");
        	writeToStream(bos,aa.getEmbeddedEncs() ,"emb");
        	
        }
     
        bos.close();
        
        fontsUsed.clear();
        
        if(debugStreams)
        	System.out.println("total = "+bos.size());
        
        return bos.toByteArray();
    }

    /**
     * generic method to return a serilized object from an InputStream
     *
     * NOT PART OF API and subject to change (DO NOT USE)
     *
     * @param bis - ByteArrayInputStream containing serilized object
     * @return - deserilized object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object restoreFromStream(ByteArrayInputStream bis) throws IOException, ClassNotFoundException{

        //turn back into object
        ObjectInput os=new ObjectInputStream(bis);

        return os.readObject();
    }

    /**
     * generic method to serilized an object to an OutputStream
     *
     * NOT PART OF API and subject to change (DO NOT USE)
     *
     * @param bos - ByteArrayOutputStream to serilize to
     * @param obj - object to serilize
     * @param string2 
     * @throws IOException
     */
    public void writeToStream(ByteArrayOutputStream bos, Object obj, String string2) throws IOException{
    	int start = bos.size();
    	
        ObjectOutput os=new ObjectOutputStream(bos);

        os.writeObject(obj);
        
        int end = bos.size();
        
        if(debugStreams)
        	System.out.println(string2+" = "+((end-start)));
        
        os.close();
    }
    
    /**
     * for font if we are generatign glyph on first render
     */
    public void checkFontSaved(Object glyph, String name, PdfFont currentFontData) {
    	
    	//save glyph at start
    	/**now text*/
        pageObjects.addElement(glyph);
        objectType.addElement(MARKER);

        currentItem++;
        
    	
    	//if(currentFontData.isFontSubsetted())
    	//	fontsAlreadySent.remove(name);
    	
        if(fontsUsed.get(name)==null || currentFontData.isFontSubsetted()){
            fonts.put(name,currentFontData.getGlyphData());
            fontsUsed.put(name,"x");
    }
    }
    //  <end-13>
    
	public boolean hasObjectsBehind(float[][] CTM) {
		
		boolean hasObject=false;
		
		double x=CTM[2][0];
		double y=CTM[2][1];
		double w=CTM[0][0];
		if(w==0)
			w=CTM[0][1];
		double h=CTM[1][1];
		if(h==0)
			h=CTM[1][0];
		
		Rectangle[] areas=this.areas.get();
		int count=areas.length;
		for(int i=0;i<count;i++){
			if(areas[i]!=null && areas[i].intersects(new Rectangle((int)x,(int)y,(int)w,(int)h))){
				
				i=count;
				hasObject=true;
			}
		}
		
		return hasObject;
	}



	public void setObjectStoreRef(ObjectStore objectStoreRef) {
		this.objectStoreRef = objectStoreRef;
	}

    /**
     * use by type3 fonts to differentiate images in local store
     */
    public void setType3Glyph(String pKey) {
        this.rawKey=pKey;

        isType3Font=true;
        
    }

    /**
     * return copy of image correct way round
     * (not part of API - used by Storypad)
     */
    public BufferedImage getLoresImage(String imageName) {

        //updise down
        int idx=imageName.indexOf("-");
        if(idx!=-1)
        imageName=imageName.substring(idx+1,imageName.length());

        Object id=imageID.get(imageName);

        if(id==null)
            return null;
        else {
            BufferedImage source= (BufferedImage) pageObjects.elementAt(((Integer)id).intValue());


            //turn upside down
            AffineTransform image_at2 =new AffineTransform();
            image_at2.scale(1,-1);
            image_at2.translate(0,-source.getHeight());

            AffineTransformOp invert3= new AffineTransformOp(image_at2,  ColorSpaces.hints);

            BufferedImage image =new BufferedImage(source.getWidth(),source.getHeight(),source.getType());

            invert3.filter(source,image);

            return image;
        }

    }
}

