package net.narusas.aceauction.model;

import java.sql.Date;

import junit.framework.TestCase;

public class 담당계Test extends TestCase {
	public void testDate() {
//		담당계 item = new 담당계(null, "2006.06.01", null, null, null);
//		Date s = new Date(2006 - 1900, 5 - 1, 2);
//		System.out.println(s);
//
//		Date end = new Date(2006 - 1900, 5 - 1, 3);
//		System.out.println(end);
//		assertTrue(item.isInScoop(s, end));

	}
	
	public void testSQLDate() {
		Date d = new Date(System.currentTimeMillis());
		d = new Date(2000-1900, 6-1, 1);
		System.out.println(d.getYear());
	}
	
	
}
