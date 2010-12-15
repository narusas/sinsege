package net.narusas.aceauction.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class �ǹ���Ȳ_�뵵Converter {
	private static Properties props;
	static Pattern p1 = Pattern.compile("(.*)��$");

	static {
		props = new Properties();
		try {
			props.load(new FileReader("�ǹ���Ȳ_�뵵.cfg"));
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
		temp = str.split("��");
		if (temp.length != 1) {
			return temp[0];
		}
		if (str.contains("�μӰǹ�")) {
			return "�μӰǹ�";
		}

		String res = str.replaceAll("�Ϲ�", "");
		res = res.replaceAll("����", "");
		res = res.replaceAll("����", "");
		res = res.replaceAll("��Ȱ", "");

		while (res.contains("(")) {
			int start = res.indexOf("(");
			int end = res.indexOf(")");
			res = res.substring(0, start) + res.substring(end + 1);
		}
		if (res.endsWith("����")) {
			return "����";
		}
		if (res.endsWith("����")) {
			return "����";
		}
		return res;
	}

}
