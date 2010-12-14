package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;

import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class 스피드옥션Fetcher extends PageFetcher {

	public static String id;

	public static String pass;

	private static 스피드옥션Fetcher instance;

	static {
		try {
			String text = NFile.getText(new File("cfg/account.dat"));
			String[] dat = text.split(",");
			id = dat[0];
			pass = dat[1];
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public 스피드옥션Fetcher() throws HttpException, IOException {
		super("http://www.speedauction.co.kr");
	}

	public String getID() {
		return id;
	}

	public String getPassword() {
		return pass;
	}

	public void login() throws IOException, HttpException {
		client.executeMethod(new GetMethod("http://www.speedauction.co.kr"));
		client.executeMethod(new GetMethod("http://www.speedauction.co.kr/v2"));
		client.executeMethod(new GetMethod("http://www.speedauction.co.kr/v2/index.htm"));
		PostMethod m = new PostMethod("https://www.speedauction.co.kr/cyosProc/login_form_proc.php");
		
		NameValuePair[] params = new NameValuePair[] { new NameValuePair("id", id), //
				new NameValuePair("pw", pass), //
				new NameValuePair("idsave", "on"),//
				new NameValuePair("pwsave", "on"), };

		m.setRequestBody(params);
		client.executeMethod(m);
		GetMethod m2 = new GetMethod("http://www.speedauction.co.kr/v2");
		client.executeMethod(m2);

	
	}

	@Override
	protected void prepare() throws HttpException, IOException {
		login();
	}

	public static 스피드옥션Fetcher getInstance() {
		if (instance == null) {
			try {
				instance = new 스피드옥션Fetcher();
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	

}
