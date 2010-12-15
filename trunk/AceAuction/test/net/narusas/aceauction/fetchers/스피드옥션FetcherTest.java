package net.narusas.aceauction.fetchers;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class ���ǵ����FetcherTest extends TestCase {
	private ���ǵ����Fetcher fetcher;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		���ǵ����Fetcher.id = "smollkimbab";
		���ǵ����Fetcher.pass = "qw7611";

		fetcher = ���ǵ����Fetcher.getInstance();

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
				.contains("����Ư���� �߱� ������6��"));
	}

	public void testResult() throws IOException {
		String eventYear = "2003";
		String eventNo = "8078";
		String no = "4";
		String courtNo = "A01";
		String chargeNo = "���6��";
		String page = fetcher
				.fetch("/v2/info-result.htm?courtNo=A01&courtNo2=���6��&eventNo1=2003&eventNo2=8078&objNo=6");
		assertTrue(page.contains("�����ǹ̵��"));
		// System.out.println(page.con);
	}
}
