package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

import junit.framework.TestCase;

public class JedgeFetcherTest extends TestCase {
	public void testRegx() {
		String src = "<frame style=\"padding:0 20px;\" src=\"http://210.98.146.20/main.asp?bub_cd=000210&amp;sa_no=20070130025941&amp;ord_hoi=1\" name=\"mainFrame\" scrolling=\"NO\" >";
		Pattern p = Pattern.compile("<frame style=[^>]* src=\"([^\"]+)\"[^>]+");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		String pageUrl = HTMLUtils.converHTMLSpecialChars(m.group(1));
		assertEquals("http://210.98.146.20/main.asp?bub_cd=000210&sa_no=20070130025941&ord_hoi=1", pageUrl);

		m = Pattern.compile("(http://[^/]+)(/.*)").matcher(pageUrl);
		assertTrue(m.find());
		assertEquals("http://210.98.146.20", m.group(1));

		m = Pattern.compile("document.MainFrame.location\\s*=\\s*'([^']+)").matcher(
				" document.MainFrame.location ='/file/300071/000210-20070130025941-1-0000.pdf';");
		assertTrue(m.find());
		assertEquals("/file/300071/000210-20070130025941-1-0000.pdf", m.group(1));

	}

	public void testURL() throws HttpException, IOException {
		
		
//		String url = "http://210.98.146.20/file/401675/이룸09-0212-14.pdf";
//		String fileName = url.substring(url.lastIndexOf('/')+1);
//		String pre = url.substring(0, url.lastIndexOf('/')+1);
////		System.out.println(fileName);
//		String encodedFileName = URLEncoder.encode(fileName, "euc-kr");
//		
//		PageFetcher fetcher = new PageFetcher("");
//		System.out.println(pre+encodedFileName);
//		fetcher.downloadBinary(pre+encodedFileName, new File("abc"));
	}
	
	public void testPage() throws HttpException, IOException {
//		String url = "http://210.98.146.20/main.asp?bub_cd=000210&sa_no=20080130006480&ord_hoi=1";
//		PageFetcher fetcher = new PageFetcher("");
///**
//		GET /main.asp?bub_cd=000210&sa_no=20080130006480&ord_hoi=1 HTTP/1.1
//				Host: 210.98.146.20
//				User-Agent: Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; ko; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3
//				Accept: text/html,application/xhtml+xml,application/xml;q=0.9,**;q=0.8
//				Accept-Language: ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3
//				Accept-Encoding: gzip,deflate
//				Accept-Charset: EUC-KR,utf-8;q=0.7,*;q=0.7
//				Keep-Alive: 115
//				Connection: keep-alive
//				Referer: http://www.courtauction.go.kr/RetrieveRealEstSaGamEvalSeo.laf
//*/
//		
//		
//		System.out.println(fetcher.fetch(url, new NameValuePair[] {//
//				new NameValuePair("Host", "210.98.146.20"), //
//				new NameValuePair("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; ko; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3"), //
//				new NameValuePair("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"), //
//				new NameValuePair("Accept-Language", "ko-kr,ko;q=0.8,en-us;q=0.5,en;q=0.3"), //
//				new NameValuePair("Accept-Encoding", "210.98.146.20"), //
//				new NameValuePair("Accept-Charset", "EUC-KR,utf-8;q=0.7,*;q=0.7"), //
//				new NameValuePair("Keep-Alive", "115"), //
//				new NameValuePair("Connection", "keep-alive"), //
//				new NameValuePair("Referer", "http://www.courtauction.go.kr/RetrieveRealEstSaGamEvalSeo.laf") //
//		}));
		
		
	}
	
	public void testRegx2() {
		Matcher m = Pattern.compile("http://([^/$]+)").matcher("http://11.22.33.44");
		assertTrue(m.find());
		assertEquals("11.22.33.44", m.group(1));
	}
}
