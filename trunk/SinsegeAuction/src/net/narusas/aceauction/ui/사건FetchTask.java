/*
 * 
 */
package net.narusas.aceauction.ui;

import java.util.List;
import java.util.logging.Logger;

import net.narusas.aceauction.fetchers.��������Fetcher;
import net.narusas.aceauction.interaction.Alert;
import net.narusas.aceauction.interaction.ProgressBar;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.���;

// TODO: Auto-generated Javadoc
/**
 * The Class ���FetchTask.
 */
public class ���FetchTask implements Runnable {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The model. */
	private final ���ListModel model;

	/** The table model. */
	private final BeansTableModel tableModel;
	
	/** The ����. */
	private final ���� ����;

	/**
	 * Instantiates a new ��� fetch task.
	 * 
	 * @param model the model
	 * @param ���� the ����
	 * @param tableModel the table model
	 */
	public ���FetchTask(���ListModel model, ���� ����, BeansTableModel tableModel) {
		this.model = model;
		this.���� = ����;
		this.tableModel = tableModel;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		logger.info("���̺��� �����մϴ�");
		tableModel.clear();
		tableModel.setBeans(����);
		model.clear();
		if (���� == null) {
			return;
		}
		try {
			logger.info("��� Fetcher�� �����մϴ�. ");
			��������Fetcher fetcher = new ��������Fetcher(����.get����(), ����);
			logger.info("����� Fetcher�մϴ�.  ");
			List<���> sagun = fetcher.getSaguns();
			logger.info("Table�� �����մϴ�. ");

			for (int i = 0; i < sagun.size(); i++) {
				model.addElement(sagun.get(i));
			}
		} catch (Exception e) {
			ProgressBar.getInstance().canceled(e.getMessage());
			Alert.getInstance().alert(e.getMessage(), e);
		}
	}
}
