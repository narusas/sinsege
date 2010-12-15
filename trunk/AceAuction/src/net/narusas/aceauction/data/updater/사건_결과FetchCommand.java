package net.narusas.aceauction.data.updater;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.narusas.aceauction.fetchers.대법원사건Fetcher_결과;
import net.narusas.aceauction.model.담당계;

public class 사건_결과FetchCommand implements Runnable {
	static Logger logger = Logger.getLogger("log");

	private final List<String> sa_nos;

	private final 담당계 담당계;

	public 사건_결과FetchCommand(담당계 담당계, List<String> sa_nos) {
		this.담당계 = 담당계;
		this.sa_nos = sa_nos;
	}

	public void run() {
		try {
			logger.info("사건 Fetcher를 생성합니다. ");
			대법원사건Fetcher_결과 fetcher = new 대법원사건Fetcher_결과(담당계.get법원(), 담당계);
			logger.info("사건을 Fetcher합니다.  ");
			String[] pages = fetcher.getPages();
			logger.info("가져온 Page에서 사건을 분석합니다. ");
			for (String page : pages) {
				List<String> temp = fetcher.parse(page);
				for (String sa_no : temp) {
					sa_nos.add(sa_no);
				}
			}

		} catch (Exception ex) {
			logger.log(Level.INFO, "사건을 가져오는데 실패했습니다", ex);
			ex.printStackTrace();

		}
	}

}
