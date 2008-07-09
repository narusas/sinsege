package net.narusas.aceauction.data;

import junit.framework.TestCase;

public class 금액ConverterTest extends TestCase {
	public void test1() {
		assertEquals("1", 금액Converter.convert("1원"));
		assertEquals("", 금액Converter.convert(""));
		assertEquals("", 금액Converter.convert(null));
		
		assertEquals("알수없음", 금액Converter.convert("알수없음"));
		
		assertEquals("1000", 금액Converter.convert("1,000원"));
		assertEquals("1", 금액Converter.convert("금1원"));
		assertEquals("1", 금액Converter.convert("월 금1원"));
		
		assertEquals("이십", 금액Converter.convert("이십원"));
		
		assertEquals("1234567891235", 금액Converter.convert("금 일조 2천3백4십5억 6천7백8십9만 일천이백삼십오원"));
		
		
		assertEquals("25000000", 금액Converter.convert("2,500만원"));
		
		assertEquals("370000000", 금액Converter.convert("금삼억칠천만원정"));
		
		assertEquals("375000000", 금액Converter.convert("금삼억칠천오백만원정"));
		assertEquals("375200000", 금액Converter.convert("금삼억칠천오백이십만원정"));
		assertEquals("375290000", 금액Converter.convert("금삼억칠천오백이십구만원정"));
		
		assertEquals("195000000", 금액Converter.convert("금195,000,000원정"));
		
		
		
	}
}