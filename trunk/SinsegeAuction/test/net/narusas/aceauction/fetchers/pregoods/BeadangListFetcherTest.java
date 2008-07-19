package net.narusas.aceauction.fetchers.pregoods;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpException;

import net.narusas.aceauction.fetchers.PageFetcher;
import junit.framework.TestCase;

public class BeadangListFetcherTest extends TestCase {
	public void testFetch() throws HttpException, IOException {
//		PageFetcher fetcher = new PageFetcher(
//		"http://www.courtauction.go.kr/au/SuperServlet?target_command=");

		
		PageFetcher fetcher = new PageFetcher(
				"http://www.courtauction.go.kr/au/SuperServlet?target_command=");

		String bub_cd = "00211";
		String bub_name = "���ﵿ���������";
		String jp_cd = "1001";
		String dam_nm = "���1��";

		
		fetcher.fetch("au.command.aua.A312SearchCommand&bub_cd="+bub_cd);
		
		String cookieName = "������-���䱸�������";
		
		fetcher.setCookie( cookieName +'='+bub_cd);
		
		//URLEncoder.encode(, "euc-kr")
		String query = "bub_cd=" + URLEncoder.encode(bub_cd , "euc-kr")//
				+ "&" + "bub_nm=" + URLEncoder.encode(bub_name, "euc-kr")//
				+ "&" + "jp_cd=" + URLEncoder.encode(jp_cd, "euc-kr")//
				+ "&" + "dam_nm=" + URLEncoder.encode(dam_nm, "euc-kr");
		
		
		
		String page = fetcher.fetch("au.command.aua.A312ListCommand&"+query);
		System.out.println(page);
		
	}
}
