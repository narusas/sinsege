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
 * �ٸ� Page Fetcher���� ���� ���� Ŭ����. ���������� Apaceh HttpClient�� ����Ѵ�.
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
	 * ������ URL�� HTML�� ȹ��(Fetch)�ؿ´�.
	 * 
	 * @param path
	 *            ��ǥ�� �Ǵ� url�� path
	 * 
	 * @return ���� HTML
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
	 * ������ URL�� HTML�� ȹ��(Fetch)�ؿ´�. ���ö� HTTP Request Header�� �߰����� ���� �Է��Ͽ� ��û��
	 * �����Ѵ�.
	 * 
	 * @param path
	 *            ��ǥ�� �Ǵ� url�� path
	 * @param headers
	 *            �߰��� HTTP Request Headers
	 * 
	 * @return ���� HTML
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
	 * ���̳ʸ� �����͸� Fetch�ϰ��� �Ҷ� ����Ѵ�.
	 * 
	 * @param url
	 *            ��ǥ�� �Ǵ� url�� path
	 * @param headers
	 *            �߰��� HTTP Request Headers
	 * 
	 * @return ���̳ʸ� ����Ÿ.
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
	 * HTTP Post Request�� �����Ѵ�.
	 * 
	 * @param path
	 *            ��ǥ�� �Ǵ� url�� path
	 * @param data
	 *            ���������ϴ� post data
	 * 
	 * @return ���� ����� ����ִ� PostMethd(See Apaceh HttpClient)
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
	 * URL�� �⺻������ url prefix�� path�� ���ļ� �����ȴ�. ���� �� �˰����� �������� �ʴٸ� ���� Ŭ��������
	 * �������̵��Ұ�.
	 * 
	 * @param path
	 *            the path
	 * 
	 * @return �ռ��� Real URL
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
