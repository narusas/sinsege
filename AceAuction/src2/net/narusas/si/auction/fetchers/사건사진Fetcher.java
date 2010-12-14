package net.narusas.si.auction.fetchers;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.model.사건;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 사건사진Fetcher {
	final Logger logger = LoggerFactory.getLogger("auction");

	String fetch(사건 사건, int 사진페이지번호) throws IOException {
		String query = MessageFormat.format("/RetrieveSaPhotoInfo.laf" //
				+ "?jiwonNm={0}&saNo={1}&targetRow={2}" //
		, HTMLUtils.encodeUrl(사건.get법원().get법원명()), String.valueOf(사건.get사건번호()), 사진페이지번호);

		return 대법원Fetcher.getInstance().fetch(query);
	}

	/**
	 * 첫번째 페이지를 얻어온다.
	 * 
	 * @param 사건
	 * @return
	 * @throws IOException
	 */
	String fetch(사건 사건) throws IOException {
		return fetch(사건, 1);
	}

	int parseLastPage(String html) {
		Pattern p = Pattern.compile("goPage\\('(\\d+)'\\)", Pattern.MULTILINE);
		Matcher m = p.matcher(html);
		String lastStr = null;
		while (m.find()) {
			lastStr = m.group(1);
		}
		if (lastStr == null) {
			return 1;
		}

		return Integer.parseInt(lastStr);
	}

	String parseImgUrl(String html) {
		Pattern p = Pattern.compile("<img src=\"(/DownFront[^\"]+)", Pattern.MULTILINE);
		Matcher m = p.matcher(html);
		if (m.find()) {
			return HTMLUtils.converHTMLSpecialChars(m.group(1));
		}

		return null;
	}

	List<String> fetchAllImageURLs(사건 사건) throws IOException {
		String html = fetch(사건);
		List<String> urls = new LinkedList<String>();
		String url = parseImgUrl(html);
		if (url == null) {
			return urls;
		}
		urls.add(url);

		int last = parseLastPage(html);
		for (int i = 2; i <= last; i++) {
			urls.add(parseImgUrl(fetch(사건, i)));
		}
		return urls;
	}

}
