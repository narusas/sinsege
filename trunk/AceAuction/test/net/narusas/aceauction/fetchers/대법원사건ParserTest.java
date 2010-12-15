package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.aceauction.model.물건;
import net.narusas.aceauction.model.사건;
import net.narusas.si.auction.builder.present.물건현황;

import org.htmlparser.Node;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class 대법원사건ParserTest extends TestCase {
	private String fixture_slice;

	private 대법원사건Parser parser;

	private NodeList ns;

	private String fixture01;

	private String fixture02;

	private String fixture03;

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		parser = new 대법원사건Parser();
		fixture01 = read(new File("mulgunlist.html"));
		fixture02 = read(new File("problem_01.htm"));
		fixture03 = read(new File("서울남부지방법원_P1.htm"));
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
		assertTrue(slices[0].contains("2003타경8078"));
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

	public void testParse사건() {
		사건 s = new 사건(null, null);
		parser.parse사건(s, ns); //
		assertEquals(4, s.items.size());
		물건 item1 = s.items.get(0);
		assert물건(item1, 4, false, "상가,오피스텔,근린시설", "120,000,000", "31,458,000",
				"일괄매각\r\n" + "대금지급기일(기한)이후 지연이자율 연2할\r\n"
						+ "임대차: 물건명세서와 같음\r\n" + "대지권 미등기임");

		물건현황 bld1 = item1.get상세내역(0);
		String expected = "1동의 건물의 표시\r\n"
				+ "서울 중구 을지로6가 18-185, 18-17, 18-184, 18-212\r\n" + "밀리오레\r\n"
				+ "철골, 철근콘크리트조평스라브지붕 20층 판매, 업무시설\r\n" + " \r\n"
				+ "전유부분의 건물의 표시\r\n" + " 건물의번호 : 6층 제148호\r\n"
				+ " 구 조 : 철골, 철근콘크리트조\r\n" + " 면 적 : 3.84㎡"; //
		assert건물(bld1, "서울특별시 중구 을지로6가  18-185외 3필지밀리오레 6층 148호", expected);

		물건현황 bld2 = item1.get상세내역(1);
		assert건물(bld2, "서울특별시 중구 을지로6가  18-184",
				"대 1190.1㎡ (갑구순위 제61호 1190.1분의 0.33 박민원 지분)");

		assert물건(s.items.get(1), 6, false, "상가,오피스텔,근린시설", "120,000,000",
				"31,458,000", "일괄매각\r\n" + "대금지급기일(기한)이후 지연이자율 연2할\r\n"
						+ "임대차: 물건명세서와 같음\r\n" + "대지권 미등기임");

		assert물건(s.items.get(2), 7, false, "상가,오피스텔,근린시설", "103,000,000",
				"27,001,000", "일괄매각\r\n" + "대금지급기일(기한)이후 지연이자율 연2할\r\n"
						+ "임대차: 물건명세서와 같음\r\n" + "대지권 미등기임");

		assert물건(s.items.get(3), 8, false, "상가,오피스텔,근린시설", "60,000,000",
				"15,729,000", "일괄매각\r\n" + "대금지급기일(기한)이후 지연이자율 연2할\r\n"
						+ "임대차: 물건명세서와 같음\r\n" + "대지권 미등기임");
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
		List<사건> res = parser.parsePage(fixture02, null, null);
		사건 s = res.get(0);

	}

	private void assert물건(물건 item, int i, boolean b, String type, String price,
			String lowPrice, String bigo) {
		assertEquals(i, item.get물건번호());
		assertEquals(type, item.get물건종류());
		assertFalse(b);
		assertEquals(price, item.get가격());
		assertEquals(lowPrice, item.get최저가());
		// assertEquals(bigo, item.get비고());
	}

	void assert건물(물건현황 bld, String address, String detail) {
		assertEquals(address + "=" + bld.get주소(), address.trim(), bld.get주소());
		// assertEquals(detail, bld.getDetail());
	}

	public void testSliceSagun() {
		String[] res = parser.sliceSagun(fixture01);
		assertTrue(res[0].contains("2003타경8078"));
		assertFalse(res[0].contains("2003타경28874"));
		assertTrue(res[1].contains("2003타경28874"));
		assertFalse(res[1].contains("2003타경8078"));
	}

	public void testParsePage() throws ParserException {
		List<사건> res = parser.parsePage(fixture01, null, null);
		사건 s = res.get(0);
		물건 item1 = s.items.get(0);
		assert물건(item1, 4, false, "상가,오피스텔,근린시설", "120,000,000", "31,458,000",
				"일괄매각\n" + "대금지급기일(기한)이후 지연이자율 연2할\n" + "임대차:  물건명세서와 같음\n"
						+ "대지권 미등기임");

		물건현황 bld1 = item1.get상세내역(0);
		assert건물(bld1, "서울특별시 중구 을지로6가  18-185외 3필지밀리오레 6층 148호",

		"1동의 건물의 표시\n" + "서울 중구 을지로6가 18-185, 18-17, 18-184, 18-212\n"
				+ "밀리오레\n" + "철골, 철근콘크리트조 평스라브지붕 20층 판매, 업무시설\n" + " \n"
				+ "전유부분의 건물의 표시\n" + "   건물의번호 : 6층 제148호\n"
				+ "   구      조 : 철골, 철근콘크리트조\n" + "   면      적 : 3.84㎡");

		assert건물(item1.get상세내역(1), "서울특별시 중구 을지로6가  18-184",
				"대 1190.1㎡  (갑구순위 제61호 1190.1분의 0.33 박민원 지분)");

		assert물건(s.items.get(1), 6, false, "상가,오피스텔,근린시설", "120,000,000",
				"31,458,000", "일괄매각\n" + "대금지급기일(기한)이후 지연이자율 연2할\n"
						+ "임대차:  물건명세서와 같음\n" + "대지권 미등기임");

		assert물건(s.items.get(2), 7, false, "상가,오피스텔,근린시설", "103,000,000",
				"27,001,000", "일괄매각\n" + "대금지급기일(기한)이후 지연이자율 연2할\n"
						+ "임대차:  물건명세서와 같음\n" + "대지권 미등기임");

		assert물건(s.items.get(3), 8, false, "상가,오피스텔,근린시설", "60,000,000",
				"15,729,000", "일괄매각\n" + "대금지급기일(기한)이후 지연이자율 연2할\n"
						+ "임대차:  물건명세서와 같음\n" + "대지권 미등기임");
	}

	public void testParse명세서포함사건() throws ParserException {
		String[] slices = parser.sliceSagun(fixture01);
		String src = parser.fixSlice(slices[1]);
		ns = parser.parseDOM(src);
		사건 s = new 사건(null, null);
		parser.parse사건(s, ns);

	}

	public void testSliceSagun2() {
		String[] res = parser.sliceSagun(fixture02);
		assertTrue(res.length > 0);
		assertTrue(res[0].contains("2003타경28874"));

		assertFalse(res[0].contains("2004타경11477"));
		assertTrue(res[1].contains("2004타경11477"));
		assertFalse(res[1].contains("2003타경28874"));
	}

	public void testParseProblem1() throws ParserException {
		String[] slices = parser.sliceSagun(fixture03);
		String src = parser.fixSlice(slices[1]);
		ns = parser.parseDOM(src);
		사건 s = new 사건(null, null);
		parser.parse사건(s, ns);
	}
}
