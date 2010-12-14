package net.narusas.si.auction.model.dao;

import java.util.List;

import net.narusas.si.auction.db.GenericDao;
import net.narusas.si.auction.model.지역;

public interface 지역Dao extends GenericDao<지역, Integer> {
	List<지역> getMainAreas();
	

	지역 match(String 지역명);

	지역 findSub(지역 parent, String sub지역명);
}
