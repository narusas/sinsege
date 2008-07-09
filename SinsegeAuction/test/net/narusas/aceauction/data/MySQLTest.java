package net.narusas.aceauction.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

public class MySQLTest extends TestCase {
	public void testConditionString() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {
		DB db = new DB();
		Connection conn = db.dbConnect();
		Statement stmt = conn.createStatement();
//		stmt.execute("INSERT INTO ac_goods_type (code, name) VALUES (1,\"100\");");
//		stmt.execute("INSERT INTO ac_goods_type (code, name) VALUES (2,\"10\");");
//		stmt.execute("INSERT INTO ac_goods_type (code, name) VALUES (3,\"50\");");
//		stmt.execute("INSERT INTO ac_goods_type (code, name) VALUES (4,\"200\");");
//		ResultSet rs = stmt.executeQuery("SELECT * FROM ac_goods_type WHERE name > 30;");
//		
//		while(rs.next()) {
//			System.out.println(rs.getString(2));
//		}
		
	}
}
