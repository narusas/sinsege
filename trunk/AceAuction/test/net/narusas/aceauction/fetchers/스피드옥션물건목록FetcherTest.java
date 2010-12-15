package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.aceauction.model.���ǵ���ǹ���;
import net.narusas.util.lang.NFile;

public class ���ǵ���ǹ��Ǹ��FetcherTest extends TestCase {
	public void testSample() throws IOException{
		String src = NFile.getText(new File("fixtures/speedy_search_list_2.htm"));
		���ǵ���ǹ��Ǹ��Fetcher fetcher = new ���ǵ���ǹ��Ǹ��Fetcher("2003", "8078");
		List<���ǵ���ǹ���> res = fetcher.parse����s(src);
		assertEquals(4, res.size());
	}
	
	public void testRegx() {
		String src = "onclick=\"open_result_page('info-result.htm','A01','���6��','2003','8078','4',0);\">";
		 Pattern p = Pattern
			.compile("open_result_page\\('[^']+','(\\w+)','([^']+)','(\\d+)','(\\d+)','(\\d+)',(\\d+)\\)");;;

		 Matcher m = p.matcher(src);
		 assertTrue(m.find());
	}
}
