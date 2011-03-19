/*
 * 
 */
package net.narusas.si.auction.fetchers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.narusas.util.lang.NInputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageFetcher {
	final Logger logger = LoggerFactory.getLogger("auction");
	/** The url_prefix. */
	private final String url_prefix;

	/** The client. */
	protected HttpClient client;

	/** The state. */
	protected HttpState state;

	/** The last fetched time. */
	long lastFetchedTime;
	String encoding = "euc-kr";

	public PageFetcher(String url_prefix) throws HttpException, IOException {
		this.url_prefix = url_prefix;
		client = new HttpClient();
		state = new HttpState();
		client.setState(state);
		prepare();
	}

	public HttpState getState() {
		return state;
	}

	public String fetch(String path) throws IOException {
		for (int i = 0; i < 3; i++) {
			GetMethod method = get(path);

			byte[] buf = NInputStream.readBytes(method.getResponseBodyAsStream());
			method.releaseConnection();
			String temp = new String(buf, getEncoding());
			if (temp != null && "".equals(temp.trim()) == false) {
				return temp;
			}
		}
		throw new java.net.ConnectException("Server response invalid response");
	}

	private String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String fetch(String path, NameValuePair[] headers) throws IOException {
		// GetMethod method = get(path);
		GetMethod method = new GetMethod(getRealURL(path));
		addHeaders(headers, method);
		logger.info("BEFORE");
		
		client.executeMethod(method);
		logger.info("AFTER");
		
		long length = method.getResponseContentLength();
		logger.info("LENGTH:"+length);
		if (length >0){
			byte[] buf = new byte[(int) length];
			method.getResponseBodyAsStream().read(buf,0,(int)length);
			logger.info("COMPLETE");
			
			return new String(buf, getEncoding());
		}
		
		
		byte[] buf = NInputStream.readBytes(method.getResponseBodyAsStream());
		method.releaseConnection();
		return new String(buf, getEncoding());
	}

	public byte[] fetchBinary(String url, NameValuePair[] headers) throws HttpException, IOException {
		GetMethod method = get(url);
		addHeaders(headers, method);

		client.executeMethod(method);
		byte[] buf = NInputStream.readBytes(method.getResponseBodyAsStream());
		method.releaseConnection();
		return buf;
	}

	public byte[] fetchBinary(String url) throws HttpException, IOException {
		GetMethod method = get(url);
		client.executeMethod(method);
		byte[] buf = NInputStream.readBytes(method.getResponseBodyAsStream());
		method.releaseConnection();
		return buf;
	}

	public void downloadBinary(String url, File f) throws HttpException, IOException {
		logger.debug("Download Binary: url:"+url+" toFile:"+f.getAbsolutePath());
		GetMethod method = get(url);
		client.executeMethod(method);
		logger.debug("HTTPClient executed");
		BufferedInputStream bin = new BufferedInputStream(method.getResponseBodyAsStream());
		BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(f));
		byte[] buf = new byte[4096];
		while (true) {
			int r = bin.read(buf, 0, 4096);
			if (r == -1) {
				break;
			}
			bout.write(buf, 0, r);
		}
		bin.close();
		bout.close();
		method.releaseConnection();
		logger.debug("HTTPClient read end");
		// NOutputStream.leftShift(bout, bin);
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
		client.executeMethod(method);
		return method;
	}

	/**
	 * Gets the url_prefix.
	 * 
	 * @return the url_prefix
	 */
	public String getUrl_prefix() {
		return url_prefix;
	}

	public PostMethod post(String path, NameValuePair[] data) throws HttpException, IOException {
		return post(path, data, null);
	}

	public PostMethod post(String path, NameValuePair[] params, String[][] headers) throws HttpException, IOException {
		update();
		PostMethod m = new PostMethod(getRealURL(path));
		m.setRequestBody(params);
		if (headers != null) {
			for (String[] pair : headers) {
				m.setRequestHeader(pair[0], pair[1]);
			}
		}
		client.executeMethod(m);
		return m;
	}

	public PostMethod postAbsoulte(String url, NameValuePair[] data) throws HttpException, IOException {
		update();
		PostMethod m = new PostMethod(url);
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
	 * URL; �⺻��8�� url prefix�� path�� ���ļ� ��ȴ�. ���� �� �˰?���� ��������
	 * �ʴٸ� ��' Ŭ�������� �9���̵��Ұ�.
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
