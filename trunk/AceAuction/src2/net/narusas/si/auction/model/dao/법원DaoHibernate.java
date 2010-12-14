package net.narusas.si.auction.model.dao;

import net.narusas.si.auction.db.GenericDaoHibernate;
import net.narusas.si.auction.model.법원;

public class 법원DaoHibernate extends GenericDaoHibernate<법원, Integer> implements 법원Dao {

	public 법원DaoHibernate() {
		super(법원.class);
	}

}
