package net.narusas.si.auction.updater;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.fetchers.사건기일내역Fetcher;
import net.narusas.si.auction.fetchers.사건내역Fetcher;
import net.narusas.si.auction.model.기일내역;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.model.dao.기일내역Dao;
import net.narusas.si.auction.model.dao.물건Dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 경매결과Updater {
	final Logger logger = LoggerFactory.getLogger("auction");
	private 사건 사건;
	private final boolean useDone;

	public 경매결과Updater(사건 사건, boolean useDone) {
		this.사건 = 사건;
		this.useDone = useDone;
	}

	public void execute() {
		물건Dao 물건dao = (물건Dao) App.context.getBean("물건DAO");
		List<물건> goodsList = 물건dao.get(사건);
		if (goodsList == null) {
			return;
		}
		사건기일내역Fetcher f = new 사건기일내역Fetcher();
		기일내역Dao 기일내역Dao = (기일내역Dao) App.context.getBean("기일내역DAO");
		try {
			사건.set물건목록(fake물건목록(goodsList));
			f.update(사건);

			for (물건 old물건 : goodsList) {
				logger.info(사건.get사건번호() + ":" + old물건.get물건번호() + "의 기일내역을 갱신합니다. ");
				if (old물건.is완료여부()) {
					logger.info(사건.get사건번호() + ":" + old물건.get물건번호() + "는 이미 완료된 물건입니다.");
					continue;
				}
				물건 newGoods = 사건.get물건By물건번호(old물건.get물건번호());
				기일내역 old기일 = old물건.get기일내역();
				old물건.merge(newGoods);
				if (isEmpty(newGoods) == false) {
					기일내역Dao.removeFor(old물건);
				} else {
					fetch기일결과(old물건);
					old물건.set기일내역(old기일);
				}

				// if (isEmpty(old물건)) {
				//					
				// }
				if (useDone) {
					old물건.set완료여부(true);
				}

				logger.info("유찰수 :" + old물건.get유찰수() + ", 결과=" + old물건.get기일결과() + ", 내역=" + old물건.get기일내역());
				물건dao.saveOrUpdate(old물건);
			}

		} catch (IOException e) {
			e.printStackTrace();
			logger.info(사건.get사건번호() + "의 기일내역 페이지를 얻어오지 못했습니다. ");
		}

	}

	private void fetch기일결과(물건 old물건) throws IOException {
		사건내역Fetcher f = new 사건내역Fetcher();

		// 물건내역Fetcher f = new 물건내역Fetcher();
		old물건.set기일결과(f.fetch기일결과(old물건.get사건()));
	}

	private boolean isEmpty(물건 old물건) {
		return old물건.get기일내역() == null || old물건.get기일내역().get기일목록() == null || old물건.get기일내역().get기일목록().size() == 0;
	}

	private List<물건> fake물건목록(List<물건> goodsList) {
		List<물건> res = new LinkedList<물건>();
		for (물건 물건 : goodsList) {
			물건 fake = new 물건();
			fake.set물건번호(물건.get물건번호());
			res.add(fake);
		}
		return res;
	}
}
