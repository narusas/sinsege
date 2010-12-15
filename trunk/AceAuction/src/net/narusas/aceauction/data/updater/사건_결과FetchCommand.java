package net.narusas.aceauction.data.updater;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.narusas.aceauction.fetchers.��������Fetcher_���;
import net.narusas.aceauction.model.����;

public class ���_���FetchCommand implements Runnable {
	static Logger logger = Logger.getLogger("log");

	private final List<String> sa_nos;

	private final ���� ����;

	public ���_���FetchCommand(���� ����, List<String> sa_nos) {
		this.���� = ����;
		this.sa_nos = sa_nos;
	}

	public void run() {
		try {
			logger.info("��� Fetcher�� �����մϴ�. ");
			��������Fetcher_��� fetcher = new ��������Fetcher_���(����.get����(), ����);
			logger.info("����� Fetcher�մϴ�.  ");
			String[] pages = fetcher.getPages();
			logger.info("������ Page���� ����� �м��մϴ�. ");
			for (String page : pages) {
				List<String> temp = fetcher.parse(page);
				for (String sa_no : temp) {
					sa_nos.add(sa_no);
				}
			}

		} catch (Exception ex) {
			logger.log(Level.INFO, "����� �������µ� �����߽��ϴ�", ex);
			ex.printStackTrace();

		}
	}

}
