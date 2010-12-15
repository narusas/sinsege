package net.narusas.aceauction.data.builder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.model.���;
import net.narusas.aceauction.pdf.gamjung.GamjungParser.Group;

public class �����򰡼�Builder {
	static Logger logger = Logger.getLogger("log");

	private long getExisting���id(��� ���) throws Exception {
		PreparedStatement stmt = DB.prepareStatement("SELECT * FROM ac_event WHERE no=? AND court_code=?;");
		stmt.setLong(1, ���.get��ǹ�ȣ());
		stmt.setLong(2, DB.toLong(���.court.getCode()));
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		if (rs.next()) {
			return rs.getLong("id");
		}
		return -1;
	}

	public void insert(��� s, String ���, String ����, String ��ġ�ױ���, String �̿����, String ����, String ���ΰ���, String �ó���) {
		try {
			DB.reConnect();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		long id = -1;
		try {
			id = getExisting���id(s);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (id == -1) {
			return;
		}
		PreparedStatement stmt = null;
		try {
			stmt = DB
					.prepareStatement("UPDATE ac_event SET judgement_location=?, judgement_use=?, judgement_land=?, judgement_road=?, judgement_temp=? WHERE id=?;");
			stmt.setString(1, ��ġ�ױ���);
			stmt.setString(2, �̿����);
			stmt.setString(3, ����);
			stmt.setString(4, ���ΰ���);
			stmt.setString(5, �ó���);
			stmt.setLong(6, id);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.cleanup(stmt);
		}

		stmt = null;
		try {
			stmt = DB
					.prepareStatement("UPDATE ac_goods SET judgement_office=?, judgement_date=? WHERE event_no=?;");
			stmt.setString(1, ���);
			stmt.setString(2, ����);
			stmt.setLong(3, id);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.cleanup(stmt);
		}
		
	}
}
