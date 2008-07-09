/*
 * 
 */
package net.narusas.aceauction.util;

import java.text.SimpleDateFormat;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class DateUtil.
 */
public class DateUtil {
	
	/** The format. */
	static SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");

	/**
	 * Date string.
	 * 
	 * @param date the date
	 * 
	 * @return the string
	 */
	public static String dateString(java.util.Date date) {
		return format.format(date);
	}
}
