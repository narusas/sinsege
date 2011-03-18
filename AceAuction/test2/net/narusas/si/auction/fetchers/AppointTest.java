package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.사건;
import net.narusas.util.lang.NFile;

import org.junit.Test;

public class AppointTest  {
//	public void test1() throws IOException {
//		사건기일내역Fetcher f = new 사건기일내역Fetcher();
//		
//		String html = f.fetch("창원지방법원", "20080130018280");
//		System.out.println(html);
//	}
	@Test
	public void testParse() throws IOException {
		
		String html = NFile.getText(new File("fixture2/035_기일내역_결과.html"), "euc-kr");
		사건기일내역Fetcher f = new 사건기일내역Fetcher();
		LinkedList<물건> goodsList = new LinkedList<물건>();
		물건 goods = new 물건();
		goods.set물건번호(1);
		goodsList.add(goods);
		사건 s = new 사건();
		
		s.set물건목록(goodsList);
		f.parse(s, html);
		System.out.println(goods.get기일결과());
		System.out.println(goods.get최저가());
		System.out.println(goods.get기일날자());
		
		
	}
}
