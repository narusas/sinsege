package net.narusas.si.auction.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.narusas.si.auction.db.GenericDaoHibernate;
import net.narusas.si.auction.model.지역;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public class 지역DaoHibernate extends GenericDaoHibernate<지역, Integer> implements 지역Dao {

	private ArrayList<지역> mains;

	protected 지역DaoHibernate() {
		super(지역.class);
	}

	@Override
	public List<지역> getMainAreas() {
		if (mains == null) {
			mains = new ArrayList<지역>();
			for (int i = 2; i <= 17; i++) {
				mains.add(get(i));
			}
		}
		return mains;
	}

	@Override
	public 지역 match(String 지역명) {
		Collection<지역> temp = mains;
		for (지역 지역 : temp) {
			지역 matched = match(지역, 지역명);
			if (matched != null) {
				return matched;
			}
		}
		return null;
	}

	지역 match(final 지역 area, final String 지역명) {
		Object res = getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				session.refresh(area);
				Collection<지역> temp = area.get하위지역();
				for (지역 지역 : temp) {
					System.out.println(지역.get지역명());
					if (지역명.contains(area.get지역명())) {
						지역 matched = match(지역, 지역명);
						return matched != null ? matched : 지역;
					}
				}
				return null;
			}
		});

		return (지역) res;
	}

	@Override
	public 지역 findSub(final 지역 area, final String 지역명) {
		Object res = getHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				session.refresh(area);
				Collection<지역> temp = area.get하위지역();
				for (지역 지역 : temp) {
//					System.out.println(지역.get지역명());
					if (지역명.equals(지역.get지역명())) {
						return 지역;
					}
				}
				return null;
			}
		});

		return (지역) res;
	}

}
