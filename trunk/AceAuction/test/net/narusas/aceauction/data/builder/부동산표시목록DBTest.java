package net.narusas.aceauction.data.builder;

import net.narusas.si.auction.builder.present.Entry;
import net.narusas.si.auction.builder.present.부동산표시목록Builder;
import junit.framework.TestCase;

public class 부동산표시목록DBTest extends TestCase {
	public void test1() {
		부동산표시목록Builder db = new 부동산표시목록Builder(0, null, null);
		assertTrue(db.landMatch.size() > 0);
		assertTrue(db.bldMatch.size() > 0);
		assertTrue(db.전유부분Match.size() > 0);
		assertEquals(1, db.전유부분Match.size());
		Entry e = db.전유부분Match.get(0);
		assertEquals("주택", e.getKey()[0]);
		assertEquals("근린", e.getKey()[1]);
	}
}
