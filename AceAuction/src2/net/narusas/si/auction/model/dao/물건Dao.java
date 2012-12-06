package net.narusas.si.auction.model.dao;

import java.util.Date;
import java.util.List;

import net.narusas.si.auction.db.GenericDao;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;

public interface 물건Dao extends GenericDao<물건, Integer> {

	물건 find(법원 법원, 담당계 담당계, 사건 사건, Integer 물건번호);

	List<물건> get(사건 event, Date 시작일, Date 종료일, String 결과종류);
	List<물건> get(사건 event);

	@Override
	public void update(물건 t);

}
