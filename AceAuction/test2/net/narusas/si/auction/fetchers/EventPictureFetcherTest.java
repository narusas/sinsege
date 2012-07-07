package net.narusas.si.auction.fetchers;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.util.lang.NFile;

public class EventPictureFetcherTest extends TestCase {
	private String html;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		html = NFile.getText(new File("fixture2/017_물건내역_사진다수.html"), "utf-8");
	}
	
	public void testParseMaxPage() {
		Pattern p = Pattern.compile("goPage\\('(\\d+)'\\)", Pattern.MULTILINE);
		Matcher m = p.matcher(html);
		assertTrue(m.find());
		assertEquals("2", m.group(1));
		assertTrue(m.find());
		assertEquals("3", m.group(1));
		assertTrue(m.find());
		assertEquals("3", m.group(1));
		
		assertEquals(3, new 사건사진Fetcher().parseLastPage(html));
	}
	
	public void testParseImgURL() {
		Pattern p = Pattern.compile("<img src=\"(/DownFront[^\"]+)", Pattern.MULTILINE);
		Matcher m = p.matcher(html);
		assertTrue(m.find());
		assertEquals("/DownFront?spec=default&amp;dir=kj/2007/1116&amp;filename=B000210200701300340751.jpg&amp;downloadfilename=B000210200701300340751.jpg", m.group(1));
		
		assertEquals("/DownFront?spec=default&amp;dir=kj/2007/1116&amp;filename=B000210200701300340751.jpg&amp;downloadfilename=B000210200701300340751.jpg", 
				new 사건사진Fetcher().parseImgUrl(html));
	}
}
