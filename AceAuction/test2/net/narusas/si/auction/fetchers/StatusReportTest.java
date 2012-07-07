package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.util.lang.NFile;

public class StatusReportTest extends TestCase {
	private String html;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

	}

	public void testParse() throws IOException {
		html = NFile.getText(new File("fixture2/019_현황조사서_부동산표시목록.html"), "euc-kr");
		String temp = html.substring(html.indexOf("<h3>부동산표시목록</h3>") - 57);
		StringBuffer buf = new StringBuffer();
		String[] lines = temp.split("\n");
		for (String line : lines) {
			buf.append(line).append("\n");
			if (line.startsWith("</div>")) {
				break;
			}
		}
		// System.out.println(buf.toString());
	}

	public void testParse2() throws IOException {
		html = NFile.getText(new File("fixture2/020_현황조사서.html"), "euc-kr");
		String temp = html.substring(html.indexOf("<div class=\"paper paper_bg\">"));
		temp = temp.substring(0, temp.indexOf("<div class=\"tbl_btn\">") - 1);
		// System.out.println(temp);
	}

	public void testParse3() throws IOException {
		html = NFile.getText(new File("fixture2/021_감정평가서_MainFrame.html"), "euc-kr");
		Pattern p = Pattern.compile("<frame [^>]* src=\"([^\"]+)\"[^>]+");
		Matcher m = p.matcher(html);
		assertTrue(m.find());
		// System.out.println(m.group(1));
	}

	public void testParse4() throws IOException {
		html = NFile.getText(new File("fixture2/022_감정평가서_PDFURLPage.html"), "euc-kr");
		Pattern p = Pattern.compile("(top_pdf.php[^\"]+)");
		Matcher m = p.matcher(html);
		assertTrue(m.find());
		assertEquals("top_pdf.php?pdf=/downfiles/78804ee280898723eee50a55217e0888.pdf", m.group(1));
	}

	public void testFetch() throws IOException {
		사건감정평가서Fetcher f = new 사건감정평가서Fetcher();
		html = f.fetchMainPage("서울중앙지방법원", "20060130037251");
		String url = f.parsePDFPageURL(html);
		System.out.println(url);

		Pattern p = Pattern.compile("(http://[^/]+)(/.*)");
		Matcher m = p.matcher(url);
		m.find();
		assertEquals("http://210.98.146.20", m.group(1));
		assertEquals("/main.asp?bub_cd=000210&sa_no=20060130037251&ord_hoi=1", m.group(2));

		String prefix = m.group(1);
		String path = m.group(2);
		html = f.fetch(prefix, path);
		url = f.parsePDFPageURL(html);
		System.out.println("/"+url);
		html= f.fetch(prefix, "/"+url);
		System.out.println(html);
		
		Pattern p2 = Pattern.compile("parent\\.aks2\\.location\\.href=\"([^\"]+.pdf)\"");
		Matcher m2 = p2.matcher(html);
		m2.find();
		
		url = m2.group(1);
		File file = new File("test.pdf");
		
		f.downloadPDF(url, file);
		
	}
	
	
}
