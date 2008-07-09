package net.narusas.aceauction.model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.aceauction.model.위지상.Line;
import net.narusas.aceauction.model.위지상.LineChunk;

public class 위지상Test extends TestCase {
	public void testParseStructure() {
		String src = "서울특별시 강동구 고덕동 152-3\n"//
				+ "위 지상\n"//
				+ "    벽돌조 평슬래브지붕 2층 단독주택\n"//
				+ "    1층 82.49㎡\n"//
				+ "    2층 82.49㎡\n"//
				+ "    지층 82.49㎡\n"//
				+ "    (지층 주택 64.43㎡\n"//
				+ "    대피소 18.06㎡)";

		List<Line> contents = 위지상.getContents(위지상.splitLines(src));
		List<LineChunk> items = 위지상.parseSerialLineChunks(contents);
		assertEquals(3, items.size());

		assertEquals("    벽돌조 평슬래브지붕 2층 단독주택\n", items.get(0).toString());
		assertFalse(items.get(0).isAreaSerial);
		assertEquals("    1층 82.49㎡\n    2층 82.49㎡\n    지층 82.49㎡\n", items.get(1).toString());
		assertTrue(items.get(1).isAreaSerial);
		assertEquals("    (지층 주택 64.43㎡\n    대피소 18.06㎡)\n", items.get(2).toString());
		assertFalse(items.get(2).isAreaSerial);

		assertItems(new 위지상(src, null), new String[][] {//
				{ "1층", "82.49㎡" }, { "2층", "82.49㎡" }, { "지층", "82.49㎡" } });
	}

//	public void testRegx() {
//		assertEquals("82.49㎡", 위지상.getValidArea("    1층 82.49㎡"));
//		assertNull(위지상.getValidArea("    (지층 주택 64.43㎡\n"));
//		assertNull(위지상.getValidArea("    대피소 18.06㎡)"));
//		assertNull(위지상.getValidArea("    대피소 18.06㎡ abc"));
//		Pattern onlyHasAreaPattern = Pattern.compile("^[\\d.]+㎡$");
//		Matcher m = onlyHasAreaPattern.matcher(" 46.81㎡".trim());
//		assertTrue(m.find());
//	}

	private void assertItems(위지상 items, String[][] expected) {
		assertEquals(expected.length, items.getFloors().size());
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i][0], items.getFloors().get(i).getText());
			assertEquals(expected[i][1], items.getFloors().get(i).getArea());
		}

	}

	public void test2() {
		String src = "서울특별시 광진구 구의동 35-53\n" + //
				"위 지상\n" + //
				" 연와조 경사스라브위 기와 2층 단독\n" + //
				" 주택\n" + //
				" 1층 94.44㎡\n" + //
				" 2층 60.42㎡\n" + //
				" 지하실 94.44㎡\n";
		assertItems(new 위지상(src, null), new String[][] { { "1층", "94.44㎡" }, { "2층", "60.42㎡" },
				{ "지하실", "94.44㎡" } });
	}

	public void testHave내역() {
		String src = "서울특별시 광진구 중곡동 196-11\n"//
				+ "위 지상\n"//
				+ " 철근콘크리트 및 벽돌조 슬래브\n"//
				+ " 지붕 2층 목욕탕 및 여관\n"//
				+ " 1층 188.03㎡\n"//
				+ " 2층 188.03㎡\n"//
				+ " 옥탑 13.79㎡\n"//
				+ " 지층 79.34㎡\n"//
				+ " 내역:1층 목욕탕 2층 여관,\n"//
				+ " 벽돌조 슬래브지붕\n"//
				+ " 201.82㎡\n";
		assertItems(new 위지상(src, null), new String[][] { { "1층", "188.03㎡" }, { "2층", "188.03㎡" },
				{ "옥탑", "13.79㎡" }, { "지층", "79.34㎡" } });
	}

	public void test단층() {
		String src = "서울특별시 강동구 천호동 449-39\n"//
				+ "위 지상\n"//
				+ " 벽돌조 시멘트기와지붕단층주택\n"//
				+ " 46.81㎡";

		List<Line> lines = 위지상.getContents(위지상.splitLines(src));
		assertEquals(1, lines.size());
		assertEquals(" 벽돌조 시멘트기와지붕단층주택 46.81㎡", lines.get(0).text);
		assertTrue(lines.get(0).hasArea);
		assertEquals("46.81㎡", lines.get(0).area);

		List<LineChunk> items = 위지상.parseSerialLineChunks(lines);
		assertEquals(1, items.size());
		assertEquals(" 벽돌조 시멘트기와지붕단층주택 46.81㎡\n", items.get(0).toString());
		assertTrue(items.get(0).isAreaSerial);

		assertItems(new 위지상(src, null), new String[][] { { "단층주택", "46.81㎡" } });
	}

	public void test분리된자료() {
		String src = "전라북도 익산시 신동 761-13\n"//
				+ "위 지상\n"//
				+ " 1호\n"//
				+ " 철근콘크리트조 및 시멘트벽돌조 슬래브지붕\n"//
				+ " 1층 철근콘트리트조 일반음식점\n"//
				+ " 114.63㎡\n"//
				+ " 2층 철근콘트리트조 사무실\n"//
				+ " 114.63㎡\n"//
				+ " 3층 시멘트벽돌조 주택\n"//
				+ " 97.69㎡\n"//
				+ " 4층 시멘트벽돌조 계단실\n"//
				+ " 16.49㎡\n"//
				+ " 지하 철근콘트리트조 소매점\n"//
				+ " 133.49㎡\n";
		assertItems(new 위지상(src, null), new String[][] { { "1층 철근콘트리트조 일반음식점", "114.63㎡" },
				{ "2층 철근콘트리트조 사무실", "114.63㎡" }, { "3층 시멘트벽돌조 주택", "97.69㎡" },
				{ "4층 시멘트벽돌조 계단실", "16.49㎡" }, { "지하 철근콘트리트조 소매점", "133.49㎡" }, });
	}

	//
	public void test3() {
		String src = "전라북도 익산시 함열읍 흘산리 505-2\n"//
				+ "전라북도 익산시 함열읍 흘산리 505-6\n"//
				+ "전라북도 익산시 함열읍 흘산리 505-7\n"//
				+ "위 지상\n"//
				+ " 4호\n"//
				+ " 철골조 기타(샌드위치판넬)지붕 단층 공장(창고) 188㎡\n";
		assertItems(new 위지상(src, null), new String[][] { { "단층 공장", "188㎡" } });
	}

	//
	public void test여러개의단층() {
		String src = "서울특별시 중구 충무로5가 2\n"//
				+ "위 지상\n"//
				+ " 철골조 인슈판넬지붕 단층공장\n"//
				+ " 584.23㎡\n"//
				+ " 부속건물\n"//
				+ " 철골조 인슈판넬지붕 단층 사무실\n"//
				+ " 및 식당 387㎡\n"//
				+ " 철골조 인슈판넬지붕 단층 기숙사\n"//
				+ " 162㎡\n"//
				+ " 경량철골조 인슈판넬지붕 단층\n"//
				+ " 경비실 17.27㎡\n";

		List<Line> lines = 위지상.getContents(위지상.splitLines(src));
		assertEquals(4, lines.size());

		assertLine(" 철골조 인슈판넬지붕 단층공장 584.23㎡", true, "584.23㎡", lines.get(0));
		assertLine(" 철골조 인슈판넬지붕 단층 사무실 및 식당 387㎡", true, "387㎡", lines.get(1));
		assertLine(" 철골조 인슈판넬지붕 단층 기숙사 162㎡", true, "162㎡", lines.get(2));
		assertLine(" 경량철골조 인슈판넬지붕 단층 경비실 17.27㎡", true, "17.27㎡", lines.get(3));

		List<LineChunk> items = 위지상.parseSerialLineChunks(lines);
		assertEquals(1, items.size());
		assertEquals(" 철골조 인슈판넬지붕 단층공장 584.23㎡\n" + " 철골조 인슈판넬지붕 단층 사무실 및 식당 387㎡\n"
				+ " 철골조 인슈판넬지붕 단층 기숙사 162㎡\n" + " 경량철골조 인슈판넬지붕 단층 경비실 17.27㎡\n", items.get(0)
				.toString());
		assertTrue(items.get(0).isAreaSerial);

		assertItems(new 위지상(src, null), new String[][] { { "단층공장", "584.23㎡" },
				{ "단층 사무실 및 식당", "387㎡" }, { "단층 기숙사", "162㎡" }, { "단층 경비실", "17.27㎡" } });
	}

	public void testComplicated() {
		String src = "전라북도 익산시 목천동 716\n"//
				+ "위 지상\n"//
				+ " 블록조 스레트지붕 단층 주택\n"//
				+ " 43.96㎡\n"//
				+ " 프레페브조 조립식판넬지붕 단층 음식점\n"//
				+ " 84.36㎡\n"//
				+ " 목조 초가지붕 단층 음식점\n"//
				+ " 38.13㎡\n"//
				+ " 벽돌조 및 철근콘크리트조 슬래브지붕 단층 주택 및 근린생활시설\n"//
				+ " 음식점 45.6㎡\n"//
				+ " 주 택 48.84㎡\n"//
				+ " 벽돌조 슬래브지붕 단층화장실 4.2㎡\n";

		List<Line> lines = 위지상.getContents(위지상.splitLines(src));
		assertEquals(7, lines.size());

		assertLine(" 블록조 스레트지붕 단층 주택 43.96㎡", true, "43.96㎡", lines.get(0));
		assertLine(" 프레페브조 조립식판넬지붕 단층 음식점 84.36㎡", true, "84.36㎡", lines.get(1));
		assertLine(" 목조 초가지붕 단층 음식점 38.13㎡", true, "38.13㎡", lines.get(2));
		assertLine(" 벽돌조 및 철근콘크리트조 슬래브지붕 단층 주택 및 근린생활시설", false, null, lines.get(3));
		assertLine(" 음식점 45.6㎡", true, "45.6㎡", lines.get(4));
		assertLine(" 주 택 48.84㎡", true, "48.84㎡", lines.get(5));
		assertLine(" 벽돌조 슬래브지붕 단층화장실 4.2㎡", true, "4.2㎡", lines.get(6));

		List<LineChunk> items = 위지상.parseSerialLineChunks(lines);
		assertEquals(3, items.size());
		assertEquals(
				" 블록조 스레트지붕 단층 주택 43.96㎡\n 프레페브조 조립식판넬지붕 단층 음식점 84.36㎡\n 목조 초가지붕 단층 음식점 38.13㎡\n",
				items.get(0).toString());
		assertEquals(" 벽돌조 및 철근콘크리트조 슬래브지붕 단층 주택 및 근린생활시설\n", items.get(1).toString());
		assertEquals(" 음식점 45.6㎡\n 주 택 48.84㎡\n 벽돌조 슬래브지붕 단층화장실 4.2㎡\n", items.get(2).toString());

		assertTrue(items.get(0).isAreaSerial);

		assertItems(new 위지상(src, null), new String[][] { { "단층 주택", "43.96㎡" },
				{ "단층 음식점", "84.36㎡" }, { "단층 음식점", "38.13㎡" }, { "음식점", "45.6㎡" },
				{ "주 택", "48.84㎡" }, { "단층화장실", "4.2㎡" }, });
	}

	public void testComplicated2() {
		String src = "전라북도 익산시 금마면 동고도리 816-15\n"//
				+ "전라북도 익산시 금마면 동고도리 819-4\n"//
				+ "전라북도 익산시 금마면 동고도리 817-1\n"//
				+ "위 지상\n"//
				+ " 철근콘크리트조 스라브지붕 단층\n"//
				+ " 볼링장및 휴게음식점\n"//
				+ " 1층 볼링장 584.21㎡\n"//
				+ " 휴게음식점 27.84㎡\n"//
				+ " 지하 볼링장 584.21㎡\n"//
				+ " 휴게음식점 27.84㎡\n";
		assertTrue(위지상.isStructures(위지상.splitLines(src)));
		assertItems(new 위지상(src, null), new String[][] { { "1층 볼링장", "584.21㎡" },
				{ "휴게음식점", "27.84㎡" }, { "지하 볼링장", "584.21㎡" }, { "휴게음식점", "27.84㎡" } });
	}

	public void test4() {
		String src = "경기도 포천시 가산면 금현리 597\n"//
				+ "위 지상\n"//
				+ " 조립식 경량판넬조 판넬지붕 단층\n"//
				+ " 공장\n"//
				+ " 198.0㎡\n";

		List<Line> lines = 위지상.getContents(위지상.splitLines(src));
		assertEquals(1, lines.size());

		assertLine(" 조립식 경량판넬조 판넬지붕 단층 공장 198.0㎡", true, "198.0㎡", lines.get(0));

		List<LineChunk> items = 위지상.parseSerialLineChunks(lines);
		assertEquals(1, items.size());

		assertEquals(" 조립식 경량판넬조 판넬지붕 단층 공장 198.0㎡\n", items.get(0).toString());
		assertTrue(items.get(0).isAreaSerial);

		assertItems(new 위지상(src, null), new String[][] { { "단층 공장", "198.0㎡" }, });
	}

	public void test중간에층수() {
		String src = "전라북도 익산시 함열읍 석매리 407-11\n"//
				+ "위 지상\n"//
				+ " 벽돌조 슬래브 및 아스팔트 슁글지붕2층 주택\n"//
				+ " 1층 벽돌조 슬래브지붕 주택\n"//
				+ " 112.74㎡\n"//
				+ " 2층 벽돌조 아스팔트슁글지붕주택\n"//
				+ " 37.66㎡";
		//

		Pattern p = Pattern.compile("((\\d+층|지하|지하\\s*\\d+층)+)");
		Matcher m = p.matcher(" 1층 벽돌조 슬래브지붕 주택");
		assertTrue(m.find());
		assertItems(new 위지상(src, null), new String[][] { { "1층 주택", "112.74㎡" },
				{ "2층 주택", "37.66㎡" }, });

		assertEquals("위 지상\n"//
				+ " 벽돌조 슬래브 및 아스팔트 슁글지붕2층 주택\n"//
				+ " 1층 벽돌조 슬래브지붕 주택\n"//
				+ " 112.74㎡\n"//
				+ " 2층 벽돌조 아스팔트슁글지붕주택\n"//
				+ " 37.66㎡", 부동산표시목록Item.parseStructure(src).get(1));
	}

	//
	public void testComplicated3() {
		String src = "위 지상\n"//
				+ " 철근콘크리트조 슬래브지붕 5층 숙박시설및 위락시설\n"//
				+ " 지하 위락시설(유흥주점) 190.19㎡\n"//
				+ " 보이라실 22.57㎡\n"//
				+ " 1층 계단실,화장실 52.76㎡\n"//
				+ " 주차장 228.33㎡\n"//
				+ " 2층 숙박시설(여관) 281.09㎡\n"//
				+ " 3층 숙박시설(여관) 281.09㎡\n"//
				+ " 4층 숙박시설(여관) 281.09㎡\n"//
				+ " 5층 숙박시설(여관) 158.39㎡\n"//
				+ " 철골조 판넬지붕 5층엘리베이터실\n"//
				+ " 1층 4.00㎡\n"//
				+ " 2층 4.00㎡\n"//
				+ " 3층 4.00㎡\n"//
				+ " 4층 4.00㎡\n"//
				+ " 5층 4.00㎡";

		assertTrue(위지상.isStructures(위지상.splitLines(src)));
		assertItems(new 위지상(src, null), new String[][] { { "지하 위락시설", "190.19㎡" },
				{ "보이라실", "22.57㎡" }, { "1층 계단실,화장실", "52.76㎡" }, { "주차장", "228.33㎡" },
				{ "2층 숙박시설", "281.09㎡" }, { "3층 숙박시설", "281.09㎡" },
				{ "4층 숙박시설", "281.09㎡" }, { "5층 숙박시설", "158.39㎡" }, { "1층", "4.00㎡" },
				{ "2층", "4.00㎡" }, { "3층", "4.00㎡" }, { "4층", "4.00㎡" }, { "5층", "4.00㎡" }, });
	}

	private void assertLine(String text, boolean hasArea, String area, Line line) {
		assertEquals(text, line.text);
		assertEquals(hasArea, line.hasArea);
		assertEquals(area, line.area);
	}
	
	public void testCase5() {
		String src = "경기도 화성시 정남면 보통리 12-12\n"//
			+"경기도 화성시 정남면 보통리 12-88\n"//
			+"위 지상\n"//
			+"    철근콘크리트조 (철근)콘크리트지붕 3층 제1종근린생활시설\n"//
			+"    1층 255.29㎡ (소매점)\n"//
			+"    2층 238.85㎡ (소매점)\n"//
			+"        194.19㎡ (일반음식점)\n"//
			+"    3층 167.08㎡ (단독주택)\n"//
			+"        132.91㎡ (소매점)";
		
		String[] temp = 위지상.splitLines(src); 
		List<Line> lines = 위지상.getContents(temp);
		assertEquals(4, lines.size());
		

		assertLine("    철근콘크리트조 콘크리트지붕 3층 제1종근린생활시설", false, null, lines.get(0));
		assertLine("    1층 255.29㎡ ", true, "255.29㎡", lines.get(1));

		
		assertItems(
				new 위지상(src, null), 
				new String[][] { 
			{ "1층", "255.29㎡" },
			{ "2층", "238.85㎡" },
			{ "3층", "167.08㎡" },
			
			
			});
		
	}
	

}
