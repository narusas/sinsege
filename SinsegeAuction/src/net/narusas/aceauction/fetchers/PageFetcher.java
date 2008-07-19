/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

// TODO: Auto-generated Javadoc
/**
 * 다른 Page Fetcher들을 위한 상위 클래스. 내부적으로 Apaceh HttpClient를 사용한다.
 * 
 * @author narusas
 */
public class PageFetcher {

	/** The url_prefix. */
	private final String url_prefix;

	/** The client. */
	public HttpClient client;

	/** The state. */
	protected HttpState state;

	/** The last fetched time. */
	long lastFetchedTime;

	private String appendCookie;

	/**
	 * Instantiates a new page fetcher.
	 * 
	 * @param url_prefix
	 *            the url_prefix
	 * 
	 * @throws HttpException
	 *             the http exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public PageFetcher(String url_prefix) throws HttpException, IOException {
		this.url_prefix = url_prefix;
		client = new HttpClient();
		state = new HttpState();

		prepare();
	}

	/**
	 * 지정된 URL의 HTML을 획득(Fetch)해온다.
	 * 
	 * @param path
	 *            목표가 되는 url의 path
	 * 
	 * @return 얻어온 HTML
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String fetch(String path) throws IOException {
		GetMethod method = get(path);
		byte[] buf = method.getResponseBody();
		method.releaseConnection();
		return new String(buf, "euc-kr");
	}

	/**
	 * 지정된 URL의 HTML을 획득(Fetch)해온다. 얻어올때 HTTP Request Header에 추가적인 값을 입력하여 요청을
	 * 수행한다.
	 * 
	 * @param path
	 *            목표가 되는 url의 path
	 * @param headers
	 *            추가할 HTTP Request Headers
	 * 
	 * @return 얻어온 HTML
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String fetch(String path, NameValuePair[] headers)
			throws IOException {
		GetMethod method = new GetMethod(path);
		addHeaders(headers, method);
		client.executeMethod(method);
		byte[] buf = method.getResponseBody();
		method.releaseConnection();
		return new String(buf, "euc-kr");
	}

	/**
	 * 바이너리 데이터를 Fetch하고자 할때 사용한다.
	 * 
	 * @param url
	 *            목표가 되는 url의 path
	 * @param headers
	 *            추가할 HTTP Request Headers
	 * 
	 * @return 바이너리 데이타.
	 * 
	 * @throws HttpException
	 *             the http exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public byte[] fetchBinary(String url, NameValuePair[] headers)
			throws HttpException, IOException {
		GetMethod method = new GetMethod(url);
		addHeaders(headers, method);

		client.executeMethod(method);
		byte[] buf = method.getResponseBody();
		method.releaseConnection();
		return buf;
	}

	/**
	 * Gets the.
	 * 
	 * @param path
	 *            the path
	 * 
	 * @return the gets the method
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws HttpException
	 *             the http exception
	 */
	public GetMethod get(String path) throws IOException, HttpException {
		update();
		GetMethod method = new GetMethod(getRealURL(path));
		setCookie(method);
		client.executeMethod(method);
		System.out.println(method.getRequestHeader("Cookie"));
		return method;
	}

	private void setCookie(GetMethod method) {
		if (appendCookie == null) {
			return;
		}

		Header cookie = method.getRequestHeader("Cookie");
		System.out.println(cookie);
		if (cookie == null){
			method.setRequestHeader("Cookie" , appendCookie);
		}
		
//		HttpClientParams params = client.getParams();
//		String cookie = (String) params.getParameter("Cookie");
//		if (cookie == null ){
//			cookie = appendCookie;
//			params.setParameter("Cookie", cookie);
//			return;
//		}
//		if (cookie.contains(appendCookie)) {
//			return;
//		}
//		cookie += "; " + appendCookie;
//		params.setParameter("Cookie", cookie);
	}

	public void setCookie(String appendCookie) {
		this.appendCookie = appendCookie;
	}

	/**
	 * Gets the url_prefix.
	 * 
	 * @return the url_prefix
	 */
	public String getUrl_prefix() {
		return url_prefix;
	}

	/**
	 * HTTP Post Request를 전송한다.
	 * 
	 * @param path
	 *            목표가 되는 url의 path
	 * @param data
	 *            보내고자하는 post data
	 * 
	 * @return 실행 결과를 들고있는 PostMethd(See Apaceh HttpClient)
	 * 
	 * @throws HttpException
	 *             the http exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public PostMethod post(String path, NameValuePair[] data)
			throws HttpException, IOException {
		update();
		PostMethod m = new PostMethod(getRealURL(path));
		m.setRequestBody(data);
		client.executeMethod(m);
		return m;
	}

	/**
	 * Adds the headers.
	 * 
	 * @param headers
	 *            the headers
	 * @param method
	 *            the method
	 */
	private void addHeaders(NameValuePair[] headers, GetMethod method) {
		if (headers != null) {
			for (NameValuePair pair : headers) {
				method.setRequestHeader(pair.getName(), pair.getValue());
			}
		}
	}

	/**
	 * Update.
	 * 
	 * @throws HttpException
	 *             the http exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void update() throws HttpException, IOException {
		if (lastFetchedTime + 1000 * 60 * 10 < System.currentTimeMillis()) {
			lastFetchedTime = System.currentTimeMillis();
			// prepare();
		}
	};

	/**
	 * URL을 기본적으로 url prefix와 path를 합쳐서 생성된다. 만약 이 알고리즘이 적절하지 않다면 하위 클래스에서
	 * 오버라이딩할것.
	 * 
	 * @param path
	 *            the path
	 * 
	 * @return 합성된 Real URL
	 */
	protected String getRealURL(String path) {
		return url_prefix + path;
	}

	/**
	 * Prepare.
	 * 
	 * @throws HttpException
	 *             the http exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected void prepare() throws HttpException, IOException {
	}

}
