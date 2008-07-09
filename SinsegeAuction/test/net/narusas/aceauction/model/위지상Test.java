package net.narusas.aceauction.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.aceauction.model.������.Line;
import net.narusas.aceauction.model.������.LineChunk;

public class ������Test extends TestCase {
	public void testParseStructure() {
		String src = "����Ư���� ������ ����� 152-3\n"//
				+ "�� ����\n"//
				+ "    ������ �򽽷������� 2�� �ܵ�����\n"//
				+ "    1�� 82.49��\n"//
				+ "    2�� 82.49��\n"//
				+ "    ���� 82.49��\n"//
				+ "    (���� ���� 64.43��\n"//
				+ "    ���Ǽ� 18.06��)";

		List<Line> contents = ������.getContents(������.splitLines(src));
		List<LineChunk> items = ������.parseSerialLineChunks(contents);
		assertEquals(3, items.size());

		assertEquals("    ������ �򽽷������� 2�� �ܵ�����\n", items.get(0).toString());
		assertFalse(items.get(0).isAreaSerial);
		assertEquals("    1�� 82.49��\n    2�� 82.49��\n    ���� 82.49��\n", items.get(1).toString());
		assertTrue(items.get(1).isAreaSerial);
		assertEquals("    (���� ���� 64.43��\n    ���Ǽ� 18.06��)\n", items.get(2).toString());
		assertFalse(items.get(2).isAreaSerial);

		assertItems(new ������(src, null), new String[][] {//
				{ "1��", "82.49��" }, { "2��", "82.49��" }, { "����", "82.49��" } });
	}

//	public void testRegx() {
//		assertEquals("82.49��", ������.getValidArea("    1�� 82.49��"));
//		assertNull(������.getValidArea("    (���� ���� 64.43��\n"));
//		assertNull(������.getValidArea("    ���Ǽ� 18.06��)"));
//		assertNull(������.getValidArea("    ���Ǽ� 18.06�� abc"));
//		Pattern onlyHasAreaPattern = Pattern.compile("^[\\d.]+��$");
//		Matcher m = onlyHasAreaPattern.matcher(" 46.81��".trim());
//		assertTrue(m.find());
//	}

	private void assertItems(������ items, String[][] expected) {
		assertEquals(expected.length, items.getFloors().size());
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i][0], items.getFloors().get(i).getText());
			assertEquals(expected[i][1], items.getFloors().get(i).getArea());
		}

	}

	public void test2() {
		String src = "����Ư���� ������ ���ǵ� 35-53\n" + //
				"�� ����\n" + //
				" ������ ��罺����� ��� 2�� �ܵ�\n" + //
				" ����\n" + //
				" 1�� 94.44��\n" + //
				" 2�� 60.42��\n" + //
				" ���Ͻ� 94.44��\n";
		assertItems(new ������(src, null), new String[][] { { "1��", "94.44��" }, { "2��", "60.42��" },
				{ "���Ͻ�", "94.44��" } });
	}

	public void testHave����() {
		String src = "����Ư���� ������ �߰ 196-11\n"//
				+ "�� ����\n"//
				+ " ö����ũ��Ʈ �� ������ ������\n"//
				+ " ���� 2�� ����� �� ����\n"//
				+ " 1�� 188.03��\n"//
				+ " 2�� 188.03��\n"//
				+ " ��ž 13.79��\n"//
				+ " ���� 79.34��\n"//
				+ " ����:1�� ����� 2�� ����,\n"//
				+ " ������ ����������\n"//
				+ " 201.82��\n";
		assertItems(new ������(src, null), new String[][] { { "1��", "188.03��" }, { "2��", "188.03��" },
				{ "��ž", "13.79��" }, { "����", "79.34��" } });
	}

	public void test����() {
		String src = "����Ư���� ������ õȣ�� 449-39\n"//
				+ "�� ����\n"//
				+ " ������ �ø�Ʈ������ش�������\n"//
				+ " 46.81��";

		List<Line> lines = ������.getContents(������.splitLines(src));
		assertEquals(1, lines.size());
		assertEquals(" ������ �ø�Ʈ������ش������� 46.81��", lines.get(0).text);
		assertTrue(lines.get(0).hasArea);
		assertEquals("46.81��", lines.get(0).area);

		List<LineChunk> items = ������.parseSerialLineChunks(lines);
		assertEquals(1, items.size());
		assertEquals(" ������ �ø�Ʈ������ش������� 46.81��\n", items.get(0).toString());
		assertTrue(items.get(0).isAreaSerial);

		assertItems(new ������(src, null), new String[][] { { "��������", "46.81��" } });
	}

	public void test�и����ڷ�() {
		String src = "����ϵ� �ͻ�� �ŵ� 761-13\n"//
				+ "�� ����\n"//
				+ " 1ȣ\n"//
				+ " ö����ũ��Ʈ�� �� �ø�Ʈ������ ����������\n"//
				+ " 1�� ö����Ʈ��Ʈ�� �Ϲ�������\n"//
				+ " 114.63��\n"//
				+ " 2�� ö����Ʈ��Ʈ�� �繫��\n"//
				+ " 114.63��\n"//
				+ " 3�� �ø�Ʈ������ ����\n"//
				+ " 97.69��\n"//
				+ " 4�� �ø�Ʈ������ ��ܽ�\n"//
				+ " 16.49��\n"//
				+ " ���� ö����Ʈ��Ʈ�� �Ҹ���\n"//
				+ " 133.49��\n";
		assertItems(new ������(src, null), new String[][] { { "1�� ö����Ʈ��Ʈ�� �Ϲ�������", "114.63��" },
				{ "2�� ö����Ʈ��Ʈ�� �繫��", "114.63��" }, { "3�� �ø�Ʈ������ ����", "97.69��" },
				{ "4�� �ø�Ʈ������ ��ܽ�", "16.49��" }, { "���� ö����Ʈ��Ʈ�� �Ҹ���", "133.49��" }, });
	}

	//
	public void test3() {
		String src = "����ϵ� �ͻ�� �Կ��� ��긮 505-2\n"//
				+ "����ϵ� �ͻ�� �Կ��� ��긮 505-6\n"//
				+ "����ϵ� �ͻ�� �Կ��� ��긮 505-7\n"//
				+ "�� ����\n"//
				+ " 4ȣ\n"//
				+ " ö���� ��Ÿ(������ġ�ǳ�)���� ���� ����(â��) 188��\n";
		assertItems(new ������(src, null), new String[][] { { "���� ����", "188��" } });
	}

	//
	public void test�������Ǵ���() {
		String src = "����Ư���� �߱� �湫��5�� 2\n"//
				+ "�� ����\n"//
				+ " ö���� �ν��ǳ����� ��������\n"//
				+ " 584.23��\n"//
				+ " �μӰǹ�\n"//
				+ " ö���� �ν��ǳ����� ���� �繫��\n"//
				+ " �� �Ĵ� 387��\n"//
				+ " ö���� �ν��ǳ����� ���� �����\n"//
				+ " 162��\n"//
				+ " �淮ö���� �ν��ǳ����� ����\n"//
				+ " ���� 17.27��\n";

		List<Line> lines = ������.getContents(������.splitLines(src));
		assertEquals(4, lines.size());

		assertLine(" ö���� �ν��ǳ����� �������� 584.23��", true, "584.23��", lines.get(0));
		assertLine(" ö���� �ν��ǳ����� ���� �繫�� �� �Ĵ� 387��", true, "387��", lines.get(1));
		assertLine(" ö���� �ν��ǳ����� ���� ����� 162��", true, "162��", lines.get(2));
		assertLine(" �淮ö���� �ν��ǳ����� ���� ���� 17.27��", true, "17.27��", lines.get(3));

		List<LineChunk> items = ������.parseSerialLineChunks(lines);
		assertEquals(1, items.size());
		assertEquals(" ö���� �ν��ǳ����� �������� 584.23��\n" + " ö���� �ν��ǳ����� ���� �繫�� �� �Ĵ� 387��\n"
				+ " ö���� �ν��ǳ����� ���� ����� 162��\n" + " �淮ö���� �ν��ǳ����� ���� ���� 17.27��\n", items.get(0)
				.toString());
		assertTrue(items.get(0).isAreaSerial);

		assertItems(new ������(src, null), new String[][] { { "��������", "584.23��" },
				{ "���� �繫�� �� �Ĵ�", "387��" }, { "���� �����", "162��" }, { "���� ����", "17.27��" } });
	}

	public void testComplicated() {
		String src = "����ϵ� �ͻ�� ��õ�� 716\n"//
				+ "�� ����\n"//
				+ " ����� ����Ʈ���� ���� ����\n"//
				+ " 43.96��\n"//
				+ " ��������� �������ǳ����� ���� ������\n"//
				+ " 84.36��\n"//
				+ " ���� �ʰ����� ���� ������\n"//
				+ " 38.13��\n"//
				+ " ������ �� ö����ũ��Ʈ�� ���������� ���� ���� �� �ٸ���Ȱ�ü�\n"//
				+ " ������ 45.6��\n"//
				+ " �� �� 48.84��\n"//
				+ " ������ ���������� ����ȭ��� 4.2��\n";

		List<Line> lines = ������.getContents(������.splitLines(src));
		assertEquals(7, lines.size());

		assertLine(" ����� ����Ʈ���� ���� ���� 43.96��", true, "43.96��", lines.get(0));
		assertLine(" ��������� �������ǳ����� ���� ������ 84.36��", true, "84.36��", lines.get(1));
		assertLine(" ���� �ʰ����� ���� ������ 38.13��", true, "38.13��", lines.get(2));
		assertLine(" ������ �� ö����ũ��Ʈ�� ���������� ���� ���� �� �ٸ���Ȱ�ü�", false, null, lines.get(3));
		assertLine(" ������ 45.6��", true, "45.6��", lines.get(4));
		assertLine(" �� �� 48.84��", true, "48.84��", lines.get(5));
		assertLine(" ������ ���������� ����ȭ��� 4.2��", true, "4.2��", lines.get(6));

		List<LineChunk> items = ������.parseSerialLineChunks(lines);
		assertEquals(3, items.size());
		assertEquals(
				" ����� ����Ʈ���� ���� ���� 43.96��\n ��������� �������ǳ����� ���� ������ 84.36��\n ���� �ʰ����� ���� ������ 38.13��\n",
				items.get(0).toString());
		assertEquals(" ������ �� ö����ũ��Ʈ�� ���������� ���� ���� �� �ٸ���Ȱ�ü�\n", items.get(1).toString());
		assertEquals(" ������ 45.6��\n �� �� 48.84��\n ������ ���������� ����ȭ��� 4.2��\n", items.get(2).toString());

		assertTrue(items.get(0).isAreaSerial);

		assertItems(new ������(src, null), new String[][] { { "���� ����", "43.96��" },
				{ "���� ������", "84.36��" }, { "���� ������", "38.13��" }, { "������", "45.6��" },
				{ "�� ��", "48.84��" }, { "����ȭ���", "4.2��" }, });
	}

	public void testComplicated2() {
		String src = "����ϵ� �ͻ�� �ݸ��� ������ 816-15\n"//
				+ "����ϵ� �ͻ�� �ݸ��� ������ 819-4\n"//
				+ "����ϵ� �ͻ�� �ݸ��� ������ 817-1\n"//
				+ "�� ����\n"//
				+ " ö����ũ��Ʈ�� ��������� ����\n"//
				+ " ������� �ް�������\n"//
				+ " 1�� ������ 584.21��\n"//
				+ " �ް������� 27.84��\n"//
				+ " ���� ������ 584.21��\n"//
				+ " �ް������� 27.84��\n";
		assertTrue(������.isStructures(������.splitLines(src)));
		assertItems(new ������(src, null), new String[][] { { "1�� ������", "584.21��" },
				{ "�ް�������", "27.84��" }, { "���� ������", "584.21��" }, { "�ް�������", "27.84��" } });
	}

	public void test4() {
		String src = "��⵵ ��õ�� ����� ������ 597\n"//
				+ "�� ����\n"//
				+ " ������ �淮�ǳ��� �ǳ����� ����\n"//
				+ " ����\n"//
				+ " 198.0��\n";

		List<Line> lines = ������.getContents(������.splitLines(src));
		assertEquals(1, lines.size());

		assertLine(" ������ �淮�ǳ��� �ǳ����� ���� ���� 198.0��", true, "198.0��", lines.get(0));

		List<LineChunk> items = ������.parseSerialLineChunks(lines);
		assertEquals(1, items.size());

		assertEquals(" ������ �淮�ǳ��� �ǳ����� ���� ���� 198.0��\n", items.get(0).toString());
		assertTrue(items.get(0).isAreaSerial);

		assertItems(new ������(src, null), new String[][] { { "���� ����", "198.0��" }, });
	}

	public void test�߰�������() {
		String src = "����ϵ� �ͻ�� �Կ��� ���Ÿ� 407-11\n"//
				+ "�� ����\n"//
				+ " ������ ������ �� �ƽ���Ʈ ��������2�� ����\n"//
				+ " 1�� ������ ���������� ����\n"//
				+ " 112.74��\n"//
				+ " 2�� ������ �ƽ���Ʈ������������\n"//
				+ " 37.66��";
		//

		Pattern p = Pattern.compile("((\\d+��|����|����\\s*\\d+��)+)");
		Matcher m = p.matcher(" 1�� ������ ���������� ����");
		assertTrue(m.find());
		assertItems(new ������(src, null), new String[][] { { "1�� ����", "112.74��" },
				{ "2�� ����", "37.66��" }, });

		assertEquals("�� ����\n"//
				+ " ������ ������ �� �ƽ���Ʈ ��������2�� ����\n"//
				+ " 1�� ������ ���������� ����\n"//
				+ " 112.74��\n"//
				+ " 2�� ������ �ƽ���Ʈ������������\n"//
				+ " 37.66��", �ε���ǥ�ø��Item.parseStructure(src).get(1));
	}

	//
	public void testComplicated3() {
		String src = "�� ����\n"//
				+ " ö����ũ��Ʈ�� ���������� 5�� ���ڽü��� �����ü�\n"//
				+ " ���� �����ü�(��������) 190.19��\n"//
				+ " ���̶�� 22.57��\n"//
				+ " 1�� ��ܽ�,ȭ��� 52.76��\n"//
				+ " ������ 228.33��\n"//
				+ " 2�� ���ڽü�(����) 281.09��\n"//
				+ " 3�� ���ڽü�(����) 281.09��\n"//
				+ " 4�� ���ڽü�(����) 281.09��\n"//
				+ " 5�� ���ڽü�(����) 158.39��\n"//
				+ " ö���� �ǳ����� 5�����������ͽ�\n"//
				+ " 1�� 4.00��\n"//
				+ " 2�� 4.00��\n"//
				+ " 3�� 4.00��\n"//
				+ " 4�� 4.00��\n"//
				+ " 5�� 4.00��";

		assertTrue(������.isStructures(������.splitLines(src)));
		assertItems(new ������(src, null), new String[][] { { "���� �����ü�", "190.19��" },
				{ "���̶��", "22.57��" }, { "1�� ��ܽ�,ȭ���", "52.76��" }, { "������", "228.33��" },
				{ "2�� ���ڽü�", "281.09��" }, { "3�� ���ڽü�", "281.09��" },
				{ "4�� ���ڽü�", "281.09��" }, { "5�� ���ڽü�", "158.39��" }, { "1��", "4.00��" },
				{ "2��", "4.00��" }, { "3��", "4.00��" }, { "4��", "4.00��" }, { "5��", "4.00��" }, });
	}

	private void assertLine(String text, boolean hasArea, String area, Line line) {
		assertEquals(text, line.text);
		assertEquals(hasArea, line.hasArea);
		assertEquals(area, line.area);
	}
	
	public void testCase5() {
		String src = "��⵵ ȭ���� ������ ���븮 12-12\n"//
			+"��⵵ ȭ���� ������ ���븮 12-88\n"//
			+"�� ����\n"//
			+"    ö����ũ��Ʈ�� (ö��)��ũ��Ʈ���� 3�� ��1���ٸ���Ȱ�ü�\n"//
			+"    1�� 255.29�� (�Ҹ���)\n"//
			+"    2�� 238.85�� (�Ҹ���)\n"//
			+"        194.19�� (�Ϲ�������)\n"//
			+"    3�� 167.08�� (�ܵ�����)\n"//
			+"        132.91�� (�Ҹ���)";
		
		String[] temp = ������.splitLines(src); 
		List<Line> lines = ������.getContents(temp);
		assertEquals(4, lines.size());
		

		assertLine("    ö����ũ��Ʈ�� ��ũ��Ʈ���� 3�� ��1���ٸ���Ȱ�ü�", false, null, lines.get(0));
		assertLine("    1�� 255.29�� ", true, "255.29��", lines.get(1));

		
		assertItems(
				new ������(src, null), 
				new String[][] { 
			{ "1��", "255.29��" },
			{ "2��", "238.85��" },
			{ "3��", "167.08��" },
			
			
			});
		
	}
	

}
