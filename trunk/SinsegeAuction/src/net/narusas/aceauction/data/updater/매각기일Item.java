/*
 * 
 */
package net.narusas.aceauction.data.updater;

import java.sql.ResultSet;
import java.sql.SQLException;

// TODO: Auto-generated Javadoc
/**
 * The Class 매각기일Item.
 */
public class 매각기일Item {

	/** The date. */
	java.sql.Date date;

	/** The id. */
	long id;

	/** The no. */
	int no;

	/** The price. */
	String price;

	/** The result. */
	String result;

	/** The type. */
	String type;

	/**
	 * Instantiates a new 매각기일 item.
	 * 
	 * @param rs the rs
	 * 
	 * @throws SQLException the SQL exception
	 */
	public 매각기일Item(ResultSet rs) throws SQLException {
		// no, fixed_date, lowest_price, result
		id = rs.getLong(1);
		no = rs.getInt(2);
		date = rs.getDate(3);
		price = rs.getString(4);
		result = rs.getString(5);
		type = rs.getString(6);
	}

	/**
	 * Gets the time.
	 * 
	 * @return the time
	 */
	public long getTime() {
		return date.getTime();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + no + "," + date + "," + price + "," + result + "]";
	}

}