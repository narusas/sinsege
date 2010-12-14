package net.narusas.si.auction.model.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import net.narusas.si.auction.db.GenericDaoHibernate;
import net.narusas.si.auction.model.기일;
import net.narusas.si.auction.model.당사자;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.사건;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

public class 당사자DaoHibernate extends GenericDaoHibernate<당사자, Long> implements 당사자Dao {

	public 당사자DaoHibernate() {
		super(당사자.class);
	}

	public List<당사자> findBy(final 사건 event) {
		HibernateCallback callback = new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createCriteria(당사자.class).add(Restrictions.eq("사건", event)).list();
			}
		};
		return (List<당사자>) getHibernateTemplate().execute(callback);
	}

	
	@Override
	public void removeFor(사건 event) {
		if (event.getId() == null){
			return;
		}
		DetachedCriteria crit = DetachedCriteria.forClass(당사자.class);
		HashMap<String, Object> target = new HashMap<String, Object>();
		target.put("사건", event);

		crit.add(Restrictions.allEq(target));
		List res = getHibernateTemplate().findByCriteria(crit);
		
		if (res != null) {
			getHibernateTemplate().deleteAll(res);
			for (Object object : res) {
				당사자 people = (당사자) object;
				getHibernateTemplate().delete(people);
			}
		}

	}
}
