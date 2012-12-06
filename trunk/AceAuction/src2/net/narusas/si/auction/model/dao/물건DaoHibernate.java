package net.narusas.si.auction.model.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.narusas.si.auction.db.GenericDaoHibernate;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class 물건DaoHibernate extends GenericDaoHibernate<물건, Integer> implements 물건Dao {

	public 물건DaoHibernate() {
		super(물건.class);
	}

	@Override
	public 물건 find(final 법원 법원, final 담당계 담당계, final 사건 사건, final Integer 물건번호) {
		DetachedCriteria crit = DetachedCriteria.forClass(물건.class);
		HashMap<String, Object> target = new HashMap<String, Object>();
		target.put("법원", 법원);
		target.put("사건", 사건);
		target.put("물건번호", 물건번호);

		crit.add(Restrictions.allEq(target));
		List res = getHibernateTemplate().findByCriteria(crit);
		if (res == null || res.size() == 0) {
			return null;
		}
		물건 goods = (물건) res.get(0);
		goods.set담당계(담당계);
		goods.get사건().set담당계(담당계);
		return goods;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<물건> get(사건 event, Date 시작일, Date 종료일, String 결과종류) {
		DetachedCriteria crit = DetachedCriteria.forClass(물건.class);

		// if ("전체".equals(결과종류) ==false) {
		// crit.add(Restrictions.eq("사건", event));
		// crit.add(Restrictions.eq("기일결과", 결과종류));
		// }
		// else {
		HashMap<String, Object> target = new HashMap<String, Object>();
		target.put("사건", event);
		crit.add(Restrictions.allEq(target));
		// }

		List<물건> temp = getHibernateTemplate().findByCriteria(crit);
		if (temp == null || temp.size() == 0) {
			return null;
		}

		List<물건> res = new ArrayList<물건>();
		for (물건 goods : temp) {
			if (goods.get기일날자() == null) {
				continue;
			}
			if ("전체".equals(결과종류) == false) {
				Date 기일날자 = parse기일날자(goods.get기일날자());
				
				if (기일날자 == null || 기일날자.getTime() < 시작일.getTime()   || 기일날자.getTime() > 종료일.getTime()) {
					continue;
				}
				if (결과종류.equals(goods.get기일결과()) == false) {
					continue;
				}
			}

			goods.set담당계(event.get담당계());
			goods.get사건().set담당계(event.get담당계());
			res.add(goods);
		}
		return res;

	}
	

	private Date parse기일날자(String 기일날자) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(기일날자);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<물건> get(사건 event) {
		DetachedCriteria crit = DetachedCriteria.forClass(물건.class);
		HashMap<String, Object> target = new HashMap<String, Object>();
		target.put("사건", event);

		crit.add(Restrictions.allEq(target));
		List res = getHibernateTemplate().findByCriteria(crit);
		if (res == null || res.size() == 0) {
			return null;
		}
		for (Object object : res) {
			물건 goods = (물건) object;
			goods.set담당계(event.get담당계());
			goods.get사건().set담당계(event.get담당계());
		}
		return res;

	}

	@Override
	public void update(물건 t) {
		// if (t.get선박목록() != null && t.get선박목록().size()!=0){
		// 기일내역Dao 기일내역Dao = (기일내역Dao) App.context.getBean("기일내역DAO");
		// getHibernateTemplate().update(t.get선박목록());
		// }

		super.update(t);
	}

}
