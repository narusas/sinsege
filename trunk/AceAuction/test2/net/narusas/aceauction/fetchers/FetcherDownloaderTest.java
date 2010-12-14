package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpException;

import net.narusas.si.auction.fetchers.PageFetcher;
import junit.framework.TestCase;

public class FetcherDownloaderTest extends TestCase {
	public void testSimepl() throws HttpException, IOException {
		// PageFetcher f = new PageFetcher("");
		// String url= "http://210.98.146.20/binary/1/400474/류호광2010 - 79.pdf";
		// // System.out.println(url);
		// // String expected =
		// "http://210.98.146.20/binary/1/400474/%EB%A5%98%ED%98%B8%EA%B4%912010%20-%2079.pdf";
		// // assertEquals(expected, url);
		// f.downloadBinary(url, new File("a.pdf"));
		String url = "http://210.98.146.20/binary/1/401725/미도 제1-1005-10.pdf";

		String expected = "http://210.98.146.20/binary/1/401725/%EB%AF%B8%EB%8F%84%20%EC%A0%9C1-1005-10.pdf";

		String fileName = url.substring(url.lastIndexOf('/') + 1);
		String pre = url.substring(0, url.lastIndexOf('/') + 1);
		String encodedFileName = URLEncoder.encode(fileName, "utf-8");

		String u = pre + encodedFileName;
//		u = urlEncode(u);
		u = u.replace("+", "%20");

		assertEquals(expected, u);

	}

	private String urlEncode(String url) throws UnsupportedEncodingException {
		String head = url.substring(0, url.lastIndexOf("/"));
		String tail = url.substring(url.lastIndexOf("/") + 1);

		return head + "/" + URLEncoder.encode(tail, "utf-8");
	}
}
