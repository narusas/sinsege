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
 * Created on 06-Jul-2005
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
 * FormObject.java
 * ---------------
 *
 * Original Author:  Mark Stephens (mark@idrsolutions.com)
 * Contributor(s):
 *
 */
package org.jpedal.objects.acroforms;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.JTextField;

import org.jpedal.objects.GraphicsState;
import org.jpedal.utils.LogWriter;

/**
 * @author Chris Wade
 * 
 * This class holds all the data for the form fields
 */
public class FormObject{
    
    public static final int ANNOTPOPUP = 10;
    public static final int ANNOTTEXT = 11;
    public static final int ANNOTSQUARE = 12;
    public static final int ANNOTINK = 13;
    public static final int ANNOTFREETEXT = 14;
    public static final int ANNOTLINK = 15;
    public static final int ANNOTSTAMP = 16;
    
    public static final int FORMBUTTON = 0;
    public static final int FORMCHOICE = 1;
    public static final int FORMTEXT = 2;
    public static final int FORMSIG = 3;

    public FormObject(){
    	
    }
    
    public boolean isXFAObject=false;
    public int rotation = 0;
    public String userName;
    public String fieldName;
    public String mapName;
    public boolean[] characteristic = new boolean[9];
    public Rectangle Rect = new Rectangle(0,0,0,0);
    public int type=-1;
    public boolean[] flags = new boolean[32];
    
    public int[] topIndex=null;
    public String selectedItem;
    public String[] itemsList;
    
    public Map activateAction;
    public Map enteredAction;
    public Map exitedAction;
    
    public Object border;
    public Color borderColor;
    public Color backgroundColor=null;
    
    public int allignment=-1;
    public Color textColor;
    public Font textFont;
    public int textSize=-1;
    public String textString;
    public int maxTextLength=-1;
    public int textPosition = -1;
    public String defaultValue;
    public String popupTitle;
    
    public String normalCaption;
    public String rolloverCaption;
    public String downCaption;
    
    public boolean appearancesUsed=false;
    public boolean offsetDownIcon = false;
    public boolean noDownIcon = false;
    public boolean invertDownIcon = false;
    public String whenToScaleIcon;
    public String defaultState;
    public String onState;
    public String currentState;
    public String normalOffState,normalOnState;
    public BufferedImage normalOffImage=null;
    public BufferedImage normalOnImage;
    public BufferedImage rolloverOffImage=null;
    public BufferedImage rolloverOnImage;
    public BufferedImage downOffImage=null;
    public BufferedImage downOnImage;
    private boolean hasNormalOffImage=false;
    private boolean hasRolloverOffImage=false;
    private boolean hasDownOffImage=false;
    private boolean hasDownImages=false;
    private boolean hasRolloverOn=false;
    private boolean hasNormalOn=false;
    public Map kidData = null;
    
    
    /** the C color for annotations */
    public Color cColor;
    /** the contents for any text display on the annotation*/
    public String contents;
    /** whether the annotation is being displayed or not by default*/
    public boolean show=false;
    /** the graphics state for the annotation ink list*/
    public GraphicsState graphicsState=null;
    /** the internal bounds from the Rect of the annotation*/
    public Rectangle internalBounds;
	public int pageNumber=-1;
	
	public String stateTocheck="";
	/** map that references the display value from the export values */
	public Map valuesMap = null;
	/** javascript command to validate the value selected */
	public String validateValue = null;
    
    public FormObject duplicate(){
        FormObject newObject=new FormObject();
        
        newObject.rotation=rotation;
        newObject.userName=userName;
        newObject.fieldName=fieldName;
        newObject.mapName=mapName;
        newObject.characteristic=characteristic;
        newObject.Rect=Rect;
        newObject.type=type;
        newObject.flags=flags;
        
        newObject.topIndex=topIndex;
        newObject.selectedItem=selectedItem;
        newObject.itemsList=itemsList;
        
        newObject.activateAction=activateAction;
        newObject.enteredAction=enteredAction;
        newObject.exitedAction=exitedAction;
        
        newObject.border=border;
        newObject.borderColor=borderColor;
        newObject.backgroundColor=backgroundColor;
        
        newObject.allignment=allignment;
        newObject.textColor=textColor;
        newObject.textFont=textFont;
        newObject.textSize=textSize;
        newObject.textString=textString;
        newObject.maxTextLength=maxTextLength;
        newObject.textPosition=textPosition;
        newObject.defaultValue=defaultValue;
        
        newObject.normalCaption=normalCaption;
        newObject.rolloverCaption=rolloverCaption;
        newObject.downCaption=downCaption;
        
        newObject.appearancesUsed=appearancesUsed;
        newObject.offsetDownIcon=offsetDownIcon;
        newObject.noDownIcon=noDownIcon;
        newObject.invertDownIcon=invertDownIcon;
        newObject.whenToScaleIcon=whenToScaleIcon;
        newObject.defaultState=defaultState;
        newObject.onState=onState;
        newObject.currentState=currentState;
        newObject.normalOffImage=normalOffImage;
        newObject.normalOnImage=normalOnImage;
        newObject.rolloverOffImage=rolloverOffImage;
        newObject.rolloverOnImage=rolloverOnImage;
        newObject.downOffImage=downOffImage;
        newObject.downOnImage=downOnImage;
        newObject.hasNormalOffImage=hasNormalOffImage;
        newObject.hasRolloverOffImage=hasRolloverOffImage;
        newObject.hasDownOffImage=hasDownOffImage;
        newObject.hasDownImages=hasDownImages;
        newObject.hasRolloverOn=hasRolloverOn;
        newObject.hasNormalOn=hasNormalOn;
        newObject.kidData=kidData;
        newObject.pageNumber = pageNumber;
        
        //annotations
        newObject.cColor=cColor;
        newObject.contents=contents;
        newObject.show=show;
        newObject.internalBounds=internalBounds;
        newObject.popupTitle = popupTitle;
        
        newObject.stateTocheck=stateTocheck;
        
        return newObject;
    }
    /**
     * turns all data into a string
     */
    public String toString(){
        StringBuffer buf = new StringBuffer();
    
        buf.append("\n rotation=");
	    buf.append(rotation);
	    buf.append("\n username=");
		buf.append(userName);
	    buf.append("\n fieldName=");
		buf.append(fieldName);
	    buf.append("\n mapName=");
		buf.append(mapName);
	    buf.append("\n characteristic=");
		buf.append(ConvertToString.convertArrayToString(characteristic));
	    buf.append("\n Rect=");
		buf.append(Rect);
	    buf.append("\n type=");
		buf.append(type);
	    buf.append("\n flags=");
		buf.append(flags);
	    
	    buf.append("\n topIndex=");
		buf.append(topIndex);
	    buf.append("\n selectedItem=");
		buf.append(selectedItem);
	    buf.append("\n itemsList=");
		buf.append(itemsList);
	        
	    buf.append("\n activateAction=");
		buf.append(activateAction);
	    buf.append("\n enteredAction=");
		buf.append(enteredAction);
	    buf.append("\n exitedAction=");
		buf.append(exitedAction);
	    
	    buf.append("\n border=");
		buf.append(border);
	    buf.append("\n borderColor=");
		buf.append(borderColor);
	    buf.append("\n backgroundColor=");
		buf.append(backgroundColor);
	    
	    buf.append("\n alignment=");
		buf.append(allignment);
	    buf.append("\n textColor=");
		buf.append(textColor);
	    buf.append("\n textFont=");
		buf.append(textFont);
	    buf.append("\n textSize=");
		buf.append(textSize);
	    buf.append("\n textString=");
		buf.append(textString);
	    buf.append("\n maxTextLength=");
		buf.append(maxTextLength);
	    buf.append("\n textPosition=");
		buf.append(textPosition);
	    buf.append("\n defaultValue=");
		buf.append(defaultValue);
		buf.append("\n popupTitle=");
		buf.append(popupTitle);
	    
	    buf.append("\n normalCaption=");
		buf.append(normalCaption);
	    buf.append("\n rolloverCaption=");
		buf.append(rolloverCaption);
	    buf.append("\n downCaption=");
		buf.append(downCaption);
	    
	    //add images data
		buf.append(toStringImages());
		
	    buf.append("\n kidData=");
	    buf.append(kidData);
	    
	    //annotation
	    buf.append("\n Ccolor=");
	    buf.append(cColor);
	    buf.append("\n contents=");
	    buf.append(contents);
	    buf.append("\n show=");
	    buf.append(show);
	    buf.append("\n internalBounds=");
	    buf.append(internalBounds);
        
        return buf.toString();
    }
    
    /**
     * turns all Image data into a string
     */
    public String toStringImages(){
        StringBuffer buf = new StringBuffer();
        buf.append("\n appearancesUsed=");
        buf.append(appearancesUsed);
        buf.append("\n offsetdownicon=");
        buf.append(offsetDownIcon);
        buf.append("\n nodownIcon");
        buf.append(noDownIcon);
        buf.append("\n invertDownIcon=");
        buf.append(invertDownIcon);
        buf.append("\n whentoscaleicon=");
        buf.append(whenToScaleIcon);
        buf.append("\n defaultstate=");
        buf.append(defaultState);
        buf.append("\n onstate=");
        buf.append(onState);
        buf.append("\n currentstate=");
        buf.append(currentState);
        buf.append("\n normaloff=");
        buf.append(normalOffImage);
        buf.append("\n normalon=");
        buf.append(normalOnImage);
        buf.append("\n rolloveroff=");
        buf.append(rolloverOffImage);
        buf.append("\n rolloveron=");
        buf.append(rolloverOnImage);
        buf.append("\n downoff=");
        buf.append(downOffImage);
        buf.append("\n downon=");
        buf.append(downOnImage);
        buf.append("\n hasnormaloff=");
        buf.append(hasNormalOffImage);
        buf.append("\n hasrolloveroff=");
        buf.append(hasRolloverOffImage);
        buf.append("\n hasdownoff=");
        buf.append(hasDownOffImage);
        buf.append("\n hasdownimages=");
        buf.append(hasDownImages);
        buf.append("\n hasrolloveron=");
        buf.append(hasRolloverOn);
        buf.append("\n hasnormalon=");
        buf.append(hasNormalOn);
        
        return buf.toString();
    }
    
    /**
     * create null border or add the specified border
     */
    public void setBorder(Object borderInfo) {
        border = borderInfo;
    }

    /**
     * creates and sets an alignment variable
     */
	public void setHorizontalAlign(Object field) {
    	allignment = JTextField.LEFT;
		if(field.equals("0")){
            allignment = JTextField.LEFT;//2
		}else if(field.equals("1")){
		    allignment = JTextField.CENTER;//0
		}else if(field.equals("2")){
		    allignment = JTextField.RIGHT;//4
		}else {
			LogWriter.writeFormLog("FormObject.setHorizontalAlign not taking "+field,FormStream.debugUnimplemented);
	    }
    }

    /**
     * sets the text color for this form
     */
    public void setTextColor(Color color) {
        textColor = color;
    }

    /**
     * set the text font for this form
     */
    public void setTextFont(Font font) {
        textFont = font;
    }

    /**
     * sets the text size for this form
     */
    public void setTextSize(int size) {
        textSize = size;
    }

    /**
     * sets the child on state,
     * only applicable to radio buttons
     */
    public void setChildOnState(String curValue) {
        onState = curValue;
    }

    /**
     * sets the current state,
     * only applicable to check boxes
     */
    public void setCurrentState(String curValue) {
        currentState = curValue;
    }

    /**
     * sets the text value
     */
    public void setTextValue(String text) {
        textString = text;
    }

    /**
     * sets the selected item
     * only applicable to the choices fields
     */
    public void setSelectedItem(String curValue) {
        selectedItem = curValue;
    }

    /**
     * sets the username for this field
     */
    public void setUserName(String field) {
        userName = field;
    }

    /**
     * sets the field name for this field 
     */
    public void setFieldName(String field) {
        fieldName = field;
    }

    /**
     * sets the map name for this field
     */
    public void setMapName(String field) {
        mapName = field;
    }

    /**
     * sets the default state from this fields appearance streams
     */
    public void setDefaultState(String state) {
        defaultState = state;
    }

    /**
     * sets the characterstic's for this form,
     * if <b>characterInt</b> is -1, the booleans are set to <b>newCharacteristicArray</b>,
     * otherwise, the boolean at index <b>characterInt</b>-1 is set to true
     */
    public void setCharacteristic(int characterInt,boolean[] newCharacteristicArray) {
    	/**
    	 * 1 = invisible
    	 * 2 = hidden
    	 * 3 = print
    	 * 4 = nozoom
    	 * 5= norotate
    	 * 6= noview
    	 * 7 = read only (ignored by wiget)
    	 * 8 = locked
    	 * 9 = togglenoview
    	 */
    	if(characterInt==-1){
    		characteristic = newCharacteristicArray;
    	}else {
    		characteristic[characterInt-1] = true;
    	}
    }
    
    /** turns each individual characteristic on
     * @see setCharacteristic(int characterInt,boolean[] newCharacteristicArray) 
	 */
    public void setCharacteristic(int characterInt){
    	setCharacteristic(characterInt,null);
    }

    /**
     * sets the list of items
     * used with choice fields
     */
    public void setlistOfItems(String[] items) {
        itemsList = items;
    }

    /**
     * sets the top index
     * for the choice fields
     */
    public void setTopIndex(int[] index) {
        topIndex = index;
    }

    /**
     * sets the maximum text length
     */
    public void setMaxTextLength(int length) {
        maxTextLength = length;
    }

    /**
     * sets the bounding rectangle for this form
     */
    public void setBoundingRectangle(Rectangle rectangle) {
        Rect  = rectangle;
    }

    /**
     * sets the type this form specifies
     */
    public void setType(int type){
        this.type = type;
    }

    /**
     * sets the flag <b>pos</b> to value of <b>flag</b>
     */
    public void setFlag(int pos,boolean flag) {
    	/*
    	flags[1]=(flagValue & READONLY)==READONLY
		flags[2]=(flagValue & REQUIRED)==REQUIRED;
		flags[3]=(flagValue & NOEXPORT)==NOEXPORT;
		//FORMBUTTON
		flags[14]=(flagValue & NOTOGGLETOOFF)==NOTOGGLETOOFF;
		flags[15]=(flagValue & RADIO)==RADIO;
		flags[16]=(flagValue & PUSHBUTTON)==PUSHBUTTON;
		flags[25]=(flagValue & RADIOINUNISON)==RADIOINUNISON;//same as RICHTEXT
		//FORMTEXT
		flags[12]=(flagValue & MULTILINE)==MULTILINE;
		flags[13]=(flagValue & PASSWORD)==PASSWORD;
		flags[20]=(flagValue & FILESELECT)==FILESELECT;
		flags[22]=(flagValue & DONOTSPELLCHECK)==DONOTSPELLCHECK;
		flags[23]=(flagValue & DONOTSCROLL)==DONOTSCROLL;
		flags[25]=(flagValue & RICHTEXT)==RICHTEXT;//same as RADIOINUNISON
		//FORMCHOICE
		flags[17]=(flagValue & COMBO)==COMBO;
		flags[18]=(flagValue & EDIT)==EDIT;
		flags[19]=(flagValue & SORT)==SORT;
		flags[21]=(flagValue & MULTISELECT)==MULTISELECT;
		flags[26]=(flagValue & COMMITONSELCHANGE)==COMMITONSELCHANGE;
		flags[22]=(flagValue & DONOTSPELLCHECK)==DONOTSPELLCHECK;

		flags[24]=(flagValue & COMB)==COMB;
		 */
   		flags[pos-1] = flag;
    }
    
    /**
     * sets the flags array to be <b>interactiveFlags</b>
     */
    public void setFlags(boolean[] interactiveFlags){
   		flags = interactiveFlags;
    }

    /**
     * sets the default value
     */
    public void setDefaultValue(String value) {
        defaultValue = value;
    }

    /**
     * sets the activate action
     */
    public void setActivateAction(Map activateData) {
        activateAction = activateData;
    }

    /**
     * sets the entered action
     */
    public void setEnteredAction(Map enteredData) {
        enteredAction = enteredData;
    }

    /**
     * sets the exited action
     */
    public void setExitedAction(Map exitedData) {
        exitedAction = exitedData;
    }

    /**
     * the normal off image
     * if only one state call this to set normalOffImage to be the default image
     */
    public void setNormalAppOff(BufferedImage image,String state) {
    	normalOffState = state;
        normalOffImage = image;
        hasNormalOffImage = true;
        appearancesUsed = true;
    }

    /**
     * the on normal image
     */
    public void setNormalAppOn(BufferedImage image,String state) {
    	normalOnState = state;
        normalOnImage = image;
        hasNormalOn = true;
        appearancesUsed = true;
    }
    
    /**
     * sets the rollover off image
     * if only one state call this to set rolloverOffImage to be the default image
     */
    public void setRolloverAppOff(BufferedImage image) {
        rolloverOffImage = image;
        hasRolloverOffImage= true;
        appearancesUsed = true;
    }

    /**
     * sets the rollover on image
     */
    public void setRolloverAppOn(BufferedImage image) {
        rolloverOnImage = image;
        hasRolloverOn = true;
        appearancesUsed = true;
    }
    
    /**
     * sets the down off image
     * if only one state call this to set downOffImage to be the default image
     */
    public void setDownAppOff(BufferedImage image) {
        downOffImage = image;
        hasDownOffImage = true;
        hasDownImages = true;
        appearancesUsed = true;
    }

    /**
     * sets the down on image
     */
    public void setDownAppOn(BufferedImage image) {
        downOnImage = image;
        hasDownImages = true;//check can be removed if speed needed
        appearancesUsed = true;
    }

    /**
     * sets the border color
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * sets the background color for this form
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * sets the normal caption for this form
     * 
     */
    public void setNormalCaption(String caption) {
        normalCaption = caption;
    }

    /**
     * sets the text positioning relative to the icon
     */
    public void setTextPosition(int positioning) {
        textPosition = positioning;
    }

    /**
     * sets whether there should be a down looking icon
     */
    public void setOffsetDownApp() {
        offsetDownIcon = true;
    }

    /**
     * sets whether a down icon should be used
     */
    public void setNoDownIcon() {
        noDownIcon = true;
    }
    
    /**
     * sets whether to invert the normal icon for the down icon
     */
    public void setInvertForDownIcon() {
		invertDownIcon = true;
	}

    /**
     * sets the rotation factor
     */
    public void setRotation(int rotate) {
        rotation = rotate;
    }

    /**
     * sets the rollover caption
     */
    public void setRolloverCaption(String caption) {
        rolloverCaption = caption;
    }

    /**
     * sets the down caption
     */
    public void setDownCaption(String caption) {
        downCaption = caption;
    }

    /**
     * sets when the icon should be scaled
     */
    public void setWhenToScaleIcon(String scaleIcon) {
        whenToScaleIcon = scaleIcon;
    }

    /**
     * returns true if has normal of image
     */
    public boolean hasNormalOff() {
        return hasNormalOffImage;
    }

    /**
     * returns true if has rollover off image
     */
    public boolean hasRolloverOff() {
        return hasRolloverOffImage;
    }

    /**
     * returns true if has down off image
     */
    public boolean hasDownOff() {
        return hasDownOffImage;
    }

    /**
     * returns true if has one or more down images set
     */
    public boolean hasDownImage() {
        return hasDownImages;
    }

    /**
     * returns true if has a rollover on image
     */
    public boolean hasRolloverOn() {
        return hasRolloverOn;
    }

    /**
     * returns true if has a normal on image
     */
    public boolean hasNormalOn() {
        return hasNormalOn;
    }

    /**
     * sets the kid data for the radio button group to be setup
     */
    public void setKidData(Map data) {
        kidData = data;
    }
    
    /**
     * sets the C color for annotations
     */
    public void setCColor(Color newColor) {
        cColor = newColor;
    }
    
    /**
     * sets the contents for the annotation
     */
    public void setContents(String newString) {
        contents = newString;
    }
    
    /**
     * resolve what type of field <b>type</b> specifies
     * and return as String
     */
    public String resolveType(int type) {
        
        if(type==FORMBUTTON)
            return "Button";
        else if(type==FORMCHOICE)
            return "Choice";
        else if(type==FORMTEXT)
            return "Text";
        else if(type==ANNOTPOPUP)
            return "PopUp";
        else if(type==ANNOTSQUARE)
            return "Square";
        else if(type==ANNOTTEXT)
            return "Text Annot";
        
        
        return null;
    }
    
    /**
     * sets the show option for the annotation
     */
    public void setOpenState(boolean newBoolean) {
        show = newBoolean;
    }
    
    /**
     * returns the already setup graphics stat, if not setup creates a new one
     * this allows the stroke to be setup prior to drawing the shape
     */
    public GraphicsState getGraphicsState() {
        if(graphicsState==null)
            graphicsState = new org.jpedal.objects.GraphicsState();
        return graphicsState;
    }
    
    /**
     * sets the internal bounding box fro the annotation
     */
    public void setInternalBounds(float left, float top, float right, float bottom) {
        int x = (int) (left+0.5);
        int y = (int) (top+0.5);
        int w = (int) (Rect.width-(left+right+0.5));
        int h = (int) (Rect.height-(top+bottom+0.5));
        internalBounds = new Rectangle(x,y,w,h);
    }
    
	/**
	 * sets the popup title bar text
	 */
	public void setPopupTitle(String title) {
		popupTitle = title;
	}
	
	/**
	 * set the page number for this form
	 */
	public void setPageNumber(Object field) {
		if(field instanceof String){
			pageNumber = Integer.parseInt((String) field);
		}else {
			LogWriter.writeFormLog("{FormObject.setPageNumber} pagenumber being set to UNKNOWN type",false);
		}
	}
	
	public void setStateToCheck(String stateTocheck) {
		this.stateTocheck=stateTocheck;
		
	}
	
	public void overwriteWith(FormObject form) {
		if(form==null)
			return;
		
		if(form.rotation!=0)
			rotation = form.rotation;
        if(form.userName!=null)
			userName = form.userName;
        if(form.fieldName!=null)
        	fieldName = form.fieldName;
        if(form.mapName!=null)
        	mapName = form.mapName;
        if(form.characteristic!=null)
        	characteristic = form.characteristic;
        if(form.Rect!=null)
        	Rect = form.Rect;
        if(form.type!=-1)
        	type = form.type;
        if(form.flags!=null)
        	flags = form.flags;
        
        if(form.topIndex!=null)	
        	topIndex = form.topIndex;
        if(form.selectedItem!=null)	
        	selectedItem = form.selectedItem;
        if(form.itemsList!=null)
        	itemsList = form.itemsList;
        
        if(form.activateAction!=null)
        	activateAction = form.activateAction;
        if(form.enteredAction!=null)
        	enteredAction = form.enteredAction;
        if(form.exitedAction!=null)	
        	exitedAction = form.exitedAction;
        
        if(form.border!=null)		
        	border = form.border;
        if(form.borderColor!=null)	
        	borderColor = form.borderColor;
        if(form.backgroundColor!=null)	
        	backgroundColor = form.backgroundColor;
        
        if(form.allignment!=-1)	
        	allignment = form.allignment;
        if(form.textColor!=null)	
        	textColor = form.textColor;
        if(form.textFont!=null)	
        	textFont = form.textFont;
        if(form.textSize!=-1)		
        	textSize = form.textSize;
        if(form.textString!=null)	
        	textString = form.textString;
        if(form.maxTextLength!=-1)
        	maxTextLength = form.maxTextLength;
        if(form.textPosition!=-1)	
        	textPosition = form.textPosition;
        if(form.defaultValue!=null)	
        	defaultValue = form.defaultValue;
        
        if(form.normalCaption!=null)
        	normalCaption = form.normalCaption;
        if(form.rolloverCaption!=null)	
        	rolloverCaption = form.rolloverCaption;
        if(form.downCaption!=null)	
        	downCaption = form.downCaption;
        
        if(form.appearancesUsed!=false)	
        	appearancesUsed = form.appearancesUsed;
        if(form.offsetDownIcon!=false)	
        	offsetDownIcon = form.offsetDownIcon;
        if(form.noDownIcon!=false)	
        	noDownIcon = form.noDownIcon;
        if(form.invertDownIcon!=false)
        	invertDownIcon = form.invertDownIcon;
        if(form.whenToScaleIcon!=null)
        	whenToScaleIcon = form.whenToScaleIcon;
        if(form.defaultState!=null)	
        	defaultState = form.defaultState;
        if(form.onState!=null)		
        	onState = form.onState;
        if(form.currentState!=null)	
        	currentState = form.currentState;
        if(form.normalOffImage!=null)
        	normalOffImage = form.normalOffImage;
        if(form.normalOnImage!=null)
        	normalOnImage = form.normalOnImage;
        if(form.rolloverOffImage!=null)
        	rolloverOffImage = form.rolloverOffImage;
        if(form.rolloverOnImage!=null)	
        	rolloverOnImage = form.rolloverOnImage;
        if(form.downOffImage!=null)		
        	downOffImage = form.downOffImage;
        if(form.downOnImage!=null)	
        	downOnImage = form.downOnImage;
        if(form.hasNormalOffImage!=false)
        	hasNormalOffImage = form.hasNormalOffImage;
        if(form.hasRolloverOffImage!=false)	
        	hasRolloverOffImage = form.hasRolloverOffImage;
        if(form.hasDownOffImage!=false)	
        	hasDownOffImage = form.hasDownOffImage;
        if(form.hasDownImages!=false)	
        	hasDownImages = form.hasDownImages;
        if(form.hasRolloverOn!=false)	
        	hasRolloverOn = form.hasRolloverOn;
        if(form.hasNormalOn!=false)	
        	hasNormalOn = form.hasNormalOn;
        if(form.kidData!=null)		
        	kidData = form.kidData;
        if(form.pageNumber!=-1)	
        	pageNumber = form. pageNumber;
        
        //annotations
        if(form.cColor!=null)	
        	cColor = form.cColor;
        if(form.contents!=null)	
        	contents = form.contents;
        if(form.show!=false)		
        	show = form.show;
        if(form.internalBounds!=null)
        	internalBounds = form.internalBounds;
        if(form. popupTitle!=null)	
        	popupTitle = form. popupTitle;
        
        if(form.stateTocheck!=null)	
        	stateTocheck = form.stateTocheck;
	}
	
	/**
	 * store the javascript command for validating the value that changed
	 */
	public void setValidateValueCommand(String tmp) {
		validateValue = tmp;
	}
}