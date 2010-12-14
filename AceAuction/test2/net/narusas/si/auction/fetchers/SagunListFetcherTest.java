package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;
import net.narusas.util.lang.NFile;

public class SagunListFetcherTest extends TestCase {
	// http://www.courtauction.go.kr/RetrieveRealEstMulDetailList.laf
	// ?jiwonNm=%B4%EB%C0%FC%C1%F6%B9%E6%B9%FD%BF%F8
	// &jpDeptCd=1001
	// &termStartDt=20090217
	// &termEndDt=20090224
	// &ipchalGbncd=000332
	// &_NEXT_CMD=RetrieveRealEstMulDetailList.laf
	// &_NEXT_SRNID=PNO102002
	// &srnID=PNO102005

	private String html;
	private 사건목록Fetcher f;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		html = NFile.getText(new File("fixture2/005_기간입찰사건목록.html"));
		f = new 사건목록Fetcher();
	}

	public void testFetch() throws IOException {

		법원 court = new 법원();
		court.set법원명("대전지방법원");
		court.set법원코드(280);

		담당계 charge = new 담당계();
		charge.set소속법원(court);
		charge.set담당계코드(1001);
		charge.set담당계이름("경매1계");
		charge.set입찰_시작날자(new Date(2009 - 1900, 3 - 1, 24));
		charge.set입찰_끝날자(new Date(2009 - 1900, 3 - 1, 31));
		List<사건> list = f.fetchAll(court, charge);
		assertEquals(10, list.size());
	}

	public void testFetch2() throws IOException {
		법원 court = new 법원();
		court.set법원명("서울중앙지방법원");
		court.set법원코드(210);

		담당계 charge = new 담당계();
		charge.set소속법원(court);
		charge.set담당계코드(1007);
		charge.set담당계이름("경매7계");
		charge.set매각기일(new Date(2009 - 1900, 3 - 1, 26));
		List<사건> list = f.fetchAll(court, charge);
//		System.out.println(list);
		assertEquals(38, list.size());
	}

	public void testParseLastPage() throws IOException {
		// 페이지당 사건이 20개씩 있으며 인덱스는 1부터시작
		// 따라서 7페이지까지 있는 사건임.

		assertEquals(141, f.parseLastPage(html));

	}

	// public void testListUp() {
	// List<사건> list = f.parseSagunList(html);
	// assertEquals(11, list.size());
	// }
	//
	// public void testFetchAll() throws IOException {
	// List<사건> list = f.fetchAll(법원.findByName("제주지방법원"), new
	// 담당계(법원.findByName("제주지방법원"), 1006, "경매6계",
	// new Date(), new Date(2009 - 1900, 3 - 1, 57), new Date(2009 - 1900, 3 -
	// 1, 12), "", "", ""));
	// // System.out.println(list);
	// assertTrue(list.size() > 30);
	//		
	// }

}
