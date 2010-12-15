package net.narusas.aceauction.ui;

import java.util.List;
import java.util.logging.Logger;

import net.narusas.aceauction.fetchers.��������Fetcher;
import net.narusas.aceauction.interaction.Alert;
import net.narusas.aceauction.interaction.ProgressBar;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.���;

public class ���FetchTask implements Runnable {

	static Logger logger = Logger.getLogger("log");

	private final ���ListModel model;

	private final BeansTableModel tableModel;
	private final ���� ����;

	public ���FetchTask(���ListModel model, ���� ����, BeansTableModel tableModel) {
		this.model = model;
		this.���� = ����;
		this.tableModel = tableModel;
	}

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
