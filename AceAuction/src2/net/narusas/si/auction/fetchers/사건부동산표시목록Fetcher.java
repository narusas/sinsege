package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import net.narusas.si.auction.builder.FileUploaderBG;
import net.narusas.si.auction.model.사건;
import net.narusas.util.lang.NFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 사건부동산표시목록Fetcher {
	final Logger logger = LoggerFactory.getLogger("auction");

	String fetch(사건 사건) throws IOException {
		String query = MessageFormat.format("/RetrieveRealEstHjosaDispMokrok.laf" //
				+ "?jiwonNm={0}&saNo={1}" //
		, HTMLUtils.encodeUrl(사건.get법원().get법원명()), String.valueOf(사건.get사건번호()));

		return 대법원Fetcher.getInstance().fetch(query);
	}

	String parse(String html) {
		int pos = html.indexOf("<h3>부동산표시목록</h3>");
		if (pos == -1) {
			return null;
		}
		String temp = html.substring(html.indexOf("<h3>부동산표시목록</h3>") - 57);
		StringBuffer buf = new StringBuffer();
		String[] lines = temp.split("\n");
		for (String line : lines) {
			buf.append(line).append("\n");
			if (line.startsWith("</div>")) {
				break;
			}
		}
		return buf.toString();
	}

	public String download(사건 사건) throws IOException {
		logger.info("사건의 부동사표시목록 페이지를 다운로드합니다");
		File path = new File("download/" + 사건.getPath());
		if (path.exists() == false) {
			path.mkdirs();
		}
		File f = new File(path, "LandMark");
		String text = parse(fetch(사건));
		if (text == null) {
			return null;
		}
		NFile.write(f, text, "euc-kr");
		FileUploaderBG.getInstance().upload(사건.getPath(), "LandMark", f);
		return text;
	}


}
