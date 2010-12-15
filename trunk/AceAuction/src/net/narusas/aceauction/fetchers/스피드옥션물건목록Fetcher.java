package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.model.���ǵ���ǹ���;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class ���ǵ���ǹ��Ǹ��Fetcher {

	static Logger logger = Logger.getLogger("log");

	static Pattern p = Pattern
			.compile("open_result_page\\('[^']+','(\\w+)','([^']+)','(\\d+)','(\\d+)','(\\d+)',(\\d+)\\)");

	private ���ǵ����Fetcher fetcher;

	private final String no;
	private final String year;
	public ���ǵ���ǹ��Ǹ��Fetcher(String year, String no) throws HttpException, IOException {
		super();
		this.year = year;
		this.no = no;
		fetcher = ���ǵ����Fetcher.getInstance();
	}

	public String getPage() throws HttpException, IOException {
		logger.info("������ �������⸦ �õ��մϴ�");

		PostMethod m = fetcher.post("/v2/info-list.htm", new NameValuePair[] {
				new NameValuePair("eventNo", year), new NameValuePair("eventNo2", no), });
		logger.info("������ �������⿡ �����߽��ϴ�.");

		return new String(m.getResponseBody(), "euc-kr");
	}

	public List<���ǵ���ǹ���> get����s() throws HttpException, IOException {
		return parse����s(getPage());
	}

	public List<���ǵ���ǹ���> parse����s(String src) {
		logger.info("���ǵ���� ���� �������� �м��� �����մϴ�.");

		HashSet<���ǵ���ǹ���> res = new HashSet<���ǵ���ǹ���>();
		Matcher m = p.matcher(src);
		while (m.find()) {
			res.add(new ���ǵ���ǹ���(m.group(1), m.group(2), m.group(3), m.group(4), m.group(5), m
					.group(6)));
		}
		logger.info("���ǵ���� ���� �������� �м��� �������ϴ�.");

		return new ArrayList<���ǵ���ǹ���>(res);
	}
}
