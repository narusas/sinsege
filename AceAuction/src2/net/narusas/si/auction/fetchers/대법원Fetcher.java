/*
 * 
 */
package net.narusas.si.auction.fetchers;

import java.io.IOException;

import net.narusas.util.lang.NInputStream;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * 
 * @author narusas
 */
public class 대법원Fetcher extends PageFetcher {

	/** The instance. */
	private static 대법원Fetcher instance;

	/** The Constant URL_PREFIX. */
	final static String URL_PREFIX = "http://www.courtauction.go.kr";

	private boolean isPrepared;

	public 대법원Fetcher() throws HttpException, IOException {
		super(URL_PREFIX);
	}

	public boolean checkValidAccess(String res) {
		return res.indexOf("�߸�� b��") == -1;
	}

	public void prepare() throws HttpException, IOException {
		if (isPrepared) {
			return;
		}
		GetMethod m = get("/index.jsp");
		m = get("/RetrieveMainInfo.laf");
		// String html = m.getResponseBodyAsString();
		String html = new String(NInputStream.readBytes(m.getResponseBodyAsStream()), "euc-kr");
		PostMethod post = new PostMethod("http://www.courtauction.go.kr/RetrieveMainInfo.laf");
		post.setRequestBody(new NameValuePair[] {
				new NameValuePair("_NEXT_SRNID", HTMLUtils.findPropertyValue("_NEXT_SRNID", html)),
				new NameValuePair("_NEXT_CMD", HTMLUtils.findPropertyValue("_NEXT_CMD", html)),

		});
		client.executeMethod(post);
		isPrepared = true;
	}

	public static 대법원Fetcher getInstance() {
		if (instance == null) {
			try {
				instance = new 대법원Fetcher();
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
}
