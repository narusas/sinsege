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
 * HTML Table을 분석하는 유틸리티 클래스.
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
	 * 주어진 문자열(src)에서 지정된 문자열(Target)을 검색하고, 그 검색된 위치의 바로 다음 TD 셀의 내용을 읽어 반환한다.
	 * 
	 * @param src HTML을 포함한 대상 문자열
	 * @param target 검색대상이 되는 문자열
	 * 
	 * @return 검색결과
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
	 * 주어진 위치에서 바로 다음에 나오는 TD의 내용을 읽어서 반환한다.
	 * 
	 * @param src 검색할 문자열
	 * @param pos 검색을 시작할 위치
	 * 
	 * @return 검색결과
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
	 * 주어진 문자열(src)에서 지정된 문자열(Target)을 검색하고, 그 검색된 위치의 바로 다음 TD 셀의 내용을 읽은후 불필요한
	 * html 요소를 제거하고 반환한다.
	 * 
	 * @param src HTML을 포함한 대상 문자열
	 * @param target 검색대상이 되는 문자열
	 * 
	 * @return 검색결과
	 */
	public static String getNextTDValueStripped(String src, String target) {
		return strip(getNextTDValue(src, target));
	}

	/**
	 * 주어진 문자열에서 Table을 파싱하여 Table객체를 반환한다. 단 테이블은 첫번째 줄에 제목(Header)를 가지고 있으며
	 * 합쳐진 셀이 없는 일반적을 테이블이여야 한다.
	 * 
	 * @param src HTML을 포함하는 문자열
	 * @param startPos 파싱을 시작할 위치
	 * @param endPos 파싱을 종료할 위치
	 * 
	 * @return the table
	 */
	public static Table parseTable(String src, int startPos, int endPos) {
		TDValue res;
		LinkedList<String> keys = new LinkedList<String>();
		int trStart = 0, trEnd = 0;
		String text = null;
		trStart = startPos;

		// 첫번째 TR을 찾는다.
		Matcher m = TR_OPEN.matcher(src);
		m.find(trStart);

		trStart = m.end();

		m = TR_CLOSE.matcher(src);
		m.find(trStart);
		trEnd = m.start();

		text = src.substring(trStart, trEnd);

		// 첫번째 줄에서 제목들을 찾는다.
		res = findHeaders(keys, text);
		Table t = new Table(keys);

		// 각 줄을 분석한다.
		while (true) {
			// TR 시작을 찾는다.
			m = TR_OPEN.matcher(src);
			m.find(trStart);
			trStart = m.start();
			if (trStart == -1 || trStart >= endPos) {
				break;
			}

			trStart = m.end();

			// TR끝을 찾는다.
			m = TR_CLOSE.matcher(src);
			m.find(trStart);
			trEnd = m.start();

			text = src.substring(trStart, trEnd);
			// 첫번째 셀을 찾는다.
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
	 * 불필요한 HTML요소를 제거한다.
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
	 * 문자열에서 CR(\n), LF(\r)를 제거한다.
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
