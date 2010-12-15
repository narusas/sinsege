package net.narusas.aceauction.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import junit.framework.TestCase;
import net.narusas.aceauction.model.过盔;

public class InitialData extends TestCase {
	private Connection conn;

	public void close() throws SQLException {
		conn.close();
	}

	public void testSetupCourt() throws SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager
				.getConnection("jdbc:mysql://210.109.102.179/test?user=acea&password=dhrtus_ace");
		PreparedStatement stmt = conn
				.prepareStatement("INSERT INTO ac_court (code, name, parent_code, ord) VALUES (?,?,?,?);");

		for (int i = 0; i < 过盔.size(); i++) {
			过盔 c = 过盔.get(i);
			stmt.setInt(1, Integer.parseInt(c.getCode()));
			stmt.setString(2, c.getName());
			if (c.getParent() == null) {
				stmt.setInt(3, Integer.parseInt(c.getCode()));
			} else {
				stmt.setInt(3, Integer.parseInt(c.getParent().getCode()));
			}
			stmt.setInt(4, i + 1);
			stmt.execute();
		}
	}
}
