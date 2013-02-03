package net.narusas.si.auction.fetchers;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class 경매공매사이트FetcherTest {

	@Test
	public void test() {
		String src ="		<td style=\"white-space: pre-line;\"><a href=\"javascript:DetailView('서울중앙지법',2012,27316,1)\">서울 동작구 상도동 242-62 이조휴엔빌리지 4층 402호</a>";
		Pattern p = Pattern.compile("javascript:DetailView\\('([^']+)',(\\d+),(\\d+),(\\d+)");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("서울중앙지법", m.group(1));
		assertEquals("2012", m.group(2));
		assertEquals("27316", m.group(3));
		assertEquals("1", m.group(4));
	}
	
	@Test
	public void 마지막페이지(){
		String src="<div align=\"center\" class=\"prOff\">&nbsp;<font color='#333333' style='font-family:verdana'><b>1</b></font>ㆍ<a href='javascript:gotoPage(2)' class=navi>2</a>ㆍ<a href='javascript:gotoPage(3)' class=navi>3</a>ㆍ<a href='javascript:gotoPage(4)' class=navi>4</a>ㆍ<a href='javascript:gotoPage(5)' class=navi>5</a>&nbsp;&nbsp;<a href='javascript:gotoPage(5)' class=navi><img src='/images/arrow_last.gif' width='12' height='10' align='absmiddle' border='0' hspace='2' alt='끝으로'></a></div>";
		Pattern p = Pattern.compile("gotoPage\\((\\d+)");
		Matcher m = p.matcher(src);
		while(m.find()){
			System.out.println(m.group(1));
		}
		
	}
	
	@Test
	public void 등기부등본(){
		String src ="<input type='hidden' name='popid' value=''><input type='hidden' name='land_certify' value='JJ01/1102/2011/035/1102-2011035860-0002-A.pdf'><input type='hidden' name='build_certify' value='JJ01/1102/2011/035/1102-2011035860-0002-B.pdf'><input type='hidden' name='bubwoncd' value='1102'>";
		Pattern p = Pattern.compile("name='land_certify'\\s+value='([^']+).*name='build_certify'\\s+value='([^']+)");
		Matcher m = p.matcher(src);
		assertTrue(m.find());
		assertEquals("JJ01/1102/2011/035/1102-2011035860-0002-A.pdf", m.group(1));
		assertEquals("JJ01/1102/2011/035/1102-2011035860-0002-B.pdf", m.group(2));
	}
	
	@Test
	public void 등기부URL() throws UnsupportedEncodingException {
		경매공매사이트Fetcher.사건 s = new 경매공매사이트Fetcher.사건(); 
		s.set법원명("서울중앙지법");
		s.setYear(2011);
		s.setNo(35860);
		s.setSeq(2);
		assertEquals("bubwon=%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%FD&num1=2011&num2=35860&num3=2",
				String.format(
						"bubwon=%s&num1=%s&num2=%s&num3=%s",
						new Object[]{
								URLEncoder.encode(s.get법원명(),"EUC-KR"),
								s.getYear(), s.getNo(), s.getSeq()
						}
						)		
		);
		
	}
	
	@Test
	public void ttt() throws UnsupportedEncodingException {
		String src = "%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%FD";
		System.out.println(URLDecoder.decode(src,"EUC-KR"));
	}

}
