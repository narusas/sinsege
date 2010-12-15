package net.narusas.aceauction.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 건물현황_용도Converter {
	private static Properties props;
	static Pattern p1 = Pattern.compile("(.*)등$");

	static {
		props = new Properties();
		try {
			props.load(new FileReader("건물현황_용도.cfg"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String convert(String str) {
		Matcher m = p1.matcher(str);
		if (m.find()) {
			str = m.group(1);
		}
		if (props.containsKey(str)) {
			return props.getProperty(str);
		}
		String[] temp = str.split(",");
		if (temp.length != 1) {
			return temp[0];
		}
		temp = str.split("및");
		if (temp.length != 1) {
			return temp[0];
		}
		if (str.contains("부속건물")) {
			return "부속건물";
		}

		String res = str.replaceAll("일반", "");
		res = res.replaceAll("대중", "");
		res = res.replaceAll("관련", "");
		res = res.replaceAll("생활", "");

		while (res.contains("(")) {
			int start = res.indexOf("(");
			int end = res.indexOf(")");
			res = res.substring(0, start) + res.substring(end + 1);
		}
		if (res.endsWith("공장")) {
			return "공장";
		}
		if (res.endsWith("주택")) {
			return "주택";
		}
		return res;
	}

}
