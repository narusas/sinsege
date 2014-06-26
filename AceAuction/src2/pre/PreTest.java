package pre;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

public class PreTest {

	@Test
	public void 담당계목록얻기() {
		PreFetcher f = new PreFetcher();
		List<담당계> list = f.fetch담당계("서울중앙지방법원");
		System.out.println(list);
		
		//[담당계 [id=1001, name=경매1계], 담당계 [id=1002, name=경매2계], 담당계 [id=1003, name=경매3계], 담당계 [id=1004, name=경매4계], 담당계 [id=1005, name=경매5계], 담당계 [id=1006, name=경매6계], 담당계 [id=1007, name=경매7계], 담당계 [id=1008, name=경매8계], 담당계 [id=1009, name=경매9계], 담당계 [id=1010, name=경매10계], 담당계 [id=1011, name=경매11계], 담당계 [id=1012, name=경매12계], 담당계 [id=1013, name=경매13계], 담당계 [id=1014, name=경매14계], 담당계 [id=1015, name=경매15계], 담당계 [id=1021, name=경매21계], 담당계 [id=1022, name=경매22계]]

	}

	@Test
	public void  목록얻기() throws Exception {
		//Request URL:https://www.courtauction.go.kr/RetrieveBdangYoguJonggiNotify.laf
//		Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
//				Accept-Encoding:gzip,deflate,sdch
//				Accept-Language:ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4
//				Cache-Control:max-age=0
//				Connection:keep-alive
//				Content-Length:407
//				Content-Type:application/x-www-form-urlencoded
//				Cookie:WMONID=gHuVDlBl7tg
//				Host:www.courtauction.go.kr
//				Origin:https://www.courtauction.go.kr
//				Referer:https://www.courtauction.go.kr/InitMulSrch.laf
//				User-Agent:Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36
		
//		srnID:PNO101005
//		srchMthd:1
//		jiwonNm:(unable to decode value)
//		jpDeptCd:1001
//		saYear:2014
//		saSer:
//		daepyoSidoCd:
//		daepyoSiguCd:
//		daepyoDongCd:
//		rd1Cd:
//		rd2Cd:
//		realVowel:35207_45207
//		rd3Rd4Cd:
//		ihrelNm:
//		_NAVI_CMD:
//		_NAVI_SRNID:
//		_SRCH_SRNID:PNO101005
//		_CUR_CMD:InitMulSrch.laf
//		_CUR_SRNID:PNO101005
//		_NEXT_CMD:RetrieveBdangYoguJonggiNotify.laf
//		_NEXT_SRNID:PNO101005
//		_PRE_SRNID:
//		_LOGOUT_CHK:
//		_FORM_YN:Y
		// srnID=PNO101005&srchMthd=1&jiwonNm=%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%E6%B9%FD%BF%F8&jpDeptCd=1001&saYear=2014&saSer=&daepyoSidoCd=&daepyoSiguCd=&daepyoDongCd=&rd1Cd=&rd2Cd=&realVowel=35207_45207&rd3Rd4Cd=&ihrelNm=&_NAVI_CMD=&_NAVI_SRNID=&_SRCH_SRNID=PNO101005&_CUR_CMD=InitMulSrch.laf&_CUR_SRNID=PNO101005&_NEXT_CMD=RetrieveBdangYoguJonggiNotify.laf&_NEXT_SRNID=PNO101005&_PRE_SRNID=&_LOGOUT_CHK=&_FORM_YN=Y
		HttpClient client = new HttpClient();
		HttpState state = new HttpState();

//		PostMethod m = new PostMethod("https://www.courtauction.go.kr/RetrieveBdangYoguJonggiNotify.laf?srnID=PNO101005&srchMthd=1&jiwonNm=%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%E6%B9%FD%BF%F8&jpDeptCd=1001&saYear=2014&saSer=&daepyoSidoCd=&daepyoSiguCd=&daepyoDongCd=&rd1Cd=&rd2Cd=&realVowel=35207_45207&rd3Rd4Cd=&ihrelNm=&_NAVI_CMD=&_NAVI_SRNID=&_SRCH_SRNID=PNO101005&_CUR_CMD=InitMulSrch.laf&_CUR_SRNID=PNO101005&_NEXT_CMD=RetrieveBdangYoguJonggiNotify.laf&_NEXT_SRNID=PNO101005&_PRE_SRNID=&_LOGOUT_CHK=&_FORM_YN=Y");
		PostMethod m = new PostMethod("https://www.courtauction.go.kr/RetrieveBdangYoguJonggiNotify.laf?srnID=PNO101005&srchMthd=1&jiwonNm=%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%E6%B9%FD%BF%F8&jpDeptCd=1001&saYear=2014");

		m.setRequestHeader("Host", "www.courtauction.go.kr");
		m.setRequestHeader("Referer", "https://www.courtauction.go.kr/InitMulSrch.laf");
		client.executeMethod(m);
		 System.out.println(m.getResponseBodyAsString());
	}
	
	@Test
	public void ttt() throws Exception, IOException {
		//
		HttpClient client = new HttpClient();
		HttpState state = new HttpState();
		PostMethod m = new PostMethod("https://www.courtauction.go.kr/RetrieveRealEstDetailInqSaList.laf?"
				+ "jiwonNm=%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%E6%B9%FD%BF%F8"
				+ "&saNo=20140130004508"
				+ "&srnID=PNO101005"
				+ "&_SRCH_SRNID=PNO101005"
				);
		

		m.setRequestHeader("Host", "www.courtauction.go.kr");
		m.setRequestHeader("Referer", "https://www.courtauction.go.kr/RetrieveBdangYoguJonggiNotify.laf");
//		Cookie:WMONID=gHuVDlBl7tg; mvJiwonNm=%9C%B8%EC%99%EC%A7%80%EB%B0%A9%EB%90; realJiwonNm=%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%E6%B9%FD%BF%F8; daepyoSidoCd=; daepyoSiguCd=; rd1Cd=; rd2Cd=; realVowel=35207_45207; locIdx=201401300038880

		//://Origin:https://www.courtauction.go.kr
//		Referer:https://www.courtauction.go.kr/RetrieveBdangYoguJonggiNotify.laf

		client.executeMethod(m);
		 System.out.println(m.getResponseBodyAsString());
	}
	
	@Test
	public void url() throws UnsupportedEncodingException {
		String url = "%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%E6%B9%FD%BF%F8";
		System.out.println(URLDecoder.decode(url,"UTF-8"));
	}
}
