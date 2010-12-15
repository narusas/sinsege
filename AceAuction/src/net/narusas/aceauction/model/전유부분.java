package net.narusas.aceauction.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 전유부분 {
	String text;

	public 전유부분(String text) {
		this.text = text;
	}

	public String get구조() {
		Pattern p = Pattern.compile("    구      조 : ([^\\d^\n]*)\\s*[\\d.\\s*㎡]*", Pattern.MULTILINE);
		Matcher m = p.matcher(text);
		if (m.find()) {
			return m.group(1).trim();
		}
		return "";
	}

	public String get면적() {
		Pattern p = Pattern.compile("    면      적 : [^\\d]*([\\d.]+\\s*㎡)", Pattern.MULTILINE);
		Matcher m = p.matcher(text);
		if (m.find()) {
			return m.group(1);
		}
		p = Pattern.compile("    구      조 : [^\\d]*([\\d.]+\\s*㎡)", Pattern.MULTILINE);
		m = p.matcher(text);
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}
}
