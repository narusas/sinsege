package net.narusas.aceauction.fetchers.pregoods;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpException;

public class BeadangListFetcherTest extends TestCase {
	public void testFetch() throws HttpException, IOException {

		String bub_cd = "000211";
		String bub_name = "서울동부지방법원";
		String jp_cd = "1001";
		String dam_nm = "경매1계";

		BeadangListFetcher fetcher = new BeadangListFetcher();

		List<SagunListItem> result = fetcher.fetch(bub_cd, bub_name, jp_cd,
				dam_nm);

		assertEquals(42, result.size());
	}

	public void testParseLink() {
		String src = "<a href=\"javascript:loadSaDetail( '20080130005794', '2008.07.21' )\" id=\"a1\">2008타경5794</a>";
		Pattern linkPattern = Pattern
				.compile("javascript:loadSaDetail\\( '(\\d+)', '([^']+)' \\)");
		Matcher m = linkPattern.matcher(src);
		assertTrue(m.find());
		assertEquals("20080130005794", m.group(1));
		assertEquals("2008.07.21", m.group(2));
	}
}
