package net.narusas.si.auction.model.dao;

import net.narusas.si.auction.db.GenericDaoHibernate;
import net.narusas.si.auction.model.단지;

public class 단지DaoHibernate extends GenericDaoHibernate<단지, Long> implements 단지Dao {
	protected 단지DaoHibernate() {
		super(단지.class);
	}

}
