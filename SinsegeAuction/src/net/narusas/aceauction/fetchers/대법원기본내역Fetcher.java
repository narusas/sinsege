/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.logging.Logger;

import net.narusas.aceauction.model.���;

// TODO: Auto-generated Javadoc
/**
 * The Class ������⺻����Fetcher.
 */
public class ������⺻����Fetcher {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/**
	 * Update.
	 * 
	 * @param s the s
	 * 
	 * @return the ���
	 */
	public ��� update(��� s) {
		return parse(getPage(s), s);
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
	private String getCookie(String �����ڵ�, String ����ڸ�, int ������ڵ�, Date ����,
			PageFetcher fetcher) throws IOException,
			UnsupportedEncodingException {
		return fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C100ListCommand&"
						+ "bub_cd="
						+ �����ڵ�
						+ "&search_flg=2&"
						+ "mae_giil="
						+ ����.toString().replaceAll("-",".")
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
				.fetch("/au/SuperServlet?target_command=au.command.auc.C311ListCommand&"
						+ "sa_no="
						+ ��ǹ�ȣ
						+ "&"
						+ "bub_cd="
						+ �����ڵ�
						+ "&search_flg=2&page=1&level=&addr1=&addr2=&addr3=&mulutil_cd=&amt_flg=&amt=&"
						+ "mae_giil="
						+ ����.toString().replaceAll("-",".")
						+ "&"
						+ "jp_cd="
						+ ������ڵ�
						+ "&"
						+ "dam_nm="
						+ URLEncoder.encode(����ڸ�, "euc-kr")
						+ "&"
						+ "allbub=" + �����ڵ� + "&browser=2&check_msg=");
	}

	/**
	 * Gets the page.
	 * 
	 * @param s the s
	 * 
	 * @return the page
	 */
	private String getPage(��� s) {
		String page = getPage(s.court.getCode(), s.charge.get�����̸�(), s.charge
				.get�����ڵ�(), s.charge.get�Ű�����(), s.��ǹ�ȣ);
//		logger.info(page);
		return page;
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
	String getPage(String �����ڵ�, String ����ڸ�, int ������ڵ�, Date ����, long ��ǹ�ȣ) {
		�����Fetcher fetcher = �����Fetcher.getInstance();
		try {

			String res = getPage(�����ڵ�, ����ڸ�, ������ڵ�, ����, ��ǹ�ȣ, fetcher);

			// ������ ���� �Ǿ� �������� �ұ��ϰ� �߸��� �����̶�ٰ� ������ ��찡 �ִ�. �̸� ���� �ѹ��� ������
			// �����̴�.
			if (fetcher.checkValidAccess(res)) {
				return res;
			}

			// ���� �� ����
			getCookie(�����ڵ�, ����ڸ�, ������ڵ�, ����, fetcher);

			// �ٽ� ������ ȹ��
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
	 * Parses the.
	 * 
	 * @param src the src
	 * @param ��� the ���
	 * 
	 * @return the ���
	 */
	��� parse(String src, ��� ���) {
		���.set��Ǹ�(TableParser.getNextTDValueStripped(src, "��Ǹ�"));
		���.set��������(TableParser.getNextTDValueStripped(src, "��������"));
		���.set���ð�������(TableParser.getNextTDValueStripped(src, "���ð�������"));
		���.setû���ݾ�(TableParser.getNextTDValueStripped(src, "û���ݾ�"));
		���.set�������(TableParser.getNextTDValueStripped(src, "�������"));
		���.set��������(TableParser.getNextTDValueStripped(src, "��������"));
		���.set����װ���������(TableParser.getNextTDValueStripped(src, "����װ�/��������"));

		TableParser.TDValue res = null;
		���.clearä����();
		���.clearä����();
		���.clear������();

		// ä���� ó��.
		int pos = src.indexOf("ä����");
		while (pos != -1) {
			res = TableParser.getNextTDValueByPos(src, pos);
			pos = res.pos;

			���.addä����(TableParser.getNextTDValueByPos(src, pos).text);
			pos = src.indexOf("ä����", pos + 3);
		}

		// ä����
		pos = src.indexOf(">ä����<");
		while (pos != -1) {
			res = TableParser.getNextTDValueByPos(src, pos);
			pos = res.pos;

			���.addä����(TableParser.getNextTDValueByPos(src, pos).text);
			pos = src.indexOf(">ä����<", pos + ">ä����<".length());
		}

		// ������
		pos = src.indexOf(">������<");
		while (pos != -1) {
			res = TableParser.getNextTDValueByPos(src, pos);
			pos = res.pos;

			���.add������(TableParser.getNextTDValueByPos(src, pos).text);
			pos = src.indexOf(">������<", pos + ">������<".length());
		}

		// ä���ڰ� �����ڴ� �ΰ��� ��� �߰� �ȴ�.
		pos = src.indexOf(">ä���ڰ������<");
		while (pos != -1) {
			res = TableParser.getNextTDValueByPos(src, pos);
			pos = res.pos;
			String temp = TableParser.getNextTDValueByPos(src, pos).text;
			���.addä����(temp);
			���.add������(temp);

			pos = src.indexOf(">ä���ڰ������<", pos + ">ä���ڰ������<".length());
		}

		// ���䱸���⳻��
		int startPos = src.indexOf("���䱸���⳻��</b>");
		if (startPos != -1) {
			int endPos = src.indexOf("</table>", startPos);
			���.set���䱸���⳻��(TableParser.parseTable(src, startPos, endPos));
		}

		return ���;
	}

}
