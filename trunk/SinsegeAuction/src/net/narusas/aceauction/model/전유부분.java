/*
 * 
 */
package net.narusas.aceauction.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class �����κ�.
 */
public class �����κ� {
	
	/** The text. */
	String text;

	/**
	 * Instantiates a new �����κ�.
	 * 
	 * @param text the text
	 */
	public �����κ�(String text) {
		this.text = text;
	}

	/**
	 * Gets the ����.
	 * 
	 * @return the ����
	 */
	public String get����() {
		Pattern p = Pattern.compile("    ��      �� : ([^\\d^\n]*)\\s*[\\d.\\s*��]*", Pattern.MULTILINE);
		Matcher m = p.matcher(text);
		if (m.find()) {
			return m.group(1).trim();
		}
		return "";
	}

	/**
	 * Gets the ����.
	 * 
	 * @return the ����
	 */
	public String get����() {
		Pattern p = Pattern.compile("    ��      �� : [^\\d]*([\\d.]+\\s*��)", Pattern.MULTILINE);
		Matcher m = p.matcher(text);
		if (m.find()) {
			return m.group(1);
		}
		p = Pattern.compile("    ��      �� : [^\\d]*([\\d.]+\\s*��)", Pattern.MULTILINE);
		m = p.matcher(text);
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}
}
