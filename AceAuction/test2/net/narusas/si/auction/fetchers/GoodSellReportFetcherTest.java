package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.model.매각물건명세서;
import net.narusas.si.auction.model.매각물건명세서비고;
import net.narusas.util.lang.NFile;

import junit.framework.TestCase;

public class GoodSellReportFetcherTest extends TestCase {
//	public void testParseNoRent() throws IOException {
//		String html = NFile.getText(new File("fixture2/029_물건_매각물건명세서_임차내역없음.html"), "euc-kr");
//		assertFalse(html.contains("매각물건명세서가 없습니다"));
//		Sheet sheet = Sheet.parse(html, "<caption>매각물건명세서</caption>", true, true);
//		assertEquals("조사된 임차내역 없음", sheet.valueAt(0, 0));
//	}
//
//	public void testParseNoPage() throws IOException {
//		String html = NFile.getText(new File("fixture2/028_물건_매각물건명세서_없을때.html"), "euc-kr");
//		assertTrue(html.contains("매각물건명세서가 없습니다"));
//	}
//
//	public void testParseRent() throws IOException {
//		String html = NFile.getText(new File("fixture2/027_물건_매각물건명세서.html"), "euc-kr");
//		assertFalse(html.contains("매각물건명세서가 없습니다"));
//		assertFalse(html.contains("조사된 임차내역 없음"));
//
//		Pattern p = Pattern.compile("<td rowspan=[^>]+>([^<]*)</td>\\s+" + "<td>([^<]*)</td>\\s+<td>([^<]*)</td>\\s+"
//				+ "<td>([^<]*)</td>\\s+<td>([^<]*)</td>\\s+" + "<td>([^<]*)</td>\\s+<td>([^<]*)</td>\\s+"
//				+ "<td>([^<]*)</td>\\s+<td>([^<]*)</td>\\s+" + "<td>([^<]*)</td>\\s+</tr>", Pattern.MULTILINE);
//		Matcher m = p.matcher(html);
//		assertTrue(m.find());
//		assertEquals("위니아.딤채 직영점", m.group(1));
//		assertEquals("디046호 전부 ", m.group(2));
//		assertEquals("현황조사", m.group(3));
//		assertEquals("임차인", m.group(4));
//		assertEquals("미상", m.group(5));
//		assertEquals("없음", m.group(6));
//		assertEquals("", m.group(7));
//		assertEquals("미상", m.group(8));
//		assertEquals("미상", m.group(9));
//		assertEquals("", m.group(10));
//
//		Pattern commentP = Pattern.compile("&lt; 비고 &gt; &nbsp;<br/>([^<]*)", Pattern.MULTILINE);
//		Matcher m2 = commentP.matcher(html);
//		m2.find();
//		assertEquals("위니아.딤채 직영점 : 부동산현황조사보고서에 의하면 위니아.딤채 직영점은 관리비만 납부하고 점유하고 있음. ", m2.group(1));
//
//		// Sheet sheet = Sheet.parse(html, "<caption>매각물건명세서</caption>", true,
//		// true);
//
//		// assertEquals(10, sheet.rowColumnSize(0));
//		//
//		// String 비고 =
//		// HTMLUtils.converHTMLSpecialChars(sheet.valueAt(sheet.rowSize() - 1,
//		// 0));
//		// 비고 = 비고.substring(비고.indexOf("/") + 1);
//		// assertEquals("위니아.딤채 직영점 : 부동산현황조사보고서에 의하면 위니아.딤채 직영점은 관리비만 납부하고 점유하고 있음.",
//		// 비고);
//		//
//		// // String
//		//
//		// String 점유인 = sheet.valueAt(0, 0);
//		// String 점유부분 = sheet.valueAt(0, 1);
//		// String 출처 = sheet.valueAt(0, 2);
//		//
//		// String 당사자구분 = sheet.valueAt(0, 3);
//		// String 점유기간 = sheet.valueAt(0, 4);
//		//
//		// String 보증금 = sheet.valueAt(0, 5);
//		// String 차임 = sheet.valueAt(0, 6);
//		// String 전입일자 = sheet.valueAt(0, 7);
//		// String 확정일자 = sheet.valueAt(0, 8);
//		// String 배당요구 = sheet.valueAt(0, 9);
//		//
//		// 매각물건명세서 item = new 매각물건명세서();
//		// item.set점유인(점유인);
//		// item.set당사자구분(당사자구분);
//		// item.set점유부분(점유부분);
//		// item.set점유기간(점유기간);
//		// item.set보증금(보증금);
//		// item.set차임(차임);
//		// item.set전입일자(전입일자);
//		// item.set확정일자(확정일자);
//		// item.set배당요구(배당요구);
//		// item.set정보출처(출처);
//
//		// System.out.println(item);
//
//	}

	// public void testParseMultiRent() throws IOException {
	// String html = NFile.getText(new
	// File("fixture2/30_물건_매각물건명세서_임차인여러명.html"), "euc-kr");
	// assertFalse(html.contains("매각물건명세서가 없습니다"));
	// assertFalse(html.contains("조사된 임차내역 없음"));
	//
	// // System.out.println(new 물건매각물건명세서Fetcher().parse(html));
	//
	// 매각물건명세서비고 comments = new 물건매각물건명세서Fetcher().parse비고(html);
	// assertEquals("전기수 : 소유자 김지선의 자임. ", comments.get비고());
	// assertEquals("해당사항 없음", comments.get비소멸권리());
	// assertEquals("해당사항 없음", comments.get지상권개요());
	// assertEquals(
	// "일괄매각. 제시외건물포함.\n"
	// +
	// "조재록으로부터 설비공사대금 금36,300,000원을 위하여 본건 건물중 2층부엌방 및 옥상출입문 등을 점유한다면서 유치권신고를 하였으나, 그 스스로 이사건 경매개시결정기입등기후인 2008.12.15일 이후부터 점유를 개시하였다고 하는 바, 실제로 그렇다면 그 유치권은 매수인에게 대항할 수 없음(대법원2005다22688판결참조)\n"
	// +
	// "문상엽으로부터 집수리공사대금 금11,000,000원을 위하여 지층현관입구방 및 지층창고를 점유하다면서 유치권신고를 하였으나,그 스스로 이사건 경매개시결정기입등기이후인 2008.10.30일 부터라고 하는 바, 실제로 그렇다면 그 유치권은 매수인에게 대항할 수 없음(대법원 2005다22688판결참조).",
	// comments.get비고란());
	//
	// }

	public void testParseRent양식변경() throws IOException {
		String html = NFile.getText(new File("fixture2/037_매각물건명세서_양식변경.html"), "euc-kr");
		물건매각물건명세서Fetcher f = new 물건매각물건명세서Fetcher();
		
		List<매각물건명세서> res = f.parse(html);
		System.out.println(res);
	}
	
	public void testParse() {
		String src = "          <td rowspan=\"2\">김일환</td>  \n"+
"                 \n"+
"          <td>지하층2호 </td>\n"+
"          <td>현황조사</td>\n"+
"          <td>주거<br/>임차인</td>\n"+
"		  <td>미상</td>\n"+
"		  <td>미상</td>\n"+
"		  <td></td>\n"+
"		  <td>2005.7.12.</td>\n"+
"		  <td>미상</td>\n"+
"		  <td></td>\n"+
"		</tr>";
		
		Pattern namePattern = Pattern.compile(//
				"<td rowspan=[^>]+>([^<]*)</td>\\s+" //
				+ "<td>([^<]*)</td>\\s+" //
				+ "<td>([^<]*)</td>\\s+" //
				+ "<td>([^<]*)</td>\\s+" //
				+ "<td>([^<]*)</td>\\s+"//
				+ "<td>([^<]*)</td>\\s+" //
				+ "<td>([^<]*)</td>\\s+" //
				+ "<td>([^<]*)</td>\\s+" //
				+ "<td>([^<]*)</td>\\s+"//
				+ "<td>([^<]*)</td>\\s+" //
				+ "</tr>", Pattern.MULTILINE);
		
		Matcher m = namePattern.matcher(src);
		assertTrue(m.find());
		
	}
}
