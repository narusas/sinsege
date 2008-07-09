package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.util.lang.NFile;

public class 스피드옥션사진FetcherTest extends TestCase {
	// <form name=info_list2 method=post action='' target='_self'>
	// <input type=hidden name=courtNo value='A04'>
	// <input type=hidden name=courtNo2 value='경매1계'>
	// <input type=hidden name=eventNo1 value='2005'>
	// <input type=hidden name=eventNo2 value='28294'>
	// <input type=hidden name=objNo value='1'>
	// <input type=hidden name=selldate value='20070402'>
	// <input type=hidden name=map_selldate value='2007-04-02 10:00'>
	// <input type=hidden name=obj_list value=',1'>
	// <input type=hidden name=addr_text value='서울특별시강서구화곡동56-441'>
	// <input type=hidden name=view_gubun value=''>
	// <input type=hidden name=r_url value='/v2/info-result.htm'>
	// <input type=hidden name=serino value=''>
	// <input type=hidden name=bid_choice_cnt value='1'>
	// <input type=hidden name=bid_choice_flag value='inforesult'>
	// <input type=hidden name=bid_choice0 value='A04-2005-28294-1'>
	// <input type=hidden name=objuse value='다세대(빌라)'>
	// <input type=hidden name=gamjungprice value='100000000.00'>
	// <input type=hidden name=lowprice value='100000000.00'>
	// <input type=hidden name=lowpricerate value='100'>
	// </form>

	// info-result-zoom.htm

	// function zoom_view(send_file,view_gubun)
	// {
	// var form = document.info_list2;
	// form.view_gubun.value = view_gubun;
	// var
	// style="top=0,left=0,width=1000,height=680,scrollbars=yes,resizable=yes,border=0";
	// window.open(send_file,'zoom',style);
	// form.action=send_file;
	// form.target="zoom";
	//		
	// form.submit();
	// }

	public void testParseParams() throws IOException {
		스피드옥션ZoomPageFetcher fetcher = new 스피드옥션ZoomPageFetcher();
		String src = NFile.getText(new File("fixtures/speedy_search_result.html"));
		fetcher.init(src);
		assertEquals("A01", fetcher.getParam("courtNo"));
		assertEquals("경매6계", fetcher.getParam("courtNo2"));
		assertEquals("A01-2003-28874-1", fetcher.getParam("bid_choice0"));
		assertEquals("서울특별시강남구역삼동728-6", fetcher.getParam("addr_text"));
	}

	public void testAccess() throws IOException {
		스피드옥션ZoomPageFetcher fetcher = new 스피드옥션ZoomPageFetcher();
		String src = NFile.getText(new File("fixtures/speedy_search_result.html"));
		fetcher.init(src);
		String html = fetcher.get사진page();
//		System.out.println(html);
		String[] urls = fetcher.parse사진링크(html);
		assertNotNull(urls);
		assertTrue(urls.length>0);
		for (String url : urls) {
			System.out.println(url);
		}
	}

	public void testRegx() {
		String src = "<input type=hidden name=courtNo value='A01'>";
		Pattern p = Pattern.compile("<input[^>]+name=(\\w+) value='([^']*)'>");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("courtNo", m.group(1));
		assertEquals("A01", m.group(2));
	}

	public void testRegx2() {
		String src = "fn_view_img('http://www.speedauction.co.kr/courtfileroot/gamjungimg/A01/2003/28874/4_1170297583.gif');\">"
				+ "<img src='http://www.speedauction.co.kr/courtfileroot/gamjungimg/A01/2003/28874/4_1170297583.gif' onerror=\"this.src='img/noimg02.gif'\" height=80 width=100>"
				+ "</a></td>";
		
		Pattern p = Pattern.compile("(http://www.speedauction.co.kr/courtfileroot/gamjungimg/[^'^\"]+)");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("http://www.speedauction.co.kr/courtfileroot/gamjungimg/A01/2003/28874/4_1170297583.gif", m.group(1));
	}
}
