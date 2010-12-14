package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.util.lang.NFile;
import junit.framework.TestCase;

public class EventStatusReportFetcherTest extends TestCase {
	public void test부동산의현황() throws IOException {
		String txt = NFile.getText(new File("fixture2/040_사건현황조사서_부동산의현황.html"),"utf-8");

		System.out.println(new 사건현황조사서Fetcher().parse부동산의현황(txt));
	}
	
	
}
