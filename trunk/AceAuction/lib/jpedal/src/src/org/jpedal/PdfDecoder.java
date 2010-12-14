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
 * PdfDecoder.java
 * ---------------
 * (C) Copyright 2005, by IDRsolutions and Contributors.
 *
 * Original Author:  Mark Stephens (mark@idrsolutions.com)
 * Contributor(s):
 *
 */
package org.jpedal;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.net.JarURLConnection;
//<start-13>
import javax.print.attribute.SetOfIntegerSyntax;
//<end-13>
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;

//<start-jfl>
//<start-adobe>
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.acroforms.*;
//<end-adobe>
//<end-jfl>

import org.jpedal.color.ColorSpaces;

import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfFontException;

import org.jpedal.io.*;
import org.jpedal.objects.*;

//<start-jfl>

//<start-adobe>

//<start-thin>
import org.jpedal.examples.simpleviewer.gui.generic.GUIThumbnailPanel;
//<end-thin>

import org.jpedal.gui.Hotspots;

import org.jpedal.objects.outlines.OutlineData;
import org.jpedal.objects.structuredtext.MarkedContentGenerator;

//<end-adobe>
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;
import org.jpedal.utils.Strip;
import org.jpedal.external.Options;
import org.jpedal.external.ImageHandler;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//<<end-jfl>

import org.jpedal.parser.PdfStreamDecoder;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.fonts.StandardFonts;

/**
/**
 * Provides an object to decode pdf files and provide a rasterizer if required -
 * Normal usage is to create instance of PdfDecoder and access via public
 * methods. Examples showing usage in org.jpedal.examples - Inherits indirectly
 * from JPanel so can be used as a standard Swing component -
 *
 * Extends other classes to separate out GUI and business logic but should be
 * regarded as ONE object and PdfPanel should not be instanced - We recommend
 * you access JPedal using only public methods listed in API
 */
public class PdfDecoder extends PdfPanel implements Printable, Pageable {

	public static final String version = "3.10b10STD";

	public static boolean optimiseType3Rendering=false;

	int test = 0;

	private int duplexGapEven=0;
	private int duplexGapOdd=0;

	Map overlayType=new HashMap();
	Map overlayColors=new HashMap();
	Map overlayObj=new HashMap();

	//data from external FDF file
	Map fdfData=null;


	/** flag for xfa form */
	private boolean isXFA = false;

	//<start-jfl>
    Javascript javascript=null;
    ImageHandler customImageHandler=null;

	/** provide access to pdf file objects */
	PdfObjectReader currentPdfFile;

	/** page lookup table using objects as key */
	private PageLookup pageLookup = new PageLookup();

	/** flag to stop multiple access to background decoding */
	private boolean isBackgroundDecoding = false;

    //<start-adobe>
    /** store outline data extracted from pdf */
	private OutlineData outlineData = null;

    //<end-adobe>

    /** store outline data object reference so can be read if needed */
	private Object outlineObject = null;

	/** store information object */
	private String XMLObject;

    //<start-adobe>
    /**marked content*/
	MarkedContentGenerator content=new MarkedContentGenerator();
    //<end-adobe>

    /** store image data extracted from pdf */
	private PdfImageData pdfImages = new PdfImageData();

	/** store image data extracted from pdf */
	private PdfImageData pdfBackgroundImages = new PdfImageData();

	/** store text data and can be passed out to other classes */
	private PdfData pdfData;

	/** store text data and can be passed out to other classes */
	private PdfData pdfBackgroundData;

	/** lookup table to precalculated height values */
	public static org.jpedal.fonts.PdfHeightTable currentHeightLookupData = null;

	//<end-jfl>

	/** flag to show if on mac so we can code around certain bugs */
	public static boolean isRunningOnMac = false;
	public static boolean isRunningOnWindows = false;
	public static boolean isRunningOnLinux = false;

	/** provide print debug feature */
	private boolean debugPrint = false;


	private boolean hasViewListener = false;

    //<start-adobe>
    //<end-adobe>

    private boolean oddPagesOnly=false,evenPagesOnly=false;

	private boolean pagesPrintedInReverse=false;
	private boolean stopPrinting = false;


	/** PDF version */
	private String pdfVersion = "";

	/** Used to calculate displacement*/
	private int lastWidth;

	private int lastPage;

	public static boolean isDraft = true;

	private boolean useForms = true;

	/** direct graphics 2d to render onto */
	private Graphics2D g2 = null;



	/** flag to show embedded fonts present */
	private boolean hasEmbeddedFonts = false;

	/** list of fonts for decoded page */
	private String fontsInFile = "";

	/** dpi for final images */
	public static int dpi = 72;

	/**
	 * flag to tell software to embed x point after each character so we can
	 * merge any overlapping text together
	 */
	public static boolean embedWidthData = false;

	/** flag to show outline */
	private boolean hasOutline = false;

	/** actual page range to print */
	private int start = 0, end = -1;

	/** id demo flag disables output in demo */
	// <start-demo>
	public static final boolean inDemo = false;


	/** printing object */
	PdfStreamDecoder currentPrintDecoder = null;

	/**used by Canoo for printing*/
	DynamicVectorRenderer printRender=null;

	/** last page printed */
	private int lastPrintedPage = -1;

	/** flag to show extraction mode includes any text */
	public static final int TEXT = 1;

	/** flag to show extraction mode includes original images */
	public static final int RAWIMAGES = 2;

	/** flag to show extraction mode includes final scaled/clipped */
	public static final int FINALIMAGES = 4;

	/** undocumented flag to allow shape extraction */
	protected static final int PAGEDATA = 8;

	/** flag to show extraction mode includes final scaled/clipped */
	public static final int RAWCOMMANDS = 16;

	/** flag to show extraction of clipped images at highest res */
	public static final int CLIPPEDIMAGES = 32;

	/** flag to show extraction of clipped images at highest res */
	public static final int TEXTCOLOR = 64;

	/** flag to show extraction of raw cmyk images */
	public static final int CMYKIMAGES = 128;

	/** flag to show extraction of xforms metadata */
	public static final int XFORMMETADATA = 256;

	/** flag to show extraction of colr required (used in Storypad grouping) */
	public static final int COLOR = 512;

	/** flag to show render mode includes any text */
	public static final int RENDERTEXT = 1;

	/** flag to show render mode includes any images */
	public static final int RENDERIMAGES = 2;

	/** reference object for annotations */
	private String annotObject = null;

	/** flag to show if form data contained in current file */
	private boolean isForm = false;

	/** current extraction mode */
	private static int extractionMode = 7;

	/** current render mode */
	protected static int renderMode = 7;

	/** decodes page */
	private PdfStreamDecoder current;


	/** holds pdf id (ie 4 0 R) which stores each object */
	Map pagesReferences = new Hashtable();

	/**flag to show if page read to stop multiple reads on Annots in multipage mode*/
	Map pagesRead=new HashMap();


	/** global resources */
	Map globalRes;

	/** used to remap fonts onto truetype fonts (set internally) */
	public static Map fontSubstitutionTable = null;

    /** used to store number for subfonts in TTC*/
    public static Map fontSubstitutionFontID=null;

    /** used to remap fonts onto truetype fonts (set internally) */
	public static Map fontSubstitutionLocation = new Hashtable();

	/** used to remap fonts onto truetype fonts (set internally) */
	public static Map fontSubstitutionAliasTable = new Hashtable();

	/**
	 * flag to show if there must be a mapping value (program exits if none
	 * found)
	 */
	public static boolean enforceFontSubstitution = false;

	/** flag to show user wants us to display printable area when we print */
	private boolean showImageable = false;

	/** font to use in preference to Lucida */
	public static String defaultFont = null;

	/** holds pageformats */
	private Map pageFormats = new Hashtable();

	final private static String separator = System.getProperty("file.separator");

	/** flag to show if data extracted as text or XML */
	private static boolean isXMLExtraction = true;

	// stores default page sizes at each level so we can lookup
	private Map globalMediaValues = new Hashtable();

	/**
	 * used by Storypad to include images in PDFData)
	 */
	private boolean includeImages;

	/** interactive status Bar */
	private StatusBar statusBar = null;

	/**
	 * flag to say if java 1.3 version should be used for JPEG conversion (new
	 * JPEG bugs in Suns 1.4 code)
	 */
	public static boolean use13jPEGConversion = false;

	/**
	 * uses the scaling applied to the page unless over 1. In this case it uses
	 * a value of 1
	 */
	private boolean usePageScaling = false;

	/** tells JPedal to display screen using hires images */
	boolean useHiResImageForDisplay = false;

	/** flag used to show if printing worked */
	private boolean operationSuccessful = true;

	/** Any printer errors */
	private String pageErrorMessages = "";

	String filename;

	private ObjectStore backgroundObjectStoreRef = new ObjectStore();

	// <start-13>
	private SetOfIntegerSyntax range;


    //list of pages in range for quick lookup
    private int[] listOfPages;

    // <end-13>

	public static boolean flattenDebug = false;


	/**
	 * printing mode using inbuilt java fonts and getting java to rasterize
	 * fonts using Java font if match found (added to get around limitations in
	 * PCL printing via JPS)
	 */
	public static final int TEXTGLYPHPRINT = 1;

	/**
	 * printing mode using inbuilt java fonts and getting java to rasterize
	 * fonts using Java font if match found (added to get around limitations in
	 * PCL printing via JPS) - this is the default off setting
	 */
	public static final int NOTEXTPRINT = 0;

	/**
	 * printing mode using inbuilt java fonts and getting java to rasterize
	 * fonts using Java font if match found (added to get around limitations in
	 * PCL printing via JPS) - this is the default off setting
	 */
	public static final int TEXTSTRINGPRINT = 2;


	//flag to track if page decoded twice
	private int lastPageDecoded =-1;


	/**used is bespoke version of JPedal - do not use*/
	private boolean isCustomPrinting =false;

    public static final int SUBSTITUTE_FONT_USING_FILE_NAME = 1;
    public static final int SUBSTITUTE_FONT_USING_POSTSCRIPT_NAME = 2;

    /**determine how font substitution is done*/
    private static int fontSubstitutionMode=PdfDecoder.SUBSTITUTE_FONT_USING_FILE_NAME;
    //private static int fontSubstitutionMode=PdfDecoder.SUBSTITUTE_FONT_USING_POSTSCRIPT_NAME;


    //<start-adobe>
    //<start-jfl>
	/**
	 * pass current locations into Renderer so it can draw forms on
	 * other pages correctly offset
	 * @param xReached
	 * @param yReached
	 */
	protected void setMultiPageOffsets(int[] xReached, int[] yReached) {
		/**pass in values for forms/annots*/
		if(formsAvailable){
			if((currentFormRenderer!=null)&&(currentAcroFormData!=null))
				currentFormRenderer.setPageDisplacements(xReached,yReached);

			if((showAnnotations)&&(currentAnnotRenderer!=null)&&(annotsData!=null))
				currentAnnotRenderer.setPageDisplacements(xReached,yReached);
		}

	}
    //<end-adobe>



	/**
	 * see if file open - may not be open if user interrupted open or problem
	 * encountered
	 */
	public boolean isOpen() {
		return isOpen;
	}

    //<start-adobe>
    /**
	 * return markedContent object as XML Document
	 */
	public Document getMarkedContent() {

		return content.getMarkedContentTree(currentPdfFile,this,pageLookup);
	}

    //<end-jfl>

	/**
	 * used by remote printing to pass in page metrics
	 * @param pageData
	 */
	public void setPageData(PdfPageData pageData) {
		this.pageData=pageData;
	}

	//used by Storypad to create set of outlines - not part of API
	//and will change
	public void setAlternativeOutlines(Rectangle[] outlines,String altName) {
		this.alternateOutlines=outlines;
		this.altName=altName;

		this.repaint();
	}

	/**
	 * used by Storypad to display split spreads not aprt of API
	 */
	public void flushAdditionalPages() {
		pages.clearAdditionalPages();
		xOffset=0;
		additionalPageCount=0;

	}

	/**
	 * used by Storypad to display split spreads not aprt of API
	 */
	public void addAdditionalPage(DynamicVectorRenderer dynamicRenderer,int pageWidth,int origPageWidth) {

		//pageWidth=pageWidth+this.insetW+this.insetW;
		pages.addAdditionalPage(dynamicRenderer, pageWidth, origPageWidth);

		if(additionalPageCount==0){
			lastWidth = xOffset + origPageWidth;
			xOffset=xOffset+pageWidth;
		}
		else{
			xOffset=xOffset+pageWidth;
			lastWidth = lastWidth+lastPage;
		}
		additionalPageCount++;
		lastPage=pageWidth;
		this.updateUI();
	}

	public int getXDisplacement(){
		return lastWidth;    
	}

	public int getAdditionalPageCount(){
		return additionalPageCount;
	}

    //<end-adobe>
    /**
	 * work out machine type so we can call OS X code to get around Java bugs.
	 */
	static {

		/**
		 * see if mac
		 */
		try {
			String name = System.getProperty("os.name");
			if (name.equals("Mac OS X"))
				PdfDecoder.isRunningOnMac = true;
			else if(name.startsWith("Windows")){
				PdfDecoder.isRunningOnWindows = true;
			}else{
				if(name.equals("Linux")){
					PdfDecoder.isRunningOnLinux = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}



	}

	/**
	 * allows a number of fonts to be mapped onto an actual font and provides a
	 * way around slightly differing font naming when substituting fonts - So if
	 * arialMT existed on the target machine and the PDF contained arial and
	 * helvetica (which you wished to replace with arialmt), you would use the
	 * following code -
	 *
	 * String[] aliases={"arial","helvetica"};
	 * currentPdfDecoder.setSubstitutedFontAliases("arialmt",aliases); -
	 *
	 * comparison is case-insensitive and file type/ending should not be
	 * included - For use in conjunction with -Dorg.jpedal.fontdirs options which allows
	 * user to pass a set of comma separated directories with Truetype fonts
	 * (directories do not need to exist so can be multi-platform setting)
	 *
	 */
	public void setSubstitutedFontAliases(String fontFileName, String[] aliases) {

		if (aliases != null) {

			String name = fontFileName.toLowerCase(), alias;
			int count = aliases.length;
			for (int i = 0; i < count; i++) {
				alias = aliases[i].toLowerCase();
				if (!alias.equals(name))
					fontSubstitutionAliasTable.put(alias, name);
			}
		}
	}

	/**
	 * takes a comma separated list of font directories and add to substitution
	 *
	 */
	private String addFonts(String fontDirs, String failed) {

		StringTokenizer fontPaths = new StringTokenizer(fontDirs, ",");

		while (fontPaths.hasMoreTokens()) {

			String fontPath = fontPaths.nextToken();

			if (!fontPath.endsWith("/") & !fontPath.endsWith("\\"))
				fontPath = fontPath + separator;

			//LogWriter.writeLog("Looking in " + fontPath + " for TT fonts");

			addTTDir(fontPath, failed);
		}

		return failed;
	}

    //<start-adobe>
    /**
	 * turns off the viewable area, scaling the page back to original scaling
	 * <br>
	 * <br>
	 * NOT RECOMMENDED FOR GENERAL USE (this has been added for a specific
	 * client and we have found it can be unpredictable on some PDF files).
	 */
	public void resetViewableArea() {

		if(viewableArea!=null){
			viewableArea = null;
			// @fontHandle currentDisplay.setOptimiseDrawing(true);
			setPageRotation(displayRotation);
			repaint();
		}
	}

	/**
	 * allows the user to create a viewport within the displayed page, the
	 * aspect ratio is keep for the PDF page <br>
	 * <br>
	 * Passing in a null value is the same as calling resetViewableArea()
	 *
	 * <br>
	 * <br>
	 * The viewport works from the bottom left of the PDF page <br>
	 * The general formula is <br>
	 * (leftMargin, <br>
	 * bottomMargin, <br>
	 * pdfWidth-leftMargin-rightMargin, <br>
	 * pdfHeight-bottomMargin-topMargin)
	 *
	 * <br>
	 * <br>
	 * NOT RECOMMENDED FOR GENERAL USE (this has been added for a specific
	 * client and we have found it can be unpredictable on some PDF files).
	 *
	 * <br>
	 * <br>
	 * The viewport will not be incorporated in printing <br>
	 * <br>
	 * Throws PdfException if the viewport is not totally enclosed within the
	 * 100% cropped pdf
	 */
	public AffineTransform setViewableArea(Rectangle viewport)
	throws PdfException {

		if (viewport != null) {

			double x = viewport.getX();
			double y = viewport.getY();
			double w = viewport.getWidth();
			double h = viewport.getHeight();

			// double crx = pageData.getCropBoxX(pageNumber);
			// double cry = pageData.getCropBoxY(pageNumber);
			double crw = pageData.getCropBoxWidth(pageNumber);
			double crh = pageData.getCropBoxHeight(pageNumber);

			// throw exception if viewport cannot fit in cropbox
			if (x < 0 || y < 0 || (x + w) > crw || (y + h) > crh) {
				throw new PdfException(
				"Viewport is not totally enclosed within displayed panel.");
			}

			// if viewport exactlly matches the cropbox
			if (crw == w && crh == h) {
			} else {// else work out scaling ang apply

				viewableArea = viewport;
				currentDisplay.setOptimiseDrawing(false);
				setPageRotation(displayRotation);
				repaint();
			}
		} else {
			resetViewableArea();
		}

		return viewScaling;
	}
    //<end-adobe>

    /**
	 * deprecated please use setFontDirs(String[] fontDirs)
	 */
	public static String setTTFontDirs(String[] fontDirs){
		throw new RuntimeException("setTTFontDirs has been deprecated - please use setFontDirs");
	}

	/**
	 * takes a String[] of font directories and adds to substitution - Can just
	 * be called for each JVM - Should be called before file opened - this
	 * offers an alternative to the call -DFontDirs - Passing a null value
	 * flushes all settings
	 *
	 * @return String which will be null or list of directories it could not
	 *         find
	 */
	public static String setFontDirs(String[] fontDirs) {

		String failed = null;

        if (PdfDecoder.fontSubstitutionTable == null){
			fontSubstitutionTable = new HashMap();
             fontSubstitutionFontID=new HashMap();
        }

        try {
			if (fontDirs == null) { // idiot safety test
				LogWriter.writeLog("Null font parameter passed");
				PdfDecoder.fontSubstitutionAliasTable.clear();
				PdfDecoder.fontSubstitutionLocation.clear();
				PdfDecoder.fontSubstitutionTable.clear();
                PdfDecoder.fontSubstitutionFontID.clear();
            } else {

				int count = fontDirs.length;

				for (int i = 0; i < count; i++) {

					String fontPath = fontDirs[i];

					// allow for 'wrong' separator
					if (!fontPath.endsWith("/") & !fontPath.endsWith("\\"))
						fontPath = fontPath + separator;

					//System.out.println("Looking in " + fontPath
					//		+ " for TT fonts");
					//LogWriter.writeLog("Looking in " + fontPath
					//		+ " for TT fonts");

					failed = addTTDir(fontPath, failed);
				}
			}
		} catch (Exception e) {
			LogWriter.writeLog("Unable to run setFontDirs " + e.getMessage());
		}

		return failed;
	}

	/**
	 * add a truetype font directory and contents to substitution
	 */
	private static String addTTDir(String fontPath, String failed) {

		if (PdfDecoder.fontSubstitutionTable == null){
			fontSubstitutionTable = new HashMap();
            fontSubstitutionFontID=new HashMap();
        }

        File currentDir = new File(fontPath);

		if ((currentDir.exists()) && (currentDir.isDirectory())) {

			String[] files = currentDir.list();

			if (files != null) {
				int count = files.length;

				for (int i = 0; i < count; i++) {
					String currentFont = files[i];

					addFontFile(currentFont, fontPath);

				}
			}
		} else {
			if (failed == null) {
				failed = fontPath;
			} else {
				failed = failed + "," + fontPath;
			}
		}

		return failed;
	}


    /**
     * set mode to use when substituting fonts (default is to use Filename (ie arial.ttf)
     * Options are  SUBSTITUTE_FONT_USING_FILE_NAME,SUBSTITUTE_FONT_USING_POSTSCRIPT_NAME
     */
    public static void setFontSubstitutionMode(int mode) {
       fontSubstitutionMode=mode;
    }


    /**
	 * method to add a single file to the PDF renderer
	 * @param currentFont - actual font name we use to identify
	 * @param fontPath  - full path to font file used for this font
	 */
	public static void addFontFile(String currentFont, String fontPath) {

        if (PdfDecoder.fontSubstitutionTable == null){
            fontSubstitutionTable = new HashMap();
            fontSubstitutionFontID=new HashMap();
        }

        //add separator if needed
        if(fontPath!=null && !fontPath.endsWith("/") && !fontPath.endsWith("\\"))
            fontPath=fontPath+separator;

        String name=currentFont.toLowerCase();

        //decide font type
        int type = StandardFonts.getFontType(name);


        if (type!=StandardFonts.FONT_UNSUPPORTED) {
            // see if root dir exists
            InputStream in = null;
            try {
                in = new FileInputStream(fontPath + currentFont);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // if it does, add
            if (in != null) {

                String fontName="";

                //name from file
                int pointer = currentFont.indexOf(".");
                if (pointer == -1)
                    fontName = currentFont.toLowerCase();
                else
                    fontName = currentFont.substring(0, pointer).toLowerCase();

                //choose filename
                if(fontSubstitutionMode==PdfDecoder.SUBSTITUTE_FONT_USING_FILE_NAME){

                    fontSubstitutionTable.put(fontName,"/TrueType");
                    fontSubstitutionLocation.put(fontName, fontPath+ currentFont);

                }else if(type==StandardFonts.TRUETYPE_COLLECTION || type==StandardFonts.TRUETYPE){

                    //read 1 or more font mappings from file
                    String[] fontNames= new String[0];
                    try {
                        fontNames = StandardFonts.readPostscriptNames(type,fontPath+currentFont);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for(int i=0;i<fontNames.length;i++){

                        //allow for null and use font name
                        if(fontNames[i]==null)
                           fontNames[i]=Strip.stripAllSpaces(fontName);

                        fontSubstitutionTable.put(fontNames[i],"/TrueType");
                        fontSubstitutionLocation.put(fontNames[i], fontPath+ currentFont);
                        fontSubstitutionFontID.put(fontNames[i],new Integer(i));
                    }
                }
            } else {
                LogWriter.writeLog("No fonts found at " + fontPath);
            }
        }
    }

    //<start-adobe>
    /**
	 * return type of alignment for pages if smaller than panel
	 * - see options in Display class.
	 */
	public int getPageAlignment() {
		return alignment;
	}


    /**
	 * This will be needed for text extraction as it paramter makes sure widths
	 * included in text stream
	 *
	 * @param newEmbedWidthData -
	 *            flag to embed width data in text fragments for use by grouping
	 *            algorithms
	 */
	public final void init(boolean newEmbedWidthData) {

		/** get local handles onto objects/data passed in */
		embedWidthData = newEmbedWidthData;

	}
    //<end-adobe>

    /**
	 * Recommend way to create a PdfDecoder if no rendering of page may be
	 * required<br>
	 * Otherwise use PdfDecoder()
	 *
	 * @param newRender
	 *            flag to show if pages being rendered for JPanel or extraction
	 */
	public PdfDecoder(boolean newRender) {

		pages=new SingleDisplay(this);


		/** get local handles onto flag passed in */
		this.renderPage = newRender;

		setLayout(null);

		startup();
	}

    //<start-adobe>
    /**
	 * Not part of API - internal IDR method subject to frequent change
	 */
	public PdfDecoder(int mode, boolean newRender) {

		pages=new SingleDisplay(this);

		/** get local handles onto flag passed in */
		this.renderPage = newRender;

        extractionMode = 1;

        setLayout(null);

		init(true);

		startup();

		PdfStreamDecoder.runningStoryPad=true;

	}
    //<end-adobe>

    /**
	 *
	 */
	private void startup() {

        //<start-adobe>
        formsAvailable = PdfStreamDecoder.isFormSupportAvailable();

		if (this.formsAvailable) {

			currentFormRenderer = new DefaultAcroRenderer();
			currentAnnotRenderer = new DefaultAnnotRenderer();

			//pass in user handler if set
			if(formsActionHandler!=null){
				currentFormRenderer .resetActionHandler(formsActionHandler);
				currentAnnotRenderer.resetActionHandler(formsActionHandler);
			} 
		}
        //<end-adobe>

        /**
		 * set global flags
		 */
		String debugFlag = System.getProperty("debug");

		if (debugFlag != null)
			LogWriter.setupLogFile(true, 1, "", "v", false);

		// <start-13>
		/**
		 * pick up D options and use settings
		 */
		try {
			String fontMaps = System.getProperty("org.jpedal.fontmaps");

			if (fontMaps != null) {
				StringTokenizer fontPaths = new StringTokenizer(fontMaps, ",");

				while (fontPaths.hasMoreTokens()) {

					String fontPath = fontPaths.nextToken();
					StringTokenizer values = new StringTokenizer(fontPath, "=:");

					int count = values.countTokens() - 1;
					String nameInPDF[] = new String[count];
					String key = values.nextToken();
					for (int i = 0; i < count; i++)
						nameInPDF[i] = values.nextToken();

					setSubstitutedFontAliases(key, nameInPDF); //$NON-NLS-1$

				}
			}

		} catch (Exception e) {
			LogWriter.writeLog("Unable to read org.jpedal.fontmaps " + e.getMessage());
		}

		/**
		 * pick up D options and use settings
		 */
		try {
			String fontDirs = System.getProperty("org.jpedal.fontdirs");
			String failed = null;
			if (fontDirs != null)
				failed = addFonts(fontDirs, failed);
			if (failed != null)
				LogWriter.writeLog("Could not find " + failed);
		} catch (Exception e) {
			LogWriter.writeLog("Unable to read FontDirs " + e.getMessage());
		}
		// <end-13>

		// needs to be set so we can over-ride
		if (renderPage) {
			setToolTipText("image preview");

			// initialisation on font
			highlightFont = new Font("Lucida", Font.BOLD, size);

			setPreferredSize(new Dimension(100, 100));
		}
	}

	/**flag to enable popup of error messages in JPedal*/
	public static boolean showErrorMessages=false;

	/**
	 * Recommend way to create a PdfDecoder for renderer only viewer (not
	 * recommended for server extraction only processes)
	 */
	public PdfDecoder() {

		pages=new SingleDisplay(this);

		this.renderPage = true;

		setLayout(null);

		startup();
	}

	private boolean isOpen=false;

    //<start-adobe>
    //<start-jfl>
    //<end-adobe>


    /**
	 * convenience method to close the current PDF file
	 */
	final public void closePdfFile() {
		
		if(!isOpen)
			return;
		isOpen=false;
		
		displayScaling=null;
		

		lastPageDecoded =-1;

		// ensure no previous file still being decoded
		stopDecoding();
		pages.disableScreen();

		//flag all pages unread
		pagesRead.clear();

        //<start-adobe>
        //<start-jfl>
		if(javascript!=null && javascript.hasJavascript())
			javascript.closeFile();
		//<end-jfl>
        //<end-adobe>

        //flush arrays
		overlayType.clear();
		overlayColors.clear();
		overlayObj.clear();

        //<start-adobe>
        // pass handle into renderer
		if (formsAvailable) {
			currentAcroFormData=null;
			if ((currentFormRenderer != null)) {
				currentFormRenderer.openFile(pageCount);
				currentFormRenderer.init(currentAcroFormData, insetW,
						insetH, pageData, currentPdfFile);
			}

			// flush Annots structures - required as Annots exist on
			// page level, Forms on Doc level
			if ((showAnnotations) && (currentAnnotRenderer != null))
				currentAnnotRenderer.openFile(0);
		}
        //<end-adobe>


		if (currentPdfFile != null)
			currentPdfFile.closePdfFile();

		currentPdfFile = null;

		pages.disableScreen();
		currentDisplay.flush();
		objectStoreRef.flush();

		ObjectStore.flushPages();

		oldScaling=-1;

	}

	/**
	 * convenience method to get the PDF data as a byte array - works however
	 * file was opened.
	 *
	 * @return byte array containing PDF file
	 */
	final public byte[] getPdfBuffer() {

		byte[] buf = null;
		if (currentPdfFile != null)
			buf = currentPdfFile.getPdfBuffer();

		return buf;
	}

    //<start-adobe>
    /**
	 * Access should not generally be required to
	 * this class. Please look at getBackgroundGroupingObject() - provide method
	 * for outside class to get data object containing text and metrics of text. -
	 * Viewer can only access data for finding on page
	 *
	 * @return PdfData object containing text content from PDF
	 */
	final public PdfData getPdfBackgroundData() {
		return pdfBackgroundData;
	}

	/**
	 * Access should not generally be required to
	 * this class. Please look at getGroupingObject() - provide method for
	 * outside class to get data object containing raw text and metrics of text<br> -
	 * Viewer can only access data for finding on page
	 *
	 * @return PdfData object containing text content from PDF
	 */
	final public PdfData getPdfData() throws PdfException {
		if ((extractionMode & PdfDecoder.TEXT) == 0)
			throw new PdfException(
			"[PDF] Page data object requested will be empty as text extraction disabled. Enable with PdfDecoder method setExtractionMode(PdfDecoder.TEXT | other values");
		else
			return pdfData;
	}

	/**
	 * <B>Not part of API</B> provide method for outside class to get data
	 * object containing information on the page for calculating grouping <br>
	 * Please note: Structure of PdfPageData is not guaranteed to remain
	 * constant. Please contact IDRsolutions for advice.
	 *
	 * @return PdfPageData object
	 * @deprecated from 2.50
	 */
	final public PdfPageData getPdfBackgroundPageData() {
		return pageData;
	}

    /** flag to show if PDF document contains an outline */
	final public boolean hasOutline() {
		return hasOutline;
	}

	/** return a DOM document containing the PDF Outline object as a DOM Document - may return null*/
	final public Document getOutlineAsXML() {

		if (outlineData == null) {
			if (outlineObject != null) {

				/**/
				try {
					outlineData = new OutlineData(pageCount);
					outlineData.readOutlineFileMetadata(outlineObject,
							currentPdfFile, pageLookup);

				} catch (Exception e) {
					System.out.println("Exception " + e + " accessing outline "
							+ outlineObject);
					outlineData = null;
				}
				/***/

			}
		}
		if(outlineData!=null)
			return outlineData.getList();
		else return null;
	}

    /**
	 * <B>Not part of API</B> provide method for outside class to get data
	 * object containing information on the page for calculating grouping <br>
	 * Please note: Structure of PdfPageData is not guaranteed to remain
	 * constant. Please contact IDRsolutions for advice.
	 *
	 * @return PdfPageData object
	 */
	final public PdfPageData getPdfPageData() {
		return pageData;
	}
    //<end-adobe>

	/**
	 * set page range (inclusive) -
	 * If end is less than start it will print them
	 * backwards (invalid range will throw PdfException)
	 * @throws PdfException
	 *
	 */
	public void setPagePrintRange(int start, int end) throws PdfException {
		this.start = start;
		this.end = end;

		//all returns huge number not page end range
		if(end==2147483647)
			end=getPageCount();

		//if actually backwards, reverse order
		if(start>end){
			int tmp=start;
			start=end;
			end=tmp;
		}
		if((start<1)||(end<1)||(start>this.pageCount)||(end>this.pageCount))
			throw new PdfException(Messages.getMessage("PdfViewerPrint.InvalidPageRange")+" "+start+" "+end);

	}

	/**
	 * allow user to select only odd or even pages to print
	 */
	public void setPrintPageMode(int mode){
		oddPagesOnly=(mode & PrinterOptions.ODD_PAGES_ONLY)==PrinterOptions.ODD_PAGES_ONLY;
		evenPagesOnly=(mode & PrinterOptions.EVEN_PAGES_ONLY)==PrinterOptions.EVEN_PAGES_ONLY;

		pagesPrintedInReverse=(mode & PrinterOptions.PRINT_PAGES_REVERSED)==PrinterOptions.PRINT_PAGES_REVERSED;

	}

	// <start-13>
	/**
	 * set inclusive range to print (see SilentPrint.java and SimpleViewer.java
	 * for sample print code (invalid range will throw PdfException)
     * can  take values such as  new PageRanges("3,5,7-9,15");
	 */
	public void setPagePrintRange(SetOfIntegerSyntax range) throws PdfException {
		this.range = range;
		this.start = range.next(0); // find first

        int rangeCount=0;

        //get number of items
        if(range!=null){
            for(int ii=0;ii<this.pageCount;ii++){
                if(range.contains(ii))
                rangeCount++;
            }
        }

        //setup array
        listOfPages=new int[rangeCount+1];

        // find last
		int i = start;
		this.end = start;
		if(range.contains(2147483647)) //allow for all returning largest int
			end=getPageCount();
		else{
			while (range.next(i) != -1)
				i++;
			end = i;
		}

		//if actually backwards, reverse order
		if(start>end){
			int tmp=start;
			start=end;
			end=tmp;
		}

        //populate table
        if(range!=null){
            int j=0;

            for(int ii=start;ii<end+1;ii++){
                if(range.contains(ii) && (!oddPagesOnly || (ii & 1)==1) && (!evenPagesOnly || (ii & 1)==0)){
                    listOfPages[j]=ii-start;
                    j++;
                }
            }
        }

        if((start<1)||(end<1)||(start>this.pageCount)||(end>this.pageCount))
			throw new PdfException(Messages.getMessage("PdfViewerPrint.InvalidPageRange")+" "+start+" "+end);

	}

	// <end-13><end-jfl>

	/**
	 * tells program to try and use Java's font printing if possible as work
	 * around for issue with PCL printing - values are PdfDecoder.TEXTGLYPHPRINT
	 * (use Java to rasterize font if available) PdfDecoder.TEXTSTRINGPRINT(
	 * print as text not raster - fastest option) PdfDecoder.NOTEXTPRINT
	 * (default - highest quality)
	 */
	public void setTextPrint(int textPrint) {
		this.textPrint = textPrint;
	}

	/** flag to use Java's inbuilt font renderer if possible */
	private int textPrint = 0;

	/**the size above which objects stored on disk (-1 is off) */
	private int minimumCacheSize = -1;

	/** return any messages on decoding */
	String decodeStatus = "";

	/** current print page or -1 if finished */
	private int currentPrintPage = 0;

	private boolean imagesProcessedFully;

	private Object customSwingHandle;

	private Object formsActionHandler;

	private boolean generateGlyphOnRender;

	private boolean thumbnailsBeingDrawn;

	private float oldScaling=-1;

	/**switch on Javascript*/
	private boolean useJavascript=false;

	/**
	 * implements the standard Java printing functionality if start <end it will
	 * print it in reverse
	 *
	 * @param graphics -
	 *            object page rendered onto
	 * @param pf
	 *            PageFormat object used to print
	 * @param page -
	 *            current page index (less 1 so start at page 0)
	 * @return int Printable.PAGE_EXISTS or Printable.NO_SUCH_PAGE
	 * @throws PrinterException
	 */
	public int print(Graphics graphics, PageFormat pf, int page)
	throws PrinterException {

		//used to debug remote printing
		final boolean debugRemote=false;

		if(debugRemote)
			System.out.println("Print called");

		Map values=null;
		String value=null;

		//exit if requested
		if(stopPrinting){
			stopPrinting=false;
			start=0;
			end=0;
            
            return Printable.NO_SUCH_PAGE;
		}

		int i = Printable.PAGE_EXISTS;

		int dx=0,dy=0;

		//<start-13>
		/**
		 * needs to double up to get correct page
		 */

        /*adjust if just printing half pages*/
		if((range==null)&&(oddPagesOnly || evenPagesOnly)){
			page=page*2;

		// convert page ranges
        }else if (this.range != null) {
            
            page=listOfPages[page];

        }
		// <end-13>

		String printAnnotObject = null;
		PdfAnnots printAnnots = null;

		if (debugPrint)
			System.out.println("Passed range test");

		try {

			double scaling = 1;

			/**
			 * set scaling value - pageScaling is deprecated but left in for
			 * moment to provide backwards compatability
			 */
			if (usePageScaling)
				scaling = this.scaling;

			// increase index to match out page numbers- JavaPrint starts at 0
			// also allow for reverse
			int lastPage=end,firstPage=start;

			if(pagesPrintedInReverse){
				firstPage=start;
				lastPage=end;
				page = end - page;
			}else if(((end!=-1)&&(end<start))){
				firstPage=end;
				lastPage=start;
				page = start - page;
			}else
				page = start + page;

			if (end == -1)
				page++;

			currentPrintPage = page;

			// make sure in range
			if ((page > pageCount) | ((end != -1) & (page > lastPage))) {

				currentPrintPage = -1;

				if (debugPrint)
					System.out.println("no such page");

				return Printable.NO_SUCH_PAGE;
			}

			if ((end == -1) | ((page >= firstPage) & (page <= lastPage))) {

				if (debugPrint)
					System.out.println("page in range");

				operationSuccessful = true;
				pageErrorMessages = "";

				try {

					/**
					 * setup for decoding page
					 */

					/** get pdf object id for page to decode */
					String currentPageOffset = (String) pagesReferences.get(new Integer(page));

					if ((currentPageOffset != null) || isCustomPrinting) {

						if (debugPrint && !isCustomPrinting)
							System.out.println("currentPageOffset="+ currentPageOffset);

						if (currentPdfFile == null && !isCustomPrinting)
							throw new PrinterException("File not open - did you call closePdfFile() inside a loop and not reopen");

						if(!isCustomPrinting){
							/** read page or next pages */
							values = currentPdfFile.readObject(currentPageOffset, false, null);

                            //<start-adobe>
                            /**
							 * read the Annotations for the page we have found lots
							 * of issues with annotations so trap errors
							 */
							try {
								// copied from decodepage to print the annotations
								printAnnotObject = currentPdfFile.getValue((String) values.get("Annots"));

								if(printAnnotObject != null && printAnnotObject.equals("null"))
									printAnnotObject=null;

								if (formsAvailable && renderPage && (printAnnotObject != null)&& (showAnnotations)) {

									printAnnots = new PdfAnnots(currentPdfFile,pageLookup);
									printAnnots.readAnnots(printAnnotObject);

									currentAnnotRenderer.init(printAnnots,insetW, insetH, pageData,currentPdfFile);
								}
							} catch (Exception e1) {
								LogWriter.writeLog("[PDF] " + e1+ " with annotation");
							}

                            //<end-adobe>
                        }

                        //<start-adobe>
                        /** flush annotations */
						if (printHotspots != null && !isCustomPrinting)
							printHotspots.flushAnnotationsDisplayed();
                        //<end-adobe>

                        /** decode page only on first pass */
						if ((lastPrintedPage != page)) {

							currentPrintDecoder = new PdfStreamDecoder(true);
							currentPrintDecoder.setExternalImageRender(customImageHandler);
							currentPrintDecoder.setTextPrint(textPrint);

							/** the ObjectStore for this file for printing */
							ObjectStore objectPrintStoreRef = new ObjectStore();

							currentPrintDecoder.optimiseDisplayForPrinting();
							currentPrintDecoder.setStore(objectPrintStoreRef);

							if(!isCustomPrinting){
								/** get information for the page */
								value = (String) values.get("Contents");

								if (value != null) {
									Map res = currentPdfFile.getSubDictionary(values.get("Resources"));

									try {
										currentPrintDecoder.init(true, renderPage,renderMode, 0, pageData, page,null, currentPdfFile, globalRes,res);
									} catch (PdfException ee) {
										throw new PdfFontException(ee.getMessage());
									}
								}
							}
						}

						// setup transformations
						AffineTransform printScaling = new AffineTransform();

						/**
						 * copy of crop values for clipping
						 */
						int clipW,clipH,clipX,clipY,crx,crw,cry,crh,mediaW,mediaH,mediaX,mediaY;

						/**
						 *set default pagesize
						 */

						/** get media box - ie page size*/
						mediaW = pageData.getMediaBoxWidth(page);
						mediaH = pageData.getMediaBoxHeight(page);
						mediaX = pageData.getMediaBoxX(page);
						mediaY = pageData.getMediaBoxY(page);

						/** get crop box and rotation*/
						crx = clipX = pageData.getCropBoxX(page);
						cry = clipY = pageData.getCropBoxY(page);
						crw = clipW = pageData.getCropBoxWidth(page) + 1;
						crh = clipH = pageData.getCropBoxHeight(page) + 1;

						/**
						 * get imageble area - ie box we print into
						 */
						int iX = (int) pf.getImageableX();
						int iY = (int) pf.getImageableY();
						int iW = (int) pf.getImageableWidth() - 1;
						int iH = (int) pf.getImageableHeight() - 1;

						boolean needsTurning=(((iW>iH)&&(crw<crh))||((iW<iH)&&(crw>crh)));

						if(!IsPrintAutoRotateAndCenter)
							needsTurning=false;

						if(needsTurning){
							/** get media box - ie page size*/
							mediaW = pageData.getMediaBoxHeight(page);
							mediaH = pageData.getMediaBoxWidth(page);
							mediaX = pageData.getMediaBoxY(page);
							mediaY = pageData.getMediaBoxX(page);

							/*** get crop box and rotation*/
							crx = clipX = pageData.getCropBoxY(page);
							cry = clipY = pageData.getCropBoxX(page);
							crw = clipW = pageData.getCropBoxHeight(page) + 1;
							crh = clipH = pageData.getCropBoxWidth(page) + 1;

						}

						// put border on printable area if requrested
						if (showImageable) {

							Rectangle printableArea = new Rectangle(iX,iY, iW, iH);

							System.out.println("image=" + iX + " "+ iY + " " + iW + " "+ iH);
							Graphics2D printableG2 = (Graphics2D) graphics;

							printableG2.setColor(Color.red);
							printableG2.fill(printableArea);
							printableG2.setColor(Color.white);
							for(int xx=0;xx<1000;xx=xx+50){
								printableG2.draw(new Line2D.Float(xx,0, xx,1000));
								printableG2.draw(new Line2D.Float(0,xx,1000, xx));
							}

							printableG2.draw(printableArea);
							printableG2.draw(new Line2D.Float(iX,iY, iX + iW, iY+ iH));
							printableG2.draw(new Line2D.Float(iX,iY + iH, iX+ iW, iY));
						}

						double pScale = 1.0;

						/**
						 * size of the page we are printing
						 */
						int print_x_size = crw, print_y_size = crh;

						//old scaling code (should not be used)
						if (usePageScaling) {
							/** avoid oversize scaling shrinking page */
							print_x_size = (int) ((crw) * scaling);
							print_y_size = (int) ((crh) * scaling);

							if (((print_x_size > iW) | (print_y_size > iH))) {
								print_x_size = crw;
								print_y_size = crh;
								scaling = 1;
							}
						}

						/**
						 * workout scaling factor and use the smaller scale
						 * factor
						 */
						double pageScaleX = (double) iW/ print_x_size;
						double pageScaleY = (double) iH/ print_y_size;
						double newScaling;
						boolean scaledOnX=true;
						if (pageScaleX < pageScaleY)
							newScaling = pageScaleX;
						else{
							scaledOnX=false;
							newScaling = pageScaleY;
						}

						boolean hasScaling=false;


						if (usePageScaling) {

							pScale = newScaling;

							//old scaling code (should not beused)
							if (((print_x_size > iW) | (print_y_size > iH))) {

								/** adjust settings to fit page */
								print_x_size = (int) (print_x_size * pScale);
								print_y_size = (int) (print_y_size * pScale);
							}

						} else { // new scaling code

							switch(pageScalingMode){

							case PrinterOptions.PAGE_SCALING_FIT_TO_PRINTER_MARGINS:
								pScale = newScaling;
								hasScaling = true;
								break;

								/** adjust settings to fit page */
							case PrinterOptions.PAGE_SCALING_REDUCE_TO_PRINTER_MARGINS:
								if (newScaling < 1)
									pScale = newScaling;

								hasScaling = true;
								break;

								// do nothing
							case PrinterOptions.PAGE_SCALING_NONE:
								break;

							}

							/** center image in middle of page
							 * if needs centering
							 * */
							dx = (int) (((iW) - (crw * pScale)) / 2);
							dy = (int) (((iH) - (crh * pScale)) / 2);

							/**correctly align*/
							if ((pageScalingMode == PrinterOptions.PAGE_SCALING_FIT_TO_PRINTER_MARGINS)||(pScale<1)){
								if(scaledOnX)
									dx=0;
								else
									dy=0;
							}

							if ((!needsTurning)&&((hasScaling)||IsPrintAutoRotateAndCenter))
								printScaling.translate(dx, dy);
						}

						/**
						 * turn around if needed
						 */
						if (needsTurning) {

							printScaling.scale(1, -1);
							printScaling.translate(0, -crh*pScale);

							printScaling.scale(pScale, pScale);

							printScaling.rotate(Math.PI/2);
							printScaling.translate(0,-crw);

							if(hasScaling || IsPrintAutoRotateAndCenter)
								printScaling.translate(-dy/pScale,-(dx/pScale));

							printScaling.translate((cry),-(crx));

							printScaling.translate(-iX/ pScale,-iY/ pScale);
						}else{
							printScaling.translate(-(crx * pScale),(cry * pScale));
							printScaling.translate(iX,iY);

							/**
							 * set appropiate scaling
							 */
							if (pScale != 1) {
								if (this.usePageScaling) {
									printScaling.translate(print_x_size,print_y_size);
									printScaling.scale(1, -1);
									printScaling.translate(-print_x_size, 0);
								} else {
									printScaling.translate(print_x_size* pScale, print_y_size* pScale);
									printScaling.scale(1, -1);
									printScaling.translate(-print_x_size* pScale, 0);
								}
								printScaling.scale(pScale, pScale);

							} else {
								printScaling.translate(print_x_size,print_y_size);
								printScaling.scale(1, -1);
								printScaling.translate(-print_x_size, 0);
								printScaling.scale(scaling, scaling);// &&
							}
						}

						/** reassign of crop values for clipping */
						crx = clipX;
						cry = clipY;
						crw = clipW;
						crh = clipH;

						// turn off double buffering
						RepaintManager currentManager = RepaintManager.currentManager(this);
						currentManager.setDoubleBufferingEnabled(false);

						Graphics2D g2 = (Graphics2D) graphics;

						g2.setRenderingHints(ColorSpaces.hints);
						g2.transform(printScaling);
						//showImageable=true;
						if (showImageable) {

							g2.setColor(Color.black);
							if(needsTurning){
								g2.draw(new Rectangle(mediaY, mediaX, mediaH,mediaW));
								g2.drawLine(mediaY, mediaX, mediaH + mediaY,mediaW + mediaX);
								g2.drawLine(mediaY, mediaW + mediaX, mediaH+ mediaY, mediaX);
							}else{
								g2.draw(new Rectangle(mediaX, mediaY, mediaW,mediaH));
								g2.drawLine(mediaX, mediaY, mediaW + mediaX,mediaH + mediaY);
								g2.drawLine(mediaX, mediaH + mediaY, mediaW+ mediaX, mediaY);
							}

							g2.setColor(Color.blue);
							if(needsTurning){
								g2.draw(new Rectangle(cry, crx, crh, crw));
								g2.drawLine(cry, crx, crh + cry, crw + crx);
								g2.drawLine(cry, crw + crx, crh + cry, crx);
							}else{
								g2.draw(new Rectangle(crx, cry, crw, crh));
								g2.drawLine(crx, cry, crw + crx, crh + cry);
								g2.drawLine(crx, crh + cry, crw + crx, cry);
							}
						} else if(needsTurning)
							g2.clip(new Rectangle(cry, crx, crh, crw));
						else
							g2.clip(new Rectangle(crx, cry, crw, crh));

						/**
						 * pass in values as needed for patterns
						 */
						currentPrintDecoder.getRenderer().setScalingValues(crx, crh + cry,(float) pScale);

						/** decode page only on first pass */
						if (lastPrintedPage != page) {

							if (debugPrint)
								System.out.println("About to decode stream");

							if(!isCustomPrinting){
								try {
									currentPrintDecoder.decodePageContent(value, 0, 0, null);

									//store for printing
									Integer key=new Integer(page);
									int[] type=(int[])overlayType.get(key);
									Color[] colors=(Color[]) overlayColors.get(key);
									Object[] obj=(Object[]) overlayObj.get(key);

									//add to screen display
									currentPrintDecoder.getRenderer().drawAdditionalObjectsOverPage(type,colors, obj);
								} catch (PdfException e2) {
									e2.printStackTrace();
									throw new PrinterException(e2.getMessage());
								}
							}else{
							}

							lastPrintedPage = page;

							if (debugPrint)
								System.out.println("Decoded stream");
						}


						if (debugPrint)
							System.out.println("About to print stream");

						/**
						 * set any indent
						 */
						//alter to create guttering in duplex
						if(duplexGapOdd!=0 || duplexGapEven!=0){

							Shape clip=g2.getClip();
							if(page % 2 != 1)
								g2.translate(duplexGapEven,0);
							else
								g2.translate(duplexGapOdd,0);

							if(clip!=null)
								g2.setClip(clip);
						}

						/**
						 * print
						 */
						if(!isCustomPrinting)
							currentPrintDecoder.print(g2, null,showImageable);
						else if(printRender ==null){
							LogWriter.writeLog("No data for page "+page);
						}else
							printRender.paint(g2,null,null,null,false);

						if (debugPrint)
							System.out.println("Rendered");

						g2.setClip(null);

                        //<start-adobe>
                        // set up page hotspots
						if (!isCustomPrinting && printAnnots != null && printHotspots != null)
							printHotspots.setHotspots(printAnnots);
                        //<end-adobe>


                        //<start-adobe>

                        /** draw any annotations */
						if (printHotspots != null)
							printHotspots.addHotspotsToDisplay(g2,userAnnotIcons, page);

						if (debugPrint)
							System.out.println("About to add annots/forms");

						if (formsAvailable) {
							/**
							 * draw acroform data onto Panel
							 */
							if ((currentFormRenderer != null)&& (currentAcroFormData != null)) {

								/** make sure they exist */
								currentFormRenderer.createDisplayComponentsForPage(page, null,(float) scaling,0);

								// always use 0 for printing and extraction on forms
								currentFormRenderer.renderFormsOntoG2(g2,page, (float) scaling, 0);
							}

							if (showAnnotations && currentAnnotRenderer != null && printAnnotObject != null) {

								/** make sure they exist */
								currentAnnotRenderer.createDisplayComponentsForPage(page, null,(float) scaling,0);

								// always use 0 for printing and extraction on forms
								currentAnnotRenderer.renderFormsOntoG2(g2,page, (float) scaling, 0);
							}
						}

                        //<end-adobe>

                        if (debugPrint)
							System.out.println("Added");

						// fudge to get border round just the page
						if ((useBorder) && (myBorder != null))
							myBorder.paintBorder(this, g2, crx, cry, crw,crh);

						// turn on double buffering
						currentManager.setDoubleBufferingEnabled(true);

						if (!currentPrintDecoder.isPageSuccessful()) {
							operationSuccessful = false;
							pageErrorMessages = pageErrorMessages+ currentPrintDecoder.getPageFailureMessage();
						}

						if (debugPrint)
							System.out.println("Successful="+ operationSuccessful + "\n" + pageErrorMessages);
					}

				} catch (PdfFontException e) {
					operationSuccessful = false;
					pageErrorMessages = pageErrorMessages+ "Missing substitute fonts\n";

					if (debugPrint)
						System.out.println("Exception e=" + e);
				}
			}

		} catch (Error err) {
			operationSuccessful = false;
			pageErrorMessages = pageErrorMessages+ "Memory Error on printing\n";

			if (debugPrint)
				System.out.println("Error=" + err);

		}

		/** force printing to terminate on failure */
		if (!operationSuccessful)
			i = Printable.NO_SUCH_PAGE;

		if (debugPrint)
			System.out.println("return i=" + i);

		return i;

	}

	/**
	 * generate BufferedImage of a page in current file
	 */
	public BufferedImage getPageAsImage(int pageIndex) throws PdfException {
		return getPageAsImage(pageIndex, false);
	}

	/**
	 * generate BufferedImage of a page in current file
	 */
	public BufferedImage getPageAsTransparentImage(int pageIndex)
	throws PdfException {
		return getPageAsImage(pageIndex, true);
	}

	/**
	 * generate BufferedImage of a page in current file
	 */
	private BufferedImage getPageAsImage(int pageIndex,
			boolean imageIsTransparent) throws PdfException {

		BufferedImage image = null;

		// make sure in range
		if ((pageIndex > pageCount) | (pageIndex < 1)) {
			LogWriter.writeLog("Page " + pageIndex + " not in range");
		} else {

			/**
			 * setup for decoding page
			 */
			PdfAnnots printAnnots = null;
			String printAnnotObject = null;

			/** get pdf object id for page to decode */
			String currentPageOffset = (String) pagesReferences
			.get(new Integer(pageIndex));

			if ((currentPageOffset != null)) {

				if (currentPdfFile == null)
					throw new PdfException(
					"File not open - did you call closePdfFile() inside a loop and not reopen");

				/** read page or next pages */
				Map values = currentPdfFile.readObject(currentPageOffset,
						false, null);

                //<start-adobe>
                /**
				 * read the Annotations for the page we have found lots of
				 * issues with annotations so trap errors
				 */
				try {
					// copied from decodepage to print the annotations
					printAnnotObject = currentPdfFile.getValue((String) values
							.get("Annots"));

					if (formsAvailable) {
						if ((renderPage) && (printAnnotObject != null)
								&& (showAnnotations)) {

							printAnnots = new PdfAnnots(currentPdfFile,
									pageLookup);
							printAnnots.readAnnots(printAnnotObject);

							currentAnnotRenderer.init(printAnnots, insetW,
									insetH, pageData, currentPdfFile);

						}
					}

				} catch (Exception e1) {
					LogWriter.writeLog("[PDF] " + e1 + " with annotation");
				}


				/** flush annotations */
				if (printHotspots != null)
					printHotspots.flushAnnotationsDisplayed();

                //<end-adobe>

                /** get information for the page */
				String value = (String) values.get("Contents");

                /**
				 * setup transformations and image
				 */
				AffineTransform imageScaling = setPageParametersForImage(
						scaling, pageIndex);

				int mediaW = pageData.getMediaBoxWidth(pageIndex);
				int mediaH = pageData.getMediaBoxHeight(pageIndex);
				int rotation = pageData.getRotation(pageIndex);

				int crw = pageData.getCropBoxWidth(pageIndex);
				int crh = pageData.getCropBoxHeight(pageIndex);
				int crx = pageData.getCropBoxX(pageIndex);
				int cry = pageData.getCropBoxY(pageIndex);

				boolean rotated = false;
				int w, h;
				if ((rotation == 90) | (rotation == 270)) {
					h = (int) (crw * scaling);
					w = (int) (crh * scaling);
					rotated = true;
				} else {
					w = (int) (crw * scaling);
					h = (int) (crh * scaling);
				}

				image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

				Graphics graphics = image.getGraphics();

				Graphics2D g2 = (Graphics2D) graphics;
				if (!imageIsTransparent) {
					g2.setColor(Color.white);
					g2.fillRect(0, 0, w, h);
				}

				/**
				 * decode the contents or fill in white
				 */
//				if (value != null) {

				ObjectStore localStore = new ObjectStore();
				PdfStreamDecoder currentImageDecoder = new PdfStreamDecoder();

				currentImageDecoder.setExternalImageRender(customImageHandler);
				currentImageDecoder.setName(filename);
				currentImageDecoder.setStore(localStore);

				Map resValue = currentPdfFile.getSubDictionary(values.get("Resources"));

				if (imageIsTransparent) {

					DynamicVectorRenderer imageDisplay = new DynamicVectorRenderer(pageNumber,
							false, 5000, localStore);
					currentImageDecoder.init(true, renderPage, renderMode,
							0, pageData, pageIndex, imageDisplay,
							currentPdfFile, globalRes, resValue);

				} else {
					currentImageDecoder.init(true, renderPage, renderMode,
							0, pageData, pageIndex, null, currentPdfFile,
							globalRes, resValue);
				}

				/**
				 * pass in values as needed for patterns
				 */
				currentImageDecoder.getRenderer().setScalingValues(crx, crh + cry, scaling);

				g2.setRenderingHints(ColorSpaces.hints);
				g2.transform(imageScaling);

				if (rotated)
					g2.translate(-crx, cry);

				/** decode and print in 1 go */
				currentImageDecoder.setDirectRendering(g2);//(Graphics2D) graphics);
				if(value!=null)
					currentImageDecoder.decodePageContent(value, 0, 0, null);// mediaX,mediaY);

				g2.setClip(null);


                //<start-adobe>

                // set up page hotspots
				if ((printAnnots != null) && (printHotspots != null))
					printHotspots.setHotspots(printAnnots);

				/** draw any annotations */
				if (printHotspots != null)
					printHotspots.addHotspotsToDisplay(g2, userAnnotIcons,
							pageIndex);

                if (formsAvailable) {
					/**
					 * draw acroform data onto Panel
					 */
					if ((currentFormRenderer != null)
							&& (currentAcroFormData != null)) {

						/** make sure they exist */
						currentFormRenderer.createDisplayComponentsForPage(
								pageIndex, this, scaling, 0);

						currentFormRenderer.renderFormsOntoG2(g2,
								pageIndex, scaling, 0);

						currentFormRenderer.resetScaledLocation(oldScaling,displayRotation,0);
						//TODO figure out what the indent should be, NOT 0
					}

					/**
					 * draw acroform data onto Panel
					 */
					if ((showAnnotations) && (printAnnotObject != null)) {

						/** make sure they exist */
						currentAnnotRenderer
						.createDisplayComponentsForPage(pageIndex,
								this, scaling, 0);

						currentAnnotRenderer.renderFormsOntoG2(g2,
								pageIndex, scaling, 0);

					}
				}
                //<end-adobe>


				localStore.flush();
			}
//			}

			if ((!imageIsTransparent) && (image != null))
				image = ColorSpaceConvertor.convertToRGB(image);

		}

		return image;

	}

	/**
	 * provide method for outside class to clear store of objects once written
	 * out to reclaim memory
	 *
	 * @param reinit
	 *            lag to show if image data flushed as well
	 */
	final public void flushObjectValues(boolean reinit) {

		if (pdfData != null)
			pdfData.flushTextList(reinit);

        //<start-adobe>
        if (currentAcroFormData != null)
			currentAcroFormData = new PdfFormData(inDemo);
        //<end-adobe>

        if ((pdfImages != null) && (reinit))
			pdfImages.clearImageData();

	}

    //<start-adobe>

    //<start-jfl>
	/**
	 * provide method for outside class to get data object
	 * containing images
	 *
	 * @return PdfImageData containing image metadata
	 */
	final public PdfImageData getPdfImageData() {
		return pdfImages;
	}

	/**
	 * provide method for outside class to get data object
	 * containing images.
	 *
	 * @return PdfImageData containing image metadata
	 */
	final public PdfImageData getPdfBackgroundImageData() {
		return pdfBackgroundImages;
	}

    /**
	 * provide method for outside class to get Annots data object<br>
	 * Please note: Structure of PdfPageData is not guaranteed to remain
	 * constant<br>
	 * Please contact IDRsolutions for advice (pass in null object if using
	 * externally)
	 *
	 * @return PdfAnnots object containing annotation data
	 */
	final public PdfAnnots getPdfAnnotsData(AcroRenderer currentAnnotRenderer) {

		//not supported
		if(isXFA)
			return null;
		/**/

		if (annotsData == null) {
			try {

				if (annotObject != null && !annotObject.equals("null")) {
					annotsData = new PdfAnnots(currentPdfFile, pageLookup);
					annotsData.readAnnots(annotObject);
				}
			} catch (Exception e) {
				LogWriter.writeLog("[PDF] " + e + " with annotation");
			}

			// pass handle into renderer
			if (formsAvailable) {
				if (currentAnnotRenderer == null)
					currentAnnotRenderer = this.currentAnnotRenderer;

				if ((showAnnotations) && (currentAnnotRenderer != null))
					currentAnnotRenderer.init(annotsData, insetW, insetH, pageData,
							currentPdfFile);

			}
		}

		return annotsData;
	}

	/**
	 * read the form data from the pdf file<br>
	 *
	 * @param currentFormOffset -
	 *            object reference to form
	 */
	private void readAcroForm(Object currentFormOffset) {

		String value, formObject = "";

		// table to hold children
		Map kidData = new HashMap();

		// System.out.println(currentFormOffset);

		if (!useForms)
			return;

		LogWriter.writeLog("Form data being read");

		/** start the queue */
		Vector queue = new Vector();

		/**
		 * read form object metadata
		 */
		Map values;
		if (currentFormOffset instanceof String)
			values = currentPdfFile.readObject((String) currentFormOffset,
					false, null);
		else
			values = (Map) currentFormOffset;


		/** flag if XFA */
		isXFA = (values.get("XFA") != null);

		if(isXFA)
			return ;
		/**/


		/** read the fields */
		value = (String) values.get("Fields");
		if (value != null) {

			/** allow for values after fields */
			int p = value.indexOf("]");
			if (p != 0)
				value = value.substring(0, p + 1);

			/** strip the array braces */
			value = Strip.removeArrayDeleminators(value);

			boolean fieldsToProcess = true;

			/** put in queue */
			StringTokenizer initialValues = new StringTokenizer(value, "R");

			// allow for empty list

			// text fields
			Map fields = new HashMap();
			// setup a list of fields which are string values
			fields.put("T", "x");
			fields.put("TM", "x");
			fields.put("TU", "x");
			fields.put("CA", "x");
			fields.put("R", "x");
			fields.put("V", "x");
			fields.put("RC", "x");
			fields.put("DA", "x");
			fields.put("DV", "x");

			/** allow for empty queue */
			if (initialValues.hasMoreTokens()) {

				formObject = initialValues.nextToken().trim() + " R";
				// first value
				while (initialValues.hasMoreTokens())
					queue.addElement(initialValues.nextToken().trim() + " R");
			} else
				fieldsToProcess = false;

			StringTokenizer kidObjects = null;
			int kidCount;

			Map names = new HashMap();

			/** read each form object */
			while (fieldsToProcess) {

				/** read each form object */
				Map formData = currentPdfFile.readObject(formObject, false,fields);

				/** if its a kid with 1 element, add in data from parent */
				Map parentData = (Map) kidData.get(formObject);

				Map parentObj;
				String parentName;

				//handle form names
				if (formData.containsKey("T")) {
					if (formData.containsKey("Kids")) { //build list of names, scanning backwards recursively

						String name=(String)currentPdfFile.resolveToMapOrString("T", formData.get("T"));
						String parentRef = (String)formData.get("Parent");

						while(parentRef!=null){
							//read parent object
							parentObj=currentPdfFile.readObject(parentRef, false,fields);

							//get any name set there and append if not null. Try again
							parentName=(String)currentPdfFile.resolveToMapOrString("T", parentObj.get("T"));
							if(parentName!=null){
								name=parentName+"."+name;

								//System.out.println(name);

								//carry on up tree
								parentRef = (String)parentObj.get("Parent");
							}
						}

						//set fully qualified name
						names.put(formObject, name);
					} else {

						//String str = (String)currentPdfFile.resolveToMapOrString("T", formData.get("T"));

						//if(str.indexOf("Arbeitsverh�ltnis_4")!= -1){
						//System.out.println(str+"         ...>>>>>><<<<<<");
						//}

						Object parent = formData.get("Parent");
						if (parent == null) {
							formData.put("T", currentPdfFile.resolveToMapOrString("T", formData.get("T")));
						} else {
							formData.put("T", names.get(parent)+ "."+ currentPdfFile.resolveToMapOrString("T",formData.get("T")));
						}
					}

					//add in external data
					if(fdfData!=null){
						String name=(String)formData.get("T");
						String valueFromFDF=(String)fdfData.get(name);
						//System.out.println(name+" "+valueFromFDF+" "+fdfData+" "+formData);

						if(valueFromFDF!=null)
							formData.put("V",valueFromFDF);
					}
				}

				if (parentData != null) {
					Iterator i = parentData.keySet().iterator();

					while (i.hasNext()) {
						Object key = i.next();
						Object kidValue = parentData.get(key);

						if (!formData.containsKey(key))
							formData.put(key, kidValue);

					}
				}

				// System.out.println(formObject+" "+formData);
				String kids = (String) formData.get("Kids");
				//TODO @chris
				if (kids != null) {

					String initialPageList = kids;
					if (initialPageList.startsWith("["))
						// handle any square brackets (ie arrays)
						initialPageList = Strip.removeArrayDeleminators(initialPageList).trim();

					// put kids in the queue
					kidObjects = new StringTokenizer(initialPageList, "R");
					/**
					 * allow for kids being used as a way to separate out
					 * distinct objects (ie tree structure) rather than kids as
					 * part of composite object (ie buttons in group)
					 */
//					System.out.println("FT="+formData.get("FT"));
					if (formData.get("FT") == null
							|| !(((String) formData.get("FT")).indexOf("Btn") != -1)) {
						kidCount = 1;
					} else {// must be a button
						// kidCount=1;
						kidCount = kidObjects.countTokens();

						String flag = (String) (formData.get("Ff"));
						if (flag != null) {
							int flagValue = Integer.parseInt(flag);

							// if((flagValue &
							// FormStream.RADIO)==FormStream.RADIO ||
							// !((flagValue &
							// FormStream.PUSHBUTTON)==FormStream.PUSHBUTTON)){
							if (((flagValue & FormStream.PUSHBUTTON) == FormStream.PUSHBUTTON)) {

								// kidCount=kidObjects.countTokens();
								kidCount = 1;
							}
						}
					}
				} else {
					kidCount = 0;
				}
				String type = (String) formData.get("Type");
//				System.out.println("kidcount="+kidCount+" type="+type);
				if ((kidCount > 1)
						| ((type != null) && (type.equals("/Annot")))) {
					/** setup forms object */
					if (currentAcroFormData == null)
						currentAcroFormData = new PdfFormData(inDemo);

					if (kidCount == 0)
						kidCount = 1;
					currentAcroFormData.incrementCount(kidCount);

					if (flattenDebug) {

						/**
						 * convert any indirect values into actual data and put
						 * in array
						 */
						Map cleanedFormData = new HashMap();
						currentPdfFile.flattenValuesInObject(true, true,
								formData, cleanedFormData, fields, pageLookup,
								formObject);

						formData = cleanedFormData;

					} else { // setup page
						// removed to fix abacus file as need original
						// PageNumber to divide
						if(getPageCount()<2){
							formData.put("PageNumber", "1");
						}

						// add page
						if (formData.containsKey("P")) {
							try {
								Object rawValue = formData.get("P");

								if (rawValue != null && pageLookup != null
										&& rawValue instanceof String) {
									int page = pageLookup
									.convertObjectToPageNumber((String) rawValue);
									formData.put("PageNumber", "" + page);
									// currentForm.remove("P");
								}
							} catch (Exception e) {
							}

						}

						// flatten any kids (not Issie or Patrick)
						if (kidCount > 1) {
							String kidrefs = (String) formData.get("Kids");

							// put kids in the queue
							kidObjects = new StringTokenizer(Strip
									.removeArrayDeleminators(kidrefs), "R");

							Map formObjects = new HashMap();

							while (kidObjects.hasMoreTokens()) {
								String next_value = kidObjects.nextToken()
								.trim()
								+ " R";

								Map kidValue = currentPdfFile.readObject(
										next_value, false, fields);

								kidValue.put("PageNumber", "1");

								// add page
								if (kidValue.containsKey("P")) {
									try {
										Object rawValue = kidValue.get("P");

										if (rawValue != null
												&& pageLookup != null
												&& rawValue instanceof String) {
											int page = pageLookup
											.convertObjectToPageNumber((String) rawValue);
											kidValue.put("PageNumber", ""
													+ page);

										}
									} catch (Exception e) {
									}
								}
								formObjects.put(next_value, kidValue);
							}

							formData.put("Kids", formObjects);
						}

					}

					formData.put("obj", formObject);
//					System.out.println("form="+formData);
					/** store the element in our form object */
					try {
						currentAcroFormData.addFormElement(formData);
					} catch (Exception e) {
						e.printStackTrace();

					}

				} else if (kidCount == 1) { // separate out indirect (which we
					// flatten) from genuine groups

					// its an indirect kid [1 0 R] so flatten
					while (kidObjects.hasMoreTokens()) {
						String next_value = kidObjects.nextToken().trim()
						+ " R";
						queue.addElement(next_value);
						formData.remove("Kids");
						kidData.put(next_value, formData);
					}

				}

				// exit when all pages read
				if (queue.isEmpty())
					break;

				// get next page from queue
				formObject = (String) queue.firstElement();

				// and remove from our queue to avoid infinite loop
				queue.removeElement(formObject);

			}
		}
	}

	private void convertXMLtoValues(StringBuffer xmlStream) {

//		System.out.println("------XML stream-------");

//		System.out.println(xmlStream);
		/**
		 * now parse the XML
		 */
		StringBufferInputStream str = new StringBufferInputStream(xmlStream
				.toString());

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
			.newInstance();
			Document doc = factory.newDocumentBuilder().parse(str);

			NodeList nodes = doc.getChildNodes();

			scanNodes(nodes, 0);

			str.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void scanNodes(NodeList nodes, int level) {

		int count = nodes.getLength();

		StringBuffer space = new StringBuffer();
		for (int jj = 0; jj < level; jj++)
			space.append("   ");
		try {
			for (int i = 0; i < count; i++) {

				// get next node
				Node e = nodes.item(i);

//				System.out.println(space + e.getNodeName() + "="
//				+ e.getNodeValue() + " Type=" + e.getNodeType());

				// show attributes
				NamedNodeMap atts = e.getAttributes();
				if (atts != null) {
					int attCount = atts.getLength();
					for (int j = 0; j < attCount; j++) {
						Node cc = atts.item(j);
//						System.out.println(space + "   " + cc.getNodeName()
//						+ "=" + cc.getNodeValue());
					}
				}
				// carry on down tree
				if (e.hasChildNodes()) {
					// System.out.println(space+"children---------");
					scanNodes(e.getChildNodes(), level + 1);
					// System.out.println(space+"-----------------");
				}
			}
		} catch (Exception e) {
		}
	}

    //<end-adobe>

    /**
	 * set render mode to state what is displayed onscreen (ie
	 * RENDERTEXT,RENDERIMAGES) - only generally required if you do not wish to
	 * show all objects on screen (default is all). Add values together to
	 * combine settings.
	 *
	 */
	final public void setRenderMode(int mode) {

		renderMode = mode;

		setExtractionMode(mode);

	}

	/**
	 * set extraction mode telling JPedal what to extract -
	 * (TEXT,RAWIMAGES,FINALIMAGES - add together to combine) - See
	 * org.jpedal.examples for specific extraction examples
	 */
	final public void setExtractionMode(int mode) {

		extractionMode = mode;

	}

	/**
	 * General reset routine which should be called when new
	 * file opened if PdfDecoder is reused.
	 */
	final public void markAllPagesAsUnread() {

        //<start-adobe>
        this.flushToolTips();
        //<end-adobe>
    }

    //<start-adobe>
    /**
	 * flag which is set to true if current PDF is a form
	 */
	final public boolean isForm() {
		return isForm;
	}

	/**
	 *  provide method for
	 * outside class to get data object containing all form data - Returns null
	 * in demo version
	 */
	final public PdfFormData getPdfFormData() {

		return currentAcroFormData;

	}
    //<end-adobe>

    /**read object and setup Annotations for multipage view*/
	protected Map readObjectForPage(String currentPageOffset, int page,boolean redraw) {
		/** read page or next pages */
		Map values = currentPdfFile.readObject(currentPageOffset,false, null);

        //<start-adobe>
        annotsData=null;

		if ((showAnnotations) && (currentAnnotRenderer != null)) {

			//avoid multiple reads
			Integer key=new Integer(page);

			if(pagesRead.get(key)==null){

				pagesRead.put(key,"x");

				/**
				 * read the annotations reference for the page we have
				 * found lots of issues with annotations so trap errors
				 */
				annotObject = currentPdfFile.getValue((String) values.get("Annots"));

				if (renderPage)
					annotsData = getPdfAnnotsData(currentAnnotRenderer);
			}
		}

        /**
		 * draw acroform data onto Panel
		 */
		if ((formsAvailable) && (renderPage) && (!stopDecoding)) {

			if ((currentFormRenderer != null)
					&& (currentAcroFormData != null) && (!stopDecoding)) {
				currentFormRenderer.createDisplayComponentsForPage(
						page, null, scaling, displayRotation);
			}

			if ((showAnnotations) && (currentAnnotRenderer != null)
					&& (annotsData != null) && (!stopDecoding)) {
				currentAnnotRenderer.createDisplayComponentsForPage(
						page, null, scaling, displayRotation);
			}

			//force redraw
			if(redraw){
				lastFormPage=-1;
				lastEnd=-1;
				lastStart=-1;
			}

			//	this.validate();
		}
        //<end-adobe>

        return values;
	}


    /**
	 * method to return null or object giving access info fields and metadata.
	 */
	final public PdfFileInformation getFileInformationData() {

		if (currentPdfFile != null)
			return currentPdfFile.readPdfFileMetadata(XMLObject);
		else
			return null;

	}

    //<start-adobe>


    /**
	 * gives fine tuning over what is extracted -
	 *
	 * mode is a set of values which together show if text and images are
	 * rendered default is all can be set as a value for specific creator
	 * programs -
	 *
	 * dpi is the image dpi - Images are assumed at 72dpi so a value of 72 will
	 * not cause any rescaling -
	 *
	 * scaling is the page dpi as a factor of 72 (ie 1-72, 2=144)
	 */
	final public void setExtractionMode(int mode, int imageDpi, float scaling) {

		if(dpi % 72 != 0)
			LogWriter.writeLog("Dpi is not a factor of 72- this may cause problems");

		dpi = imageDpi;

		//if (scaling < .5)
		//	scaling = .5f;

		this.scaling = scaling;

		setExtractionMode(mode);

	}

	/**
	 * just extract annotations for a page - if you want to decode the page and
	 * extract the annotations use decodePage(int pageNumber) which does both.
	 *
	 * Now returns PdfAnnots object
	 */
	final public PdfAnnots decodePageForAnnotations(int i) {

		/** set general values and update flag */
		String value;

		PdfAnnots annotsData = null;

		/** check in range */
		if (i > getPageCount()) {

			LogWriter.writeLog("Page out of bounds");

		} else {

			/** get pdf object id for page to decode */
			String currentPageOffset = (String) pagesReferences
			.get(new Integer(i));

			/**
			 * decode the file if not already decoded, there is a valid object
			 * id and it is unencrypted
			 */
			if ((currentPageOffset != null)) {

				// if(currentPdfFile==null)
				// throw new PdfException ("File not open - did you call
				// closePdfFile() inside a loop and not reopen");

				/** read page or next pages */
				Map values = currentPdfFile.readObject(currentPageOffset,
						false, null);

				/**
				 * read the annotationations for the page we have found lots of
				 * issues with annotations so trap errors
				 */
				try {
					value = currentPdfFile.getValue((String) values
							.get("Annots"));

					if (value != null) {
						annotsData = new PdfAnnots(currentPdfFile, pageLookup);
						annotsData.readAnnots(value);
					}

				} catch (Exception e) {
					LogWriter.writeLog("[PDF] " + e + " with annotation");
				}
			}
		}

		return annotsData;
	}
    //<end-adobe>

    /**
	 * get pdf as Image of any page scaling is size (100 = full size)
	 * Use getPageAsImage to create images
	 * @deprecated
	 */
	final public BufferedImage getPageAsThumbnail(int pageNumber, int h) {

		BufferedImage image;
		int mediaX, mediaY, mediaW, mediaH;

		/** the actual display object */

		DynamicVectorRenderer imageDisplay = new DynamicVectorRenderer(pageNumber,true,
				1000, this.objectStoreRef); //
		imageDisplay.setHiResImageForDisplayMode(useHiResImageForDisplay);
		// simageDisplay.setDirectRendering((Graphics2D) graphics);

		try {

			/** check in range */
			if (pageNumber > getPageCount()) {

				LogWriter.writeLog("Page " + pageNumber + " out of bounds");

			} else {

				/** resolve page size */
				mediaX = pageData.getMediaBoxX(pageNumber);
				mediaY = pageData.getMediaBoxY(pageNumber);
				mediaW = pageData.getMediaBoxWidth(pageNumber);
				mediaH = pageData.getMediaBoxHeight(pageNumber);

				/** get pdf object id for page to decode */
				String currentPageOffset = (String) pagesReferences.get(new Integer(pageNumber));

				/**
				 * decode the file if not already decoded, there is a valid
				 * object id and it is unencrypted
				 */
				if ((currentPageOffset != null)) {

					/** read page or next pages */
					Map values = currentPdfFile.readObject(currentPageOffset,
							false, null);

					/** get information for the page */
					String value = (String) values.get("Contents");

					if (value != null) {

						PdfStreamDecoder imageDecoder = new PdfStreamDecoder(useHiResImageForDisplay);
						imageDecoder.setExternalImageRender(customImageHandler);
						imageDecoder.setName(filename);
						imageDecoder.setStore(objectStoreRef);
						Map resValue = currentPdfFile.getSubDictionary(values
								.get("Resources"));

						imageDecoder.init(true, true, renderMode, 0, pageData,
								pageNumber, imageDisplay, currentPdfFile,
								globalRes, resValue);

						int rotation = pageData.getRotation(pageNumber);
						imageDisplay.init(mediaW, mediaH, rotation);
						imageDecoder.decodePageContent(value, mediaX, mediaY,
								null);

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		/**
		 * workout scaling and get image
		 */
		image = getImageFromRenderer(h, imageDisplay, pageNumber);

		return image;

	}

    //<start-adobe>
    /**
	 * set status bar to use when decoding a page - StatusBar provides a GUI
	 * object to display progress and messages.
	 *
	 */
	public void setStatusBarObject(StatusBar statusBar) {
		this.statusBar = statusBar;
	}

	/**
	 * wait for rendering to finiah
	 */
	public void waitForRenderingToFinish(){

		//wait to die
		while (isDecoding()) {
			// System.out.println("Waiting to die");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// should never be called
				e.printStackTrace();
			}
		}
	}
    //<end-adobe>

    /**
	 * ask JPedal if stopDecoding() request completed
	 */
	public boolean isDecoding() {

		boolean decodeStatus = true;
		
		if ((!isDecoding) &&(!pages.isDecoding())&& ((current == null) || (current.exitedDecoding())))
			decodeStatus = false;
		//System.out.println(isDecoding+" "+current+" "+current);
		return decodeStatus;
	}

	/**
	 * ask JPedal to stop printing a page
	 */
	final public void stopPrinting() {
		stopPrinting = true;
	}

	/**
	 * ask JPedal to stop decoding a page - this is not part
	 * of api and not a
	 * recommend way to shutdown a thread
	 */
	public void stopDecoding() {

		if(stopDecoding)
			return;

		pages.stopGeneratingPage();
		stopDecoding = true;

		if(currentPdfFile!=null)
			currentPdfFile.setInterruptRefReading(true);

		if (current != null)
			current.terminateDecoding();
		
		// wait to die
		while (isDecoding()) {
			// System.out.println("Waiting to die");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// should never be called
				e.printStackTrace();
			}
		}

		if(currentPdfFile!=null)
			currentPdfFile.setInterruptRefReading(false);
		stopDecoding = false;

		//currentDisplay.flush();
		//screenNeedsRedrawing = true;

        //<start-adobe>
        if (renderPage && formsAvailable) {
			if (currentFormRenderer != null)
				currentFormRenderer.removeDisplayComponentsFromScreen(this);

			if ((currentAnnotRenderer != null) && (showAnnotations))
				currentAnnotRenderer.removeDisplayComponentsFromScreen(this);

			//invalidate();
		}
        //<end-adobe>

    }

    //<start-adobe>

    //<end-jfl>

	/**
	 * gets DynamicVector Object - NOT PART OF API and subject to change (DO NOT USE)
	 *
	 */
	public DynamicVectorRenderer getDynamicRenderer(){
		return currentDisplay;
	}

	//<start-jfl>
	/**
	 * extract marked content - not yet live
	 */
	final public void decodePageForMarkedContent(String ref,Object pageStream) throws Exception {

		//ref can page 1 (ie page number) or 12 0 R (ie ref)

		/** set general values and update flag */
		String value;

		/** check in range */
		if (!stopDecoding) {

			boolean debug = true;

			if(debug )
				System.out.println("Start decode of "+ref);


			int page=-1;
			if(ref.indexOf(" ")!=-1){
				page=pageLookup.convertObjectToPageNumber(ref);
			}else{
				page=Integer.parseInt(ref);
			}
			//get Page data in Map

			/** get pdf object id for page to decode */
			Map pageValues = getPageData(page);
			Map resValue = null;
			if(pageValues!=null){
				resValue=getPageResources(pageValues);
			}
			/** read page or next pages */
			if(pageValues!=null){

				/** get information for the page */
				value = (String) pageValues.get("Contents");

				// if (!value.equals("null"))
				if ((value != null)) {
					PdfStreamDecoder current = new PdfStreamDecoder(useHiResImageForDisplay);

					current.setName(filename);
					current.setStore(objectStoreRef);
					current.includeImages();

					/** pass in statusBar */
					current.setStatusBar(statusBar);

					int mode=PdfDecoder.TEXT+PdfDecoder.RAWIMAGES+PdfDecoder.FINALIMAGES;
					if (!stopDecoding) {
						current.init(true, false, renderMode,
								mode, pageData, page,
								null, currentPdfFile, globalRes,
								resValue);
					}

                    current.setMapForMarkedContent(pageStream);

                    current.decodePageContent(value, 0, 0, null);


					//pdfImages = current.getImages();

				}
			}
		}
	}

	/**used to decode multiple pages on views
	 **/
	public final void decodeOtherPages(int pageCount) {
		pages.decodeOtherPages(pageNumber, pageCount);
	}
    //<end-adobe>

    /**
	 * decode a page, - <b>page</b> must be between 1 and
	 * <b>PdfDecoder.getPageCount()</b> - Will kill off if already running
	 */
	final public void decodePage(int page) throws Exception {


		if(lastPageDecoded ==page)
		lastPageDecoded =page;

		decodeStatus = "";

		if(this.displayView!=Display.SINGLE_PAGE){
			return ;
		}

		/**
		 * shutdown if already decoding previous page
		 */
		stopDecoding();


		if (isDecoding) {
			LogWriter.writeLog("[PDF]WARNING - this file is being decoded already");
			isDecoding = false; // can be switched off to instrcut method to
			// exit asap

		} else {


			isDecoding = true; // can be switched off to instrcut method to
			// exit asap

			cursorBoxOnScreen = null;

            //<start-adobe>
            if ((renderPage) && (formsAvailable)) {
				if (currentFormRenderer != null)
					currentFormRenderer.removeDisplayComponentsFromScreen(this);

				if ((currentAnnotRenderer != null) && (showAnnotations))
					currentAnnotRenderer.removeDisplayComponentsFromScreen(this);

				//invalidate();
			}
            //<end-adobe>

            /** flush renderer */
			currentDisplay.flush();
			//pages.refreshDisplay();

			/** set general values and update flag */
			String value;

			/** check in range */
			if (page > getPageCount() || page < 1) {

				LogWriter.writeLog("Page out of bounds");

			} else if (!stopDecoding) {

                //<start-adobe>
                // <start-13>
				/**
				 * title changes to give user something to see under timer
				 * control
				 */
				Timer t = null;
				if (statusBar != null) {
					ActionListener listener = new ProgressListener();
					t = new Timer(500, listener);
					t.start(); // start it
				}
				// <end-13>

                //<end-adobe>

                this.pageNumber = page;

				//get Page data in Map

				/** get pdf object id for page to decode */
				Map pageValues = getPageData(page);
				Map resValue = null;
				if(pageValues!=null)
					resValue=getPageResources(pageValues);

				/** read page or next pages */
				if(pageValues!=null && !stopDecoding){

                    //<start-adobe>
                    /**
					 * read the annotations reference for the page we have found
					 * lots of issues with annotations so trap errors
					 */
					annotObject = currentPdfFile.getValue((String) pageValues.get("Annots"));
					annotsData = null;
					if ((renderPage) && (!stopDecoding)) {
						pagesRead.put(new Integer(page),"x");
						annotsData = getPdfAnnotsData(currentAnnotRenderer);
					}
                    //<end-adobe>

                    /** get information for the page */
					value = (String) pageValues.get("Contents");

                    //<start-adobe>
                    /** flush annotations */
					if ((displayHotspots != null) && (!stopDecoding))
						displayHotspots.flushAnnotationsDisplayed();
                    //<end-adobe>

                    // if (!value.equals("null"))
					if ((value != null) && (!stopDecoding)) {

						current = new PdfStreamDecoder(useHiResImageForDisplay);
						current.setExternalImageRender(customImageHandler);


						current.setName(filename);
						current.setStore(objectStoreRef);
						if (includeImages)
							current.includeImages();

						/** pass in statusBar */
						current.setStatusBar(statusBar);

						/** set hires mode or not for display */
						currentDisplay.setHiResImageForDisplayMode(useHiResImageForDisplay);


						if (!stopDecoding) {
							current.init(true, renderPage, renderMode,
									extractionMode, pageData, page,
									currentDisplay, currentPdfFile, globalRes,
									resValue);
						}

						/** pass in visual multithreaded status bar */
						if (!stopDecoding)
							current.setStatusBar(statusBar);

						int mediaW = pageData.getMediaBoxWidth(pageNumber);
						int mediaH = pageData.getMediaBoxHeight(pageNumber);
						//int mediaX = pageData.getMediaBoxX(pageNumber);
						//int mediaY = pageData.getMediaBoxY(pageNumber);
						int rotation = pageData.getRotation(pageNumber);
						currentDisplay.init(mediaW, mediaH, rotation);
						/** toke out -min's%% */

						if (g2 != null)
							current.setDirectRendering(g2);

						try {
							if (!stopDecoding)
								current.decodePageContent(value, 0, 0, null);// mediaX,mediaY);/**removed
							// min_x,min_y%%*/
						} catch (Error err) {
							decodeStatus = decodeStatus
							+ "Error in decoding page "
							+ err.toString();
						}

						if (!stopDecoding) {
							hasEmbeddedFonts = current.hasEmbeddedFonts();

							fontsInFile = PdfStreamDecoder.getFontsInFile();

							pdfData = current.getText();
							if (embedWidthData)
								pdfData.widthIsEmbedded();

							// store page width/height so we can translate 270
							// rotation co-ords
							pdfData.maxX = mediaW;
							pdfData.maxY = mediaH;

							pdfImages = current.getImages();

                            //<start-adobe>
                            /** get shape info */
							pageLines = current.getPageLines();
                            //<end-adobe>
						}

						imagesProcessedFully = current.hasAllImages();

						current = null;

					}
				}

				/** turn off status bar update */
				// <start-adobe>
				if (t != null) {

					t.stop();
					statusBar.setProgress(100);

				}
				// <end-adobe>

				isDecoding = false;
				pages.refreshDisplay();

                //<start-adobe>
                /**
				 * handle acroform data to display
				 */
				if ((formsAvailable) && (renderPage) && (!stopDecoding)) {

					if ((currentFormRenderer != null)
							&& (currentAcroFormData != null) && (!stopDecoding)) {
						currentFormRenderer.createDisplayComponentsForPage(
								page, this, scaling, displayRotation);
					}

					if ((showAnnotations) && (currentAnnotRenderer != null)
							&& (annotsData != null) && (!stopDecoding)) {
						currentAnnotRenderer.createDisplayComponentsForPage(
								page, this, scaling, displayRotation);
					}

					//	this.validate();
				}

                // set up page hotspots
				if ((annotsData != null) && (displayHotspots != null)
						&& (!stopDecoding))
					displayHotspots.setHotspots(annotsData);
                //<end-adobe>

            }

            //track JBIG error
            if(currentPdfFile.containsJBIG())
                decodeStatus=decodeStatus+"Unsupported JBIG streams in PDF";
            
            current = null;


		}
	}

	/**
	 * used internally and should not be needed
	 */
	private Map getPageResources(Map pageValues) {
		Map resValue = currentPdfFile.getSubDictionary(pageValues.get("Resources"));

		// if not present, look for it back up tree
		if ((resValue == null) && (!stopDecoding)) {
			String parent = (String) pageValues.get("Parent");
			while ((parent != null) && (resValue == null)) {
				Map parentValues = currentPdfFile.readObject(parent, false, null);

				Object res = parentValues.get("Resources");
				if (res == null) {
					parent = (String) parentValues
					.get("Parent");
				} else if (res instanceof String)
					resValue = currentPdfFile
					.getSubDictionary(res);
				else
					resValue = (Map) res;
			}
		}
		return resValue;
	}

	/**
	 * return actual Object data for page
	 */
	private Map getPageData(int page) throws PdfException {
		String currentPageOffset = (String) pagesReferences.get(new Integer(page));

		/**
		 * decode the file if not already decoded, there is a valid
		 * object id and it is unencrypted
		 */
		if (currentPageOffset!=null && currentPdfFile == null)
			throw new PdfException("File not open - did you call closePdfFile() inside a loop and not reopen");

		return currentPdfFile.readObject(currentPageOffset,false, null);

	}
	//<end-jfl>

    //<start-adobe>
    /**
	 * allow user to add grapical content on top of page - for display and printing
	 * only effects last page decoded and will be flushed on new page/file.
	 * Additional calls will overwrite current settings on page
	 * ONLY works in SINGLE VIEW displaymode
     *
     * Passing a null value for last parameter will flush all items on page
	 *
	 *
	 */
	public void drawAdditionalObjectsOverPage(int page,int[] type, Color[] colors, Object[] obj) throws PdfException {

        Integer key=new Integer(page);

        if(obj==null){ //flush page
           overlayType.remove(key);
           overlayColors.remove(key);
           overlayObj.remove(key);

           if(page==this.pageNumber)
                currentDisplay.drawAdditionalObjectsOverPage(null,null, null); 
        }else{ //store for printing and add if items already there

            int[] oldType= (int[]) overlayType.get(key);
            if(oldType==null)
                overlayType.put(key,type);
            else{ //merge items

                int oldLength=oldType.length;
                int newLength=type.length;
                int[] combined =new int[oldLength+newLength];

                for(int i=0;i<oldLength;i++)
                combined[i]=oldType[i];

                for(int i=0;i<newLength;i++)
                combined[i+oldLength]=type[i];

                overlayType.put(key, combined);
            }


            Color[] oldCol= (Color[]) overlayColors.get(key);
            if(oldCol==null)
                overlayColors.put(key,colors);
            else{ //merge items

                int oldLength=oldCol.length;
                int newLength=colors.length;
                Color[] combined =new Color[oldLength+newLength];

                for(int i=0;i<oldLength;i++)
                combined[i]=oldCol[i];

                for(int i=0;i<newLength;i++)
                combined[i+oldLength]=colors[i];

                overlayColors.put(key, combined);
            }


            Object[] oldObj= (Object[]) overlayObj.get(key);
            if(oldType==null)
                overlayObj.put(key,obj);
            else{ //merge items

                int oldLength=oldObj.length;
                int newLength=obj.length;
                Object[] combined =new Object[oldLength+newLength];

                for(int i=0;i<oldLength;i++)
                combined[i]=oldObj[i];

                for(int i=0;i<newLength;i++)
                combined[i+oldLength]=obj[i];

                overlayObj.put(key, combined);
            }


            //add to screen display
            if(page==this.pageNumber)
                currentDisplay.drawAdditionalObjectsOverPage(type,colors, obj);
        }
    }
    //<end-adobe>

    /**
	 * uses hires images to create a higher quality display - downside is it is
	 * slower and uses more memory (default is false).- Does nothing in OS
	 * version
	 *
	 * @param value
	 */
	public void useHiResScreenDisplay(boolean value) {
	}

    //<start-adobe>
    //<start-jfl>
	/**
	 * decode a page as a background thread (use
	 * other background methods to access data).
	 */
	final public void decodePageInBackground(int i) throws Exception {

		if (isBackgroundDecoding) {
			LogWriter
			.writeLog("[PDF]WARNING - this file is being decoded already in background");
		} else {
			isBackgroundDecoding = true;

			/** set general values and update flag */
			String value;

			/** check in range */
			if (i > getPageCount()) {

				LogWriter.writeLog("Page out of bounds");

			} else {

				/** get pdf object id for page to decode */
				String currentPageOffset = (String) pagesReferences
				.get(new Integer(i));

				/**
				 * decode the file if not already decoded, there is a valid
				 * object id and it is unencrypted
				 */
				if ((currentPageOffset != null)) {

					if (currentPdfFile == null)
						throw new PdfException(
						"File not open - did you call closePdfFile() inside a loop and not reopen");

					/** read page or next pages */
					Map values = currentPdfFile.readObject(currentPageOffset,
							false, null);

					/** get information for the page */
					value = (String) values.get("Contents");

					// if (!value.equals("null"))
					if (value != null) {

						PdfStreamDecoder backgroundDecoder = new PdfStreamDecoder();
						backgroundDecoder.setExternalImageRender(customImageHandler);
						backgroundDecoder.setName(filename);
						backgroundDecoder.setStore(backgroundObjectStoreRef);

						Map resValue = currentPdfFile.getSubDictionary(values
								.get("Resources"));

						backgroundDecoder.init(true, false, 0, extractionMode,
								pageData, i, null, currentPdfFile, globalRes,
								resValue);

						backgroundDecoder.decodePageContent(value, 0, 0, null);
						/** removed min_x,min_y%% */

						pdfBackgroundData = backgroundDecoder.getText();
						if (embedWidthData)
							pdfBackgroundData.widthIsEmbedded();

						// store page width/height so we can translate 270
						// rotation co-ords
						int mediaW = pageData.getMediaBoxWidth(i);
						int mediaH = pageData.getMediaBoxHeight(i);
						//int mediaX = pageData.getMediaBoxX(i);
						//int mediaY = pageData.getMediaBoxY(i);

						pdfBackgroundData.maxX = mediaW;
						pdfBackgroundData.maxY = mediaH;

						pdfBackgroundImages = backgroundDecoder.getImages();

					}
				}

			}
			isBackgroundDecoding = false;
		}
	}
    //<end-adobe>

    /**
	 * get page count of current PDF file
	 */
	final public int getPageCount() {
		return pageCount;
	}

	/**
	 * return true if the current pdf file is encrypted <br>
	 * check <b>isFileViewable()</b>,<br>
	 * <br>
	 * if file is encrypted and not viewable - a user specified password is
	 * needed.
	 */
	final public boolean isEncrypted() {
		if (currentPdfFile != null)
			return currentPdfFile.isEncrypted();
		else
			return false;
	}

	/** show if encryption password has been supplied */
	final public boolean isPasswordSupplied() {
		if (currentPdfFile != null)
			return currentPdfFile.isPasswordSupplied();
		else
			return false;
	}

	/**
	 * show if encrypted file can be viewed,<br>
	 * if false a password needs entering
	 */
	public boolean isFileViewable() {
		if (currentPdfFile != null)
			return currentPdfFile.isFileViewable();
		else
			return false;
	}

	/** show if content can be extracted */
	public boolean isExtractionAllowed() {
		if (currentPdfFile != null)
			return currentPdfFile.isExtractionAllowed();
		else
			return false;
	}

    /**give user access to PDF flag value
     * - if file not open or input not valid
     * returns -1
     *
     * Possible values in PdfFLAGS
     *
     * ie PDFflags.USER_ACCESS_PERMISSIONS - return P value
     * PDFflags.VALID_PASSWORD_SUPPLIED - tell if password supplied and if owner or user
     **/
	public int getPDFflag(Integer i) {
		if (currentPdfFile != null)
			return currentPdfFile.getPDFflag(i);
		else
			return -1;
	}

    /**
	 * used to retest access and see if entered password is valid,<br>
	 * If so file info read and isFileViewable will return true
	 */
	final private void verifyAccess() {
		if (currentPdfFile != null) {
			try {
				openPdfFile();
			} catch (Exception e) {
				LogWriter.writeLog("Exception " + e + " opening file");
			}
		}
	}
	//<end-jfl>


	/**
	 * set the font used for default from Java fonts on system - Java fonts are
	 * case sensitive, but JPedal resolves this internally, so you could use
	 * Webdings, webdings or webDings for Java font Webdings - checks if it is a
	 * valid Java font (otherwise it will default to Lucida anyway)
	 */
	public final void setDefaultDisplayFont(String fontName)
	throws PdfFontException {

		boolean isFontInstalled = false;

		// get list of fonts and see if installed
		String[] fontList = GraphicsEnvironment.getLocalGraphicsEnvironment()
		.getAvailableFontFamilyNames();

		int count = fontList.length;

		for (int i = 0; i < count; i++) {
			if (fontList[i].toLowerCase().equals(fontName.toLowerCase())) {
				isFontInstalled = true;
				defaultFont = fontList[i];
				i = count;
			}
		}

		if (!isFontInstalled)
			throw new PdfFontException("Font " + fontName
					+ " is not available.");

	}

	//<start-jfl>
	/**
	 * set a password for encryption - software will resolve if user or owner
	 * password- calls verifyAccess() from 2.74 so no separate call needed
	 */
	final public void setEncryptionPassword(String password) throws PdfException {

        if(currentPdfFile==null)
        throw new PdfException("Must open PdfDecoder file first");

        currentPdfFile.setEncryptionPassword(password);
		verifyAccess();
	}

	/**
	 * routine to open a byte stream cntaining the PDF file and extract key info
	 * from pdf file so we can decode any pages. Does not actually decode the
	 * pages themselves.
	 */
	final public void openPdfArray(byte[] data) throws PdfException {

		LogWriter.writeMethod("{openPdfArray}", 0);

		globalRes=null;
		pagesReferences.clear();

		try {

			currentPdfFile = new PdfReader();

			/** get reader object to open the file */
			currentPdfFile.openPdfFile(data);

			openPdfFile();

			if(stopDecoding)
				closePdfFile();

			/** store file name for use elsewhere as part of ref key without .pdf */
			objectStoreRef.storeFileName("r" + System.currentTimeMillis());

		} catch (Exception e) {
			throw new PdfException("[PDF] OpenPdfArray generated exception "
					+ e.getMessage());
		}
	}

	/**
	 * routine to open PDF file and extract key info from pdf file so we can
	 * decode any pages. Does not actually decode the pages themselves. Also
	 * reads the form data. You must explicitly close any open files with
	 * closePdfFile() to Java will not release all the memory
	 */
	final public void openPdfFile(final String filename) throws PdfException {

		displayScaling=null;


		LogWriter.writeMethod("{openPdfFile " + filename + "}", 0);

		this.filename = filename;
		globalRes=null;
		pagesReferences.clear();


		/** store file name for use elsewhere as part of ref key without .pdf */
		objectStoreRef.storeFileName(filename);

		/**
		 * possible caching of code File testFile=new File(filename);
		 *
		 * int size=(int)testFile.length(); if(size<300*1024){ byte[]
		 * fileData=new byte[size]; // read the object try {
		 *
		 * FileInputStream fis=new FileInputStream(testFile);
		 *
		 * //get binary data fis.read( fileData ); } catch( Exception e ) {
		 * LogWriter.writeLog( "Exception " + e + " reading from file" ); }
		 *
		 * openPdfFile(fileData); }else
		 */

		currentPdfFile = new PdfReader();

		/** get reader object to open the file */
		if(!stopDecoding)
			currentPdfFile.openPdfFile(filename);

		if(!stopDecoding)
			openPdfFile();


			if(stopDecoding)
				closePdfFile();

	}

	/**
	 * routine to open PDF file via URL and extract key info from pdf file so we
	 * can decode any pages - Does not actually decode the pages themselves -
	 * Also reads the form data - Based on an idea by Peter Jacobsen
	 *
	 * You must explicitly close any open files with closePdfFile() to Java will
	 * not release all the memory
	 */
	final public void openPdfFileFromURL(String pdfUrl) throws PdfException {

		LogWriter.writeMethod("{openPdfFileFromURL " + pdfUrl + "}", 0);

		displayScaling=null;
		globalRes=null;
		pagesReferences.clear();


		URL url;
		byte[] pdfByteArray = null;
		InputStream is;
		ByteArrayOutputStream os;

		try {
			url = new URL(pdfUrl);
			is = url.openStream();
			os = new ByteArrayOutputStream();

			// Download buffer
			byte[] buffer = new byte[4096];

			// Download the PDF document
			int read;
			while ((read = is.read(buffer)) != -1) {
				os.write(buffer, 0, read);
			}
			// Copy output stream to byte array
			pdfByteArray = os.toByteArray();

			// Close streams
			is.close();
			os.close();

		} catch (IOException e) {
			LogWriter.writeLog("[PDF] Exception " + e + " opening URL "
					+ pdfUrl);
		}

		currentPdfFile = new PdfReader();

		/** get reader object to open the file */
		currentPdfFile.openPdfFile(pdfByteArray);

		openPdfFile();

		/** store file name for use elsewhere as part of ref key without .pdf */
		objectStoreRef.storeFileName("<raw data>");

		if(stopDecoding)
			closePdfFile();
	}

	/**
	 * common code to all open routines
	 */
	private void openPdfFile() throws PdfException {
		
		isOpen=false;
		
		LogWriter.writeMethod("{openPdfFile}", 0);
		// ensure no previous file still being decoded
		//and preserve status if stop request issued
		boolean decodingStaus=this.stopDecoding;
		stopDecoding();
		stopDecoding=decodingStaus;

		pageNumber = 1; // reset page number for metadata

		lastFormPage=-1;
		lastEnd=-1;
		lastStart=-1;

		//handle fdf
		if(filename!=null && filename.toLowerCase().endsWith(".fdf")){

			int i = filename.lastIndexOf("/");

			if(i==-1)
				i = filename.lastIndexOf("\\");

			String path="";
			if(i!=-1)
				path=filename.substring(0,i + 1);

			/**read in data from fdf*/

			Map data=currentPdfFile.readFDF();

			/** store file name for use elsewhere as part of ref key without .pdf */
			byte[] pdfFile=currentPdfFile.getByteTextStringValue(data.get("F"),fdfData);

			if(pdfFile!=null)
				filename=path+currentPdfFile.getTextString(pdfFile);
			objectStoreRef.storeFileName(filename);

			//open actual PDF
			this.currentPdfFile.openPdfFile(filename);

			//make it just the fields
			fdfData=(Map)data.get("Fields");
		}else
			fdfData=null;

		try {
			isDecoding = true;

			pages.resetCachedValues();



			// set cache size to use
			if(!stopDecoding){
				currentPdfFile.setCacheSize(minimumCacheSize);

                // reset printing
                lastPrintedPage = -1;
                this.currentPrintDecoder = null;

                //<start-adobe>

                //reset javascript object
                if(javascript!=null)
                javascript.reset();

				if (formsAvailable) {
					if (currentFormRenderer != null) {
						currentFormRenderer.removeDisplayComponentsFromScreen(this);
					}

					if ((showAnnotations) && (currentAnnotRenderer != null)) {
						currentAnnotRenderer.removeDisplayComponentsFromScreen(this);
					}
				}
				//invalidate();

                //<end-adobe>

            }

			if (!stopDecoding){

				// reset page data - needed to flush crop settings
				pageData = new PdfPageData();
				// read and log the version number of pdf used
				pdfVersion = currentPdfFile.getType();
				LogWriter.writeLog("Pdf version : " + pdfVersion);

				if(pdfVersion==null){
					currentPdfFile=null;
					isDecoding=false;
				}
				
				if (pdfVersion.indexOf("1.5") != -1)
					LogWriter.writeLog("Please note Pdf version 1.5  some features not fully supported ");
				else if (pdfVersion.indexOf("1.6") != -1)
					LogWriter.writeLog("Please note Pdf version 1.6  new features not fully supported ");

				LogWriter.writeMethod("{about to read ref table}", 0);

			}
			// read reference table so we can find all objects and say if
			// encrypted
			String root_id = null;
			if(!stopDecoding)
				root_id = currentPdfFile.readReferenceTable();
			Map values=null;

			if (!stopDecoding){

				// read the catalog
				LogWriter.writeMethod("{about to read catalog}", 0);

				if(root_id!=null)
					values = currentPdfFile.readObject(root_id, false, null);
			}

			// open if not encrypted or has password
			if ((!stopDecoding)&&((!this.isEncrypted()) | (this.isPasswordSupplied()))) {

				// read any info and assign to global value
				XMLObject = (String) values.get("Metadata");

				// get pointer to pages and read the read page info
				String value = (String) values.get("Pages");
				LogWriter.writeMethod("{about to read pages}", 0);


				if (stopDecoding)
					return;

				if (value != null) {
					LogWriter.writeLog("Pages being read " + value);
					pageNumber = 1; // reset page number for metadata

					// reset lookup table
					pageLookup = new PageLookup();

					readAllPageReferences(value);

					pageCount = pageNumber - 1; // save page count
					pageNumber = 0; // reset page number for metadata;

					if (this.getPageCount() == 0)
						LogWriter.writeLog("No pages found");
				}

				if (!stopDecoding){

					// read any names
					Object names = null;
					try {
						names = values.get("Names");
						if (names != null){

                            if(javascript==null  && this.useJavascript)
                            javascript=new Javascript();
                            
                            currentPdfFile.readNames(names,javascript);
                        }
					} catch (Exception e) {
						LogWriter.writeLog("Exception reading Names object "
								+ names + " " + objectStoreRef.fullFileName);
					}
				}

				if (!stopDecoding){

                    isXFA = false;

                    //<start-adobe>

                    // read any info and assign to global value
					outlineObject = values.get("Outlines");
					outlineData = null;
					hasOutline = outlineObject != null;


					// Read any form data
					Object rawValue = values.get("AcroForm");
					if (rawValue != null) {
						/**
						 * reinitialize formRenderer for a file with AcroForms
						 */
						// if(currentFormRenderer==null)
						// currentFormRenderer=new DefaultAcroRenderer();
						// LogWriter.writeLog("Acro Form being read ");
						readAcroForm(rawValue);

						// LogWriter.writeLog("Data read");
						isForm = true;
					} else {
						isForm = false;

						currentAcroFormData = null;

					}

					/**
					 * objects for structured content
					 */
					//read any structured info
					Object obj=values.get("StructTreeRoot");
					Map markInfo=null,structTreeRoot=null;
					if(obj!=null){
						if (obj instanceof String)
							structTreeRoot=currentPdfFile.readObject((String) obj,false, null);
						else
							structTreeRoot= (Map) obj;
					}

                    //mark info object
					obj=values.get("MarkInfo");

					if(obj!=null){
						if (obj instanceof String)
							markInfo=currentPdfFile.readObject((String) obj,false, null);
						else
							markInfo= (Map) obj;
					}

					content.setRootValues(structTreeRoot,markInfo);

					// pass handle into renderer
					if (formsAvailable) {
						if ((currentFormRenderer != null)) {
							currentFormRenderer.openFile(pageCount);
							currentFormRenderer.init(currentAcroFormData, insetW,
									insetH, pageData, currentPdfFile);
						}

						// flush Annots structures - required as Annots exist on
						// page level, Forms on Doc level
						if ((showAnnotations) && (currentAnnotRenderer != null))
							currentAnnotRenderer.openFile(pageCount);
					}

                    //<end-adobe>
                }

			}

			currentOffset = null;

			// reset so if continuous view mode set it will be recalculated for
			// page
			pages.disableScreen();

			if(!stopDecoding)
				pages.stopGeneratingPage();

			//force back if only 1 page
			if(pageCount<2)
				displayView=Display.SINGLE_PAGE;

            //<start-adobe>
            setDisplayView(this.displayView,alignment); //force reset and add back listener
            //<end-adobe>

            isOpen=true;
		} catch (PdfException e) {
			isDecoding = false;
			throw new PdfException(e.getMessage() + " opening file");
		}

		isDecoding = false;

	}

	/**
	 * read the data from pages lists and pages so we can open each page.
	 *
	 * @param currentPageOffset -
	 *            object reference to first trailer
	 */
	private void readAllPageReferences(String currentPageOffset) {

		LogWriter.writeMethod("{readAllPageReferences " + currentPageOffset
				+ "}", 0);

		// LogWriter.writeLog("Page metadata being read for
		// "+currentPageOffset);

		Map values = currentPdfFile.readObject(currentPageOffset, false, null);


		// get if kid, pages, page
		String type = (String) values.get("Type");
		if (type == null)
			type = "/Pages";

		/**
		 * handle common values which can occur at page level or higher
		 */


		/** page rotation */
		String value = currentPdfFile.getValue((String) values.get("Rotate"));

		//check inheritance
		if (value == null){
			String parent=(String) values.get("Parent");
			while(parent!=null && value==null){
				Map parentObject=currentPdfFile.readObject(parent, false, null);
				value = currentPdfFile.getValue((String) parentObject.get("Rotate"));
				parent=(String) parentObject.get("Parent");

			}
		}


		if (value == null)
			value = "0";

		pageData.setPageRotation(value, pageNumber);

		/**
		 * handle media and crop box, defaulting to higher value if needed (ie
		 * Page uses Pages and setting crop box
		 */
		String mediaValue = currentPdfFile.getValue((String) values
				.get("MediaBox"));
		if (mediaValue != null)
			pageData.setMediaBox(mediaValue);
		else {
			String testType = type;
			if (type.equals("Page")) {
				mediaValue = (String) globalMediaValues.get("Pages");
				if (mediaValue == null)
					testType = "Kids";
			}
			if ((mediaValue == null) && (testType.equals("Kids"))) {

				mediaValue = (String) globalMediaValues.get("Kids");
				if (mediaValue == null)
					testType = "Catalog";
			}
			if ((mediaValue == null) && (testType.equals("Catalog"))) {

				mediaValue = (String) globalMediaValues.get("Catalog");
			}
			if (mediaValue == null)
				mediaValue = "0 0 800 800";
		}

		value = currentPdfFile.getValue((String) values.get("CropBox"));
		if (value != null)
			pageData.setCropBox(value);
		if (!type.equals("Page"))
			globalMediaValues.put(type, mediaValue);



		if (stopDecoding)
			return;

		/** process page ro read next level down */
		if (type.indexOf("/Pages") != -1) {

			globalRes = currentPdfFile
			.getSubDictionary(values.get("Resources"));

			value = Strip.removeArrayDeleminators(currentPdfFile
					.getValue((String) values.get("Kids"))); // get initial
			// pages

			if (value.length() > 0) {
				/** allow for empty value and put next pages in the queue */
				StringTokenizer initialValues = new StringTokenizer(value, "R");
				while (initialValues.hasMoreTokens())
					readAllPageReferences(initialValues.nextToken().trim()
							+ " R");
				// queue.addElement(initialValues.nextToken().trim() + " R");
			}
		} else if (type.indexOf("/Page") != -1) {
			// store ref for later
			pagesReferences.put(new Integer(pageNumber), currentPageOffset);
			pageLookup.put(currentPageOffset, pageNumber);

			pageData.checkSizeSet(pageNumber); // make sure we have min values
			// for page size

			/**if(structTreeRoot!=null){
				int structParents=Integer.parseInt((String)values.get("StructParents"));
				lookupStructParents.put(new Integer(pageNumber),new Integer(structParents));
			}*/

			pageNumber++;
		}
	}
	//<end-jfl>

	// <start-13>
	private static ArrayList getDirectoryMatches(String sDirectoryName)
	throws IOException {

		sDirectoryName = sDirectoryName.replaceAll("\\.", "/");
		URL u = Thread.currentThread().getContextClassLoader().getResource(
				sDirectoryName);
		ArrayList retValue = new ArrayList(0);
		String s = u.toString();

		System.out.println("scanning " + s);

		if (s.startsWith("jar:") && s.endsWith(sDirectoryName)) {
			int idx = s.lastIndexOf(sDirectoryName);
			s = s.substring(0, idx); // isolate entry name

			System.out.println("entry= " + s);

			URL url = new URL(s);
			// Get the jar file
			JarURLConnection conn = (JarURLConnection) url.openConnection();
			JarFile jar = conn.getJarFile();

			for (Enumeration e = jar.entries(); e.hasMoreElements();) {
				JarEntry entry = (JarEntry) e.nextElement();
				if ((!entry.isDirectory())
						& (entry.getName().startsWith(sDirectoryName))) { // this
					// is
					// how
					// you
					// can
					// match
					// to
					// find
					// your
					// fonts.
					// System.out.println("Found a match!");
					String fontName = entry.getName();
					int i = fontName.lastIndexOf("/");
					fontName = fontName.substring(i + 1);
					retValue.add(fontName);
				}
			}
		} else {
			// Does not start with "jar:"
			// Dont know - should not happen
			LogWriter.writeLog("Path: " + s);
		}
		return retValue;
	}

	/** read values from the classpath */
	private static ArrayList readIndirectValues(InputStream in)
	throws IOException {
		ArrayList fonts;
		BufferedReader inpStream = new BufferedReader(new InputStreamReader(in));
		fonts = new ArrayList(0);
		while (true) {
			String nextValue = inpStream.readLine();
			if (nextValue == null)
				break;

			fonts.add(nextValue);
		}

		inpStream.close();

		return fonts;
	}

	/**
	 * This routine allows the user to add truetype,
	 * type1 or type1C fonts which will be used to disalay the fonts in PDF
	 * rendering and substitution as if the fonts were embedded in the PDF <br>
	 * This is very useful for clients looking to keep down the size of PDFs
	 * transmitted and control display quality -
	 *
	 * Thanks to Peter for the idea/code -
	 *
	 * How to set it up -
	 *
	 * JPedal will look for the existence of the directory fontPath (ie
	 * com/myCompany/Fonts) -
	 *
	 * If this exists, Jpedal will look for 3 possible directories (tt,t1c,t1)
	 * and make a note of any fonts if these directories exist -
	 *
	 * When fonts are resolved, this option will be tested first and if a font
	 * if found, it will be used to display the font (the effect will be the
	 * same as if the font was embedded) -
	 *
	 * If the enforceMapping is true, JPedal assumes there must be a match and
	 * will throw a PdfFontException -
	 *
	 * Otherwise Jpedal will look in the java font path for a match or
	 * approximate with Lucida -
	 *
	 * The Format is defined as follows: -
	 *
	 * fontname = filename
	 *
	 * Type1/Type1C Font names exclude any prefix so /OEGPNB+FGHeavyItalic is
	 * resolved to FGHeavyItalic -
	 *
	 * Each font have the same name as the font it replaces (so Arial will
	 * require a font file such as Arial.ttf) and it must be unique (there
	 * cannot be an Arial font in each sub-directory) -
	 *
	 * So to use this functionality, place the fonts in a jar or add to the
	 * JPedal jar and call this method after instancing PdfDecoder - JPedal will
	 * do the rest
	 *
	 * @param fontPath -
	 *            root directory for fonts
	 * @param enforceMapping -
	 *            tell JPedal if all fonts should be in this directory
	 * @return flag (true if fonts added)
	 */
	public boolean addSubstituteFonts(String fontPath, boolean enforceMapping) {

		boolean hasFonts = false;

		try {
			String[] dirs = { "tt", "t1c", "t1" };
			String[] types = { "/TrueType", "/Type1C", "/Type1" };

			// check fontpath ends with separator - we may need to check this.
			// if((!fontPath.endsWith("/"))&(!fontPath.endsWith("\\")))
			// fontPath=fontPath=fontPath+separator;

			enforceFontSubstitution = enforceMapping;

			ClassLoader loader = this.getClass().getClassLoader();

			// see if root dir exists
			InputStream in = loader.getResourceAsStream(fontPath);

			LogWriter.writeLog("Looking for root " + fontPath);

			// if it does, look for sub-directories
			if (in != null) {

				LogWriter
				.writeLog("Adding fonts fonts found in  tt,t1c,t1 sub-directories of "
						+ fontPath);

				hasFonts = true;

				for (int i = 0; i < dirs.length; i++) {

					if (!fontPath.endsWith("/"))
						fontPath = fontPath + "/";

					String path = fontPath + dirs[i] + "/";

					// see if it exists
					in = loader.getResourceAsStream(path);

					// if it does read its contents and store
					if (in != null) {
						System.out.println("Found  " + path + " " + in);

						ArrayList fonts;

						try {

							// works with IDE or jar
							if (in instanceof ByteArrayInputStream)
								fonts = readIndirectValues(in);
							else
								fonts = getDirectoryMatches(path);

							String value, fontName;

							// now assign the fonts
							int count = fonts.size();
							for (int ii = 0; ii < count; ii++) {

								value = (String) fonts.get(ii);

								if (value == null)
									break;

								int pointer = value.indexOf(".");
								if (pointer == -1)
									fontName = value;
								else
									fontName = value.substring(0, pointer);

								fontSubstitutionTable.put(fontName
										.toLowerCase(), types[i]);
								fontSubstitutionLocation.put(fontName
										.toLowerCase(), path + value);
								//LogWriter.writeLog("Added from jar ="
								//		+ fontName + " path=" + path + value);

							}

						} catch (Exception e) {
							LogWriter.writeLog("Exception " + e
									+ " reading substitute fonts");
							System.out.println("Exception " + e
									+ " reading substitute fonts");
							// <start-demo>
							// <end-demo>
						}
					}

				}
			} else
				LogWriter.writeLog("No fonts found at " + fontPath);

		} catch (Exception e) {
			LogWriter.writeLog("Exception adding substitute fonts "
					+ e.getMessage());
		}

		return hasFonts;

	}

	// <end-13>

    //<start-adobe>
    /**
	 * return Map with user-defined annotation icons for display
	 */
	public Map getUserIconsForAnnotations() {
		return userAnnotIcons;
	}

	/**
	 * allow user to set own icons for annotation hotspots to display in
	 * renderer - pass user selection of hotspots as an array of format
	 * Image[number][page] where number is Annot number on page and page is
	 * current page -1 (ie 0 is page 1).
	 */
	public void addUserIconsForAnnotations(int page, String type, Image[] icons) {

		if (userAnnotIcons == null)
			userAnnotIcons = new Hashtable();

		userAnnotIcons.put((page) + "-" + type, icons);

		if (displayHotspots == null) {
			displayHotspots = new Hotspots();
			printHotspots = new Hotspots();
		}

		/** ensure type logged */
		displayHotspots.checkType(type);
		printHotspots.checkType(type);
	}

	/**
	 * initialise display hotspots and save global values
	 */
	public void createPageHostspots(String[] annotationTypes, String string) {
		displayHotspots = new Hotspots(annotationTypes, string);
		printHotspots = new Hotspots(annotationTypes, string);

	}


    //<end-adobe>

    /**
	 * show the imageable area in printout for debugging purposes
	 */
	public void showImageableArea() {

		showImageable = true;

	}

	/**
	 * part of pageable interface
	 *
	 * @see java.awt.print.Pageable#getNumberOfPages()
	 */
	public int getNumberOfPages() {

		//handle 1,2,5-7,12
        if(range!=null){
            int rangeCount=0;
            for(int ii=1;ii<this.pageCount+1;ii++){
                if(range.contains(ii) && (!oddPagesOnly || (ii & 1)==1) && (!evenPagesOnly || (ii & 1)==0))
                rangeCount++;
            }
            return rangeCount;
        }

        int count=1;


        if (end != -1){
			count= end - start + 1;
			if(count<0) //allow for reverse order
				count=2-count;
		}

		if(range!=null && (oddPagesOnly || evenPagesOnly)){
			return (count+1) / 2;
		}else{
			return count;
		}
	}

	/**
	 * part of pageable interface
	 *
	 * @see java.awt.print.Pageable#getPageFormat(int)
	 */
	public PageFormat getPageFormat(int p) throws IndexOutOfBoundsException {

		Object returnValue;

		int actualPage;

		if (end == -1)
			actualPage=p+1;
		else if(end>start)
			actualPage=start+p;
		else
			actualPage=start-p;

		returnValue = pageFormats.get(new Integer(actualPage));

		if (this.debugPrint)
			System.out.println(returnValue + " Get page format for page p=" + p
					+ " start=" + start + " pf=" + pageFormats + " "
					+ pageFormats.keySet());

		if (returnValue == null) {
			returnValue = pageFormats.get("standard");
			if (this.debugPrint)
				System.out.println(returnValue + " returned for standard");
		}

		PageFormat pf = new PageFormat();

		if (returnValue != null)
			pf = (PageFormat) returnValue;

		//usePDFPaperSize=true;
		if(usePDFPaperSize){

			int crw = pageData.getCropBoxWidth(actualPage);
			int crh = pageData.getCropBoxHeight(actualPage);

			Paper customPaper=new Paper();
			/** if(IsPrintAutoRotateAndCenter){
				 customPaper.setSize(crh,crw);
				 customPaper.setImageableArea(0,0,crh,crw);
			 }else{*/
			customPaper.setSize(crw,crh);
			customPaper.setImageableArea(0,0,crw,crh);
			//}
			pf.setPaper(customPaper);

		}

		if(!IsPrintAutoRotateAndCenter){

			pf.setOrientation(PageFormat.PORTRAIT);

		}else{
			//int crw = pageData.getCropBoxWidth(actualPage);
			//int crh = pageData.getCropBoxHeight(actualPage);

			//Set PageOrientation to best use page layout
			//int orientation = crw < crh ? PageFormat.PORTRAIT: PageFormat.LANDSCAPE;
			//pf.setOrientation(orientation);

		}

		return pf;
	}

	/**
	 * part of pageable interface
	 *
	 * @see java.awt.print.Pageable#getPrintable(int)
	 */
	public Printable getPrintable(int page) throws IndexOutOfBoundsException {

		return this;
	}

	/**
	 * set pageformat for a specific page - if no pageFormat is set a default
	 * will be used. Recommended to use setPageFormat(PageFormat pf)
	 */
	public void setPageFormat(int p, PageFormat pf) {

		if (this.debugPrint)
			System.out.println("Set page format for page " + p);

		pageFormats.put(new Integer(p), pf);

	}

	/**
	 * set pageformat for a specific page - if no pageFormat is set a default
	 * will be used.
	 */
	public void setPageFormat(PageFormat pf) {

		if (this.debugPrint)
			System.out.println("Set page format Standard for page");

		pageFormats.put("standard", pf);

	}

    /**
	 * shows if text extraction is XML or pure text
	 */
	public static boolean isXMLExtraction() {

		return isXMLExtraction;
	}

	/**
	 * XML extraction is the default - pure text extraction is much faster
	 */
	public static void useTextExtraction() {

		isXMLExtraction = false;
	}

	/**
	 * XML extraction is the default - pure text extraction is much faster
	 */
	public static void useXMLExtraction() {

		isXMLExtraction = true;
	}

    /**
	 * remove all displayed objects for JPanel display (wipes current page)
	 */
	public void clearScreen() {
		currentDisplay.flush();
		pages.refreshDisplay();
	}

	//<start-jfl>
	/**
	 * allows user to cache large objects to disk to avoid memory issues,
	 * setting minimum size in bytes (of uncompressed stream) above which object
	 * will be stored on disk if possible (default is -1 bytes which is all
	 * objects stored in memory) - Must be set before file opened.
	 * @noinspection SameParameterValue
	 */
	public void setStreamCacheSize(int size) {
		this.minimumCacheSize = size;
	}

	/**
	 * used to display non-PDF files
	 */
	public void addImage(BufferedImage img) {
		currentDisplay.drawImage(img);

	}

	/**
	 * shows if embedded fonts present on page just decoded
	 */
	public boolean hasEmbeddedFonts() {
		return hasEmbeddedFonts;
	}

	/** convert form ref into actual object */
	public Map resolveFormReference(String ref) {

		// text fields
		Map fields = new HashMap();

		// setup a list of fields which are string values
		fields.put("T", "x");
		fields.put("TM", "x");
		fields.put("TU", "x");
		fields.put("CA", "x");
		fields.put("R", "x");
		fields.put("V", "x");
		fields.put("RC", "x");
		fields.put("DA", "x");
		fields.put("DV", "x");

		return currentPdfFile.readObject(ref, false, fields);

	}

	/**
	 * shows if whole document contains embedded fonts and uses them
	 */
	final public boolean PDFContainsEmbeddedFonts() throws Exception {

		boolean hasEmbeddedFonts = false;

		/**
		 * scan all pages
		 */
		for (int page = 1; page < getPageCount() + 1; page++) {

			/** get pdf object id for page to decode */
			String currentPageOffset = (String) pagesReferences
			.get(new Integer(page));

			/**
			 * decode the file if not already decoded, there is a valid object
			 * id and it is unencrypted
			 */
			if ((currentPageOffset != null)) {

				/** read page or next pages */
				Map values = currentPdfFile.readObject(currentPageOffset,
						false, null);

				/** get information for the page */
				String value = (String) values.get("Contents");

				if (value != null) {

					PdfStreamDecoder current = new PdfStreamDecoder();

					current.setExternalImageRender(customImageHandler);
					Map resValue = currentPdfFile.getSubDictionary(values
							.get("Resources"));

					// if not present, look for it back up tree
					if (resValue == null) {
						String parent = (String) values.get("Parent");
						while ((parent != null) && (resValue == null)) {
							Map parentValues = currentPdfFile.readObject(
									parent, false, null);

							Object res = parentValues.get("Resources");
							if (res == null) {
								parent = (String) parentValues.get("Parent");
							} else if (res instanceof String)
								resValue = currentPdfFile.getSubDictionary(res);
							else
								resValue = (Map) res;
						}
					}

					current.init(true, renderPage, renderMode, extractionMode,
							pageData, page, currentDisplay, currentPdfFile,
							globalRes, resValue);

					hasEmbeddedFonts = current.hasEmbeddedFonts();

					// exit on first true
					if (hasEmbeddedFonts)
						page = this.getPageCount();
				}
			}
		}

		return hasEmbeddedFonts;
	}

	/**
	 * Returns list of the fonts used on the current page decoded
	 */
	public String getFontsInFile() {
		if (fontsInFile == null)
			return "No fonts defined";
		else
			return fontsInFile;
	}

	/**
	 * include image data in PdfData - <b>not part of API, please do not use</b>
	 */
	public void includeImagesInStream() {
		includeImages = true;
	}

    //<start-adobe>
    /**
	 * return lines on page after decodePage run - <b>not part of API, please do
	 * not use</b>
	 */
	public PageLines getPageLines() {
		return this.pageLines;
	}
    //<end-adobe>

    /**
	 *
	 * if <b>true</b> uses the original jpeg routines provided by sun, else
	 * uses the imageIO routine in java 14 which is default<br>
	 * only required for PDFs where bug in some versions of ImageIO fails to
	 * render JPEGs correctly
	 */
	public void setEnableLegacyJPEGConversion(boolean newjPEGConversion) {

		use13jPEGConversion = newjPEGConversion;
	}

	/** used to update statusBar object if exists */
	class ProgressListener implements ActionListener {

		public void actionPerformed(ActionEvent evt) {

			statusBar.setProgress((int) (statusBar.percentageDone));
		}

	}

    //<start-adobe>
    /**
	 * Allow user to access Forms object - returns null not available
	 */
	public AcroRenderer getCurrentFormRenderer() {
		if (!this.formsAvailable)
			return null;
		else
			return currentFormRenderer;
	}
    //<end-adobe>

	/**
	 * shows if page reported any errors while printing or being decoded. Log
	 * can be found with getPageFailureMessage()
	 *
	 * @return Returns the printingSuccessful.
	 */
	public boolean isPageSuccessful() {
		return operationSuccessful;
	}

	/**
	 * return any errors or other messages while calling decodePage() - zero
	 * length is no problems
	 */
	public String getPageDecodeReport() {
		return decodeStatus;
	}

	/**
	 * Return String with all error messages from last printed (useful for
	 * debugging)
	 */
	public String getPageFailureMessage() {
		return pageErrorMessages;
	}

	/**
	 * Extract a section of rendered page as
	 * BufferedImage -coordinates are PDF co-ordinates. If you wish to use hires
	 * image, you will need to enable hires image display with
	 * decode_pdf.useHiResScreenDisplay(true);
	 *
	 * @param t_x1
	 * @param t_y1
	 * @param t_x2
	 * @param t_y2
	 * @param scaling
	 * @return pageErrorMessages - Any printer errors
	 */
	public BufferedImage getSelectedRectangleOnscreen(float t_x1, float t_y1,
			float t_x2, float t_y2, float scaling) {

		/** get page sizes */
		//int mediaBoxW = pageData.getMediaBoxWidth(pageNumber);
		int mediaBoxH = pageData.getMediaBoxHeight(pageNumber);
		//int mediaBoxX = pageData.getMediaBoxX(pageNumber);
		//int mediaBoxY = pageData.getMediaBoxY(pageNumber);
		int crw = pageData.getCropBoxWidth(pageNumber);
		int crh = pageData.getCropBoxHeight(pageNumber);
		int crx = pageData.getCropBoxX(pageNumber);
		int cry = pageData.getCropBoxY(pageNumber);

		// check values for rotated pages
		if (t_y2 < cry)
			t_y2 = cry;
		if (t_x1 < crx)
			t_x1 = crx;
		if (t_y1 > (crh + cry))
			t_y1 = crh + cry;
		if (t_x2 > (crx + crw))
			t_x2 = crx + crw;

		if ((t_x2 - t_x1) < 1 || (t_y1 - t_y2) < 1)
			return null;

		float scalingFactor = scaling / 100;
		float imgWidth = t_x2 - t_x1;
		float imgHeight = t_y1 - t_y2;

		/**
		 * create the image
		 */
		BufferedImage img = new BufferedImage((int) (imgWidth * scalingFactor),
				(int) (imgHeight * scalingFactor), BufferedImage.TYPE_INT_RGB);

		Graphics2D g2 = img.createGraphics();

		/**
		 * workout the scaling
		 */

		if (cry > 0)// fix for negative pages
			cry = mediaBoxH - crh - cry;

		// use 0 for rotated extraction
		AffineTransform scaleAf = getScalingForImage(pageNumber,0, scalingFactor);// (int)(mediaBoxW*scale),
		// (int)(mediaBoxH*scale),
		int cx = -crx, cy = -cry;

		scaleAf.translate(cx, -cy);
		scaleAf.translate(-(t_x1 - crx), mediaBoxH - t_y1 - cry);

		AffineTransform af = g2.getTransform();

		g2.transform(scaleAf);

		if ((currentDisplay.addBackground())) {
			g2.setColor(currentDisplay.getBackgroundColor());
			g2.fill(new Rectangle(crx, cry, crw, crh));
		}

		currentDisplay.paint(g2, null, null, null,false);

        //<start-adobe>
        if (formsAvailable) {
			/**
			 * draw acroform data onto Panel
			 */
			if ((currentFormRenderer != null) && (currentAcroFormData != null)) {

				currentFormRenderer.renderFormsOntoG2(g2, pageNumber, scaling,0);

				//TODO figure out the indent field for resetLocations
				currentFormRenderer.resetScaledLocation(oldScaling,displayRotation,0);

			}

			/**
			 * draw acroform data onto Panel
			 */
			if ((showAnnotations) && (currentAnnotRenderer != null)
					&& (this.annotsData != null)) {

				currentAnnotRenderer.renderFormsOntoG2(g2, pageNumber, scaling,0);

			}
		}

		// set up page hotspots
		if ((annotsData != null) && (displayHotspots != null))
			displayHotspots.setHotspots(annotsData);

        //<end-adobe>

        g2.setTransform(af);


		g2.dispose();

		return img;
	}
	//<end-jfl>

	/**
	 * return object which provides access to file images and name
	 */
	public ObjectStore getObjectStore() {
		return objectStoreRef;
	}

	/**
	 * return object which provides access to file images and name (use not
	 * recommended)
	 */
	public void setObjectStore(ObjectStore newStore) {
		objectStoreRef = newStore;
	}

    //<start-adobe>
    //<start-jfl>
	/**
	 * returns object containing grouped text - Please see
	 * org.jpedal.examples.text for example code.
	 */
	public PdfGroupingAlgorithms getGroupingObject() throws PdfException {

		PdfData textData = getPdfData();
		if (textData == null)
			return null;
		else
			return new PdfGroupingAlgorithms(textData);
	}

	/**
	 * returns object containing grouped text from background grouping - Please
	 * see org.jpedal.examples.text for example code
	 */
	public PdfGroupingAlgorithms getBackgroundGroupingObject() {

		PdfData textData = this.getPdfBackgroundData();
		if (textData == null)
			return null;
		else
			return new PdfGroupingAlgorithms(textData);
	}


    //<end-adobe>

    /** get PDF version in file */
	final public String getPDFVersion() {
		return pdfVersion;
	}

	/**
	 * returns object, handling any indirect references
	 * @param string
	 * @param rawAnnotDetails
	 */
	public Map resolveToMapOrString(String string, Object rawAnnotDetails) {

		Map returnValue=null;
		if(rawAnnotDetails instanceof Map){
			returnValue=(Map)((Map)rawAnnotDetails).get(string);
		}

		if(returnValue==null)
			return (Map) currentPdfFile.resolveToMapOrString(string,rawAnnotDetails);
		else
			return returnValue;
	}
	//<end-jfl>

    //<start-adobe>
    /** used for non-PDF files to reset page */
	public void resetForNonPDFPage() {

		displayScaling=null;


		/** set hires mode or not for display */
		currentDisplay.setHiResImageForDisplayMode(false);

		fontsInFile = "";
		pageCount = 1;
		hasOutline = false;

		if (formsAvailable) {
			if (currentFormRenderer != null)
				currentFormRenderer.removeDisplayComponentsFromScreen(this);

			if ((currentAnnotRenderer != null) && (showAnnotations))
				currentAnnotRenderer.removeDisplayComponentsFromScreen(this);

			//invalidate();
		}

		// reset page data
		this.pageData = new PdfPageData();
	}
    //<end-adobe>

    /** provides details on printing to enable debugging info for IDRsolutions */
	public void setDebugPrint(boolean debugPrint) {
		this.debugPrint = debugPrint;
	}

    //<start-adobe>
    /**
	 * set view mode used in panel and redraw in new mode
	 * SINGLE_PAGE,CONTINUOUS,FACING,CONTINUOUS_FACING delay is the time in
	 * milli-seconds which scrolling can stop before background page drawing
	 * starts
	 * Multipage views not in OS releases
	 */
	public void setDisplayView(int displayView,int orientation) {

		this.alignment=orientation;

		if(pages!=null)
			pages.stopGeneratingPage();

        pages=new SingleDisplay(pageNumber,pageCount,currentDisplay);
        /**/

		boolean needsReset=(displayView!=Display.SINGLE_PAGE || this.displayView!=Display.SINGLE_PAGE);
		if(needsReset && (this.displayView==Display.FACING || displayView==Display.FACING)); 
		needsReset=false;

		if(displayView!=Display.SINGLE_PAGE)
			needsReset=true;

		boolean hasChanged=displayView!=this.displayView;

		this.displayView = displayView;

		/***/


		/**
		 * setup once per page getting all page sizes and working out settings
		 * for views
		 */
		if (currentOffset==null)
			currentOffset=new PageOffsets(pageCount,pageData);

		pages.setup(useAcceleration,currentOffset,this);
		pages.init(scaling,pageCount,displayRotation,pageNumber,currentDisplay,true, pageData,insetW,insetH);

		// force redraw
		lastFormPage=-1;
		lastEnd=-1;
		lastStart=-1;

		pages.refreshDisplay();
		updateUI();

	}
    //<end-adobe>

    //<start-jfl>
	/**
	 * show if page is an XFA form
	 */
	public boolean isXFAForm() {
		return isXFA;
	}

	/**
	 * show if page is an XFA form
	 */
	public boolean hasJavascript() {
		return javascript!=null && javascript.hasJavascript();
	}

	/**
	 * return page currently being printed or -1 if finished
	 */
	public int getCurrentPrintPage() {
		return currentPrintPage;
	}

	public void resetCurrentPrintPage() {
		currentPrintPage = 0;
	}

	/**flag to show if we suspect problem with some images*/
	public boolean hasAllImages() {
		return imagesProcessedFully;
	}

	/**set print mode (Matches Abodes Auto Print and rotate output*/
	public void setPrintAutoRotateAndCenter(boolean value) {
		IsPrintAutoRotateAndCenter=value;

	}

    //<start-adobe>
    /**allows external helper classes to be added to JPedal to alter default functionality -
	 * not part of the API and should be used in conjunction with IDRsolutions only
	 * <br>if Options.FormsActionHandler is the type then the <b>newHandler</b> should be
	 * of the form <b>org.jpedal.objects.acroforms.ActionHandler</b>
	 *
	 * @param newHandler
	 * @param type
	 */
	public void addExternalHandler(Object newHandler,int type){

		switch(type){
		case Options.ImageHandler:
			customImageHandler=(ImageHandler) newHandler;
			break;

        case Options.Renderer:
			//cast and assign here
			break;

        case Options.FormFactory:
			if (formsAvailable)
				currentFormRenderer.setFormFactory((FormFactory) newHandler);
			break;

        case Options.MultiPageUpdate:
			customSwingHandle=newHandler;
			break;

        case Options.FormsActionHandler:
			formsActionHandler=newHandler;

			if(currentFormRenderer!=null)
				currentFormRenderer.resetActionHandler(formsActionHandler);

			if(currentAnnotRenderer!=null)
				currentAnnotRenderer.resetActionHandler(formsActionHandler);

			break;
        
        default:
			throw new IllegalArgumentException("Unknown type");

		}
	}
    //<end-adobe>

    /**
	 * used internally by multiple pages
	 * scaling -1 to ignore, -2 to force reset
	 */
	public int getYCordForPage(int page,float scaling){

		if(scaling==-2 ||(scaling!=-1f && scaling!=oldScaling)){
			oldScaling=scaling;
			pages.setPageOffsets(this.pageCount,page);

			//System.out.println("xxxxxxx  RESET xxxxxxxxxxx "+scaling);
		}
		return pages.getYCordForPage(page);
	}

	public void unsetScaling() {

		displayScaling=null;


	}


    //<start-adobe>
    /**
	 * return full list of Fields for Annots and Forms*/
	public Set getNamesForAllFields() throws PdfException {

		//ensure all annots (which exist at a page level) are read
		//and decode forms in sync
		if(currentAnnotRenderer!=null ){
			for(int p=1;p<this.pageCount+1;p++){
				/** get pdf object id for page to decode */
				String currentPageOffset = (String) pagesReferences.get(new Integer(p));

				/**
				 * decode the file if not already decoded, there is a valid
				 * object id and it is unencrypted
				 */
				if ((currentPageOffset != null))
					readObjectForPage(currentPageOffset, p,false);

			}
		}

		Set set = new HashSet();

		if(currentFormRenderer!=null){
			List forms = currentFormRenderer.getComponentNameList();

			if(forms!=null)
				set.addAll(forms);
		}

		if(currentAnnotRenderer!=null ){


			List annots = currentAnnotRenderer.getComponentNameList();

			if(annots!=null)
				set.addAll(annots);
		}

		return set;

	}

    /**return swing widget regardless of whether it came from Annot or form*/
	public Component[] getSwingComponentForComponent(String name) {

		Component[] comps = null;

		if(currentFormRenderer!=null)
			comps=currentFormRenderer.getComponentsByName(name);

		if(currentAnnotRenderer!=null && comps==null)
			comps=currentAnnotRenderer.getComponentsByName(name);

		return comps;

	}


	public PdfObjectReader getIO() {
		return currentPdfFile;
	}



	public void setThumbnailsDrawing(boolean b) {
		thumbnailsBeingDrawn=b;
		pages.setThumbnailsDrawing(b);

	}

	public boolean isThumbnailsDrawing() {
		return thumbnailsBeingDrawn;
	}


	//<start-thin>
	public void setThumbnailPanel(GUIThumbnailPanel thumbnails) {
		pages.setThumbnailPanel(thumbnails);

	}
    //<end-thin>
    //<end-adobe>

    //<end-jfl>
}
