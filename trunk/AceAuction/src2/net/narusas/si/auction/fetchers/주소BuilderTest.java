package net.narusas.si.auction.fetchers;

import junit.framework.TestCase;
import net.narusas.si.auction.fetchers.주소통합Builder.통합주소;

public class 주소BuilderTest extends TestCase {

	private 주소통합Builder 주소Builder;
	RoadConverter roadConverter;

	@Override
	protected void setUp() throws Exception {
		주소Builder = new 주소통합Builder();
		roadConverter =  new RoadConverter();
	}
	
	private void assert소재지(String 시도, String 시군구, String 읍면, String  동리,  String 번지,String 번지이하, String  도로명,  Integer 건물주번, Integer 건물부번,  통합주소 주소) {
		System.out.println( 주소 );
		assertEquals(시도,   주소.시도);
		assertEquals(시군구,   주소.시군구);
		assertEquals(읍면,   주소.읍면);
		assertEquals(동리,   주소.동리);
		assertEquals(번지,   주소.번지);
		assertEquals(번지이하,   주소.번지이하);
		assertEquals(도로명,   주소.도로명);
		assertEquals(건물주번,   주소.건물주번);
		assertEquals(건물부번,   주소.건물부번);
	}
	
	public void test2(){
		assert소재지("경상남도", 	"창원시 마산회원구",		"내서읍", 		null, 		null, 		null, 									"중리상곡로", 			20, 1,  	주소Builder.parse소재지("경상남도 창원시 마산회원구 내서읍 중리상곡로 20-1"));
		assert소재지("경상남도", 	"창원시 의창구",		"동읍", 			null, 		null, 		null, 									"용정길46번길",  		24, null,  	주소Builder.parse소재지("경상남도 창원시 의창구 동읍 용정길46번길 24"));
		assert소재지("경상남도", 	"창원시 의창구",		"동읍", 			null, 		null, 		null,									"용정길46번길",  		24, 1,  	주소Builder.parse소재지("경상남도 창원시 의창구 동읍 용정길46번길 24-1"));
		assert소재지("서울특별시",	"중구",				null, 			null, 		null, 		"25동 18층 1808호 (신당동,남산타운아파트)", 	"다산로",  			32, null,  	주소Builder.parse소재지("(아파트) 서울특별시 중구 다산로 32, 25동 18층 1808호 (신당동,남산타운아파트)"));
		assert소재지("경상북도", 	"포항시 북구",			null,			null, 		null, 		"102동 1층 102호 (용흥동,1차우방타운)",  		"대안길",				56, null, 	주소Builder.parse소재지("경상북도 포항시 북구 대안길 56, 102동 1층 102호 (용흥동,1차우방타운)"));
		assert소재지("인천광역시", 	"부평구", 			null, 			null, 		null, 		"117동 8층 801호 (삼산동,삼산타운주공아파트)", 	"굴포로",  			105, null, 	주소Builder.parse소재지("인천광역시 부평구 굴포로 105, 117동 8층 801호 (삼산동,삼산타운주공아파트)"));
		assert소재지("서울특별시", 	"성북구", 			null, 			"성북동", 	"179-191",	"3층 303호", 								null,				null,null,	주소Builder.parse소재지("(연립주택) 서울특별시 성북구 성북동 179-191 3층 303호"));
		assert소재지("인천광역시", 	"계양구", 			null,			null,		null, 		"105동 28층 2801호 (서운동,계양임광그대가)",	"효서로", 			417,null,	주소Builder.parse소재지("(아파트) 인천광역시 계양구 효서로 417, 105동 28층 2801호 (서운동,계양임광그대가)"));
		assert소재지("경상북도", 	"포항시 북구", 		null,			null,		null, 		"209동 8층 805호 (창포동,창포2단지주공아파트)",	"새천년대로1075번길",	10,null,	주소Builder.parse소재지("경상북도 포항시 북구 새천년대로1075번길 10, 209동 8층 805호 (창포동,창포2단지주공아파트)"));
		assert소재지("경상북도", 	"포항시 북구", 		null,			null,		null, 		"109동 5층 502호 (용흥동,2차우방타운)", 		"대안길",				56, null,	주소Builder.parse소재지("경상북도 포항시 북구 대안길 56, 109동 5층 502호 (용흥동,2차우방타운)"));
		assert소재지("경상북도", 	"포항시 북구", 		null,			null, 		null,		"3동 5층 504호", 							"양학로",				35,null,	주소Builder.parse소재지("경상북도 포항시 북구 양학로 35, 3동 5층 504호"));
		assert소재지("경상북도", 	"포항시 북구", 		null,			null, 		null,		null,									"중흥로213번길",		24,12,		주소Builder.parse소재지("경상북도 포항시 북구 중흥로213번길 24-12"));
		assert소재지("경상북도", 	"포항시 북구", 		null, 			"죽도동", 	"620-24",	null, 		 							"중흥로213번길",		24,12,		주소Builder.parse소재지("경상북도 포항시 북구 죽도동 620-24"));
		assert소재지("경상북도", 	"포항시 북구",			null,			null,		null, 		"206동 1층 101호 (두호동,창포2차아이파크)", 	"대곡로", 			21,null,	주소Builder.parse소재지("경상북도 포항시 북구 대곡로 21, 206동 1층 101호 (두호동,창포2차아이파크)"));
		assert소재지("경기도", 		"파주시", 			"문산읍",			null,		null, 		"202동 1층 104호 (파주힐스테이트2차)", 		"당동1로",	 		67,null,	주소Builder.parse소재지("(아파트) 경기도 파주시 문산읍 당동1로 67, 202동 1층 104호 (파주힐스테이트2차)"));
		assert소재지("경상남도",	"창원시 의창구", 		"동읍",			null,		null, 		null,									"용정길46번길",		24,null, 	주소Builder.parse소재지("경상남도 창원시 의창구 동읍 용정길46번길 24"));
		assert소재지("경상남도", 	"창원시 마산회원구", 	"내서읍",			null, 		null, 		null,									"중리상곡로", 			20,1, 		주소Builder.parse소재지("경상남도 창원시 마산회원구 내서읍 중리상곡로 20-1"));
		assert소재지("울산광역시", 	"중구", 				null,			null, 		null, 		null,									"젊음의2거리", 		29,null, 	주소Builder.parse소재지("울산광역시 중구 젊음의2거리 29"));
		assert소재지("경상남도", 	"창원시 의창구", 		"동읍",			null, 		null, 		null,									"용정길46번길", 		24,null, 	주소Builder.parse소재지("경상남도 창원시 의창구 동읍 용정길46번길 24"));
		
		assert소재지("서울특별시", 	"노원구", 			null, 			"상계동",		"1255", 	"은빛아파트 110동 13층 1301호",	 			"동일로245길", 		162,null, 	주소Builder.parse소재지("서울특별시 노원구 상계동  1255은빛아파트 110동 13층 1301호"));
		assert소재지("서울특별시", 	"노원구", 			null, 			"중계동",		"369-7", 	"중계2단지주공아파트종합상가 지하층 1호",	 		"한글비석로", 			332,null, 	주소Builder.parse소재지("서울특별시 노원구 중계동  369-7중계2단지주공아파트종합상가 지하층 1호"));
		assert소재지("경기도", 		"남양주시", 			"와부읍",			"도곡리",		"1012", 	"한강우성아파트 106동 1층 102호",	 			"덕소로", 			270,null, 	주소Builder.parse소재지("경기도 남양주시 와부읍 도곡리 1012한강우성아파트 106동 1층 102호"));
		assert소재지("서울특별시", 	"동대문구", 			null,			"제기동",		"892-66", 	"현대아파트 101동 8층 802호",	 			"정릉천동로", 			90,null, 	주소Builder.parse소재지("서울특별시 동대문구 제기동  892-66현대아파트 101동 8층 802호"));
		assert소재지("경기도", 		"양주시", 			null,			"봉양동",		null, 		"산121-2",	 							null, 				null,null, 	주소Builder.parse소재지("경기도 양주시 봉양동  산121-2"));
		assert소재지("경기도", 		"양주시", 			"광적면",			"우고리",		"465", 		null,	 								null, 				null,null, 	주소Builder.parse소재지("경기도 양주시 광적면 우고리 465"));
		assert소재지("경기도", 		"양주시", 			"광적면",			"우고리",		"465", 		null,	 								null, 				null,null, 	주소Builder.parse소재지("경기도 양주시 광적면 우고리 465"));

	}
}
