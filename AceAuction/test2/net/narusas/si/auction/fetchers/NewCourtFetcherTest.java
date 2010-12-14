package net.narusas.si.auction.fetchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.htmlparser.Parser;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class NewCourtFetcherTest extends TestCase {
	public void testInitSession() throws HttpException, IOException {
		HttpClient client = new HttpClient();
		HttpState state = new HttpState();
		GetMethod get = new GetMethod("http://www.courtauction.go.kr/index.html");
		client.executeMethod(get);
		get = new GetMethod("http://www.courtauction.go.kr/index.jsp");
		client.executeMethod(get);
		String html = get.getResponseBodyAsString();
		PostMethod post = new PostMethod("http://www.courtauction.go.kr/RetrieveMainInfo.laf");
		post.setRequestBody(new NameValuePair[] {
				new NameValuePair("_NEXT_SRNID", findPropertyValue("_NEXT_SRNID", html)),
				new NameValuePair("_NEXT_CMD", findPropertyValue("_NEXT_CMD", html)),

		});
		client.executeMethod(post);
		html = post.getResponseBodyAsString();
		System.out.println(html);
//		System.out.println(findLink("alt=\"기일별검색\"", html));
		// System.out.println(findFrom(
		// "alt=\"기일별검색\"","porActSubmit\\(\"\",\"InitMulSrch.laf\",\"\",\"([^\"]+)\"\\)",
		// html));

		// post = new
		// PostMethod("http://www.courtauction.go.kr/InitMulSrch.laf");
		// post.setRequestBody(new NameValuePair[] {
		// new NameValuePair("_NEXT_SRNID",
		// findLink(
		// "alt=\"기일별검색\"","porActSubmit\\(\"\",\"InitMulSrch.laf\",\"\",\"([^\"]+)\"\\)",
		// html)),
		// new NameValuePair("_NEXT_CMD", "InitMulSrch.laf"),
		//
		// });
		// client.executeMethod(post);
		// html = post.getResponseBodyAsString();
		// System.out.println(html);
		// 기일별 검색 페이지.
		// http://www.courtauction.go.kr/InitMulSrch.laf?&_NEXT_CMD=InitMulSrch.laf&_NEXT_SRNID=PNO10200

		// System.out.println(post.getResponseBodyAsString());

	}

	private LinkTag findLink(String subject, String src) {
		String splited = src.substring(0, src.indexOf(subject));
		splited = splited.substring(splited.lastIndexOf("<a"));
		// System.out.println(splited);
		Parser parser = new Parser();
		try {
			parser.setInputHTML(splited);
			NodeList list = parser.parse(null);
			return (LinkTag) list.elementAt(0);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
	}

	private String findFrom(String from, String regx, String src) {
		String splited = src.substring(src.indexOf(from));

		return find(regx, splited);
	}

	List<String> find(String regx, String src, int spotCount) {
		List<String> res = new ArrayList<String>();
		Matcher m = java.util.regex.Pattern.compile(regx).matcher(src);

		if (m.find() == false) {
			return res;
		}

		for (int i = 0; i < spotCount; i++) {
			res.add(m.group(i + 1));
		}
		return res;
	}

	String find(String regx, String src) {
		List<String> res = find(regx, src, 1);
		if (res.size() == 0) {
			return null;
		}
		return res.get(0);
	}

	String findPropertyValue(String key, String src) {
		return find(key + "\"\\s+value=\"([^\"]+)", src);
	}

}
