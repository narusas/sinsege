package net.narusas.si.auction.updater;

import java.util.List;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.fetchers.사건기일내역Fetcher;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.model.dao.기일내역Dao;
import net.narusas.si.auction.model.dao.물건Dao;
import net.narusas.si.auction.model.dao.사건Dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 물건Batch {
	final Logger logger = LoggerFactory.getLogger("auction");
	private List<Integer> ids;
	private 물건Dao 물건dao;
	private 사건Dao 사건dao;
	private 기일내역Dao 기일내역Dao;
	

	public 물건Batch(List<Integer> ids) {
		this.ids = ids;
		물건dao = (물건Dao) App.context.getBean("물건DAO");
		사건dao = (사건Dao) App.context.getBean("사건DAO");
		기일내역Dao = (기일내역Dao) App.context.getBean("기일내역DAO");
		
	}

	public void execute() {
		logger.info("물건 목록을 갱신합니다.");
		logger.info("대상 물건  목록 :"+ ids);
		for (Integer id: ids) {
			logger.info("다음 물건을 대상으로 작업을 시작합니다:"+ id);
			물건 goods = 물건dao.find(id);
			logger.info("물건:"+goods.getId());
			logger.info("물건이 속한 사건의  ID:"+goods.get사건().getId());
			사건  event =  사건dao.find(goods.get사건().getId());
			logger.info("물건이 속한 사건:"+event);
			new 경매결과Updater( event, null, true, "전체", null, null, true).execute();
			
			
		}
	}
}
