package net.narusas.si.auction.fetchers;

import java.io.IOException;

import junit.framework.TestCase;
import net.narusas.si.auction.fetchers.스피드옥션ZoomPageFetcher.등기부등본Links;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class SpeedAuctionLoginTest extends TestCase {
	public void testLogin() throws HttpException, IOException {
		스피드옥션Fetcher f = 스피드옥션Fetcher.getInstance();
		f.login();
		f.get("/v2/info_result_switch.php?" + "courtNo=A01&" + "courtNo2=경매3계&" + "eventNo1=2007&"
				+ "eventNo2=11096&" + "objNo=1&" + "speed_usagecode=21");
		PostMethod m = f
				.post("/v2/info-result.htm",
						new NameValuePair[] { new NameValuePair("courtNo", "A01"), //
								new NameValuePair("courtNo2", "경매3계"), //
								new NameValuePair("eventNo1", "2007"),//
								new NameValuePair("eventNo2", "11096"),//
								new NameValuePair("objNo", "1"),//
						// new NameValuePair("speed_usagecode", "21"),//
						},
						new String[][] { {
								"Referer",
								"http://www.speedauction.co.kr/v2/info_result_switch.php?courtNo=A01&eventNo1=2007&eventNo2=28674&objNo=1&speed_usagecode=41" } });

		String html = new String(m.getResponseBody(), "euc-kr");

		스피드옥션ZoomPageFetcher ff = new 스피드옥션ZoomPageFetcher();
		등기부등본Links links = ff.parse등기부등본Link(html);
		System.out.println(links.get건물등기부등본PDFLink());
		System.out.println(links.get토지등기부등본PDFLink());
	}

	// public void testFetchDetail() throws HttpException, IOException {
	// 스피드옥션물건상세내용Fetcher fetcher = new 스피드옥션물건상세내용Fetcher();
	// fetcher.prepare();
	// String src = fetcher.getPage("A01", "경매3계", 2007, 11096, "1");
	// System.out.println(src);
	// }
}
