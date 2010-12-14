package net.narusas.si.auction.app.onbid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;

import net.narusas.si.auction.fetchers.PageFetcher;

public class 공매결과일정Fetcher {
	String startPage = "http://www.onbid.co.kr/frontup/portal/result/control/result/getKamcoAuctionList.do";

	public List<공매결과일정> fetch() throws HttpException, IOException {
		PageFetcher f = new PageFetcher(
				"http://www.onbid.co.kr/frontup/portal/result/control/result/getKamcoAuctionList.do");
		String page = f.fetch("");
		List<공매결과일정> res = new ArrayList<공매결과일정>();
		String[] chunks = page.split("<\\!-- 반복구간 시작 -->");
		for (String chunk : chunks) {
			chunk = chunk.replaceAll("<br>", " ");
			chunk = chunk.replaceAll("&nbsp;", " ");
			chunk = chunk.replaceAll("<font[^>]*>", "");
			chunk = chunk.replaceAll("</font[^>]*>", "");
			chunk = chunk.replaceAll("\\s+", " ");
			// System.out.println(chunk);

			Pattern p = Pattern.compile("<a href=\"javascript:goTo\\(" + "'([^']+)'\\s*," + "\\s*'([^']+)'\\s*,"
					+ "\\s*'([^']+)'\\s*," + "\\s*'([^']+)'\\s*," + "\\s*'([^']+)'\\s*," + "\\s*'([^']+)'\\s*,"
					+ "\\s*'([^']+)'\\)[^>]+>"
					+ "([^<]+)</a> </td> <td[^>]+></td> <td[^>]+>([^<]+)</td> <td[^>]+></td> <td[^>]+>([^<]+)</td>");

			Matcher m = p.matcher(chunk);
			if (m.find() == false) {
				continue;
			}

			String auction_no = m.group(1);
			String business_type = m.group(2);
			String up_pageSize = m.group(3);
			String up_pageIndex = m.group(4);
			String up_orderfield = m.group(5);
			String up_flag = m.group(6);
			String strOpenState = m.group(7);

			String 개찰일시 = m.group(8);
			String 자산구분 = m.group(9);
			String 부점명 = m.group(10);
			공매결과일정 schedule = new 공매결과일정();
			schedule.setLinkInof(auction_no, business_type, up_pageSize, up_pageIndex, up_orderfield, up_flag,
					strOpenState);
			schedule.set개찰일시(개찰일시);
			schedule.set자산구분(자산구분);
			schedule.set부점명(부점명);

			System.out.println(schedule);
			res.add(schedule);
		}
		return res;
	}
}
