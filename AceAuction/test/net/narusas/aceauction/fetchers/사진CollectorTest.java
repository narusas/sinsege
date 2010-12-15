package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpException;

public class 사진CollectorTest extends TestCase {
	public void testCollect사진() throws HttpException, IOException {
		사진Collector collector = new 사진Collector();
		File gamjung = new File("fixtures/감정평가서_회사로고.pdf");
		List<File> files = collector.collect(gamjung, "000310", 20070130015455L);
		assertTrue(files.size() > 0);
	}
}
