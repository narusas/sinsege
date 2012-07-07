package net.narusas.si.auction.app.onbid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.fetchers.PageFetcher;

import org.apache.commons.httpclient.HttpException;

public class 공매결과물건Fetcher {
	public List<공매물건> fetch(공매결과일정 schedule) throws HttpException, IOException {
		List<공매물건> result = new ArrayList<공매물건>();
		PageFetcher f = new PageFetcher("http://www.onbid.co.kr");
		System.out.println(schedule.getLinke(1));
		String page = f.fetch(schedule.getLinke(1));
		
		// System.out.println(page);
		page = page.substring(page.indexOf("<!-- 목록 반복 시작 -->") + 10);
		Pattern totalPattern = Pattern.compile("javascript:paging\\((\\d+)\\)");
		Matcher m = totalPattern.matcher(page);
		String lastPage = null;
		while (m.find()) {
			lastPage = m.group(1);
		}
		System.out.println("######## Last Page:" + lastPage);
		if (lastPage == null) {
			lastPage = "1";
		}
		int total = Integer.parseInt(lastPage);
		int pageNo = 2;
		do {
			parse(result, page);
			System.out.println(schedule.getLinke(pageNo) );
			page = f.fetch(schedule.getLinke(pageNo));
		} while (pageNo++ <= total);
		return result;
	}

	private void parse(List<공매물건> result, String page) {
		String[] temp = page.split("<\\!-- 반복구간 시작 -->");
		String[] chunks = new String[temp.length - 1];
		System.arraycopy(temp, 1, chunks, 0, temp.length - 1);
		for (String chunk : chunks) {
			System.out.println("##############################################");
			공매물건 item = new 공매물건();
			chunk = chunk.replaceAll("<br>", " ");
			chunk = chunk.replaceAll("&nbsp;", " ");
			chunk = chunk.replaceAll("<font[^>]*>", "");
			chunk = chunk.replaceAll("</font[^>]*>", "");
			chunk = chunk.replaceAll("\\s+", " ");
			Pattern itemNoPattern = Pattern.compile(">(\\d+-\\d+-\\d+)</td>");
			Matcher itemMatcher = itemNoPattern.matcher(chunk);
			itemMatcher.find();
			item.set입찰번호(itemMatcher.group(1));

			Pattern pricePattern = Pattern.compile("align=right>\\s*([\\d,]+)</td>");
			Matcher priceMatcher = pricePattern.matcher(chunk);
			priceMatcher.find();// 예정가
			if (priceMatcher.find()) {
				item.set낙찰가(priceMatcher.group(1));
			}

			Pattern resultPattern = Pattern.compile("td>(유찰[^<]*|낙찰[^<]*)</td>");
			Matcher reulstMatcher = resultPattern.matcher(chunk);
			if (reulstMatcher.find()) {
				item.set개찰결과(reulstMatcher.group(1));
				System.out.println(reulstMatcher.group(1));
			}
			result.add(item);
			System.out.println(item);
		}
	}
}
