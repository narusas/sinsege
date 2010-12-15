package net.narusas.aceauction.ui;

import java.util.List;
import java.util.logging.Logger;

import net.narusas.aceauction.fetchers.대법원사건Fetcher;
import net.narusas.aceauction.interaction.Alert;
import net.narusas.aceauction.interaction.ProgressBar;
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.사건;

public class 사건FetchTask implements Runnable {

	static Logger logger = Logger.getLogger("log");

	private final 사건ListModel model;

	private final BeansTableModel tableModel;
	private final 담당계 담당계;

	public 사건FetchTask(사건ListModel model, 담당계 담당계, BeansTableModel tableModel) {
		this.model = model;
		this.담당계 = 담당계;
		this.tableModel = tableModel;
	}

	public void run() {
		logger.info("테이블을 정리합니다");
		tableModel.clear();
		tableModel.setBeans(담당계);
		model.clear();
		if (담당계 == null) {
			return;
		}
		try {
			logger.info("사건 Fetcher를 생성합니다. ");
			대법원사건Fetcher fetcher = new 대법원사건Fetcher(담당계.get법원(), 담당계);
			logger.info("사건을 Fetcher합니다.  ");
			List<사건> sagun = fetcher.getSaguns();
			logger.info("Table을 갱신합니다. ");

			for (int i = 0; i < sagun.size(); i++) {
				model.addElement(sagun.get(i));
			}
		} catch (Exception e) {
			ProgressBar.getInstance().canceled(e.getMessage());
			Alert.getInstance().alert(e.getMessage(), e);
		}
	}
}
