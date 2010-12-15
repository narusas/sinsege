package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.interaction.ProgressBar;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.���;

import org.apache.commons.httpclient.HttpException;
import org.htmlparser.util.ParserException;

/**
 * ����� ����Ʈ���� ��� ����� ������ Fetcher.
 * 
 * @author narusas
 * 
 */
public class ��������Fetcher {

	static Logger logger = Logger.getLogger("log");

	static Pattern p = Pattern
			.compile("�Ű����� :<b> (\\d\\d\\d\\d.\\d+.\\d+) \\(([^,]*), (.+)\\)");

	private ���� charge;

	private final int chargeCode;

	private final String chargeName;

	private final ���� court;
	private final Date date;

	private PageFetcher fetcher;

	public ��������Fetcher(���� courtCode, Date date, int chargeCode,
			String chargeName) throws HttpException, IOException {
		this.court = courtCode;
		this.date = date;
		this.chargeCode = chargeCode;
		this.chargeName = chargeName;
		fetcher = �����Fetcher.getInstance();
	}

	public ��������Fetcher(���� court, ���� charge) throws HttpException, IOException {
		this(court, charge.get�Ű�����(), charge.get�����ڵ�(), charge.get�����̸�());
		this.charge = charge;
	}

	public String getPage(int pageNo) throws IOException {
		logger.log(Level.FINEST,pageNo + "��° ������ �������⸦ �õ��մϴ�");
		pageNo = pageNo == 0 ? 1 : pageNo;
		try {

			String query = "/au/SuperServlet?target_command=au.command.auc.C100ListCommand&page="
					+ pageNo
					+ "&search_flg=2&bub_cd="
					+ court.getCode()
					+ "&level=&addr1=&addr2=&addr3=&mulutil_cd=&amt_flg=&amt="
					+ "&mae_giil="
					+ date.toString().replaceAll("-", ".")
					+ "&jp_cd="
					+ chargeCode
					+ "&dam_nm="
					+ URLEncoder.encode(chargeName, "euc-kr")
					+ "&browser=&check_msg=";
			String res = fetcher.fetch(query);

			logger.log(Level.FINEST,pageNo + "��° �������� �������µ� �����߽��ϴ�");
			return res;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

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
	 * ��� �������� �о�ͼ� �м�, ��� ��ü���� ��ȯ�Ѵ�.
	 * 
	 * @return
	 * @throws IOException
	 * @throws ParserException
	 */
	public List<���> getSaguns() throws IOException, ParserException {
		logger.info("��ü ��� ����� �����⸦ �õ��մϴ�.");
		String[] pages = getPages();

		logger.info("����������� ������ ������ ������ ������Ʈ�մϴ�.");
		updateCharge(charge, pages[0]);

		ArrayList<���> list = new ArrayList<���>();
		��������Parser parser = new ��������Parser();
		ProgressBar.getInstance().addMax(pages.length);
		logger.info("�� ��� ������ ���� " + pages.length + "�Դϴ�. ");
		for (int i = 0; i < pages.length; i++) {
			logger.info(i + "�������� ����� �м��մϴ�.");
			String page = pages[i];
			list.addAll(parser.parsePage(page, court, charge));
		}
		logger.info("���� �������� ���� ����Ǵ� ����� ��Ĩ�ϴ�.");
		ArrayList<���> res = merge(list);
		logger.info("����� �м��� �������ϴ�.");
		return res;
	}

	private void updateCharge(���� charge2, String src) {
//		System.out.println(src);
		if (charge2 == null) {
			return;
		}
		charge2.set���(parseLocationInfo(src));
		charge2.set�Ű��ð�(parseTimeInfo(src));
	}

	public static String parseDateInfo(String src) {
		Matcher m = p.matcher(src);
		m.find();

		return m.group(1) + " " + m.group(2);
	}

	public static String parseLocationInfo(String src) {
		Matcher m = p.matcher(src.replaceAll("&nbsp;", " "));
		m.find();

		return m.group(3);
	}

	private static String parseTimeInfo(String src) {
		Matcher m = p.matcher(src.replaceAll("&nbsp;", " "));
		m.find();

		return m.group(2);
	}

	/**
	 * ������������ ���� ����Ǵ� ��ǵ��� ��ģ��.
	 * 
	 * @param list
	 * @return
	 */
	static ArrayList<���> merge(ArrayList<���> list) {
		ArrayList<���> res = new ArrayList<���>();
		res.add(list.get(0));

		��� s2 = null;
		for (int i = 1; i < list.size(); i++) {
			��� s = res.get(res.size() - 1);
			s2 = list.get(i);

			if (s.getEventNo() == s2.getEventNo()) {
				logger.info(s.getEventNo() + " ����� �������� �Ѿ�� ��Ĩ�ϴ�.");
				s.merge(s2);
			} else {
				res.add(s2);
			}
		}
		return res;
	}

}
