package net.narusas.aceauction.data.builder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.si.auction.builder.present.������Item;
import net.narusas.si.auction.builder.present.��������Ȳ;
import net.narusas.si.auction.builder.present.�ε���ǥ�ø��Item;
import net.narusas.si.auction.builder.present.�����κ�;

public class �ε���ǥ�ø��ItemTest extends TestCase {
	String fixture01 = "1���� �ǹ��� ǥ��\n"//
			+ "    ����Ư���� ���α� û� 4-7\n"//
			+ "    ����Ư���� ���α� û� 4-9\n"//
			+ "    ����Ư���� ���α� û� 4-11\n"//
			+ "    ����Ư���� ���α� û� 4-12\n"//
			+ "    ö����ũ��Ʈ�� ��罽�������� 3����������\n"//
			+ "    1�� 433.08��\n"//
			+ "    2�� 430.11��\n"//
			+ "    3�� 430.11��\n"//
			+ "    ����1��382.1��\n"//
			+ "    ����2��608.28�� \n"//
			+ "    �ű�������\n"//
			+ "\n"//
			+ "�����κ��� �ǹ��� ǥ��\n"//
			+ "    �ǹ��ǹ�ȣ : 1�� 102ȣ\n"//
			+ "    ��      �� : ö����ũ��Ʈ��\n"//
			+ "    ��      �� : 194.93��\n"//
			+ "                 ��1��14.28��\n"//
			+ "\n"//
			+ "�������� ������ ������ ǥ��\n"//
			+ "    ���� �� ǥ�� : 1. ����Ư�������α�û�4-7\n"//
			+ "                    �� 815.5��\n"//
			+ "                   2. ����Ư�������α�û�4-9\n"//
			+ "                    �� 461.2��\n"//
			+ "                   3. ����Ư�������α�û�4-11\n"//
			+ "                    �� 193.1��\n"//
			+ "                   4. ����Ư�������α�û�4-12\n"//
			+ "                    �� 63.1��\n"//
			+ "    ������������ : 1. ������\n"//
			+ "                   2. ������\n"//
			+ "                   3. ������\n"//
			+ "                   4. ������\n"//
			+ "    �������Ǻ��� : 1. 1532.9 ���� 216\n"//
			+ "                2. 1532.9 ���� 216\n"//
			+ "                3. 1532.9 ���� 216\n"//
			+ "                4. 1532.9 ���� 216\n";

	String fixture02 = "1���� �ǹ��� ǥ��\n" + "    ����Ư���� ���ϱ� ������ 717-4\n"
			+ "    ����Ư���� ���ϱ� ������ 717-17\n" + "    ö����ũ��Ʈ�� �򽽷������� 4�� �ټ�������\n"
			+ "    1�� 85.92��\n" + "    2�� 160.8��\n" + "    3�� 160.8��\n"
			+ "    4�� 131.41��\n" + "    ����1�� 93.18��\n" + "    ��ž 17.24�� \n"
			+ "    \n" + "\n" + "�����κ��� �ǹ��� ǥ��\n" + "    �ǹ��ǹ�ȣ : 4�� 402ȣ\n"
			+ "    ��      �� : ö����ũ��Ʈ��\n" + "    ��      �� : 60.06��\n" + "\n"
			+ "�������� ������ ������ ǥ��\n" + "    ���� �� ǥ�� : 1. ����Ư���ü��ϱ�������717-4\n"
			+ "                    �� 149��\n"
			+ "                   2. ����Ư���ü��ϱ�������717-17\n"
			+ "                    �� 149��\n" + "    ������������ : 1, 2. ������\n"
			+ "    �������Ǻ��� : 1, 2. 298 ���� 30.64";

	public void testParseStructure01() {
		List<String> nodes = �ε���ǥ�ø��Item.parseStructure(fixture01);
		assertEquals(3, nodes.size());
		assertTrue(nodes.get(0).startsWith("1���� �ǹ��� ǥ��"));
		assertTrue(nodes.get(1).startsWith("�����κ��� �ǹ��� ǥ��"));
		assertTrue(nodes.get(2).startsWith("�������� ������ ������ ǥ��"));

		�����κ� item = new �����κ�(nodes.get(1));
		assertEquals("194.93��", item.get����());
		assertEquals("ö����ũ��Ʈ��", item.get����());
		��������Ȳ item2 = new ��������Ȳ(nodes.get(2), null);
		List<������Item> items = item2.getItems();
		assertEquals("1,����Ư�������α�û�4-7,�� 815.5��,������,1532.9 ���� 216", items
				.get(0).toString());
		assertEquals("2,����Ư�������α�û�4-9,�� 461.2��,������,1532.9 ���� 216", items
				.get(1).toString());
		assertEquals("3,����Ư�������α�û�4-11,�� 193.1��,������,1532.9 ���� 216", items.get(
				2).toString());
		assertEquals("4,����Ư�������α�û�4-12,�� 63.1��,������,1532.9 ���� 216", items
				.get(3).toString());
	}

	// public void testParseStructure02() {
	// List<String> nodes = �ε���ǥ�ø��Item.parseStructure(fixture02);
	// assertEquals(3, nodes.size());
	// assertTrue(nodes.get(0).startsWith("1���� �ǹ��� ǥ��"));
	// assertTrue(nodes.get(1).startsWith("�����κ��� �ǹ��� ǥ��"));
	// assertTrue(nodes.get(2).startsWith("�������� ������ ������ ǥ��"));
	//
	// �����κ� item = new �����κ�(nodes.get(1));
	// assertEquals("60.06��", item.get����());
	// assertEquals("ö����ũ��Ʈ��", item.get����());
	// ��������Ȳ item2 = new ��������Ȳ(nodes.get(2), null);
	// List<������Item> items = item2.getItems();
	// assertEquals("1,����Ư���ü��ϱ�������717-4,�� 149��,������,298 ���� 30.64",
	// items.get(0).toString());
	// assertEquals("2,����Ư���ü��ϱ�������717-17,�� 149��,������,298 ���� 30.64",
	// items.get(1).toString());
	//
	// assertEquals((30.64 + 30.64) / 298, item2.getRatio(), 0.01f);
	// assertEquals((149 + 149) * (30.64 + 30.64) / 298, item2.get����(), 0.01f);
	// }

	public void testConvertUnit() {
		Pattern p = �ε���ǥ�ø��Item.unitPattern1;
		Matcher m = p.matcher("���� 15��7ȩ 2���� 15��1��");
		assertTrue(m.find());
		String area1 = m.group(1);
		assertEquals("15��7ȩ ", m.group(1));
		assertTrue(m.find());
		String area2 = m.group(1);
		assertEquals("15��1��", m.group(1));

		assertEquals(51.897, �ε���ǥ�ø��Item.convert��ȩ��ToMeterSquare(area1), 0.001f);
		assertEquals(49.62, �ε���ǥ�ø��Item.convert��ȩ��ToMeterSquare(area2), 0.001f);

		assertEquals("45.285��", �ε���ǥ�ø��Item.convertAreaUnit("13�� 7ȩ"));

		assertEquals("���� 0.33�� abc", �ε���ǥ�ø��Item.convertAreaUnit("���� 1ȩ abc"));
		assertEquals("���� 0.363�� abc", �ε���ǥ�ø��Item
				.convertAreaUnit("���� 1ȩ1�� abc"));
		assertEquals("���� 3.635�� abc", �ε���ǥ�ø��Item
				.convertAreaUnit("���� 1��1ȩ abc"));

		assertEquals("���� 51.897�� 2���� 49.62�� abc", �ε���ǥ�ø��Item
				.convertAreaUnit("���� 15��7ȩ 2���� 15��1�� abc"));

	}

	public void testConvertUnit2() {
		Pattern p = �ε���ǥ�ø��Item.unitPattern2;
		Matcher m = p.matcher("�Ӿ� 1��3��1����");
		assertTrue(m.find());
		String area1 = m.group(1);
		assertEquals("1��3��1����", m.group(1));

		assertEquals(1 * 9917 + 3 * 991.7 + 1 * 99.17, �ε���ǥ�ø��Item
				.convert���ܹ���ToMeterSquare(area1), 0.001f);

		assertEquals("�Ӿ� 12991.27��  abc", �ε���ǥ�ø��Item
				.convertAreaUnit("�Ӿ� 1��3��1���� abc"));
		assertEquals("�Ӿ� 991.7��  abc", �ε���ǥ�ø��Item
				.convertAreaUnit("�Ӿ� 1�ܺ� abc"));
		assertEquals("�Ӿ� 6644.39��  abc", �ε���ǥ�ø��Item
				.convertAreaUnit("�Ӿ� 6��7���� abc"));

	}

}
