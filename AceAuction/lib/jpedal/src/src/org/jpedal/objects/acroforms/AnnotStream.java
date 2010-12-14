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
 * AnnotStream.java
 * ---------------
 * (C) Copyright 2005, by IDRsolutions and Contributors.
 *
 * 
 * --------------------------
 */
package org.jpedal.objects.acroforms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JTextField;

import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.PdfShape;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Strip;

public class AnnotStream extends FormStream {
	
	private int pageHeight;
	private int insetH;

	protected AnnotStream(){}
	
	public AnnotStream(PdfObjectReader inCurrentPdfFile) {
		
		currentPdfFile = inCurrentPdfFile;
		
		type=DefaultAcroRenderer.ANNOTATION;
		
		init();
		
	}
	
	public void setPageHeightAndInset(int pageHeight, int insetH){
		this.pageHeight=pageHeight;
		this.insetH=insetH;
	}
	
	/**
     * parses over the stream decoding any values that don't 
     */
	protected void parseStream(FormObject formObject) {
		
	    //explicitly set some values
	    formObject.setBorder(null);
	    
	    //remove value "P" as is parent Object
	    currentField.remove("P");
	    
	    boolean notFound = false;
	    
	    Object subtype = currentPdfFile.resolveToMapOrString("Subtype",currentField.get("Subtype"));
	    if(subtype==null){
	    	return;
	    }else {
	    	if(!((String)subtype).startsWith("/")){
	    		subtype = "/"+subtype;
	    	}
	    }
	    
	    if(subtype.equals("/Text")){
	    	if(debug)
	    		System.out.println("text field encountered");
	    	
	    	formObject.setCharacteristic(4);//nozoom
    		formObject.setCharacteristic(5);//norotate
    		
    		formObject.setType(FormObject.ANNOTTEXT);
    		
	    	//order the commands so they have any fields decoded first
    		createOrderedCommandArray();
    		
    		for(int i=0;i<commands.length;i++){
    			String curCommand = commands[i];
    			Object curField = currentPdfFile.resolveToMapOrString(curCommand,currentField.get(curCommand));
		        
				if(curCommand.equals("Type")){
		        	if(curField.equals("/Annot")){
		        		//OK ignore
		        	}else {
		        	}
		        }else if(curCommand.equals("Subtype")){
		        	//ignore as is /Text
		        }else if(curCommand.equals("Rect")){
		        	createBoundsRectangle((String)curField,formObject);
		        }else if(curCommand.equals("AP")){
		        	boolean apSet = commandAP((Map)curField,formObject);
		        	if(!apSet){
		        	}
		        }else if(curCommand.equals("C")){
		        	boolean cSet = commandC(formObject, curField);
		        	if(!cSet){
		        	}
		        }else if(curCommand.equals("Contents")){
		        	boolean contentsSet = commandContents(formObject,curField);
		        	if(!contentsSet){
		        	}
		        }else if(curCommand.equals("Popup")){
		        	commandPopup(curField,formObject);
		        }else if(curCommand.equals("Open")){
		        	formObject.setOpenState(Boolean.valueOf((String) curField).booleanValue());
		        }else if(curCommand.equals("Name")){
	//		        	System.out.println("Name of icon type="+currentField.getxx("Name"));
		        }else if(curCommand.equals("M")){
		        	//ignore as is the date and time this annotation was last modified
		        }else if(curCommand.equals("CreationDate")){
		        	//ignore as is the date and time this annotation was created
		        }else if(curCommand.equals("RC")){
		        	commandRC(curField,formObject);
		        }else if(curCommand.equals("F")){
		        	workOutCharachteristic((String)curField,formObject);
		        }else if(curCommand.equals("T")){
		        	formObject.setPopupTitle((String)curField);
		        }else if(curCommand.equals("NM")){
		        	commandNM(formObject, curField);
		        }else if(curCommand.equals("Subj")){
		        	//ignore if textual representation of annotation, for use with disabilities
		        	if(debugUnimplemented)
		        		System.out.println("Subj FOR USE WITH DISABILITIES AnnotStream.parseStream() field="+curField);
		        }else if(curCommand.equals("PageNumber")){
		        	//ignore as MARK dealt with elsewhere
		        	formObject.setPageNumber(curField);
		        }else if(curCommand.equals("BS")){
		        	formObject.setBorder(curField);
		        }else {
		        }
    		}
    		
    		//System.out.println("ignoring State="+currentField.getxx("State")+" and StateModel="+currentField.getxx("StateModel"));
    		
    	}else if(subtype.equals("/Popup")){
//    		System.out.println("popup field="+ConvertToString.convertMapToString(currentField));
    		
    	}else if(subtype.equals("/Ink")){
    		
    		formObject.setType(FormObject.ANNOTINK);
    		
    		//order the commands so they have any fields decoded first
    		createOrderedCommandArray();
    		
    		for(int i=0;i<commands.length;i++){
    			String curCommand = commands[i];
    			Object curField = currentPdfFile.resolveToMapOrString(curCommand,currentField.get(curCommand));
		        
		        if(curCommand.equals("Type")){
		        	if(curField.equals("/Annot")){
		        		//OK ignore
		        	}else {
		        	}
		        }else if(curCommand.equals("Subtype")){
		        	//ignore as is /Ink
		        }else if(curCommand.equals("Rect")){
		        	createBoundsRectangle((String)curField,formObject);
		        }else if(curCommand.equals("AP")){
		        	if(!formObject.hasNormalOff()){
			        	//check normal off image not already created by Inklist,
			        	//to stop AP command being called twice
			        	boolean apSet = commandAP((Map)curField,formObject);
			        	if(!apSet){
			        	}
		        	}
		        }else if(curCommand.equals("C")){
		        	boolean cSet = commandC(formObject, curField);
		        	if(!cSet){
		        	}
		        }else if(curCommand.equals("F")){
		        	workOutCharachteristic((String)curField,formObject);
		        }else if(curCommand.equals("T")){
		        	formObject.setPopupTitle((String)curField);
		        }else if(curCommand.equals("M")){
		        	//ignore as is the date and time this annotation was last modified
		        }else if(curCommand.equals("CreationDate")){
		        	//ignore as is the date and time this annotation was created
		        }else if(curCommand.equals("NM")){
		        	commandNM(formObject, curField);
		        }else if(curCommand.equals("Subj")){
		        	//ignore if textual representation of annotation, for use with disabilities
		        	if(debugUnimplemented)
		        		System.out.println("Subj FOR USE WITH DISABILITIES AnnotStream.parseStream() field="+curField);
		        }else if(curCommand.equals("PageNumber")){
		        	//ignore as MARK dealt with elsewhere
		        	formObject.setPageNumber(curField);
		        }else if(curCommand.equals("InkList")){
		        	//if there is already an appearance image saved don't create a new one
		        	if(currentField.containsKey("AP"))
		        		commandAP((Map)currentPdfFile.resolveToMapOrString("AP",currentField.get("AP")),formObject);
		        	if(!formObject.hasNormalOff())
		        		commandInkList(curField,formObject);
		        }else if(curCommand.equals("Popup")){
		        	commandPopup(curField,formObject);
		        }else {
		        }
		    }
		    
    	}else if(subtype.equals("/Square")){
    		
    		formObject.setType(FormObject.ANNOTSQUARE);
    		
    		//order the commands so they have any fields decoded first
    		createOrderedCommandArray();
    		
    		for(int i=0;i<commands.length;i++){
    			String curCommand = commands[i];
    			Object curField = currentPdfFile.resolveToMapOrString(curCommand,currentField.get(curCommand));
		        
		        if(curCommand.equals("Type")){
		        	if(curField.equals("/Annot")){
		        		//OK ignore
		        	}else {
		        	}
		        }else if(curCommand.equals("Subtype")){
		        	//ignore as is /Ink
		        }else if(curCommand.equals("Rect")){
		        	createBoundsRectangle((String)curField,formObject);
		        }else if(curCommand.equals("AP")){
		        	boolean apSet = commandAP((Map)curField,formObject);
		        	if(!apSet){
		        	}
		        }else if(curCommand.equals("C")){
		        	boolean cSet = commandC(formObject, curField);
		        	if(!cSet){
		        	}
		        }else if(curCommand.equals("F")){
		        	workOutCharachteristic((String)curField,formObject);
		        }else if(curCommand.equals("T")){
		        	formObject.setPopupTitle((String)curField);
		        }else if(curCommand.equals("M")){
		        	//ignore as is the date and time this annotation was last modified
		        }else if(curCommand.equals("CreationDate")){
		        	//ignore as is the date and time this annotation was created
		        }else if(curCommand.equals("NM")){
		        	commandNM(formObject, curField);
		        }else if(curCommand.equals("Subj")){
		        	//ignore if textual representation of annotation, for use with disabilities
		        	if(debugUnimplemented)
		        		System.out.println("Subj FOR USE WITH DISABILITIES AnnotStream.parseStream() field="+curField);
		        }else if(curCommand.equals("PageNumber")){
		        	//ignore as MARK dealt with elsewhere
		        	formObject.setPageNumber(curField);
		        }else if(curCommand.equals("Popup")){
		        	commandPopup(curField,formObject);
		        }else if(curCommand.equals("RD")){
		        	//the internal boundary of the square or circle
		        	commandRD(curField,formObject);
		        }else {
		        }
		    }
    		
    	}else if(subtype.equals("/FreeText")){
    		
    		formObject.setType(FormObject.ANNOTFREETEXT);
    		
    		//order the commands so they have any fields decoded first
    		createOrderedCommandArray();
    		
    		for(int i=0;i<commands.length;i++){
    			String curCommand = commands[i];
    			Object curField = currentPdfFile.resolveToMapOrString(curCommand,currentField.get(curCommand));
		        
		        if(curCommand.equals("Type")){
		        	if(curField.equals("/Annot")){
		        		//OK ignore
		        	}else {
		        	}
		        }else if(curCommand.equals("Subtype")){
		        	//ignore as is /Ink
		        }else if(curCommand.equals("Rect")){
		        	createBoundsRectangle((String)curField,formObject);
		        }else if(curCommand.equals("AP")){
		        	boolean apSet = commandAP((Map)curField,formObject);
		        	if(!apSet){
		        	}
		        }else if(curCommand.equals("C")){
		        	boolean cSet = commandC(formObject, curField);
		        	if(!cSet){
		        	}
		        }else if(curCommand.equals("F")){
		        	workOutCharachteristic((String)curField,formObject);
		        }else if(curCommand.equals("T")){
		        	formObject.setPopupTitle((String)curField);
		        }else if(curCommand.equals("M")){
		        	//ignore as is the date and time this annotation was last modified
		        }else if(curCommand.equals("CreationDate")){
		        	//ignore as is the date and time this annotation was created
		        }else if(curCommand.equals("NM")){
		        	commandNM(formObject, curField);
		        }else if(curCommand.equals("Subj")){
		        	//ignore if textual representation of annotation, for use with disabilities
		        	if(debugUnimplemented)
		        		System.out.println("Subj FOR USE WITH DISABILITIES AnnotStream.parseStream() field="+curField);
		        }else if(curCommand.equals("PageNumber")){
		        	//ignore as MARK dealt with elsewhere
		        	formObject.setPageNumber(curField);
		        }else if(curCommand.equals("DS")){
		        	//default style dictionary, rich text p640
		        	if(debug)
		        		System.out.println("freetext annotstream.parsestream DS NOT IMPLEMENTED");
		        }else if(curCommand.equals("Contents")){
		        	boolean contentsSet = commandContents(formObject,curField);
		        	if(!contentsSet){
		        	}
		        }else if(curCommand.equals("RC")){
		        	commandRC(curField,formObject);
		        }else if(curCommand.equals("DA")){
		        	formObject.setDefaultValue((String)curField);
		        }else if(curCommand.equals("Open")){
		        	formObject.setOpenState(Boolean.valueOf((String) curField).booleanValue());
		        }else if(curCommand.equals("BS")){
		        	formObject.setBorder(curField);
		        }else if(curCommand.equals("IT")){
		        	//name of intent not used
		        	//can be either FreeTextCallout functions as callout, or FreeTextTypeWriter functions as a type writer object.
//		        	System.out.println("IT command field="+curField);
//		        	ConvertToString.printStackTrace(1);
		        }else if(curCommand.equals("RD")){
		        	//set of numbers that define the difference between the Rect entry of this feild and the boundary of the field
		        	
		        	//left, top , right, bottom
		        	StringTokenizer tok = new StringTokenizer((String)curField,"[ ]");
		        	int left = new Double(tok.nextToken()).intValue();
		        	int top = new Double(tok.nextToken()).intValue();
		        	int right = new Double(tok.nextToken()).intValue();
		        	int bottom = new Double(tok.nextToken()).intValue();
		        	
		        	Rectangle rect = formObject.Rect;
		        	rect.x += left;
		        	rect.y += top;
		        	rect.width += left + right;
		        	rect.height += top + bottom;
		        	formObject.setBoundingRectangle(rect);
		        }else {
		        }
		    }
    	}else if(subtype.equals("/Link")){
    		
    		formObject.setType(FormObject.ANNOTLINK);
    		
//    		need to decode 7-bit acsii for the URI action
    		
    		//order the commands so they have any fields decoded first
    		createOrderedCommandArray();
    		
    		for(int i=0;i<commands.length;i++){
    			String curCommand = commands[i];

                Object curField = currentPdfFile.resolveToMapOrString(curCommand,currentField.get(curCommand));
		        
		        if(curCommand.equals("Type")){
		        	if(curField.equals("/Annot")){
		        		//OK ignore
		        	}else if(curField.equals("/Action")){
		        		//ignore as action events are captured elsewhere "A"
		        	}else {
		        	}
		        }else if(curCommand.equals("Subtype")){
		        	//ignore as is /Link
		        }else if(curCommand.equals("Rect")){
		        	createBoundsRectangle((String)curField,formObject);
		        }else if(curCommand.equals("PageNumber")){
		        	//ignore as MARK dealt with elsewhere
		        	formObject.setPageNumber(curField);
		        }else if(curCommand.equals("StructParent")){
		        	//ignore as not useful yet
		        }else if(curCommand.equals("A")){
		        	commandA(curField,formObject);
		        }else if(curCommand.equals("H")){
		        	commandH(curField,formObject);
		        }else if(curCommand.equals("Border")){
		        	commandBorder(curField);
		        }else if(curCommand.equals("BS")){
		        	formObject.setBorder(curField);
		        }else if(curCommand.equals("C")){
		        	commandC(formObject,curField);
		        }else if(curCommand.equals("Dest") && curField!=null){ //can be null in some files
		        	//specifiying a destination to jump to
		        	
		        	Map activateAction = new HashMap(),destMap = new HashMap();

                    //allow for indirect map
                    if(curField instanceof Map)
                    curField=((Map)curField).get("D");
                    
                    StringTokenizer tok = new StringTokenizer((String)curField,"[ ]",true);
		        	String token;
		        	StringBuffer ref = new StringBuffer(5),position = new StringBuffer();
		        	int count = 0;
		        	while(tok.hasMoreTokens()){
		        		token = tok.nextToken();
		        		if(token.equals("[") || token.equals("]")){
		        			//ignore
		        		}else if(token.equals(" ")){
		        			count++;
		        			if(count<3){
		        				ref.append(token);
		        			}
		        		}else if(count<3){
		        			ref.append(token);
		        		}
		        		
		        		if(count>2){
		        			position.append(token);
		        		}
		        	}
		        	
		        	String reference = ref.toString();
                    //System.out.println("ref="+reference);
                    destMap.put("Page",currentPdfFile.getSubDictionary(reference));
		        	destMap.put("Position",position.toString().trim());
		        	
		        	activateAction.put("Dest",destMap);
		        	formObject.setActivateAction(activateAction);
		        	
		        }else if(curCommand.equals("M")){
		        	//ignore as is the date and time this annotation was last modified
		        }else {
		        }
		    }
    	}else if(subtype.equals("/Widget")){
    		
    		super.parseStream(formObject);
    		
    		//@interest: I would have expected this to call decodeStream in FormStream
    		//(which it extends) and then call decodeCommand (also in FormStream).
    		//It does 1 but calls decodeCommand in AnnotStream 
    		//At least on my mac it does
    		//I have altered decodeCommand into DecodeformComand and decodeAnnotCommand to stop
    		//this.
    		//Its not what I expected from inheritace :-(
    		super.decodeStream(formObject);
    	}else if(subtype.equals("/Highlight")){
    		LogWriter.writeFormLog("{AnnotStream.parseStream} Highlight command NOT IMPLEMENTED",debugUnimplemented);
    	}else if(subtype.equals("/Stamp")){
    		
    		formObject.setType(FormObject.ANNOTSTAMP);
    		
//    		need to decode 7-bit acsii for the URI action
    		
    		//order the commands so they have any fields decoded first
    		createOrderedCommandArray();
    		
    		for(int i=0;i<commands.length;i++){
    			String curCommand = commands[i];
    			Object curField = currentPdfFile.resolveToMapOrString(curCommand,currentField.get(curCommand));
		        
		        if(curCommand.equals("Type")){
		        	if(curField.equals("/Annot")){
		        		//OK ignore
		        	}else {
		        	}
		        }else if(curCommand.equals("Subtype")){
		        	//ignore as is /Stamp
		        }else if(curCommand.equals("Rect")){
		        	createBoundsRectangle((String)curField,formObject);
		        }else if(curCommand.equals("Popup")){
		        	createPopupComponent((Map)curField,formObject,pageHeight,insetH);
		        }else if(curCommand.equals("C")){
		        	//backgroung of closed annot icon
		        	//title bar of popup window
		        	commandC(formObject,curField);
		        }else if(curCommand.equals("M")){
		        	//ignore as is date and time last modified
		        }else if(curCommand.equals("F")){
		        	workOutCharachteristic((String)curField,formObject);
		        }else if(curCommand.equals("T")){
		        	formObject.setPopupTitle((String)curField);
		        }else if(curCommand.equals("Subj")){
		        	//ignore if textual representation of annotation, for use with disabilities
		        	if(debugUnimplemented)
		        		System.out.println("Subj FOR USE WITH DISABILITIES AnnotStream.parseStream() field="+currentPdfFile.resolveToMapOrString("Subj",curField));
		        }else if(curCommand.equals("Name")){
		        	//System.out.println("Name of icon type="+currentField.getxx("Name"));
		        }else if(curCommand.equals("NM")){
		        	commandNM(formObject, curField);
		        }else if(curCommand.equals("CreationDate")){
		        	//ignore as is the date and time this annotation was created
		        }else if(curCommand.equals("AP")){
		        	commandAP((Map)curField,formObject);
		        }else {
		        }
		    }
    	}else {
    	}
	}

	private void commandBorder(Object curField) {
//		if BS entry ignore Border entry
    	if(currentField.containsKey("BS"))
    		return;
    	
    	//[horizontal radius, vertical radius, width]
    	//if radius is 0 square corders, if width 0 no border
    	curField = Strip.removeArrayDeleminators((String)curField);
    	StringTokenizer tok = new StringTokenizer((String)curField);
    	float horizRad = 0;
		float verticRad = 0;
		float width = 0;
    	try{
    		horizRad = Float.parseFloat(tok.nextToken());
    		verticRad = Float.parseFloat(tok.nextToken());
    		width = Float.parseFloat(tok.nextToken());
    	}catch(NumberFormatException e){
    		LogWriter.writeFormLog("Border in annot.parsestream Link NOT numbers",debugUnimplemented);
    		return;
    	}
    	
    	if(width==0){
    		return;
    	}else if(horizRad==0 && verticRad==0){
    		//square corners
    		LogWriter.writeFormLog("{AnnotStream.commandBorder} squared border NOT IMPLEMENTED",debugUnimplemented);
    	}else {
    		//create rounded rectangle border
    		LogWriter.writeFormLog("{AnnotStream.commandBorder} create rounded rectangle border NOT IMPLEMENTED",debugUnimplemented);
    	}
    	
    	if(tok.hasMoreTokens() && tok.nextToken().startsWith("[")){
    		//dash array
    		LogWriter.writeFormLog("{AnnotStream.commandBorder} dash array NOT IMPLEMENTED",debugUnimplemented);
    	}
	}

	private void createOrderedCommandArray() {
		Iterator iter = currentField.keySet().iterator();
		keySize=currentField.keySet().size();
		commands = new String[keySize];
		int endnum = keySize-1, num = 0;
		while(iter.hasNext()){
			String command = (String)iter.next();
			/*
			 * add all commands that need processing last to
			 * the end of the array,
			 */
			if(command.equals("AP")){
				commands[endnum] = command;
				endnum--;
			}else {
				commands[num] = command;
				num++;
			}
		}
	}

	/**
	 * resolve the RC command
	 */
	private void commandRC(Object curField,FormObject formObject) {
		//rich text command
		if(debugUnimplemented)
			System.out.println("RC rich text NOT IMPLEMENTED command="+curField);
	}

	/**
	 * create and assign the popup with the field required
	 */
	private void commandPopup(Object curField,FormObject formObject) {
        //the popup data for this annotation
//        if(field instanceof Map){
//            Map mapForField = (Map)field;
//            createBoundsRectangle((String)mapForField.getxx("Rect"),formObject);
//            
//            formObject.setType(FormObject.POPUP);
//            
//            int characteristic = workOutCharachteristic((String)mapForField.getxx("F"));
//            formObject.setCharacteristic(characteristic);
//            
//            formObject.setOpenState(new Boolean((String)mapForField.getxx("Open")).booleanValue());
//        }else {
//            LogWriter.writeFormLog("{stream} Popup command NON Map NOT implemented field="+field,debugUnimplemented);
//        }
		
		Map clickAction = new HashMap();
		Component popupcomp = createPopupComponent((Map)curField,formObject,pageHeight,insetH);
		if(popupcomp!=null){
			clickAction.put("Popup",popupcomp);
			formObject.setActivateAction(clickAction);
			
			//System.out.println("Popup action added="+currentField.getxx("Popup"));
		}else {
		}
	}

	/**
	 * rgb color, used for background of closed icon, title bar of popup window, border of link annotation
	 */
	private boolean commandC(FormObject formObject, Object curField) {
		Color cColor = generateColorFromString((String)curField);
		if(cColor!=null){
			formObject.setCColor(cColor);
			return true;
		}else {
			return false;
		}
	}

	/**
	 * resolve the contents of the annotation
	 */
	private boolean commandContents(FormObject formObject, Object curField) {
		
		if(curField instanceof String){
		    formObject.setContents((String)curField);
		    return true;
		}else {
		    LogWriter.writeFormLog("{stream} Contents command is NON String field="+curField,debugUnimplemented);
		}
		return false;
	}

	/**
	 * setup the popup component, and return it
	 */
	private Component createPopupComponent(Map popupObj,FormObject formObject,int pageHeight,int insetH) {
		
		Component popupcomp = null;
		
		
		String popupTitle;
		if(formObject.popupTitle==null)
			popupTitle = "TEST popupTitle not set in formobject";
		else
			popupTitle = formObject.popupTitle;
		popupcomp = new JTextField(popupTitle);
		
		Rectangle rect = createBoundsRectangle((String)currentPdfFile.resolveToMapOrString("Rect", popupObj.get("Rect")),null);
		rect.y = pageHeight-rect.y-rect.height+insetH;
		popupcomp.setBounds(rect);
		
		popupcomp.setVisible(Boolean.valueOf((String) currentPdfFile.resolveToMapOrString("Open", popupObj.get("Open"))).booleanValue());
		
		return popupcomp;
	}

	/**
     * decodes any streams that need previous data to be decoded first
     */
    protected void decodeStream(FormObject formObject) {
    	
    	for(int j=0;j<keySize;j++){
	        
	        String command =commands[j];
	        Object field = currentPdfFile.resolveToMapOrString(command,currentField.get(command));
	        
	        //@interest: was originally a super.decodeCommand followed by decodeCommad
	        boolean notFound=decodeFormCommand(command,field,formObject);
	        if(notFound)
	        	decodeAnnotCommand(command,field,formObject);
        }
    }
    
    protected boolean decodeAnnotCommand(String command, Object field,FormObject formObject) {
    	
    	//flag to show if processed
		boolean notFound=false;
    	
    	if(command.equals("InkList")){
            //the ink list join the dots array
            
            /**current shape object being drawn note we pass in handle on pageLines*/
            PdfShape currentDrawShape=new PdfShape();
            
            String paths = Strip.removeArrayDeleminators((String)field);
            StringTokenizer tok = new StringTokenizer(paths,"[] ",true);
            
            boolean isFirstPoint = false;
            String next,first=null,second=null;
            while(tok.hasMoreTokens()){
                next = tok.nextToken();
                if(next.equals("[")){
                    isFirstPoint = true;
                    continue;
                }else if(next.equals("]")){
                    continue;
                }else if(next.equals(" ")){
                    continue;
                }else {
                    if(first!=null){
                        second = next;
                    }else {
                        first = next;
                        continue;
                    }
                }
                
                if(isFirstPoint){
                    currentDrawShape.moveTo(Float.parseFloat(first)-formObject.Rect.x,Float.parseFloat(second)-formObject.Rect.y);
                    isFirstPoint = false;
                }else{
                    currentDrawShape.lineTo(Float.parseFloat(first)-formObject.Rect.x,Float.parseFloat(second)-formObject.Rect.y);
                }
                
                first = null;
            }
//          close for s command
//            currentDrawShape.closeShape();
                
            org.jpedal.objects.GraphicsState currentGraphicsState=formObject.getGraphicsState();
            
            Shape currentShape =
                currentDrawShape.generateShapeFromPath(null,
                    currentGraphicsState.CTM,
                    false,null,false,null,currentGraphicsState.getLineWidth(),0);
            
            Stroke inkStroke = currentGraphicsState.getStroke();
            
            BufferedImage image = new BufferedImage(formObject.Rect.width,formObject.Rect.height,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D)image.getGraphics();
            g2.setStroke(inkStroke);
            g2.setColor(Color.red);
            g2.scale(1,-1);
            g2.translate(0,-image.getHeight());
            g2.draw(currentShape);
            
            g2.dispose();
            
            //ShowGUIMessage.showGUIMessage("path draw",image,"path drawn");
            
            formObject.setNormalAppOff(image,null);
            
        }else if(command.equals("RD")){
            //rectangle differences left top right bottom order as recieved
            //the bounds of the internal object, in from the Rect
            
            StringTokenizer tok = new StringTokenizer(Strip.removeArrayDeleminators((String)field));
            float left = Float.parseFloat(tok.nextToken());
            float top = Float.parseFloat(tok.nextToken());
            float right = Float.parseFloat(tok.nextToken());
            float bottom = Float.parseFloat(tok.nextToken());
            
            formObject.setInternalBounds(left,top,right,bottom);
        }else {
        	notFound = true;
        }
    	
    	return notFound;
    }
    
	/**
	 * rectangle differences left top right bottom order as recieved
	 * the bounds of the internal object, in from the Rect
	 */
	private void commandRD(Object field, FormObject formObject) {
		
		StringTokenizer tok = new StringTokenizer(Strip.removeArrayDeleminators((String)field));
		float left = Float.parseFloat(tok.nextToken());
		float top = Float.parseFloat(tok.nextToken());
		float right = Float.parseFloat(tok.nextToken());
		float bottom = Float.parseFloat(tok.nextToken());
		
		formObject.setInternalBounds(left,top,right,bottom);
	}

	/**
	 * join the dots and save the image for the inklist annot
	 */
	private void commandInkList(Object field, FormObject formObject) {
		//the ink list join the dots array
		if(debug)
			System.out.println("inklist array="+field);
		
		/**current shape object being drawn note we pass in handle on pageLines*/
		PdfShape currentDrawShape=new PdfShape();
		
		String paths = Strip.removeArrayDeleminators((String)field);
		StringTokenizer tok = new StringTokenizer(paths,"[] ",true);
		int countArrays=0;
		boolean isFirstPoint = false;
		String next,first=null,second=null;
		while(tok.hasMoreTokens()){
		    next = tok.nextToken();
		    if(next.equals("[")){
		        countArrays++;
		        isFirstPoint = true;
		        continue;
		    }else if(next.equals("]")){
		        countArrays--;
		        continue;
		    }else if(next.equals(" ")){
		        continue;
		    }else {
		        if(first!=null){
		            second = next;
		        }else {
		            first = next;
		            continue;
		        }
		    }
		    
		    if(isFirstPoint){
		        currentDrawShape.moveTo(Float.parseFloat(first)-formObject.Rect.x,Float.parseFloat(second)-formObject.Rect.y);
		        isFirstPoint = false;
		    }else{
		        currentDrawShape.lineTo(Float.parseFloat(first)-formObject.Rect.x,Float.parseFloat(second)-formObject.Rect.y);
		    }
		    
		    first = null;
		}
//          close for s command
//            currentDrawShape.closeShape();
		    
		org.jpedal.objects.GraphicsState currentGraphicsState=formObject.getGraphicsState();
		
		Shape currentShape =
		    currentDrawShape.generateShapeFromPath(null,
		        currentGraphicsState.CTM,
		        false,null,false,null,currentGraphicsState.getLineWidth(),0);
		
		Stroke inkStroke = currentGraphicsState.getStroke();
		
		BufferedImage image = new BufferedImage(formObject.Rect.width,formObject.Rect.height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D)image.getGraphics();
		g2.setStroke(inkStroke);
		g2.setColor(Color.red);
		g2.scale(1,-1);
		g2.translate(0,-image.getHeight());
		g2.draw(currentShape);
		
		g2.dispose();
		
		formObject.setNormalAppOff(image,null);
	}
}
