/*
 * 
 */
package net.narusas.aceauction.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class °Ç¹°ÇöÈ²_ÃşÇüConverter.
 */
public class °Ç¹°ÇöÈ²_ÃşÇüConverter {
	
	/** The p. */
	static Pattern p = Pattern.compile("(\\d+\\s*Ãş)");
	
	/** The p2. */
	static Pattern p2 = Pattern.compile("\\s+([^\\s]+Ãş)");

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
		floor = floor.replaceAll("Áö»ó", "");
		floor = floor.replaceAll("ÁöÃş", "ÁöÇÏÃş");
		Matcher m = p2.matcher(floor);
		if (m.find()) {
			return floor.substring(m.start()).trim();
		}

		return floor.trim();
	}

}
