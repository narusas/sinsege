package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.aceauction.model.����;

import org.apache.commons.httpclient.HttpException;

public class GGAuctionFetcherTest extends TestCase {
	// public void testFetch���ε() throws HttpException, IOException,
	// InterruptedException {
	// ��������Fetcher fetcher = new ��������Fetcher();
	// ���� court = ����.court("000212");
	// File f = File.createTempFile("�ǹ����ε", ".pdf");
	// NFile.write(f, fetcher.fetch�ǹ����ε(court, "2005", "28294"));
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
	// // NFile.write(new File("pdf2.pdf"), fetcher.fetch�������ε(court, "2005",
	// // "28294"));
	// // NFile.write(new File("pdf3.pdf"), fetcher.fetch���ε(court, "2005",
	// // "28294"));
	// }

	public void test����() throws HttpException, IOException {
		�����ɾ�Fetcher fetcher = new �����ɾ�Fetcher();
		���� court = ����.findByName("000210");
		List<File> mulguns = fetcher.fetch���ǻ���(court, "2005", "28294");
		assertNotNull(mulguns);
		assertEquals(3, mulguns.size());
	}

}
