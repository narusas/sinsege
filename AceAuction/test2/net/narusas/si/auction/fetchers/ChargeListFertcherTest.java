package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.법원;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;

public class ChargeListFertcherTest extends TestCase {
	public void test기간입찰분석FetchHTML() throws Exception {
		기간입찰목록Fetcher f = new 기간입찰목록Fetcher();
		String html = f.fetchPage();
		assertNotNull(html);
		assertTrue(html.contains("<caption>매각 일정</caption>"));
		assertTrue(html.contains("입찰기간"));

		List<담당계> event2 = f.parse(html);
		assertNotNull(event2);
		assertTrue(event2.size() > 0);
	}

	public void test기간입찰분석ParseHTML() throws Exception {
		String html = NFile.getText(new File("fixture2/004_기간별검색.html"));

		기간입찰목록Fetcher f = new 기간입찰목록Fetcher();
		List<담당계> event2 = f.parse(html);
		assertNotNull(event2);
		assertTrue(event2.size() > 0);
	}

	public void test기간입찰분석Regx() throws IOException {
		String html = NFile.getText(new File("fixture2/004_기간별검색.html"));
		Pattern p = Pattern.compile(기간입찰목록Fetcher.REGX_COMMON, Pattern.MULTILINE);
		Matcher m = p.matcher(html);
		assertTrue(m.find());
		assertEquals("2009.02.17", m.group(1));
		assertEquals("2009.02.24", m.group(2));
		assertEquals("2009.03.03", m.group(3));
		assertEquals("대전지방법원", m.group(4));

		Pattern p2 = Pattern.compile(기간입찰목록Fetcher.REGX_CHARGE, Pattern.MULTILINE);
		Matcher m2 = p2.matcher(html);
		assertTrue(m2.find(m.end()));
		assertEquals("대전지방법원", m2.group(1));
		assertEquals("1001", m2.group(2)); // 담담계 코드
		assertEquals("20090217", m2.group(3));
		assertEquals("20090224", m2.group(4)); // 종료
		assertEquals("1000", m2.group(5)); // 시간
		assertEquals("", m2.group(6));
		assertEquals("", m2.group(7));
		assertEquals("", m2.group(8));
		assertEquals("제106호 법정", m2.group(9));
		assertEquals("경매1계&nbsp;&nbsp;", m2.group(10));
	}

	// ///////////// 기일 입찰
	public void testParse기일입찰() throws HttpException, IOException {
		기일입찰목록Fetcher f = new 기일입찰목록Fetcher();
		String html = f.fetchPage(법원.findByName("서울중앙지방법원"));
		System.out.println(html);
	}

}
