package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.model.Table;
import net.narusas.aceauction.model.스피드옥션물건상세내역;

import org.apache.commons.httpclient.HttpException;

public class 스피드옥션물건상세내용Fetcher extends 스피드옥션Fetcher {

	static boolean debug = false;

	static Logger logger = Logger.getLogger("log");

	static Pattern tagPattern = Pattern.compile("(<[^>]*>)");

	static Pattern 감정시점Pattern = Pattern.compile("감정시점 : (\\d+-\\d+-\\d+)");

	static Pattern 경매개시결정Pattern = Pattern.compile("경매개시결정 : ([^<]+)");

	static Pattern 배당요구종기Pattern = Pattern.compile("배당요구종기 : ([^<]+)");

	static Pattern 보존등기일Pattern = Pattern.compile("보존등기일:(\\d+-\\d+-\\d+)");

	static Pattern 사건접수Pattern = Pattern.compile("사건접수\\s+([^<^\\s]+)");
	static Pattern 입찰방법Pattern = Pattern.compile("입찰방법 : ([^<]+)");

	public 스피드옥션물건상세내용Fetcher() throws HttpException, IOException {
		super();

	}

	public String getPage(String court, String 담당계, int eventYear,
			int eventNo, String no) throws IOException {
		logger.info("스프드옥션 상세 정보 페이지 가져오기를 시도합니다");
		logger.info("/v2/info-result.htm?courtNo=" + court + "&courtNo2=" + 담당계
				+ "&eventNo1=" + eventYear + "&eventNo2=" + eventNo + "&objNo="
				+ no);

		String res = fetch("/v2/info-result.htm?courtNo=" + court
				+ "&courtNo2=" + 담당계 + "&eventNo1=" + eventYear + "&eventNo2="
				+ eventNo + "&objNo=" + no);
		System.out.println(res);
		logger.info("스프드옥션 상세 정보 페이지 가져오기에 성공했습니다.");

		return res;
	}

	public 스피드옥션물건상세내역 parse(String src) {
		// System.out.println(src);
		logger.info("스프드옥션 상세 정보 페이지의 분석을 시작합니다.");

		String 물건종별 = TableParser.getNextTDValueStripped(src, "물건종별");
		String 채권자 = TableParser.getNextTDValueStripped(src, "채 권 자");
		String 채무자 = TableParser.getNextTDValueStripped(src, "채 무 자");
		String 소유자 = TableParser.getNextTDValueStripped(src, "소 유 자");

		String 토지면적 = TableParser.getNextTDValueStripped(src, "토지면적");
		String 대지권 = TableParser.getNextTDValueStripped(src,
				"<font color=366AB3>대지권");
		String 건물면적 = TableParser.getNextTDValueStripped(src, "건물면적");
		String 전용면적 = TableParser.getNextTDValueStripped(src, "전용면적");
		String 제시외면적 = TableParser.getNextTDValueStripped(src, "제시외면적");

		String 감정가 = TableParser.getNextTDValueStripped(src, "감 정 가");
		String 최저가 = TableParser.getNextTDValueStripped(src, "최 저 가");
		String 보증금 = TableParser.getNextTDValueStripped(src, "보 증 금");

		String 매각대상 = TableParser.getNextTDValueStripped(src, "매각대상");
		String 청구금액 = TableParser.getNextTDValueStripped(src, "청구금액");
		String 감정평가현황 = TableParser.getNextTDValueStripped(src, "감정평가현황")
				.replace('◈', ' ').trim();

		logger.info("기본 정보의 분석을 완료했습니다. ");

		String 감정시점 = re(src, 감정시점Pattern);
		String 입찰방법 = re(src, 입찰방법Pattern);
		String 배당요구종기 = re(src, 배당요구종기Pattern);
		String 경매개시결정 = re(src, 경매개시결정Pattern);
		String 사건접수 = re(src, 사건접수Pattern);
		String 보존등기일 = re(src, 보존등기일Pattern);

		logger.info("날자정보의 분석을 완료했습니다. ");

		int pos = src.indexOf("<strong>합계</strong>");
		String 감정평가_토지 = null;
		String 감정평가_건물 = null;
		String 감정평가_제시외건물포함 = null;
		String 감정평가_제시외건물미포함 = null;
		String 감정평가_기타 = null;
		String 감정평가_합계 = null;
		String 감정평가_비고 = null;
		if (pos != -1) {
			TableParser.TDValue res = TableParser.getNextTDValueByPos(src, pos);
			감정평가_토지 = res.text;

			res = TableParser.getNextTDValueByPos(src, res.pos);
			감정평가_건물 = res.text;

			res = TableParser.getNextTDValueByPos(src, res.pos);
			감정평가_제시외건물포함 = res.text;

			res = TableParser.getNextTDValueByPos(src, res.pos);
			감정평가_제시외건물미포함 = res.text;

			res = TableParser.getNextTDValueByPos(src, res.pos);
			감정평가_기타 = res.text;

			res = TableParser.getNextTDValueByPos(src, res.pos);
			감정평가_합계 = res.text;

			res = TableParser.getNextTDValueByPos(src, res.pos);
			res = TableParser.getNextTDValueByPos(src, res.pos);
			감정평가_비고 = res.text;

			logger.info("감정평가 정보의 분석을 완료했습니다. ");

		}

		스피드옥션물건상세내역 detail = new 스피드옥션물건상세내역();
		detail.set물건종별(물건종별);
		detail.set채권자(채권자);
		detail.set채무자(채무자);
		detail.set소유자(소유자);
		detail.set토지면적(토지면적);
		detail.set토지면적(토지면적);
		detail.set대지권(대지권);
		detail.set건물면적(건물면적);
		detail.set전용면적(전용면적);
		detail.set제시외면적(제시외면적);

		detail.set입찰방법(입찰방법);
		detail.set배당요구종기(배당요구종기);
		detail.set경매개시결정(경매개시결정);

		detail.set사건접수(사건접수);

		detail.set매각대상(매각대상);

		detail.set감정가(감정가);
		detail.set최저가(최저가);
		detail.set보증금(보증금);
		detail.set청구금액(청구금액);

		detail.set감정평가현황(감정평가현황);
		detail.set감정시점(감정시점);
		detail.set감정평가_토지(감정평가_토지);
		detail.set감정평가_건물(감정평가_건물);
		detail.set감정평가_제시외건물포함(감정평가_제시외건물포함);
		detail.set감정평가_제시외건물미포함(감정평가_제시외건물미포함);
		detail.set감정평가_기타(감정평가_기타);
		detail.set감정평가_기계기구(감정평가_기타);
		detail.set감정평가_합계(감정평가_합계);
		detail.set감정평가_비고(감정평가_비고);

		detail.set보존등기일(보존등기일);

		int startPos = src.indexOf("<font color=#000000> 건물현황</font>");
		if (startPos == -1) {
			startPos = src.indexOf("<font color=#000000> <b>건물현황</font>");
		}

		logger.info("건물현황 정보의 존재여부 :" + (startPos >= 0));
		if (startPos != -1) {
			startPos = src.indexOf("<TABLE cellSpacing=0 ", startPos);
			int endPos = src.indexOf("</table", startPos);
			// HashMap<String, List<String>> map = TableParser.parseTable(src,
			// startPos, endPos, 1);
			Table map = TableParser.parseTable(src, startPos, endPos);
			detail.set건물현황(map);
			// debug = true;
			logger.info("건물현황 정보의 분석을 완료했습니다. ");

		}

		startPos = src.indexOf("<font color=#000000><b> 대지권현황</font>");
		if (startPos != -1) {
			int endPos = src.indexOf("</table", startPos);
			endPos = src.indexOf("</table", endPos + 1);
			Table map = TableParser.parseTable(src, startPos, endPos);
			detail.set대지권현황(map);
		}
		logger.info("대지권현황정보의 분석을 완료했습니다. ");

		startPos = src.indexOf("<font color=#000000><b> 토지현황</font>");
		if (startPos != -1) {
			int endPos = src.indexOf("</table", startPos);
			endPos = src.indexOf("</table", endPos + 1);
			Table map = TableParser.parseTable(src, startPos, endPos);
			detail.set토지현황(map);
		}
		logger.info("토지현황 정보의 분석을 완료했습니다. ");

		startPos = src.indexOf("매각기일현황		    </font>");
		if (startPos == -1) {
			startPos = src.indexOf("매각/개찰기일현황    </font>");
		}
		if (startPos != -1) {
			startPos = src.indexOf("<table", startPos);
			int endPos = src.indexOf("</table", startPos);
			Table map = TableParser.parseTable(src, startPos, endPos);
			// System.out.println(map.getRecords());
			detail.set매각기일현황(map);
		}
		logger.info("매각기일 정보의 분석을 완료했습니다. ");

		startPos = src.indexOf("<font color=#000000><b> 제시외건물현황</font>");
		if (startPos != -1) {
			startPos = src.indexOf("<TR", startPos);
			int endPos = src.indexOf("</table", startPos);
			Table map = TableParser.parseTable(src, startPos, endPos);
			detail.set제시외건물현황(map);
		}
		logger.info("제시외건물현황의 분석을 완료했습니다. ");

		startPos = src.indexOf("<font color=366AB3>기계기구</font>");
		if (startPos != -1) {
			startPos = src.indexOf("<TR", startPos);
			int endPos = src.indexOf("</TABLE", startPos);
			Table map = TableParser.parseTable(src, startPos, endPos);
			detail.set기계기구(map);
		}
		logger.info("기계기구의 분석을 완료했습니다. ");

		logger.info("분석을 완료했습니다. ");

		return detail;
	}

	private String re(String src, Pattern p) {
		Matcher m;
		String 배당요구종기;
		m = p.matcher(src);
		String temp = null;
		if (m.find()) {
			temp = m.group(1);
		}
		배당요구종기 = temp;
		return 배당요구종기;
	}

}
