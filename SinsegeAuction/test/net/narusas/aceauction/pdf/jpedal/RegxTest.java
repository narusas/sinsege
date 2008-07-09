package net.narusas.aceauction.pdf.jpedal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class RegxTest extends TestCase {
	public void test1() {
		assertPattern("3-2번가처분등기말소", "3-2");
		assertPattern("7번가압류등기말소", "7");
	}

	private void assertPattern(String src, String expected) {
		Pattern removePattern = Pattern.compile("(\\d+[-]*[\\d]*)번.*말소");
		Matcher m = removePattern.matcher(src);
		assertTrue(m.find());

		assertEquals(expected, m.group(1));
	}
	
	public void test2(){
		Pattern p = Pattern.compile("(.+\\s*지분[\\d\\.\\s]+분의[\\d\\.\\s]+중[\\d\\.\\s]+이전)");
		Matcher m = p.matcher("김정호,김숙희,김은기,성기양,김종운지분55.04분의211.232중18.368이전");
		assertTrue(m.find());
	}
}
