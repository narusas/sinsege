package net.narusas.aceauction.ui;

import java.util.logging.Logger;

import javax.swing.DefaultListModel;

import net.narusas.aceauction.fetchers.������⺻����Fetcher;
import net.narusas.aceauction.fetchers.��������ϳ���Fetcher;
import net.narusas.aceauction.fetchers.��������ÿܰǹ�Fetcher;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.���;

public class ����ListModel extends DefaultListModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4971337582135501164L;
	static Logger logger = Logger.getLogger("log");

	public void update(��� s) {
		clear();
		logger.info("��������� �⺻������ ���ɴϴ�");
		������⺻����Fetcher f = new ������⺻����Fetcher();
		f.update(s);

		logger.info("��������� ���ϳ����� ���ɴϴ�");

		��������ϳ���Fetcher f2 = new ��������ϳ���Fetcher();
		f2.update(s);

		logger.info("��������� ���ǳ���(���ÿܰǹ�)�� ���ɴϴ�");

		��������ÿܰǹ�Fetcher f3 = new ��������ÿܰǹ�Fetcher();
		f3.update(s);

		logger.info("��������� ������ �Ϸ� �Ǿ����ϴ�. ");

		if (s == null) {
			return;
		}

		����[] m = s.get����s();
		for (int i = 0; i < m.length; i++) {
			addElement(m[i]);
		}
	}

}
