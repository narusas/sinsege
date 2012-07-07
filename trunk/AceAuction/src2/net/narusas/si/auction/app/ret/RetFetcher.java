package net.narusas.si.auction.app.ret;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.fetchers.PageFetcher;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class RetFetcher extends PageFetcher {

	private String sessionId;
	private String scriptId;

	public RetFetcher() throws HttpException, IOException {
		super("http://www.ret.co.kr");
	}

	@Override
	public void prepare() throws HttpException, IOException {
		String html = fetch("/");
		html = fetch("/ret/price/price01.jsp");

		Cookie[] cookies = getState().getCookies();

		for (Cookie cookie : cookies) {
//			System.out.println(cookie.getName() + ":" + cookie.getValue());
			if ("JSESSIONID".equals(cookie.getName())) {
				sessionId = cookie.getValue();
				break;
			}
		}

		PostMethod res = post("/ret/dwr/call/plaincall/__System.pageLoaded.dwr", new NameValuePair[] {
				new NameValuePair("c0-scriptName", "__System"), //
				new NameValuePair("c0-methodName", "pageLoaded"),
				new NameValuePair("c0-id", "0"), //

				new NameValuePair("callCount", "1"), //
				new NameValuePair("windowName", ""),//

				new NameValuePair("batchId", "0"), //
				new NameValuePair("page", "%2Fret%2Fprice%2Fprice01.jsp"),
				new NameValuePair("httpSessionId", sessionId), //
				new NameValuePair("scriptSessionId", ""),

		});
		String str = res.getResponseBodyAsString();
		Pattern p = Pattern.compile("handleNewScriptSession\\(\"([^\"]+)");
		Matcher m = p.matcher(str);
		m.find();
		scriptId = m.group(1);
//		System.out.println(scriptId);
	}

	public String fetch(String path, List<NameValuePair> pairs) throws HttpException, IOException {
		ArrayList<NameValuePair> temp = new ArrayList<NameValuePair>();
		temp.addAll(pairs);
		temp.add(new NameValuePair("callCount", "1"));
		temp.add(new NameValuePair("windowName", ""));
		temp.add(new NameValuePair("batchId", "0"));
		temp.add(new NameValuePair("c0-id", "0"));
		temp.add(new NameValuePair("page", "%2Fret%2Fprice%2Fprice01.jsp"));
		temp.add(new NameValuePair("httpSessionId", sessionId));
		temp.add(new NameValuePair("scriptSessionId", scriptId));

		PostMethod res = post(path, (NameValuePair[]) temp.toArray(new NameValuePair[temp.size()]));
		String html = res.getResponseBodyAsString();
		return convertUnicode(html);
	}

	Pattern p = Pattern.compile("\\\\u(....)");

	private String convertUnicode(String html) {
		Matcher m = p.matcher(html);
		while (m.find()) {
			char ch = (char) (Integer.parseInt(m.group(1), 16));
			html = html.replace("\\u" + m.group(1), "" + ch);
		}

		return html;
	}

}
