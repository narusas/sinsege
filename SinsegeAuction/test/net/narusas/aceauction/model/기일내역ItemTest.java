package net.narusas.aceauction.model;

import java.util.LinkedList;

import junit.framework.TestCase;

public class ���ϳ���ItemTest extends TestCase {
	public void testContains() {

		Row record = new Row();
		record.add("1");
		record.add("2");
		record.add("3");
		record.add("4");
		record.add("5");
		record.add("6");
		record.add("7");

		���ϳ���Item item1 = new ���ϳ���Item(record);
		���ϳ���Item item2 = new ���ϳ���Item(record);

		LinkedList list = new LinkedList();
		list.add(item1);
		assertTrue(list.contains(item2));
	}
}
