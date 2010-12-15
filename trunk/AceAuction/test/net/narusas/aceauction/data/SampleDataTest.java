package net.narusas.aceauction.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.TestCase;

public class SampleDataTest extends TestCase {
	public void testInsert() throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			SQLException, IOException {
		DB db = new DB();
		Connection conn = db.dbConnect();
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO ac_charge "
				+ "(charge_code, court_code, name, fixed_date, start_date, end_date, location, method, time) "
				+ "VALUES (?,?,?,?,?,?,?,?,?);");
		stmt.setInt(1, 1003);
		stmt.setInt(2, 210);
		stmt.setString(3, "담당3계");
		stmt.setDate(4, new Date(2007 - 1900, 5 - 1, 1));
		stmt.setDate(5, null);
		stmt.setDate(6, null);
		stmt.setString(7, "담당법원");
		stmt.setString(8, "기일입찰");
		stmt.setString(9, "10:00");
		stmt.execute();
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		int charge_no = rs.getInt(1);

		stmt = conn
				.prepareStatement("INSERT INTO ac_event (no, event_year, event_no, name, accept_date, claim_price, merged_to, court_code, charge_id) "
						+ "VALUES (?,?,?,?,?,?,?,?,?);");
		stmt.setLong(1, 19990130058236L);
		stmt.setInt(2, 1999);
		stmt.setInt(3, 58236);

		stmt.setString(4, "임의경매");
		stmt.setDate(5, new Date(1999 - 1900, 9, 4));
		stmt.setLong(6, 1000000000L);
		stmt.setString(7, "");

		stmt.setInt(8, 1003);
		stmt.setInt(9, charge_no);
		stmt.execute();
		rs = stmt.getGeneratedKeys();
		rs.next();
		long sagun_no = rs.getLong(1);

		stmt = conn.prepareStatement("INSERT INTO ac_goods " + "(no, "// 1
				+ "type_code,"// 2
				+ "sell_target,"// 3
				+ "sell_price,"// 4
				+ "lowest_price,"// 5
				+ "guarantee_price,"// 6
				+ "accept_date,"// 7
				+ "decision_date,"// 8
				+ "devidend_date,"// 9
				+ "registration_date,"// 10
				+ "building_area,"// 11
				+ "land_area,"// 12
				+ "exclusive_area,"// 13
				+ "exclusion_area,"// 14
				+ "comment,"// 15
				+ "court_code," // 16
				+ "charge_id," // 17
				+ "event_no," // 18
				+ "area_code," // 19
				+ "area_code_2," // 20
				+ "area_code_3," // 21
				+ "address," // 22
				+ "usage_code" // 23
				+ ") " + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");

		stmt.setInt(1, 1);
		stmt.setInt(2, 1);
		stmt.setString(3, "건물매각");
		stmt.setLong(4, 100);
		stmt.setLong(5, 100);
		stmt.setString(6, "100");
		stmt.setDate(7, new Date(1999 - 1900, 1, 1));
		stmt.setDate(8, new Date(1999 - 1900, 1, 1));
		stmt.setDate(9, new Date(1999 - 1900, 1, 1));
		stmt.setDate(10, new Date(1999 - 1900, 1, 1));
		stmt.setString(11, "");
		stmt.setString(12, "");
		stmt.setString(13, "");
		stmt.setString(14, "");
		stmt.setString(15, "");
		stmt.setInt(16, 210);
		stmt.setInt(17, charge_no);
		stmt.setLong(18, sagun_no);
		stmt.setInt(19, 1);
		stmt.setInt(20, 1);
		stmt.setInt(21, 1);
		stmt.setString(22, "");
		stmt.setInt(23, 1);
		stmt.execute();
		rs = stmt.getGeneratedKeys();
		rs.next();
		int goodsId = rs.getInt(1);
		rs.close();

		stmt = conn.prepareStatement("INSERT INTO ac_appoint_statement "//
				+ "(" //
				+ "no, "// 1
				+ "fixed_date, "// 2
				+ "type, "// 3
				+ "location, "// 4
				+ "lowest_price, "// 5
				+ "result, "// 6
				+ "goods_id"// 7
				+ ") " + "VALUES (?,?,?,?,?,?,?);");

		stmt.setInt(1, 1);
		stmt.setDate(2, new Date(2005 - 1900, 3, 3));
		stmt.setString(3, "");
		stmt.setString(4, "");
		stmt.setString(5, "100");
		stmt.setString(6, "유찰");
		stmt.setLong(7, goodsId);
		stmt.execute();

	}
}
