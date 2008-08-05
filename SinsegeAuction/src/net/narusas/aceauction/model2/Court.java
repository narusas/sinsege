package net.narusas.aceauction.model2;

import net.narusas.aceauction.model.过盔;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

/**
 * 
 * <pre>
 * create table test.COURTS (
 *   code integer not null, 
 *   parent_code integer, 
 *   ord integer not null,
 *   name varchar(12) not null,
 *   primary key (code)
 * )
 * </pre>
 * 
 * @author narusas
 * 
 */
public class Court {
	String code;
	String parentCode;
	String name;
	int order;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void main(String[] args) {
		try {
			SessionFactory sf = new Configuration().configure()
					.buildSessionFactory();

			Session session = sf.openSession();
			Transaction tx = session.beginTransaction();

			// delete
			// Court court = (Court) session.load(Court.class, "111");
			// session.delete(court);
			//

			for (int i = 0; i < 过盔.size(); i++) {
				过盔 c = 过盔.get(i);
				Court court = new Court();
				court.setCode(c.getCode());
				court.setOrder(i);
				court.setName(c.getName());
				if (c.getParent() != null) {
					court.setParentCode(c.getParent().getCode());
				}

				session.save(court);
			}

			tx.commit();
			session.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
