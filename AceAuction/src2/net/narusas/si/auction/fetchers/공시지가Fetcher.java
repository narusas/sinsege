package net.narusas.si.auction.fetchers;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.narusas.si.auction.model.물건;

/**
 * 
 * @author narusas
 * 
 */
public class 공시지가Fetcher {
	final Logger logger = LoggerFactory.getLogger("auction");
	private 물건 goods;
	private String 링크정보;

	public 공시지가Fetcher fill공시지가(물건 goods) {
		this.goods = goods;
		return this;
	}

	// POST /OnnaraLink.laf HTTP/1.1
	// Host: www.courtauction.go.kr
	// User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2)
	// AppleWebKit/534.52.7 (KHTML, like Gecko) Version/5.1.2 Safari/534.52.7
	// Content-Length: 25
	// Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
	// Origin: http://www.courtauction.go.kr
	// Content-Type: application/x-www-form-urlencoded
	// Referer:
	// http://www.courtauction.go.kr/RetrieveRealEstCarHvyMachineMulDetailInfo.laf
	// Accept-Language: ko-kr
	// Accept-Encoding: gzip, deflate
	// Cookie: locIdx=201101300029661;
	// toMul=%B0%C5%C3%A2%C1%F6%BF%F8%2C20110130002966%2C1%2C20111223%2CB%5E%BC%AD%BF%EF%C1%DF%BE%D3%C1%F6%B9%E6%B9%FD%BF%F8%2C20100130012570%2C1%2C20111229%2CB;
	// page=default20; realJiwonNm=%B0%C5%C3%A2%C1%F6%BF%F8; daepyoSidoCd=;
	// daepyoSiguCd=; mvJiwonNm=%BF%EF%BB%EA%C1%F6%B9%E6%B9%FD%BF%F8;
	// WMONID=PsJXiAUFBQR
	// Pragma: no-cache
	// Connection: keep-alive
	//
	// q_pnu=4889033029201080000

	public 공시지가Fetcher from물건내역HTML(String chunk) throws HttpException, IOException {
		// q_pnu
		if (has공시지가링크(chunk) == false) {
			return this;
		}
		링크정보 = parse링크정보(chunk);
		return this;
	}

	public String 공시지가() throws HttpException, IOException {
		String html = fetch1단계링크(링크정보);
		String 키정보 = parse키정보(html);
		html = fetch공시지가페이지(키정보, 링크정보);
		return parse공시지가(html);
	}

	public String fetch1단계링크(String 링크정보) throws HttpException, IOException {
		대법원Fetcher.getInstance().prepare();
		String html = 대법원Fetcher.getInstance().fetch("/OnnaraLink.laf?q_pnu=" + 링크정보);
		return html;
	}

	public boolean has공시지가링크(String chunk) {
		boolean has공시지가링크 = chunk.contains("javascript:showGongsiJiga");
		logger.info("공시지가 링크 존재 여부 : " + has공시지가링크);
		return has공시지가링크;
	}

	public String parse링크정보(String chunk) {
		Matcher m = Pattern.compile("javascript:showGongsiJiga\\('(\\d+)").matcher(chunk);
		if (m.find() == false) {
			return null;
		}
		String 공시지가링크 = m.group(1);
		logger.info("공시지가 링크:" + 공시지가링크);
		return 공시지가링크;
	}

	public String parse키정보(String html) {
		Matcher m = Pattern.compile("name=\"key\" value=\"([^\"]+)").matcher(html);
		if (m.find() == false) {
			logger.info("공시지가 키 정보가 없습니다.");
			return null;
		}
		String 키정보 = m.group(1);
		logger.info("공시지가 키 정보:" + 키정보);

		return 키정보;
	}

	// POST /pt/map/klisMapInfo.jsp HTTP/1.1
	// Host: www.onnara.go.kr
	// User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2)
	// AppleWebKit/534.52.7 (KHTML, like Gecko) Version/5.1.2 Safari/534.52.7
	// Content-Length: 64
	// Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
	// Origin: http://www.courtauction.go.kr
	// Content-Type: application/x-www-form-urlencoded
	// Referer: http://www.courtauction.go.kr/OnnaraLink.laf
	// Accept-Language: ko-kr
	// Accept-Encoding: gzip, deflate
	// Cookie:
	// JSESSIONID=KyFBTsLGC1LRc4vjjcwyWkVM6BQ3fvMlX255D7yHCG3DDpvCl3QG!-541412877
	// Pragma: no-cache
	// Connection: keep-alive
	//
	// key=GML5mo7kFs%2FiJTenVEJQ9DkVLh4OBEj6&q_pnu=4889033029201080000

	public String fetch공시지가페이지(String key, String q_pnu) throws HttpException, IOException {
		logger.info("온나라에서 공시지가 페이지를 가져 옵니다. URL: http://www.onnara.go.kr/pt/map/klisMapInfo.jsp?key=" + key + "&q_pnu=" + q_pnu);
		PageFetcher f = new PageFetcher("http://www.onnara.go.kr");
		PostMethod post = f.post("/pt/map/klisMapInfo.jsp", new NameValuePair[] { new NameValuePair("key", key),
				new NameValuePair("q_pnu", q_pnu)

		});
		return post.getResponseBodyAsString();
	}

	public String parse공시지가(String html) {
		
		if (html == null || html.contains("<!-- 공시지가 -->") == false) {
			logger.info("공시지가 정보가 없습니다");
			return null;
		}
		System.out.println(html);
		html = html.substring(html.indexOf("<!-- 공시지가 -->"));
		html = html.substring(0, html.indexOf("</div>"));
		String 공시지가 = HTMLUtils.strip(html).replaceAll("\n", " ");
		logger.info("분석된 공시지가:" + 공시지가);
		return 공시지가;

	}

}
