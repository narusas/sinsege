package net.narusas.si.auction.fetchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class 주소BuilderTest extends TestCase{

	public void testBuild() {
//		String 소재지 = "인천광역시 부평구 굴포로 105, 117동 8층 801호 (삼산동,삼산타운주공아파트)";
		String 소재지 = "경상북도 포항시 북구 대안길 56, 102동 1층 102호 (용흥동,1차우방타운)";
//		String 소재지 = "서울시 노원구 상계5동 136-1 9/4";
		
		testBuild(소재지);
	}
	public void testBuild(String 소재지) {
		if (소재지.trim().startsWith("(")) {
			소재지 = 소재지.substring(소재지.indexOf(")") + 1).trim();
		}

		String[] tokens = 소재지.split(" ");

		String target시도 = tokens[0];
		String target시군구 = tokens[1];
		String target읍면동 = tokens[2];
		String target번지이하 = tokens[3];


		String token = tokens[2];
		if (token.endsWith("군") || token.endsWith("구")) {
			target시군구 = tokens[1] + " " + tokens[2];
			target읍면동 = tokens[3];
			target번지이하= tokens[4];
		}
		
		System.out.println("시도:" + target시도);
		System.out.println("시군구:" + target시군구);
		System.out.println("읍면동:" + target읍면동);

		System.out.println("--------");
		Pattern 신규주소명_길번호_규칙 = Pattern.compile("(\\d+),?.*");
		Matcher m1 = 신규주소명_길번호_규칙.matcher(target번지이하);
		if ((target읍면동.endsWith("로") || target읍면동.endsWith("길") )&& m1.find()) {
			target읍면동 = target읍면동 +" "+ m1.group(1);
		}

		System.out.println("시도:" + target시도);
		System.out.println("시군구:" + target시군구);
		System.out.println("읍면동:" + target읍면동);
		String 번지이하 = 소재지.substring(소재지.indexOf(target읍면동) + target읍면동.length()).trim();
		if (번지이하.startsWith(",")){
			번지이하 = 번지이하.substring(1).trim();
		}
		System.out.println("번지이하:" + 번지이하);
		String addr = target읍면동 + " " + 번지이하;
		System.out.println(addr);
		
		Pattern slimAddrPattern2 = Pattern.compile("(.*[길로] \\d+)");
		Matcher m = slimAddrPattern2.matcher(addr);
		if (m.find()) {
			System.out.println("##### " + m.group(1));
		}

		
//		Pattern slimAddrPattern1 = Pattern.compile("(\\S+[동리가] \\s*[산]*\\s*[\\d-]*)");
//		m = slimAddrPattern1.matcher(addr);
//		if (m.find()) {
//			System.out.println("##### " + m.group(1));
//		}

		// 주소 주소 = new
		// 주소Builder().parse("인천광역시 부평구 굴포로 105, 117동 8층 801호 (삼산동,삼산타운주공아파트)");
		// System.out.println(주소);
	}

}
