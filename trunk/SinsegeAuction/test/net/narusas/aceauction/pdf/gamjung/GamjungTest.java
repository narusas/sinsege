package net.narusas.aceauction.pdf.gamjung;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class GamjungTest extends TestCase {

	public void test1() throws IOException {
		GamjungParser parser = new GamjungParser();
		parser.printParts(parser.parse(new File("fixtures/감정평가서8924.pdf")));
	}


}
