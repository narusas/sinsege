package net.narusas.si.auction.model.dao;

import java.util.Date;
import java.util.List;

import net.narusas.si.auction.db.GenericDao;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.법원;

public interface 담당계Dao extends GenericDao<담당계, Long> {
	담당계 get(법원 get소속법원, int get담당계코드, Date get매각기일);

	List<담당계> find(법원 법원, Date start, Date end);
}
