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
 * SimpleViewer.java
 * ---------------
 *
 * Original Author:  Mark Stephens (mark@idrsolutions.com)
 * Contributor(s):
 *
 */

package org.jpedal.examples.simpleviewer;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.jpedal.PdfDecoder;
import org.jpedal.Display;
import org.jpedal.io.JAIHelper;

import org.jpedal.examples.simpleviewer.gui.SwingGUI;
import org.jpedal.examples.simpleviewer.gui.generic.GUIMouseHandler;
import org.jpedal.examples.simpleviewer.gui.generic.GUISearchWindow;
import org.jpedal.examples.simpleviewer.gui.generic.GUIThumbnailPanel;

import org.jpedal.examples.simpleviewer.gui.swing.SwingSearchWindow;
import org.jpedal.examples.simpleviewer.gui.swing.SwingMouseHandler;
import org.jpedal.examples.simpleviewer.gui.swing.SwingThumbnailPanel;
import org.jpedal.examples.simpleviewer.utils.Printer;
import org.jpedal.examples.simpleviewer.utils.PropertiesFile;

import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfFontException;
import org.jpedal.external.Options;
import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;
import org.w3c.dom.Node;

/**
 * Scope:<b>(All)</b>
 * <br>Description: Demo to show JPedal being used as a GUI viewer,
 * and to demonstrate some of JPedal's capabilities
 *
 *
 * <br>This class provides the framework for the Viewer and calls other classes which provide the following
 * functions:-
 *
 * <br>Values commonValues - repository for general settings
 * Printer currentPrinter - All printing functions and access methods to see if printing active
 * PdfDecoder decode_pdf - PDF library and panel
 * ThumbnailPanel thumbnails - provides a thumbnail pane down the left side of page - thumbnails can be clicked on to goto page
 * PropertiesFile properties - saved values stored between sessions
 * SwingGUI currentGUI - all Swing GUI functions
 * SearchWindow searchFrame (not GPL) - search Window to search pages and goto references on any page
 * Commands currentCommands - parses and executes all options
 * SwingMouseHandler mouseHandler - handles all mouse and related activity
 */
public class SimpleViewer {

	/**repository for general settings*/
	protected Values commonValues=new Values();

	/**All printing functions and access methods to see if printing active*/
	protected Printer currentPrinter=new Printer();

	/**PDF library and panel*/
	final protected PdfDecoder decode_pdf = new PdfDecoder(true);

	/**encapsulates all thumbnail functionality - just ignore if not required*/
	protected GUIThumbnailPanel thumbnails=new SwingThumbnailPanel(commonValues,decode_pdf);

	/**values saved on file between sessions*/
	private PropertiesFile properties=new PropertiesFile();

	/**general GUI functions*/
	protected SwingGUI currentGUI=new SwingGUI(decode_pdf,commonValues,thumbnails,properties);

	/**search window and functionality*/
	private GUISearchWindow searchFrame=new SwingSearchWindow(commonValues,currentGUI,decode_pdf);

	/**command functions*/
	protected Commands currentCommands=new Commands(commonValues,currentGUI,decode_pdf,
			thumbnails,properties,searchFrame,currentPrinter);

	/**all mouse actions*/
	protected GUIMouseHandler mouseHandler=new SwingMouseHandler(decode_pdf,currentGUI,commonValues,currentCommands);

	/**scaling values which appear onscreen*/
	protected String[] scalingValues;


	/**
	 * setup and run client, loading defaultFile on startup
	 */
	public void setupViewer(String defaultFile) {

		setupViewer();

		openDefaultFile(defaultFile);

	}

	/**
	 * open the file passed in by user on startup (do not call directly)
	 */
	private void openDefaultFile(String defaultFile) {

		//get any user set dpi
		String hiresFlag=System.getProperty("org.jpedal.hires");
		if(hiresFlag!=null)
			commonValues.setUseHiresImage(true);

		//get any user set dpi
		String memFlag=System.getProperty("org.jpedal.memory");
		if(memFlag!=null)
			commonValues.setUseHiresImage(false);

		//reset flag
		if(thumbnails.isShownOnscreen())
		thumbnails.resetToDefault();

		commonValues.maxViewY=0;// ensure reset for any viewport

		/**
		 * open any default file and selected page
		 */
		if(defaultFile!=null){

			File testExists=new File(defaultFile);
			boolean isURL=false;
			if(defaultFile.startsWith("http:")){
				LogWriter.writeLog("Opening http connection");
				isURL=true;
			}

			if((!isURL) && (!testExists.exists())){
				currentGUI.showMessageDialog(defaultFile+"\n"+Messages.getMessage("PdfViewerdoesNotExist.message"));
			}else if((!isURL) &&(testExists.isDirectory())){
				currentGUI.showMessageDialog(defaultFile+"\n"+Messages.getMessage("PdfViewerFileIsDirectory.message"));
			}else{

				commonValues.setSelectedFile(defaultFile);
				commonValues.setFileSize(testExists.length() >> 10);

				currentGUI.setViewerTitle(null);

				/**see if user set Page*/
				String page=System.getProperty("org.jpedal.page");
				String bookmark=System.getProperty("org.jpedal.bookmark");
				if(page!=null){

					try{
						int pageNum=Integer.parseInt(page);

						if(pageNum<1){
							pageNum=-1;
							System.err.println(page+ " must be 1 or larger. Opening on page 1");
							LogWriter.writeLog(page+ " must be 1 or larger. Opening on page 1");
						}

						if(pageNum!=-1)
							openFile(testExists,pageNum);


					}catch(Exception e){
						System.err.println(page+ "is not a valid number for a page number. Opening on page 1");
						LogWriter.writeLog(page+ "is not a valid number for a page number. Opening on page 1");
					}
				}else if(bookmark!=null){
					openFile(testExists,bookmark);
				}else
					currentCommands.openFile(defaultFile);

			}
		}
	}

	/**
	 * setup and run client
	 */
	public SimpleViewer() {
		//enable error messages which are OFF by default
		PdfDecoder.showErrorMessages=true;
	}

	/**
	 * setup and run client passing in paramter to show if
	 * running as applet, webstart or JSP (only applet has any effect
	 * at present)
	 */
	public SimpleViewer(int modeOfOperation) {

		//enable error messages which are OFF by default
		PdfDecoder.showErrorMessages=true;

		commonValues.setModeOfOperation(modeOfOperation);

	}

	/**
	 * initialise and run client (default as Application in own Frame)
	 */
	public void setupViewer() {


        /**switch on thumbnails if flag set*/
		String setThumbnail=System.getProperty("org.jpedal.thumbnail");
		if(setThumbnail!=null){
			if(setThumbnail.equals("true"))
				thumbnails.setThumbnailsEnabled(true);
			else if(setThumbnail.equals("true"))
				thumbnails.setThumbnailsEnabled(false);
		}else //default
			thumbnails.setThumbnailsEnabled(true);

		/**non-GUI initialisation*/
		init(null);

		/**
		 * gui setup
		 */
		currentGUI.init(scalingValues,currentCommands,currentPrinter);

		setupButtonsAndMenus();

		mouseHandler.setupMouse();

		/**
		 * setup window for warning if renderer has problem
		 */
		decode_pdf.setMessageFrame(currentGUI.getFrame());

		boolean showFirstTimePopup = properties.getValue("showfirsttimepopup").equals("true");
		if(showFirstTimePopup){
			currentGUI.showFirstTimePopup();
			properties.setValue("showfirsttimepopup","false");
		}

        if(JAIHelper.isJAIused()){
            if(properties != null && properties.getValue("showddmessage").equals("true")){

                JOptionPane.showMessageDialog(decode_pdf, Messages.getMessage("PdfViewer.JAIWarning") +
                        Messages.getMessage("PdfViewer.JAIWarning1") +
                        Messages.getMessage("PdfViewer.JAIWarning2") +
                        Messages.getMessage("PdfViewer.JAIWarning3") +
                        Messages.getMessage("PdfViewer.JAIWarning4"));

                properties.setValue("showddmessage","false");
            }
        }

        /**
		 * check for itext and tell user about benefits
		 */
		if(!commonValues.isContentExtractor()){
			boolean showItextMessage = properties.getValue("showitextmessage").equals("true");
			
			if (!commonValues.isItextOnClasspath() && showItextMessage) {

				currentGUI.showItextPopup();

				properties.setValue("showitextmessage","false");
			}
		}
	}

	/**
	 * setup the viewer
	 */
	protected void init(ResourceBundle bundle) {

        /**
		 * allow user to define country and language settings
		 *
		Locale aLocale = new Locale("en", "EN");

		Locale.setDefault(aLocale);


		/**
		 * load correct set of messages
		 */
		if(bundle==null){
			try{
				Messages.setBundle(ResourceBundle.getBundle("org.jpedal.international.messages"));
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Exception loading resource bundle");
			}
		}else
			Messages.setBundle(bundle);

		/**setup scaling values which ar displayed for user to choose*/
        this.scalingValues= new String[]{Messages.getMessage("PdfViewerScaleWindow.text"),Messages.getMessage("PdfViewerScaleHeight.text"),
                Messages.getMessage("PdfViewerScaleWidth.text"),
                "25","50","75","100","125","150","200","250","500","750","1000"};

        /**
         * setup display
         */
        if(commonValues.isContentExtractor())
            decode_pdf.setDisplayView(Display.SINGLE_PAGE,Display.DISPLAY_LEFT_ALIGNED);
        else
            decode_pdf.setDisplayView(Display.SINGLE_PAGE,Display.DISPLAY_CENTERED);

        //pass through GUI for use in multipages
        decode_pdf.addExternalHandler(currentGUI, Options.MultiPageUpdate);

        /**debugging code to create a log*
		LogWriter.setupLogFile(true,1,"","v",false);
		//LogWriter.log_name =  "/mnt/shared/log.txt";
		/**/

		//make sure widths in data CRITICAL if we want to split lines correctly!!
		decode_pdf.init(true);

		/**
		 * ANNOTATIONS code 1
		 *
		 * use for annotations, loading icons and enabling display of annotations
		 * this enables general annotations with an icon for each type.
		 * See below for more specific function.
		 *
		 *
		 * which can be enabled with this code before file opened
		 *
		 */
		decode_pdf.createPageHostspots(currentGUI.getAnnotTypes(),"org/jpedal/examples/simpleviewer/annots/");
		
		/**
		 * ANNOTATIONS code 2
		 *
		 * replace Annotations with your own custom annotations
		 *
		 */
		//decode_pdf.setAnnotationsVisible(false); //disable built-in annotations and use custom versions
		//code to create a unique iconset - see method createUniqueAnnotationIcons() in  GUI class
		
		
		
		//this allows the user to place fonts in the classpath and use these for display, as if embedded
		//decode_pdf.addSubstituteFonts("org/jpedal/res/fonts/", true);

		//set to extract all
		//COMMENT OUT THIS LINE IF USING JUST THE VIEWER
		decode_pdf.setExtractionMode(0,72,1); //values extraction mode,dpi of images, dpi of page as a factor of 72

		//don't extract text and images (we just want the display)

		/**/
		/**
		 * FONT EXAMPLE CODE showing JPedal's functionality to set values for
		 * non-embedded fonts.
		 *
		 * This allows sophisticated substitution of non-embedded fonts.
		 *
		 * Most font mapping is done as the fonts are read, so these calls must
		 * be made BEFORE the openFile() call.
		 */

		/**
		 * FONT EXAMPLE - Replace global default for non-embedded fonts.
		 *
		 * You can replace Lucida as the standard font used for all non-embedded and substituted fonts
		 * by using is code.
		 * Java fonts are case sensitive, but JPedal resolves currentGUI.frame, so you could
		 * use Webdings, webdings or webDings for Java font Webdings
		 */
		try{
			//choice of example font to stand-out (useful in checking results to ensure no font missed.
			//In general use Helvetica or similar is recommended
			decode_pdf.setDefaultDisplayFont("SansSerif");
		}catch(PdfFontException e){ //if its not available catch error and show valid list

			System.out.println(e.getMessage());

			//get list of fonts you can use
			String[] fontList =GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			System.out.println(Messages.getMessage("PdfViewerFontsFound.message"));
			System.out.println("=====================\n");
			int count = fontList.length;
			for (int i = 0; i < count; i++) {
				Font f=new Font(fontList[i],1,10);
				System.out.println(fontList[i]+" ("+Messages.getMessage("PdfViewerFontsPostscript.message")+"="+f.getPSName()+")");

			}
			System.exit(1);

		}/***/

		/**
		 * IMPORTANT note on fonts for EXAMPLES
		 *
		 * USEFUL TIP : The SimpleViewer displays a list of fonts used on the
		 * current PDF page with the File > Fonts menu option.
		 *
		 * PDF allows the use of weights for fonts so Arial,Bold is a weight of
		 * Arial. This value is not case sensitive so JPedal would regard
		 * arial,bold and aRiaL,BoLd as the same.
		 *
		 * Java supports a set of Font families internally (which may have
		 * weights), while JPedals substitution facility uses physical True Type
		 * fonts so it is resolving each font weight separately. So mapping
		 * works differently, depending on which is being used.
		 *
		 * If you are using a font, which is named as arial,bold you can use
		 * either arial,bold or arial (and JPedal will then try to select the
		 * bold weight if a Java font is used).
		 *
		 * So for a font such as Arial,Bold JPedal will test for an external
		 * truetype font substitution (ie arialMT.ttf) mapped to Arial,Bold. BUT
		 * if the substitute font is a Java font an additional test will be made
		 * for a match against Arial if there is no match on Arial,Bold.
		 *
		 * If you want to map all Arial to equivalents to a Java font such as
		 * Times New Roman, just map Arial to Times New Roman (only works for
		 * inbuilt java fonts). Note if you map Arial,Bold to a Java font such
		 * as Times New Roman, you will get Times New Roman in a bold weight, if
		 * available. You cannot set a weight for the Java font.
		 *
		 * If you wish to substitute Arial but not Arial,Bold you should
		 * explicitly map Arial,Bold to Arial,Bold as well.
		 *
		 * The reason for the difference is that when using Javas inbuilt fonts
		 * JPedal can resolve the Font Family and will try to work out the
		 * weight internally. When substituting Truetype fonts, these only
		 * contain ONE weight so JPedal is resolving the Font and any weight as
		 * a separate font . Different weights will require separate files.
		 *
		 * Open Source version does not support all font capabilities.
		 */

		/**
		 * FONT EXAMPLE - Use fonts placed in jar for substitution (1.4 and above only)
		 *
		 * This allows users to store fonts in the jar and use these for
		 * substitution. Please see javadoc for full description of usage.
		 */
		//decode_pdf.addSubstituteFonts(fontPath,enforceMapping)

		/**
		 * FONT EXAMPLE - Use fonts located on machine for substitution
		 *
		 * This code explains how to use JPedal to substitute fonts which are
		 * not embedded using fonts held in any font directory.
		 *
		 * It works as follows:-
		 *
		 * If the -Dorg.jpedal.fontdirs="C:/win/fonts/","/mnt/X11/fonts" is set to a
		 * comma-separated list of directories, any truetype fonts (with .ttf
		 * file ending) will be logged and added to the substitution table. So
		 * arialMT.ttf will be added as arialmt. If arialmt is used in the PDF
		 * but not embedded, JPedal will use this font file to render it.
		 *
		 * If a command line paramter is not appropriate, the call
		 * setFontDirs(String[] fontDirs) will achieve the same.
		 *
		 *
		 * If the name is not an exact match (ie you have arialMT which you wish
		 * to use to display arial, you can use the method
		 * setSubstitutedFontAliases(String[] name, String[] aliases) to convert
		 * it internally - see sample code at bottom of note.
		 *
		 * The Name is not case-sensitive.
		 *
		 * Spaces are important so TimesNewRoman and Times New Roman are
		 * degarded as 2 fonts.
		 *
		 * If you have 2 copies of arialMT.ttf in the scanned directories, the
		 * last one will be used.
		 *
		 * If the file was called arialMT,bold.ttf it is resolved as
		 * ArialMT,bold only.
		 *
		 */
		String[] aliases1={"helvetica","arial"};
		decode_pdf.setSubstitutedFontAliases("arial",aliases1);
		
		String[] aliases2={"Helvetica-Bold"};
		decode_pdf.setSubstitutedFontAliases("Arial-BoldMT",aliases1);
		
		decode_pdf.setFontDirs(new String[]{"C:/windows/fonts/","C:/winNT/fonts/"});
		/**
		 * FONT EXAMPLE - Use Standard Java fonts for substitution
		 *
		 * This code tells JPedal to substitute fonts which are not embedded.
		 *
		 * The Name is not case-sensitive.
		 *
		 * Spaces are important so TimesNewRoman and Times New Roman are
		 * degarded as 2 fonts.
		 *
		 * If you have 2 copies of arialMT.ttf in the scanned directories, the
		 * last one will be used.
		 *
		 *
		 * If you wish to use one of Javas fonts for display (for example, Times
		 * New Roman is a close match for myCompanyFont in the PDF, you can the
		 * code below
		 *
		 * String[] aliases={"Times New Roman"};//,"helvetica","arial"};
		 * decode_pdf.setSubstitutedFontAliases("myCompanyFont",aliases);
		 *
		 * Here is is used to map Javas Times New Roman (and all weights) to
		 * TimesNewRoman.
		 *
		 * This can also be done with the command -org.jpedal.fontmaps="TimesNewRoman=Times New Roman","font2=pdfFont1"
		 */
		//String[] nameInPDF={"TimesNewRoman"};//,"helvetica","arial"};
		//decode_pdf.setSubstitutedFontAliases("Times New Roman",nameInPDF);

        /**
         * add in external handlers for code
         *

        ImageHandler myExampleImageHandler=new ExampleImageHandler();
        try {
            decode_pdf.addExternalHandler(myExampleImageHandler, Options.ImageHandler);
        } catch (PdfException e) {
            e.printStackTrace();
        }
         /**/

    }

	/**
	 * sets up all the toolbar items
	 */
	private void setupButtonsAndMenus() {

		createSwingMenu(true);

		/**
		 * combo boxes on toolbar
		 * */
		currentGUI.addCombo(Messages.getMessage("PdfViewerToolbarScaling.text"), Messages.getMessage("PdfViewerToolbarTooltip.zoomin"), Commands.SCALING);

		currentGUI.addCombo(Messages.getMessage("PdfViewerToolbarRotation.text"), Messages.getMessage("PdfViewerToolbarTooltip.rotation"), Commands.ROTATION);


		createButtons();

		currentGUI.addCursor();

		/**status object on toolbar showing 0 -100 % completion */
		currentGUI.initStatus();


	}

	/**
	 * setup up the buttons
	 * (add your own here if required)
	 */
	private void createButtons() {

		currentGUI.addButton(GUIFactory.BUTTONBAR,Messages.getMessage("PdfViewerToolbarTooltip.openFile"),"/org/jpedal/examples/simpleviewer/res/open.gif",Commands.OPENFILE);

		currentGUI.addButton(GUIFactory.BUTTONBAR,Messages.getMessage("PdfViewerToolbarTooltip.print"),"/org/jpedal/examples/simpleviewer/res/print.gif",Commands.PRINT);

		currentGUI.addButton(GUIFactory.BUTTONBAR,Messages.getMessage("PdfViewerToolbarTooltip.search"),"/org/jpedal/examples/simpleviewer/res/find.gif",Commands.FIND);
		
		currentGUI.addButton(GUIFactory.BUTTONBAR,Messages.getMessage("PdfViewerToolbarTooltip.properties"),"/org/jpedal/examples/simpleviewer/res/properties.gif",Commands.DOCINFO);

		currentGUI.addButton(GUIFactory.BUTTONBAR,Messages.getMessage("PdfViewerToolbarTooltip.about"),"/org/jpedal/examples/simpleviewer/res/about.gif",Commands.INFO);

		/**snapshot screen function*/
		currentGUI.addButton(GUIFactory.BUTTONBAR,Messages.getMessage("PdfViewerToolbarTooltip.snapshot"),"/org/jpedal/examples/simpleviewer/res/snapshotX.gif",Commands.SNAPSHOT);

		/**
		 * external/itext button option example adding new option to Export menu
		 * an icon is set wtih location on classpath
		 * "/org/jpedal/examples/simpleviewer/res/newfunction.gif"
		 * Make sure it exists at location and is copied into jar if recompiled
		 */
		//currentGUI.addButton(currentGUI.BUTTONBAR,tooltip,"/org/jpedal/examples/simpleviewer/res/newfunction.gif",Commands.NEWFUNCTION);

		/**
		 * external/itext menu option example adding new option to Export menu
		 * Tooltip text can be externalised in Messages.getMessage("PdfViewerTooltip.NEWFUNCTION")
		 * and text added into files in res package
		 */


	}

	/**
	 * create items on drop down menus
	 */
	protected void createSwingMenu(boolean includeAll) {

		JMenu fileMenuList = new JMenu(Messages.getMessage("PdfViewerFileMenu.text"));
		currentGUI.addToMainMenu(fileMenuList);

		JMenu view = new JMenu(Messages.getMessage("PdfViewerViewMenu.text"));
		currentGUI.addToMainMenu(view);

		JMenu goTo = new JMenu(Messages.getMessage("GoToViewMenuGoto.text"));
		view.add(goTo);

		currentGUI.addMenuItem(goTo,Messages.getMessage("GoToViewMenuGoto.FirstPage"),"",Commands.FIRSTPAGE);
		currentGUI.addMenuItem(goTo,Messages.getMessage("GoToViewMenuGoto.BackPage"),"",Commands.BACKPAGE);
		currentGUI.addMenuItem(goTo,Messages.getMessage("GoToViewMenuGoto.ForwardPage"),"",Commands.FORWARDPAGE);
		currentGUI.addMenuItem(goTo,Messages.getMessage("GoToViewMenuGoto.LastPage"),"",Commands.LASTPAGE);
		currentGUI.addMenuItem(goTo,Messages.getMessage("GoToViewMenuGoto.GoTo"),"",Commands.GOTO);

		goTo.addSeparator();

		if(includeAll){

			currentGUI.addMenuItem(goTo,Messages.getMessage("GoToViewMenuGoto.PreviousDoucment"),"",Commands.PREVIOUSDOCUMENT);
			currentGUI.addMenuItem(goTo,Messages.getMessage("GoToViewMenuGoto.NextDoucment"),"",Commands.NEXTDOCUMENT);

			/**
			 * add export menus
			 **/
			JMenu export = new JMenu(Messages.getMessage("PdfViewerExportMenu.text"));
			currentGUI.addToMainMenu(export);


			if(commonValues.isItextOnClasspath()){
				JMenu pdf = new JMenu(Messages.getMessage("PdfViewerExportMenuPDF.text"));
				export.add(pdf);

				currentGUI.addMenuItem(pdf,Messages.getMessage("PdfViewerExportMenuOnePerPage.text"),"",Commands.PDF);
				currentGUI.addMenuItem(pdf,Messages.getMessage("PdfViewerExportMenuNUp.text"),"",Commands.NUP);

				//<start-13>
				JMenu pageTools = new JMenu(Messages.getMessage("PdfViewerPageToolsMenu.text"));
				currentGUI.addToMainMenu(pageTools);

				currentGUI.addMenuItem(pageTools,Messages.getMessage("PdfViewerPageToolsMenuRotate.text"),"",Commands.ROTATE);
				currentGUI.addMenuItem(pageTools,Messages.getMessage("PdfViewerPageToolsMenuDelete.text"),"",Commands.DELETE);
				currentGUI.addMenuItem(pageTools,Messages.getMessage("PdfViewerPageToolsMenuAddPage.text"),"",Commands.ADD);
				currentGUI.addMenuItem(pageTools,Messages.getMessage("PdfViewerPageToolsMenuAddHeaderFooter.text"),"",Commands.ADDHEADERFOOTER);
				currentGUI.addMenuItem(pageTools,Messages.getMessage("PdfViewerPageToolsMenuStampText.text"),"",Commands.STAMPTEXT);
				currentGUI.addMenuItem(pageTools,Messages.getMessage("PdfViewerPageToolsMenuStampImage.text"),"",Commands.STAMPIMAGE);
				currentGUI.addMenuItem(pageTools,Messages.getMessage("PdfViewerPageToolsMenuSetCrop.text"),"",Commands.SETCROP);
				//<end-13>
			}


			/**
			 * external/itext menu option example adding new option to Export menu
			 */
			//currentGUI.addMenuItem(export,"NEW",tooltip,Commands.NEWFUNCTION);
			/**
			 * external/itext menu option example adding new option to Export menu
			 * Tooltip text can be externalised in Messages.getMessage("PdfViewerTooltip.NEWFUNCTION")
			 * and text added into files in res package
			 */

			currentGUI.addMenuItem(export,"Bitmap",Messages.getMessage("PdfViewerExportMenuBitmap.text"),Commands.BITMAP);

			JMenu content=new JMenu(Messages.getMessage("PdfViewerExportMenuContent.text"));
			export.add(content);

			currentGUI.addMenuItem(content,Messages.getMessage("PdfViewerExportMenuImages.text"),"",Commands.IMAGES);

			currentGUI.addMenuItem(content,Messages.getMessage("PdfViewerExportMenuText.text"),"",Commands.TEXT);


		}

		currentGUI.addMenuItem(view,Messages.getMessage("PdfViewerViewMenuAutoscroll.text"),Messages.getMessage("PdfViewerViewMenuTooltip.autoscroll"),Commands.AUTOSCROLL);


		//full page mode
		view.addSeparator();
		currentGUI.addMenuItem(view,Messages.getMessage("PdfViewerViewMenuFullScreenMode.text"),Messages.getMessage("PdfViewerViewMenuTooltip.fullScreenMode"),Commands.FULLSCREEN);


		/**
		 * add open options
		 **/
        if(includeAll){
            JMenu Open = new JMenu(Messages.getMessage("PdfViewerFileMenuOpen.text"));
            fileMenuList.add(Open);

            currentGUI.addMenuItem(Open,Messages.getMessage("PdfViewerFileMenuOpen.text"),Messages.getMessage("PdfViewerFileMenuTooltip.open"),Commands.OPENFILE);
            currentGUI.addMenuItem(Open,Messages.getMessage("PdfViewerFileMenuOpenurl.text"),Messages.getMessage("PdfViewerFileMenuTooltip.openurl"),Commands.OPENURL);

            fileMenuList.addSeparator();


		}

		if(includeAll)
			currentGUI.addMenuItem(fileMenuList,Messages.getMessage("PdfViewerFileMenuSave.text"),
					Messages.getMessage("PdfViewerFileMenuTooltip.save"),Commands.SAVE);


        if((includeAll)&&(commonValues.isItextOnClasspath()))
			currentGUI.addMenuItem(fileMenuList,
					Messages.getMessage("PdfViewerFileMenuResaveForms.text"),
					Messages.getMessage("PdfViewerFileMenuTooltip.saveForms"),
					Commands.SAVEFORM);

		fileMenuList.addSeparator();

		currentGUI.addMenuItem(fileMenuList,Messages.getMessage("PdfViewerFileMenuDocProperties.text"),
				Messages.getMessage("PdfViewerFileMenuTooltip.props"),Commands.DOCINFO);

		fileMenuList.addSeparator();

		if(includeAll){
			currentGUI.addMenuItem(fileMenuList,Messages.getMessage("PdfViewerFileMenuPrint.text"),
					Messages.getMessage("PdfViewerFileMenuTooltip.print"),Commands.PRINT);

			fileMenuList.addSeparator();
		}

		if(includeAll)
			currentCommands.recentDocumentsOption(fileMenuList);

		fileMenuList.addSeparator();

		currentGUI.addMenuItem(fileMenuList,Messages.getMessage("PdfViewerFileMenuExit.text"),
				Messages.getMessage("PdfViewerFileMenuTooltip.exit"),Commands.EXIT);

		if(includeAll){
			JMenu help = new JMenu(Messages.getMessage("PdfViewerHelpMenu.text"));

			currentGUI.addToMainMenu(help);
			currentGUI.addMenuItem(help,Messages.getMessage("PdfViewerHelpMenu.VisitWebsite"),"",Commands.VISITWEBSITE);
			currentGUI.addMenuItem(help,Messages.getMessage("PdfViewerHelpMenuabout.text"),Messages.getMessage("PdfViewerHelpMenuTooltip.about"),Commands.INFO);
		}	
	}
	
	/** main method to run the software as standalone application */
	public static void main(String[] args) {

        /**
		 * set the look and feel for the GUI components to be the
		 * default for the system it is running on
		 */
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		}catch (Exception e) { 
			LogWriter.writeLog("Exception " + e + " setting look and feel");
		}
		
		SimpleViewer current = new SimpleViewer();
		
		if (args.length > 0)
			current.setupViewer(args[0]);
		else 
			current.setupViewer();
		
	}
	
	/**
	 * General code to open file at specified boomark - do not call directly
	 *  
	 * @param file File the PDF to be decoded
	 * @param bookmark - if not present, exception will be thrown
	 */
	private void openFile(File file, String bookmark) {
		
		try{
			
			boolean fileCanBeOpened=currentCommands.openUpFile(file.getCanonicalPath());

            //reads tree and populates lookup table
			Node rootNode= decode_pdf.getOutlineAsXML().getFirstChild();
			Object bookmarkPage=null;
			if(rootNode!=null)
				bookmarkPage=currentGUI.getBookmark(bookmark);
			
			if(bookmarkPage==null)
				throw new PdfException("Unknown bookmark "+bookmark);
			
			int page=Integer.parseInt((String)bookmarkPage);
			commonValues.setCurrentPage(page);
			if(fileCanBeOpened)
				currentCommands.processPage();
			
		}catch(Exception e){
			System.err.println("Exception " + e + " processing file");
			
			
			commonValues.setProcessing(false);
		}
	}
	
	/**
	 * General code to open file at specified page - do not call directly
	 *  
	 * @param file File the PDF to be decoded
	 * @param page int page number to show the user
	 */
	private void openFile(File file, int page) {

        try{
			boolean fileCanBeOpened=currentCommands.openUpFile(file.getCanonicalPath());

            commonValues.setCurrentPage(page);
			
			if(fileCanBeOpened)
				currentCommands.processPage();
		}catch(Exception e){
			System.err.println("Exception " + e + " processing file"); 
			
			
			commonValues.setProcessing(false);
		}
	}	
}
