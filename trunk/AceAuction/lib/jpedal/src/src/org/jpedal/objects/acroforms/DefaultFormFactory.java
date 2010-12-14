package org.jpedal.objects.acroforms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.LookupOp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.jpedal.gui.ShowGUIMessage;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Strip;


/**
 * default set of methds to create Form fields using standard Swing components.
 * Can be replaced with your own set if required.
 */
public class DefaultFormFactory implements FormFactory{

    final private static boolean debugUnimplemented = false;//to show unimplemented parts
    
    /**
     * shows the icon images as they are being applied to the component
     */
    private static boolean showIconImages=false;
    
    private AcroRenderer acrorend;
    
    //prints all debugging information
    private boolean printAllouts=false;
    //used when only one method needs debugging,
    //see create(Button or choice or text) first line /**/
    private boolean printouts=printAllouts;
    
    private ActionHandler formsActionHandler;
    
    private DefaultFormFactory(){}
    
    /**
     * allows access to rennderer variables
     * @param actionHandler 
     */
    public DefaultFormFactory(AcroRenderer acroRenderer, ActionHandler actionHandler){
        acrorend = acroRenderer;
        formsActionHandler = actionHandler;
    }
    
    public void reset(AcroRenderer acroRenderer, ActionHandler actionHandler){
    	acrorend = acroRenderer;
        formsActionHandler = actionHandler;
    }

    /**
	 * setup annotations display with popups, etc
	 */
	public Component annotationButton(final FormObject form) {
		
		//System.out.println("formobject="+formObject);

		JButton but = new JButton();//formObject.contents);
//		System.out.println("annotation text="+formObject.contents);
		
		setupButton(but,form);
		
		setupUniversalFeatures(but,form);
		
//		but.setBorderPainted(false);
//		but.setBorder(null);
		
		return but;
	}
	
	/**
     * setup and return the ComboBox field specified in the FormObject
     */
    public Component comboBox(final FormObject form) {
		
        //populate items array with list from Opt
        String[] items = form.itemsList;
        JComboBox comboBox;
        if(items==null)
        	comboBox = new JComboBox();
        else
        	comboBox = new JComboBox(items);
        
        //get and set currently selected value
        String textValue = form.selectedItem;
        if(form.valuesMap!=null){
        	comboBox.setSelectedItem(form.valuesMap.get(textValue));
        }else {
        	comboBox.setSelectedItem(textValue);
        }
        
        if(printouts)
        	System.out.println("currently selected value="+textValue);
        
        if(form.flags[18]){//FormStream.EDIT
        	if(printouts)
        		System.out.println("drop list and an editable text box");
        	comboBox.setEditable(true);
        	
        }else{//is not editable
        	if(printouts)
        		System.out.println("only a drop list");
        	comboBox.setEditable(false);
        }
        
//    	    if(form instanceof XFAFormObject && ((XFAFormObject)form).choiceShown==XFAFormObject.CHOICE_ENTRY){
//    	    	comboBox.addMouseListener(formsActionHandler.setComboClickOnEntry());
//    	    }
       	
        
    	    setupUniversalFeatures(comboBox,form);
        
        if((form.flags!=null)&&(form.flags[1])){//FormStream.READONLY
    	    comboBox.setEditable(false);//combo box
           	comboBox.setEnabled(false);
            	
            if(printouts)
                System.out.println("READONLY="+comboBox);
        }
        
        return comboBox;
    }
    
    /**
     * setup and return the CheckBox button specified in the FormObject
     */
    public Component checkBoxBut(final FormObject form) {
		
        //			the text value
		JCheckBox checkBut = new JCheckBox();
        
        setupButton(checkBut,form);
       	
        setupUniversalFeatures(checkBut,form);
        
        if((form.flags!=null)&&(form.flags[1])){//FormStream.READONLY
            checkBut.setEnabled(false);
            checkBut.setDisabledIcon(checkBut.getIcon());
            checkBut.setDisabledSelectedIcon(checkBut.getSelectedIcon());
//              retComponent.setForeground(Color.magenta);
//              retComponent.setBackground(Color.magenta);
                if(debugUnimplemented)
                    System.out.println("READONLY comp="+checkBut);
            }
        
        return checkBut;
    }
    
    /**
     * setup and return the List field specified in the FormObject
     */
    public Component listField(final FormObject form) {
        
        //populate the items array with list from Opt
        String[] items=form.itemsList;
        
        //create list (note we catch null value)
        JList list;
        if(items!=null)
        	list = new JList(items);
        else
        	list = new JList();
        
        if(!form.flags[21])//mulitselect
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //if there is a top index or selected value select it
        if(form.topIndex!=null){
        	list.setSelectedIndices(form.topIndex);
        	if(printouts){
        		System.out.println("topIndex should be="+ConvertToString.convertArrayToString(form.topIndex));
        	}
        }else {
        	String textValue = form.selectedItem;
        	if(form.valuesMap!=null){
        		list.setSelectedValue(form.valuesMap.get(textValue),true);
        	}else {
        		list.setSelectedValue(textValue,true);
        	}
        	if(printouts)
        		System.out.println("currently selected value="+textValue);
        }
        
        setupUniversalFeatures(list,form);
        
        return list;
    }
    
    /**
     * setup and return the multi line password field specified in the FormObject
     */
    public Component multiLinePassword(final FormObject form) {
		
        JPasswordField multipass;
        String textValue = form.textString;
       	int maxLength = form.maxTextLength;
        
        if(maxLength != -1)
        	multipass = new JPasswordField(textValue,maxLength);
        else
        	multipass = new JPasswordField(textValue);
        multipass.setEchoChar('*');
        
        if(printouts){
        	System.out.println("there is a password to be entered that can be MULTILINED");
        }
        
        setupUniversalFeatures(multipass,form);
        
        setupTextFeatures(multipass,form);
        
        if((form.flags!=null)&&(form.flags[1])){//FormStream.READONLY
        	multipass.setEnabled(false);
            multipass.setEditable(false);
            if(printouts)
                System.out.println("READONLY="+multipass);
        }
        
        return multipass;
    }

    /**
     * setup and return the multi line text area specified in the FormObject
     */
    public Component multiLineText(final FormObject form) {
    	
        JTextArea newTextarea = new JTextArea(form.textString);
        newTextarea.setLineWrap(true);

        //set length
    	int maxLength = form.maxTextLength;
        if(maxLength!=-1){
    		newTextarea.setColumns(maxLength);
    		if(printouts)
    			System.out.println("textlength added="+maxLength);
    	}
        
        setupUniversalFeatures(newTextarea,form);
        
        if((form.flags!=null)&&(form.flags[1])){//FormStream.READONLY
        	newTextarea.setEnabled(false);
            newTextarea.setEditable(false);
            if(printouts)
                System.out.println("READONLY="+newTextarea);
        }
        
        return newTextarea;
    }
    
    /**
     * setup and return a signature field component,
     * <b>Note:</b> SKELETON METHOD FOR FUTURE UPGRADES.
     */
    public Component signature(FormObject form){
		
		
		JButton sigBut=new JButton();
        
       	setupButton(sigBut,form);
       	
       	setupUniversalFeatures(sigBut,form);
            
        if((form.flags!=null)&&(form.flags[1])){//FormStream.READONLY
        	sigBut.setEnabled(false);
        	sigBut.setDisabledIcon(sigBut.getIcon());
        	sigBut.setDisabledSelectedIcon(sigBut.getSelectedIcon());
        	if(debugUnimplemented)
        		System.out.println("READONLY comp="+sigBut);
        }
        
        return sigBut;
    }
    
    /**
     * setup and return the Push button specified in the FormObject
     */
    public Component pushBut(final FormObject form) {
		
        //the text value
		JButton pushBut=new JButton();
        
        setupButton(pushBut,form);
        
        setupUniversalFeatures(pushBut,form);
        
        if((form.flags!=null)&&(form.flags[1])){//FormStream.READONLY
            pushBut.setEnabled(false);
            pushBut.setDisabledIcon(pushBut.getIcon());
            pushBut.setDisabledSelectedIcon(pushBut.getSelectedIcon());
            if(debugUnimplemented)
                System.out.println("READONLY comp="+pushBut);
        }
        
        return pushBut;
    }

    /**
     * setup and return the Radio button specified in the FormObject
     */
    public Component radioBut(final FormObject form) {
        
        //the text value
		JRadioButton radioBut = new JRadioButton();
    	//radioBut.setContentAreaFilled(false);//false for transparency
    
        setupButton(radioBut,form);
        
        setupUniversalFeatures(radioBut,form);
        
        if((form.flags!=null)&&(form.flags[1])){//FormStream.READONLY
            radioBut.setEnabled(false);
            radioBut.setDisabledIcon(radioBut.getIcon());
            radioBut.setDisabledSelectedIcon(radioBut.getSelectedIcon());
//              retComponent.setForeground(Color.magenta);
//              retComponent.setBackground(Color.magenta);
                if(debugUnimplemented)
                    System.out.println("READONLY comp="+radioBut);
            }
        
        return radioBut;
    }


    /**
     * setup and return the single line password field specified in the FormObject
     */
    public Component singleLinePassword(final FormObject form) {
		
        JPasswordField newPassword=new JPasswordField(form.textString);
        newPassword.setEchoChar('*');
        
        //				set length
       	int maxLength = form.maxTextLength;
       	if(maxLength!=-1){
       		newPassword.setColumns(maxLength);
       		if(printouts)
       			System.out.println("textlength added="+maxLength);
       	}
        
       	setupUniversalFeatures(newPassword,form);
        
       	setupTextFeatures(newPassword,form);
        
        if((form.flags!=null)&&(form.flags[1])){//FormStream.READONLY
        	newPassword.setEnabled(false);
            newPassword.setEditable(false);
            if(printouts)
                System.out.println("READONLY="+newPassword);
        }
        
        return newPassword;
    }

    /**
     * setup and return the single line text field specified in the FormObject
     */
    public Component singleLineText(final FormObject form) {
        
        JTextField newTextfield = new JTextField(form.textString);
        
        //set length
    	int maxLength = form.maxTextLength;
    	if(maxLength!=-1){
    		newTextfield.setColumns(maxLength);
    		if(printouts)
    			System.out.println("textlength added="+maxLength);
    	}
        
    	setupUniversalFeatures(newTextfield,form);
    	
    	setupTextFeatures(newTextfield,form);
        
        if((form.flags!=null)&&(form.flags[1])){//FormStream.READONLY
        	newTextfield.setEnabled(false);
            newTextfield.setEditable(false);
            if(printouts)
                System.out.println("READONLY="+newTextfield);
        }
        
        return newTextfield;
        }
        
	//############ below is all text setup ################ TAG
    /**
	 * sets up all the required attributes for all text fields
	 */
	private void setupTextFeatures(JTextField textcomp,FormObject form) {
        //set text field alignment
        if(form.allignment!=-1){
        	textcomp.setHorizontalAlignment(form.allignment);
    	}
        
        }
        
    //################# below is buttons setup ################## TAG
    /**
     * sets up the buttons captions, images, etc
     * for normal, rollover, down and off or on if radio or check buttons
     */
    private void setupButton(AbstractButton comp,FormObject form) {
    	//transparancy
//    	((AbstractButton) comp).setContentAreaFilled(false);//false for transparency
    	
    	comp.setText(form.normalCaption);
    	comp.setContentAreaFilled(false);
    	if(form.downCaption!=null || form.rolloverCaption!=null)
    		comp.addMouseListener(formsActionHandler.setupChangingCaption(form.normalCaption,form.rolloverCaption,form.downCaption));
    	
    	if(form.appearancesUsed){
    		appearanceImages(form,comp,showIconImages);
        }
        
    	if(form.textPosition!=-1){
            /*
             * if there are any appearance images, then the text is set back to null,
             * if the textPosition needs to be setup you need to either set the text back here or not 
             * set it to null in appearanceImages. 
             * 
             * If you need to set this up check file acodabb.pdf page 4 as it has an icon with text being
             * set to overlay the icon, which doesn't work.
             */
            switch(form.textPosition){
            case 0: comp.setIcon(null);break;//0=caption only
            case 1: comp.setText(null);break;//1=icon only
            case 2: comp.setVerticalTextPosition(SwingConstants.BOTTOM);break;//2=caption below icon
            case 3: comp.setVerticalTextPosition(SwingConstants.TOP);break;//3=caption above icon
            case 4: comp.setHorizontalTextPosition(SwingConstants.RIGHT);break;//4=caption on right of icon
            case 5: comp.setHorizontalTextPosition(SwingConstants.LEFT);break;//5=caption on left of icon
            case 6: comp.setText(null);break;/*checkBut.setVerticalTextPosition(SwingConstants.CENTER);
            	comp.setHorizontalTextPosition(SwingConstants.CENTER);//6=caption overlaid ontop of icon */
            }
        }
    	
    	//TODO get margin data from formobject
    	Insets insetZero = new Insets(0,0,0,0);
        comp.setMargin(insetZero);
        
    }
    
    /**
     * gets each appearance image from the map <b>appearance</b> and
     * and adds it to the relevent icon for the AbstractButton <b>comp</b>
     * showImages is to display the appearance images for that FormObject
     */
    private void appearanceImages(final FormObject form, AbstractButton comp,boolean showImages) {
        //get default state
        String defaultState = form.defaultState;
        if(printouts)
            System.out.println("default state="+defaultState);
        if(defaultState!=null && defaultState.equals(form.normalOnState)){
            	comp.setSelected(true);
        }
        
        if(form.hasNormalOff()){
            comp.setText(null);
           	comp.setIcon(new FixImageIcon(form.normalOffImage));
            if(showImages)
                ShowGUIMessage.showGUIMessage("normalAppOffImage",form.normalOffImage,"normalAppOff");
        }
        if(form.hasNormalOn()){
            comp.setText(null);
            comp.setSelectedIcon(new FixImageIcon(form.normalOnImage));
            if(showImages)
                ShowGUIMessage.showGUIMessage("normalAppOnImage",form.normalOnImage,"normalAppOn");
        }
        
        if(form.noDownIcon){
            comp.setPressedIcon(comp.getIcon());
        }else {
            BufferedImage downOff = null,downOn = null;
            
            if(form.offsetDownIcon && !form.hasDownImage()){
                if(form.hasNormalOff()){
                    if(form.hasNormalOn()){
                        downOn = createPressedLook(form.normalOnImage);
                        downOff = createPressedLook(form.normalOffImage);
                    }else {
                        downOff = createPressedLook(form.normalOffImage);
                    }
                }else if(form.hasNormalOn()){
                    downOff = createPressedLook(form.normalOnImage);
                }
            }else if(form.invertDownIcon){
            	if(form.hasNormalOff()){
                    if(form.hasNormalOn()){
                    	downOff = invertImage(form.normalOffImage);   
        	    		downOn = invertImage(form.normalOnImage);
                    }else {
                    	downOff = invertImage(form.normalOffImage);
                    }
                }else if(form.hasNormalOn()){
                	downOff = invertImage(form.normalOnImage);
                }
            }else {
                downOff = form.downOffImage;
                downOn = form.downOnImage;
            }
            
            if(downOff==null && downOn==null){
//            	invert to show button action
            	if(form.hasNormalOff()){
                    if(form.hasNormalOn()){
                    	downOff = invertImage(form.normalOffImage);   
        	    		downOn = invertImage(form.normalOnImage);
                    }else {
                    	downOff = invertImage(form.normalOffImage);
                    }
                }else if(form.hasNormalOn()){
                	downOff = invertImage(form.normalOnImage);
                }
            }
            
            if(showImages){
                ShowGUIMessage.showGUIMessage("downAppOffImage",form.downOffImage,"downAppOff");
                ShowGUIMessage.showGUIMessage("downAppOnImage",form.downOnImage,"downAppOn");
            }
            
            if(downOff==null || downOn==null){
                
                if(downOff!=null){
                    comp.setText(null);
                    comp.setPressedIcon(new FixImageIcon(downOff));
                }else if(downOn!=null){
                    comp.setText(null);
                    comp.setPressedIcon(new FixImageIcon(downOn));
                }
                
                if(printouts)
                    System.out.println("CHECK there is an off or on down image not both does this work, DefaultAcro.createAppearanceImages");
            }else{
                
                if(comp.isSelected())
                    comp.setPressedIcon(new FixImageIcon(downOn));
                else
                    comp.setPressedIcon(new FixImageIcon(downOff));
                
                comp.addActionListener(formsActionHandler.setupChangingDownIcon(downOff,downOn));
            }
        }
        
        if(form.hasRolloverOff()){
            comp.setRolloverEnabled(true);
            comp.setText(null);
            comp.setRolloverIcon(new FixImageIcon(form.rolloverOffImage));
            if(showImages)
                ShowGUIMessage.showGUIMessage("rolloverAppOffImage",form.rolloverOffImage,"rolloverAppOff");
        }
        if(form.hasRolloverOn()){
            comp.setRolloverEnabled(true);
            comp.setText(null);
            comp.setRolloverSelectedIcon(new FixImageIcon(form.rolloverOnImage));
            if(showImages)
                ShowGUIMessage.showGUIMessage("rolloverAppOnImage",form.rolloverOnImage,"rolloverAppOn");
        }
    }
    
	private BufferedImage invertImage(BufferedImage image) {
    	if(image==null)
    		return null;
    	
    	BufferedImage ret = new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
        
        byte reverse[] = new byte[256]; 
        for (int j=0; j<200; j++){  
            		reverse[j]=(byte)(256-j);  
     	}	 
        ByteLookupTable blut=new ByteLookupTable(0, reverse);  
        LookupOp lop = new LookupOp(blut, null);  
        lop.filter(image,ret);
        
        return ret;
	}

	/**
     * create a pressed look of the <b>image</b> and return it
     */
    private BufferedImage createPressedLook(Image image) {
    	BufferedImage pressedImage = new BufferedImage(image.getWidth(null)+2,image.getHeight(null)+2,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)pressedImage.getGraphics();
        g.drawImage(image, 1, 1, null);
        g.dispose();
        return pressedImage;
    }
    
	//############  below is universal setup ################## TAG
    /**
     * sets up the features for all fields, transparancy, font, color, border, actions,
     * background color, 
     */
    private void setupUniversalFeatures(JComponent comp,FormObject form){
    	
    	comp.setOpaque(false);
    	if(form.textFont!=null)
    		comp.setFont(form.textFont);
    	comp.setForeground(form.textColor);
       	
		Border newBorder = createBorderStyle(form.border,form.borderColor,Color.white);
		comp.setBorder(newBorder);
        if(printouts){
        	System.out.println("borderStyle="+newBorder);
        }
        
        if(form.backgroundColor!=null){
        	comp.setBackground(form.backgroundColor);
        	comp.setOpaque(true);
        }

        //@action - this is where all mouse events are setup
        //we could clone for additional items such as Key Listeners
        
        setupMouseListener(comp,form);
        if(printouts){
        	System.out.println("flagNum="+form.characteristic);
        }
        
    }
    
    /**
     * setup the events for currentComp, from the specified parameters
     *
     * @Action - mouse events added here
     */
    private void setupMouseListener(final Component currentComp,FormObject form){ 
    	/* bit 1 is index 0 in boolean[]
    	 * 1 = invisible
    	 * 2 = hidden - dont display or print
    	 * 3 = print - print if set, dont if not
    	 * 4 = nozoom
    	 * 5= norotate
    	 * 6= noview
    	 * 7 = read only (ignored by wiget)
    	 * 8 = locked
    	 * 9 = togglenoview
    	 */

        if(form.characteristic[0] || form.characteristic[1] || form.characteristic[5]){
    		currentComp.setVisible(false);
    	}
    	
    	if(form.characteristic[8]){ 
    		/*set visible on mouse over*/
    		currentComp.addMouseListener(formsActionHandler.setupCommand("togglenoview",acrorend));
    	}
    	
    	/*entered Action*/
    	if(form.enteredAction!=null){
    		if(currentComp instanceof JComboBox){
    			
    			((JComboBox)currentComp).getComponent(0).addMouseListener(formsActionHandler.setupEnteredAction(form.enteredAction,acrorend));
    		}else{
    			
    			currentComp.addMouseListener(formsActionHandler.setupEnteredAction(form.enteredAction,acrorend));
    		}
    	}
    	
    	/*exited action*/
    	if(form.exitedAction!=null){
    		if(currentComp instanceof JComboBox){
    			
    			((JComboBox)currentComp).getComponent(0).addMouseListener(formsActionHandler.setupExitedAction(form.exitedAction,acrorend));
    		}else{
    			currentComp.addMouseListener(formsActionHandler.setupExitedAction(form.exitedAction,acrorend));
    		}
    	}
    	
    	/*activate action*/
    	if(form.activateAction!=null){
    		Iterator iter = form.activateAction.keySet().iterator();
    		while(iter.hasNext()){
    			Object nextKey = iter.next();
    			if(nextKey.equals("ResetForm")){
    				if(currentComp instanceof JComboBox){
    					((JComboBox)currentComp).getComponent(0).addMouseListener(formsActionHandler.setupCommand("ResetForm",acrorend));
    				}else{
    					currentComp.addMouseListener(formsActionHandler.setupCommand("ResetForm",acrorend));
    				}
    			}else if(nextKey.equals("Print")){
    				if(currentComp instanceof JComboBox){
    					((JComboBox)currentComp).getComponent(0).addMouseListener(formsActionHandler.setupCommand("Print",acrorend));
    				}else{
    					currentComp.addMouseListener(formsActionHandler.setupCommand("Print",acrorend));
    				}
    			}else if(nextKey.equals("SaveAs")){
    				if(currentComp instanceof JComboBox){
    					((JComboBox)currentComp).getComponent(0).addMouseListener(formsActionHandler.setupCommand("SaveAs",acrorend));
    				}else{
    					currentComp.addMouseListener(formsActionHandler.setupCommand("SaveAs",acrorend));
    				}
    			}else if(nextKey.equals("Hide")){
    				if(currentComp instanceof JComboBox){
    					((JComboBox)currentComp).getComponent(0).addMouseListener(formsActionHandler.setupHideAction(((Map)form.activateAction.get("Hide")),acrorend));
    				}else{
    					currentComp.addMouseListener(formsActionHandler.setupHideAction(((Map)form.activateAction.get("Hide")),acrorend));
    				}
    			}else if(nextKey.equals("SubmitForm")){
    				Map aDataMap = (Map) form.activateAction.get("SubmitForm");
    				
    				if(currentComp instanceof JComboBox){
    					((JComboBox)currentComp).getComponent(0).addMouseListener(formsActionHandler.setupSubmitAction(aDataMap,acrorend));
    				}else{
    					currentComp.addMouseListener(formsActionHandler.setupSubmitAction(aDataMap,acrorend));
    				}
    			}else if(nextKey.equals("Popup")){
    				if(currentComp instanceof JComboBox){
    					((JComboBox)currentComp).getComponent(0).addMouseListener(formsActionHandler.setupClickedAction(form.activateAction,acrorend));
    				}else{
    					currentComp.addMouseListener(formsActionHandler.setupClickedAction(form.activateAction,acrorend));
    				}
    			}else if(nextKey.equals("URL")){
    				String text = (String) form.activateAction.get("URL");
    				((JComponent)currentComp).setToolTipText(text);
    			}else if(nextKey.equals("GoToR")){
    				if(currentComp instanceof JComboBox){
    					((JComboBox)currentComp).getComponent(0).addMouseListener(formsActionHandler.setupClickedAction(form.activateAction,acrorend));
    				}else{
    					currentComp.addMouseListener(formsActionHandler.setupClickedAction(form.activateAction,acrorend));
    				}
    			}else if(nextKey.equals("Dest")){
    				if(currentComp instanceof JComboBox){
    					((JComboBox)currentComp).getComponent(0).addMouseListener(formsActionHandler.setupClickedAction(form.activateAction,acrorend));
    				}else{
    					currentComp.addMouseListener(formsActionHandler.setupClickedAction(form.activateAction,acrorend));
    				}
    			}else {
    				LogWriter.writeFormLog("key not implemented DefaultFormFactory.setupMouseListener key="+nextKey,debugUnimplemented);
    			}
    		}
    	}
    	
    	if(form.validateValue!=null){
    		if(currentComp instanceof JComboBox){
				((JComboBox)currentComp).getComponent(0).addPropertyChangeListener(formsActionHandler.setupValidateAction(form.validateValue));
			}else{
				currentComp.addPropertyChangeListener(formsActionHandler.setupValidateAction(form.validateValue));
			}
    	}
    	
    }

    /**
	 * setup the border style
	 */
	private  Border createBorderStyle(Object borderObj, Color borderColor,Color borderBackgroundColor) {
		/**Type must be Border
		 * W width in points (if 0 no border, default =1)
		 * S style - (default =S)
		 * 	S=solid, D=dashed (pattern specified by D entry below), B=beveled(embossed appears to above page),
		 * 	I=inset(engraved appeared to be below page), U=underline ( single line at bottom of boundingbox)
	     * D array phase - e.g. [a b] c means:-  a=on blocks,b=off blocks(if not present default to a),
	     * 		c=start of off block preseded index is on block.
	     * 	i.e. [4] 6 :- 4blocks on 4blocks off, block[6] if off - 1=off 2=on 3=on 4=on 5=on 6=off 7=off 8=off 9=off etc...
		 *
		 */
		if(printouts){
	        System.out.println("createBorderStyle() color="+borderColor+" background color="+borderBackgroundColor+"\n\tfield="+borderObj);
		}

		Map borderStream = new HashMap();
		if(borderObj==null){
		    borderStream.put("S","/S");
		    borderStream.put("W","1");
		}else{
		    if(borderObj instanceof Map){
		        borderStream = (Map)borderObj;
		    }else {
		    	LogWriter.writeFormLog("{DefaultFormFactory.createBorderStyle} border stream is String DefaultFormFactory.createBorderStyle",debugUnimplemented);
		    }
		}

		if(borderBackgroundColor==null){
//		    borderBackgroundColor = new Color(0,0,0,0);
		    if(printouts)
		        System.out.println("background border color null");
		}
		if(borderColor==null){
//		    borderColor = new Color(0,0,0,0);
		    if(printouts)
		        System.out.println("border color null");
		    return null;
		}

	    Border newBorder = null;

		String width = ((String)borderStream.get("W"));
	    if(printouts)
	        System.out.println("width="+width);

		String style = ((String)borderStream.get("S"));
	    style = Strip.checkRemoveLeadingSlach(style);
	    if(printouts)
	    	System.out.println("style="+style);

	    if(style.equals("U")){
	        if(printouts)
	            System.out.println("FormStream.createBorderStyle() U CHECK="+ConvertToString.convertMapToString(borderStream,null));
	        newBorder = BorderFactory.createMatteBorder(0,0,Integer.parseInt(width),0,borderColor);//underline field

	    }else if(style.equals("I")){
	        if(printouts)
            	System.out.println("FormStream.createBorderStyle() I CHECK="+ConvertToString.convertMapToString(borderStream,null));
            newBorder = BorderFactory.createEtchedBorder(borderColor,borderBackgroundColor);//inset below page

	    }else if(style.equals("B")){
	        if(printouts)
	            System.out.println("FormStream.createBorderStyle() B CHECK="+ConvertToString.convertMapToString(borderStream,null));
	        newBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED,borderColor,borderBackgroundColor);//beveled above page

	    }else if(style.equals("S")){
	        if(printouts)
	        	System.out.println("FormStream.createBorderStyle() S CHECK="+ConvertToString.convertMapToString(borderStream,null));
	        newBorder = BorderFactory.createLineBorder(borderColor,Integer.parseInt(width));//solid

	    }else if(style.equals("D")){
			Object dashPattern = borderStream.get("D");

	        if(dashPattern instanceof String){
	            if(debugUnimplemented)
					System.out.println("createBorderStyle D pattern is String="+(dashPattern));
	        }else if(dashPattern instanceof Map){
	            if(debugUnimplemented)
					System.out.println("createBorderStyle D pattern is Map="+ConvertToString.convertMapToString(((Map)dashPattern),null));
	        }else{
	            if(debugUnimplemented)
					System.out.println("createBorderStyle D pattern is UNKNOWN="+dashPattern);
	        }

			//setup dash pattern TODO MARK dashline
//	        new DashedBorder(Color.magenta,width,height);
//	    (new BasicStroke(3/scaling, BasicStroke.CAP_ROUND,
//	            BasicStroke.JOIN_ROUND, 0, new float[]{0,6/scaling,0,6/scaling}, 0));

	    }else{
	        if(debugUnimplemented)
			    System.out.println("NOT IMPLEMENTED BS - border stream="+ConvertToString.convertMapToString(borderStream,null));
	    }

	    return newBorder;
	}

    	}
