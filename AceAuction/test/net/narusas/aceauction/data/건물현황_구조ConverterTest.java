package net.narusas.aceauction.data;

import junit.framework.TestCase;

public class �ǹ���Ȳ_����ConverterTest extends TestCase {
	public void test1(){
		assertEquals("��������", �ǹ���Ȳ_����Converter.convert("������"));
		assertEquals("��������", �ǹ���Ȳ_����Converter.convert("��������"));
		assertEquals("��������", �ǹ���Ȳ_����Converter.convert("����"));
		assertEquals("ö��,��ũ��Ʈ����", �ǹ���Ȳ_����Converter.convert("ö�ٹ���ũ��Ʈ"));
		assertEquals("����", �ǹ���Ȳ_����Converter.convert("����"));
		
	}
}
