package net.narusas.aceauction.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

public class RemoveDuplicatedLandTest extends TestCase {
	public void test() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, IOException {
		DB db = new DB();
		Connection conn = db.dbConnect();
		Statement stmt = conn.createStatement();// ment();
		ResultSet rs = stmt.executeQuery("SELECT * FROM ac_land_statement;");

		while (rs.next()) {
			long id = rs.getLong("id");
			long goods_id = rs.getLong("goods_id");
			long no = rs.getLong("no");
			System.out.println("CHECKL:"+id);
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2
					.executeQuery("SELECT * FROM ac_land_statement WHERE goods_id="
							+ goods_id + " AND no=" + no + ";");
			if(rs2.next()){
				while(rs2.next()){
					long id2 = rs2.getLong("id");
					delete(id2, conn);
				}
			}
			rs2.close();
		}

	}

	private void delete(long id, Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		stmt
				.executeUpdate("DELETE FROM ac_land_statement WHERE id=" + id
						+ ";");
		stmt.close();
	}
}
