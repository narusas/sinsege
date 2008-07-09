/*
 * 
 */
package net.narusas.aceauction.ui2;

import java.util.List;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;

import net.narusas.aceauction.fetchers.����Fetcher;
import net.narusas.aceauction.interaction.Alert;
import net.narusas.aceauction.interaction.ProgressBar;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;

// TODO: Auto-generated Javadoc
/**
 * The Class ����_���FetchTask.
 */
public class ����_���FetchTask implements Runnable {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The charge list model. */
	private final DefaultListModel chargeListModel;
	
	/** The court. */
	private final ���� court;

	/**
	 * Instantiates a new ����_��� fetch task.
	 * 
	 * @param court the court
	 * @param chargeListModel the charge list model
	 */
	public ����_���FetchTask(���� court, DefaultListModel chargeListModel) {
		this.court = court;
		this.chargeListModel = chargeListModel;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		logger.info("���̺��� �����մϴ�");
		chargeListModel.clear();
		if (court == null) {
			logger.info("������ ������ �����Ƿ� �����մϴ�.");

			return;
		}
		try {

			logger.info("Fetcher�� �����մϴ�. ");
			����Fetcher fetcher = ����Fetcher.get����Fetcher_�Ű����(court);
			logger.info("���� ������ Fetch�մϴ�. ");
			List<����> list = fetcher.fetchCharges();
			for (int i = 0; i < list.size(); i++) {
				chargeListModel.addElement(list.get(i));
			}
		} catch (Exception e) {
			Alert.getInstance().alert("���� ����� ������Ʈ �ϴ� ���߿� ������ �߻��߽��ϴ�.", e);
			ProgressBar.getInstance().canceled(e.getMessage());
		}
	}

}
