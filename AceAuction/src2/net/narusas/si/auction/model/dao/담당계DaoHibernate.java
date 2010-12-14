package net.narusas.si.auction.model.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.narusas.si.auction.db.GenericDaoHibernate;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.법원;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;

public class 담당계DaoHibernate extends GenericDaoHibernate<담당계, Long> implements 담당계Dao {

	public 담당계DaoHibernate() {
		super(담당계.class);
	}

	public 담당계 get(법원 소속법원, int 담당계코드, Date 매각기일) {
		DetachedCriteria crit = DetachedCriteria.forClass(담당계.class);
		Date d1 = new Date(매각기일.getTime() );
		Date d2 = new Date(매각기일.getTime() + 1);
		crit.add(Expression.eq("소속법원", 소속법원)).add(Expression.eq("담당계코드", 담당계코드)).add(Expression.eq("매각기일", 매각기일));
		List res = getHibernateTemplate().findByCriteria(crit);

//		System.out.println(res);
		if (res == null || res.size() == 0) {
			DetachedCriteria crit2 = DetachedCriteria.forClass(담당계.class);
			crit2.add(Expression.eq("소속법원", 소속법원)).add(Expression.eq("담당계코드", 담당계코드)).add(Expression.ge("매각기일", d1)).add(
					Expression.lt("매각기일", d2));
			res = getHibernateTemplate().findByCriteria(crit2);
			if (res != null && res.size() > 0) {
				return (담당계) res.get(0);
			}
			return null;
		}

		return (담당계) res.get(0);
	}

	@Override
	public List<담당계> find(법원 법원, Date start, Date end) {
		start = new Date(start.getTime());
		end = new Date(end.getTime()+1);
		DetachedCriteria crit = DetachedCriteria.forClass(담당계.class);
		if (start != null && end != null) {
			crit.add(Restrictions.and(Restrictions.eq("소속법원", 법원), Restrictions.between("매각기일", start, end)));
		} else if (start != null && end == null) {
			crit.add(Restrictions.and(Restrictions.eq("소속법원", 법원), Restrictions.ge("매각기일", start)));
		} else if (start == null && end != null) {
			crit.add(Restrictions.and(Restrictions.eq("소속법원", 법원), Restrictions.le("매각기일", end)));
		} else {
			return null;
		}

		return getHibernateTemplate().findByCriteria(crit);
	}

}
