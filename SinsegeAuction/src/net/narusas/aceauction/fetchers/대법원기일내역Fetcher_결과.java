/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.logging.Logger;

import net.narusas.aceauction.model.Table;
import net.narusas.aceauction.model.사건;

// TODO: Auto-generated Javadoc
/**
 * ui상에서만 사용되는 클래스이다.
 * 
 * @author narusas
 * @see 대법원기일내역Fetcher
 */
public class 대법원기일내역Fetcher_결과 {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/**
	 * Gets the page.
	 * 
	 * @param 법원코드 the 법원코드
	 * @param 담당자명 the 담당자명
	 * @param 담당자코드 the 담당자코드
	 * @param 날자 the 날자
	 * @param 사건번호 the 사건번호
	 * 
	 * @return the page
	 */
	public String getPage(String 법원코드, String 담당자명, int 담당자코드, Date 날자,
			long 사건번호) { 
		logger.info("기일내역 페이지를 얻어옵니다.");
		대법원Fetcher fetcher = 대법원Fetcher.getInstance();
		try {
			String res = get기일내역Page(법원코드, 담당자명, 담당자코드, 날자, 사건번호, fetcher);
			if (fetcher.checkValidAccess(res)) {
				return res;
			}
			getCookie(법원코드, 담당자명, 담당자코드, 날자, fetcher);
			res = get기일내역Page(법원코드, 담당자명, 담당자코드, 날자, 사건번호, fetcher);
			return res;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the 기일내역 table.
	 * 
	 * @param 법원코드 the 법원코드
	 * @param 담당자명 the 담당자명
	 * @param 담당자코드 the 담당자코드
	 * @param 날자 the 날자
	 * @param 사건번호 the 사건번호
	 * 
	 * @return the 기일내역 table
	 */
	public Table get기일내역Table(String 법원코드, String 담당자명, int 담당자코드,
			Date 날자, long 사건번호) { 
		return parse(getPage(법원코드, 담당자명, 담당자코드, 날자, 사건번호)); 
	}

	/**
	 * Parses the.
	 * 
	 * @param src the src
	 * 
	 * @return the table
	 */
	public Table parse(String src) {
		logger.info("기일내역의 분석을 시작합니다. ");
		int startPos = src.indexOf("기일내역&nbsp;&nbsp;&nbsp;");
		if (startPos != -1) {
			int endPos = src.indexOf("</table>", startPos);
			return TableParser.parseTable(src, startPos, endPos);

		}
		return null;
	}

	/**
	 * Gets the cookie.
	 * 
	 * @param 법원코드 the 법원코드
	 * @param 담당자명 the 담당자명
	 * @param 담당자코드 the 담당자코드
	 * @param 날자 the 날자
	 * @param fetcher the fetcher
	 * 
	 * @return the cookie
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	private void getCookie(String 법원코드, String 담당자명, int 담당자코드, Date 날자,
			PageFetcher fetcher) throws IOException,
			UnsupportedEncodingException {
		logger.info("쿠키를얻기위해 억세스합니다. ");
		fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C100ListCommand&" //
						+ "bub_cd=" + 법원코드//
						+ "&search_flg=6&"//
						+ "mae_giil=" + 날자.toString().replaceAll("-",".")//
						+ "&jp_cd=" + 담당자코드//
						+ "&dam_nm=" + URLEncoder.encode(담당자명, "euc-kr")//
						+ "&browser=2&check_msg=");
	}

	/**
	 * Gets the page.
	 * 
	 * @param s the s
	 * 
	 * @return the page
	 */
	private String getPage(사건 s) {
		return getPage(s.court.getCode(), s.charge.get담당계이름(), s.charge
				.get담당계코드(), s.charge.get매각기일(), s.사건번호);
	}

	/**
	 * Gets the 기일내역 page.
	 * 
	 * @param 법원코드 the 법원코드
	 * @param 담당자명 the 담당자명
	 * @param 담당자코드 the 담당자코드
	 * @param 날자 the 날자
	 * @param 사건번호 the 사건번호
	 * @param fetcher the fetcher
	 * 
	 * @return the 기일내역 page
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	private String get기일내역Page(String 법원코드, String 담당자명, int 담당자코드,
			Date 날자, long 사건번호, PageFetcher fetcher) throws IOException,
			UnsupportedEncodingException {

		// /au/SuperServlet?target_command=au.command.auc.C313ListCommand&search_flg=6&bub_cd=000210&sa_no=20070130004692&browser=2&check_msg=&jong_day=&page=2&giil=2007.08.07&jp_cd=1006&dam_nm=%B0%E6%B8%C56%B0%E8
		String res = fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C313ListCommand&search_flg=6"
						+ "&bub_cd=" + 법원코드// 
						+ "&sa_no=" + 사건번호//
						+ "&browser=2" + "&check_msg=" + "&jong_day="
						// +
						// "&page=2"
						+ "&giil=" + 날자.toString().replaceAll("-",".")// 
						+ "&jp_cd=" + 담당자코드// 
						+ "&dam_nm=" + URLEncoder.encode(담당자명, "euc-kr")//

				);
		return res;
	}
}
