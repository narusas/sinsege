/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.narusas.aceauction.model.Row;
import net.narusas.aceauction.model.Table;
import net.narusas.aceauction.model.명세서;
import net.narusas.aceauction.model.물건;

// TODO: Auto-generated Javadoc
/**
 * 대법원 사이트에서 매각물건 명세서를 얻어오는 Fetcher.
 * 
 * @author narusas
 */
public class 대법원매각물건명세서Fetcher {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/**
	 * 물건에 해당하는 매각물건명세서를 대법원 사이트에서 얻어 와서 사건의 정보를 갱신한다.
	 * 
	 * @param s 갱신하고자 하는 사건.
	 * 
	 * @return the 물건
	 */

	public 물건 update(물건 s) {
		String page = getPage(s);
		s.set매각물건명세서HTML(page);
		return parse(page, s);
	}

	/**
	 * Filter table.
	 * 
	 * @param src the src
	 * @param startPos the start pos
	 * @param endPos the end pos
	 * 
	 * @return the table
	 */
	private Table filterTable(String src, int startPos, int endPos) {
		Table table = fixTable(src, startPos, endPos);
		removeDuplicatedRow(table);
		return table;
	}

	/**
	 * Fix table.
	 * 
	 * @param src the src
	 * @param startPos the start pos
	 * @param endPos the end pos
	 * 
	 * @return the table
	 */
	private Table fixTable(String src, int startPos, int endPos) {
		logger.log(Level.FINEST, "매각물건명세서 테이블을 수정합니다. ");
		Table table = TableParser.parseTable(src, startPos, endPos);
		List<Row> toRemove = new LinkedList<Row>();
		for (int i = 0; i < table.getRows().size(); i++) {
			Row r = table.getRows().get(i);
			// 매각물건 명세서 테이블의 총 컬럼 수는 10이다. 그런데 컬럼이 9개라는 것은
			// 점유자의 성명 컬럼에 <td rows="2"> 등으로 한칸을 먹은 상태에서 컬럼이 진행 되고 있다는 것으로
			// 한명의 점유자에 속한 정보라는 의미이다.
			// 따라서 컬럼의 수가 9이면, 한줄 앞의 정보를 이용하여 추가하게 하는 것이다.
			if (r.getValues().size() == 9 && i != 0) {
				r.getValues().add(0, table.getRows().get(i - 1).getValue(0));
			} else {
				// 9보다 작은 컬럼은 보통 비고 이거나, 의미없는 주석이므로 무시한다.
				if (r.getValues().size() < 9) {
					break;
				}

				if (r.getValue(0).equals(r.getValue(1))
						&& r.getValue(0).equals(r.getValue(2))) {
					toRemove.add(r);
				}
			}
		}
		table.getRows().removeAll(toRemove);
		return table;
	}

	/**
	 * Gets the cookies.
	 * 
	 * @param fetcher the fetcher
	 * 
	 * @return the cookies
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void getCookies(PageFetcher fetcher) throws IOException {
		logger.log(Level.FINEST,"세션정보가 잘못되어 쿠키를 다시 얻어 옵니다.");
		fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C100ListCommand&"
						+ "bub_cd=000210"
						+ "&search_flg=2&"
						+ "mae_giil=2006.03.20"
						+ "&"
						+ "jp_cd="
						+ "&"
						+ "dam_nm=" + "&browser=2&check_msg=");
	}

	/**
	 * Gets the page.
	 * 
	 * @param 법원코드 the 법원코드
	 * @param 사건번호 the 사건번호
	 * @param 매물번호 the 매물번호
	 * @param fetcher the fetcher
	 * 
	 * @return the page
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private String getPage(String 법원코드, long 사건번호, String 매물번호,
			PageFetcher fetcher) throws IOException {
		logger.info(법원코드 + " " + 사건번호 + " " + 매물번호 + "에 해당하는 명세서 페이지를 얻어옵니다. ");
		return fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C101MyungCommand&"
						+ "sa_no="
						+ 사건번호
						+ "&"
						+ "bub_cd="
						+ 법원코드
						+ "&"
						+ "maemul_no="
						+ 매물번호
						+ "&search_flg=2&page=1&level=&addr1=&addr2=&addr3=&mulutil_cd=&amt_flg=&amt=&"
						+ "&browser=2&check_msg=");
	}

	/**
	 * Gets the page.
	 * 
	 * @param s the s
	 * 
	 * @return the page
	 */
	private String getPage(물건 s) {
		return getPage(s.사건.court.getCode(), s.사건.사건번호, String.valueOf(s.물건번호));
	}

	/**
	 * Parses the table.
	 * 
	 * @param src the src
	 * 
	 * @return the table
	 */
	private Table parseTable(String src) {
		int startPos = src.indexOf("사업자등록신청일자와 확정일자의 유무와 그 일자");
		startPos = src.indexOf("</table>", startPos) + 5;
		int endPos = src.indexOf("</table>", startPos);
		Table table = filterTable(src, startPos, endPos);
		return table;
	}

	/**
	 * Removes the duplicated row.
	 * 
	 * @param table the table
	 */
	private void removeDuplicatedRow(Table table) {
		logger.log(Level.FINEST,"중복된 레코드를 제거합니다. ");
		for (int i = 0; i < table.getRows().size(); i++) {
			if (i != 0
					&& table.getRows().get(i).getValue(0).equals(
							table.getRows().get(i - 1).getValue(0))) {
				table.getRows().remove(i - 1);
				i--;
			}
		}
	}

	/**
	 * Set물건.
	 * 
	 * @param src the src
	 * @param s the s
	 * @param table the table
	 */
	private void set물건(String src, 물건 s, Table table) {

		int start = src.indexOf("< 비고 ><br>");
		int end = src.indexOf("</tr", start);
		String 비고 = TableParser.strip(src.substring(start, end));

		String 권리 = TableParser.getNextTDValueStripped(src,
				"등기된 부동산에 관한 권리 또는 가처분으로  매각허가에 의하여 그 효력이 소멸되지 아니하는 것");
		String 개요 = TableParser.getNextTDValueStripped(src,
				"매각허가에 의하여 설정된 것으로 보는 지상권의 개요");
		String 비고란 = TableParser.getNextTDValueStripped(src, "비고란");
		s.set명세서(new 명세서(table, 비고, 권리, 개요, 비고란));
	}

	/**
	 * Gets the page.
	 * 
	 * @param 법원코드 the 법원코드
	 * @param 사건번호 the 사건번호
	 * @param 매물번호 the 매물번호
	 * 
	 * @return the page
	 */
	String getPage(String 법원코드, long 사건번호, String 매물번호) {
		대법원Fetcher fetcher = 대법원Fetcher.getInstance();
		try {
			String res = getPage(법원코드, 사건번호, 매물번호, fetcher);
			if (fetcher.checkValidAccess(res)) {
				return res;
			}

			getCookies(fetcher);
			res = getPage(법원코드, 사건번호, 매물번호, fetcher);
			return res;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Parses the.
	 * 
	 * @param src the src
	 * @param s the s
	 * 
	 * @return the 물건
	 */
	물건 parse(String src, 물건 s) {
		logger.info("명세서 분석을 시작합니다.");
		Table table = parseTable(src);
		set물건(src, s, table);

		logger.info("명세서 분석을 종료합니다.");
		return s;
	}
}
