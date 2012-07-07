package net.narusas.si.auction.fetchers;

import java.util.List;

import junit.framework.TestCase;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.법원;

public class FixedDateChargeListTest extends TestCase {
	// ///////////// 기일 입찰
	public void testParse기일입찰() throws Exception {
		기일입찰목록Fetcher f = new 기일입찰목록Fetcher();
		법원 court = new 법원();
		court.set법원명("서울중앙지방법원");
		court.set법원코드(210);
		String html = f.fetchPage(court);
		// System.out.println(html);
		List<담당계> event2 = f.parse(court, html);
		System.out.println(event2);
		assertNotNull(event2);
		assertTrue(event2.size() > 0);
	}
}
