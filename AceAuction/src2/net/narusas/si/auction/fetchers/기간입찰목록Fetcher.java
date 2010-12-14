package net.narusas.si.auction.fetchers;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.법원;

/**
 * http://www.courtauction.go.kr/RetrieveBubwGiilList.laf?ipchalGbncd=000332&
 * _NEXT_SRNID=PNO102005 에서 기간입찰의 전체 목ㄹㅗㄱ을 얻을수 있다.
 * 
 * @author narusas
 * 
 */
public class 기간입찰목록Fetcher {
	public static final String REGX_COMMON = "<tr>\\s+<td>(\\d\\d\\d\\d\\.\\d\\d\\.\\d\\d)\\s+~\\s+(\\d\\d\\d\\d\\.\\d\\d\\.\\d\\d)"
			+ "\\s+</td>\\s+<td>\\s+(\\d\\d\\d\\d\\.\\d\\d\\.\\d\\d)\\s+</td>\\s+"
			+ "<td>\\s*([^<]+)</td>\\s*<td[^>]+>\\s*";
	public static final String REGX_CHARGE = "<a href=\"#\" onclick=\"javascript:listSrch\\("
			+ "'([^']*)',\\s+'([^']*)',\\s+'([^']*)',\\s+'([^']*)',\\s+'([^']*)',\\s+'([^']*)',\\s+'([^']*)',\\s+'([^']*)',\\s+'([^']*)'"
			+ "[^>]+>\\s*([^<]*)";

	Pattern p_common = Pattern.compile(REGX_COMMON, Pattern.MULTILINE);
	Pattern p_charge = Pattern.compile(REGX_CHARGE, Pattern.MULTILINE);

	
	public List<담당계> fetch() throws IOException, Exception {
		return parse(fetchPage());
	}

	public String fetchPage() throws IOException {
		대법원Fetcher.getInstance().prepare();
		return 대법원Fetcher.getInstance().fetch(
				"/RetrieveBubwGiilList.laf?ipchalGbncd=000332&_NEXT_SRNID=PNO102005");
	}

	public List<담당계> parse(String html) throws Exception {
		String temp = html.substring(html.indexOf("<caption>매각 일정</caption>"));
		temp = html.substring(html.indexOf("<tbody>"));
		temp = temp.substring(0, temp.indexOf("</table>"));
		String[] trs = temp.split("</tr");

		List<담당계> res = new ArrayList<담당계>();

		for (String tr : trs) {
			Matcher m = p_common.matcher(tr);
			// System.out.println(tr);
			if (m.find() == false) {
				continue;
			}
			Date startDay = HTMLUtils.toDate(m.group(1));
			Date endDay = HTMLUtils.toDate(m.group(2));
			// System.out.println(startDay);
			// System.out.println(endDay);

			java.sql.Date eventDay = HTMLUtils.toDate(m.group(3));
			// System.out.println(eventDay);

			// FIXME 불필요한 의존성.
			String courtName = m.group(4);

			법원 court = 법원.findByName(courtName);
			System.out.println(courtName+":"+court);
			Matcher m2 = p_charge.matcher(tr);

			while (m2.find()) {
				int chargeCode = Integer.parseInt(m2.group(2).trim());
				String time = m2.group(4);

				String chargeName = m2.group(10);
				String location = m2.group(9);

				// Builder 쪽에서
				// 담당계 charge = 담당계.find(chargeCode,
				// Integer.parseInt(court.getCode()), eventDay);
				// if (charge == null) {
				담당계 charge = new 담당계(court, chargeCode, HTMLUtils.strip(chargeName), eventDay, startDay,
						endDay, location, "기간입찰", time);
				// }
				res.add(charge);

			}
		}

		return res;
	}

}
