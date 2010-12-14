package net.narusas.si.auction.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateUtil {
	private SessionFactory sf;
	private static final HibernateUtil INSTANCE = new HibernateUtil();

	public void closeSession() {
		sf.getCurrentSession().close();
	}

	public Transaction beginTransaction() {
		return sf.getCurrentSession().beginTransaction();
	}

	public void commitTransaction() {
		sf.getCurrentSession().getTransaction().commit();
	}

	public void rollbackTransaction() {
		if (sf.getCurrentSession().isConnected()) {
			Transaction tx = sf.getCurrentSession().getTransaction();
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
		}
	}

	public static HibernateUtil getInstance() {
		return INSTANCE;
	}

	public void setSessionFactory(SessionFactory sf) {
		this.sf = sf;
	}

	public Session getCurrentSession() {
		return sf.getCurrentSession();
	}

	public SessionFactory getSessionFactory() {
		return this.sf;
	}

}