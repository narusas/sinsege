package net.narusas.si.auction.app.onbid;

import net.narusas.si.auction.db.GenericDaoHibernate;

public class 공매일정DaoHibernate extends GenericDaoHibernate<공매일정, Long> implements 공매일정Dao {

	protected 공매일정DaoHibernate() {
		super(공매일정.class);
	}

}
