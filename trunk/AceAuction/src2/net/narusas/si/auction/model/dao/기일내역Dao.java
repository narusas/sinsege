package net.narusas.si.auction.model.dao;

import net.narusas.si.auction.db.GenericDao;
import net.narusas.si.auction.model.기일;
import net.narusas.si.auction.model.물건;

public interface 기일내역Dao extends GenericDao<기일, Long> {
	void removeFor(물건 물건);
}
