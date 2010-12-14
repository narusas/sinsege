package net.narusas.si.auction.fetchers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.법원;

import org.apache.commons.httpclient.HttpException;

public class 기일입찰목록Fetcher {
	public static final String REGX_COMMON = "<tr>\\s+<td>(\\d\\d\\d\\d\\.\\d\\d\\.\\d\\d)\\s+~\\s+(\\d\\d\\d\\d\\.\\d\\d\\.\\d\\d)"
			+ "\\s+</td>\\s+<td>\\s+(\\d\\d\\d\\d\\.\\d\\d\\.\\d\\d)\\s+</td>\\s+"
			+ "<td>\\s*([^<]+)</td>\\s*<td[^>]+>\\s*";
	public static final String REGX_CHARGE = "<a href=\"#\" onclick=\"javascript:listSrch\\("
			+ "'([^']*)',\\s+'([^']*)',\\s+'([^']*)',\\s+'([^']*)',\\s+'([^']*)',\\s+'([^']*)',\\s+'([^']*)',\\s+'([^']*)',\\s+'([^']*)'"
			+ "[^>]+>\\s*([^<]*)";
	Pattern p_common = Pattern.compile(REGX_COMMON, Pattern.MULTILINE);
	Pattern p_charge = Pattern.compile(REGX_CHARGE, Pattern.MULTILINE);

	public List<담당계> fetch(법원 court) throws HttpException, IOException, Exception {
		return parse(court, fetchPage(court));
	}

	public String fetchPage(법원 court) throws HttpException, IOException {
		대법원Fetcher.getInstance().prepare();
		String tmp = 대법원Fetcher.getInstance().fetch(
				"/RetrieveBubwGiilList.laf?ipchalGbncd=000331&_NEXT_SRNID=PNO102005" +
				"&_SRCH_SRNID=PNO102005&_CUR_CMD=RetrieveBubwGiilList.laf"+
				"&jiwonNm="
						+ HTMLUtils.encodeUrl(court.get법원명()));
		if (tmp.contains("검색결과가 없습니다")) {
			tmp = 대법원Fetcher.getInstance().fetch(
					"/RetrieveBubwGiilList.laf?ipchalGbncd=000331&_NEXT_SRNID=PNO102005" +
					"&_SRCH_SRNID=PNO102005&_CUR_CMD=RetrieveBubwGiilList.laf"+
					"&jiwonNm="
							+ HTMLUtils.encodeUrl(court.get법원명()));
		}
		
		return tmp;
	}

	public List<담당계> parse(법원 court, String html) throws Exception {
		String temp = html.substring(html.indexOf("<caption>매각 일정</caption>"));
		temp = html.substring(html.indexOf("<tbody>"));
		temp = temp.substring(0, temp.indexOf("</table>"));
		String[] trs = temp.split("</tr");

		List<담당계> res = new ArrayList<담당계>();

		for (String tr : trs) {
			Matcher m2 = p_charge.matcher(tr);

			while (m2.find()) {
				int chargeCode = Integer.parseInt(m2.group(2).trim());
				java.util.Date eventDay = new SimpleDateFormat("yyyyMMdd").parse(m2.group(3));
				String firstTime = convertTime(m2.group(5));
				String secondTime = convertTime(m2.group(6));
				
				String time = mergeTime(firstTime, secondTime);

				String chargeName = m2.group(10);
				String location = m2.group(9);

				담당계 charge = new 담당계(court, chargeCode, HTMLUtils.strip(chargeName), eventDay, location, "기일입찰", time);
				res.add(charge);

			}
		}

		return res;
	}

	private String mergeTime(String firstTime, String secondTime) {
		if (secondTime == null){
			return firstTime;
		}
		return firstTime+","+secondTime;
	}

	private String convertTime(String str) {
		if (str == null || "".equals(str)){
			return null;
		}
		return str.substring(0,2)+":"+str.substring(2, 4);
	}
}
