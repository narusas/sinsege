package net.narusas.si.auction.model.dao;

import net.narusas.si.auction.db.GenericDao;
import net.narusas.si.auction.model.매각물건명세서;
import net.narusas.si.auction.model.물건;

public interface 매각물건명세서Dao  extends GenericDao<매각물건명세서, Long> {

	void removeFor(물건 goods);

}
