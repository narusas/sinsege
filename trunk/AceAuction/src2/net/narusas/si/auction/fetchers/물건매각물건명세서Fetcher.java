package net.narusas.si.auction.fetchers;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.model.매각물건명세서;
import net.narusas.si.auction.model.매각물건명세서비고;
import net.narusas.si.auction.model.물건;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 물건매각물건명세서Fetcher {
	final Logger logger = LoggerFactory.getLogger("auction");

	public String fetch(물건 물건) throws IOException {
		String query = MessageFormat.format("/RetrieveRealEstMgakMulMseo.laf" //
				+ "?jiwonNm={0}&saNo={1}&maemulSer={2}",//
				HTMLUtils.encodeUrl(물건.get사건().get법원().get법원명()), //
				String.valueOf(물건.get사건().get사건번호()), //
				물건.get물건번호());

		return 대법원Fetcher.getInstance().fetch(query);
	}
	
	public void update최선순위설정일자(물건  goods, String html){
		int pos = html.indexOf("<table class=\"Ltbl_dt\" summary=\"매각물건명세서 기본정보 표\">");
		if (pos == -1){
			return;
		}
		String tmp = html.substring(pos);
		String  최선순위설정일자 = HTMLUtils.findTHAndNextValue(tmp, "최선순위 설정일자");
		goods.set최선순위설정일자(최선순위설정일자);
	}

	public List<매각물건명세서> parse(String html) {
		List<매각물건명세서> items = new LinkedList<매각물건명세서>();
		if (html.contains("매각물건명세서가 없습니다") || html.contains("조사된 임차내역 없음")) {
			return items;
		}
		
		
		
		html = html.substring(html.indexOf("summary=\"매각물건명세서 상세표\""));
		html = html.substring(html.indexOf("<tbody>") + 7);
		html = html.replaceAll("<br/>", "\n");
		html = html.replaceAll("<br />", "\n");
		Pattern namePattern = Pattern.compile(//
				"<td rowspan=\"(\\d+)[^>]+>([^<]*)</td>\\s+" //
						+ "<td>([^<]*)</td>\\s+" //
						+ "<td>([^<]*)</td>\\s+" //
						+ "<td>([^<]*)</td>\\s+" //
						+ "<td>([^<]*)</td>\\s+"//
						+ "<td>([^<]*)</td>\\s+" //
						+ "<td>([^<]*)</td>\\s+" //
						+ "<td>([^<]*)</td>\\s+" //
						+ "<td>([^<]*)</td>\\s+"//
						+ "<td>([^<]*)</td>\\s+" //
						+ "</tr>", Pattern.MULTILINE);

		Pattern itemPattern = Pattern.compile("<td>([^<]*)</td>\\s+<td>([^<]*)</td>\\s+"
				+ "<td>([^<]*)</td>\\s+<td>([^<]*)</td>\\s+" + "<td>([^<]*)</td>\\s+<td>([^<]*)</td>\\s+"
				+ "<td>([^<]*)</td>\\s+<td>([^<]*)</td>\\s+" + "<td>([^<]*)</td>\\s+</tr>", Pattern.MULTILINE);

		Matcher m = namePattern.matcher(html);
//		System.out.println(html);
		while (m.find()) {
			매각물건명세서 item = new 매각물건명세서();
			int rowSpan = Integer.parseInt(m.group(1));
			{
				String 점유인 = m.group(2);
				String 점유부분 = m.group(3);
				String 출처 = m.group(4);

				String 당사자구분 = m.group(5);
				String 점유기간 = m.group(6);

				String 보증금 = m.group(7);
				String 차임 = m.group(8);
				String 전입일자 = m.group(9);
				String 확정일자 = m.group(10);
				String 배당요구 = m.group(11);

				item.set점유인(점유인);
				item.set당사자구분(당사자구분);
				item.set점유부분(점유부분);
				item.set점유기간(점유기간);
				item.set보증금(보증금);
				item.set차임(차임);
				item.set전입일자(전입일자);
				item.set확정일자(확정일자);
				item.set배당요구(배당요구);
				item.set정보출처(출처);
			}
			if (rowSpan > 1) {
				String chunk = html.substring(m.end());
				for (int i = 2; i <= rowSpan; i++) {
					Matcher m2 = itemPattern.matcher(chunk);

					if (m2.find()) {
						// System.out.println(m2.group(0));
						String 점유부분 = m2.group(1);
						String 출처 = m2.group(2);

						String 당사자구분 = m2.group(3);
						String 점유기간 = m2.group(4);

						String 보증금 = m2.group(5);
						String 차임 = m2.group(6);
						String 전입일자 = m2.group(7);
						String 확정일자 = m2.group(8);
						String 배당요구 = m2.group(9);
						item.set당사자구분(당사자구분);
						item.set점유부분(점유부분);
						item.set점유기간(점유기간);
						item.set보증금(보증금);
						item.set차임(차임);
						item.set전입일자(전입일자);
						item.set확정일자(확정일자);
						item.set배당요구(배당요구);
						item.set정보출처(출처);
					}
					chunk = html.substring(m2.end());

				}
				
			}
			items.add(item);
		}

		return items;

	}

	private String onlyDateString(String group) {
		Pattern datePattern = Pattern.compile("(\\d+\\.\\d+\\.\\d+)");
		return null;
	}

	public 매각물건명세서비고 parse비고(String html) {
		매각물건명세서비고 comment = new 매각물건명세서비고();
		//                            &lt; 비고 &gt; &nbsp;<br />경복임(주민등록등재자) : 임차인 경복임은 임차인 박미라의 어머니임 
		Matcher m2 = Pattern.compile("&lt; 비고 &gt; &nbsp;<br\\s*/>([^<]*)", Pattern.MULTILINE).matcher(html);
		if (m2.find()) {
			comment.set비고(m2.group(1));
		}

		m2 = Pattern.compile("<td>[^<]*효력이 소멸되지 아니하는 것[^<]*</td>\\s+</tr>\\s+<tr>\\s+<td [^>]+>([^<]*)",
				Pattern.MULTILINE).matcher(html);
		if (m2.find()) {
			comment.set비소멸권리(m2.group(1));
		}

		m2 = Pattern.compile("<td>[^<]*지상권의 개요[^<]*</td>\\s+</tr>\\s+<tr>\\s+<td [^>]+>([^<]*)", Pattern.MULTILINE)
				.matcher(html);
		if (m2.find()) {
			comment.set지상권개요(m2.group(1));
		}

		m2 = Pattern.compile("<td>[^<]*비고란[^<]*</td>\\s+</tr>\\s+<tr>\\s+<td [^>]+>([^<]*)", Pattern.MULTILINE)
				.matcher(html);
		if (m2.find()) {
			comment.set비고란(m2.group(1));
		}
		return comment;
	}

}
