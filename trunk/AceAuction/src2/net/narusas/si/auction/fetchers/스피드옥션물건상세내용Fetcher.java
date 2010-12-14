package net.narusas.si.auction.fetchers;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;

public class 스피드옥션물건상세내용Fetcher extends 스피드옥션Fetcher {

	static boolean debug = false;

	static Logger logger = Logger.getLogger("log");

	static Pattern tagPattern = Pattern.compile("(<[^>]*>)");

	static Pattern 감정시점Pattern = Pattern.compile("감정시점 : (\\d+-\\d+-\\d+)");

	static Pattern 경매개시결정Pattern = Pattern.compile("경매개시결정 : ([^<]+)");

	static Pattern 배당요구종기Pattern = Pattern.compile("배당요구종기 : ([^<]+)");

	static Pattern 보존등기일Pattern = Pattern.compile("보존등기일:(\\d+-\\d+-\\d+)");

	static Pattern 사건접수Pattern = Pattern.compile("사건접수\\s+([^<^\\s]+)");
	static Pattern 입찰방법Pattern = Pattern.compile("입찰방법 : ([^<]+)");

	public 스피드옥션물건상세내용Fetcher() throws HttpException, IOException {
		super();

	}

	public String getPage(String court, String 담당계, int eventYear, int eventNo, String no) throws IOException {
		logger.info("스프드옥션 상세 정보 페이지 가져오기를 시도합니다");
		logger.info("/v2/info-result.htm?courtNo=" + court + "&courtNo2=" + 담당계 + "&eventNo1=" + eventYear
				+ "&eventNo2=" + eventNo + "&objNo=" + no);

		String res = fetch("/v2/info-result.htm?courtNo=" + court + "&courtNo2=" + 담당계 + "&eventNo1="
				+ eventYear + "&eventNo2=" + eventNo + "&objNo=" + no);
		System.out.println(res);
		logger.info("스프드옥션 상세 정보 페이지 가져오기에 성공했습니다.");

		return res;
	}

}