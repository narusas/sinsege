/*
* ===========================================
* Java Pdf Extraction Decoding Access Library
* ===========================================
*
* Project Info:  http://www.jpedal.org
* Project Lead:  Mark Stephens (mark@idrsolutions.com)
*
* (C) Copyright 2003, IDRsolutions and Contributors.
*
* This file is part of JPedal
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
* PdfStreamDecoder.java
* ---------------
* (C) Copyright 2002, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
* 
*/
package org.jpedal.parser;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;

import java.awt.image.Raster;

import java.awt.image.WritableRaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.jpedal.PdfDecoder;
import org.jpedal.function.Function;
import org.jpedal.external.ImageHandler;

import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.PdfData;
import org.jpedal.objects.StoryData;
import org.jpedal.objects.PdfImageData;

import org.jpedal.color.ColorSpaces;
import org.jpedal.color.ColorspaceDecoder;
import org.jpedal.color.DeviceCMYKColorSpace;
import org.jpedal.color.DeviceGrayColorSpace;
import org.jpedal.color.DeviceRGBColorSpace;
import org.jpedal.color.GenericColorSpace;
import org.jpedal.color.PdfPaint;
import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfFontException;

import org.jpedal.fonts.*;
import org.jpedal.fonts.glyph.GlyphFactory;
import org.jpedal.fonts.glyph.MarkerGlyph;
import org.jpedal.fonts.glyph.PdfGlyph;
import org.jpedal.fonts.glyph.PdfJavaGlyphs;
import org.jpedal.fonts.glyph.T3Size;
import org.jpedal.fonts.glyph.UnrendererGlyph;

import org.jpedal.gui.ShowGUIMessage;
import org.jpedal.images.ImageTransformer;
import org.jpedal.images.ImageTransformerDouble;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.ObjectStore;

import org.jpedal.io.PdfObjectReader;
import org.jpedal.io.StatusBar;

import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.PageLines;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.PdfShape;
import org.jpedal.objects.TextState;

//<start-adobe>
import org.jpedal.objects.acroforms.ConvertToString;
import org.jpedal.objects.structuredtext.StructuredContentHandler;
//<end-adobe>

import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.utils.Fonts;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Matrix;
import org.jpedal.utils.Strip;
import org.jpedal.utils.repositories.Vector_Object;


/**
 * Contains the code which 'parses' the commands in
 * the stream and extracts the data (images and text).
 * Users should not need to call it.
 */
public class PdfStreamDecoder implements StreamDecoder{

    public static boolean useShading=true;

    private boolean TRFlagged=false;

    ImageHandler customImageHandler=null;

    boolean rejectSuperimposedImages=false;


	//save font info and generate glyph on first render
    private boolean generateGlyphOnRender=false;

	final static int[] prefixes={60,40}; //important that [ comes before (  '<'=60 '('=40
    final static int[] suffixes={62,41}; //'>'=62 ')'=41

    /**flag to show if image transparent*/
    boolean isMask=true;

    /**flag used to show if printing worked*/
    private boolean pageSuccessful=true;

    /**Any printer errors*/
    private String pageErrorMessages="";

    /** last text stream decoded*/
    private StringBuffer textData;

    /**interactive display*/
    private StatusBar statusBar=null;

    /**get percentage of gap required to create a space 1=100% */
    public static float currentThreshold = 0.6f; //referenced in Decoder if changed
    public static Map currentThresholdValues; //referenced in Decoder if changed

    /**holds mappings for colorspaces (ie Cs6 = CalRGB)*/
    private Map rawColorspaceValues = new HashMap();

    /**holds colorspace objects*/
    private Map colorspaceObjects=new HashMap();

    /**flag to engage text printing*/
    private int textPrint=0;

    /**holds mappings for shading */
    private Map rawShadingValues = new HashMap();

    //last Trm incase of multple Tj commands
    protected boolean multipleTJs =false;

    /**provide access to pdf file objects*/
    private PdfObjectReader currentPdfFile;

    /**flag to show if image is for clip*/
    private boolean isClip = false;

    /**thousand as a value*/
    final private static float THOUSAND=1000;

    /**flag to show content is being rendered*/
    private boolean renderText=false;

    /**flag to show content is being rendered*/
    private boolean renderImages=false;

    /**copy of flag to tell program whether to create
     * (and possibly update) screen display
     */
    private boolean renderPage = false;

    /**flag to show raw images extracted*/
    private boolean rawImagesExtracted=true;

    /**flag to show raw images extracted*/
    private boolean finalImagesExtracted=true;

    /**flag to allow Xform metadata to be extracted*/
    private boolean xFormMetadata=true;

    /**flag to show raw images extracted*/
    private boolean clippedImagesExtracted=true;

    private boolean   markedContentExtracted=false;

    //<start-adobe>
    private StructuredContentHandler contentHandler;
    //<end-adobe>

    /**flag to show text is being extracted*/
    private boolean textExtracted=true;

    /**flags to show we need colour data as well*/
    private boolean textColorExtracted=false,colorExtracted=false;

    /**start of ascii escape char*/
    private static final String[] hex={"&#0;","&#1;","&#2;","&#3;",
"&#4;","&#5;","&#6;","&#7;","&#8;","&#9;","&#10;","&#11;",
"&#12;","&#13;","&#14;","&#15;","&#16;","&#17;","&#18;","&#19;",
"&#20;","&#21;","&#22;","&#23;","&#24;","&#25;","&#26;",
"&#27;","&#28;","&#29;","&#30;","&#31;"};

    /**store text data and can be passed out to other classes*/
    private PdfData pdfData = new PdfData();

    /**current XML tag*/
    private String font_as_string="";

    /**stroke colorspace*/
    protected GenericColorSpace strokeColorSpace=new DeviceRGBColorSpace();

    /**nonstroke colorspace*/
    protected GenericColorSpace nonstrokeColorSpace=new DeviceRGBColorSpace();

    /**flag to show if we physical generate a scaled version of the
     * images extracted*/
    private  boolean createScaledVersion = true;

    /**store image data extracted from pdf*/
    private PdfImageData pdfImages=new PdfImageData();

    /**token counter so we can work out order objects drawn*/
    private int tokenNumber = 0;

    /**flag to show if page content or a substream*/
    private boolean isPageContent = true;

    /**flag to show if stack setup*/
    private boolean isStackInitialised=false;

    /**stack for graphics states*/
    private Vector_Object graphicsStateStack;

    /**stack for graphics states*/
    private Vector_Object strokeColorStateStack;

    /**stack for graphics states*/
    private Vector_Object nonstrokeColorStateStack;

    /**stack for graphics state clips*/
    private Vector_Object clipStack;

    /**stack for graphics states*/
    private Vector_Object textStateStack;

     /**horizontal and vertical lines on page*/
    private PageLines pageLines=new PageLines();

    /**current shape object being drawn note we pass in handle on pageLines*/
    private PdfShape currentDrawShape=new PdfShape();

    /**maximum ops*/
    private final int MAXOPS=50;

    /**current op*/
    private int currentOp=0;

    /**actual numbe rof operands read*/
    private int operandCount=0;

    /**lookup table for operands on commands*/
    private String[] operand= new String[MAXOPS];
    private int[] opStart= new int[MAXOPS];
    private int[] opEnd= new int[MAXOPS];

    /**flag to show type of move command being executed*/
    protected int moveCommand = 0; //0=t*, 1=Tj, 2=TD

    /**current font for text*/
    private String currentFont = "";

    /**name of current image in pdf*/
    private String currentImage = "";

    /**store id values used for XObject*/
    private Map currentXObjectValues = new Hashtable(),localXObjects=new Hashtable();

    private Map currentPatternValues = new Hashtable();

    /**gap between characters*/
    protected float charSpacing = 0;

    /**current graphics state - updated and copied as file decode*/
    protected GraphicsState currentGraphicsState=new GraphicsState();

    /**current text state - updated and copied as file decode*/
    protected TextState currentTextState = new TextState();

    /**used to store font information from pdf and font functionality*/
    protected PdfFont currentFontData;

    /**fonts*/
    private Map fonts=new Hashtable();

    /**length of current text fragment*/
    private int textLength = 0;

    /**flag to show we use hi-res images to draw onscreen*/
    private boolean useHiResImageForDisplay=false;

    /**co-ords (x1,y1 is top left corner)*/
    private float x1, y1, x2, y2;

    /** holds page information used in grouping*/
    private PdfPageData pageData = new PdfPageData();

    protected DynamicVectorRenderer current;


    protected GlyphFactory factory=null;
    /**

    /**page number*/
    private int pageNum;

    /**list of fonts used for display*/
    private static String fontsInFile;

    /**xml color tag to show color*/
    private String currentColor=GenericColorSpace.cb+"C='1' M='1' Y='1' K='1'>";

    /**max width of type3 font*/
    private int T3maxWidth,T3maxHeight;

    /**allows uasge of old approximation for height (Deprecated so please advise IDRsolutions if being used)*/
    private boolean legacyTextMode=false;

    private boolean extractRawCMYK=false;

    /**flag to show we are drawing directly to g2 and not storing to render later*/
    protected boolean renderDirectly;

    /**hook onto g2 if we render directly*/
    protected Graphics2D g2;

    /**clip if we render directly*/
    private Shape defaultClip;

    /**flag to show embedded fonts present*/
    private boolean hasEmbeddedFonts=false;

    /**flag to show if images are included in datastream*/
    private boolean includeImagesInData=false;

    /**track font size*/
    protected int lastFontSize=-1;

    /**shows if t3 glyph uses internal colour or current colour*/
    public boolean ignoreColors=false;

    /**constant value for ellipsis*/
    private static String ellipsis=""+(char)Integer.parseInt("2026",16);

    /**flag to show being used for printing onto G2*/
    private boolean isPrinting;

    private String fileName="";

    private ObjectStore objectStoreStreamRef;

    /**tracks name of xform in case we need to save*/
    private String lastFormID=null;

    private int pageH;

    private int formLevel=0;

    /**signal to decoding loop to exit*/
    private boolean requestExit=false,exited=false;

    /**holds parameters for graphics states*/
    private Map gs_state = new HashMap();

    private boolean imagesProcessedFully;

    /**flag to show we keep raw stream from objects*/
    private boolean keepRaw=false;

    public static boolean runningStoryPad=false;

    private static boolean testFontSupport;

    private Map scalings=new HashMap();
	private boolean extractedContent;
    private boolean isType3Font;

    /** hold values used in TR transfer function*/
    private Map TRfunctionsCache=new HashMap();
    private Map imposedImages;


	/**
     * NOT PART OF API
     *
     * to replace ObjectStore.getCurrentFileName()
     *
     * <b>for internal use</b>
     */
    public void setName(String name){

        if(name!=null){
            this.fileName=name.toLowerCase();

            /**check no separators*/
            int sep=fileName.lastIndexOf(47); // '/'=47
            if(sep!=-1)
                fileName=fileName.substring(sep+1);
            sep=fileName.lastIndexOf(92); // '\\'=92
            if(sep!=-1)
                fileName=fileName.substring(sep+1);
            sep=fileName.lastIndexOf(46); // "."=46
            if(sep!=-1)
                fileName=fileName.substring(0,sep);
        }
    }

    /** should be called after constructor or other methods may not work
     * <p>Also initialises DynamicVectorRenderer*/
    public void setStore(ObjectStore newObjectRef){
        objectStoreStreamRef = newObjectRef;

        current=new DynamicVectorRenderer(this.pageNum,objectStoreStreamRef,isPrinting);
        current.setHiResImageForDisplayMode(useHiResImageForDisplay);

    }


            /**/

    public PdfStreamDecoder(PdfObjectReader currentPdfFile,boolean useHires,boolean isType3Font){

        StandardFonts.checkLoaded(StandardFonts.STD);

		 //lazy init on t1
		if(factory==null)
           factory=new org.jpedal.fonts.glyph.T1GlyphFactory();

		this.currentPdfFile=currentPdfFile;
        this.useHiResImageForDisplay=useHires;

        this.isType3Font=isType3Font;

        String operlapValue=System.getProperty("org.jpedal.rejectsuperimposedimages");
        this.rejectSuperimposedImages=(operlapValue!=null && operlapValue.toLowerCase().indexOf("true")!=-1);
        
    }

    public PdfStreamDecoder(PdfObjectReader currentPdfFile){

        StandardFonts.checkLoaded(StandardFonts.STD);

		 //lazy init on t1
		if(factory==null)
           factory=new org.jpedal.fonts.glyph.T1GlyphFactory();

		this.currentPdfFile=currentPdfFile;

        String operlapValue=System.getProperty("org.jpedal.rejectsuperimposedimages");
        this.rejectSuperimposedImages=(operlapValue!=null && operlapValue.toLowerCase().indexOf("true")!=-1);

    }
    
    public PdfStreamDecoder(){

             StandardFonts.checkLoaded(StandardFonts.STD);

			 //lazy init on t1
			if(factory==null)
                factory=new org.jpedal.fonts.glyph.T1GlyphFactory();

        String operlapValue=System.getProperty("org.jpedal.rejectsuperimposedimages");
        rejectSuperimposedImages=(operlapValue!=null && operlapValue.toLowerCase().indexOf("true")!=-1);

    }

    public void print(Graphics2D g2,AffineTransform scaling,boolean showImageable){

            if(showImageable)
                current.setBackgroundColor(null);
            current.paint(g2,null,scaling,null,false);
    }

    /**
     * create new StreamDecoder to create screen display with hires images
     */
    public PdfStreamDecoder(boolean useHiResImageForDisplay) {

            StandardFonts.checkLoaded(StandardFonts.STD);

			//lazy init on t1
		if(factory==null)
            factory=new org.jpedal.fonts.glyph.T1GlyphFactory();


        String operlapValue=System.getProperty("org.jpedal.rejectsuperimposedimages");
        rejectSuperimposedImages=(operlapValue!=null && operlapValue.toLowerCase().indexOf("true")!=-1);

    }

    /**used internally to allow for colored streams*/
    public void setDefaultColors(PdfPaint strokeCol, PdfPaint nonstrokeCol) {

        this.strokeColorSpace.setColor(strokeCol);
        this.nonstrokeColorSpace.setColor(nonstrokeCol);
        currentGraphicsState.setStrokeColor(strokeCol);
        currentGraphicsState.setNonstrokeColor(nonstrokeCol);
    }

    /**method ensures images rendered as ARGB rather than RGB. Used internally
     * to ensure prints correctly on some files. Not recommended for
     * external usage.
     */
    public void optimiseDisplayForPrinting(){
        isPrinting=true;
    }

    /**return the data*/
    public PdfData getText(){
        return  pdfData;
    }

    /**return the data*/
    public PdfImageData getImages(){
        return  pdfImages;
    }

    final private BufferedImage processImageXObject(
        String image_name,
        Map values,
        boolean createScaledVersion,
        byte[] objectData) throws PdfException,PdfFontException {

        LogWriter.writeMethod("{processImageXObject}"+values,0);

        boolean imageMask = false;
        String decodeArray = "";

        BufferedImage image=null;

        //add filename to make it unique
        image_name = fileName+ "-" + image_name;

        //get values for Image from metadata
        String image_filter = currentPdfFile.getValue((String) values.get("Filter"));
        String raw_colorspace_name =(String) values.get("ColorSpace");

        if(raw_colorspace_name!=null)
        raw_colorspace_name=Strip.removeArrayDeleminators(raw_colorspace_name);

        int width = Integer.parseInt((String) values.get("Width"));
        int height = Integer.parseInt((String) values.get("Height"));

        int depth=1;
        String rawDepth=(String) values.get("BitsPerComponent");
        if(rawDepth!=null)
        depth = Integer.parseInt(rawDepth);

        String value = (String) values.get("Decode");
        if (value != null)
            decodeArray = value;

        value = (String) values.get("ImageMask");
        if (value != null){
            imageMask = Boolean.valueOf(value).booleanValue();
            isMask=true;
        }else
            isMask=false;

        //handle colour information
        GenericColorSpace decodeColorData=new GenericColorSpace();

        if(raw_colorspace_name!=null){

            if(raw_colorspace_name.startsWith("/")){ //allow for direct value like /DeviceRGB
                decodeColorData=ColorspaceDecoder.getColorSpaceInstance(isPrinting,currentGraphicsState.CTM,raw_colorspace_name,null,currentPdfFile);
            }else{
                Map colorspaceValues =(currentPdfFile.getSubDictionary(raw_colorspace_name));
                raw_colorspace_name=(String)colorspaceValues.get("rawValue");

                decodeColorData=ColorspaceDecoder.getColorSpaceInstance(isPrinting,currentGraphicsState.CTM,raw_colorspace_name,colorspaceValues,currentPdfFile);
            }
        }

        //set any intent
        value = (String) values.get("Intent");
        decodeColorData.setIntent(value);

        //set value for user to see
        if (raw_colorspace_name == null)
        raw_colorspace_name="/RGB (Default)";

        //tell user and log
        LogWriter.writeLog("Processing XObject: "+ image_name+ " width="+ width+ " Height="+ height+ " Depth="+ depth+ " filter="
                + image_filter+ " colorspace="+ raw_colorspace_name);

        /**
         * allow user to process image
         */
        if(customImageHandler!=null){
                values.put("stream",objectData);
                image=customImageHandler.processImageData(values,currentGraphicsState);
        }

        //extract and process the image
        if((customImageHandler==null)||((image==null)&& !customImageHandler.alwaysIgnoreGenericHandler()))
        image=processImage(decodeColorData,raw_colorspace_name,
                objectData,
                image_name,
                width,
                height,
                depth,
                image_filter,
                decodeArray,
                imageMask,
                createScaledVersion,values);

        return image;



    }

    /**
     * read in the image and process and save raw image
     */
    final private BufferedImage processImage(GenericColorSpace decodeColorData,
                                             String colorspaceName,
                                             byte[] data,String name,
                                             int w,int h,int d,String filter,
                                             String decodeArray,boolean imageMask,
                                             boolean createScaledVersion, Map decodeParams) throws PdfException {

        if (LogWriter.debug)
            LogWriter.writeMethod("{process_image}");

        boolean removed=false;

        BufferedImage image = null;
        String type = "jpg";

        int colorspaceID=decodeColorData.getID();

        /**setup any imageMask*/
        byte[] maskCol =new byte[4];
        if ((imageMask == true))
        getMaskColor(maskCol);

        /**handle any decode array*/
        if(decodeArray.length() == 0){
        }else if((filter!=null)&&((filter.indexOf("JPXDecode")!=-1)||(filter.indexOf("DCT")!=-1))){ //don't apply on jpegs
        }else
            applyDecodeArray(data, d, decodeArray,colorspaceID);


        if ((imageMask == true)) {/** create an image from the raw data*/

                //try to keep as binary if possible
                boolean hasObjectBehind=current.hasObjectsBehind(currentGraphicsState.CTM);

                //see if black and back object
                if(maskCol[0]==0 && maskCol[1]==0 && maskCol[2]==0 && !hasObjectBehind && !this.isType3Font){
                    WritableRaster raster =Raster.createPackedRaster(new DataBufferByte(data, data.length), w, h, 1, null);
                    image =new BufferedImage(w,h,BufferedImage.TYPE_BYTE_BINARY);
                    image.setData(raster);
                }else{
                    //if(hasObjectBehind){
                    //image=ColorSpaceConvertor.convertToARGB(image);
                        byte[] index={maskCol[0],maskCol[1],maskCol[2],(byte)255,(byte)255,(byte)255};
                        image = convertIndexedToFlat(decodeColorData.getID(),1,w, h, data, index, index.length,true);
    //ShowGUIMessage.showGUIMessage("x",image, "x");
                    /**}else{
                        WritableRaster raster =Raster.createPackedRaster(new DataBufferByte(data, data.length), w, h, d, null);
                        image = new BufferedImage(new IndexColorModel(d, 1, maskCol, 0, false), raster, false, null);
                    }*/
                }

        }else if (filter == null) { //handle no filters

            //save out image
            LogWriter.writeLog("Image "+ name+ " "+ w+ "W * "+ h+ "H with No Compression at BPC "+ d+" and Colorspace="+colorspaceName);

            image =makeImage(decodeColorData,w,h,d,data);

        } else if (filter.indexOf("DCT") != -1) { //handle JPEGS

            LogWriter.writeLog("JPeg Image "+ name+ " "+ w+ "W * "+ h+ "H");

            /**
             * get image data,convert to BufferedImage from JPEG & save out
             */
            if(colorspaceID== ColorSpaces.DeviceCMYK){
                if(extractRawCMYK){
                    LogWriter.writeLog("Raw CMYK image " + name + " saved.");
                    if(!objectStoreStreamRef.saveRawCMYKImage(data, name))
                        addPageFailureMessage("Problem saving Raw CMYK image "+name);
                }
            }

            //separation, renderer
            try{
                image=decodeColorData.JPEGToRGBImage(data,w,h,decodeArray);

                removed=ColorSpaceConvertor.wasRemoved;
            }catch(Exception e){
                addPageFailureMessage("Problem converting "+name+" to JPEG");
                e.printStackTrace();
                image=null;
            }
            type = "jpg";
        }else if(filter.indexOf("JPXDecode")!=-1){

            LogWriter.writeLog("JPeg 2000 Image "+ name+ " "+ w+ "W * "+ h+ "H");

//          try {
//                java.io.FileOutputStream fos=new java.io.FileOutputStream("/Users/markee/test.jpeg");
//                fos.write(data);
//                fos.close();
//
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }

            image = decodeColorData.JPEG2000ToRGBImage(data);

            type = "jpg";

        } else { //handle other types
            LogWriter.writeLog(name+ " "+ w+ "W * "+ h+ "H  with "+ filter+ " BPC="+d+" CS="+colorspaceName);

            image =makeImage(decodeColorData,w,h,d,data);

            //choose type on basis of size and avoid ICC as they seem to crash the Java class
            if ((d == 8)| (nonstrokeColorSpace.getID()== ColorSpaces.DeviceRGB)| (nonstrokeColorSpace.getID()== ColorSpaces.ICC))
                type = "jpg";
        }

        if (image != null) {

            /**handle any soft mask*/

            String mask = (String) decodeParams.get("Mask");
            Object smask=decodeParams.get("SMask");

            /**handle any soft mask*/
            if(smask!=null){

                //read in Smask
                Map maskValues=null;
                String ref="";
                if(smask instanceof Map)
                    maskValues=(Map) smask;
                else{
                    ref=(String) smask;
                    maskValues = currentPdfFile.readObject(ref,false, null);
                }



                /**read the stream*/
                byte[] objectData =currentPdfFile.readStream(maskValues,ref,true,true,keepRaw);

                if(objectData!=null){
                    //process the image and save raw version
                    BufferedImage smaskImage =processImageXObject(name,maskValues,false,objectData);


                    String matte=(String) decodeParams.get("Matte");
                    if(matte!=null){
                    }

                    //apply mask
                    image=applySmask(image,smaskImage,(String) maskValues.get("ColorSpace"));

                }
                /**handle any mask*/
            }else if(mask!=null){

                    //see if object or colors
                    if(mask.indexOf("[")!=-1){

                        //get values
                        mask=Strip.removeArrayDeleminators(mask);

                        int colorComponents=decodeColorData.getColorComponentCount();

                        //allow for indexed
                        byte[] index=decodeColorData.getIndexedMap();
                        if(index!=null){
                            StringTokenizer rawValues=new StringTokenizer(mask);
                            StringBuffer newValue=new StringBuffer();
                            int itemCount=rawValues.countTokens();
                            while(rawValues.hasMoreTokens()){
                                int indexValue=Integer.parseInt(rawValues.nextToken());
                                for(int i=0;i<colorComponents;i++){
                                    newValue.append(String.valueOf((index[(indexValue*colorComponents)+i] & 255)));
                                    newValue.append(' ');
                                }
                            }
                            mask=newValue.toString();
                        }

                        StringTokenizer colValues=new StringTokenizer(mask);

                        //work out number of values invloved
                        int numberColors=colValues.countTokens()/colorComponents;

                        //put values in  the table
                        int[][] matches=new int[numberColors][colorComponents];
                        for(int currentCol=0;currentCol<numberColors;currentCol++){
                            for(int comp=0;comp<colorComponents;comp++){
                                int value=Integer.parseInt(colValues.nextToken());

                                if(colorComponents==1){
                                    matches[currentCol][0]=value;
                                    matches[currentCol][1]=value;
                                    matches[currentCol][2]=value;
                                }else if(colorComponents==3){
                                    matches[currentCol][comp]=value;
                                }else{
                                }
                            }
                        }

                        image = convertPixelsToTransparent(image, colorComponents, numberColors, matches);

                    }else if(mask.endsWith(" R")){
                        Map maskValues = currentPdfFile.readObject(mask,false, null);

                        /**read the stream*/
                        byte[] objectData =currentPdfFile.readStream(maskValues,mask,true,true,keepRaw);

                       if(objectData!=null)
                           image=overlayImage(image,objectData);
                       
                    }else{
                    }
            }

            //simulate overPrint
            if(colorspaceID== ColorSpaces.DeviceCMYK && currentGraphicsState.getNonStrokeOP() && currentGraphicsState.getOPM()==1.0f){

                image=simulateOP(image);

                if(image==null)
                return null;
            }

            data = null;

            if (image.getSampleModel().getNumBands() == 1)
                type = "tif";

            if( (isPageContent)&&((clippedImagesExtracted)||(finalImagesExtracted)|| (rawImagesExtracted))){

                //save the raw image or blank if demo or encryption enabled
                if (currentPdfFile.isExtractionAllowed()){

                    if(PdfDecoder.inDemo){
                        
                        int imageType=image.getType();
                        if(imageType==0)
                            imageType=BufferedImage.TYPE_INT_RGB;
                        BufferedImage newImage=new BufferedImage(image.getWidth(),image.getHeight(),imageType);
                        Graphics2D g2= newImage.createGraphics();
                        g2.drawImage(image,null,null);

                        int x=image.getWidth();
                        int y=image.getHeight();

                        //add demo cross
                        g2.setColor(Color.red);
                        g2.drawLine(0, 0, x,y);
                        g2.drawLine(0, y, x, 0);

                        objectStoreStreamRef.saveStoredImage(name,addBackgroundToMask(newImage),false,createScaledVersion,type);
                    }else{
                        if(!PdfStreamDecoder.runningStoryPad)
                        //    objectStoreStreamRef.saveStoredImage(name,image,false,createScaledVersion,type);
                        //else
                            objectStoreStreamRef.saveStoredImage(name,addBackgroundToMask(image),false,createScaledVersion,type);
                    }
                }else{

                    /**create copy and scale if required*/
                    if(PdfDecoder.dpi!=72){

                        int imageType=image.getType();
                        if(imageType==0)
                            imageType=BufferedImage.TYPE_INT_RGB;
                        BufferedImage newImage=new BufferedImage(image.getWidth(),image.getHeight(),imageType);
                        newImage.createGraphics().drawImage(image,null,null);
                        float s=((float)PdfDecoder.dpi)/72;
                        AffineTransform scale = new AffineTransform();
                        scale.scale(s, s);
                        AffineTransformOp scalingOp =new AffineTransformOp(scale, ColorSpaces.hints);
                        newImage =scalingOp.filter(newImage, null);
                        objectStoreStreamRef.saveStoredImage(name,addBackgroundToMask(newImage),false,createScaledVersion,type);

                    }else{
                        objectStoreStreamRef.saveStoredImage(name,addBackgroundToMask(image),false,createScaledVersion,type);
                    }
                }

            }
		}

        if(image == null && !removed){
            imagesProcessedFully = false;
        }

        //apply any tranfer function
        String TR=currentGraphicsState.getTR();

        if(TR!=null){

            if(TR.endsWith("R")){ //array of values

                image=applyTR(image,TR);

            }else{
            }

        }

        return image;
    }

    /**
     * add MASK to image
     */
    private BufferedImage overlayImage(BufferedImage image, byte[] maskData) {
        Raster ras=image.getRaster();
        image=ColorSpaceConvertor.convertToARGB(image);

        int width=image.getWidth();

        //workout y offset (remember needs to be factor of 8)
        int lineBytes=width;
        if((lineBytes & 7)!=0)
        lineBytes=lineBytes+8;
        lineBytes=lineBytes>>3;

        int bytes=0;

        //int[] transparentPixel={255,0,0,0};
        int[] transparentPixel={0,0,255,0};
        int[] bit={0,2,4,8,16,32,64,128};
        
        for(int y=0;y<image.getHeight();y++){

            for(int x=0;x<width;x++){

                int xOffset=(x>>3);
                
                byte b=maskData[bytes+xOffset];
                boolean isTransparent=(b & bit[x & 7])==0;

                //if it matched replace and move on
                if(!isTransparent)
                    image.getRaster().setPixel(x,y,transparentPixel);

            }

            bytes=bytes+lineBytes;

        }
        return image;
    }

    /**
     * add MASK to image
     */
    private BufferedImage convertPixelsToTransparent(BufferedImage image, int colorComponents, int numberColors, int[][] matches) {
        Raster ras=image.getRaster();
        image=ColorSpaceConvertor.convertToARGB(image);

        int[] transparentPixel={255,0,0,0};
        for(int y=0;y<image.getHeight();y++){
            for(int x=0;x<image.getWidth();x++){

                int[] values=new int[4];
                //get raw color data
                ras.getPixel(x,y,values);

                //see if we have a match
                boolean noMatch=true;
                for(int currentCol=0;currentCol<numberColors;currentCol++){

                    //assume it matches
                    noMatch=false;

                    //test assumption
                        for(int comp=0;comp<colorComponents;comp++){
                            if(matches[currentCol][comp]!=values[comp]){
                                comp=colorComponents;
                                noMatch=true;
                            }
                        }

                        //if it matched replace and move on
                        if(!noMatch){
                            image.getRaster().setPixel(x,y,transparentPixel);
                            currentCol=numberColors;
                        }
                }
            }
        }
        return image;
    }

    /**
     * CMYK overprint mode
     */
    private BufferedImage simulateOP(BufferedImage image) {
        Raster ras=image.getRaster();
        image=ColorSpaceConvertor.convertToARGB(image);

        boolean hasNoTransparent=false;
        int[] transparentPixel={255,0,0,0};
        for(int y=0;y<image.getHeight();y++){
            for(int x=0;x<image.getWidth();x++){

                int[] values=new int[4];
                //get raw color data
                ras.getPixel(x,y,values);

                //see if black
                boolean transparent=values[1]==0 && values[2]==0 && values[3]==0;

                //if it matched replace and move on
                if(transparent)
                    image.getRaster().setPixel(x,y,transparentPixel);
                else
                    hasNoTransparent=true;

            }
        }

        if(hasNoTransparent)
            return image;
        else
            return null;
    }

    /**
     * apply TR
     */
    private BufferedImage applyTR(BufferedImage image,String TR) {

        /**
         * get TR function first
         **/

        Function[] f;//see if cached or read in when first needed

        Object TRfunctions=TRfunctionsCache.get(TR);

        if(TRfunctions!=null) //use cached if stored
            f =(Function[]) TRfunctions;
        else{
            f =new Function[4];

            int count=0;

            //get functions
            StringTokenizer objValues=new StringTokenizer(TR,"R");


            while(objValues.hasMoreTokens()){

                String ref=(objValues.nextToken()+"R").trim();
                Map TRfunction=currentPdfFile.readObject(ref,false,null);

                if(TRfunction!=null){

                    int functionType =Integer.parseInt((String) TRfunction.get("FunctionType"));
                    float[] functionDomain=null;
                    float[] range=null;
                    String value =(String) TRfunction.get("Domain");
                    if (value != null) {
                        StringTokenizer matrixValues = new StringTokenizer(value, "[] ");
                        functionDomain = new float[matrixValues.countTokens()];
                        int i = 0;
                        while (matrixValues.hasMoreTokens()) {
                            functionDomain[i] = Float.parseFloat(matrixValues.nextToken());
                            i++;
                        }
                    }

                    value = (String) TRfunction.get("Range");
                    if (value != null) {
                        StringTokenizer matrixValues = new StringTokenizer(value, "[] ");
                        range = new float[matrixValues.countTokens()];
                        int i = 0;
                        while (matrixValues.hasMoreTokens()) {
                            range[i] = Float.parseFloat(matrixValues.nextToken());
                            i++;
                        }
                    }

                    byte[] stream=null;
                    if(ref!=null)
                        stream=currentPdfFile.readStream(ref,true);
                    else{
                        stream=(byte[]) TRfunction.get("DecodedStream");
                    }

                    /** setup the translation function */
                    f[count] = Function.getInstance(
                            stream,
                            TRfunction,
                            functionDomain,
                            range,
                            functionType,
                            currentPdfFile);
                }

                count++;

            }

            //cache incase used again
            TRfunctionsCache.put(TR, f);

        }

        /**
         * apply colour transform
         */
        Raster ras=image.getRaster();
        //image=ColorSpaceConvertor.convertToARGB(image);

        for(int y=0;y<image.getHeight();y++){
            for(int x=0;x<image.getWidth();x++){

                int[] values=new int[4];

                //get raw color data
                ras.getPixel(x,y,values);

                for(int a=0;a<3;a++){
                    float[] raw={values[a]/255f};
                    String[] processed=f[a].compute(raw,null);
                    values[a]= (int) (255*Float.parseFloat(processed[0]));
                }

                image.getRaster().setPixel(x,y,values);
            }
        }

        return image;

    }


    /**apply soft mask*/
    private BufferedImage applySmask(BufferedImage image, BufferedImage smask,String colorspace) {

        int[] gray={0};
        int[] val={0,0,0,0};
        int[] transparentPixel={0,0,0,0};

        //fix for Smask encoded with DCTDecode
        if((colorspace!=null)&&(colorspace.equals("/DeviceGray")) ){
            smask=ColorSpaceConvertor.convertColorspace(smask,BufferedImage.TYPE_BYTE_GRAY);
            val=gray;
        }

        Raster ras=image.getRaster();
        Raster mask=smask.getRaster();
        image=ColorSpaceConvertor.convertToARGB(image);

        int colorComponents=smask.getColorModel().getNumComponents();

        for(int y=0;y<image.getHeight();y++){
            for(int x=0;x<image.getWidth();x++){

                int[] values=new int[colorComponents];
                //get raw color data
                mask.getPixel(x,y,values);

                //see if we have a match
                boolean noMatch=true;

                //assume it matches
                noMatch=false;

                //test assumption
                if(colorComponents==1){  //hack to filter out DCTDecode stream
                  if(values[0]>127)
                  noMatch=true;
                }else{

                    for(int comp=0;comp<colorComponents;comp++){
                        if(values[comp]!=val[comp]){
                            comp=colorComponents;
                            noMatch=true;
                        }
                    }
                }

                //if it matched replace and move on
                if(!noMatch)
                    image.getRaster().setPixel(x,y,transparentPixel);

            }
        }
        return image;
    }

    /**
     * @param maskCol
     */
    private void getMaskColor(byte[] maskCol) {
        int foreground =nonstrokeColorSpace.getColor().getRGB();
        maskCol[0]=(byte) ((foreground>>16) & 0xFF);
        maskCol[1]=(byte) ((foreground>>8) & 0xFF);
        maskCol[2]=(byte) ((foreground) & 0xFF);
    }

    /**
     * apply DecodeArray
     */
    private void applyDecodeArray(byte[] data, int d, String decodeArray,int type) {

        /**get the values*/
        StringTokenizer components =new StringTokenizer(decodeArray, "[ ]");

        int count = components.countTokens(),i=0;
        float[] array = new float[count];

        int maxValue=0;
        while (components.hasMoreTokens()) {
            array[i] = Float.parseFloat(components.nextToken());
            if(maxValue<array[i])
                maxValue=(int) array[i];
            i++;
        }

        /**
         * see if will not change output
         * and ignore if unnecessary
         */
        boolean isIdentify=true; //assume true and disprove
        int compCount=array.length;

        for(int comp=0;comp<compCount;comp=comp+2){
            if((array[comp]!=0.0f)||((array[comp+1]!=1.0f)&&(array[comp+1]!=255.0f))){
                isIdentify=false;
                comp=compCount;
            }
        }

        if(isIdentify)
        return ;

        /**
         * handle rgb
         */
        if((d==8 && maxValue>1)&&(type==ColorSpaces.DeviceRGB || type==ColorSpaces.CalRGB || type==ColorSpaces.DeviceCMYK)){

            System.out.println("x");
            int j=0;

            for(int ii=0;ii<data.length;ii++){
                int currentByte=(data[ii] & 0xff);
                if(currentByte<array[j])
                    currentByte=(int) array[j];
                else if(currentByte>array[j+1])
                    currentByte=(int)array[j+1];

                j=j+2;
                if(j==array.length)
                j=0;
                data[ii]=(byte)currentByte;
            }
        }else{
            /**
             * apply array
             *
             * Assumes black and white or gray colorspace
             * */
            maxValue = (d<< 1);
            int divisor = maxValue - 1;

            for(int ii=0;ii<data.length;ii++){
            byte currentByte=data[ii];

                int dd=0;
                int newByte=0;
                int min=0,max=1;
                for(int bits=7;bits>-1;bits--){
                int current=(currentByte >> bits) & 0001;

                    current =(int)(array[min]+ (current* ((array[max] - array[min])/ (divisor))));

                    /**check in range and set*/
                    if (current > maxValue)
                        current = maxValue;
                    if (current < 0)
                        current = 0;

                    current=((current & 0001)<<bits);

                    newByte=newByte+current;

                    //rotate around array
                    dd=dd+2;

                    if(dd==count){
                        dd=0;
                        min=0;
                        max=1;
                    }else{
                        min=min+2;
                        max=max+2;
                    }
                }

                data[ii]=(byte)newByte;

            }
        }

    }



    public void init(boolean isPageContent,boolean renderPage,int renderMode, int extractionMode,PdfPageData currentPageData,int pageNumber,DynamicVectorRenderer current,PdfObjectReader currentPdfFile,Map globalRes,Map resValue) throws PdfException{

        if(current!=null)
        this.current=current;
        this.pageNum=pageNumber;
        this.pageData=currentPageData;
        this.isPageContent=isPageContent;
        this.currentPdfFile=currentPdfFile;

        //setup height
        this.pageH = pageData.getMediaBoxHeight(pageNumber);

        //set width
        pageLines.setMaxWidth(pageData.getCropBoxWidth(pageNumber)-pageData.getCropBoxX(pageNumber),
                pageData.getCropBoxHeight(pageNumber)-pageData.getCropBoxY(pageNumber));

        textExtracted=(extractionMode & PdfDecoder.TEXT)==PdfDecoder.TEXT;

        this.renderPage=renderPage;

        renderText=renderPage &&(renderMode & PdfDecoder.RENDERTEXT) == PdfDecoder.RENDERTEXT;
        renderImages=renderPage &&(renderMode & PdfDecoder.RENDERIMAGES )== PdfDecoder.RENDERIMAGES;

        extractRawCMYK=clippedImagesExtracted=(extractionMode &PdfDecoder.CMYKIMAGES)==PdfDecoder.CMYKIMAGES;
        rawImagesExtracted=(extractionMode & PdfDecoder.RAWIMAGES) == PdfDecoder.RAWIMAGES;
        clippedImagesExtracted=(extractionMode &PdfDecoder.CLIPPEDIMAGES)==PdfDecoder.CLIPPEDIMAGES;
        finalImagesExtracted=(extractionMode & PdfDecoder.FINALIMAGES) == PdfDecoder.FINALIMAGES;
        xFormMetadata=(extractionMode & PdfDecoder.XFORMMETADATA) == PdfDecoder.XFORMMETADATA;

        textColorExtracted=(extractionMode & PdfDecoder.TEXTCOLOR) == PdfDecoder.TEXTCOLOR;

        colorExtracted=(extractionMode & PdfDecoder.COLOR) == PdfDecoder.COLOR;

        /**init text extraction*/
        if((legacyTextMode)&&(textExtracted)){
            if(PdfDecoder.currentHeightLookupData==null)
                PdfDecoder.currentHeightLookupData = new org.jpedal.fonts.PdfHeightTable();
        }
        //flag if colour info being extracted
        if(textColorExtracted)
            pdfData.enableTextColorDataExtraction();

        if ((finalImagesExtracted) | (renderImages))
            createScaledVersion = true;
        else
            createScaledVersion = false;

        currentFontData=new PdfFont(currentPdfFile);

        //delete
        strokeColorSpace = new DeviceRGBColorSpace();
        nonstrokeColorSpace = new DeviceRGBColorSpace();

        if (globalRes != null)
            readResources(true,globalRes,true);

        /**read the resources for the page*/
        if (resValue != null)
            readResources(true,resValue,true);

    }

    //////////////////////////////////////////////////////
    /**
     * turn raw data into a BufferedImage
     */
    final private BufferedImage makeImage(GenericColorSpace decodeColorData,int w,int h,int d,byte[] data) {

        LogWriter.writeMethod("{makeImage}",0);

        ColorSpace cs=decodeColorData.getColorSpace();
        int ID=decodeColorData.getID();
        int comp = cs.getNumComponents();
        BufferedImage image = null;
        byte[] index =decodeColorData.getIndexedMap();

        /** create an image from the raw data*/
        DataBuffer db = new DataBufferByte(data, data.length);

        if (((index != null))) { //indexed images

            /**convert index to rgb if CMYK or ICC*/
            if ((comp == 4))
                comp=3;

            index=decodeColorData.convertIndexToRGB(index);

            //workout size and check in range
            int size =decodeColorData.getIndexSize()+1;

            //allow for half bytes (ie bootitng.pdf)
            if((d==4)&&(size>16))
                size=16;
            
//                WritableRaster raster =Raster.createPackedRaster(db, w, h, d, null);

//                ColorModel cm=new IndexColorModel(d, size, index, 0, false);
//                image = new BufferedImage(cm, raster, false, null);

            	image = convertIndexedToFlat(decodeColorData.getID(),d,w, h, data, index, size,false);

        } else if (d == 1) { //bitmaps next

        	WritableRaster raster =Raster.createPackedRaster(db, w, h, d, null);
            image =new BufferedImage(w,h,BufferedImage.TYPE_BYTE_BINARY);
            image.setData(raster);

        }else if((ID==ColorSpaces.Separation)|(ID==ColorSpaces.DeviceN)){
            LogWriter.writeLog("Converting Separation/DeviceN colorspace to sRGB ");
            image=decodeColorData.dataToRGB(data,w,h);

        }else if(ID==6){
            LogWriter.writeLog("Converting lab colorspace to sRGB ");
            image=decodeColorData.dataToRGB(data,w,h);

            //direct images
        } else if (comp == 4) { //handle CMYK or ICC

            //ICC (note CMYK uses ICC so check which type and check enough data)
            if((ID==3)) //&((w*h*4)==data.length)) /**CMYK*/
                image =ColorSpaceConvertor.algorithmicConvertCMYKImageToRGB(data,w,h);
            else
                image =ColorSpaceConvertor.convertFromICCCMYK(w,h,data, cs);

            //ShowGUIMessage.showGUIMessage("y",image,"y");
        } else if (comp == 3) {

            //work out from size what sort of image data we have
            if (w * h == data.length) {
                if (d == 8 && index!=null){
                	image = convertIndexedToFlat(decodeColorData.getID(),d,w, h, data, index, index.length,false);

                    //image =new BufferedImage(w,h,BufferedImage.TYPE_BYTE_INDEXED);

                //WritableRaster raster =Raster.createPackedRaster(db,w,h,d,null);
                //image.setData(raster);
                }else{
                     int[] bands = {0};
                    image =new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
                    Raster raster =Raster.createInterleavedRaster(db,w,h,w * 1,1,bands,null);
                    image.setData(raster);

                }
            } else{

                int[] bands = {0,1,2};
                image =new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
                Raster raster =Raster.createInterleavedRaster(db,w,h,w * 3,3,bands,null);
                image.setData(raster);

            }
        } else if ((comp == 1)&&((d == 8)||(d==4))) {

            //expand out 4 bit raster as does not appear to be easy way
            if(d==4){
                int origSize=data.length;
                int newSize=w*h;

                byte[] newData=new byte[newSize];
                byte rawByte;
                int ptr=0,currentLine=0;
                boolean oddValues=((w & 1)==1);
                for(int ii=0;ii<origSize;ii++){
                    rawByte=data[ii];

                    currentLine=currentLine+2;
                    newData[ptr]=(byte) (rawByte & 240);
                    ptr++;

                    if((oddValues)&&(currentLine>w)){ //ignore second value if odd as just packing
                        currentLine=0;
                    }else{
                        newData[ptr]=(byte) ((rawByte & 15) <<4);
                        ptr++;
                    }

                    if(ptr==newSize)
                        ii=origSize;
                }
                db = new DataBufferByte(newData, newData.length);

            }

            image =new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
            int[] bands ={0};
            Raster raster =Raster.createInterleavedRaster(db,w,h,w,1,bands,null);
            image.setData(raster);

        } else
            LogWriter.writeLog("Image "+ cs.getType()+ " not currently supported with components "+ comp);

        //convert type 0 to rgb (as do work with other ops)
        //if (image.getType() == 0)
        	//image = ColorSpaceConvertor.convertToRGB(image);

        return image;
    }

    private BufferedImage convertIndexedToFlat(int ID,int d,int w, int h, byte[] data, byte[] index, int size,boolean isARGB) {

    	BufferedImage image;
    	DataBuffer db;
    	int[] bandsRGB = {0,1,2};
    	int[] bandsARGB = {0,1,2,3};
    	int[] bands;
    	int components=3,id1,pt=0;
    	
    	if(isARGB){
    		bands=bandsARGB;
    		components=4;
    	}else
    		bands=bandsRGB;

    	int length=(w*h*components);

    	byte[] newData=new byte[length];

    	if(d==8){

    		for(int ii=0;ii<data.length-1;ii++){

    			int id=(data[ii] & 0xff)*3;

    			if(pt>=length)
    				break;
    			
    			newData[pt++]=index[id];
    			newData[pt++]=index[id+1];
    			newData[pt++]=index[id+2];
    			
    			if(isARGB){
    				if(id==0)
    					newData[pt++]=(byte)255;
    				else
    					newData[pt++]=0;
    			}
    		}
    	}else if(d==4){

    		int[] shift={4,0};
    		int widthReached=0;

    		for(int ii=0;ii<data.length;ii++){

    			for(int samples=0;samples<2;samples++){

    				id1=((data[ii]>>shift[samples]) & 15)*3;

    				if(pt>=length)
    					break;
    				
    				newData[pt++]=index[id1];
    				newData[pt++]=index[id1+1];
    				newData[pt++]=index[id1+2];

    				if(isARGB){
    					if(id1==0)
    						newData[pt++]=(byte)0;
    					else
    						newData[pt++]=0;
    				}

    				//ignore filler bits
    				widthReached++;
    				if(widthReached==w){
    					widthReached=0;
    					samples=8;
    				}
    			}
    		}
    	}else if(d==1){

    		//work through the bytes
    		int widthReached=0;
    		for(int ii=0;ii<data.length-1;ii++){

    			for(int bits=0;bits<8;bits++){

    				//int id=((data[ii] & (1<<bits)>>bits))*3;
    				int id=((data[ii]>>(7-bits)) & 1)*3;

    				if(pt>=length)
    					break;

    				newData[pt++]=index[id];
    				newData[pt++]=index[id+1];
    				newData[pt++]=index[id+2];
    				
    				if(isARGB){
    					if(id==0)
    						newData[pt++]=(byte)255;
    					else
    						newData[pt++]=0;
    				}
    				//ignore filler bits
    				widthReached++;
    				if(widthReached==w){
    					widthReached=0;
    					bits=8;
    				}
    			}
    		}
    	}else if(d==2){

    		//work through the bytes
    		for(int ii=0;ii<data.length-1;ii++){

    			for(int bits=0;bits<8;bits=bits+2){

    				int id=((data[ii] & (3<<bits)>>bits))*3;

    				if(isARGB){
    					if(id==0)
    						newData[pt++]=(byte)255;
    					else
    						newData[pt++]=0;
    				}

    				if(pt>=length)
    					break;
    				newData[pt++]=index[id];
    				newData[pt++]=index[id+1];
    				newData[pt++]=index[id+2];
    			}
    		}    
    	}else{

    	}

    	/**create the image*/
    	db = new DataBufferByte(newData, newData.length);
    	if(isARGB)
    		image =new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
    	else
    		image =new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);

    	WritableRaster raster =Raster.createInterleavedRaster(db,w,h,w * components,components,bands,null);
    	image.setData(raster);

    	return image;
    }
    /////////////////////////////////////////////////////////////////////////
    /**
     * read the XObjects and process the images.
     */
    final private void processXObjects(Map rawValues,boolean isGlobal)  {

        LogWriter.writeMethod("{processXObjects}", 0);

        String X_name = "";
        Iterator fontList = rawValues.keySet().iterator();

        while (fontList.hasNext()) {

            X_name = (String) fontList.next();
            Object xObjectTester = rawValues.get(X_name);
            if(xObjectTester instanceof Map){
                //test if Map and use
                /**
                 * put data into array for later retrieval
                 */
                if(isGlobal)
                    currentXObjectValues.put(X_name, xObjectTester);
                else
                    localXObjects.put(X_name, xObjectTester);

//				X_object = (String) xObjectTester;
            }else{
                /**
                 * put data into array for later retrieval
                 */
                if(isGlobal)
                    currentXObjectValues.put(X_name, xObjectTester);
                else
                    localXObjects.put(X_name, xObjectTester);
            }
        }
    }

    /**
     * read the Pattern objects
     */
    final private void processPatterns(Map rawValues)  {

        LogWriter.writeMethod("{processPatterns}", 0);

        String name = "", object = "";
        Iterator list = rawValues.keySet().iterator();

        while (list.hasNext()) {

            name = (String) list.next();
            object = (String) rawValues.get(name);

            /**
             * put data into array for later retrieval
             */
            currentPatternValues.put(name, object);


        }
    }

    final private void readFonts(boolean resetList,Map rawValues,boolean canBeSubstituted) throws PdfException {

        LogWriter.writeMethod("{readFonts}", 0);

        Map values=new HashMap();
        String font_id = "";

        if(resetList)
        fontsInFile="";

        //remove any pageNumber which can occur on forms
        //and causes problems below
        rawValues.remove("PageNumber");

        Iterator fontList = rawValues.keySet().iterator();

        while (fontList.hasNext()) {

            font_id = (String) fontList.next();

            //get values allowing for direct
            Object objectID=rawValues.get(font_id);

            if(objectID instanceof String){
                if(currentPdfFile!=null)
                        values = currentPdfFile.readObject((String)rawValues.get(font_id),false, null);
                else
                    values=rawValues;
            }else if(objectID instanceof Map)
                values=(Map) objectID;

            PdfFont currentFontData=createFont(values,font_id,canBeSubstituted);

            if(currentFontData!=null)
            fonts.put(font_id,currentFontData);

        }
    }

    private PdfFont createFont(Map values,String font_id,boolean canBeSubstituted) throws PdfException{

        String subtype = "";

        String type = (String) values.get("Type");

        String subFont=null;

        /**allow for no actual object*/
        if(type==null)
            return null;

        /**
         * make sure it is a font
         */
        if (type.equals("/Font")) {
            //deal with types
            subtype = (String) values.get("Subtype");
            Map descFontValues=null;

            //see if font stream embedded inside
            boolean hasEmbeddedFont=false;

            if(PdfDecoder.fontSubstitutionTable!=null){

                Object raw=null;
                //allow for CID font
                Object value=values.get("DescendantFonts");
                if(value!=null){
                    Map CIDvalues = readDescendantFontObject(value, values);
                    raw=CIDvalues.get("FontDescriptor");
                }else
                    raw=values.get("FontDescriptor");

                Map descValues=new HashMap();

                if(raw instanceof Map)
                    descValues=(Map) raw;
                else if(raw instanceof String)
                    descValues=currentPdfFile.readObject((String)raw,false,null);
           
                if(descValues!=null)
                    hasEmbeddedFont=((descValues.get("FontFile")!=null)||(descValues.get("FontFile2")!=null)||(descValues.get("FontFile3")!=null));
            }


            /**handle any font remapping*/
            if((PdfDecoder.fontSubstitutionTable!=null)&&(!hasEmbeddedFont) && canBeSubstituted){

                String rawFont=(String) values.get("BaseFont");

                if(rawFont==null)
                    rawFont=(String) values.get("Name");

                if(rawFont==null)
                    rawFont=font_id;
                else if(rawFont.startsWith("/"))
                        rawFont=rawFont.substring(1);

                String baseFont=(rawFont).toLowerCase();

                if(baseFont.startsWith("/"))
                    baseFont=baseFont.substring(1);

                //strip any postscript
                int pointer=baseFont.indexOf("+");
                if(pointer==6)
                    baseFont=baseFont.substring(7);

                String testFont=baseFont.toLowerCase();

                String newSubtype=(String)PdfDecoder.fontSubstitutionTable.get(testFont);

                //check aliases
                if(newSubtype==null){
                //check for mapping
                    HashMap fontsMapped=new HashMap();
                    String nextFont;
                    while(true){
                        nextFont=(String) PdfDecoder.fontSubstitutionAliasTable.get(testFont);

                        if(nextFont==null)
                            break;

                        testFont=nextFont;

                        if(fontsMapped.containsKey(testFont)){
                            StringBuffer errorMessage=new StringBuffer("[PDF] Circular font mapping for fonts");
                            Iterator i=fontsMapped.keySet().iterator();
                            while(i.hasNext()){
                                errorMessage.append(' ');
                                errorMessage.append(i.next());
                            }
                            throw new PdfException(errorMessage.toString());
                        }
                        fontsMapped.put(nextFont,"x");

                    }

                    if(testFont!=null)
                    newSubtype=(String)PdfDecoder.fontSubstitutionTable.get(testFont);
                }

                if(newSubtype!=null){
                    subtype=newSubtype;
                    subFont=(String) PdfDecoder.fontSubstitutionLocation.get(testFont);

                }else if(PdfDecoder.enforceFontSubstitution){
                    LogWriter.writeLog("baseFont="+baseFont+" fonts added= "+PdfDecoder.fontSubstitutionTable);
                    throw new PdfFontException("No substitute Font found for "+baseFont);
                }
             }

            /**/
            //workout type
            int fontType=0;

            String descFont=null;

            boolean isCID=false;

            if (subtype.equals("/Type1")|| subtype.equals("/Type1C")|| subtype.equals("/MMType1")) {
                fontType=StandardFonts.TYPE1;
            }else if (subtype.equals("/TrueType")){
                fontType=StandardFonts.TRUETYPE;
            }else if (subtype.equals("/Type3")){
                fontType=StandardFonts.TYPE3;
            }else if (subtype.equals("/Type0")) {

                isCID=true;

                /**check the descendant font exists*/
                Object value=values.get("DescendantFonts");

                if (value == null){
                    LogWriter.writeLog("[PDF] No Descender font for CID font");
                }else{

                    descFontValues = readDescendantFontObject(value, values);

                    subtype=(String) descFontValues.get("Subtype");

                    if (subtype.equals("/CIDFontType0")){
                        fontType=StandardFonts.CIDTYPE0;
                    }else{
                        fontType=StandardFonts.CIDTYPE2;
                    }
                }
            } else{
                LogWriter.writeLog("Font type " + subtype + " not supported");
                currentFontData=new PdfFont(currentPdfFile);
            }

            try{
			    currentFontData=FontFactory.createFont(currentGraphicsState,subtype,fontType,currentPdfFile,subFont);

                /**set an alternative to Lucida*/
                if(PdfDecoder.defaultFont!=null)
                    currentFontData.setDefaultDisplayFont(PdfDecoder.defaultFont);

                currentFontData.createFont(values, font_id,renderPage,descFontValues, objectStoreStreamRef);

            }catch(Exception e){
                LogWriter.writeLog("[PDF] Problem "+e+" reading Font  type "+subtype+" in "+fileName);
                addPageFailureMessage("Problem "+e+" reading Font type "+subtype+" in "+fileName);
            }

            if(fontsInFile==null)
            fontsInFile="";

            if(currentFontData.isFontSubstituted()){
                fontsInFile=font_id+"  "+currentFontData.getFontName()+"  "+subtype.substring(1)+"  Substituted ("+subFont+" "+subtype+")\n"+fontsInFile;
            }else if(currentFontData.isFontEmbedded){
                hasEmbeddedFonts=true;
                if(currentFontData.is1C())
                    fontsInFile=font_id+"  "+currentFontData.getFontName()+" Type1C  Embedded\n"+fontsInFile;
                else
                    fontsInFile=font_id+"  "+currentFontData.getFontName()+"  "+subtype.substring(1)+"  Embedded\n"+fontsInFile;
            }else
                fontsInFile=font_id+"  "+currentFontData.getFontName()+"  "+subtype.substring(1)+"\n"+fontsInFile;

        } else
            LogWriter.writeLog("Not a font object");

        return currentFontData;
    }

    private Map readDescendantFontObject(Object value, Map values) {

        String descFont;
        Map descFontValues;

        if(value instanceof String){
            descFont = (String) values.get("DescendantFonts");

            /**allow for indirect value*/
            if(descFont.startsWith("["))
                descFont= Strip.removeArrayDeleminators(descFont);

            /** read the  descendant font*/
            descFontValues =currentPdfFile.readObject(descFont,false, null);

        }else
            descFontValues=(Map) ((Map)value).get("rawValue");

        String direct=(String) descFontValues.get("rawValue");
        if(direct!=null)
            descFontValues =currentPdfFile.readObject(Strip.removeArrayDeleminators(direct),false, null);

        return descFontValues;
    }

    /**
         * read page header and extract page metadata
     * @throws PdfException
         */
    public final void readResources(boolean resetList,Map resource_values,boolean isGlobal) throws PdfException {

    	LogWriter.writeMethod("{readResources}", 0);

    	Map values;

    	//decode colourspaces
    	Object obj=resource_values.get("ColorSpace");
    	if(obj!=null){
    		values = currentPdfFile.getSubDictionary(obj);
    		if (values != null){
    			readColorSpaceSettings(values);
    		}
    	}

    	//decode fonts
    	obj=resource_values.get("Font");
    	if(obj!=null){
    		values = currentPdfFile.getSubDictionary(obj);
    		if (values != null)
    			readFonts(resetList,values,true);
    	}

    	//XObjects if required
    	obj=resource_values.get("XObject");
    	if(obj!=null){
    		values = currentPdfFile.getSubDictionary(obj);
    		if ((values != null))
    			processXObjects(values,isGlobal);
    	}

    	//patterns and shading if required
    	try{
    		//decode shading
    		obj=resource_values.get("Shading");
    		if(obj!=null){
    			values = currentPdfFile.getSubDictionary(obj);
    			if (values != null)
    				readShadingSettings(values);
    		}

    		//read any pattern settings
    		obj=resource_values.get("Pattern");
    		if(obj!=null){
    			values = currentPdfFile.getSubDictionary(obj);
    			if ((values != null))
    				processPatterns(values);
    		}
    	}catch(Exception e){
    	}

    	obj=resource_values.get("ExtGState");
    	if(obj!=null){
    		try{
    			//Graphics State
    			values =currentPdfFile.getSubDictionary(obj);
    			if (values != null)
    				readGraphicsState(values);
    		}catch(Exception e){
    		}
    	}

    	/**
        //decode procs
        value = (String) resource_values.get("ProcSet");
        if (value != null)
            readProc(value);
    	 */
    }

    /**
     * read page header and extract page metadata when stored in a Map as opposed to
     * objects referenced in Map (ie inside AP stream for form)
     */
public final void readResourcesForForm(Map resource_values) throws PdfFontException,PdfException {

    LogWriter.writeMethod("{readResourcesForForm}", 0);

    //decode colourspaces
    Map values =(Map) resource_values.get("ColorSpace");
    if (values != null){
        readColorSpaceSettings(values);
    }

    //decode fonts
    values = (Map) resource_values.get("Font");
    if (values != null)
        readFonts(false,values,false);

    //XObjects if required
    values = (Map) resource_values.get("XObject");
    if ((values != null))
        processXObjects(values,true);

    //patterns and shading if required
    try{
        //decode shading
        values = (Map) resource_values.get("Shading");
        if (values != null)
            readShadingSettings(values);

        //read any pattern settings
        values = (Map) resource_values.get("Pattern");
        if ((values != null))
            processPatterns(values);

    }catch(Exception e){
    }

    try{
        //Graphics State
        values =currentPdfFile.getSubDictionary(resource_values.get("ExtGState"));
        if (values != null)
            readGraphicsState(values);
    }catch(Exception e){
    }
    /**
    //decode procs
    value = (String) resource_values.get("ProcSet");
    if (value != null)
        readProc(value);
    */

}
    /**
     * examine and decode ColorSpace command
     */
    final private void readColorSpaceSettings(Map rawValues)  {

        LogWriter.writeMethod("{readColorSpaces}", 0);

        String col_id = "",currentRawValue="";
        Iterator colList = rawValues.keySet().iterator();

        while (colList.hasNext()) {

            col_id = (String) colList.next();
            currentRawValue=(String) rawValues.get(col_id);

            rawColorspaceValues.put(col_id,currentRawValue);

        }
    }

    private Object getObjectFromCache(Map objectCache,Map objectRefs,String id){

        //get value and regenerate if not cached in Map
        Object cachedObject =objectCache.get(id);
        if(cachedObject==null){
            String currentRawValue=(String) objectRefs.get(id);

            //get values if object or just just string
            if((currentRawValue!=null)&&(currentRawValue.endsWith(" R"))){
                cachedObject=currentPdfFile.readObject(currentRawValue,false, null);
            }else{
                cachedObject= currentRawValue;
            }

            objectCache.put(currentRawValue,cachedObject);
        }

        return cachedObject;
    }

    /**
     * examine and decode shading command
     */
    final private void readShadingSettings(Map rawValues)  {

        LogWriter.writeMethod("{readShadingSettings}", 0);

        String id = "";
        Object currentRawValue="";
        Iterator list = rawValues.keySet().iterator();

        while (list.hasNext()) {

            id = (String) list.next();
            currentRawValue=rawValues.get(id);

            Map rawShadingValue;

            //get values if object or just just string
            if((currentRawValue instanceof String)&&(currentRawValue.toString().endsWith(" R"))){
                rawShadingValue=currentPdfFile.readObject((String)currentRawValue,false, null);
            }else{
                rawShadingValue=(Map) currentRawValue;
            }



            /**currentPdfFile.flattenValuesInObject(true,false,rawShadingValue,shadingValue,null,null,null);
            Map shadingValue=new HashMap();
            rawShadingValue=shadingValue;
            /**/
            rawShadingValues.put(id,rawShadingValue);
            //System.out.println(rawShadingValue);
        }
    }

    /**
     *
     *  objects off the page, stitch into a stream and
     * decode and put into our data object. Could be altered
     * if you just want to read the stream
     * @throws PdfException
     */
    public final T3Size decodePageContent(String contents,int minX,int minY,GraphicsState newGS) throws PdfException{/* take out min's%%*/


		LogWriter.writeMethod("{decodePageContent "+contents+"}", 0);

        //check switched off
        requestExit=false;
        exited=false;
        imagesProcessedFully = true;

        if((!this.renderDirectly)&&(statusBar!=null))
            statusBar.percentageDone=0;

        if(newGS!=null)
            currentGraphicsState = newGS;
        else
            currentGraphicsState = new GraphicsState(minX,minY);/* take out min's%%*/


        //save for later
        if (renderPage == true){

            /**
             * check setup and throw exception if null
             */
            if(current==null)
                throw new PdfException("DynamicVectorRenderer not setup PdfStreamDecoder setStore(...) should be called");

            if(renderDirectly)
                current.renderClip(currentGraphicsState.getClippingShape(),null,defaultClip,g2);
            else
                current.drawClip(currentGraphicsState) ;
        }


        //get the binary data from the file
        byte[] b_data = readPageIntoStream(contents);

        //if page data found, turn it into a set of commands
        //and decode the stream of commands
        if (b_data.length > 0) {

            //reset graphics state for each page and flush queue
            //currentGraphicsState.resetCTM();
            decodeStreamIntoObjects(b_data);

        }

        if(requestExit){
            current.flush();
            exited=true;
        }

        /**fontHandle
        //lose font handles asap
        currentFontData.unsetUnscaledFont();
        currentFontData=null;
        this.releaseResources();
        fonts=null;
        */
        T3Size t3=new T3Size();
        t3.x = T3maxWidth;
        t3.y = T3maxHeight;
        return t3;

    }

    /**
     * decode the actual 'Postscript' stream into text and images by extracting
     * commands and decoing each.
     */
    public final void decodeStreamIntoObjects(byte[] characterStream) {

        LogWriter.writeMethod("{decodeStreamIntoObjects}", 0);

        final boolean debug=false;

        int count=prefixes.length;
        int start=0,end=0;
        int sLen=characterStream.length;

        if((!this.renderDirectly)&&(statusBar!=null)){
            statusBar.percentageDone=0;
            statusBar.resetStatus("stream");
        }

        if(requestExit){
            exited=true;
            return;
        }

        int streamSize=characterStream.length,charCount = streamSize;
        int initSize=255,dataPointer = 0,startCommand=0; //reset

        if(characterStream.length==0)
            return ;

        int current=0,nextChar=(int)characterStream[0],commandID=-1;

        /**
         * loop to read stream and decode
         */
        while (true) {

            if((!this.renderDirectly)&&(statusBar!=null))
                statusBar.percentageDone=(100*dataPointer)/streamSize;

            if(requestExit){
                exited=true;
                return;
            }

            current=nextChar;

            if(current==13 || current==10 || current==32){

                dataPointer++;

                while(true){ //read next valid char

                    if(dataPointer==charCount) //allow for end of stream
                        break;

                    current =(int)characterStream[dataPointer];

                    if((current!=13)&&(current!=10)&&(current!=32))
                        break;

                    dataPointer++;

                }
            }

            if(dataPointer==charCount) //allow for end of stream
                break;

            /**
             * read in value (note several options)
             */
            boolean matchFound=false;
            int type=0;

            if((current==60)&&(characterStream[dataPointer+1]==60)) //look for <<
                type=1;
            else if(current==91) //[
                type=2;
            else if((current>=97)&&(current<=122)) //lower case alphabetical a-z
                type=3;
            else if((current>=65)&&(current<=90)) //upper case alphabetical A-Z
                type=3;
            else if((current==39)||(current==34)) //not forgetting the non-alphabetical commands '\'-'\"'/*
                type=3;
            else if(current==32)
                type=4;
            else
                type=0;

            if(debug)
                System.out.println("Char="+current+" type="+type);

            if(type==3){ //option - its an aphabetical so may be command or operand values

                start=dataPointer;

                while(true){ //read next valid char

                    dataPointer++;
                    if((dataPointer)==sLen) //trap for end of stream
                        break;

                    current = characterStream[dataPointer];
                    //return,space,( / or [
                    if ((current == 13) || (current == 10) || (current == 32)|| (current == 40) || (current == 47)|| (current == 91))
                        break;

                }

                end=dataPointer-1;


                //move back if ends with / or [
                int endC=characterStream[end];
                if((endC==47)||(endC==91))
                    end--;

                //see if command
                commandID=-1;
                if(end-start<3){ //no command over 3 chars long
                    //convert token to int
                    int key=0,x=0;
                    for(int i2=end;i2>start-1;i2--){
                        key=key+(characterStream[i2]<<x);
                        x=x+8;
                    }
                    commandID=Cmd.getCommandID(key);
                }

                /**
                 * if command execute otherwise add to stack
                */
                if (commandID==-1) {

                    opStart[currentOp]=start;
                    opEnd[currentOp]=end;



                    currentOp++;
                    if (currentOp == this.MAXOPS)
                        currentOp = 0;
                    operandCount++;
                }else{


                    if(requestExit){
                        exited=true;
                        return;
                    }

                    try {
                        dataPointer = processToken(commandID,characterStream,startCommand, dataPointer);
                        startCommand=dataPointer;
                    } catch (Exception e) {

                        LogWriter.writeLog("[PDF] "+ e);
                        LogWriter.writeLog("Processing token >" + Cmd.getCommandAsString(commandID)
                                + "<>" + fileName+" <"+pageNum);

                    } catch (OutOfMemoryError ee) {
                        addPageFailureMessage("Memory error decoding token stream");
                        LogWriter.writeLog("[MEMORY] Memory error - trying to recover");
                        System.gc();
                    }

                    currentOp=0;
                    operandCount=0;
                }
            }else if(type!=4){

                start=dataPointer;

                //option  << values >>
                //option  [value] and [value (may have spaces and brackets)]
                if((type==1)||(type==2)){

                    boolean inStream=false;
                    matchFound=true;

                    int last=32;  // ' '=32

                    while(true){ //read rest of chars

                        if((last==92) &&(current==92)) //allow for \\  \\=92
                            last=120;  //'x'=120

                        else
                            last = current;

                        dataPointer++; //roll on counter

                        if(dataPointer==sLen) //allow for end of stream
                            break;

                        //read next valid char, converting CR to space
                        current = characterStream[dataPointer];
                        if((current==13)||(current==10))
                            current=32;

                        //exit at end
                        boolean isBreak=false;


                        if((current==62)&&(last==62)&&(type==1))  //'>'=62
                            isBreak=true;

                        if(type==2){
                            //stream flags
                            if((current==40)&&(last!=92)) 	//'('=40 '\\'=92
                                inStream=true;
                            else if((current==41)&&(last!=92))
                                inStream=false;

                            //exit at end
                            if ((inStream == false) && (current ==93)	//']'=93
                                    && (last != 92))
                                isBreak=true;
                        }

                        if(isBreak)
                            break;
                    }

                    end=dataPointer;
                }

                if(!matchFound){ //option 3 other braces

                    int last=32;
                    for(int startChars=0;startChars<count;startChars++){

                        if(current==prefixes[startChars]){
                            matchFound=true;

                            start=dataPointer;

                            int numOfPrefixs=0;//counts the brackets when inside a text stream
                            while(true){ //read rest of chars

                                if((last==92) &&(current==92)) //allow for \\ '\\'=92
                                    last=120; //'x'=120
                                else
                                    last = current;
                                dataPointer++; //roll on counter

                                if(dataPointer==sLen)
                                    break;
                                current = characterStream[dataPointer]; //read next valid char, converting CR to space
                                if((current==13)|(current==10))
                                    current=32;

                                if(current ==prefixes[startChars] && last!=92) // '\\'=92
                                    numOfPrefixs++;

                                if ((current == suffixes[startChars])&& (last != 92)){ //exit at end  '\\'=92
                                    if(numOfPrefixs==0)
                                        break;
                                    else{
                                        numOfPrefixs--;

                                    }
                                }
                            }
                            startChars=count; //exit loop after match
                        }
                    }
                    end=dataPointer;
                }

                //option 2 -its a value followed by a deliminator (CR,space,/)
                if(!matchFound){

                    if(debug)
                        System.out.println("Not type 2");

                    start=dataPointer;

                    while(true){ //read next valid char
                        dataPointer++;
                        if((dataPointer)==sLen) //trap for end of stream
                            break;

                        current = characterStream[dataPointer];
                        if ((current == 13) || (current == 10) || (current == 32) || (current == 40) || (current == 47) || (current == 91))
//							// '('=40	'/'=47  '['=91
                            break;

                    }

                    end=dataPointer;

                    if(debug)
                        System.out.println("end="+end);
                }

                if(debug)
                    System.out.println("stored start="+start+" end="+end);

                if(end<characterStream.length){
                    int next=(int)characterStream[end];
                    if(next==47 || next==91)
                        end--;
                }
                
                opStart[currentOp]=start;
                opEnd[currentOp]=end;



                currentOp++;
                if (currentOp == this.MAXOPS)
                    currentOp = 0;
                operandCount++;

            }

            //increment pointer
            if(dataPointer < charCount){

                nextChar=(int)characterStream[dataPointer];
                if(nextChar != 47 && nextChar != 40 && nextChar!= 91){
                    dataPointer++;
                    if(dataPointer<charCount)
                    nextChar=(int)characterStream[dataPointer];
                }
            }

            //break at end
            if ((charCount <= dataPointer))
                break;
        }
    }

    ////////////////////////////////////////////////////////////////////////
    final private void d1(float urX,float llX,float wX,float urY,float llY,float wY) {

        //flag to show we use text colour or colour in stream
        ignoreColors=true;

        /**/
        //not fully implemented
        //float urY = Float.parseFloat(generateOpAsString(0,characterStream));
        //float urX = Float.parseFloat(generateOpAsString(1,characterStream));
        //float llY = Float.parseFloat(generateOpAsString(2,characterStream));
        //float llX = Float.parseFloat(generateOpAsString(3,characterStream));
        //float wY = Float.parseFloat(generateOpAsString(4,characterStream));
        //float wX = Float.parseFloat(generateOpAsString(5,characterStream));
        /***/

        //this.minX=(int)llX;
        //this.minY=(int)llY;

        //currentGraphicsState = new GraphicsState(0,0);/*remove values on contrutor%%*/

        //setup image to draw on
        //current.init((int)(wX),(int)(urY-llY+1));

        //wH=urY;
        //wW=llX;

        T3maxWidth=(int)wX;
        if(wX==0)
            T3maxWidth=(int)(llX-urX);
        else
            T3maxWidth=(int)wX; //Float.parseFloat(generateOpAsString(5,characterStream));

        T3maxHeight=(int)wY;
        if(wY==0)
            T3maxHeight=(int)(urY-llY);
        else
            T3maxHeight=(int)wY; //Float.parseFloat(generateOpAsString(5,characterStream));

        /***/
    }
    ////////////////////////////////////////////////////////////////////////
    final private void d0(int w,int y) {

        //flag to show we use text colour or colour in stream
        ignoreColors=false;

        //float glyphX = Float.parseFloat((String) operand.elementAt(0));
        T3maxWidth=w;
        T3maxHeight=y;

        //setup image to draw on
        //current.init((int)glyphX,(int)glyphY);

    }
    ////////////////////////////////////////////////////////////////////////
    final private void TD(boolean isLowerCase,float x,float y) {

        relativeMove(x, y);

        if (!isLowerCase) { //set leading as well
            float TL = -y;
            currentTextState.setLeading(TL);
        }
        multipleTJs=false;

    }
    ///////////////////////////////////////////////////////////////////
    /**
     * get postscript data (which may be split across several objects)
     */
    final private byte[] readPageIntoStream(String contents){

        LogWriter.writeMethod("{readPageIntoStream}", 0);

        //reset buffer object
        byte[] binary_data = new byte[0], decoded_stream_data=null;

        //reset text state
        currentTextState = new TextState();

        //single stream value or indirect onto array
         if(!contents.startsWith("[")){

              //get the data for an object

              Map objValues =currentPdfFile.readObject(contents,false, null);

			  decoded_stream_data = currentPdfFile.readStream(objValues,contents,true,true,false);

              if(decoded_stream_data==null)
                  contents=(String) objValues.get("rawValue");
              else
                  binary_data=decoded_stream_data;

         }

         /**read an array*/
        if((contents!=null)&&(decoded_stream_data==null)){

			contents=Strip.removeArrayDeleminators(contents).trim();

			// Read page and all objects on page
			StringTokenizer initial_values = new StringTokenizer(contents, "R");

			//readl all objects for page into stream
			while (initial_values.hasMoreTokens()) {
				String current_page_value = initial_values.nextToken().trim();

				if (current_page_value.length() == 0)
					break;

				//get the data for an object
				decoded_stream_data =currentPdfFile.readStream(current_page_value + " R",true);

				/**
				 * append into data_buffer by copying processed_data then
				 * binary_data into temp and then temp back into binary_data
				 */
				//if (decoded_stream_data == null)
				//LogWriter.writeLog("Null stream returned");
				//else {
				if (decoded_stream_data != null){
					int current_length = binary_data.length + 1;

					//find end of our data which we decompressed.
					int processed_length = decoded_stream_data.length;
					if (processed_length > 0) { //trap error
						while (decoded_stream_data[processed_length - 1] == 0)
							processed_length--;

						//put current into temp so I can resize array
						byte[] temp = new byte[current_length];
						System.arraycopy(
								binary_data,
								0,
								temp,
								0,
								current_length - 1);

						//add a space between streams
						temp[current_length - 1] =  ' ';

						//resize
						binary_data = new byte[current_length + processed_length];

						//put original data back
						System.arraycopy(temp, 0, binary_data, 0, current_length);

						//and add in new data
						System.arraycopy(decoded_stream_data,0,binary_data,current_length,processed_length);
					}
				}
			}

			//LogWriter.writeLog("page "+ current_page_value + " processed");
		}

		return binary_data;
    }

    /**convert to to String*/
    private String generateOpAsString(int p,byte[] dataStream) {

        String s="";

        int start=this.opStart[p];
        int end=this.opEnd[p];

        //lose spaces or returns at end
        while((dataStream[end]==32)||(dataStream[end]==13)||(dataStream[end]==10))
            end--;

        int count=end-start+1;

        //discount duplicate spaces
        int spaces=0;
        for(int ii=0;ii<count;ii++){
            if((ii>0)&&((dataStream[start+ii]==32)||(dataStream[start+ii]==13)||(dataStream[start+ii]==10))&&
                    ((dataStream[start+ii-1]==32)||(dataStream[start+ii-1]==13)||(dataStream[start+ii-1]==10)))
                spaces++;
        }

        char[] charString=new char[count-spaces];
        int pos=0;

        for(int ii=0;ii<count;ii++){
            if((ii>0)&&((dataStream[start+ii]==32)||(dataStream[start+ii]==13)||(dataStream[start+ii]==10))&&
                    ((dataStream[start+ii-1]==32)||(dataStream[start+ii-1]==13)||(dataStream[start+ii-1]==10)))
                {
            }else{
                if((dataStream[start+ii]==10)||(dataStream[start+ii]==13))
                    charString[pos]=' ';
                else
                    charString[pos]=(char)dataStream[start+ii];
                pos++;
            }
        }

        s=String.copyValueOf(charString);

        return s;

    }

    ////////////////////////////////////////////////////
    final private void BT() {

        //set values used in plot
        currentTextState.resetTm();

        //keep position in case we need
        currentTextState.setTMAtLineStart();

        //currentGraphicsState.setClippingShape(null);

        //font info
        currentFont = currentTextState.getFontName();
        currentTextState.setCurrentFontSize(0);
        lastFontSize=-1;

        //currentGraphicsState.setLineWidth(0);
        
        //save for later and set TR
        if (renderPage == true){

            if(renderDirectly){
                current.renderClip(currentGraphicsState.getClippingShape(),null,defaultClip,g2);
            }else{
                current.drawClip(currentGraphicsState) ;
                current.drawTR(GraphicsState.FILL);
                //current.setLineWidth(0);
              //  current.drawColor((Color)currentGraphicsState.getNonstrokeColor(),GraphicsState.FILL);
             //   current.drawColor((Color)currentGraphicsState.getStrokeColor(),GraphicsState.STROKE);

            }
        }
    }

    //////////////////////////////////////////////////////////
    /**
     * restore GraphicsState status from graphics stack
     */
    final private void restoreGraphicsState() {

        if(!isStackInitialised){

            LogWriter.writeLog("No GraphicsState saved to retrieve");

        }else{

            currentGraphicsState = (GraphicsState) graphicsStateStack.pull();
            currentTextState = (TextState) textStateStack.pull();

            strokeColorSpace=(GenericColorSpace) strokeColorStateStack.pull();
            nonstrokeColorSpace=(GenericColorSpace) nonstrokeColorStateStack.pull();

            Object currentClip=clipStack.pull();

            if(currentClip==null)
                currentGraphicsState.setClippingShape(null);
            else
                currentGraphicsState.setClippingShape((Area)currentClip);

            //save for later
            if (renderPage == true){

                if(renderDirectly)
                    current.renderClip(currentGraphicsState.getClippingShape(),null,defaultClip,g2);
                else{
                        current.drawClip(currentGraphicsState) ;/**/

                        current.resetOnColorspaceChange();
                        current.drawFillColor(currentGraphicsState.getNonstrokeColor());
                        current.drawStrokeColor(currentGraphicsState.getStrokeColor());

                        /**
                         * align display
                         */
                        current.setGraphicsState(GraphicsState.FILL,currentGraphicsState.getNonStrokeAlpha());
                        current.setGraphicsState(GraphicsState.STROKE,currentGraphicsState.getStrokeAlpha());

                        //current.drawTR(currentGraphicsState.getTextRenderType()); //reset TR value
                }
            }


        }
    }
    ///////////////////////////////////////////////////////////////////////
    final private void L(float x,float y) {
        currentDrawShape.lineTo(x,y);
    }
    ///////////////////////////////////////////////////////////////////////
    final private void F(boolean isStar) {

        //set Winding rule
        if (isStar){
            currentDrawShape.setEVENODDWindingRule();
        }else
            currentDrawShape.setNONZEROWindingRule();

        currentDrawShape.closeShape();

        //generate shape and stroke
        Shape currentShape =
            currentDrawShape.generateShapeFromPath(currentGraphicsState.getClippingShape(),
                currentGraphicsState.CTM,
                isClip, pageLines,true,nonstrokeColorSpace.getColor(),
                    currentGraphicsState.getLineWidth(),pageData.getCropBoxWidth(1));

        //save for later
        if (renderPage == true){

            currentGraphicsState.setStrokeColor( strokeColorSpace.getColor());
            currentGraphicsState.setNonstrokeColor(nonstrokeColorSpace.getColor());
            currentGraphicsState.setFillType(GraphicsState.FILL);

            if(renderDirectly)
                current.renderShape(currentGraphicsState.getFillType(),currentGraphicsState.getStrokeColor(),
                        currentGraphicsState.getNonstrokeColor(),currentGraphicsState.getStroke(), currentShape,g2,
                        currentGraphicsState.getStrokeAlpha(),currentGraphicsState.getNonStrokeAlpha()) ;
            else
                current.drawShape(currentShape,currentGraphicsState) ;

        }
        




        //always reset flag
        isClip = false;
        currentDrawShape.resetPath(); // flush all path ops stored

    }
    ////////////////////////////////////////////////////////////////////////
    final private void TC(float tc) {
        currentTextState.setCharacterSpacing(tc);
    }
    ////////////////////////////////////////////////////////
    final private void CM(float[][] Trm) {

        //multiply to get new CTM
        currentGraphicsState.CTM =Matrix.multiply(Trm, currentGraphicsState.CTM);

        multipleTJs=false;

    }
    ////////////////////////////////////////////////////////////////////////
    /**
     * used by TD and T* to move current co-ord
     */
    protected final void relativeMove(float new_x, float new_y) {

        //create matrix to update Tm
        float[][] temp = new float[3][3];

        currentTextState.Tm = currentTextState.getTMAtLineStart();

        //set Tm matrix
        temp[0][0] = 1;
        temp[0][1] = 0;
        temp[0][2] = 0;
        temp[1][0] = 0;
        temp[1][1] = 1;
        temp[1][2] = 0;
        temp[2][0] = new_x;
        temp[2][1] = new_y;
        temp[2][2] = 1;

        //multiply to get new Tm
        currentTextState.Tm = Matrix.multiply(temp, currentTextState.Tm);

        currentTextState.setTMAtLineStart();

        //move command
        moveCommand = 2; //0=t*, 1=Tj, 2=TD
    }
    ////////////////////////////////////////////////////////////////////////
    final private void S(boolean isLowerCase) {

        //close for s command
        if (isLowerCase)
            currentDrawShape.closeShape();

        Shape currentShape =
            currentDrawShape.generateShapeFromPath( null,
                currentGraphicsState.CTM,
                isClip,pageLines,false,null,
                    currentGraphicsState.getLineWidth(),pageData.getCropBoxWidth(1));

        if(currentShape!=null){ //allow for the odd combination of f then S

            if(currentShape.getBounds().getWidth()<=1){// && currentGraphicsState.getLineWidth()<=1.0f){
                currentShape=currentShape.getBounds2D();
            //    System.out.println("XX");
            }

//            if(currentShape!=null && currentShape.getBounds().getX()>628 && currentShape.getBounds().getX()<630){
//            System.out.println(currentShape+" "+currentShape.getBounds2D());
//            Matrix.show(currentGraphicsState.CTM);
//           System.exit(1);
//            }

            //save for later
            if (renderPage == true){

                currentGraphicsState.setStrokeColor( strokeColorSpace.getColor());
                currentGraphicsState.setNonstrokeColor( nonstrokeColorSpace.getColor());
                currentGraphicsState.setFillType(GraphicsState.STROKE);

                if(renderDirectly)
                    current.renderShape(currentGraphicsState.getFillType(),
                            currentGraphicsState.getStrokeColor(),currentGraphicsState.getNonstrokeColor(),
                            currentGraphicsState.getStroke(), currentShape,g2,currentGraphicsState.getStrokeAlpha(),
                            currentGraphicsState.getNonStrokeAlpha()) ;
                else
                    current.drawShape( currentShape,currentGraphicsState);

            }



        }

        //always reset flag
        isClip = false;
        currentDrawShape.resetPath(); // flush all path ops stored

    }
    ///////////////////////////////////////////////////////////////////////////
    final private void I() {
        //if (currentToken.equals("i")) {
            //int value =
            //	(int) Float.parseFloat((String) operand.elementAt(0));

            //set value
            //currentGraphicsState.setFlatness(value);
        //}
    }

    ////////////////////////////////////////////////////////////
    final private void D(byte[] characterStream) {


        String values = ""; //used to combine values

        //and the dash array
        int items = operandCount;

        if(items==1)
            values=generateOpAsString(0,characterStream);
        else{
            //concat values
            StringBuffer list=new StringBuffer();
            for (int i = items - 1; i > -1; i--){
                list.append(generateOpAsString(i,characterStream));
                list.append(" ");
            }
            values=list.toString();
        }

        //allow for default
        if ((values.equals("[ ] 0 "))| (values.equals("[]0"))| (values.equals("[] 0 "))) {
            currentGraphicsState.setDashPhase(0);
            currentGraphicsState.setDashArray(new float[0]);
        } else {

            //get dash pattern
            int pointer=values.indexOf("]");

            String dash=values.substring(0,pointer);
            int phase=(int)Float.parseFloat(values.substring(pointer+1,values.length()).trim());

            //put into dash array
            StringTokenizer dash_values =new StringTokenizer(dash, "[ ]");
            int count=dash_values.countTokens();
            float[] dash_array = new float[count];

            for(int i=0;i<count;i++)
                dash_array[i] = Float.parseFloat(dash_values.nextToken());

            //put array into global value
            currentGraphicsState.setDashArray(dash_array);

            //last value is phase
            currentGraphicsState.setDashPhase(phase);

        }
    }
    ////////////////////////////////////////////////////////////////////////
    final private void SCN(boolean isLowerCase,byte[] stream)  {

        String[] operand=getValues(operandCount,stream);

        if(isLowerCase)
            nonstrokeColorSpace.setColor(operand,operandCount);
        else
            strokeColorSpace.setColor(operand,operandCount);

    }

    /***
     * COMMANDS - refer to Adobes pdf manual to
     * see what these commands do
     */
    /////////////////////////////////////////////////
    final private void B(boolean isStar,boolean isLowerCase) {

        //set Winding rule
        if (isStar)
            currentDrawShape.setEVENODDWindingRule();
        else
            currentDrawShape.setNONZEROWindingRule();

        //close for s command
        if (isLowerCase)
            currentDrawShape.closeShape();

        Shape currentShape =
            currentDrawShape.generateShapeFromPath( null,
                currentGraphicsState.CTM,
                isClip,pageLines,false,null,
                    currentGraphicsState.getLineWidth(),pageData.getCropBoxWidth(1));

        //save for later
        if ((renderPage == true)&&(currentShape!=null)){

            currentGraphicsState.setStrokeColor( strokeColorSpace.getColor());
            currentGraphicsState.setNonstrokeColor( nonstrokeColorSpace.getColor());
            currentGraphicsState.setFillType(GraphicsState.FILLSTROKE);

            if(renderDirectly)
                current.renderShape(currentGraphicsState.getFillType(),
                        currentGraphicsState.getStrokeColor(),
                        currentGraphicsState.getNonstrokeColor(),
                        currentGraphicsState.getStroke(),currentShape,g2,
                        currentGraphicsState.getStrokeAlpha(),currentGraphicsState.getNonStrokeAlpha()) ;
            else
                current.drawShape( currentShape,currentGraphicsState) ;

        }



        //always reset flag
        isClip = false;

        currentDrawShape.resetPath(); // flush all path ops stored
    }
    ///////////////////////////////////////////////////////////////////////
    /**handle the M commands*/
    final private void mm(int mitre_limit) {

        //handle M command
        currentGraphicsState.setMitreLimit(mitre_limit);

    }

    /**handle the m commands*/
    final private void M(float x,float y) {

        //handle m command
        currentDrawShape.moveTo(x, y);


    }
    /////////////////////////////////////////////////////
    final private void J(boolean isLowerCase,int value) {

        int style = 0;
        if (!isLowerCase) {

            //map join style
            if (value == 0)
                style = BasicStroke.JOIN_MITER;
            if (value == 1)
                style = BasicStroke.JOIN_ROUND;
            if (value == 2)
                style = BasicStroke.JOIN_BEVEL;

            //set value
            currentGraphicsState.setJoinStyle(style);
        } else {
            //map cap style
            if (value == 0)
                style = BasicStroke.CAP_BUTT;
            if (value == 1)
                style = BasicStroke.CAP_ROUND;
            if (value == 2)
                style = BasicStroke.CAP_SQUARE;

            //set value
            currentGraphicsState.setCapStyle(style);
        }
    }
    ////////////////////////////////////////////////////////////////////////
    final private void RG(boolean isLowerCase,byte[] stream)  {

        //ensure color values reset
        current.resetOnColorspaceChange();

        //set flag to show which color (stroke/nonstroke)
        boolean isStroke=!isLowerCase;

        String[] operand=getValues(operandCount,stream);

        //set colour
        if(isStroke){
                if (strokeColorSpace.getID() != ColorSpaces.DeviceRGB)
                strokeColorSpace=new DeviceRGBColorSpace();

                strokeColorSpace.setColor(operand,operandCount);
        }else{
                if (nonstrokeColorSpace.getID() != ColorSpaces.DeviceRGB)
                nonstrokeColorSpace=new DeviceRGBColorSpace();

                nonstrokeColorSpace.setColor(operand,operandCount);
        }
    }
    ////////////////////////////////////////////////////////////////////////
    final private void Y(float x3,float y3,float x,float y) {
        currentDrawShape.addBezierCurveY(x, y, x3, y3);
    }
    ////////////////////////////////////////////////////////////////////////
    final private void TZ(float tz) {

        //Text height
        currentTextState.setHorizontalScaling(tz / 100);
    }
    ////////////////////////////////////////////////////////////////////////
    final private void RE(float x,float y,float w,float h) {

        //get values
        currentDrawShape.appendRectangle(x, y, w, h);
    }
    //////////////////////////////////////////////////////////////////////
    final private void ET() {

    	//currentGraphicsState.setLineWidth(0);
    	//current.setLineWidth(0);
        
    	current.resetOnColorspaceChange();

    }
    ////////////////////////////////////////////////////////////////////////
    /**
     * put item in graphics stack
     */
    final private void pushGraphicsState() {

        if(!isStackInitialised){
            isStackInitialised=true;

            graphicsStateStack = new Vector_Object(10);
             textStateStack = new Vector_Object(10);
             strokeColorStateStack= new Vector_Object(20);
             nonstrokeColorStateStack= new Vector_Object(20);
            clipStack=new Vector_Object(20);
        }

        //store
        graphicsStateStack.push(currentGraphicsState.clone());

        //store clip
        Area currentClip=currentGraphicsState.getClippingShape();
        if(currentClip==null)
            clipStack.push(null);
        else{
            clipStack.push(currentClip.clone());
        }
        //store text state (technically part of gs)
        textStateStack.push(currentTextState.clone());

        //save colorspaces
        nonstrokeColorStateStack.push(nonstrokeColorSpace.clone());
        strokeColorStateStack.push(strokeColorSpace.clone());

        current.resetOnColorspaceChange();

    }

    final private void MP() {

        //<start-adobe>
        if(markedContentExtracted)
        contentHandler.MP();
        //<end-adobe>

    }

    final private void DP(int startCommand, int dataPointer,byte[] raw,String op) {

        //<start-adobe>
        if(markedContentExtracted){
            /**read the dictionary*/
            Map rootObject=new HashMap();

            if(op.endsWith(" R"))
                rootObject=currentPdfFile.readObject(op,false,null);
            else
                currentPdfFile.readDictionary("",0,rootObject,startCommand-1,raw,false,new HashMap(),dataPointer);

            contentHandler.DP(rootObject);
        }
        //<end-adobe>
		
    }

    ////////////////////////////////////////////////////////////////////////
    final private void EMC() {

        //<start-adobe>
        if(markedContentExtracted)
        contentHandler.EMC();
        //<end-adobe>
    }

    final private void TJ(byte[] characterStream,int startCommand,int dataPointer) {

		//extract the text
        StringBuffer current_value=processTextArray(characterStream, startCommand,dataPointer);

        //<start-adobe>
        //will be null if no content
        if ((current_value != null) && (isPageContent)) {

            /**add raw element if not in marked content*/
            if (!markedContentExtracted) {

                //get colour if needed
                if(textColorExtracted){
                    if ((currentGraphicsState.getTextRenderType() & GraphicsState.FILL) == GraphicsState.FILL){
                        currentColor=this.nonstrokeColorSpace.getXMLColorToken();
                    }else{
                        currentColor=this.strokeColorSpace.getXMLColorToken();
                    }
                }

                /**save item and add in graphical elements*/
                if(textExtracted){

                    pdfData.addRawTextElement(
                            (charSpacing * THOUSAND),
                            currentTextState.writingMode,
                            font_as_string,
                            currentFontData.getCurrentFontSpaceWidth(),
                            currentTextState,
                            x1,
                            y1,
                            x2,
                            y2,
                            moveCommand,
                            current_value,
                            tokenNumber,
                            textLength,currentColor);
                }
            }else
                contentHandler.setText(current_value,x1,y1,x2,y2);
            
		}
        //<end-adobe>

        moveCommand = -1; //flags no move!
    }

    ///////////////////////////////////////////////////////////////////////
    final private void G(boolean isLowerCase,byte[] stream) {

        //ensure color values reset
        current.resetOnColorspaceChange();

        boolean isStroke=!isLowerCase;
        String[] operand=getValues(operandCount,stream);

        //set colour and colorspace
        if(isStroke){
                if (strokeColorSpace.getID() != ColorSpaces.DeviceGray)
                strokeColorSpace=new DeviceGrayColorSpace();

                strokeColorSpace.setColor(operand,operandCount);

        }else{
                if (nonstrokeColorSpace.getID() != ColorSpaces.DeviceGray)
                nonstrokeColorSpace=new DeviceGrayColorSpace();

                nonstrokeColorSpace.setColor(operand,operandCount);
        }
    }

    private void TL(float tl) {
        currentTextState.setLeading(tl);
    }

    /**
     * examine processGraphicsState command and store values
     */
    public void readGraphicsState(Map rawValues){

        Object object;
        String id;
        Iterator keys=rawValues.keySet().iterator();

        //work through each item in turn
        while (keys.hasNext()) {
            id = (String) keys.next();
            object = rawValues.get(id);

            //get values and store
            Map values;
            if(object instanceof String)
                values =currentPdfFile.readObject((String) object,false, null);
            else
                values=(Map)object;

            gs_state.put("/"+id,values);

        }
    }

    private void BDC(int startCommand, int dataPointer,byte[] raw,String op) {

        //<start-adobe>
        if(markedContentExtracted){
        	
            Map rootObject=new HashMap();

			if(op.endsWith(" R"))
                rootObject=currentPdfFile.readObject(op,false,null);
            else  {
                currentPdfFile.readDictionary("",0,rootObject,startCommand-1,raw,false,new HashMap(),dataPointer);
			}
			contentHandler.BDC(rootObject);
        }
        //<end-adobe>
    }

    private void BMC(String op) {

        //<start-adobe>
        if(markedContentExtracted)
            contentHandler.BMC(op);

        //<end-adobe>
    }

final float parseFloat(int id,byte[] stream){

        float f=0f,dec=0f,num=0f;

        int start=opStart[id];
        int charCount=opEnd[id]-start;

        int floatptr=charCount;
        int intStart=0;
        boolean isMinus=false;
        //hand optimised float code
        //find decimal point
        for(int j=charCount-1;j>-1;j--){
            if(stream[start+j]==46){ //'.'=46
                floatptr=j;
                break;
            }
        }

        int intChars=floatptr;
        //allow for minus
        if(stream[start]==43){ //'+'=43
            intChars--;
            intStart++;
        }else if(stream[start]==45){ //'-'=45
            //intChars--;
            intStart++;
            isMinus=true;
        }

        //optimisations
        int intNumbers=intChars-intStart;
        int decNumbers=charCount-floatptr;

        if((intNumbers>3)){ //non-optimised to cover others
            isMinus=false;
            f=Float.parseFloat(this.generateOpAsString(id,stream));

        }else{

            float units=0f,tens=0f,hundreds=0f,tenths=0f,hundredths=0f, thousands=0f, tenthousands=0f,hunthousands=0f;
            int c;

            //hundreds
            if(intNumbers>2){
                c=stream[start+intStart]-48;
                switch(c){
                    case 1:
                        hundreds=100.0f;
                        break;
                    case 2:
                        hundreds=200.0f;
                        break;
                    case 3:
                        hundreds=300.0f;
                        break;
                    case 4:
                        hundreds=400.0f;
                        break;
                    case 5:
                        hundreds=500.0f;
                        break;
                    case 6:
                        hundreds=600.0f;
                        break;
                    case 7:
                        hundreds=700.0f;
                        break;
                    case 8:
                        hundreds=800.0f;
                        break;
                    case 9:
                        hundreds=900.0f;
                        break;
                }
                intStart++;
            }

            //tens
            if(intNumbers>1){
                c=stream[start+intStart]-48;
                switch(c){
                    case 1:
                        tens=10.0f;
                        break;
                    case 2:
                        tens=20.0f;
                        break;
                    case 3:
                        tens=30.0f;
                        break;
                    case 4:
                        tens=40.0f;
                        break;
                    case 5:
                        tens=50.0f;
                        break;
                    case 6:
                        tens=60.0f;
                        break;
                    case 7:
                        tens=70.0f;
                        break;
                    case 8:
                        tens=80.0f;
                        break;
                    case 9:
                        tens=90.0f;
                        break;
                }
                intStart++;
            }

            //units
            if(intNumbers>0){
                c=stream[start+intStart]-48;
                switch(c){
                    case 1:
                        units=1.0f;
                        break;
                    case 2:
                        units=2.0f;
                        break;
                    case 3:
                        units=3.0f;
                        break;
                    case 4:
                        units=4.0f;
                        break;
                    case 5:
                        units=5.0f;
                        break;
                    case 6:
                        units=6.0f;
                        break;
                    case 7:
                        units=7.0f;
                        break;
                    case 8:
                        units=8.0f;
                        break;
                    case 9:
                        units=9.0f;
                        break;
                }
            }

            //tenths
            if(decNumbers>1){
                floatptr++; //move beyond.
                c=stream[start+floatptr]-48;
                switch(c){
                    case 1:
                        tenths=0.1f;
                        break;
                    case 2:
                        tenths=0.2f;
                        break;
                    case 3:
                        tenths=0.3f;
                        break;
                    case 4:
                        tenths=0.4f;
                        break;
                    case 5:
                        tenths=0.5f;
                        break;
                    case 6:
                        tenths=0.6f;
                        break;
                    case 7:
                        tenths=0.7f;
                        break;
                    case 8:
                        tenths=0.8f;
                        break;
                    case 9:
                        tenths=0.9f;
                        break;
                }
            }

            //hundredths
            if(decNumbers>2){
                floatptr++; //move beyond.
                //c=value.charAt(floatptr)-48;
                c=stream[start+floatptr]-48;
                switch(c){
                    case 1:
                        hundredths=0.01f;
                        break;
                    case 2:
                        hundredths=0.02f;
                        break;
                    case 3:
                        hundredths=0.03f;
                        break;
                    case 4:
                        hundredths=0.04f;
                        break;
                    case 5:
                        hundredths=0.05f;
                        break;
                    case 6:
                        hundredths=0.06f;
                        break;
                    case 7:
                        hundredths=0.07f;
                        break;
                    case 8:
                        hundredths=0.08f;
                        break;
                    case 9:
                        hundredths=0.09f;
                        break;
                }
            }

            //thousands
            if(decNumbers>3){
                floatptr++; //move beyond.
                c=stream[start+floatptr]-48;
                switch(c){
                    case 1:
                        thousands=0.001f;
                        break;
                    case 2:
                        thousands=0.002f;
                        break;
                    case 3:
                        thousands=0.003f;
                        break;
                    case 4:
                        thousands=0.004f;
                        break;
                    case 5:
                        thousands=0.005f;
                        break;
                    case 6:
                        thousands=0.006f;
                        break;
                    case 7:
                        thousands=0.007f;
                        break;
                    case 8:
                        thousands=0.008f;
                        break;
                    case 9:
                        thousands=0.009f;
                        break;
                }
            }

            //tenthousands
            if(decNumbers>4){
                floatptr++; //move beyond.
                c=stream[start+floatptr]-48;
                switch(c){
                    case 1:
                        tenthousands=0.0001f;
                        break;
                    case 2:
                        tenthousands=0.0002f;
                        break;
                    case 3:
                        tenthousands=0.0003f;
                        break;
                    case 4:
                        tenthousands=0.0004f;
                        break;
                    case 5:
                        tenthousands=0.0005f;
                        break;
                    case 6:
                        tenthousands=0.0006f;
                        break;
                    case 7:
                        tenthousands=0.0007f;
                        break;
                    case 8:
                        tenthousands=0.0008f;
                        break;
                    case 9:
                        tenthousands=0.0009f;
                        break;
                }
            }

//			tenthousands
            if(decNumbers>5){
                floatptr++; //move beyond.
                c=stream[start+floatptr]-48;

                switch(c){
                    case 1:
                        hunthousands=0.00001f;
                        break;
                    case 2:
                        hunthousands=0.00002f;
                        break;
                    case 3:
                        hunthousands=0.00003f;
                        break;
                    case 4:
                        hunthousands=0.00004f;
                        break;
                    case 5:
                        hunthousands=0.00005f;
                        break;
                    case 6:
                        hunthousands=0.00006f;
                        break;
                    case 7:
                        hunthousands=0.00007f;
                        break;
                    case 8:
                        hunthousands=0.00008f;
                        break;
                    case 9:
                        hunthousands=0.00009f;
                        break;
                }
            }

            dec=tenths+hundredths+thousands+tenthousands+hunthousands;
            num=hundreds+tens+units;
            f=num+dec;

        }

        if(isMinus)
            return -f;
        else
            return f;
    }

    ////////////////////////////////////////////////////////////////////////
    final private void TM() {

        //keep position in case we need
        currentTextState.setTMAtLineStart();

        multipleTJs=false;

        //move command
        moveCommand = 1; //0=t*, 1=Tj, 2=TD

    }
    //////////////////////////////////////////////////////////////////////////
    final private void H() {
        currentDrawShape.closeShape();
    }
    ////////////////////////////////////////////////////////////////////////
    final private void TR(int value) {

        //Text render mode

        if (value == 0)
            value = GraphicsState.FILL;
        else if (value == 1)
            value = GraphicsState.STROKE;
        else if (value == 2)
            value = GraphicsState.FILLSTROKE;
        else if(value==3)
            value = GraphicsState.INVISIBLE;
        else if(value==7)
            value = GraphicsState.CLIPTEXT;

        currentGraphicsState.setTextRenderType(value);

        if (renderPage == true){

            if(!renderDirectly)
                current.drawTR(value);
        }

    }
    ////////////////////////////////////////////////////////////////////////
    final private void Q(boolean isLowerCase) {

        //save or retrieve
        if (isLowerCase)
            pushGraphicsState();
        else{
            restoreGraphicsState();

            //switch to correct font
            String fontID=currentTextState.getFontID();

            Object restoredFont=fonts.get(fontID);
            if(restoredFont!=null){
            	currentFontData=(PdfFont)  restoredFont;
            }

        }
    }

    final private int ID(byte[] characterStream,int dataPointer) throws Exception{


        //reset global flag
        isMask=false;

        BufferedImage image =   null;

        boolean hasCustomHandler=customImageHandler!=null;
        String filter_value = null, currentValue;
        String inline_filter_value;
        boolean inline_imageMask = false;
        String inline_decodeArray = "";
        String inline_colorspace="/DeviceRGB";

        //store pointer to current place in file
        int inline_start_pointer = dataPointer + 1;
        int inline_image_width = 0;
        int inline_image_height = 0;
        int inline_image_depth = 0;

        //find end of stream
        int i = inline_start_pointer;
        int streamLength=characterStream.length;

        //find end
        while (true) {

            //look for end EI
            if ((streamLength-i>3)&&((characterStream[i] == 32)||(characterStream[i] == 10)||(characterStream[i] == 13))
                && (characterStream[i + 1] == 69)
                && (characterStream[i + 2] == 73)
                && ((characterStream[i+3] == 32)||(characterStream[i+3] == 10)||(characterStream[i+3] == 13)))
                break;

            i++;

            if(i==streamLength)
                break;
        }

        if((renderImages)| (finalImagesExtracted)| (clippedImagesExtracted)| (rawImagesExtracted)){

        /**
         * make sure values split correctly
         */
            ArrayList processed_values = new ArrayList();

            //reorder values so work
        if(operandCount>0){
            String[] orderedOps=new String[MAXOPS];
            int[] orderedOpStart=new int[MAXOPS];
            int[] orderedOpEnd=new int[MAXOPS];
            int opid=0;
            for(int jj=this.currentOp-1;jj>-1;jj--){

                orderedOpStart[opid]=opStart[jj];
                orderedOpEnd[opid]=opEnd[jj];
                if(opid==operandCount)
                    jj=-1;
                opid++;
            }
            if(opid==operandCount){
                currentOp--; //decrease to make loop comparison faster
                for(int jj=this.MAXOPS-1;jj>currentOp;jj--){

                    orderedOpStart[opid]=opStart[jj];
                    orderedOpEnd[opid]=opEnd[jj];
                    if(opid==operandCount)
                        jj=currentOp;
                    opid++;
                }
                currentOp++;
            }

            opStart=orderedOpStart;
            opEnd=orderedOpEnd;
        }

        int items = operandCount;
        StringBuffer raw_values = new StringBuffer();
        for (int ii = 0; ii < items; ii++) {

            //ensure spaces inbetween << and >>
            int i1=generateOpAsString(ii,characterStream).indexOf("<<");
            if(i1>0){
                raw_values.append(generateOpAsString(ii,characterStream).substring(0,i1));
                raw_values.append(' ');
                raw_values.append(generateOpAsString(ii,characterStream).substring(i1));
            }else{
                i1=generateOpAsString(ii,characterStream).indexOf(">>");
                if((i1!=-1)&&(generateOpAsString(ii,characterStream).indexOf(" >>")==-1)){
                    raw_values.append(generateOpAsString(ii,characterStream).substring(0,i1));
                    raw_values.append(' ');
                    raw_values.append(generateOpAsString(ii,characterStream).substring(i1));
                }else
                    raw_values.append(generateOpAsString(ii,characterStream));
            }
            raw_values.append(' ');
        }

        StringTokenizer values =new StringTokenizer(raw_values.toString().trim(), "[]/ ", true);

        while (values.hasMoreTokens()) {
            String token = values.nextToken();
            if (token.equals("/"))
                processed_values.add(token + values.nextToken());
            else if (token.equals("[")) {
                while (token.indexOf("]") == -1) {
                    String next = values.nextToken();
                    if (next.equals("/"))
                        token = token + " " + next;
                    else
                        token = token + next;

                }

                processed_values.add(token);
            } else if (!token.equals(" "))
                processed_values.add(token);

        }

    ArrayList operand = processed_values;

        Map objData=new Hashtable();

        /**
         * set meta data using values before ID command
         */
        items = operand.size();

        for (int ii = 0; ii < items; ii++) {
            currentValue = (String) operand.get(ii);

            if (currentValue.equals("/W") || currentValue.equals("/Width")) { //width

                ii++;
                inline_image_width =
                    Integer.parseInt((String) operand.get(ii));

                if(hasCustomHandler)
                objData.put("Width",operand.get(ii));

            } else if (currentValue.equals("/IM")) { //image mask

                ii++;
                if (((String) operand.get(ii)).indexOf("true")!= -1){
                    inline_imageMask = true;
                    isMask=true;
                }
                if(hasCustomHandler)
                objData.put("ImageMask",operand.get(ii));

            } else if (currentValue.equals("/D")||(currentValue.equals("/Decode"))) {

                ii++;
                inline_decodeArray = (String) operand.get(ii);
                if(hasCustomHandler)
                objData.put("Decode",operand.get(ii));

            } else if (currentValue.equals("/H") || currentValue.equals("/Height")) { //height

                ii++;
                inline_image_height =
                    Integer.parseInt((String) operand.get(ii));
                if(hasCustomHandler)
                objData.put("Height",operand.get(ii));

            } else if (currentValue.equals("/BPC") || (currentValue.equals("/BitsPerComponent"))) { //bits used

                ii++;
                inline_image_depth =
                    Integer.parseInt((String) operand.get(ii));
                if(hasCustomHandler)
                objData.put("BitsPerComponent",operand.get(ii));

            } else if (currentValue.equals("/CS") ||(currentValue.equals("/ColorSpace"))) { //colorspace

                ii++;
                inline_colorspace= (String) operand.get(ii);

                //allow for an object
                if(inline_colorspace.startsWith("[")){
                    while(inline_colorspace.endsWith("]")==false){
                        inline_colorspace=inline_colorspace+operand.get(ii);
                        ii++;
                    }

                    inline_colorspace=Strip.removeArrayDeleminators(inline_colorspace);

                }
                if(hasCustomHandler)
                objData.put("ColorSpace",inline_colorspace);

            } else if (currentValue.equals("/F") || (currentValue.equals("/Filter"))) { //any filters used (may be several)
                filter_value = "";

                ii++;
                filter_value = (String) operand.get(ii);

                if (filter_value.indexOf("[") != -1) {
                    while (filter_value.indexOf("]") == -1) {
                        ii++;
                        String filter_name =(String) operand.get(ii);
                        filter_value = filter_value + " " + filter_name;

                    }
                    filter_value=Strip.removeArrayDeleminators(filter_value);
                }

                if(hasCustomHandler)
                objData.put("Filter",filter_value);

            } else if (currentValue.equals("/DP") || (currentValue.equals("/DecodeParms"))) { //any decode values
                ii++;
                String params = (String) operand.get(ii);
                Map objValues=new Hashtable();


                //allow for array of values
                if (params.startsWith("[")) {
                    while (params.indexOf("]") == -1) {
                        ii++;
                        params = params + " "+operand.get(ii);
                    }
                    objData.put("DecodeParms",params);
                }

                //allow for array of values
                if (params.startsWith("<<")) {
                    ii++;
                    while(true){

                        String key=(String) operand.get(ii);
                        if(key.equals(">>"))
                            break;

                        if(key.startsWith("/")){
                            objValues.put(key.substring(1),operand.get(ii+1));
                            ii++;
                        }
                        ii++;

                    }
                    objData.put("DecodeParms",objValues);
                }
            }
        }

        //always set (even if null) just in case LZW
        inline_filter_value = filter_value;

        //load the data
        //		generate the name including file name to make it unique
        String image_name =this.fileName+ "-IN-" + tokenNumber;

        /**
         * put image data in array
         */
        byte[] image_data = new byte[i - inline_start_pointer];
        System.arraycopy(
            characterStream,
            inline_start_pointer,
            image_data,
            0,
            i - inline_start_pointer);

        /**
         * allow user to process image
         */
        if(customImageHandler!=null){
                objData.put("Stream", image_data);
              image=customImageHandler.processImageData(objData,currentGraphicsState);
        }

        //handle filters (JPXDecode/DCT decode is handle by process image)
        if ((inline_filter_value != null)&&
        (!inline_filter_value.startsWith("/JPXDecode"))&&
        (!inline_filter_value.startsWith("/DCT"))){

            image_data =
                currentPdfFile.decodeFilters(
                    image_data,
                    inline_filter_value,
                    objData,inline_image_width,inline_image_height,false,null);
        }

        //handle colour information
        GenericColorSpace decodeColorData=new GenericColorSpace();
        if(inline_colorspace!=null){

            //see if defined
            String colorspaceObject=null;
            if(inline_colorspace.startsWith("/"))
                colorspaceObject =inline_colorspace .substring(1);

            /**get be a string or a Map*/
            Object colorspaceValues =getObjectFromCache(colorspaceObjects,rawColorspaceValues,colorspaceObject);

            //allow for a direct value (ie DeviceRGB)
            if(colorspaceValues==null){
                decodeColorData=ColorspaceDecoder.getColorSpaceInstance(isPrinting,currentGraphicsState.CTM,inline_colorspace,null,currentPdfFile);
            }else{

                //convert colorspace and get details
                if(colorspaceValues instanceof String)
                    decodeColorData=ColorspaceDecoder.getColorSpaceInstance(isPrinting,currentGraphicsState.CTM,(String)colorspaceValues,null,currentPdfFile);
                else
                    decodeColorData=ColorspaceDecoder.getColorSpaceInstance(isPrinting,currentGraphicsState.CTM,null,(Map)colorspaceValues,currentPdfFile);
            }
        }

        if(image_data!=null){
            //flag to show if plotted and generates image (stored in global image object)
            boolean alreadyCached=(!isType3Font && useHiResImageForDisplay && current.isImageCached(this.pageNum));

            if((!alreadyCached)&&((customImageHandler==null)||((image==null)&& !customImageHandler.alwaysIgnoreGenericHandler())))
            image=processImage(decodeColorData,inline_colorspace,
                    image_data,
                    image_name,
                    inline_image_width,
                    inline_image_height,
                    inline_image_depth,
                    inline_filter_value,
                    inline_decodeArray,
                    inline_imageMask,
                    createScaledVersion,objData);

            /**used to debug code by popping up window after glyph*/

            //generate name including filename to make it unique
            currentImage = image_name;
            if (image != null || alreadyCached){
                if((renderDirectly)|(this.useHiResImageForDisplay)){

                    currentGraphicsState.x=currentGraphicsState.CTM[2][0];
                    currentGraphicsState.y=currentGraphicsState.CTM[2][1];
                    if(renderDirectly){
                        current.renderImage(null,image,currentGraphicsState.getNonStrokeAlpha(),currentGraphicsState,g2,currentGraphicsState.x,currentGraphicsState.y);
                    }else
                        current.drawImage(pageNum,image,currentGraphicsState,alreadyCached,image_name);
                }else{
                    if(this.clippedImagesExtracted)
                        generateTransformedImage(image,image_name);
					else
                        generateTransformedImageSingle(image,image_name);
                }

                if(image!=null)
                image.flush();
            }

            }
        }

        dataPointer = i + 3;

        return dataPointer;

    }
    ////////////////////////////////////////////////////////////////////////
    final private void TS(float ts) {

        //Text rise
        currentTextState.setTextRise(ts);
    }
    ////////////////////////////////////////////////////////////////////////
    final private void double_quote(byte[] characterStream,int startCommand,int dataPointer,float tc,float tw) {

        //Tc part
        currentTextState.setCharacterSpacing(tc);

        //Tw
        currentTextState.setWordSpacing(tw);
        TSTAR();
        TJ(characterStream, startCommand,dataPointer);
    }
    ////////////////////////////////////////////////////////////////////////
    private  void TSTAR() {
        relativeMove(0, -currentTextState.getLeading());

        //move command
        moveCommand = 0; //0=t*, 1=Tj, 2=TD

        multipleTJs=false;
    }
    //////////////////
    final private void K(boolean isLowerCase,byte[] stream) {

        //ensure color values reset
        current.resetOnColorspaceChange();

        //set flag to show which color (stroke/nonstroke)
        boolean isStroke=!isLowerCase;

        /**allow for less than 4 values
         * (ie second mapping for device colourspace
         */
        if (operandCount > 3) {

            String[] operand=getValues(operandCount,stream);

            //set colour and make sure in correct colorspace
            if(isStroke){
                    if (strokeColorSpace.getID() != ColorSpaces.DeviceCMYK)
                    strokeColorSpace=new DeviceCMYKColorSpace();

                    strokeColorSpace.setColor(operand,operandCount);
            }else{
                    if (nonstrokeColorSpace.getID() != ColorSpaces.DeviceCMYK)
                    nonstrokeColorSpace=new DeviceCMYKColorSpace();

                    nonstrokeColorSpace.setColor(operand,operandCount);
            }
        }
    }

    private String[] getValues(int count,byte[] dataStream) {

        String[] op=new String[count];
        for(int i=0;i<count;i++)
            op[i]=this.generateOpAsString(i,dataStream);
        return op;
    }

    final private void W(boolean isStar) {

        //set Winding rule
        if (isStar)
            currentDrawShape.setEVENODDWindingRule();
        else
            currentDrawShape.setNONZEROWindingRule();

        //set clipping flag
        isClip = true;

    }

    /**set width from lower case w*/
    final private void width(float w) {

        //ensure minimum width
        //if(w<1)
        //w=1;

        currentGraphicsState.setLineWidth(w);

    }

    final private void one_quote(
        byte[] characterStream,
        int startCommand,int dataPointer) {

        TSTAR();
        TJ(characterStream, startCommand,dataPointer);

    }

    private void N() {

        if (isClip == true) {

            //create clipped shape
            currentDrawShape.closeShape();

            currentGraphicsState.updateClip(new Area(currentDrawShape.generateShapeFromPath(  null,
                    currentGraphicsState.CTM,
                    false,null,false,null,0,0)));
            currentGraphicsState.checkWholePageClip(pageData.getMediaBoxHeight(pageNum)+pageData.getMediaBoxY(pageNum));

            //always reset flag
            isClip = false;

            //save for later
            if (renderPage == true){
               if(renderDirectly){

                   //set the stroke to current value
                   Stroke newStroke=currentGraphicsState.getStroke();
                   g2.setStroke(newStroke);

                   current.renderClip(currentGraphicsState.getClippingShape(),null,defaultClip,g2) ;
               }else
                    current.drawClip(currentGraphicsState) ;
            }
        }

        currentDrawShape.resetPath(); // flush all path ops stored

    }


    ////////////////////////////////////////////////////////////////////////
    final private void sh(String shadingObject) {
         if(!runningStoryPad)
         JOptionPane.showMessageDialog(null,"Shading is Not displayed in the GPL version of JPedal");
        /**/
    }
    ////////////////////////////////////////////////////////////////////////
    final private void TW(float tw) {
        currentTextState.setWordSpacing(tw);
    }
    ////////////////////////////////////////////////////////
    final private void CS(boolean isLowerCase,String colorspaceObject) {

        //ensure color values reset
        current.resetOnColorspaceChange();

        //lose the /
        if(colorspaceObject .startsWith("/"))
        colorspaceObject =colorspaceObject .substring(1);

        /**get be a string or a Map*/
        Object colorspaceValues =getObjectFromCache(colorspaceObjects,rawColorspaceValues,colorspaceObject);

        //allow for a direct value (ie DeviceRGB)
        if(colorspaceValues==null)
        colorspaceValues=colorspaceObject;

        //set flag for stroke
        boolean isStroke = !isLowerCase;

        GenericColorSpace newColorSpace;

        //convert colorspace and get details
        if(colorspaceValues instanceof String)
            newColorSpace=ColorspaceDecoder.getColorSpaceInstance(isPrinting,currentGraphicsState.CTM,(String)colorspaceValues,null,currentPdfFile);
        else
            newColorSpace=ColorspaceDecoder.getColorSpaceInstance(isPrinting,currentGraphicsState.CTM,null,(Map)colorspaceValues,currentPdfFile);

        //pass in pattern arrays containing all values
        if(newColorSpace.getID()==ColorSpaces.Pattern){
            newColorSpace.setPattern(currentPatternValues,pageH);
            newColorSpace.setGS(currentGraphicsState);
        }

        if(isStroke)
            strokeColorSpace=newColorSpace;
        else
            nonstrokeColorSpace=newColorSpace;


    }
    ////////////////////////////////////////////////////////////////////////
    final private void V(float x3,float y3,float x2,float y2) {
        currentDrawShape.addBezierCurveV(x2, y2, x3, y3);
    }
    ////////////////////////////////////////////////////////////////////////
    final private void TF(float Tfs,String fontID) {

    	
        //set global variables to new values
        currentTextState.setFontTfs(Tfs);

        Object newFont=fonts.get(fontID);
        if(newFont!=null){
            //@fontHandle currentFontData.unsetUnscaledFont();
            currentFontData=(PdfFont)newFont;
        }

        //convert ID to font name and store
        currentFont = currentFontData.getFontName();
        currentTextState.setFont(currentFont,fontID);
        font_as_string =
            Fonts.createFontToken(
        currentFont,
                currentTextState.getCurrentFontSize());

    }

    /**
     * process each token and add to text or decode
     * if not known command, place in array (may be operand which is
     * later used by command)
     */
    final private int processToken(int commandID,
                                   byte[] characterStream,
                                   int startCommand,int dataPointer) throws PdfFontException,Exception
            {

        //reorder values so work
        if(operandCount>0){

            int[] orderedOpStart=new int[MAXOPS];
            int[] orderedOpEnd=new int[MAXOPS];
            int opid=0;
            for(int jj=this.currentOp-1;jj>-1;jj--){

                orderedOpStart[opid]=opStart[jj];
                orderedOpEnd[opid]=opEnd[jj];
                if(opid==operandCount)
                    jj=-1;
                opid++;
            }
            if(opid==operandCount){
                currentOp--; //decrease to make loop comparison faster
                for(int jj=this.MAXOPS-1;jj>currentOp;jj--){

                    orderedOpStart[opid]=opStart[jj];
                    orderedOpEnd[opid]=opEnd[jj];
                    if(opid==operandCount)
                        jj=currentOp;
                    opid++;
                }
                currentOp++;
            }

            opStart=orderedOpStart;
            opEnd=orderedOpEnd;
        }

        
        /**
         * call method to handle commands
         */

        /**text commands first and all other
         * commands if not found in first
         **/
        boolean notFound=true;
        if(((renderText)|| (textExtracted))) {

            notFound=false;

            switch(commandID){
            case Cmd.Tc :
                TC(parseFloat(0,characterStream));
            break;
            case Cmd.Tw :
                TW(parseFloat(0,characterStream));
            break;
            case Cmd.Tz :
                TZ(parseFloat(0,characterStream));
            break;
            case Cmd.TL :
                TL(parseFloat(0,characterStream));
            break;
            case Cmd.Tf :
                TF(parseFloat(0,characterStream),(generateOpAsString(1,characterStream)).substring(1));
            break;
            case Cmd.Tr :
                TR(Integer.parseInt(generateOpAsString(0,characterStream)));
            break;
            case Cmd.Ts :
                TS(parseFloat(0,characterStream));
            break;
            case Cmd.TD :
                TD(false,parseFloat(1,characterStream),parseFloat(0,characterStream));
            break;
            case Cmd.Td :
                TD(true,parseFloat(1,characterStream),parseFloat(0,characterStream));
            break;
            case Cmd.Tm :
                //set Tm matrix
                currentTextState.Tm[0][0] =parseFloat(5,characterStream);
                currentTextState.Tm[0][1] =parseFloat(4,characterStream);
                currentTextState.Tm[0][2] = 0;
                currentTextState.Tm[1][0] =parseFloat(3,characterStream);
                currentTextState.Tm[1][1] =parseFloat(2,characterStream);
                currentTextState.Tm[1][2] = 0;
                currentTextState.Tm[2][0] =parseFloat(1,characterStream);
                currentTextState.Tm[2][1] =parseFloat(0,characterStream);
                currentTextState.Tm[2][2] = 1;

                TM();
            break;
            case Cmd.Tstar :
                TSTAR();
            break;
            case Cmd.Tj :
                TJ(characterStream, startCommand,dataPointer);
            break;
            case Cmd.TJ :
                TJ(characterStream, startCommand,dataPointer);
            break;
            case Cmd.quote :
                one_quote(characterStream,startCommand,dataPointer);
            break;
            case Cmd.doubleQuote :
                double_quote(characterStream,startCommand,dataPointer,parseFloat(1,characterStream),parseFloat(2,characterStream));
            break;
            default:
                notFound=true;
            break;

            }
        }

        if((renderPage)||(textColorExtracted)||(colorExtracted)) {

            notFound=false;

            switch(commandID){
            case Cmd.rg :
                RG(true,characterStream);
            break;
            case Cmd.RG :
                RG(false,characterStream);
            break;
            case Cmd.SCN :
                SCN(false,characterStream);
            break;
            case Cmd.scn :
                SCN(true,characterStream);
            break;
            case Cmd.SC :
                SCN(false,characterStream);
            break;
            case Cmd.sc :
                SCN(true,characterStream);
            break;
            case Cmd.cs :
                CS(true,generateOpAsString(0,characterStream));
            break;
            case Cmd.CS :
                CS(false,generateOpAsString(0,characterStream));
            break;
            case Cmd.g :
                G(true,characterStream);
            break;
            case Cmd.G :
                G(false,characterStream);
            break;
            case Cmd.k :
                K(true,characterStream);
            break;
            case Cmd.K :
                K(false,characterStream);
            break;
            case Cmd.sh:
                sh(generateOpAsString(0,characterStream).substring(1));
            break;
            default:
                notFound=true;
            break;

            }
        }

        if(notFound){

            /**
             * other commands here
             */
            switch (commandID) {
            case Cmd.ID :
                dataPointer=ID(characterStream,dataPointer);
            break;
            case Cmd.B :
                B(false,false);
            break;
            case Cmd.b :
                B(false,true);
            break;
            case Cmd.bstar :
                B(true,true);
            break;
            case Cmd.Bstar :
                B(true,false);
            break;
            case Cmd.c :
                float x3 =parseFloat(1,characterStream);
                float y3 = parseFloat(0,characterStream);
                float x2 =parseFloat(3,characterStream);
                float y2 = parseFloat(2,characterStream);
                float x = parseFloat(5,characterStream);
                float y = parseFloat(4,characterStream);
                currentDrawShape.addBezierCurveC(x, y, x2, y2, x3, y3);
            break;
            case Cmd.d :
                D(characterStream);
            break;
            case Cmd.F :
                F(false);
            break;
            case Cmd.f :
                F(false);
            break;
            case Cmd.Fstar :
                F(true);
            break;
            case Cmd.fstar :
                F(true);
            break;
            case Cmd.h :
                H();
            break;
            case Cmd.l :
                L(parseFloat(1,characterStream),parseFloat(0,characterStream));
            break;
            case Cmd.m :
                M(parseFloat(1,characterStream),parseFloat(0,characterStream));
            break;
            case Cmd.n :
                N();
            break;
            case Cmd.S :
                S(false);
            break;
            case Cmd.s :
                S(true);
            break;
            case Cmd.v :
                V(parseFloat(1,characterStream),parseFloat(0,characterStream),parseFloat(3,characterStream),parseFloat(2,characterStream));
            break;
            case Cmd.Wstar :
                W(true);
            break;
            case Cmd.W :
                W(false);
            break;
            case Cmd.y :
                Y(parseFloat(1,characterStream),parseFloat(0,characterStream),parseFloat(3,characterStream),parseFloat(2,characterStream));
            break;
            case Cmd.re :
                RE(parseFloat(3,characterStream),parseFloat(2,characterStream),parseFloat(1,characterStream),parseFloat(0,characterStream));
            break;
            case Cmd.cm :
                //create temp Trm matrix to update Tm
                float[][] Trm = new float[3][3];

                //set Tm matrix
                Trm[0][0] = parseFloat(5,characterStream);
                Trm[0][1] = parseFloat(4,characterStream);
                Trm[0][2] = 0;
                Trm[1][0] = parseFloat(3,characterStream);
                Trm[1][1] = parseFloat(2,characterStream);
                Trm[1][2] = 0;
                Trm[2][0] = parseFloat(1,characterStream);
                Trm[2][1] = parseFloat(0,characterStream);
                Trm[2][2] = 1;

                CM(Trm);
            break;
            case Cmd.gs :
                gs(gs_state.get(generateOpAsString(0,characterStream)));
                break;
            case Cmd.i:
                I();
            break;
            case Cmd.J :
                J(false,Integer.parseInt(generateOpAsString(0,characterStream)));
            break;
            case Cmd.j :
                J(true,Integer.parseInt(generateOpAsString(0,characterStream)));
            break;
            case Cmd.q :
                Q(true);
            break;
            case Cmd.Q :
                Q(false);
            break;
            case Cmd.MP :
                MP();
                break;
            case Cmd.DP :
                DP(startCommand,  dataPointer, characterStream,generateOpAsString(0,characterStream));
            break;
            case Cmd.BDC :
                BDC(startCommand,  dataPointer, characterStream,generateOpAsString(0,characterStream));
                break;
            case Cmd.BMC :
                BMC(generateOpAsString(0,characterStream));
            break;
            case Cmd.d0 :
                d0((int) parseFloat(0,characterStream),(int) parseFloat(1,characterStream));
            break;
            case Cmd.d1 :
                d1(parseFloat(1,characterStream),
                        parseFloat(3,characterStream),
                        parseFloat(5,characterStream),
                        parseFloat(0,characterStream),
                        parseFloat(2,characterStream),
                        parseFloat(4,characterStream));
            break;
            case Cmd.EMC :
                EMC();
            break;
            case Cmd.BT :
                BT();
            break;
            case Cmd.ET :
                ET();
            break;
            case Cmd.Do :
                DO(generateOpAsString(0,characterStream));
            break;
            case Cmd.M:
                mm((int) (parseFloat(0,characterStream)));
            break;
            case Cmd.w:
                width(parseFloat(0,characterStream));
            break;

            }
        }

        //reset array of trailing values
        currentOp=0;
        operandCount=0;

        //increase pointer
        tokenNumber++;

        return dataPointer;

            }

    private void gs(Object values) {

        /**
         * set gs
         */
        currentGraphicsState.setMode(values);

        /**
         * align display
         */
        current.setGraphicsState(GraphicsState.FILL,currentGraphicsState.getNonStrokeAlpha());
        current.setGraphicsState(GraphicsState.STROKE,currentGraphicsState.getStrokeAlpha());

    }

    //////////////////////////////////////////////////////////////////////
    /**
     * process form or image - we must always process XForms becasue they may contain text
     */
    final private void DO(String name) throws PdfFontException, PdfException {

        Map currentValues;

        //lose the /
        name = name.substring(1);

        /**
         * ignore multiple overlapping images
         */
        if(rejectSuperimposedImages){

            if(imposedImages==null)
            imposedImages=new HashMap();

            String key=((int)currentGraphicsState.CTM[2][0])+"-"+((int)currentGraphicsState.CTM[2][1])+"-"+
            ((int)currentGraphicsState.CTM[0][0])+"-"+((int)currentGraphicsState.CTM[1][1])+"-"+
            ((int)currentGraphicsState.CTM[0][1])+"-"+((int)currentGraphicsState.CTM[1][0]);

            if(imposedImages.get(key)==null)
                imposedImages.put(key,"x");
            else
                return ;
        }
        
        //generate name including filename to make it unique less /
        currentImage = this.fileName + "-" + name;

        Object rawObject =localXObjects.get(name);
        if(rawObject==null)
            rawObject=currentXObjectValues.get(name);

        String objectRef=null;

        if(rawObject==null)
            currentValues=null;
        else if(rawObject instanceof Map)
            currentValues=(Map) rawObject;
        else{
            objectRef=(String)rawObject;
            currentValues = currentPdfFile.readObject(objectRef,false, null);
        }

        if(requestExit){
            exited=true;
            return;
        }

        try{


            if (currentValues != null) {

                String subtype = (String) currentValues.get("Subtype");
                if (subtype.equals("/Form")) {

                    if(this.xFormMetadata){

                        lastFormID=name;

                        //creat Map with just the values required
                        Map xFormData=new HashMap();
                        String[] requiredKeys={"OPI","BBox","Matrix"};

                        int count=requiredKeys.length;
                        for(int j=0;j<count;j++){
                            Object value=currentValues.get(requiredKeys[j]);

                            if(value!=null)
                                xFormData.put(requiredKeys[j],value);
                        }

                        Map newValues=new HashMap();
                        Map textFields=new HashMap();
                        textFields.put("F","x");

                        currentPdfFile.flattenValuesInObject(false,false,xFormData,newValues,textFields,null,objectRef);

                        this.pdfImages.setXformData(lastFormID, newValues);
                    }

                    if((!this.renderDirectly)&&(statusBar!=null))
                    statusBar.inSubroutine(true);

                    /**read the stream*/
                    byte[] objectData =currentPdfFile.readStream(currentValues,objectRef,true,true,keepRaw);

                    //reset operand
                    currentOp=0;
                    operandCount=0;

                    if(objectData!=null)
                    processXForm(currentValues, objectData);

                    if((!this.renderDirectly)&&(statusBar!=null))
                    statusBar.inSubroutine(false);

                    lastFormID=null;

                } else if (subtype.equals("/Image")) {

                    /**don't process unless needed*/
                    //<start-adobe>
					if (!markedContentExtracted && contentHandler!=null)
                        contentHandler.setImageName(name);
					//<end-adobe>

                    if((renderImages) | (clippedImagesExtracted)| (finalImagesExtracted) | (rawImagesExtracted)){

                        /**read the stream*/
                        byte[] objectData =currentPdfFile.readStream(currentValues,objectRef,true,true,keepRaw);

                        if(objectData!=null){

                            boolean alreadyCached=(useHiResImageForDisplay && current.isImageCached(this.pageNum));

                            BufferedImage image=null;

                            //process the image and save raw version
							if(!alreadyCached)
                                image =processImageXObject(name,currentValues,createScaledVersion,objectData);


                            if(requestExit){
                                exited=true;
                                return;
                            }

                            //save transformed image
                            if ((image != null)||(alreadyCached)){

                                if((renderDirectly)|(useHiResImageForDisplay)){

                                    if((PdfDecoder.isRunningOnMac)&&(!alreadyCached))
                                       image=clipForMac(image);
                                      
                                    if(requestExit){
                                        exited=true;
                                        return;
                                    }

                                    currentGraphicsState.x=currentGraphicsState.CTM[2][0];
                                    currentGraphicsState.y=currentGraphicsState.CTM[2][1];
                                    if(renderDirectly)
                                        current.renderImage(null,image,currentGraphicsState.getNonStrokeAlpha(),currentGraphicsState,g2,currentGraphicsState.x,currentGraphicsState.y);
                                    else  if (image!=null || alreadyCached)
                                        current.drawImage(pageNum,image,currentGraphicsState,alreadyCached,name);

                                }else{

                                    if(this.clippedImagesExtracted)
                                        generateTransformedImage(image,name);
									else{
                                        try{
                                        	generateTransformedImageSingle(image,name);
                                        }catch(Exception e){
                                        	LogWriter.writeLog("Exception "+e+" on tansforming image in file");
                                        }
									}
                                }

                                if(image!=null)
                                image.flush();
                            }

                            /**used to debug code*/

                        }
                    }
                } else {
                    LogWriter.writeLog("[PDF] " + subtype + " not supported");
                }
            }
        }catch(Error e){
            e.printStackTrace();
            imagesProcessedFully = false;
            addPageFailureMessage("Error "+e+" in DO with image "+currentValues+" isPrinting="+isPrinting+" useHiResImageForDisplay="+useHiResImageForDisplay);
        }

    }

    /**
     * routine to decode an XForm stream
     */
    private void processXForm(Map currentValues, byte[] formData) throws PdfFontException,PdfException {

        String matrix = currentPdfFile.getValue((String) currentValues.get("Matrix"));

        float[] matches={1f,0f,0f,1f,0f,0f};
        float[] transformMatrix=new float[6];

        /**
         * work through values and see if all match
         * exit on first failure
        */
        boolean isIdentity=true;// assume right and try to disprove

        if(matrix!=null){
            StringTokenizer match = new StringTokenizer(matrix,"[ ]");

            for(int ii=0;ii<6;ii++){

                //get value
                float value=Float.parseFloat(match.nextToken());

                transformMatrix[ii]=value;

                //see if it matches if not set flag
                if(value!=matches[ii]){
                    isIdentity=false;
                //	break;
                }
            }
        }

        float[][] CTM=null;

        if(matrix!=null && !isIdentity) {

            scalings.put(new Integer(formLevel),CTM);

            CTM=currentGraphicsState.CTM;

            float[][] scaleFactor={{transformMatrix[0],transformMatrix[1],0},
                    {transformMatrix[2],transformMatrix[3],0},
                    {transformMatrix[4],transformMatrix[5],1}};

            scaleFactor=Matrix.multiply(scaleFactor,CTM);
            currentGraphicsState.CTM=scaleFactor;
        }

        //track depth
        formLevel++;

        //preserve colorspaces
        GenericColorSpace mainStrokeColorData=(GenericColorSpace)strokeColorSpace.clone();
        GenericColorSpace mainnonStrokeColorData=(GenericColorSpace)nonstrokeColorSpace.clone();

        //preserve GS state
        Map old_gs_state=gs_state;
        gs_state=new HashMap();
        Iterator keys=old_gs_state.keySet().iterator();
        while(keys.hasNext()){
            Object key=keys.next();
            gs_state.put(key,old_gs_state.get(key));
        }

        //preserve fonts
        Map rawFonts=fonts;
        fonts=new HashMap();

        /**read any resources*/
        Map resValue = this.currentPdfFile.getSubDictionary(currentValues.get("Resources"));
        if (resValue != null)
        readResources(false,resValue,false);

        /**decode the stream*/
        if(formData.length>0)
        decodeStreamIntoObjects(formData);

        formLevel--;

        //restore old matrix
        CTM=(float[][]) scalings.get(new Integer(formLevel));
        if(CTM!=null)
            currentGraphicsState.CTM=CTM;

        //flush local refs if duplicates
        if(formLevel==0)
        localXObjects.clear();


        /**restore old colorspace and fonts*/
        strokeColorSpace=mainStrokeColorData;
        nonstrokeColorSpace=mainnonStrokeColorData;
        fonts=rawFonts;
        gs_state=old_gs_state;

    }
    ////////////////////////////////////////////////////////
    /**
     * save the current image, clipping and resizing. Id reparse, we don't
     * need to repeat some actions we know already done.
     */
    final private void generateTransformedImageSingle(BufferedImage image,String image_name) {

        LogWriter.writeMethod("{generateTransformedImageSingle}", 0);

        float x = 0, y = 0, w = 0, h = 0;

        //if valid image then process
        if (image != null) {

            // get clipped image and co-ords
            Area clipping_shape = currentGraphicsState.getClippingShape();

            /**
             * scale the raw image to correct page size (at 72dpi)
             */
            //object to scale and clip. Creating instance does the scaling
            ImageTransformer image_transformation;

            //object to scale and clip. Creating instance does the scaling
            image_transformation =new ImageTransformer(PdfDecoder.dpi,currentGraphicsState,image,true,PdfDecoder.isDraft);

            if(requestExit){
                exited=true;
                return;
            }

            //get initial values
            x = image_transformation.getImageX();
            y = image_transformation.getImageY();
            w = image_transformation.getImageW();
            h = image_transformation.getImageH();

            //get back image, which will become null if TOO small
            image = image_transformation.getImage();

            //apply clip as well if exists and not inline image
            if ((image != null) && (clipping_shape != null)&&(clipping_shape.getBounds().getWidth()>1)&&(clipping_shape.getBounds().getHeight()>1)&&
                    (customImageHandler!=null)&&(!customImageHandler.imageHasBeenScaled())) {

                //see if clip is wider than image and ignore if so
                boolean ignore_image = clipping_shape.contains(x, y, w, h);

                if (ignore_image == false) {
                    //do the clipping
                    image_transformation.clipImage(clipping_shape);

                    //get ALTERED values
                    x = image_transformation.getImageX();
                    y = image_transformation.getImageY();
                    w = image_transformation.getImageW();
                    h = image_transformation.getImageH();
                }
            }

            //alter image to allow for way we draw 'upside down'
            image = image_transformation.getImage();

            image_transformation = null; //flush

            if(requestExit){
                exited=true;
                return;
            }

            //allow for null image returned (ie if too small)
            if (image != null) {

                /**turn correct way round if needed*/
                //if((currentGraphicsState.CTM[0][1]!=0 )&&(currentGraphicsState.CTM[1][0]!=0 )&&(currentGraphicsState.CTM[0][0]>=0 )){

                /*if((currentGraphicsState.CTM[0][1]>0 )&&(currentGraphicsState.CTM[1][0]>0 )&&(currentGraphicsState.CTM[0][0]>=0 )){
                        double dx=1,dy=1,scaleX=0,scaleY=0;

                        if(currentGraphicsState.CTM[0][1]>0){
                            dx=-1;
                            scaleX=image.getWidth();
                        }
                        if(currentGraphicsState.CTM[1][0]>0){
                            dy=-1;
                            scaleY=image.getHeight();
                        }

                        AffineTransform image_at =new AffineTransform();
                        image_at.scale(dx,dy);
                        image_at.translate(-scaleX,-scaleY);
                        AffineTransformOp invert= new AffineTransformOp(image_at,  ColorSpaces.hints);
                        image = invert.filter(image,null);


                    }
                    */
                //store  final image on disk & in memory
                if((finalImagesExtracted)| (rawImagesExtracted)){
                    pdfImages.setImageInfo(currentImage, pageNum, x, y, w, h,lastFormID);

                    if(includeImagesInData){

                        float xx=x;
                        float yy=y;

                        if(clipping_shape!=null){

                            int minX=(int)clipping_shape.getBounds().getMinX();
                            int maxX=(int)clipping_shape.getBounds().getMaxX();

                            int minY=(int)clipping_shape.getBounds().getMinY();
                            int maxY=(int)clipping_shape.getBounds().getMaxY();

                            if((xx>0 && xx<minX)||(xx<0))
                            xx=minX;

                            float currentW=xx+w;
                            if(xx<0)
                            currentW=w;
                            if(maxX<(currentW))
                                w=maxX-xx;

                            if(yy>0 && yy<minY)
                            yy=minY;

                            if(maxY<(yy+h))
                                h=maxY-yy;

                        }

                        pdfData.addImageElement(xx,yy,w,h,currentImage);
                    }

                }
                //add to screen being drawn
				if ((renderImages) || (!isPageContent)) {

                    //check it is not null
                    if (image != null) {
                        currentGraphicsState.x=x;
                        currentGraphicsState.y=y;

                        if(renderDirectly)
                            current.renderImage(null,image,currentGraphicsState.getNonStrokeAlpha(),currentGraphicsState,g2,currentGraphicsState.x,currentGraphicsState.y);
                        else
                            current.drawImage(pageNum,image,currentGraphicsState,false,image_name);

                    }
				}

                /**save if required*/
                if( (isPageContent)& (finalImagesExtracted)) {


                    if (PdfDecoder.inDemo) {
                        int cw = image.getWidth();
                        int ch = image.getHeight();

                        Graphics2D g2 = image.createGraphics();
                        g2.setColor(Color.red);
                        g2.drawLine(0, 0, cw, ch);
                        g2.drawLine(0, ch, cw, 0);
                    }

                    //save the scaled/clipped version of image if allowed
                    if(currentPdfFile.isExtractionAllowed() && !PdfStreamDecoder.runningStoryPad){

                        String image_type = objectStoreStreamRef.getImageType(currentImage);
                        objectStoreStreamRef.saveStoredImage(
                                currentImage,
                                addBackgroundToMask(image),
                                false,
                                false,
                                image_type);
                    }
            }
			}
        } else
            //flag no image and reset clip
            LogWriter.writeLog("NO image written");


    }

    private BufferedImage addBackgroundToMask(BufferedImage image) {
        if(isMask){

            int cw = image.getWidth();
            int ch = image.getHeight();

            BufferedImage background=new BufferedImage(cw,ch,BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = background.createGraphics();
            g2.setColor(Color.white);
            g2.fillRect(0, 0, cw, ch);
            g2.drawImage(image,0,0,null);
            image=background;

        }
        return image;
    }


    /**
     * pass in status bar object
     *
     */
    public void setStatusBar(StatusBar statusBar){
        this.statusBar=statusBar;
    }

    /**
     * clip image as MAC has nasty bug :-(
     */
    final private BufferedImage clipForMac(BufferedImage image) {

        LogWriter.writeMethod("{clipForMac}", 0);

        //if valid image then process
        if ((image != null)) {

            /**
             * scale the raw image to correct page size (at 72dpi)
             */

            //object to scale and clip. Creating instance does the scaling
            ImageTransformerDouble image_transformation =new ImageTransformerDouble(PdfDecoder.dpi,currentGraphicsState,image,createScaledVersion,false);

            if(requestExit){
                exited=true;
                return null;
            }
            //extract images either scaled/clipped or scaled then clipped

            image_transformation.doubleScaleTransformShear(true);

            if(requestExit){
                exited=true;
                return null;
            }

            //get intermediat eimage and save
            image = image_transformation.getImage();

        }
        return image;
    }


    /**
     * save the current image, clipping and resizing. This gives us a
     * clipped hires copy. In reparse, we don't
     * need to repeat some actions we know already done.
     */
    final private void generateTransformedImage(BufferedImage image,String image_name) {

        LogWriter.writeMethod("{generateTransformedImage}", 0);

        float x = 0, y = 0, w = 0, h = 0;

        //if valid image then process
        if ((image != null)) {

            /**
             * scale the raw image to correct page size (at 72dpi)
             */

            //object to scale and clip. Creating instance does the scaling
            ImageTransformerDouble image_transformation =new ImageTransformerDouble(PdfDecoder.dpi,currentGraphicsState,image,createScaledVersion,true);

            //extract images either scaled/clipped or scaled then clipped

            image_transformation.doubleScaleTransformShear(false);


            if(requestExit){
                exited=true;
                return;
            }

            //get intermediate image and save
            image = image_transformation.getImage();

            //save the scaled/clipped version of image if allowed
            {//if(currentPdfFile.isExtractionAllowed()){

                /**make sure the right way*/
/*
				int dx=1,dy=1,iw=0,ih=0;
				if(currentGraphicsState.CTM[0][0]<0){
					dx=-dx;
					iw=image.getWidth();
				}

				if(currentGraphicsState.CTM[1][1]<0){
					dy=-dy;
					ih=image.getHeight();
				}
				if((dy<0)|(dx<0)){

					AffineTransform image_at =new AffineTransform();
					image_at.scale(dx,dy);
					image_at.translate(-iw,-ih);
					AffineTransformOp invert= new AffineTransformOp(image_at,  ColorSpaces.hints);
					image = invert.filter(image,null);

				}

*/

                String image_type = objectStoreStreamRef.getImageType(currentImage);
                if(image_type==null)
                    image_type="tif";

                if (PdfDecoder.inDemo) {
                    Graphics2D g2 = image.createGraphics();
                    g2.setColor(Color.red);
                    int cw = image.getWidth();
                    int ch = image.getHeight();
                    g2.drawLine(0, 0, cw, ch);
                    g2.drawLine(0, ch, cw, 0);
                }

                if(requestExit){
                    exited=true;
                    return;
                }

                if(objectStoreStreamRef.saveStoredImage(
                        "CLIP_"+currentImage,
                        addBackgroundToMask(image),
                        false,
                        false,
                        image_type))
                    addPageFailureMessage("Problem saving "+image);

            }

            if((finalImagesExtracted)|(renderImages))
                image_transformation.doubleScaleTransformScale();

            //complete the image and workout co-ordinates
            image_transformation.completeImage();

            //get initial values
            x = image_transformation.getImageX();
            y = image_transformation.getImageY();
            w = image_transformation.getImageW();
            h = image_transformation.getImageH();

            //get final image to allow for way we draw 'upside down'
            image = image_transformation.getImage();

            image_transformation = null; //flush

            //allow for null image returned (ie if too small)
            if (image != null) {

                //store  final image on disk & in memory
                if((finalImagesExtracted)| (clippedImagesExtracted)|(rawImagesExtracted)){
                    pdfImages.setImageInfo(currentImage, pageNum, x, y, w, h,lastFormID);

                    if(includeImagesInData)
                        pdfData.addImageElement(x,y,w,h,currentImage);

                }

                //add to screen being drawn
                if ((renderImages) || (!isPageContent)) {

                    //check it is not null
                    if (image != null) {
                        currentGraphicsState.x=x;
                        currentGraphicsState.y=y;

                        if(renderDirectly)
                            current.renderImage(null,image,currentGraphicsState.getNonStrokeAlpha(),currentGraphicsState,g2,currentGraphicsState.x,currentGraphicsState.y);
                        else
                            current.drawImage(pageNum,image,currentGraphicsState,false,image_name);

                    }
                }

                /**used to debug code by popping up window after glyph*
                 Object[] options = { "OK" };
                 int n =JOptionPane.showOptionDialog(null,null,"Storypad",JOptionPane.OK_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,options[0]);
                 /***/

                /**save if required*/
                if( (!renderDirectly)&&(isPageContent)&& (finalImagesExtracted)) {

                    if (PdfDecoder.inDemo) {
                        Graphics2D g2 = image.createGraphics();
                        g2.setColor(Color.red);
                        int cw = image.getWidth();
                        int ch = image.getHeight();
                        g2.drawLine(0, 0, cw, ch);
                        g2.drawLine(0, ch, cw, 0);
                    }

                    //save the scaled/clipped version of image if allowed
                    if(currentPdfFile.isExtractionAllowed()){
                        String image_type = objectStoreStreamRef.getImageType(currentImage);
                        objectStoreStreamRef.saveStoredImage(
                            currentImage,
                            addBackgroundToMask(image),
                            false,
                            false,
                            image_type);
                    }
                }

            }
        } else
            //flag no image and reset clip
            LogWriter.writeLog("NO image written");

    }

    /**
     * turn TJ into string and plot. THis routine is long but requently called so we want all code 'inlined'
     */
    final private StringBuffer processTextArray(byte[] stream,int startCommand,int dataPointer) {

        //flag text found as opposed to just spacing
        boolean hasContent=false;

        boolean isMultiple=false;
        
        boolean firstTime=true;

        //roll on at start if necessary
        while((stream[startCommand]==91)||(stream[startCommand]==10)||(stream[startCommand]==13)||(stream[startCommand]==32)){

            if(stream[startCommand]==91)
                isMultiple=true;

            startCommand++;
        }

        //set threshold - value indicates several possible values
        float currentThreshold=PdfStreamDecoder.currentThreshold;
        if(currentThreshold<0){
            
            Float specificSetting=(Float)PdfStreamDecoder.currentThresholdValues.get(currentFontData.getFontName());

            if(specificSetting==null) //use default
                currentThreshold=-currentThreshold;
            else //use specific
                currentThreshold=specificSetting.floatValue();

        }

        /**reset global variables and initialise local ones*/
        textLength = 0;
        int Tmode=currentGraphicsState.getTextRenderType();
        //int foreground =((Color)nonstrokeColorSpace.getColor()).getRGB();
        int orientation=0; //show if horizontal or vertical text and running which way using constants in PdfData
        boolean isHorizontal=true,inText = false;
        float[][] Trm = new float[3][3];
        float[][] temp = new float[3][3];
        float[][] TrmBeforeSpace = new float[3][3];
        char rawChar = ' ', nextChar, lastChar = ' ', openChar = ' ', lastTextChar = 'x';
        int fontSize = 0, rawInt = 0;
        float width = 0,fontScale = 1,lastWidth = 0,currentWidth = 0,leading = 0;
        String displayValue = "";
        float TFS = currentTextState.getTfs();
        float rawTFS=TFS;
        
        if(TFS<0)
            TFS=-TFS;

        int type=currentFontData.getFontType();

        float spaceWidth = currentFontData.getCurrentFontSpaceWidth();
        String unicodeValue="";
        textData = new StringBuffer(50); //used to return a value

        float currentGap=0;

        boolean isCID = currentFontData.isCIDFont();

        /**set colors*/
		if((renderText)&&(Tmode!=GraphicsState.INVISIBLE)){
            currentGraphicsState.setStrokeColor(strokeColorSpace.getColor());
            currentGraphicsState.setNonstrokeColor(nonstrokeColorSpace.getColor());
		}

        /**set character size */
        int charSize=2;
        if(isCID)
            charSize=4;

        /** create temp matrix for current text location and factor in scaling*/
        Trm = Matrix.multiply(currentTextState.Tm, currentGraphicsState.CTM);
        Trm[0][0]=Trm[0][0];
        Trm[0][1]=Trm[0][1];
        Trm[1][0]=Trm[1][0];
        Trm[1][1]=Trm[1][1];

        //adjust for negative TFS
        if(rawTFS<0){
                Trm[2][0]=Trm[2][0]-(Trm[0][0]/2);
                Trm[2][1]=Trm[2][1]-(Trm[1][1]/2);
        }

        charSpacing = currentTextState.getCharacterSpacing() / TFS;
        float wordSpacing = currentTextState.getWordSpacing() / TFS;
        //<end-13><end-14>

        if(multipleTJs){ //allow for consecutive TJ commands
            Trm[2][0]=currentTextState.Tm[2][0];
            Trm[2][1]=currentTextState.Tm[2][1];
        }

        /**define matrix used for converting to correctly scaled matrix and multiply to set Trm*/
        temp[0][0] = rawTFS * currentTextState.getHorizontalScaling();
        temp[1][1] = rawTFS;
        temp[2][1] = currentTextState.getTextRise();
        temp[2][2] =1;
        Trm = Matrix.multiply(temp, Trm);

        //check for leading before text
        if((isMultiple) &&(stream[startCommand]!=60) &&(stream[startCommand]!=40) &&(stream[startCommand]!=93)){

            float offset=0;
            while((stream[startCommand]!=40)){
                StringBuffer kerning=new StringBuffer();
                while((stream[startCommand]!=40)&&(stream[startCommand]!=32)){
                    kerning.append((char)stream[startCommand]);
                    startCommand++;
                }
                offset=offset+Float.parseFloat(kerning.toString());

                while(stream[startCommand]==32)
                    startCommand++;
            }
            offset=Trm[0][0]*offset/THOUSAND;

            Trm[2][0]=Trm[2][0]-offset;

        }


        multipleTJs=true; //flag will be reset by Td/Tj/T* if move takes place.

        /**workout if horizontal or vertical plot and set values*/
        if (Trm[1][1] != 0) {
            isHorizontal=true;
            orientation = PdfData.HORIZONTAL_LEFT_TO_RIGHT;

            fontSize = Math.round(Trm[1][1] );
            if(fontSize==0)
                fontSize = Math.round(Trm[0][1] );

            fontScale = Trm[0][0];

        } else {

            isHorizontal=false;
            fontSize = Math.round(Trm[1][0] );

            if(fontSize==0)
                fontSize = Math.round(Trm[0][0] );

            if(fontSize<0){
                fontSize=-fontSize;
                orientation = PdfData.VERTICAL_BOTTOM_TO_TOP;
            }else
                orientation = PdfData.VERTICAL_TOP_TO_BOTTOM;
            fontScale = Trm[0][1];
        }

        if(fontSize==0)
            fontSize=1;

        /**
         * text printing mode to get around problems with PCL printers
         */
        Font javaFont=null;

        if((textPrint!=PdfDecoder.NOTEXTPRINT)&&(isPrinting))
            javaFont=currentFontData.getJavaFontX(fontSize);

        /**extract starting x and y values (we update Trm as we work through text)*/
        float x = Trm[2][0];
        float y = Trm[2][1];

        /**set max height for CID of guess sensble figure for non-CID*/
        float max_height = fontSize ;

        if (isCID)
        max_height = Trm[1][1];

        /**now work through all glyphs and render/decode*/
        int i = startCommand;

        int numOfPrefixes=0;
        while (i < dataPointer) {

            //extract the next binary index value and convert to char, losing any returns
            while(true){
                if((lastChar==92)&&(rawChar==92))//checks if \ has been escaped in '\\'=92
                    lastChar=120;
                else
                    lastChar = rawChar;

                rawInt = stream[i];
                if (rawInt < 0)
                    rawInt = 256 + rawInt;
                rawChar = (char) rawInt;

                //eliminate escaped tabs and returns
                if((rawChar==92)&&((stream[i+1]==13)|(stream[i+1]==10))){ // '\\'=92
                    i++;
                    rawInt = stream[i];
                    if (rawInt < 0)
                        rawInt = 256 + rawInt;
                    rawChar = (char) rawInt;
                }

                //stop any returns in data stream getting through (happens in ghostscript)
                if((rawChar!=10)&(rawChar!=13))
                    break;

                i++;
            }

            /**flag if we have enetered/exited text block*/
            if (inText) {
                //non CID deliminator (allow for escaped deliminator)
                if ( (lastChar != 92) && ((rawChar==40) || (rawChar==41))) {  // '\\'=92 ')'=41
                    if(rawChar==40){
                        numOfPrefixes++;
                    }else if(rawChar==41){ //')'=41
                        if(numOfPrefixes<=0){
                    inText = false; //unset text flag
                        }else {
                            numOfPrefixes--;
                        }
                    }
                } else if ((openChar == 60) && (rawChar == 62))  // ie <01>tj  '<'=60 '<'=62
                    inText = false; //unset text flag
            }

            /**either handle glyph, process leading or handle a deliminator*/
            if (inText) { //process if still in text

                lastTextChar = rawChar; //remember last char so we can avoid a rollon at end if its a space

                //convert escape or turn index into correct glyph allow for stream
                if ((openChar == 60)) { //'<'=60

                    //get the hex value
                    StringBuffer hexString = new StringBuffer(4);
                    hexString.append(rawChar);

                    for (int i2 = 1; i2 < charSize; i2++) {
                        int nextInt = stream[i + i2];

                        if(nextInt==62){ //allow for less than 4 chars at end of stream (ie 6c>)
                            i2=4;
                            charSize=2;
                        }else if((nextInt==10)|(nextInt==13)){ //avoid any returns
                            i++;
                            i2--;
                        }else{
                            if (nextInt < 0)
                                nextInt = 256 + nextInt;
                            rawChar = (char) nextInt;
                            hexString.append(rawChar);
                        }
                    }

                    i = i + charSize-1; //move offset

                    //convert to value
                    rawInt = Integer.parseInt(hexString.toString(), 16);
                    rawChar = (char) rawInt;
                    displayValue =currentFontData.getGlyphValue(rawInt);

                    if(textExtracted)
                        unicodeValue =currentFontData.getUnicodeValue(displayValue,rawInt);

				} else if ((isCID)&&(currentFontData.isDoubleByte())){  //could be nonCID cid

                    if(rawChar==92){ // '\\'=92

                        //extract the next binary index value and convert to char, losing any returns
                        while(true){
                            //lastChar = rawChar;
                            rawInt = stream[i];
                            if (rawInt < 0)
                                rawInt = 256 + rawInt;
                            rawChar = (char) rawInt;

                            if (rawInt < 0)
                                rawInt = 256 + rawInt;
                            rawChar = (char) rawInt;
                            //handle escaped chars
                            if(rawInt==92){
                                i++;
                                rawInt = stream[i];
                                rawChar=(char)rawInt;

                                if(rawChar=='n'){
                                    rawInt='\n';
                                }else if(rawChar=='b'){
                                    rawInt='\b';
                                }else if(rawChar=='t'){
                                    rawInt='\t';
                                }else if(rawChar=='r'){
                                    rawInt='\r';
                                }else if(rawChar=='f'){
                                    rawInt='\f';
                                }  else if ((stream.length > (i + 2))&& (Character.isDigit((char) stream[i]))){

                                    //see how long number is
                                    int numberCount=1;
                                    if(Character.isDigit((char) stream[i + 1])){
                                        numberCount++;
                                        if(Character.isDigit((char) stream[i + 2]))
                                            numberCount++;
                                    }

                                    // convert octal escapes
                                    rawInt = readEscapeValue(i, numberCount, 8, stream);
                                    i = i + numberCount-1;

                                }
                            }

                            //eliminate escaped tabs and returns
                            if((rawChar!=10)&(rawChar!=13))
                                break;

                            i++;
                        }
                    }

                    {

                        i++;
                        //extract the next binary index value and convert to char, losing any returns
                        int nextInt = stream[i];

                        if (nextInt < 0)
                            nextInt = 256 + nextInt;

                        //handle escaped chars
                        if(nextInt==92){
                            i++;
                            nextInt = stream[i];
                            rawChar=(char)nextInt;
                            if(rawChar=='n'){
                                nextInt='\n';
                            }else if(rawChar=='b'){
                                nextInt='\b';
                            }else if(rawChar=='t'){
                                nextInt='\t';
                            }else if(rawChar=='r'){
                                nextInt='\r';
                            }else if(rawChar=='f'){
                                nextInt='\f';
                            }  else if ((stream.length > (i + 2))&& (Character.isDigit((char) stream[i]))){

                                    //see how long number is
                                    int numberCount=1;
                                    if(Character.isDigit((char) stream[i + 1])){
                                        numberCount++;
                                        if(Character.isDigit((char) stream[i + 2]))
                                            numberCount++;
                                    }

                                    // convert octal escapes
                                    nextInt = readEscapeValue(i, numberCount, 8, stream);
                                    i = i + numberCount-1;

                                }
                        }

                        rawInt=(rawInt*256)+nextInt;
                    }

                    rawChar = (char) rawInt;

                    displayValue = String.valueOf(rawChar);
                    unicodeValue =currentFontData.getUnicodeValue(displayValue,rawInt);

                    //fix for \\) at end of stream
                    if(rawChar==92)
                    rawChar=120;

                }else if ((rawChar == 92)) { // any escape chars '\\'=92

                    i++;
                    lastChar=rawChar;//update last char as escape
                    rawInt = stream[i];
                    rawChar = (char) rawInt;

                    if ((stream.length > (i + 2))&& (Character.isDigit((char) stream[i]))){

                        //see how long number is
                        int numberCount=1;
                        if(Character.isDigit((char) stream[i + 1])){
                            numberCount++;
                            if(Character.isDigit((char) stream[i + 2]))
                                numberCount++;
                        }

                        // convert octal escapes
                        rawInt = readEscapeValue(i, numberCount, 8, stream);
                        i = i + numberCount-1;

                        if(rawInt>255)
                            rawInt=rawInt-256;

                        displayValue=currentFontData.getGlyphValue(rawInt);

                        if(textExtracted)
                            unicodeValue=currentFontData.getUnicodeValue(displayValue,rawInt);

                        rawChar =(char)rawInt; //set to dummy value as may be / value

                        //allow for \134 (ie \\)
                        if(rawChar==92) // '\\'=92
                            rawChar=120;

                    } else {

                        rawInt = stream[i];
                        rawChar = (char) rawInt;

                        if (rawChar == 'u') { //convert unicode of format uxxxx to char value
                            rawInt =readEscapeValue(i + 1, 4, 16, stream);
                            i = i + 4;
                            //rawChar = (char) rawInt;
                            displayValue =currentFontData.getGlyphValue(rawInt);
                            if(textExtracted)
                                unicodeValue =currentFontData.getUnicodeValue(displayValue,rawInt);

                        } else {

                            if(rawChar=='n'){
                                rawInt='\n';
                                rawChar='\n';
                            }else if(rawChar=='b'){
                                rawInt='\b';
                                rawChar='\b';
                            }else if(rawChar=='t'){
                                rawInt='\t';
                                rawChar='\t';
                            }else if(rawChar=='r'){
                                rawInt='\r';
                                rawChar='\r';
                            }else if(rawChar=='f'){
                                rawInt='\f';
                                rawChar='\f';
                            }

                            displayValue =currentFontData.getGlyphValue(rawInt);
                            if(textExtracted)
                                unicodeValue =currentFontData.getUnicodeValue(displayValue,rawInt);
                            if (displayValue.length() > 0) //set raw char
                                rawChar = displayValue.charAt(0);
                        }
                    }
                } else if (isCID){  //could be nonCID cid
                    displayValue = String.valueOf(rawChar);
                    unicodeValue=displayValue;
                }else{

                    displayValue =currentFontData.getGlyphValue(rawInt);

                    if(textExtracted)
                        unicodeValue =currentFontData.getUnicodeValue(displayValue,rawInt);
                }

                //MOVE pointer to next location by updating matrix
                temp[0][0] = 1;
                temp[0][1] = 0;
                temp[0][2] = 0;
                temp[1][0] = 0;
                temp[1][1] = 1;
                temp[1][2] = 0;
                temp[2][0] = (currentWidth + leading); //tx;
                temp[2][1] = 0; //ty;
                temp[2][2] = 1;
                Trm = Matrix.multiply(temp, Trm); //multiply to get new Tm

                /**save pointer in case its just multiple spaces at end*/
                if ((rawChar == ' ') && (lastChar != ' '))
                    TrmBeforeSpace = Trm;

                leading = 0; //reset leading

//                	System.out.println("=========="+" "+rawInt+" "+displayValue+" "+unicodeValue+" "+currentFontData+" "+currentFontData.getFontName());
                
                currentWidth = currentFontData.getWidth(rawInt);
                
                PdfJavaGlyphs glyphs =currentFontData.getGlyphData();

				/**if we have a valid character and we are rendering, draw it */
                if ((renderText)&&(Tmode!=GraphicsState.INVISIBLE)){

                    if((isPrinting)&&(javaFont!=null)&&(textPrint==PdfDecoder.TEXTSTRINGPRINT)){

                        /**support for TR7*/
                        if(Tmode==GraphicsState.CLIPTEXT){

                            /**set values used if rendering as well*/
                            Area transformedGlyph2;

                            if((PdfDecoder.isRunningOnMac)|(StandardFonts.isStandardFont(currentFontData.getBaseFontName())))
                                transformedGlyph2= glyphs.getStandardGlyph(Trm, rawInt, displayValue, currentWidth);
                            else
								transformedGlyph2= glyphs.getApproximateGlyph(Trm, rawInt, displayValue, currentWidth);

                            if(transformedGlyph2!=null)
                                    currentGraphicsState.addClip(transformedGlyph2);

                        }

                        current.drawText(Trm,displayValue,currentGraphicsState,Trm[2][0],-Trm[2][1],javaFont);
					}else if(((textPrint!=PdfDecoder.TEXTGLYPHPRINT)||(javaFont==null))&&(currentFontData.isFontEmbedded)){

                        //get glyph if not CID
                        String charGlyph="notdef";

                        try{

                            if(!currentFontData.isCIDFont())
                            charGlyph=currentFontData.getMappedChar(rawInt,false);

                            PdfGlyph glyph= null;

                               glyph= glyphs.getEmbeddedGlyph( factory,charGlyph ,Trm, rawInt, displayValue, currentWidth,currentFontData.getEmbeddedChar(rawInt));


							//avoid null type 3 glyphs and set color if needed
                            if(type==StandardFonts.TYPE3){

                                if(glyph!=null){
                                    float h=(float) (fontSize*currentFontData.FontMatrix[3]*glyph.getmaxHeight());
                                    if(h>max_height)
                                    max_height=h;
                                }
                                
                                if((glyph!=null)&&(glyph.getmaxWidth()==0))
                                    glyph=null;
                                else if(glyph!=null && glyph.ignoreColors()){

                                    glyph.lockColors(currentGraphicsState.getNonstrokeColor(),currentGraphicsState.getNonstrokeColor());
                                }
                            }
                            
                            if(glyph!=null){

                            	float[][] finalTrm={{Trm[0][0],Trm[0][1],0},
                            			{Trm[1][0],Trm[1][1] ,0},
                            			{Trm[2][0],Trm[2][1],1}};
                            	
                            	float[][] finalScale={{(float) currentFontData.FontMatrix[0],(float)currentFontData.FontMatrix[1],0},
                            			{(float) currentFontData.FontMatrix[2],(float) currentFontData.FontMatrix[3],0},
                            			{0,0,1}};
                            	
                            	//factor in fontmatrix (which may include italic)
                            	finalTrm=Matrix.multiply(finalTrm, finalScale);
                            	
                            	finalTrm[2][0]=Trm[2][0];
                            	finalTrm[2][1]=Trm[2][1];
                            	
                            	//manipulate matrix to get right rotation
                            	if(finalTrm[1][0]<0 && finalTrm[0][1]<0){
                            		finalTrm[1][0]=-finalTrm[1][0];
                            		finalTrm[0][1]=-finalTrm[0][1];
                            	}
                            	
                            	//line width
                                float lineWidth=0;//currentGraphicsState.getLineWidth();
                                //if(Trm[0][0]!=0)
                                //	lineWidth=lineWidth*Trm[0][0];
                        		//else if( Trm[0][1]!=0)
                        		//	lineWidth=lineWidth*Trm[0][1];
                        		//if(lineWidth<0)
                        		//	lineWidth=-lineWidth;
                        		
                        		if(lineWidth>0){
                        			
                        			//System.out.println(currentFontData.getBaseFontName());
                        			//System.out.println(displayValue+"------------------"+Trm[0][0]+" "+Trm[0][1]+" "+Trm[1][1]+" "+" width="+currentGraphicsState.getLineWidth()+" type="+currentGraphicsState.getTextRenderType());
                        			
//                        			if(((int)Trm[0][0])==57){
//                        				
//                        				//if(lineWidth<7)
//                        					//System.exit(1);
//                        				
//                        				lineWidth=104;
//                        			}else if(((int)Trm[0][0])==86){
//                        				lineWidth=115;
//                        			}else if(((int)Trm[0][0])==32){
//                        				lineWidth=215;	
//                        			}else if(((int)Trm[0][0])==19){
//                        				lineWidth=104;		
//                        			}else{
//                            			lineWidth=0;
//                            		}
                        			
                        			if(lineWidth>0){
                        				Matrix.show(currentGraphicsState.CTM);
                            			System.out.println(lineWidth+" "+displayValue+"------------------"+Trm[0][0]+" "+Trm[0][1]+" "+Trm[1][0]+" "+Trm[1][1]+" "+" width="+currentGraphicsState.getLineWidth()+" type="+currentGraphicsState.getTextRenderType()
                            					+" "+currentFontData.FontMatrix[0]+" "+currentFontData.FontMatrix[1]+" "+currentFontData.FontMatrix[2]+" "+currentFontData.FontMatrix[3]);
                        			}
                        		}else
                        			lineWidth=0;
                        	
								//create shape for text using tranformation to make correct size
                                AffineTransform at=new AffineTransform(finalTrm[0][0],finalTrm[0][1],finalTrm[1][0],finalTrm[1][1] ,finalTrm[2][0],finalTrm[2][1]);
//                                AffineTransform at=new AffineTransform(Trm[0][0],Trm[0][1],Trm[1][0],Trm[1][1] ,Trm[2][0],Trm[2][1]);
//
//                                if((type==StandardFonts.TYPE3)&&(renderDirectly)&& currentWidth!=0) //allow for rotated text with no width
//                                	at.scale((currentWidth/glyph.getmaxWidth()),currentFontData.FontMatrix[3]);
//                                else
//                                   at.scale(currentFontData.FontMatrix[0],currentFontData.FontMatrix[3]);
                                
                                //add to renderer
                                int fontType=type;
                                if(type==StandardFonts.OPENTYPE){
                                    fontType=DynamicVectorRenderer.TYPE1C;
                                    
                                    //and fix for scaling in OTF
                                    float z=1000f/((float)glyph.getmaxWidth());
                                    at.scale(currentWidth*z, 1);
                                    
                                }else if((type==StandardFonts.TRUETYPE)||(type==StandardFonts.CIDTYPE2)||(currentFontData.isFontSubstituted())){
                                    fontType=DynamicVectorRenderer.TRUETYPE;
                                }else if(type==StandardFonts.TYPE3){
                                    fontType=DynamicVectorRenderer.TYPE3;
                                }else{
                                    fontType=DynamicVectorRenderer.TYPE1C;
                                }

                                //negative as flag to show we need to decode later
                                if(generateGlyphOnRender)
                                        fontType=-fontType;

                                /**
                                 * add glyph outline to shape in TR7 mode
                                 */
                                if((Tmode==GraphicsState.CLIPTEXT)){

                                    Area glyphShape=glyph.getShape();
                                    if(glyphShape!=null){
                                        glyphShape.transform(at);
                                        currentGraphicsState.addClip(glyphShape);
                                    }
                                }
                                
                                if(renderDirectly){

                                    PdfPaint strokeCol=null,fillCol=null;
                                    int text_fill_type = currentGraphicsState.getTextRenderType();

                                    //for a fill
                                    if ((text_fill_type & GraphicsState.FILL) == GraphicsState.FILL)

                                        fillCol=currentGraphicsState.getNonstrokeColor();

                                    //and/or do a stroke
                                    if ((text_fill_type & GraphicsState.STROKE) == GraphicsState.STROKE)
                                        strokeCol=currentGraphicsState.getStrokeColor();

                                    //set the stroke to current value
                                    Stroke newStroke=currentGraphicsState.getStroke();
                                    g2.setStroke(newStroke);

                                    
                                    current.renderEmbeddedText(text_fill_type,glyph,fontType,g2,
                                    		at,false,strokeCol,fillCol,
											currentGraphicsState.getStrokeAlpha(),
											currentGraphicsState.getNonStrokeAlpha(),null,(int)lineWidth) ;
								}else
                                    current.drawEmbeddedText( Trm,fontSize,glyph,fontType,currentGraphicsState,at,lineWidth);
                            }                            
                        } catch (Exception e) {

                            addPageFailureMessage("Exception "+e+" on embedded font renderer");

                        }

                    }else if((displayValue.length() > 0) && (!displayValue.startsWith("&#"))){

                        //substitute ellipsis
                        if(displayValue.equals("..."))
                            displayValue=ellipsis;

                        /**set values used if rendering as well*/
                        Object transformedGlyph2=null;

						{ //render now

                            Area glyph;
                            if((PdfDecoder.isRunningOnMac)|(StandardFonts.isStandardFont(currentFontData.getBaseFontName())))
                                glyph= glyphs.getStandardGlyph(Trm, rawInt, displayValue, currentWidth);
                            else
								glyph= glyphs.getApproximateGlyph(Trm, rawInt, displayValue, currentWidth);

                            if(glyph!=null){
                                   //if its already generated we just need to move it
                                   AffineTransform at2 =AffineTransform.getTranslateInstance(Trm[2][0],(Trm[2][1]));
                                   glyph.transform(at2);

                                   /**support for TR7*/
                                   if(Tmode==GraphicsState.CLIPTEXT){
                                       currentGraphicsState.addClip(glyph);
                                   }
                            }
                            transformedGlyph2=glyph;
                        }

                        if(transformedGlyph2!=null){

                            //add to renderer
                            if(renderDirectly){
                                PdfPaint currentCol=null,fillCol=null;
                                int text_fill_type = currentGraphicsState.getTextRenderType();

                                //for a fill
                                if ((text_fill_type & GraphicsState.FILL) == GraphicsState.FILL)

                                    fillCol=currentGraphicsState.getNonstrokeColor();

                                //and/or do a stroke
                                if ((text_fill_type & GraphicsState.STROKE) == GraphicsState.STROKE)
                                    currentCol=currentGraphicsState.getStrokeColor();

                                //set the stroke to current value
                                Stroke newStroke=currentGraphicsState.getStroke();
                                g2.setStroke(newStroke);

                                current.renderText( text_fill_type,(Area) transformedGlyph2,g2,false,currentCol,fillCol,
										currentGraphicsState.getStrokeAlpha(),currentGraphicsState.getNonStrokeAlpha(),null) ;
                            }else
                                current.drawText(Trm,transformedGlyph2,currentGraphicsState);
                        }
                    }
                }

//				if((displayValue.equals("h"))&&(currentFontData.getFontName().equals("TimesNewRomanPS-BoldMT"))){
//					System.exit(1);
//					}


				/**track estimated heights of each letter on line to get maximum height for rectangluar outline*/
                if((legacyTextMode)&&(textExtracted)&&(!isCID)){

                    float h =PdfDecoder.currentHeightLookupData.getCharHeight(rawChar,fontSize);
                    if (max_height < h)
                        max_height = h;
				}

                /**now we have plotted it we update pointers and extract the text*/
                currentWidth = currentWidth + charSpacing;

				//see if about to add spaces
				boolean hasTextSpace=runningStoryPad && (charSpacing/spaceWidth)>1;

                if (rawChar == ' ') //add word spacing if
                    currentWidth = currentWidth + wordSpacing;

                //workout gap between chars and decide if we should add a space
                currentGap = (width + charSpacing - lastWidth);
				String spaces="";
                if ((currentGap > 0) & (lastWidth > 0)) {

                    float realGap=currentGap*fontScale;

                    if(runningStoryPad && realGap>160 && fontSize>11){ //fix for way hermes rolls titles together

                        /**calculate rectangular shape of text*/
                        calcCoordinates(x, Trm, isHorizontal, max_height, fontSize, y);

                        if(isHorizontal)
						pdfData.addRawTextElement((charSpacing * THOUSAND),currentTextState.writingMode,font_as_string,currentFontData.getCurrentFontSpaceWidth(),currentTextState,x1,y1,x2-realGap,y2,moveCommand,textData,tokenNumber,textLength,currentColor);
                        textData=new StringBuffer();
                        textLength=-1;
                        width=0;
                        x=x2;
                    }else{
                        //textData.append(getSpaces(currentGap, spaceWidth, currentThreshold));
                        spaces=getSpaces(currentGap, spaceWidth, currentThreshold);
                	}
				}


                textLength++; //counter on chars in data
                width = width + currentWidth;
                lastWidth = width; //increase width by current char

                //add unicode value to our text data with embedded width
                if((textExtracted)&&(unicodeValue.length() > 0)&&(Tmode!=GraphicsState.INVISIBLE)) {

                    //add character to text we have decoded with width
					//if large space separate out
                    if (PdfDecoder.embedWidthData == true) {

                    	if(hasTextSpace && spaces.length()>0){
							textData.append(StoryData.marker);
							if((isHorizontal)|(PdfGroupingAlgorithms.oldTextExtraction))
								textData.append(Trm[2][0]-((charSpacing) * fontScale));
							else
								textData.append(Trm[2][1]-((charSpacing) * fontScale));

							textData.append(StoryData.marker);
							textData.append((charSpacing) * fontScale);
							textData.append(StoryData.marker);
						}

						textData.append(spaces);

                        //embed width information in data
                        if((isHorizontal)|(PdfGroupingAlgorithms.oldTextExtraction)){
							textData.append(StoryData.marker);
							textData.append(Trm[2][0]);
							textData.append(StoryData.marker);

						}else{
							textData.append(StoryData.marker);
							textData.append(Trm[2][1]);
							textData.append(StoryData.marker);
						}
						if(hasTextSpace)
							textData.append((currentWidth-charSpacing) * fontScale);
						else
							textData.append(currentWidth * fontScale);

						textData.append(StoryData.marker);

                    }else
						textData.append(spaces);

                    /**add data to output*/
                    StringBuffer current_value=new StringBuffer(unicodeValue);

                    //turn chars less than 32 into escape
                    int length=current_value.length();
                    char next;
                    for (int ii = 0; ii < length; ii++) {
                        next = current_value.charAt(ii);

                        if((!runningStoryPad)||(next!=32 && next!=10 && next!=13))
                        hasContent=true;

                        if(PdfDecoder.isXMLExtraction()&&(next=='<'))
                            textData.append("&lt;");
                        else if(PdfDecoder.isXMLExtraction()&&(next=='>'))
                            textData.append("&gt;");
                        else if (next > 31)
                            textData.append(next);
                        else
                            textData.append(hex[next]);
                    }
                }else
                	textData.append(spaces);

            } else if ((rawChar ==40) | ((rawChar == 60))) { //start of text stream '('=40 '<'=60

                inText = true; //set text flag - no escape character possible
                openChar = rawChar;


            } else if ((rawChar == 41) || ((rawChar == 62)&&(openChar==60))||((!inText)&&((rawChar=='-')||(rawChar>='0' && rawChar<='9')))) { // ')'=41 '>'=62 '<'=60

                //handle leading between text ie -100 in  (The)-100(text)

                float value = 0;
                i++;

                //allow for spaces
                while(stream[i]==32) //' '=32
                    i++;

                nextChar = (char) stream[i];

                //allow for )( or >< (ie no value)
                if((nextChar==40)|(nextChar==60)){ //'('=40 '<'=60
                    i--;
                }else if ((nextChar != 39)&&(nextChar != 34)&&(nextChar != 40)&& (nextChar != 93)&& (nextChar != 60)) { //leading so roll on char
                    //'\''=39 '\"'=34 '('=40  //']'=93 '<'=60
                    StringBuffer currentLeading = new StringBuffer(6);

                    int leadingStart=i; //allow for failure
                    boolean failed=false;
                    boolean isMultipleValues=false;
                    while (!failed) {
                        rawChar = nextChar;
                        if(rawChar!=10 && rawChar !=13)
                        currentLeading.append(rawChar);
                        nextChar = (char) stream[i + 1];

                        if(nextChar==32)
                            isMultipleValues=true;

                        if ((nextChar == 40) | (nextChar == 60)) // '('=40 '<'=60
                            break;

                        if((nextChar==45)|(nextChar==46)|(nextChar==32)|(Character.isDigit(nextChar))){
                            //'-'=45 '.'=46 ' '=32
                        }else
                            failed=true;
                        
                        i++;
                    }

                    if(failed)
                        i= leadingStart;
                    else{

                        //more than one value separated by space
                        if(isMultipleValues){
                            StringTokenizer values=new StringTokenizer(currentLeading.toString());
                            value=0;
                            while(values.hasMoreTokens())
                                value =value +Float.parseFloat(values.nextToken());

                            value =-value/ THOUSAND;
                        }else if(currentLeading.length()>0)
                            value = -Float.parseFloat(currentLeading.toString()) / THOUSAND;

                         }
                    }

                width = width + value;
                leading = leading + value; //keep count on leading

            }

            i++;
        }

        /**all text is now drawn (if required) and text has been decoded*/

        //final move to get end of shape
        temp[0][0] = 1;
        temp[0][1] = 0;
        temp[0][2] = 0;
        temp[1][0] = 0;
        temp[1][1] = 1;
        temp[1][2] = 0;
        temp[2][0] = (currentWidth + leading); //tx;
        temp[2][1] = 0; //ty;
        temp[2][2] = 1;
        Trm = Matrix.multiply(temp, Trm); //multiply to get new Tm

        //update Tm to cursor
        currentTextState.Tm[2][0] = Trm[2][0];
        currentTextState.Tm[2][1] = Trm[2][1];

        /**used to debug code by popping up window after glyph*
         Object[] options = { "OK" };
         int n =JOptionPane.showOptionDialog(null,null,"Storypad",JOptionPane.OK_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,options[0]);
         /***/


        /** now workout the rectangulat shape this text occupies
         * by creating a box of the correct width/height and transforming it
         * (this routine could undoutedly be better coded but it works and I
         * don't want to break it!!)
         */
        if(textExtracted){

            //subtract character spacing once to make correct number(chars-1)
            width = width - charSpacing;

            /**roll on if last char is not a space - otherwise restore to before spaces*/
            if (lastTextChar == ' ')
                Trm = TrmBeforeSpace;

            /**calculate rectangular shape of text*/
            calcCoordinates(x, Trm, isHorizontal, max_height, fontSize, y);

            //System.out.println(y1+" "+y2+" "+fontSize+" "+y);
            //Matrix.show(Trm);

            /**return null for no text*/
            if (textData.length() == 0 || !hasContent) //return null if no text
                textData = null;

            /**set textState values*/
            currentTextState.writingMode = orientation;
            if(fontSize!=lastFontSize){
            currentTextState.setCurrentFontSize(Math.abs(fontSize));
                font_as_string =Fonts.createFontToken(currentFont,currentTextState.getCurrentFontSize());

                lastFontSize=fontSize;
            }


            /**

            if(textData!=null && org.jpedal.grouping.PdfGroupingAlgorithms.removeHiddenMarkers(textData.toString()).toString().indexOf("Judo")!=-1){
                System.out.println(x1+" "+y1+","+x2+" "+y2+" "+org.jpedal.grouping.PdfGroupingAlgorithms.removeHiddenMarkers(textData.toString())+" "+
                        max_height+" "+fontSize+" "+currentFontData.getFontName()+" "+Trm[0][0]);
                System.out.println(this.moveCommand+"<<");//charSpacing+" "+currentFontData.getCurrentFontSpaceWidth());
      // System.exit(1);

            }

                        /***/

        //if(textData!=null && x1>320 && x2<400 && y1>880 && y1<885)
        //System.out.println(y1+" "+org.jpedal.grouping.PdfGroupingAlgorithms.removeHiddenMarkers(textData.toString()));

            /**
             * hack for Times
             */
            if(runningStoryPad && !isHorizontal)
            textData=null;
            
            return textData;
        }else
          return null;

    }
  
    private void calcCoordinates(float x, float[][] trm, boolean horizontal, float max_height, int fontSize, float y) {
        x1 = x;
        x2 = trm[2][0] - (charSpacing * trm[0][0]);

        if (horizontal) {
            if (trm[1][0] < 0) {
                x1 = x + trm[1][0] - (charSpacing * trm[0][0]);
                x2 = trm[2][0];
            } else if (trm[1][0] > 0) {
                x1 = x;
                x2 = trm[2][0];
            }
        } else if (trm[1][0] > 0) {
            x1 = trm[2][0];
            x2 = x + trm[1][0] - (charSpacing * trm[0][0]);
        } else if (trm[1][0] < 0) {
            x2 = trm[2][0];
            x1 = x + trm[1][0] - (charSpacing * trm[0][0]);
        }

        /**any adjustments*/
        if (horizontal) {
            //workout the height ratio
            float s_height= 1.0f;
            if(legacyTextMode || currentFontData.getFontType()==StandardFonts.TYPE3)
            s_height=(max_height  / (fontSize));

            if (trm[0][1] != 0) {
                y1 =trm[2][1]- trm[0][1]+ ((trm[0][1] + trm[1][1]) * s_height);
                y2 = y;

            } else {
                y1 = y + (trm[1][1] * s_height);
                y2 = trm[2][1];
            }
        } else if (trm[0][1] <= 0) {
            y2 = trm[2][1];
            y1 = y;
        } else if (trm[0][1] > 0) {
            y1 = trm[2][1];
            y2 = y;
        }
    }

    /**
     * workout spaces (if any) to add into content for a gap
     * from user settings, space info in pdf
     */
    final private String getSpaces(
        float currentGap,
        float spaceWidth,
        float currentThreshold) {
        String space = "";

		if (spaceWidth > 0) {
            if ((currentGap > spaceWidth)) {
                while (currentGap >= spaceWidth) {
                    space = " " + space;
                    currentGap = currentGap - spaceWidth;
                }
            } else if (currentGap > spaceWidth * currentThreshold) {
                //ensure a gap of at least space_thresh_hold
                space = space + " ";
            }
        }


		return space;
	}

    /**
     * get unicode/escape value and convert to value
     */
    final private int readEscapeValue(
        int start,
        int count,
        int base,
        byte[] characterStream) {
        StringBuffer chars = new StringBuffer();
        for (int pointer = 0; pointer < count; pointer++)
            chars.append((char) characterStream[start + pointer]);

        return Integer.parseInt(chars.toString(), base);
    }

    /**
     * Returns the fonts used in the file
     */
    public  static String getFontsInFile() {
        return fontsInFile;
    }

    /**
     * setup stream decoder to render directly to g2
     * (used by image extraction)
     */
    public void setDirectRendering(Graphics2D g2) {

        this.renderDirectly=true;
        this.g2=g2;
        this.defaultClip=g2.getClip();

    }

    /**
     shows if embedded fonts present
     */
    public boolean hasEmbeddedFonts() {
        return hasEmbeddedFonts;
    }

    /**
     * include image data in PdfData (used by Storypad, not part of API)
     */
    public void includeImages(){
        includeImagesInData=true;
    }

    /**
     * return object holding horizontal and vertical lines
     */
    public PageLines getPageLines() {

            pageLines.lookForCompositeLines();
        return pageLines;
    }

    /**
     * flag to show if printing failed
     */
    public boolean isPageSuccessful() {

        return pageSuccessful;
    }

    /**
     * return list of messages
     */
    public String getPageFailureMessage() {
        return pageErrorMessages;
    }

    /**
     * add message on printer problem
     */
    public void addPageFailureMessage(String value) {
        pageSuccessful=false;
        pageErrorMessages=pageErrorMessages+value+"\n";
    }

    /**
     *
     *
    public void releaseResources() {

        Object[] fontList = fonts.keySet().toArray();
        String font_id;
        int count=fontList.length;
        for(int i=0;i<count;i++) {

            font_id = (String) fontList[i];

            //get values allowing for direct
            Object objectID=fonts.get(font_id);
            fonts.remove(font_id);
            ((PdfFont)objectID).releaseFont();
            System.out.println(objectID);
            objectID=null;
            System.out.println(objectID);
        }
        fonts.clear();

        this.rawColorspaceValues.clear();
        this.rawShadingValues.clear();


    }*/

    /**returns  Map containing all font objects*/
    public Map getFontMap() {
        return fonts;
    }


    public StringBuffer getlastTextStreamDecoded() {
        return textData;
    }


    public DynamicVectorRenderer getRenderer() {

        return this.current;
    }

    /**
     * tells program to try and use Java's font printing if possible
     * as work around for issue with PCL printing
     */
    public void setTextPrint(int textPrint) {
        this.textPrint = textPrint;
    }

    public boolean exitedDecoding() {
        return exited;

    }
    public void terminateDecoding() {
        exited=false;
    }

    public static boolean isFormSupportAvailable() {

        boolean supported=true;


        return supported;
    }

    /**flag to show if we suspect problem with some images*/
    public boolean hasAllImages() {

        return imagesProcessedFully;
    }

    public void setExternalImageRender(ImageHandler customImageHandler) {
        this.customImageHandler = customImageHandler;
        if(this.customImageHandler!=null)
            keepRaw=true;
    }

    //<start-adobe>


    /**
     * used internally for structured content extraction.
     * Does not work on OS version
     */
    public void setMapForMarkedContent(Object pageStream) {

    	 markedContentExtracted=true;

        contentHandler=new StructuredContentHandler(pageStream);

        extractedContent=contentHandler.hasContent();
        
    }
    //<end-adobe>

}
