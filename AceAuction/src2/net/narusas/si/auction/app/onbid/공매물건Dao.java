package net.narusas.si.auction.app.onbid;

import net.narusas.si.auction.db.GenericDao;

public interface 공매물건Dao extends GenericDao<공매물건, Long>{

	공매물건 find(공매물건 item);
}
