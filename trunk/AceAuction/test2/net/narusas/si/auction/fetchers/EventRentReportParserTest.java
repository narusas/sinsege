package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import junit.framework.TestCase;
import net.narusas.si.auction.model.매각물건명세서;
import net.narusas.util.lang.NFile;

public class EventRentReportParserTest extends TestCase {
	public void testParse() throws IOException {
		String html = NFile.getText(new File("fixture2/026_현황조사서_Splited.html"), "euc-kr");

		사건임대차관계조사서Parser parser = new 사건임대차관계조사서Parser();
		Map<Integer, Collection<매각물건명세서>> list = parser.parse(html);
		System.out.println(list);
	}

}
