/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.model.Row;
import net.narusas.aceauction.model.Table;

// TODO: Auto-generated Javadoc
/**
 * HTML Table�� �м��ϴ� ��ƿ��Ƽ Ŭ����.
 * 
 * @author narusas
 */
public class TableParser {
	
	/**
	 * The Class TDValue.
	 */
	public static class TDValue {
		
		/** The pos. */
		int pos;
		
		/** The text. */
		String text;

		/**
		 * Instantiates a new tD value.
		 * 
		 * @param text the text
		 * @param pos the pos
		 */
		public TDValue(String text, int pos) {
			this.pos = pos;
			this.text = text;
		}
	}

	/** The T d_ close. */
	static Pattern TD_CLOSE = Pattern.compile("(</[Tt][Dd][^>]*>)");

	/** The T d_ open. */
	static Pattern TD_OPEN = Pattern.compile("(<[Tt][Dd][^>]*>)");

	/** The T r_ close. */
	static Pattern TR_CLOSE = Pattern.compile("(</[Tt][Rr][^>]*>)");

	/** The T r_ open. */
	static Pattern TR_OPEN = Pattern.compile("(<[Tt][Rr][^>]*>)");

	/**
	 * �־��� ���ڿ�(src)���� ������ ���ڿ�(Target)�� �˻��ϰ�, �� �˻��� ��ġ�� �ٷ� ���� TD ���� ������ �о� ��ȯ�Ѵ�.
	 * 
	 * @param src HTML�� ������ ��� ���ڿ�
	 * @param target �˻������ �Ǵ� ���ڿ�
	 * 
	 * @return �˻����
	 */
	public static String getNextTDValue(String src, String target) {
		int pos = src.indexOf(target);
		if (pos == -1) {
			return "";
		}

		Matcher m = TD_OPEN.matcher(src);
		if (m.find(pos) == false) {
			return "";
		}

		pos = m.end();

		m = TD_CLOSE.matcher(src);
		if (m.find(pos) == false) {
			return "";
		}
		int endPos = m.start();

		return src.substring(pos, endPos).trim();
	}

	/**
	 * �־��� ��ġ���� �ٷ� ������ ������ TD�� ������ �о ��ȯ�Ѵ�.
	 * 
	 * @param src �˻��� ���ڿ�
	 * @param pos �˻��� ������ ��ġ
	 * 
	 * @return �˻����
	 */
	public static TDValue getNextTDValueByPos(String src, int pos) {

		Matcher m = TD_OPEN.matcher(src);
		if (m.find(pos) == false) {
			return new TDValue("", new Integer(-1));
		}

		pos = m.end();
		m = TD_CLOSE.matcher(src);
		int endPos = 0;
		if (m.find(pos)) {
			endPos = m.start();
		} else {
			m = TR_CLOSE.matcher(src);
			if (m.find(pos)) {
				endPos = m.start();
			} else {
				endPos = src.length() - 1;
			}
		}

		String text = src.substring(pos, endPos).trim();
		return new TDValue(TableParser.strip(text), new Integer(src.indexOf(
				">", endPos) + 1));
	}

	/**
	 * �־��� ���ڿ�(src)���� ������ ���ڿ�(Target)�� �˻��ϰ�, �� �˻��� ��ġ�� �ٷ� ���� TD ���� ������ ������ ���ʿ���
	 * html ��Ҹ� �����ϰ� ��ȯ�Ѵ�.
	 * 
	 * @param src HTML�� ������ ��� ���ڿ�
	 * @param target �˻������ �Ǵ� ���ڿ�
	 * 
	 * @return �˻����
	 */
	public static String getNextTDValueStripped(String src, String target) {
		return strip(getNextTDValue(src, target));
	}

	/**
	 * �־��� ���ڿ����� Table�� �Ľ��Ͽ� Table��ü�� ��ȯ�Ѵ�. �� ���̺��� ù��° �ٿ� ����(Header)�� ������ ������
	 * ������ ���� ���� �Ϲ����� ���̺��̿��� �Ѵ�.
	 * 
	 * @param src HTML�� �����ϴ� ���ڿ�
	 * @param startPos �Ľ��� ������ ��ġ
	 * @param endPos �Ľ��� ������ ��ġ
	 * 
	 * @return the table
	 */
	public static Table parseTable(String src, int startPos, int endPos) {
		TDValue res;
		LinkedList<String> keys = new LinkedList<String>();
		int trStart = 0, trEnd = 0;
		String text = null;
		trStart = startPos;

		// ù��° TR�� ã�´�.
		Matcher m = TR_OPEN.matcher(src);
		m.find(trStart);

		trStart = m.end();

		m = TR_CLOSE.matcher(src);
		m.find(trStart);
		trEnd = m.start();

		text = src.substring(trStart, trEnd);

		// ù��° �ٿ��� ������� ã�´�.
		res = findHeaders(keys, text);
		Table t = new Table(keys);

		// �� ���� �м��Ѵ�.
		while (true) {
			// TR ������ ã�´�.
			m = TR_OPEN.matcher(src);
			m.find(trStart);
			trStart = m.start();
			if (trStart == -1 || trStart >= endPos) {
				break;
			}

			trStart = m.end();

			// TR���� ã�´�.
			m = TR_CLOSE.matcher(src);
			m.find(trStart);
			trEnd = m.start();

			text = src.substring(trStart, trEnd);
			// ù��° ���� ã�´�.
			res = getNextTDValueByPos(text, 0);

			Row r = new Row();
			for (int i = 0; i < keys.size(); i++) {
				if (res.pos == -1) {
					break;
				}
				String value = stripCRLF(res.text);

				r.add(value);

				res = getNextTDValueByPos(text, res.pos);
			}
			t.add(r);

		}
		return t;
	}

	/**
	 * ���ʿ��� HTML��Ҹ� �����Ѵ�.
	 * 
	 * @param src the src
	 * 
	 * @return the string
	 */
	public static String strip(String src) {
		String temp = src.replaceAll("(<[^>]*>)", "");
		temp = temp.replaceAll("&nbsp;", " ");
		temp = temp.replaceAll("&lt;;", "<");
		temp = temp.replaceAll("&gt;", ">");
		temp = temp.replaceAll("&amp;", "&");

		temp = temp.replaceAll("     ", " ");
		temp = temp.replaceAll("    ", " ");
		temp = temp.replaceAll("   ", " ");
		temp = temp.replaceAll("  ", " ");

		return temp.trim();
	}

	/**
	 * ���ڿ����� CR(\n), LF(\r)�� �����Ѵ�.
	 * 
	 * @param src the src
	 * 
	 * @return the string
	 */
	public static String stripCRLF(String src) {
		String temp = src.replaceAll("\\n", "");
		return temp.replaceAll("\\r", "");
	}

	/**
	 * Find headers.
	 * 
	 * @param keys the keys
	 * @param text the text
	 * 
	 * @return the tD value
	 */
	private static TDValue findHeaders(LinkedList<String> keys, String text) {
		TDValue res;
		res = getNextTDValueByPos(text, 0);
		while (true) {
			if (res.pos == -1) {
				break;
			}

			String key = res.text;
			keys.add(key);
			res = getNextTDValueByPos(text, res.pos);
		}
		return res;
	}
}
