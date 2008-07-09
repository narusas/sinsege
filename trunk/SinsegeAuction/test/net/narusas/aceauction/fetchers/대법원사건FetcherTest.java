package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.aceauction.model.법원;
import net.narusas.aceauction.model.사건;
import net.narusas.util.lang.NFile;

import org.htmlparser.util.ParserException;

public class 대법원사건FetcherTest extends TestCase {

	private 대법원사건Fetcher fetcher;

	private String fixture01;

	private String fixture_slice;

	protected void setUp() throws Exception {
		super.setUp();
		fetcher = new 대법원사건Fetcher(법원.findByCode("서울중앙지방법원"),//
				new Date(2007 - 1900, 3 - 1, 20),// 
				1006, "경매6계");
		fixture01 = NFile.getText(new File("fixtures/mulgunlist.html"));
		fixture_slice = NFile.getText(new File("fixtures/slice.html"));
	}

	// public void test1() {
	// SagunFetcher fetcher = new SagunFetcher(CourtTable.code("서울중앙지방법원"),
	// "2007.03.20", "1006", "경매6계");
	// assertNotNull(fetcher.getSaguns());
	// }

	// public void testGetPage() throws HttpException, IOException {
	// // 5페이지에서는 [5]에 링크가 없어야 한다.
	// String page = fetcher.getPage(5);
	// assertTrue(page.contains("<b>[5]</b>"));
	// assertTrue(page
	// .contains("<A HREF='javascript:goMove(6);' id='a1'> [6] </A>"));
	// }

	// public void testCollectPages() throws HttpException, IOException {
	// String[] pages = fetcher.getPages();
	// assertEquals(9, pages.length);
	// }

	public void testBasicInfo() {
		assertEquals("2007.03.20 10:00", fetcher.parseDateInfo(fixture01));
		assertEquals("경매법정", fetcher.parseLocationInfo(fixture01));
	}

	public void testAll() throws ParserException, IOException {
		List<사건> s = fetcher.getSaguns();
		System.out.println(s.get(0));
	}

}
