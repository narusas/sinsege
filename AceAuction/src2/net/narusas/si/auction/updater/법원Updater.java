package net.narusas.si.auction.updater;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.model.dao.담당계Dao;
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

	public 법원Updater(법원 법원, Date start, Date end, boolean useDone, String 결과종류) {
		this.법원 = 법원;
		this.start = start;
		this.end = end;
		this.useDone = useDone;
		this.결과종류 = 결과종류;
	}

	@Override
	public void run() {
		logger.info(법원.get법원명() + "의 기일내역을 갱신합니다. (" + format(start) + " ~ " + format(end) + ")");

		담당계Dao 담당계dao = (담당계Dao) App.context.getBean("담당계DAO");
		List<담당계> 담당계List = 담당계dao.find(법원, start, end);
		if (담당계List == null || 담당계List.size() == 0) {
			logger.info("지정된 기간안에 처리된 사건이 없습니다. ");
		}
		사건Dao 사건dao = (사건Dao) App.context.getBean("사건DAO");
		for (담당계 담당계 : 담당계List) {
			List<사건> 사건List = 사건dao.findBy(담당계);
			for (사건 사건 : 사건List) {
				try {
					new 경매결과Updater(사건, useDone, 결과종류, start, end).execute();
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
