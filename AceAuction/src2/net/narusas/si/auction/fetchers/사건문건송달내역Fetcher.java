package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import net.narusas.si.auction.builder.FileUploaderBG;
import net.narusas.si.auction.model.사건;
import net.narusas.util.lang.NFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 사건문건송달내역Fetcher {
	final Logger logger = LoggerFactory.getLogger("auction");

	String fetch(사건 사건) throws IOException {
		String query = MessageFormat.format("/RetrieveRealEstSaDetailInqMungunSongdalList.laf" //
				+ "?jiwonNm={0}&saNo={1}" //
		, HTMLUtils.encodeUrl(사건.get법원().get법원명()), String.valueOf(사건.get사건번호()));

		return 대법원Fetcher.getInstance().fetch(query);
	}

	String parse문건처리내역(String html) {
		int startPos = html.indexOf("<table class=\"Ltbl_dt\" summary=\"문건처리내역 표\">");
		if (startPos == -1) {
			return "";
		}
		int endPos = html.indexOf("</table>", startPos);
		return html.substring(startPos, endPos + 8);
	}

	String parse송달내역(String html) {
		int startPos = html.indexOf("<table class=\"Ltbl_dt\" summary=\"송달내역 표\">");
		int endPos = html.indexOf("</table>", startPos);
		return html.substring(startPos, endPos + 8);
	}

	public String[] download(사건 사건) throws IOException {
		logger.info("사건의 문서 송달내역 페이지를 다운로드합니다");
		File path = new File("download/" + 사건.getPath());
		if (path.exists() == false) {
			path.mkdirs();
		}
		String html = fetch(사건);
		String 문건처리내역 = parse문건처리내역(html);
		if (문건처리내역 != null && "".equals(문건처리내역.trim()) == false) {
			File 문건처리내역File = new File(path, "Mungun.html");
			NFile.write(문건처리내역File, 문건처리내역, "euc-kr");
			FileUploaderBG.getInstance().upload(사건.getPath(), "Mungun.html", 문건처리내역File);
		}

		String 송달내역 = parse송달내역(html);
		if (송달내역 != null && "".equals(송달내역.trim()) == false) {
			File 송달내역File = new File(path, "Song.html");
			NFile.write(송달내역File, 송달내역, "euc-kr");
			FileUploaderBG.getInstance().upload(사건.getPath(), "Song.html", 송달내역File);
		}

		return new String[] { 문건처리내역, 송달내역 };
	}

}
