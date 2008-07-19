package net.narusas.aceauction.fetchers.pregoods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;

import net.narusas.aceauction.fetchers.PageFetcher;
import net.narusas.aceauction.model.법원;

public class ChargeFetcher {
	public List<Charge> fetch(법원 court) throws HttpException, IOException{
		PageFetcher fetcher = new PageFetcher(
		"http://www.courtauction.go.kr/au/SuperServlet?target_command=au.command.aua.A312SearchCommand&bub_cd=");

		String page = fetcher.fetch(court.getCode());
		Pattern p = Pattern
				.compile("<option value=\"(\\d+)\" >([^<]+)</option>");
		Matcher m = p.matcher(page);

		List<Charge> result = new ArrayList<Charge>();
		while (m.find()) {
			String code = m.group(1);
			String name = m.group(2);
			result.add(new Charge(code, name));
		}
		return result;
	}
}
