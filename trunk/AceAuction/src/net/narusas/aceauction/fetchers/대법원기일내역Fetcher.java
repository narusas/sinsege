package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.logging.Logger;

import net.narusas.aceauction.model.사건;

/**
 * Builder에서 사용되는 기일내역Fetcher이다.
 * 
 * @author narusas
 * 
 */
public class 대법원기일내역Fetcher {
	Logger logger = Logger.getLogger("log");

	public String getPage(String 법원코드, String 담당자명, int 담당자코드, Date 날자,
			long 사건번호) {
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
	 * 사건에 해당하는 기일 내역을 대법원 사이트에서 얻어 와서 사건의 정보를 갱신한다.
	 * 
	 * @param s
	 *            갱신하고자 하는 사건.
	 * @return
	 */
	public 사건 update(사건 s) {
		return parse(getPage(s), s);
	}

	private void getCookie(String 법원코드, String 담당자명, int 담당자코드, Date 날자,
			PageFetcher fetcher) throws IOException,
			UnsupportedEncodingException {
		logger.info("세션정보가 잘못되어 쿠키를 다시 얻어 옵니다.");
		fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C100ListCommand&"
						+ "bub_cd="
						+ 법원코드
						+ "&search_flg=2&"
						+ "mae_giil="
						+ 날자
						+ "&"
						+ "jp_cd="
						+ 담당자코드
						+ "&"
						+ "dam_nm="
						+ URLEncoder.encode(담당자명, "euc-kr")
						+ "&browser=2&check_msg=");
	}

	private String getPage(사건 s) {
		String page = getPage(s.court.getCode(), s.charge.get담당계이름(), s.charge
				.get담당계코드(), s.charge.get매각기일(), s.사건번호);
//		logger.info(page);
		return page;
	}

	private String get기일내역Page(String 법원코드, String 담당자명, int 담당자코드, Date 날자,
			long 사건번호, PageFetcher fetcher) throws IOException,
			UnsupportedEncodingException {
		logger.info("기일내역 페이지를 얻어옵니다.");
		String res = fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C313ListCommand&"
						+ "sa_no="
						+ 사건번호
						+ "&"
						+ "bub_cd="
						+ 법원코드
						+ "&search_flg=2&page=1&level=&addr1=&addr2=&addr3=&mulutil_cd=&amt_flg=&amt=&"
						+ "mae_giil="
						+ 날자
						+ "&"
						+ "jp_cd="
						+ 담당자코드
						+ "&"
						+ "dam_nm="
						+ URLEncoder.encode(담당자명, "euc-kr")
						+ "&"
						+ "allbub=" + 법원코드 + "&browser=2&check_msg=");
		return res;
	}

	private 사건 parse(String src, 사건 s) {
		logger.info("기일내역의 분석을 시작합니다. ");
		int startPos = src.indexOf("기일내역&nbsp;&nbsp;&nbsp;");
		if (startPos != -1) {
			int endPos = src.indexOf("</table>", startPos);
			s.set기일내역(TableParser.parseTable(src, startPos, endPos));
		}
		return s;
	}
}
