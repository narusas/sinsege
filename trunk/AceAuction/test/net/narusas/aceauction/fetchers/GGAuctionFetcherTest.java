package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.aceauction.model.법원;

import org.apache.commons.httpclient.HttpException;

public class GGAuctionFetcherTest extends TestCase {
	// public void testFetch등기부등본() throws HttpException, IOException,
	// InterruptedException {
	// 지지옥션Fetcher fetcher = new 지지옥션Fetcher();
	// 법원 court = 법원.court("000212");
	// File f = File.createTempFile("건물등기부등본", ".pdf");
	// NFile.write(f, fetcher.fetch건물등기부등본(court, "2005", "28294"));
	// Process ps = Runtime.getRuntime().exec(
	// "c:\\Program Files\\Adobe\\Acrobat 7.0\\Reader\\AcroRd32.exe " +
	// f.getAbsolutePath());
	//
	// InputStream in = ps.getInputStream();
	// while (true) {
	// int r = in.read();
	// if (r == -1) {
	// break;
	// }
	// System.out.print((char) r);
	// }
	// // NFile.write(new File("pdf2.pdf"), fetcher.fetch토지등기부등본(court, "2005",
	// // "28294"));
	// // NFile.write(new File("pdf3.pdf"), fetcher.fetch등기부등본(court, "2005",
	// // "28294"));
	// }

	public void test사진() throws HttpException, IOException {
		인포케어Fetcher fetcher = new 인포케어Fetcher();
		법원 court = 법원.findByName("000210");
		List<File> mulguns = fetcher.fetch물건사진(court, "2005", "28294");
		assertNotNull(mulguns);
		assertEquals(3, mulguns.size());
	}

}
