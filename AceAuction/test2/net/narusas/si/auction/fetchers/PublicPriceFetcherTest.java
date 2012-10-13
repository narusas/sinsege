package net.narusas.si.auction.fetchers;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.junit.Test;


import org.junit.Test;

public class PublicPriceFetcherTest {


	@Test
	public void test링크정보() {
		String src = "onclick=\"javascript:showGongsiJiga('4889033029201080000'); return false;\">";
		공시지가Fetcher f = new 공시지가Fetcher();
		assertEquals("4889033029201080000", f.parse링크정보(src));
	}

	@Test
	public void test키정보() {
		String src = "<input type=\"hidden\" name=\"key\" value=\"GML5mo7kFs/iJTenVEJQ9DkVLh4OBEj6\" />";
		공시지가Fetcher f = new 공시지가Fetcher();
		assertEquals("GML5mo7kFs/iJTenVEJQ9DkVLh4OBEj6", f.parse키정보(src));
	}

	
	public void testFetch키정보페이지() throws HttpException, IOException {
		공시지가Fetcher f = new 공시지가Fetcher();
		System.out.println(f.fetch1단계링크("4889033029201080000"));
	}
	
	
	@Test
	public void testParse공시지가() {
		String src = "				<!-- 공시지가 -->\n"+
				
				
"					<div class=\"mapbox01\" style=\"padding:12 0 0 0; width:680px; float:left\">\n"+
"						<span class=\"st\">공시지가(2011.1월 기준)</span>\n"+
"						<strong style=\"padding:0 0 0 110px\"></strong>  <span class=\"orangeb\">             185</span><span class=\"blueb\">원 /m<sup>2</sup></span>\n"+
"						&nbsp;\n"+
"						<img src=\"/ext/image/old/common/btn/btn_last10year.gif\" style=\"vertical-align:middle\" onclick=\"onPunPopup();\" style=\"cursor:hand;\">\n"+
"					</div>\n"+
"				\n"+
"				<!--// 공시지가 -->\n";
		
		공시지가Fetcher f = new 공시지가Fetcher();
		assertEquals("공시지가(2011.1월 기준)  185원 /m2", f.parse공시지가(src));
	}
	
	@Test
	public void testFetch온나라페이지() throws HttpException, IOException {
		공시지가Fetcher f = new 공시지가Fetcher();
		assertEquals("공시지가(2012.1월 기준)  195원 /m2", f.parse공시지가(f.fetch공시지가페이지("GML5mo7kFs/iJTenVEJQ9DkVLh4OBEj6", "4889033029201080000")));
	}


}