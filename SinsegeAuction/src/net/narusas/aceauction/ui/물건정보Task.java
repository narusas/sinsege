/*
 * 
 */
package net.narusas.aceauction.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import net.narusas.aceauction.fetchers.대법원매각물건명세서Fetcher;
import net.narusas.aceauction.model.Row;
import net.narusas.aceauction.model.Table;
import net.narusas.aceauction.model.기일내역Item;
import net.narusas.aceauction.model.물건;
import net.narusas.aceauction.model.물건현황;
import net.narusas.aceauction.model.스피드옥션물건상세내역;
import net.narusas.aceauction.model.제시외건물;

// TODO: Auto-generated Javadoc
/**
 * The Class 물건정보Task.
 */
public class 물건정보Task implements Runnable {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The info table model. */
	private final BeansTableModel infoTableModel;

	/** The list model. */
	private final 목록ListModel listModel;

	/** The 명령. */
	private final String 명령;
	
	/** The 물건. */
	private final 물건 물건;

	/**
	 * Instantiates a new 물건정보 task.
	 * 
	 * @param 물건 the 물건
	 * @param 명령 the 명령
	 * @param infoTableModel the info table model
	 * @param listModel the list model
	 */
	public 물건정보Task(물건 물건, String 명령, BeansTableModel infoTableModel,
			목록ListModel listModel) {
		this.물건 = 물건;
		this.명령 = 명령;
		this.infoTableModel = infoTableModel;
		this.listModel = listModel;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		logger.info("테이블을 정리합니다");
		infoTableModel.clear();
		if (물건 == null || 명령 == null) {
			logger.info("물건 또는 명령이 없습니다. ");
			return;
		}

		스피드옥션물건상세내역 detail = 물건.getDetail();
		if ("상세정보".equals(명령)) {
			logger.info("상세정보를 테이블에 표시합니다. ");
			listModel.clear();
			infoTableModel.setBeans(detail);
		}
		if ("건물현황".equals(명령)) {
			logger.info("건물현황을 목록에 표시합니다. ");
			update(detail.get건물현황());
		}
		if ("대지권현황".equals(명령)) {
			logger.info("대지권현황을 목록에 표시합니다. ");
			update(detail.get대지권현황());
		}
		if ("매각기일현황".equals(명령)) {
			logger.info("매각기일현황을 목록에 표시합니다. ");
			update(detail.get매각기일현황());
		}
		if ("토지현황".equals(명령)) {
			logger.info("토지현황을 목록에 표시합니다. ");
			update(detail.get토지현황());
		}
		if ("제시외건물현황".equals(명령)) {
			logger.info("제시외건물현황을 목록에 표시합니다. ");
			update(detail.get제시외건물현황());
		}
		if ("기계기구".equals(명령)) {
			logger.info("기계기구을 목록에 표시합니다. ");
			update(detail.get기계기구());
		}

		if ("대법원 기일내역".equals(명령)) {
			logger.info("대법원 기일내역을 목록에 표시합니다. ");
			update(물건.get기일내역());
		}

		if ("사진".equals(명령)) {
			logger.info("사진목록에 표시합니다. ");
			update사진(물건.get사진());
		}

		if ("매각물건명세서".equals(명령)) {
			update(물건);
		}

		if ("대법원물건현황".equals(명령)) {
			update물건현황(물건.get물건현황());
		}

		if ("대법원제시외건물".equals(명령)) {
			update(물건.get제시외건물s());
		}

	}

	/**
	 * Update.
	 * 
	 * @param 제시외건물s the 제시외건물s
	 */
	private void update(LinkedList<제시외건물> 제시외건물s) {
		listModel.clear();
		infoTableModel.clear();
		int i = 1;
		for (제시외건물 건물 : 제시외건물s) {
			listModel.addElement("" + i + " " + 건물);
			i++;
		}
	}

	/**
	 * Update.
	 * 
	 * @param 기일내역 the 기일내역
	 */
	private void update(List<기일내역Item> 기일내역) {
		listModel.clear();
		infoTableModel.clear();
		int i = 1;
		for (기일내역Item record : 기일내역) {
			listModel.addElement("" + i + " " + record.get기일결과());
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
	 * @param 물건 the 물건
	 */
	private void update(물건 물건) {
		logger.info("매각물건명세서를 가져옵니다.  ");
		대법원매각물건명세서Fetcher fetcher = new 대법원매각물건명세서Fetcher();
		fetcher.update(물건);
		listModel.clear();
		infoTableModel.clear();
		infoTableModel.setBeans(물건.get명세서());

		logger.info("매각물건명세서를 목록에 표시합니다. ");

		Table 점유자 = 물건.get명세서().get점유자();
		List<Row> records = 점유자.getRows();
		for (Row record : records) {
			listModel.addElement(record.getValue(0));
		}
	}

	/**
	 * Update물건현황.
	 * 
	 * @param 건물s the 건물s
	 */
	private void update물건현황(List<물건현황> 건물s) {
		listModel.clear();
		infoTableModel.clear();
		int i = 1;
		for (물건현황 건물 : 건물s) {
			listModel.addElement("" + i + " " + 건물);
			i++;
		}
	}

	/**
	 * Update사진.
	 * 
	 * @param 사진 the 사진
	 */
	private void update사진(List<String> 사진) {
		listModel.clear();
		infoTableModel.clear();
		int i = 1;
		for (String url : 사진) {
			listModel.addElement("" + i + " " + url);
			i++;
		}
	}

}
