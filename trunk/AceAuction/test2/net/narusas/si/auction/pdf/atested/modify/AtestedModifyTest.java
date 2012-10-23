package net.narusas.si.auction.pdf.atested.modify;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;
import org.junit.Test;

public class AtestedModifyTest extends PDFTextStripper {

	public AtestedModifyTest() throws IOException {
		super();
	}

	@Test
	public void test() throws IOException {
		setSortByPosition(true);
		File f = new File("fixture2/087_인포_제주_2011-15393.pdf");
		assertTrue(f.exists());
		PDDocument doc = PDDocument.load(f);
		StringWriter outputStream = new StringWriter();
		writeText(doc, outputStream);
		System.out.println(outputStream.toString());
		List allPages = document.getDocumentCatalog().getAllPages();
		for (int i = 0; i < allPages.size(); i++) {
			PDPage page = (PDPage) allPages.get(i);
			System.out.println("Processing page: " + i);
			PDStream contents = page.getContents();
			if (contents != null) {
				processStream(page, page.findResources(), page.getContents()
						.getStream());
			}
		}

	}

	protected void processTextPosition(TextPosition text) {
		System.out.println("String[" + text.getXDirAdj() + ","
				+ text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale="
				+ text.getXScale() + " height=" + text.getHeightDir()
				+ " space=" + text.getWidthOfSpace() + " width="
				+ text.getWidthDirAdj() + "]" + text.getCharacter());
		
	}

}
