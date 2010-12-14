package net.narusas.si.auction.fetchers;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 부동산표시목록Parser {

	/** The Constant unitPattern1. */
	public final static Pattern unitPattern1 = Pattern.compile("(((\\d+평)\\s*|(\\d+홉)\\s*|(\\d+작)\\s*)+)",
			Pattern.MULTILINE);

	/** The Constant unitPattern2. */
	public final static Pattern unitPattern2 = Pattern.compile(
			"(((\\d+정)\\s*|(\\d+단)\\s*|(\\d+무)\\s*)+(\\d)*보)", Pattern.MULTILINE);

	/** The 단unit. */
	static Pattern 단unit = Pattern.compile("(\\d+)단", Pattern.MULTILINE);

	/** The 무unit. */
	static Pattern 무unit = Pattern.compile("(\\d+)무", Pattern.MULTILINE);

	/** The 보unit. */
	static Pattern 보unit = Pattern.compile("(\\d+)보", Pattern.MULTILINE);

	/** The 작unit. */
	static Pattern 작unit = Pattern.compile("(\\d+)작", Pattern.MULTILINE);

	/** The 정unit. */
	static Pattern 정unit = Pattern.compile("(\\d+)정", Pattern.MULTILINE);

	/** The 평unit. */
	static Pattern 평unit = Pattern.compile("(\\d+)평", Pattern.MULTILINE);

	/** The 홉unit. */
	static Pattern 홉unit = Pattern.compile("(\\d+)홉", Pattern.MULTILINE);

	public static String convertAreaUnit(String chunk) {
		String temp = "";
		Matcher m = unitPattern1.matcher(chunk);
		int pos = 0;
		while (m.find()) {
			temp += chunk.substring(pos, m.start());
			temp += toNoString(convert평홉작ToMeterSquare(m.group(1))) + "㎡ ";
			pos = m.end();
		}
		temp += chunk.substring(pos);
		// if (pos == 0) {
		// temp = chunk;
		// }

		String chunk2 = temp.trim();
		m = unitPattern2.matcher(chunk2);
		pos = 0;
		String temp2 = "";
		while (m.find()) {
			temp2 += chunk2.substring(pos, m.start());
			temp2 += toNoString(convert정단무보ToMeterSquare(m.group(1))) + "㎡ ";
			pos = m.end();
		}

		temp2 += chunk2.substring(pos);
		// if (pos == 0) {
		// temp2 = chunk2;
		// }

		return temp2.trim();
	}

	public static double convert평홉작ToMeterSquare(String area) {
		int pyung = getValue(area, 평unit);
		int hop = getValue(area, 홉unit);
		int jac = getValue(area, 작unit);
		return pyung * 3.3058 + hop * 0.33 + jac * 0.033;
	}

	public static double convert정단무보ToMeterSquare(String area) {
		int 정 = getValue(area, 정unit);
		int 단 = getValue(area, 단unit);
		int 무 = getValue(area, 무unit);
		int 보 = getValue(area, 보unit);

		return 정 * 9917 + 단 * 991.7 + 무 * 99.17 + 보 * 3.3;
	}

	/**
	 * Gets the value.
	 * 
	 * @param area
	 *            the area
	 * @param unit
	 *            the unit
	 * 
	 * @return the value
	 */
	private static int getValue(String area, Pattern unit) {
		Matcher m = unit.matcher(area);
		if (m.find()) {
			return Integer.parseInt(m.group(1));
		}
		return 0;
	}

	/**
	 * To no string.
	 * 
	 * @param value
	 *            the value
	 * 
	 * @return the string
	 */
	private static String toNoString(double value) {
		int v = (int) (value * 1000);
		double v2 = ((double) v) / 1000;
		return String.valueOf(v2);

	}

	public static List<String> parseStructure(String text) {
		List<String> result = new LinkedList<String>();

		String[] lines = text.split("[\n\r]");
		String temp = lines[0];
		for (int i = 1; i < lines.length; i++) {
			String line = lines[i];
			if (countHeadSpace(line) == 0 && !line.equals("")) {
				result.add(temp);
				temp = line;
			} else {
				temp += "\n" + line;
			}
		}
		result.add(temp);
		return result;
	}

	public static int countHeadSpace(String line) {
		char[] chars = line.toCharArray();
		int count = 0;
		for (char c : chars) {
			if (c == ' ') {
				count++;
				continue;
			}
			break;
		}
		return count;
	}

}
