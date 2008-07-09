package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.TransformerException;

import junit.framework.TestCase;
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.법원;
import net.narusas.aceauction.model.사건;
import net.narusas.aceauction.model.현황조사서;

public class 대법원현황조사서FetcherTest extends TestCase {
	public void test1() throws IOException {
		// System.out
		// .println(URLDecoder
		// .decode(
		// "2000%C5%B8%B0%E611660%232002%C5%B8%B0%E624387%28%C1%DF%BA%B9%29&userSaList_RelSa=2000%C5%B8%B0%E611660%232002%C5%B8%B0%E624387",
		// "euc-kr"));
		//
		// System.out
		// .println(URLDecoder
		// .decode(
		// "displaySaList_RelSa=%28%C1%DF%BA%B9%29%232002%C5%B8%B0%E624387%28%C1%DF%BA%B9%29&userSaList_RelSa=%232002%C5%B8%B0%E624387",
		// "euc-kr"));

		// 현황조사서=000210,20000130011660,2000타경11660,<br>2002타경24387<br>(중복),,,
		// '000210',
		// '20040130023586',
		// '2004타경23586',
		// '<br>2006타경1832<br>(중복)<br>2004타경50663<br>(병합)',
		// '20040130023586#20040130050663#',
		// '2004타경23586#2004타경50663#',
		// '20040130023586'
		String bub_code = "000210";
		String sa_no = "2004타경23586";
		String mo_sa_no = "2000타경11660";
		String rela = "<br>2006타경1832<br>(중복)<br>2004타경50663<br>(병합)";
		String saList_HasHyunjo = "20040130023586#20040130050663#";
		String userSaList_HasHyunjo = "2004타경23586#2004타경50663#";
		String sano_HasHyunjo = "20040130023586";

		StringBuffer query = new StringBuffer("/au/SuperServlet?");
		대법원현황조사서Fetcher.addParam(query, "target_command",
				"au.command.auc.C102HyunCommand");
		대법원현황조사서Fetcher.addParam(query, "search_flg", "2");
		대법원현황조사서Fetcher.addParam(query, "page", "1");
		대법원현황조사서Fetcher.addParam(query, "bub_cd", bub_code);
		대법원현황조사서Fetcher.addParam(query, "sa_no", sa_no);
		대법원현황조사서Fetcher.addParam(query, "level", "");
		대법원현황조사서Fetcher.addParam(query, "addr1", "");
		대법원현황조사서Fetcher.addParam(query, "addr2", "");
		대법원현황조사서Fetcher.addParam(query, "addr3", "");

		대법원현황조사서Fetcher.addParam(query, "mulutil_cd", "");
		대법원현황조사서Fetcher.addParam(query, "amt_flg", "");
		대법원현황조사서Fetcher.addParam(query, "amt", "");

		대법원현황조사서Fetcher.addParam(query, "mae_giil", "2007.04.10");
		대법원현황조사서Fetcher.addParam(query, "jp_cd", "1002");

		대법원현황조사서Fetcher.addParam(query, "dam_nm", "경매2계");
		대법원현황조사서Fetcher.addParam(query, "allbub", bub_code);
		대법원현황조사서Fetcher.addParam(query, "browser", "2");

		String[] array_relsaInfo = rela.substring(4).split("<br>");
		ArrayList new_array_displaySaList_RelSa = new ArrayList();
		ArrayList new_array_userSaList_RelSa = new ArrayList();
		int temp = 0;
		int new_array_count = 1;

		new_array_displaySaList_RelSa.add(mo_sa_no);
		new_array_userSaList_RelSa.add(mo_sa_no);

		for (int i = 0; i < array_relsaInfo.length; i++) {
			if (array_relsaInfo[i].indexOf('(') != -1) {
				for (int j = temp; j < i; j++) {
					new_array_displaySaList_RelSa.add(array_relsaInfo[j]
							+ array_relsaInfo[i]);
					new_array_userSaList_RelSa.add(array_relsaInfo[j]);
					new_array_count++;
				}
				temp = i + 1;
			}
		}

		String displaySaList_RelSa = "";
		String userSaList_RelSa = "";

		for (int h = 0; h < new_array_displaySaList_RelSa.size(); h++) {
			if (h != (new_array_displaySaList_RelSa.size() - 1)) {
				displaySaList_RelSa += new_array_displaySaList_RelSa.get(h)
						+ "#";
				userSaList_RelSa += new_array_userSaList_RelSa.get(h) + "#";
			} else {
				displaySaList_RelSa += new_array_displaySaList_RelSa.get(h);
				userSaList_RelSa += new_array_userSaList_RelSa.get(h);
			}
		}

		대법원현황조사서Fetcher.addParam(query, "displaySaList_RelSa",
				displaySaList_RelSa);
		대법원현황조사서Fetcher.addParam(query, "userSaList_RelSa", userSaList_RelSa);

		대법원현황조사서Fetcher.addParam(query, "saList_HasHyunjo", saList_HasHyunjo);
		대법원현황조사서Fetcher.addParam(query, "userSaList_HasHyunjo",
				userSaList_HasHyunjo);
		대법원현황조사서Fetcher.addParam(query, "sano_HasHyunjo", sano_HasHyunjo);

		대법원현황조사서Fetcher.addParam(query, "check_msg", "");
		대법원현황조사서Fetcher.addParam(query, "IHsa_no",
				mo_sa_no.indexOf("(이송전)") > 0 ? mo_sa_no : "");

		PageFetcher f = 대법원Fetcher.getInstance();

		f
				.fetch("/au/SuperServlet?target_command=au.command.auc.C100ListCommand&"
						+ "bub_cd="
						+ "00210"
						+ "&search_flg=2&"
						+ "mae_giil="
						+ "2007.04.03"
						+ "&"
						+ "jp_cd="
						+ "1009"
						+ "&"
						+ "dam_nm="
						+ URLEncoder.encode("경매2계", "euc-kr")
						+ "&browser=2&check_msg=");

		// System.out.println(query.toString());
		// System.out.println(f.fetch(query.toString()));
		// System.out.println(query.toString());
		// assertEquals(
		// "/au/SuperServlet?target_command=au.command.auc.C102HyunCommand&search_flg=2&page=1&bub_cd=000210&sa_no=20000130011660&level=&addr1=&addr2=&addr3=&mulutil_cd=&amt_flg=&amt=&mae_giil=2007.04.10&jp_cd=1002&dam_nm=%B0%E6%B8%C52%B0%E8&allbub=000210&browser=2&displaySaList_RelSa=2000%C5%B8%B0%E611660%232002%C5%B8%B0%E624387%28%C1%DF%BA%B9%29&userSaList_RelSa=2000%C5%B8%B0%E611660%232002%C5%B8%B0%E624387&saList_HasHyunjo=&userSaList_HasHyunjo=&sano_HasHyunjo=&check_msg=&IHsa_no=",
		// query.toString());
		//		
		// <frame name="c102Top" scrolling="no" noresize
		// src="/au/auc/C102Top.jsp?level=&search_flg=2&userSaList_HasHyunjo=2004%C5%B8%B0%E623586%232004%C5%B8%B0%E650663%23&jp_cd=1002&page=1&amt=&sano_HasHyunjo=20040130023586&allbub=000210&check_msg=&mae_giil=2007.04.10&displaySaList_RelSa=2000%C5%B8%B0%E611660%232006%C5%B8%B0%E61832%28%C1%DF%BA%B9%29%232004%C5%B8%B0%E650663%28%BA%B4%C7%D5%29&userSaList_RelSa=2000%C5%B8%B0%E611660%232006%C5%B8%B0%E61832%232004%C5%B8%B0%E650663&addr3=&addr2=&addr1=&amt_flg=&bub_cd=000210&saList_HasHyunjo=20040130023586%2320040130050663%23&browser=2&IHsa_no=&mulutil_cd=&dam_nm=%B0%E6%B8%C52%B0%E8&ord_hoi0=1&id=0&jumyuxml_id=6242644&imxml_id=6295817&realmokxml_id=6242641&imwrite_cd=2&ord_hoi=1&sanm_cd=02&sa_no=20040130023586&IHsa_no="
		// marginwidth="0" marginheight="0">
		// <frame name="c102Bottom" scrolling="auto"
		// src="/au/hh100/hhd203.jsp?bub_cd=000210&sa_no=20040130023586&id=0&doc_id=&imwrite_cd=2&ord_hoi=1&sanm_cd=02"
		// marginwidth="15" marginheight="0">

		String src2 = "<frame name=\"c102Top\" scrolling=\"no\" noresize src=\"/au/auc/C102Top.jsp?level=&search_flg=2&userSaList_HasHyunjo=2004%C5%B8%B0%E623586%232004%C5%B8%B0%E650663%23&jp_cd=1002&page=1&amt=&sano_HasHyunjo=20040130023586&allbub=000210&check_msg=&mae_giil=2007.04.10&displaySaList_RelSa=2000%C5%B8%B0%E611660%232006%C5%B8%B0%E61832%28%C1%DF%BA%B9%29%232004%C5%B8%B0%E650663%28%BA%B4%C7%D5%29&userSaList_RelSa=2000%C5%B8%B0%E611660%232006%C5%B8%B0%E61832%232004%C5%B8%B0%E650663&addr3=&addr2=&addr1=&amt_flg=&bub_cd=000210&saList_HasHyunjo=20040130023586%2320040130050663%23&browser=2&IHsa_no=&mulutil_cd=&dam_nm=%B0%E6%B8%C52%B0%E8&ord_hoi0=1&id=0&jumyuxml_id=6242644&imxml_id=6295817&realmokxml_id=6242641&imwrite_cd=2&ord_hoi=1&sanm_cd=02&sa_no=20040130023586&IHsa_no=\" marginwidth=\"0\" marginheight=\"0\">";
		Pattern p1 = Pattern
				.compile("<frame name=\"c102Top\" [^>]+src=\"([^\"]+)");

		Matcher m = p1.matcher(src2);
		assertTrue(m.find());
		assertEquals(
				"/au/auc/C102Top.jsp?level=&search_flg=2&userSaList_HasHyunjo=2004%C5%B8%B0%E623586%232004%C5%B8%B0%E650663%23&jp_cd=1002&page=1&amt=&sano_HasHyunjo=20040130023586&allbub=000210&check_msg=&mae_giil=2007.04.10&displaySaList_RelSa=2000%C5%B8%B0%E611660%232006%C5%B8%B0%E61832%28%C1%DF%BA%B9%29%232004%C5%B8%B0%E650663%28%BA%B4%C7%D5%29&userSaList_RelSa=2000%C5%B8%B0%E611660%232006%C5%B8%B0%E61832%232004%C5%B8%B0%E650663&addr3=&addr2=&addr1=&amt_flg=&bub_cd=000210&saList_HasHyunjo=20040130023586%2320040130050663%23&browser=2&IHsa_no=&mulutil_cd=&dam_nm=%B0%E6%B8%C52%B0%E8&ord_hoi0=1&id=0&jumyuxml_id=6242644&imxml_id=6295817&realmokxml_id=6242641&imwrite_cd=2&ord_hoi=1&sanm_cd=02&sa_no=20040130023586&IHsa_no=",
				m.group(1));

		// String page = f.fetch(m.group(1));
		String page = "function jf_GetDocId( id )\n" + "		{\n"
				+ "			var doc_id = '';\n" + "			\n" + "			if ( id == '0' )\n"
				+ "				doc_id = '';\n" + "			else if ( id == '2' )\n"
				+ "				doc_id = '6242644';\n" + "			else if ( id == '3' )\n"
				+ "				doc_id = '6295817';\n" + "			else if ( id == '4' )\n"
				+ "				doc_id = '';\n" + "				\n" + "			return doc_id;";

		String src3 = "<frame name=\"c102Bottom\" scrolling=\"auto\" src=\"/au/hh100/hhd203.jsp?bub_cd=000210&sa_no=20040130023586&id=0&doc_id=&imwrite_cd=2&ord_hoi=1&sanm_cd=02\" marginwidth=\"15\" marginheight=\"0\">";
		Pattern p2 = Pattern
				.compile("<frame name=\"c102Bottom\" [^>]+src=\"([^\"]+)");
		m = p2.matcher(src3);
		assertTrue(m.find());
		assertEquals(
				"/au/hh100/hhd203.jsp?bub_cd=000210&sa_no=20040130023586&id=0&doc_id=&imwrite_cd=2&ord_hoi=1&sanm_cd=02",
				m.group(1));

		Pattern p3 = Pattern
				.compile("function jf_GetDocId\\( id \\)\\s+\\{[^\\}]+else if \\( id == '2' \\)\\s+doc_id = '(\\d*)[^\\}]+else if \\( id == '4' \\)\\s+doc_id = '(\\d*)'");
		m = p3.matcher(page);
		assertTrue(m.find());
		assertEquals("6242644", m.group(1));
		assertEquals("", m.group(2));

		String src4 = "doc_id = jf_GetDocId( id );\n"
				+ "		\n"
				+ "			var bottomUrl = \"/au/hh100/hhd203.jsp?bub_cd=000210&sa_no=20040130023586&imwrite_cd=2&id=\" + id +\"&doc_id=\" + doc_id + \"&ord_hoi=1&sanm_cd=02\";";

		Pattern p4 = Pattern
				.compile("var bottomUrl = \"([^\"]+)\"[^\"]+\"([^\"]+)\"[^\"]+\"([^\"]+)");
		m = p4.matcher(src4);
		assertTrue(m.find());
		assertEquals(
				"/au/hh100/hhd203.jsp?bub_cd=000210&sa_no=20040130023586&imwrite_cd=2&id=",
				m.group(1));
		assertEquals("&doc_id=", m.group(2));
		assertEquals("&ord_hoi=1&sanm_cd=02", m.group(3));
	}

	public void test2() throws IOException, TransformerException {
		// javascript:loadHyun( '000210',
		// '20060130002514',
		// '2006타경2514',
		// '<br>2006타경10829<br>2006타경27643<br>(중복)',
		// '20060130002514#',
		// '2006타경2514#',
		// '20060130002514'

		현황조사서 report = new 현황조사서("000210", "20060130002514", "2006타경2514",
				"<br>2006타경10829<br>2006타경27643<br>(중복)", "20060130002514#",
				"2006타경2514#", "20060130002514");

		법원 c = 법원.findByName("서울중앙지방법원");
		사건 s = new 사건(c, new 담당계(c, "2007.04.03", "", "1001", "경매1계"));

		대법원현황조사서Fetcher f = new 대법원현황조사서Fetcher();
		String framePage = f.getPage(s, report);
		String topURL = f.parseTopPageURL(framePage);
		String topPage = f.fetch(topURL);
		String[] docIDs = f.parseDocID(topPage);
		String[] urlPieces = f.parseBottomPageURLPiece(topPage);
		String 점유관계조사서URL = f.getXMLDocURL("2", docIDs[0], urlPieces);
		String 부동상표시목록URL = f.getXMLDocURL("4", docIDs[1], urlPieces);
		// System.out.println(점유관계조사서URL);

	
		assertEquals(
				"/au/hh100/hhd203.jsp?bub_cd=000210&sa_no=20060130002514&imwrite_cd=2&id=2&doc_id=7180704&ord_hoi=1&sanm_cd=01",

				점유관계조사서URL);

		assertEquals(
				"/au/hh100/hhd203.jsp?bub_cd=000210&sa_no=20060130002514&imwrite_cd=2&id=4&doc_id=7179820&ord_hoi=1&sanm_cd=01",
				부동상표시목록URL);
		String 점유관계조사서xml = f.fetch(점유관계조사서URL);
		String 부동상표시목록xml = f.fetch(부동상표시목록URL);
		
		XSLTParser parser = new XSLTParser("/xslt/부동산표시목록_view.xsl");
		String 부동상표시목록html = parser.parse(부동상표시목록xml);
		
		
		parser = new XSLTParser("/xslt/현황및점유관계_view.xsl");
		String 점유관계조사서html = parser.parse(점유관계조사서xml);
//		System.out.println(점유관계조사서html);
	}
	
	public void test3() throws IOException, TransformerException{
		현황조사서 report = new 현황조사서("000210", "20060130002514", "2006타경2514",
				"<br>2006타경10829<br>2006타경27643<br>(중복)", "20060130002514#",
				"2006타경2514#", "20060130002514");

		법원 c = 법원.findByName("서울중앙지방법원");
		사건 s = new 사건(c, new 담당계(c, "2007.04.03", "", "1001", "경매1계"));

		대법원현황조사서Fetcher f = new 대법원현황조사서Fetcher();
		String[] htmls = f.fetchAll(s, report);
		System.out.println(htmls[1]);
	}

}
