package net.narusas.si.auction.updater;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.model.dao.사건Dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 사건Updater extends Thread {
	final Logger logger = LoggerFactory.getLogger("auction");
	private final long 사건번호;
	private final 법원 법원;
	private final boolean useDone;
	private String 종류선택;

	public 사건Updater(법원 법원, long 사건번호, boolean useDone, String 종류선택) {
		super();
		this.법원 = 법원;
		this.사건번호 = 사건번호;
		this.useDone = useDone;
		this.종류선택 = 종류선택;
	}

	@Override
	public void run() {
		logger.info(법원.get법원명() + " " + 사건번호 + "의 기일내역을 갱신합니다. ");
		사건Dao 사건dao = (사건Dao) App.context.getBean("사건DAO");
		사건 old = 사건dao.find(법원, 사건번호);
		if (old == null) {
			logger.info("해당 사건이 DB에 입력되어 있지 않습나다");
		}
		new 경매결과Updater(old, null, useDone, 종류선택, null, null, false).execute();
	}

}
