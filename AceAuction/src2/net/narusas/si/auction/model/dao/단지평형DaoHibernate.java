package net.narusas.si.auction.model.dao;

import net.narusas.si.auction.db.GenericDaoHibernate;
import net.narusas.si.auction.model.단지평형;

public class 단지평형DaoHibernate extends GenericDaoHibernate<단지평형, Long> implements 단지평형Dao {

	public 단지평형DaoHibernate() {
		super(단지평형.class);
	}
}
