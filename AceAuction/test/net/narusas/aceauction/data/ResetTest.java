package net.narusas.aceauction.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

public class ResetTest extends TestCase {
	public void testClear() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException,
			IOException {
		DB db = new DB();
		Connection conn = db.dbConnect();
		Statement stmt = conn.createStatement();

		stmt.executeUpdate("UPDATE  ac_goods SET done=0 WHERE id>=0;");
	}
}
