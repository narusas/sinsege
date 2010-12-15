package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.aceauction.model.���ÿܰǹ�;
import net.narusas.util.lang.NFile;

public class ��������ÿܰǹ�ParserTest extends TestCase {
	public void test��Ŀ����() throws IOException {
		String src = NFile.getText(new File("fixtures/���ǳ���_���ÿܹ���.htm"));
		String target = src.substring(src.indexOf("���ǳ��� </b>"), src
				.indexOf("����)<br>"));
		target = target.substring(target.indexOf("<table"), target
				.lastIndexOf("<table"));
		String[] slices = target.split("<table");
		// for (String slice : slices) {
		// System.out.println(slice);
		// }
		assertEquals("1", TableParser.getNextTDValueStripped(slices[1], "���ǹ�ȣ"));
		String temp = TableParser.getNextTDValueStripped(slices[1], ">���ÿܰǹ�<");
		String[] temp2 = temp.split("[\\n\\r]");
		Pattern p1 = Pattern.compile("\\(�뵵\\)([^\\(^<]+)");
		Pattern p2 = Pattern.compile("\\(����\\)([^\\(^<]+)");
		Pattern p3 = Pattern.compile("\\(����\\)([^\\(^<]+)");

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
		��������ÿܰǹ�Fetcher parser = new ��������ÿܰǹ�Fetcher();
		String src = NFile.getText(new File("fixtures/���ǳ���_���ÿܹ���.htm"));
		List<���ÿܰǹ�> blds = parser.parse(src);
		assertNotNull(blds);
		assertEquals(2, blds.size());
		assertEquals(new ���ÿܰǹ�(1, "����", "������ ����ƽ����", "1.4","addr",1), blds.get(0));
		assertEquals(new ���ÿܰǹ�(1, "���Ϸ���", "�ǳ��� �ǳ�����", "6.0","addr",1), blds.get(1));
	}
}
