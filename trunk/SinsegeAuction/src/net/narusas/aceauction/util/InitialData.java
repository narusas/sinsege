/*
 * 
 */
package net.narusas.aceauction.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import junit.framework.TestCase;
import net.narusas.aceauction.model.过盔;

// TODO: Auto-generated Javadoc
/**
 * The Class InitialData.
 */
public class InitialData extends TestCase {

	/** The conn. */
	private static Connection conn;

	/**
	 * Close.
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	public void close() throws SQLException {
		conn.close();
	}

	/**
	 * Test setup court.
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 * @throws InstantiationException
	 *             the instantiation exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public void testSetupCourt() throws SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {

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

	public static void main(String[] args) {
		try {
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
			
			
			
			
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void setup() {
		// TODO Auto-generated method stub

	}
}
