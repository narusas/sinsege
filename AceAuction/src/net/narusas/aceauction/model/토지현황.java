package net.narusas.aceauction.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import net.narusas.aceauction.data.DB;

public class ������Ȳ {
	private final long goods_id;
	private final String �Ű�����;
	String address;
	String area;
	String use;

	public ������Ȳ(long goods_id, String address, String use, String area, String �Ű�����) {
		super();
		this.goods_id = goods_id;
		this.address = address;
		this.use = use;
		this.area = area;
		this.�Ű����� = �Ű�����;
	}

	public String getAddress() {
		return address;
	}

	public String getArea() {
		return area;
	}

	public long getGoods_id() {
		return goods_id;
	}

	public String getUse() {
		return use;
	}

	public void insert() throws Exception {
		PreparedStatement stmt = null;
		try {
			stmt = DB.prepareStatement("INSERT INTO ac_land_statement " + //
					"(" // +
					+ "goods_id,"// 1
					+ "address," // 2
					+ "use_for," // 3
					+ "use_for2," // 4
					+ "area," // 5
					+ "sell_comment" // 6
					+ ") " //
					+ "VALUES (?,?,?,?,?,?);");
			stmt.setLong(1, goods_id);
			stmt.setString(2, address);
			stmt.setString(3, use);
			stmt.setString(4, "");
			stmt.setString(5, area);
			stmt.setString(6, �Ű�����);
			stmt.execute();
		} finally {
			DB.cleanup(stmt);
		}
	}

	public boolean isTypeMatch(String[] keys) {
		for (String key: keys) {
			if (key.equals(getUse())){
				return true;
			}
		}
		return false;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public static void clear������ȲFor����(int id) throws Exception {
		Statement stmt = DB.createStatement();
		stmt.executeUpdate("DELETE FROM ac_land_statement WHERE goods_id=" + id + ";");
	}

	public static boolean has��Ȳ(int goods_id) throws Exception {
		Statement stmt = DB.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM ac_land_statement WHERE goods_id="+goods_id+";");
		boolean has = rs.next();
		DB.cleanup(rs, stmt);
		return has;
	}
	
}
