/*
 * 
 */
package net.narusas.aceauction.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class �ǹ���Ȳ_����Converter.
 */
public class �ǹ���Ȳ_����Converter {
	
	/** The p. */
	static Pattern p = Pattern.compile("(\\d+\\s*��)");
	
	/** The p2. */
	static Pattern p2 = Pattern.compile("\\s+([^\\s]+��)");

	/**
	 * Convert.
	 * 
	 * @param floor the floor
	 * 
	 * @return the string
	 */
	public static String convert(String floor) {
		if (floor == null) {
			return "";
		}
		floor = floor.replaceAll("����", "");
		floor = floor.replaceAll("����", "������");
		Matcher m = p2.matcher(floor);
		if (m.find()) {
			return floor.substring(m.start()).trim();
		}

		return floor.trim();
	}

}
