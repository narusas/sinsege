/*
 * ===========================================
 * Java Pdf Extraction Decoding Access Library
 * ===========================================
 *
 * Project Info:  http://www.jpedal.org
 * Project Lead:  Mark Stephens (mark@idrsolutions.com)
 *
 * ---------------
 * ExtractFormData.java
 * ---------------
 * (C) Copyright 2002, by IDRsolutions and Contributors.
 *
 */
package org.jpedal.examples.acroform;

//JFC

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jpedal.PdfDecoder;
import org.jpedal.objects.PdfFormData;
import org.jpedal.utils.LogWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * <p>Sample code showing how jpedal library can be used to extract form data
 *  Scope:<b>(Ent/Viewer only)</b>
 */
public class ExtractFormData {
	
	/**output where we put files*/
	private String outputDir = System.getProperty("user.dir");
	
	/**number of files read*/
	private int fileCount=0;
	
	/**flag to show if we print messages*/
	static boolean outputMessages=false;
	
	public int getFileCount(){
		return fileCount;
	}
	
	/**correct separator for OS */
	String separator = System.getProperty("file.separator");
	
	/**the decoder object which decodes the pdf and returns a data object*/
	PdfDecoder decodePdf = null;
	
	/**tell software to save to file*/
	private boolean saveToFile=true;
	
	private Document doc;
	
	/**sample file which can be setup - substitute your own. 
	 * If a directory is given, all the files in the directory will be processed*/
	//private static String test_file = "/mnt/shared/storypad/input/acadapp.pdf";
	private static String test_file = "/home/markee/acroSample.pdf";
	
	public ExtractFormData() {
	}
	
	/**example method to open a file and extract the form data*/
	public ExtractFormData(String file_name) {
		
		//check output dir has separator
		if (outputDir.endsWith(separator) == false)
			outputDir = outputDir + separator+"forms"+separator;
		
		//create a directory if it doesn't exist
		File output_path = new File(outputDir);
		if (output_path.exists() == false){
			output_path.mkdirs();
		}
		
		/**
		 * if file name ends pdf, do the file otherwise 
		 * do every pdf file in the directory. We already know file or
		 * directory exists so no need to check that, but we do need to
		 * check its a directory
		 */
		if (file_name.toLowerCase().endsWith(".pdf")) {
			decodePage("",file_name);
		} else {
			
			/**
			 * get list of files and check directory
			 */
			String[] files = null;
			File inputFiles = null;
			
			/**make sure name ends with a deliminator for correct path later*/
			if (!file_name.endsWith(separator))
				file_name = file_name + separator;
			
			try {
				inputFiles = new File(file_name);
				if (!inputFiles.isDirectory()) {
					System.err.println(
							file_name + " is not a directory. Exiting program");
				}
				files = inputFiles.list();
			} catch (Exception ee) {
				LogWriter.writeLog(
						"Exception trying to access file " + ee.getMessage());
			}
			
			/**now work through all pdf files*/
			long fileCount = files.length;
			
			for (int i = 0; i < fileCount; i++) {
				if (files[i].toLowerCase().endsWith(".pdf")) {
					
					if(outputMessages)
						System.out.println(">>_"+file_name + files[i]);
					
					decodePage(file_name, files[i]);
				}
			}
		}
	}
	
	/**
	 * routine to decode a page
	 */
	private void decodePage(String dir,String name) {
		
		String file_name=dir+name;
		
		//PdfDecoder returns a PdfException if there is a problem
		try {
			decodePdf = new PdfDecoder(false);
			
			/**
			 * open the file (and read metadata including form in  file)
			 * NO OTHER ACTIVITY REQUIRED TO GET FORM DATA!!
			 */
			if(outputMessages)
				System.out.println("Opening file :" + file_name);
			decodePdf.openPdfFile(file_name);
			
		} catch (Exception e) {
			e.printStackTrace();
			
			System.err.println("Exception " + e + " in pdf code with "+file_name);
		}
		
		/**
		 * extract data from pdf (if allowed). 
		 */
		if ((decodePdf.isEncrypted())&&(!decodePdf.isExtractionAllowed())) {
			if(outputMessages){
				System.out.println("Encrypted settings");
				System.out.println("Please look at SimpleViewer for code sample to handle such files");
			}
		}else{
			
			fileCount++;
			
			/**
			 * get extracted data from pdf
			 */
			//get the PdfImages object which now holds the content
			PdfFormData formData = decodePdf.getPdfFormData();
			
			if(outputMessages)
				System.out.println(formData);
			/**demo returns object  - in which every 4th letter is always turned into   '1' so Value will become 1al1e
			 */
			if (formData != null) {
				
				//extract the document
				List formDoc = formData.getFormData();
				
				/**We now have the data as an object. Each field can be accessed as a Map
				 * using Map value = (Map) formDoc.get(i);
				 * 
				 * Below we turn this into an XML object as an example of what you might do
				 */
				
				//generate a name and path for output
				String outputName=name;
				int pointer=outputName.lastIndexOf(".");
				if(pointer>0)
					outputName=outputName.substring(0,pointer);
				pointer=outputName.lastIndexOf("/");
				if(pointer>0)
					outputName=outputName.substring(pointer+1);	
				pointer=outputName.lastIndexOf("\\");
				if(pointer>0)
					outputName=outputName.substring(pointer+1);		
				String saveName = outputDir + outputName+".xml";
				
				//get number of form fields
				int itemCount = formDoc.size();
				
				DocumentBuilder db=null;
				try {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					db = dbf.newDocumentBuilder();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				}
				
				doc =  db.newDocument();
				
				//display/output client data
				
				/**display data*/
				
				Element root = doc.createElement("Root");
				
				//output("<ExtractedFormData>");
				for (int i = 0; i < itemCount; i++) {
					//and write out the data
					Map value = (Map) formDoc.get(i);
					Element e = doc.createElement("Object"+i);
					extractKeyValues(value,e);
					
					root.appendChild(e);
					
				}
				doc.appendChild(root);
				
				InputStream stylesheet = this.getClass().getResourceAsStream("/org/jpedal/examples/simpleviewer/res/xmlstyle.xslt");
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				
				try {
					Transformer transformer = transformerFactory.newTransformer(new StreamSource(stylesheet));
					if(outputMessages)
						transformer.transform(new DOMSource(doc), new StreamResult(System.out));
					
					
					if(saveToFile){
						StreamResult stream=new StreamResult(saveName);
						transformer.transform(new DOMSource(doc), stream);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		}
		
		/**close the pdf file*/
		decodePdf.closePdfFile();
		
	}
	
	/**
	 * print values and recurse down Maps
	 */
	private void extractKeyValues(Map value,Node root) {
		
		/**decompress any cached streams from disk*/
		if(value.containsKey("startStreamOnDisk")){
			int start=((Integer)value.get("startStreamOnDisk")).intValue();
			int end=((Integer)value.get("endStreamOnDisk")).intValue();
			byte[] stream=decodePdf.getIO().readStreamFromPDF(start,end);
			value.put("Stream", stream );
			value.remove("startStreamOnDisk");
			value.remove("endStreamOnDisk");
		}
		
		/**get the keys and work through*/
		Iterator keys = value.keySet().iterator();
		
		Map section;
		String currentKey = null;
		
		/**
		 * values not to output
		 */
		Map ignoredKeys=new HashMap();
		ignoredKeys.put("P","x");
		ignoredKeys.put("Parent","x");
		ignoredKeys.put("obj","x");
		
		/**show elements*/
		while (keys.hasNext()) {
			currentKey = (String) keys.next();
			
			//tags to ignore - remember they can be nested
			if(ignoredKeys.containsKey(currentKey))
				continue;
			
			Object nextValue=value.get(currentKey);
			
			//create Node for this value
			
			Element currentElement= doc.createElement(currentKey);
			
			//check for object ref and resolve
			if(nextValue.toString().endsWith(" R")){
				Object resolvedValue=this.decodePdf.resolveFormReference((String) nextValue);
				nextValue=resolvedValue;
			}
			
			if(nextValue instanceof byte[]){
				nextValue=decodePdf.getIO().getTextString((byte[])nextValue);
				currentElement.setAttribute("value",(String)nextValue);
				
			}else if(nextValue instanceof Map){
				
				section=(Map)nextValue;
				
				/**
				 * a kids table may contain Multiple values which need to be done in
				 * turn!
				 */
				if(currentKey.equals("Kids")){
					Iterator kidKeys=section.keySet().iterator();
					while(kidKeys.hasNext()){
						Map kidSection=new HashMap();
						Object nextKey=kidKeys.next();
						Object kidValue=section.get(nextKey);
						kidSection.put("Kid",kidValue);
						extractKeyValues(kidSection,currentElement);
					}
				}else{
					extractKeyValues(section,currentElement);
				}
				
			}else{ //its a string may need to allow for [ x 0 R y 0 R]
				String val=(String)nextValue;
				currentElement.setAttribute("value",val);
			}
			
			root.appendChild(currentElement);
			
		}			
	}
	
	//////////////////////////////////////////////////////////////////////////
	/**
	 * main routine which checks for any files passed and runs the demo
	 */
	public static void main(String[] args) {
		
		if(outputMessages)
			System.out.println("Simple demo to extract form data");
		
		//set to default
		String file_name = test_file;
		
		//check user has passed us a filename and use default if none
		if (args.length != 1){
			if(outputMessages)
				System.out.println("Default test file used");
		}else {
			file_name = args[0];
			if(outputMessages)
				System.out.println("File :" + file_name);
		}
		
		//check file exists
		File pdf_file = new File(file_name);
		
		//if file exists, open and get number of pages
		if (pdf_file.exists() == false) {
			if(outputMessages)
				System.out.println("File " + file_name + " not found");
		}
		ExtractFormData text1 = new ExtractFormData(file_name);
	}

	//return location of files
	public String getOutputDir() {
		return outputDir;
	}
}
