package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.si.auction.builder.present.대지권현황;
import net.narusas.si.auction.model.물건;
import net.narusas.util.lang.NFile;

public class GoodsFetcherTest extends TestCase {
	private String html;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		html = NFile.getText(new File("fixture2/009_물건내역.html"));

	}

	public void testParse() {
		물건내역Fetcher fetcher = new 물건내역Fetcher();
		물건 goods = new 물건();
		// fetcher.parse(html, goods);
	}

	public void testParseHTML() {
		assertEquals("일괄매각. 제시외건물포함 대금지급기일(기한)이후지연이자율:연2할", HTMLUtils.strip(HTMLUtils.stripCRLF(HTMLUtils
				.findTHAndNextValueAsComplex(html, "물건비고"))));

		Pattern p = Pattern.compile("목록(\\d+)\\s*소재지");
		Matcher m = p.matcher(html);
		assertTrue(m.find());
		assertEquals("1", m.group(1));
		assertTrue(m.find());
		assertEquals("2", m.group(1));
		assertFalse(m.find());
		assertEquals("(대지) 서울특별시 종로구 부암동 306-7", HTMLUtils.strip(HTMLUtils.stripCRLF(HTMLUtils
				.findTHAndNextValueAsComplex(html, "목록1 소재지"))));
		assertEquals("(단독주택) 서울특별시 종로구 부암동 306-7", HTMLUtils.strip(HTMLUtils.stripCRLF(HTMLUtils
				.findTHAndNextValueAsComplex(html, "목록2 소재지"))));

		assertEquals("2005.09.30", HTMLUtils.findTHAndNextValue(html, "경매개시일"));
		assertEquals("2006.01.16", HTMLUtils.findTHAndNextValue(html, "배당요구종기"));
		assertEquals("2005.09.29", HTMLUtils.findTHAndNextValue(html, "사건접수"));
		assertEquals("473,389,500원", HTMLUtils.findTHAndNextValue(html, "감정평가액"));
		assertEquals("473,389,500원", HTMLUtils.findTHAndNextValue(html, "최저매각가격"));
		assertEquals("200,000,000원", HTMLUtils.findTHAndNextValue(html, "청구금액"));
		assertEquals("2009.03.19 10:00 경매법정", HTMLUtils.strip(HTMLUtils.stripCRLF(HTMLUtils
				.findTHAndNextValue(html, "매각기일"))));

		Sheet sheet1 = Sheet.parse(html, "<caption>기일내역</caption>");
		assertEquals("기일", sheet1.getHeader(0));
		assertEquals("기일종류", sheet1.getHeader(1));
		assertEquals("기일장소", sheet1.getHeader(2));
		assertEquals("최저매각가격", sheet1.getHeader(3));
		assertEquals("기일결과", sheet1.getHeader(4));

		assertEquals("2009.03.19 (10:00)", sheet1.valueAt(0, 0));
		assertEquals("매각기일", sheet1.valueAt(0, 1));
		assertEquals("경매법정", sheet1.valueAt(0, 2));
		assertEquals("473,389,500원", sheet1.valueAt(0, 3));
		assertEquals("", sheet1.valueAt(0, 4));

		assertEquals("2009.03.26 (14:00)", sheet1.valueAt(1, 0));
		assertEquals("매각결정기일", sheet1.valueAt(1, 1));
		assertEquals("경매법정", sheet1.valueAt(1, 2));
		assertEquals("", sheet1.valueAt(1, 3));
		assertEquals("", sheet1.valueAt(1, 4));

		Sheet sheet2 = Sheet.parse(html, "<caption>목록내역</caption>", true, true);
		assertEquals("목록번호", sheet2.getHeader(0));
		assertEquals("목록구분", sheet2.getHeader(1));
		assertEquals("상세내역", sheet2.getHeader(2));

		assertEquals("1", sheet2.valueAt(0, 0));
		assertEquals("토지", sheet2.valueAt(0, 1));
		assertEquals("대 261㎡", HTMLUtils.strip(HTMLUtils.stripCRLF(sheet2.valueAt(0, 2))));

		assertEquals("2", sheet2.valueAt(1, 0));
		assertEquals("건물", sheet2.valueAt(1, 1));
//		System.out.println(sheet2.valueAt(1, 2));
		assertTrue(sheet2.valueAt(1, 2).contains("세멘벽돌조"));

		부동산표시목록Parser parser = new 부동산표시목록Parser();
		String temp = sheet2.valueAt(1, 2);
		temp = 부동산표시목록Parser.convertAreaUnit(temp);
//		System.out.println(temp);
//		System.out.println("############");
		List<String> chunks = 부동산표시목록Parser.parseStructure(temp);
//		System.out.println(chunks);
		전유부분 전유부분 = null;

//		for (String chunk : chunks) {
//			chunk = 부동산표시목록Parser.convertAreaUnit(chunk);
//			if (chunk.startsWith("전유부분의 건물의 표시")) {
//				System.out.println( new 전유부분(chunk));
//			} else if (chunk.startsWith("대지권의 목적인 토지의 표시") || chunk.startsWith("대지권의 표시")) {
//				System.out.println(new 대지권현황(chunk, ""));
//			} else if (chunk.contains("위 지상")) {
//				System.out.println(new 위지상(chunk,""));
//			} else if (chunk.contains("매각지분")) {
//				System.out.println(chunk);
//			}
//		}

	}
	
	public void testParse부동산표시목록() throws IOException{
		String html = NFile.getText(new File("fixture2/013_물건내역.html"));
		Sheet sheet2 = Sheet.parse(html, "<caption>목록내역</caption>", true, true);
		부동산표시목록Parser parser = new 부동산표시목록Parser();
		String temp = sheet2.valueAt(0, 2);
		assertEquals("1동의 건물의 표시\n"+
				"    서울특별시 종로구 숭인동 1479\n"+
				"    철근콘크리트 및 별돌조\n"+
				"    평스라브지붕 다세대주택\n"+
				"    1층 73.92㎡\n"+
				"    2층 83.58㎡\n"+
				"    3층 70.00㎡\n"+
				"    4층 48.00㎡\n"+
				"    지층 89.32㎡\n"+
				"    대한빌라\n"+
				"\n"+
				"전유부분의 건물의 표시\n"+
				"    건물의 번호 : 지1층 비1호\n"+
				"    구          조 : 철근콘크리트 및 벽돌조 82.12㎡\n"+
				"\n"+
				"대지권의 목적인 토지의 표시\n"+
				"    토 지 의  표시 : 1. 서울특별시종로구숭인동1479\n"+
				"                            대 159㎡\n"+
				"    대지권의 종류 : 1. 소유권\n"+
				"    대지권의 비율 : 1. 159 분의 53.33"+
				"", temp);
		temp = 부동산표시목록Parser.convertAreaUnit(temp);
		List<String> chunks = 부동산표시목록Parser.parseStructure(temp);
		String address = "";
		for (String str : chunks) {
			System.out.println("##########################");
			System.out.println(str);
			System.out.println("#########");
			if (str.startsWith("전유부분의 건물의 표시")) {
				전유부분 전유부분 = 전유부분Parser.parse(str);
				System.out.println(전유부분);
			} else if (str.startsWith("대지권의 목적인 토지의 표시") || str.startsWith("대지권의 표시")) {
				대지권현황 대지권 = new 대지권현황(str, address.toString());
				System.out.println(대지권);
			} else if (str.contains("위 지상")) {
				위지상 위지상 = new 위지상(str, address.toString());
				System.out.println(위지상);
			} else if (str.contains("매각지분")) {
				String 매각지분 = str;
				System.out.println(매각지분);
			}
		}
	}
	
	public void testParse감정평가서() throws IOException {

		String html = NFile.getText(new File("fixture2/013_물건내역.html"));
		Pattern p = Pattern.compile("<li><p class=\"law_title\">\\d+\\)\\s*([^<]+)</p>\\s*<ul><li><span[^>]+>(.*)</span>", Pattern.MULTILINE);
		Matcher m = p.matcher(html);
		String[] names = {"위치 및 주위환경", "교통상황","건물의 구조","이용상태","설비내역","토지의 형상 및 이용상태","인접 도로상태등",
				"토지이용계획 및 제한상태","공부와의 차이","기타참고사항(임대관례 및 기타)"};
		String[] values = {"본건은 서울특별시 종로구 숭인동 소재 '현대힐스테이트' 북동측 인근에 위치하며, 부근<br />은 주상용 건물, 소규모 점포, 다세대주택 등 공동주택, 단독주택, 학교, 공공시설 등이<br />혼재함.",
				"본건까지 차량접근이 가능하며, 버스정류장 및 지하철1ㆍ2호선(신설동역)이 인근에 소재<br />하는 등 대중교통여건은 보통임.",
				"철근콘크리트 및 벽돌조 평스라브지붕 지하1층 지상4층건으로서,<br />-외벽 : 적벽돌치장쌓기 및 일부 석재붙임 마감,<br />-내벽 : 벽지 및 일부 타일붙임, 페인트 마감 등,<br />-창호 : 칼라샷시, 하이샷시 창호임.",
				"ㆍ지1층 비1호 : 근린생활시설(현황 공실),<br />ㆍ2층 201호 : 다세대주택(방, 주방, 욕실, 보일러실 등 : 현황 공실),<br />ㆍ2층 202호 : 다세대주택(방, 주방, 욕실, 보일러실 등 : 현황 공실),<br />ㆍ3층 301호 : 다세대주택(원룸, 욕실 등),<br />ㆍ3층 302호 : 다세대주택(원룸 등 : 현황 공실),<br />ㆍ4층 401호 : 다세대주택(방2, 주방 및 거실, 욕실 등)로 이용중임.<br /><br />",
				"도시가스보일러에 의한 난방시설이며, 위생설비 및 급배수시설 등이 구비되어 있음.<br />",
				"부정형의 토지로, 주상용 건부지로 이용중임.",
				"본건 동측으로 노폭 약10미터 도로와 접함.",
				"도시지역, 제2종일반주거지역, 지구단위계획구역, 도로(접함), 대공방어협조구역(위탁고<br />도:54-236m)<군사기지및군사시설보호법>, 상대정화구역(대광중ㆍ고등학교)<학교보건법>,<br />동대문지구단위계획결정고시:2002.4.6(서고시제2002-112호)/건폐율:60%/기준용적률:150%<br />.허용용적률:200%/최대개발규모:500㎡이하/전층불허용도(D)등세부사항은건축과로문의임.",
				"ㆍ집합건축물대장상 1층 주차장(63.12㎡)은 '공용부분'이나, 현황 '상가(대한공사)'로<br />  이용중임.<br /><br />< 종물 및 부합물 ><br />ㆍ기호㉠(다용도실) 약8.54㎡가 소재하며, 제시목록 기호4(3층 301호)에 부합함.<br />ㆍ기호㉡(욕실) 약5.04㎡가 소재하며, 제시목록 기호5(3층 302호)에 부합함.<br />ㆍ기호㉢(방 등 증축부분) 약12.48㎡가 소재하며, 제시목록 기호6(4층 401호)에 부합함.",
				"ㆍ임대관계 등 미상임.<br />ㆍ본건 공히 토지 별도등기 있으며 등기부등본상 다음과 같음.<br />  1토지(갑구3번 가압류등기, 을구1번, 2번, 3번, 4번, 5번, 6번 근저당권 설정등기)임."
				
				
		};
		for(int i=0;i<names.length;i++){
			String name = names[i];
			String value =values[i];
			assertTrue(m.find());
			assertEquals(name, m.group(1));
			assertEquals(value, m.group(2));
		}
		
		
	}
	
	public void testParse인근매각현황표 () throws IOException {
		String html = NFile.getText(new File("fixture2/041_물건_인근매각현황.html"));
		String chunk = html.substring(html.indexOf("<table class=\"Ltbl_list\" summary=\"인근매각통계 표\">"));
//		chunk = chunk.substring(0, chunk.indexOf("</tbody"));
//		System.out.println(chunk);
		Sheet sheet = Sheet.parse(chunk, "인근매각통계");
		
//		System.out.println(sheet);
		for (int i = 0; i < sheet.rowSize(); i++) {
			System.out.println("####################");
			for(int col=0;col<6;col++){
				System.out.println(sheet.valueAt(i, col));	
			}
			
			
			
		}
	}
}
