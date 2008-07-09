/*
 * 
 */
package net.narusas.aceauction.data.updater;

import java.sql.ResultSet;
import java.sql.SQLException;

// TODO: Auto-generated Javadoc
/**
 * The Class 담당계Item.
 */
public class 담당계Item {

	/** The no. */
	private int no;

	/**
	 * Instantiates a new 담당계 item.
	 * 
	 * @param rs the rs
	 * 
	 * @throws SQLException the SQL exception
	 */
	public 담당계Item(ResultSet rs) throws SQLException {
		no = rs.getInt(1);

	}

	/**
	 * Gets the charge id.
	 * 
	 * @return the charge id
	 */
	public String getChargeId() {
		return "" + no;
	}

}