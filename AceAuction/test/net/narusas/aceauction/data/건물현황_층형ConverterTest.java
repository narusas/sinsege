package net.narusas.aceauction.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class 건물현황_층형ConverterTest extends TestCase {
	public void test1() {
		assertEquals("4층", 건물현황_층형Converter.convert("4층"));
		assertEquals("4층", 건물현황_층형Converter.convert("지상4층"));
		assertEquals("4층401호", 건물현황_층형Converter.convert("4층401호"));
		assertEquals("지하층", 건물현황_층형Converter.convert("지층"));
		assertEquals("1층 102호", 건물현황_층형Converter
				.convert("4-7외 3필지 신구파인힐 1층 102호"));
		
		assertEquals("지하1층 102호", 건물현황_층형Converter
				.convert("4-7외 3필지 신구파인힐 지하1층 102호"));
		assertEquals("지하층 102호", 건물현황_층형Converter
				.convert("4-7외 3필지 신구파인힐 지층 102호"));
	}

	public void test2() {
		Pattern p = Pattern.compile("\\s+([^\\s]+층)");
		String src = "4-7외 3필지 신구파인힐 1층 102호";
		Matcher m = p.matcher(src);
		assertTrue(m.find());
	}
}
