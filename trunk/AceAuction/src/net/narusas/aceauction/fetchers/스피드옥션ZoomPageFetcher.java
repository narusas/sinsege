package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.model.물건;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class 스피드옥션ZoomPageFetcher {

	static Logger logger = Logger.getLogger("log");

	static Pattern p1 = Pattern.compile("<input[^>]+name=(\\w+) value='([^']*)'>");

	static Pattern p2 = Pattern
			.compile("(http://www.speedauction.co.kr/courtfileroot/gamjungimg/[^\\s^'^\"]+)");

	static Pattern p3 = Pattern.compile("(http://[^/]+/[a-zA-Z]\\d+/[^\\s^'^\"]+[jJ][pP][gG])");

	static Pattern p4 = Pattern.compile("(http://[^/]+/[a-zA-Z]\\d+\\d+/[^\\s^'^\"]+[jJ][pP][gG])");

	static Pattern pdfPattern = Pattern.compile("src=(http[^>]+)");

	private Properties param = new Properties();
	private String slice;
	public void fetch등기부등본(물건 물건, String src) throws HttpException, IOException {
		init(src);
		물건.set건물등기부등본(parsePDF(get건물등기부등본page()));
		물건.set토지등기부등본(parsePDF(get토지등기부등본page()));
	}

	public String[] fetch사진(String src) throws IOException {
		init(src);
		String html = get사진page();
		String[] urls = parse사진링크(html);
		return urls;
	}

	public String[] fetch사진(물건 물건, String src) throws IOException {
		String[] urls = parse사진링크(src);
		for (String url : urls) {
			// System.out.println(url);
//			logger.info(url);
			물건.add사진(url);
		}
		return urls;
	}

	public String getParam(String key) {
		return param.getProperty(key);
	}

	public String get건물등기부등본page() throws HttpException, IOException {
		스피드옥션Fetcher fetcher = 스피드옥션Fetcher.getInstance();
		PostMethod m = fetcher.post("/v2/info-result-zoom2.htm", getParams());
		return new String(m.getResponseBody(), "euc-kr");
	}

	public String get사진page() throws IOException {
		스피드옥션Fetcher fetcher = 스피드옥션Fetcher.getInstance();
		PostMethod m = fetcher.post("/v2/info-result-zoom.htm", getParams());

		return new String(m.getResponseBody(), "euc-kr");
	}

	public String get집합건물등기부등본page() throws HttpException, IOException {
		스피드옥션Fetcher fetcher = 스피드옥션Fetcher.getInstance();
		PostMethod m = fetcher.post("/v2/info-result-zoom4.htm", getParams());
		return new String(m.getResponseBody(), "euc-kr");
	}

	public String get토지등기부등본page() throws HttpException, IOException {
		스피드옥션Fetcher fetcher = 스피드옥션Fetcher.getInstance();
		PostMethod m = fetcher.post("/v2/info-result-zoom3.htm", getParams());
		return new String(m.getResponseBody(), "euc-kr");
	}

	public void init(String src) {
		this.slice = src
				.substring(src.indexOf("<form name=info_list2"), src.lastIndexOf("</form>"));
		Matcher m = p1.matcher(slice);
		while (m.find()) {
			param.setProperty(m.group(1), m.group(2));
		}
	}

	public String[] parse사진링크(String src) {
		HashSet<String> res = new HashSet<String>();
		Matcher m = p2.matcher(src);
		while (m.find()) {
			if ("http://www.speedauction.co.kr/courtfileroot/gamjungimg/D01/2005/48942/1.gif_1167876192"
					.equals(m.group(1).trim())) {
				continue;
			}
			res.add(m.group(1));
		}
		m = p3.matcher(src);
		while (m.find()) {
			if ("http://222.237.76.182/A02/2006/3300168_A02_10464_1.JPG".equals(m.group(1).trim())) {
				continue;
			}
			res.add(m.group(1));
		}
		return res.toArray(new String[res.size()]);
	}

	private NameValuePair[] getParams() {
		ArrayList<NameValuePair> res = new ArrayList<NameValuePair>();
		Iterator keys = param.keySet().iterator();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			res.add(new NameValuePair(key, param.getProperty(key)));
		}
		return res.toArray(new NameValuePair[res.size()]);
	}

	private String parsePDF(String src) {
		System.out.println(src);
		Matcher m = pdfPattern.matcher(src);
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}
}
