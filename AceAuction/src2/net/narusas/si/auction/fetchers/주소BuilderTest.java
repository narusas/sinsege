package net.narusas.si.auction.fetchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.model.주소;
import junit.framework.TestCase;

public class 주소BuilderTest extends TestCase {

	private 주소Builder 주소Builder;

	@Override
	protected void setUp() throws Exception {
		주소Builder = new 주소Builder();
	}
	
	public void test1() {
		assert소재지("경상북도", "포항시 북구", "대안길", "56, 102동 1층 102호 (용흥동,1차우방타운)", parse소재지("경상북도 포항시 북구 대안길 56, 102동 1층 102호 (용흥동,1차우방타운)"));
		assert소재지("인천광역시", "부평구", "굴포로", "105, 117동 8층 801호 (삼산동,삼산타운주공아파트)", parse소재지("인천광역시 부평구 굴포로 105, 117동 8층 801호 (삼산동,삼산타운주공아파트)"));
		assert소재지("서울특별시", "성북구", "성북동", "179-191 3층 303호", parse소재지("(연립주택) 서울특별시 성북구 성북동 179-191 3층 303호"));
		assert소재지("인천광역시", "계양구", "효서로", "417, 105동 28층 2801호 (서운동,계양임광그대가)", parse소재지("(아파트) 인천광역시 계양구 효서로 417, 105동 28층 2801호 (서운동,계양임광그대가)"));
		
		assert소재지("경상북도", "포항시 북구", "새천년대로1075번길", "10, 209동 8층 805호 (창포동,창포2단지주공아파트)", parse소재지("경상북도 포항시 북구 새천년대로1075번길 10, 209동 8층 805호 (창포동,창포2단지주공아파트)"));
		assert소재지("경상북도", "포항시 북구", "대안길", "56, 109동 5층 502호 (용흥동,2차우방타운)", parse소재지("경상북도 포항시 북구 대안길 56, 109동 5층 502호 (용흥동,2차우방타운)"));
		assert소재지("경상북도", "포항시 북구", "양학로", "35, 3동 5층 504호", parse소재지("경상북도 포항시 북구 양학로 35, 3동 5층 504호"));
		assert소재지("경상북도", "포항시 북구", "중흥로213번길", "24-12", parse소재지("경상북도 포항시 북구 중흥로213번길 24-12"));
		assert소재지("경상북도", "포항시 북구", "죽도동", "620-24", parse소재지("경상북도 포항시 북구 죽도동 620-24"));
		assert소재지("경상북도", "포항시 북구", "대곡로", "21, 206동 1층 101호 (두호동,창포2차아이파크)", parse소재지("경상북도 포항시 북구 대곡로 21, 206동 1층 101호 (두호동,창포2차아이파크)"));
	}
	
	// 
	//경상북도 포항시 북구 대곡로 21, 206동 1층 101호 (두호동,창포2차아이파크)
	//경상북도 포항시 북구 삼흥로88번길 28-3, 비동 4층 402호 (두호동,한샘하우젠빌)
	//경상북도 포항시 북구 대곡로 7, 104동 2층 204호 (두호동,창포아이파크) 
	//경상북도 포항시 북구 두호로 66, 18동 2층 203호 (두호동,두호아파트) 
	//경상북도 포항시 북구 서동로47번길 34, 2층 201호 (덕수동,타운빌) 
	//경상북도 포항시 북구 삼호로473번길 4-10 
	//경상북도 포항시 북구 환호동 502-5 
	//경상북도 포항시 북구 성실로 21-26, 2동 2층 203호 (장성동,대명장성맨션3차)
	//경상북도 포항시 북구 환호동 502 

//	public void test1Build() {
//		// (연립주택) 서울특별시 성북구 성북동 179-191 3층 303호
//		// String 소재지 = "인천광역시 부평구 굴포로 105, 117동 8층 801호 (삼산동,삼산타운주공아파트)";
////		String 소재지 = "경상북도 포항시 북구 대안길 56, 102동 1층 102호 (용흥동,1차우방타운)";
//		// String 소재지 = "서울시 노원구 상계5동 136-1 9/4";
//		// 주소 addr =
//		// 주소Builder.parse("경상북도 포항시 북구 대안길 56, 102동 1층 102호 (용흥동,1차우방타운)");
//		// System.out.println(addr);
//		
//		
//	}

	private void assert소재지(String 시도, String 시군구, String 읍면동, String 번지이하, String[] 소재지) {
		print소재지(소재지);
		assertEquals(시도, 소재지[0]);
		assertEquals(시군구, 소재지[1]);
		assertEquals(읍면동, 소재지[2]);
		assertEquals(번지이하, 소재지[3]);
	}

	private void print소재지(String[] res) {
		String target시도 = res[0];
		String target시군구 = res[1];
		String target읍면동 = res[2];
		String target번지이하 = res[3];
		System.out.println("시도:" + target시도);
		System.out.println("시군구:" + target시군구);
		System.out.println("읍면동:" + target읍면동);
		System.out.println("번지이하:" + target번지이하);
	}

	public String[] parse소재지(String 소재지) {
		소재지 = 소재지.trim();
		if (소재지.startsWith("(")) {
			소재지 = 소재지.substring(소재지.indexOf(")") + 1).trim();
		}
		String[] tokens = 소재지.split(" ");

		String target시도 = tokens[0];
		String target시군구 = tokens[1];
		String target읍면동 = tokens[2];
		String target번지이하 = others(tokens, 3);
		if (target읍면동.endsWith("군") || target읍면동.endsWith("구")) {
			target시군구 = tokens[1] + " " + tokens[2];
			target읍면동 = tokens[3];
			target번지이하 = others(tokens, 4);
		}
		
		System.out.println("### "+target읍면동);
		System.out.println("### "+target번지이하);

//		Pattern 신규주소명_길번호_규칙 = Pattern.compile("(\\d+),?.*");
//		Matcher m1 = 신규주소명_길번호_규칙.matcher(target번지이하);
//		if ((target읍면동.endsWith("로") || target읍면동.endsWith("길")) && m1.find()) {
//			System.out.println("#here");
//			target읍면동 = target읍면동 + " " + m1.group(1);
//			target번지이하 = others(tokens, 5);
//		}

		// String 번지이하 = 소재지.substring(소재지.indexOf(target읍면동) +
		// target읍면동.length()).trim();
		// if (번지이하.startsWith(",")){
		// 번지이하 = 번지이하.substring(1).trim();
		// }
		// String addr = target읍면동 + " " + 번지이하;
		//
		// Pattern slimAddrPattern2 = Pattern.compile("(.*[길로] \\d+)");
		// Matcher m = slimAddrPattern2.matcher(addr);
		// if (m.find()) {
		// System.out.println("##### " + m.group(1));
		// }
		//
		return new String[] { target시도, target시군구, target읍면동, target번지이하 };

	}

	private String others(String[] tokens, int i) {
		StringBuffer buf = new StringBuffer();
		for (; i < tokens.length; i++) {
			buf.append(tokens[i]).append(" ");
		}
		return buf.toString().trim();
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
			target번지이하 = tokens[4];
		}

		System.out.println("시도:" + target시도);
		System.out.println("시군구:" + target시군구);
		System.out.println("읍면동:" + target읍면동);

		System.out.println("--------");
		Pattern 신규주소명_길번호_규칙 = Pattern.compile("(\\d+),?.*");
		Matcher m1 = 신규주소명_길번호_규칙.matcher(target번지이하);
		if ((target읍면동.endsWith("로") || target읍면동.endsWith("길")) && m1.find()) {
			target읍면동 = target읍면동 + " " + m1.group(1);
		}

		System.out.println("시도:" + target시도);
		System.out.println("시군구:" + target시군구);
		System.out.println("읍면동:" + target읍면동);
		String 번지이하 = 소재지.substring(소재지.indexOf(target읍면동) + target읍면동.length()).trim();
		if (번지이하.startsWith(",")) {
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

		// Pattern slimAddrPattern1 =
		// Pattern.compile("(\\S+[동리가] \\s*[산]*\\s*[\\d-]*)");
		// m = slimAddrPattern1.matcher(addr);
		// if (m.find()) {
		// System.out.println("##### " + m.group(1));
		// }

		// 주소 주소 = new
		// 주소Builder().parse("인천광역시 부평구 굴포로 105, 117동 8층 801호 (삼산동,삼산타운주공아파트)");
		// System.out.println(주소);
	}

}
