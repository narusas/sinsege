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
 * DefaultAcroRenderer.java
 * ---------------
 * (C) Copyright 2005, by IDRsolutions and Contributors.
 *
 * 
 * --------------------------
 */
package org.jpedal.objects.acroforms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import org.jpedal.PdfDecoder;
import org.jpedal.PdfPanel;
import org.jpedal.exception.PdfException;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.PdfAnnots;
import org.jpedal.objects.PdfFormData;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Strip;


/**
 * used to help setup, print and display,
 * forms and annotations on the page
 * 
 * calles methods in FormFactory which you can override with your own Factory
 * to use different forms
 */
public class DefaultAcroRenderer implements AcroRenderer {
	
	final public static boolean useXFACode=true;

    /**log method called*/
    final private boolean showMethods=false;

    /** used for debugging the renderer */
    final private static boolean debug = false;
    final private static boolean debugUnimplemented = false;
    
    /**used to draw pages offset if not in SINGLE_PAGE mode*/
	int[] xReached,yReached;
	
	/**creates all GUI components*/
	FormFactory formFactory;
	
	public static final int ANNOTATION = 1;
    public static final int FORM = 2;
    public static final int XFAFORM = 3;
	
	/**identify type of OBject (ie form, annotation)*/
    public int type=0;
    
     /**stores the name and component index in allFields array*/
    private Map nameToCompIndex;
    
    /** the reset to value of the indexed field */
    private String[] defaultValues;
    
    private Map typeValues;

    /**array to hold components*/
    private Component[] allFields;
    
	/** holds the mediaBox and cropBox values */
	private PdfPageData pageData;
	
	/**insets from page*/
	private int insetW,insetH;
	
	/**used to only rescale as needed*/
	private float lastScaling=-1;
	
	/**used to ensure redraw on rotation*/
	private int oldRotation=0;

    /**Indent if centered*/
    private int oldIndent=0;

    /**holds all the data*/
	private List acroFormDataList;
	
    /**number of form fields in total for this document*/
    private int formCount=0;
    
    /**number of entries in acroFormDataList, each entry can have a button group of more that one button*/
    private int fieldCount=0;
	
	/**next free slot*/
	private int nextFreeField=0;
	
	/** holds the location and size for each field */ 
	private float[][] boundingBoxs;
	
	private int[] fontSize;
	
	/**array to hold page for each component so we can scan quickly on page change*/
	private int[] pageMap;
	
	/**table to store if page components already built*/
	private int[] trackPagesRendered;
	
	/**track page displayed*/
	private int currentPage;
	
	/**tracks which is first item on page*/
	private int startID;
	private int pageHeight;
	private int cropOtherY;
	
	/**panel components attached to*/
	private PdfDecoder panel;
	
	protected int pageCount=0;
	
	/**track page scaling*/
	private float displayScaling;

	/** handle on object reader for decoding objects*/
    private PdfObjectReader currentPdfFile;
    
    /** local handle to allow only one instance */
    private FormDecoder formDecoder;

    /** array to store fontsizes as the field is set up for use on rendering*/
    private int[] fontSizes;
    
    /** the last name added to the nameToCompIndex map*/
    private String lastNameAdded="";
    
    /** 
     * map that stores the field number for fields that have parts 
     * that need to be displayed on a different page 
     */
    private Map additionFieldsMap = new HashMap();
	private float scaling=0;
	private int rotation;
	
	private int startPage;
	private int endPage;
    private int indent;
    
    /**
     * handles events like URLS, EMAILS
     */
    ActionHandler formsActionHandler;

    public DefaultAcroRenderer(){this(null);}
    
    /**allow user to add own handler or intialise ours*/
    public DefaultAcroRenderer(Object userActionHandler) {
    	
    	resetActionHandler(userActionHandler);
	}

    /**example K*/
    public void K(){
        System.out.println(" K called");
    }
    
    /**
     * reset handler (must be called Before page opened)
     * - null Object resets to default
     */
    public void resetActionHandler(Object userActionHandler){
    	
    	if(userActionHandler!=null)
    		formsActionHandler = (ActionHandler) userActionHandler;
    	else
    		formsActionHandler=new DefaultActionHandler();
    	
    	if(formFactory!=null)
    	formFactory.reset(this,formsActionHandler);
    }

	/**
     * make all components invisible
     */
    public void removeDisplayComponentsFromScreen(PdfPanel panel) {
    	
        if(showMethods)
                    System.out.println("removeDisplayComponentsFromScreen ");


        /**
        if(allFields!=null){
            int count=allFields.length;
            for(int i=0;i<count;i++)
                if(allFields[i]!=null){
                    //allFields[i].setVisible(false);
                    panel.remove(allFields[i]);
                    allFields[i].invalidate();
                }
        }
        /***/
        panel.removeAll();

    }

	/**
	 * initialise holders and variables and get a handle on data object
	 *
	 * Complicated as Annotations stored on a PAGE basis whereas FORMS stored on
	 * a file basis
	 */
	public void init(Object obj,int insetW,int insetH,PdfPageData pageData,PdfObjectReader currentPdfFile) {

        if(showMethods)
                    System.out.println("init "+type+" "+this);


        boolean resetToEmpty=true;

		this.currentPdfFile = currentPdfFile;

		//track inset on page
		this.insetW=insetW;
		this.insetH=insetH;

		this.pageData=pageData;

		if(obj==null){
			acroFormDataList=null;
			formCount=0;
			fieldCount=0;
		}else if(type==FORM){

			//upcast to form
			PdfFormData acroFormData=(PdfFormData) obj;

			/**
			 * choose correct decoder for form data
			 */
			if(acroFormData.hasXFAFormData()){


//System.out.println("formdata="+ConvertToString.convertArrayToString(acroFormData.getFormData()));

				//XFA data is all in the PdfFormData object at this point.
				//Haven't decided best plan but think we'll probalby need to
				//parse config, template and fields in parallel to decode.
			}else
				formDecoder = new FormStream(currentPdfFile);


			acroFormDataList=acroFormData.getFormData();
			formCount=acroFormData.getTotalCount();
			fieldCount=this.acroFormDataList.size();


		}else if(type==ANNOTATION){

			//upcast to Annot
			PdfAnnots annotData=(PdfAnnots) obj;

			formDecoder = new AnnotStream(currentPdfFile);

			acroFormDataList=annotData.getAnnotRawDataList();
			int size=acroFormDataList.size();
			formCount=formCount+size;
			fieldCount=size;

			resetToEmpty=false;
		}else{
		}

		resetContainers(resetToEmpty);

	}

	protected void resetContainers(boolean resetToEmpty) {
		
		if(debug)
			System.out.println("DefaultAcroRenderer.resetContainers()");
		
		additionFieldsMap.clear();
		
		nextFreeField=0;

		/**form or reset Annots*/
		if(resetToEmpty){
			
			allFields=new Component[formCount+1];
			pageMap=new int[formCount+1];
			fontSize=new int[formCount+1];
			nameToCompIndex = new HashMap(formCount+1);
			defaultValues = new String[formCount+1];
			typeValues = new HashMap();
			
			//start up boundingBoxs
			boundingBoxs=new float[formCount+1][4];
			
	        fontSizes = new int[formCount+1];
	        
	        //flag all fields as unread
	        trackPagesRendered=new int[pageCount+1];
			for(int i=0;i<pageCount+1;i++)
				trackPagesRendered[i]=-1;

        }else{
			
			Component[] tmpFields=allFields;
			int[] tmpMap=pageMap;
			int[] tmpSize=fontSize;
			String[] tmpValues=defaultValues;
			float[][] tmpBoxs=boundingBoxs;
			int[] tmpSizes=fontSizes;

			allFields=new Component[formCount+1];
			pageMap=new int[formCount+1];
			fontSize=new int[formCount+1];
			defaultValues = new String[formCount+1];
			
			//start up boundingBoxs
			boundingBoxs=new float[formCount+1][4];
			
	        fontSizes = new int[formCount+1];

	        //populate
	        for(int i=0;i<tmpFields.length;i++){
	        	
	        		if(tmpFields[i]==null)
	        			break;
	        		
	        		allFields[i]=tmpFields[i];
	        		pageMap[i]=tmpMap[i];

	        		fontSize[i]=tmpSize[i];
	        		defaultValues[i]=tmpValues[i];

                System.arraycopy(tmpBoxs[i], 0, boundingBoxs[i], 0, 4);
	        		
	        		fontSizes[i]=tmpSizes[i];
	        		
	        		this.nextFreeField++;
	        }
		}
        
        if(formFactory==null){
        	formFactory=new DefaultFormFactory(this,formsActionHandler);
        }else {
        	//@Mark to keep customers formfactory usable
        	formFactory.reset(this,formsActionHandler);
        }
        
	}
	
	/**
	 * build forms display using standard swing components
	 */
	public void createDisplayComponentsForPage(int page,PdfDecoder panel,float scaling,int rotation) {
		
        if(showMethods)
        System.out.println("createDisplayComponentsForPage "+page+" "+scaling);
        
        this.scaling=scaling;
		this.rotation=rotation;

        int mediaHeight=pageData.getMediaBoxHeight(page);
		int cropTop=(pageData.getCropBoxHeight(page)+pageData.getCropBoxY(page));
		
		//take into account crop
		cropOtherY=0;
		if(mediaHeight!=cropTop)
			cropOtherY=(mediaHeight-cropTop);
		
		this.pageHeight=mediaHeight;
		this.displayScaling=scaling;
		//track page displayed
		this.currentPage=page;
		
		if(panel!=null){
			this.panel=panel;	
		}
		
		//setup page height and inset for popup
		if(type==ANNOTATION)
			((AnnotStream) formDecoder).setPageHeightAndInset(pageHeight,insetH);
		
		if(trackPagesRendered==null)
			return;

		/**
		 * get pointer to first Component or return -1
		 */
		if(debug){
			for(int i=0;i<trackPagesRendered.length;i++)
				System.out.println("pages rendered="+trackPagesRendered[i]);
		}
		
		int firstComp=trackPagesRendered[page];
		
		/**see if already done*/
		if(firstComp==-1){
			
			startID=nextFreeField;

            //flag start
			trackPagesRendered[page]=nextFreeField;
			
			//reset the items list in the form/annot stream for new page or file
			if(type==FORM)
				formDecoder.resetItems();
			
			/** ATTENTION 
			 * 
			 * fieldNum is the index in AcroFormDataList to get the next FORMOBJECT to be setup,
			 * the FORMOBJECT can have a button group in which there may be more than one FORMFIELD.
			 * 
			 * formNum is the index in allFields, to which the FORMFIELD will be stored
			 */
			
			//create array for use with XFA
			FormObject[] forms = null;
			int i=0;
			if(useXFACode && type==XFAFORM){
				forms = new FormObject[fieldCount];
				
			}
			
			//scan list for all relevent values and add as components, also enabling
//			for(int fieldNum=0;fieldNum<fieldCount;fieldNum++){
			for(int fieldNum=fieldCount-1;fieldNum>-1;fieldNum--){
				
				Map currentField=(Map) acroFormDataList.get(fieldNum);

                int formPage=-1;
				Object rawPageNumber=currentPdfFile.resolveToMapOrString("PageNumber", currentField.get("PageNumber"));
				if(rawPageNumber!=null)
					formPage=Integer.parseInt((String)rawPageNumber);
				
                //to allow annotations through for this page
				if(formPage==-1 && type==ANNOTATION)
					formPage = page;
				
                /**
				 * work through raw form data parsing and converting into an intermediate Object we use to build components.
				 * This allows us to abstract the decoding to handle different form types and the component generation so
				 * we could create SWT as well as Swing.
				 */
				if(formPage==page){
					ButtonGroup bg = new ButtonGroup();
					Component nextComp;
					FormObject formObject=null;
					
					if(useXFACode && type==XFAFORM){
						nextComp = null;
						
						formObject = ((AnnotStream)formDecoder).createAppearanceString(currentField,currentPdfFile);
						
						if(formObject==null)
							continue;
						
						forms[i++] = formObject;
					}else {
						//original FDF form parsing code
	//					convert the form data into a formObject which we will then parse
						formObject = formDecoder.createAppearanceString(currentField,currentPdfFile);
						//trap when an annot is already been decoded as a form,
						//May also cause a problem if different fields have same Rect entry
						//If Rect entry is null, it assumes its a buttonGroup
						if(formObject==null)
							continue;
						
						//now we turn the data into a Swing component
						nextComp = createField(formObject,bg,nextFreeField);
					}
					
					/**
					 * handle composite object where forms part of group
					 */
					checkButtonGroupAndApply(page,scaling,rotation,formPage,bg,nextComp,formObject);
				}
			}
			
								
			/**
			 * handles composite objects (ie group of radio buttons) which may be split across pages
			 */
			if(additionFieldsMap.get(""+page)!=null){
				ArrayList list = (ArrayList)additionFieldsMap.get(""+page);
				Iterator iter = list.iterator();
				while(iter.hasNext()){
					apply((Component) iter.next(),page,scaling,rotation);
				}
			}
								
		}//else if((panel!=null)){ //if renderering to screen and already parsed just add
			
			//displayComponentsOnscreen(page,page, panel, scaling, rotation);
		//}
	}

	private void checkButtonGroupAndApply(int page,float scaling,int rotation,int formPage,ButtonGroup bg,Component nextComp,FormObject formObject) {
		if(bg.getButtonCount()>1){
			AbstractButton[] sortedButtons= sortGroupSmallestFirst(bg);
			
			for(int j=0;j<bg.getButtonCount();j++){
				if(sortedButtons[j].getLabel().equals(""+page)){
					
					if((formObject.currentState!=null && formObject.currentState.equals(removeStateToCheck(sortedButtons[j].getName(),true)))
							|| (formObject.onState!=null && formObject.onState.equals(removeStateToCheck(sortedButtons[j].getName(),true)))){
						sortedButtons[j].setSelected(true);
					}
					
					apply(sortedButtons[j],formPage,scaling,rotation);
				}else {
					if(additionFieldsMap.get(sortedButtons[j].getLabel())!=null){
						ArrayList list = (ArrayList)additionFieldsMap.get(sortedButtons[j].getLabel());
						list.add(sortedButtons[j]);
						additionFieldsMap.put(sortedButtons[j].getLabel(),list);
					}else {
						ArrayList list = new ArrayList();
						list.add(sortedButtons[j]);
						additionFieldsMap.put(sortedButtons[j].getLabel(),list);
					}
				}
			}
		}else if(nextComp!=null){ //other form objects
			//System.out.println("but="+nextComp.getName()+" cur="+formObject.currentState+" on="+formObject.onState);
			if(formObject.flags[14]){////FormStream.NOTOGGLETOOFF
				if(nextComp instanceof AbstractButton){
					AbstractButton but = (AbstractButton)nextComp;
	                but.setBounds(formObject.Rect);
	                but.setText(""+formObject.pageNumber);
	                new ButtonGroup().add(but);  // Add to button group
				}else {
				}
			}
			apply(nextComp,formPage,scaling,rotation);
		}
	}
			
	/**display range (inclusive)*/
	public void displayComponentsOnscreen(int startPage, int endPage,PdfPanel panel, float scaling, int rotation) {

        if(showMethods)
                System.out.println(this+" displayComponentsOnscreen "+startPage+" "+endPage);

        this.startPage=startPage;
		this.endPage=endPage;

        /**    MIGHT be needed for multi display
        boolean multiPageDisplay=(startPage!=endPage);

        //remove all invisible forms
        if(multiPageDisplay){

            int start=1;
            int end=startPage;
            //from start to first page
            //removePageRangeFromDisplay(start, end, panel); //end not included in range

            //from end to last page
            int last=1+trackPagesRendered.length;
            //removePageRangeFromDisplay(end, last, panel);
        }
         /**/
        //make sure this page is inclusive in loop
		endPage++;
		
		for(int page=startPage;page<endPage;page++){

			if(trackPagesRendered.length>page)
				startID=trackPagesRendered[page];
			else
				startID=-1;

			int currentComp=startID;
			//just put on page, allowing for no values (last one alsways empty as array 1 too big

            if(startID!=-1){
				while((pageMap[currentComp]>=startPage)&&(pageMap[currentComp]<endPage)){
					if(allFields[currentComp]!=null){
                        scaleComponent(pageMap[currentComp],scaling, rotation, currentComp,allFields[currentComp],true);
						if(panel!=null)
                        panel.add(allFields[currentComp]);

                    }
					currentComp++;
					if(currentComp==pageMap.length)
						break;				
				}
			}
        }
	}

    /**
     * explicitly remove items not seen so user cannot glimpse on scroll in multipage view
     */
    public void removePageRangeFromDisplay(int start, int end, PdfPanel panel) {

        try{
        for(int page=start;page<end;page++){

            int currentComp=trackPagesRendered[page];

            //System.out.println(start+" "+end+" "+page+" "+currentComp);
            //remove invisible forms
            if(currentComp!=-1){
                while((pageMap[currentComp]>=start)&&(pageMap[currentComp]<=end)){
                    if(allFields[currentComp]!=null)
                        panel.remove(allFields[currentComp]);

                    currentComp++;
                    if(currentComp==pageMap.length)
                        break;

                }
            }
        }
        }catch(Exception ee){
            ee.printStackTrace();
            System.exit(1);
        }
    }

    /** sorts the buttongroup into ascending order, smallest area first*/
    private  AbstractButton[] sortGroupSmallestFirst(ButtonGroup bg) {

        int items = bg.getButtonCount();
        AbstractButton[] buttons = new AbstractButton[items];

        Enumeration butGrp = bg.getElements();
        for(int i=0;i<items;i++){
            if(butGrp.hasMoreElements())
                buttons[i] = (AbstractButton) butGrp.nextElement();
        }

        //sort buttons array
        return (AbstractButton[]) sortCompsAscending(buttons);
    }
    
    /** 
     * sorts as a tree like structure in array representation,
     * the Compo~nent array in ascending height order,
     * smallest height first
     */
    final public static Component[] sortCompsAscending( Component[] primary){
		//reference
		//Sorts.quicksort(new int[1],new int[1]);

        /** copy so we don't sort original */
        int items = primary.length;
        
        //pointer to left side of unsorted array
        int left = items/2; 
        //pointer to right side of unsorted array 
        int right = items-1;
        
        //sift through array into a heap
        while (left>0) {
            
            left=left-1; 
            
            //go through tree starting with leaves and going up
            siftCompsAscending(primary, left, right);
        }
        
        //rearrange heap into a sorted array
        while (right>0) { 
            
            //assert: largest unsorted value is at a[0]
            //move largest item to right end
            Component tempA=primary[0];
            primary[0]=primary[right]; 
            primary[right]=tempA;
            //assert: a[right..] is sorted
            
            //right is largest and sorted decrement it
            right=right-1;
            
            //get largest value in the tree to the leftMost position
           siftCompsAscending(primary,left,right);
        }
        //assert: right==0, therefore a[0..] is all sorted
        
        return primary;
    }

    /**
     * see sortCompsAscending(Component[])
     * This Is Called from That Method ONLY
     */
    private static void siftCompsAscending(Component[] primary, int left, int right) {
                int currentLeft;
                Component primaryTMP;
                int childL;
                
                //assign left to local
                currentLeft = left;
                //temp store of left item
                primaryTMP = primary[currentLeft];
                
                //Left child node of currentLeft
               childL = 2*left+1;
                
                
                //Find a[left]'s larger child
                if ((childL<right) && shouldSwapControl(primary[childL],primary[childL+1])) {
                    childL=childL+1;
                }
                //assert: a[childL] is larger child
                
                //sift temp to be in correct place in highest on leftMost and arranged as tree
                while ((childL<=right) && shouldSwapControl(primaryTMP,primary[childL])){
                    //assign highest item to leftmost position
                    primary[currentLeft]=primary[childL];
                    currentLeft=childL; 
                    childL=2*childL+1;
                    
                    //pick highest child 
                    if ((childL<right) && shouldSwapControl(primary[childL],primary[childL+1])){
                        childL=childL+1;
                    }
                }
                //put temp in the correct place in the sub-heap
                primary[currentLeft]=primaryTMP;
                //assert: a[left] is the root a sub-heap.
    }

    /**
     * the control of the order in the sortCompsAscending(Component[]) method
     */
    private static boolean shouldSwapControl(Component arg1,Component arg2) {
        
        Rectangle first = arg1.getBounds();
        Rectangle second = arg2.getBounds();
        
        /**
         * sorts by area, same as acrobat
        return (first.width*first.height)<(second.width*second.height);
        */
        return (first.width*first.height)<(second.width*second.height);
    }
    
    private String removeStateToCheck(String curCompName,boolean returnState){
    	if(curCompName!= null){
        	int ptr=curCompName.indexOf("-(");
        	/** NOTE if indexOf string changes change ptr+# to same length */
        	if(ptr!=-1){
        		if(returnState)
        			curCompName = curCompName.substring(ptr+2,curCompName.length()-1);
        		else
        		curCompName=curCompName.substring(0,ptr);
        }
        }
    	
    	return curCompName;
    }
    
    private void apply(Component nextComp, int formPage, float scaling, int rotation){
        
        //add fieldname to map for action events
        String curCompName = removeStateToCheck(nextComp.getName(),false);

        if(curCompName!=null && !lastNameAdded.equals(curCompName)){
            nameToCompIndex.put(curCompName,new Integer(nextFreeField));
            lastNameAdded = curCompName;
        }
        
        //save font size for later
        int currentFontSize=-1;
        int rawSize=fontSizes[nextFreeField];
        currentFontSize=rawSize;
        
        //setup and add component to selection
        if(nextComp!=null){
            
            //set location and size
//          String rect = (String)currentField.getxx("Rect");
            Rectangle rect = nextComp.getBounds();
            if(rect!=null){
                if(debug)
                    System.out.println("rectangle="+rect);
                
//              103 478 8   26
//              103 479 8   8
//              103 478 8   36
//              103 478 8   45
//              103 478 8   17
                
//              if(rect.x==103 && rect.width==8){
//                  System.out.println("temp guage Details="+fieldStream);
//                  count++;
//                  if(count<2)
//                      continue;
//              }
                

                
//              StringTokenizer tok = new StringTokenizer(rect);
                
//              float x1=Float.parseFloat(tok.nextToken()),y1=Float.parseFloat(tok.nextToken()),
//              x2=Float.parseFloat(tok.nextToken()),y2=Float.parseFloat(tok.nextToken());
                float x1=rect.x,y1=rect.y,
                x2=rect.width+rect.x,y2=rect.height+rect.y;
                
                boundingBoxs[nextFreeField][0]=x1;
                boundingBoxs[nextFreeField][1]=y1;
                boundingBoxs[nextFreeField][2]=x2;
                boundingBoxs[nextFreeField][3]=y2;

            }
        
            //put into array
            allFields[nextFreeField]=nextComp;
            
            fontSize[nextFreeField]=currentFontSize;
            
            //make visible
            scaleComponent(formPage,scaling,rotation, nextFreeField,nextComp,true);
            
            //add to panel
            //if((panel!=null))
              //  panel.add(nextComp);
            
        }
        
        pageMap[nextFreeField]=formPage;
        nextFreeField++;
    }
	
	/**
	 * create a Java button to handle FDF button
	 */
	private Component createField(final FormObject formObject,ButtonGroup bg,int formNum) {

        if(showMethods)
            System.out.println("createField "+formNum+" "+formObject);

        Component retComponent = null;
        
        try{
        //define which type of component will be created
        boolean button=false,text=false,choice=false,signature=false;
        int typeFlag = formObject.type;
        if(typeFlag!=-1){
            button = typeFlag==FormObject.FORMBUTTON;
            text = typeFlag==FormObject.FORMTEXT;
            choice = typeFlag==FormObject.FORMCHOICE;
            signature =typeFlag==FormObject.FORMSIG; 
        }

        //flags used to alter interactivity of all fields
		boolean readOnly=false,required=false,noexport=false;
        
        boolean[] flags = formObject.flags;
		if(flags!=null){
		    readOnly=flags[1];//READONLY;//
		    required=flags[2];//REQUIRED;//
		    noexport=flags[3];//NOEXPORT;//
            
            /*
             boolean comb=flags[24];//FormStream.COMB
             boolean comminOnSelChange=flags[26];//FormStream.COMMITONSELCHANGE
             boolean donotScrole=flags[23];//FormStream.DONOTSCROLL
             boolean doNotSpellCheck=flags[22];//FormStream.DONOTSPELLCHECK
             boolean fileSelect=flags[20];//FormStream.FILESELECT
             boolean isCombo=flags[17];//FormStream.COMBO
             boolean isEditable=flags[18];//FormStream.EDIT
            boolean isMultiline=flags[12];//FormStream.MULTILINE
            boolean isPushButton=flags[16];//FormStream.PUSHBUTTON
            boolean isRadio=flags[15];//FormStream.RADIO
            boolean hasNoToggleToOff=flags[14];//FormStream.NOTOGGLETOOFF
            boolean hasPassword=flags[13];//FormStream.PASSWORD
            boolean multiSelect=flags[21];//FormStream.MULTISELECT
            boolean radioinUnison=flags[25];//FormStream.RADIOINUNISON
            boolean richtext=flags[25];//same as RICHTEXT
            boolean sort=flags[19];//FormStream.SORT
            */
		}
	    
		if(debug){
			if(flags!=null){
        			System.out.println("FLAGS - pushbutton="+flags[16]+" radio="+flags[15]+" notoggletooff="+
                    flags[14]+"\n multiline="+flags[12]+" password="+flags[13]+
                    "\n combo="+flags[17]+" editable="+flags[18]+" readOnly="+readOnly+
					"\n BUTTON="+button+" TEXT="+text+" CHOICE="+choice+" SIGNATURE="+signature+
                    "\n characteristic="+ConvertToString.convertArrayToString(formObject.characteristic));
			}else {
				System.out.println("FLAGS - all false");
		}
		}
		
		if(debugUnimplemented && flags!=null && (required || flags[19] || noexport || flags[20] || flags[21] || flags[23] || flags[25] || flags[25]))
		    System.out.println("renderer UNTESTED FLAGS - readOnly="+readOnly+" required="+required+" sort="+flags[19]+" noexport="+noexport+
                    " fileSelect="+flags[20]+" multiSelect="+flags[21]+" donotScrole="+flags[23]+" radioinUnison="+flags[25]+
                    " richtext="+flags[25]);
        
		/** setup field */
		if(button){//----------------------------------- BUTTON  ----------------------------------------
            //flags used for button types
            boolean isPushButton=false,isRadio=false,hasNoToggleToOff=false,radioinUnison=false;
            if(flags!=null){
                isPushButton=flags[16];//FormStream.PUSHBUTTON
                isRadio=flags[15];//FormStream.RADIO
                hasNoToggleToOff=flags[14];//FormStream.NOTOGGLETOOFF
                radioinUnison=flags[25];//FormStream.RADIOINUNISON
            }
            
			if(isPushButton){
				typeValues.put(formObject.fieldName,FormFactory.PUSHBUTTON);
				
			    retComponent = formFactory.pushBut(formObject);
				
			}else if(isRadio){
				typeValues.put(formObject.fieldName,FormFactory.RADIOBUTTON);
				
				if(formObject.kidData!=null){
                	
                	Iterator iter = formObject.kidData.keySet().iterator();
                    while(iter.hasNext()){
                        FormObject form = (FormObject)formObject.kidData.get(iter.next());
                        
        					Component checkComp = createField(form,bg,++formNum);
                        
                        try{
                            AbstractButton but = (AbstractButton)checkComp;
                            but.setBounds(form.Rect);
                            but.setLabel(""+form.pageNumber);
                            bg.add(but);  // Add to button group
                        }catch(ClassCastException cc){
                        }
                    }
                }
                
				retComponent = formFactory.radioBut(formObject);
				
			}else {//checkBox
				typeValues.put(formObject.fieldName,FormFactory.CHECKBOXBUTTON);
				
                if(formObject.kidData!=null){
                    Iterator iter = formObject.kidData.keySet().iterator();
                    while(iter.hasNext()){
                        FormObject form = (FormObject)formObject.kidData.get(iter.next());
                        
        					Component checkComp = createField(form,bg,++formNum);
                        
                        try{
                            AbstractButton but = (AbstractButton)checkComp;
                            but.setBounds(form.Rect);
                            but.setLabel(""+form.pageNumber);
                            bg.add(but);  // Add to button group
                        }catch(ClassCastException cc){
                        }
                    }
                }
                
			    retComponent = formFactory.checkBoxBut(formObject);
				
			}
			
		}else if(text){ //-----------------------------------------------  TEXT --------------------------------------
		    //flags used for text types
            boolean isMultiline=false,hasPassword=false,doNotScroll =false,richtext=false,fileSelect=false,doNotSpellCheck=false;
            if(flags!=null){
                isMultiline=flags[12];//FormStream.MULTILINE
                hasPassword=flags[13];//FormStream.PASSWORD
                doNotScroll =flags[23];//FormStream.DONOTSCROLL
                richtext=flags[25];//same as RICHTEXT
                fileSelect=flags[20];//FormStream.FILESELECT
                doNotSpellCheck=flags[22];//FormStream.DONOTSPELLCHECK
            }

            if(isMultiline){

				if(hasPassword){
					typeValues.put(formObject.fieldName,FormFactory.MULTILINEPASSWORD);
					
				    retComponent = formFactory.multiLinePassword(formObject);
					
				}else{
					typeValues.put(formObject.fieldName,FormFactory.MULTILINETEXT);
					
				    retComponent = formFactory.multiLineText(formObject);
					
				}
			}else {//singleLine
				
				if(hasPassword){
					typeValues.put(formObject.fieldName,FormFactory.SINGLELINEPASSWORD);
					
				    retComponent = formFactory.singleLinePassword(formObject);
					
				}else{
					typeValues.put(formObject.fieldName,FormFactory.SINGLELINETEXT);
					
				    retComponent = formFactory.singleLineText(formObject);
				}
			}
		}else if(choice){//----------------------------------------- CHOICE ----------------------------------------------
		    //flags used for choice types
            boolean isCombo=false,multiSelect=false,sort=false,isEditable=false,doNotSpellCheck=false,comminOnSelChange=false;
            if(flags!=null){
                isCombo=flags[17];//FormStream.COMBO
                multiSelect=flags[21];//FormStream.MULTISELECT
                sort=flags[19];//FormStream.SORT
                isEditable=flags[18];//FormStream.EDIT
                doNotSpellCheck=flags[22];//FormStream.DONOTSPELLCHECK
                comminOnSelChange=flags[26];//FormStream.COMMITONSELCHANGE
            }
            
			if(isCombo){// || (type==XFAFORM && ((XFAFormObject)formObject).choiceShown!=XFAFormObject.CHOICE_ALWAYS)){
				typeValues.put(formObject.fieldName,FormFactory.COMBOBOX);
				
			    retComponent = formFactory.comboBox(formObject);
				
			}else {//it is a list
				typeValues.put(formObject.fieldName,FormFactory.LIST);
				
			    retComponent = formFactory.listField(formObject);
				}
		}else if(signature){
			typeValues.put(formObject.fieldName,FormFactory.SIGNATURE);
			
			retComponent = formFactory.signature(formObject);
			
		}else if(type==ANNOTATION){
			typeValues.put(formObject.fieldName,FormFactory.ANNOTATION);
            
			retComponent = formFactory.annotationButton(formObject);
            
        }else {
            
			if(debug){
				if(flags!=null){
				System.out.println("UNIMPLEMENTED field=FLAGS - pushbutton="+flags[16]+" radio="+flags[15]+
						" multiline="+flags[12]+" password="+flags[13]+" combo="+flags[17]+
						" BUTTON="+button+" TEXT="+text+" CHOICE="+choice);
				}else {
					System.out.println("UNIMPLEMENTED field=BUTTON="+button+" TEXT="+text+" CHOICE="+choice+" FLAGS=all false");
				}
			}
			return null;
		}
		
		//append state to name so we can retrieve later if needed
        if(formObject.fieldName!=null){
        	
        	String name=formObject.fieldName;
        	
        		if(formObject.stateTocheck!=null)
        			name=name+"-("+formObject.stateTocheck+")";
            retComponent.setName(name);
        }
        if(formObject.Rect!=null){
        	Rectangle rect = formObject.Rect;
//        	if(type==XFAFORM && choice && !flags[17]){//flags[17]==combo
//        		rect.y -= rect.height;
//        	}
            retComponent.setBounds(rect);
//        	if(formObject.Rect.width>200 || formObject.Rect.height>400){
//        		retComponent.setBounds(new Rectangle(0,0,0,0));
//        		retComponent.setVisible(false);
//        		retComponent.setEnabled(false);
//        	}
        }

        fontSizes[formNum] = formObject.textSize;
        	if(formObject.valuesMap!=null)
        		defaultValues[formNum] = (String) formObject.valuesMap.get(Strip.checkRemoveLeadingSlach(formObject.defaultValue));
        	else 
        		defaultValues[formNum] = Strip.checkRemoveLeadingSlach(formObject.defaultValue);

        }catch(Exception ee){
            ee.printStackTrace();
        	//is this needed?
        }
        return retComponent;
	}

    /** 
	 * allows user to access Components already decoded so they can directly access or add listeners -
	 * not needed to create display PLEASE DO NOT USE
	 * @deprecated
	 */
	public Component[] getDisplayComponentsForPage(int i) {

        if(showMethods)
            System.out.println("getDisplayComponentsForPage "+i);

        return allFields;
	}

	/**
	 * alter location and bounds so form objects scale correctly
	 */
	public void resetScaledLocation(float scaling,int rotation,int indent) {

        if(showMethods)
        	 System.out.println("resetScaledLocation scaling="+scaling+" indent="+indent+" rotation="+rotation);

        this.indent=indent;
        this.displayScaling=scaling;
        this.rotation=rotation;
		
        //System.out.println("Reset scaling "+scaling+" "+lastScaling+" "+startID+" "+this.pageHeight);
		/**
		debug=true;
		/**/

        //we get a spurious call in linux resulting in an exception
        if(trackPagesRendered==null)
                    return;

        //only if necessary
		if(scaling!=lastScaling || rotation!=oldRotation || indent !=oldIndent){

            oldRotation=rotation;
			lastScaling=scaling;
			oldIndent=indent;
			
			int currentComp;
			if(startPage<trackPagesRendered.length-1){
				currentComp=trackPagesRendered[startPage];//startID;
			}else {
				currentComp = 0;
			}

            //reset all locations
			if((allFields!=null)&&(currentPage>0)&&(currentComp!=-1)&&(pageMap.length>currentComp)){

                //just put on page, allowing for no values (last one alsways empty as array 1 too big
				//while(pageMap[currentComp]==currentPage){
                while((pageMap[currentComp]>=startPage)&&(pageMap[currentComp]<=endPage)&&(allFields[currentComp]!=null)){
                    //System.out.println("added"+currentComp);
                //while(currentComp<pageMap.length){//potential fix to help rotation
					if(panel!=null)
					panel.remove(allFields[currentComp]);

                    scaleComponent(pageMap[currentComp],scaling,rotation, currentComp,allFields[currentComp],true);
					
					if(panel!=null)
					panel.add(allFields[currentComp]);
					currentComp++;
				}
			}
		}
	}

	/**
	 * alter font and size to match scaling. Note we pass in compoent so we can
	 * have multile copies (needed if printing page displayed).
	 */
	private void scaleComponent(int currentPage,float scaling,int rotation, int i,Component comp,boolean repaint) {
		if(showMethods)
			System.out.println("DefaultAcroRenderer.scaleComponent()");
		
		if(comp==null)
			return;

        int x = 0,y=0,w=0,h=0;
		
		int cropOtherX=(pageData.getMediaBoxWidth(currentPage)-pageData.getCropBoxWidth(currentPage)-pageData.getCropBoxX(currentPage));
		
		if(rotation==0){
		    //old working routine
//		    int x = (int)((boundingBoxs[i][0])*scaling)+insetW-pageData.getCropBoxX(currentPage);
//		    int y = (int)((pageData.getMediaBoxHeight(currentPage)-boundingBoxs[i][3]-cropOtherY)*scaling)+insetH;
//		    int w = (int)((boundingBoxs[i][2]-boundingBoxs[i][0])*scaling);
//		    int h = (int)((boundingBoxs[i][3]-boundingBoxs[i][1])*scaling);
		    
		    int crx = pageData.getCropBoxX(currentPage);
		    //new hopefully more accurate routine
		    float x100 = (boundingBoxs[i][0])-(crx)+insetW;
		    /** 
		     * if we are drawing the forms to "extract image" or "print", 
		     * we don't translate g2 by insets we translate by crop x,y
		     * so add on crop values
		     * we should also only be using 0 rotation
		     */
		    if(!repaint){
		        x100+=crx;
		    }
		    float y100 = (pageData.getMediaBoxHeight(currentPage)-boundingBoxs[i][3]-cropOtherY)+insetH;
		    float w100 = (boundingBoxs[i][2]-boundingBoxs[i][0]);
		    float h100 = (boundingBoxs[i][3]-boundingBoxs[i][1]);
		    
		    x = (int) (((x100-insetW)*scaling)+insetW);
		    y = (int) (((y100-insetH)*scaling)+insetH);
		    w = (int) (w100*scaling);
		    h = (int) (h100*scaling);
			
		}else if(rotation==90){
		    //old working routine
//		    int x = (int)((boundingBoxs[i][1]-pageData.getCropBoxY(currentPage))*scaling)+insetW;
//			int y = (int)((boundingBoxs[i][0])*scaling)+insetH;
//			int w = (int)((boundingBoxs[i][3]-boundingBoxs[i][1])*scaling);
//			int h = (int)((boundingBoxs[i][2]-boundingBoxs[i][0])*scaling);
			
		    //new hopefully better routine
		    float x100 = (boundingBoxs[i][1]-pageData.getCropBoxY(currentPage))+insetW;
		    float y100 = (boundingBoxs[i][0]-pageData.getCropBoxX(currentPage))+insetH;
		    float w100 = (boundingBoxs[i][3]-boundingBoxs[i][1]);
		    float h100 = (boundingBoxs[i][2]-boundingBoxs[i][0]);
			
			x = (int)(((x100-insetH)*scaling)+insetH);
			y = (int)(((y100-insetW)*scaling)+insetW);
			w = (int)(w100*scaling);
			h = (int)(h100*scaling);

		}else if(rotation==180){
		    //old working routine
//		    int x = (int)((pageData.getMediaBoxWidth(currentPage)-boundingBoxs[i][2]-cropOtherX)*scaling)+insetW;
//			int y = (int)((boundingBoxs[i][1]-pageData.getCropBoxY(currentPage))*scaling)+insetH;
//			int w = (int)((boundingBoxs[i][2]-boundingBoxs[i][0])*scaling);
//			int h = (int)((boundingBoxs[i][3]-boundingBoxs[i][1])*scaling);
		    
		    //new hopefully better routine
		    int x100 = (int)(pageData.getMediaBoxWidth(currentPage)-boundingBoxs[i][2]-cropOtherX)+insetW;
			int y100 = (int)(boundingBoxs[i][1]-pageData.getCropBoxY(currentPage))+insetH;
			int w100 = (int)(boundingBoxs[i][2]-boundingBoxs[i][0]);
			int h100 = (int)(boundingBoxs[i][3]-boundingBoxs[i][1]);
			
			x = (int)(((x100-insetW)*scaling)+insetW);
			y = (int)(((y100-insetH)*scaling)+insetH);
			w = (int)(w100*scaling);
			h = (int)(h100*scaling);
			
		}else if(rotation==270){
		    //old working routine
//		    int x = (int)((pageData.getMediaBoxHeight(currentPage)-boundingBoxs[i][3]-cropOtherY)*scaling)+insetW;
//			int y = (int)((pageData.getMediaBoxWidth(currentPage)-boundingBoxs[i][2]-cropOtherX)*scaling)+insetH;
//			int w = (int)((boundingBoxs[i][3]-boundingBoxs[i][1])*scaling);
//			int h = (int)((boundingBoxs[i][2]-boundingBoxs[i][0])*scaling);
			
		    //new hopefully improved routine
		    float x100 = (pageData.getMediaBoxHeight(currentPage)-boundingBoxs[i][3]-cropOtherY)+insetW;
		    float y100 = (pageData.getMediaBoxWidth(currentPage)-boundingBoxs[i][2]-cropOtherX)+insetH;
		    float w100 = (boundingBoxs[i][3]-boundingBoxs[i][1]);
		    float h100 = (boundingBoxs[i][2]-boundingBoxs[i][0]);
			
			x = (int)(((x100-insetH)*scaling)+insetH);
			y = (int)(((y100-insetW)*scaling)+insetW);
			w = (int)(w100*scaling);
			h = (int)(h100*scaling);
		
		}
		
		//factor in offset if multiple pages displayed
        if((xReached!=null)){
			x=x+xReached[currentPage];
			y=y+yReached[currentPage];
		}
		
        comp.setBounds(indent+x,y,w,h);

        /**
		 * rescale the font size
		 */
		if(debug)
		    System.out.println("check font size="+comp);
		
		Font resetFont = comp.getFont();
		if(resetFont!=null){
			int rawSize= fontSize[i];
			if(rawSize==-1)
			    rawSize=8;
			
			if(rawSize==0){//best fit

				//work out best size for bounding box of object
				int height = (int)(boundingBoxs[i][3]-boundingBoxs[i][1]);
				int width = (int)(boundingBoxs[i][2]-boundingBoxs[i][0]);

				height *= 0.85;

				rawSize = height;

				if(comp instanceof JTextComponent){
					if((((JTextComponent)comp).getText().length()*height)/2>width){
						int len = ((JTextComponent)comp).getText().length();
							width /= len;
						rawSize = width;
			}
				}else if(comp instanceof JButton){
			
					String text=((JButton)comp).getText();
					if((text!=null)&&((((JButton)comp).getText().length()*height)/2>width)){
						int len = ((JButton)comp).getText().length();
						if(!(len<=0))
							width /= len;
						rawSize = width;
					}
				}else {
//					System.out.println("else="+width);
				}

//				rawSize = height;
			}

			int size =(int)(rawSize*scaling);
			if(size<1){
				size = 1;
			}
			
			if(debug)
			    System.out.println(size+"<<<<<<resetfont="+resetFont);
			
			Font newFont = new Font(resetFont.getFontName(),resetFont.getStyle(),size);
			//resetFont.getAttributes().put(java.awt.font.TextAttribute.SIZE,size);
			if(debug)
			    System.out.println("newfont="+newFont);
			
			comp.setFont(newFont);
		}
		
		/**
		 * rescale the icons if any 
		 */
	    if(comp!=null && comp instanceof AbstractButton){
	        AbstractButton but = ((AbstractButton)comp);
	        
	        Icon curIcon = but.getIcon();
	        if(curIcon instanceof FixImageIcon){
	            ((FixImageIcon) curIcon).setWH(comp.getWidth(),comp.getHeight());
	        }
	        
	        curIcon = but.getPressedIcon();
	        if(curIcon instanceof FixImageIcon){
	            ((FixImageIcon) curIcon).setWH(comp.getWidth(),comp.getHeight());
	        }
	        
	        curIcon = but.getSelectedIcon();
	        if(curIcon instanceof FixImageIcon){
	            ((FixImageIcon) curIcon).setWH(comp.getWidth(),comp.getHeight());
	        }
	        
	        curIcon = but.getRolloverIcon();
	        if(curIcon instanceof FixImageIcon){
	            ((FixImageIcon) curIcon).setWH(comp.getWidth(),comp.getHeight());
	        }
	        
	        curIcon = but.getRolloverSelectedIcon();
	        if(curIcon instanceof FixImageIcon){
	            ((FixImageIcon) curIcon).setWH(comp.getWidth(),comp.getHeight());
	        }
	    }
		
		if(repaint){
		  //  comp.invalidate();
		  //  comp.repaint();
		}
	}

	/**
	 * draw the forms onto dislay for print of image. Note different routine to 
	 * handle forms also displayed at present
	 */
	public void renderFormsOntoG2(Graphics2D g2,int pageIndex,float scaling,int rotation) {
		
        if(showMethods)
        	System.out.println("renderFormsOntoG2 ");

        AffineTransform defaultAf=g2.getTransform();
	
        
		//setup scaling
		AffineTransform aff=g2.getTransform();
		aff.scale(1,-1);
		aff.translate(0,-pageHeight-insetH);
		g2.setTransform(aff);
		
		//remove so don't appear rescaled on screen
//		if((currentPage==pageIndex)&&(panel!=null))
//			this.removeDisplayComponentsFromScreen(panel);//removed to stop forms disappearing on printing
		
		//get start number
		int currentComp=trackPagesRendered[pageIndex];
		
		try{
			
			//not displayed so we can manipulate at will

			Component[] formComps=allFields;

			if((formComps!=null)&&(currentComp!=-1)){
				
				/**needs to go onto a panel to be drawn*/
				JPanel dummyPanel=new JPanel();
				
				//disable indent while we print
				int tempIndent=indent;
				indent=0;
				
				//just put on page, allowing for no values (last one alsways empty as array 1 too big
				while(pageMap[currentComp]==pageIndex){
					
					Component comp= formComps[currentComp];
					if(comp!=null && comp.isVisible()){
						dummyPanel.add(comp);
						
						try{
							renderComponent(g2, currentComp, comp,rotation);
							dummyPanel.remove(comp);
						}catch (Exception cc){

						}
					}
					currentComp++;
					
					if(currentComp==pageMap.length)
						break;
				}
				indent=tempIndent; //put back indent
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		g2.setTransform(defaultAf);
		
		
		//put componenents back 
		if((currentPage==pageIndex)&&(panel!=null)){
//			createDisplayComponentsForPage(pageIndex,this.panel,this.displayScaling,this.rotation);
//			panel.invalidate();
//			panel.repaint();
			resetScaledLocation(this.displayScaling,this.rotation,this.indent);
		}
		
	}
	
	/**
	 * render component onto G2 for print of image creation
	 */
	private void renderComponent(Graphics2D g2, int currentComp, Component comp,int rotation) {
		if(showMethods)
			System.out.println("DefaultAcroRenderer.renderComponent()");
		
		if(comp!=null){
			
			boolean editable = false;
			
			if(comp instanceof JComboBox){
				if(((JComboBox)comp).isEditable()){
					editable = true;
					((JComboBox)comp).setEditable(false);
				}
				
				/**fix for odd bug in Java when using Windows l & f - might need refining*/
				if(!UIManager.getLookAndFeel().isNativeLookAndFeel()){
					
					if(((JComboBox)comp).getComponentCount()>0)
						renderComponent(g2,currentComp,((JComboBox)comp).getComponent(0),rotation);
				}
				
			}
			
			scaleComponent(currentPage,1,rotation, currentComp,comp,false);
			
			AffineTransform ax=g2.getTransform();
			g2.translate(comp.getBounds().x-insetW,comp.getBounds().y+cropOtherY);
			comp.paint(g2);
			g2.setTransform(ax);
			
			if(editable /*&& comp instanceof JComboBox*/){
				((JComboBox)comp).setEditable(true);
			}
		}
	}
	
	
	/**
     * return the component associated with this objectName (returns null if no match). Names are case-sensitive.
     * Please also see method getComponentNameList(int pageNumber), 
     * if objectName is null then all components will be returned
     */
	public Component[] getComponentsByName(String objectName) {

        if(showMethods)
        	System.out.println("getComponentNameList "+objectName);
        
        if(objectName==null){
        	return allFields;
        }
        
        Object checkObj = nameToCompIndex.get(objectName);
        if(checkObj==null)
       		return null;
        
        //allow for duplicates
        String duplicateComponents=(String) duplicates.get(objectName);

        if(checkObj instanceof Integer){
            int  index = ((Integer)checkObj).intValue();
            
            boolean moreToProcess = true;
            int firstIndex = index;
            while(moreToProcess){
                if(index+1<allFields.length && allFields[index+1]!=null && 
                		removeStateToCheck(allFields[index+1].getName(),false).equals(objectName)){
                    
                    index += 1;
                }else {
                    moreToProcess = false;
                }
            }
            
            int size = index+1 - firstIndex;
            Component[] compsToRet = new Component[size];
            
            for(int i=0;i<size;i++,firstIndex++){
                compsToRet[i] = allFields[firstIndex];
                if(firstIndex==index)
                    break;
            }
            
            //recreate list and add in any duplicates
            if(duplicateComponents!=null && duplicateComponents.indexOf(",")!=-1){

                StringTokenizer additionalComponents=new StringTokenizer(duplicateComponents,",");

                int count=additionalComponents.countTokens();

                Component[] origComponentList= compsToRet;
                compsToRet = new Component[size+count];

                //add in original components
                for(int i=0;i<size;i++)
                compsToRet[i]=origComponentList[i];

                //and duplicates
                for(int i=0;i<count;i++)
                compsToRet[i+size]=allFields[Integer.parseInt(additionalComponents.nextToken())];


            }
            
            //return allFields[index];
            return compsToRet;
        }else {
            LogWriter.writeFormLog("{stream} ERROR DefaultAcroRenderer.getComponentByName() Object NOT Integer and NOT null",debugUnimplemented);
            
            return null;
        }
	}

	/**
     * return a List containing the names of  forms on a specific page which has been decoded.
     * 
     * 
     * USE of this method is NOT recommened. Use getNamesForAllFields() in PdfDecoder
	 * @throws PdfException  An exception is thrown if page not yet decoded
     */
	public List getComponentNameList() throws PdfException {

        if(showMethods)
            System.out.println("getComponentNameList");

        if((trackPagesRendered==null)|(this.fieldCount==0))
			return null;
		
        
		/**make sure all forms decoded*/
		for(int p=1;p<this.pageCount+1;p++){
			this.createDisplayComponentsForPage(p,null,this.scaling,this.rotation);
		}
		

        List list= getComponentNameList(-1);

        /**debug code
        //check list for duplicates
        int count=list.size();
        for(int i=0;i<count;i++){
            String key=(String) list.get(i);

            for(int j=i+1;j<count;j++){
                String nextkey=(String)list.get(j);
                if(nextkey.equals(key))
                System.out.println("Duplicates on List "+key);
	}
        }/**/
	

        return list;
    }

    private Map duplicates =new HashMap();

	/**
     * return a List containing the names of  forms on a specific page which has been decoded.
     * 
	 * @throws PdfException  An exception is thrown if page not yet decoded
     */
	public List getComponentNameList(int pageNumber) throws PdfException {

        if(showMethods)
                    System.out.println("getComponentNameList "+pageNumber);

        if((trackPagesRendered==null)|(this.fieldCount==0))
			return null;
		
        if((pageNumber!=-1)&&(trackPagesRendered[pageNumber]==-1))
        	return null; //now we can interrupt decode page this is more appropriate
          //  throw new PdfException("[PDF] Page "+pageNumber+" not decoded");
        
        int currentComp;
        if(pageNumber==-1)
        	currentComp=0;
        else
        	currentComp=trackPagesRendered[pageNumber];
        
        ArrayList nameList = new ArrayList();
        
        //go through all fields on page and add to list
        String lastName = "";
        String currentName = "";
        while((pageNumber==-1)||(pageMap[currentComp]==pageNumber)){
            if(allFields[currentComp]!=null){
                //ensure following fields don't get added if (e.g they are a group)
            	
                currentName = removeStateToCheck(allFields[currentComp].getName(),false);
                if(currentName!=null && !lastName.equals(currentName)){

                    //track duplicates
                    String previous= (String) duplicates.get(currentName);
                    if(previous!=null)
                        duplicates.put(currentName, previous+","+currentComp);
                    else
                        duplicates.put(currentName,""+currentComp);

                    //add to list
                        nameList.add(currentName);
                        lastName = currentName;
                }
            }
            currentComp++;
            if(currentComp==pageMap.length)
                break;
        }
        
        return nameList;
	}

	/**setup object which creates all GUI objects*/
	public void setFormFactory(FormFactory newFormFactory) {
        if(showMethods)
                        System.out.println("setFormFactory "+newFormFactory);

        formFactory=newFormFactory;
		
	}

	
	/**does nothing or FORM except set type, resets annots*/
	public void openFile(int pageCount) {

        if(showMethods)
                    System.out.println("openFile "+pageCount);


        this.pageCount=pageCount;
		
		type=FORM;
		
	}

	public List getAllComponents() throws PdfException {

        if(showMethods)
                        System.out.println("getAllComponents");

        return null;
	}

	/**offsets for forms in multi-page mode*/
	public void setPageDisplacements(int[] xReached, int[] yReached) {

        if(showMethods)
            System.out.println("setPageDisplacements ");

        this.xReached=xReached;
		this.yReached=yReached;
		
	}

	/**
	 * returns the default values for all the forms in this document
	 */
	public String[] getDefaultValues() {
		return defaultValues;
	}

	/**
	 * returns the Type of pdf form, of the named field
	 */
	public Integer getTypeValueByName(String fieldName){
		return (Integer) typeValues.get(fieldName);
}
}
