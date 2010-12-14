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
 * Created on 11-Mar-2005
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
 * FormStream.java
 * ---------------
 *
 * Original Author:  Mark Stephens (mark@idrsolutions.com)
 * Contributor(s):
 *
 */
package org.jpedal.objects.acroforms;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.jpedal.gui.ShowGUIMessage;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Strip;

import org.jpedal.parser.PdfStreamDecoder;

/**
 * @author chris
 *
 * reads the form map object and sets up another map which is specified in FormsMapData.txt
 */
public class FormStream extends OverStream{
    
    /** keep the FormXObject for all uses and for access to the fonts decoded */
    private FormXObject appearanceObject = null;
    
    /** store of the formField used for this form*/
    protected Map currentField;
    
    protected int keySize;
    
    protected String[] commands;

    //only display ONCE
	private static boolean messageShown=false;

    
    /**stop anyone creating empty  instance*/
    protected FormStream(){}
    
	/**
	 * initialize internal structure
	 */
	public FormStream(PdfObjectReader inCurrentPdfFile) {
		type=DefaultAcroRenderer.FORM;
		currentPdfFile = inCurrentPdfFile;
		init();
	}
	
	protected void init(){
		
	
	}
    
    /**
     * takes in a FormObject already populated with values for the child to overwrite
     */
    private FormObject createAppearanceString(FormObject parentForm,Map formField){
        
    	currentField = formField;
    	
    	//check the field does not already exist as a form field
    	//this method should only be called if the parent Rect entry is null, So check this Rect
		Object rectTocheck = currentPdfFile.resolveToMapOrString("Rect",currentField.get("Rect"));
		if(!addItem(rectTocheck)){
			return null;
		}
        
		//scan through stream first resolving 'forward references'
        parseStream(parentForm);
        
        if(type!=DefaultAcroRenderer.ANNOTATION)
	    	decodeStream(parentForm);
        
        if(debugSpecific)
            System.out.println("createdParent="+parentForm);
        
        return parentForm;
    }
	
	/**
	 * create the form object from the formField
	 */
	public FormObject createAppearanceString(Map formField,PdfObjectReader inCurrentPdfFile){
//        System.out.println("field="+ConvertToString.convertMapToString(formField,inCurrentPdfFile));
        
		FormObject formObject = new FormObject();
		currentPdfFile = inCurrentPdfFile;
		currentField = formField;
	    
	    //check the field does not already exist as a form field
		Object rectTocheck = currentPdfFile.resolveToMapOrString("Rect",currentField.get("Rect"));
		//May also cause a problem if different fields have same Rect entry
	    //If Rect entry is null, it assumes its a buttonGroup
		if(rectTocheck!=null && (!addItem(rectTocheck))){
			return null;
		}
		
		//scan through stream first resolving 'forward references'
	    parseStream(formObject);
	    
	    if(type!=DefaultAcroRenderer.ANNOTATION)
	    	decodeStream(formObject);
	    
	    if(debugSpecific)
            System.out.println("createdNormal="+formObject);
        
	    return formObject;
	    
	}

	/**
     * parses over the stream decoding any values that don't depend on other values in seperate parts of the map
     */
	protected void parseStream(FormObject formObject) {
		
	    //explicitly set some values
	    formObject.setBorder(null);
	    
	    //remove value "P" as is parent Object
	    currentField.remove("P");
	    
	    //order the commands so they have any fields decoded first
		createOrderedCommandArray();
		
		for(int i=0;i<commands.length;i++){
			String command = commands[i];
			
			Object field = currentPdfFile.resolveToMapOrString(command, currentField.get(command));
			
			if(debug){
        		System.out.println("Parsing "+command+" "+field);
			}
	        parseCommand(command,field,formObject);
	        
	    }
		
		if(formObject.fieldName==null){
			Object parent = currentPdfFile.resolveToMapOrString("Parent",currentField.get("Parent"));
			if(parent instanceof Map){
				String fieldName = (String) currentPdfFile.resolveToMapOrString("T",((Map)parent).get("T"));
				formObject.setFieldName(fieldName);
			}else if(parent!=null){
			}
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
			}else if(command.equals("Kids")){
				commands[endnum] = command;
				endnum--;
			}else if(command.equals("H")){
				commands[endnum] = command;
				endnum--;
			}else if(command.equals("I")){
				commands[endnum] = command;
				endnum--;
			}else {
				commands[num] = command;
				num++;
			}
		}
	}

	protected boolean parseCommand(String command, Object field,FormObject formObject) {

		//flag to show if found so we can easily separate out 
		//generic from Annot specific
		boolean notFound=false;
		
		if(command.equals("Parent")){
			//just ignore
			if(field instanceof Map) {
				Map parentMap = (Map) field;
				
				if(parentMap.containsKey("V")){
					//on state for radio buttons and comboboxes groups
					commandV(field,formObject);
				}
			}
		}else if(command.equals("V")){
            commandV(field, formObject);
        }else if(command.equals("Q")){
		    formObject.setHorizontalAlign(field);
			
        }else if(command.equals(("AP"))){
            //left till decodeStream()
        }else if(command.equals(("DA"))){
            //needs text value so done in decode stream
            
        }else if(command.equals(("TU"))){
            /**
             * user name to be used when generating error or status messages for the field
             */
            formObject.setUserName((String)field);
            LogWriter.writeFormLog("{stream} userName NOT IMPLEMENTED="+field,debugUnimplemented);
            
        }else if(command.equals(("T"))){
            /**partial field name */
            formObject.setFieldName((String)field);
            if(debug)
                System.out.println("fieldName="+field);
            
        }else if(command.equals(("TM"))){
            /** mapping name to be used when exporting form data from the document */
            formObject.setMapName((String)field);
            LogWriter.writeFormLog("{stream} mapName NOT IMPLEMENTED="+field,debugUnimplemented);
            
        }else if(command.equals(("AS"))){
            /**
             * selects the applicable appearance stream if more than one entry in appearance dictionary
             */
            String state = null;
            if(field instanceof Map){
                Map mapField = (Map)field;
                mapField.remove("PageNumber");
                if(mapField.containsKey("rawValue")){
                    state = (String) currentPdfFile.resolveToMapOrString("rawValue",mapField.get("rawValue"));
                    
                    if(mapField.size()>1){
                        LogWriter.writeFormLog("{stream} AS IS MAP UNKNOWN field="+field,debugUnimplemented);
                    }
                }else {
                    LogWriter.writeFormLog("{stream} AS IS MAP field="+field,debugUnimplemented);
                }
            }else {
                state = (String) field;
            }
            
            state = Strip.checkRemoveLeadingSlach(state);
            
            formObject.setDefaultState(state);
            
            if(debug)
                System.out.println("AS defaultState="+field);
            
        }else if((command.equals(("F")))){
            workOutCharachteristic((String)field,formObject);
            
        }else if(command.equals(("Kids"))){
            //ignore as needs access to other values, so is decoded in decodeStream
        }else if(command.equals(("Opt"))){
            /**
             * choice fields only
             * an array of options as a list of items to choose from
             * each element has a string value, and may have 
             * 	an appearance string which should be used when that item is selected
             */
            String[] items;
            formObject.valuesMap = new HashMap();
            
            if(field instanceof String)
                items = populateItemsArrayWithValues((String) field,formObject.valuesMap);
            else
                items=populateItemsArrayWithValues((String) currentPdfFile.resolveToMapOrString("rawValue", ((Map) field).get("rawValue")),formObject.valuesMap);
            
            if(formObject.valuesMap.size()<1)
            	formObject.valuesMap = null;
            
            formObject.setlistOfItems(items);
            
            if(debug)
                System.out.println("Opt - list for the choice field="+ConvertToString.convertArrayToString(formObject.itemsList));
        }else if(command.equals(("BS"))){
            /**
             * if no BS or Border entry present a solid border of 1point is used
             * 
             * Dictionary - 
             * Type must be Border
             * W width in points (if 0 no border, default =1)
             * S style - (default =S) 
             * 	S=solid, D=dashed (pattern specified by D entry below), B=beveled(embossed appears to above page), 
             * 	I=inset(engraved appeared to be below page), U=underline ( single line at bottom of boundingbox)
             * D array phase - e.g. [a b] c means:-  a=on blocks,b=off blocks(if not present default to a), c=start of off block preseded by on block.
             * 	i.e. [4] 6 :- 1=off 2=on 3=on 4=on 5=on 6=off 7=off 8=off 9=off etc...
             */
            formObject.setBorder(field);
            
            if(debug)
                System.out.println("BS - border="+formObject.border);
            
        }else if(command.equals(("Ti"))){
            /**
             * the indexed position in the Opt array of the first visible option
             */
            
            formObject.setTopIndex(new int[]{Integer.parseInt((String)field)});
            
            if(debug)
                System.out.println("TI - index of item in array that is currently selected="+formObject.topIndex);
        }else if(command.equals(("MaxLen"))){
            /** maximum length the fields text can be */
            formObject.setMaxTextLength(Integer.parseInt((String)field));
            
            if(debug)
                System.out.println("MaxLen - max length of text="+formObject.maxTextLength);
        }else if(command.equals(("Rect"))){
            createBoundsRectangle((String)currentPdfFile.resolveToMapOrString("Rect", currentField.get("Rect")),formObject);
        }else if(command.equals(("FT"))){
            /**use formObject.types
             * field type accessed by array as specified in [ ] below
             * 
             * the type of field this dictionary discribes
             * Btn = button [0]
             * Tx = text [1]
             * Ch = choice [2]
             * Sig = signature field [3]
             */
            String type=null;
            if(field instanceof Map){
                Map mapField = (Map)field;
                mapField.remove("PageNumber");
                if(mapField.containsKey("rawValue")){
                    type=Strip.checkRemoveLeadingSlach((String) currentPdfFile.resolveToMapOrString("rawValue", mapField.get("rawValue")));
                    
                    if(mapField.size()>1){
                        LogWriter.writeFormLog("{stream} type IS MAP UNKNOWN field="+field,debugUnimplemented);
                        
                    }
                }else {
                    LogWriter.writeFormLog("{stream} type IS MAP field="+field,debugUnimplemented);
                }
            }else {
                type=Strip.checkRemoveLeadingSlach((String) field);
            }
            
            if(type==null){
                if(debug)
                    System.out.println("type=null, field="+ConvertToString.convertMapToString(currentField,currentPdfFile));//KEEP in to stop null pointers
            }else if(type.equals("Btn")){
                formObject.setType(FormObject.FORMBUTTON);
            }else if(type.equals("Tx")){
                formObject.setType(FormObject.FORMTEXT);
            }else if(type.equals("Ch")){
                formObject.setType(FormObject.FORMCHOICE);
            }else if(type.equals("Sig")){
                formObject.setType(FormObject.FORMSIG);
            }else {
                if(debug)
                    System.out.println("Unsupported FIELD type "+type);
            }
            
            if(debug)
                System.out.println("type="+formObject.resolveType(formObject.type));
            
        }else if(command.equals(("MK"))){
        	commandMK((Map)field,formObject);
        }else if(command.equals(("Ff"))){
            commandFf(formObject);
        }else if(command.equals("AA")){
            /**
             * trigger events dictionary
             * E = a dictionary - an action when the cursor enters the form
             * X = a dictionary - an action when the cursor exits the form
             */
            resolveAdditionalAction(field,formObject);
            
        }else if(command.equals("Type")){
            /*ONES TO IGNORE*/
        }else if(command.equals("Subtype")){
        	if(field.equals("/Widget")){
        		//ignore
        	}else {
        		notFound = true;
        	}
        }else if(command.equals("PageNumber")){
            /*MARK IMPLEMENTED ELSEWHERE*/
        	formObject.setPageNumber(field);
        }else if(command.equals("StructParent")){
            /* this annotations entry in the structual parent tree */
            LogWriter.writeFormLog("{stream} CHECK 'StructParent' NOT Implemented",debugUnimplemented);
        }else if(command.equals("A")){
            commandA(field, formObject);
            
        }else if(command.equals("DV")){
            /** 
             * the default value for which fields revert to when a rest form command is actioned
             */
            if(field instanceof String){
                formObject.setDefaultValue((String)field);
                if(debug)
                    System.out.println("{stream} defaultValue="+field);
            }else if(field instanceof Map){
                Map dvMap =(Map) field; 
                if(dvMap.containsKey("rawValue")){
                    formObject.setDefaultValue((String)currentPdfFile.resolveToMapOrString("rawValue", dvMap.get("rawValue")));
                }else if(dvMap.containsKey("PageNumber")){
                    //ignore as sorted elsewere
                }else {
                    LogWriter.writeFormLog("{stream} unknown entry as Map DV command NOT IMPLEMENTED field="+dvMap,debugUnimplemented);
                }
            }else{
                LogWriter.writeFormLog("{stream} unknown DV command NOT IMPLEMENTED field="+field,debugUnimplemented);
            }
            
        }else if(command.equals("I")){
            /* decoded in decodeStram() as value of V needed */
        }else if(command.equals("H")){
            /*
             * decoded in decodeStream() 
             */
        }else if(command.equals("DR")){
            //ignore DEFAULT MAP as works without
            
            if(field instanceof Map){
                //	        Map curMap = (Map)field;
                LogWriter.writeFormLog("{stream} DR command this Must have a Font entry used for the default font with text map UNIMPLEMENTED field"
                        /*+"="+field*/,debugUnimplemented);
            }else {
                LogWriter.writeFormLog("{stream} DR command this Must have a Font entry used for the default font with text non map UNIMPLEMENTED field"
                        /*+"="+field*/,debugUnimplemented);
            }
            
        }else if(command.equals("Lock")){
            /**
             * in signiture fields
             * specifies a list of form fields to lock when signed
             * 
             * Type - should always be SigFieldLock so ignore
             * Action - should be -
             * 	All - for all fields need locking
             * 	Include - for all fields in 'Fields' array need locking
             * 	Exclude - for all fields except those in 'Fields' array
             * Fields - the array of fields (needed if include or exclude is specified)
             */
            
            LogWriter.writeFormLog("{stream} Lock command UNIMPLEMENTED only needed for signiture fields, field="+field,debugUnimplemented);
            
        }else if(command.equals("NeedAppearances")){
            //ignore as works without
        }else if(command.equals("NM")){
        	commandNM(formObject, field);
        }else if(command.equals("obj")){
            //ignore as is MARK's store for this objects ref in the pdf
        //}else if(command.equals("RV")){//TODO @chris needs to be implemented properly
        //    System.out.println("(internal only RV command not implemented FormStream.parseCommand");
        }else {
            /* template if block
            if(command.equals("Subj")){
            System.out.println("exit formstream ###");
            ConvertToString.printStackTrace(1);
       			if(exitOnError)
       				System.exit(0);
        }else 
             */
            LogWriter.writeFormLog("{stream} UNIMPLEMENTED command - "+command+" in form Stream=\n\t"+field,debugUnimplemented);
            
            notFound=true;
        }
        
        return notFound;
	}

	protected void commandA(Object field, FormObject formObject) {
		/*
		 * action to be performed when field is activated
		 */
		Map mapA = new HashMap();
		mapA.put("A",field);
		resolveAdditionalAction(mapA,formObject);
	}
    
    /**
	 * read and setup the form flags for the Ff entry
	 */
	private void commandFf(FormObject formObject) {
		/**use formObject.flags
		 * to get flags representing field preferences the following are accessed by array address (bit position -1)
		 * 
		 * <b>bit positions</b>
		 * all
		 * 1=readonly - if set there is no interaction
		 * 2=required - if set the field must have a value when submit-form-action occures
		 * 3=noexport - if set the field must not be exported by a submit-form-action
		 * 
		 * Choice fields
		 * 18=combo - set its a combobox, else a list box
		 * 19=edit - defines a comboBox to be editable
		 * 20=sort - defines list to be sorted alphabetically
		 * 22=multiselect - if set more than one items can be selected, else only one
		 * 23=donotspellcheck - (only used on editable combobox) don't spell check
		 * 27=commitOnselchange - if set commit the action when selection changed, else commit when user exits field
		 * 
		 * text fields
		 * 13=multiline - uses multipul lines else uses a single line
		 * 14=password - a password is intended
		 * 21=fileselect -text in field represents a file pathname to be submitted
		 * 23=donotspellcheck - don't spell check
		 * 24=donotscroll - once the field is full don't enter anymore text.
		 * 25=comb - (only if maxlen is present, (multiline, password and fileselect are CLEAR)), the text is justified across the field to MaxLen
		 * 26=richtext - use richtext format specified by RV entry in field dictionary 
		 * 
		 * button fields
		 * 15=notoggletooff - (use in radiobuttons only) if set one button must always be selected
		 * 16=radio - if set is a set of radio buttons
		 * 17=pushbutton - if set its a push button
		 * 	if neither 16 nor 17 its a check box
		 * 26=radiosinunison - if set all radio buttons with the same on state are turned on and off in unison (same behaviour as html browsers)
		 */
		String flag=(String)(currentPdfFile.resolveToMapOrString("Ff",currentField.get("Ff")));
		if(flag!=null){
		    int flagValue = Integer.parseInt(flag);
		    
		    boolean[] flags = new boolean[32];
		    /**/
		    flags[1]=(flagValue & READONLY)==READONLY;
		    flags[2]=(flagValue & REQUIRED)==REQUIRED;
		    flags[3]=(flagValue & NOEXPORT)==NOEXPORT;
		    flags[12]=(flagValue & MULTILINE)==MULTILINE;
		    flags[13]=(flagValue & PASSWORD)==PASSWORD;
		    flags[14]=(flagValue & NOTOGGLETOOFF)==NOTOGGLETOOFF;
		    flags[15]=(flagValue & RADIO)==RADIO;
		    flags[16]=(flagValue & PUSHBUTTON)==PUSHBUTTON;
		    flags[17]=(flagValue & COMBO)==COMBO;
		    flags[18]=(flagValue & EDIT)==EDIT;
		    flags[19]=(flagValue & SORT)==SORT;
		    flags[20]=(flagValue & FILESELECT)==FILESELECT;
		    flags[21]=(flagValue & MULTISELECT)==MULTISELECT;
		    flags[22]=(flagValue & DONOTSPELLCHECK)==DONOTSPELLCHECK;
		    flags[23]=(flagValue & DONOTSCROLL)==DONOTSCROLL;
		    flags[24]=(flagValue & COMB)==COMB;
		    flags[25]=(flagValue & RICHTEXT)==RICHTEXT;//same as RADIOINUNISON
		    flags[25]=(flagValue & RADIOINUNISON)==RADIOINUNISON;//same as RICHTEXT
		    flags[26]=(flagValue & COMMITONSELCHANGE)==COMMITONSELCHANGE;
		    
		    formObject.setFlags(flags);
		    
		    if(flags[3]|| flags[22] || flags[24] || flags[26]){
		        LogWriter.writeFormLog("{stream} new flags (3 22 24 26) UNIMPLEMENTED flags - 3="+
		                flags[3]+" 22="+flags[22]+" 24="+flags[24]+" 26= "+flags[26],debugUnimplemented);
		    }
		    
		    if(debug){
		        System.out.println("Ff values flags="+
		                ConvertToString.convertArrayToString(formObject.flags)+"\n");
		    }
		}
	}

	/**
	 * setup the unique name for NM command
	 */
	protected void commandNM(FormObject formObject, Object curField) {
		//the unique name for the field
		formObject.setFieldName((String)curField);
	}
	
	/**
	 * resolves the V command
	 */
	private void commandV(Object field, FormObject formObject) {
		/**
         * Pushbutton does not use
         * Checkbox use to identify its appearance state
         * Radio button - parent V appearance state of currently ON child field (default is off)
         * Text use as the fields text value
         * Choice fields V is text String, shows which option is currently selected
         */
		
		String curValue = null;
		//find value to check
		if(field instanceof Map){
		    Map curMap = (Map)field;
		    if(curMap.containsKey("rawValue")){
		        curValue = (String) currentPdfFile.resolveToMapOrString("rawValue", curMap.get("rawValue"));
		        
		    }else if(curMap.containsKey("Type") && 
		            Strip.checkRemoveLeadingSlach((String) currentPdfFile.resolveToMapOrString("Type", curMap.get("Type"))).equals("Sig")){
		        /*ignore as is for Signiture fields*/
		    }else {
		        LogWriter.writeFormLog("{stream} V Map UNKNOWN="+field,debugUnimplemented);
		    }
		}else {
		    curValue = (String)field;
		}
		
		//check value
		if(curValue!=null){
		    String typeField = Strip.checkRemoveLeadingSlach((String)currentPdfFile.resolveToMapOrString("FT",currentField.get("FT")));
		    if(typeField.equals("Btn")){//button
		        
		        String flag=(String)(currentPdfFile.resolveToMapOrString("Ff",currentField.get("Ff")));
		        if(flag!=null){
		            int flagValue = Integer.parseInt(flag);
		            if((flagValue & RADIO)==RADIO){//isRadioButton
		                
		                formObject.setChildOnState(curValue);
		                
		                if(debug)
		                    System.out.println("Ff - radiobutton selectedChild state="+formObject.onState);
		                
		            }else if(!((flagValue & PUSHBUTTON)==PUSHBUTTON)){//checkbox
		                
		                formObject.setCurrentState(curValue);
		                
		                if(debug)
		                    System.out.println("Ff - checkBox selected state="+formObject.currentState);
		            }
		        }else {
		        	//no Ff value means defaults to 0, which is RADIO false, PUSHBUTTON false
		        	//namely checkbox
		        	formObject.setCurrentState(curValue);
		        }
		        
		    }else if(typeField.equals("Tx")){//text
		        
		        //hande any escape charcters
		        StringBuffer rawText=new StringBuffer(curValue);
		        int count=rawText.length();
		        StringBuffer scannedText=new StringBuffer();
		        //scannedText.setLength(count);
		        char c;
		        for(int i=0;i<count;i++){
		            c=rawText.charAt(i);
		            if(c=='\\'){
		                i++;
		                c=rawText.charAt(i);
		                if(c=='t')
		                    scannedText.append('\t');
		                else if(c=='n')
		                    scannedText.append('\n');
		                else if(c=='r')
		                    scannedText.append('\r');
		                else 
		                    scannedText.append(c);
		            }else
		                scannedText.append(c);
		        }
		        
		        formObject.setTextValue(scannedText.toString());
		        
		        if(debug)
		            System.out.println("value - text for text fields="+formObject.textString);
		        
		    }else if(typeField.equals("Ch")){//choice
		    	String selectedItem = (String)currentPdfFile.resolveToMapOrString("V", curValue);
		        formObject.setSelectedItem(selectedItem);
		        
		        if(debug)
		            System.out.println("value - choice currently selected String item="+formObject.textString);
		        
		    }else if(typeField.equals("Sig")){//signiture
		        
		        LogWriter.writeFormLog("{stream} value - signiture value NOT IMPLEMENTED field="+curValue,debugUnimplemented);
		        
		    }else {
		        LogWriter.writeFormLog("{stream} NOT IMPLEMENTED command=V field="+curValue+
		                " currentField="+ConvertToString.convertMapToString(currentField,currentPdfFile),debugUnimplemented);
		    }
		}
	}

    /**
	 * defines actions to be exicuted on events 'Trigger Events'
     *
     * @Action This is where the raw data is parsed and put into the FormObject
	 */
	private  void resolveAdditionalAction(Object field,FormObject formObject) {

        /**
	     * entries NP, PP, FP, LP never used
	     * A action when pressed in active area ?some others should now be ignored?
	     * E action when cursor enters active area
	     * X action when cursor exits active area
	     * D action when cursor button pressed inside active area
	     * U action when cursor button released inside active area
	     * Fo action on input focus
	     * Bl action when input focus lost
	     * PO action when page containing is opened, 
	     * 	actions O of pages AA dic, and OpenAction in document catalog should be done first
	     * PC action when page is closed, action C from pages AA dic follows this
	     * PV action on viewing containing page
	     * PI action when no longer visible in viewer
	     * K action on - [javascript]
	     * 	keystroke in textfield or combobox
	     * 	modifys the list box selection
	     * 	(can access the keystroke for validity and reject or modify)
	     * F the display formatting of the field (e.g 2 decimal places) [javascript]
	     * V action when fields value is changed [javascript]
	     * C action when another field changes (recalculate this field) [javascript]
	     */


        if(field instanceof Map){
	        Map fieldMap = (Map)field;
	        if(debug)
	            System.out.println("actionfield="+ConvertToString.convertMapToString(fieldMap,currentPdfFile));
	        
	        Iterator iter = fieldMap.keySet().iterator();
	        while(iter.hasNext()){
	            String iD = (String)iter.next();
	            
	            if(iD.equals("A")){//fieldMap.containsKey("A")){
//	            	Altered to allow for Obj ref
	            		Object Aobj=fieldMap.get("A");
	            		if(Aobj instanceof String)
	            			Aobj=currentPdfFile.readObject((String)Aobj,false,null);
	                Object data= currentPdfFile.resolveToMapOrString("A", Aobj);
					
	                if(data instanceof Map){
	                    Map aDataMap = (Map) data;
	                    
	                    Map activateData = new HashMap();
	                    
	                    if(aDataMap.containsKey("S")){
	                        String command = (String) currentPdfFile.resolveToMapOrString("S",aDataMap.get("S"));
	                        command = Strip.checkRemoveLeadingSlach(command);
	                        if(command.equals("Named")){
	                            if(aDataMap.containsKey("N")){
	                                String nameCmd = (String) currentPdfFile.resolveToMapOrString("N", aDataMap.get("N"));
	                                nameCmd = Strip.checkRemoveLeadingSlach(nameCmd);
	                                
	                                if(nameCmd.equals("Print")){
	                                    LogWriter.writeFormLog("Named Action=Print PageNumber="+currentPdfFile.resolveToMapOrString("PageNumber", aDataMap.get("PageNumber")),debugUnimplemented);
	                                    activateData.put("Print","currentPage");
	                                }else if(nameCmd.equals("SaveAs")){
	                                	//TODO save the pdf, with changes to forms
	                                	
	                                	LogWriter.writeFormLog("Named Action=SaveAs pagenumber="+currentPdfFile.resolveToMapOrString("Pagenumber", aDataMap.get("PageNumber")),debugUnimplemented);
	                                	activateData.put("SaveAs","currentPage");
	                                }else if(nameCmd.startsWith("AcroForm:")){
	                                	//Named action, spec says optional to viewer application to resolve
	                                	if(debug)
	                                		System.out.println("Named action AcroForm:"+nameCmd);
	                                }else if(nameCmd.startsWith("NextPage")){
	                                	//move to next page
	                                	LogWriter.writeFormLog("Named action NextPage NOT implemented"+nameCmd,debugUnimplemented);
	                                }else if(nameCmd.startsWith("ZoomTo")){
	                                	//bring up zoom options
	                                	
	                                }else if(nameCmd.startsWith("FullScreen")){
	                                	//bring up page in full screen
	                                	LogWriter.writeFormLog("Named action FullScreen NOT implemented"+nameCmd,debugUnimplemented);
	                                }else {
	                                    LogWriter.writeFormLog("{stream} Named Action NOT IMPLEMENTED command="+nameCmd+
	                                            " field="+ConvertToString.convertMapToString(aDataMap,currentPdfFile),debugUnimplemented);
	                                }
	                            }
	                        }else if(command.equals("ResetForm")){
	                            activateData.put("ResetForm", Boolean.TRUE);
	                            if(debug)
	                                System.out.println("{stream} ResetForm field="+ConvertToString.convertMapToString(aDataMap,currentPdfFile));
	                            
	                        }else if(command.equals("SubmitForm")){
	                        	if(aDataMap.containsKey("Fields")){
		                        	StringTokenizer fieldsTok = new StringTokenizer((String) aDataMap.get("Fields"),"[]()");
		                        	String tok,preName = null;
		                        	StringBuffer names = new StringBuffer();
		                        	while(fieldsTok.hasMoreTokens()){
		                        		tok = fieldsTok.nextToken();
		                        		if(tok.indexOf(".x")!=-1){
		                        			preName = tok.substring(tok.indexOf(".")+1,tok.indexOf(".x")+1);
		                        		}
		                        		if(tok.indexOf(" R")!=-1){
		                        			tok = (String) currentPdfFile.readObject(tok.trim(),false,null).get("T");
		                        			if(preName!=null){
		                        				names.append(preName);
		                        			}
		                        			names.append(tok.substring(1,tok.length()-1));
		                        			names.append(',');
		                        		}
		                        	}
		                        	aDataMap.put("Fields",names.toString());
	                        	}
	                        	
	                        	activateData.put("SubmitForm",aDataMap);
	                            
	                        }else if(command.equals("JavaScript")){
	                            LogWriter.writeFormLog("{stream} JavaScript field="+ConvertToString.convertMapToString(aDataMap,currentPdfFile),debugUnimplemented);
	                            
	                        }else if(command.equals("Hide")){
	                            
	                            Map hideMap = new HashMap();
	                            getHideMap(aDataMap, hideMap);
	                            
	                            activateData.put("Hide",hideMap);
	                            
	                        }else if(command.equals("URI")){
	                        	//URL command not implemented
	                        	String url = removeBrackets((String)aDataMap.get("URI"));
	                        	activateData.put("URL",url);
	                        }else if(command.equals("Launch")){
	                            LogWriter.writeFormLog("{stream} launch activate action NOT IMPLEMENTED",debugUnimplemented);
	                        }else if(command.equals("GoTo")){
	                        	//A /GoTo action with value "D" specifiying a destination to jump to
	                        	LogWriter.writeFormLog("{FormStream.resolveAdditionalAction} /GoTo action NOT IMPLEMENTED",debugUnimplemented);
	                        }else if(command.equals("GoToR")){
	                        	//A /GoToR action is a goto remote file action,
	                        	//F specifies the file
	                        	//D specifies the location or page
	                        	
	                        	Map gotomap = new HashMap();
	                        	
	                        	gotomap.put("page",aDataMap.get("D"));
	                        	Map dataMap = (Map)currentPdfFile.resolveToMapOrString("F", aDataMap.get("F"));
	                        	gotomap.put("type",dataMap.get("Type"));
	                        	gotomap.put("file",dataMap.get("F"));
	                        	
	                        	activateData.put("GoToR",gotomap);
	                        }else {
	                            LogWriter.writeFormLog("{stream} UNKNOWN command for Activate Action command="+
	                                    command+" field="+ConvertToString.convertMapToString(aDataMap,currentPdfFile),debugUnimplemented);
	                        }
	                        
	                    }else{
	                        LogWriter.writeFormLog("{stream} Activate Action UNKNOWN command NOT IMPLEMENTED field="+
	                                ConvertToString.convertMapToString(aDataMap,currentPdfFile),debugUnimplemented);
	                    }
	                    
	                    formObject.setActivateAction(activateData);
	                }else{
	                    LogWriter.writeFormLog("{stream} A additionalAction NON Map UNIMPLEMENTED",debugUnimplemented);
	                }
	            }else if(iD.equals("E")){//fieldMap.containsKey("E")){
	            	
	            	Map enteredData =(Map) currentPdfFile.resolveToMapOrString("E", fieldMap.get("E"));
//	            	System.out.println("entered action ="+enteredData);
	               
	                String typeName =(String) currentPdfFile.resolveToMapOrString("S", enteredData.get("S"));
	                enteredData.put("command",typeName);
	                enteredData.remove("S");
	                
	                if(enteredData.containsKey("T")){
	                    Object fieldToAlter = currentPdfFile.resolveToMapOrString("T",enteredData.get("T"));
	                    enteredData.put("fields",fieldToAlter);
	                    enteredData.remove("T");
	                }
	                
	                Boolean hide = Boolean.TRUE;
	                if(enteredData.containsKey("H"))
	                    hide = Boolean.valueOf((String) currentPdfFile.resolveToMapOrString("H", enteredData.get("H")));
	                enteredData.put("hide",hide);
	                enteredData.remove("H");
	                
	                if(debug)
	                    System.out.println("Entered Action="+typeName+" hide="+hide+" data="+enteredData);
	                
	                formObject.setEnteredAction(enteredData);
	                
	            }else if(iD.equals("X")){//fieldMap.containsKey("X")){
	            	Map exitedData =(Map) currentPdfFile.resolveToMapOrString("X", fieldMap.get("X"));
//	                System.out.println("exited action="+ConvertToString.convertMapToString(exitedData,currentPdfFile));
	                
	                String typeName =(String) currentPdfFile.resolveToMapOrString("S", exitedData.get("S"));
	                exitedData.put("command",typeName);
	                exitedData.remove("S");
	                
	                if(exitedData.containsKey("T")){
	                    Object fieldToAlter = currentPdfFile.resolveToMapOrString("T",exitedData.get("T"));
	                    exitedData.put("fields",fieldToAlter);
	                    exitedData.remove("T");
	                }
	                
	                Boolean hide = Boolean.TRUE;
	                if(exitedData.containsKey("H"))
	                    hide = Boolean.valueOf((String) currentPdfFile.resolveToMapOrString("H", exitedData.get("H")));
	                exitedData.put("hide",hide);
	                exitedData.remove("H");
	                
	                if(debug)
	                    System.out.println("Exit Action="+typeName+" hide="+hide+" data="+exitedData);
	                formObject.setExitedAction(exitedData);
	            }else if(iD.equals("K")){//fieldMap.containsKey("K")){//@MARK
	                
	                LogWriter.writeFormLog("{stream} NOT IMPLEMENTED K command in FormStream.resolveAdditionalAction field="+currentPdfFile.resolveToMapOrString("K", fieldMap.get("K")),debugUnimplemented);
	            }else if(iD.equals("C")){//fieldMap.containsKey("C")){//@MARK
	                
	                LogWriter.writeFormLog("{stream} NOT IMPLEMENTED C command in FormStream.resolveAdditionalAction field="+currentPdfFile.resolveToMapOrString("C", fieldMap.get("C")),debugUnimplemented);
	            }else if(iD.equals("D")){//fieldMap.containsKey("D")){//@MARK
	                
	                LogWriter.writeFormLog("{stream} NOT IMPLEMENTED D command in FormStream.resolveAdditionalAction field="+currentPdfFile.resolveToMapOrString("D", fieldMap.get("D")),debugUnimplemented);
	            }else if(iD.equals("F")){//fieldMap.containsKey("F")){//@MARK
	                
	                LogWriter.writeFormLog("{stream} NOT IMPLEMENTED F command in FormStream.resolveAdditionalAction field="+currentPdfFile.resolveToMapOrString("F", fieldMap.get("F")),debugUnimplemented);
	            }else if(iD.equals("PageNumber")){//fieldMap.containsKey("PageNumber")){
	                /**
	                 * ignore pagenumber as setup elsewhere
	                 * 
	                 LogWriter.writeFormLog("{stream} NOT IMPLEMENTED PageNumber command in FormStream.resolveAdditionalAction field="+fieldMap.getxx("PageNumber"),debugUnimplemented);
	                 */
	            }else if(iD.equals("Bl")){//fieldMap.containsKey("Bl")){//@MARK
	                //action when input focus lost
	                LogWriter.writeFormLog("{stream} NOT IMPLEMENTED Bl command in FormStream.resolveAdditionalAction field="+currentPdfFile.resolveToMapOrString("B1", fieldMap.get("B1")),debugUnimplemented);
	            }else if(iD.equals("V")){
	            	Object obj = currentPdfFile.resolveToMapOrString(iD,fieldMap.get(iD));
	            	if(obj instanceof Map){
	            		Map objmap = (Map) obj;
	            		String type = (String)objmap.get("S");
	            		if(type.equals("/JavaScript")){
	            			String tmp = (String)objmap.get("JS");
	            			//assign to javascript at correct point
	            			formObject.setValidateValueCommand(tmp);
	            		}
	            	}
	            }else {
	                LogWriter.writeFormLog("{stream} "+iD+" NOT IMPLEMENTED in FormStream.resolveAdditionalAction field="+currentPdfFile.resolveToMapOrString(iD, fieldMap.get(iD)),debugUnimplemented);
	            }
	        }
	    }else{
	        LogWriter.writeFormLog("{stream} AdditionAction is String UNIMPLEMENTED",debugUnimplemented);
	    }
	        
	}

    private String removeBrackets(String text) {
        return text.substring(1,text.length()-2);
    }

	/**
     * goes through the map and adds the required data to the hideMap and returns it
     */
    private void getHideMap(Map aDataMap, Map hideMap) {
        
        if(!Strip.checkRemoveLeadingSlach((String)currentPdfFile.resolveToMapOrString("S", aDataMap.get("S"))).equals("Hide")){
            LogWriter.writeFormLog("{stream} getHideMap has a NON Hide value field="+aDataMap,debugUnimplemented);
        }
        
        String[] fields;
        if(hideMap.containsKey("fields")){
            String[] mapToAdd = (String[]) hideMap.get("fields");
            fields = new String[mapToAdd.length+1];
            System.arraycopy(mapToAdd, 0, fields, 0, mapToAdd.length);
            fields[fields.length-1] = (String)currentPdfFile.resolveToMapOrString("T", aDataMap.get("T"));
        }else{
            fields = new String[] {(String)currentPdfFile.resolveToMapOrString("T", aDataMap.get("T"))};
        }
        hideMap.put("fields",fields);
        
        
        Boolean hideFlag = Boolean.TRUE;
        if(aDataMap.containsKey("H")){
            hideFlag = Boolean.valueOf((String) currentPdfFile.resolveToMapOrString("H", aDataMap
					.get("H")));
        }
        
        Boolean[] hideFlags;
        if(hideMap.containsKey("hide")){
            Boolean[] mapToAdd = (Boolean[]) hideMap.get("hide");
            hideFlags = new Boolean[mapToAdd.length+1];
            System.arraycopy(mapToAdd, 0, hideFlags, 0, mapToAdd.length);
            hideFlags[hideFlags.length-1] = hideFlag;
        }else{
            hideFlags = new Boolean[] {hideFlag};
        }
        hideMap.put("hide",hideFlags);
        
        
        if(aDataMap.containsKey("Next")){
            getHideMap((Map)currentPdfFile.resolveToMapOrString("Next", aDataMap.get("Next")),hideMap);
        }
    }

    /**
	 * goes down through all Map representations,
	 * prints { 'nextMap' }
	 * or NON map
	 */
	private  void trickleThroughMaps(Map downField,String commandName,boolean debug, boolean debugUnimplemented,FormObject formObject) {
	    
		if(commandName.equals("N")){
		    N(downField, debug, debugUnimplemented,formObject);
		}else if(commandName.equals("D")){
		    D(downField, debug, debugUnimplemented,formObject);
		}else if(commandName.equals("R")){
		    R(downField, debug, debugUnimplemented,formObject);
		}else {
		    
		    LogWriter.writeFormLog("{stream} NOT SINGLED in Trickle "+commandName+"=",debugUnimplemented);
		    
	        Iterator iter = downField.keySet().iterator();
	        while(iter.hasNext()){
	            String newCommand = (String) iter.next();
	            Object newField = currentPdfFile.resolveToMapOrString(newCommand, downField.get(newCommand));
	            
	            if(newField instanceof Map){
	                Map newFieldMap = (Map) newField;
	                
	                trickleThroughMaps(newFieldMap,newCommand,debug,debugUnimplemented,formObject);
	            }else{
	                
	                if(newCommand.equals("SW")){
	                    /*
	                     * A always scale to fit bounding box
	                     * B only scale down
	                     * S only scale up
	                     * N never scale
	                     * default A
	                     */
	                    String actiontype = Strip.checkRemoveLeadingSlach((String)newField);
	                    if(actiontype.equals("N")){
	                       // ;//never scale
	                    }else {
			                LogWriter.writeFormLog("{stream} UNIMPLEMENTED SW field="+newField,debugUnimplemented);
	                    }
	        		    
	        		}else if(newCommand.equals("PageNumber")){
	        		    //ignore pagenumber as implemented elsewhere
	        		}else {
		                LogWriter.writeFormLog("{stream} NON SINGLED command="+newCommand+" field="+newField,debugUnimplemented);
	        		}
	            }
		    }
		}
	}

    /**
	 * decode the AP command, if any value needed has not been read yet, returns false,
	 * otherwise returns true, to say ap has been setup
	 */
	protected boolean commandAP(Map downField,FormObject formObject) {
		/**
         * has entries
         * N = normal appearance
         * R = rollover appearance ( mouse hovers over with no button pressed )
         * D = down appearance
         * may have MK
         * 
         * each on defines a formXObject
         */
		boolean apSet = false;
		
		Iterator downFieldIter = downField.keySet().iterator();
		while(downFieldIter.hasNext()){
			
			String downFieldCommand = (String) downFieldIter.next();
			
			Object downFieldWithinField = currentPdfFile.resolveToMapOrString(downFieldCommand, downField.get(downFieldCommand));
			
			if(debug)
				System.out.println("AP "+downFieldCommand+"="+downFieldWithinField+" - trickleTroughMaps() NOT IMPLEMENTED");
			
			if(downFieldWithinField instanceof Map){
//				System.out.println(elementNum+" Map="+downFieldCommand+"="+printToScreen((Map)downFieldWithinField));
				
				if(debug)
					System.out.println("CHECKING="+downFieldCommand+" {");
				trickleThroughMaps((Map)downFieldWithinField,downFieldCommand,debug,debugUnimplemented,formObject);
				if(debug)
					System.out.println("}");
			}else{
				if(debug)
					System.out.println("NON Map in AP="+downFieldCommand+"="+downField);
			}
		}
		
		apSet = true;
		return apSet;
	}

	/**
     * Iterate throught the kids to get the button groups
     */
    private void resolveKidsArray(Object rawDownField, boolean debug, boolean debugUnimplemented,FormObject formObject) {
    	
    		boolean isString=false;
    		StringTokenizer objectRefs=null;
    		Map downField=null;
    		Iterator downFieldIter=null;
    		String downFieldCommand;
    		Object downFieldWithinField;
    		
        Map kidData = new HashMap();
        
        
        //workout if string or map
        if(rawDownField instanceof String)
        		isString=true;
        
        //set values accordingly
        if(!isString){
        		downField=(Map)rawDownField;
        		downFieldIter = downField.keySet().iterator();
        }else{
        		objectRefs=new StringTokenizer(Strip.removeArrayDeleminators((String)rawDownField),"R");
        }
        
        while(((!isString)&&(downFieldIter.hasNext()))||((isString)&&(objectRefs.hasMoreTokens()))){
        	
	        	//get required value
	        	if(!isString){
	        		downFieldCommand = (String) downFieldIter.next();
	        		downFieldWithinField = currentPdfFile.resolveToMapOrString(downFieldCommand, downField.get(downFieldCommand));
	        	}else{
	        		downFieldCommand = objectRefs.nextToken().trim()+" R";
	        		downFieldWithinField=currentPdfFile.readObject(downFieldCommand,false,null);
	        	}
	        	
	        	if(downFieldWithinField instanceof Map){
	        		FormObject parentForm = formObject.duplicate();
	        		
	        		kidData.put(downFieldCommand,createAppearanceString(parentForm,(Map) downFieldWithinField));
	        		
	        	}else{
	        	}
        	
        }
        formObject.setKidData(kidData);
        
    }
	
	/**
     * decode N command
     */
	private void N(Map downField, boolean debug, boolean debugUnimplemented,FormObject formObject) {
		
//	    if(debugSpecific)
//	        System.out.println("command=N, field="+ConvertToString.convertMapToString(downField,currentPdfFile));
	    
	    if(downField.containsKey("MK")){
	    		Map map=(Map) currentPdfFile.resolveToMapOrString("MK", downField.get("MK"));	
	    		commandMK(map,formObject);
	    }
	    if(downField.containsKey("CachedStream"))
	    	currentPdfFile.readStreamIntoMemory(downField);
	    
	    if(downField.containsKey("DecodedStream")){
	    	if(!formObject.hasNormalOff()){
	    		if(appearanceObject==null)
	                appearanceObject = new FormXObject(currentPdfFile);
	            
		        formObject.setNormalAppOff(appearanceObject.decode(downField),null);
	            
	            if(showIconsOnCreate){
	            		ShowGUIMessage.showGUIMessage("N normalAppImage",formObject.normalOffImage,"normalAppImage");
	            }
	    	}
            
	    }else {//if no type, check for sub Objects
	        
	        Iterator iter = downField.keySet().iterator();
	        while(iter.hasNext()){
	            String stateTocheck = (String) iter.next();
	           
	            if(stateTocheck.equals("Off")){
            		Map offApp=(Map) currentPdfFile.resolveToMapOrString(stateTocheck, downField.get(stateTocheck));
					
		            if(appearanceObject==null)
		                appearanceObject = new FormXObject(currentPdfFile);
		            
		            formObject.setNormalAppOff(appearanceObject.decode(offApp),stateTocheck);
		            
		            if(showIconsOnCreate){
		            		ShowGUIMessage.showGUIMessage("N normalAppOff",formObject.normalOffImage,"normalAppOff");
		            }
		            
		        }else if(stateTocheck.equals("On")){
		        		Map onApp=(Map) currentPdfFile.resolveToMapOrString(stateTocheck, downField.get(stateTocheck));
					
		            if(appearanceObject==null)
		                appearanceObject = new FormXObject(currentPdfFile);
		            
		            formObject.setNormalAppOn(appearanceObject.decode(onApp),stateTocheck);
		            
		            if(showIconsOnCreate){
		            		ShowGUIMessage.showGUIMessage("normalAppOn",formObject.normalOnImage,"normalAppOn");
		            }
		            
		            if(!formObject.hasNormalOff()){
		                formObject.setNormalAppOff(OpaqueImage,null);
		            }
		            
		        }else if(stateTocheck.equals("PageNumber")){
		            continue;
		        }else {
		            
		        		//store so kids can retrieve later
		        		formObject.setStateToCheck(stateTocheck);
		        		
		            Object paramField=currentPdfFile.resolveToMapOrString(stateTocheck, downField.get(stateTocheck));
					
		            if(paramField instanceof Map){
			            Map onApp = (Map) paramField;
			            
			            if(appearanceObject==null)
			                appearanceObject = new FormXObject(currentPdfFile);
			            
			            formObject.setNormalAppOn(appearanceObject.decode(onApp),stateTocheck);
			            
			            if(showIconsOnCreate){
			            		ShowGUIMessage.showGUIMessage("N normalAppOn",formObject.normalOnImage,"normalAppOn");
			            }
			            
			            if(!formObject.hasNormalOff()){
			                formObject.setNormalAppOff(OpaqueImage,null);
			            }
			            
		            }else {
		                LogWriter.writeFormLog("{stream} String NOT IMPLEMENTED in FormStream.N stateTocheck="+stateTocheck+" field="+paramField,debugUnimplemented);
		            }
		            
		        }
	        }
	    }
	}
	
	/**
     * decode R command
     */
    private void R(Map downField, boolean debug, boolean debugUnimplemented,FormObject formObject) {
    	
//        if(debugSpecific)
//            System.out.println("command=R, field="+ConvertToString.convertMapToString(downField));
        
        if(downField.containsKey("MK")){
            LogWriter.writeFormLog("{stream} MK command in Rollover appearance field="+currentPdfFile.resolveToMapOrString("MK", downField.get("MK")),debugUnimplemented);//trickleThroughMaps((Map)downField.get("MK"),"MK",debug,debugUnimplemented);
        }
        
        if(downField.containsKey("CachedStream"))
	    	currentPdfFile.readStreamIntoMemory(downField);
        
        if(downField.containsKey("DecodedStream")){
            
            if(appearanceObject==null)
                appearanceObject = new FormXObject(currentPdfFile);
            
//            if(debugSpecific)
//                System.out.println("ROLL==========================================="
//                        +formObject.fieldName);
            formObject.setRolloverAppOff(appearanceObject.decode(downField));
            //ShowGUIMessage.showGUIMessage("rolloverApp",formObject.rolloverOffImage,"rolloverApp");
//            if(debugSpecific)
//                System.out.println("DONE=========");
            
        }else{//if no type, check for sub Objects
            
            Iterator iter = downField.keySet().iterator();
            while(iter.hasNext()){
                String stateTocheck = (String) iter.next();
               
                if(stateTocheck.equals("Off")){
    	            Map offApp = (Map) currentPdfFile.resolveToMapOrString(stateTocheck, downField.get(stateTocheck));
    	            
    	            if(appearanceObject==null)
		                appearanceObject = new FormXObject(currentPdfFile);
    	            
//    	            if(debugSpecific)
//    	                System.out.println(stateTocheck+" - rolloverOff============================="
//    	                        +formObject.fieldName);
    	            formObject.setRolloverAppOff(appearanceObject.decode(offApp));
    	            //ShowGUIMessage.showGUIMessage("rolloverAppOff",formObject.rolloverOffImage)),"rolloverAppOff");
//    	            if(debugSpecific)
//    	                System.out.println("DONE=========");
    	            
    	        }else if(stateTocheck.equals("On")){
    	            Map onApp = (Map) currentPdfFile.resolveToMapOrString(stateTocheck, downField.get(stateTocheck));
    	            
    	            if(appearanceObject==null)
		                appearanceObject = new FormXObject(currentPdfFile);
    	            
//    	            if(debugSpecific)
//    	                System.out.println(stateTocheck+" - rolloverOn============================="
//    	                        +formObject.fieldName);
    	            formObject.setRolloverAppOn(appearanceObject.decode(onApp));
    	            //ShowGUIMessage.showGUIMessage("rolloverAppOn",formObject.rolloverOffImage),"rolloverAppOn");
    	            
    	            if(!formObject.hasRolloverOff()){
    	                formObject.setRolloverAppOff(OpaqueImage);
    	            }
//    	            if(debugSpecific)
//    	                System.out.println("DONE=========");
    	            
    	        }else if(stateTocheck.equals("PageNumber")){
    	            continue;
    	        }else {
    	            
    	            Object paramField = currentPdfFile.resolveToMapOrString(stateTocheck, downField.get(stateTocheck));
    	            
    	            if(paramField instanceof Map){
    		            Map onApp = (Map) paramField;
    		            
    		            if(appearanceObject==null)
    		                appearanceObject = new FormXObject(currentPdfFile);
    		            
//    		            if(debugSpecific)
//    		                System.out.println(stateTocheck+" - rolloverOn========="+formObject.fieldName);
    		            formObject.setRolloverAppOn(appearanceObject.decode(onApp));
    		            //ShowGUIMessage.showGUIMessage("rolloverAppOn",formObject.rolloverOffImage,"rolloverAppOn");
    		            
    		            if(!formObject.hasRolloverOff()){
    		                formObject.setRolloverAppOff(OpaqueImage);
    		            }
    		            
//    		            if(debugSpecific)
//    		                System.out.println("DONE=========");
    		            
    	            }else {
    	                LogWriter.writeFormLog("{stream} String NOT IMPLEMENTED in FormStream.R stateTocheck="+stateTocheck+" field="+paramField,debugUnimplemented);
    	            }
    	            
    	        }
            }
        }
    }
	
	/**
     * decode D command
     */
	private void D(Map downField, boolean debug, boolean debugUnimplemented,FormObject formObject) {
	    
//	    if(debugSpecific)
//	        System.out.println("command=D, field="+ConvertToString.convertMapToString(downField));
	    
	    if(downField.containsKey("MK")){
	        LogWriter.writeFormLog("{stream} MK command in Down appearance field="+currentPdfFile.resolveToMapOrString("MK", downField.get("MK")),debugUnimplemented);//trickleThroughMaps((Map)downField.get("MK"),"MK",debug,debugUnimplemented);
	    }
	    
	    if(downField.containsKey("CachedStream"))
	    	currentPdfFile.readStreamIntoMemory(downField);
	    
	    if(downField.containsKey("DecodedStream")){
            
	        if(appearanceObject==null)
                appearanceObject = new FormXObject(currentPdfFile);
            
//            if(debugSpecific)
//                System.out.println("DOWN=========================================="
//                        +formObject.fieldName);
            formObject.setDownAppOff(appearanceObject.decode(downField));
            //ShowGUIMessage.showGUIMessage("downApp",formObject.downOffImage,"downApp");
//            if(debugSpecific)
//                System.out.println("DONE=========");
            
	    }else{//if no type, check for sub Objects
	        
	        Iterator iter = downField.keySet().iterator();
	        while(iter.hasNext()){
	            String stateTocheck = (String) iter.next();
	           
	            if(stateTocheck.equals("Off")){
	            	
		            Map offApp=(Map) currentPdfFile.resolveToMapOrString(stateTocheck, downField.get(stateTocheck));
					
		            if(appearanceObject==null)
		                appearanceObject = new FormXObject(currentPdfFile);
		            
//		            if(debugSpecific)
//		                System.out.println(stateTocheck+" - downOff==========================="
//		                        +formObject.fieldName);
		            formObject.setDownAppOff(appearanceObject.decode(offApp));
		            //ShowGUIMessage.showGUIMessage("downAppOff",formObject.downOffImage,"downAppOff");
//		            if(debugSpecific)
//		                System.out.println("DONE=========");
		            
		        }else if(stateTocheck.equals("On")){
		        		Map onApp=(Map) currentPdfFile.resolveToMapOrString(stateTocheck, downField.get(stateTocheck));
					
		            if(appearanceObject==null)
		                appearanceObject = new FormXObject(currentPdfFile);
		            
//		            if(debugSpecific)
//		                System.out.println(stateTocheck+" - downOn============================="
//		                        +formObject.fieldName);
		            formObject.setDownAppOn(appearanceObject.decode(onApp));
		            //ShowGUIMessage.showGUIMessage("downAppOn",formObject.downOffImage,"downAppOn");
		            
		            if(!formObject.hasDownOff()){
		                formObject.setDownAppOff(OpaqueImage);
		            }
//		            if(debugSpecific)
//		                System.out.println("DONE=========");
		            
		        }else if(stateTocheck.equals("PageNumber")){
		            continue;
		        }else {
		            
		        		Object paramField=currentPdfFile.resolveToMapOrString(stateTocheck, downField.get(stateTocheck));
					
		            if(paramField instanceof Map){
			            Map onApp = (Map) paramField;
			            
			            if(appearanceObject==null)
			                appearanceObject = new FormXObject(currentPdfFile);
			            
//			            if(debugSpecific)
//			                System.out.println(stateTocheck+" - On================================"
//			                        +formObject.fieldName);
			            formObject.setDownAppOn(appearanceObject.decode(onApp));
			            //ShowGUIMessage.showGUIMessage("downAppOn",formObject.downOffImage),"downAppOn");
			            
			            if(!formObject.hasDownOff()){
			                formObject.setDownAppOff(OpaqueImage);
			            }
//			            if(debugSpecific)
//			                System.out.println("DONE=========");
			            
		        	}else{
		        	    LogWriter.writeFormLog("{stream} String NOT IMPLEMENTED in FormStream.D stateTocheck="+stateTocheck+" field="+paramField,debugUnimplemented);
	            	}
		        }
	        }
	    }
	}

    /**  Filters the MK command and its properties
	 * 
	 * appearance characteristics dictionary  (all optional)
	 * R rotation on wiget relative to page
	 * BC array of numbers, range between 0-1 specifiying the border color
	 * 	number of array elements defines type of colorspace
	 * 		0=transparant
	 * 		1=gray
	 * 		3=rgb
	 * 		4=cmyk
	 * BG same as BC but specifies wigets background color
	 * 
	 * buttons only - 
	 * CA its normal caption text
	 * 
	 * pushbuttons only - 
	 * RC rollover caption text
	 * AC down caption text
	 * I formXObject defining its normal icon
	 * RI formXObject defining its rollover icon
	 * IX formXObject defining its down icon
	 * IF icon fit dictionary, how to fit its icon into its rectangle 
	 * 	(if specified must contain all following)
	 * 	SW when it should be scaled to fit ( default A)
	 * 		A always
	 * 		B when icon is bigger
	 * 		S when icon is smaller
	 * 		N never
	 * 	S type of scaling - (default P)
	 * 		P keep aspect ratio
	 * 		A ignore aspect ratio (fit exactly to width and hight)
	 * 	A array of 2 numbers specifying its location when scaled keeping the aspect ratio
	 * 		range between 0.0-1.0, [x y] would be positioned x acress, y up
	 * TP positioning of text relative to icon - (integer)
	 * 	0=caption only
	 * 	1=icon only
	 * 	2=caption below icon
	 * 	3=caption above icon
	 * 	4=caption on right of icon
	 * 	5=caption on left of icon
	 * 	6=caption overlaid ontop of icon
	 */
	protected void commandMK(Map data,FormObject formObject){
		
		Iterator iter = data.keySet().iterator();
		while(iter.hasNext()){
			String nextCommand = (String) iter.next();
			Object nextField =currentPdfFile.resolveToMapOrString(nextCommand, data.get(nextCommand));
			
			if(nextCommand.equals("CA")){//for button fields only
				formObject.setNormalCaption((String)nextField);
			}else if(nextCommand.equals("BC")){//border color
				Color borderColor = generateColorFromString((String)nextField);
				formObject.setBorderColor(borderColor);
			}else if(nextCommand.equals("BG")){//border background
				Color backgroundColor = generateColorFromString((String)nextField);
				formObject.setBackgroundColor(backgroundColor);
			}else if(nextCommand.equals("R")){//rotated (must be multipul of 90)
				formObject.setRotation(Integer.parseInt((String)nextField));
			}else if(nextCommand.equals("RC")){//rollover text  (pushbutton only)
				formObject.setRolloverCaption((String)nextField);
			}else if(nextCommand.equals("AC")){//down text  (pushbutton only)
				formObject.setDownCaption((String)nextField);
			}else if(nextCommand.equals("I")){//normal icon (formXObject pushbutton only)
				if(nextField instanceof Map){
					if(appearanceObject==null)
		                appearanceObject = new FormXObject(currentPdfFile);
		            
	                formObject.setNormalAppOff(appearanceObject.decode((Map)nextField),null);
		            
		            if(showIconsOnCreate){
		            	ShowGUIMessage.showGUIMessage("MKI normalAppImage",formObject.normalOffImage,"normalAppImage");
		            }
				}else {
		            
					LogWriter.writeFormLog("{stream} MK I NOT implemented field="+nextField,debugUnimplemented);
				}
			}else if(nextCommand.equals("RI")){//rollover icon (formXObject pushbutton only)
				LogWriter.writeFormLog("{stream} MK RI NOT implemented field="+nextField,debugUnimplemented);
			}else if(nextCommand.equals("IX")){//down icon (formXObject pushbutton only)
				LogWriter.writeFormLog("{stream} MK IX NOT implemented field="+nextField,debugUnimplemented);
			}else if(nextCommand.equals("IF")){
				/*icon fit dictionary, how to fit its icon into its rectangle (pushbutton fields only) 
				 * 	(if specified must contain all following)
				 * 	SW when it should be scaled to fit ( default A)
				 * 		A always
				 * 		B when icon is bigger
				 * 		S when icon is smaller
				 * 		N never
				 * 	S type of scaling - (default P)
				 * 		P keep aspect ratio
				 * 		A ignore aspect ratio (fit exactly to width and hight)
				 * 	A array of 2 numbers specifying its location when scaled keeping the aspect ratio
				 * 		range between 0.0-1.0, [x y] would be positioned x acress, y up
				 */
			    
			    if(nextField instanceof Map){
			        Map mapField = (Map)nextField;
			        if(mapField.containsKey("SW")){
			            String whenToScale = Strip.checkRemoveLeadingSlach((String)currentPdfFile.resolveToMapOrString("SW",mapField.get("SW")));
			            if(whenToScale.equals("A")){
			                //ignore as already scales to fit always
			                formObject.setWhenToScaleIcon("A");
			            }else if(whenToScale.equals("N")){
			                //never scale the icon
			                formObject.setWhenToScaleIcon("N");
			            }else {
			                LogWriter.writeFormLog("{stream} MK IF Map Unimplemented command="+whenToScale+
			                        " field="+nextField,debugUnimplemented);
			            }
			        }else if(mapField.size()<1){
			        	//no map Ignore
			        }else {
			            LogWriter.writeFormLog("{stream} MK IF unknown command type field="+mapField,debugUnimplemented);
			        }
			    }else {
					LogWriter.writeFormLog("{stream} MK IF String NOT implemented field="+nextField,debugUnimplemented);
			    }
			}else if(nextCommand.equals("TP")){
				/*positioning of text relative to icon - (integer)
				 * 	0=caption only
				 * 	1=icon only
				 * 	2=caption below icon
				 * 	3=caption above icon
				 * 	4=caption on right of icon
				 * 	5=caption on left of icon
				 * 	6=caption overlaid ontop of icon
				 */
		        formObject.setTextPosition(Integer.parseInt((String)nextField));
		        
			}else if(nextCommand.equals("PageNumber")){
				/* page number implemented elswhere */
			}else {
			    LogWriter.writeFormLog("{stream} MK command NOT IMPLEMENTED command="+nextCommand,debugUnimplemented);
			}
		}
	}

	/**
	 * extracts all items from <b>object</b> String and store in String array for use as element list
	 */
	private  String[] populateItemsArrayWithValues(String object,Map valuesMap) {
		String[] items;
		boolean inString=false;
		
		//@note the first () of the square brackets set may be needed to store for indexing.
		
		StringTokenizer token = new StringTokenizer(object,"[]()\\",true);
		
		items = new String[token.countTokens()];
		
		int i=0,brackets=0,sqBrk=0;
		if(false){
			/**ORIGINAL*/
			//token.nextToken();//ignore first token [
			StringBuffer valueToAdd=null;
			String val = null;
			while(token.hasMoreTokens()){
				val=token.nextToken();
				//if first ( initialise stringbuffer, and set inString,
				//else increment bracket counter and add to valueToAdd
				if(val.equals("(")){
					brackets++;
					if(brackets==1){
						inString=true;
						valueToAdd = new StringBuffer();
					}else{
						valueToAdd.append(val);
					}
					
				//if last ) "namely brackets==0" add valueToAdd into items array and set !inString,
				//else decrement bracket counter and add to valueToAdd
				}else if(val.equals(")")){
					brackets--;
					if(brackets==0){
						inString=false;
						if(sqBrk<2){
							items[i++]=valueToAdd.toString();
						}
					}else{
						valueToAdd.append(val);
					}
				}else if(!inString && val.equals("[")){
					sqBrk++;
					
				}else if(sqBrk>1 && val.equals("]")){
					sqBrk--;
					if(sqBrk==1){
						items[i++]=valueToAdd.toString();
					}
					
				}else if(inString){
					valueToAdd.append(val);
				}
			}
			if(brackets!=0)//catch uncoordinated array
				items[i++]=valueToAdd.toString();
		//end if
		}else {
//			token.nextToken();//ignore first token [
			StringBuffer valueToAdd=null;
			String val = null;
			String[] mapItems = new String[2];
			int m=0;
			boolean escape = false;
			while(token.hasMoreTokens()){
				val=token.nextToken();
				
				if(val.equals("\\") && !escape){
					escape = true;
					continue;//ignore the back slach for escape characters
				}else {
					escape = false;
				}
				
				//if first ( initialise stringbuffer, and set inString,
				//else increment bracket counter and add to valueToAdd
				if(val.equals("(")){
					brackets++;
					if(brackets==1){
						inString=true;
						valueToAdd = new StringBuffer();
					}else{
						valueToAdd.append(val);
					}
					
				//if last ) "namely brackets==0" add valueToAdd into items array and set !inString,
				//else decrement bracket counter and add to valueToAdd
				}else if(val.equals(")")){
					brackets--;
					if(brackets==0){
						inString=false;
						if(sqBrk<2){
							items[i++]=valueToAdd.toString();
						}else {
							if(m<2)//catch errors
								mapItems[m++] = valueToAdd.toString();
						}
					}else{
						valueToAdd.append(val);
					}
				}else if(!inString && val.equals("[")){
					sqBrk++;
					
				}else if(sqBrk>1 && val.equals("]")){
					sqBrk--;
					if(sqBrk==1){
						if(m==2){
							valuesMap.put(mapItems[0],mapItems[1]);
							m=0;
						}
						items[i++]=valueToAdd.toString();
					}
					
				}else if(inString){
					valueToAdd.append(val);
				}
			}
			if(brackets!=0)//catch uncoordinated array
				items[i++]=valueToAdd.toString();
		}//else end
		
		String[] valuesArray=new String[i]; 
		for(i=0;i<items.length;i++){
			
			if(items[i]!=null)
				valuesArray[i]=items[i].trim();
		}
		items=valuesArray;
		
		return items;
	}
	
	/**
     * decodes any streams that need previous data to be decoded first
     */
    protected void decodeStream(FormObject formObject) {
        for(int j=0;j<keySize;j++){
	        
	        String command =commands[j];
	        Object field = currentPdfFile.resolveToMapOrString(command, currentField.get(command));
			
	        if(debug){
	        		System.out.println("Decoding "+command+" "+field);
	        }
	        
	        //@interest: this does not call decodeCommand below in this class
	        //but in AnnotStream, thereby missing all the key items
	        decodeFormCommand(command,field,formObject);
	        
        }
    }

	protected boolean decodeFormCommand(String command, Object field,FormObject formObject) {
		if(field==null) return false;
		//flag to show if processed
		boolean notFound=false;
		
		if(command.equals(("AP"))){
        	boolean apSet = commandAP((Map)field,formObject);
			if(!apSet){
			}
            
        }else if(command.equals("I")){
            /*
             * for choice fields where the multi select flag is selected,
             * it is the items selected at startup, unless V has different values in which case V is used
             */
        	//TODO
            String[] optList = formObject.itemsList;

//            if(formObject.selectedItem!=null){
            	String defaultSelection = Strip.removeArrayDeleminators((String)field);
            	if(defaultSelection.indexOf(" ")!=-1){
            		//multiSelection
            		StringTokenizer tok = new StringTokenizer(defaultSelection," ");
            		int[] index = new int[tok.countTokens()];
            		int i=0;
            		while(tok.hasMoreTokens()){
            			index[i++] = Integer.parseInt(tok.nextToken());
            		}
            		formObject.setTopIndex(index);
            	}
//            	System.out.println("currentSelected="+formObject.selectedItem+" new="+defaultSelection);
//            }else {
//            	//if V command exists keep it, dont use this value
//            }
            
        }else if(command.equals("H")){
            commandH(field, formObject);
            
        }else if(command.equals("Kids")){
            /**
             * Radio buttons - each of the radio buttons as sub buttons of this set, each is a seperate checkbox field
             */
            if(debug)
                System.out.println("Kids=");//+field);
            
//            Altered code to handle field as Map or list of obj refs
            resolveKidsArray(field, debug, debugUnimplemented,formObject);
        }else if(command.equals(("DA"))){
        	byte[] stream=new byte[0];
        	if(field instanceof Map){
        		Map mapField = (Map)field;
        		mapField.remove("PageNumber");
        		if(mapField.containsKey("rawValue")){
        			Object test = currentPdfFile.resolveToMapOrString("rawValue", mapField.get("rawValue"));
        			if(test instanceof byte[])
        				stream = (byte[])test;
        			else
        				stream = ((String)test).getBytes();
        			
       				decodeFontCommandObj((String) currentPdfFile.resolveToMapOrString("rawValue", mapField.get("rawValue")),formObject);
        			
        			if(mapField.size()>1){
        				LogWriter.writeFormLog("{stream} type IS MAP UNKNOWN field="+field,debugUnimplemented);
        				
        			}
        		}else {
        			LogWriter.writeFormLog("{stream} DA IS MAP field="+field,debugUnimplemented);
        		}
        	}else {
        		Object test = field;
        		if(test instanceof byte[])
        			stream = (byte[])test;
        		else
        			stream = ((String)test).getBytes();
        		
       			decodeFontCommandObj((String) field,formObject);
        	}
        	
        	//this is font stream
        	/*stream*/
        	//turn into byte array and add
        	// (<text>)tj
        	String textString = formObject.contents;
        	if(textString!=null){
        		byte[] textbytes = textString.getBytes();
        		
	        	int streamLength = stream.length;
	        	byte[] newbytes = new byte[streamLength+textbytes.length];
	        	for(int i=0;i<newbytes.length;i++){
	        		if(i<streamLength)
	        			newbytes[i] = stream[i];
	        		else
	        			newbytes[i] = textbytes[i-streamLength];
	        	}
	        	
	        	//then send into stream decoder
	        	PdfStreamDecoder textDecoder = new PdfStreamDecoder();
	        	textDecoder.decodeStreamIntoObjects(newbytes);
	        	
	        	StringBuffer textData = textDecoder.getlastTextStreamDecoded();
	        	if(textData!=null)
	        		formObject.setTextValue(textData.toString());
        	}
        	
        }else {
        	notFound=true;
        }
		
        return notFound;
	}

	protected void commandH(Object field, FormObject formObject) {
		/**
		 * highlighting mode
		 * done when the mouse is pressed or held down inside the fields active area
		 * N nothing
		 * I invert the contents
		 * O invert the border
		 * P display down appearance stream, or if non available offset the normal to look down
		 * T same as P 
		 * 
		 * this overides the down appearance
		 * Default value = I
		 */
		String key = Strip.checkRemoveLeadingSlach((String)field);
		if(key.equals("T") || key.equals("P")){
		    if(formObject.hasDownImage()){
		        //has down appearance, leave
		    }else {
		        formObject.setOffsetDownApp();
		    }
		}else if(key.equals("N")){
		    //do nothing on press
		    formObject.setNoDownIcon();
		    
		}else if(key.equals("I")){
		    //invert the contents colors
			formObject.setInvertForDownIcon();
			
		}else if(key.equals("O")){
			//invert the border
			if(debugUnimplemented && !messageShown){
				System.out.println("FormStream.commandH caused this");
				
				messageShown=true;
				ConvertToString.printStackTrace(1);
			}
//			formObject.setInvertBorder();
		}else {
			LogWriter.writeFormLog("{FormStream.commandH} H command NOT IMPLEMENTED field="+field,debugUnimplemented);
		}
	}
}
