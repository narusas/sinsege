package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import net.narusas.si.auction.fetchers.사건기일내역Fetcher;
import net.narusas.si.auction.model.사건;
import net.narusas.util.lang.NFile;
import junit.framework.TestCase;

public class DateListFetcherTest extends TestCase {
	public void testSome() throws IOException {
		사건  event = new  사건();
		사건기일내역Fetcher f = new 사건기일내역Fetcher();
		String html = NFile.getText(new File("fixture2/082_기일내역_결과처리.html"));
		f.parse(event, html);
	}
	
	public void testIgnoreResut() {
		String src = "매각\n(312,178,000원)";
		assertTrue(isIgnorable(src));
	}
	
	private boolean isIgnorable(String 기일결과) {
		return 기일결과.contains("매각") && 기일결과.contains("(") && 기일결과.contains("원") &&기일결과.contains(")");
	}

}
