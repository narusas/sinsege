package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.���;
import net.narusas.util.lang.NFile;

import org.htmlparser.util.ParserException;

public class ��������FetcherTest extends TestCase {

	private ��������Fetcher fetcher;

	private String fixture01;

	private String fixture_slice;

	protected void setUp() throws Exception {
		super.setUp();
		fetcher = new ��������Fetcher(����.findByCode("�����߾��������"),//
				new Date(2007 - 1900, 3 - 1, 20),// 
				1006, "���6��");
		fixture01 = NFile.getText(new File("fixtures/mulgunlist.html"));
		fixture_slice = NFile.getText(new File("fixtures/slice.html"));
	}

	// public void test1() {
	// SagunFetcher fetcher = new SagunFetcher(CourtTable.code("�����߾��������"),
	// "2007.03.20", "1006", "���6��");
	// assertNotNull(fetcher.getSaguns());
	// }

	// public void testGetPage() throws HttpException, IOException {
	// // 5������������ [5]�� ��ũ�� ����� �Ѵ�.
	// String page = fetcher.getPage(5);
	// assertTrue(page.contains("<b>[5]</b>"));
	// assertTrue(page
	// .contains("<A HREF='javascript:goMove(6);' id='a1'> [6] </A>"));
	// }

	// public void testCollectPages() throws HttpException, IOException {
	// String[] pages = fetcher.getPages();
	// assertEquals(9, pages.length);
	// }

	public void testBasicInfo() {
		assertEquals("2007.03.20 10:00", fetcher.parseDateInfo(fixture01));
		assertEquals("��Ź���", fetcher.parseLocationInfo(fixture01));
	}

	public void testAll() throws ParserException, IOException {
		List<���> s = fetcher.getSaguns();
		System.out.println(s.get(0));
	}

}
