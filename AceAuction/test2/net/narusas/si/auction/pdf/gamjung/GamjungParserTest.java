package net.narusas.si.auction.pdf.gamjung;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;
import net.narusas.si.auction.pdf.gamjung.GamjungParser.Group;

public class GamjungParserTest extends TestCase {
	public void testParse() throws IOException {
		// File f= new File("fixture2/024_감정평가서.pdf");
		// GamjungParser parser = new GamjungParser();
		// List<Group> res = parser.parse(f);
		// for (Group group : res) {
		// System.out.println("##########################");
		//			
		// System.out.println(group.get냉난방());
		// System.out.println("################");
		// System.out.println(group.get도로관련());
		// System.out.println("################");
		// System.out.println(group.get위치및교통());
		// System.out.println("################");
		// System.out.println(group.get이용상태());
		// }
	}

	public void testParse2() throws IOException {
//		File f = new File("fixture2/038_청주2008_3472_감정평가서.pdf");
		File f = new File("fixture2/039_중앙2계_2008_33529_감정평가서.pdf");
		GamjungParser parser = new GamjungParser();
		List<Group> res = parser.parse(f);
		System.out.println(parser.getDate());
		System.out.println(parser.getSrc());
		for (Group group : res) {
			System.out.println("######## 냉난방");
			System.out.println(group.get냉난방());

			System.out.println("######## 도로관련");
			System.out.println(group.get도로관련());

			System.out.println("######## 위치및교통");
			System.out.println(group.get위치및교통());

			System.out.println("######## 이용상태");
			System.out.println(group.get이용상태());

			System.out.println("######## 토지");
			System.out.println(group.get토지());

			String text = group.get토지();
			byte[] data = text.getBytes("euc-kr");
			System.out.println(new String(data, "euc-kr"));
		}

	}
}
