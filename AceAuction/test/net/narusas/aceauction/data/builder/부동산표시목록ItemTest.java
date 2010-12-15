package net.narusas.aceauction.data.builder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.si.auction.builder.present.대지권Item;
import net.narusas.si.auction.builder.present.대지권현황;
import net.narusas.si.auction.builder.present.부동산표시목록Item;
import net.narusas.si.auction.builder.present.전유부분;

public class 부동산표시목록ItemTest extends TestCase {
	String fixture01 = "1동의 건물의 표시\n"//
			+ "    서울특별시 종로구 청운동 4-7\n"//
			+ "    서울특별시 종로구 청운동 4-9\n"//
			+ "    서울특별시 종로구 청운동 4-11\n"//
			+ "    서울특별시 종로구 청운동 4-12\n"//
			+ "    철근콘크리트조 경사슬래브지붕 3층연립주택\n"//
			+ "    1층 433.08㎡\n"//
			+ "    2층 430.11㎡\n"//
			+ "    3층 430.11㎡\n"//
			+ "    지하1층382.1㎡\n"//
			+ "    지하2층608.28㎡ \n"//
			+ "    신구파인힐\n"//
			+ "\n"//
			+ "전유부분의 건물의 표시\n"//
			+ "    건물의번호 : 1층 102호\n"//
			+ "    구      조 : 철근콘크리트조\n"//
			+ "    면      적 : 194.93㎡\n"//
			+ "                 지1층14.28㎡\n"//
			+ "\n"//
			+ "대지권의 목적인 토지의 표시\n"//
			+ "    토지 의 표시 : 1. 서울특별시종로구청운동4-7\n"//
			+ "                    대 815.5㎡\n"//
			+ "                   2. 서울특별시종로구청운동4-9\n"//
			+ "                    대 461.2㎡\n"//
			+ "                   3. 서울특별시종로구청운동4-11\n"//
			+ "                    대 193.1㎡\n"//
			+ "                   4. 서울특별시종로구청운동4-12\n"//
			+ "                    대 63.1㎡\n"//
			+ "    대지권의종류 : 1. 소유권\n"//
			+ "                   2. 소유권\n"//
			+ "                   3. 소유권\n"//
			+ "                   4. 소유권\n"//
			+ "    대지권의비율 : 1. 1532.9 분의 216\n"//
			+ "                2. 1532.9 분의 216\n"//
			+ "                3. 1532.9 분의 216\n"//
			+ "                4. 1532.9 분의 216\n";

	String fixture02 = "1동의 건물의 표시\n" + "    서울특별시 성북구 정릉동 717-4\n"
			+ "    서울특별시 성북구 정릉동 717-17\n" + "    철근콘크리트조 평슬래브지붕 4층 다세대주택\n"
			+ "    1층 85.92㎡\n" + "    2층 160.8㎡\n" + "    3층 160.8㎡\n"
			+ "    4층 131.41㎡\n" + "    지하1층 93.18㎡\n" + "    옥탑 17.24㎡ \n"
			+ "    \n" + "\n" + "전유부분의 건물의 표시\n" + "    건물의번호 : 4층 402호\n"
			+ "    구      조 : 철근콘크리트조\n" + "    면      적 : 60.06㎡\n" + "\n"
			+ "대지권의 목적인 토지의 표시\n" + "    토지 의 표시 : 1. 서울특별시성북구정릉동717-4\n"
			+ "                    대 149㎡\n"
			+ "                   2. 서울특별시성북구정릉동717-17\n"
			+ "                    대 149㎡\n" + "    대지권의종류 : 1, 2. 소유권\n"
			+ "    대지권의비율 : 1, 2. 298 분의 30.64";

	public void testParseStructure01() {
		List<String> nodes = 부동산표시목록Item.parseStructure(fixture01);
		assertEquals(3, nodes.size());
		assertTrue(nodes.get(0).startsWith("1동의 건물의 표시"));
		assertTrue(nodes.get(1).startsWith("전유부분의 건물의 표시"));
		assertTrue(nodes.get(2).startsWith("대지권의 목적인 토지의 표시"));

		전유부분 item = new 전유부분(nodes.get(1));
		assertEquals("194.93㎡", item.get면적());
		assertEquals("철근콘크리트조", item.get구조());
		대지권현황 item2 = new 대지권현황(nodes.get(2), null);
		List<대지권Item> items = item2.getItems();
		assertEquals("1,서울특별시종로구청운동4-7,대 815.5㎡,소유권,1532.9 분의 216", items
				.get(0).toString());
		assertEquals("2,서울특별시종로구청운동4-9,대 461.2㎡,소유권,1532.9 분의 216", items
				.get(1).toString());
		assertEquals("3,서울특별시종로구청운동4-11,대 193.1㎡,소유권,1532.9 분의 216", items.get(
				2).toString());
		assertEquals("4,서울특별시종로구청운동4-12,대 63.1㎡,소유권,1532.9 분의 216", items
				.get(3).toString());
	}

	// public void testParseStructure02() {
	// List<String> nodes = 부동산표시목록Item.parseStructure(fixture02);
	// assertEquals(3, nodes.size());
	// assertTrue(nodes.get(0).startsWith("1동의 건물의 표시"));
	// assertTrue(nodes.get(1).startsWith("전유부분의 건물의 표시"));
	// assertTrue(nodes.get(2).startsWith("대지권의 목적인 토지의 표시"));
	//
	// 전유부분 item = new 전유부분(nodes.get(1));
	// assertEquals("60.06㎡", item.get면적());
	// assertEquals("철근콘크리트조", item.get구조());
	// 대지권현황 item2 = new 대지권현황(nodes.get(2), null);
	// List<대지권Item> items = item2.getItems();
	// assertEquals("1,서울특별시성북구정릉동717-4,대 149㎡,소유권,298 분의 30.64",
	// items.get(0).toString());
	// assertEquals("2,서울특별시성북구정릉동717-17,대 149㎡,소유권,298 분의 30.64",
	// items.get(1).toString());
	//
	// assertEquals((30.64 + 30.64) / 298, item2.getRatio(), 0.01f);
	// assertEquals((149 + 149) * (30.64 + 30.64) / 298, item2.get면적(), 0.01f);
	// }

	public void testConvertUnit() {
		Pattern p = 부동산표시목록Item.unitPattern1;
		Matcher m = p.matcher("건평 15평7홉 2층평 15평1작");
		assertTrue(m.find());
		String area1 = m.group(1);
		assertEquals("15평7홉 ", m.group(1));
		assertTrue(m.find());
		String area2 = m.group(1);
		assertEquals("15평1작", m.group(1));

		assertEquals(51.897, 부동산표시목록Item.convert평홉작ToMeterSquare(area1), 0.001f);
		assertEquals(49.62, 부동산표시목록Item.convert평홉작ToMeterSquare(area2), 0.001f);

		assertEquals("45.285㎡", 부동산표시목록Item.convertAreaUnit("13평 7홉"));

		assertEquals("건평 0.33㎡ abc", 부동산표시목록Item.convertAreaUnit("건평 1홉 abc"));
		assertEquals("건평 0.363㎡ abc", 부동산표시목록Item
				.convertAreaUnit("건평 1홉1작 abc"));
		assertEquals("건평 3.635㎡ abc", 부동산표시목록Item
				.convertAreaUnit("건평 1평1홉 abc"));

		assertEquals("건평 51.897㎡ 2층평 49.62㎡ abc", 부동산표시목록Item
				.convertAreaUnit("건평 15평7홉 2층평 15평1작 abc"));

	}

	public void testConvertUnit2() {
		Pattern p = 부동산표시목록Item.unitPattern2;
		Matcher m = p.matcher("임야 1정3단1무보");
		assertTrue(m.find());
		String area1 = m.group(1);
		assertEquals("1정3단1무보", m.group(1));

		assertEquals(1 * 9917 + 3 * 991.7 + 1 * 99.17, 부동산표시목록Item
				.convert정단무보ToMeterSquare(area1), 0.001f);

		assertEquals("임야 12991.27㎡  abc", 부동산표시목록Item
				.convertAreaUnit("임야 1정3단1무보 abc"));
		assertEquals("임야 991.7㎡  abc", 부동산표시목록Item
				.convertAreaUnit("임야 1단보 abc"));
		assertEquals("임야 6644.39㎡  abc", 부동산표시목록Item
				.convertAreaUnit("임야 6단7무보 abc"));

	}

}
