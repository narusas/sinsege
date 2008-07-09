/*
* ===========================================
* Java Pdf Extraction Decoding Access Library
* ===========================================
*
* Project Info:  http://www.jpedal.org
* Project Lead:  Mark Stephens (mark@idrsolutions.com)
*
* (C) Copyright 2002, IDRsolutions and Contributors.
*
* ---------------
* Output.java
* ---------------
* (C) Copyright 2002, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*/
package org.jpedal.examples.text.extractheadlines;

import java.io.FileWriter;
import java.io.PrintWriter;

public class Output {


	/**output file*/
	private String targetFile="";
	
	/**called to setup output*/
	public void open(String targetFile) {
		this.targetFile=targetFile;
	}

	/**called to write to file*/
	public void outputSection(String section, String refPage,String file) {
		
		//write message
		PrintWriter log_file = null;
		try
		{
			log_file = new PrintWriter( new FileWriter( targetFile, true ) );
			log_file.println(refPage+" "+section+" "+file);
			log_file.flush();
			log_file.close();
		}
		catch( Exception e )
		{
			System.err.println( "Exception " + e + " attempting to write to file " + targetFile );
		}
	}

	/**called at end of output to flush/close/release*/
	public void close() {
		
	}

}
