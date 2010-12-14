/**
 * ===========================================
 * Java Pdf Extraction Decoding Access Library
 * ===========================================
 *
 * Project Info:  http://www.jpedal.org
 * Project Lead:  Mark Stephens
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
  * Javascript.java
  * ---------------
  * (C) Copyright 2006, by IDRsolutions and Contributors.
  *

  * Original Author:  Mark Stephens (mark@idrsolutions.com)
  * Contributor(s):
 *
 */
package org.jpedal.objects;

/**
 * general container for javascript
 */
public class Javascript {

    static{
        
        // see if javascript present
        java.io.InputStream in = Javascript.class.getClassLoader().getResourceAsStream("org/mozilla/javascript/Context.class");
        if (in == null)
        throw new RuntimeException("JPedal Must have Rhino on classpath for Javascript");

    }
    
    private static boolean debugJavaScript = false;


    public boolean isJavaScriptEnabled() {
        return enableJavaScript;
    }

    private boolean enableJavaScript = false;

    private boolean hasJavascript;


    public boolean hasJavascript() {
        return hasJavascript;
    }

    public void readJavascript() {
        hasJavascript=true;
    }

    public void reset() {
        hasJavascript=false;
    }
    
    public static void execute(String command,String args){
    	if(debugJavaScript ){
    		System.out.println("execute "+command+"("+args+")");
    	}
    }

    
    /**
     * store and execute code from Names object
     */
    public void setCode(String nextKey, String value) {


    }

    
    public void closeFile(){

    }





}
