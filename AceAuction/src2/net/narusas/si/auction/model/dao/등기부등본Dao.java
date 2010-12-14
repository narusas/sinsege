package net.narusas.si.auction.model.dao;

import java.util.Collection;

import net.narusas.si.auction.db.GenericDao;
import net.narusas.si.auction.model.등기부등본;
import net.narusas.si.auction.model.물건;

public interface 등기부등본Dao extends GenericDao<등기부등본, Long> {

	등기부등본 get(물건 물건, String type);

	Collection<등기부등본> get(물건 물건);

	void removeFor(물건 goods, String type);

}
