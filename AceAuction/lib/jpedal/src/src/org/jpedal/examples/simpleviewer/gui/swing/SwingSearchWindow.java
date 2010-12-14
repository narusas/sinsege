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
  * SwingSearchWindow.java
  * ---------------
  *
  * Original Author:  Mark Stephens (mark@idrsolutions.com)
  * Contributor(s):
 *
 */
package org.jpedal.examples.simpleviewer.gui.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import org.jpedal.PdfDecoder;
import org.jpedal.examples.simpleviewer.Values;
import org.jpedal.examples.simpleviewer.gui.SwingGUI;
import org.jpedal.examples.simpleviewer.gui.generic.GUISearchWindow;
import org.jpedal.exception.PdfException;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.Messages;
import org.jpedal.utils.SwingWorker;

/**provides interactive search Window and search capabilities*/
public class SwingSearchWindow extends JFrame implements GUISearchWindow{


    /**flag to stop multiple listeners*/
    private boolean isSetup=false;

    String defaultMessage="Enter your text here";

    JTextField searchText=null;
    JCheckBox searchAll;
    JTextField searchCount;
    DefaultListModel listModel;
    SearchList results;

    MouseListener ML;
    ActionListener AL=null;
    WindowListener WL;
    KeyListener KL;

    /**swing thread to search in background*/
    SwingWorker searcher=null;

    /**flag to show searching taking place*/
    public boolean isSearch=false;

    JButton searchButton=null;

    /**number fo search items*/
    private int itemFoundCount=0;

    /**used when fiding text to highlight on page*/
    Map textPages=new HashMap();
    Map textRectangles=new HashMap();

    final JPanel nav=new JPanel();

    Values commonValues;
    SwingGUI currentGUI;
    PdfDecoder decode_pdf;

    /**deletes message when user starts typing*/
    private boolean deleteOnClick;

    public SwingSearchWindow(Values commonValues,SwingGUI currentGUI,PdfDecoder decode_pdf) {

        this.commonValues=commonValues;
        this.currentGUI=currentGUI;
        this.decode_pdf=decode_pdf;
    }

    /**
     * find text on page
     */
    public void find(){

        /**
         * pop up new window to search text (initialise if required
         */
        if(isSetup){ //global variable so do NOT reinitialise
            searchCount.setText(Messages.getMessage("PdfViewerSearch.ItemsFound")+" "+itemFoundCount);
            searchText.selectAll();
            searchText.grabFocus();
        }else{
            isSetup=true;


            defaultMessage=Messages.getMessage("PdfViewerSearchGUI.DefaultMessage");

            searchText=new JTextField(defaultMessage);

            searchButton=new JButton(Messages.getMessage("PdfViewerSearch.Button"));

            nav.setLayout(new BorderLayout());

            WL = new WindowListener(){
                public void windowOpened(WindowEvent arg0) {}

                //flush objects on close
                public void windowClosing(WindowEvent arg0) {

                    removeSearchWindow(true);
                }

                public void windowClosed(WindowEvent arg0) {}

                public void windowIconified(WindowEvent arg0) {}

                public void windowDeiconified(WindowEvent arg0) {}

                public void windowActivated(WindowEvent arg0) {}

                public void windowDeactivated(WindowEvent arg0) {}
            };

            this.addWindowListener(WL);

            nav.add(searchButton,BorderLayout.EAST);

            nav.add(searchText,BorderLayout.CENTER);

            searchAll=new JCheckBox();
            searchAll.setSelected(true);
            searchAll.setText(Messages.getMessage("PdfViewerSearch.CheckBox"));
            nav.add(searchAll,BorderLayout.NORTH);

            itemFoundCount=0;
            textPages.clear();
            textRectangles.clear();
            listModel = null;

            searchCount=new JTextField(Messages.getMessage("PdfViewerSearch.ItemsFound")+" "+itemFoundCount);
            searchCount.setEditable(false);
            nav.add(searchCount,BorderLayout.SOUTH);

            listModel = new DefaultListModel();
            results=new SearchList(listModel,textPages);

            results.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

            ML = new MouseListener(){
                public void mouseClicked(MouseEvent arg0) {

                    if(!commonValues.isProcessing()){//{if (!event.getValueIsAdjusting()) {

                        float scaling=currentGUI.getScaling();
                        int inset=currentGUI.getPDFDisplayInset();

                        int id=results.getSelectedIndex();

                        decode_pdf.setFoundTextAreas(null);

                        if(id!=-1){

                            Integer key=new Integer(id);
                            Object newPage=textPages.get(key);

                            if(newPage!=null){
                                int nextPage=((Integer)newPage).intValue();
                                Rectangle highlight=(Rectangle) textRectangles.get(key);

                                //move to new page
                                if(commonValues.getCurrentPage()!=nextPage){
                                    commonValues.setCurrentPage(nextPage);

                                    currentGUI.resetStatusMessage(Messages.getMessage("PdfViewer.LoadingPage")+" "+commonValues.getCurrentPage());

                                    /**reset as rotation may change!*/
                                    decode_pdf.setPageParameters(scaling, commonValues.getCurrentPage());

                                    //decode the page
                                    currentGUI.decodePage(false);

                                    decode_pdf.invalidate();
                                }

                                //draw rectangle
                                int scrollInterval = decode_pdf.getScrollInterval();
                                //previous one to revert back to but other more accurate
//							decode_pdf.scrollRectToVisible(new Rectangle((int)((highlight.x*scaling)+scrollInterval),(int)(mediaGUI.cropH-((highlight.y-currentGUI.cropY)*scaling)-scrollInterval*2),scrollInterval*4,scrollInterval*6));

                                int x = (int)((highlight.x-currentGUI.cropX)*scaling)+inset;
                                int y = (int)((currentGUI.cropH-(highlight.y-currentGUI.cropY))*scaling)+inset;
                                int w = (int)(highlight.width*scaling);
                                int h = (int)(highlight.height*scaling);

                                Rectangle scrollto = new Rectangle(x-scrollInterval,y-h-scrollInterval,w+scrollInterval*2,h+scrollInterval*2);
                                decode_pdf.scrollRectToVisible(scrollto);
                                decode_pdf.setFoundTextArea(highlight);

                                decode_pdf.invalidate();
                                decode_pdf.repaint();

                            }
                        }
                    }
                }

                public void mousePressed(MouseEvent arg0) {}

                public void mouseReleased(MouseEvent arg0) {}

                public void mouseEntered(MouseEvent arg0) {}

                public void mouseExited(MouseEvent arg0) {}
            };

            results.addMouseListener(ML);

            //setup searching
            //if(AL==null){
            AL = new ActionListener(){
                public void actionPerformed(ActionEvent e) {

                    if(!isSearch){

                        try {
                            searchText();
                        } catch (Exception e1) {
                            System.out.println("Exception "+e1);
                            e1.printStackTrace();
                        }
                    }else{
                        searcher.interrupt();
                        isSearch=false;
                        searchButton.setText(Messages.getMessage("PdfViewerSearch.Button"));
                    }
                }
            };

            searchButton.addActionListener(AL);
            //}

            searchText.selectAll();
            deleteOnClick=true;

            KL = new KeyListener(){
                public void keyTyped(KeyEvent e) {

                    //clear when user types
                    if(deleteOnClick){
                        deleteOnClick=false;
                        searchText.setText("");
                    }
                    int id = e.getID();
                    if (id == KeyEvent.KEY_TYPED) {
                        char key=e.getKeyChar();
                        if(key=='\n'){
                            try {
                                searchText();
                            } catch (Exception e1) {
                                System.out.println("Exception "+e1);
                                e1.printStackTrace();
                            }
                        }
                    }
                }

                public void keyPressed(KeyEvent arg0) {}

                public void keyReleased(KeyEvent arg0) {}
            };

            searchText.addKeyListener(KL);

            //build frame
            JScrollPane scrollPane=new JScrollPane();
            scrollPane.getViewport().add(results);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.getVerticalScrollBar().setUnitIncrement(80);
            scrollPane.getHorizontalScrollBar().setUnitIncrement(80);

            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(scrollPane,BorderLayout.CENTER);
            getContentPane().add(nav,BorderLayout.NORTH);

            //position and size
            Container frame;
            if(commonValues.getModeOfOperation() == Values.RUNNING_APPLET){
                frame = currentGUI.getFrame().getContentPane();
            }else{
                frame = currentGUI.getFrame();
            }

            int w=230;

            int h=frame.getHeight();
            int x1=frame.getLocationOnScreen().x;
            int x=frame.getWidth()+x1;
            int y=frame.getLocationOnScreen().y;
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

            int width = d.width;
            if(x+w>width){
                x=width-w;
                frame.setSize(x-x1,frame.getHeight());
            }

            setSize(w,h);
            setLocation(x,y);
            //<start-13>
            searchAll.setFocusable(false);
            //<end-13>
            searchText.grabFocus();
        }
        show();

    }

    public void removeSearchWindow(boolean justHide) {

        //System.out.println("remove search window");

        hide();

        setVisible(false);

        if(searcher!=null)
            searcher.interrupt();

        if(isSetup && !justHide){
            if(listModel!=null)
                listModel.clear();//removeAllElements();

            //searchText.setText(defaultMessage);
            //searchAll=null;
            //if(nav!=null)
            //    nav.removeAll();

            itemFoundCount=0;
            isSearch=false;

        }

    }

    private void searchText() throws Exception {

        /** if running terminate first */
        if ( (searcher != null))
            searcher.interrupt();

        searchButton.setText(Messages.getMessage("PdfViewerSearchButton.Stop"));
        searchButton.invalidate();
        searchButton.repaint();
        isSearch=true;

        searchCount.setText(Messages.getMessage("PdfViewerSearch.Scanning1"));
        searchCount.repaint();

        searcher = new SwingWorker() {

            public Object construct() {


                try {

                    listModel.removeAllElements();
                    results.repaint();

                    int listCount=0;
                    textPages.clear();

                    textRectangles.clear();
                    itemFoundCount=0;
                    decode_pdf.setFoundTextAreas(null);

                    //get text
                    String textToFind=searchText.getText();

                    //get page sizes
                    PdfPageData pageSize=decode_pdf.getPdfPageData();
                    int x1,y1,x2,y2;

                    //page range
                    int startPage=1;
                    int endPage=commonValues.getPageCount()+1;
                    if(!searchAll.isSelected()){
                        startPage=commonValues.getCurrentPage();
                        endPage=startPage+1;
                    }

                    //search all pages
                    for(int i=startPage;i<endPage;i++){

                        if (Thread.interrupted())
                            throw new InterruptedException();

                        /** common extraction code */
                        PdfGroupingAlgorithms currentGrouping = null;

                        /** create a grouping object to apply grouping to data */
                        try {

                            if(i==commonValues.getCurrentPage())
                                currentGrouping =decode_pdf.getGroupingObject();
                            else{
                                decode_pdf.decodePageInBackground(i);
                                currentGrouping =decode_pdf.getBackgroundGroupingObject();
                            }

                            //tell JPedal we want teasers
                            currentGrouping.generateTeasers();

                            //set size
                            x1=pageSize.getMediaBoxX(i);
                            x2=pageSize.getMediaBoxWidth(i);
                            y1=pageSize.getMediaBoxY(i);
                            y2=pageSize.getMediaBoxHeight(i);

                            float[] co_ords=currentGrouping.findTextInRectangle(x1,y2,x2+x1,y1,i,textToFind,false,true);

                            //other pair of points so we can highlight
                            float[] endPoints=currentGrouping.getEndPoints();

                            //	other pair of points so we can highlight
                            final String[] teasers=currentGrouping.getTeasers();

                            if (Thread.interrupted())
                                throw new InterruptedException();

                            if((co_ords!=null)&&(teasers!=null)){

                                //update count display
                                itemFoundCount=itemFoundCount+teasers.length;

                                //store details so we can lookup
                                int count=co_ords.length,next=0;

                                for(int ii=0;ii<count;ii=ii+2){

                                    int wx1=(int)co_ords[ii];
                                    int wy1=(int)co_ords[ii+1];
                                    int wx2=(int)endPoints[ii];
                                    int wy2=(int)endPoints[ii+1];

                                    final String tease=teasers[ii/2];

                                    Runnable setTextRun = new Runnable() {
                                        public void run() {
                                            listModel.addElement(tease);
                                        }
                                    };
                                    SwingUtilities.invokeAndWait(setTextRun);

                                    Integer key=new Integer(listCount);
                                    listCount++;
                                    textRectangles.put(key,new Rectangle(wx1,wy2,wx2-wx1,wy1-wy2));
                                    textPages.put(key,new Integer(i));

                                    // System.out.println("Added "+key+" i="+i+" "+teasers[ii/2]);

                                    next++;
                                }
                            }

                            //new value or 16 pages elapsed
                            if((co_ords!=null)|((i % 16) ==0)){
                                searchCount.setText(Messages.getMessage("PdfViewerSearch.ItemsFound")+" "+
                                        itemFoundCount+" "+ Messages.getMessage("PdfViewerSearch.Scanning")+i);
                                searchCount.invalidate();
                                searchCount.repaint();
                            }

                        } catch (PdfException e1) {
                        }
                    }

                    searchCount.setText(Messages.getMessage("PdfViewerSearch.ItemsFound")+" "
                            +itemFoundCount+"  "+Messages.getMessage("PdfViewerSearch.Done"));
                    results.invalidate();
                    results.repaint();

                    //reset search button
                    isSearch=false;
                    searchButton.setText(Messages.getMessage("PdfViewerSearch.Button"));


                } catch (Exception e) {
                }

                return null;
            }
        };

        searcher.start();
    }

    public void grabFocusInInput() {
        searchText.grabFocus();

    }

    public boolean isSearchVisible() {

        return this.isVisible();
    }
}
