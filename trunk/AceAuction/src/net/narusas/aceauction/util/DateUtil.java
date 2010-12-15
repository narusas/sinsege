package net.narusas.aceauction.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	static SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");

	public static String dateString(java.util.Date date) {
		return format.format(date);
	}
}
