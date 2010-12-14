package net.narusas.si.auction.fetchers;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.util.lang.NFile;

public class SheetTest extends TestCase {
	public void testParseTable() throws Exception {
		String html = NFile.getText(new File("fixture2/034_물건내역_Sheet분석실패.html"),"euc-kr");
		Sheet sheet = Sheet.parse(html, "<caption>목록내역</caption>", true, true);
		assertEquals(3, sheet.headerSize());
		assertEquals("목록번호", sheet.getHeader(0));
		assertEquals("목록구분", sheet.getHeader(1));
		assertEquals("상세내역", sheet.getHeader(2));

		assertEquals(2, sheet.rowSize());
//
		assertEquals("1", sheet.valueAt(0, 0));
		assertEquals("토지", sheet.valueAt(0, 1));
		assertTrue(sheet.valueAt(0, 2).startsWith("대 297.5㎡"));
		assertTrue(sheet.valueAt(0, 2).endsWith("황성호 지분전부"));
		
		assertEquals("2", sheet.valueAt(1, 0));
		assertEquals("건물", sheet.valueAt(1, 1));
		assertTrue(sheet.valueAt(1, 2).startsWith("서울특별시 성북구 성북동1가 35-1"));
		assertTrue(sheet.valueAt(1, 2).endsWith("경매할지분 2분의 1 황성호 지분전부"));
		String 상세내역 = sheet.valueAt(1, 2);
		List<String> res = 부동산표시목록Builder.parseStructure(상세내역);
		System.out.println(res);
//
//		assertEquals("2", sheet.valueAt(1, 0));
//		assertEquals("서울특별시 관악구 봉천동 959-12", sheet.valueAt(1, 1));
//		assertEquals("2008.10.31 (연기)", sheet.valueAt(1, 2));
	}
	

}
