package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.logging.Logger;

import net.narusas.aceauction.model.���;

/**
 * Builder���� ���Ǵ� ���ϳ���Fetcher�̴�.
 * 
 * @author narusas
 * 
 */
public class ��������ϳ���Fetcher {
	Logger logger = Logger.getLogger("log");

	public String getPage(String �����ڵ�, String ����ڸ�, int ������ڵ�, Date ����,
			long ��ǹ�ȣ) {
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
	 * ��ǿ� �ش��ϴ� ���� ������ ����� ����Ʈ���� ��� �ͼ� ����� ������ �����Ѵ�.
	 * 
	 * @param s
	 *            �����ϰ��� �ϴ� ���.
	 * @return
	 */
	public ��� update(��� s) {
		return parse(getPage(s), s);
	}

	private void getCookie(String �����ڵ�, String ����ڸ�, int ������ڵ�, Date ����,
			PageFetcher fetcher) throws IOException,
			UnsupportedEncodingException {
		logger.info("���������� �߸��Ǿ� ��Ű�� �ٽ� ��� �ɴϴ�.");
		fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C100ListCommand&"
						+ "bub_cd="
						+ �����ڵ�
						+ "&search_flg=2&"
						+ "mae_giil="
						+ ����
						+ "&"
						+ "jp_cd="
						+ ������ڵ�
						+ "&"
						+ "dam_nm="
						+ URLEncoder.encode(����ڸ�, "euc-kr")
						+ "&browser=2&check_msg=");
	}

	private String getPage(��� s) {
		String page = getPage(s.court.getCode(), s.charge.get�����̸�(), s.charge
				.get�����ڵ�(), s.charge.get�Ű�����(), s.��ǹ�ȣ);
//		logger.info(page);
		return page;
	}

	private String get���ϳ���Page(String �����ڵ�, String ����ڸ�, int ������ڵ�, Date ����,
			long ��ǹ�ȣ, PageFetcher fetcher) throws IOException,
			UnsupportedEncodingException {
		logger.info("���ϳ��� �������� ���ɴϴ�.");
		String res = fetcher
				.fetch("/au/SuperServlet?target_command=au.command.auc.C313ListCommand&"
						+ "sa_no="
						+ ��ǹ�ȣ
						+ "&"
						+ "bub_cd="
						+ �����ڵ�
						+ "&search_flg=2&page=1&level=&addr1=&addr2=&addr3=&mulutil_cd=&amt_flg=&amt=&"
						+ "mae_giil="
						+ ����
						+ "&"
						+ "jp_cd="
						+ ������ڵ�
						+ "&"
						+ "dam_nm="
						+ URLEncoder.encode(����ڸ�, "euc-kr")
						+ "&"
						+ "allbub=" + �����ڵ� + "&browser=2&check_msg=");
		return res;
	}

	private ��� parse(String src, ��� s) {
		logger.info("���ϳ����� �м��� �����մϴ�. ");
		int startPos = src.indexOf("���ϳ���&nbsp;&nbsp;&nbsp;");
		if (startPos != -1) {
			int endPos = src.indexOf("</table>", startPos);
			s.set���ϳ���(TableParser.parseTable(src, startPos, endPos));
		}
		return s;
	}
}
