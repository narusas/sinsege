/*
 * 
 */
package net.narusas.si.auction.converters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class 현황주소Converter.
 */
public class 현황주소Converter {
	
	/** The p1. */
	static Pattern p1 = Pattern.compile("외\\s*\\d+\\s*필지");

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
