package net.narusas.aceauction.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import net.narusas.aceauction.data.DB;

public class 건물현황 {
	private final String detail;
	private final String 매각지분comment;
	String address;
	String area;
	String comment;
	String floor;
	long goods_id;
	String structure;

	public 건물현황(int goods_id, String address, String floor, String structure,
			String area, String comment, String detail, String 매각지분comment) {
		this.goods_id = goods_id;
		this.address = address;
		this.floor = floor;
		this.structure = structure;
		this.area = area;
		this.comment = comment;
		this.detail = detail;
		this.매각지분comment = 매각지분comment;
	}

	public 건물현황(long goods_id, String address, String floor, String structure,
			String area, String detail, String 매각지분comment) {
		this.goods_id = goods_id;
		this.address = address;
		this.floor = floor;
		this.structure = structure;
		this.area = area;
		this.detail = detail;
		this.매각지분comment = 매각지분comment;
	}

	public void insert() throws Exception {
		PreparedStatement stmt = null;
		try {
			stmt = DB.prepareStatement("INSERT INTO ac_bld_statement " + //
					"(" // +
					+ "no," // 1
					+ "address," // 2
					+ "floor," // 3
					+ "structure," // 4
					+ "area," // 5
					+ "goods_id,"// 6
					+ "comment,"// 7
					+ "sell_comment"// 8
					+ ") " //
					+ "VALUES (?,?,?,?,?,?,?,?);");
			stmt.setInt(1, 0);
			stmt.setString(2, address);
			stmt.setString(3, floor);
			stmt.setString(4, structure);
			stmt.setString(5, area);
			stmt.setLong(6, goods_id);
			stmt.setString(7, comment);
			stmt.setString(8, 매각지분comment);
			stmt.execute();
		} finally {
			DB.cleanup(stmt);
		}
	}

	public boolean match(String[] texts) {

		for (String text : texts) {
			boolean res = (structure != null && structure.contains(text))
					|| (floor != null && floor.contains(text))
					|| (detail != null && detail.contains(text));
			if (res ==false) {
				return false;
			}
		}
		return true;
	}

	public static void clear건물현황For물건(int id) throws Exception {
		Statement stmt = DB.createStatement();
		stmt.executeUpdate("DELETE FROM ac_bld_statement WHERE goods_id=" + id
				+ ";");
	}

	public static boolean has현황(int goods_id) throws Exception {
		Statement stmt = DB.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM ac_bld_statement WHERE goods_id="+goods_id+";");
		boolean has = rs.next();
		DB.cleanup(rs, stmt);
		return has;
	}

}
