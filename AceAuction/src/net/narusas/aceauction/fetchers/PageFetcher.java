package net.narusas.aceauction.fetchers;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * �ٸ� Page Fetcher���� ���� ���� Ŭ����. ���������� Apaceh HttpClient�� ����Ѵ�.
 * 
 * @author narusas
 * 
 */
public class PageFetcher {

	private final String url_prefix;

	protected HttpClient client;

	protected HttpState state;

	long lastFetchedTime;

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
	 * @return ���� HTML
	 * @throws IOException
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
	 * @return ���� HTML
	 * @throws IOException
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
	 * @return ���̳ʸ� ����Ÿ.
	 * @throws HttpException
	 * @throws IOException
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

	public GetMethod get(String path) throws IOException, HttpException {
		update();
		GetMethod method = new GetMethod(getRealURL(path));
		client.executeMethod(method);
		return method;
	}

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
	 * @return ���� ����� ����ִ� PostMethd(See Apaceh HttpClient)
	 * @throws HttpException
	 * @throws IOException
	 */
	public PostMethod post(String path, NameValuePair[] data)
			throws HttpException, IOException {
		update();
		PostMethod m = new PostMethod(getRealURL(path));
		m.setRequestBody(data);
		client.executeMethod(m);
		return m;
	}

	private void addHeaders(NameValuePair[] headers, GetMethod method) {
		if (headers != null) {
			for (NameValuePair pair : headers) {
				method.setRequestHeader(pair.getName(), pair.getValue());
			}
		}
	}

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
	 * @return �ռ��� Real URL
	 */
	protected String getRealURL(String path) {
		return url_prefix + path;
	}

	protected void prepare() throws HttpException, IOException {
	}

}
