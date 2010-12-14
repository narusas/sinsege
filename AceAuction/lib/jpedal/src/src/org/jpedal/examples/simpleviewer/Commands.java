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
 * Commands.java
 * ---------------
 *
 * Original Author:  Mark Stephens (mark@idrsolutions.com)
 * Contributor(s):
 *
 */
package org.jpedal.examples.simpleviewer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.imageio.ImageIO;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

import org.jpedal.PdfDecoder;
import org.jpedal.Display;
import org.jpedal.examples.simpleviewer.gui.SwingGUI;
import org.jpedal.examples.simpleviewer.gui.generic.GUISearchWindow;
import org.jpedal.examples.simpleviewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.examples.simpleviewer.gui.popups.*;

import org.jpedal.examples.simpleviewer.utils.ItextFunctions;

import org.jpedal.examples.simpleviewer.gui.popups.ErrorDialog;
import org.jpedal.examples.simpleviewer.gui.popups.SaveText;
import org.jpedal.examples.simpleviewer.utils.Exporter;
import org.jpedal.examples.simpleviewer.utils.FileFilterer;
import org.jpedal.examples.simpleviewer.utils.IconiseImage;

import org.jpedal.examples.simpleviewer.utils.Printer;
import org.jpedal.examples.simpleviewer.utils.PropertiesFile;
import org.jpedal.exception.PdfException;
import org.jpedal.grouping.PdfGroupingAlgorithms;

import org.jpedal.io.JAIHelper;
import org.jpedal.objects.PdfFileInformation;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.BrowserLauncher;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;
import org.jpedal.utils.Strip;
import org.jpedal.utils.SwingWorker;

/**code to execute the actual commands*/
public class Commands {

	public static final int INFO = 1;
	public static final int BITMAP = 2;
	public static final int IMAGES = 3;
	public static final int TEXT = 4;
	public static final int SAVE = 5;
	public static final int PRINT = 6;
	public static final int EXIT = 7;
	public static final int AUTOSCROLL = 8;
	public static final int DOCINFO = 9;
	public static final int OPENFILE = 10;
	//public static final int BOOKMARK = 11;
	public static final int FIND = 12;
	public static final int SNAPSHOT = 13;
	public static final int OPENURL = 14;
	public static final int VISITWEBSITE = 15;
	public static final int PREVIOUSDOCUMENT = 16;
	public static final int NEXTDOCUMENT = 17;

	public static final int FIRSTPAGE = 50;
	public static final int FBACKPAGE = 51;
	public static final int BACKPAGE = 52;
	public static final int FORWARDPAGE = 53;
	public static final int FFORWARDPAGE = 54;
	public static final int LASTPAGE = 55;
	public static final int GOTO = 56;
	
	public static final int SINGLE = 57;
	public static final int CONTINUOUS = 58;
	public static final int CONTINUOUS_FACING = 59;
	public static final int FACING = 60;

	public static final int FULLSCREEN=61;
	
	/**combo boxes start at 250*/
	public static final int QUALITY = 250;
	public static final int ROTATION = 251;
	public static final int SCALING = 252;

	/**
	 * external/itext menu options start at 500 - add your own CONSTANT here
	 * and refer to action using name at ALL times
	 */
	public static final int SAVEFORM = 500;
	public static final int PDF = 501;
	public static final int ROTATE=502;
	public static final int DELETE=503;
	public static final int ADD=504;
	public static final int SECURITY=505;
	public static final int ADDHEADERFOOTER=506;
	public static final int STAMPTEXT=507;
	public static final int STAMPIMAGE=508;
	public static final int SETCROP=509;
	public static final int NUP = 510;
	public static final int HANDOUTS = 511;
	//public static final int NEWFUNCTION = 512;


	private Values commonValues;
	private SwingGUI currentGUI;
	private PdfDecoder decode_pdf;

	private GUIThumbnailPanel thumbnails;

	/**window for full screen mode*/
	Window win;

	/**image if file tiff or png or jpg*/
	private BufferedImage img=null;

	private int noOfRecentDocs;
	private RecentDocuments recent;

	private JMenuItem[] recentDocuments;

	private final Font headFont=new Font("SansSerif",Font.BOLD,14);

	/**flag used for text popup display to show if menu disappears*/
	private boolean display=true;

	private PropertiesFile properties;
	final private GUISearchWindow searchFrame;

	private Printer currentPrinter;

	/**atomic lock for open thread*/
	private boolean isOpening;
	private boolean fileIsURL;

	public Commands(Values commonValues,SwingGUI currentGUI,PdfDecoder decode_pdf,GUIThumbnailPanel thumbnails,
			PropertiesFile properties , GUISearchWindow searchFrame,Printer currentPrinter) {
		this.commonValues=commonValues;
		this.currentGUI=currentGUI;
		this.decode_pdf=decode_pdf;
		this.thumbnails=thumbnails;
		this.properties=properties;
		this.currentPrinter=currentPrinter;

		this.noOfRecentDocs=properties.getNoRecentDocumentsToDisplay();
		recentDocuments = new JMenuItem[noOfRecentDocs];
		this.recent=new RecentDocuments(noOfRecentDocs,properties);

		this.searchFrame=searchFrame;
	}

	/**
	 * main routine which executes code for current command
	 */
	public void executeCommand(int ID) {

		String fileToOpen;
		switch(ID){

		case INFO:
			currentGUI.getInfoBox();
			break;

		case BITMAP:
			if(commonValues.getSelectedFile()==null){
				JOptionPane.showMessageDialog(currentGUI.getFrame(),Messages.getMessage("PdfViewer.OpenFile"));
			}else{
				//get values from user
				SaveBitmap current_selection = new SaveBitmap(commonValues.getInputDir(), commonValues.getPageCount(),commonValues.getCurrentPage());
				int userChoice=current_selection.display(currentGUI.getFrame(),Messages.getMessage("PdfViewer.SaveAsBitmap"));
				
				
				//get parameters and call if YES
				if(fileIsURL){
					currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.CannotExportFromURL"));
				}else if (userChoice == JOptionPane.OK_OPTION){
					Exporter exporter=new Exporter(currentGUI,commonValues.getSelectedFile(),decode_pdf);
					exporter.extractPagesAsImages(current_selection);
				}
			}
			break;

		case IMAGES:
			if(commonValues.getSelectedFile()==null){
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
			}else{
				//get values from user

				SaveImage current_selection = new SaveImage(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
				int userChoice=current_selection.display(currentGUI.getFrame(),Messages.getMessage("PdfViewerTitle.SaveImagesFromPageRange"));

				//get parameters and call if YES
				if(fileIsURL){
					currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.CannotExportFromURL"));
				}else if (userChoice == JOptionPane.OK_OPTION){
					Exporter exporter=new Exporter(currentGUI,commonValues.getSelectedFile(),decode_pdf);
					exporter.extractImagesOnPages(current_selection);
				}
			}
			break;

		case TEXT:

            if(commonValues.getSelectedFile()==null){
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
            }else if(!decode_pdf.isExtractionAllowed()){
                currentGUI.showMessageDialog("Not allowed");
            }else{
				//get values from user
				SaveText current_selection = new SaveText(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
				int userChoice=current_selection.display(currentGUI.getFrame(),Messages.getMessage("PdfViewerTitle.SaveTextFromPageRange"));

				//get parameters and call if YES
				if(fileIsURL){
					currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.CannotExportFromURL"));
				}else if (userChoice == JOptionPane.OK_OPTION){
					Exporter exporter=new Exporter(currentGUI,commonValues.getSelectedFile(),decode_pdf);
					exporter.extractTextOnPages(current_selection);
				}
			}
			break;

		case SAVE:
			saveFile();
			break;

		case PRINT:
			if(commonValues.getSelectedFile()!=null){
				if(!currentPrinter.isPrinting()){
					if(!commonValues.isPDF()){
						currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.ImagePrinting"));
					}else{
						currentPrinter.printPDF(decode_pdf,currentGUI);
					}
				}else
					currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintFinish.message"));
			}else
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewerNoFile.message"));
			break;

		case EXIT:
			if(currentPrinter.isPrinting())
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewerStillPrinting.text"));
			else
				exit();
			break;

		case AUTOSCROLL:
			currentGUI.toogleAutoScrolling();
			break;

		case DOCINFO:
			if(!commonValues.isPDF())
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.ImageSearch"));
			else
				currentGUI.showDocumentProperties(commonValues.getSelectedFile(), commonValues.getInputDir(), commonValues.getFileSize(), commonValues.getPageCount(), commonValues.getCurrentPage());
			break;

		case OPENFILE:

			/**
			 * warn user on forms
			 */
			handleUnsaveForms();


            if(currentPrinter.isPrinting())
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintWait.message"));
			else if(commonValues.isProcessing() || isOpening)
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
			else{
                if(commonValues.isContentExtractor())
                 currentGUI.setPDFOutlineVisible(false);
                 
                selectFile();
                 
                fileIsURL=false;;
			}
			break;

		case SNAPSHOT:
			
			if(decode_pdf.getDisplayView()!=Display.SINGLE_PAGE){
				currentGUI.showMessageDialog(Messages.getMessage("PageLayoutMessage.SinglePageOnly"));
			}else{
				commonValues.toggleExtractImageOnSelection();

				currentGUI.toggleSnapshotButton();
			}
			
			break;

		case FIND:
			if(commonValues.getSelectedFile()==null){
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
			}else if(!commonValues.isPDF()){
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.ImageSearch"));
			}else if(decode_pdf.getDisplayView()!=Display.SINGLE_PAGE){
					currentGUI.showMessageDialog(Messages.getMessage("PageLayoutMessage.SinglePageOnly"));
			}else if((!searchFrame.isSearchVisible()))
				searchFrame.find();
			else
				searchFrame.grabFocusInInput();

			break;

		case OPENURL:

			/**
			 * warn user on forms
			 */
			handleUnsaveForms();

			if(currentPrinter.isPrinting())
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintWait.message"));
			else if(commonValues.isProcessing() || isOpening)
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
			else{

				currentGUI.resetNavBar();
				String newFile=selectURL();
					if(newFile!=null){
						commonValues.setSelectedFile(newFile);
					fileIsURL=true;
					}
				}

			break;

		case VISITWEBSITE:
			try {
				BrowserLauncher.openURL(Messages.getMessage("PdfViewer.VisitWebsite"));
			} catch (IOException e1) {
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.ErrorWebsite"));
			}
			break;

		case PREVIOUSDOCUMENT:
			if(currentPrinter.isPrinting())
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintWait.message"));
			else if(commonValues.isProcessing() || isOpening)
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
			else{
				fileToOpen=recent.getPreviousDocument();
				if(fileToOpen!=null)
					open(fileToOpen);
			}
			break;

		case NEXTDOCUMENT:
			if(currentPrinter.isPrinting())
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintWait.message"));
			else if(commonValues.isProcessing() || isOpening)
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
			else{
				fileToOpen=recent.getNextDocument();
				if(fileToOpen!=null)
					open(fileToOpen);
			}
			break;

		case FIRSTPAGE:
			if((commonValues.getSelectedFile()!=null)&&(commonValues.getPageCount()>1)&&(commonValues.getCurrentPage()!=1))
				back(commonValues.getCurrentPage()-1);
			break;

		case FBACKPAGE:
			if(commonValues.getSelectedFile()!=null)
				if(commonValues.getCurrentPage()<10)
					back(commonValues.getCurrentPage()-1);
				else
					back(10);
			break;

		case BACKPAGE:
			if(commonValues.getSelectedFile()!=null)
				back(1);
			break;

		case FORWARDPAGE:
			if(commonValues.getSelectedFile()!=null)
				forward(1);
			break;

		case FFORWARDPAGE:
			if(commonValues.getSelectedFile()!=null)
				if(commonValues.getPageCount()<commonValues.getCurrentPage()+10)
					forward(commonValues.getPageCount()-commonValues.getCurrentPage());
				else
					forward(10);
			break;

		case LASTPAGE:
			if((commonValues.getSelectedFile()!=null)&&(commonValues.getPageCount()>1)&&(commonValues.getPageCount()-commonValues.getCurrentPage()>0))
				forward(commonValues.getPageCount()-commonValues.getCurrentPage());
			break;

		case GOTO:
			String page = currentGUI.showInputDialog(Messages.getMessage("PdfViewer.EnterPageNumber"), Messages.getMessage("PdfViewer.GotoPage"), JOptionPane.QUESTION_MESSAGE);
			if(page != null)
				gotoPage(page);
			break;
			
	    //start-os>
		case SINGLE:
            currentGUI.alignLayoutMenuOption(Display.SINGLE_PAGE);
            decode_pdf.setDisplayView(Display.SINGLE_PAGE, Display.DISPLAY_CENTERED);
            currentGUI.resetRotationBox();
			break;
		case CONTINUOUS:
            currentGUI.alignLayoutMenuOption(Display.CONTINUOUS);
            decode_pdf.setDisplayView(Display.CONTINUOUS, Display.DISPLAY_CENTERED);
            currentGUI.setSelectedComboIndex(Commands.ROTATION, 0);
			break;
		case CONTINUOUS_FACING:
            currentGUI.alignLayoutMenuOption(Display.CONTINUOUS_FACING);
            decode_pdf.setDisplayView(Display.CONTINUOUS_FACING, Display.DISPLAY_CENTERED);
            currentGUI.setSelectedComboIndex(Commands.ROTATION, 0);
			break;
		case FACING:
            currentGUI.alignLayoutMenuOption(Display.FACING);
            decode_pdf.setDisplayView(Display.FACING, Display.DISPLAY_CENTERED);
            currentGUI.decodePage(false);//ensure all pages appear
            currentGUI.setSelectedComboIndex(Commands.ROTATION, 0);
            break;


		case FULLSCREEN:
			// Determine if full-screen mode is supported directly
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			if (gs.isFullScreenSupported()) {
				// Full-screen mode is supported
			} else {
				// Full-screen mode will be simulated
			}

			// Create a window for full-screen mode; add a button to leave full-screen mode
			if(win==null){
				Frame frame = new Frame(gs.getDefaultConfiguration());
				win = new Window(frame);
			}

			currentGUI.getFrame().getContentPane().remove(currentGUI.getDisplayPane());
			win.add(currentGUI.getDisplayPane(),BorderLayout.CENTER);

			// Create a button that leaves full-screen mode
			Button btn = new Button("Return");
			win.add(btn, BorderLayout.NORTH);

			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {


					// Return to normal windowed mode
					GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
					GraphicsDevice gs = ge.getDefaultScreenDevice();
					gs.setFullScreenWindow(null);

					win.remove(currentGUI.getDisplayPane());
					currentGUI.getFrame().getContentPane().add(currentGUI.getDisplayPane(), BorderLayout.CENTER);
					currentGUI.getDisplayPane().invalidate();
					currentGUI.getDisplayPane().updateUI();
					currentGUI.getFrame().getContentPane().validate();

					win.dispose();
				}
			});


			try {
				// Enter full-screen mode
				gs.setFullScreenWindow(win);
				win.validate();
			}catch(Error e){
				currentGUI.showMessageDialog("Full screen mode not supported on this machine.\n" +
						"JPedal will now exit");

				this.exit();
				// ...
			} finally {
				// Exit full-screen mode
			//	gs.setFullScreenWindow(null);
			}
			break;
		

			
		case QUALITY:
			if(!commonValues.isProcessing()){
				boolean useHiresImage=true;
				if(currentGUI.getSelectedComboIndex(Commands.QUALITY)==0)
					useHiresImage = false;

                if(commonValues.getSelectedFile()!=null){
                	
                	decode_pdf.unsetScaling();
                    //tell user page will be redrawn
                    currentGUI.showMessageDialog(Messages.getMessage("PdfViewerReparseWait.message"));

                    //reset flag and re-decode page
                    decode_pdf.useHiResScreenDisplay(useHiresImage);
                    commonValues.setUseHiresImage(useHiresImage);

                    try {
                        currentGUI.decodePage(false);
                    } catch (Exception e1) {
                    	System.err.println("Exception"+" " + e1 + "decoding page after image quality changes");
    					e1.printStackTrace();
                    }
                    //decode_pdf.updateUI();
                }
            }

			break;

		//<end-os>

		case SCALING:
			if(!commonValues.isProcessing()){
				if(commonValues.getSelectedFile()!=null)
					currentGUI.zoom(false);
			}
			break;

		case ROTATION:
			if(commonValues.getSelectedFile()!=null)
				currentGUI.rotate();
				currentGUI.getDisplayPane();
			break;

		/**
		 * external/itext menu options start at 500 - add your own code here
		 */
		case SAVEFORM:
			saveChangedForm();
			break;

        //<start-13>
			
		case PDF:
			if(commonValues.getSelectedFile()==null){
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
			}else{
				//get values from user
				SavePDF current_selection = new SavePDF(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
				int userChoice=current_selection.display(currentGUI.getFrame(),Messages.getMessage("PdfViewerTitle.SavePagesAsPdf"));

				//get parameters and call if YES
				if (userChoice == JOptionPane.OK_OPTION){
					ItextFunctions itextFunctions=new ItextFunctions(currentGUI,commonValues.getSelectedFile(),decode_pdf);
					itextFunctions.extractPagesToNewPDF(current_selection);
				}
			}
			break;

		case ROTATE:
			if(commonValues.getSelectedFile()==null){
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
			}else{
				//get values from user
				RotatePDFPages current_selection = new RotatePDFPages(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
				int userChoice=current_selection.display(currentGUI.getFrame(),Messages.getMessage("PdfViewerRotation.text"));

				//get parameters and call if YES
				if (userChoice == JOptionPane.OK_OPTION){

					PdfPageData currentPageData=decode_pdf.getPdfPageData();

					decode_pdf.closePdfFile();
					ItextFunctions itextFunctions=new ItextFunctions(currentGUI,commonValues.getSelectedFile(),decode_pdf);
					itextFunctions.rotate(commonValues.getPageCount(),currentPageData,current_selection);
					open(commonValues.getSelectedFile());
				}
				
			}

			break;

		case SETCROP:
			if(commonValues.getSelectedFile()==null){
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
			}else{
				//get values from user
				CropPDFPages cropPage = new CropPDFPages(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
				int cropPageChoice=cropPage.display(currentGUI.getFrame(),Messages.getMessage("PdfViewerTooltip.PDFCropPages"));

				//get parameters and call if YES
				if (cropPageChoice == JOptionPane.OK_OPTION){

					PdfPageData currentPageData=decode_pdf.getPdfPageData();

					decode_pdf.closePdfFile();
					ItextFunctions itextFunctions=new ItextFunctions(currentGUI,commonValues.getSelectedFile(),decode_pdf);
					itextFunctions.setCrop(commonValues.getPageCount(),currentPageData,cropPage);
					open(commonValues.getSelectedFile());
				}
			}

			break;

		case NUP:
			
			if(commonValues.getSelectedFile()==null){
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
			}else{
//				get values from user
				ExtractPDFPagesNup nup = new ExtractPDFPagesNup(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
				int nupChoice=nup.display(currentGUI.getFrame(),Messages.getMessage("PdfViewerNUP.titlebar"));

				//get parameters and call if YES
				if (nupChoice == JOptionPane.OK_OPTION){

					PdfPageData currentPageData=decode_pdf.getPdfPageData();

					//decode_pdf.closePdfFile();
					ItextFunctions itextFunctions=new ItextFunctions(currentGUI,commonValues.getSelectedFile(),decode_pdf);
					itextFunctions.nup(commonValues.getPageCount(),currentPageData,nup);
					//open(commonValues.getSelectedFile());
				}
			}

			break;

		case HANDOUTS:
			if(fileIsURL)
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.CannotExportFromURL"));

				if(commonValues.getSelectedFile()==null){
					currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
				}else{
					if(!fileIsURL){//ensure file choose not displayed if opened from URL
						JFileChooser chooser1 = new JFileChooser();
						chooser1.setFileSelectionMode(JFileChooser.FILES_ONLY);
						
						int approved1=chooser1.showSaveDialog(null);
						if(approved1==JFileChooser.APPROVE_OPTION){

							File file = chooser1.getSelectedFile();

							ItextFunctions itextFunctions=new ItextFunctions(currentGUI,commonValues.getSelectedFile(),decode_pdf);
							itextFunctions.handouts(file.getAbsolutePath());
						}
					}
				}
			
			break;

		case DELETE:

			if(commonValues.getSelectedFile()==null){
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
			}else{
				//get values from user
				DeletePDFPages deletedPages = new DeletePDFPages(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
				int deletedPagesChoice=deletedPages.display(currentGUI.getFrame(),Messages.getMessage("PdfViewerDelete.text"));

				//get parameters and call if YES
				if (deletedPagesChoice == JOptionPane.OK_OPTION){

					PdfPageData currentPageData=decode_pdf.getPdfPageData();

					decode_pdf.closePdfFile();
					ItextFunctions itextFunctions=new ItextFunctions(currentGUI,commonValues.getSelectedFile(),decode_pdf);
					itextFunctions.delete(commonValues.getPageCount(),currentPageData,deletedPages);
					open(commonValues.getSelectedFile());
				}
			}

			break;

		case ADDHEADERFOOTER:

			if(commonValues.getSelectedFile()==null){
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
			}else{

				//get values from user
				AddHeaderFooterToPDFPages addHeaderFooter = new AddHeaderFooterToPDFPages(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
				int headerFooterPagesChoice=addHeaderFooter.display(currentGUI.getFrame(),Messages.getMessage("PdfViewerTitle.AddHeaderAndFooters"));

				//get parameters and call if YES
				if (headerFooterPagesChoice == JOptionPane.OK_OPTION){

					PdfPageData currentPageData=decode_pdf.getPdfPageData();

					decode_pdf.closePdfFile();
					ItextFunctions itextFunctions=new ItextFunctions(currentGUI,commonValues.getSelectedFile(),decode_pdf);
					itextFunctions.addHeaderFooter(commonValues.getPageCount(),currentPageData,addHeaderFooter);
					open(commonValues.getSelectedFile());
				}
			}

			break;

		case STAMPTEXT:

			if(commonValues.getSelectedFile()==null){
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
			}else{
				//get values from user
				StampTextToPDFPages stampText = new StampTextToPDFPages(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
				int stampTextChoice=stampText.display(currentGUI.getFrame(),Messages.getMessage("PdfViewerStampText.text"));

				//get parameters and call if YES
				if (stampTextChoice == JOptionPane.OK_OPTION){

					PdfPageData currentPageData=decode_pdf.getPdfPageData();

					decode_pdf.closePdfFile();
					ItextFunctions itextFunctions=new ItextFunctions(currentGUI,commonValues.getSelectedFile(),decode_pdf);
					itextFunctions.stampText(commonValues.getPageCount(),currentPageData,stampText);
					open(commonValues.getSelectedFile());
				}
			}

			break;

		case STAMPIMAGE:

			if(commonValues.getSelectedFile()==null){
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
			}else{

				//get values from user
				StampImageToPDFPages stampImage = new StampImageToPDFPages(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
				int stampImageChoice=stampImage.display(currentGUI.getFrame(),Messages.getMessage("PdfViewerStampImage.text"));

				//get parameters and call if YES
				if (stampImageChoice == JOptionPane.OK_OPTION){

					PdfPageData currentPageData=decode_pdf.getPdfPageData();

					decode_pdf.closePdfFile();
					ItextFunctions itextFunctions=new ItextFunctions(currentGUI,commonValues.getSelectedFile(),decode_pdf);
					itextFunctions.stampImage(commonValues.getPageCount(),currentPageData,stampImage);
					open(commonValues.getSelectedFile());
				}
			}

			break;

		case ADD:

			if(commonValues.getSelectedFile()==null){
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
			}else{
				//get values from user
				InsertBlankPDFPage addPage = new InsertBlankPDFPage(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
				int positionToAdd=addPage.display(currentGUI.getFrame(),Messages.getMessage("PdfViewer.BlankPage"));

				//get parameters and call if YES
				if (positionToAdd == JOptionPane.OK_OPTION){

					PdfPageData currentPageData=decode_pdf.getPdfPageData();

					decode_pdf.closePdfFile();
					ItextFunctions itextFunctions=new ItextFunctions(currentGUI,commonValues.getSelectedFile(),decode_pdf);
					itextFunctions.add(commonValues.getPageCount(),currentPageData,addPage);
					open(commonValues.getSelectedFile());
				}
			}

			break;

		case SECURITY:

			if(commonValues.getSelectedFile()==null){
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
			}else{
				//get values from user
				EncryptPDFDocument encryptPage = new EncryptPDFDocument(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
				int encrypt=encryptPage.display(currentGUI.getFrame(),"Standard Security");

				//get parameters and call if YES
				if (encrypt == JOptionPane.OK_OPTION){

					PdfPageData currentPageData=decode_pdf.getPdfPageData();

					decode_pdf.closePdfFile();
					ItextFunctions itextFunctions=new ItextFunctions(currentGUI,commonValues.getSelectedFile(),decode_pdf);
					itextFunctions.encrypt(commonValues.getPageCount(),currentPageData,encryptPage);
					open(commonValues.getSelectedFile());
				}
			}

			break;
		//<end-13>

		//case MYFUNCTION:
			/**add your code here. We recommend you put it in an external class such as
			 *
			 * MyFactory.newFunction(parameters);
			 *
			 * or
			 *
			 * ItextFunctions itextFunctions=new ItextFunctions(currentGUI.getFrame(),commonValues.getSelectedFile(),decode_pdf);
			 * itextFunctions.newFeature(parameters);
			 */
			//break;

		default:
			System.out.println("No menu item set");
		}
	}

	 /**add listeners to forms to track changes - could also do other tasks like send data to
	  * database server
	  */
	private void saveChangedForm() {
		org.jpedal.objects.acroforms.AcroRenderer formRenderer=decode_pdf.getCurrentFormRenderer();

		if(formRenderer==null)
			return;

		List names=null;

		try {
			names = formRenderer.getComponentNameList();
		} catch (PdfException e1) {
			e1.printStackTrace();
		}

		if(names==null){
			currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFields"));
		}else{
			/**
			 * create the file chooser to select the file
			 */
			File file=null;
			String fileToSave="";
			boolean finished=false;
			while(!finished){
				JFileChooser chooser = new JFileChooser(commonValues.getInputDir());
				chooser.setSelectedFile(new File(commonValues.getInputDir()+"/"+commonValues.getSelectedFile()));
				chooser.addChoosableFileFilter(new FileFilterer(new String[]{"pdf"}, "Pdf (*.pdf)"));
				chooser.addChoosableFileFilter(new FileFilterer(new String[]{"fdf"}, "fdf (*.fdf)"));
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				//set default name to current file name
				int approved=chooser.showSaveDialog(null);
				if(approved==JFileChooser.APPROVE_OPTION){
					file = chooser.getSelectedFile();
					fileToSave=file.getAbsolutePath();

					if(!fileToSave.endsWith(".pdf")){
						fileToSave += ".pdf";
						file=new File(fileToSave);
					}

					if(fileToSave.equals(commonValues.getSelectedFile())){
						currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.SaveError"));
						continue;
					}

					if(file.exists()){
						int n=currentGUI.showConfirmDialog(fileToSave+"\n" +
								Messages.getMessage("PdfViewerMessage.FileAlreadyExists")+".\n" +
								Messages.getMessage("PdfViewerMessage.ConfirmResave"),
								Messages.getMessage("PdfViewerMessage.Resave")
								,JOptionPane.YES_NO_OPTION);
						if(n==1)
							continue;
					}
					finished=true;
				}else{
					return;
				}
			}

			//<start-13>
			ItextFunctions itextFunctions=new ItextFunctions(currentGUI,commonValues.getSelectedFile(),decode_pdf);
			itextFunctions.saveFormsData(fileToSave);
			//<end-13>
			
			/**
			 * reset flag and graphical clue
			 */
			commonValues.setFormsChanged(false);
			currentGUI.setViewerTitle(null);

		}
	}

	/**
	 * warns user forms unsaved and offers save option
	  */
	public void handleUnsaveForms() {
		if((commonValues.isFormsChanged())&&(commonValues.isItextOnClasspath())){
			int n = currentGUI.showConfirmDialog(Messages.getMessage("PdfViewerFormsUnsavedOptions.message"),Messages.getMessage("PdfViewerFormsUnsavedWarning.message"), JOptionPane.YES_NO_OPTION);

			if(n==JOptionPane.YES_OPTION)
				saveChangedForm();
		}
		commonValues.setFormsChanged(false);
	}

	/**
	 * extract selected area as a rectangle and show onscreen
	 */
	public void extractSelectedScreenAsImage() {

		/**ensure co-ords in right order*/
		int t_x1=commonValues.m_x1;
		int t_x2=commonValues.m_x2;
		int t_y1=commonValues.m_y1;
		int t_y2=commonValues.m_y2;

		if(commonValues.m_y1<commonValues.m_y2){
			t_y2=commonValues.m_y1;
			t_y1=commonValues.m_y2;
		}

		if(commonValues.m_x1>commonValues.m_x2){
			t_x2=commonValues.m_x1;
			t_x1=commonValues.m_x2;
		}
		float scaling = 100;
		
		if(PdfDecoder.isRunningOnWindows)
			scaling = 100*currentGUI.getScaling();
		
		
		final BufferedImage snapShot=decode_pdf.getSelectedRectangleOnscreen(t_x1,t_y1,t_x2,t_y2,scaling);

		/**
		 * put in panel
		 */
		//if(temp!=null){
		JPanel image_display = new JPanel();
		image_display.setLayout( new BorderLayout() );

		//wrap image so we can display
		if( snapShot != null ){
			IconiseImage icon_image = new IconiseImage( snapShot );

			//add image if there is one
			image_display.add( new JLabel( icon_image ), BorderLayout.CENTER );
		}else{
			return;
		}

		final JScrollPane image_scroll = new JScrollPane();
		image_scroll.getViewport().add( image_display );

		//set image size
		int imgSize=snapShot.getWidth();
		if(imgSize<snapShot.getHeight())
			imgSize=snapShot.getHeight();
		imgSize=imgSize+50;
		if(imgSize>450)
			imgSize=450;

		/**resizeable pop-up for content*/
		JFrame frame = currentGUI.getFrame();
		final JDialog displayFrame =  new JDialog(frame,true);
		if(commonValues.getModeOfOperation()!=Values.RUNNING_APPLET){
			displayFrame.setLocationRelativeTo(null);
			displayFrame.setLocation(frame.getLocationOnScreen().x+10,frame.getLocationOnScreen().y+10);
		}

		displayFrame.setSize(imgSize,imgSize);
		displayFrame.setTitle(Messages.getMessage("PdfViewerMessage.SaveImage"));
		displayFrame.getContentPane().setLayout(new BorderLayout());
		displayFrame.getContentPane().add(image_scroll,BorderLayout.CENTER);

		JPanel buttonBar=new JPanel();
		buttonBar.setLayout(new BorderLayout());
		displayFrame.getContentPane().add(buttonBar,BorderLayout.SOUTH);

		/**
		 * yes option allows user to save content
		 */
		JButton yes=new JButton(Messages.getMessage("PdfMessage.Yes"));
		yes.setFont(new Font("SansSerif", Font.PLAIN, 12));
		buttonBar.add(yes,BorderLayout.WEST);
		yes.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {

				displayFrame.hide();

				File file=null;
				String fileToSave="";
				boolean finished=false;
				while(!finished){
					final JFileChooser chooser = new JFileChooser(System.getProperty( "user.dir" ) );

					chooser.addChoosableFileFilter( new FileFilterer( new String[] { "tif", "tiff" }, "TIFF" ) );
					chooser.addChoosableFileFilter( new FileFilterer( new String[] { "jpg","jpeg" }, "JPEG" ) );

					int approved = chooser.showSaveDialog(image_scroll);

					if(approved==JFileChooser.APPROVE_OPTION){

						file = chooser.getSelectedFile();
						fileToSave=file.getAbsolutePath();

						String format=chooser.getFileFilter().getDescription();

						if(format.equals("All Files"))
							format="TIFF";

						if(!fileToSave.toLowerCase().endsWith(("."+format).toLowerCase())){
							fileToSave += "."+format;
							file=new File(fileToSave);
						}

						if(file.exists()){

							int n=currentGUI.showConfirmDialog(fileToSave+"\n" +
									Messages.getMessage("PdfViewerMessage.FileAlreadyExists")+".\n" +
									Messages.getMessage("PdfViewerMessage.ConfirmResave"),
									Messages.getMessage("PdfViewerMessage.Resave"),JOptionPane.YES_NO_OPTION);
							if(n==1)
								continue;
						}

						if(JAIHelper.isJAIused())
							JAIHelper.confirmJAIOnClasspath();


						//Do the actual save
						if(snapShot!=null) {
							if(JAIHelper.isJAIused())
								javax.media.jai.JAI.create("filestore", snapShot, fileToSave, format);
							else if(format.toLowerCase().startsWith("tif"))
								currentGUI.showMessageDialog("Please setup JAI library for Tiffs");
							else{
								try {
									ImageIO.write(snapShot,format,new File(fileToSave));
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						}
						finished=true;
					}else{
						return;
					}
				}

				displayFrame.dispose();

			}
		});

		/**
		 * no option just removes display
		 */
		JButton no=new JButton(Messages.getMessage("PdfMessage.No"));
		no.setFont(new Font("SansSerif", Font.PLAIN, 12));
		buttonBar.add(no,BorderLayout.EAST);
		no.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				displayFrame.dispose();
			}});

		/**show the popup*/
		displayFrame.show();
	//}
		
	}

	/**
	 * routine to link GUI into text extraction functions
	 */
	public void extractSelectedText() {

        if(!decode_pdf.isExtractionAllowed()){
            currentGUI.showMessageDialog("Not allowed");
            return ;
        }

        /**ensure co-ords in right order*/
		int t_x1=commonValues.m_x1;
		int t_x2=commonValues.m_x2;
		int t_y1=commonValues.m_y1;
		int t_y2=commonValues.m_y2;

		if(commonValues.m_y1<commonValues.m_y2){
			t_y2=commonValues.m_y1;
			t_y1=commonValues.m_y2;
		}

		if(commonValues.m_x1>commonValues.m_x2){
			t_x2=commonValues.m_x1;
			t_x1=commonValues.m_x2;
		}

		//methods available and description
		String[] groupings = {Messages.getMessage("PdfViewerRect.label"),
				Messages.getMessage("PdfViewerRect.message"),
				Messages.getMessage("PdfViewerTable.label"),
				Messages.getMessage("PdfViewerTable.message"),
				Messages.getMessage("PdfViewerWordList.label"),
				Messages.getMessage("PdfViewerWordList.message"),
				Messages.getMessage("PdfViewerFind.label"),
				Messages.getMessage("PdfViewerFind.message")};

		//flag to show if text or XML
		boolean isXML=true;

		/**
		 * build and show a display
		 */
		JPanel display_value = new JPanel();
		display_value.setLayout(new BoxLayout(display_value,BoxLayout.Y_AXIS));


		JLabel region = new JLabel(Messages.getMessage("PdfViewerCoords.message")+
				" " + commonValues.m_x1
				+ " , " + commonValues.m_y1 + " , " + commonValues.m_x2 + " , " + commonValues.m_y2);
		region.setFont(headFont);

		display_value.add(region);
		display_value.add(new JLabel(" "));

		/**
		 * provide list of groupings available and brief description
		 */
		int groupingCount = groupings.length / 2;
		Object[] options = new Object[groupingCount + 2];

		for (int i = 0; i < groupingCount; i++) {
			options[i + 2] = groupings[(i * 2)];

			JLabel groupingTitle = new JLabel(groupings[i * 2]);
			groupingTitle.setForeground(Color.blue);
			groupingTitle.setFont(headFont); //$NON-NLS-1$
			display_value.add(groupingTitle);

			JLabel groupingInfo = new JLabel(groupings[(i * 2) + 1]);
			groupingInfo.setFont(headFont); //$NON-NLS-1$
			display_value.add(groupingInfo);
			display_value.add(new JLabel(" ")); //$NON-NLS-1$

		}

		//add other buttons
		options[0] = Messages.getMessage("PdfViewerCancel.text");
		options[1] = Messages.getMessage("PdfViewerHelpMenu.text");

		/**
		 * bringup display and process user requests
		 */
		display = true;

		while (display) {
			int n = currentGUI.showOptionDialog(display_value,
					Messages.getMessage("PdfViewerGroupingOptions.message"),
					JOptionPane.OK_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, options,
					options[0]);

			String extractedText = null;

			try {
				/** common extraction code */
				PdfGroupingAlgorithms currentGrouping = null;

				if (n > 1) {

					/** create a grouping object to apply grouping to data */
					currentGrouping = decode_pdf.getGroupingObject();

				}

				//switch off display - pops up again if help selected
				display = false;

				//make choice
				switch (n) {
				case 1: //help

					JTextArea info = new JTextArea(Messages.getMessage("PdfViewerGroupingInfo.message"));

					currentGUI.showMessageDialog(info);
					display = true;
					break;

				case 2: //text extraction

					/**get the text*/
					extractedText = currentGrouping.extractTextInRectangle(
							t_x1, t_y1, t_x2, t_y2, commonValues.getCurrentPage(), false,
							true);

					/**
					 * find out if xml or text - as we need to turn xml off before
					 * extraction. So we assume xml and strip out. This is obviously
					 */
					int useXml=currentGUI.showConfirmDialog(Messages.getMessage("PdfViewerXmlMessage.message"),
							Messages.getMessage("PdfViewerOutputFormat.message"),JOptionPane.YES_NO_OPTION);

					if(extractedText==null)
						currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.NoTextFound"));
					else if(useXml!=0){
						extractedText=Strip.stripXML(extractedText).toString();

						isXML=false;
					}else
						isXML=true;

					break;

				case 3: //table

					//rest to default in case text option selected
					isXML=true;

					Map content;

					/**
					 * find out if xml or text - as we need to turn xml off before
					 * extraction. So we assume xml and strip out. This is obviously
					 */
					int useCSV=currentGUI.showConfirmDialog(Messages.getMessage("PdfViewerXHTML.message"),
							Messages.getMessage("PdfViewerOutputFormat.message"),
							JOptionPane.YES_NO_OPTION);

					if(useCSV!=0)
						content = currentGrouping.extractTextAsTable(t_x1,
								t_y1, t_x2, t_y2, commonValues.getCurrentPage(), true, false,
								false, false, 0, false);
					else
						content = currentGrouping.extractTextAsTable(t_x1,
								t_y1, t_x2, t_y2, commonValues.getCurrentPage(), false, true,
								true, false, 1, false);

					extractedText = (String) content.get("content");
					break;

				case 4: //text wordlist extraction


					//always reset to use unaltered co-ords
					PdfGroupingAlgorithms.useUnrotatedCoords=true;

					//page data so we can choose portrait or landscape
					PdfPageData pageData=decode_pdf.getPdfPageData();
					int rotation=pageData.getRotation(commonValues.getCurrentPage());
					if(rotation!=0){
						int alterCoords=currentGUI.showConfirmDialog(Messages.getMessage("PdfViewerRotatedCoords.message"),
								Messages.getMessage("PdfViewerOutputFormat.message"),
								JOptionPane.YES_NO_OPTION);

						if(alterCoords==0)
							PdfGroupingAlgorithms.useUnrotatedCoords=false;
					}


					/**get the text*/
					Vector words = currentGrouping.extractTextAsWordlist(
							t_x1,
							t_y1,
							t_x2,
							t_y2,
							commonValues.getCurrentPage(),
							false,
							true,"()!;.,\\/\"\"\'\'");

					/**
					 * find out if xml or text - as we need to turn xml off before
					 * extraction. So we assume xml and strip out. This is obviously
					 */
					useXml=currentGUI.showConfirmDialog(Messages.getMessage("PdfViewerXmlMessage.message"),
							Messages.getMessage("PdfViewerOutputFormat.message"),JOptionPane.YES_NO_OPTION);

					if(words==null)
						currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.NoTextFound"));
					else if(useXml!=0){
						isXML=false;
					}else
						isXML=true;

					if(words!=null){
						//put words into list
						StringBuffer textOutput=new StringBuffer();
						Iterator wordIterator=words.iterator();

						while(wordIterator.hasNext()){

							String currentWord=(String) wordIterator.next();

							/**remove the XML formatting if present - not needed for pure text*/
							if(!isXML)
								currentWord=Strip.convertToText(currentWord);

							int wx1=(int)Float.parseFloat((String) wordIterator.next());
							int wy1=(int)Float.parseFloat((String) wordIterator.next());
							int wx2=(int)Float.parseFloat((String) wordIterator.next());
							int wy2=(int)Float.parseFloat((String) wordIterator.next());

							/**this could be inserting into a database instead*/
							textOutput.append(currentWord+","+wx1+","+wy1+","+wx2+","+wy2+"\n");

						}

						extractedText=textOutput.toString();
					}

					break;

				case 5: //find word in text

					String textToFind=currentGUI.showInputDialog(Messages.getMessage("PdfViewerMessage.GetUserInput"));

					//if cancel return to menu.
					if(textToFind==null || textToFind.length()<1){
						display=true;
						break;
					}

					//<start-demo>
					/**<end-demo>
	                    JOptionPane.showMessageDialog(currentGUI.getFrame(),Messages.getMessage("PdfViewerMessage.FindDemo"));
	                    textToFind=null;
	                    /**/

					boolean isCaseSensitive=false;

					int caseSensitiveOption=currentGUI.showConfirmDialog(Messages.getMessage("PdfViewercase.message"),
							null,	JOptionPane.YES_NO_OPTION);

					if(caseSensitiveOption==0)
						isCaseSensitive=true;

					boolean findAll=false;

					int findAllOption=currentGUI.showConfirmDialog(Messages.getMessage("PdfViewerfindAll.message"),
							null,	JOptionPane.YES_NO_OPTION);

					if(findAllOption==0)
						findAll=true;


					if(textToFind!=null){
						float[] co_ords = currentGrouping.findTextInRectangle(
								t_x1,
								t_y1,
								t_x2,
								t_y2,
								commonValues.getCurrentPage(),
								textToFind,
								isCaseSensitive,
								findAll);

						if(co_ords!=null){
							if(co_ords.length<3)
								currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.Found")+" "+co_ords[0]+","+co_ords[1]);
							else{
								StringBuffer displayCoords = new StringBuffer();
								for(int i=0;i<co_ords.length/2;i++){
									displayCoords.append(Messages.getMessage("PdfViewerMessage.FoundAt")+" ");
									System.out.println("coord.length="+co_ords.length+" i="+i);
									displayCoords.append(co_ords[i*2]);
									displayCoords.append(",");
									displayCoords.append(co_ords[i*2+1]);
									displayCoords.append("\n");
								}
								currentGUI.showMessageDialog(displayCoords.toString());
							}
						}else
							currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.NotFound"));
					}

					break;

				default: //just in case user edits code and to handle cancel
					break;

				}

				if (extractedText != null) {

					JScrollPane scroll=new JScrollPane();
					try {
						JTextPane text_pane=new JTextPane();
						scroll = currentGUI.createPane(text_pane,extractedText,  isXML);
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
					scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
					scroll.setPreferredSize(new Dimension(400,400));

					/**resizeable pop-up for content*/
					final JDialog displayFrame =  new JDialog(currentGUI.getFrame(),true);
					if(commonValues.getModeOfOperation()!=Values.RUNNING_APPLET){
						JFrame frame = currentGUI.getFrame();
						displayFrame.setLocation(frame.getLocationOnScreen().x+10,frame.getLocationOnScreen().y+10);
					}

					displayFrame.setSize(450,450);

					displayFrame.setTitle(Messages.getMessage("PdfViewerExtractedText.menu"));
					displayFrame.getContentPane().setLayout(new BorderLayout());
					displayFrame.getContentPane().add(scroll,BorderLayout.CENTER);

					JPanel buttonBar=new JPanel();
					buttonBar.setLayout(new BorderLayout());
					displayFrame.getContentPane().add(buttonBar,BorderLayout.SOUTH);

					/**
					 * yes option allows user to save content
					 */
					JButton yes=new JButton(Messages.getMessage("PdfViewerMenu.return"));
					yes.setFont(new Font("SansSerif", Font.PLAIN, 12));
					buttonBar.add(yes,BorderLayout.WEST);
					yes.addActionListener(new ActionListener(){

						public void actionPerformed(ActionEvent e) {
							display=true;
							displayFrame.dispose();

						}
					});

					/**
					 * no option just removes display
					 */
					JButton no=new JButton(Messages.getMessage("PdfViewerFileMenuExit.text"));
					no.setFont(new Font("SansSerif", Font.PLAIN, 12));
					buttonBar.add(no,BorderLayout.EAST);
					no.addActionListener(new ActionListener(){

						public void actionPerformed(ActionEvent e) {

							displayFrame.dispose();
						}});

					/**show the popup*/
					displayFrame.show();

				}

			} catch (PdfException e) {
				System.err.println("Exception " + e.getMessage()
						+ " in file " + commonValues.getSelectedFile());
				e.printStackTrace();
			}
		}
	}

	/**
	 * called by nav functions to decode next page
	 */
	private void decodeImage(final boolean resizePanel) {

		//remove any search highlight
		decode_pdf.setFoundTextArea(null);
		decode_pdf.setFoundTextAreas(null);

		currentGUI.setRectangle(null);

		//stop user changing scaling while decode in progress
		currentGUI.resetComboBoxes(false);


		decode_pdf.clearScreen();

		/** if running terminate first */
		thumbnails.terminateDrawing();

		commonValues.setProcessing(true);

		SwingWorker worker = new SwingWorker() {
			public Object construct() {

				try {

					currentGUI.updateStatusMessage(Messages.getMessage("PdfViewerDecoding.page"));

					if(img!=null)
						decode_pdf.addImage(img);
					
					PdfPageData page_data = decode_pdf.getPdfPageData();
					if(img!=null)
						page_data.setMediaBox("0 0 "+img.getWidth()+" "+img.getHeight());
					page_data.checkSizeSet(1);
					currentGUI.resetRotationBox();
					
					/**
					 * make sure screen fits display nicely
					 */
					if ((resizePanel) && (thumbnails.isShownOnscreen()))
						currentGUI.zoom(false);

					if (Thread.interrupted())
						throw new InterruptedException();
					currentGUI.setPageNumber();

					//<start-13>
					currentGUI.setViewerTitle(null); //restore title
					//<end-13>

				} catch (Exception e) {
					//<start-13>
					currentGUI.setViewerTitle(null); //restore title
					//<end-13>
				}

				currentGUI.setStatusProgress(100);

				//reanable user changing scaling
				currentGUI.resetComboBoxes(true);

				//ensure drawn
				decode_pdf.repaint();

				return null;
			}
		};

		worker.start();

	}

	/**
	 *  initial method called to open a new PDF
	 */
	protected boolean openUpFile(String selectedFile) {

		commonValues.maxViewY=0;// rensure reset for any viewport

		boolean fileCanBeOpened = true;

		decode_pdf.closePdfFile();

        /** reset default values */
        currentGUI.setScalingToDefault();

        /** ensure all data flushed from PdfDecoder before we decode the file */
		//decode_pdf.flushObjectValues(true);
		decode_pdf.markAllPagesAsUnread();

		try {

			/** opens the pdf and reads metadata */
			if(commonValues.isPDF()){
				if(selectedFile.startsWith("http:")){
					try{
						decode_pdf.openPdfFileFromURL(commonValues.getSelectedFile());
					}catch(Exception e){
						currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.UrlError"));
						selectedFile=null;
						fileCanBeOpened=false;
					}
				}else{
					decode_pdf.openPdfFile(commonValues.getSelectedFile());

				}
				
				
				//reset thumbnails
				currentGUI.reinitThumbnails();


            }else{

				//set values for page display
				decode_pdf.resetForNonPDFPage();
				
				boolean isTiff=selectedFile.toLowerCase().indexOf(".tif")!=-1;

				//decode image
                if(JAIHelper.isJAIused())
                JAIHelper.confirmJAIOnClasspath();
                
                if(isTiff && JAIHelper.isJAIused()){
					try {
						// Load the source image from a file.
						img = javax.media.jai.JAI.create("fileload", commonValues.getSelectedFile()).getAsBufferedImage();
					} catch (Exception e) {
						LogWriter.writeLog("Exception " + e + Messages.getMessage("PdfViewerError.Loading") + commonValues.getSelectedFile());
					}
				}else{
					try {
						// Load the source image from a file.
						img=ImageIO.read(new File(commonValues.getSelectedFile()));
					} catch (Exception e) {
						LogWriter.writeLog("Exception " + e + "loading " + commonValues.getSelectedFile());
					}

				}

			}
		//<<>>
			currentGUI.updateStatusMessage("opening file");

			/** popup window if needed */
			if ((fileCanBeOpened)&&(decode_pdf.isEncrypted()) && (!decode_pdf.isFileViewable())) {
				fileCanBeOpened = false;
				//<start-13>
				/**
				 * //<end-13>JOptionPane.showMessageDialog(currentGUI.frame,"Please
				 * use Java 1.4 to display encrypted files"); //<start-13>
				 */

				String password = currentGUI.showInputDialog(Messages.getMessage("PdfViewerPassword.message")); //$NON-NLS-1$

				/** try and reopen with new password */
				if (password != null) {
					decode_pdf.setEncryptionPassword(password);
					//decode_pdf.verifyAccess();

					if (decode_pdf.isFileViewable())
						fileCanBeOpened = true;

				}

                if(!fileCanBeOpened)
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPasswordRequired.message"));
				//<end-13>
			}
//				
			currentGUI.reinitialiseTabs();
			
			if (fileCanBeOpened) {

				if(!commonValues.isContentExtractor()){
					properties.addRecentDocument(commonValues.getSelectedFile());
					updateRecentDocuments(properties.getRecentDocuments());
				}

				recent.addToFileList(commonValues.getSelectedFile());

				/** reset values */
				commonValues.setCurrentPage(1);
			}
				
            
			} catch (Exception e) {
			System.err.println(("Exception " + e + " opening file"));

			ErrorDialog.showError(e,Messages.getMessage("PdfViewerOpenerror"),currentGUI.getFrame());
			System.exit(1);

		}
			
        if(!decode_pdf.isOpen() && commonValues.isPDF())
            return false;
        else
            return fileCanBeOpened;

	}

	/**
	 *  checks file can be opened (permission)
	 */
	protected void openFile(String selectedFile) {

		//get any user set dpi
		String hiresFlag=System.getProperty("org.jpedal.hires");
		if(hiresFlag!=null)
			commonValues.setUseHiresImage(true);

		//get any user set dpi
		String memFlag=System.getProperty("org.jpedal.memory");
		if(memFlag!=null){
			commonValues.setUseHiresImage(false);
		}

		//reset flag
		thumbnails.resetToDefault();

		//flush forms list
		currentGUI.setNoPagesDecoded();

        //remove search frame if visible
		if(searchFrame!=null)
			searchFrame.removeSearchWindow(false);

		commonValues.maxViewY=0;// rensure reset for any viewport
		String ending=selectedFile.toLowerCase().trim();
		commonValues.setPDF(ending.endsWith(".pdf")||ending.endsWith(".fdf"));

		
		currentGUI.setQualityBoxVisible(commonValues.isPDF());

        commonValues.setCurrentPage(1);
		
        boolean fileCanBeOpened=openUpFile(commonValues.getSelectedFile());

        try{
			if(fileCanBeOpened)
				processPage();
			else{
				currentGUI.setViewerTitle(Messages.getMessage("PdfViewer.NoFile"));
				decode_pdf.clearScreen();
				this.currentGUI.zoom(false);
				commonValues.setPageCount(1);
				commonValues.setCurrentPage(1);
			}
		}catch(Exception e){
			System.err.println(Messages.getMessage("PdfViewerError.Exception")+" " + e + " "+ Messages.getMessage("PdfViewerError.DecodeFile"));

		}

		//commonValues.setProcessing(false);
	}


	/**
	 * decode and display selected page
	 */
	protected void processPage() {

        if(commonValues.isPDF()&&((decode_pdf.isOpen()||!commonValues.isPDF()))){
			/**
			 * get PRODUCER and if OCR disable text printing
			 */
			PdfFileInformation currentFileInformation=decode_pdf.getFileInformationData();

			/**switch all on by default*/
			decode_pdf.setRenderMode(PdfDecoder.RENDERIMAGES+PdfDecoder.RENDERTEXT);

			String[] values=currentFileInformation.getFieldValues();
			String[] fields=currentFileInformation.getFieldNames();

			/** holding all creators that produce OCR pdf's */
			String[] ocr = {"TeleForm","dgn2pdf"};

			for(int i=0;i<fields.length;i++){

				if((fields[i].equals("Creator"))|(fields[i].equals("Producer"))){

					for(int j=0;j<ocr.length;j++){

						if(values[i].equals(ocr[j])){

							decode_pdf.setRenderMode(PdfDecoder.RENDERIMAGES);

							/**
							 * if we want to use java 13 JPEG conversion
							 */
							decode_pdf.setEnableLegacyJPEGConversion(true);

						}
					}
				}

				boolean currentProcessingStatus=commonValues.isProcessing();
				commonValues.setProcessing(true);	//stops listeners processing spurious event

                if(commonValues.isUseHiresImage()){
					decode_pdf.useHiResScreenDisplay(true);
					currentGUI.setSelectedComboIndex(Commands.QUALITY,1);
				}else{
					decode_pdf.useHiResScreenDisplay(false);
					currentGUI.setSelectedComboIndex(Commands.QUALITY,0);
				}
				commonValues.setProcessing(currentProcessingStatus);

			}
		}
        commonValues.setPageCount(decode_pdf.getPageCount());

		/**special customisations for images*/
		if(!commonValues.isPDF()){
			commonValues.setPageCount(1);
			decode_pdf.useHiResScreenDisplay(true);

		}


        if(commonValues.getPageCount()<commonValues.getCurrentPage()){
			commonValues.setCurrentPage(commonValues.getPageCount());
			System.err.println(commonValues.getCurrentPage()+ " out of range. Opening on last page");
			LogWriter.writeLog(commonValues.getCurrentPage()+ " out of range. Opening on last page");
		}


        //values extraction mode,dpi of images, dpi of page as a factor of 72

        decode_pdf.setExtractionMode(PdfDecoder.TEXT, 72, currentGUI.getScaling());
        
        /**
		 * update the display, including any rotation
		 */
		currentGUI.setPageNumber();

        currentGUI.resetRotationBox();

        if(commonValues.isPDF()){
        	currentGUI.messageShown=false;
			currentGUI.decodePage(true);
        }else{
            //resize (ensure at least certain size)
            currentGUI.zoom(false);

            //add a border
            decode_pdf.setPDFBorder(BorderFactory.createLineBorder(Color.black, 1));

            /** turn off border in printing */
    		decode_pdf.disableBorderForPrinting();

            decodeImage(true);

			commonValues.setProcessing(false);
        }




    }

	/** opens a pdf file and calls the display/decode routines */
	public void selectFile() {

		//remove search frame if visible
		if(searchFrame!=null)
			searchFrame.removeSearchWindow(false);

        currentGUI.resetNavBar();
        
		/**
		 * create the file chooser to select the file
		 */
		final JFileChooser chooser = new JFileChooser(commonValues.getInputDir());
		if(commonValues.getSelectedFile()!=null)
			chooser.setSelectedFile(new File(commonValues.getSelectedFile()));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		String[] pdf = new String[] { "pdf" };
		String[] fdf = new String[] { "fdf" };
		String[] png = new String[] { "png","tif","tiff","jpg","jpeg" };
		chooser.addChoosableFileFilter(new FileFilterer(png, "Images (Tiff, Jpeg,Png)"));
		chooser.addChoosableFileFilter(new FileFilterer(fdf, "fdf (*.fdf)"));
		chooser.addChoosableFileFilter(new FileFilterer(pdf, "Pdf (*.pdf)"));
		
		final int state = chooser.showOpenDialog(currentGUI.getFrame());

		//ensure immediate redraw of blank screen
		//decode_pdf.invalidate();
		//decode_pdf.repaint();

		final File file = chooser.getSelectedFile();

		/**
		 * decode
		 */
		if (file != null && state == JFileChooser.APPROVE_OPTION) {

			String ext=file.getName().toLowerCase();
			boolean isValid=((ext.endsWith(".pdf"))||(ext.endsWith(".fdf"))||
					(ext.endsWith(".tif"))||(ext.endsWith(".tiff"))||
					(ext.endsWith(".png"))||
					(ext.endsWith(".jpg"))||(ext.endsWith(".jpeg")));
			
			if(isValid){
				/** save path so we reopen her for later selections */
				try {
					commonValues.setInputDir(chooser.getCurrentDirectory().getCanonicalPath());
					open(file.getAbsolutePath());

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}else{
                decode_pdf.repaint();
				currentGUI.showMessageDialog( Messages.getMessage("PdfViewer.NotValidPdfWarning"));
			}

		} else { //no file selected so redisplay old
            decode_pdf.repaint();
			currentGUI.showMessageDialog( Messages.getMessage("PdfViewerMessage.NoSelection"));
        }
	}

	private String selectURL() {

		String selectedFile = currentGUI.showInputDialog(Messages.getMessage("PdfViewerMessage.RequestURL"));

		//lose any spaces
		if(selectedFile!=null)
			selectedFile=selectedFile.trim();

		if((selectedFile!=null) &&(!selectedFile.trim().startsWith("http://"))){
			currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.URLMustContain"));
			selectedFile=null;
		}

		if(selectedFile!=null && !selectedFile.endsWith(".pdf")){
			currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NotValidPdfWarning"));
			selectedFile=null;
		}

		if(selectedFile!=null){

			commonValues.setSelectedFile(selectedFile);

			boolean failed=false;
			try {
				URL testExists=new URL(selectedFile);
				URLConnection conn=testExists.openConnection();

				if(conn.getContent()==null)
					failed=true;
			} catch (Exception e) {
				failed=true;
			}

			if(failed){
				selectedFile=null;
				currentGUI.showMessageDialog("URL "+selectedFile+" "+Messages.getMessage("PdfViewerError.DoesNotExist"));
			}
		}

		//ensure immediate redraw of blank screen
		//decode_pdf.invalidate();
		//decode_pdf.repaint();

		/**
		 * decode
		 */
		if (selectedFile != null ) {
			try {

				commonValues.setFileSize(0);

				/** save path so we reopen her for later selections */
				//commonValues.setInputDir(new URL(commonValues.getSelectedFile()).getPath());

				currentGUI.setViewerTitle(null);

			} catch (Exception e) {
				System.err.println(Messages.getMessage("PdfViewerError.Exception")+" " + e + " "+Messages.getMessage("PdfViewerError.GettingPaths"));
			}

			/**
			 * open the file
			 */
			if ((selectedFile != null) && (commonValues.isProcessing() == false)) {

				/**
				 * trash previous display now we are sure it is not needed
				 */
				//decode_pdf.repaint();

				/** if running terminate first */
				thumbnails.terminateDrawing();

				decode_pdf.flushObjectValues(true);

				//reset the viewableArea before opening a new file
				decode_pdf.resetViewableArea();

				currentGUI.stopThumbnails();

                openFile(commonValues.getSelectedFile());

			}

		} else { //no file selected so redisplay old
            decode_pdf.repaint();
			currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.NoSelection"));

		}

		return selectedFile;
	}

	/**move forward one page*/
	private void forward(int count) {

		if (!commonValues.isProcessing()) { //lock to stop multiple accesses

			/**if in range update count and decode next page. Decoded pages are cached so will redisplay
			 * almost instantly*/
			int updatedTotal=commonValues.getCurrentPage()+count;

			if (updatedTotal <= commonValues.getPageCount()) {

                /**
                 * adjust for double jump on facing
                 */
				if(decode_pdf.getDisplayView() == Display.FACING || decode_pdf.getDisplayView() == Display.CONTINUOUS_FACING){
					if((updatedTotal & 1)==1){
						if(updatedTotal<commonValues.getPageCount())
                        updatedTotal++;
                       else if(commonValues.getPageCount()-updatedTotal>1)
                       updatedTotal--;
                    }
                }
				commonValues.setCurrentPage(updatedTotal);
				currentGUI.setPageNumber();
				
				if(decode_pdf.getDisplayView() == Display.CONTINUOUS ||decode_pdf.getDisplayView() == Display.CONTINUOUS_FACING){
					
					currentGUI.decodePage(false);
					
					return ;
				}
				
				currentGUI.resetStatusMessage("Loading Page "+commonValues.getCurrentPage());
				/**reset as rotation may change!*/
				decode_pdf.setPageParameters(currentGUI.getScaling(), commonValues.getCurrentPage());

				//would reset scaling on page change to default
				//currentGUI.setScalingToDefault();
				
				//decode the page
				currentGUI.decodePage(false);
				
				//if scaling to window reset screen to fit rotated page
//				if(currentGUI.getSelectedComboIndex(Commands.SCALING)<3)
//					currentGUI.zoom();

			}
		}else
			currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
	}



	/** move back one page */
	private void back(int count) {

		if (!commonValues.isProcessing()) { //lock to stop multiple accesses

			/**
			 * if in range update count and decode next page. Decoded pages are
			 * cached so will redisplay almost instantly
			 */
			int updatedTotal=commonValues.getCurrentPage()-count;
			if (updatedTotal >= 1) {

                /**
                 * adjust for double jump on facing
                 */
                if(decode_pdf.getDisplayView() == Display.FACING || decode_pdf.getDisplayView() == Display.CONTINUOUS_FACING){
                   if((updatedTotal & 1)==1 && updatedTotal!=1)
                        updatedTotal--;
                }

                commonValues.setCurrentPage(updatedTotal);
                currentGUI.setPageNumber();
				
				if(decode_pdf.getDisplayView() == Display.CONTINUOUS ||
						decode_pdf.getDisplayView() == Display.CONTINUOUS_FACING){
					
					currentGUI.decodePage(false);
					
					return ;
				}

				currentGUI.resetStatusMessage("loading page "+commonValues.getCurrentPage());

				/** reset as rotation may change! */
				decode_pdf.setPageParameters(currentGUI.getScaling(), commonValues.getCurrentPage());

				//would reset scaling on page change to default
				//currentGUI.setScalingToDefault(); //set to 100%

				currentGUI.decodePage(false);

				//if scaling to window reset screen to fit rotated page
				//if(currentGUI.getSelectedComboIndex(Commands.SCALING)<3)
				//	currentGUI.zoom();

			}
		}else
			currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
	}

	public void gotoPage(String page) {
		int newPage;

		//allow for bum values
		try{
            newPage=Integer.parseInt(page);

			/**
             * adjust for double jump on facing
             */
            if(decode_pdf.getDisplayView() == Display.FACING || decode_pdf.getDisplayView() == Display.CONTINUOUS_FACING){
                if((newPage & 1)==1 && newPage!=1){
                    newPage--;
                }
            }


			//allow for invalid value
			if((newPage>decode_pdf.getPageCount())|(newPage<1)){
				
				currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPageLabel.text")+ " "+
						page+" "+Messages.getMessage("PdfViewerOutOfRange.text")+" "+decode_pdf.getPageCount());
				
				newPage=commonValues.getCurrentPage();



                currentGUI.setPageNumber();
			}

		}catch(Exception e){
			currentGUI.showMessageDialog(">"+page+ "< "+Messages.getMessage("PdfViewerInvalidNumber.text"));
			newPage=commonValues.getCurrentPage();
			currentGUI.pageCounter2.setText(""+commonValues.getCurrentPage());
		}

		//open new page
		if((!commonValues.isProcessing())&&(commonValues.getCurrentPage()!=newPage)){

				commonValues.setCurrentPage(newPage);
				currentGUI.decodePage(false);
				//currentGUI.zoom();
		}
	}
	
	
	private void open(final String file) {

        currentGUI.resetNavBar();
        

        boolean isURL = file.startsWith("http:");
    	try {
			
			if(!isURL){
				fileIsURL=false;
				commonValues.setFileSize(new File(file).length() >> 10);
			}else
				fileIsURL=true;
			
			commonValues.setSelectedFile(file);
			currentGUI.setViewerTitle(null);
			
		} catch (Exception e) {
			LogWriter.writeLog("Exception " + e + " getting paths");
		}
		
		/** check file exists */
		File testFile = new File(commonValues.getSelectedFile());
		if (!isURL && !testFile.exists()) {
			currentGUI.showMessageDialog(Messages.getMessage("PdfViewerFile.text") + commonValues.getSelectedFile() + Messages.getMessage("PdfViewerNotExist"));
		
			/** open the file*/
		} else if ((commonValues.getSelectedFile() != null) && (commonValues.isProcessing() == false)) {
			
            currentGUI.stopThumbnails();

            decode_pdf.flushObjectValues(true);
			
			//reset the viewableArea before opening a new file
			decode_pdf.resetViewableArea();
			/**/
            SwingWorker worker = new SwingWorker() {
				public Object construct() {
					if(!isOpening){
						isOpening=true;
						openFile(commonValues.getSelectedFile());
						isOpening=false;
					}
					return null;
				}
			};
			worker.start();

            /**
            while(1==1){

            SwingWorker worker = new SwingWorker() {
				public Object construct() {
					if(!isOpening){
						isOpening=true;
                        System.out.println("Open FIle>>>>>>>>>>>>>>>>>>>>>");
                        try {
                            decode_pdf.openPdfFile(commonValues.getSelectedFile());
                        } catch (PdfException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                        //openFile(commonValues.getSelectedFile());
                        System.out.println("Open FIle<<<<<<<<<<<<<<<<<<<<<");
                        isOpening=false;
                    }
					return null;
				}
			};
			worker.start();

            while( !decode_pdf.isOpen()){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

            //random sleep
            try {
                long sleep=(long)(Math.random()*100);
                System.out.println("sleep for "+sleep);
                    Thread.sleep( sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            System.out.println("1stop!!!");
            decode_pdf.stopDecoding();
            System.out.println("2stop!!!");

            //decode_pdf.closePdfFile();
            }/***/

        }
	}
	
	private void updateRecentDocuments(String[] recentDocs) {
		if(recentDocs == null)
			return;
		
		for(int i=0; i<recentDocs.length;i++){
			if(recentDocs[i] != null){
				String shortenedFileName = recent.getShortenedFileName(recentDocs[i]);
				
				recentDocuments[i].setText(i+1 + ": " + shortenedFileName);
				if(recentDocuments[i].getText().equals(i+1 + ": "))
					recentDocuments[i].setVisible(false);
				else
					recentDocuments[i].setVisible(true);
				recentDocuments[i].setName(recentDocs[i]);
			}
		}
	}
	
	
	protected void recentDocumentsOption(JMenu file) {
		String[] recentDocs=properties.getRecentDocuments();
		if(recentDocs == null)
			return;
		
		for(int i=0;i<noOfRecentDocs;i++){
			
			if(recentDocs[i]==null)
				recentDocs[i]="";
			
			String fileNameToAdd=recentDocs[i];
			String shortenedFileName = recent.getShortenedFileName(fileNameToAdd);
			
			recentDocuments[i] = new JMenuItem(i+1 + ": " + shortenedFileName);
			if(recentDocuments[i].getText().equals(i+1 + ": "))
				recentDocuments[i].setVisible(false);
			recentDocuments[i].setName(fileNameToAdd);
			recentDocuments[i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					
					if(currentPrinter.isPrinting())
						currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintWait.message"));
					else if(commonValues.isProcessing() || isOpening)
						currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
					else{
						/**
						 * warn user on forms
						 */
						handleUnsaveForms();

						JMenuItem item = (JMenuItem)e.getSource();
						String fileName = item.getName();
						
						if (!fileName.equals(""))
							open(fileName);
					}
				}
			});
			file.add(recentDocuments[i]);
		}
	}
	
	
	
	private void saveFile() {	
		
		/**
		 * create the file chooser to select the file
		 */
		File file=null;
		String fileToSave="";
		boolean finished=false;
		
		while(!finished){
			JFileChooser chooser = new JFileChooser(commonValues.getInputDir());
			chooser.setSelectedFile(new File(commonValues.getInputDir()+"/"+commonValues.getSelectedFile()));
			chooser.addChoosableFileFilter(new FileFilterer(new String[]{"pdf"}, "Pdf (*.pdf)"));
			chooser.addChoosableFileFilter(new FileFilterer(new String[]{"fdf"}, "fdf (*.fdf)"));
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
			//set default name to current file name 
			int approved=chooser.showSaveDialog(null);
			if(approved==JFileChooser.APPROVE_OPTION){
				
				FileInputStream fis=null;
				FileOutputStream fos=null;
				
				file = chooser.getSelectedFile();
				fileToSave=file.getAbsolutePath();
				
				if(!fileToSave.endsWith(".pdf")){
					fileToSave += ".pdf";
					file=new File(fileToSave);
				}
				
				if(fileToSave.equals(commonValues.getSelectedFile()))
					return;
				
				if(file.exists()){
					int n=currentGUI.showConfirmDialog(fileToSave+"\n" +
							Messages.getMessage("PdfViewerMessage.FileAlreadyExists")+"\n" +
							Messages.getMessage("PdfViewerMessage.ConfirmResave"),
							Messages.getMessage("PdfViewerMessage.Resave"),JOptionPane.YES_NO_OPTION);
					if(n==1)
						continue;
				}
				
				try {
					fis=new FileInputStream(commonValues.getSelectedFile());
					fos=new FileOutputStream(fileToSave);
					
					byte[] buffer=new byte[4096];
					int bytes_read;
					
					while((bytes_read=fis.read(buffer))!=-1)
						fos.write(buffer,0,bytes_read);
				} catch (Exception e1) {
					
					//e1.printStackTrace();
					currentGUI.showMessageDialog(Messages.getMessage("PdfViewerException.NotSaveInternetFile"));
				}
				
				try{
					fis.close();
					fos.close();
				} catch (Exception e2) {
					//e2.printStackTrace();
				}
				
				finished=true;
			}else{
				return;
			}
		}
	}
	
	/**Clean up and exit program*/
	private void exit() {

        thumbnails.terminateDrawing();
		
		/**
		 * warn user on forms
		 */
		handleUnsaveForms();
		
		/**
		 * create the dialog
		 */
		currentGUI.showConfirmDialog(new JLabel(Messages.getMessage("PdfViewerExiting")),
				Messages.getMessage("PdfViewerprogramExit"), 
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		
		/**cleanup*/
		decode_pdf.closePdfFile();
		
		flush();
		
		//@exit
		System.exit(1);
	}
	
	/**
	 * routine to remove all objects from temp store
	 */
	public final void flush() {
		
		String target=commonValues.getTarget();

        if(target!=null){
            //get contents

            File temp_files = new File(target);
            String[] file_list = temp_files.list();
            if (file_list != null) {
                for (int ii = 0; ii < file_list.length; ii++) {
                    File delete_file = new File(target + commonValues.getSeparator()+file_list[ii]);
                    delete_file.delete();
                }
            }

        }
	}
}
