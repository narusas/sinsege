package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpException;

public class 대법원_사진Test extends TestCase {
	public void testFetch() throws HttpException, IOException {
		// http://www.courtauction.go.kr/au/hh100/hhCc00.jsp?bub_cd=000210&sa_no=20040130012890&ord_hoi=2&img_type=1&ser_no=1
		// PageFetcher f = new
		// PageFetcher("http://www.courtauction.go.kr/au/hh100/hhCc00.jsp?");
		// byte[] jpg =
		// f.fetchBinary("http://www.courtauction.go.kr/au/hh100/hhCc00.jsp?bub_cd=000210&sa_no=20040130012890&ord_hoi=2&img_type=1&ser_no=1",
		// null);
		// File file = new File("aaa.jpg");
		// NFile.write(file, jpg);

		대법원_사건_사진Fetcher fetcher = new 대법원_사건_사진Fetcher();
		List<File> file = fetcher.fetch("./", "000310", 20070130015455L);
		assertTrue(file.get(0).exists());
		assertTrue(file.get(0).length() > 0);
	}
}
