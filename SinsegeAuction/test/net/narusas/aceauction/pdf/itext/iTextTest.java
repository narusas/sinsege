package net.narusas.aceauction.pdf.itext;

import java.io.IOException;

import junit.framework.TestCase;

import com.lowagie.text.DocumentException;

public class iTextTest extends TestCase {

	public void test1() throws IOException, DocumentException {
		���ε���ں���.convert("fixtures/������ ���� ����.pdf", "res.pdf");
	}
}
