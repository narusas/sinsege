/*
 * 
 */
package net.narusas.aceauction.model;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// TODO: Auto-generated Javadoc
/**
 * The Class �ε���ǥ�ø��Item.
 */
public class �ε���ǥ�ø��Item {
	
	/** The Constant unitPattern1. */
	public final static Pattern unitPattern1 = Pattern.compile(
			"(((\\d+��)\\s*|(\\d+ȩ)\\s*|(\\d+��)\\s*)+)", Pattern.MULTILINE);
	
	/** The Constant unitPattern2. */
	public final static Pattern unitPattern2 = Pattern.compile(
			"(((\\d+��)\\s*|(\\d+��)\\s*|(\\d+��)\\s*)+(\\d)*��)",
			Pattern.MULTILINE);
	
	/** The ��unit. */
	static Pattern ��unit = Pattern.compile("(\\d+)��", Pattern.MULTILINE);
	
	/** The ��unit. */
	static Pattern ��unit = Pattern.compile("(\\d+)��", Pattern.MULTILINE);
	
	/** The ��unit. */
	static Pattern ��unit = Pattern.compile("(\\d+)��", Pattern.MULTILINE);
	
	/** The ��unit. */
	static Pattern ��unit = Pattern.compile("(\\d+)��", Pattern.MULTILINE);
	
	/** The ��unit. */
	static Pattern ��unit = Pattern.compile("(\\d+)��", Pattern.MULTILINE);

	/** The ��unit. */
	static Pattern ��unit = Pattern.compile("(\\d+)��", Pattern.MULTILINE);

	/** The ȩunit. */
	static Pattern ȩunit = Pattern.compile("(\\d+)ȩ", Pattern.MULTILINE);

	/** The address. */
	public �ε���ǥ�ø���ּ� address;

	/** The serial no. */
	private final String serialNo;

	/** The ������. */
	private ��������Ȳ ������;

	/** The �Ű�����. */
	private String �Ű�����;

	/** The ������. */
	private ������ ������;

	/** The �����κ�. */
	private �����κ� �����κ�;

	/** The detail. */
	String detail;

	/**
	 * Instantiates a new �ε���ǥ�ø�� item.
	 * 
	 * @param serialNo the serial no
	 * @param address the address
	 * @param detail the detail
	 */
	public �ε���ǥ�ø��Item(String serialNo, �ε���ǥ�ø���ּ� address, String detail) {
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
	public �ε���ǥ�ø���ּ� getAddress() {
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
	 * Gets the ����.
	 * 
	 * @return the ����
	 */
	public String get����() {
		// System.out.println("############################");
		// System.out.println(detail);
		// System.out.println("############################");
		if (has�����κаǹ�ǥ��()) {
			return get�����κб���();
		}

		if (detail.startsWith("1���� �ǹ��� ǥ��")) {
			String[] lines = detail.split("\n");
			return lines[2].trim();
		}
		Pattern p = Pattern.compile("1���ǹ���ǥ��: (.*)$", Pattern.MULTILINE);
		Matcher m = p.matcher(detail);
		if (m.find()) {
			return m.group(1);
		}
		String temp = detail.replaceAll("\\s", "");

		p = Pattern.compile("1���ǹ���ǥ��:(.*)$", Pattern.MULTILINE);
		m = p.matcher(temp);
		if (m.find()) {
			return m.group(1);
		}

		String[] lines = detail.split("\\n");
		return lines[0];
	}

	/**
	 * Gets the ������.
	 * 
	 * @return the ������
	 */
	public ��������Ȳ get������() {
		return ������;
	}

	/**
	 * Gets the ��������.
	 * 
	 * @return the ��������
	 */
	public String get��������() {
		Pattern p = Pattern.compile("([\\d.\\s��]+)");
		Matcher m = p.matcher(convertAreaUnit(detail));
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}

	/**
	 * Gets the �Ű�����.
	 * 
	 * @return the �Ű�����
	 */
	public String get�Ű�����() {
		return �Ű�����;
	}

	/**
	 * Gets the ������.
	 * 
	 * @return the ������
	 */
	public ������ get������() {
		return ������;
	}
	
	/**
	 * Gets the �����κ�.
	 * 
	 * @return the �����κ�
	 */
	public �����κ� get�����κ�() {
		return �����κ�;
	}

	/**
	 * Gets the �����κб���.
	 * 
	 * @return the �����κб���
	 */
	public String get�����κб���() {
		Pattern p = p = Pattern.compile(
				"    ��      �� : ([^\\d]*)\\s*[\\d.\\s��]*$", Pattern.MULTILINE);
		Matcher m = p.matcher(detail);
		if (m.find()) {
			return m.group(1).trim();
		}
		return "";
	}

	/**
	 * Gets the �����κи���.
	 * 
	 * @return the �����κи���
	 */
	public String get�����κи���() {
		Pattern p = Pattern.compile("    ��      �� : (.*)$", Pattern.MULTILINE);
		Matcher m = p.matcher(detail);
		if (m.find()) {
			return m.group(1);
		}
		p = Pattern.compile("    ��      �� : [^\\d]*([\\d.]+\\s*��)$",
				Pattern.MULTILINE);
		m = p.matcher(detail);
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}

	/**
	 * Has�����κаǹ�ǥ��.
	 * 
	 * @return true, if successful
	 */
	public boolean has�����κаǹ�ǥ��() {
		return detail.contains("�����κ���");
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
			if (chunk.startsWith("�����κ��� �ǹ��� ǥ��")) {
				�����κ� = new �����κ�(chunk);
			} else if (chunk.startsWith("�������� ������ ������ ǥ��")
					|| chunk.startsWith("�������� ǥ��")) {
				������ = new ��������Ȳ(chunk, address.toString());
			} else if (chunk.contains("�� ����")) {
				������ = new ������(chunk, address.toString());
			} else if (chunk.contains("�Ű�����")) {
				�Ű����� = chunk;
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
			temp += toNoString(convert��ȩ��ToMeterSquare(m.group(1))) + "�� ";
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
			temp2 += toNoString(convert���ܹ���ToMeterSquare(m.group(1))) + "�� ";
			pos = m.end();
		}

		temp2 += chunk2.substring(pos);
		// if (pos == 0) {
		// temp2 = chunk2;
		// }

		return temp2.trim();
	}

	/**
	 * Convert���ܹ��� to meter square.
	 * 
	 * @param area the area
	 * 
	 * @return the double
	 */
	public static double convert���ܹ���ToMeterSquare(String area) {
		int �� = getValue(area, ��unit);
		int �� = getValue(area, ��unit);
		int �� = getValue(area, ��unit);
		int �� = getValue(area, ��unit);

		return �� * 9917 + �� * 991.7 + �� * 99.17 + �� * 3.3;
	}

	/**
	 * Convert��ȩ�� to meter square.
	 * 
	 * @param area the area
	 * 
	 * @return the double
	 */
	public static double convert��ȩ��ToMeterSquare(String area) {
		int pyung = getValue(area, ��unit);
		int hop = getValue(area, ȩunit);
		int jac = getValue(area, ��unit);
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
