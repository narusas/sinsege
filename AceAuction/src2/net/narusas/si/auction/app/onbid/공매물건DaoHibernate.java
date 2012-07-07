package net.narusas.si.auction.app.onbid;

import java.util.List;

import net.narusas.si.auction.db.GenericDaoHibernate;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;

public class 공매물건DaoHibernate extends GenericDaoHibernate<공매물건, Long> implements 공매물건Dao {

	protected 공매물건DaoHibernate() {
		super(공매물건.class);
	}

	public 공매물건 find(공매물건 item) {
		DetachedCriteria crit = DetachedCriteria.forClass(공매물건.class);
		crit.add(
//						Restrictions.and(
//								Restrictions.eq("입찰번호", item.get입찰번호()), 
				Expression.eq("물건관리번호", item.get물건관리번호())
//						)
				);
		// crit.add(Restrictions.eq("입찰번호", item.get입찰번호()));
		List matched = getHibernateTemplate().findByCriteria(crit);
		if (matched == null || matched.size() == 0) {
			return null;
		}
		return (공매물건) matched.get(0);
	}
	public 공매물건 findBy입찰번호(공매물건 item) {
		DetachedCriteria crit = DetachedCriteria.forClass(공매물건.class);
		crit.add(
//						Restrictions.and(
					Expression.eq("입찰번호", item.get입찰번호())
//								Restrictions.eq("물건관리번호", item.get물건관리번호())
//						)
				);
		// crit.add(Restrictions.eq("입찰번호", item.get입찰번호()));
		List matched = getHibernateTemplate().findByCriteria(crit);
		if (matched == null || matched.size() == 0) {
			return null;
		}
		return (공매물건) matched.get(0);
	}
}
