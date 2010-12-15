package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import net.narusas.util.lang.NFile;

public class 스피드옥션등기부등본FetcherTest extends TestCase {
	public void testFetch() throws IOException {
		스피드옥션ZoomPageFetcher fetcher = new 스피드옥션ZoomPageFetcher();
		String src = NFile.getText(new File("fixtures/speedy_search_result_등본두개.html"));
		fetcher.init(src);
		System.out.println(fetcher.get토지등기부등본page());
	}
}
