/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.narusas.aceauction.model.Row;
import net.narusas.aceauction.model.Table;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;

// TODO: Auto-generated Javadoc
/**
 * ����� ����Ʈ���� �Ű����� ������ ������ Fetcher.
 * 
 * @author narusas
 */
public class ������Ű����Ǹ���Fetcher {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/**
	 * ���ǿ� �ش��ϴ� �Ű����Ǹ����� ����� ����Ʈ���� ��� �ͼ� ����� ������ �����Ѵ�.
	 * 
	 * @param s �����ϰ��� �ϴ� ���.
	 * 
	 * @return the ����
	 */

	public ���� update(���� s) {
		String page = getPage(s);
		s.set�Ű����Ǹ���HTML(page);
		return parse(page, s);
	}

	/**
	 * Filter table.
	 * 
	 * @param src the src
	 * @param startPos the start pos
	 * @param endPos the end pos
	 * 
	 * @return the table
	 */
	private Table filterTable(String src, int startPos, int endPos) {
		Table table = fixTable(src, startPos, endPos);
		removeDuplicatedRow(table);
		return table;
	}

	/**
	 * Fix table.
	 * 
	 * @param src the src
	 * @param startPos the start pos
	 * @param endPos the end pos
	 * 
	 * @return the table
	 */
	private Table fixTable(String src, int startPos, int endPos) {
		logger.log(Level.FINEST, "�Ű����Ǹ��� ���̺��� �����մϴ�. ");
		Table table = TableParser.parseTable(src, startPos, endPos);
		List<Row> toRemove = new LinkedList<Row>();
		for (int i = 0; i < table.getRows().size(); i++) {
			Row r = table.getRows().get(i);
			// �Ű����� ���� ���̺��� �� �÷� ���� 10�̴�. �׷��� �÷��� 9����� ����
			// �������� ���� �÷��� <td rows="2"> ������ ��ĭ�� ���� ���¿��� �÷��� ���� �ǰ� �ִٴ� ������
			// �Ѹ��� �����ڿ� ���� ������� �ǹ��̴�.
			// ���� �÷��� ���� 9�̸�, ���� ���� ������ �̿��Ͽ� �߰��ϰ� �ϴ� ���̴�.
			if (r.getValues().size() == 9 && i != 0) {
				r.getValues().add(0, table.getRows().get(i - 1).getValue(0));
			} else {
				// 9���� ���� �÷��� ���� ��� �̰ų�, �ǹ̾��� �ּ��̹Ƿ� �����Ѵ�.
				if (r.getValues().size() < 9) {
					break;
				}

				if (r.getValue(0).equals(r.getValue(1))
						&& r.getValue(0).equals(r.getValue(2))) {
					toRemove.add(r);
				}
			}
		}
		table.getRows().removeAll(toRemove);
		return table;
	}

	/**
	 * Gets the cookies.
	 * 
	 * @param fetcher the fetcher
	 * 
	 * @return the cookies
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void getCookies(PageFetcher fetcher) throws IOException {
		logger.log(Level.FINEST,"���������� �߸��Ǿ� ��Ű�� �ٽ� ��� �ɴϴ�.");
		fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C100ListCommand&"
						+ "bub_cd=000210"
						+ "&search_flg=2&"
						+ "mae_giil=2006.03.20"
						+ "&"
						+ "jp_cd="
						+ "&"
						+ "dam_nm=" + "&browser=2&check_msg=");
	}

	/**
	 * Gets the page.
	 * 
	 * @param �����ڵ� the �����ڵ�
	 * @param ��ǹ�ȣ the ��ǹ�ȣ
	 * @param �Ź���ȣ the �Ź���ȣ
	 * @param fetcher the fetcher
	 * 
	 * @return the page
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private String getPage(String �����ڵ�, long ��ǹ�ȣ, String �Ź���ȣ,
			PageFetcher fetcher) throws IOException {
		logger.info(�����ڵ� + " " + ��ǹ�ȣ + " " + �Ź���ȣ + "�� �ش��ϴ� ���� �������� ���ɴϴ�. ");
		return fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C101MyungCommand&"
						+ "sa_no="
						+ ��ǹ�ȣ
						+ "&"
						+ "bub_cd="
						+ �����ڵ�
						+ "&"
						+ "maemul_no="
						+ �Ź���ȣ
						+ "&search_flg=2&page=1&level=&addr1=&addr2=&addr3=&mulutil_cd=&amt_flg=&amt=&"
						+ "&browser=2&check_msg=");
	}

	/**
	 * Gets the page.
	 * 
	 * @param s the s
	 * 
	 * @return the page
	 */
	private String getPage(���� s) {
		return getPage(s.���.court.getCode(), s.���.��ǹ�ȣ, String.valueOf(s.���ǹ�ȣ));
	}

	/**
	 * Parses the table.
	 * 
	 * @param src the src
	 * 
	 * @return the table
	 */
	private Table parseTable(String src) {
		int startPos = src.indexOf("����ڵ�Ͻ�û���ڿ� Ȯ�������� ������ �� ����");
		startPos = src.indexOf("</table>", startPos) + 5;
		int endPos = src.indexOf("</table>", startPos);
		Table table = filterTable(src, startPos, endPos);
		return table;
	}

	/**
	 * Removes the duplicated row.
	 * 
	 * @param table the table
	 */
	private void removeDuplicatedRow(Table table) {
		logger.log(Level.FINEST,"�ߺ��� ���ڵ带 �����մϴ�. ");
		for (int i = 0; i < table.getRows().size(); i++) {
			if (i != 0
					&& table.getRows().get(i).getValue(0).equals(
							table.getRows().get(i - 1).getValue(0))) {
				table.getRows().remove(i - 1);
				i--;
			}
		}
	}

	/**
	 * Set����.
	 * 
	 * @param src the src
	 * @param s the s
	 * @param table the table
	 */
	private void set����(String src, ���� s, Table table) {

		int start = src.indexOf("< ��� ><br>");
		int end = src.indexOf("</tr", start);
		String ��� = TableParser.strip(src.substring(start, end));

		String �Ǹ� = TableParser.getNextTDValueStripped(src,
				"���� �ε��꿡 ���� �Ǹ� �Ǵ� ��ó������  �Ű��㰡�� ���Ͽ� �� ȿ���� �Ҹ���� �ƴ��ϴ� ��");
		String ���� = TableParser.getNextTDValueStripped(src,
				"�Ű��㰡�� ���Ͽ� ������ ������ ���� ������� ����");
		String ���� = TableParser.getNextTDValueStripped(src, "����");
		s.set����(new ����(table, ���, �Ǹ�, ����, ����));
	}

	/**
	 * Gets the page.
	 * 
	 * @param �����ڵ� the �����ڵ�
	 * @param ��ǹ�ȣ the ��ǹ�ȣ
	 * @param �Ź���ȣ the �Ź���ȣ
	 * 
	 * @return the page
	 */
	String getPage(String �����ڵ�, long ��ǹ�ȣ, String �Ź���ȣ) {
		�����Fetcher fetcher = �����Fetcher.getInstance();
		try {
			String res = getPage(�����ڵ�, ��ǹ�ȣ, �Ź���ȣ, fetcher);
			if (fetcher.checkValidAccess(res)) {
				return res;
			}

			getCookies(fetcher);
			res = getPage(�����ڵ�, ��ǹ�ȣ, �Ź���ȣ, fetcher);
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
	 * @param s the s
	 * 
	 * @return the ����
	 */
	���� parse(String src, ���� s) {
		logger.info("���� �м��� �����մϴ�.");
		Table table = parseTable(src);
		set����(src, s, table);

		logger.info("���� �м��� �����մϴ�.");
		return s;
	}
}
