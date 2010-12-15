package net.narusas.aceauction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class RegularExpressTest extends TestCase {
	public void test1() {
		String src = "<table col=\"3\">aaaa</table><abc>";
		Pattern p = Pattern.compile("<([/]*table[^>]*)>");
		Matcher m = p.matcher(src);
		while (m.find()) {
		}
	}

	public void test2() {
		String src = "<td align=center>◈ 감정시점 : 2003-10-212</td>";
		Pattern p = Pattern.compile("감정시점 : (\\d+-\\d+-\\d+)");
		Matcher m = p.matcher(src);
		while (m.find()) {
		}
	}

	public void testTD() {
		String src = "<td abc><TD>";
		Pattern p = Pattern.compile("(<[Tt][Dd][^>]*>)");
		Matcher m = p.matcher(src);
		while (m.find()) {
		}
	}

	// public void testSplit사건번호() {
	// String src = "20030130008078";
	// 사건 s = new 사건(null, null);
	// s.set사건번호(src);
	// assertEquals("2003", s.getEventYear());
	//
	// assertEquals("8078", s.getEventNo());
	//
	// }

	public void test입찰방법() {
		Pattern p = Pattern.compile("입찰방법 : ([^<]+)");
		Matcher m = p.matcher("align=absmiddle> 입찰방법 : 기일입찰</td>");
		m.find();
		assertEquals("기일입찰", m.group(1));
	}

	public void test배당요구종기() {
		Pattern p = Pattern.compile("배당요구종기 : ([^<]+)");
		Matcher m = p.matcher("align=absmiddle> 배당요구종기 : 2006-08-08</td>");
		m.find();
		assertEquals("2006-08-08", m.group(1));
	}

	public void test경매개시결정() {
		Pattern p = Pattern.compile("경매개시결정 : ([^<]+)");
		Matcher m = p.matcher("align=absmiddle> 경매개시결정 : 2006-05-10</td>");
		m.find();
		assertEquals("2006-05-10", m.group(1));
	}

	public void test사건접수() {
		Pattern p = Pattern.compile("사건접수\\s+([^<^\\s]+)");
		Matcher m = p.matcher("<td align=right>사건접수 2006-05-09 <font color=#F11D42>");
		m.find();
		assertEquals("2006-05-09", m.group(1));
	}

	public void test지분() {
		Pattern p = Pattern.compile("\\(([^\\)]*지분[^\\)]*)\\)");
		Matcher m = p.matcher("aa(bbb지분중얼마ccc)ddd");
		assertTrue(m.find());
		assertEquals("bbb지분중얼마ccc", m.group(1));
	}

	public void testPDF() {
		Pattern p = Pattern.compile("src=(http[^>]+)");
		Matcher m = p.matcher("name=iPdf src=http://222.237.76.182:8001/20061215/e03_2006-8370_1_1.pdf>");
		assertTrue(m.find());
		assertEquals("http://222.237.76.182:8001/20061215/e03_2006-8370_1_1.pdf", m.group(1));
	}

	public void testPhoto() {
		String src = "javascript:onclick=fn_view_img('http://222.237.76.182/E01/2006/972201_E01_16849_1.JPG');";
		Pattern p3 = Pattern.compile("(http://[^/]+/[a-zA-Z]\\d+/[^\\s^'^\"]+[jJ][pP][gG])");

		Matcher m = p3.matcher(src);
		assertTrue(m.find());
		// System.out.println(m.group(1));
	}

	public void test현황조사서() {
		String src = "<a href=\"javascript:loadHyun( '000210', \n" + "												  '20000130011660', \n"
				+ "												  '2000타경11660', \n" + "												  '<br>2002타경24387<br>(중복)',\n"
				+ "												  '123', \n" + "												  '456', \n" + "												  '' \n"
				+ "											     );\" id=\"a1\">";

		Pattern p1 = Pattern.compile("loadHyun\\( \\'(\\d+)\\'", Pattern.MULTILINE);
		Matcher m1 = p1.matcher(src);
		assertTrue(m1.find());
		assertEquals("000210", m1.group(1));

		Pattern p2 = Pattern.compile("\\'([^\\']*)\\'", Pattern.MULTILINE);
		Matcher m2 = p2.matcher(src);
		assertTrue(m2.find(m1.end()));
		assertEquals("20000130011660", m2.group(1));

		assertTrue(m2.find(m2.end()));
		assertEquals("2000타경11660", m2.group(1));

		assertTrue(m2.find(m2.end()));
		assertEquals("<br>2002타경24387<br>(중복)", m2.group(1));

		assertTrue(m2.find(m2.end()));
		assertEquals("123", m2.group(1));

		assertTrue(m2.find(m2.end()));
		assertEquals("456", m2.group(1));

		assertTrue(m2.find(m2.end()));
		assertEquals("", m2.group(1));

		assertFalse(m2.find(m2.end()));
	}

	public void test감정평가서() {
		String src = "<a href=\"javascript:loadGam( '000412',\n" + "			 '20050130030813', \n" + "			 '2005타경30813', \n"
				+ "			 '<br>2006타경11192<br>(중복)',\n" + "			 '20050130030813#', \n" + "			 '2005타경30813#', \n"
				+ "			 '20050130030813', \n" + "			 '2005타경30813', \n" + "			 '', \n" + "			 '2007.04.17',\n"
				+ "			 '33253' \n" + "		    );\" id=\"a1\">\n";

		Pattern p1 = Pattern.compile("loadGam\\( \\'(\\d+)\\'", Pattern.MULTILINE);
		Matcher m1 = p1.matcher(src);
		assertTrue(m1.find());
		assertEquals("000412", m1.group(1));

		Pattern p2 = Pattern.compile("\\'([^\\']*)\\'", Pattern.MULTILINE);
		Matcher m2 = p2.matcher(src);
		assertTrue(m2.find(m1.end()));
		assertEquals("20050130030813", m2.group(1));

		assertMatcher(m2, "2005타경30813");
		assertMatcher(m2, "<br>2006타경11192<br>(중복)");
		assertMatcher(m2, "20050130030813#");
		assertMatcher(m2, "2005타경30813#");
		assertMatcher(m2, "20050130030813");
		assertMatcher(m2, "2005타경30813");
		assertMatcher(m2, "");

		assertMatcher(m2, "2007.04.17");
		assertMatcher(m2, "33253");

	}

	public void assertMatcher(Matcher m, String target) {
		assertTrue(m.find());
		assertEquals(target, m.group(1));
	}

	public void test제시외건물주소1() {

		// String[] buf = src.split(" ");
		// for(int i=0; i<buf.length;i++) {
		//			
		// }
		Pattern p = Pattern.compile(" (\\d+[-,]*\\d*[\\S\\s]*)$");
		Matcher m = p.matcher("경기도 포천시 이동면 도평리 272-1");
		assertTrue(m.find());
		assertEquals("272-1", m.group(1));

		m = p.matcher("경기도 포천시 이동면 도평리 272-1, 272-2");
		assertTrue(m.find());
		assertEquals("272-1, 272-2", m.group(1));

	}

	public void test제시외건물주소2() {
		Pattern p = Pattern.compile("(\\d+[-,]*\\d*[\\S\\s]*)$");

		String[] buf = "경기도 포천시 이동면 도평리 272-1, 272-2".split(" ");
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			Matcher m = p.matcher(buf[i]);
			if (m.find()) {
				res.append(buf[i - 1]).append(" ").append(buf[i]);
				i++;
				for (; i < buf.length; i++) {
					res.append(" ").append(buf[i]);
				}
			}
		}
		assertEquals("도평리 272-1, 272-2", res.toString());
	}

	public void test제시외건물층형() {
		String src = "조적조 스레트지붕 단층";
		String floor = null;
		if (src.endsWith("층")) {
			floor = src.substring(src.length() - 2);
		}
		assertEquals("단층", floor);
	}

	public void testDot() {
		String src = "2007.03.07";
		String[] buf = src.split("\\.");
		assertEquals("2007", buf[0]);

	}

	public void testSplitDate() {
		String src = "2007.04.13 ~ 2007.04.20";
		String[] temp = src.split("~");
		assertEquals("2007.04.13", temp[0].trim());
		assertEquals("2007.04.20", temp[1].trim());
	}

	public void testBecause() {
		Pattern becausePattern = Pattern.compile("(\\d+년\\s*\\d+월\\s*\\d+일\n)");
		Matcher m = becausePattern.matcher("2004년3월10일\n서울중앙지방법원의\n강제경매개시결정(2004\n타경7836)");
		assertTrue(m.find());
		assertEquals("2004년3월10일\n", m.group(1));

	}

	public void test사건장소시간() {
		String src1 = "매각기일 :<b> 2007.05.15 (10:00, 제106호 법정)";
		String src2 = "매각기일 :<b> 2007.05.07 ((1)10:00 (2)14:00 , 제106호 법정)";
		assertInfo("2007.05.15", "10:00", "제106호 법정", src1);
		assertInfo("2007.05.07", "(1)10:00 (2)14:00", "제106호 법정", src2);
	}

	private void assertInfo(String date, String time, String location, String src) {
		Pattern p = Pattern.compile("매각기일 :<b> (\\d\\d\\d\\d.\\d+.\\d+) \\(([^,]*), (.+)\\)");
		Matcher m = p.matcher(src);
		m.find();
		assertEquals(date, m.group(1));
		assertEquals(time, m.group(2).trim());
		assertEquals(location, m.group(3));

	}

	public void testResult() {
		String src = "매각(2,000)";
		Pattern p = Pattern.compile("\\(([^\\)]+)\\)");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("2,000", m.group(1));

	}

	public void testExtractExt() {
		String name = "aaa.pdf";
		String ext = name.substring(name.indexOf(".") + 1);
		assertEquals("pdf", ext);

	}

	public void testArea() {
		Pattern p = Pattern.compile("전유부분의건물의표시:\\s*[^\\s]*\\s*([\\d.]+\\s*㎡)", Pattern.MULTILINE);
		Matcher m = p.matcher("전유부분의건물의표시: 철골철근콘크리트조\n40.17㎡");
		assertTrue(m.find());
		assertEquals("40.17㎡", m.group(1));

		String fix = "전유부분의 건물의 표시\n    건물의번호 : 1층 201호\n    구      조 : 철근콘크리트조 47.47㎡";
		p = Pattern.compile("    구      조 : [^\\d]*([\\d.]+㎡)$", Pattern.MULTILINE);

		m = p.matcher(fix);
		assertTrue(m.find());
		assertEquals("47.47㎡", m.group(1).trim());
	}

	public void testStr() {
		Pattern p = Pattern.compile("전유부분의건물의표시:\\s*([^\\s]*)\\s*[\\d.]+\\s*㎡", Pattern.MULTILINE);
		Matcher m = p.matcher("전유부분의건물의표시: 철골철근콘크리트조\n40.17㎡");
		assertTrue(m.find());
		assertEquals("철골철근콘크리트조", m.group(1));

		String fix = "전유부분의 건물의 표시\n    건물의번호 : 1층 201호\n    구      조 : 철근콘크리트조 47.47㎡";
		p = Pattern.compile("    구      조 : ([^\\d]*)\\s*[\\d.㎡]*$", Pattern.MULTILINE);
		m = p.matcher(fix);
		assertTrue(m.find());
		assertEquals("철근콘크리트조", m.group(1).trim());
	}

	public void testBld() {
		Pattern p = Pattern.compile("1동건물의표시: (.*)$", Pattern.MULTILINE);
		Matcher m = p.matcher("1동건물의표시: 철근콘크리트구조 기타지붕");
		assertTrue(m.find());
		assertEquals("철근콘크리트구조 기타지붕", m.group(1));
	}

	public void testSeparatedNumber() {
		Pattern p = Pattern.compile("([\\d]+[,\\d\\s]*).(.*)$");
		Matcher m = p.matcher("1. 서울특별시종로구청운동4-7");
		assertTrue(m.find());
		assertEquals("1", m.group(1));

		m = p.matcher("1, 2. 서울특별시종로구청운동4-7");
		assertTrue(m.find());
		assertEquals("1, 2", m.group(1));
	}

	public void testTextArea() {
		String src = "1층 82.49㎡";
		Pattern p = Pattern.compile("(\\d+층|지층|지하실)\\s*([\\d.]*㎡)");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("1층", m.group(1));
		assertEquals("82.49㎡", m.group(2));

		src = "    1층 벽돌조 슬래브지붕 주택\n"//
				+ "    112.74㎡\n";
		p = Pattern.compile("(\\d+층|지층|지하실)[^$]*$\\s*([\\d.]*㎡)", Pattern.MULTILINE);
		m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("1층", m.group(1));
		assertEquals("112.74㎡", m.group(2));

	}

	public void testStartWithSpace() {
		String src = "    133.49㎡\n";
		Pattern p = Pattern.compile("^\\s+([\\d.]*㎡)[\\n$]");
		Matcher m = p.matcher(src);
		assertTrue(m.find());

		src = "    1층 188.03㎡\n";
		m = p.matcher(src);
		assertFalse(m.find());
	}

	public void testAdditionalWord() {
		Pattern p = Pattern.compile("(단층\\s*[^\\s^\\n]*)");
		Matcher m = p.matcher("    벽돌조 시멘트기와지붕단층주택\n");
		assertTrue(m.find());
		assertEquals("단층주택", m.group(1));

		m = p.matcher("    벽돌조 시멘트기와지붕단층 공장\n");
		assertTrue(m.find());
		assertEquals("단층 공장", m.group(1));
	}

	public void testMultilineSeach() {
		String src = "    철골조 인슈판넬지붕 단층 사무실\n"//
				+ "    및 식당 387㎡\n";

		Pattern p = Pattern.compile("(단층[\\s\\n]*[^\\d]*)", Pattern.MULTILINE);
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("단층 사무실\n    및 식당 ", m.group(1));
	}

	public void testParseDate() {
		Pattern p = Pattern.compile("(\\d{4,4})[.-]+\\s*(\\d+)[.-]+\\s*(\\d+)");
		Matcher m = p.matcher("2002-1-22");
		assertTrue(m.find());

		assertEquals("2002", m.group(1));
		assertEquals("1", m.group(2));
		assertEquals("22", m.group(3));

		m = p.matcher("2002.1.22");
		assertTrue(m.find());

		assertEquals("2002", m.group(1));
		assertEquals("1", m.group(2));
		assertEquals("22", m.group(3));

		m = p.matcher("2002-0317");
		assertFalse(m.find());

		m = p.matcher("2002");
		assertFalse(m.find());

	}

	public void testRemoveBrace() {
		Pattern p = Pattern.compile("(\\([^\\)]+\\))");
		Matcher m = p.matcher("aa(bb)cc");
		assertTrue(m.find());
		assertEquals("(bb)", m.group(1));

	}

	public void testGamjungSrc() {
		String line = "(주주주주))써브감정평가법인써브감정평가법인써브감정평가법인써브감정평가법인";
		// Pattern[] patterns = new Pattern[] { Pattern.compile("(\\w+감정평가법인)"),
		// Pattern.compile("(\\w+감정평가사)"), Pattern.compile("(\\w+감정원)"),
		// Pattern.compile("(\\w+법인)"), Pattern.compile("(\\w+사무소)"),
		// Pattern.compile("(\\w+감정원)"),
		//
		// };
		Pattern p = Pattern.compile("감정평가법인");
		Matcher m = p.matcher(line);
		assertTrue(m.find());
		String splited = line.substring(0, m.start());

		assertEquals("(주주주주))써브", splited);
		Pattern p2 = Pattern.compile("([^\\(\\)]+)$");
		Matcher m2 = p2.matcher(splited);
		assertTrue(m2.find());
		assertEquals("써브", m2.group(1));

	}

	public void testTwoDate() {
		String src = "2007.12.20\n(2007.12.06~2007.12.13)";
		Pattern p = Pattern.compile("\\((\\d+.\\d+.\\d+)~(\\d+.\\d+.\\d+)\\)");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("2007.12.06", m.group(1));
		assertEquals("2007.12.13", m.group(2));

	}

//	public void testExtractTwoDate() {
//		String src = "<tr bgcolor=\"#FFFFFF\" valign=\"middle\" align=\"center\">\n" +
//				"                  <td width=\"20%\" height=\"26\">2007.12.06 ~ 2007.12.13<br>( 매각기일 2007.12.20 )</td>";
//		
//		Pattern p2 = Pattern
//		.compile(">\\s*(\\d\\d\\d\\d.\\d+.\\d+\\s*~\\s*\\d\\d\\d\\d.\\d+.\\d+)");
//		Matcher m = p2.matcher(src);
//		assertTrue(m.find());
//		assertEquals("2007.12.06", m.group(1));
//		assertEquals("2007.12.13", m.group(2));
//	}
	
	public void test매수인(){
		String src = "매수인김영수/ 입찰10명 /매각567,900,000원(350%)";
		String[] tokens = src.split("/");
		Pattern p = Pattern.compile("매수인(.*)");
		Matcher m = p.matcher(tokens[0]);
		assertTrue(m.find());
		
		p = Pattern.compile("입찰(.*)");
		m = p.matcher(tokens[1]);
		assertTrue(m.find());
		
		String[] tokens2 = tokens[2].split("\\(");
		p = Pattern.compile("매각(.*)");
		m = p.matcher(tokens2[0]);
		assertTrue(m.find());
		assertEquals("567,900,000원", m.group(1));
		
		String t = tokens2[1].replaceAll("\\)", "");
		assertEquals("350%", t);
		
		
	}
	
	public void testParseDetailPHP() {
		String src = "detail.php?ssid=22176ce72cc28c0f50891746df061d49&gubun=21";
		Pattern p = Pattern.compile("detail.php\\?ssid=([^&]+)&gubun=([^\"]+)");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("22176ce72cc28c0f50891746df061d49", m.group(1));
		assertEquals("21", m.group(2));
		
	}
	
	public void testParam() {
		String src = "<param name=\"ssid\" value=\"e39066493d81932d83cae74662177c54\">\n<param name=\"fsize\" value=\"69544\">";
		Pattern p = Pattern.compile("fsize\" value=\"([^\"]+)");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("69544", m.group(1));
	}
}
