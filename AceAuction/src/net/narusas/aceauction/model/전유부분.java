package net.narusas.aceauction.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class �����κ� {
	String text;

	public �����κ�(String text) {
		this.text = text;
	}

	public String get����() {
		Pattern p = Pattern.compile("    ��      �� : ([^\\d^\n]*)\\s*[\\d.\\s*��]*", Pattern.MULTILINE);
		Matcher m = p.matcher(text);
		if (m.find()) {
			return m.group(1).trim();
		}
		return "";
	}

	public String get����() {
		Pattern p = Pattern.compile("    ��      �� : [^\\d]*([\\d.]+\\s*��)", Pattern.MULTILINE);
		Matcher m = p.matcher(text);
		if (m.find()) {
			return m.group(1);
		}
		p = Pattern.compile("    ��      �� : [^\\d]*([\\d.]+\\s*��)", Pattern.MULTILINE);
		m = p.matcher(text);
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}
}
