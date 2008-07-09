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
* PdfException.java
* ---------------
* (C) Copyright 2002, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
* $Id: PdfException.java,v 1.1 2005/04/05 15:38:35 chris Exp $
*
* Changes (since 01-Jun-2002)
* --------------------------
* 20-06-2002 (ms) Added to project
*/
package org.jpedal.exception;

/**
 * a generic exception which code can throw if there is an error
 */
public class PdfException extends Exception
{
	
	/**feedback on exception*/
	protected String error_message = "";
	
	/**
	 * display the error message
	 */
	public String getMessage()
	{
		return error_message;
	}
	
	public PdfException(){}
	
	/**set message at exception*/
	public PdfException( String message ) 
	{
		error_message = message;
	}
}
