package net.narusas.aceauction.data;

import junit.framework.TestCase;

public class ��Ȳ�ּ�ConverterTest extends TestCase {
	public void test1() {
		assertEquals("�ּ�", ��Ȳ�ּ�Converter.convert("�ּ� �� 3 ����"));
		assertEquals("�ּ�", ��Ȳ�ּ�Converter.convert("�ּ� ��3����"));
		assertEquals("�ּ�", ��Ȳ�ּ�Converter.convert("�ּҿ�3����"));
	}
}
