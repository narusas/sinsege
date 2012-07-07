package net.narusas.si.auction.app.onbid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.fetchers.PageFetcher;

import org.apache.commons.httpclient.HttpException;

public class 공매물건Fetcher {

	List<공매물건> fetch(공매일정 schedule, FetcherCallback fetcherCallback) throws HttpException, IOException {
		PageFetcher f = new PageFetcher("http://www.onbid.co.kr");

		List<공매물건> res = new ArrayList<공매물건>();
		fetcherCallback.log("첫페이지를 얻어옵니다");
		int total = fetch(schedule, f, 1, res);
		fetcherCallback.setTotal(total);
		for (int i = 2; i <= total; i++) {
			fetcherCallback.progress(i);
			fetch(schedule, f, i, res);
		}
		return res;
	}

	Pattern pattern = Pattern
			.compile("detailSearch\\('(\\d*)','(\\d*)','(\\d*)','[^']*','[^']*','[^']*','[^']*','(\\d+)'");

	private int fetch(공매일정 schedule, PageFetcher f, int pageNo, List<공매물건> res) throws IOException {
		String url = schedule.getUrl() + "&pageSize=10" + "&pageIndex=" + pageNo + "&orderName=" + "&orderType=DESC";
		System.out.println(url);
		// System.out.println(url);
		String page = f.fetch(url);

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
		boolean end = false;

		while (true) {
			공매물건 item = new 공매물건();
			int pos = page.indexOf("<!-- 목록 반복 시작 -->");
			if (pos == -1) {
				pos = page.length() - 2;
				// page = page.substring(pos + 1);
				end = true;
			}
			try {

				Matcher detailSearchMatcher = pattern.matcher(page);
				if (detailSearchMatcher.find()) {
					String ANNOUNCE_NO = detailSearchMatcher.group(1);
					String AUCTION_NO = detailSearchMatcher.group(2);
					String GOODS_ID = detailSearchMatcher.group(3);
					String RNUM = detailSearchMatcher.group(4);

					String detailUrl = "/frontup/portal/catalog/control/pubaucTrcnCllt/goPortalCatalogDetailView.do"
							+ "?ANNOUNCE_NO=" + ANNOUNCE_NO + "&AUCTION_NO=" + AUCTION_NO + "&GOODS_ID=" + GOODS_ID
							+ "&MENU_CODE=m0303000100&RNUM=" + RNUM + "&PRENEXT_YN=Y&BUSINESS_TYPE=5" + "&FLAG=";
					item.setUrl("http://www.onbid.co.kr" + detailUrl);
				}

				String chunk = page.substring(0, pos);
				chunk = chunk.substring(chunk.indexOf("<tr"));
				chunk = chunk.substring(chunk.indexOf("<td") + 1);
				chunk = chunk.substring(chunk.indexOf("<td") + 1);
				chunk = chunk.substring(chunk.indexOf("<td"));

				chunk = chunk.replaceAll("<[bB][rR]>", "\n");
				chunk = chunk.replaceAll("&nbsp;", " ");
				chunk = chunk.replaceAll("<font[^>]*>", "");
				chunk = chunk.replaceAll("<b>", "");
				chunk = chunk.replaceAll("</b>", "");
				chunk = chunk.replaceAll("<U>", "");
				chunk = chunk.replaceAll("</U>", "");

				chunk = chunk.replaceAll("</font[^>]*>", "");
				chunk = chunk.replaceAll("(<img[^>]*>)", "");
				chunk = chunk.replaceAll("(<input[^>]*>)", "");

				Pattern infoPattern = Pattern.compile("<td[^>]+>([^/]+)/([^\\(]+)\\(([^\\)]+)\\)</td>");
				Matcher m2 = infoPattern.matcher(chunk);
				m2.find();
				item.set상위용도(m2.group(1).trim());
				item.set하위용도(m2.group(2).trim());
				item.set입찰번호(m2.group(3).trim());

				Pattern infoPattern2 = Pattern.compile("<span[^>]+>([^<]+)</span>");
				Matcher m3 = infoPattern2.matcher(chunk);
				m3.find();
				item.set소재지(m3.group(1));
				// System.out.println(m3.group(1));

				Pattern pattern = Pattern.compile("\\[물건관리번호\\]([^<]+)<");
				Matcher matcher = pattern.matcher(chunk);
				matcher.find();
				item.set물건관리번호(matcher.group(1));

				chunk = chunk.substring(matcher.end());
				chunk = chunk.substring(chunk.indexOf("</table"));
				chunk = chunk.substring(chunk.indexOf("</td>") + 1);
				chunk = chunk.substring(chunk.indexOf("</td>") + 1);
				matcher = Pattern.compile("<td[^>]+><td[^>]+>").matcher(chunk);
				m.find();

				matcher = Pattern.compile("<td[^>]+>([^/]+)/([^<]+)").matcher(chunk);
				matcher.find();
				item.set처분정보1(matcher.group(1).trim());
				item.set처분정보2(matcher.group(2).trim());

				chunk = chunk.substring(matcher.end());
				chunk = chunk.substring(chunk.indexOf("</td>") + 1);
				chunk = chunk.substring(chunk.indexOf("</td>") + 1);

				matcher = Pattern.compile("<td[^>]+>([^\\n]+)\\n([^\\n]+)\\n\\(([^\\)]+)").matcher(chunk);
				matcher.find();
				item.set감정가(matcher.group(1));
				item.set최초예정가액(matcher.group(2));
				item.set최저입찰가(matcher.group(3));
				chunk = chunk.substring(matcher.end());
				chunk = chunk.substring(chunk.indexOf("</td>") + 1);
				chunk = chunk.substring(chunk.indexOf("</td>") + 1);
				chunk = chunk.substring(chunk.indexOf("</td>") + 1);
				chunk = chunk.substring(chunk.indexOf("</td>") + 1);

				matcher = Pattern.compile("<td[^>]+>([^\\n]+)\\n([^<]+)").matcher(chunk);
				matcher.find();
				item.set물건상태(matcher.group(1).trim());
				item.set유찰회수(matcher.group(2).trim());

				chunk = chunk.substring(matcher.end());
				chunk = chunk.substring(chunk.indexOf("<table") + 1);
				chunk = chunk.substring(chunk.indexOf("<tr") + 1);
				chunk = chunk.substring(chunk.indexOf("<tr") + 1);
				matcher = Pattern.compile("<td[^>]+>([^<]+)").matcher(chunk);
				if (matcher.find()) {
					item.set내역(matcher.group(1).replaceAll("^\\s+", "").trim());
				}

				System.out.println(item);
				res.add(item);
				item.set공매일정(schedule);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			page = page.substring(pos + 1);

			if (end) {
				break;
			}
		}

		return total;

	}
}
