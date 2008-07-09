/*
* ===========================================
* Java Pdf Extraction Decoding Access Library
* ===========================================
*
* Project Info:  http://www.jpedal.org
* Project Lead:  Mark Stephens (mark@idrsolutions.com)
*
* (C) Copyright 2003, IDRsolutions and Contributors.
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
* PdfFileInformation.java
* ---------------
* (C) Copyright 2002, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*/
package org.jpedal.objects;

/**
 * <p>Added as a repository to store PDF file metadata (both legacy fields and XML metadata) in so that it can be accesed. 
 * <p>Please see org.jpedal.examples (especially SimpleViewer) for example code.
 */
public class PdfFileInformation
{
	/**list of pdf information fields data might contain*/
	private final static String[] information_fields =
		{
			"Title",
			"Author",
			"Subject",
			"Keywords",
			"Creator",
			"Producer",
			"CreationDate",
			"ModDate",
			"Trapped" };
	
	/**assigned values found in pdf information object*/
	private String[] information_values = {"","","","","","","","",""};	
	
	/**Any XML metadata as a string*/
	private String XMLmetadata="";

	private byte[] rawData=null;
			
	/**return list of field names*/
	public String[] getFieldNames(){
		return information_fields;
	}
	
	/**return XML data embedded inside PDF*/
	public String getFileXMLMetaData(){
		return XMLmetadata;
	}

	/**return XML data embedded inside PDF in its raw format*/
	public byte[] getFileXMLMetaDataArray(){
		return rawData;
	}
	
	/**set list of field names as file opened by JPedal (should not be used externally)*/
	public void setFileXMLMetaData(String value,byte[] rawData){
		XMLmetadata=value;
		this.rawData=rawData;
	}
	
	/**return list of field values to match field names (legacy non-XML information fields)*/
	public String[] getFieldValues(){
		return information_values;
	}
	
	/**set the information values as file opened by JPedal (should not be used externally)*/
	public void setFieldValue(int i,String convertedValue){
		information_values[i] =convertedValue;
	}

}
