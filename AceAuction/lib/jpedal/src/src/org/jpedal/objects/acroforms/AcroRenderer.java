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
* AcroRenderer.java
* ---------------
* (C) Copyright 2005, by IDRsolutions and Contributors.
*
* 
* --------------------------
*/
package org.jpedal.objects.acroforms;

import java.awt.Component;
import java.awt.Graphics2D;
import java.util.List;

import org.jpedal.PdfPanel;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.PdfPageData;

/**
 * sets up the display and called to draw objects as needed
 */
public interface AcroRenderer {
	
	/**reset Handler*/
	public void resetActionHandler(Object userActionHandler);
	
	/**called before decode page by clearscreen to remove components  -user should not call*/
	public void removeDisplayComponentsFromScreen(PdfPanel panel);
	
	/**called when new file opened - different purpose for Annots and form*/
	public void openFile(int pageCount);
	
	/**called before decode page by clearscreen to remove components  -user should not call*/
	public void init(Object obj,int insetW,int insetH,PdfPageData pageData,PdfObjectReader curentPdfFile);
	
	/**create display - called inside PDF decoder once page decoded -user should not call
	 * @param panel*/
	public void createDisplayComponentsForPage(int page, PdfDecoder panel,float scaling,int rotation);
	
	/**set size to match scaling - called inside PDF decoder when rendering to adjust size -user should not call
	 * */
	public void resetScaledLocation(float scaling, int rotation, int indent);
	
	
	/**get array of components from displayed page (will create if page not displayed)
	 * 
	 * @deprecated
	 * */
	public Component[] getDisplayComponentsForPage(int i);

	/**
	 * add forms onto g2 (used to print and add to thumbnails
	 */
	public void renderFormsOntoG2(Graphics2D g2,int pageIndex,float scaling,int rotation);

    
    /**
     * return the component associated with this objectName (returns null if no match). Names are case-sensitive.
     * Please also see method getComponentNameList(int pageNumber)
     */
    public Component[] getComponentsByName(String objectName);
    
    /**
     * return a List containing the names of  forms on a specific page which has been decoded.
     * 
     * @throws PdfException  An exception is thrown if page not yet decoded
     */
    public List getComponentNameList(int pageNumber)  throws PdfException;
    
    /**
     * return a List containing the names of  forms on a specific page which has been decoded.
     * 
     * @throws PdfException  An exception is thrown if page not yet decoded
     */
    public List getComponentNameList()  throws PdfException;
    
    /** returns the default values for all the forms in this document */
    public String[] getDefaultValues();
    
    /**setup object which creates all GUI objects*/
	public void setFormFactory(FormFactory newFormFactory);

	/**used to draw forms from multiple pages*/
	public void displayComponentsOnscreen(int startPage, int endPage,PdfPanel panel, float scaling, int rotation);

	/**page offsets in multi-page mode*/
	public void setPageDisplacements(int[] xReached, int[] yReached);

	public void removePageRangeFromDisplay(int i, int j, PdfPanel decoder);
	
	public Integer getTypeValueByName(String fieldName);

}
