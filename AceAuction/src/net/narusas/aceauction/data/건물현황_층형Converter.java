package net.narusas.aceauction.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class �ǹ���Ȳ_����Converter {
	static Pattern p = Pattern.compile("(\\d+\\s*��)");
	static Pattern p2 = Pattern.compile("\\s+([^\\s]+��)");

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
