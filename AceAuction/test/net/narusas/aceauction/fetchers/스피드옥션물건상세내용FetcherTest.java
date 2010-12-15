package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import net.narusas.aceauction.interaction.ProgressBar;
import net.narusas.aceauction.model.���ǵ���ǸŰ�������Ȳ;
import net.narusas.aceauction.model.���ǵ���ǹ��ǻ󼼳���;
import net.narusas.util.lang.NFile;

public class ���ǵ���ǹ��ǻ󼼳���FetcherTest extends TestCase {
	private ���ǵ���ǹ��ǻ󼼳���Fetcher fetcher;

	@Override
	protected void setUp() throws Exception {
		ProgressBar.getInstance().setMaxProgress(10);

		fetcher = new ���ǵ���ǹ��ǻ󼼳���Fetcher();
	}

	// public void test1() throws HttpException, IOException {
	// System.out.println(fetcher.parse(fetcher.getPage("A01", "���6��", "2003",
	// "8078", "4")));
	// }

	public void testParse() throws IOException {
		String src = NFile.getText(new File(
				"fixtures/speedy_search_result.html"));
		assertEquals("<STRONG><font color=366AB3>�ټ���(����)&nbsp;", TableParser
				.getNextTDValue(src, "��������"));
		assertEquals("�ټ���(����)", TableParser.strip(TableParser.getNextTDValue(
				src, "��������")));

		assertEquals("243.52 �� (73.66��)", TableParser.getNextTDValueStripped(
				src, "�ǹ�����"));
		assertEquals("����/�ǹ��ϰ��Ű�", TableParser.getNextTDValueStripped(src,
				"�Ű����"));

		���ǵ���ǹ��ǻ󼼳��� detail = fetcher.parse(src);

		// System.out.println(detail);
		// System.out.println(fetcher.parse(NFile.getText(new File(
		// "fixtures/speedy_search_result_2.html"))));
		// System.out.println(fetcher.parse(NFile.getText(new File(
		// "fixtures/speedy_search_result_P01.html"))));
	}

	public void testParse2() throws IOException {
		String src = NFile.getText(new File(
				"fixtures/speedy_search_result_���ÿܰǹ�.html"));

		���ǵ���ǹ��ǻ󼼳��� detail = fetcher.parse(src);

		// System.out.println(detail);
	}

	public void testParse�Ű�������Ȳ() throws IOException {
		String src = NFile.getText(new File("fixtures/speedy_RESULT_02.htm"));

		���ǵ���ǹ��ǻ󼼳��� detail = fetcher.parse(src);
		���ǵ���ǸŰ�������Ȳ state = new ���ǵ���ǸŰ�������Ȳ(detail.get�Ű�������Ȳ(), "������");
		System.out.println(state);
	}
}
