/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.법원;

import org.apache.commons.httpclient.HttpException;

// TODO: Auto-generated Javadoc
/**
 * Updater UI에서 사용되는 사건 Fetcher이다.
 * 
 * @author narusas
 */
public class 대법원사건Fetcher_결과 {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The charge. */
	private 담당계 charge;

	/** The charge code. */
	private final int chargeCode;

	/** The charge name. */
	private final String chargeName;

	/** The court. */
	private final 법원 court;

	/** The date. */
	private final Date date;
	
	/** The fetcher. */
	private PageFetcher fetcher;

	/** The p. */
	Pattern p = Pattern.compile("loadSaDetail\\( \\'(\\d+)\\'",
			Pattern.MULTILINE);

	/**
	 * Instantiates a new 대법원사건 fetcher_결과.
	 * 
	 * @param courtCode the court code
	 * @param date the date
	 * @param chargeCode the charge code
	 * @param chargeName the charge name
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public 대법원사건Fetcher_결과(법원 courtCode, Date date, int chargeCode,
			String chargeName) throws HttpException, IOException {
		this.court = courtCode;
		this.date = date;
		this.chargeCode = chargeCode;
		this.chargeName = chargeName;
		fetcher = 대법원Fetcher.getInstance();
	}

	/**
	 * Instantiates a new 대법원사건 fetcher_결과.
	 * 
	 * @param court the court
	 * @param charge the charge
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public 대법원사건Fetcher_결과(법원 court, 담당계 charge) throws HttpException,
			IOException {
		this(court, charge.get매각기일(), charge.get담당계코드(), charge.get담당계이름());
		this.charge = charge;
	}

	/**
	 * Gets the page.
	 * 
	 * @param pageNo the page no
	 * 
	 * @return the page
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String getPage(int pageNo) throws IOException {
		logger.log(Level.FINEST,pageNo + "번째 페이지 가져오기를 시도합니다");
		pageNo = pageNo == 0 ? 1 : pageNo;
		try {
			// /au/SuperServlet?target_command=au.command.auc.C210ListCommand&page=2
			// &bub_cd=000210&giil=2007.08.07&jp_cd=1006&dam_nm=%B0%E6%B8%C56%B0%E8&browser=&check_msg=
			String query = "/au/SuperServlet?target_command=au.command.auc.C210ListCommand" // 
					+ "&page="
					+ pageNo // 
					+ "&bub_cd="
					+ court.getCode()// 
					+ "&giil="
					+ date.toString().replaceAll("-",".")// 
					+ "&jp_cd="
					+ chargeCode// 
					+ "&dam_nm="
					+ URLEncoder.encode(chargeName, "euc-kr")
					+ "&check_msg=";
			String res = fetcher.fetch(query);
			logger.log(Level.FINEST,pageNo + "번째 페이지를 가져오는데 성공했습니다");
			return res;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * Gets the pages.
	 * 
	 * @return the pages
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String[] getPages() throws IOException {
		logger.info("전체 페이지를 얻어올 준비중입니다.");
		int max = 1;
		boolean finding = true;

		while (finding) {
			logger.info(max + "가 제일 마지막 페이지인지를 분석중입니다.");
			String page = getPage(max);
			Pattern p = Pattern.compile("javascript:goMove\\((\\d+)\\)");
			Matcher m = p.matcher(page);
			finding = false;
			while (m.find()) {
				int v = Integer.parseInt(m.group(1));
				if (v > max) {
					max = v;
					finding = true;
				}
			}
		}
		logger.info(max + "가 제일 마지막 페이지입니다.");

		String[] res = new String[max];
		for (int i = 1; i <= max; i++) {
			res[i - 1] = getPage(i);
		}

		return res;
	}

	/**
	 * Parses the.
	 * 
	 * @param page the page
	 * 
	 * @return the list< string>
	 */
	public List<String> parse(String page) {
		Matcher m = p.matcher(page);
		LinkedList<String> res = new LinkedList<String>();
		while (m.find()) {
			res.add(m.group(1));
		}
		return res;
	}

}
