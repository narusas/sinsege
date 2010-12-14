package net.narusas.aceauction.dwrtest;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;
import net.narusas.si.auction.fetchers.PageFetcher;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class RetTest extends TestCase {
	public void testSimple() throws HttpException, IOException {
		PageFetcher f = new PageFetcher("http://www.ret.co.kr");
		String html = f.fetch("/");
		html = f.fetch("/ret/price/price01.jsp");
		
		Cookie[] cookies = f.getState().getCookies();
		
		//POST /ret/dwr/call/plaincall/__System.pageLoaded.dwr HTTP/1.1
		String sessionId = null;
		for (Cookie cookie : cookies) {
			System.out.println(cookie.getName() + ":" + cookie.getValue());
			if ("JSESSIONID".equals(cookie.getName())){
				sessionId = cookie.getValue();
				break;
			}
		}
		
		/**
		 * callCount=1
windowName=
c0-scriptName=__System
c0-methodName=pageLoaded
c0-id=0
batchId=0
page=%2Fret%2Fprice%2Fprice01.jsp
httpSessionId=0001Tt4lko8-RpXBj6-2oG0j2Q7:14l1rhbma
scriptSessionId=

		 */
		PostMethod res = f.post("/ret/dwr/call/plaincall/__System.pageLoaded.dwr", new NameValuePair[] {
				new NameValuePair("callCount", "1"), new NameValuePair("windowName", ""),
				new NameValuePair("c0-scriptName", "__System"),
				new NameValuePair("c0-methodName", "pageLoaded"), 
				new NameValuePair("c0-id", "0"),
				new NameValuePair("batchId", "0"), 
				new NameValuePair("page", "%2Fret%2Fprice%2Fprice01.jsp"),
				new NameValuePair("httpSessionId", sessionId), 
				new NameValuePair("scriptSessionId", ""),

		});
		System.out.println(res.getResponseBodyAsString());
		String str = res.getResponseBodyAsString();
		Pattern p = Pattern.compile("handleNewScriptSession\\(\"([^\"]+)");
		Matcher m = p.matcher(str);
		m.find();
		String scriptId = m.group(1);
		System.out.println(scriptId);
		
		
		
/**
 * 도 요청 
 * callCount=1
windowName=
c0-scriptName=KabnetService
c0-methodName=queryForList
c0-id=0
c0-param0=string:ADDR.getDoList
c0-param1=null:null
batchId=1
page=%2Fret%2Fprice%2Fprice01.jsp
httpSessionId=0001Tt4lko8-RpXBj6-2oG0j2Q7:14l1rhbma
scriptSessionId=F6A7DF1D74B41CA3FE9E1D832C0F02D6

 */
		res = f.post("/ret/dwr/call/plaincall/KabnetService.queryForObject.dwr", new NameValuePair[] {
				new NameValuePair("callCount", "1"), new NameValuePair("windowName", ""),
				new NameValuePair("c0-scriptName", "KabnetService"),
				new NameValuePair("c0-methodName", "queryForList"), 
				new NameValuePair("c0-param0", "string:ADDR.getDoList"),
				new NameValuePair("c0-param1", "null:null"),
				new NameValuePair("c0-id", "0"),
				
				new NameValuePair("batchId", "0"), 
				new NameValuePair("page", "%2Fret%2Fprice%2Fprice01.jsp"),
				new NameValuePair("httpSessionId", sessionId), 
				new NameValuePair("scriptSessionId", scriptId),

		});
		System.out.println(res.getResponseBodyAsString());
		

		System.out.println("################################");
		//		//ADDR.getCityList
		
//		
//		/**
//		 * callCount=1
//windowName=
//c0-scriptName=KabnetService
//c0-methodName=queryForList
//c0-id=0
//c0-param0=string:ADDR.getDongList
//c0-e1=string:11
//c0-e2=string:68
//c0-param1=Object_Object:{do_code:reference:c0-e1, city_code:reference:c0-e2}
//batchId=4
//page=%2Fret%2Fprice%2Fprice01.jsp
//httpSessionId=0001opzMx5Hx7w_iIOB0zsVb-g2:14l1rhbma
//scriptSessionId=D68225330F104DE31575B77CCE77CE84
//		 */
//
//		res = f.post("/ret/dwr/call/plaincall/KabnetService.queryForObject.dwr", new NameValuePair[] {
//				new NameValuePair("callCount", "1"), new NameValuePair("windowName", ""),
//				new NameValuePair("c0-scriptName", "KabnetService"),
//				new NameValuePair("c0-methodName", "queryForList"), 
//				new NameValuePair("c0-param0", "string:ADDR.getCityList"),
//				new NameValuePair("c0-e1", "string:11"),
//				new NameValuePair("c0-param1", "Object_Object:{do_code:reference:c0-e1}"),
//				new NameValuePair("c0-id", "0"),
//				
//				new NameValuePair("batchId", "0"), 
//				new NameValuePair("page", "%2Fret%2Fprice%2Fprice01.jsp"),
//				new NameValuePair("httpSessionId", sessionId), 
//				new NameValuePair("scriptSessionId", scriptId),
//		});
//		System.out.println(res.getResponseBodyAsString());
//		
//		System.out.println("################################");
//		//ADDR.getDongList
//		res = f.post("/ret/dwr/call/plaincall/KabnetService.queryForObject.dwr", new NameValuePair[] {
//				new NameValuePair("callCount", "1"), new NameValuePair("windowName", ""),
//				new NameValuePair("c0-scriptName", "KabnetService"),
//				new NameValuePair("c0-methodName", "queryForList"), 
//				new NameValuePair("c0-param0", "string:ADDR.getDongList"),
//				new NameValuePair("c0-e1", "string:11"),
//				new NameValuePair("c0-e2", "string:68"),
//				new NameValuePair("c0-param1", "Object_Object:{do_code:reference:c0-e1,city_code:reference:c0-e2}"),
//				new NameValuePair("c0-id", "0"),
//				
//				new NameValuePair("batchId", "0"), 
//				new NameValuePair("page", "%2Fret%2Fprice%2Fprice01.jsp"),
//				new NameValuePair("httpSessionId", sessionId), 
//				new NameValuePair("scriptSessionId", scriptId),
//		});
//		System.out.println(res.getResponseBodyAsString());
//		//dwr.engine.remote.handleCallback("5","0",{status:"OK",result:[{"DONG_NAME":"고덕동","DONG_CODE":"0102"},{"DONG_NAME":"길동","DONG_CODE":"0105"},{"DONG_NAME":"둔촌동","DONG_CODE":"0106"},{"DONG_NAME":"명일동","DONG_CODE":"0101"},{"DONG_NAME":"상일동","DONG_CODE":"0103"},{"DONG_NAME":"성내동","DONG_CODE":"0108"},{"DONG_NAME":"암사동","DONG_CODE":"0107"},{"DONG_NAME":"천호동","DONG_CODE":"0109"}]});
//
//		/**
//		 * callCount=1
//windowName=
//c0-scriptName=KabnetService
//c0-methodName=queryForList
//c0-id=0
//c0-param0=string:RETPRICE_APT.getRetPriceAptList
//c0-e1=string:11740102                      <-  추정컨데 도+도시+동+단지
//c0-param1=Object_Object:{addr_code:reference:c0-e1}
//batchId=6
//page=%2Fret%2Fprice%2Fprice01.jsp
//httpSessionId=0001LD6150-SB1OhmvRBE1XpMlD:14l1rhbma
//scriptSessionId=CB3F6982E563A241A7EF84CBB0903A19
//
//		 */
//		res = f.post("/ret/dwr/call/plaincall/KabnetService.queryForObject.dwr", new NameValuePair[] {
//				new NameValuePair("callCount", "1"), new NameValuePair("windowName", ""),
//				new NameValuePair("c0-scriptName", "KabnetService"),
//				new NameValuePair("c0-methodName", "queryForList"), 
//				new NameValuePair("c0-id", "0"),
//				new NameValuePair("c0-param0", "string:RETPRICE_APT.getRetPriceAptList"),
//				new NameValuePair("c0-e1", "string:11740102"),
//				new NameValuePair("c0-param1", "Object_Object:{do_code:reference:c0-e1}"),
//				new NameValuePair("batchId", "0"), 
//				new NameValuePair("page", "%2Fret%2Fprice%2Fprice01.jsp"),
//				new NameValuePair("httpSessionId", sessionId), 
//				new NameValuePair("scriptSessionId", scriptId),
//		});
//		System.out.println(res.getResponseBodyAsString());
//		
//		//dwr.engine.remote.handleCallback("6","0",{status:"OK",result:[{JUSO:"서울 강동구 고덕동 492","DANJI_AREA":"43개동 2500세대","APTS_CODE":346,"SP_USEAREA_MAX":59,"APTP_DATE":"20100405","SP_USEAREA_MIN":34,"APTS_TOT_DONG":43,"APTS_H_NUM":2500,"APTS_SHT_NAME":null,"TOWN_APT_CODE":1555,"COMPLT_MONTH":"1984-05","SA_PRICE_M2":1271,"APTS_NAME":"고덕시영","RA_PRICE_M2":142},{JUSO:"서울 강동구 고덕동 312","DANJI_AREA":"4개동 448세대","APTS_CODE":345,"SP_USEAREA_MAX":84,"APTP_DATE":"20100405","SP_USEAREA_MIN":59,"APTS_TOT_DONG":4,"APTS_H_NUM":448,"APTS_SHT_NAME":null,"TOWN_APT_CODE":1559,"COMPLT_MONTH":"1995-11","SA_PRICE_M2":633,"APTS_NAME":"배재현대","RA_PRICE_M2":244},{JUSO:"서울 강동구 고덕동 220","DANJI_AREA":"2개동 171세대","APTS_CODE":340,"SP_USEAREA_MAX":107,"APTP_DATE":"20100405","SP_USEAREA_MIN":66,"APTS_TOT_DONG":2,"APTS_H_NUM":171,"APTS_SHT_NAME":null,"TOWN_APT_CODE":1554,"COMPLT_MONTH":"1986-06","SA_PRICE_M2":713,"APTS_NAME":"삼익그린12차","RA_PRICE_M2":193},{JUSO:"서울 강동구 고덕동 486","DANJI_AREA":"8개동 807세대","APTS_CODE":341,"SP_USEAREA_MAX":84,"APTP_DATE":"20100405","SP_USEAREA_MIN":50,"APTS_TOT_DONG":8,"APTS_H_NUM":807,"APTS_SHT_NAME":null,"TOWN_APT_CODE":1556,"COMPLT_MONTH":"1996-03","SA_PRICE_M2":541,"APTS_NAME":"아남","RA_PRICE_M2":218},{JUSO:"서울 강동구 고덕동 212","DANJI_AREA":"71개동 2600세대","APTS_CODE":343,"SP_USEAREA_MAX":55,"APTP_DATE":"20100405","SP_USEAREA_MIN":32,"APTS_TOT_DONG":71,"APTS_H_NUM":2600,"APTS_SHT_NAME":null,"TOWN_APT_CODE":1558,"COMPLT_MONTH":"1982-09","SA_PRICE_M2":1353,"APTS_NAME":"주공2단지","RA_PRICE_M2":141}]});
//
//		
//		
		
		
		
//POST /ret/price/price01_detail.jsp HTTP/1.1
//Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*
//Referer: http://www.ret.co.kr/ret/price/price01.jsp
//Accept-Language: ko
//User-Agent: Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322)
//Content-Type: application/x-www-form-urlencoded
//Accept-Encoding: gzip, deflate
//Host: www.ret.co.kr
//Content-Length: 159
//Connection: Keep-Alive
//Cache-Control: no-cache
//Cookie: JSESSIONID_TW=00017dXvhKs6a7180FdybMmiv1N:13phbtjir; JSESSIONID=0001LD6150-SB1OhmvRBE1XpMlD:14l1rhbma
//
//		apts_code=346&
//		town_apt_code=1555&
//		addr_code=11740102&
//		in_seq=&
//		gubun=4&
//		aptp_date=&
//		do_code=11&
//		city_code=74&
//		dong_code=0102&
//		do_combo=11&
//		city_combo=74&
//		dong_combo=0102
//		 */
		
		
//		/**
//		 * callCount=1
//windowName=
//c0-scriptName=KabnetService
//c0-methodName=queryForObject
//c0-id=0
//c0-param0=string:APT.getAptInfo
//c0-e1=string:346		
//c0-param1=Object_Object:{apts_code:reference:c0-e1}
//batchId=1
//page=%2Fret%2Fprice%2Fprice01_detail.jsp
//httpSessionId=0001LD6150-SB1OhmvRBE1XpMlD:14l1rhbma
//scriptSessionId=3843C92ACC6B14B8B7DE3C98A0E3E189
//		 */
		res = f.post("/ret/dwr/call/plaincall/KabnetService.queryForObject.dwr", new NameValuePair[] {
				new NameValuePair("callCount", "1"), new NameValuePair("windowName", ""),
				new NameValuePair("c0-scriptName", "KabnetService"),
				new NameValuePair("c0-methodName", "queryForList"), 
				new NameValuePair("c0-id", "0"),
				new NameValuePair("c0-param0", "string:APT.getAptInfo"),
				new NameValuePair("c0-e1", "string:346"),
				new NameValuePair("c0-param1", "Object_Object:{apts_code:reference:c0-e1}"),
				new NameValuePair("batchId", "0"), 
				new NameValuePair("page", "%2Fret%2Fprice%2Fprice01.jsp"),
				new NameValuePair("httpSessionId", sessionId), 
				new NameValuePair("scriptSessionId", scriptId),
		});
		System.out.println(res.getResponseBodyAsString());
		
		/**
		 * ################################
throw 'allowScriptTagRemoting is false.';
//#DWR-INSERT
//#DWR-REPLY
dwr.engine.remote.handleCallback("0","0",{status:"OK",result:[{"APTIMG_YN":"Y","APTS_NEAR":"경희대부속병원,우체국,소방서,동사무소,파출소,서울근린공원","HEAT_CODE_NM":"개별난방",JUSO:"서울 강동구 고덕동 492","APTS_COMP":"현대건설,한라건설","DANJI_AREA":"43개동 2500세대","APTS_BUS":null,"APT_CONT":"재건축추진중(안전진단 2004.06.15)","APTS_CODE":346,"APTS_TOT_DONG":43,"APTS_H_NUM":2500,"TOWN_APT_CODE":1555,"FUEL_CODE_NM":"도시가스","COMPLT_MONTH":"198405","MOFF_TEL":"02)429-6174","APTS_NAME":"고덕시영","APTS_SCHOOL":"명덕,묘곡초,배재중,고덕중,광문고,배재고,명일여중"}]});

		 */
//
		
		
		/**
		 * callCount=1
windowName=
c0-scriptName=KabnetService
c0-methodName=queryForList
c0-id=0
c0-param0=string:RETPRICE_PYONG.getRetPricePyongList_info2
c0-e1=string:346
c0-param1=Object_Object:{apts_code:reference:c0-e1}
batchId=2
page=%2Fret%2Fprice%2Fprice01_detail.jsp
httpSessionId=0001LD6150-SB1OhmvRBE1XpMlD:14l1rhbma
scriptSessionId=3843C92ACC6B14B8B7DE3C98A0E3E189

		 */
		res = f.post("/ret/dwr/call/plaincall/KabnetService.queryForObject.dwr", new NameValuePair[] {
				new NameValuePair("callCount", "1"), new NameValuePair("windowName", ""),
				new NameValuePair("c0-scriptName", "KabnetService"),
				new NameValuePair("c0-methodName", "queryForList"), 
				new NameValuePair("c0-id", "0"),
				new NameValuePair("c0-param0", "string:RETPRICE_PYONG.getRetPricePyongList_info2"),
				new NameValuePair("c0-e1", "string:346"),
				new NameValuePair("c0-param1", "Object_Object:{apts_code:reference:c0-e1}"),
				new NameValuePair("batchId", "0"), 
				new NameValuePair("page", "%2Fret%2Fprice%2Fprice01.jsp"),
				new NameValuePair("httpSessionId", sessionId), 
				new NameValuePair("scriptSessionId", scriptId),
		});
		System.out.println(res.getResponseBodyAsString());
		/**
		 * throw 'allowScriptTagRemoting is false.';
//#DWR-INSERT
//#DWR-REPLY
dwr.engine.remote.handleCallback("2","0",{status:"OK",result:[
{"SP_LIVING":0,"PYNIMG_YN":"Y","SP_TOT_HOUSE":800,"SP_LITCHEN":1,"SP_ROOMLIVING":0,"APTS_CODE":346,PTYPE:null,"APT_TYPE_NM":"계단","SP_BATHROOM":1,"SP_USEAREA":34.01,"SP_FLD_AREA":56.52,"SP_ROOM":2,"PYN_TYPE":13},
{"SP_LIVING":0,"PYNIMG_YN":"Y","SP_TOT_HOUSE":1000,"SP_LITCHEN":1,"SP_ROOMLIVING":0,"APTS_CODE":346,PTYPE:"b","APT_TYPE_NM":"계단","SP_BATHROOM":1,"SP_USEAREA":43,"SP_FLD_AREA":71.46,"SP_ROOM":2,"PYN_TYPE":17},
{"SP_LIVING":0,"PYNIMG_YN":"Y","SP_TOT_HOUSE":500,"SP_LITCHEN":1,"SP_ROOMLIVING":1,"APTS_CODE":346,PTYPE:null,"APT_TYPE_NM":"계단","SP_BATHROOM":1,"SP_USEAREA":49.03,"SP_FLD_AREA":81.64,"SP_ROOM":2,"PYN_TYPE":19},
{"SP_LIVING":0,"PYNIMG_YN":"Y","SP_TOT_HOUSE":200,"SP_LITCHEN":1,"SP_ROOMLIVING":0,"APTS_CODE":346,PTYPE:null,"APT_TYPE_NM":"계단","SP_BATHROOM":1,"SP_USEAREA":59,"SP_FLD_AREA":98.04,"SP_ROOM":3,"PYN_TYPE":22}]});

		 */
		
		
		/**
		 * callCount=1
windowName=
c0-scriptName=KabnetService
c0-methodName=queryForList
c0-id=0
c0-param0=string:RETPRICE_PYONG.getRetPricePyongList_Area
c0-e1=string:346
c0-param1=Object_Object:{apts_code:reference:c0-e1}
batchId=3
page=%2Fret%2Fprice%2Fprice01_detail.jsp
httpSessionId=0001LD6150-SB1OhmvRBE1XpMlD:14l1rhbma
scriptSessionId=3843C92ACC6B14B8B7DE3C98A0E3E189

		 */
		res = f.post("/ret/dwr/call/plaincall/KabnetService.queryForObject.dwr", new NameValuePair[] {
				new NameValuePair("callCount", "1"), new NameValuePair("windowName", ""),
				new NameValuePair("c0-scriptName", "KabnetService"),
				new NameValuePair("c0-methodName", "queryForList"), 
				new NameValuePair("c0-id", "0"),
				new NameValuePair("c0-param0", "string:RETPRICE_PYONG.getRetPricePyongList_Area"),
				new NameValuePair("c0-e1", "string:346"),
				new NameValuePair("c0-param1", "Object_Object:{apts_code:reference:c0-e1}"),
				new NameValuePair("batchId", "0"), 
				new NameValuePair("page", "%2Fret%2Fprice%2Fprice01.jsp"),
				new NameValuePair("httpSessionId", sessionId), 
				new NameValuePair("scriptSessionId", scriptId),
		});
		System.out.println(res.getResponseBodyAsString());
		
		/**
		 * throw 'allowScriptTagRemoting is false.';
//#DWR-INSERT
//#DWR-REPLY
dwr.engine.remote.handleCallback("3","0",{status:"OK",result:[{"SL_PRICE":"                43,000","SP_USEAREA":35,"RL_PRICE":"                 4,000","SP_TOT_HOUSE":800,"APTS_NAME":"고덕시영","RU_PRICE":"                 5,000","SA_PRICE":"                44,000","RA_PRICE":"                 4,500","SU_PRICE":"                45,000","PYN_TYPE":13,"APTS_CODE":346,"APTP_DATE":"20100405"},{"SL_PRICE":"                54,000","SP_USEAREA":44,"RL_PRICE":"                 5,500","SP_TOT_HOUSE":1000,"APTS_NAME":"고덕시영","RU_PRICE":"                 6,000","SA_PRICE":"                54,500","RA_PRICE":"                 5,750","SU_PRICE":"                55,000","PYN_TYPE":17,"APTS_CODE":346,"APTP_DATE":"20100405"},{"SL_PRICE":"                62,000","SP_USEAREA":50,"RL_PRICE":"                 7,000","SP_TOT_HOUSE":500,"APTS_NAME":"고덕시영","RU_PRICE":"                 8,000","SA_PRICE":"                63,000","RA_PRICE":"                 7,500","SU_PRICE":"                64,000","PYN_TYPE":19,"APTS_CODE":346,"APTP_DATE":"20100405"},{"SL_PRICE":"                72,000","SP_USEAREA":59,"RL_PRICE":"                 9,000","SP_TOT_HOUSE":200,"APTS_NAME":"고덕시영","RU_PRICE":"                10,500","SA_PRICE":"                73,000","RA_PRICE":"                 9,750","SU_PRICE":"                74,000","PYN_TYPE":22,"APTS_CODE":346,"APTP_DATE":"20100405"}]});


		 */

		/**
		 * callCount=1
windowName=
c0-scriptName=KabnetService
c0-methodName=queryForList
c0-id=0
c0-param0=string:RETPRICE_PYONG.getRetPricePyong_year
c0-e1=string:346
c0-e2=number:13
c0-param1=Object_Object:{apts_code:reference:c0-e1, pyn_type:reference:c0-e2}
batchId=7
page=%2Fret%2Fprice%2Fprice01_detail.jsp
httpSessionId=0001LD6150-SB1OhmvRBE1XpMlD:14l1rhbma
scriptSessionId=3843C92ACC6B14B8B7DE3C98A0E3E189


		 */
		res = f.post("/ret/dwr/call/plaincall/KabnetService.queryForObject.dwr", new NameValuePair[] {
				new NameValuePair("callCount", "1"), new NameValuePair("windowName", ""),
				new NameValuePair("c0-scriptName", "KabnetService"),
				new NameValuePair("c0-methodName", "queryForList"), 
				new NameValuePair("c0-id", "0"),
				new NameValuePair("c0-param0", "string:RETPRICE_PYONG.getRetPricePyongList_year"),
				new NameValuePair("c0-e1", "string:346"),
				new NameValuePair("c0-param1", "Object_Object:{apts_code:reference:c0-e1}"),
				new NameValuePair("batchId", "0"), 
				new NameValuePair("page", "%2Fret%2Fprice%2Fprice01.jsp"),
				new NameValuePair("httpSessionId", sessionId), 
				new NameValuePair("scriptSessionId", scriptId),
		});
		System.out.println(res.getResponseBodyAsString());
		/**
		 * throw 'allowScriptTagRemoting is false.';
//#DWR-INSERT
//#DWR-REPLY
dwr.engine.remote.handleCallback("6","0",{status:"OK",result:[{"RU_COLOR":"base","SU_CHANGE":-1000,"SU_PRICE":"                45,000","APTS_CODE":346,"APTP_DATE":"20100320","SL_PRICE":"                43,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"down","RU_PRICE":"                 5,500","SA_PRICE":"                44,000","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"                46,000","APTS_CODE":346,"APTP_DATE":"20100220","SL_PRICE":"                44,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"base","RU_PRICE":"                 5,500","SA_PRICE":"                45,000","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"                46,000","APTS_CODE":346,"APTP_DATE":"20100120","SL_PRICE":"                44,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"base","RU_PRICE":"                 5,500","SA_PRICE":"                45,000","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":-1000,"SU_PRICE":"                46,000","APTS_CODE":346,"APTP_DATE":"20091220","SL_PRICE":"                44,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"down","RU_PRICE":"                 5,500","SA_PRICE":"                45,000","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":-1000,"SU_PRICE":"                47,000","APTS_CODE":346,"APTP_DATE":"20091120","SL_PRICE":"                45,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"down","RU_PRICE":"                 5,500","SA_PRICE":"                46,000","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"                48,000","APTS_CODE":346,"APTP_DATE":"20091020","SL_PRICE":"                46,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"base","RU_PRICE":"                 5,500","SA_PRICE":"                47,000","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"                48,000","APTS_CODE":346,"APTP_DATE":"20090920","SL_PRICE":"                46,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"base","RU_PRICE":"                 5,500","SA_PRICE":"                47,000","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":5000,"SU_PRICE":"                48,000","APTS_CODE":346,"APTP_DATE":"20090820","SL_PRICE":"                46,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"up","RU_PRICE":"                 5,500","SA_PRICE":"                47,000","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":2000,"SU_PRICE":"                43,000","APTS_CODE":346,"APTP_DATE":"20090720","SL_PRICE":"                40,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"up","RU_PRICE":"                 5,500","SA_PRICE":"                41,500","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":2000,"SU_PRICE":"                41,000","APTS_CODE":346,"APTP_DATE":"20090620","SL_PRICE":"                38,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"up","RU_PRICE":"                 5,500","SA_PRICE":"                39,500","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"                39,000","APTS_CODE":346,"APTP_DATE":"20090520","SL_PRICE":"                36,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"base","RU_PRICE":"                 5,500","SA_PRICE":"                37,500","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":3000,"SU_PRICE":"                39,000","APTS_CODE":346,"APTP_DATE":"20090420","SL_PRICE":"                36,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"up","RU_PRICE":"                 5,500","SA_PRICE":"                37,500","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":2000,"SU_PRICE":"                36,000","APTS_CODE":346,"APTP_DATE":"20090320","SL_PRICE":"                33,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"up","RU_PRICE":"                 5,500","SA_PRICE":"                34,500","RA_PRICE":"                 5,000"}]});


		 */
		
		/**
		 * [
		 * {"RU_COLOR":"base","SU_CHANGE":-1000,"SU_PRICE":"                45,000","APTS_CODE":346,"APTP_DATE":"20100320","SL_PRICE":"                43,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"down","RU_PRICE":"                 5,500","SA_PRICE":"                44,000","RA_PRICE":"                 5,000"},
		 * {"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"                46,000","APTS_CODE":346,"APTP_DATE":"20100220","SL_PRICE":"                44,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"base","RU_PRICE":"                 5,500","SA_PRICE":"                45,000","RA_PRICE":"                 5,000"},
		 * {"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"                46,000","APTS_CODE":346,"APTP_DATE":"20100120","SL_PRICE":"                44,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"base","RU_PRICE":"                 5,500","SA_PRICE":"                45,000","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":-1000,"SU_PRICE":"                46,000","APTS_CODE":346,"APTP_DATE":"20091220","SL_PRICE":"                44,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"down","RU_PRICE":"                 5,500","SA_PRICE":"                45,000","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":-1000,"SU_PRICE":"                47,000","APTS_CODE":346,"APTP_DATE":"20091120","SL_PRICE":"                45,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"down","RU_PRICE":"                 5,500","SA_PRICE":"                46,000","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"                48,000","APTS_CODE":346,"APTP_DATE":"20091020","SL_PRICE":"                46,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"base","RU_PRICE":"                 5,500","SA_PRICE":"                47,000","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"                48,000","APTS_CODE":346,"APTP_DATE":"20090920","SL_PRICE":"                46,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"base","RU_PRICE":"                 5,500","SA_PRICE":"                47,000","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":5000,"SU_PRICE":"                48,000","APTS_CODE":346,"APTP_DATE":"20090820","SL_PRICE":"                46,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"up","RU_PRICE":"                 5,500","SA_PRICE":"                47,000","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":2000,"SU_PRICE":"                43,000","APTS_CODE":346,"APTP_DATE":"20090720","SL_PRICE":"                40,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"up","RU_PRICE":"                 5,500","SA_PRICE":"                41,500","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":2000,"SU_PRICE":"                41,000","APTS_CODE":346,"APTP_DATE":"20090620","SL_PRICE":"                38,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"up","RU_PRICE":"                 5,500","SA_PRICE":"                39,500","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"                39,000","APTS_CODE":346,"APTP_DATE":"20090520","SL_PRICE":"                36,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"base","RU_PRICE":"                 5,500","SA_PRICE":"                37,500","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":3000,"SU_PRICE":"                39,000","APTS_CODE":346,"APTP_DATE":"20090420","SL_PRICE":"                36,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"up","RU_PRICE":"                 5,500","SA_PRICE":"                37,500","RA_PRICE":"                 5,000"},{"RU_COLOR":"base","SU_CHANGE":2000,"SU_PRICE":"                36,000","APTS_CODE":346,"APTP_DATE":"20090320","SL_PRICE":"                33,000","SP_USEAREA":34.01,"RU_CHANGE":0,"RL_PRICE":"                 4,500","SU_COLOR":"up","RU_PRICE":"                 5,500","SA_PRICE":"                34,500","RA_PRICE":"                 5,000"}]});
		 */
		
		
		
		
		/**
		 * callCount=1
windowName=
c0-scriptName=KabnetService
c0-methodName=queryForList
c0-id=0
c0-param0=string:RETPRICE_PYONG.getRetPricePyongList_info
c0-e1=string:18371
c0-e2=number:55
c0-param1=Object_Object:{apts_code:reference:c0-e1, pyn_type:reference:c0-e2}
batchId=12
page=%2Fret%2Fprice%2Fprice01_detail.jsp
httpSessionId=0001fXa1Jf74t4aSJBjSAF8s6pw:14l1rhbma
scriptSessionId=E615559CBD02792E65306AE50AEBD6D4

		 */
		/**
		 * throw 'allowScriptTagRemoting is false.';
//#DWR-INSERT
//#DWR-REPLY
dwr.engine.remote.handleCallback("13","0",{status:"OK",result:[{"TRADE_YYYY":"2010"},{"TRADE_YYYY":"2009"},{"TRADE_YYYY":"2008"}]});

		 */

/**
 * callCount=1
windowName=
c0-scriptName=KabnetService
c0-methodName=queryForList
c0-id=0
c0-param0=string:RETPRICE_PYONG.getRetPricePyong_year
c0-e1=string:18371
c0-e2=number:55
c0-param1=Object_Object:{apts_code:reference:c0-e1, pyn_type:reference:c0-e2}
batchId=13
page=%2Fret%2Fprice%2Fprice01_detail.jsp
httpSessionId=0001fXa1Jf74t4aSJBjSAF8s6pw:14l1rhbma
scriptSessionId=E615559CBD02792E65306AE50AEBD6D4

 */
		/**
		 * throw 'allowScriptTagRemoting is false.';
//#DWR-INSERT
//#DWR-REPLY
dwr.engine.remote.handleCallback("12","0",{status:"OK",result:[{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               174,000","APTS_CODE":18371,"APTP_DATE":"20100320","SL_PRICE":"               153,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                55,000","SU_COLOR":"base","RU_PRICE":"                60,000","SA_PRICE":"               163,500","RA_PRICE":"                57,500"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               174,000","APTS_CODE":18371,"APTP_DATE":"20100220","SL_PRICE":"               153,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                55,000","SU_COLOR":"base","RU_PRICE":"                60,000","SA_PRICE":"               163,500","RA_PRICE":"                57,500"},{"RU_COLOR":"up","SU_CHANGE":0,"SU_PRICE":"               174,000","APTS_CODE":18371,"APTP_DATE":"20100120","SL_PRICE":"               153,000","SP_USEAREA":153.8,"RU_CHANGE":3000,"RL_PRICE":"                55,000","SU_COLOR":"base","RU_PRICE":"                60,000","SA_PRICE":"               163,500","RA_PRICE":"                57,500"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               174,000","APTS_CODE":18371,"APTP_DATE":"20091220","SL_PRICE":"               153,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"base","RU_PRICE":"                57,000","SA_PRICE":"               163,500","RA_PRICE":"                53,500"},{"RU_COLOR":"base","SU_CHANGE":1000,"SU_PRICE":"               174,000","APTS_CODE":18371,"APTP_DATE":"20091120","SL_PRICE":"               153,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"up","RU_PRICE":"                57,000","SA_PRICE":"               163,500","RA_PRICE":"                53,500"},{"RU_COLOR":"base","SU_CHANGE":3000,"SU_PRICE":"               173,000","APTS_CODE":18371,"APTP_DATE":"20091020","SL_PRICE":"               152,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"up","RU_PRICE":"                57,000","SA_PRICE":"               162,500","RA_PRICE":"                53,500"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               170,000","APTS_CODE":18371,"APTP_DATE":"20090920","SL_PRICE":"               150,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"base","RU_PRICE":"                57,000","SA_PRICE":"               160,000","RA_PRICE":"                53,500"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               170,000","APTS_CODE":18371,"APTP_DATE":"20090820","SL_PRICE":"               150,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"base","RU_PRICE":"                57,000","SA_PRICE":"               160,000","RA_PRICE":"                53,500"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               170,000","APTS_CODE":18371,"APTP_DATE":"20090720","SL_PRICE":"               150,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"base","RU_PRICE":"                57,000","SA_PRICE":"               160,000","RA_PRICE":"                53,500"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               170,000","APTS_CODE":18371,"APTP_DATE":"20090620","SL_PRICE":"               150,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"base","RU_PRICE":"                57,000","SA_PRICE":"               160,000","RA_PRICE":"                53,500"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               170,000","APTS_CODE":18371,"APTP_DATE":"20090520","SL_PRICE":"               150,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"base","RU_PRICE":"                57,000","SA_PRICE":"               160,000","RA_PRICE":"                53,500"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               170,000","APTS_CODE":18371,"APTP_DATE":"20090420","SL_PRICE":"               150,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"base","RU_PRICE":"                57,000","SA_PRICE":"               160,000","RA_PRICE":"                53,500"},{"RU_COLOR":"base","SU_CHANGE":0,"SU_PRICE":"               170,000","APTS_CODE":18371,"APTP_DATE":"20090320","SL_PRICE":"               150,000","SP_USEAREA":153.8,"RU_CHANGE":0,"RL_PRICE":"                50,000","SU_COLOR":"base","RU_PRICE":"                57,000","SA_PRICE":"               160,000","RA_PRICE":"                53,500"}]});

		 */
		
		
		/**
		 * callCount=1
windowName=
c0-scriptName=KabnetService
c0-methodName=queryForList
c0-id=0
c0-param0=string:RETPRICE_PYONG.getRetPricePyongList_info3
c0-e1=string:2010
c0-e2=string:1
c0-e3=string:18371
c0-param1=Object_Object:{year:reference:c0-e1, month:reference:c0-e2, apts_code:reference:c0-e3}
batchId=14
page=%2Fret%2Fprice%2Fprice01_detail.jsp
httpSessionId=0001fXa1Jf74t4aSJBjSAF8s6pw:14l1rhbma
scriptSessionId=E615559CBD02792E65306AE50AEBD6D4


throw 'allowScriptTagRemoting is false.';
//#DWR-INSERT
//#DWR-REPLY
dwr.engine.remote.handleCallback("14","0",{status:"OK",result:[{M1:null,M2:null,M3:null,"TRADE_DATE":"01 ~ 10","PYN_TYPE":135,"DISP_PYN_TYPE":null},{M1:null,M2:null,M3:null,"TRADE_DATE":"11 ~ 20","PYN_TYPE":135,"DISP_PYN_TYPE":135},{M1:null,M2:null,M3:null,"TRADE_DATE":"21 ~ 31","PYN_TYPE":135,"DISP_PYN_TYPE":null},{M1:null,M2:null,M3:null,"TRADE_DATE":"01 ~ 10","PYN_TYPE":154,"DISP_PYN_TYPE":null},{M1:null,M2:null,M3:null,"TRADE_DATE":"11 ~ 20","PYN_TYPE":154,"DISP_PYN_TYPE":154},{M1:null,M2:null,M3:null,"TRADE_DATE":"21 ~ 31","PYN_TYPE":154,"DISP_PYN_TYPE":null},{M1:null,M2:null,M3:null,"TRADE_DATE":"01 ~ 10","PYN_TYPE":171,"DISP_PYN_TYPE":null},{M1:null,M2:null,M3:null,"TRADE_DATE":"11 ~ 20","PYN_TYPE":171,"DISP_PYN_TYPE":171},{M1:null,M2:null,M3:null,"TRADE_DATE":"21 ~ 31","PYN_TYPE":171,"DISP_PYN_TYPE":null}]});

		 */
	}
}
