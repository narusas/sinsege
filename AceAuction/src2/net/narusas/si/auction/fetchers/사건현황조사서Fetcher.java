package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.builder.FileUploaderBG;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.model.점유관계;
import net.narusas.util.lang.NFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 사건현황조사서Fetcher {
	final Logger logger = LoggerFactory.getLogger("auction");

	String fetch(사건 사건) throws IOException {
		String query = MessageFormat.format("/RetrieveRealEstSaHjosa.laf" //
				+ "?jiwonNm={0}&saNo={1}&srnID=PNO102018" //
		, HTMLUtils.encodeUrl(사건.get법원().get법원명()), String.valueOf(사건.get사건번호()));

		return 대법원Fetcher.getInstance().fetch(query);
	}

	String parse(String html) {
		int pos = html.indexOf("<div class=\"paper paper_bg\">");
		if (pos == -1) {
			return null;
		}
		String temp = html.substring(pos);

		temp = temp.substring(0, temp.indexOf("<div class=\"tbl_btn\">") - 1);
		return temp;
	}

	public String download(사건 사건) throws IOException {
		logger.info("사건의 현황조사서 페이지를 다운로드합니다");
		File path = new File("download/" + 사건.getPath());
		if (path.exists() == false) {
			path.mkdirs();
		}
		File f = new File(path, "CurrentStatus");
		String html = fetch(사건);
		String text = parse(html);
		if (text == null) {
			logger.info("사건의 현황조사서가 없는 물건입니다.");
			return null;
		}
		NFile.write(f, text, "euc-kr");
		FileUploaderBG.getInstance().upload(사건.getPath(), "CurrentStatus", f);
		return html;
	}

	
	
	public String parse부동산의현황(String html) {
		if (html == null || html.contains("<h4>2. 부동산의 현황</h4>") == false) {
			return null;
		}
		String chunk = chunk부동산의현황(html);
		Pattern liPattern = Pattern.compile("<li[^>]*>([^<]*)", Pattern.MULTILINE);
		Matcher m = liPattern.matcher(chunk);
		StringBuffer buf = new StringBuffer();
		while (m.find()) {
			buf.append(m.group(1)).append("\n");
		}
		return buf.toString();
	}

	String chunk부동산의현황(String html) {
		int pos = html.indexOf("<h4>2. 부동산의 현황</h4>");
		String chunk = html.substring(pos, html.indexOf("</div>", pos));
		return chunk;
	}

	public List<점유관계> parse부동산점유관계(String html) {
		if (html == null || html.contains("<h4>1. 부동산의 점유관계</h4>") == false) {
			return null;
		}
		String chunk = html.substring(html.indexOf("<h4>1. 부동산의 점유관계</h4>"));
		if (chunk.indexOf("<table") == -1){
			return null;
		}
		chunk = chunk.substring(chunk.indexOf("<table"));
		chunk = chunk.substring(0, chunk.indexOf("div"));
		
		ArrayList<점유관계> res = new ArrayList<점유관계>();
		
		
		
//		Pattern p = Pattern.compile("소재지</th>\\s*<td>([^<]*)</td>\\s*</tr>\\s*<tr>\\s*<th[^>]*>점유관계</th>\\s*<td>([^<]+)</td>\\s*</tr>\\s*<tr>\\s*<th[^>]*>기타</th>\\s*<td>([^<]*)</td>");
		String[] chunks = chunk.split("</table>");
//		Matcher m = p.matcher(html);
		for (String str : chunks) {
			String address = HTMLUtils.findTHAndNextValueAsComplex(str, "소재지");
			if (address == null || "".equals(address)){
				continue;
			}
			String occufy =  HTMLUtils.findTHAndNextValueAsComplex(str, "점유관계");
			String etc = HTMLUtils.findTHAndNextValueAsComplex(str, "기타");
			점유관계 item = new 점유관계();
			item.set소재지(address.replaceAll("^\\d+\\.", "").trim());
			item.set점유관계(occufy.trim());
			item.set기타(etc.trim());
			res.add(item);
			
		}
		return res;
	}
}
