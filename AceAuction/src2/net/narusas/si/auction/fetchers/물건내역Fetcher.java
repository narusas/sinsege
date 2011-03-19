package net.narusas.si.auction.fetchers;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.converters.금액Converter;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.물건인근매각통계Item;
import net.narusas.si.auction.model.사건종류;
import net.narusas.si.auction.model.주소;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 물건내역Fetcher {
	final Logger logger = LoggerFactory.getLogger("auction");

	public boolean update(물건 물건) throws IOException {
		return parse(fetch(물건), 물건);
	}

	String fetch(물건 물건) throws IOException {

		String query = MessageFormat.format("/RetrieveRealEstCarHvyMachineMulDetailInfo.laf" //
				+ "?jiwonNm={0}&saNo={1}&srnID=PNO102018&maemulSer={2}" //
		, HTMLUtils.encodeUrl(물건.get법원().get법원명()), //
				String.valueOf(물건.get사건().get사건번호()), //
				물건.get물건번호());
		Exception e = null;
		for(int i=0;i<3;i++){
			try {
				return 대법원Fetcher.getInstance().fetch(query);
			} catch (Exception e1) {
				e = e1;
			}	
		}
		throw new IOException(e);
		

	}

	public boolean parse(String html, 물건 goods) {

		if (fillBasicInfo(html, goods) == false) {
			return false;
		}
		fill소재지(html, goods);
		fill표시목록(html, goods);
		fill감정평가요항(html, goods);
		fill인근매각표(html, goods);
		return true;
	}

	private void fill인근매각표(String html, 물건 goods) {
		if (html.contains("인근매각통계 표") == false) {
			return;
		}
		String chunk = html.substring(html.indexOf("<table class=\"Ltbl_list\" summary=\"인근매각통계 표\">"));
		Sheet sheet = Sheet.parse(chunk, "인근매각통계");

		for (int i = 0; i < sheet.rowSize(); i++) {
			물건인근매각통계Item item = new 물건인근매각통계Item(goods, sheet, i);
			logger.info(item.toString());
			goods.add인근매각통계(item);
		}
	}

	private void fill감정평가요항(String html, 물건 goods) {
		// if (goods.get사건().get감정평가요항RawText() != null &&
		// "".equals(goods.get사건().get감정평가요항RawText().trim()) == false) {
		// return;
		// }

		Pattern p = Pattern.compile(
				"<li><p class=\"law_title\">(\\d+\\)\\s*[^<]+)</p>\\s*<ul>\\s*<li>\\s*<span[^>]+>(.*)</span>",
				Pattern.MULTILINE);
		Matcher m = p.matcher(html);
		StringBuffer buf = new StringBuffer();
		while (m.find()) {
			buf.append(m.group(1) + "=" + m.group(2) + "\n\n");
		}

		goods.get사건().set감정평가요항RawText(buf.toString());

	}

	private void fill표시목록(String html, 물건 goods) {
		Sheet sheet = Sheet.parse(html, "<caption>목록내역</caption>", true, true);

		if (goods.get사건().get종류() == 사건종류.부동산) {
			부동산표시목록Builder builder = new 부동산표시목록Builder();
			for (int i = 0; i < sheet.rowSize(); i++) {
				int 목록번호 = Integer.parseInt(sheet.valueAt(i, 0));
				String 목록구분 = sheet.valueAt(i, 1);
				String 상세내역 = sheet.valueAt(i, 2);
				try {
					builder.build(goods, 목록번호, 목록구분, 상세내역);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				builder.update종합(goods);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (goods.get사건().get종류() == 사건종류.자동차 || goods.get사건().get종류() == 사건종류.중장비) {
			자동차표시목록Builder builder = new 자동차표시목록Builder();
			builder.build(html, sheet, goods);

		} else if (goods.get사건().get종류() == 사건종류.선박) {
			선박표시목록Builder builder = new 선박표시목록Builder();
			builder.build(html, sheet, goods);
		}
		// else if (goods.get사건().get종류() == 사건종류.중장비) {
		// 중장비표시목록Builder builder = new 중장비표시목록Builder();
		// builder.build(sheet, goods);
		// }
	}

	private void fill소재지(String html, 물건 goods) {
		Pattern p = Pattern.compile("목록(\\d+)\\s*소재지");
		Matcher m = p.matcher(html);

		while (m.find()) {
			String no = m.group(1);
			String 소재지 = HTMLUtils.strip(HTMLUtils.stripCRLF(HTMLUtils.findTHAndNextValueAsComplex(html, "목록" + no
					+ " 소재지")));
			logger.info("물건의 소재지는 " + 소재지 + " 입니다. ");

			if (소재지 != null && "".equals(소재지.trim()) == false) {
				// 사건이 부동산일때만 주소를 처리하고, 선박,자동차,중장비등일경우 그냥 소재지만 입력하게 함.
				if (goods.get사건().get종류() == 사건종류.부동산) {
					주소 주소 = new 주소Builder().parse(소재지);
					if (goods.get소재지() == null || "".equals(goods.get소재지().trim())) {
						update주소(goods, 주소);
					}
					goods.add부동산표시(Integer.parseInt(no), 주소);
				} else {
					주소 addr = new 주소();
					addr.set시도(goods.get법원().get지역());
					goods.set지역_도(goods.get법원().get지역());
					goods.set소재지(소재지);

					goods.add부동산표시(Integer.parseInt(no), addr);
				}
			}
		}
	}

	private void update주소(물건 goods, 주소 addr) {

		goods.set지역_도(addr.get시도());
		goods.set지역_시군구(addr.get시군구());
		goods.set지역_동읍면(addr.get읍면동());
		goods.set소재지(addr.get소재지());
	}

	private boolean fillBasicInfo(String html, 물건 goods) {
		logger.info("물건의 기본정보를 분석합니다");
		String 비고 = HTMLUtils.strip(HTMLUtils.stripCRLF(HTMLUtils.findTHAndNextValueAsComplex(html, "물건비고")));
		Date 경매개시일 = HTMLUtils.toDate(HTMLUtils.findTHAndNextValue(html, "경매개시일"));
		Date 배당요구종기일 = HTMLUtils.toDate(HTMLUtils.findTHAndNextValue(html, "배당요구종기"));
		Date 사건접수일 = HTMLUtils.toDate(HTMLUtils.findTHAndNextValue(html, "사건접수"));

		String temp1 = 금액Converter.convert(HTMLUtils.findTHAndNextValue(html, "감정평가액"));
		Long 감정평가액 = temp1 == null ? null : Long.parseLong(temp1);

		String 최저매각가격RawText = 금액Converter.validate금액(HTMLUtils.findTHAndNextValueAsComplex(html, "최저매각가격"));

		String temp2 = 금액Converter.convert(최저매각가격RawText);
		Long 최저매각가격 = temp2 == null ? null : Long.parseLong(temp2);

		String temp3 = 금액Converter.convert(HTMLUtils.findTHAndNextValue(html, "청구금액"));
		Long 청구금액 = temp3 == null ? null : Long.parseLong(temp3);

		String 물건종류 = HTMLUtils.findTHAndNextValueAsComplex(html, "물건종류");

		// if (물건종류 != null
		// && (물건종류.contains("자동차") || 물건종류.contains("차량") ||
		// 물건종류.contains("중기") || 물건종류.contains("선박"))
		// ) {

		if (물건종류 == null || "".equals(물건종류.trim())) {
			logger.info("물건 종류가 처리가능한 종류가 아닙니다. " + 물건종류);
			return false;
		}

		Integer no = 물건종별Stage1.parse물건종류(물건종류);
		goods.set물건종별(no);

		String 매각기일 = null, 시간 = null, 장소 = null;
		String temp = HTMLUtils.strip(HTMLUtils.stripCRLF(HTMLUtils.findTHAndNextValue(html, "매각기일")));
		if (temp != null) {
			Pattern p = Pattern.compile("(\\d+\\.\\d+\\.\\d+)\\s+(\\d+:\\d+)\\s+(.+)");
			Matcher m = p.matcher(temp);

			if (m.find()) {
				매각기일 = m.group(1);
				시간 = m.group(2);
				장소 = m.group(3);
			}
		}
		goods.set비고(비고);
		goods.set보증율(parse보증율(비고));
		goods.set경매개시결정일(경매개시일);
		goods.set배당요구종기일(배당요구종기일);
		goods.set사건접수일(사건접수일);
		goods.set감정가(감정평가액);
		goods.set최저가(최저매각가격);
		logger.info("물건종류:" + goods.get물건종별());
		logger.info("비고:" + 비고);
		logger.info("보증율:" + goods.get보증율());

		logger.info("비고:" + 비고);
		logger.info("경매개시일:" + 경매개시일);
		logger.info("배당요구종기일:" + 배당요구종기일);
		logger.info("사건접수일:" + 사건접수일);
		logger.info("감정평가액:" + 감정평가액);
		logger.info("최저매각가격:" + 최저매각가격);
		return true;
	}

	private Integer parse보증율(String 비고) {
		String comment = 비고;
		String garuantee_ratio = "10";
		// 재매각, 보증금, 특별매각
		if (comment != null && (comment.contains("재매각") || comment.contains("보증금") || comment.contains("특별매각") || comment.contains("매수신청보증액"))) {
			Pattern p = Pattern.compile("(\\d+)%");
			Matcher m = p.matcher(comment);
			if (m.find()) {
				garuantee_ratio = m.group(1) + "";
			} else {
				p = Pattern.compile("(\\d+)할");
				m = p.matcher(comment);
				if (m.find()) {
					garuantee_ratio = m.group(1) + "0";
				}
			}
		}
		return Integer.parseInt(garuantee_ratio);
	}

	public String fetch기일결과(물건 물건) throws IOException {
		String html = fetch(물건);
		return HTMLUtils.findTHAndNextValue(html, "종국결과");
	}
}
