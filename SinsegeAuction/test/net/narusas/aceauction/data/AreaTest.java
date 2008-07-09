package net.narusas.aceauction.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.aceauction.util.AreaDB;

public class AreaTest extends TestCase {
	public void testRegx() {
		String src = "_js_area[0] = new Array()\n" + "_js_area[0][0] = '7551'\n"
				+ "_js_area[0][1] = '30ºí·°8-1·ÔÆ®Ã¢Áø¾ÆÆ®ºô'\n" + "_js_area[0][2] = '416'";
		Pattern p = Pattern.compile("_js_area\\[(\\d+)\\]\\[0\\] = '(\\d+)'\\s*"
				+ "_js_area\\[\\d+\\]\\[1\\] = '([^']+)'\\s*" + "_js_area\\[\\d+\\]\\[2\\] = '(\\d+)'");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("0", m.group(1));
		assertEquals("7551", m.group(2));
		assertEquals("30ºí·°8-1·ÔÆ®Ã¢Áø¾ÆÆ®ºô", m.group(3));
		assertEquals("416", m.group(4));
	}

	public void testSet() throws Exception {
		AreaDB db = new AreaDB();
		db.update();
	}
}
