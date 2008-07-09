package net.narusas.aceauction.data;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.TestCase;

public class ¿Ï·áÃ³¸®SampleDateTest extends TestCase {
	public void testInsert() throws Exception {
		DB db = new DB();
		db.dbConnect();

		PreparedStatement stmt = db.prepareStatement("INSERT INTO ac_charge "
				+ "(charge_code, court_code, name, fixed_date, start_date, end_date, location, method, time) "
				+ "VALUES (?,?,?,?,?,?,?,?,?);");
		stmt.setInt(1, 1003);
		stmt.setInt(2, 210);
		stmt.setString(3, "°æ¸Å3°è");
		stmt.setDate(4, new Date(1999 - 1900, 1, 1));
		stmt.setDate(5, null);
		stmt.setDate(6, null);
		stmt.setString(7, "¹ý¿ø");
		stmt.setString(8, "±âÀÏÀÔÂû");
		stmt.setString(9, "10:00");
		stmt.execute();
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		int charge_no = rs.getInt(1);
		rs.close();

		stmt = db
				.prepareStatement("INSERT INTO ac_event (no, event_year, event_no, name, accept_date, claim_price, merged_to, court_code, charge_id) "
						+ "VALUES (?,?,?,?,?,?,?,?,?);");
		stmt.setLong(1, 200501300009723L);
		stmt.setInt(2, 2005);
		stmt.setInt(3, 9723);

		stmt.setString(4, "name");
		stmt.setDate(5, new Date(1999 - 1900, 1, 1));
		stmt.setLong(6, 100);
		stmt.setString(7, "");

		stmt.setInt(8, 210);
		stmt.setInt(9, charge_no);
		stmt.execute();
		rs = stmt.getGeneratedKeys();
		rs.next();
		long event_no = rs.getLong(1);
		rs.close();

		stmt = db.prepareStatement("INSERT INTO ac_goods " + "(no, "// 1
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
				+ "usage_code," // 23
				+ "done" // 23

				+ ") " + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");

		stmt.setInt(1, 4);
		stmt.setInt(2, 1);
		stmt.setString(3, "");
		stmt.setLong(4, 100);
		stmt.setLong(5, 100);
		stmt.setString(6, "");
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
		stmt.setLong(18, event_no);
		stmt.setInt(19, 1);
		stmt.setInt(20, 100);
		stmt.setInt(21, 1000);
		stmt.setString(22, "");
		stmt.setInt(23, 1);
		stmt.setInt(24, 0);

		stmt.execute();
		rs = stmt.getGeneratedKeys();
		rs.next();
		int goods_id = rs.getInt(1);
		rs.close();

		// int no = 1;
		// Date d = new Date(2006-1900,9-1,5);
		// String price = "120000000";
		// String result = "À¯Âû";
		stmt = db.prepareStatement("INSERT INTO ac_appoint_statement "//
				+ "(" //
				+ "no, "// 1
				+ "fixed_date, "// 2
				+ "type, "// 3
				+ "location, "// 4
				+ "lowest_price, "// 5
				+ "result, "// 6
				+ "goods_id"// 7
				+ ") " + "VALUES (?,?,?,?,?,?,?);");

		insertAppoint(stmt, goods_id, 1, new Date(2006 - 1900, 9 - 1, 5), "120000000", "À¯Âû");
		insertAppoint(stmt, goods_id, 2, new Date(2006 - 1900, 10 - 1, 10), "96000000", "À¯Âû");
		insertAppoint(stmt, goods_id, 3, new Date(2006 - 1900, 11 - 1, 14), "76800000", "À¯Âû");
		insertAppoint(stmt, goods_id, 4, new Date(2006 - 1900, 12 - 1, 12), "61440000", "¸Å°¢");
		insertAppoint(stmt, goods_id, 5, new Date(2007 - 1900, 1 - 1, 26), "", "¹Ì³³");
		insertAppoint(stmt, goods_id, 6, new Date(2007 - 1900, 2 - 1, 20), "61440000", "À¯Âû");
		insertAppoint(stmt, goods_id, 7, new Date(2007 - 1900, 3 - 1, 27), "49152000", "");
		// insertAppoint(stmt, goods_id, 8, new Date(2007-1900,9-1,5),
		// "120000000", "À¯Âû");

		¸Å°¢±âÀÏUpdaterDB dbUpdater = new ¸Å°¢±âÀÏUpdaterDB( null);
		dbUpdater.update(goods_id);
	}

	/**
	 * @param stmt
	 * @param goods_id
	 * @param no
	 * @param d
	 * @param price
	 * @param result
	 * @throws SQLException
	 */
	private void insertAppoint(PreparedStatement stmt, int goods_id, int no, Date d, String price, String result)
			throws SQLException {
		stmt.setInt(1, no);
		stmt.setDate(2, d);
		stmt.setString(3, "");
		stmt.setString(4, "");
		stmt.setString(5, price);
		stmt.setString(6, result);
		stmt.setLong(7, goods_id);
		stmt.execute();
	}
}
