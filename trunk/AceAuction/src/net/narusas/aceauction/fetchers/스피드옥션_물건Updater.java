package net.narusas.aceauction.fetchers;

import java.io.IOException;
import java.util.logging.Logger;

import net.narusas.aceauction.model.����;

import org.apache.commons.httpclient.HttpException;

public class ���ǵ����_����Updater {
	static Logger logger = Logger.getLogger("log");

	public static void updateDetail(���� item, String[] gamjungInfo)
			throws HttpException, IOException {
		// if (items == null) {
		// return;
		// }
		// List<�ε���ǥ�ø��Item> item = items.getItem(this);

		���ǵ���ǹ��ǻ󼼳���Fetcher fetcher = new ���ǵ���ǹ��ǻ󼼳���Fetcher();
		String src = fetcher.getPage(item.���.court.get���ǵ����Code(),
				item.���.charge.get�����̸�(), item.���.getEventYear(), item.���
						.getEventNo(), String.valueOf(item.���ǹ�ȣ));
		item.setDetail(fetcher.parse(src));

		logger.info("���ε  URL�� ���� �غ� �մϴ�.  ");
		try {
			���ǵ����ZoomPageFetcher ����Fetcher = new ���ǵ����ZoomPageFetcher();
			����Fetcher.init(src);
			// ����Fetcher.fetch����(this, ����Fetcher.get����page());
			����Fetcher.fetch���ε(item, src);

		} catch (Exception ex) {
			logger.info(ex.getMessage());
			ex.printStackTrace();
		}
		logger.info("���ε  URL�� ���Խ��ϴ�.");
	}
}
