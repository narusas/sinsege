package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import net.narusas.aceauction.interaction.ProgressBar;
import net.narusas.aceauction.model.스피드옥션매각기일현황;
import net.narusas.aceauction.model.스피드옥션물건상세내역;
import net.narusas.util.lang.NFile;

public class 스피드옥션물건상세내용FetcherTest extends TestCase {
	private 스피드옥션물건상세내용Fetcher fetcher;

	@Override
	protected void setUp() throws Exception {
		ProgressBar.getInstance().setMaxProgress(10);

		fetcher = new 스피드옥션물건상세내용Fetcher();
	}

	// public void test1() throws HttpException, IOException {
	// System.out.println(fetcher.parse(fetcher.getPage("A01", "경매6계", "2003",
	// "8078", "4")));
	// }

	public void testParse() throws IOException {
		String src = NFile.getText(new File(
				"fixtures/speedy_search_result.html"));
		assertEquals("<STRONG><font color=366AB3>다세대(빌라)&nbsp;", TableParser
				.getNextTDValue(src, "물건종별"));
		assertEquals("다세대(빌라)", TableParser.strip(TableParser.getNextTDValue(
				src, "물건종별")));

		assertEquals("243.52 ㎡ (73.66평)", TableParser.getNextTDValueStripped(
				src, "건물면적"));
		assertEquals("토지/건물일괄매각", TableParser.getNextTDValueStripped(src,
				"매각대상"));

		스피드옥션물건상세내역 detail = fetcher.parse(src);

		// System.out.println(detail);
		// System.out.println(fetcher.parse(NFile.getText(new File(
		// "fixtures/speedy_search_result_2.html"))));
		// System.out.println(fetcher.parse(NFile.getText(new File(
		// "fixtures/speedy_search_result_P01.html"))));
	}

	public void testParse2() throws IOException {
		String src = NFile.getText(new File(
				"fixtures/speedy_search_result_제시외건물.html"));

		스피드옥션물건상세내역 detail = fetcher.parse(src);

		// System.out.println(detail);
	}

	public void testParse매각이일현황() throws IOException {
		String src = NFile.getText(new File("fixtures/speedy_RESULT_02.htm"));

		스피드옥션물건상세내역 detail = fetcher.parse(src);
		스피드옥션매각기일현황 state = new 스피드옥션매각기일현황(detail.get매각기일현황(), "보증금");
		System.out.println(state);
	}
}
