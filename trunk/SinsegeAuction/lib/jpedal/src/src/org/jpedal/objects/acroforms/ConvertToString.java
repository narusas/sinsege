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
 * ConvertToString.java
 * ---------------
 *
 * Original Author:  Mark Stephens (mark@idrsolutions.com)
 * Contributor(s):
 *
 */
package org.jpedal.objects.acroforms;

import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;

import org.jpedal.io.PdfObjectReader;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author chris
 *
 * class to help convert data structures to a single String
 */
final public class ConvertToString {

	final static public String convertMapToString(NamedNodeMap currentMap){
		if(currentMap==null)
			return null;
		
		StringBuffer buf = new StringBuffer();
		
		Node node;
		for(int i=0;i<currentMap.getLength();i++){
			node = currentMap.item(i);
			buf.append("\t");
			buf.append(node.getNodeName());
			buf.append("=");
			buf.append(node.getNodeValue());
			buf.append("\n");
		}
		
		return buf.toString();
		
	}
	
    final static public String convertMapToString(Map currentField,PdfObjectReader currentPdfFile) {
        return convertMapToString(currentField,1,currentPdfFile);
    }
    
	/**
	 * converts a Map to a single String, each value seperated by a new line
	 */
	final static private String convertMapToString(Map currentField,int indent,PdfObjectReader currentPdfFile) {
		
		StringBuffer ret = new StringBuffer();
		Iterator iter=(currentField.keySet()).iterator();
		
		while(iter.hasNext()){
			String key=(String) iter.next();
			
			Object next = currentField.get(key);
			if(next instanceof Map){
			    ret.append("\n");
			    for(int i=0;i<indent;i++)
			        ret.append(" ");
			    ret.append(key+"="+convertMapToString((Map)next,indent+1,currentPdfFile));
			}else{
			    ret.append("\n");
			    for(int i=0;i<indent;i++)
			        ret.append(" ");
			    if(currentPdfFile!=null && !key.equals("Parent") && !key.equals("obj") && 
			    		((next instanceof String && ((String)next).indexOf("0 R")!=-1) || 
			    		(key.equals("T") || key.equals("V") || key.equals("DA") || key.equals("TU") || key.equals("CA"))))
			    	ret.append(key+"="+currentPdfFile.resolveToMapOrString(key,next));
			    else if(currentPdfFile!=null){
			    	if((next instanceof String && ((String)next).indexOf("0 R")!=0) || 
			    			(key.equals("T") || key.equals("V") || key.equals("DA") || key.equals("TU") || key.equals("CA") || key.equals("D"))){
			    		Object obj = currentPdfFile.resolveToMapOrString(key,next);
			    		if(obj instanceof Map){
			    			ret.append(key+"="+convertMapToString((Map)obj,indent+1,currentPdfFile));
			    		}else {
			    			ret.append(key+"="+obj);
			    		}
			    	}else {
			    		ret.append(key+"="+next);
			    	}
			    }else {
			    	ret.append(key+"="+next);
			    }
			}
		}
		return ret.toString();
	}
	
	/**
	 * converts the String array into a single String, seperated by ,commas
	 */
	final static public String convertArrayToString(String[] items){
		
		if(items!=null){
			
		StringBuffer ret = new StringBuffer();
		for(int i=0;i<items.length;i++){
			if(i>0)
				ret.append(", ");
			ret.append(items[i]);
		}
		return ret.toString();
		}else
			return null;
	}

	/**
	 * converts the boolean array into a single String, seperated by ,commas and numbered
	 */
	public static String convertArrayToString(boolean[] flags) {
		if(flags!=null){
			StringBuffer ret = new StringBuffer();
			for(int i=0; i<flags.length;i++){
				if(i>0)
					ret.append(", ");
				ret.append(i);
				ret.append("=");
				ret.append(flags[i]);
			}
			
			return ret.toString();
		}else{
			return null;
		}
	}

	/**
	 * @param fs
	 * @return
	 */
	public static String convertArrayToString(float[] values) {
		if(values!=null){
			StringBuffer ret = new StringBuffer();
			for(int i=0; i<values.length;i++){
				if(i>0)
					ret.append(", ");
				ret.append(values[i]);
			}
			
			return ret.toString();
		}else{
			return null;
		}
	}

	/**
	 * @param selectedItems
	 * @return
	 */
	public static String convertArrayToString(int[] items) {
		if(items!=null){
			StringBuffer ret = new StringBuffer();
			for(int i=0; i<items.length;i++){
				if(i>0)
					ret.append(", ");
				ret.append(items[i]);
			}
			
			return ret.toString();
		}else{
			return null;
		}
	}

	/**
	 * converts all rectangles into string form, comma seperated and a new line per rectangle
	 */
	public static String convertArrayToString(Rectangle[] boxes) {
		
		StringBuffer ret = new StringBuffer();
		for(int i=0;i<boxes.length;i++){
			if(i>0)
				ret.append(",\n ");
			ret.append(boxes[i].x);
			ret.append(" ");
			ret.append(boxes[i].y);
			ret.append(" ");
			ret.append(boxes[i].width);
			ret.append(" ");
			ret.append(boxes[i].height);
		}
		
		return ret.toString();
	}

    /**
     * @param components
     * @return
     */
    public static String convertArrayToString(Object[] components) {

        StringBuffer ret = new StringBuffer();
        ret.append("count=");
        ret.append(components.length);
        for(int i=0;i<components.length;i++){
            ret.append("\n");
            ret.append(components[i]);
        }
        return ret.toString();
    }

    /**
     * @param formData
     * @return
     */
    public static String convertArrayToString(List data) {
        if(data==null)
        	return "null";
        StringBuffer ret = new StringBuffer();
        ret.append("count=");
        ret.append(data.size());
        for(int i=0;i<data.size();i++){
            ret.append("\n");
            ret.append(data.get(i));
        }
        return ret.toString();
    }

    /**
     * @param booleans
     * @return
     */
    public static String convertArrayToString(Boolean[] flags) {
        if(flags!=null){
			StringBuffer ret = new StringBuffer();
			for(int i=0; i<flags.length;i++){
				if(i>0)
					ret.append(", ");
				ret.append(i);
				ret.append("=");
				ret.append(flags[i]);
			}
			
			return ret.toString();
		}else{
			return null;
		}
    }

    public static String convertButtonGroupToString(ButtonGroup bg) {
        StringBuffer buf = new StringBuffer();
        Enumeration list = bg.getElements();
        while(list.hasMoreElements()){
            buf.append(list.nextElement());
            buf.append("\n");
        }
        
        return buf.toString();
    }
    
    public static void printStackTrace(int level) {
    	//<start-13>
		Throwable stackgetter = new Throwable();
		StackTraceElement[] elems = stackgetter.getStackTrace();
		if(level==-1)
			level = elems.length-1;
		
		for(int i=1;i<=level;i++){//start at 1 to ignore ConvertToString.printStackTrace line
			System.out.println(elems[i]);
		}
		//<end-13>
	}

    public static String convertDocumentToString(Node formData){
    	return convertDocumentToString(formData,0);
    }

    private static String convertDocumentToString(Node formData,int level) {
    	if(formData==null)
    		return null;
    	
    	StringBuffer buf = new StringBuffer();
    	
    	Node nextNode = formData;
    	buf.append(nextNode.getNodeName());
    	buf.append(" = ");
    	buf.append(nextNode.getNodeValue());
    	buf.append(" type=");
    	buf.append(nextNode.getNodeType());
    	NamedNodeMap att = nextNode.getAttributes();
    	if(att!=null){
    		buf.append(" attributes=");
    		buf.append(att.toString());
    	}
    	buf.append("\n");
    	for(int i=0;i<level;i++){
    		buf.append(".");
    	}
    	
    	NodeList nodes = nextNode.getChildNodes();
    	for(int i=0;i<nodes.getLength();i++){
    		buf.append(convertDocumentToString(nodes.item(i),++level));
    	}
    	
    	return buf.toString();
    }

	/** depth starts at 0 */
	public static String convertArrayToString(Node formData,int depth) {
		StringBuffer buff = new StringBuffer();
		buff.append("nodename=");
		buff.append(formData.getNodeName());
		
		buff.append(" nodetype=");
		buff.append(formData.getNodeType());
		
		buff.append(" nodevalue=");
		buff.append(formData.getNodeValue());
		
		buff.append(" parent=");
		buff.append(formData.getParentNode());
		
		buff.append(" Children - \n");
		depth++;
		for(int i=0;i<depth;i++){
			buff.append(" ");
}
		NodeList nodes = formData.getChildNodes();
		for(int i=0;i<nodes.getLength();i++){
			buff.append(convertArrayToString(nodes.item(i),depth));
		}
		return buff.toString();
	}

}
