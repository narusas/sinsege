/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

// TODO: Auto-generated Javadoc
/**
 * 대법원 경매 사이트(http://www.courtauction.go.kr) 에서 페이지을 얻어 오기 위한 기본 Fetcher. 주요 기능은
 * URL Prefix의 제공과, Session 수립이다.
 * 
 * @author narusas
 */
public class 대법원Fetcher extends PageFetcher {

	/** The instance. */
	private static 대법원Fetcher instance;

	/** The Constant URL_PREFIX. */
	final static String URL_PREFIX = "http://www.courtauction.go.kr";

	/**
	 * Instantiates a new 대법원 fetcher.
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private 대법원Fetcher() throws HttpException, IOException {
		super(URL_PREFIX);
	}

	/**
	 * Check valid access.
	 * 
	 * @param res the res
	 * 
	 * @return true, if successful
	 */
	public boolean checkValidAccess(String res) {
		return res.indexOf("잘못된 접근") == -1;
	}

	/**
	 * 세션을 수립하는 과정울 수행한다.
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void prepare() throws HttpException, IOException {
		GetMethod m = get("/au/Frame.jsp");
		m = get("/au/SuperServlet?target_command=au.command.MainBodyCommand&bub_cd=");
		m = get("/au/auc/C110Search.jsp");
		m = get("/au/SuperServlet?target_command=au.command.auc.C110ListCommand&browser=2&check_msg=&bub_cd=$code");
	}

	/**
	 * Gets the single instance of 대법원Fetcher.
	 * 
	 * @return single instance of 대법원Fetcher
	 */
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
