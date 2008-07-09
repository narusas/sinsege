/**
* ===========================================
* Storypad
* ===========================================
*
* Project Info:  http://www.idrsolutions.com
* Project Lead:  Mark Stephens (mark@idrsolutions.com)
*
* (C) Copyright 2004, IDRsolutions and Contributors.
*
* ---------------
* StorypadData.java.java
* ---------------
* (C) Copyright 2002, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal.objects;

import org.jpedal.utils.Strip;

import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

/**
 * basic data structure
 */
public class StoryData implements Cloneable, Serializable {

    public static final int X1=0;

    public static final int X2=1;

    public static final int Y1=2;

    public static final int Y2=3;

    Map newShapes=new HashMap();


    /**track linked areas*/
	public float[] lx1,lx2,ly1,ly2;


	/**used to hide our encoding values in fragments of data so we can strip out*/
    public static String marker = ( "" + ( (char)( 65535 ) ) );
    public static String hiddenMarker = ( "" + ( (char)( 65534 ) ) );

    /**initial array size*/
    protected int max=2000;

    /**Linked items*/
    public Object[] links=new Object[max];

    /**hold the raw content*/
	public String[] contents=new String[max];
	public String[] rawData=new String[max];

    public String[] prefix=new String[max];

    public int[]   regExpStatus=new int[max];

    /**hold the content unformatted*/
    public String[] unformattedContent=new String[max];

    /**hold flag on raw content orientation*/
    public int[] f_writingMode=new int[max];

    /**hold raw content*/
    public String[] f_font_used=new String[max];

    /**hold raw content*/
    public String[] font_data=new String[max];

    /**hold raw content*/
    public int[] text_length=new int[max];

    /**hold raw content*/
    public int[] move_command=new int[max];

    /**hold raw content*/
    public float[] f_character_spacing=new float[max];

    /**hold raw content*/
    public int[] token_counter=new int[max];

    /**hold raw content*/
    public int[] f_end_font_size=new int[max];

    /**hold raw content*/
    public float[] space_width=new float[max];

    /**hold raw content*/
    public float[] f_x1=new float[max];

    /**hold color content*/
    public String[] colorTag=new String[max];

     /**hold raw content*/
    public float[] f_x2=new float[max];

     /**hold raw content*/
    public float[] f_y1=new float[max];

     /**hold raw content*/
    public float[] f_y2=new float[max];

    /**user defined label -ie TEXT, BYLINE*/
    public String[] category=new String[max];

    /**IMAGE or TEXT*/
    public int[] objectType=new int[max];
    
    public int[] lineCount=new int[max];

    /**flag to show colour info extracted*/
    boolean isColorExtracted;

    /**
     * get a co-ord value for record
     * @deprecated use getCorordinates(type) and cache for faster access
     */
    public float getCoordinate(int type,int storyNumber){

        float value=-1;

        switch (type) {
        case StoryData.X1:
            value = f_x1[storyNumber];
            break;
        case StoryData.X2:
            value = f_x2[storyNumber];
            break;
        case StoryData.Y1:
            value = f_y1[storyNumber];
            break;
        case StoryData.Y2:
            value = f_y2[storyNumber];
            break;
        default:
            value = -1;
        }

        return value;

    }

    /**
     * get a co-ord value for record
     */
    public float[] getCoordinates(int type,boolean isCombinedArea){

		float[] value=null;

        switch (type) {
        case StoryData.X1:
			if(isCombinedArea && lx1!=null)
				value=lx1;
			else
				value = f_x1;
            break;
        case StoryData.X2:
			if(isCombinedArea  && lx2!=null)
				value=lx2;
			else
				value = f_x2;
            break;
        case StoryData.Y1:
			if(isCombinedArea  && ly1!=null)
				value=ly1;
			else
				value = f_y1;
            break;
        case StoryData.Y2:
			if(isCombinedArea  && ly2!=null)
				value=ly2;
			else
				value = f_y2;
            break;
        default:
            value = null;
        }

        return value;

    }

    /**
     * set colour extraction
     */
    public void enableTextColorDataExtraction() {
        isColorExtracted=true;
    }

    /**
     * flag to show if color extracted in xml
     */
    public boolean isColorExtracted() {
        return isColorExtracted;
    }

    /**
     * get type (ie TEXT,IMAGE)
     */
    public int getObjectType(int i) {
        return objectType[i];
    }


    /**
     * get type (ie BYLINE,CAPTION)
     */
    public String getCategory(int i) {

        return category[i];
    }

    /**
     * get text as XML value (unicode plus XML tags)
     */
    public String getXMLContent(int current_story) {

        String value=unformattedContent[current_story];
        if(prefix[current_story]!=null)
        value=prefix[current_story]+value;
        return value;
    }

    /**
     * get text as XML value (unicode plus XML tags)
     */
    public void setXMLContent(int current_story,String content) {

        unformattedContent[current_story]=content;
    }

    /**
     * get text content (unicode)
     */
    public String getTextContent(int current_story,boolean addPrefix,boolean stripXML) {

        String value=contents[current_story];
        if(prefix[current_story]!=null && addPrefix){
			if(stripXML)
				value= Strip.convertToText(prefix[current_story])+value;
			else
				value= prefix[current_story]+value;
		}

		return value;
    }

    /**
     * set text content (unicode)
     */
    public void setTextContent(int current_story,String content) {

        contents[current_story]=content;


    }

    /**
     * set flag to show what category (ie title,byline)
     */
    public void setCategory(String newCategory, int current_story) {
        category[current_story]=newCategory;

    }

    /**
     * set flag to show what type (ie IMAGE, TEXT)
     */
    public void setObjectType(int newType, int current_story) {
        objectType[current_story]=newType;
    }

    /**
     * get number of stories
     */
    public int getStoryCount(){
        return f_x1.length;
    }

     public Object clone() {
            try {
                return super.clone();
            } catch (Exception e) {
                throw new Error("This should not occur since we implement Cloneable");
            }
        }


    /**
     *Returns the linkedItems as a list of ids
     */
    public int[] getLinkedItems(int i) {
        return (int[]) links[i];
    }

    /**
     * Sets the linkedItems as a list of ids
     */
    public void setLinkedItems(int i,int[] list) {
        links[i]=list;
    }


    /**
     * The linkedItems to set (int[] with ids
     */
    public void appendLinkedItem(int i,int[] linkedItems) {

        if(linkedItems!=null){
            for(int j=0;j<linkedItems.length;j++){
                if(linkedItems[j]!=-1)
                System.out.println(i+" Added "+linkedItems[j]);
            }
        }

        this.links[i] = linkedItems;
    }

    /**
     * used by Storypad to hold shape where sliced up
     * @param current_story
     * @return
     */
    public String getNewShapeData(int current_story) {
       return (String) newShapes.get(new Integer(current_story));
    }

    /**
     * used by Storypad to hold shape where sliced up
     */
    public void addNewShape(int current_story, String data) {
        newShapes.put(new Integer(current_story),data);
    }
}
