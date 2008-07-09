/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.model.���;
import net.narusas.aceauction.model.���ÿܰǹ�;

// TODO: Auto-generated Javadoc
/**
 * ����� ����Ʈ���� ���ÿ� �ǹ� ������ ������ Fetcher.
 * 
 * @author narusas
 */
public class ��������ÿܰǹ�Fetcher {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The p1. */
	static Pattern p1 = Pattern.compile("\\(�뵵\\)([^\\(^<]*)");

	/** The p2. */
	static Pattern p2 = Pattern.compile("\\(����\\)([^\\(^<]*)");

	/** The p3. */
	static Pattern p3 = Pattern.compile("\\(����\\)([^\\(^<]*)");
	
	/** The last. */
	private ���ÿܰǹ� last;

	/**
	 * ��ǿ� �ش��ϴ� ���ÿ� �ǹ� ��� �������� �о�ͼ� �м�, ���ÿܰǹ� ��ü�� ����� ��ȯ�Ѵ�.
	 * 
	 * @param �����ڵ� the �����ڵ�
	 * @param ����ڸ� the ����ڸ�
	 * @param ������ڵ� the ������ڵ�
	 * @param ���� the ����
	 * @param ��ǹ�ȣ the ��ǹ�ȣ
	 * 
	 * @return the list<���ÿܰǹ�>
	 */
	public List<���ÿܰǹ�> fetchAll(String �����ڵ�, String ����ڸ�, int ������ڵ�, Date ����,
			long ��ǹ�ȣ) {
		return parse(getPage(�����ڵ�, ����ڸ�, ������ڵ�, ����, ��ǹ�ȣ));
	}

	/**
	 * Gets the page.
	 * 
	 * @param �����ڵ� the �����ڵ�
	 * @param ����ڸ� the ����ڸ�
	 * @param ������ڵ� the ������ڵ�
	 * @param ���� the ����
	 * @param ��ǹ�ȣ the ��ǹ�ȣ
	 * 
	 * @return the page
	 */
	public String getPage(String �����ڵ�, String ����ڸ�, int ������ڵ�, Date ����,
			long ��ǹ�ȣ) {
		�����Fetcher fetcher = �����Fetcher.getInstance();
		try {

			String res = getPage(�����ڵ�, ����ڸ�, ������ڵ�, ����, ��ǹ�ȣ, fetcher);
//			logger.info(res);
			if (fetcher.checkValidAccess(res)) {
				return res;
			}
			getCookie(�����ڵ�, ����ڸ�, ������ڵ�, ����, fetcher);
			res = getPage(�����ڵ�, ����ڸ�, ������ڵ�, ����, ��ǹ�ȣ, fetcher);

			return res;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Update.
	 * 
	 * @param s the s
	 */
	public void update(��� s) {
		s.set���ÿܰǹ�(fetchAll(s.court.getCode(), s.charge.get�����̸�(), s.charge
				.get�����ڵ�(), s.charge.get�Ű�����(), s.��ǹ�ȣ));
	}

	/**
	 * Gets the cookie.
	 * 
	 * @param �����ڵ� the �����ڵ�
	 * @param ����ڸ� the ����ڸ�
	 * @param ������ڵ� the ������ڵ�
	 * @param ���� the ����
	 * @param fetcher the fetcher
	 * 
	 * @return the cookie
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	private void getCookie(String �����ڵ�, String ����ڸ�, int ������ڵ�, Date ����,
			PageFetcher fetcher) throws IOException,
			UnsupportedEncodingException {
		fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C100ListCommand&"
						+ "bub_cd="
						+ �����ڵ�
						+ "&search_flg=2&"
						+ "mae_giil="
						+ ����.toString().replaceAll("-", ".")
						+ "&"
						+ "jp_cd="
						+ ������ڵ�
						+ "&"
						+ "dam_nm="
						+ URLEncoder.encode(����ڸ�, "euc-kr")
						+ "&browser=2&check_msg=");
	}

	/**
	 * Gets the page.
	 * 
	 * @param �����ڵ� the �����ڵ�
	 * @param ����ڸ� the ����ڸ�
	 * @param ������ڵ� the ������ڵ�
	 * @param ���� the ����
	 * @param ��ǹ�ȣ the ��ǹ�ȣ
	 * @param fetcher the fetcher
	 * 
	 * @return the page
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	private String getPage(String �����ڵ�, String ����ڸ�, int ������ڵ�, Date ����,
			long ��ǹ�ȣ, PageFetcher fetcher) throws IOException,
			UnsupportedEncodingException {
		return fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C312ListCommand&"
						+ "sa_no="
						+ ��ǹ�ȣ
						+ "&"
						+ "bub_cd="
						+ �����ڵ�
						+ "&search_flg=2&page=1&level=&addr1=&addr2=&addr3=&mulutil_cd=&amt_flg=&amt=&"
						+ "mae_giil="
						+ ����.toString().replaceAll("-", ".")
						+ "&"
						+ "jp_cd="
						+ ������ڵ�
						+ "&"
						+ "dam_nm="
						+ URLEncoder.encode(����ڸ�, "euc-kr")
						+ "&"
						+ "allbub="
						+ �����ڵ� + "&browser=2&check_msg=");
	}

	/**
	 * Parse���ÿܰǹ�.
	 * 
	 * @param res the res
	 * @param ���ǹ�ȣ the ���ǹ�ȣ
	 * @param temp the temp
	 * @param address the address
	 * @param contains the contains
	 */
	private void parse���ÿܰǹ�(LinkedList<���ÿܰǹ�> res, String ���ǹ�ȣ, String temp,
			String address, int contains) {
		for (String s : temp.split("[\\n\\r]")) {

			String ss = s.trim();
			if ("".equals(ss)) {
				continue;
			}
			// System.out.println(ss);
			Matcher m = p1.matcher(ss);
			m.find();
			String �뵵 = m.group(1);
			if ("\"".equals(�뵵.trim())) {
				�뵵 = last.get�뵵();
			}
			m = p2.matcher(ss);
			m.find();
			String ���� = m.group(1);
			if ("\"".equals(����.trim())) {
				���� = last.get����();
			}
			m = p3.matcher(ss);
			m.find();
			String ���� = m.group(1);
			if ("\"".equals(����.trim())) {
				���� = last.get����();
			}
			last = new ���ÿܰǹ�(Integer.parseInt(���ǹ�ȣ.trim()), �뵵.trim(), ����
					.trim(), ����.trim(), address, contains);
			res.add(last);
		}
	}

	/**
	 * Parses the.
	 * 
	 * @param src the src
	 * 
	 * @return the list<���ÿܰǹ�>
	 */
	List<���ÿܰǹ�> parse(String src) {
		if (src == null) {
			logger.info("���ÿ� �ǹ� �������� ������ ���߽��ϴ�. ");
			return null;
		}

		LinkedList<���ÿܰǹ�> res = new LinkedList<���ÿܰǹ�>();
		String target = src.substring(src.indexOf("���ǳ��� </b>"), src
				.indexOf("����)<br>"));
		target = target.substring(target.indexOf("<table"), target
				.lastIndexOf("<table"));
		String[] slices = target.split("<table");
		// System.out.println(slices.length + "���� �����̽��� �ֽ��ϴ�. ");
		for (String data : slices) {
			String ���ǹ�ȣ = TableParser.getNextTDValueStripped(data, "���ǹ�ȣ");
			if (���ǹ�ȣ == null || "".equals(���ǹ�ȣ)) {
				continue;
			}
			String slice = data;
			int contains = 0;
			if (data.contains("���ÿ�") && data.contains("����")) {
				contains = 1;
			} else if (data.contains("���ÿ�") && data.contains("����")) {
				contains = 2;
			}

			int pos = slice.indexOf(">���ÿ�<");
			// System.out.println("���ÿ� �ǹ� ���� ��ġ " + pos);
			while (pos != -1) {
				// System.out.println("���ÿ� �ǹ� ���� ��ġ " + pos);
				String s = slice.substring(0, pos);

				int trPos = s.lastIndexOf("<tr");
				s = s.substring(0, trPos);
				s = s.substring(s.lastIndexOf("<tr"));

				String address = TableParser.getNextTDValue(s, ">���");
				if (address.indexOf("<") != -1) {
					address = TableParser.strip(
							address.substring(0, address.indexOf("<"))).trim();
				}
				TableParser.TDValue d = TableParser.getNextTDValueByPos(slice,
						pos);
				String temp = d.text;// TableParser.getNextTDValueStripped(slice,
				// ">���ÿܰǹ�<");
				if (temp == null || "".equals(temp)) {
					break;
				}
				parse���ÿܰǹ�(res, ���ǹ�ȣ, temp, address, contains);
				pos = slice.indexOf(">���ÿܰǹ�<", pos + 7);
			}
		}
		return res;
	}

}
