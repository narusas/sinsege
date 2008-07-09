/*
 * 
 */
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

// TODO: Auto-generated Javadoc
/**
 * The Class ���ǵ����Fetcher.
 */
public class ���ǵ����Fetcher extends PageFetcher {

	/** The id. */
	public static String id;

	/** The pass. */
	public static String pass;

	/** The instance. */
	private static ���ǵ����Fetcher instance;

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

	/**
	 * Instantiates a new ���ǵ���� fetcher.
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ���ǵ����Fetcher() throws HttpException, IOException {
		super("http://www.speedauction.co.kr");
	}

	/**
	 * Gets the iD.
	 * 
	 * @return the iD
	 */
	public String getID() {
		return id;
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return pass;
	}

	/**
	 * Login.
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws HttpException the http exception
	 */
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

	/* (non-Javadoc)
	 * @see net.narusas.aceauction.fetchers.PageFetcher#prepare()
	 */
	@Override
	protected void prepare() throws HttpException, IOException {
		login();
	}

	/**
	 * Gets the single instance of ���ǵ����Fetcher.
	 * 
	 * @return single instance of ���ǵ����Fetcher
	 */
	public static ���ǵ����Fetcher getInstance() {
		if (instance == null) {
			try {
				instance = new ���ǵ����Fetcher();
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
}
