package net.narusas.aceauction.fetchers;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class 스피드옥션FetcherTest extends TestCase {
	private 스피드옥션Fetcher fetcher;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		스피드옥션Fetcher.id = "smollkimbab";
		스피드옥션Fetcher.pass = "qw7611";

		fetcher = 스피드옥션Fetcher.getInstance();

	}

	public void testCreate() throws HttpException, IOException {
		assertEquals("smollkimbab", fetcher.getID());
		assertEquals("qw7611", fetcher.getPassword());
	}

	public void testLogin() throws IOException {
		String page = fetcher.fetch("/v2/");
		assertTrue(page.contains("smollkimbab"));
	}

	public void testSearch() throws IOException {
		String year = "2003";
		String no = "8078";

		PostMethod m = fetcher.post("/v2/info-list.htm", new NameValuePair[] {
				new NameValuePair("eventNo", year),
				new NameValuePair("eventNo2", no), });
		assertTrue((new String(m.getResponseBody(), "euc-kr"))
				.contains("서울특별시 중구 을지로6가"));
	}

	public void testResult() throws IOException {
		String eventYear = "2003";
		String eventNo = "8078";
		String no = "4";
		String courtNo = "A01";
		String chargeNo = "경매6계";
		String page = fetcher
				.fetch("/v2/info-result.htm?courtNo=A01&courtNo2=경매6계&eventNo1=2003&eventNo2=8078&objNo=6");
		assertTrue(page.contains("대지권미등기"));
		// System.out.println(page.con);
	}
}
