/*
* ===========================================
* Java Pdf Extraction Decoding Access Library
* ===========================================
*
* Project Info:  http://www.jpedal.org
* Project Lead:  Mark Stephens (mark@idrsolutions.com)
*
* (C) Copyright 2004, IDRsolutions and Contributors.
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
* Name.java
* ---------------
* (C) Copyright 2007, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal.fonts.tt;

import java.util.Map;
import java.util.HashMap;

/**
 * font strings partially implemented - full spec is at http://www.microsoft.com/OpenType/OTSpec/name.htm
 */
public class Name extends Table {
	
	/**max number of glyphs*/
	private int encoding=0;

    private String postscriptName=null;
    
    private Map strings=new HashMap();

    public static final Integer COPYRIGHT_NOTICE=new Integer(0);
    public static final Integer FONT_FAMILY_NAME=new Integer(1);
    public static final Integer FONT_SUBFAMILY_NAME=new Integer(2);
    public static final Integer UNIQUE_FONT_IDENTIFIER=new Integer(3);
    public static final Integer FULL_FONT_NAME=new Integer(4);
    public static final Integer VERSION_STRING=new Integer(5);
    public static final Integer POSTSCRIPT_NAME=new Integer(6);

    public Name(FontFile2 currentFontFile){
	
		//LogWriter.writeMethod("{readMapxTable}", 0);
		
		//move to start and check exists
		int startPointer=currentFontFile.selectTable(FontFile2.NAME);

        //read 'head' table
		if(startPointer!=0){

            //read global details
            int format=currentFontFile.getNextUint16();
            int count=currentFontFile.getNextUint16();
            int offset=currentFontFile.getNextUint16();

            /**
             * read strings
             */
            for(int i=0;i<count;i++){

                //get table values
                int platformID=currentFontFile.getNextUint16();
                int platformSpecificID=currentFontFile.getNextUint16();
                int langID=currentFontFile.getNextUint16();
                int nameID=currentFontFile.getNextUint16();
                int length=currentFontFile.getNextUint16();
                int offset2=currentFontFile.getNextUint16();

                //only these 2 variations at present
                if((platformID==1 && platformSpecificID==0 && langID==0)||
                        (platformID==3 && platformSpecificID==0 && langID==1033) ||
                        (platformID==3 && platformSpecificID==1 && langID==1033)){

                    //read actual string for location, altering/restoring pointers
                    int oldP=currentFontFile.getPointer();

                    currentFontFile.setPointer(startPointer+offset+offset2);

                    int nextChar;

                    //allow for 2 bytes in char
                    if(platformID==0 || platformID==3)
                        length=length/2;

                    StringBuffer s=new StringBuffer();
                    s.setLength(length);

                    for(int ii=0;ii<length;ii++){
                        if(platformID==0 || platformID==3)
                            nextChar=currentFontFile.getNextUint16();
                        else
                            nextChar=currentFontFile.getNextUint8();

                        s.setCharAt(ii,(char)nextChar);
                    }

                    String str=s.toString();

                    if(str!=null)
                        strings.put(new Integer(nameID),str);
                    
                    currentFontFile.setPointer(oldP);
                }
            }
        }
	}
	
	public int getEncoding(){
		return encoding;
	}

    /**
     * return a String value in font - keys also defined as Integers in NAME class
     *
     * not all encodings or ID 20 handled at present
     */
    public String getString(Integer id) {
        return (String) strings.get(id);
    }
}
