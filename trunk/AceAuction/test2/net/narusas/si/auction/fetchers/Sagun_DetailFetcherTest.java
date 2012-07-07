package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.si.auction.model.당사자;
import net.narusas.si.auction.model.사건;
import net.narusas.util.lang.NFile;

public class Sagun_DetailFetcherTest extends TestCase {
	private String html;
	private String html2;
	private String html3;
	private String html4;

	@Override
	public void setName(String name) {
		super.setName(name);
		try {
			html = NFile.getText(new File("fixture2/007_기일입찰_사건001_사건내역.html"));
			html2 = NFile.getText(new File("fixture2/008_기일입찰_사건2008_8950_복수물건.html"));
			html3 = NFile.getText(new File("fixture2/011_사건내역.html"));
			html4 = NFile.getText(new File("fixture2/015_사건내역_제시외건물포함.html"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testParseTHRegs() {
		assertEquals("2007타경11096", HTMLUtils.findTHAndNextValue(html, "사건번호"));
		assertEquals("부동산강제경매", HTMLUtils.findTHAndNextValue(html, "사건명"));
		assertEquals("2007.04.16", HTMLUtils.findTHAndNextValue(html, "개시결정일자"));
		assertEquals("2007.04.13", HTMLUtils.findTHAndNextValue(html, "접수일자"));
		assertEquals("경매3계&nbsp;&nbsp;\n        전화 : 530-1815(구내:1815)", HTMLUtils.findTHAndNextValue(html,
				"담당계"));

		assertEquals("8,380,000원", HTMLUtils.findTHAndNextValue(html, "청구금액"));
		assertEquals("", HTMLUtils.findTHAndNextValue(html, "사건항고/정지여부"));
		assertEquals("미종국", HTMLUtils.findTHAndNextValue(html, "종국결과"));
		assertEquals("", HTMLUtils.findTHAndNextValue(html, "종국일자"));

		String chunk = HTMLUtils.findTHAndNextValueAsComplex(html, "중복/병합/이송");
		assertNotNull(chunk);

		List<String> anchors = HTMLUtils.findAnchors(chunk);
		assertTrue(anchors.size() > 0);
		assertEquals("2007타경31731(중복)", HTMLUtils.strip(anchors.get(0)));
		assertEquals("2008타경18933(중복)", HTMLUtils.strip(anchors.get(1)));
		assertEquals("2008타경23676(중복)", HTMLUtils.strip(anchors.get(2)));


	}

	
	
	public void testSetUp사건() {
		사건 s = new 사건();
		new 사건내역Fetcher().parseHTML(s, html);
		assertEquals("부동산강제경매", s.get사건명());
		assertEquals(new Date(2007 - 1900, 4 - 1, 16), s.get개시결정일자());
		assertEquals(new Date(2007 - 1900, 4 - 1, 13), s.get접수일자());

		assertEquals(new Long(8380000), s.get청구금액());

		assertEquals("", s.get사건항고정지여부());
		// assertEquals("미종국", s.get종국결과());
		// assertEquals(null, s.get종국일자());
		assertEquals("2007타경31731(중복),2008타경18933(중복),2008타경23676(중복)", s.get병합());

		// 배당요구종기내역 detail = s.get배당요구종기내역();
		// assertNotNull(detail);
		//		
		// assertEquals(2, detail.size());
		// List<배당요구종기내역Item> items = detail.get배당요구종기내역Items();
		// assertEquals(2, items.size());
		// 배당요구종기내역Item item1 = items.get(0);
		// assertEquals("1", item1.get목록번호());
		// assertEquals("서울특별시 관악구 봉천동 959-12", item1.get소재지());
		// assertEquals("2007.06.25", item1.get배당요구종기일());
		//		
		//		
		// 배당요구종기내역Item item2 = items.get(1);
		// assertEquals("2", item2.get목록번호());
		// assertEquals("서울특별시 관악구 봉천동 959-12", item2.get소재지());
		// assertEquals("2008.10.31 (연기)", item2.get배당요구종기일());
	}

	// public void testParse배당요구종기내역(){
	// 배당요구종기내역 detail = new 사건_사건내역Fetcher().parse배당요구종기내역(html);
	// assertEquals(2, detail.size());
	// List<배당요구종기내역Item> items = detail.get배당요구종기내역Items();
	// assertEquals(2, items.size());
	// 배당요구종기내역Item item1 = items.get(0);
	// assertEquals("1", item1.get목록번호());
	// assertEquals("서울특별시 관악구 봉천동 959-12", item1.get소재지());
	// assertEquals("2007.06.25", item1.get배당요구종기일());
	//		
	//		
	// 배당요구종기내역Item item2 = items.get(1);
	// assertEquals("2", item2.get목록번호());
	// assertEquals("서울특별시 관악구 봉천동 959-12", item2.get소재지());
	// assertEquals("2008.10.31 (연기)", item2.get배당요구종기일());
	// }
	//	

	// TODO 물건은 일단 나중. 사건부터 모두 처리하고 볼것

	public void testParse물건내역() {
		assertEquals("1", HTMLUtils.strip(HTMLUtils.findTHAndNextValueAsComplex(html, "물건번호")));
		assertEquals("상가,오피스텔,근린시설", HTMLUtils.strip(HTMLUtils.findTHAndNextValue(html, "물건용도")));
		assertEquals("1,256,869,600원 (1,256,869,600원)", HTMLUtils.strip(HTMLUtils.stripCRLF(HTMLUtils
				.findTHAndNextValueAsComplex(html, "감정평가액<br />\\(최저매각가격\\)"))));
		assertEquals("-일괄매각. 제시외건물포함\n-대금지급기일(기한)이후 지연이자율 : 연2할\n-임대차 : 물건명세서와 같음", HTMLUtils.strip(HTMLUtils
				.findTHAndNextValueAsComplex(html, "물건비고")));

		assertEquals("매각준비 -> 매각공고", HTMLUtils.strip(HTMLUtils.findTHAndNextValueAsComplex(html, "물건상태")));

		assertEquals("2009.03.04", HTMLUtils.strip(HTMLUtils.findTHAndNextValueAsComplex(html, "기일정보")));

	}

	public void testParse당사자내역() {
		사건 event = new 사건();
		List<당사자> detail = new 사건내역Fetcher().parse당사자(event, html);
		assertEquals(2, detail.size());
	}

	public void testParse물건목록() {
		Pattern p = Pattern.compile("<th[^>]*>물건번호</th>\\s*<td[^>]*>\\s*(\\d+)", Pattern.MULTILINE);
		Matcher m = p.matcher(html2);
		int count = 0;
		while (m.find()) {
			count++;
			assertEquals("" + count, m.group(1));
		}
		assertEquals(58, count);
		사건 event = new 사건();
		assertEquals(58, new 사건내역Fetcher().parse물건(event, html2).size());
		assertEquals(1, new 사건내역Fetcher().parse물건(event, html).size());
	}

	public void testSetup2() {
		사건 s = new 사건();
		new 사건내역Fetcher().parseHTML(s, html3);
		assertEquals("2007타경36415(병합)",s.get병합());
	}
	
	public void test제시외건물() {
		assertNull(HTMLUtils.findTHAndNextValueAsComplex(html2, "제시외"));
		String chunk = HTMLUtils.findTHAndNextValueAsComplex(html4, "제시외");
		assertEquals("1.(용도)사무실및창고(구조)블럭조슬레이트지붕(면적)150㎡<br />" +
				"2.(용도)사무실(구조)블럭조슬레이트지붕(면적)24㎡<br />" +
				"3.(용도)화장실및욕실(구조)블럭조판넬지붕(면적)6㎡<br />" +
				"4.(용도)주방및욕실(구조)블럭조판넬지붕(면적)7.5㎡<br />" +
				"5.(용도)통로(구조)목조및블럭조기와지붕위판넬지붕(면적)6.7㎡<br />" +
				"6.(용도)주택및창고(구조)블럭조슬레이트지붕(면적)32㎡<br />" +
				"7.(용도)창고(구조)판넬조판넬지붕(면적)1㎡<br />" +
				"8.(용도)보일러실(구조)판넬조판넬지붕(면적)0.5㎡<br />" +
				"9.(용도)사무실(구조)판넬조판넬지붕(면적)4.5㎡"
				, chunk);
		String[] lines = chunk.split("<br />");
		Pattern usage = Pattern.compile("\\(용도\\)([^\\(]+)");
		Pattern structure = Pattern.compile("\\(구조\\)([^\\(]+)");
		Pattern area = Pattern.compile("\\(면적\\)([^\\(]+)");
		
		assertEquals("1.(용도)사무실및창고(구조)블럭조슬레이트지붕(면적)150㎡",lines[0]);
		Matcher m = usage.matcher(lines[0]);
		assertTrue(m.find());
		assertEquals( "사무실및창고",m.group(1));
		m = structure.matcher(lines[0]);
		assertTrue(m.find());
		assertEquals( "블럭조슬레이트지붕",m.group(1));
		m = area.matcher(lines[0]);
		assertTrue(m.find());
		assertEquals( "150㎡",m.group(1));
		
		
		assertEquals("2.(용도)사무실(구조)블럭조슬레이트지붕(면적)24㎡",lines[1]);
		assertEquals("3.(용도)화장실및욕실(구조)블럭조판넬지붕(면적)6㎡",lines[2]);
		assertEquals("4.(용도)주방및욕실(구조)블럭조판넬지붕(면적)7.5㎡",lines[3]);
		assertEquals("5.(용도)통로(구조)목조및블럭조기와지붕위판넬지붕(면적)6.7㎡",lines[4]);
		assertEquals("6.(용도)주택및창고(구조)블럭조슬레이트지붕(면적)32㎡",lines[5]);
		assertEquals("7.(용도)창고(구조)판넬조판넬지붕(면적)1㎡",lines[6]);
		assertEquals("8.(용도)보일러실(구조)판넬조판넬지붕(면적)0.5㎡",lines[7]);
		assertEquals("9.(용도)사무실(구조)판넬조판넬지붕(면적)4.5㎡",lines[8]);
	}
	
	public void test기일내역fetcher() {
		
	}
}
