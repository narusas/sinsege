package net.narusas.aceauction.data;

import java.util.regex.Matcher;

import junit.framework.TestCase;

public class 건물현황_용도ConverterTest extends TestCase {
	public void testRegx() {
		Matcher m = 건물현황_용도Converter.p1.matcher("건물등");
		assertTrue(m.find());
		assertEquals("건물", m.group(1));
		
		m = 건물현황_용도Converter.p1.matcher("건물등등");
		assertTrue(m.find());
		assertEquals("건물등", m.group(1));
		
		
		assertEquals("a",건물현황_용도Converter.convert("a,b") );
		assertEquals("a",건물현황_용도Converter.convert("a및b") );
		assertEquals("부속건물",건물현황_용도Converter.convert("a부속건물") );
		
		assertEquals("건물",건물현황_용도Converter.convert("일반건물") );
		assertEquals("건물",건물현황_용도Converter.convert("대중건물") );
		assertEquals("건물",건물현황_용도Converter.convert("관련건물") );
		assertEquals("건물",건물현황_용도Converter.convert("생활건물") );
		
		assertEquals("근린시설",건물현황_용도Converter.convert("근린생활시설") );
		
		assertEquals("건물",건물현황_용도Converter.convert("건물등") );
		assertEquals("건물등",건물현황_용도Converter.convert("건물등등") );
		
		assertEquals("건물",건물현황_용도Converter.convert("건물(등등)") );
		assertEquals("건물건물",건물현황_용도Converter.convert("건물(등등)건물(등등)") );
		
		assertEquals("공장",건물현황_용도Converter.convert("건물공장") );
		assertEquals("주택",건물현황_용도Converter.convert("건물주택") );
		assertEquals("종교시설",건물현황_용도Converter.convert("교회") );
		assertEquals("공장",건물현황_용도Converter.convert("가내공장등") );
	}
}
