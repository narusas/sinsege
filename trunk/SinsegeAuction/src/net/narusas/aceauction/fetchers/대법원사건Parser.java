/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.model.감정평가서;
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.물건;
import net.narusas.aceauction.model.물건현황;
import net.narusas.aceauction.model.법원;
import net.narusas.aceauction.model.사건;
import net.narusas.aceauction.model.현황조사서;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

// TODO: Auto-generated Javadoc
/**
 * 대법원 사이트에서 얻어온 HTML을 분석하여 사건 객체를 생성하는 파서. 내부적으로 HTML Parser를 사용한다.
 * 
 * @author narusas
 */
public class 대법원사건Parser {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The p. */
	static Pattern p = Pattern.compile("\\d\\d:\\d\\d");

	/** The p1. */
	static Pattern p1 = Pattern.compile("(\\d+타경\\d+)");

	/** The 현황조사서 pattern1. */
	static Pattern 현황조사서Pattern1 = Pattern.compile("loadHyun\\( \\'(\\d+)\\'", Pattern.MULTILINE);

	/** The 현황조사서 pattern2. */
	static Pattern 현황조사서Pattern2 = Pattern.compile("\\'([^\\']*)\\'", Pattern.MULTILINE);

	/**
	 * 특정 대상 문자열을 중심으로 문자열을 잘랐기 때문에 정상적인 HTML Table 구조가 깨진 문자열을 정상 테이블을 가지게끔
	 * 수정한다.
	 * 
	 * @param src 대상 HTML을 들고 있는 문자열.
	 * 
	 * @return the string
	 */
	public String fixSlice(String src) {
		String temp = src.substring(src.indexOf("</td>") + 5).trim();
		temp = "<table><tr><td></td><td></td>" + temp;
		return temp;
	}

	/**
	 * Parses the dom.
	 * 
	 * @param src the src
	 * 
	 * @return the node list
	 * 
	 * @throws ParserException the parser exception
	 */
	public NodeList parseDOM(String src) throws ParserException {
		Parser parser = new Parser();
		parser.setFeedback(Parser.STDOUT);
		Parser.getConnectionManager().setMonitor(parser);
		Parser.getConnectionManager().setRedirectionProcessingEnabled(true);
		Parser.getConnectionManager().setCookieProcessingEnabled(true);
		parser.setInputHTML(src);
		NodeList ns = parser.parse(null);
		return ns;
	}

	/**
	 * Parses the page.
	 * 
	 * @param html the html
	 * @param court the court
	 * @param charge the charge
	 * 
	 * @return the list<사건>
	 * 
	 * @throws ParserException the parser exception
	 */
	public List<사건> parsePage(String html, 법원 court, 담당계 charge) throws ParserException {

		List<사건> result = new ArrayList<사건>();
		String[] slices = sliceSagun(html);
		for (int i = 0; i < slices.length; i++) {
			logger.log(Level.FINEST,i + "번째 사건 슬라이스를 처리한다. ");
			사건 s = new 사건(court, charge);
			// 간단하게 분석될수 있는 부분을 처리한다.

			s.set사건번호(parse사건번호(slices[i]));
			s.set병합(parse병합(slices[i], s.get사건번호()));
			s.set현황조사서(parse현황조사서(slices[i]));
			s.set감정평가서(parse감정평가서(slices[i]));

			// 복잡하게 나누어져 있는 부분을 처리한다.
			String src = fixSlice(slices[i]);
			// logger.info(src);
			// System.out.println(src);
			NodeList ns = parseDOM(src);
			parse사건(s, ns);

			// 사건이 부동산 사건이 아니면 무시한다.
			// if (s.get사건명() != null && (s.get사건명().contains("자동차") ||
			// s.get사건명().contains("선박"))) {
			// continue;
			// }
			result.add(s);
		}
		return result;
	}

	/**
	 * 사건번호를 파싱한다.
	 * 
	 * @param src 대상 HTML을 들고 있는 문자열.
	 * 
	 * @return the string
	 */
	public String parseSagunNo(String src) {
		Pattern p = Pattern.compile("\\'\\d+\\', \\'(\\d+)\\' \\);");
		Matcher m = p.matcher(src);
		m.find();
		return m.group(1);
	}

	/**
	 * Parses the td row span.
	 * 
	 * @param tdText the td text
	 * 
	 * @return the int
	 */
	public int parseTDRowSpan(String tdText) {
		if (tdText.contains("rowspan")) {
			Pattern p = Pattern.compile("rowspan=\"(\\d+)\"");
			Matcher m = p.matcher(tdText);
			m.find();
			return Integer.parseInt(m.group(1));
		}
		return 1;
	}

	/**
	 * 사건을 파싱한다. 하나의 사건에는 n개의 물건이 있을수 있으며, 한개의 물건에는 n개의 물건현황이 있을수 있다. HTML
	 * Table의 특성상 첫줄에는 상당히 많은 정보가 몰려 있으며 이를 특별히 처리하였다.
	 * 
	 * @param sagun the sagun
	 * @param ns the ns
	 * 
	 * @return the 사건
	 */
	public 사건 parse사건(사건 sagun, NodeList ns) {
		Node tr = ns.elementAt(0).getChildren().elementAt(0);

		// skip first 2 fixed & blank td
		Node td = tr.getChildren().elementAt(2);

		// Item의 TD Row의 갯수를 파악하여, 추후에 몇번 반복을 해야 하나의 아이템의 파싱이 종료되는지 파악해둔다.
		int rows = parseTDRowSpan(td.getText());
		물건 물건 = new 물건();

		parse물건part1(td.getFirstChild(), 물건);

		if (물건.get물건종류().contains("자동차") || 물건.get물건종류().contains("선박")) {
			return sagun;
		}

		Node detailTD = td.getNextSibling().getNextSibling();
		물건현황 물건현황 = parse상세내역TD(detailTD);

		parse물건part2(물건, detailTD);

		물건.add(물건현황);

		sagun.add(물건);

		// 여러개의 물건현황이 있다면 처리한다.
		for (int i = 1; i < rows; i++) {
			tr = tr.getNextSibling().getNextSibling();
			detailTD = tr.getChildren().elementAt(1);
			물건현황 = parse상세내역TD(detailTD);
			물건.add(물건현황);
		}

		// 여러개의 물건이 있다면 처리한다.
		while (true) {
			tr = tr.getNextSibling();
			if (tr == null) {
				break;
			}
			if (tr.getChildren() == null) {
				continue;
			}

			try {
				td = tr.getChildren().elementAt(1);

				rows = parseTDRowSpan(td.getText());
				물건 = new 물건();
				parse물건part1(td.getFirstChild(), 물건);

				detailTD = td.getNextSibling().getNextSibling();
				물건현황 = parse상세내역TD(detailTD);

				parse물건part2(물건, detailTD);

				물건.add(물건현황);

				sagun.add(물건);
				for (int i = 1; i < rows; i++) {
					tr = tr.getNextSibling().getNextSibling();
					detailTD = tr.getChildren().elementAt(1);
					물건현황 = parse상세내역TD(detailTD);
					물건.add(물건현황);
				}
			} catch (Throwable th) {
				th.printStackTrace();
				logger.warning(th.getMessage());
			}

		}

		return sagun;
	}

	/**
	 * Parse사건.
	 * 
	 * @param s the s
	 * @param slice the slice
	 * 
	 * @return the 사건
	 * 
	 * @throws ParserException the parser exception
	 */
	public 사건 parse사건(사건 s, String slice) throws ParserException {
		NodeList ns = parseDOM(slice);
		return parse사건(s, ns);
	}

	/**
	 * Slice sagun.
	 * 
	 * @param src the src
	 * 
	 * @return the string[]
	 */
	public String[] sliceSagun(String src) {

		// 사건 목록이 시작하는 바로 위에서 끝까지 자른다.
		int pos = src.indexOf("<form name=\"listForm\" >") + "<form name=\"listForm\" >".length();
		if (pos == -1) {
			pos = src.indexOf("소재지 및 상세내역") + "소재지 및 상세내역".length();
		}
		String listHTML = src.substring(pos);

		// 사건 목록이 끝나는 부분에서 자른다.
		listHTML = listHTML.substring(0, listHTML.indexOf("<!--관심사건등록"));

		// 사건별로 자른다.
		String[] listSlice = listHTML.split("javascript:loadSaDetail\\(");
		String[] res = new String[listSlice.length - 1];
		for (int i = 1; i < listSlice.length; i++) {
			res[i - 1] = listSlice[i].substring(0, listSlice[i].lastIndexOf("</tr>") + 5);
		}
		return res;
	}

	/**
	 * 문자열을 HTML TR단위롤 잘라서 복수개의 문자열로 만든다.
	 * 
	 * @param src 대상 HTML을 들고 있는 문자열.
	 * 
	 * @return the string[]
	 */
	public String[] sliceTR(String src) {
		return src.split("\\</tr\\>");
	}

	/**
	 * Gets the nos.
	 * 
	 * @param str the str
	 * @param 사건번호 the 사건번호
	 * 
	 * @return the nos
	 */
	private String getNos(String str, long 사건번호) {
		HashSet<String> set = new HashSet<String>();
		Matcher m = p1.matcher(str);
		String temp = "";
		if (m.find() == false) {
			return "";
		}
		String target = String.valueOf(사건번호);// 사건번호.replaceAll(" ", "");
		String eventYear = target.substring(0, 4);
		String eventNo = target.substring(7);
		// System.out.println("#####################");
		target = eventYear + "타경" + Integer.parseInt(eventNo);
		// System.out.println(target);

		while (m.find()) {
			String no = m.group(1).replaceAll(" ", "");
			// System.out.println(no);
			if (no.contains(target) || target.contains(no)) {
				continue;
			}
			set.add(no);
		}
		// System.out.println("#####################");

		for (String word : set) {
			temp += word + ",";
		}
		return temp;
	}

	/**
	 * Parse물건part1.
	 * 
	 * @param txt the txt
	 * @param item the item
	 */
	private void parse물건part1(Node txt, 물건 item) {
		// 물건번호를 파싱.
		item.set물건번호(Integer.parseInt(txt.getText().trim()));
		Node next = txt.getNextSibling();
		if (next.getText().startsWith("a")) {
			item.setHas명세서(true);
			Pattern p = Pattern.compile("'(\\d+)', '(\\d+)', '(\\d+)'");
			Matcher m = p.matcher(next.getText());
			m.find();
			item.set명세서번호(m.group(3));
			next = next.getNextSibling().getNextSibling().getNextSibling();
		} else {
			next = next.getNextSibling();
			item.setHas명세서(false);
			item.set명세서번호(null);
		}

		// 물건 종류를 파싱.
		item.set물건종류(next.getText().trim());
	}

	/**
	 * Parse물건part2.
	 * 
	 * @param item the item
	 * @param detailTD the detail td
	 */
	private void parse물건part2(물건 item, Node detailTD) {
		Node bigoTD = detailTD.getNextSibling().getNextSibling();

		// 비고의 내용이 없다면 TXT node도 없는 것이니 children이 없을수 있다.
		if (bigoTD.getChildren() == null) {
			item.set비고(null);
		} else {
			Node bigoNode = bigoTD.getChildren().elementAt(0);
			item.set비고(bigoNode.getText().trim());
		}

		Node priceTD = bigoTD.getNextSibling().getNextSibling();
		Node priceNode = priceTD.getChildren().elementAt(0);
		item.set가격(priceNode.getText().trim());

		Node lowPriceNode = priceTD.getChildren().elementAt(6);
		item.set최저가(lowPriceNode.getText().trim());
	}

	/**
	 * Parse병합.
	 * 
	 * @param str the str
	 * @param 사건번호 the 사건번호
	 * 
	 * @return the string
	 */
	private String parse병합(String str, long 사건번호) {
		if (str.contains("병합")) {
			return "병합 " + getNos(str, 사건번호);
		}

		if (str.contains("중복")) {
			return "중복 " + getNos(str, 사건번호);
		}

		return "";
	}

	/**
	 * Parse사건번호.
	 * 
	 * @param src the src
	 * 
	 * @return the string
	 */
	private String parse사건번호(String src) {
		Pattern p = Pattern.compile("'(\\d+)', '(\\d+)'");
		Matcher m = p.matcher(src);
		m.find();
		return m.group(2);
	}

	/**
	 * Parse상세내역 td.
	 * 
	 * @param detailTD the detail td
	 * 
	 * @return the 물건현황
	 */
	private 물건현황 parse상세내역TD(Node detailTD) {
		Node addressNode = null;
		Node detailNode = null;
		int lastIndex = 0;
		String text = detailTD.getChildren().elementAt(0).getText();
		Matcher m = p.matcher(text);
		if (m.find()) {
			addressNode = detailTD.getChildren().elementAt(1);
			detailNode = detailTD.getChildren().elementAt(9);
			lastIndex = 9;
		} else {
			addressNode = detailTD.getChildren().elementAt(0);
			detailNode = detailTD.getChildren().elementAt(8);
			lastIndex = 8;
		}

		String address = addressNode.getText().replaceAll("&nbsp;", " ").trim();

		String detail = detailNode.getText().trim().trim();

		while (true) {
			lastIndex++;
			if (detailTD.getChildren().size() <= lastIndex) {
				break;
			}
			Node node = detailTD.getChildren().elementAt(lastIndex);
			if (node == null) {
				break;
			}
			String next = node.getText();
			if (next.contains("td")) {
				break;
			}
			if (next.contains("br")) {
				continue;
			}

			detail += " " + next.trim();
		}

		물건현황 bld = new 물건현황(address);
		bld.setDetail(detail);
		return bld;
	}

	/**
	 * Parse현황조사서.
	 * 
	 * @param src the src
	 * 
	 * @return the 현황조사서
	 */
	private 현황조사서 parse현황조사서(String src) {
		Matcher m1 = 현황조사서Pattern1.matcher(src);
		m1.find();
		String bub_cd = m1.group(1);

		Matcher m2 = 현황조사서Pattern2.matcher(src);
		m2.find(m1.end());
		String sa_no = m2.group(1);

		m2.find(m2.end());
		String mo_sa_no = m2.group(1);

		m2.find(m2.end());
		String relsaInfo = m2.group(1);

		m2.find(m2.end());
		String saList_HasHyunjo = m2.group(1);

		m2.find(m2.end());
		String userSaList_HasHyunjo = m2.group(1);

		m2.find(m2.end());
		String sano_HasHyunjo = m2.group(1);

		return new 현황조사서(bub_cd, sa_no, mo_sa_no, relsaInfo, saList_HasHyunjo, userSaList_HasHyunjo, sano_HasHyunjo);
	}

	/**
	 * Parse감정평가서.
	 * 
	 * @param slice the slice
	 * 
	 * @return the 감정평가서
	 */
	public static 감정평가서 parse감정평가서(String slice) {
		String bub_cd, sa_no, mo_sa_no, relsaInfo, saList_HasGam, userSaList_HasGam, sano_HasGam, userSano_HasGam, ibub_nm, mae_giil, gam_no;
		Pattern p1 = Pattern.compile("loadGam\\( \\'(\\d+)\\'", Pattern.MULTILINE);
		Matcher m1 = p1.matcher(slice);
		m1.find();
		bub_cd = m1.group(1);

		Pattern p2 = Pattern.compile("\\'([^\\']*)\\'", Pattern.MULTILINE);
		Matcher m2 = p2.matcher(slice);
		m2.find(m1.end());
		sa_no = m2.group(1);

		m2.find();
		mo_sa_no = m2.group(1);
		m2.find();
		relsaInfo = m2.group(1);
		m2.find();
		saList_HasGam = m2.group(1);
		m2.find();
		userSaList_HasGam = m2.group(1);
		m2.find();
		sano_HasGam = m2.group(1);
		m2.find();
		userSano_HasGam = m2.group(1);
		m2.find();
		ibub_nm = m2.group(1);
		m2.find();
		mae_giil = m2.group(1);
		m2.find();
		gam_no = m2.group(1);
		return new 감정평가서(bub_cd, sa_no, mo_sa_no, relsaInfo, saList_HasGam, userSaList_HasGam, sano_HasGam,
				userSano_HasGam, ibub_nm, mae_giil, gam_no);
	}
}
