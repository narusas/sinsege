package net.narusas.aceauction.data.updater;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ����Item {

	private int no;

	public ����Item(ResultSet rs) throws SQLException {
		no = rs.getInt(1);

	}

	public String getChargeId() {
		return "" + no;
	}

}