/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.logging.Logger;

import net.narusas.aceauction.model.사건;

// TODO: Auto-generated Javadoc
/**
 * The Class 대법원기본내역Fetcher.
 */
public class 대법원기본내역Fetcher {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/**
	 * Update.
	 * 
	 * @param s the s
	 * 
	 * @return the 사건
	 */
	public 사건 update(사건 s) {
		return parse(getPage(s), s);
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
	private String getCookie(String 법원코드, String 담당자명, int 담당자코드, Date 날자,
			PageFetcher fetcher) throws IOException,
			UnsupportedEncodingException {
		return fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C100ListCommand&"
						+ "bub_cd="
						+ 법원코드
						+ "&search_flg=2&"
						+ "mae_giil="
						+ 날자.toString().replaceAll("-",".")
						+ "&"
						+ "jp_cd="
						+ 담당자코드
						+ "&"
						+ "dam_nm="
						+ URLEncoder.encode(담당자명, "euc-kr")
						+ "&browser=2&check_msg=");
	}

	/**
	 * Gets the page.
	 * 
	 * @param 법원코드 the 법원코드
	 * @param 담당자명 the 담당자명
	 * @param 담당자코드 the 담당자코드
	 * @param 날자 the 날자
	 * @param 사건번호 the 사건번호
	 * @param fetcher the fetcher
	 * 
	 * @return the page
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	private String getPage(String 법원코드, String 담당자명, int 담당자코드, Date 날자,
			long 사건번호, PageFetcher fetcher) throws IOException,
			UnsupportedEncodingException {
		return fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C311ListCommand&"
						+ "sa_no="
						+ 사건번호
						+ "&"
						+ "bub_cd="
						+ 법원코드
						+ "&search_flg=2&page=1&level=&addr1=&addr2=&addr3=&mulutil_cd=&amt_flg=&amt=&"
						+ "mae_giil="
						+ 날자.toString().replaceAll("-",".")
						+ "&"
						+ "jp_cd="
						+ 담당자코드
						+ "&"
						+ "dam_nm="
						+ URLEncoder.encode(담당자명, "euc-kr")
						+ "&"
						+ "allbub=" + 법원코드 + "&browser=2&check_msg=");
	}

	/**
	 * Gets the page.
	 * 
	 * @param s the s
	 * 
	 * @return the page
	 */
	private String getPage(사건 s) {
		String page = getPage(s.court.getCode(), s.charge.get담당계이름(), s.charge
				.get담당계코드(), s.charge.get매각기일(), s.사건번호);
//		logger.info(page);
		return page;
	}

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
	String getPage(String 법원코드, String 담당자명, int 담당자코드, Date 날자, long 사건번호) {
		대법원Fetcher fetcher = 대법원Fetcher.getInstance();
		try {

			String res = getPage(법원코드, 담당자명, 담당자코드, 날자, 사건번호, fetcher);

			// 세션이 수립 되어 있음에도 불구하고 잘못된 접근이라다고 나오는 경우가 있다. 이를 위해 한번더 얻어오는
			// 로직이다.
			if (fetcher.checkValidAccess(res)) {
				return res;
			}

			// 세션 재 수립
			getCookie(법원코드, 담당자명, 담당자코드, 날자, fetcher);

			// 다시 페이지 획득
			res = getPage(법원코드, 담당자명, 담당자코드, 날자, 사건번호, fetcher);
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
	 * @param 사건 the 사건
	 * 
	 * @return the 사건
	 */
	사건 parse(String src, 사건 사건) {
		사건.set사건명(TableParser.getNextTDValueStripped(src, "사건명"));
		사건.set접수일자(TableParser.getNextTDValueStripped(src, "접수일자"));
		사건.set개시결정일자(TableParser.getNextTDValueStripped(src, "개시결정일자"));
		사건.set청구금액(TableParser.getNextTDValueStripped(src, "청구금액"));
		사건.set종국결과(TableParser.getNextTDValueStripped(src, "종국결과"));
		사건.set종국일자(TableParser.getNextTDValueStripped(src, "종국일자"));
		사건.set사건항고정지여부(TableParser.getNextTDValueStripped(src, "사건항고/정지여부"));

		TableParser.TDValue res = null;
		사건.clear채권자();
		사건.clear채무자();
		사건.clear소유자();

		// 채권자 처리.
		int pos = src.indexOf("채권자");
		while (pos != -1) {
			res = TableParser.getNextTDValueByPos(src, pos);
			pos = res.pos;

			사건.add채권자(TableParser.getNextTDValueByPos(src, pos).text);
			pos = src.indexOf("채권자", pos + 3);
		}

		// 채무자
		pos = src.indexOf(">채무자<");
		while (pos != -1) {
			res = TableParser.getNextTDValueByPos(src, pos);
			pos = res.pos;

			사건.add채무자(TableParser.getNextTDValueByPos(src, pos).text);
			pos = src.indexOf(">채무자<", pos + ">채무자<".length());
		}

		// 소유자
		pos = src.indexOf(">소유자<");
		while (pos != -1) {
			res = TableParser.getNextTDValueByPos(src, pos);
			pos = res.pos;

			사건.add소유자(TableParser.getNextTDValueByPos(src, pos).text);
			pos = src.indexOf(">소유자<", pos + ">소유자<".length());
		}

		// 채무자겹 소유자는 두곳에 모두 추가 된다.
		pos = src.indexOf(">채무자겸소유자<");
		while (pos != -1) {
			res = TableParser.getNextTDValueByPos(src, pos);
			pos = res.pos;
			String temp = TableParser.getNextTDValueByPos(src, pos).text;
			사건.add채무자(temp);
			사건.add소유자(temp);

			pos = src.indexOf(">채무자겸소유자<", pos + ">채무자겸소유자<".length());
		}

		// 배당요구종기내역
		int startPos = src.indexOf("배당요구종기내역</b>");
		if (startPos != -1) {
			int endPos = src.indexOf("</table>", startPos);
			사건.set배당요구종기내역(TableParser.parseTable(src, startPos, endPos));
		}

		return 사건;
	}

}
