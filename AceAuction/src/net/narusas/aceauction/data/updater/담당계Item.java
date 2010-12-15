package net.narusas.aceauction.data.updater;

import java.sql.ResultSet;
import java.sql.SQLException;

public class 담당계Item {

	private int no;

	public 담당계Item(ResultSet rs) throws SQLException {
		no = rs.getInt(1);

	}

	public String getChargeId() {
		return "" + no;
	}

}