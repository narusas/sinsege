package net.narusas.si.auction.builder;

import java.util.Collection;
import java.util.List;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.app.build.EventNotifier;
import net.narusas.si.auction.fetchers.물건내역Fetcher;
import net.narusas.si.auction.model.기일;
import net.narusas.si.auction.model.매각물건명세서;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.model.dao.기일내역Dao;
import net.narusas.si.auction.model.dao.매각물건명세서Dao;
import net.narusas.si.auction.model.dao.물건Dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 물건목록Batch {
	final Logger logger = LoggerFactory.getLogger("auction");
	private final List<물건> 물건목록;
	private final 사건 사건;

	public 물건목록Batch(사건 사건, List<물건> 물건목록) {
		this.사건 = 사건;
		this.물건목록 = 물건목록;
	}

	public void execute() {
		물건내역Fetcher f = new 물건내역Fetcher();
		EventNotifier.set물건목록Size(물건목록.size());
		for (int i = 0; i < 물건목록.size(); i++) {
			물건 goods = 물건목록.get(i);
			EventNotifier.progress물건(i, goods);

			logger.info("\n\n물건번호 " + goods.get물건번호() + " 작업을 시작합니다.");
			try {
				if (f.update(goods)) {
					save(goods);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("\n\n물건번호 " + goods.get물건번호() + " 작업을 종료합니다.");
		}
	}

	private void save(물건 goods) {
		물건Dao dao = (물건Dao) App.context.getBean("물건DAO");
		물건 old = dao.find(사건.get법원(), 사건.get담당계(), 사건, goods.get물건번호());
		if (old != null) {
			updateOld(goods, dao, old);
		} else { // DB에 저장되지 않은 새로운 물건일때
			saveNew(goods, dao);
		}

	}

	private void saveNew(물건 goods, 물건Dao dao) {
		dao.saveOrUpdate(goods);
		Collection<매각물건명세서> list = goods.get매각물건명세목록();
		if (list != null) {
			매각물건명세서Dao dao2 = (매각물건명세서Dao) App.context.getBean("매각물건명세서DAO");
			dao2.removeFor(goods);
			for (매각물건명세서 매각물건명세서 : list) {
				dao2.saveOrUpdate(매각물건명세서);
			}
		}
	}

	private void updateOld(물건 goods, 물건Dao dao, 물건 old) {
		기일내역Dao 기일내역Dao = (기일내역Dao) App.context.getBean("기일내역DAO");
		기일내역Dao.removeFor(old);
		old.merge(goods);
		old.set완료여부(false);
		try {
			for (기일  item : old.get기일내역().get기일목록()) {
				기일내역Dao.save(item);
			}
			
			dao.update(old);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
