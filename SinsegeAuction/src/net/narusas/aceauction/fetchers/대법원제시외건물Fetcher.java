/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.model.사건;
import net.narusas.aceauction.model.제시외건물;

// TODO: Auto-generated Javadoc
/**
 * 대법원 사이트에서 제시외 건물 정보를 얻어오는 Fetcher.
 * 
 * @author narusas
 */
public class 대법원제시외건물Fetcher {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The p1. */
	static Pattern p1 = Pattern.compile("\\(용도\\)([^\\(^<]*)");

	/** The p2. */
	static Pattern p2 = Pattern.compile("\\(구조\\)([^\\(^<]*)");

	/** The p3. */
	static Pattern p3 = Pattern.compile("\\(면적\\)([^\\(^<]*)");
	
	/** The last. */
	private 제시외건물 last;

	/**
	 * 사건에 해당하는 제시외 건물 목록 페이지를 읽어와서 분석, 제시외건물 객체의 목록을 반환한다.
	 * 
	 * @param 법원코드 the 법원코드
	 * @param 담당자명 the 담당자명
	 * @param 담당자코드 the 담당자코드
	 * @param 날자 the 날자
	 * @param 사건번호 the 사건번호
	 * 
	 * @return the list<제시외건물>
	 */
	public List<제시외건물> fetchAll(String 법원코드, String 담당자명, int 담당자코드, Date 날자,
			long 사건번호) {
		return parse(getPage(법원코드, 담당자명, 담당자코드, 날자, 사건번호));
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
	public String getPage(String 법원코드, String 담당자명, int 담당자코드, Date 날자,
			long 사건번호) {
		대법원Fetcher fetcher = 대법원Fetcher.getInstance();
		try {

			String res = getPage(법원코드, 담당자명, 담당자코드, 날자, 사건번호, fetcher);
//			logger.info(res);
			if (fetcher.checkValidAccess(res)) {
				return res;
			}
			getCookie(법원코드, 담당자명, 담당자코드, 날자, fetcher);
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
	 * Update.
	 * 
	 * @param s the s
	 */
	public void update(사건 s) {
		s.set제시외건물(fetchAll(s.court.getCode(), s.charge.get담당계이름(), s.charge
				.get담당계코드(), s.charge.get매각기일(), s.사건번호));
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
		fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C100ListCommand&"
						+ "bub_cd="
						+ 법원코드
						+ "&search_flg=2&"
						+ "mae_giil="
						+ 날자.toString().replaceAll("-", ".")
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
				.fetch("/au/SuperServlet?target_command=au.command.auc.C312ListCommand&"
						+ "sa_no="
						+ 사건번호
						+ "&"
						+ "bub_cd="
						+ 법원코드
						+ "&search_flg=2&page=1&level=&addr1=&addr2=&addr3=&mulutil_cd=&amt_flg=&amt=&"
						+ "mae_giil="
						+ 날자.toString().replaceAll("-", ".")
						+ "&"
						+ "jp_cd="
						+ 담당자코드
						+ "&"
						+ "dam_nm="
						+ URLEncoder.encode(담당자명, "euc-kr")
						+ "&"
						+ "allbub="
						+ 법원코드 + "&browser=2&check_msg=");
	}

	/**
	 * Parse제시외건물.
	 * 
	 * @param res the res
	 * @param 물건번호 the 물건번호
	 * @param temp the temp
	 * @param address the address
	 * @param contains the contains
	 */
	private void parse제시외건물(LinkedList<제시외건물> res, String 물건번호, String temp,
			String address, int contains) {
		for (String s : temp.split("[\\n\\r]")) {

			String ss = s.trim();
			if ("".equals(ss)) {
				continue;
			}
			// System.out.println(ss);
			Matcher m = p1.matcher(ss);
			m.find();
			String 용도 = m.group(1);
			if ("\"".equals(용도.trim())) {
				용도 = last.get용도();
			}
			m = p2.matcher(ss);
			m.find();
			String 구조 = m.group(1);
			if ("\"".equals(구조.trim())) {
				구조 = last.get구조();
			}
			m = p3.matcher(ss);
			m.find();
			String 면적 = m.group(1);
			if ("\"".equals(면적.trim())) {
				면적 = last.get면적();
			}
			last = new 제시외건물(Integer.parseInt(물건번호.trim()), 용도.trim(), 구조
					.trim(), 면적.trim(), address, contains);
			res.add(last);
		}
	}

	/**
	 * Parses the.
	 * 
	 * @param src the src
	 * 
	 * @return the list<제시외건물>
	 */
	List<제시외건물> parse(String src) {
		if (src == null) {
			logger.info("제시외 건물 페이지를 얻어오지 못했습니다. ");
			return null;
		}

		LinkedList<제시외건물> res = new LinkedList<제시외건물>();
		String target = src.substring(src.indexOf("물건내역 </b>"), src
				.indexOf("주의)<br>"));
		target = target.substring(target.indexOf("<table"), target
				.lastIndexOf("<table"));
		String[] slices = target.split("<table");
		// System.out.println(slices.length + "개의 슬라이스가 있습니다. ");
		for (String data : slices) {
			String 물건번호 = TableParser.getNextTDValueStripped(data, "물건번호");
			if (물건번호 == null || "".equals(물건번호)) {
				continue;
			}
			String slice = data;
			int contains = 0;
			if (data.contains("제시외") && data.contains("포함")) {
				contains = 1;
			} else if (data.contains("제시외") && data.contains("제외")) {
				contains = 2;
			}

			int pos = slice.indexOf(">제시외<");
			// System.out.println("제시외 건물 정보 위치 " + pos);
			while (pos != -1) {
				// System.out.println("제시외 건물 정보 위치 " + pos);
				String s = slice.substring(0, pos);

				int trPos = s.lastIndexOf("<tr");
				s = s.substring(0, trPos);
				s = s.substring(s.lastIndexOf("<tr"));

				String address = TableParser.getNextTDValue(s, ">목록");
				if (address.indexOf("<") != -1) {
					address = TableParser.strip(
							address.substring(0, address.indexOf("<"))).trim();
				}
				TableParser.TDValue d = TableParser.getNextTDValueByPos(slice,
						pos);
				String temp = d.text;// TableParser.getNextTDValueStripped(slice,
				// ">제시외건물<");
				if (temp == null || "".equals(temp)) {
					break;
				}
				parse제시외건물(res, 물건번호, temp, address, contains);
				pos = slice.indexOf(">제시외건물<", pos + 7);
			}
		}
		return res;
	}

}
