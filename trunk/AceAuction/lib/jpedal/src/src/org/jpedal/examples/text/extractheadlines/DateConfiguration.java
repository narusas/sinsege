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
* ExtractTextAsWordlist.java
* ---------------
* (C) Copyright 2002, by IDRsolutions and Contributors.
*
* Original Author:  Mark Stephens (mark@idrsolutions.com)
* Contributor(s):
*/
package org.jpedal.examples.text.extractheadlines;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jpedal.storypad.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DateConfiguration extends Configuration {

	/**default value*/
	String[] sectionTokens={
	"font face=\"TimesClassicDisplay\" style=\"font-size:9pt\"",};
	
	private int[] x1={651};
	private int[] y1={963};
	private int[] x2={752};
	private int[] y2={949};
	
	private DateConfiguration(){}
	
	/**initialise category and load or create config file*/
	public DateConfiguration(String configDir){
	    
		sectionName = "dates";
		
	    /**loadConfig if present*/
	    boolean fileExists=loadValues(configDir);
	    
	    if(!fileExists){
	        saveValues();
	        loadValues(configDir);
	    }
	}
	
	/**
     * write out a config file
     */
    public boolean saveValues() {
        
        try{
            //create doc and set root
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			
            Node root=doc.createElement(sectionName);
           
            //add comments
            Node creation=doc.createComment("Created "+org.jpedal.utils.TimeNow.getShortTimeNow());
            
            doc.appendChild(creation);
            
            doc.appendChild(root);
            
            /**
             * add the XML tags we look for
             */
            int count=sectionTokens.length;
            //write out tag count
            Element tagCount=doc.createElement("xmlCount");
            tagCount.setAttribute("value",""+count);
            root.appendChild(tagCount);
            
            //write out values to XML
            String key="xmlTag";
            for(int i=0;i<count;i++){

                String currentKey=key+"_"+i;
                Element section=doc.createElement(currentKey);
                
                //add values
                section.setAttribute("value",""+sectionTokens[i]);
                root.appendChild(section);
          
            }
            
            /**
             * add the locations we scan
             */
            int locCount=this.x1.length;
            //write out tag count
            Element loc=doc.createElement("locationCount");
            loc.setAttribute("value",""+locCount);
            root.appendChild(loc);
            
            //write out values to XML
            key="locTag";
            String[] coords={"x1","y1","x2","y2"};
            
            for(int i=0;i<locCount;i++){

            	for(int coord=0;coord<4;coord++){
            		String currentKey=key+"_"+i+"_"+coords[coord];
            		Element section=doc.createElement(currentKey);
            		
            		//add values
            		switch(coord){
            		case 0:
            			section.setAttribute("value",""+x1[i]);
            			break;
            		case 1:
            			section.setAttribute("value",""+y1[i]);
            			break;
            		case 2:
            			section.setAttribute("value",""+x2[i]);
            			break;
            		case 3:
            			section.setAttribute("value",""+y2[i]);
            			break;
            		}
            		root.appendChild(section);
            	}
          
            }
            
            //write out
            //use System.out for FileOutputStream to see on screen
	        InputStream stylesheet = this.getClass().getResourceAsStream("/org/jpedal/examples/simpleviewer/res/xmlstyle.xslt");
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer(new StreamSource(stylesheet));
			transformer.transform(new DOMSource(doc), new StreamResult(configDir+sectionName+".xml"));
	                    
            System.out.println("Created "+configDir+sectionName+".xml");
            
        }catch(Exception e){
            e.printStackTrace();
        } 
        
        return true;
    }
    
	

}
