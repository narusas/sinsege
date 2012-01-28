package net.narusas.si.auction.pdf.atested;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class RegexTest extends TestCase {
	public void testCurrency() {
		String src1 = "채권최고액 일본국법화 금70,000,000엔";
		Matcher m = Pattern.compile("([\\d,\\s]+\\s*(원|엔))").matcher(src1);
		assertTrue(m.find());
		System.out.println(m.group(1));
	}
	
	public void testDeatilSearch() {
		String src ="javascript:detailSearch('164669','182094','626675','5201010R000011','공고중','','kamco','1');";
		Pattern pattern = Pattern.compile("detailSearch\\('(\\d*)','(\\d*)','(\\d*)','[^']*','[^']*','[^']*','[^']*','(\\d+)'");
		Matcher m = pattern.matcher(src);
		assertTrue(m.find());
	}
	
	public void test괄호숫자() {
		String text = "(1)임의경매개시결정";
		Pattern p = Pattern.compile("\\(\\d+\\)(.*)");
		Matcher m = p.matcher(text);
		assertTrue(m.find());
		assertEquals("임의경매개시결정", m.group(1));
	}
}
