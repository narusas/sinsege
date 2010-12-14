package net.narusas.si.auction.model.dao;

import net.narusas.si.auction.db.GenericDao;
import net.narusas.si.auction.model.당사자;
import net.narusas.si.auction.model.사건;

public interface 당사자Dao extends GenericDao<당사자, Long> {

	void removeFor(사건  event);

}
