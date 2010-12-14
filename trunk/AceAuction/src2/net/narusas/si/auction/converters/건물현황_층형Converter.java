/*
 * 
 */
package net.narusas.si.auction.converters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class 건물현황_층형Converter.
 */
public class 건물현황_층형Converter {
	
	/** The p. */
	static Pattern p = Pattern.compile("(\\d+\\s*층)");
	
	/** The p2. */
	static Pattern p2 = Pattern.compile("\\s+([^\\s]+층)");

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
		floor = floor.replaceAll("지상", "");
		floor = floor.replaceAll("지층", "지하층");
		Matcher m = p2.matcher(floor);
		if (m.find()) {
			return floor.substring(m.start()).trim();
		}

		return floor.trim();
	}

}
