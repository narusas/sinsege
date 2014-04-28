package net.narusas.si.auction.pdf.gamjung;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.si.auction.fetchers.사건감정평가서Fetcher;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;
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
		// File f = new File("fixture2/038_청주2008_3472_감정평가서.pdf");
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

	public void test1() {
		String src = "<frame src=\"/RetrieveRealEstSaGamEvalSeoTop.laf?jiwonNm=대구지방법원&amp;saNo=20120130011051&amp;maeGiil=20130326&amp;ordHoi=\" name=\"topFrame\" scrolling=\"NO\" noresize>";
		Pattern p = Pattern.compile("<frame src=\"(/RetrieveRealEstSaGamEvalSeoTop[^\"]+)\"[^>]+");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("/RetrieveRealEstSaGamEvalSeoTop.laf?jiwonNm=대구지방법원&amp;saNo=20120130011051&amp;maeGiil=20130326&amp;ordHoi=",
				m.group(1));
	}

	public void test2() throws IOException {
		사건감정평가서Fetcher f = new 사건감정평가서Fetcher();
		법원 법원 = new 법원();
		법원.set법원명("대구지방법원");
		법원.set법원코드(11);
		사건 사건 = new 사건(){
			@Override
			public String getPath() {
				return "down/";
			}
		};
		사건.set법원(법원);
		사건.set사건번호(20120130011051L);
		List<File> files = f.download(사건);
		for (File file : files) {
			System.out.println(file.getAbsolutePath());
		}
//		String html =  f.fetchTopFrame("대구지방법원","20120130011051");
//		System.out.println(html);
//		Pattern optionPattern = Pattern.compile("option value=\"(\\d+),(\\d+)\"");
//		Matcher optionMatcher = optionPattern.matcher(html);
//		while(optionMatcher.find()){
//			System.out.println(optionMatcher.group(1)+":"+optionMatcher.group(2));
//		}
	}

}
