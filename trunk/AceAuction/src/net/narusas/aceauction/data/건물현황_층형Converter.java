package net.narusas.aceauction.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 勒僭⑷�淪類⑽Converter {
	static Pattern p = Pattern.compile("(\\d+\\s*類)");
	static Pattern p2 = Pattern.compile("\\s+([^\\s]+類)");

	public static String convert(String floor) {
		if (floor == null) {
			return "";
		}
		floor = floor.replaceAll("雖鼻", "");
		floor = floor.replaceAll("雖類", "雖ж類");
		Matcher m = p2.matcher(floor);
		if (m.find()) {
			return floor.substring(m.start()).trim();
		}

		return floor.trim();
	}

}
