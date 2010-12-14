package net.narusas.si.auction.db;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class GenericDaoHibernate<T, PK extends Serializable> implements GenericDao<T, PK> {
	protected HibernateTemplate hibernateTemplate;
	protected Class<? extends T> clazz;

	protected GenericDaoHibernate(Class<? extends T> clazz) {
		this.clazz = clazz;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	protected Class<? extends T> getClazz() {
		return clazz;
	}

	protected void setClazz(Class<? extends T> clazz) {
		this.clazz = clazz;
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public T get(PK id) {
		return (T) getHibernateTemplate().get(clazz, id);
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<T> get(final T t) {
		if (t == null) {
			return getAll();
		} else {
			HibernateCallback callback = new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					Example ex = Example.create(t).ignoreCase().enableLike(MatchMode.ANYWHERE);
					return session.createCriteria(clazz).add(ex).list();
				}
			};
			return (List<T>) getHibernateTemplate().execute(callback);
		}
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<T> get(final T t, final String order) {
		if (t == null) {
			return getAll();
		} else {
			HibernateCallback callback = new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					Example ex = Example.create(t).ignoreCase().enableLike(MatchMode.ANYWHERE);
					return session.createCriteria(clazz).add(ex).addOrder(Order.asc(order)).list();
				}
			};
			return (List<T>) getHibernateTemplate().execute(callback);
		}
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return getHibernateTemplate().loadAll(clazz);
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<T> getAll(String order) {
		return getHibernateTemplate().findByCriteria(
				DetachedCriteria.forClass(clazz).addOrder(Order.asc(order)));
	}

	@Transactional
	public void remove(PK id) {
		remove(get(id));
	}

	@Transactional
	public void remove(T t) {
		getHibernateTemplate().delete(t);
	}

	@Transactional
	public void save(T t) {
		getHibernateTemplate().save(t);
	}

	@Override
	public void saveOrUpdate(T t) {
		getHibernateTemplate().saveOrUpdate(t);
	}

	@Transactional
	public void update(T t) {
		getHibernateTemplate().update(t);
	}

	@Override
	public void merge(T t) {
		getHibernateTemplate().merge(t);
	}

}