package net.narusas.aceauction.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ��Ȳ�ּ�Converter {
	static Pattern p1 = Pattern.compile("��\\s*\\d+\\s*����");

	public static String convert(String address) {
		Matcher m = p1.matcher(address);
		if (m.find()) {
			return address.substring(0, m.start()).trim();
		}
		return address;
	}
}
