/*
 * 
 */
package net.narusas.aceauction.ui;

import java.util.List;
import java.util.logging.Logger;

import net.narusas.aceauction.fetchers.담당계Fetcher;
import net.narusas.aceauction.interaction.Alert;
import net.narusas.aceauction.interaction.ProgressBar;
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.법원;

// TODO: Auto-generated Javadoc
/**
 * The Class 담당계FetchTask.
 */
public class 담당계FetchTask implements Runnable {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The court. */
	private final 법원 court;
	
	/** The model. */
	private final 담당계ListModel model;
	
	/**
	 * Instantiates a new 담당계 fetch task.
	 * 
	 * @param court the court
	 * @param model the model
	 */
	public 담당계FetchTask(법원 court, 담당계ListModel model) {
		this.court = court;
		this.model = model;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		logger.info("테이블을 정리합니다");
		model.clear();
		if (court == null) {
			logger.info("지정된 법원이 없으므로 종료합니다.");

			return;
		}
		try {
			logger.info("Fetcher를 생성합니다. ");
			담당계Fetcher fetcher = 담당계Fetcher.get담당계Fetcher_경매진행(court);
			logger.info("담당계 정보를 Fetch합니다. ");
			List<담당계> list = fetcher.fetchCharges();
			for (int i = 0; i < list.size(); i++) {
				model.addElement(list.get(i));
			}
		} catch (Exception e) {
			Alert.getInstance().alert("담당계 목록을 업데이트 하는 도중에 문제가 발생했습니다.", e);
			ProgressBar.getInstance().canceled(e.getMessage());

		}
	}
}
