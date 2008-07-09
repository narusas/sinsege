package net.narusas.aceauction.pdf.jpedal;

import java.io.File;

import junit.framework.TestCase;

public class ImageExtractTest extends TestCase {
	public void testExtract() {
		File f = new File("fixtures/감정평가서_다수이미지.pdf");
		assertTrue(f.exists());
//		ExtractImages.addSizeLimit(-1, 200);
		
		ExtractImages extractor = new ExtractImages(f.getAbsolutePath());
		System.out.println(extractor.getOutputDir());
	}
}
