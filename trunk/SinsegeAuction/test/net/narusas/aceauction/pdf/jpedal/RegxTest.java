package net.narusas.aceauction.pdf.jpedal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class RegxTest extends TestCase {
	public void test1() {
		assertPattern("3-2����ó�е�⸻��", "3-2");
		assertPattern("7�����з���⸻��", "7");
	}

	private void assertPattern(String src, String expected) {
		Pattern removePattern = Pattern.compile("(\\d+[-]*[\\d]*)��.*����");
		Matcher m = removePattern.matcher(src);
		assertTrue(m.find());

		assertEquals(expected, m.group(1));
	}
	
	public void test2(){
		Pattern p = Pattern.compile("(.+\\s*����[\\d\\.\\s]+����[\\d\\.\\s]+��[\\d\\.\\s]+����)");
		Matcher m = p.matcher("����ȣ,�����,������,�����,����������55.04����211.232��18.368����");
		assertTrue(m.find());
	}
}
