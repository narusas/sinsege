package net.narusas.si.auction.model.dao;

import java.util.HashMap;
import java.util.List;

import net.narusas.si.auction.db.GenericDaoHibernate;
import net.narusas.si.auction.model.기일;
import net.narusas.si.auction.model.물건;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class 기일내역DaoHibernate extends GenericDaoHibernate<기일, Long> implements 기일내역Dao {

	protected 기일내역DaoHibernate() {
		super(기일.class);
	}

	@Override
	public void removeFor(물건 goods) {
		if (goods.getId() == null) {
			return;
		}
		DetachedCriteria crit = DetachedCriteria.forClass(기일.class);
		HashMap<String, Object> target = new HashMap<String, Object>();
		target.put("물건", goods);

		crit.add(Restrictions.allEq(target));
		List res = getHibernateTemplate().findByCriteria(crit);
		if (res != null) {
			for (Object object : res) {
				getHibernateTemplate().delete(object);
			}
		}

	}

}
