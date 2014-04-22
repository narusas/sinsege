package net.narusas.si.auction.fetchers;

import junit.framework.TestCase;
import net.narusas.si.auction.fetchers.AddressBuilder.통합주소;

public class 주소BuilderTest extends TestCase {

	private AddressBuilder 주소Builder;
	RoadConverter roadConverter;

	@Override
	protected void setUp() throws Exception {
		주소Builder = new AddressBuilder();
		roadConverter =  new RoadConverter();
	}
	
	private void assert소재지(String 시도, String  시군구그룹, String 시군구,   String 읍면, String  동리,  String 번지,String 번지이하, String  도로명,String  도로명그룹,  Integer 건물주번, Integer 건물부번,  통합주소 주소) {
		System.out.println( 주소 );
		assertEquals(시도,   주소.시도);
		assertEquals(시군구그룹,   주소.시군구그룹);
		assertEquals(시군구,   주소.시군구);
		assertEquals(읍면,   주소.읍면);
		assertEquals(동리,   주소.동리);
		assertEquals(번지,   주소.번지);
		assertEquals(번지이하,   주소.번지이하);
		assertEquals(도로명,   주소.도로명);
		assertEquals(도로명그룹,   주소.도로명그룹);
		assertEquals(건물주번,   주소.건물주번);
		assertEquals(건물부번,   주소.건물부번);
	}
	
	public void test2(){
		//경기도 포천시 신읍동 67-5
		//서울특별시 광진구 아차산로 262, 디동 15층 1503호 (자양동,더샵스타시티)
		//
		assert소재지("서울특별시", 	null, 		"광진구",			null, 			null, 		null, 		"디동 15층 1503호 (자양동,더샵스타시티)",			"아차산로", null,				262, null,  주소Builder.parse소재지("(판매시설) 서울특별시 영등포구 문래동3가 55-16 영등포에스케이리더스뷰 1층씨04호"));

		
		assert소재지("서울특별시", 	null, 		"광진구",			null, 			null, 		null, 		"디동 15층 1503호 (자양동,더샵스타시티)",			"아차산로", null,				262, null,  주소Builder.parse소재지("서울특별시 광진구 아차산로 262, 디동 15층 1503호 (자양동,더샵스타시티)"));
		assert소재지("경기도", 		null, 		"포천시",				null, 			"신읍동", 	"67-5", 	null, 									"중앙로", null,				52, null,  	주소Builder.parse소재지("경기도 포천시 신읍동 67-5"));
		assert소재지("경상남도", 	"창원시", 	"창원시 마산회원구",		"내서읍", 		null, 		null, 		null, 									"중리상곡로",null, 			20, 1,  	주소Builder.parse소재지("경상남도 창원시 마산회원구 내서읍 중리상곡로 20-1"));
		assert소재지("서울특별시",	null,		"서초구",		 		null,	 		"우면동", 	"산21-22", 	null,									null, 	null, 				null, null,	주소Builder.parse소재지("서울특별시 서초구 우면동 산21-22"));
		assert소재지("경상남도", 	"창원시", 	"창원시 의창구",		"동읍", 			null, 		null, 		null, 									"용정길46번길", "용정길", 			24, null,  	주소Builder.parse소재지("경상남도 창원시 의창구 동읍 용정길46번길 24"));
		assert소재지("경상남도", 	"창원시", 	"창원시 의창구",		"동읍", 			null, 		null, 		null,									"용정길46번길", "용정길",  		24, 1,  	주소Builder.parse소재지("경상남도 창원시 의창구 동읍 용정길46번길 24-1"));
		assert소재지("경상남도",	"창원시", 	"창원시 마산회원구",		 null,	 		null, 		null, 		"6층 609호 (합성동,보보스존)",				"3·15대로", null,	    		736, null,	주소Builder.parse소재지("(판매시설) 경상남도 창원시 마산회원구 3.15대로 736, 6층 609호 (합성동,보보스존)"));
		assert소재지("경기도",		null, 		"군포시", 			 null,	 		null, 		null, 		"101동 13층 1303호 (당동,동아아파트)",		"군포로490번길","군포로", 			22, null,	주소Builder.parse소재지("(아파트) 경기도 군포시 군포로490번길 22, 101동 13층 1303호 (당동,동아아파트)"));
		assert소재지("광주시",		null, 		null, 				 null,	 		"곤지암리", 	"124-2", 	null,									"광여로99번길","광여로", 			47, null,	주소Builder.parse소재지("(기타) 광주시 곤지암리 곤지암리 124-2"));
		assert소재지("세종특별자치시",null, 		null, 				"조치원읍", 		"교리", 		"7-24", 	null,									"조치원5길", "조치원길",	 		75, 5,		주소Builder.parse소재지("(대지) 세종특별자치시 조치원읍 교리 7-24"));
		assert소재지("경기도", 		null, 		"남양주시", 			"수동면", 		null, 		null, 		"15층 1501호 (삼청장미9차아파트)",			"비룡로", null,	 			164, null,	주소Builder.parse소재지("(아파트) 경기도 남양주시 수동면 비룡로 164, 15층 1501호 (삼청장미9차아파트)"));
//		assert소재지(null, 		"포천시", 			"내촌면", 		"마명리", 	"109-5", 	"외 위지상 가동, 나동호에 소재", 				"부마로282번길",  		33, 40,		주소Builder.parse소재지("(기타) 포천시 내촌면 마명리 109-5외 위지상 가동, 나동호에 소재"));
		assert소재지("경상북도", 	"포항시", 	"포항시 북구", 		null, 			"동빈1가", 	"69-20", 	null, 									"해동로",null,  				285, 1, 	주소Builder.parse소재지("(대지) 경상북도 포항시 북구 동빈1가 69-20"));
		assert소재지("인천광역시", 	null, 		"서구", 				null, 			null, 		null, 		null, 									"청마로148번길","청마로",  		47, null, 	주소Builder.parse소재지("인천광역시 서구 청마로 148번길47"));
		assert소재지("인천광역시", 	null, 		"서구", 				null, 			null, 		null, 		null, 									"청마로148번길", "청마로", 		47, 12, 	주소Builder.parse소재지("인천광역시 서구 청마로 148번길47-12"));
		assert소재지("제주특별자치도",null, 		"제주시", 			"구좌읍",			"세화리",		"산34", 		null, 									null,null, 					null,null, 	주소Builder.parse소재지("제주특별자치도 제주시 구좌읍 세화리 산34"));
		assert소재지("경기도", 		null, 		"양주시", 			null,			"봉양동",		"산121-2", 	null, 									null,null, 					null,null, 	주소Builder.parse소재지("경기도 양주시 봉양동  산121-2"));
		assert소재지("서울특별시", 	null, 		"동작구", 			null, 			"상도1동",	"801-1", 	null,	 								"상도로",null, 				294,1, 		주소Builder.parse소재지("서울특별시 동작구 상도1동 801-1"));
		assert소재지("서울특별시",	null, 		"중구",				null, 			null, 		null, 		"25동 18층 1808호 (신당동,남산타운아파트)", 	"다산로",null,	  			32, null,  	주소Builder.parse소재지("(아파트) 서울특별시 중구 다산로 32, 25동 18층 1808호 (신당동,남산타운아파트)"));
		assert소재지("경상북도", 	"포항시", 	"포항시 북구",			null,			null, 		null, 		"102동 1층 102호 (용흥동,1차우방타운)",  		"대안길",null,				56, null, 	주소Builder.parse소재지("경상북도 포항시 북구 대안길 56, 102동 1층 102호 (용흥동,1차우방타운)"));
		assert소재지("인천광역시", 	null, 		"부평구", 			null, 			null, 		null, 		"117동 8층 801호 (삼산동,삼산타운주공아파트)", 	"굴포로", null, 				105, null, 	주소Builder.parse소재지("인천광역시 부평구 굴포로 105, 117동 8층 801호 (삼산동,삼산타운주공아파트)"));
		assert소재지("서울특별시", 	null, 		"성북구", 			null, 			"성북동", 	"179-191",	"3층 303호", 								"성북로4길","성북로",			57,6,		주소Builder.parse소재지("(연립주택) 서울특별시 성북구 성북동 179-191 3층 303호"));
		assert소재지("인천광역시", 	null, 		"계양구", 			null,			null,		null, 		"105동 28층 2801호 (서운동,계양임광그대가)",	"효서로", null,				417,null,	주소Builder.parse소재지("(아파트) 인천광역시 계양구 효서로 417, 105동 28층 2801호 (서운동,계양임광그대가)"));
		assert소재지("경상북도", 	"포항시", 	"포항시 북구", 		null,			null,		null, 		"209동 8층 805호 (창포동,창포2단지주공아파트)",	"새천년대로1075번길","새천년대로",	10,null,	주소Builder.parse소재지("경상북도 포항시 북구 새천년대로1075번길 10, 209동 8층 805호 (창포동,창포2단지주공아파트)"));
		assert소재지("경상북도", 	"포항시", 	"포항시 북구", 		null,			null,		null, 		"109동 5층 502호 (용흥동,2차우방타운)", 		"대안길",	null,				56, null,	주소Builder.parse소재지("경상북도 포항시 북구 대안길 56, 109동 5층 502호 (용흥동,2차우방타운)"));
		assert소재지("경상북도", 	"포항시", 	"포항시 북구", 		null,			null, 		null,		"3동 5층 504호", 							"양학로",	null,				35,null,	주소Builder.parse소재지("경상북도 포항시 북구 양학로 35, 3동 5층 504호"));
		assert소재지("경상북도", 	"포항시", 	"포항시 북구", 		null,			null, 		null,		null,									"중흥로213번길","중흥로",		24,12,		주소Builder.parse소재지("경상북도 포항시 북구 중흥로213번길 24-12"));
		assert소재지("경상북도", 	"포항시", 	"포항시 북구", 		null, 			"죽도동", 	"620-24",	null, 		 							"중흥로213번길","중흥로",		24,12,		주소Builder.parse소재지("경상북도 포항시 북구 죽도동 620-24"));
		assert소재지("경상북도", 	"포항시", 	"포항시 북구",			null,			null,		null, 		"206동 1층 101호 (두호동,창포2차아이파크)", 	"대곡로", null, 				21,null,	주소Builder.parse소재지("경상북도 포항시 북구 대곡로 21, 206동 1층 101호 (두호동,창포2차아이파크)"));
		assert소재지("경기도", 		null, 		"파주시", 			"문산읍",			null,		null, 		"202동 1층 104호 (파주힐스테이트2차)", 		"당동1로", "당동로",	 		67,null,	주소Builder.parse소재지("(아파트) 경기도 파주시 문산읍 당동1로 67, 202동 1층 104호 (파주힐스테이트2차)"));
		assert소재지("경상남도",	"창원시", 	"창원시 의창구", 		"동읍",			null,		null, 		null,									"용정길46번길","용정길",			24,null, 	주소Builder.parse소재지("경상남도 창원시 의창구 동읍 용정길46번길 24"));
		assert소재지("경상남도", 	"창원시", 	"창원시 마산회원구", 	"내서읍",			null, 		null, 		null,									"중리상곡로", null,			20,1, 		주소Builder.parse소재지("경상남도 창원시 마산회원구 내서읍 중리상곡로 20-1"));
		assert소재지("울산광역시", 	 null, 		"중구", 				null,			null, 		null, 		null,									"젊음의2거리", "젊음의거리",		29,null, 	주소Builder.parse소재지("울산광역시 중구 젊음의2거리 29"));
		assert소재지("경상남도", 	"창원시", 	"창원시 의창구", 		"동읍",			null, 		null, 		null,									"용정길46번길", "용정길",		24,null, 	주소Builder.parse소재지("경상남도 창원시 의창구 동읍 용정길46번길 24"));
		
		assert소재지("서울특별시", 	null, 		"노원구", 			null, 			"상계동",		"1255", 	"은빛아파트 110동 13층 1301호",	 			"동일로245길", "동일로",		162,null, 	주소Builder.parse소재지("서울특별시 노원구 상계동  1255은빛아파트 110동 13층 1301호"));
		assert소재지("서울특별시", 	null, 		"노원구", 			null, 			"중계동",		"369-7", 	"중계2단지주공아파트종합상가 지하층 1호",	 		"한글비석로", null,			332,null, 	주소Builder.parse소재지("서울특별시 노원구 중계동  369-7중계2단지주공아파트종합상가 지하층 1호"));
		assert소재지("경기도", 		null, 		"남양주시", 			"와부읍",			"도곡리",		"1012", 	"한강우성아파트 106동 1층 102호",	 			"덕소로", null,				270,null, 	주소Builder.parse소재지("경기도 남양주시 와부읍 도곡리 1012한강우성아파트 106동 1층 102호"));
		assert소재지("서울특별시", 	null, 		"동대문구", 			null,			"제기동",		"892-66", 	"현대아파트 101동 8층 802호",	 			"정릉천동로", null,			90,null, 	주소Builder.parse소재지("서울특별시 동대문구 제기동  892-66현대아파트 101동 8층 802호"));
		assert소재지("경기도", 		null, 		"양주시", 	
				"광적면",			"우고리",		"465", 		null,	 								null, null,					null,null, 	주소Builder.parse소재지("경기도 양주시 광적면 우고리 465"));
		assert소재지("경기도", 		null, 		"양주시", 			"광적면",			"우고리",		"465", 		null,	 								null, null,					null,null, 	주소Builder.parse소재지("경기도 양주시 광적면 우고리 465"));

	}
}
