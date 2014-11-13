package net.narusas.si.auction.updater;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.fetchers.매각예정_사건목록Fetcher;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.model.dao.담당계Dao;
import net.narusas.si.auction.model.dao.물건Dao;
import net.narusas.si.auction.model.dao.사건Dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 법원Updater extends Thread {
	final Logger logger = LoggerFactory.getLogger("auction");
	private final 법원 법원;
	private final Date start;
	private final Date end;
	static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd");
	private final boolean useDone;
	private String 결과종류;
	private boolean 완료물건_진행여부;

	public 법원Updater(법원 법원, Date start, Date end, boolean useDone, String 결과종류,boolean 완료물건_진행여부) {
		this.법원 = 법원;
		this.start = start;
		this.end = end;
		this.useDone = useDone;
		this.결과종류 = 결과종류;
		this.완료물건_진행여부 = 완료물건_진행여부;
	}

	@Override
	public void run() {
		logger.info(법원.get법원명() + "의 기일내역을 갱신합니다.");
		매각예정_사건목록Fetcher f = new 매각예정_사건목록Fetcher();
		사건Dao 사건dao = (사건Dao) App.context.getBean("사건DAO");
//		try {
//			logger.info(법원.get법원명() + "의 사건 목록을 매각결과검색에서 가져 옵니다. 사건수에 따라 시간이 많이 걸릴수 있습니다. ");
//			List<사건> 사건List = f.fetchAll(법원);
//			logger.info("가져온 사건의 수:{}", 사건List.size());
//			for (사건 fetched사건 : 사건List) {
//				사건 사건 = 사건dao.find(법원, fetched사건.get사건번호());
//				try {
//					new 경매결과Updater(사건, useDone, 결과종류, start, end, true).execute();
//				} catch (Exception e) {
//					e.printStackTrace();
//					logger.info("처리중에 오류가 발생했습니다:"+e.getMessage());
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			logger.info("처리중에 오류가 발생했습니다:"+e.getMessage());
//		}
//
		담당계Dao 담당계dao = (담당계Dao) App.context.getBean("담당계DAO");
		List<담당계> 담당계List = 담당계dao.find(법원, start, end);
		if (담당계List == null || 담당계List.size() == 0) {
			logger.info("지정된 기간안에 처리된 사건이 없습니다. ");
		}
//		사건Dao 사건dao = (사건Dao) App.context.getBean("사건DAO");
		for (담당계 담당계 : 담당계List) {
			List<사건> 사건List = 사건dao.findBy(담당계);
			for (사건 사건 : 사건List) {
				try {
					new 경매결과Updater(사건, null, useDone, 결과종류, start, end,완료물건_진행여부).execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		

	}

	private String format(Date d) {
		if (d != null) {
			return dateFormatter.format(d);
		}
		return "";
	}

}
