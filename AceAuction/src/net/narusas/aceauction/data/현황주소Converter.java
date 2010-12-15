package net.narusas.aceauction.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 현황주소Converter {
	static Pattern p1 = Pattern.compile("외\\s*\\d+\\s*필지");

	public static String convert(String address) {
		Matcher m = p1.matcher(address);
		if (m.find()) {
			return address.substring(0, m.start()).trim();
		}
		return address;
	}
}
