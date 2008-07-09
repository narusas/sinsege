/*
 * 
 */
package net.narusas.aceauction.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import net.narusas.aceauction.fetchers.������Ű����Ǹ���Fetcher;
import net.narusas.aceauction.model.Row;
import net.narusas.aceauction.model.Table;
import net.narusas.aceauction.model.���ϳ���Item;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.������Ȳ;
import net.narusas.aceauction.model.���ǵ���ǹ��ǻ󼼳���;
import net.narusas.aceauction.model.���ÿܰǹ�;

// TODO: Auto-generated Javadoc
/**
 * The Class ��������Task.
 */
public class ��������Task implements Runnable {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The info table model. */
	private final BeansTableModel infoTableModel;

	/** The list model. */
	private final ���ListModel listModel;

	/** The ���. */
	private final String ���;
	
	/** The ����. */
	private final ���� ����;

	/**
	 * Instantiates a new �������� task.
	 * 
	 * @param ���� the ����
	 * @param ��� the ���
	 * @param infoTableModel the info table model
	 * @param listModel the list model
	 */
	public ��������Task(���� ����, String ���, BeansTableModel infoTableModel,
			���ListModel listModel) {
		this.���� = ����;
		this.��� = ���;
		this.infoTableModel = infoTableModel;
		this.listModel = listModel;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		logger.info("���̺��� �����մϴ�");
		infoTableModel.clear();
		if (���� == null || ��� == null) {
			logger.info("���� �Ǵ� ����� �����ϴ�. ");
			return;
		}

		���ǵ���ǹ��ǻ󼼳��� detail = ����.getDetail();
		if ("������".equals(���)) {
			logger.info("�������� ���̺� ǥ���մϴ�. ");
			listModel.clear();
			infoTableModel.setBeans(detail);
		}
		if ("�ǹ���Ȳ".equals(���)) {
			logger.info("�ǹ���Ȳ�� ��Ͽ� ǥ���մϴ�. ");
			update(detail.get�ǹ���Ȳ());
		}
		if ("��������Ȳ".equals(���)) {
			logger.info("��������Ȳ�� ��Ͽ� ǥ���մϴ�. ");
			update(detail.get��������Ȳ());
		}
		if ("�Ű�������Ȳ".equals(���)) {
			logger.info("�Ű�������Ȳ�� ��Ͽ� ǥ���մϴ�. ");
			update(detail.get�Ű�������Ȳ());
		}
		if ("������Ȳ".equals(���)) {
			logger.info("������Ȳ�� ��Ͽ� ǥ���մϴ�. ");
			update(detail.get������Ȳ());
		}
		if ("���ÿܰǹ���Ȳ".equals(���)) {
			logger.info("���ÿܰǹ���Ȳ�� ��Ͽ� ǥ���մϴ�. ");
			update(detail.get���ÿܰǹ���Ȳ());
		}
		if ("���ⱸ".equals(���)) {
			logger.info("���ⱸ�� ��Ͽ� ǥ���մϴ�. ");
			update(detail.get���ⱸ());
		}

		if ("����� ���ϳ���".equals(���)) {
			logger.info("����� ���ϳ����� ��Ͽ� ǥ���մϴ�. ");
			update(����.get���ϳ���());
		}

		if ("����".equals(���)) {
			logger.info("������Ͽ� ǥ���մϴ�. ");
			update����(����.get����());
		}

		if ("�Ű����Ǹ���".equals(���)) {
			update(����);
		}

		if ("�����������Ȳ".equals(���)) {
			update������Ȳ(����.get������Ȳ());
		}

		if ("��������ÿܰǹ�".equals(���)) {
			update(����.get���ÿܰǹ�s());
		}

	}

	/**
	 * Update.
	 * 
	 * @param ���ÿܰǹ�s the ���ÿܰǹ�s
	 */
	private void update(LinkedList<���ÿܰǹ�> ���ÿܰǹ�s) {
		listModel.clear();
		infoTableModel.clear();
		int i = 1;
		for (���ÿܰǹ� �ǹ� : ���ÿܰǹ�s) {
			listModel.addElement("" + i + " " + �ǹ�);
			i++;
		}
	}

	/**
	 * Update.
	 * 
	 * @param ���ϳ��� the ���ϳ���
	 */
	private void update(List<���ϳ���Item> ���ϳ���) {
		listModel.clear();
		infoTableModel.clear();
		int i = 1;
		for (���ϳ���Item record : ���ϳ���) {
			listModel.addElement("" + i + " " + record.get���ϰ��());
			i++;
		}
	}

	/**
	 * Update.
	 * 
	 * @param status the status
	 */
	private void update(Table status) {
		listModel.clear();
		infoTableModel.clear();
		for (Row record : status.getRows()) {
			listModel.addElement(record.getValue(0));
		}
	}

	/**
	 * Update.
	 * 
	 * @param ���� the ����
	 */
	private void update(���� ����) {
		logger.info("�Ű����Ǹ����� �����ɴϴ�.  ");
		������Ű����Ǹ���Fetcher fetcher = new ������Ű����Ǹ���Fetcher();
		fetcher.update(����);
		listModel.clear();
		infoTableModel.clear();
		infoTableModel.setBeans(����.get����());

		logger.info("�Ű����Ǹ����� ��Ͽ� ǥ���մϴ�. ");

		Table ������ = ����.get����().get������();
		List<Row> records = ������.getRows();
		for (Row record : records) {
			listModel.addElement(record.getValue(0));
		}
	}

	/**
	 * Update������Ȳ.
	 * 
	 * @param �ǹ�s the �ǹ�s
	 */
	private void update������Ȳ(List<������Ȳ> �ǹ�s) {
		listModel.clear();
		infoTableModel.clear();
		int i = 1;
		for (������Ȳ �ǹ� : �ǹ�s) {
			listModel.addElement("" + i + " " + �ǹ�);
			i++;
		}
	}

	/**
	 * Update����.
	 * 
	 * @param ���� the ����
	 */
	private void update����(List<String> ����) {
		listModel.clear();
		infoTableModel.clear();
		int i = 1;
		for (String url : ����) {
			listModel.addElement("" + i + " " + url);
			i++;
		}
	}

}
