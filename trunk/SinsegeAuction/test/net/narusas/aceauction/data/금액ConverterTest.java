package net.narusas.aceauction.data;

import junit.framework.TestCase;

public class �ݾ�ConverterTest extends TestCase {
	public void test1() {
		assertEquals("1", �ݾ�Converter.convert("1��"));
		assertEquals("", �ݾ�Converter.convert(""));
		assertEquals("", �ݾ�Converter.convert(null));
		
		assertEquals("�˼�����", �ݾ�Converter.convert("�˼�����"));
		
		assertEquals("1000", �ݾ�Converter.convert("1,000��"));
		assertEquals("1", �ݾ�Converter.convert("��1��"));
		assertEquals("1", �ݾ�Converter.convert("�� ��1��"));
		
		assertEquals("�̽�", �ݾ�Converter.convert("�̽ʿ�"));
		
		assertEquals("1234567891235", �ݾ�Converter.convert("�� ���� 2õ3��4��5�� 6õ7��8��9�� ��õ�̹��ʿ���"));
		
		
		assertEquals("25000000", �ݾ�Converter.convert("2,500����"));
		
		assertEquals("370000000", �ݾ�Converter.convert("�ݻ��ĥõ������"));
		
		assertEquals("375000000", �ݾ�Converter.convert("�ݻ��ĥõ���鸸����"));
		assertEquals("375200000", �ݾ�Converter.convert("�ݻ��ĥõ�����̽ʸ�����"));
		assertEquals("375290000", �ݾ�Converter.convert("�ݻ��ĥõ�����̽ʱ�������"));
		
		assertEquals("195000000", �ݾ�Converter.convert("��195,000,000����"));
		
		
		
	}
}