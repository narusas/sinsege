package net.narusas.aceauction.fetchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class 담당계FetcherTest extends TestCase {
	// public void testFechDate() throws HttpException, IOException {
	// 담당계Fetcher date = new 담당계Fetcher(법원.code("서울중앙지방법원")) {
	// @Override
	// protected String fetchPage() throws IOException {
	// return NFile.getText(new File("fixtures/서울중앙지방법원_담당계목록.html"));
	// }
	// };
	// assertNotNull(date.fetchCharges());
	// }

	public void testRegx() {
		String fixture = "loadMaemul( '2007.03.08', '1008', '경매8계' )";
		Pattern p = Pattern.compile("(\\d\\d\\d\\d.\\d+.\\d+)");
		Matcher m = p.matcher(fixture);
		boolean b = m.find();
		assertTrue(b);
	}

	public void testRegx2() {
		Pattern p = Pattern.compile("'(경매\\d+-?\\d?계)'");
		assertTrue(p.matcher(", '경매2-1계' );").find());
		assertTrue(p.matcher(", '경매8계' );").find());
	}

	public void testRegx3() {
		String src = "<tr bgcolor=\"#F3F3F3\" valign=\"middle\" align=\"center\">\n"
				+ "                  <td width=\"20%\" height=\"26\">2007.03.09 ~ 2007.03.16<br>( 매각기일 2007.03.22 )</td>";

		Pattern p = Pattern
				.compile(">\\s*(\\d\\d\\d\\d.\\d+.\\d+\\s*~\\s*\\d\\d\\d\\d.\\d+.\\d+)");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("2007.03.09 ~ 2007.03.16", m.group(1));
	}
}
