package net.narusas.aceauction.fetchers;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * ����� ��� ����Ʈ(http://www.courtauction.go.kr) ���� �������� ��� ���� ���� �⺻ Fetcher. �ֿ� �����
 * URL Prefix�� ������, Session �����̴�.
 * 
 * 
 * @author narusas
 * 
 */
public class �����Fetcher extends PageFetcher {

	private static �����Fetcher instance;

	final static String URL_PREFIX = "http://www.courtauction.go.kr";

	private �����Fetcher() throws HttpException, IOException {
		super(URL_PREFIX);
	}

	public boolean checkValidAccess(String res) {
		return res.indexOf("�߸��� ����") == -1;
	}

	/**
	 * ������ �����ϴ� ������ �����Ѵ�.
	 */
	protected void prepare() throws HttpException, IOException {
		GetMethod m = get("/au/Frame.jsp");
		m = get("/au/SuperServlet?target_command=au.command.MainBodyCommand&bub_cd=");
		m = get("/au/auc/C110Search.jsp");
		m = get("/au/SuperServlet?target_command=au.command.auc.C110ListCommand&browser=2&check_msg=&bub_cd=$code");
	}

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
