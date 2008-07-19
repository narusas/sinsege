package net.narusas.aceauction.fetchers.pregoods;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;

import net.narusas.aceauction.fetchers.PageFetcher;

public class BeadangListFetcher {
	PageFetcher fetcher;

	public BeadangListFetcher() {
		try {
			fetcher = new PageFetcher(
					"http://www.courtauction.go.kr/au/SuperServlet?target_command=");
		} catch (HttpException e) {
		} catch (IOException e) {
		}
	}

	public List<SagunListItem> fetch(String bub_cd, String bub_name,
			String jp_cd, String dam_nm) throws HttpException, IOException {

		String query = "bub_cd=" + URLEncoder.encode(bub_cd, "euc-kr")//
				+ "&" + "bub_nm=" + URLEncoder.encode(bub_name, "euc-kr")//
				+ "&" + "jp_cd=" + URLEncoder.encode(jp_cd, "euc-kr")//
				+ "&" + "dam_nm=" + URLEncoder.encode(dam_nm, "euc-kr");

		String page = fetcher.fetch("au.command.aua.A312ListCommand&" + query);

		Pattern linkPattern = Pattern
				.compile("javascript:loadSaDetail\\( '(\\d+)', '([^']+)' \\)");
		Matcher m = linkPattern.matcher(page);

		List<SagunListItem> result = new ArrayList<SagunListItem>();
		while (m.find()) {
			String 사건번호 = m.group(1);
			SagunListItem item = new SagunListItem(사건번호);
			result.add(item);
		}
		return result;
	}
}
