/**
 * ===========================================
 * Java Pdf Extraction Decoding Access Library
 * ===========================================
 *
 * Project Info:  http://www.jpedal.org
 * Project Lead:  Mark Stephens
 *
 * (C) Copyright 2006, IDRsolutions and Contributors.
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

  * StringUtils.java
  * ---------------
  * (C) Copyright 2006, by IDRsolutions and Contributors.
  *
  *
  * --------------------------
 */
package org.jpedal.utils;

public class StringUtils {

    /**
     * quick code to make text lower case
     */
    public static String toLowerCase(String str){

        int len=str.length();
        char c;
        char[] chars=str.toString().toCharArray();

        //strip out any odd codes
        StringBuffer changed=new StringBuffer(len);
        boolean isChanged=false;
        for(int jj=0;jj<len;jj++){
            c=chars[jj];

            //ensure lower case and flip if not
            if(c>64 && c<91){
                c=(char)(c+32);
                chars[jj]=c;
                isChanged=true;
            }
        }

        if(isChanged)
            return String.copyValueOf(chars,0,len);
        else
            return str;

    }
    
    public static String toUpperCase(String str){

        int len=str.length();
        char c;
        char[] chars=str.toString().toCharArray();

        //strip out any odd codes
        StringBuffer changed=new StringBuffer(len);
        boolean isChanged=false;
        for(int jj=0;jj<len;jj++){
            c=chars[jj];

            //ensure UPPER case and flip if not
            if(c>96 && c<123){
                c=(char)(c-32);
                chars[jj]=c;
                isChanged=true;
            }
        }

        if(isChanged)
            return String.copyValueOf(chars,0,len);
        else
            return str;

    }
    
    static final public String handleEscapeChars(String value) {
		//deal with escape characters
		int escapeChar=value.indexOf("\\");

		while(escapeChar!=-1){
		    char c=value.charAt(escapeChar+1);
		    if(c=='n'){
		        c='\n';
		    }else{
		    }


		    value=value.substring(0,escapeChar)+c+value.substring(escapeChar+2,value.length());

		    escapeChar=value.indexOf("\\");
		}
		return value;
	}
}
