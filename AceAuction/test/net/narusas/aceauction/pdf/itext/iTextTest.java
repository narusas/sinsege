package net.narusas.aceauction.pdf.itext;

import java.io.IOException;

import junit.framework.TestCase;

import com.lowagie.text.DocumentException;

public class iTextTest extends TestCase {

	public void test1() throws IOException, DocumentException {
		등기부등본날자변경.convert("fixtures/근저당 변경 이전.pdf", "res.pdf");
	}
}
