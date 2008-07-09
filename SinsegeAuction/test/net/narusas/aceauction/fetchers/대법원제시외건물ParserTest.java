package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.aceauction.model.제시외건물;
import net.narusas.util.lang.NFile;

public class 대법원제시외건물ParserTest extends TestCase {
	public void test메커니즘() throws IOException {
		String src = NFile.getText(new File("fixtures/물건내역_제시외물건.htm"));
		String target = src.substring(src.indexOf("물건내역 </b>"), src
				.indexOf("주의)<br>"));
		target = target.substring(target.indexOf("<table"), target
				.lastIndexOf("<table"));
		String[] slices = target.split("<table");
		// for (String slice : slices) {
		// System.out.println(slice);
		// }
		assertEquals("1", TableParser.getNextTDValueStripped(slices[1], "물건번호"));
		String temp = TableParser.getNextTDValueStripped(slices[1], ">제시외건물<");
		String[] temp2 = temp.split("[\\n\\r]");
		Pattern p1 = Pattern.compile("\\(용도\\)([^\\(^<]+)");
		Pattern p2 = Pattern.compile("\\(구조\\)([^\\(^<]+)");
		Pattern p3 = Pattern.compile("\\(면적\\)([^\\(^<]+)");

		for (String s : temp2) {

			String ss = s.trim();
			if ("".equals(ss)) {
				continue;
			}
			Matcher m = p1.matcher(ss);
			m.find();
			m = p2.matcher(ss);
			m.find();
			m = p3.matcher(ss);
			m.find();
		}

	}

	public void testCreate() throws IOException {
		대법원제시외건물Fetcher parser = new 대법원제시외건물Fetcher();
		String src = NFile.getText(new File("fixtures/물건내역_제시외물건.htm"));
		List<제시외건물> blds = parser.parse(src);
		assertNotNull(blds);
		assertEquals(2, blds.size());
		assertEquals(new 제시외건물(1, "현관", "샷쉬조 프라스틱지붕", "1.4","addr",1), blds.get(0));
		assertEquals(new 제시외건물(1, "보일러실", "판넬조 판넬지붕", "6.0","addr",1), blds.get(1));
	}
}
