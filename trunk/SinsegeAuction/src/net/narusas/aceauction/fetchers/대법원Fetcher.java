/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

// TODO: Auto-generated Javadoc
/**
 * ����� ��� ����Ʈ(http://www.courtauction.go.kr) ���� �������� ��� ���� ���� �⺻ Fetcher. �ֿ� �����
 * URL Prefix�� ������, Session �����̴�.
 * 
 * @author narusas
 */
public class �����Fetcher extends PageFetcher {

	/** The instance. */
	private static �����Fetcher instance;

	/** The Constant URL_PREFIX. */
	final static String URL_PREFIX = "http://www.courtauction.go.kr";

	/**
	 * Instantiates a new ����� fetcher.
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private �����Fetcher() throws HttpException, IOException {
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
		return res.indexOf("�߸��� ����") == -1;
	}

	/**
	 * ������ �����ϴ� ������ �����Ѵ�.
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
	 * Gets the single instance of �����Fetcher.
	 * 
	 * @return single instance of �����Fetcher
	 */
	public static �����Fetcher getInstance() {
		if (instance == null) {
			try {
				instance = new �����Fetcher();
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
}
