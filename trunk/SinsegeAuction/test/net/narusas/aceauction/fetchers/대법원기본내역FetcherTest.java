package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import net.narusas.aceauction.model.사건;
import net.narusas.util.lang.NFile;

public class 대법원기본내역FetcherTest extends TestCase {
	public void test1() throws IOException {
		String src = NFile.getText(new File("fixtures/기본내역.htm"));
		대법원기본내역Fetcher fetcher = new 대법원기본내역Fetcher();
		사건 s = new 사건(null, null);
		fetcher.parse(src, s);
		assertEquals("부동산임의경매", s.get사건명());
		assertEquals("[주식회사가인엔지니어링]", s.get소유자());
		assertEquals("[주식회사만승기업]", s.get채무자());
		assertEquals("[주식회사국민은행]", s.get채권자());

	}

	public void test2() {
		// 대법원기본내역Fetcher fetcher = new 대법원기본내역Fetcher();
		// System.out.println(fetcher.getPage("000210", "경매3계",
		// "1006","2007.03.27","20050130007062"));
	}

	public void test3() throws IOException {
		String src = NFile.getText(new File("fixtures/기본내역_3.htm"));
		대법원기본내역Fetcher fetcher = new 대법원기본내역Fetcher();
		사건 s = new 사건(null, null);
		 fetcher.parse(src, s);
		assertEquals("[조순이]", s.get소유자());
		assertEquals("[최범수]", s.get채무자());
		assertEquals("[한국자산관리공사]", s.get채권자());
	}

}
