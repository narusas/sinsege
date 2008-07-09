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

import net.narusas.aceauction.model.�����򰡼�;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.������Ȳ;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.���;
import net.narusas.aceauction.model.��Ȳ���缭;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

// TODO: Auto-generated Javadoc
/**
 * ����� ����Ʈ���� ���� HTML�� �м��Ͽ� ��� ��ü�� �����ϴ� �ļ�. ���������� HTML Parser�� ����Ѵ�.
 * 
 * @author narusas
 */
public class ��������Parser {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The p. */
	static Pattern p = Pattern.compile("\\d\\d:\\d\\d");

	/** The p1. */
	static Pattern p1 = Pattern.compile("(\\d+Ÿ��\\d+)");

	/** The ��Ȳ���缭 pattern1. */
	static Pattern ��Ȳ���缭Pattern1 = Pattern.compile("loadHyun\\( \\'(\\d+)\\'", Pattern.MULTILINE);

	/** The ��Ȳ���缭 pattern2. */
	static Pattern ��Ȳ���缭Pattern2 = Pattern.compile("\\'([^\\']*)\\'", Pattern.MULTILINE);

	/**
	 * Ư�� ��� ���ڿ��� �߽����� ���ڿ��� �߶��� ������ �������� HTML Table ������ ���� ���ڿ��� ���� ���̺��� �����Բ�
	 * �����Ѵ�.
	 * 
	 * @param src ��� HTML�� ��� �ִ� ���ڿ�.
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
	 * @return the list<���>
	 * 
	 * @throws ParserException the parser exception
	 */
	public List<���> parsePage(String html, ���� court, ���� charge) throws ParserException {

		List<���> result = new ArrayList<���>();
		String[] slices = sliceSagun(html);
		for (int i = 0; i < slices.length; i++) {
			logger.log(Level.FINEST,i + "��° ��� �����̽��� ó���Ѵ�. ");
			��� s = new ���(court, charge);
			// �����ϰ� �м��ɼ� �ִ� �κ��� ó���Ѵ�.

			s.set��ǹ�ȣ(parse��ǹ�ȣ(slices[i]));
			s.set����(parse����(slices[i], s.get��ǹ�ȣ()));
			s.set��Ȳ���缭(parse��Ȳ���缭(slices[i]));
			s.set�����򰡼�(parse�����򰡼�(slices[i]));

			// �����ϰ� �������� �ִ� �κ��� ó���Ѵ�.
			String src = fixSlice(slices[i]);
			// logger.info(src);
			// System.out.println(src);
			NodeList ns = parseDOM(src);
			parse���(s, ns);

			// ����� �ε��� ����� �ƴϸ� �����Ѵ�.
			// if (s.get��Ǹ�() != null && (s.get��Ǹ�().contains("�ڵ���") ||
			// s.get��Ǹ�().contains("����"))) {
			// continue;
			// }
			result.add(s);
		}
		return result;
	}

	/**
	 * ��ǹ�ȣ�� �Ľ��Ѵ�.
	 * 
	 * @param src ��� HTML�� ��� �ִ� ���ڿ�.
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
	 * ����� �Ľ��Ѵ�. �ϳ��� ��ǿ��� n���� ������ ������ ������, �Ѱ��� ���ǿ��� n���� ������Ȳ�� ������ �ִ�. HTML
	 * Table�� Ư���� ù�ٿ��� ����� ���� ������ ���� ������ �̸� Ư���� ó���Ͽ���.
	 * 
	 * @param sagun the sagun
	 * @param ns the ns
	 * 
	 * @return the ���
	 */
	public ��� parse���(��� sagun, NodeList ns) {
		Node tr = ns.elementAt(0).getChildren().elementAt(0);

		// skip first 2 fixed & blank td
		Node td = tr.getChildren().elementAt(2);

		// Item�� TD Row�� ������ �ľ��Ͽ�, ���Ŀ� ��� �ݺ��� �ؾ� �ϳ��� �������� �Ľ��� ����Ǵ��� �ľ��صд�.
		int rows = parseTDRowSpan(td.getText());
		���� ���� = new ����();

		parse����part1(td.getFirstChild(), ����);

		if (����.get��������().contains("�ڵ���") || ����.get��������().contains("����")) {
			return sagun;
		}

		Node detailTD = td.getNextSibling().getNextSibling();
		������Ȳ ������Ȳ = parse�󼼳���TD(detailTD);

		parse����part2(����, detailTD);

		����.add(������Ȳ);

		sagun.add(����);

		// �������� ������Ȳ�� �ִٸ� ó���Ѵ�.
		for (int i = 1; i < rows; i++) {
			tr = tr.getNextSibling().getNextSibling();
			detailTD = tr.getChildren().elementAt(1);
			������Ȳ = parse�󼼳���TD(detailTD);
			����.add(������Ȳ);
		}

		// �������� ������ �ִٸ� ó���Ѵ�.
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
				���� = new ����();
				parse����part1(td.getFirstChild(), ����);

				detailTD = td.getNextSibling().getNextSibling();
				������Ȳ = parse�󼼳���TD(detailTD);

				parse����part2(����, detailTD);

				����.add(������Ȳ);

				sagun.add(����);
				for (int i = 1; i < rows; i++) {
					tr = tr.getNextSibling().getNextSibling();
					detailTD = tr.getChildren().elementAt(1);
					������Ȳ = parse�󼼳���TD(detailTD);
					����.add(������Ȳ);
				}
			} catch (Throwable th) {
				th.printStackTrace();
				logger.warning(th.getMessage());
			}

		}

		return sagun;
	}

	/**
	 * Parse���.
	 * 
	 * @param s the s
	 * @param slice the slice
	 * 
	 * @return the ���
	 * 
	 * @throws ParserException the parser exception
	 */
	public ��� parse���(��� s, String slice) throws ParserException {
		NodeList ns = parseDOM(slice);
		return parse���(s, ns);
	}

	/**
	 * Slice sagun.
	 * 
	 * @param src the src
	 * 
	 * @return the string[]
	 */
	public String[] sliceSagun(String src) {

		// ��� ����� �����ϴ� �ٷ� ������ ������ �ڸ���.
		int pos = src.indexOf("<form name=\"listForm\" >") + "<form name=\"listForm\" >".length();
		if (pos == -1) {
			pos = src.indexOf("������ �� �󼼳���") + "������ �� �󼼳���".length();
		}
		String listHTML = src.substring(pos);

		// ��� ����� ������ �κп��� �ڸ���.
		listHTML = listHTML.substring(0, listHTML.indexOf("<!--���ɻ�ǵ��"));

		// ��Ǻ��� �ڸ���.
		String[] listSlice = listHTML.split("javascript:loadSaDetail\\(");
		String[] res = new String[listSlice.length - 1];
		for (int i = 1; i < listSlice.length; i++) {
			res[i - 1] = listSlice[i].substring(0, listSlice[i].lastIndexOf("</tr>") + 5);
		}
		return res;
	}

	/**
	 * ���ڿ��� HTML TR������ �߶� �������� ���ڿ��� �����.
	 * 
	 * @param src ��� HTML�� ��� �ִ� ���ڿ�.
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
	 * @param ��ǹ�ȣ the ��ǹ�ȣ
	 * 
	 * @return the nos
	 */
	private String getNos(String str, long ��ǹ�ȣ) {
		HashSet<String> set = new HashSet<String>();
		Matcher m = p1.matcher(str);
		String temp = "";
		if (m.find() == false) {
			return "";
		}
		String target = String.valueOf(��ǹ�ȣ);// ��ǹ�ȣ.replaceAll(" ", "");
		String eventYear = target.substring(0, 4);
		String eventNo = target.substring(7);
		// System.out.println("#####################");
		target = eventYear + "Ÿ��" + Integer.parseInt(eventNo);
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
	 * Parse����part1.
	 * 
	 * @param txt the txt
	 * @param item the item
	 */
	private void parse����part1(Node txt, ���� item) {
		// ���ǹ�ȣ�� �Ľ�.
		item.set���ǹ�ȣ(Integer.parseInt(txt.getText().trim()));
		Node next = txt.getNextSibling();
		if (next.getText().startsWith("a")) {
			item.setHas����(true);
			Pattern p = Pattern.compile("'(\\d+)', '(\\d+)', '(\\d+)'");
			Matcher m = p.matcher(next.getText());
			m.find();
			item.set������ȣ(m.group(3));
			next = next.getNextSibling().getNextSibling().getNextSibling();
		} else {
			next = next.getNextSibling();
			item.setHas����(false);
			item.set������ȣ(null);
		}

		// ���� ������ �Ľ�.
		item.set��������(next.getText().trim());
	}

	/**
	 * Parse����part2.
	 * 
	 * @param item the item
	 * @param detailTD the detail td
	 */
	private void parse����part2(���� item, Node detailTD) {
		Node bigoTD = detailTD.getNextSibling().getNextSibling();

		// ����� ������ ���ٸ� TXT node�� ���� ���̴� children�� ������ �ִ�.
		if (bigoTD.getChildren() == null) {
			item.set���(null);
		} else {
			Node bigoNode = bigoTD.getChildren().elementAt(0);
			item.set���(bigoNode.getText().trim());
		}

		Node priceTD = bigoTD.getNextSibling().getNextSibling();
		Node priceNode = priceTD.getChildren().elementAt(0);
		item.set����(priceNode.getText().trim());

		Node lowPriceNode = priceTD.getChildren().elementAt(6);
		item.set������(lowPriceNode.getText().trim());
	}

	/**
	 * Parse����.
	 * 
	 * @param str the str
	 * @param ��ǹ�ȣ the ��ǹ�ȣ
	 * 
	 * @return the string
	 */
	private String parse����(String str, long ��ǹ�ȣ) {
		if (str.contains("����")) {
			return "���� " + getNos(str, ��ǹ�ȣ);
		}

		if (str.contains("�ߺ�")) {
			return "�ߺ� " + getNos(str, ��ǹ�ȣ);
		}

		return "";
	}

	/**
	 * Parse��ǹ�ȣ.
	 * 
	 * @param src the src
	 * 
	 * @return the string
	 */
	private String parse��ǹ�ȣ(String src) {
		Pattern p = Pattern.compile("'(\\d+)', '(\\d+)'");
		Matcher m = p.matcher(src);
		m.find();
		return m.group(2);
	}

	/**
	 * Parse�󼼳��� td.
	 * 
	 * @param detailTD the detail td
	 * 
	 * @return the ������Ȳ
	 */
	private ������Ȳ parse�󼼳���TD(Node detailTD) {
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

		������Ȳ bld = new ������Ȳ(address);
		bld.setDetail(detail);
		return bld;
	}

	/**
	 * Parse��Ȳ���缭.
	 * 
	 * @param src the src
	 * 
	 * @return the ��Ȳ���缭
	 */
	private ��Ȳ���缭 parse��Ȳ���缭(String src) {
		Matcher m1 = ��Ȳ���缭Pattern1.matcher(src);
		m1.find();
		String bub_cd = m1.group(1);

		Matcher m2 = ��Ȳ���缭Pattern2.matcher(src);
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

		return new ��Ȳ���缭(bub_cd, sa_no, mo_sa_no, relsaInfo, saList_HasHyunjo, userSaList_HasHyunjo, sano_HasHyunjo);
	}

	/**
	 * Parse�����򰡼�.
	 * 
	 * @param slice the slice
	 * 
	 * @return the �����򰡼�
	 */
	public static �����򰡼� parse�����򰡼�(String slice) {
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
		return new �����򰡼�(bub_cd, sa_no, mo_sa_no, relsaInfo, saList_HasGam, userSaList_HasGam, sano_HasGam,
				userSano_HasGam, ibub_nm, mae_giil, gam_no);
	}
}
