package net.narusas.aceauction.data;

import java.sql.Connection;

import junit.framework.TestCase;
import net.narusas.aceauction.data.builder.���εBuilder;

public class ����DBTest extends TestCase {
	public void test1() throws Exception {
		DB db = new DB();
		Connection conn = db.dbConnect();
		
		���εBuilder db2 = new ���εBuilder(1, "file:./fixtures/���а��.pdf", null, null);
		db2.execute();
	}
	
//	public void testClear() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//		DB db = new DB();
//		Connection conn = db.dbConnect();
//		Statement stmt = conn.createStatement();
//		stmt.executeUpdate("DELETE FROM ac_attested WHERE id>=0;");
//		stmt.executeUpdate("DELETE FROM ac_attested_statement WHERE id>=0;");
//	}
	
}
