package net.narusas.aceauction.ui;

import java.util.logging.Logger;

import net.narusas.aceauction.model.���;

public class ���Task implements Runnable {

	static Logger logger = Logger.getLogger("log");

	private final BeansTableModel infoTableModel;

	private final ����ListModel listModel;

	private final ����ListModel listModel2;
	private final ��� ���;

	public ���Task(����ListModel listModel, BeansTableModel infoTableModel, ��� ���,
			����ListModel listModel2) {
		this.listModel = listModel;
		this.infoTableModel = infoTableModel;
		this.��� = ���;
		this.listModel2 = listModel2;
	}

	public void run() {
		logger.info("���̺��� �ʱ�ȭ�մϴ�");
		listModel.clear();
		infoTableModel.clear();
		if (��� == null) {
			logger.info("���õ� ����� �����ϴ�.");
			return;
		}
		logger.info("����� �߰������� ������ ȹ���ؿ� �����մϴ�.");
		listModel.update(���);
		logger.info("���̺��� �����մϴ�.");
		infoTableModel.setBeans(���);
	}

}
