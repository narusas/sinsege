/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.법원;

import org.apache.commons.httpclient.HttpException;

// TODO: Auto-generated Javadoc
/**
 * 대법원 사이트에서 지정된 법원의 담당계 목록을 읽어 온다.
 * 
 * @author narusas
 */
public class 담당계Fetcher {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The p2. */
	static Pattern p2 = Pattern.compile(">\\s*(\\d\\d\\d\\d.\\d+.\\d+\\s*~\\s*\\d\\d\\d\\d.\\d+.\\d+)");

	/** The pattern1. */
	static Pattern pattern1 = Pattern.compile("'(\\d\\d\\d\\d.\\d+.\\d+)', '(\\d+)', '((경매|집행)\\s*\\d+-?\\d?계[^']*)'",
			Pattern.DOTALL);
	
	/** The court code. */
	protected final 법원 courtCode;

	/** The fetcher. */
	protected PageFetcher fetcher;

	/** The function name. */
	protected String functionName;

	/** The query path. */
	protected String queryPath;

	/**
	 * Instantiates a new 담당계 fetcher.
	 * 
	 * @param courtCode the court code
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private 담당계Fetcher(법원 courtCode) throws HttpException, IOException {
		this.courtCode = courtCode;
		fetcher = 대법원Fetcher.getInstance();
		functionName = "loadMaemul";
		queryPath = "/au/SuperServlet?target_command=au.command.auc.C110ListCommand&browser=2&check_msg=&bub_cd=";
	}

	/**
	 * Fetch charges.
	 * 
	 * @return the list<담당계>
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<담당계> fetchCharges() throws HttpException, IOException {
		logger.info("담당계 가져오기를 준비중입니다");
		String page = fetchPage();
		logger.info("가져온 담당계를 분석중입니다. ");

		Matcher m = pattern1.matcher(page);
		LinkedList<담당계> list = new LinkedList<담당계>();
		while (m.find()) {
			logger.info("가져온 담당계를 분석중입니다. ");
			String 매각기일 = m.group(1);
			String 담당계코드 = m.group(2);
			String 담당계이름 = m.group(3);

			String t = "javascript:" + functionName + "( '" + 매각기일 + "', '" + 담당계코드 + "', '" + 담당계이름 + "' );";
			int pos = page.indexOf(t);

			String temp = page.substring(0, pos);
			pos = temp.lastIndexOf("<td");
			int startPos = temp.lastIndexOf("<tr");
			temp = temp.substring(startPos, pos);

			Matcher m2 = p2.matcher(temp);
			String 입찰기간 = null;
			if (m2.find()) {
				입찰기간 = m2.group(1);
			}
			담당계 charge = new 담당계(courtCode, 매각기일, 입찰기간, 담당계코드, 담당계이름);
			list.add(charge);
			logger.info("담당계:" + charge.toString());
		}
		logger.info("담당계분석을 완료했습니다.");
		return list;
	}

	/**
	 * Fetch page.
	 * 
	 * @return the string
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected String fetchPage() throws IOException {
		String page = fetcher.fetch(queryPath + courtCode.getCode());
		return page;
	}

	/**
	 * Gets the 담당계 fetcher_경매진행.
	 * 
	 * @param courtCode the court code
	 * 
	 * @return the 담당계 fetcher_경매진행
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static 담당계Fetcher get담당계Fetcher_경매진행(법원 courtCode) throws HttpException, IOException {
		return new 담당계Fetcher(courtCode);
	}

	/**
	 * Gets the 담당계 fetcher_매각결과.
	 * 
	 * @param courtCode the court code
	 * 
	 * @return the 담당계 fetcher_매각결과
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static 담당계Fetcher get담당계Fetcher_매각결과(법원 courtCode) throws HttpException, IOException {
		담당계Fetcher f = new 담당계Fetcher(courtCode);
		f.functionName = "loadMaeGyul";
		f.queryPath = "/au/SuperServlet?target_command=au.command.auc.C210CalendarCommand&browser=2&check_msg=&bub_cd=";
		return f;
	}
}
