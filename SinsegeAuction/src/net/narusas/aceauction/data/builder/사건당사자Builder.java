/*
 * 
 */
package net.narusas.aceauction.data.builder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.model.사건;

// TODO: Auto-generated Javadoc
/**
 * The Class 사건당사자Builder.
 */
public class 사건당사자Builder{

	/** The listener. */
	private BuildProgressListener listener;

	/**
	 * Instantiates a new 사건당사자 builder.
	 */
	public 사건당사자Builder() {
	}

	/**
	 * Insert사건당사자each.
	 * 
	 * @param data the data
	 * @param event_no the event_no
	 * @param relation the relation
	 * 
	 * @throws Exception the exception
	 */
	private void insert사건당사자each(List<String> data, long event_no, String relation)
			throws Exception {
		for (String name : data) {
			if (사건당사자exist(name, event_no, relation)) {
				continue;
			}
			PreparedStatement stmt = DB.prepareStatement("INSERT INTO ac_participant ( name, relation, event_no) "
					+ "VALUES (?,?,?);");
			stmt.setString(1, name);
			stmt.setString(2, relation);
			stmt.setLong(3, event_no);
			stmt.execute();
		}
	}

	/**
	 * Removes the existing사건당사자.
	 * 
	 * @param 사건 the 사건
	 * 
	 * @throws Exception the exception
	 */
	private void removeExisting사건당사자(사건 사건) throws Exception {
		Statement stmt = DB.createStatement();
		stmt.executeUpdate("DELETE FROM ac_appoint_statement WHERE id=" + 사건.getDbid()+ ";");

	}

	/**
	 * 사건당사자exist.
	 * 
	 * @param name the name
	 * @param event_no the event_no
	 * @param relation the relation
	 * 
	 * @return true, if successful
	 * 
	 * @throws Exception the exception
	 */
	private boolean 사건당사자exist(String name, long event_no, String relation) throws Exception {
		PreparedStatement stmt = DB.prepareStatement("SELECT * FROM ac_participant WHERE name=? AND relation=? AND event_no=?;");

		stmt.setString(1, name);
		stmt.setString(2, relation);
		stmt.setLong(3, event_no);
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		return rs.next();
	}

	/**
	 * Update사건당사자 db.
	 * 
	 * @param 사건 the 사건
	 * @param listener the listener
	 * 
	 * @throws Exception the exception
	 */
	void update사건당사자DB(사건 사건, BuildProgressListener listener) throws Exception {
		this.listener = listener;
		DB.reConnect();
		removeExisting사건당사자(사건);
		insert사건당사자each(사건.소유자, 사건.getDbid(), "소유자");
		insert사건당사자each(사건.채권자, 사건.getDbid(), "채권자");
		insert사건당사자each(사건.채무자, 사건.getDbid(), "채무자");
	}
}
