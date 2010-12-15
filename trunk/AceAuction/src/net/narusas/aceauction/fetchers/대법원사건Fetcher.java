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
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.법원;
import net.narusas.aceauction.model.사건;

import org.apache.commons.httpclient.HttpException;
import org.htmlparser.util.ParserException;

/**
 * 대법원 사이트에서 사건 목록을 얻어오는 Fetcher.
 * 
 * @author narusas
 * 
 */
public class 대법원사건Fetcher {

	static Logger logger = Logger.getLogger("log");

	static Pattern p = Pattern
			.compile("매각기일 :<b> (\\d\\d\\d\\d.\\d+.\\d+) \\(([^,]*), (.+)\\)");

	private 담당계 charge;

	private final int chargeCode;

	private final String chargeName;

	private final 법원 court;
	private final Date date;

	private PageFetcher fetcher;

	public 대법원사건Fetcher(법원 courtCode, Date date, int chargeCode,
			String chargeName) throws HttpException, IOException {
		this.court = courtCode;
		this.date = date;
		this.chargeCode = chargeCode;
		this.chargeName = chargeName;
		fetcher = 대법원Fetcher.getInstance();
	}

	public 대법원사건Fetcher(법원 court, 담당계 charge) throws HttpException, IOException {
		this(court, charge.get매각기일(), charge.get담당계코드(), charge.get담당계이름());
		this.charge = charge;
	}

	public String getPage(int pageNo) throws IOException {
		logger.log(Level.FINEST,pageNo + "번째 페이지 가져오기를 시도합니다");
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

			logger.log(Level.FINEST,pageNo + "번째 페이지를 가져오는데 성공했습니다");
			return res;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

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
	 * 사건 페이지를 읽어와서 분석, 사건 객체들을 반환한다.
	 * 
	 * @return
	 * @throws IOException
	 * @throws ParserException
	 */
	public List<사건> getSaguns() throws IOException, ParserException {
		logger.info("전체 사건 목록을 얻어오기를 시도합니다.");
		String[] pages = getPages();

		logger.info("사건페이지의 정보로 담당계의 정보를 업데이트합니다.");
		updateCharge(charge, pages[0]);

		ArrayList<사건> list = new ArrayList<사건>();
		대법원사건Parser parser = new 대법원사건Parser();
		ProgressBar.getInstance().addMax(pages.length);
		logger.info("총 사건 페이지 수는 " + pages.length + "입니다. ");
		for (int i = 0; i < pages.length; i++) {
			logger.info(i + "페이지의 사건을 분석합니다.");
			String page = pages[i];
			list.addAll(parser.parsePage(page, court, charge));
		}
		logger.info("여러 페이지에 걸쳐 진행되는 사건을 합칩니다.");
		ArrayList<사건> res = merge(list);
		logger.info("사건의 분석이 끝났습니다.");
		return res;
	}

	private void updateCharge(담당계 charge2, String src) {
//		System.out.println(src);
		if (charge2 == null) {
			return;
		}
		charge2.set장소(parseLocationInfo(src));
		charge2.set매각시간(parseTimeInfo(src));
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
	 * 여러페이지에 걸쳐 진행되는 사건들을 합친다.
	 * 
	 * @param list
	 * @return
	 */
	static ArrayList<사건> merge(ArrayList<사건> list) {
		ArrayList<사건> res = new ArrayList<사건>();
		res.add(list.get(0));

		사건 s2 = null;
		for (int i = 1; i < list.size(); i++) {
			사건 s = res.get(res.size() - 1);
			s2 = list.get(i);

			if (s.getEventNo() == s2.getEventNo()) {
				logger.info(s.getEventNo() + " 사건이 페이지가 넘어가서 합칩니다.");
				s.merge(s2);
			} else {
				res.add(s2);
			}
		}
		return res;
	}

}
