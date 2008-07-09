/*
 * 
 */
package net.narusas.aceauction.ui;

import java.util.logging.Logger;

import net.narusas.aceauction.model.사건;

// TODO: Auto-generated Javadoc
/**
 * The Class 사건Task.
 */
public class 사건Task implements Runnable {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The info table model. */
	private final BeansTableModel infoTableModel;

	/** The list model. */
	private final 물건ListModel listModel;

	/** The list model2. */
	private final 물건ListModel listModel2;
	
	/** The 사건. */
	private final 사건 사건;

	/**
	 * Instantiates a new 사건 task.
	 * 
	 * @param listModel the list model
	 * @param infoTableModel the info table model
	 * @param 사건 the 사건
	 * @param listModel2 the list model2
	 */
	public 사건Task(물건ListModel listModel, BeansTableModel infoTableModel, 사건 사건,
			물건ListModel listModel2) {
		this.listModel = listModel;
		this.infoTableModel = infoTableModel;
		this.사건 = 사건;
		this.listModel2 = listModel2;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		logger.info("테이블을 초기화합니다");
		listModel.clear();
		infoTableModel.clear();
		if (사건 == null) {
			logger.info("선택된 사건이 없습니다.");
			return;
		}
		logger.info("사건을 추가적으로 정보를 획득해와 갱신합니다.");
		listModel.update(사건);
		logger.info("테이블을 갱신합니다.");
		infoTableModel.setBeans(사건);
	}

}
