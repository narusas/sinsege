/*
 * 
 */
package net.narusas.aceauction.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import net.narusas.aceauction.data.DB;

// TODO: Auto-generated Javadoc
/**
 * The Class 건물현황.
 */
public class 건물현황 {
	
	/** The detail. */
	private final String detail;
	
	/** The 매각지분comment. */
	private final String 매각지분comment;
	
	/** The address. */
	String address;
	
	/** The area. */
	String area;
	
	/** The comment. */
	String comment;
	
	/** The floor. */
	String floor;
	
	/** The goods_id. */
	long goods_id;
	
	/** The structure. */
	String structure;

	/**
	 * Instantiates a new 건물현황.
	 * 
	 * @param goods_id the goods_id
	 * @param address the address
	 * @param floor the floor
	 * @param structure the structure
	 * @param area the area
	 * @param comment the comment
	 * @param detail the detail
	 * @param 매각지분comment the 매각지분comment
	 */
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

	/**
	 * Instantiates a new 건물현황.
	 * 
	 * @param goods_id the goods_id
	 * @param address the address
	 * @param floor the floor
	 * @param structure the structure
	 * @param area the area
	 * @param detail the detail
	 * @param 매각지분comment the 매각지분comment
	 */
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

	/**
	 * Insert.
	 * 
	 * @throws Exception the exception
	 */
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

	/**
	 * Match.
	 * 
	 * @param texts the texts
	 * 
	 * @return true, if successful
	 */
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

	/**
	 * Clear건물현황 for물건.
	 * 
	 * @param id the id
	 * 
	 * @throws Exception the exception
	 */
	public static void clear건물현황For물건(int id) throws Exception {
		Statement stmt = DB.createStatement();
		stmt.executeUpdate("DELETE FROM ac_bld_statement WHERE goods_id=" + id
				+ ";");
	}

	/**
	 * Has현황.
	 * 
	 * @param goods_id the goods_id
	 * 
	 * @return true, if successful
	 * 
	 * @throws Exception the exception
	 */
	public static boolean has현황(int goods_id) throws Exception {
		Statement stmt = DB.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM ac_bld_statement WHERE goods_id="+goods_id+";");
		boolean has = rs.next();
		DB.cleanup(rs, stmt);
		return has;
	}

}
