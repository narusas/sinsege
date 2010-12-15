package net.narusas.aceauction.data.builder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.model.���;

public class ��Ǵ����Builder{

	private BuildProgressListener listener;

	public ��Ǵ����Builder() {
	}

	private void insert��Ǵ����each(List<String> data, long event_no, String relation)
			throws Exception {
		for (String name : data) {
			if (��Ǵ����exist(name, event_no, relation)) {
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

	private void removeExisting��Ǵ����(��� ���) throws Exception {
		Statement stmt = DB.createStatement();
		stmt.executeUpdate("DELETE FROM ac_appoint_statement WHERE id=" + ���.getDbid()+ ";");

	}

	private boolean ��Ǵ����exist(String name, long event_no, String relation) throws Exception {
		PreparedStatement stmt = DB.prepareStatement("SELECT * FROM ac_participant WHERE name=? AND relation=? AND event_no=?;");

		stmt.setString(1, name);
		stmt.setString(2, relation);
		stmt.setLong(3, event_no);
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		return rs.next();
	}

	void update��Ǵ����DB(��� ���, BuildProgressListener listener) throws Exception {
		this.listener = listener;
		DB.reConnect();
		removeExisting��Ǵ����(���);
		insert��Ǵ����each(���.������, ���.getDbid(), "������");
		insert��Ǵ����each(���.ä����, ���.getDbid(), "ä����");
		insert��Ǵ����each(���.ä����, ���.getDbid(), "ä����");
	}
}
