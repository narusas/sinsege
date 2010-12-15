package net.narusas.aceauction.ui;

import java.util.logging.Logger;

import javax.swing.DefaultListModel;

import net.narusas.aceauction.fetchers.대법원기본내역Fetcher;
import net.narusas.aceauction.fetchers.대법원기일내역Fetcher;
import net.narusas.aceauction.fetchers.대법원제시외건물Fetcher;
import net.narusas.aceauction.model.물건;
import net.narusas.aceauction.model.사건;

public class 물건ListModel extends DefaultListModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4971337582135501164L;
	static Logger logger = Logger.getLogger("log");

	public void update(사건 s) {
		clear();
		logger.info("대법원에서 기본내역을 얻어옵니다");
		대법원기본내역Fetcher f = new 대법원기본내역Fetcher();
		f.update(s);

		logger.info("대법원에서 기일내역을 얻어옵니다");

		대법원기일내역Fetcher f2 = new 대법원기일내역Fetcher();
		f2.update(s);

		logger.info("대법원에서 물건내역(제시외건물)을 얻어옵니다");

		대법원제시외건물Fetcher f3 = new 대법원제시외건물Fetcher();
		f3.update(s);

		logger.info("사건정보의 갱신이 완료 되었습니다. ");

		if (s == null) {
			return;
		}

		물건[] m = s.get물건s();
		for (int i = 0; i < m.length; i++) {
			addElement(m[i]);
		}
	}

}
