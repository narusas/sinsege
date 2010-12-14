package net.narusas.si.auction.model.dao;

import net.narusas.si.auction.db.GenericDaoHibernate;
import net.narusas.si.auction.model.등기부등본Item;

public class 등기부등본ItemDaoHibernate extends GenericDaoHibernate<등기부등본Item, Long> implements 등기부등본ItemDao {

	protected 등기부등본ItemDaoHibernate() {
		super(등기부등본Item.class);
	}

}
