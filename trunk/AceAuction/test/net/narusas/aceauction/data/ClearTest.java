package net.narusas.aceauction.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

public class ClearTest extends TestCase {
	public void testClear() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException, IOException {
		DB db = new DB();
		Connection conn = db.dbConnect();
		Statement stmt = conn.createStatement();

		stmt.executeUpdate("DELETE FROM ac_charge WHERE no>=0;");
		stmt.executeUpdate("DELETE FROM ac_event WHERE no>=0;");
		stmt.executeUpdate("DELETE FROM ac_goods WHERE id>=0;");

		stmt.executeUpdate("DELETE FROM ac_appoint_statement WHERE id>=0;");
		stmt.executeUpdate("DELETE FROM ac_bld_statement WHERE id>=0;");
		stmt.executeUpdate("DELETE FROM ac_goods_statement WHERE id>=0;");
		stmt.executeUpdate("DELETE FROM ac_land_right_statement WHERE id>=0;");
		stmt.executeUpdate("DELETE FROM ac_land_statement WHERE id>=0;");
		stmt.executeUpdate("DELETE FROM ac_exclusion WHERE id>=0;");
		stmt.executeUpdate("DELETE FROM ac_participant WHERE no>=0;");
		stmt.executeUpdate("DELETE FROM ac_attested_statement WHERE id>=0;");
		stmt.executeUpdate("DELETE FROM ac_attested WHERE id>=0;");
		stmt.executeUpdate("DELETE FROM ac_goods_building WHERE id>=0;");
	}
}
