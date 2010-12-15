package net.narusas.aceauction.data.builder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.model.사건;
import net.narusas.aceauction.pdf.gamjung.GamjungParser.Group;

public class 감정평가서Builder {
	static Logger logger = Logger.getLogger("log");

	private long getExisting사건id(사건 사건) throws Exception {
		PreparedStatement stmt = DB.prepareStatement("SELECT * FROM ac_event WHERE no=? AND court_code=?;");
		stmt.setLong(1, 사건.get사건번호());
		stmt.setLong(2, DB.toLong(사건.court.getCode()));
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		if (rs.next()) {
			return rs.getLong("id");
		}
		return -1;
	}

	public void insert(사건 s, String 기관, String 시점, String 위치및교통, String 이용상태, String 토지, String 도로관련, String 냉난방) {
		try {
			DB.reConnect();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		long id = -1;
		try {
			id = getExisting사건id(s);
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
			stmt.setString(1, 위치및교통);
			stmt.setString(2, 이용상태);
			stmt.setString(3, 토지);
			stmt.setString(4, 도로관련);
			stmt.setString(5, 냉난방);
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
			stmt.setString(1, 기관);
			stmt.setString(2, 시점);
			stmt.setLong(3, id);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.cleanup(stmt);
		}
		
	}
}
