package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.model.스피드옥션물건;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class 스피드옥션물건목록Fetcher {

	static Logger logger = Logger.getLogger("log");

	static Pattern p = Pattern
			.compile("open_result_page\\('[^']+','(\\w+)','([^']+)','(\\d+)','(\\d+)','(\\d+)',(\\d+)\\)");

	private 스피드옥션Fetcher fetcher;

	private final String no;
	private final String year;
	public 스피드옥션물건목록Fetcher(String year, String no) throws HttpException, IOException {
		super();
		this.year = year;
		this.no = no;
		fetcher = 스피드옥션Fetcher.getInstance();
	}

	public String getPage() throws HttpException, IOException {
		logger.info("페이지 가져오기를 시도합니다");

		PostMethod m = fetcher.post("/v2/info-list.htm", new NameValuePair[] {
				new NameValuePair("eventNo", year), new NameValuePair("eventNo2", no), });
		logger.info("페이지 가져오기에 성공했습니다.");

		return new String(m.getResponseBody(), "euc-kr");
	}

	public List<스피드옥션물건> get물건s() throws HttpException, IOException {
		return parse물건s(getPage());
	}

	public List<스피드옥션물건> parse물건s(String src) {
		logger.info("스피드옥션 물건 페이지의 분석을 시작합니다.");

		HashSet<스피드옥션물건> res = new HashSet<스피드옥션물건>();
		Matcher m = p.matcher(src);
		while (m.find()) {
			res.add(new 스피드옥션물건(m.group(1), m.group(2), m.group(3), m.group(4), m.group(5), m
					.group(6)));
		}
		logger.info("스피드옥션 물건 페이지의 분석이 끝났습니다.");

		return new ArrayList<스피드옥션물건>(res);
	}
}
