package net.narusas.si.auction.fetchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 전유부분Parser {
	public static 전유부분 parse(String src) {
		return new 전유부분(parse구조(src), parse면적(src));
	}

	public static String parse구조(String text) {
		String hungryLine = text.replaceAll(" ", "");
		Pattern p = Pattern.compile("구조:([^\\d^\n]*)\\s*[\\d.\\s*㎡]*", Pattern.MULTILINE);
		Matcher m = p.matcher(hungryLine);
		if (m.find()) {
			return m.group(1).trim();
		}
		return "";
	}

	public static String parse면적(String text) {
		String hungryLine = text.replaceAll(" ", "");
		Pattern p = Pattern.compile("면적:[^\\d]*([\\d.]+\\s*㎡)", Pattern.MULTILINE);
		Matcher m = p.matcher(hungryLine);
		if (m.find()) {
			return m.group(1);
		}
		p = Pattern.compile("구조:[^\\d]*([\\d.]+\\s*㎡)", Pattern.MULTILINE);
		m = p.matcher(hungryLine);
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}
}
