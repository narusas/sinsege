/*
 * 
 */
package net.narusas.aceauction.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class ��Ȳ�ּ�Converter.
 */
public class ��Ȳ�ּ�Converter {
	
	/** The p1. */
	static Pattern p1 = Pattern.compile("��\\s*\\d+\\s*����");

	/**
	 * Convert.
	 * 
	 * @param address the address
	 * 
	 * @return the string
	 */
	public static String convert(String address) {
		Matcher m = p1.matcher(address);
		if (m.find()) {
			return address.substring(0, m.start()).trim();
		}
		return address;
	}
}
