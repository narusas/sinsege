/**
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
 * CustomMouseListener.java
 * ---------------
 * (C) Copyright 2006, by IDRsolutions and Contributors.
 *
 * 
 * --------------------------
 */
package org.jpedal.objects.acroforms.listeners;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.text.JTextComponent;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.ConvertToString;
import org.jpedal.utils.BrowserLauncher;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;


/**
 * class implements MouseListener to create all required actions for the associated button
 * 
 * @author chris
 */
public class CustomMouseListener implements MouseListener{
	
	private static final boolean debugMouseActions = false;
	private static final boolean debugUnimplemented = false;
	private static final boolean debugXFAActions = false;
	
	/**
	 * used to setup any actions for the forms
	 */
	private String command = "";
	private Map enteredAction = null;
	private Map exitedAction = null;
	private Map hideAction = null;
	private Map clickedAction = null;
	private Map captionChanger = null;
	
	/** javascript commands and action */
	private int javascriptWhen;
	private String javascriptCommand;
	private String javascriptArgs;
	
	/** the url to submit data to*/
	private String submitURL = null;
	
	/** holds link to the acro renderer for access to components */
	private AcroRenderer acrorend;
	
	/** submitForm map */
	private Map aDataMap;
	
	/** takes in a command to define what it does on the Events */
	public CustomMouseListener(String inCommand, AcroRenderer acrorenderer){
        command = inCommand;
        acrorend = acrorenderer;
    }
    
    /** takes a map, that specifies what to do on a specfic action
     * <br>the type must be lowercase 
     */
    public CustomMouseListener(Map actionMap,String type, AcroRenderer acrorenderer){
        if(type.equals("entered"))
            enteredAction = actionMap;
        else if(type.equals("exited"))
            exitedAction = actionMap;
        else if(type.equals("Hide"))
            hideAction = actionMap;
        else if(type.equals("Clicked"))
        	clickedAction = actionMap;
        
        acrorend = acrorenderer;
    }
	
	/**
	 * setup submitForm action specified in aDataMap and data need to be got from 
	 * all components in compsToTest array
	 */
	public CustomMouseListener(Map dataMap, AcroRenderer acrorenderer) {
//		ignore "S" as is Submitform
		
		aDataMap = dataMap;
		acrorend = acrorenderer;
		
    	Object obj = aDataMap.get("F");
    	if(obj instanceof Map){
    		submitURL = (String)((Map)obj).get("F");
    		if(submitURL.startsWith("(")){
    			submitURL = submitURL.substring(1,submitURL.length()-1);
    		}
    	}else {
    		submitURL = (String)obj;
    		if(submitURL.startsWith("(")){
    			submitURL = submitURL.substring(1,submitURL.length()-1);
    		}
    	}
    	
    	//Flags see pdf spec v1.6 p662 
//    	System.out.println("Flags="+aDataMap.get("Flags"));
	}

	/**
	 * sets up the captions to change when needed
	 */
	public CustomMouseListener(String normalCaption,String rolloverCaption,String downCaption) {
		//set up the captions to work for rollover and down presses of the mouse
		captionChanger = new HashMap();
		if(rolloverCaption!=null)
			captionChanger.put("rollover",rolloverCaption);
		if(downCaption!=null)
			captionChanger.put("down",downCaption);
		captionChanger.put("normal",normalCaption);
	}
	
	/**
	 * sets up this mouse listener to apply the assigned action
	 */
	public CustomMouseListener(int activity,String scriptType,String script,AcroRenderer acro) {
		if(debugXFAActions)
			System.out.println("setup mouse="+activity+","+scriptType+","+script);
		
		if(script.indexOf("resetData")!=-1){
			command = "ResetForm";
			acrorend = acro;
		}else if(scriptType!=null){
			if(scriptType.indexOf("javascript")!=-1){
				int index = script.indexOf("(");
				javascriptCommand = script.substring(0,index);
				javascriptArgs = script.substring(index+1,script.indexOf(")"));
				javascriptWhen = activity;
			}else if(scriptType.indexOf("submit")!=-1){
			}
		}else {
		}
//		switch(activity) {
//		case XFAFormObject.ACTION_MOUSECLICK:
//		case XFAFormObject.ACTION_MOUSEENTER:
//		case XFAFormObject.ACTION_MOUSEEXIT:
//		case XFAFormObject.ACTION_MOUSEPRESS:
//		case XFAFormObject.ACTION_MOUSERELEASE:
	}

	public void mouseEntered(MouseEvent e) {
		if(debugMouseActions)
			System.out.println("customMouseListener.mouseEntered()");
		
		if(e.getSource() instanceof AbstractButton && captionChanger!=null){
			if(captionChanger.containsKey("rollover")){
				((AbstractButton) e.getSource()).setText((String) captionChanger.get("rollover"));
			}
		}
		
		if(command.equals("togglenoview")){
		    ((JComponent) e.getSource()).setVisible(true);
		    ((JComponent) e.getSource()).repaint();
		}else if(command.equals("comboEntry")){
			((JComboBox) e.getSource()).showPopup();
		}
		
		if(enteredAction!=null){
		    String command = (String) enteredAction.get("command");
			if(command.equals("/Hide")){
				String name = (String)enteredAction.get("fields");
				int start = 0;
				if(name.startsWith("("))
					start++;
				name = name.substring(start,name.length()-start);
				
	            Component[] checkObj = acrorend.getComponentsByName(name);
	            
	            if(checkObj!=null){
					boolean hide = ((Boolean)enteredAction.get("hide")).booleanValue();
					checkObj[0].setVisible(!hide);
					checkObj[0].repaint();
	            }
			}else{
			}
		}
		
		 if(clickedAction!=null && clickedAction.containsKey("URL")){
			 ((Component) e.getSource()).setCursor(new Cursor(Cursor.HAND_CURSOR));
	 	}
		 
	}
	
	public void mouseExited(MouseEvent e) {
		if(debugMouseActions)
			System.out.println("customMouseListener.mouseExited()");
		
		if(e.getSource() instanceof AbstractButton && captionChanger!=null){
			if(captionChanger.containsKey("normal")){
				((AbstractButton) e.getSource()).setText((String) captionChanger.get("normal"));
			}
		}
		
	    if(command.equals("togglenoview")){
	        ((JComponent) e.getSource()).setVisible(false);
	        ((JComponent) e.getSource()).repaint();
	    }else if(command.equals("comboEntry")){
			((JComboBox) e.getSource()).hidePopup();
		}
	    
	    if(exitedAction!=null){
		    String command = (String) exitedAction.get("command");
			if(command.equals("/Hide")){
				String name = (String)exitedAction.get("fields");
				int start = 0;
				if(name.startsWith("("))
					start++;
				name = name.substring(start,name.length()-start);
				
	            Component[] checkObj = acrorend.getComponentsByName(name);
	            
	            if(checkObj!=null){
					boolean hide = ((Boolean)exitedAction.get("hide")).booleanValue();
					checkObj[0].setVisible(!hide);
					checkObj[0].repaint();
	            }
			}else{
			}
		}
	    
	    if(clickedAction!=null && clickedAction.containsKey("URL")){
	    	((Component) e.getSource()).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	    
	}
	
	public void mouseClicked(MouseEvent e) {
		if(debugMouseActions)
			System.out.println("customMouseListener.mouseClicked()");
		
		if(clickedAction!=null){
	    	if(clickedAction.containsKey("Popup")){
	    		if(e.getClickCount()==2){
	    		}
	    	}else if(clickedAction.containsKey("URL")){
				try {
					BrowserLauncher.openURL(((String)clickedAction.get("URL")).substring(1,((String)clickedAction.get("URL")).length()-1));
				} catch (IOException e1) {
					//TODO make all dialogs parent the viewer
					JOptionPane.showMessageDialog(null,Messages.getMessage("PdfViewer.ErrorWebsite"));
				}
	    	}else if(clickedAction.containsKey("GoToR")){
	    		Map gotomap = (Map) clickedAction.get("GoToR");
				
            	String page = (String)gotomap.get("page");
            	String type = (String)gotomap.get("type");
            	String file = (String)gotomap.get("file");
            	
            	if(type.equals("/Filespec")){
	            	if(new File(file).exists()){
	            		//Open this file, on page 'page'
	            		
	            		
	            		LogWriter.writeFormLog("{CustomMouseListener.mouseClicked} Form has GoToR command, needs methods for opening new file on page specified",debugUnimplemented);
	            	}else {
	            		JOptionPane.showMessageDialog(e.getComponent().getParent(),"The file specified "+file+" Does Not Exist!");
	            	}
            	}else {
            		LogWriter.writeFormLog("{CustomMouseListener.mouseClicked} GoToRemote NON Filespec NOT IMPLEMENTED",debugUnimplemented);
            	}
            	
//				((JComponent)currentComp).setToolTipText(text);
            	
			}else if(clickedAction.containsKey("Dest")){
				Map destMap = (Map)clickedAction.get("Dest");
				
				//change to page
				destMap.get("Page");
				//to location defined by
				destMap.get("Position");
	    		
				LogWriter.writeFormLog("{CustomMouseListener.mouseClicked} Dest NOT IMPLEMENTED needs call to change page, and scroll to position",debugUnimplemented);
			}else {
	    	}
	   	}
		
		if(submitURL!=null){
			Component[] compsToSubmit = new Component[0];
			if(aDataMap.containsKey("Fields")){
		    	StringTokenizer fieldsTok = new StringTokenizer((String) aDataMap.get("Fields"),",");
		    	String[] includeNameList = new String[0];
				String[] listOfNames = new String[fieldsTok.countTokens()];
				int i=0;
				while(fieldsTok.hasMoreTokens()){
		    		listOfNames[i++] = fieldsTok.nextToken();
				}
				
	//	    	Flags see pdf spec v1.6 p662
				Object obj = aDataMap.get("Flags");
				int value=0;
				if(obj instanceof String){
					value = Integer.parseInt((String)obj);
				}else if(obj instanceof Integer){
					value = ((Integer)obj).intValue();
				}else {
					LogWriter.writeFormLog("(internal only) flags NON Sting = "+obj.getClass()+" "+obj,debugUnimplemented);
				}
				
		    	if((value&1)==1){
		    		//fields is an exclude list
					try {
						List tmplist = acrorend.getComponentNameList();
	//					System.out.println("before="+ConvertToString.convertArrayToString(tmplist.toArray()));
						if(tmplist!=null){
							for(i=0;i<listOfNames.length;i++){
								tmplist.remove(listOfNames[i]);
							}
	//						System.out.println("after="+ConvertToString.convertArrayToString(tmplist.toArray()));
						}
					} catch (PdfException e1) {
						LogWriter.writeFormLog("DefaultFormFactory.setupMouseListener() get component name list exception",debugUnimplemented);
					}
		    	}else {
		    		//fields is an include list
					includeNameList = listOfNames;
		    	}
		    	
		    	Component[] compsToAdd,tmp;
		    	for(i=0;i<includeNameList.length;i++){
		    		compsToAdd = acrorend.getComponentsByName(includeNameList[i]);
		    		tmp = new Component[compsToSubmit.length+compsToAdd.length];
		    		if(compsToAdd.length>1){
		   				LogWriter.writeFormLog("(internal only) SubmitForm multipul components with same name",debugUnimplemented);
		    		}
		    		for(i=0;i<tmp.length;i++){
		    			if(i<compsToSubmit.length){
		    				tmp[i] = compsToSubmit[i];
		    			}else if(i-compsToSubmit.length<compsToAdd.length){
		    				tmp[i] = compsToAdd[i-compsToSubmit.length];
		    			}
		    		}
		    		compsToSubmit = tmp;
		    	}
			}else {
				compsToSubmit = acrorend.getComponentsByName(null);
			}
	    	
			String text = "";
	    	for(int i=0;i<compsToSubmit.length;i++){
	    		if(compsToSubmit[i] instanceof JTextComponent){
	    			text += ((JTextComponent) compsToSubmit[i]).getText();
	    		}else if(compsToSubmit[i] instanceof AbstractButton){
	    			text += ((AbstractButton) compsToSubmit[i]).getText();
	    		}else {
    				LogWriter.writeFormLog("(internal only) SubmitForm field form type not accounted for",debugUnimplemented);
	    		}
	    	}
	    	
	    	try {
				BrowserLauncher.openURL(submitURL+"?en&q="+text);
			} catch (IOException e1) {
				//TODO make all dialogs parent the viewer
				JOptionPane.showMessageDialog(null,Messages.getMessage("PdfViewer.ErrorWebsite"));
				e1.printStackTrace();
			}
		}
		
	}
	
	public void mousePressed(MouseEvent e) {
		if(debugMouseActions)
			System.out.println("customMouseListener.mousePressed()");
		
		if(e.getSource() instanceof AbstractButton && captionChanger!=null){
			if(captionChanger.containsKey("down")){
				((AbstractButton) e.getSource()).setText((String) captionChanger.get("down"));
			}
		}
		
		if(command.equals("ResetForm")){
			String[] defaultValues = acrorend.getDefaultValues();
			Component[] allFields = acrorend.getComponentsByName(null);
			
			for(int i=0;i<allFields.length;i++){
				if(allFields[i]!=null){// && defaultValues[i]!=null){
					
					if(allFields[i] instanceof AbstractButton){
						if(allFields[i] instanceof JCheckBox){
							//setSelectedItem(item)
							if(defaultValues[i]==null){
								((JCheckBox)allFields[i]).setSelected(false);
							}else {
								String fieldState = ((JCheckBox)allFields[i]).getName();
								int ptr=fieldState.indexOf("-(");
								/** NOTE if indexOf string changes change ptr+# to same length */
								if(ptr!=-1){
									fieldState = fieldState.substring(ptr+2,fieldState.length()-1);
								}
								
								if(fieldState.equals(defaultValues[i]))
									((JCheckBox)allFields[i]).setSelected(true);
								else
									((JCheckBox)allFields[i]).setSelected(false);
								
								LogWriter.writeFormLog("{renderer} resetform on mouse press "+allFields[i].getClass()+" - "+defaultValues[i]+" current="+((JCheckBox)allFields[i]).isSelected()+" "+((JCheckBox)allFields[i]).getText(),debugUnimplemented);
							}
							
						}else if(allFields[i] instanceof JButton){
							// ?
							LogWriter.writeFormLog("{renderer{ resetform on mouse press "+allFields[i].getClass()+" - "+defaultValues[i]+" current="+((JButton)allFields[i]).isSelected()+" "+((JButton)allFields[i]).getText(),debugUnimplemented);
							
						}else if(allFields[i] instanceof JRadioButton){
							//on/off
							if(defaultValues[i]==null){
								((JRadioButton)allFields[i]).setSelected(false);
							}else {
								String fieldState = ((JRadioButton)allFields[i]).getName();
								
								int ptr=fieldState.indexOf("-(");
								/** NOTE if indexOf string changes change ptr+# to same length */
								if(ptr!=-1){
									fieldState = fieldState.substring(ptr+2,fieldState.length()-1);
								}
								
								if(fieldState.equals(defaultValues[i]))
									((JRadioButton)allFields[i]).setSelected(true);
								else 
									((JRadioButton)allFields[i]).setSelected(false);
								
							}
						}
					}else if(allFields[i] instanceof JTextComponent){
						//text
						((JTextComponent)allFields[i]).setText(defaultValues[i]);
						
					}else if(allFields[i] instanceof JComboBox){
						// on/off
						((JComboBox)allFields[i]).setSelectedItem(defaultValues[i]);
						
					}else if(allFields[i] instanceof JList){
						((JList)allFields[i]).setSelectedValue(defaultValues[i],true);
					}
					allFields[i].repaint();
					
				}
			}
		}else if(command.equals("comboEntry")){
//			((JComboBox) e.getSource()).showPopup();
		}else if(command!=""){
		}
		
		if(hideAction!=null){
			
			String[] fieldsToHide = (String[])hideAction.get("fields");
			Boolean[] whetherToHide = (Boolean[])hideAction.get("hide");
			
			if(fieldsToHide.length!=whetherToHide.length){
				LogWriter.writeFormLog("{custommouselistener} number of fields and nuber of hides or not the same",debugUnimplemented);
				return;
			}
			
			for(int i=0;i<fieldsToHide.length;i++){
				Component[] checkObj = acrorend.getComponentsByName(fieldsToHide[i]);
				if(checkObj!=null){
					for(int j=0;j<checkObj.length;j++){
						checkObj[j].setVisible(!whetherToHide[i].booleanValue());
					}
				}
			}
		}
		
	}
	
	public void mouseReleased(MouseEvent e) {
		if(debugMouseActions)
			System.out.println("customMouseListener.mouseReleased()");
		
		if(e.getSource() instanceof AbstractButton && captionChanger!=null){
			if(captionChanger.containsKey("rollover")){
				((AbstractButton) e.getSource()).setText((String) captionChanger.get("rollover"));
			}else {
				((AbstractButton) e.getSource()).setText((String) captionChanger.get("normal"));
			}
		}
		
		if(command.equals("Print")){
			
	        if(e.getComponent().getParent() instanceof PdfDecoder){
	            PdfDecoder decode_pdf = (PdfDecoder)e.getComponent().getParent();
	            
	            //ask if user ok with printing and print if yes
	           if(JOptionPane.showConfirmDialog(null,Messages.getMessage("PdfViewerPrinting.ConfirmPrint"),Messages.getMessage("PdfViewerPrint.Printing"),JOptionPane.YES_NO_OPTION)==0){  
	           
		            //setup print job and objects
		            PrinterJob printJob = PrinterJob.getPrinterJob();
		            PageFormat pf = printJob.defaultPage();
		            
		            // Set PageOrientation to best use page layout
		            int orientation = decode_pdf.getPDFWidth() < decode_pdf
		            .getPDFHeight() ? PageFormat.PORTRAIT
		                    : PageFormat.LANDSCAPE;
		            pf.setOrientation(orientation);
		            
		            Paper paper = new Paper();
		            paper.setSize(595, 842);
		            paper.setImageableArea(43, 43, 509, 756);
		            
		            pf.setPaper(paper);
		//          allow user to edit settings and select printing
		            printJob.setPrintable(decode_pdf, pf);
		            try {
	                    printJob.print();
	                } catch (PrinterException e1) {
	                }
	            }
	        }else {
	        }
		}else if(command.equals("comboEntry")){
//			((JComboBox) e.getSource()).showPopup();
	    }else if(command.equals("ResetForm")){
//			ignore as is delt with in mousePressed()
	    }else if(command!=""){
	    }
		
	}
}
