package net.narusas.aceauction.data.builder;

import net.narusas.si.auction.builder.present.Entry;
import net.narusas.si.auction.builder.present.�ε���ǥ�ø��Builder;
import junit.framework.TestCase;

public class �ε���ǥ�ø��DBTest extends TestCase {
	public void test1() {
		�ε���ǥ�ø��Builder db = new �ε���ǥ�ø��Builder(0, null, null);
		assertTrue(db.landMatch.size() > 0);
		assertTrue(db.bldMatch.size() > 0);
		assertTrue(db.�����κ�Match.size() > 0);
		assertEquals(1, db.�����κ�Match.size());
		Entry e = db.�����κ�Match.get(0);
		assertEquals("����", e.getKey()[0]);
		assertEquals("�ٸ�", e.getKey()[1]);
	}
}
