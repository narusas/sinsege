package net.narusas.si.auction.fetchers;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.narusas.si.auction.converters.금액Converter;
import net.narusas.si.auction.model.기일;
import net.narusas.si.auction.model.목록;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.사건;

/**
 * 기일내역 페이지 자체는 사건페이지에 속하지만 정보는 물건의 정보임.
 * 
 * @author narusas
 * 
 */
public class 사건기일내역Fetcher {
	final Logger logger = LoggerFactory.getLogger("auction");

	public String fetch(사건 event) throws IOException {
		String query = MessageFormat.format("/RetrieveRealEstSaDetailInqGiilList.laf"
				+ "?jiwonNm={0}&saNo={1}&srnID=PNO102018&_SRCH_SRNID=PNO102005", //

				HTMLUtils.encodeUrl(event.get법원().get법원명()),//
				String.valueOf(event.get사건번호()));
		return 대법원Fetcher.getInstance().fetch(query);
	}

	public void parse(사건 event, String html, List<물건> fetchedGoodsList) {
		System.out.println("parse :" + event);
		Sheet sheet = Sheet.parse(html, "<caption>기일내역</caption>", true, true);
		물건 goods = null;

		List<물건> items = new LinkedList<물건>();
		for (int i = 0; i < sheet.rowSize(); i++) {
			System.out.println("###:" + sheet.rowColumnSize(i) + "=" + sheet.valueAt(i, sheet.rowColumnSize(i) - 1));
			// 물건번호, 감정평가액이 있는 줄이 column size가 7.
			if (sheet.rowColumnSize(i) == 7) {
				goods = init물건(event, sheet, i);
				if (goods == null) {
					continue;
				}
				items.add(goods);
				String 기일결과 = sheet.valueAt(i, 6);
				if (isIgnorable(기일결과)) {
					goods = null;
				}
			} else if (sheet.rowColumnSize(i) == 5) {
				if (goods == null) {
					continue;
				}

				if (add기일(sheet, goods, i) == false) {
					goods = null;
				}
			}
		}

		for (물건 물건 : items) {
			if (fetchedGoodsList != null) {
				물건 fetched물건 = findInFetchedGoodsList(물건, fetchedGoodsList);
				if (fetched물건 != null) {
					ArrayList<목록> 목록List = fetched물건.get목록s(); 
					if (목록List != null && 목록List.size() > 0) {
						목록 목록 = 목록List.get(0);
						if ("미종국".equals(목록.getComment())) {
							logger.info("물건 :" + 물건.get물건번호() + " 는 물건 비고가 미종국 사건입니다. 기일 내역을 읽습니다. ");
							handleResult(물건);
						} else {
							logger.info("물건 :" + 물건.get물건번호() + " 는 물건 비고가  " + 목록.getComment() + " 인 사건입니다. 기일 결과로 물건 비고 정보를 사용합니다. ");
							물건.set기일결과(목록.getComment());
						}
					}
				}
				else {
					handleResult(물건);
				}
			}

			else {
				handleResult(물건);
			}
		}

	}

	private 물건 findInFetchedGoodsList(물건 물건, List<물건> fetchedGoodsList) {
		if (fetchedGoodsList == null) {
			return null;
		}
		for (물건 fetchedGoods : fetchedGoodsList) {
			if (물건.get물건번호().equals(fetchedGoods.get물건번호())) {
				return fetchedGoods;
			}
		}
		return null;
	}

	boolean isEmpty(String src) {
		return src == null || "".equals(src.trim());
	}

	boolean equals(String src, String target) {
		return target != null && target.contains(src);
	}

	/**
	 * <pre>
	 * 기일내역처리 방법(돌릴때 마다 항상 덮어 씌어야한다는것)
	 *   
	 *    1. 마지막줄에 '최저매각가격'란이 빈칸이고 and 기일결과란도 빈칸이면 그 줄은 무시한다. 아예 없는줄이라 생각한다.
	 *    
	 *    2. 마지막줄에 최저매각가격이 빈칸이 아니고 기일결과란이 빈칸이면..
	 *       그 바로 위줄에 기일결과값을 분석해야함..
	 *      - 분석해야할 위줄이 없다면 ac_goods의 appoint_result은 &quot;신건&quot;(아예 무시한 줄빼고 1줄밖에 없는경우)
	 *      - 분석해야할 위줄이 있고 기일결과란이 유찰이면  ac_goods의 appoint_result도 &quot;유찰&quot;(아예 무시한 줄빼고 2줄이상인경우)
	 *      - 분석해야할 위줄이 있고 기일결과란이 '변경,미납,최고가매각불허가 등...' 유찰이 아니면 ac_goods의 appoint_result는 &quot;재진행&quot;(아예 무시한 줄빼고 2줄이상인경우)
	 *                    
	 *    3. 마지막줄에 최저매각가격란이 빈칸이 아니고 기일결과란도 빈칸이 아니면..
	 *      - 기일결과가 '매각,유찰,변경,정지,기한후납부,미납 등...&quot;이면  ac_goods의 appoint_result에 있는 그대로'매각,유찰,변경,정지,기한후납부,미납 등...'들어가면 됨..
	 *      (단 기일결과가 '매각'이면 기일결과란에 매각밑에 매각가격이 나오는데 그 매각가격을 ac_goods의 last_sell_price에 집어 넣어줘야함..)    
	 *  
	 *    4. 마지막줄에 최저매각가격란이 빈칸이고  기일결과란이 빈칸이 아니면..
	 *      - 기일결과가 '매각,유찰,변경,정지,기한후납부,미납 등...&quot;이면  ac_goods의 appoint_result에 있는 그대로'매각,유찰,변경,정지,기한후납부,미납 등...'들어가면 됨..
	 *    
	 *    5. 기일내역 페이쥐가 없는경우
	 *      - 처리방법 그림파일 참조
	 *  
	 * 그리고  위에 경우에 수 따지지 않고 항상 똑같게 적용되는것들..
	 *    유찰수appoint_count - 기일내역에서 유찰수가 몇개인지 분석해서 ac_goods의 appoint_count에 적용
	 *    최저가lowest_price - 최저매각가격이 빈칸이 아닌 마지막줄에 최저매각가격이 ac_goods의 lowest_price에 적용되어야함.
	 *    매각기일appoint_result_date - 최저매각가격이 빈칸이 아닌 마지막줄에 기일이 ac_goods의 appoint_result_date에 적용되어야함.
	 * </pre>
	 * 
	 * @param 물건
	 */
	private void handleResult(물건 물건) {
		Collection<기일> items = 물건.get기일내역().get기일목록();
		List<기일> workset = new LinkedList<기일>(items);
		기일 마지막줄 = workset.get(workset.size() - 1);

		// 1. 마지막줄에 '최저매각가격'란이 빈칸이고 and 기일결과란도 빈칸이면 그 줄은 무시한다. 아예 없는줄이라 생각한다.
		if (isEmpty(마지막줄.get최저매각가격()) && isEmpty(마지막줄.get기일결과())) {
			workset.remove(마지막줄);
		}

		// 최저가lowest_price - 최저매각가격이 빈칸이 아닌 마지막줄에 최저매각가격이 ac_goods의
		// lowest_price에 적용되어야함.
		// 매각기일appoint_result_date - 최저매각가격이 빈칸이 아닌 마지막줄에 기일이 ac_goods의
		// appoint_result_date에 적용되어야함.
		for (int i = workset.size() - 1; i >= 0; i--) {
			기일 줄 = workset.get(i);
			if (isEmpty(줄.get최저매각가격()) == false) {
				물건.set기일날자(convertDotToDash(줄.get기일()));
				물건.set최저가(parse금액(줄.get최저매각가격()));
				break;
			}
		}

		마지막줄 = workset.get(workset.size() - 1);

		// 2. 마지막줄에 최저매각가격이 빈칸이 아니고 기일결과란이 빈칸이면..
		if (isEmpty(마지막줄.get최저매각가격()) == false && isEmpty(마지막줄.get기일결과())) {
			// - 분석해야할 위줄이 없다면 ac_goods의 appoint_result은 "신건"(아예 무시한 줄빼고 1줄밖에
			// 없는경우)
			if (workset.size() == 1) {
				물건.set기일결과("신건");
			}

			// (아예 무시한 줄빼고 2줄이상인경우)
			else if (workset.size() >= 2) {

				// 그 바로 위줄에 기일결과값을 분석해야함..
				기일 마지막전줄 = workset.get(workset.size() - 2);

				// - 분석해야할 위줄이 있고 기일결과란이 유찰이면 ac_goods의 appoint_result도 "유찰"(아예
				// 무시한 줄빼고 2줄이상인경우)
				if (equals("유찰", 마지막전줄.get기일결과())) {
					물건.set기일결과("유찰");
				}
				// - 분석해야할 위줄이 있고 기일결과란이 '변경,미납,최고가매각불허가 등...' 유찰이 아니면 ac_goods의
				// appoint_result는 "재진행"(아예 무시한 줄빼고 2줄이상인경우)
				else {
					물건.set기일결과("재진행");
				}

			}
		}

		// 3. 마지막줄에 최저매각가격란이 빈칸이 아니고 기일결과란도 빈칸이 아니면..
		else if (isEmpty(마지막줄.get최저매각가격()) == false && isEmpty(마지막줄.get기일결과()) == false) {
			// - 기일결과가 '매각,유찰,변경,정지,기한후납부,미납 등..."이면 ac_goods의 appoint_result에
			// 있는 그대로'매각,유찰,변경,정지,기한후납부,미납 등...'들어가면 됨..
			물건.set기일결과(strip(마지막줄.get기일결과()));

			// (단 기일결과가 '매각'이면 기일결과란에 매각밑에 매각가격이 나오는데 그 매각가격을 ac_goods의
			// last_sell_price에 집어 넣어줘야함..)
			if (equals("매각", 마지막줄.get기일결과())) {
				물건.set매각가격(String.valueOf(parse금액(마지막줄.get기일결과())));
			}
		}

		// 4. 마지막줄에 최저매각가격란이 빈칸이고 기일결과란이 빈칸이 아니면..
		else if (isEmpty(마지막줄.get최저매각가격()) && isEmpty(마지막줄.get기일결과()) == false) {
			// - 기일결과가 '매각,유찰,변경,정지,기한후납부,미납 등..."이면 ac_goods의 appoint_result에
			// 있는 그대로'매각,유찰,변경,정지,기한후납부,미납 등...'들어가면 됨..
			물건.set기일결과(마지막줄.get기일결과());
		}

		물건.set기일Start(마지막줄.get기간start());
		물건.set기일End(마지막줄.get기간end());

		// 유찰수appoint_count - 기일내역에서 유찰수가 몇개인지 분석해서 ac_goods의 appoint_count에 적용
		int 유찰회수 = 0;
		for (기일 기일 : workset) {
			if (equals("유찰", 기일.get기일결과())) {
				유찰회수++;
			}
		}
		물건.set유찰수(유찰회수);

	}

	private String convertDotToDash(String date) {
		if (date != null) {
			return date.replaceAll("\\.", "-");
		}
		return null;
	}

	private String strip(String 기일결과) {
		if (기일결과.contains("(")) {
			기일결과 = 기일결과.substring(0, 기일결과.indexOf("("));
		}
		return 기일결과.trim();
	}

	private Long parse금액(String get기일결과) {
		Pattern p = Pattern.compile("([\\d,]+)");
		Matcher m = p.matcher(get기일결과);
		if (m.find()) {
			return Long.parseLong(m.group(1).replaceAll(",", ""));
		}
		return null;
	}

	private 물건 init물건(사건 event, Sheet sheet, int row) {
		물건 goods;
		String 기일번호Raw = sheet.valueAt(row, 0);

		if (기일번호Raw == null || "".equals(기일번호Raw.trim())) {
			return null;
		}

		Pattern noP = Pattern.compile("(\\d+)");
		Matcher m = noP.matcher(기일번호Raw);
		if (m.find() == false) {
			return null;
		}

		goods = event.get물건By물건번호(Integer.parseInt(m.group(1)));
		if (goods == null) {
			return null;
		}
		goods.set감정평가액(금액Converter.convert(sheet.valueAt(row, 1)));

		String 기일Raw = sheet.valueAt(row, 2);
		String 기일 = extractDate(기일Raw);
		String 기간start = null, 기간end = null;
		if (기일Raw.contains("~")) {
			Pattern scopePattern = Pattern.compile("(\\d\\d\\d\\d\\.\\d+\\.\\d+)\\s*~\\s*(\\d\\d\\d\\d\\.\\d+\\.\\d+)", Pattern.MULTILINE);
			m = scopePattern.matcher(기일Raw);

			if (m.find()) {
				기간start = m.group(1);
				기간end = m.group(2);
			}
		}
		String 기일종류 = sheet.valueAt(row, 3);
		String 기일장소 = sheet.valueAt(row, 4);
		String 최저매각가격 = 금액Converter.convert(sheet.valueAt(row, 5));
		String 기일결과 = sheet.valueAt(row, 6);
		System.out.println(기일결과);

		goods.add기일내역(new 기일(기일, 기일종류, 기일장소, 최저매각가격, 기일결과, 기간start, 기간end));
		return goods;
	}

	private boolean add기일(Sheet sheet, 물건 goods, int row) {
		String 기일Raw = sheet.valueAt(row, 0);
		String 기일 = extractDate(기일Raw);
		String 기간start = null, 기간end = null;
		if (기일Raw.contains("~")) {
			Pattern scopePattern = Pattern.compile("(\\d\\d\\d\\d\\.\\d+\\.\\d+)\\s*~\\s*(\\d\\d\\d\\d\\.\\d+\\.\\d+)", Pattern.MULTILINE);
			Matcher m = scopePattern.matcher(기일Raw);

			if (m.find()) {
				기간start = m.group(1);
				기간end = m.group(2);
			}
		}

		String 기일종류 = sheet.valueAt(row, 1);
		String 기일장소 = sheet.valueAt(row, 2);
		String 최저매각가격 = 금액Converter.convert(sheet.valueAt(row, 3));
		String 기일결과 = sheet.valueAt(row, 4);
		goods.add기일내역(new 기일(기일, 기일종류, 기일장소, 최저매각가격, 기일결과, 기간start, 기간end));
		if (isIgnorable(기일결과)) {
			return false;
		}

		return true;
	}

	private boolean isIgnorable(String 기일결과) {
		return 기일결과.contains("매각") && 기일결과.contains("(") && 기일결과.contains("원") && 기일결과.contains(")");
	}

	private String extractDate(String str) {
		Pattern datePattern = Pattern.compile("(\\d\\d\\d\\d\\.\\d+\\.\\d+)", Pattern.MULTILINE);
		Matcher m = datePattern.matcher(str);
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}

	public void update(사건 사건, List<물건> fetchedGoodsList) throws IOException {
		parse(사건, fetch(사건), fetchedGoodsList);
	}

}
