package net.narusas.si.auction.fetchers;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 매각예정검색의 검색 결과에 나오는 사건 목록을 얻어와 분석하는  클래스
 * 
 * 샘플 URL: http://www.courtauction.go.kr/RetrieveMgakPlanMulSrch.laf?jiwonNm=%BC%F6%BF%F8%C1%F6%B9%E6%B9%FD%BF%F8&jpDeptCd=000000&_NEXT_CMD=RetrieveMgakPlanMulSrch.laf&_NEXT_SRNID=PNO102008
 * @author narusas
 *
 */
public class 매각예정_사건목록Fetcher {
	final Logger logger = LoggerFactory.getLogger("auction");
	
	String fetchFirstPage(법원 court) throws IOException {
		return fetchPage(court,  1);
	}
	
	String fetchPage(법원 court, int 페이지번호) throws IOException{
		String url = makeUrl(court, 페이지번호);
		System.out.println(url);
		return 대법원Fetcher.getInstance().fetch(url);	
	}

	private String makeUrl(법원 court, int 페이지번호) {
		String url = MessageFormat.format(
				"/RetrieveMgakPlanMulSrch.laf" +
				"?" +
				"page=default20" +
				"&bubwLocGubun=1" +
				"&jpDeptCd=000000" +
				"&jiwonNm={1}" +
				"&page=default20" +
				"&pageSpec=default20" +
				"&targetRow={2}" +
				"&lafjOrderBy="+
				"&_NEXT_CMD=RetrieveMgakPlanMulSrch.laf&_NEXT_SRNID=PNO102008"
				,court.get법원코드()
				,HTMLUtils.encodeUrl(court.get법원명())
				,페이지번호
		);
		return url;
	}
	public List<사건> fetchAll(법원 court) throws IOException {
		String html = fetchFirstPage(court);
		int lastPageRow = parseLastPage(html);
		List<사건> res = new ArrayList<사건>();
		res.addAll(parseSagunList(html));
		for (int i = 21; i <= lastPageRow && i < 1900; i += 20) {
			html = fetchPage(court, i);
			List<사건> tmp = parseSagunList(html);
			res.addAll(tmp);
		}
		
		setup(res, court);

		return removeDuplicated(res);
	}
	
	private void setup(List<사건> res, 법원 court) {
		for (사건 s : res) {
			s.set법원(court);
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
	
	List<사건> parseSagunList(String html) {
		Pattern p = Pattern
				.compile("onclick=\"javascript:detailCaseSrch\\('([^']+)',\\s+'([^']+)',\\s+'([^']+)'\\);");
		Matcher m = p.matcher(html);
		List<사건> list = new LinkedList<사건>();
		while (m.find()) {
			list.add(parseSagun(m, html));
		}

		return list;
	}
	
	사건 parseSagun(Matcher m, String html) {
		String sagunNo = m.group(2);
		long 사건번호 = Long.parseLong(sagunNo);
		int index = html.indexOf("Ltbl_list_lvl0", m.end());
		if (index == -1) {
			index = html.indexOf("Ltbl_list_lvl1", m.end());
			if (index == -1) {
				index = html.length();
			}

		} else {
			int index2 = html.indexOf("Ltbl_list_lvl1", m.end());
			if (index2 != -1) {
				index = Math.min(index, index2);
			}
		}

		String chunk = html.substring(m.end(), index);

		사건 sagun = new 사건();
		sagun.set사건번호(사건번호);
		sagun.set신건(chunk.contains("신건"));
		System.out.println(sagun);
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
	
	public static void main(String[] args) throws IOException {
		매각예정_사건목록Fetcher f = new 매각예정_사건목록Fetcher();
		법원 court = new 법원();
		court.set법원명("인천지방법원");
		court.set법원코드(0);
//		String html = f.fetchFirstPage(court);
//		System.out.println(html);
//		List<사건> list = f.parseSagunList(html);
//		System.out.println("#################################");
//		System.out.println(list);
		
		List<사건> list = f.fetchAll(court);
		System.out.println(list);
	}
}
