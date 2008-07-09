package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.aceauction.model.스피드옥션물건;
import net.narusas.util.lang.NFile;

public class 스피드옥션물건목록FetcherTest extends TestCase {
	public void testSample() throws IOException{
		String src = NFile.getText(new File("fixtures/speedy_search_list_2.htm"));
		스피드옥션물건목록Fetcher fetcher = new 스피드옥션물건목록Fetcher("2003", "8078");
		List<스피드옥션물건> res = fetcher.parse물건s(src);
		assertEquals(4, res.size());
	}
	
	public void testRegx() {
		String src = "onclick=\"open_result_page('info-result.htm','A01','경매6계','2003','8078','4',0);\">";
		 Pattern p = Pattern
			.compile("open_result_page\\('[^']+','(\\w+)','([^']+)','(\\d+)','(\\d+)','(\\d+)',(\\d+)\\)");;;

		 Matcher m = p.matcher(src);
		 assertTrue(m.find());
	}
}
