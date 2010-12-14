package net.narusas.si.auction.model.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import net.narusas.si.auction.db.GenericDaoHibernate;
import net.narusas.si.auction.model.기일;
import net.narusas.si.auction.model.등기부등본;
import net.narusas.si.auction.model.물건;

public class 등기부등본DaoHibernate extends GenericDaoHibernate<등기부등본, Long> implements 등기부등본Dao {

	public 등기부등본DaoHibernate() {
		super(등기부등본.class);
	}

	@Override
	public 등기부등본 get(물건 물건, String type) {
		DetachedCriteria crit = DetachedCriteria.forClass(등기부등본.class);
		crit.add(Restrictions.eq("물건", 물건)).add(Restrictions.eq("type", type));

		List res = getHibernateTemplate().findByCriteria(crit);
		if (res == null || res.size() == 0) {
			return null;

		}
		return (등기부등본) res.get(0);
	}

	@Override
	public Collection<등기부등본> get(물건 물건) {
		DetachedCriteria crit = DetachedCriteria.forClass(등기부등본.class);
		crit.add(Restrictions.eq("물건", 물건));

		List res = getHibernateTemplate().findByCriteria(crit);
		if (res == null || res.size() == 0) {
			return null;

		}
		return res;
	}

	public void removeFor(물건 goods) {
		if (goods.getId() == null) {
			return;
		}
		DetachedCriteria crit = DetachedCriteria.forClass(등기부등본.class);
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
	
	public void removeFor(물건 goods, String type) {
		if (goods.getId() == null) {
			return;
		}
		DetachedCriteria crit = DetachedCriteria.forClass(등기부등본.class);
		HashMap<String, Object> target = new HashMap<String, Object>();
		target.put("물건", goods);
		target.put("type", type);

		crit.add(Restrictions.allEq(target));
		List res = getHibernateTemplate().findByCriteria(crit);
		if (res != null) {
			for (Object object : res) {
				getHibernateTemplate().delete(object);
			}
		}

	}

}
