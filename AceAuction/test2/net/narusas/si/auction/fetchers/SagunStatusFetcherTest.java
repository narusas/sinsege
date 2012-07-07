package net.narusas.si.auction.fetchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class SagunStatusFetcherTest extends TestCase {
	public void testRegx() {
		String src = "<table class=\"Ltbl_dt\" summary=\"부동산의 점유관계 표\">\n"+
"	<caption>부동산의 점유관계</caption>\n"+
"		<tr>\n"+
"			<th width=\"15%\" class=\"txtcenter\">소재지</th>\n"+
"			\n"+
"			<td>1.\n"+
"\t\t\t서울특별시 중구 남창동  5\n"+
"\t\t\t</td>\n"+
"		</tr>\n"+
"		<tr>\n"+
"			<th class=\"txtcenter\">점유관계</th>\n"+
"			<td>111111\n"+
"			\n"+
"		</td>\n"+
"		</tr>\n"+
"		\n"+
"		<tr>\n"+
"			<th class=\"txtcenter\">기타</th>\n"+
"			<td>2222\n"+
"			\n"+
"			</td>\n"+
"		</tr>\n"+
"    </table>\n";

		//Pattern p = Pattern.compile("소재지</th>\\s*<td>([^<]*)</td>\\s*<\tr>\\s*<tr>\\s*<th[^>]*>점유관계</th>\\s*<td>([^<]+)</td>\\s*</tr>\\s*<tr>\\s*<th[^>]*>기타</th>\\s*<td>([^<]*)</td>");
		Pattern p = Pattern.compile("소재지</th>\\s*<td>([^<]*)</td>\\s*</tr>\\s*<tr>\\s*<th[^>]*>점유관계</th>\\s*<td>([^<]+)</td>\\s*</tr>\\s*<tr>\\s*<th[^>]*>기타</th>\\s*<td>([^<]*)</td>");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("1.\n\t\t\t서울특별시 중구 남창동  5\n\t\t\t", HTMLUtils.findTHAndNextValueAsComplex(src, "소재지"));
		
//		System.out.println(m.group(1));
//		System.out.println(m.group(2));
//		System.out.println(m.group(3));
	}
	
	public void testStrip() {
		String src = "1.\n				서울특별시 중구 남창동  5";
		assertEquals("서울특별시 중구 남창동  5", src.replaceAll("^\\d+\\.", "").trim());
	}
}
