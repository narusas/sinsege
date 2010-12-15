package net.narusas.aceauction.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class �ǹ���Ȳ_����ConverterTest extends TestCase {
	public void test1() {
		assertEquals("4��", �ǹ���Ȳ_����Converter.convert("4��"));
		assertEquals("4��", �ǹ���Ȳ_����Converter.convert("����4��"));
		assertEquals("4��401ȣ", �ǹ���Ȳ_����Converter.convert("4��401ȣ"));
		assertEquals("������", �ǹ���Ȳ_����Converter.convert("����"));
		assertEquals("1�� 102ȣ", �ǹ���Ȳ_����Converter
				.convert("4-7�� 3���� �ű������� 1�� 102ȣ"));
		
		assertEquals("����1�� 102ȣ", �ǹ���Ȳ_����Converter
				.convert("4-7�� 3���� �ű������� ����1�� 102ȣ"));
		assertEquals("������ 102ȣ", �ǹ���Ȳ_����Converter
				.convert("4-7�� 3���� �ű������� ���� 102ȣ"));
	}

	public void test2() {
		Pattern p = Pattern.compile("\\s+([^\\s]+��)");
		String src = "4-7�� 3���� �ű������� 1�� 102ȣ";
		Matcher m = p.matcher(src);
		assertTrue(m.find());
	}
}
