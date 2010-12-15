package net.narusas.aceauction.data;

import junit.framework.TestCase;

public class 건물현황_구조ConverterTest extends TestCase {
	public void test1(){
		assertEquals("조적구조", 건물현황_구조Converter.convert("조적조"));
		assertEquals("조적구조", 건물현황_구조Converter.convert("조적구조"));
		assertEquals("조적구조", 건물현황_구조Converter.convert("조적"));
		assertEquals("철근,콘크리트구조", 건물현황_구조Converter.convert("철근및콘크리트"));
		assertEquals("목구조", 건물현황_구조Converter.convert("목저"));
		
	}
}
