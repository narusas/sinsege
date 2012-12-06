package net.narusas.si.auction.builder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.fetchers.스피드옥션Fetcher;
import net.narusas.si.auction.fetchers.스피드옥션ZoomPageFetcher;
import net.narusas.si.auction.fetchers.스피드옥션ZoomPageFetcher.등기부등본Links;
import net.narusas.si.auction.model.등기부등본;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.model.dao.등기부등본Dao;
import net.narusas.si.auction.model.dao.물건Dao;
import net.narusas.si.auction.model.dao.사건Dao;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 등기부등본ModeStrategy implements ModeStrategy {
	final Logger logger = LoggerFactory.getLogger("auction");
	private final 사건 사건;
	static 스피드옥션Fetcher f = 스피드옥션Fetcher.getInstance();

	public 등기부등본ModeStrategy(사건 사건) {
		this.사건 = 사건;
	}

	@Override
	public boolean execute() {
		logger.info("사건 (" + 사건 + " )의 등기부등본 처리를 시작합니다");
		try {
			사건Dao eventDao = (사건Dao) App.context.getBean("사건DAO");
			사건 event = eventDao.find(사건.get법원(), 사건.get사건번호());
			물건Dao dao = (물건Dao) App.context.getBean("물건DAO");
			등기부등본Dao dao2 = (등기부등본Dao) App.context.getBean("등기부등본DAO");

			List<물건> goodsList = dao.get(event);
			if (goodsList == null || goodsList.size() == 0) {
				logger.info("사건에 해당하는 물건이 DB에 입력되어있지 않습니다");
				return false;
			}
			법원 법원 = 사건.get법원();
			for (물건 물건 : goodsList) {
				Collection<등기부등본> attested = dao2.get(물건);
				if (attested != null) {
					logger.info("해당물건은 이미 등기부등본 처리가 완료된 물건입니다.");
					continue;
				}

				스피드옥션ZoomPageFetcher f = new 스피드옥션ZoomPageFetcher();
				등기부등본Links links = f.fetch(물건);

				logger.info("건물등기부등본 URL:" + links.get건물등기부등본PDFLink());
				logger.info("토지등기부등본 URL:" + links.get토지등기부등본PDFLink());
				update등기부등본(물건, links);

			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void update등기부등본(물건 물건, 등기부등본Links links) throws Exception {
		update등기부등본(물건, links.get건물등기부등본PDFLink(), "building");
		update등기부등본(물건, links.get토지등기부등본PDFLink(), "land");

	}

	private void update등기부등본(물건 물건, String url, String type) {
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
