package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;

import org.apache.commons.httpclient.HttpException;

/**
 * ����� ����Ʈ���� ������ ������ ���� ����� �о� �´�.
 * 
 * @author narusas
 * 
 */
public class ����Fetcher {

	static Logger logger = Logger.getLogger("log");

	static Pattern p2 = Pattern.compile(">\\s*(\\d\\d\\d\\d.\\d+.\\d+\\s*~\\s*\\d\\d\\d\\d.\\d+.\\d+)");

	static Pattern pattern1 = Pattern.compile("'(\\d\\d\\d\\d.\\d+.\\d+)', '(\\d+)', '((���|����)\\s*\\d+-?\\d?��[^']*)'",
			Pattern.DOTALL);
	protected final ���� courtCode;

	protected PageFetcher fetcher;

	protected String functionName;

	protected String queryPath;

	private ����Fetcher(���� courtCode) throws HttpException, IOException {
		this.courtCode = courtCode;
		fetcher = �����Fetcher.getInstance();
		functionName = "loadMaemul";
		queryPath = "/au/SuperServlet?target_command=au.command.auc.C110ListCommand&browser=2&check_msg=&bub_cd=";
	}

	public List<����> fetchCharges() throws HttpException, IOException {
		logger.info("���� �������⸦ �غ����Դϴ�");
		String page = fetchPage();
		logger.info("������ ���踦 �м����Դϴ�. ");

		Matcher m = pattern1.matcher(page);
		LinkedList<����> list = new LinkedList<����>();
		while (m.find()) {
			logger.info("������ ���踦 �м����Դϴ�. ");
			String �Ű����� = m.group(1);
			String �����ڵ� = m.group(2);
			String �����̸� = m.group(3);

			String t = "javascript:" + functionName + "( '" + �Ű����� + "', '" + �����ڵ� + "', '" + �����̸� + "' );";
			int pos = page.indexOf(t);

			String temp = page.substring(0, pos);
			pos = temp.lastIndexOf("<td");
			int startPos = temp.lastIndexOf("<tr");
			temp = temp.substring(startPos, pos);

			Matcher m2 = p2.matcher(temp);
			String �����Ⱓ = null;
			if (m2.find()) {
				�����Ⱓ = m2.group(1);
			}
			���� charge = new ����(courtCode, �Ű�����, �����Ⱓ, �����ڵ�, �����̸�);
			list.add(charge);
			logger.info("����:" + charge.toString());
		}
		logger.info("����м��� �Ϸ��߽��ϴ�.");
		return list;
	}

	protected String fetchPage() throws IOException {
		String page = fetcher.fetch(queryPath + courtCode.getCode());
		return page;
	}

	public static ����Fetcher get����Fetcher_�������(���� courtCode) throws HttpException, IOException {
		return new ����Fetcher(courtCode);
	}

	public static ����Fetcher get����Fetcher_�Ű����(���� courtCode) throws HttpException, IOException {
		����Fetcher f = new ����Fetcher(courtCode);
		f.functionName = "loadMaeGyul";
		f.queryPath = "/au/SuperServlet?target_command=au.command.auc.C210CalendarCommand&browser=2&check_msg=&bub_cd=";
		return f;
	}
}
