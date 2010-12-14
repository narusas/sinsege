package net.narusas.si.auction.fetchers;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.util.lang.NFile;
import junit.framework.TestCase;

public class GoodsDetailFetcherTest extends TestCase {
	private String html;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		html = NFile.getText(new File("fixture2/009_물건내역.html"));
	}

	public void testParse() {
		String src = "" + "	<li><p class=\"law_title\">1)년식 및 주행거리</p>\n" + "	<ul>\n"
				+ "		<li><span style=\"padding-left:10px;\">·작업시간 : 계기판상 약 49393hr<br />·사용연료 : 경유</span></li>\n"
				+ "	</ul></li>";

		Pattern p = Pattern.compile(
				"<li><p class=\"law_title\">(\\d+\\)\\s*[^<]+)</p>\\s*<ul>\\s*<li>\\s*<span[^>]+>(.*)</span>",
				Pattern.MULTILINE);
		Matcher m = p.matcher(src);
		assertTrue(m.find());

	}

}
