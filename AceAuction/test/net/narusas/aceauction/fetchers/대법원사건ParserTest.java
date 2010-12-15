package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.���;
import net.narusas.si.auction.builder.present.������Ȳ;

import org.htmlparser.Node;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class ��������ParserTest extends TestCase {
	private String fixture_slice;

	private ��������Parser parser;

	private NodeList ns;

	private String fixture01;

	private String fixture02;

	private String fixture03;

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		parser = new ��������Parser();
		fixture01 = read(new File("mulgunlist.html"));
		fixture02 = read(new File("problem_01.htm"));
		fixture03 = read(new File("���ﳲ���������_P1.htm"));
		fixture_slice = read(new File("slice.html"));
		ns = parser.parseDOM(parser.fixSlice(fixture_slice));
	}

	private String read(File f) throws IOException {
		InputStreamReader reader = new InputStreamReader(new FileInputStream(
				"fixtures/" + f), "euc-kr");
		char[] buf = new char[4096];
		StringBuffer res = new StringBuffer();
		while (true) {
			int r = reader.read(buf, 0, 4096);
			if (r == -1) {
				break;
			}
			res.append(buf, 0, r);
		}
		reader.close();
		return res.toString();
	}

	public void testSliceTR() {
		String[] slices = parser.sliceTR(fixture_slice);
		assertTrue(slices.length > 0);
		assertTrue(slices[0].contains("2003Ÿ��8078"));
	}

	public void testGetSagunNo() {
		String no = parser.parseSagunNo(fixture_slice);
		assertEquals("20030130008078", no);
	}

	public void testFixSlice() {
		String fixed = parser.fixSlice(fixture_slice);
		assertTrue(fixed.startsWith("<table>"));
	}

	public void testCreateDOM() throws ParserException {
		NodeList ns = parser.parseDOM(parser.fixSlice(fixture_slice));
		assertNotNull(ns);
		for (int i = 0; i < ns.size(); i++) {
			Node n = ns.elementAt(i);
			assertEquals("table", n.getText());
		}
	}

	public void testParse���() {
		��� s = new ���(null, null);
		parser.parse���(s, ns); //
		assertEquals(4, s.items.size());
		���� item1 = s.items.get(0);
		assert����(item1, 4, false, "��,���ǽ���,�ٸ��ü�", "120,000,000", "31,458,000",
				"�ϰ��Ű�\r\n" + "������ޱ���(����)���� ���������� ��2��\r\n"
						+ "�Ӵ���: ���Ǹ����� ����\r\n" + "������ �̵����");

		������Ȳ bld1 = item1.get�󼼳���(0);
		String expected = "1���� �ǹ��� ǥ��\r\n"
				+ "���� �߱� ������6�� 18-185, 18-17, 18-184, 18-212\r\n" + "�и�����\r\n"
				+ "ö��, ö����ũ��Ʈ���򽺶������ 20�� �Ǹ�, �����ü�\r\n" + " \r\n"
				+ "�����κ��� �ǹ��� ǥ��\r\n" + " �ǹ��ǹ�ȣ : 6�� ��148ȣ\r\n"
				+ " �� �� : ö��, ö����ũ��Ʈ��\r\n" + " �� �� : 3.84��"; //
		assert�ǹ�(bld1, "����Ư���� �߱� ������6��  18-185�� 3�����и����� 6�� 148ȣ", expected);

		������Ȳ bld2 = item1.get�󼼳���(1);
		assert�ǹ�(bld2, "����Ư���� �߱� ������6��  18-184",
				"�� 1190.1�� (�������� ��61ȣ 1190.1���� 0.33 �ڹο� ����)");

		assert����(s.items.get(1), 6, false, "��,���ǽ���,�ٸ��ü�", "120,000,000",
				"31,458,000", "�ϰ��Ű�\r\n" + "������ޱ���(����)���� ���������� ��2��\r\n"
						+ "�Ӵ���: ���Ǹ����� ����\r\n" + "������ �̵����");

		assert����(s.items.get(2), 7, false, "��,���ǽ���,�ٸ��ü�", "103,000,000",
				"27,001,000", "�ϰ��Ű�\r\n" + "������ޱ���(����)���� ���������� ��2��\r\n"
						+ "�Ӵ���: ���Ǹ����� ����\r\n" + "������ �̵����");

		assert����(s.items.get(3), 8, false, "��,���ǽ���,�ٸ��ü�", "60,000,000",
				"15,729,000", "�ϰ��Ű�\r\n" + "������ޱ���(����)���� ���������� ��2��\r\n"
						+ "�Ӵ���: ���Ǹ����� ����\r\n" + "������ �̵����");
		// System.out.println(s);
	}

	void print(String src) {
		byte[] b = src.getBytes();
		for (int i = 0; i < b.length; i++) {
			System.out.print(" " + Integer.toHexString(b[i] & 0xFF));
		}
		System.out.println();
	}

	public void testParseProblemSagun() throws ParserException {
		// System.out.println(fixture02);
		List<���> res = parser.parsePage(fixture02, null, null);
		��� s = res.get(0);

	}

	private void assert����(���� item, int i, boolean b, String type, String price,
			String lowPrice, String bigo) {
		assertEquals(i, item.get���ǹ�ȣ());
		assertEquals(type, item.get��������());
		assertFalse(b);
		assertEquals(price, item.get����());
		assertEquals(lowPrice, item.get������());
		// assertEquals(bigo, item.get���());
	}

	void assert�ǹ�(������Ȳ bld, String address, String detail) {
		assertEquals(address + "=" + bld.get�ּ�(), address.trim(), bld.get�ּ�());
		// assertEquals(detail, bld.getDetail());
	}

	public void testSliceSagun() {
		String[] res = parser.sliceSagun(fixture01);
		assertTrue(res[0].contains("2003Ÿ��8078"));
		assertFalse(res[0].contains("2003Ÿ��28874"));
		assertTrue(res[1].contains("2003Ÿ��28874"));
		assertFalse(res[1].contains("2003Ÿ��8078"));
	}

	public void testParsePage() throws ParserException {
		List<���> res = parser.parsePage(fixture01, null, null);
		��� s = res.get(0);
		���� item1 = s.items.get(0);
		assert����(item1, 4, false, "��,���ǽ���,�ٸ��ü�", "120,000,000", "31,458,000",
				"�ϰ��Ű�\n" + "������ޱ���(����)���� ���������� ��2��\n" + "�Ӵ���:  ���Ǹ����� ����\n"
						+ "������ �̵����");

		������Ȳ bld1 = item1.get�󼼳���(0);
		assert�ǹ�(bld1, "����Ư���� �߱� ������6��  18-185�� 3�����и����� 6�� 148ȣ",

		"1���� �ǹ��� ǥ��\n" + "���� �߱� ������6�� 18-185, 18-17, 18-184, 18-212\n"
				+ "�и�����\n" + "ö��, ö����ũ��Ʈ�� �򽺶������ 20�� �Ǹ�, �����ü�\n" + " \n"
				+ "�����κ��� �ǹ��� ǥ��\n" + "   �ǹ��ǹ�ȣ : 6�� ��148ȣ\n"
				+ "   ��      �� : ö��, ö����ũ��Ʈ��\n" + "   ��      �� : 3.84��");

		assert�ǹ�(item1.get�󼼳���(1), "����Ư���� �߱� ������6��  18-184",
				"�� 1190.1��  (�������� ��61ȣ 1190.1���� 0.33 �ڹο� ����)");

		assert����(s.items.get(1), 6, false, "��,���ǽ���,�ٸ��ü�", "120,000,000",
				"31,458,000", "�ϰ��Ű�\n" + "������ޱ���(����)���� ���������� ��2��\n"
						+ "�Ӵ���:  ���Ǹ����� ����\n" + "������ �̵����");

		assert����(s.items.get(2), 7, false, "��,���ǽ���,�ٸ��ü�", "103,000,000",
				"27,001,000", "�ϰ��Ű�\n" + "������ޱ���(����)���� ���������� ��2��\n"
						+ "�Ӵ���:  ���Ǹ����� ����\n" + "������ �̵����");

		assert����(s.items.get(3), 8, false, "��,���ǽ���,�ٸ��ü�", "60,000,000",
				"15,729,000", "�ϰ��Ű�\n" + "������ޱ���(����)���� ���������� ��2��\n"
						+ "�Ӵ���:  ���Ǹ����� ����\n" + "������ �̵����");
	}

	public void testParse�������Ի��() throws ParserException {
		String[] slices = parser.sliceSagun(fixture01);
		String src = parser.fixSlice(slices[1]);
		ns = parser.parseDOM(src);
		��� s = new ���(null, null);
		parser.parse���(s, ns);

	}

	public void testSliceSagun2() {
		String[] res = parser.sliceSagun(fixture02);
		assertTrue(res.length > 0);
		assertTrue(res[0].contains("2003Ÿ��28874"));

		assertFalse(res[0].contains("2004Ÿ��11477"));
		assertTrue(res[1].contains("2004Ÿ��11477"));
		assertFalse(res[1].contains("2003Ÿ��28874"));
	}

	public void testParseProblem1() throws ParserException {
		String[] slices = parser.sliceSagun(fixture03);
		String src = parser.fixSlice(slices[1]);
		ns = parser.parseDOM(src);
		��� s = new ���(null, null);
		parser.parse���(s, ns);
	}
}
