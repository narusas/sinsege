package net.narusas.aceauction.fetchers.onara;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class OnaraLandFetcherTest extends TestCase {
	public void testLandInfo() {
		String src = "<data>����;3011000000\n     </data>";
		Pattern p = Pattern.compile("<data>([^>]*)</data>");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("����;3011000000", m.group(1).trim());
	}

	public void testDefuaultMapInfo() {
		String src = "main_map[16][31] = \"4182000000|\";	//����";
		Pattern p = Pattern.compile("\\\"(\\d+)");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("4182000000", m.group(1).trim());
	}

}
