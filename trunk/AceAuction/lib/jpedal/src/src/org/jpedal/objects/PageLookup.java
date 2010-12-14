/*
* ===========================================
* Java Pdf Extraction Decoding Access Library
* ===========================================
*
* Project Info:  http://www.jpedal.org
* Project Lead:  Mark Stephens (mark@iFdrsolutions.com)
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
* PageLookup.java
* ---------------
* (C) Copyright 2002, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal.objects;

import java.util.Hashtable;
import java.util.Map;

/**
 * allow us to lookup pages
 */
public class PageLookup {
	
	/**holds pdf id (ie 4 0 R) which stores each object in reverse so we can lookup*/
	private Map pageLookup = new Hashtable();
	
	/**
	 * used to work out page id for the object (returns -1 if no value found)
	 */
	public int convertObjectToPageNumber(String offset){
		
		Object value=pageLookup.get(offset);
		
		if(value==null)
			return -1;
		else{
			return ((Integer)value).intValue();
		}
		
		
	}
	
	/**
	 * The pageLookup to set.
	 */
	public void put(String key,int value) {
		pageLookup.put(key,new Integer(value));
		
	}
}
