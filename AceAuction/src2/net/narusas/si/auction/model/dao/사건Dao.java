package net.narusas.si.auction.model.dao;

import java.util.List;

import net.narusas.si.auction.db.GenericDao;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;

public interface 사건Dao extends GenericDao<사건, Long> {
	public List<사건> findBy(담당계 charge);

//	public 사건 find(법원 법원, 담당계 담당계, long 사건번호);

	public 사건 find(법원 법원, long 사건번호);
	
	public 사건 find(Long id);

}
