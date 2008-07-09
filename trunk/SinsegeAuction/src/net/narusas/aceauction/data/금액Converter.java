/*
 * 
 */
package net.narusas.aceauction.data;

// TODO: Auto-generated Javadoc
/**
 * The Class �ݾ�Converter.
 */
public class �ݾ�Converter {

	/**
	 * Convert.
	 * 
	 * @param src the src
	 * 
	 * @return the string
	 */
	public static String convert(String src) {
		if (src == null) {
			return "";
		}
		if (src.endsWith("��")) {
			src = src.substring(0, src.length() - 1);
		}
		if (src.endsWith("����")) {
			src = src.substring(0, src.length() - 2);
		}
		if (src.startsWith("��")) {
			src = src.substring(1).trim();
		}
		if (src.startsWith("��")) {
			src = src.substring(1);
		}
		src = src.replaceAll(",", "");
		src = src.replaceAll("\\.", "");

		if (is���ڱݾ�(src)) {
			String �� = "", �� = "", �� = "", ������ = "";

			String[] temp = src.split("��");
			if (temp.length == 2) {
				�� = temp[0];
				src = temp[1];
			} else if (src.endsWith("��")) {
				�� = temp[0];
				src = "";
			}

			temp = src.split("��");
			if (temp.length == 2) {
				�� = temp[0];
				src = temp[1];
			} else if (src.endsWith("��")) {
				�� = temp[0];
				src = "";
			}

			temp = src.split("��");
			if (temp.length == 2) {
				�� = temp[0];
				������ = temp[1];
			} else if (src.endsWith("��")) {
				�� = temp[0];
				src = "";
			} else {
				������ = temp[0];
			}
			// System.out.println(��);
			return ""
					+ (toDigit(��, 1000000000000L) + toDigit(��, 100000000) + toDigit(��, 10000) + toDigit(
							������, 1));
		}

		return src;
	}

	/**
	 * Checks if is ���ڱݾ�.
	 * 
	 * @param src the src
	 * 
	 * @return true, if is ���ڱݾ�
	 */
	private static boolean is���ڱݾ�(String src) {
		String[] filter = { "��", "��", "��" };

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
	 * @param src the src
	 * @param multi the multi
	 * 
	 * @return the long
	 */
	private static long toDigit(String src, long multi) {
		String str = src.trim();
		String õ = "", �� = "", �� = "", �� = "";

		String[] temp = str.split("õ");
		if (temp.length == 2) {
			õ = temp[0];
			str = temp[1];
		} else if (src.endsWith("õ")) {
			õ = temp[0];
			str = "";
		}

		temp = str.split("��");
		if (temp.length == 2) {
			�� = temp[0];
			str = temp[1];
		} else if (src.endsWith("��")) {
			�� = temp[0];
			str = "";
		}
		temp = str.split("��");
		if (temp.length == 2) {
			�� = temp[0];
			�� = temp[1];
		} else if (src.endsWith("��")) {
			�� = temp[0];
			str = "";
		} else {
			�� = temp[0];
		}
		// System.out.println("õ:" + õ + ", ��:" + �� + ", ��:" + �� + ", ��=" + ��);
		long price = (toDigitChar(õ, 1000) + toDigitChar(��, 100) + toDigitChar(��, 10) + toDigitChar(
				��, 1))
				* multi;
		return price;

	}

	/**
	 * To digit char.
	 * 
	 * @param src the src
	 * @param multi the multi
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

		{ '��', '1' }, { '��', '2' }, { '��', '3' }, { '��', '4' }, { '��', '5' }, { '��', '6' },
				{ 'ĥ', '7' }, { '��', '8' }, { '��', '9' },

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

}
