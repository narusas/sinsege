package org.jpedal.objects.acroforms;

import java.awt.Component;

/**
 * methods to create fields for forms
  */
public interface FormFactory {

	public static final Integer LIST = new Integer(1);
	public static final Integer COMBOBOX = new Integer(2);
	public static final Integer SINGLELINETEXT = new Integer(3);
	public static final Integer SINGLELINEPASSWORD = new Integer(4);
	public static final Integer MULTILINETEXT = new Integer(5);
	public static final Integer MULTILINEPASSWORD = new Integer(6);
    public static final Integer PUSHBUTTON = new Integer(7);
    public static final Integer RADIOBUTTON = new Integer(8);
    public static final Integer CHECKBOXBUTTON = new Integer(9);
    public static final Integer ANNOTATION = new Integer(10);
    public static final Integer SIGNATURE = new Integer(11);

	/**
     * setup and return a List component, from the specified formObject
     * @see FormObject
     */
    public Component listField(FormObject formObject);

    /**
     * setup and return a ComboBox component, from the specified formObject
     * @see FormObject
     */
    public Component comboBox(FormObject formObject);

    /**
     * setup and return a single line Text component, from the specified formObject
     * @see FormObject
     */
    public Component singleLineText(FormObject formObject);

    /**
     * setup and return a single line Password component, from the specified formObject
     * @see FormObject
     */
    public Component singleLinePassword(FormObject formObject);

    /**
     * setup and return a multi line Text component, from the specified formObject
     * @see FormObject
     */
    public Component multiLineText(FormObject formObject);

    /**
     * setup and return a multi line Password component, from the specified formObject
     * @see FormObject
     */
    public Component multiLinePassword(FormObject formObject);

    /**
     * setup and return a push button component, from the specified formObject
     * @see FormObject
     */
    public Component pushBut(FormObject formObject);

    /**
     * setup and return a single radio button component, from the specified formObject
     * @see FormObject
     */
    public Component radioBut(FormObject formObject);

    /**
     * setup and return a single checkBox button component, from the specified formObject
     * @see FormObject
     */
    public Component checkBoxBut(FormObject formObject);

	/**
	 * setup annotations display with popups, etc
	 */
	public Component annotationButton(FormObject formObject);
	
	/**
	 * setup the signature field
	 */
	public Component signature(FormObject formObject);

	/**
	 * resets the factory for each page
	 */
	public void reset(AcroRenderer acroRenderer, ActionHandler formsActionHandler);

}
