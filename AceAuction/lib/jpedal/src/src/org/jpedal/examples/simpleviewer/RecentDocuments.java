package org.jpedal.examples.simpleviewer;

import java.util.Stack;
import java.util.StringTokenizer;

import org.jpedal.examples.simpleviewer.utils.PropertiesFile;


public class RecentDocuments {
	
	int noOfRecentDocs;
	PropertiesFile properties;
	
	private Stack previousFiles = new Stack();
	private Stack nextFiles = new Stack();

	public RecentDocuments(int noOfRecentDocs, PropertiesFile properties) {
		
		this.noOfRecentDocs=noOfRecentDocs;
		this.properties=properties;
		
	}
	
	String getShortenedFileName(String fileNameToAdd) {
		final int maxChars = 30;
		
		if (fileNameToAdd.length() <= maxChars)
			return fileNameToAdd;
		
		StringTokenizer st = new StringTokenizer(fileNameToAdd,"\\/");
		
		int noOfTokens = st.countTokens();
		String[] arrayedFile = new String[noOfTokens];
		for (int i = 0; i < noOfTokens; i++)
			arrayedFile[i] = st.nextToken();
		
		String filePathBody = fileNameToAdd.substring(arrayedFile[0].length(),
				fileNameToAdd.length() - arrayedFile[noOfTokens - 1].length());
		
		StringBuffer sb = new StringBuffer(filePathBody);
		
		for (int i = noOfTokens - 2; i > 0; i--) {
			
			//<start-13>
			int start = sb.lastIndexOf(arrayedFile[i]);
			//<end-13>
			
			//<start-13>
			/**
			//<end-13>
			int start = sb.toString().lastIndexOf(arrayedFile[i]);
			/**/
			
			int end = start + arrayedFile[i].length();
			sb.replace(start, end, "...");

			if (sb.toString().length() <= maxChars)
				break;
		}
		
		return arrayedFile[0] + sb.toString() + arrayedFile[noOfTokens - 1];
	}

	public String getPreviousDocument() {
		
		String fileToOpen =null;
		
		if(previousFiles.size() > 1){
			nextFiles.push(previousFiles.pop());
			fileToOpen = (String)previousFiles.pop();	
		}
		
		return fileToOpen;
	}
	
	public String getNextDocument() {
		
		String fileToOpen =null;
		
		if(nextFiles.size() > 0)
			fileToOpen = (String)nextFiles.pop();
		
		return fileToOpen;
	}

	public void addToFileList(String selectedFile) {
		previousFiles.push(selectedFile);
		
		
	}

	
}
