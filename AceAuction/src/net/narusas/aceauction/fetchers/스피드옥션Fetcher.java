package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class ½ºÇÇµå¿Á¼ÇFetcher extends PageFetcher {

	public static String id;

	public static String pass;

	private static ½ºÇÇµå¿Á¼ÇFetcher instance;

	static {
		try {
			String text = NFile.getText(new File("account.dat"));
			String[] dat = text.split(",");
			id = dat[0];
			pass = dat[1];
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ½ºÇÇµå¿Á¼ÇFetcher() throws HttpException, IOException {
		super("http://www.speedauction.co.kr");
	}

	public String getID() {
		return id;
	}

	public String getPassword() {
		return pass;
	}

	private void login() throws IOException, HttpException {
		GetMethod getM = get("/");
		// System.out.println(this.id + ":" + this.pass);
		PostMethod m = post("/cyosProc/login_form_proc.php",
				new NameValuePair[] { new NameValuePair("id", id),
						new NameValuePair("pw", pass),
						new NameValuePair("x", "30"),
						new NameValuePair("y", "13"),
						new NameValuePair("idsave", "on"),
						new NameValuePair("pwsave", "on"),

				});
		String res = m.getResponseBodyAsString();
		Pattern p = Pattern.compile("member_code=(\\d+)");
		Matcher matcher = p.matcher(res);
		matcher.find();

		getM = get("/cyosProc/session.php?member_code=" + matcher.group(1)
				+ "&job=login&return_url=");
	}

	@Override
	protected void prepare() throws HttpException, IOException {
		login();
	}

	public static ½ºÇÇµå¿Á¼ÇFetcher getInstance() {
		if (instance == null) {
			try {
				instance = new ½ºÇÇµå¿Á¼ÇFetcher();
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
}
