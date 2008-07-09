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
* ---------------
* ShowIfFileUsesEmbeddedFonts.java
* ---------------
* (C) Copyright 2005, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*
*/
package org.jpedal.examples;
import java.io.File;

import org.jpedal.PdfDecoder;

/**
 * This example opens a pdf file see if fonts are embedded
 * contains <br>Scope:<b>(Ent/Viewer only)</b>
 * 
 */
public class ShowIfEmbeddedFontsUsed
{
	
	/**user dir in which program can write*/
	private String user_dir = System.getProperty( "user.dir" );
	
	/**sample file which can be setup - substitute your own. */
	private static String test_file = "/mnt/win_d/sample.pdf";

	//not to be used
	private ShowIfEmbeddedFontsUsed() {}
	
	//////////////////////////////////////////////////////////////////////////
	/**example method to open a file and return the number of pages*/
	public ShowIfEmbeddedFontsUsed( String file_name ) 
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
			
			/**see if file contains embedded fonts*/
			System.out.println( "File contains embedded fonts=" + decode_pdf.PDFContainsEmbeddedFonts() );

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
		System.out.println( "Simple demo to see if file contains embedded fonts" );
		
		//set to default
		String file_name = test_file;
		
		//check user has passed us a filename and use default if none
		if( args.length != 1 )
			System.out.println( "Please pass the file name and any path (ie \"C:/sample.pdf\" ) as a command line value - use double quotes if it includes spaces" );
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
		ShowIfEmbeddedFontsUsed pageCount1 = new ShowIfEmbeddedFontsUsed( file_name );
	}
}
