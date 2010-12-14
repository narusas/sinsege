/*
 * 
 */
package net.narusas.si.auction.builder.present;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.fetchers.위지상;
import net.narusas.si.auction.fetchers.전유부분;
import net.narusas.si.auction.fetchers.전유부분Parser;


// TODO: Auto-generated Javadoc
/**
 * The Class 부동산표시목록Item.
 * @deprecated
 */
public class 부동산표시목록Item {
	
	/** The Constant unitPattern1. */
	public final static Pattern unitPattern1 = Pattern.compile(
			"(((\\d+평)\\s*|(\\d+홉)\\s*|(\\d+작)\\s*)+)", Pattern.MULTILINE);
	
	/** The Constant unitPattern2. */
	public final static Pattern unitPattern2 = Pattern.compile(
			"(((\\d+정)\\s*|(\\d+단)\\s*|(\\d+무)\\s*)+(\\d)*보)",
			Pattern.MULTILINE);
	
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

	/** The address. */
	public 부동산표시목록주소 address;

	/** The serial no. */
	private final String serialNo;

	/** The 대지권. */
	private 대지권현황 대지권;

	/** The 매각지분. */
	private String 매각지분;

	/** The 위지상. */
	private 위지상 위지상;

	/** The 전유부분. */
	private 전유부분 전유부분;

	/** The detail. */
	String detail;

	/**
	 * Instantiates a new 부동산표시목록 item.
	 * 
	 * @param serialNo the serial no
	 * @param address the address
	 * @param detail the detail
	 */
	public 부동산표시목록Item(String serialNo, 부동산표시목록주소 address, String detail) {
		this.serialNo = serialNo;
		this.address = address;
		this.detail = detail;
		parse(convertAreaUnit(detail));
	}

	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	public 부동산표시목록주소 getAddress() {
		return address;
	}

	/**
	 * Gets the detail.
	 * 
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * Gets the serial no.
	 * 
	 * @return the serial no
	 */
	public String getSerialNo() {
		return serialNo;
	}

	/**
	 * Gets the 구조.
	 * 
	 * @return the 구조
	 */
	public String get구조() {
		// System.out.println("############################");
		// System.out.println(detail);
		// System.out.println("############################");
		if (has전유부분건물표시()) {
			return get전유부분구조();
		}

		if (detail.startsWith("1동의 건물의 표시")) {
			String[] lines = detail.split("\n");
			return lines[2].trim();
		}
		Pattern p = Pattern.compile("1동건물의표시: (.*)$", Pattern.MULTILINE);
		Matcher m = p.matcher(detail);
		if (m.find()) {
			return m.group(1);
		}
		String temp = detail.replaceAll("\\s", "");

		p = Pattern.compile("1동건물의표시:(.*)$", Pattern.MULTILINE);
		m = p.matcher(temp);
		if (m.find()) {
			return m.group(1);
		}

		String[] lines = detail.split("\\n");
		return lines[0];
	}

	/**
	 * Gets the 대지권.
	 * 
	 * @return the 대지권
	 */
	public 대지권현황 get대지권() {
		return 대지권;
	}

	/**
	 * Gets the 대지면적.
	 * 
	 * @return the 대지면적
	 */
	public String get대지면적() {
		Pattern p = Pattern.compile("([\\d.\\s㎡]+)");
		Matcher m = p.matcher(convertAreaUnit(detail));
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}

	/**
	 * Gets the 매각지분.
	 * 
	 * @return the 매각지분
	 */
	public String get매각지분() {
		return 매각지분;
	}

	/**
	 * Gets the 위지상.
	 * 
	 * @return the 위지상
	 */
	public 위지상 get위지상() {
		return 위지상;
	}
	
	/**
	 * Gets the 전유부분.
	 * 
	 * @return the 전유부분
	 */
	public 전유부분 get전유부분() {
		return 전유부분;
	}

	/**
	 * Gets the 전유부분구조.
	 * 
	 * @return the 전유부분구조
	 */
	public String get전유부분구조() {
		Pattern p = p = Pattern.compile(
				"    구      조 : ([^\\d]*)\\s*[\\d.\\s㎡]*$", Pattern.MULTILINE);
		Matcher m = p.matcher(detail);
		if (m.find()) {
			return m.group(1).trim();
		}
		return "";
	}

	/**
	 * Gets the 전유부분면적.
	 * 
	 * @return the 전유부분면적
	 */
	public String get전유부분면적() {
		Pattern p = Pattern.compile("    면      적 : (.*)$", Pattern.MULTILINE);
		Matcher m = p.matcher(detail);
		if (m.find()) {
			return m.group(1);
		}
		p = Pattern.compile("    구      조 : [^\\d]*([\\d.]+\\s*㎡)$",
				Pattern.MULTILINE);
		m = p.matcher(detail);
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}

	/**
	 * Has전유부분건물표시.
	 * 
	 * @return true, if successful
	 */
	public boolean has전유부분건물표시() {
		return detail.contains("전유부분의");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return serialNo + "," + address + "," + detail;
	}
	
	/**
	 * Parses the.
	 * 
	 * @param detail the detail
	 */
	private void parse(String detail) {
		List<String> chunks = parseStructure(detail);
		for (String chunk : chunks) {
			chunk = convertAreaUnit(chunk);
			if (chunk.startsWith("전유부분의 건물의 표시")) {
				전유부분 = 전유부분Parser.parse(chunk);
			} else if (chunk.startsWith("대지권의 목적인 토지의 표시")
					|| chunk.startsWith("대지권의 표시")) {
				대지권 = new 대지권현황(chunk, address.toString());
			} else if (chunk.contains("위 지상")) {
				위지상 = new 위지상(chunk, address.toString());
			} else if (chunk.contains("매각지분")) {
				매각지분 = chunk;
			}
		}
	}

	/**
	 * Convert area unit.
	 * 
	 * @param chunk the chunk
	 * 
	 * @return the string
	 */
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

	/**
	 * Convert정단무보 to meter square.
	 * 
	 * @param area the area
	 * 
	 * @return the double
	 */
	public static double convert정단무보ToMeterSquare(String area) {
		int 정 = getValue(area, 정unit);
		int 단 = getValue(area, 단unit);
		int 무 = getValue(area, 무unit);
		int 보 = getValue(area, 보unit);

		return 정 * 9917 + 단 * 991.7 + 무 * 99.17 + 보 * 3.3;
	}

	/**
	 * Convert평홉작 to meter square.
	 * 
	 * @param area the area
	 * 
	 * @return the double
	 */
	public static double convert평홉작ToMeterSquare(String area) {
		int pyung = getValue(area, 평unit);
		int hop = getValue(area, 홉unit);
		int jac = getValue(area, 작unit);
		return pyung * 3.3058 + hop * 0.33 + jac * 0.033;
	}
	
	/**
	 * Count head space.
	 * 
	 * @param line the line
	 * 
	 * @return the int
	 */
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
	
	/**
	 * Parses the structure.
	 * 
	 * @param text the text
	 * 
	 * @return the list< string>
	 */
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
	
	/**
	 * Gets the value.
	 * 
	 * @param area the area
	 * @param unit the unit
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
	 * @param value the value
	 * 
	 * @return the string
	 */
	private static String toNoString(double value) {
		int v = (int) (value * 1000);
		double v2 = ((double) v) / 1000;
		return String.valueOf(v2);

	}

}
