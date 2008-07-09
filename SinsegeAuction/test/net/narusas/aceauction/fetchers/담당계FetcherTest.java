package net.narusas.aceauction.fetchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class ����FetcherTest extends TestCase {
	// public void testFechDate() throws HttpException, IOException {
	// ����Fetcher date = new ����Fetcher(����.code("�����߾��������")) {
	// @Override
	// protected String fetchPage() throws IOException {
	// return NFile.getText(new File("fixtures/�����߾��������_������.html"));
	// }
	// };
	// assertNotNull(date.fetchCharges());
	// }

	public void testRegx() {
		String fixture = "loadMaemul( '2007.03.08', '1008', '���8��' )";
		Pattern p = Pattern.compile("(\\d\\d\\d\\d.\\d+.\\d+)");
		Matcher m = p.matcher(fixture);
		boolean b = m.find();
		assertTrue(b);
	}

	public void testRegx2() {
		Pattern p = Pattern.compile("'(���\\d+-?\\d?��)'");
		assertTrue(p.matcher(", '���2-1��' );").find());
		assertTrue(p.matcher(", '���8��' );").find());
	}

	public void testRegx3() {
		String src = "<tr bgcolor=\"#F3F3F3\" valign=\"middle\" align=\"center\">\n"
				+ "                  <td width=\"20%\" height=\"26\">2007.03.09 ~ 2007.03.16<br>( �Ű����� 2007.03.22 )</td>";

		Pattern p = Pattern
				.compile(">\\s*(\\d\\d\\d\\d.\\d+.\\d+\\s*~\\s*\\d\\d\\d\\d.\\d+.\\d+)");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("2007.03.09 ~ 2007.03.16", m.group(1));
	}
}
