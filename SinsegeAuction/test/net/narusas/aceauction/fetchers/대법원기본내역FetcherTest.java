package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import net.narusas.aceauction.model.���;
import net.narusas.util.lang.NFile;

public class ������⺻����FetcherTest extends TestCase {
	public void test1() throws IOException {
		String src = NFile.getText(new File("fixtures/�⺻����.htm"));
		������⺻����Fetcher fetcher = new ������⺻����Fetcher();
		��� s = new ���(null, null);
		fetcher.parse(src, s);
		assertEquals("�ε������ǰ��", s.get��Ǹ�());
		assertEquals("[�ֽ�ȸ�簡�ο����Ͼ]", s.get������());
		assertEquals("[�ֽ�ȸ�縸�±��]", s.getä����());
		assertEquals("[�ֽ�ȸ�籹������]", s.getä����());

	}

	public void test2() {
		// ������⺻����Fetcher fetcher = new ������⺻����Fetcher();
		// System.out.println(fetcher.getPage("000210", "���3��",
		// "1006","2007.03.27","20050130007062"));
	}

	public void test3() throws IOException {
		String src = NFile.getText(new File("fixtures/�⺻����_3.htm"));
		������⺻����Fetcher fetcher = new ������⺻����Fetcher();
		��� s = new ���(null, null);
		 fetcher.parse(src, s);
		assertEquals("[������]", s.get������());
		assertEquals("[�ֹ���]", s.getä����());
		assertEquals("[�ѱ��ڻ��������]", s.getä����());
	}

}
