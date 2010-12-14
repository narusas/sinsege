package net.narusas.si.auction.fetchers;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.converters.금액Converter;
import net.narusas.si.auction.model.주소;

import junit.framework.TestCase;

public class RegxTest extends TestCase {
	public void test지분() {
		String src = "매각지분 : 6802.7분의 5.096(규약상대지권비율)(갑구110번 대방시범아파트재건축조합 1,723분의 5.68 지분에 해당됨)";
		Pattern p = Pattern.compile("([\\d\\.]+\\s*분의\\s*[\\d\\.]+)");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("6802.7분의 5.096", m.group(1));
	}

	public void test결과금액() {
		Pattern p = Pattern.compile("([\\d,]+)");
		Matcher m = p.matcher("매각<br />(536,560,000원)");
		assertTrue(m.find());
		assertEquals("536,560,000", m.group(1));
	}

	public void testSlim주소() {
		Pattern slimAddrPattern = 주소.slimAddrPattern;

		Matcher m = slimAddrPattern.matcher("신당동 200-5 누죤빌딩 8층 246호");
		assertTrue(m.find());
		assertEquals("신당동 200-5", m.group(1));

		m = slimAddrPattern.matcher("사리면 중흥리 151-1");
		assertTrue(m.find());
		assertEquals("중흥리 151-1", m.group(1));

		m = slimAddrPattern.matcher("변산면 마포리 산86-101");
		assertTrue(m.find());
		assertEquals("마포리 산86-101", m.group(1));

		m = slimAddrPattern.matcher("변산면 마포리 산 86-101");
		assertTrue(m.find());
		assertEquals("마포리 산 86-101", m.group(1));

	}

	public void testOnlyDate() {
		Pattern datePattern = Pattern.compile("(\\d+\\.\\d+\\.\\d+)");
		String src = "2007.08.23.(2층201호), 2007.10.29.(2층 202호, 실제지번정정), 2008.05.23.(2층101호, 특수주소변동)";
		// System.out.println(src.length());
		Matcher m = datePattern.matcher(src);
		assertTrue(m.find());
		assertEquals("2007.08.23", m.group(1));
	}

	public void testParseArea() {
		String src = "대(현황:일부 전) 198㎡ [제시외건물 매각제외]";

		부동산표시목록Builder builder = new 부동산표시목록Builder();

		Pattern p = Pattern.compile("([\\d\\.\\s]+㎡)");
		String temp = builder.convertAreaUnit(src);
		// System.out.println(temp);
		Matcher m = p.matcher(temp);
		assertTrue(m.find());
		// System.out.println(m.group(1));

	}

	public void test사건번호중130포함되는것() {
		Long 사건번호 = new Long(20080130013005L);
		if (사건번호 == null) {
			return;
		}

		Pattern p = Pattern.compile("^(\\d\\d\\d\\d)0130(\\d+)");
		Matcher m = p.matcher(String.valueOf(사건번호));
		if (m.find() == false) {
			return;
		}

		assertEquals(2008, Integer.parseInt(m.group(1)));
		assertEquals(13005, Integer.parseInt(m.group(2)));
	}

	public void testReplaceDirSeparator() {
		String file = "a\\b\\c";
		assertEquals("a/b/c", file.replaceAll("\\\\", "/"));
	}

	public void test태그제거() {
		String src = "abc <img a f d e >def</img>";
		assertEquals("abc def", src.replaceAll("<[^>]+>", ""));
		assertEquals("100", "100원 200원".split("원")[0]);
	}

	public void test제시외건물면적예외상황() {
		assertEquals("23.4㎡", 제시외건물Parser.parse면적("1.(용도)가추(구조)철파이프조판넬지붕단층(면적)23.4㎡"));
		assertEquals("23.4㎡", 제시외건물Parser.parse면적("1.(용도)가추(구조)철파이프조판넬지붕단층(면적)(23.4)㎡"));
		assertEquals("450주", 제시외건물Parser.parse면적("13.(용도)수목(연산홍)(면적)(450)주"));
		assertNull(제시외건물Parser.parse면적("2. (용도)기계기구 재봉통(Model: KM-640BL)등 6점 "));

		assertNull(제시외건물Parser.regxGroup1(제시외건물Parser.structurePattern, "2. (용도)기계기구 재봉통(Model: KM-640BL)등 6점 "));
	}

	public void test제시외건물포함여부() {

		assertTrue(Pattern.matches("\\.", "."));

		assertTrue(Pattern.matches(".*제시외[^\\.]*제외.*", "과수목포함. 제시외건물매각제외. 점유자미상."));
	}

	public void testArea() {
		String src = "임야 13,019㎡";
		Pattern p = Pattern.compile("([\\d,]+)");
		Matcher m = p.matcher(src);
		String res = src;
		while (m.find()) {
			String token = m.group(1);
			res = res.replace(token, token.replaceAll(",", ""));
		}
		assertEquals("임야 13019㎡", res);
	}

	public void test제시외건물용도예외상황() {

		String regx = "\\(용도\\)([^\\구]+)";
		// 예전 내용
		// String res = 제시외건물Parser.regxGroup1(Pattern.compile(regx),
		// "1.(용도)주택일부(구조)철근콘크리트조슬래브지붕1.2층(면적)25");
		// assertEquals("주택일부", res);

		// 예외 상황 내용
		String res = 제시외건물Parser.regxGroup1(Pattern.compile(regx), "1.(용도)(주택일부)(구조)철근콘크리트조슬래브지붕1.2층(면적)25");
		res = res.replace('(', ' ');
		res = res.replace(')', ' ').trim();
		assertEquals("주택일부", res);

	}

	public void test목록분리() {
		String src = "<tr>\n"
				+ "	  <th>목록1</th>\n"
				+ "	  <td colspan=\"3\">\n"
				+ "	  \n"
				+ "	  서울특별시 서초구 서초동  1337-2 현대골든텔 지하2층 1호\n"
				+ "	  \n"
				+ "	  <a href=\"#\" onclick=\"regiBU('11021996120828'); return false;\"><img src=\"/images/ic_register.gif\" alt=\"등기부 팝업\" /></a>\n"
				+ "	  </td>\n" + "	  <th>목록구분</th>\n" + "	  <td width=\"11%\">집합건물</td>\n"
				+ "	  <th width=\"6%\">비고</th>\n" + "	  <td width=\"10%\"><span class=\"txtred\">취하</span>\n"
				+ "	  </td>\n" + "	</tr>";

		Pattern noPattern = Pattern.compile("<th>목록\\s*(\\d+)</th>\\s*<td[^>]+>([^<]+)");
		Matcher m = noPattern.matcher(src);
		assertTrue(m.find());
		assertEquals("1", m.group(1));
		assertEquals("서울특별시 서초구 서초동  1337-2 현대골든텔 지하2층 1호", m.group(2).trim());

		Pattern popupPattern = Pattern.compile("regiBU\\('(\\d+)");
		m = popupPattern.matcher(src);
		assertTrue(m.find());
		assertEquals("11021996120828", m.group(1));

		Pattern typePattern = Pattern.compile("<th[^>]*>목록구분</th>\\s*<td[^>]+>([^<]+)</td>\\s*");
		m = typePattern.matcher(src);
		assertTrue(m.find());
		assertEquals("집합건물", m.group(1));

		Pattern commentPattern = Pattern.compile("<th[^>]*>비고</th>\\s*<td[^>]+><span[^>]+>([^<]+)");
		m = commentPattern.matcher(src);
		assertTrue(m.find());
		assertEquals("취하", m.group(1));

		String tmp = " <th width=\"6%\">비고</th>\n" + "		  <td width=\"10%\">미종국\n	  </td>";
		Pattern commentPattern2 = Pattern.compile("<th[^>]*>비고</th>\\s*<td[^>]*>");
		m = commentPattern2.matcher(tmp);
		assertTrue(m.find());
		String chunk = tmp.substring(m.end(), tmp.indexOf("</td", m.end()));
		assertEquals("미종국\n	  ", chunk);

		m = commentPattern2.matcher(src);
		assertTrue(m.find());
		chunk = src.substring(m.end(), src.indexOf("</td", m.end()));
		assertEquals("취하", HTMLUtils.strip(chunk));

	}

	public void testNo() {
		String targetName = MessageFormat.format("{0,number,integer}_{1}_{2}", 1111, 2, 3);
		System.out.println(targetName);

	}

	public void testSpace() {
		String src = "청구 금액 금2,99 2,168,517 원";
		assertTrue(Pattern.compile("청\\s*구\\s*금\\s*액").matcher(src).find());

		// assertEquals("2,99 2,168,517",금액Converter.convert(src));
		Matcher m = Pattern.compile("([\\d,\\s]+\\s*원)").matcher(src);
		assertTrue(m.find());
		assertEquals("2,99 2,168,517 원", m.group(1));
		assertEquals("2992168517", 금액Converter.convert(m.group(1)));
	}

	public void testCity() {
		String src = "{\"CITY_NAME\":\"강남구\",\"CITY_CODE\":\"68\"},{";
		Pattern p2 = Pattern.compile("CITY_NAME\":\"([^\"]+)\",\"CITY_CODE\":\"([^\"]+)");
		Matcher m = p2.matcher(src);
		while (m.find()) {
//			System.out.println(m.group(1));
			
		}
	}
	
	public void test123() {
		String src = "\"SL_PRICE\":\"                43,000\",\"SP_USEAREA\":35,\"RL_PRICE\":\"                 4,000\",";
//		String src = "35";
//		String src = "\"                43,000\"";
		Matcher m = Pattern.compile("((?i)[\"][^\"]+[\"]|[^:]+):((?i)[\"][^\"]+[\"]|[^,]+)[,]*").matcher(src);
		while(m.find()){
			System.out.println("--------------------");
			System.out.println("1:"+m.group(1).trim());
			System.out.println("2:"+m.group(2).trim());
		}
		
//		Matcher m = Pattern.compile("\"([^\"]+)\":\"([^\"]+)\"").matcher(src);
//		while(m.find()){
//			System.out.println("--------------------");
//			System.out.println(m.group(1).trim());
//			System.out.println(m.group(2).trim());
//		}
//		System.out.println("==============");
//		m = Pattern.compile("\"([^\"]+)\":([^\"][^,]+)").matcher(src);
//		while(m.find()){
//			System.out.println("--------------------");
//			System.out.println(m.group(1).trim());
//			System.out.println(m.group(2).trim());
//		}
	}
	
}
