/*
 * 
 */
package net.narusas.si.auction.converters;

import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class 금액Converter.
 */
public class 금액Converter {
	
	static String[] patterns = new String[]{"청\\s*구\\s*금\\s*액", "채\\s*권\\s*최\\s*고\\s*액", "전\\s*세\\s*금", "임\\s*차\\s*보\\s*증\\s*금"};
	public static Pattern[] 금액종류 =new Pattern[] {// 
			Pattern.compile("청\\s*구\\s*금\\s*액"),//
					Pattern.compile("채\\s*권\\s*최\\s*고\\s*액"),//
					Pattern.compile("전\\s*세\\s*금"),//
					Pattern.compile("임\\s*차\\s*보\\s*증\\s*금"),//
			};

			public static boolean contain금액(String str) {
				if (str == null) {
					return false;
				}
				for (Pattern type : 금액종류) {
					if (type.matcher(str).find()) {
						return true;
					}
				}
				return false;
			}

	/**
	 * Convert.
	 * 
	 * @param src
	 *            the src
	 * 
	 * @return the string
	 */
	public static String convert(String src) {
		if (src == null || "".equals(src.trim())) {
			return null;
		}
		
		String currency = "";
		
		if (src.indexOf('원')!=-1){
			src = src.substring(0,src.indexOf('원'));	
		}
		
		if (src.indexOf('엔')!=-1){
			src = src.substring(0,src.indexOf('엔'));
			currency = "엔";
		}
		for (String pattern : patterns) {
			src = src.replaceAll(pattern, "");
		}
		if (src.contains("미화") || src.contains("불")){
			src = src.replaceAll("미화", "");
			src = src.substring(0,src.indexOf('불'));
			currency = "불";
		}
		
		src = src.trim();
		if (src.endsWith("원")) {
			src = src.substring(0, src.length() - 1);
		}
		if (src.endsWith("원정")) {
			src = src.substring(0, src.length() - 2);
		}
		if (src.startsWith("월")) {
			src = src.substring(1).trim();
		}
		if (src.startsWith("금")) {
			src = src.substring(1);
		}
		if (src.startsWith("금")) {
			src = src.substring(1);
		}
		src = src.replaceAll(",", "");
		src = src.replaceAll("\\.", "");
		src = src.replaceAll(" ", "");
		if (is문자금액(src)) {
			String 조 = "", 억 = "", 만 = "", 나머지 = "";

			String[] temp = src.split("조");
			if (temp.length == 2) {
				조 = temp[0];
				src = temp[1];
			} else if (src.endsWith("조")) {
				조 = temp[0];
				src = "";
			}

			temp = src.split("억");
			if (temp.length == 2) {
				억 = temp[0];
				src = temp[1];
			} else if (src.endsWith("억")) {
				억 = temp[0];
				src = "";
			}

			temp = src.split("만");
			if (temp.length == 2) {
				만 = temp[0];
				나머지 = temp[1];
			} else if (src.endsWith("만")) {
				만 = temp[0];
				src = "";
			} else {
				나머지 = temp[0];
			}
			// System.out.println(만);
			return "" + (toDigit(조, 1000000000000L) + toDigit(억, 100000000) + toDigit(만, 10000) + toDigit(나머지, 1))+currency;
		}

		return src+currency;
	}

	/**
	 * Checks if is 문자금액.
	 * 
	 * @param src
	 *            the src
	 * 
	 * @return true, if is 문자금액
	 */
	private static boolean is문자금액(String src) {
		String[] filter = { "조", "억", "만" };

		for (String str : filter) {
			if (src.contains(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * To digit.
	 * 
	 * @param src
	 *            the src
	 * @param multi
	 *            the multi
	 * 
	 * @return the long
	 */
	private static long toDigit(String src, long multi) {
		String str = src.trim();
		String 천 = "", 백 = "", 십 = "", 일 = "";

		String[] temp = str.split("천");
		if (temp.length == 2) {
			천 = temp[0];
			str = temp[1];
		} else if (src.endsWith("천")) {
			천 = temp[0];
			str = "";
		}

		temp = str.split("백");
		if (temp.length == 2) {
			백 = temp[0];
			str = temp[1];
		} else if (src.endsWith("백")) {
			백 = temp[0];
			str = "";
		}
		temp = str.split("십");
		if (temp.length == 2) {
			십 = temp[0];
			일 = temp[1];
		} else if (src.endsWith("십")) {
			십 = temp[0];
			str = "";
		} else {
			일 = temp[0];
		}
		// System.out.println("천:" + 천 + ", 백:" + 백 + ", 십:" + 십 + ", 일=" + 일);
		long price = (toDigitChar(천, 1000) + toDigitChar(백, 100) + toDigitChar(십, 10) + toDigitChar(일, 1)) * multi;
		return price;

	}

	/**
	 * To digit char.
	 * 
	 * @param src
	 *            the src
	 * @param multi
	 *            the multi
	 * 
	 * @return the int
	 */
	private static int toDigitChar(String src, int multi) {
		if ("".equals(src)) {
			return 0;
		}
		char[] data = src.trim().toCharArray();
		StringBuffer res = new StringBuffer();

		char[][] table = {

		{ '일', '1' }, { '이', '2' }, { '삼', '3' }, { '사', '4' }, { '오', '5' }, { '육', '6' }, { '칠', '7' }, { '팔', '8' },
				{ '구', '9' },

		};
		for (char c : data) {
			if (Character.isDigit(c)) {
				res.append(c);
				continue;
			}
			for (int i = 0; i < table.length; i++) {
				if (c == table[i][0]) {
					res.append(table[i][1]);
				}
			}
		}

		int price = Integer.parseInt(res.toString()) * multi;
		return price;
	}

	public static String validate금액(String src) {
		if (src.contains("<") == false)
			return src;
		src= src.replaceAll("<[^>]+>", "");
		return  src.split("원")[0].trim();

	}

}
