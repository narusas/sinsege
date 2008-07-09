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

import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;

import org.apache.commons.httpclient.HttpException;

// TODO: Auto-generated Javadoc
/**
 * Updater UI���� ���Ǵ� ��� Fetcher�̴�.
 * 
 * @author narusas
 */
public class ��������Fetcher_��� {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The charge. */
	private ���� charge;

	/** The charge code. */
	private final int chargeCode;

	/** The charge name. */
	private final String chargeName;

	/** The court. */
	private final ���� court;

	/** The date. */
	private final Date date;
	
	/** The fetcher. */
	private PageFetcher fetcher;

	/** The p. */
	Pattern p = Pattern.compile("loadSaDetail\\( \\'(\\d+)\\'",
			Pattern.MULTILINE);

	/**
	 * Instantiates a new �������� fetcher_���.
	 * 
	 * @param courtCode the court code
	 * @param date the date
	 * @param chargeCode the charge code
	 * @param chargeName the charge name
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ��������Fetcher_���(���� courtCode, Date date, int chargeCode,
			String chargeName) throws HttpException, IOException {
		this.court = courtCode;
		this.date = date;
		this.chargeCode = chargeCode;
		this.chargeName = chargeName;
		fetcher = �����Fetcher.getInstance();
	}

	/**
	 * Instantiates a new �������� fetcher_���.
	 * 
	 * @param court the court
	 * @param charge the charge
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ��������Fetcher_���(���� court, ���� charge) throws HttpException,
			IOException {
		this(court, charge.get�Ű�����(), charge.get�����ڵ�(), charge.get�����̸�());
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
		logger.log(Level.FINEST,pageNo + "��° ������ �������⸦ �õ��մϴ�");
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
			logger.log(Level.FINEST,pageNo + "��° �������� �������µ� �����߽��ϴ�");
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
		logger.info("��ü �������� ���� �غ����Դϴ�.");
		int max = 1;
		boolean finding = true;

		while (finding) {
			logger.info(max + "�� ���� ������ ������������ �м����Դϴ�.");
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
		logger.info(max + "�� ���� ������ �������Դϴ�.");

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
