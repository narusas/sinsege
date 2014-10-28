package net.narusas.si.auction.builder;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class 현장조사서이미지BatchTest {

	@Test
	public void test() {
		Pattern p = Pattern.compile("name=\"jiwonNm\" value=\"([^\"]+)",Pattern.MULTILINE);
		Matcher m = p.matcher("<input type=\"hidden\" name=\"jiwonNm\" value=\"수원지방법원\" />");
		assertTrue(m.find());
		assertEquals("수원지방법원", m.group(1));
	}
	@Test
	public void test2() {
		//
		
		Pattern p = Pattern.compile("(/DownFront[^\"]+)",Pattern.MULTILINE);
		Matcher m = p.matcher("<img src=\"/DownFront?spec=default&amp;dir=kj/2011/0327&amp;filename=B000210201101300055867.jpg&amp;downloadfilename=B000210201101300055867.jpg\" alt=\"전경도\" />");
		assertTrue(m.find());
		assertEquals("/DownFront?spec=default&amp;dir=kj/2011/0327&amp;filename=B000210201101300055867.jpg&amp;downloadfilename=B000210201101300055867.jpg", m.group(1));
	}

}
