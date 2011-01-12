package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.fetchers.물건매각물건명세서Fetcher;
import net.narusas.si.auction.model.매각물건명세서;
import net.narusas.util.lang.NFile;

import org.junit.Test;

public class FetcherTest {
	@Test
	public void add() throws IOException {
		물건매각물건명세서Fetcher f = new 물건매각물건명세서Fetcher();
		String html = NFile.getText(new File("fixture2/079_매각물건명세서점유부분복수개.html"), "euc-kr");
		List<매각물건명세서> items = new LinkedList<매각물건명세서>();
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
		System.out.println(items);

		// List<매각물건명세서> list = f.parse(html);
		// for (매각물건명세서 매각물건명세서 : list) {
		// System.out.println(매각물건명세서);
		// }
	}

}
