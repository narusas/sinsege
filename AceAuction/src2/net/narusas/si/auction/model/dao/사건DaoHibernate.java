package net.narusas.si.auction.model.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.db.GenericDaoHibernate;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

public class 사건DaoHibernate extends GenericDaoHibernate<사건, Long> implements 사건Dao {

	public 사건DaoHibernate() {
		super(사건.class);
	}

	@Override
	public List<사건> findBy(final 담당계 charge) {
		HibernateCallback callback = new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createCriteria(사건.class).add(Restrictions.eq("담당계Id", charge.getId())).list();
			}
		};
		List<사건> temp = (List<사건>) getHibernateTemplate().execute(callback);
		for (사건 사건 : temp) {
			사건.set담당계(charge);
		}
		return temp;
	}

	@Override
	public 사건 find(법원 법원, long 사건번호) {
		DetachedCriteria crit = DetachedCriteria.forClass(사건.class);
		HashMap<String, Object> target = new HashMap<String, Object>();
		target.put("법원", 법원);
		target.put("사건번호", 사건번호);
		crit.add(Restrictions.allEq(target));
		List res = getHibernateTemplate().findByCriteria(crit);
		if (res == null || res.size() == 0) {
			return null;
		}
		System.out.println("######## " + res);
		사건 event = (사건) res.get(0);
		return update담당계(event);
	}

	private 사건 update담당계(사건 event) {
		담당계Dao chargeDao = (담당계Dao) App.context.getBean("담당계DAO");
		담당계 charge = chargeDao.get(event.get담당계Id());
		if (charge != null) {
			event.set담당계(charge);
		}
		return event;
	}

	@Override
	public 사건 find(Long id) {
		DetachedCriteria crit = DetachedCriteria.forClass(사건.class);
		HashMap<String, Object> target = new HashMap<String, Object>();
		target.put("id", id);
		crit.add(Restrictions.allEq(target));
		List res = getHibernateTemplate().findByCriteria(crit);
		if (res == null || res.size() == 0) {
			return null;
		}
		System.out.println("######## " + res);
		사건 event = (사건) res.get(0);
		return update담당계(event);
	}

}
