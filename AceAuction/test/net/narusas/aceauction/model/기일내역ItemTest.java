package net.narusas.aceauction.model;

import java.util.LinkedList;

import junit.framework.TestCase;

public class 扁老郴开ItemTest extends TestCase {
	public void testContains() {

		Row record = new Row();
		record.add("1");
		record.add("2");
		record.add("3");
		record.add("4");
		record.add("5");
		record.add("6");
		record.add("7");

		扁老郴开Item item1 = new 扁老郴开Item(record);
		扁老郴开Item item2 = new 扁老郴开Item(record);

		LinkedList list = new LinkedList();
		list.add(item1);
		assertTrue(list.contains(item2));
	}
}
