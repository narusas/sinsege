/*
 * 
 */
package net.narusas.aceauction.ui;

import java.util.List;
import java.util.logging.Logger;

import net.narusas.aceauction.fetchers.����Fetcher;
import net.narusas.aceauction.interaction.Alert;
import net.narusas.aceauction.interaction.ProgressBar;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;

// TODO: Auto-generated Javadoc
/**
 * The Class ����FetchTask.
 */
public class ����FetchTask implements Runnable {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The court. */
	private final ���� court;
	
	/** The model. */
	private final ����ListModel model;
	
	/**
	 * Instantiates a new ���� fetch task.
	 * 
	 * @param court the court
	 * @param model the model
	 */
	public ����FetchTask(���� court, ����ListModel model) {
		this.court = court;
		this.model = model;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		logger.info("���̺��� �����մϴ�");
		model.clear();
		if (court == null) {
			logger.info("������ ������ �����Ƿ� �����մϴ�.");

			return;
		}
		try {
			logger.info("Fetcher�� �����մϴ�. ");
			����Fetcher fetcher = ����Fetcher.get����Fetcher_�������(court);
			logger.info("���� ������ Fetch�մϴ�. ");
			List<����> list = fetcher.fetchCharges();
			for (int i = 0; i < list.size(); i++) {
				model.addElement(list.get(i));
			}
		} catch (Exception e) {
			Alert.getInstance().alert("���� ����� ������Ʈ �ϴ� ���߿� ������ �߻��߽��ϴ�.", e);
			ProgressBar.getInstance().canceled(e.getMessage());

		}
	}
}
