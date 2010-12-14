package net.narusas.si.auction.fetchers;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.model.매각물건명세서;

public class 사건임대차관계조사서Parser {

	public Map<Integer, Collection<매각물건명세서>> parse(String html) {
		Map<Integer, Collection<매각물건명세서>> data = new HashMap<Integer, Collection<매각물건명세서>>();
		int pos = 0;
		String 물건번호 = null, 소재지 = null;
		while (true) {
			pos = html.indexOf("<caption>임차 목적물의 용도 및 임대차 계약등의 내용</caption>", pos);
			if (pos == -1) {
				break;
			}
			int start = pos;
			int end = html.indexOf("</table>", start);
			pos = end;

			String chunk = html.substring(start, end);
			Pattern p = Pattern.compile("\\[소재지\\] (\\d+)\\.\\s+([^<]+)", Pattern.MULTILINE);
			Matcher m = p.matcher(chunk);

			if (m.find()) {
				물건번호 = m.group(1);
				소재지 = m.group(2);
			}

			List<매각물건명세서> list = parseChild(chunk, 물건번호, 소재지);
			data.put(Integer.parseInt(물건번호), list);
			// System.out.println(item);

		}
		return data;
	}

	private List<매각물건명세서> parseChild(String src, String 물건번호, String 소재지) {
		int index = 0;
		List<매각물건명세서> res = new LinkedList<매각물건명세서>();
		while (true) {
			index = src.indexOf("<th width=\"15%\" class=\"txtcenter\">점유인</th>", index);
			if (index == -1) {
				break;
			}

			String chunk = src.substring(index);
			index += 6;
			매각물건명세서 item = new 매각물건명세서();
			item.set물건번호(물건번호);
			item.set소재지(소재지);

			item.set점유인(HTMLUtils.findTHAndNextValue(chunk, "점유인"));
			item.set당사자구분(HTMLUtils.findTHAndNextValue(chunk, "당사자구분"));
			item.set점유부분(HTMLUtils.findTHAndNextValue(chunk, "점유부분"));
			item.set점유기간(HTMLUtils.findTHAndNextValue(chunk, "점유기간"));
			item.set보증금(HTMLUtils.findTHAndNextValue(chunk, "보증\\(전세\\)금"));
			item.set차임(HTMLUtils.findTHAndNextValue(chunk, "차임"));
			item.set전입일자(HTMLUtils.findTHAndNextValue(chunk, "전입일자"));
			item.set확정일자(HTMLUtils.findTHAndNextValue(chunk, "확정일자"));
			res.add(item);
		}
		return res;

	}

}
