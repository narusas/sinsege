/*
 * 
 */
package net.narusas.aceauction.ui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import net.narusas.aceauction.model.Row;
import net.narusas.aceauction.model.Table;
import net.narusas.aceauction.model.���ϳ���Item;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.������Ȳ;
import net.narusas.aceauction.model.���ǵ���ǹ��ǻ󼼳���;
import net.narusas.aceauction.model.���ÿܰǹ�;

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
	private final ���ListModel listModel;

	/** The ���. */
	private final String ���;

	/** The ��ϼ���. */
	private final String ��ϼ���;
	
	/** The ����. */
	private final ���� ����;
	
	/**
	 * Instantiates a new ��� task.
	 * 
	 * @param ���� the ����
	 * @param ��� the ���
	 * @param ��ϼ��� the ��ϼ���
	 * @param infoTableModel the info table model
	 * @param listModel the list model
	 */
	public ���Task(���� ����, String ���, String ��ϼ���, BeansTableModel infoTableModel,
			���ListModel listModel) {
		this.���� = ����;
		this.��� = ���;
		this.��ϼ��� = ��ϼ���;
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
			logger.info("������Ȳ�� ��Ͽ� ǥ���մϴ�. ");
			update(detail.get���ÿܰǹ���Ȳ());
		}
		if ("���ⱸ".equals(���)) {
			logger.info("������Ȳ�� ��Ͽ� ǥ���մϴ�. ");
			update(detail.get���ⱸ());
		}

		if ("����� ���ϳ���".equals(���)) {
			logger.info("����� ���ϳ����� ��Ͽ� ǥ���մϴ�. ");
			update(����.get���ϳ���());
		}
		if ("�Ű����Ǹ���".equals(���)) {
			logger.info("����� ���ϳ����� ��Ͽ� ǥ���մϴ�. ");
			update(����.get����());
		}

		if ("����".equals(���)) {
			logger.info("������ ���ϴ�. ");
			showImage(��ϼ���.substring(��ϼ���.indexOf(" ") + 1).trim());
		}

		if ("�����������Ȳ".equals(���)) {
			logger.info("������ǹ� ������ ��Ͽ� ǥ���մϴ�. ");
			update������Ȳ(����.get������Ȳ());
		}

		if ("��������ÿܰǹ�".equals(���)) {
			logger.info("��������ÿܰǹ� ������ ��Ͽ� ǥ���մϴ�. ");
			update(����.get���ÿܰǹ�s());
		}

	}

	/**
	 * Show image.
	 * 
	 * @param url the url
	 */
	private void showImage(String url) {
		logger.info(url + "�� ���� ������ ���ɴϴ�. ");
		try {
			URL u = new URL(url);
			BufferedImage img = ImageIO.read(u.openStream());
			ImageViewer viewer = new ImageViewer(url, img);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update.
	 * 
	 * @param ���ÿܰǹ�s the ���ÿܰǹ�s
	 */
	private void update(LinkedList<���ÿܰǹ�> ���ÿܰǹ�s) {
		int index = Integer.parseInt(��ϼ���.substring(0, ��ϼ���.indexOf(" ")).trim());
		���ÿܰǹ� �ǹ� = ���ÿܰǹ�s.get(index - 1);
		infoTableModel.setBeans(�ǹ�);
	}

	/**
	 * Update.
	 * 
	 * @param ���ϳ��� the ���ϳ���
	 */
	private void update(List<���ϳ���Item> ���ϳ���) {
		for (int i = 0; i < ���ϳ���.size(); i++) {
			if (String.valueOf(i + 1).equals(��ϼ���.substring(0, ��ϼ���.indexOf(" ")))) {
				���ϳ���Item item = ���ϳ���.get(i);
				infoTableModel.setBeans(item);
				return;
			}
		}

	}

	/**
	 * Update.
	 * 
	 * @param status the status
	 */
	private void update(Table status) {
		List<Row> r = status.getRows();
		if ("��Ÿ��Ȳ".equals(r.get(0))) {
			infoTableModel.addRow(new Object[] { r.get(0), r.get(1) });
		} else {
			for (Row record : r) {
				if (��ϼ���.equals(record.getValue(0))) {
					List<String> headers = status.getHeaders();

					for (int i = 0; i < record.getValues().size(); i++) {
						String header = headers.get(i);
						infoTableModel.addRow(new Object[] { header, record.getValue(i) });
					}
					break;
				}
			}
		}
	}

	/**
	 * Update.
	 * 
	 * @param ���� the ����
	 */
	private void update(���� ����) {
		List<Row> records = ����.get������().getRows();
		for (Row record : records) {
			if (��ϼ���.equals(record.getValue(0))) {
				List<String> headers = ����.get������().getHeaders();

				for (int i = 0; i < record.getValues().size(); i++) {
					String header = headers.get(i);
					infoTableModel.addRow(new Object[] { header, record.getValue(i) });
				}
				break;
			}
		}
	}

	/**
	 * Update������Ȳ.
	 * 
	 * @param �ǹ�s the �ǹ�s
	 */
	private void update������Ȳ(List<������Ȳ> �ǹ�s) {
		int index = Integer.parseInt(��ϼ���.substring(0, ��ϼ���.indexOf(" ")).trim());
		������Ȳ �ǹ� = �ǹ�s.get(index - 1);
		infoTableModel.setBeans(�ǹ�);
	}
}
