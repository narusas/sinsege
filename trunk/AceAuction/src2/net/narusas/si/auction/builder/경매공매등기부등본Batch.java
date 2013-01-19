package net.narusas.si.auction.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.fetchers.경매공매사이트Fetcher;
import net.narusas.si.auction.fetchers.경매공매사이트Fetcher.사건;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.dao.등기부등본Dao;
import net.narusas.si.auction.model.dao.물건Dao;
import net.narusas.si.auction.model.dao.사건Dao;

public class 경매공매등기부등본Batch extends Thread {
	final Logger logger = LoggerFactory.getLogger("auction");
	List<법원> courtList = new ArrayList<법원>();
	private 경매공매사이트Fetcher f;

	public 경매공매등기부등본Batch(Object[] selected법원s) {
		for (Object c : selected법원s) {
			courtList.add((법원) c);
		}
		try {
			f = new 경매공매사이트Fetcher();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		f.login();
		사건Dao eventDao = (사건Dao) App.context.getBean("사건DAO");
		물건Dao dao = (물건Dao) App.context.getBean("물건DAO");
		등기부등본Dao dao2 = (등기부등본Dao) App.context.getBean("등기부등본DAO");

		for (법원 court : courtList) {
			try {
				logger.info("경매공매 사이트에서 사건 목록일 가져 옵니다.");
				List<경매공매사이트Fetcher.사건> eventList = f.fetch사건목록(court);
				logger.info(""+eventList.size()+"개의 사건을 가져 왔습니다.");
				for (경매공매사이트Fetcher.사건 event : eventList) {
					logger.info(""+event.get사건번호()+" 사건에 대하 작업을 시작합니다.");
					net.narusas.si.auction.model.사건 s = eventDao.find(court,event.get사건번호());
					String html = f.fetch사건상세(event);
					f.parse등기부등본(event, html);
					logger.info("건물 등기부등본:"+event.get건물등기부등본());
					logger.info("토지 등기부등본:"+event.get토지등기부등본());
					List<물건> goodsList = dao.get(s);
					if (goodsList == null){
						continue;
					}
					
					for (물건 물건 : goodsList) {
						update등기부등본(물건, "http://img.ch24.co.kr/files/certify/"+event.get건물등기부등본(), "building");
						update등기부등본(물건, "http://img.ch24.co.kr/files/certify/"+event.get토지등기부등본(), "land");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	void update등기부등본(물건 물건, String url, String type) {
		if (url == null || "".equals(url) || url.startsWith("http") == false) {
			return;
		}
		등기부등본Builder db = new 등기부등본Builder(물건, url, 물건.get사건().get소유자(), type);
		try {
			db.execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
