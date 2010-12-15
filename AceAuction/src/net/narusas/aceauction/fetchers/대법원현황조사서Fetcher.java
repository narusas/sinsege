package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.TransformerException;

import net.narusas.aceauction.model.���;
import net.narusas.aceauction.model.��Ȳ���缭;

public class �������Ȳ���缭Fetcher {

	public String fetch(String url) throws IOException {
		PageFetcher f = �����Fetcher.getInstance();

		return f.fetch(url);
	}

	public String[] fetchAll(��� s, ��Ȳ���缭 report) throws IOException,
			TransformerException {
		String framePage = getPage(s, report);
		// System.out.println(framePage);
		String topURL = parseTopPageURL(framePage);
		String topPage = fetch(topURL);
		String[] docIDs = parseDocID(topPage);
		String[] urlPieces = parseBottomPageURLPiece(topPage);

		// String url = getXMLDocURL(type, docId, urlPieces);
		String[] �����������缭html = parseHTML(
				getXMLDocURL("2", docIDs[0], urlPieces),
				"/xslt/��Ȳ����������_view.xsl");

		// �ε��� ǥ�ø���� 1ȸ���� �а� �ϱ� ���ؼ� ���ڸ� �ٲ��ش�.
		String url = getXMLDocURL("4", docIDs[1], urlPieces);
		url = url.replaceAll("ord_hoi=2", "ord_hoi=1");
		url = url.replaceAll("ord_hoi=3", "ord_hoi=1");
		url = url.replaceAll("ord_hoi=4", "ord_hoi=1");
		url = url.replaceAll("ord_hoi=5", "ord_hoi=1");

		String[] �ε���ǥ�ø��html = parseHTML(url, "/xslt/�ε���ǥ�ø��_view.xsl");

		return new String[] { �����������缭html[0], �ε���ǥ�ø��html[0], �����������缭html[1],
				�ε���ǥ�ø��html[1] };
	}

	public String getPage(��� s, ��Ȳ���缭 report) throws IOException {
		StringBuffer query = new StringBuffer("/au/SuperServlet?");
		addParam(query, "target_command", "au.command.auc.C102HyunCommand");
		addParam(query, "search_flg", "2");
		addParam(query, "page", "1");
		addParam(query, "bub_cd", report.getBub_cd());
		addParam(query, "sa_no", report.getSa_no());
		addParam(query, "level", "");
		addParam(query, "addr1", "");
		addParam(query, "addr2", "");
		addParam(query, "addr3", "");

		addParam(query, "mulutil_cd", "");
		addParam(query, "amt_flg", "");
		addParam(query, "amt", "");

		addParam(query, "mae_giil", s.charge.get�Ű�����().toString().replaceAll("-","."));
		addParam(query, "jp_cd", String.valueOf(s.charge.get�����ڵ�()));

		addParam(query, "dam_nm", s.charge.get�����̸�());
		addParam(query, "allbub", s.court.getCode());
		addParam(query, "browser", "2");

		parseRelsaInfo(report, query);

		addParam(query, "saList_HasHyunjo", report.getSaList_HasHyunjo());
		addParam(query, "userSaList_HasHyunjo", report
				.getUserSaList_HasHyunjo());
		addParam(query, "sano_HasHyunjo", report.getSano_HasHyunjo());

		addParam(query, "check_msg", "");
		addParam(query, "IHsa_no",
				report.getMo_sa_no().indexOf("(�̼���)") > 0 ? report
						.getMo_sa_no() : "");

		PageFetcher f = �����Fetcher.getInstance();

		return f.fetch(query.toString());
	}

	private void parseRelsaInfo(��Ȳ���缭 report, StringBuffer query) {
		if (report.getRelsaInfo().length() < 5) {
			�������Ȳ���缭Fetcher.addParam(query, "displaySaList_RelSa", "");
			�������Ȳ���缭Fetcher.addParam(query, "userSaList_RelSa", "");
			return;
		}
		String[] array_relsaInfo = report.getRelsaInfo().substring(4).split(
				"<br>");
		ArrayList<String> new_array_displaySaList_RelSa = new ArrayList<String>();
		ArrayList<String> new_array_userSaList_RelSa = new ArrayList<String>();
		int temp = 0;
		int new_array_count = 1;

		new_array_displaySaList_RelSa.add(report.getMo_sa_no());
		new_array_userSaList_RelSa.add(report.getMo_sa_no());

		for (int i = 0; i < array_relsaInfo.length; i++) {
			if (array_relsaInfo[i].indexOf('(') != -1) {
				for (int j = temp; j < i; j++) {
					new_array_displaySaList_RelSa.add(array_relsaInfo[j]
							+ array_relsaInfo[i]);
					new_array_userSaList_RelSa.add(array_relsaInfo[j]);
					new_array_count++;
				}
				temp = i + 1;
			}
		}

		String displaySaList_RelSa = "";
		String userSaList_RelSa = "";

		for (int h = 0; h < new_array_displaySaList_RelSa.size(); h++) {
			if (h != (new_array_displaySaList_RelSa.size() - 1)) {
				displaySaList_RelSa += new_array_displaySaList_RelSa.get(h)
						+ "#";
				userSaList_RelSa += new_array_userSaList_RelSa.get(h) + "#";
			} else {
				displaySaList_RelSa += new_array_displaySaList_RelSa.get(h);
				userSaList_RelSa += new_array_userSaList_RelSa.get(h);
			}
		}

		�������Ȳ���缭Fetcher.addParam(query, "displaySaList_RelSa",
				displaySaList_RelSa);
		�������Ȳ���缭Fetcher.addParam(query, "userSaList_RelSa", userSaList_RelSa);
	}

	String getXMLDocURL(String docType, String docID, String[] urlPiece) {
		return urlPiece[0] + docType + urlPiece[1] + docID + urlPiece[2];
	}

	String[] parseBottomPageURLPiece(String topPage) {
		Pattern p4 = Pattern
				.compile("var bottomUrl = \"([^\"]+)\"[^\"]+\"([^\"]+)\"[^\"]+\"([^\"]+)");
		Matcher m = p4.matcher(topPage);
		m.find();

		String piece1 = m.group(1);
		String piece2 = m.group(2);
		String piece3 = m.group(3);

		return new String[] { piece1, piece2, piece3 };
	}

	String[] parseDocID(String topPage) {
		Pattern p3 = Pattern
				.compile("function jf_GetDocId\\( id \\)\\s+\\{[^\\}]+else if \\( id == '2' \\)\\s+doc_id = '(\\d*)[^\\}]+else if \\( id == '4' \\)\\s+doc_id = '(\\d*)'");
		Matcher m = p3.matcher(topPage);
		m.find();
		String �����������缭docID = m.group(1);
		String �ε���ǥ�ø��docID = m.group(2);
		return new String[] { �����������缭docID, �ε���ǥ�ø��docID };
	}

	String[] parseHTML(String url, String xslt) throws IOException,
			TransformerException {
		if (url == null || "".equals(url)) {
			return null;
		}
		// String url = getXMLDocURL(type, docId, urlPieces);

		String xml = fetch(url);
		XSLTParser parser = new XSLTParser(xslt);
		String html = parser.parse(xml);
		html = html.replace("charset=UTF-8", "charset=EUC-KR");
		return new String[] { html, xml };
	}

	String parseTopPageURL(String framePage) {
		Pattern p1 = Pattern
				.compile("<frame name=\"c102Top\" [^>]+src=\"([^\"]+)");

		Matcher m = p1.matcher(framePage);
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}

	public static void addParam(StringBuffer query, String key, String value) {
		if (!query.toString().endsWith("?")) {
			query.append("&");
		}
		try {
			query.append(key).append("=").append(
					URLEncoder.encode(value, "euc-kr"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
