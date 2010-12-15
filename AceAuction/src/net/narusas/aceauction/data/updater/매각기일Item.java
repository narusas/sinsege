package net.narusas.aceauction.data.updater;

import java.sql.ResultSet;
import java.sql.SQLException;

public class 매각기일Item {

	java.sql.Date date;

	long id;

	int no;

	String price;

	String result;

	String type;

	public 매각기일Item(ResultSet rs) throws SQLException {
		// no, fixed_date, lowest_price, result
		id = rs.getLong(1);
		no = rs.getInt(2);
		date = rs.getDate(3);
		price = rs.getString(4);
		result = rs.getString(5);
		type = rs.getString(6);
	}

	public long getTime() {
		return date.getTime();
	}

	@Override
	public String toString() {
		return "[" + no + "," + date + "," + price + "," + result + "]";
	}

}