package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import net.narusas.util.lang.NFile;

public class ���ǵ���ǵ��εFetcherTest extends TestCase {
	public void testFetch() throws IOException {
		���ǵ����ZoomPageFetcher fetcher = new ���ǵ����ZoomPageFetcher();
		String src = NFile.getText(new File("fixtures/speedy_search_result_��ΰ�.html"));
		fetcher.init(src);
		System.out.println(fetcher.get�������εpage());
	}
}
