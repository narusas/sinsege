/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.logging.Logger;

import net.narusas.aceauction.model.Table;
import net.narusas.aceauction.model.���;

// TODO: Auto-generated Javadoc
/**
 * ui�󿡼��� ���Ǵ� Ŭ�����̴�.
 * 
 * @author narusas
 * @see ��������ϳ���Fetcher
 */
public class ��������ϳ���Fetcher_��� {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

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
		logger.info("���ϳ��� �������� ���ɴϴ�.");
		�����Fetcher fetcher = �����Fetcher.getInstance();
		try {
			String res = get���ϳ���Page(�����ڵ�, ����ڸ�, ������ڵ�, ����, ��ǹ�ȣ, fetcher);
			if (fetcher.checkValidAccess(res)) {
				return res;
			}
			getCookie(�����ڵ�, ����ڸ�, ������ڵ�, ����, fetcher);
			res = get���ϳ���Page(�����ڵ�, ����ڸ�, ������ڵ�, ����, ��ǹ�ȣ, fetcher);
			return res;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the ���ϳ��� table.
	 * 
	 * @param �����ڵ� the �����ڵ�
	 * @param ����ڸ� the ����ڸ�
	 * @param ������ڵ� the ������ڵ�
	 * @param ���� the ����
	 * @param ��ǹ�ȣ the ��ǹ�ȣ
	 * 
	 * @return the ���ϳ��� table
	 */
	public Table get���ϳ���Table(String �����ڵ�, String ����ڸ�, int ������ڵ�,
			Date ����, long ��ǹ�ȣ) { 
		return parse(getPage(�����ڵ�, ����ڸ�, ������ڵ�, ����, ��ǹ�ȣ)); 
	}

	/**
	 * Parses the.
	 * 
	 * @param src the src
	 * 
	 * @return the table
	 */
	public Table parse(String src) {
		logger.info("���ϳ����� �м��� �����մϴ�. ");
		int startPos = src.indexOf("���ϳ���&nbsp;&nbsp;&nbsp;");
		if (startPos != -1) {
			int endPos = src.indexOf("</table>", startPos);
			return TableParser.parseTable(src, startPos, endPos);

		}
		return null;
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
		logger.info("��Ű��������� �＼���մϴ�. ");
		fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C100ListCommand&" //
						+ "bub_cd=" + �����ڵ�//
						+ "&search_flg=6&"//
						+ "mae_giil=" + ����.toString().replaceAll("-",".")//
						+ "&jp_cd=" + ������ڵ�//
						+ "&dam_nm=" + URLEncoder.encode(����ڸ�, "euc-kr")//
						+ "&browser=2&check_msg=");
	}

	/**
	 * Gets the page.
	 * 
	 * @param s the s
	 * 
	 * @return the page
	 */
	private String getPage(��� s) {
		return getPage(s.court.getCode(), s.charge.get�����̸�(), s.charge
				.get�����ڵ�(), s.charge.get�Ű�����(), s.��ǹ�ȣ);
	}

	/**
	 * Gets the ���ϳ��� page.
	 * 
	 * @param �����ڵ� the �����ڵ�
	 * @param ����ڸ� the ����ڸ�
	 * @param ������ڵ� the ������ڵ�
	 * @param ���� the ����
	 * @param ��ǹ�ȣ the ��ǹ�ȣ
	 * @param fetcher the fetcher
	 * 
	 * @return the ���ϳ��� page
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	private String get���ϳ���Page(String �����ڵ�, String ����ڸ�, int ������ڵ�,
			Date ����, long ��ǹ�ȣ, PageFetcher fetcher) throws IOException,
			UnsupportedEncodingException {

		// /au/SuperServlet?target_command=au.command.auc.C313ListCommand&search_flg=6&bub_cd=000210&sa_no=20070130004692&browser=2&check_msg=&jong_day=&page=2&giil=2007.08.07&jp_cd=1006&dam_nm=%B0%E6%B8%C56%B0%E8
		String res = fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C313ListCommand&search_flg=6"
						+ "&bub_cd=" + �����ڵ�// 
						+ "&sa_no=" + ��ǹ�ȣ//
						+ "&browser=2" + "&check_msg=" + "&jong_day="
						// +
						// "&page=2"
						+ "&giil=" + ����.toString().replaceAll("-",".")// 
						+ "&jp_cd=" + ������ڵ�// 
						+ "&dam_nm=" + URLEncoder.encode(����ڸ�, "euc-kr")//

				);
		return res;
	}
}
