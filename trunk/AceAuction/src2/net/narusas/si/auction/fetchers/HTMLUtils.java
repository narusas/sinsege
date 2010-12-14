package net.narusas.si.auction.fetchers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLUtils {

	public static String findFrom(String from, String regx, String src) {
		String splited = src.substring(src.indexOf(from));

		return find(regx, splited);
	}

	public static List<String> find(String regx, String src, int spotCount) {
		List<String> res = new ArrayList<String>();
		Matcher m = java.util.regex.Pattern.compile(regx).matcher(src);

		if (m.find() == false) {
			return res;
		}

		for (int i = 0; i < spotCount; i++) {
			res.add(m.group(i + 1));
		}
		return res;
	}

	public static String find(String regx, String src) {
		List<String> res = find(regx, src, 1);
		if (res.size() == 0) {
			return null;
		}
		return res.get(0);
	}

	public static String findPropertyValue(String key, String src) {
		return find(key + "\"\\s+value=\"([^\"]+)", src);
	}

	public static java.sql.Date toDate(String src) {
		if (src == null || "".equals(src.trim())) {
			return null;
		}
		List<String> tokens = find("(\\d+)\\.(\\d+)\\.(\\d+)", src, 3);
		return new java.sql.Date(Integer.parseInt(tokens.get(0)) - 1900, Integer.parseInt(tokens.get(1)) - 1,
				Integer.parseInt(tokens.get(2)));
	}

	public static String findTHAndNextValue(String html, String th) {
		Pattern thPattern = Pattern.compile("<th[^>]*>\\s*" + th + "</th[^>]*>\\s*<td[^>]*>([^<]*)</td",
				Pattern.MULTILINE);
		Matcher m = thPattern.matcher(html);
		if (m.find()) {
			return m.group(1).trim();
		}
		return null;
	}

	public static List<String> findAnchors(String chunk) {
		Pattern anchorP = Pattern.compile("(<a[^>]*>[^<]*</a[^>]*>)");
		Matcher m = anchorP.matcher(chunk);
		List<String> res = new LinkedList<String>();
		while (m.find()) {
			res.add(m.group(1));
		}
		return res;
	}

	public static String findTHAndNextValueAsComplex(String html, String th) {
		Pattern thPattern = Pattern.compile("<th[^>]*>\\s*" + th + "\\s*</th[^>]*>\\s*<td[^>]*>",
				Pattern.MULTILINE);
		Matcher m = thPattern.matcher(html);
		if (m.find()) {
			int end = m.end();
			return html.substring(end, html.indexOf("</td", end));
		}
		return null;
	}

	public static String strip(String src) {
		if (src == null) {
			return null;
		}
		String temp = src.replaceAll("(<[^>]*>)", "");
		temp = converHTMLSpecialChars(temp);
		temp = stripWhitespaces(temp);

		return temp.trim();
	}

	public static String stripWhitespaces(String temp) {
		temp = temp.replaceAll("\t", " ");
		temp = temp.replaceAll("     ", " ");
		temp = temp.replaceAll("    ", " ");
		temp = temp.replaceAll("   ", " ");
		temp = temp.replaceAll("  ", " ");
		temp = temp.replaceAll("     ", " ");
		temp = temp.replaceAll("    ", " ");
		temp = temp.replaceAll("   ", " ");
		temp = temp.replaceAll("  ", " ");
		return temp;
	}

	public static String stripCRLF(String src) {
		if (src == null) {
			return null;
		}
		String temp = src.replaceAll("\\n", "");
		return temp.replaceAll("\\r", "");
	}

	public static String converHTMLSpecialChars(String temp) {
		temp = temp.replaceAll("&nbsp;", " ");
		temp = temp.replaceAll("&lt;", "<");
		temp = temp.replaceAll("&gt;", ">");
		temp = temp.replaceAll("&amp;", "&");

		return temp;
	}

	public static String encodeUrl(String txt, String enc) {
		try {
			return URLEncoder.encode(txt, enc);
		} catch (UnsupportedEncodingException e) {
			return URLEncoder.encode(txt);
		}
	}

	public static String encodeUrl(String txt) {
		return encodeUrl(txt, "euc-kr");
	}
}
