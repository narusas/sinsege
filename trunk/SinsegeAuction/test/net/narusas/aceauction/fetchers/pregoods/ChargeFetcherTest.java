package net.narusas.aceauction.fetchers.pregoods;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.aceauction.model.����;

import org.apache.commons.httpclient.HttpException;

public class ChargeFetcherTest extends TestCase {
	public void testFetch() throws HttpException, IOException {
		ChargeFetcher fetcher = new ChargeFetcher();
		List<Charge> result = fetcher.fetch(����.get(1));
		assertEquals(9, result.size());
	}

	public void testParseData() {
		String src = "<option value=\"1001\" >���1��</option>\n<option value=\"1004\" >���4��</option>";
		Pattern p = Pattern
				.compile("<option value=\"(\\d+)\" >([^<]+)</option>");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("1001", m.group(1));
		assertEquals("���1��", m.group(2));

		assertTrue(m.find());
		assertEquals("1004", m.group(1));
		assertEquals("���4��", m.group(2));
	}

}

class Charge {

	public Charge(String code, String name) {
	}
}