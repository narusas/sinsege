/*
 * 
 */
package net.narusas.aceauction.ui;

import java.util.logging.Logger;

import net.narusas.aceauction.model.���;

// TODO: Auto-generated Javadoc
/**
 * The Class ���Task.
 */
public class ���Task implements Runnable {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The info table model. */
	private final BeansTableModel infoTableModel;

	/** The list model. */
	private final ����ListModel listModel;

	/** The list model2. */
	private final ����ListModel listModel2;
	
	/** The ���. */
	private final ��� ���;

	/**
	 * Instantiates a new ��� task.
	 * 
	 * @param listModel the list model
	 * @param infoTableModel the info table model
	 * @param ��� the ���
	 * @param listModel2 the list model2
	 */
	public ���Task(����ListModel listModel, BeansTableModel infoTableModel, ��� ���,
			����ListModel listModel2) {
		this.listModel = listModel;
		this.infoTableModel = infoTableModel;
		this.��� = ���;
		this.listModel2 = listModel2;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
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
