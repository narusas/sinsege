package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpException;

public class ����CollectorTest extends TestCase {
	public void testCollect����() throws HttpException, IOException {
		����Collector collector = new ����Collector();
		File gamjung = new File("fixtures/�����򰡼�_ȸ��ΰ�.pdf");
		List<File> files = collector.collect(gamjung, "000310", 20070130015455L);
		assertTrue(files.size() > 0);
	}
}
