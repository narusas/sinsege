package net.narusas.si.auction.fetchers;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;

public class 사건목록Fetcher {
	public String fetchFirstPage(법원 court, 담당계 charge) throws IOException {
		return fetchPage(court, charge, 1);
	}

	public String fetchPage(법원 court, 담당계 charge, int targetRow) throws IOException {
		//page=default20&jiwonNm=%BF%EF%BB%EA%C1%F6%B9%E6%B9%FD%BF%F8&maeGiil=20100709&_NEXT_CMD=&_C_ipchalGbncd=000331&bubwLocGubun=1&_C_jiwonNm=%BF%EF%BB%EA%C1%F6%B9%E6%B9%FD%BF%F8&maePlace=%B0%E6%B8%C5+%B9%FD%C1%A4&_CUR_SRNID=PNO102005&_NEXT_SRNID=PNO102002&_NAVI_SRNID=PNO102005&pageSpec=default20&pageSpec=default20&_PRE_SRNID=&_SRCH_SRNID=PNO102005&termEndDt=&_LOGOUT_CHK=&maeHh1=1000&ipchalGbncd=000331&maeHh4=&_FORM_YN=Y&page=default20&maeHh2=&maeHh3=&_NAVI_CMD=InitMulSrch.laf&srnID=PNO102005&termStartDt=&jpDeptCd=1007&_CUR_CMD=RetrieveBubwGiilList.laf&targetRow=1701&lafjOrderBy=
		대법원Fetcher.getInstance().prepare();
		if (is기간입찰(charge)) {
			return 대법원Fetcher.getInstance().fetch(
					MessageFormat.format(
							"/RetrieveRealEstMulDetailList.laf" + "?page=default20&jiwonNm={0}"
									+ "&jpDeptCd={1,number,####}" + "&termStartDt={2,date,yyyyMMdd}"
									+ "&termEndDt={3,date,yyyyMMdd}" + "&ipchalGbncd=000332"
									+ "&_NEXT_CMD=RetrieveRealEstMulDetailList.laf"
									+ "&_NEXT_SRNID=PNO102002" + "&srnID=PNO102005&&targetRow={4}", //
							HTMLUtils.encodeUrl(court.get법원명()), charge.get담당계코드(), charge.get입찰_시작날자(),
							charge.get입찰_끝날자(), targetRow));
		}
		String query =MessageFormat.format("/RetrieveRealEstMulDetailList.laf" //
				+ "?jiwonNm={0}"//
				+ "&jpDeptCd={1,number,####}" //
				+ "&maeGiil={2,date,yyyyMMdd}" //
				+ "&ipchalGbncd=000331" //
				+ "&srnID=PNO102005"//
				+ "&targetRow={3,number,####}",//
				HTMLUtils.encodeUrl(court.get법원명()), charge.get담당계코드(), charge.get매각기일(), targetRow); 

//		System.out.println(query);
		String html =대법원Fetcher.getInstance().fetch(query); 
		return html;

	}

	private boolean is기간입찰(담당계 charge) {
		return charge.get입찰_시작날자() != null && charge.get입찰_끝날자() != null;
	}

	List<사건> parseSagunList(String html) {
		Pattern p = Pattern
				.compile("onclick=\"javascript:detailCaseSrch\\('([^']+)',\\s+'([^']+)','([^']+)'\\); return false;\"");
		Matcher m = p.matcher(html);
		List<사건> list = new LinkedList<사건>();
		while (m.find()) {
			list.add(parseSagun(m,html));
		}

		return list;
	}

	사건 parseSagun(Matcher m, String html) {
		String sagunNo = m.group(2);
		long 사건번호 = Long.parseLong(sagunNo);
		int index = html.indexOf("Ltbl_list_lvl0",m.end());
		if (index == -1){
			index = html.indexOf("Ltbl_list_lvl1",m.end());
			if (index == -1){
				index = html.length();	
			}
			
		}
		else {
			int index2 = html.indexOf("Ltbl_list_lvl1",m.end());
			if (index2 != -1){
				index = Math.min(index, index2);
			}
		}
		
		
		
		String chunk = html.substring(m.end(), index);
		
		사건 sagun = new 사건();		
		sagun.set사건번호(사건번호);
		sagun.set신건(chunk.contains("신건"));
		return sagun;
	}

	int parseLastPage(String html) {
		Pattern p = Pattern.compile("onclick=\"goPage\\('(\\d+)'\\); return false;\"");
		Matcher m = p.matcher(html);
		int lastPage = 1;
		while (m.find()) {
			lastPage = Integer.parseInt(m.group(1));
		}
		return lastPage;
	}

	public List<사건> fetchAll(담당계 charge) throws IOException {
		return fetchAll(charge.get소속법원(), charge);
	}

	List<사건> fetchAll(법원 court, 담당계 charge) throws IOException {
		String html = fetchFirstPage(court, charge);
		int lastPageRow = parseLastPage(html);
		List<사건> res = new ArrayList<사건>();
		res.addAll(parseSagunList(html));
		for (int i=21; i <= lastPageRow && i< 1700; i += 20) {
			html = fetchPage(court, charge, i);
			List<사건> tmp = parseSagunList(html);
			res.addAll(tmp);  
		}
		
		setup(res, court, charge);

		return removeDuplicated(res);
	}

	private void setup(List<사건> res, 법원 court, 담당계 charge) {
		for (사건 s : res) {
			s.set법원(court);
			s.set담당계(charge);
		}
	}

	private List<사건> removeDuplicated(List<사건> list) {
		List<사건> res = new ArrayList<사건>();
		for (사건 s : list) {
			if (isDuplicated(s, res) == false) {
				res.add(s);
			}
		}
		return res;
	}

	private boolean isDuplicated(사건 s, List<사건> res) {
		for (사건 s2 : res) {
			if (s2.get사건번호() == s.get사건번호()) {
				return true;
			}
		}
		return false;
	}
}
