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
* ---------------
* PageCount.java
* ---------------
* (C) Copyright 2002, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal.examples;
import java.io.File;

import org.jpedal.PdfDecoder;

/**
 * This example opens a pdf file and gets the number of pages it
 * contains<br> Scope:<b>(Ent only)</b>
 * 
 */
public class PageCount
{
	
	/**user dir in which program can write*/
	private String user_dir = System.getProperty( "user.dir" );
	
	/**sample file which can be setup - substitute your own. */
	private static String test_file = "/mnt/win_d/sample.pdf";

	public PageCount() 
	{
		
	}
	
	//////////////////////////////////////////////////////////////////////////
	/**example method to open a file and return the number of pages*/
	public PageCount( String file_name ) 
	{
		String separator = System.getProperty( "file.separator" );
		
		//check output dir has separator
		if( user_dir.endsWith( separator ) == false )
			user_dir = user_dir + separator;
		
		/**
		 * set up PdfDecoder object telling
		 * it whether to display messages
		 * and where to find its lookup tables
		 */
		PdfDecoder decode_pdf = null;
		
		//PdfDecoder returns a PdfException if there is a problem
		try
		{
			decode_pdf = new PdfDecoder( false ); //false as no GUI display needed
			
			/**
			 * open the file (and read metadata including pages in  file)
			 */
			System.out.println( "Opening file :" + file_name );
			decode_pdf.openPdfFile( file_name );
			
			/**get page number*/
			System.out.println( "Page count=" + decode_pdf.getPageCount() );

			/**close the pdf file*/
			decode_pdf.closePdfFile();
			
		}
		catch( Exception e )
		{
			System.err.println( "Exception " + e + " in pdf code" );
			
		}
		
		
	}
	//////////////////////////////////////////////////////////////////////////
	/**
	 * main routine which checks for any files passed and runs the demo
	 */
	public static void main( String[] args )
	{
		System.out.println( "Simple demo to extract number of pages" );
		
		//set to default
		String file_name = test_file;
		
		//check user has passed us a filename and use default if none
		if( args.length != 1 )
			System.out.println( "Default test file used" );
		else
		{
			file_name = args[0];
			System.out.println( "File :" + file_name );
		}
		
		//check file exists
		File pdf_file = new File( file_name );
		
		//if file exists, open and get number of pages
		if( pdf_file.exists() == false )
		{
			System.out.println( "File " + file_name + " not found" );
		}
		PageCount pageCount1 = new PageCount( file_name );
	}
}
