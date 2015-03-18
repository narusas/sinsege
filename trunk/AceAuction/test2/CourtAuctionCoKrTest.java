import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.fetchers.HTMLUtils;
import net.narusas.si.auction.fetchers.PageFetcher;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.junit.Test;


public class CourtAuctionCoKrTest {

	@Test
	public void test() throws HttpException, IOException {
		PageFetcher f = new PageFetcher("");
		
		f.post("http://www.courtauction.co.kr/lib/loginprocess.asp", new NameValuePair[]{
				new NameValuePair("type","login"),
				new NameValuePair("loginid","radio"),
				new NameValuePair("loginpw","kch8888"),
				
		});
		
//		GET /maemul/maemul_view.asp?bubwon=%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%FD&num1=2013&num2=17248&num3=1 HTTP/1.1

		String court = "서울중앙지법";
		String eventYear = "2013";
		String eventNo = "17248";
		String mulNo = "1";
		
		String html = f.fetch("http://www.courtauction.co.kr/maemul/maemul_view.asp?bubwon="+HTMLUtils.encodeUrl(court, "EUC-KR")+"&num1="+eventYear+"&num2="+eventNo+"&num3="+mulNo);
		
		
		System.out.println(html);
	}
	
	@Test
	public void test2() throws IOException {
		  String text = NFile.getText(new File("fixture2/095_CourtAuctionCoKr.html"), "UTF-8");
//		  System.out.println(text);
		 Pattern p = Pattern.compile("name=\"dungki\\d+\" value=\"([^\"]*)", Pattern.MULTILINE);
		 Matcher m = p.matcher(text);
		 while(m.find()){
			 System.out.println(m.group(1));
		 }
				 
	}

}
