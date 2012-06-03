package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.builder.FileUploaderBG;
import net.narusas.si.auction.model.문건처리내역;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.model.송달내역;
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
			update사건문건처리내역(사건,문건처리내역);
			File 문건처리내역File = new File(path, "Mungun.html");
			NFile.write(문건처리내역File, 문건처리내역, "euc-kr");
			FileUploaderBG.getInstance().upload(사건.getPath(), "Mungun.html", 문건처리내역File);
		}

		String 송달내역 = parse송달내역(html);
		if (송달내역 != null && "".equals(송달내역.trim()) == false) {
			update송달내역(사건, 송달내역);
			File 송달내역File = new File(path, "Song.html");
			NFile.write(송달내역File, 송달내역, "euc-kr");
			FileUploaderBG.getInstance().upload(사건.getPath(), "Song.html", 송달내역File);
		}

		return new String[] { 문건처리내역, 송달내역 };
	}

	private void update사건문건처리내역(사건 사건, String text) {
		text = text.substring(text.indexOf("<tbody>"));
		String[] chunks = text.split("<tr>");
		Pattern p = Pattern.compile("<td[^>]+>([^<]+)</td>\\s+<td>\\s+([^<]+)</td>\\s+<td>\\s+([^<]+)</td>");
		List<문건처리내역> items = new ArrayList<문건처리내역>();
		for (String chunk : chunks) {
			Matcher m = p.matcher(chunk);
			if (m.find()==false){
				continue;
			}
			String 접수일 = m.group(1).trim();
			String 접수내역 = m.group(2).trim();
			String 결과 = m.group(3).trim();
			
			문건처리내역 item = new 문건처리내역();
			item.set접수일(접수일);
			item.set접수내역(접수내역);
			item.set결과(결과);
			items.add(item);
		}
		사건.set문건처리내역(items);
	}

	private void update송달내역(사건 사건, String text) {
		text = text.substring(text.indexOf("</tr>"));
		String[] chunks = text.split("<tr>");
		Pattern p = Pattern.compile("<td[^>]*>([^<]+)</td>\\s*<td[^>]*>([^<]+)</td>\\s*<td[^>]*>([^<]+)</td>");
		List<송달내역> items = new ArrayList<송달내역>();
		for (String chunk : chunks) {
			Matcher m = p.matcher(chunk);
			if (m.find()==false){
				continue;
			}
			String 송달일 = m.group(1).trim();
			String 송달내역 = m.group(2).trim();
			String 송달결과 = m.group(3).trim();
			송달내역 item = new 송달내역();
			item.set송달일(송달일);
			item.set송달내역(송달내역);
			item.set송달결과(송달결과);
			items.add(item);
		}
		사건.set송달내역(items);
	}

}
