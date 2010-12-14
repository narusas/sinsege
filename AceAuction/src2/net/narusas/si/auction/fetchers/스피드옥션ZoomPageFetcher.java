package net.narusas.si.auction.fetchers;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;

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

	public static class 등기부등본Links {
		String 건물등기부등본PDFLink;
		String 토지등기부등본PDFLink;

		public 등기부등본Links(String link, String link2) {
			건물등기부등본PDFLink = link;
			토지등기부등본PDFLink = link2;
		}

		public String get건물등기부등본PDFLink() {
			return 건물등기부등본PDFLink;
		}

		public void set건물등기부등본PDFLink(String link) {
			건물등기부등본PDFLink = link;
		}

		public String get토지등기부등본PDFLink() {
			return 토지등기부등본PDFLink;
		}

		public void set토지등기부등본PDFLink(String link) {
			토지등기부등본PDFLink = link;
		}

	}

	public 등기부등본Links fetch(물건 물건) throws HttpException, IOException {
		스피드옥션Fetcher f = 스피드옥션Fetcher.getInstance();
		법원 법원 = 물건.get법원();
		사건 사건 = 물건.get사건();
		String query = MessageFormat.format(
				"/v2/info_result_switch.php?courtNo={0}A01&eventNo1={1}&eventNo2={2}&objNo={3}",//
				법원.get스피드옥션_법원코드(), 사건.getEventYear(), 사건.getEventNo(), 물건.get물건번호()//
				);
		System.out.println(query);
		f.get(query);
		String courtCode = 법원.get스피드옥션_법원코드();
		String event1 = String.valueOf(사건.getEventYear());
		String event2 = String.valueOf(사건.getEventNo());
		String objNo = String.valueOf(물건.get물건번호());
		System.out.println("courtCode:"+courtCode);
		System.out.println("event1:"+event1);
		System.out.println("event2:"+event2);
		System.out.println("objNo:"+objNo);
		
		
		PostMethod m = f.post("/v2/info-result.htm", new NameValuePair[] {
				new NameValuePair("courtNo", 법원.get스피드옥션_법원코드()), //
				new NameValuePair("eventNo1", event1),//
				new NameValuePair("eventNo2", event2),//
				new NameValuePair("objNo", objNo),//
		}, new String[][] { {
				"Referer",
				"http://www.speedauction.co.kr/v2/info_result_switch.php?courtNo=" + courtCode + "&eventNo1="
						+ event1 + "&eventNo2=" + event2 + "&objNo=" + objNo } });

		String html = new String(m.getResponseBody(), "euc-kr");
//		System.out.println(html);
		return parse등기부등본Link(html);
	}

	public 등기부등본Links parse등기부등본Link(String src) throws HttpException, IOException {
		init(src);
		String 건물등기부등본PDFLink = parsePDFLink(get건물등기부등본page());
		String 토지등기부등본PDFLink = parsePDFLink(get토지등기부등본page());

		return new 등기부등본Links(건물등기부등본PDFLink, 토지등기부등본PDFLink);
	}

	public String[] fetch사진(String src) throws IOException {
		init(src);
		String html = get사진page();
		String[] urls = parse사진링크(html);
		return urls;
	}

	// public String[] fetch사진(물건 물건, String src) throws IOException {
	// String[] urls = parse사진링크(src);
	// for (String url : urls) {
	// // System.out.println(url);
	// // logger.info(url);
	// 물건.add사진(url);
	// }
	// return urls;
	// }

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
		this.slice = src.substring(src.indexOf("<form name=info_list2"), src.lastIndexOf("</form>"));
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

	private String parsePDFLink(String src) {
		Matcher m = pdfPattern.matcher(src);
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}
}