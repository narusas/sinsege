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
 * The Class 토지현황.
 */
public class 토지현황 {
	
	/** The goods_id. */
	private final long goods_id;
	
	/** The 매각지분. */
	private final String 매각지분;
	
	/** The address. */
	String address;
	
	/** The area. */
	String area;
	
	/** The use. */
	String use;

	/**
	 * Instantiates a new 토지현황.
	 * 
	 * @param goods_id the goods_id
	 * @param address the address
	 * @param use the use
	 * @param area the area
	 * @param 매각지분 the 매각지분
	 */
	public 토지현황(long goods_id, String address, String use, String area, String 매각지분) {
		super();
		this.goods_id = goods_id;
		this.address = address;
		this.use = use;
		this.area = area;
		this.매각지분 = 매각지분;
	}

	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Gets the area.
	 * 
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * Gets the goods_id.
	 * 
	 * @return the goods_id
	 */
	public long getGoods_id() {
		return goods_id;
	}

	/**
	 * Gets the use.
	 * 
	 * @return the use
	 */
	public String getUse() {
		return use;
	}

	/**
	 * Insert.
	 * 
	 * @throws Exception the exception
	 */
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
			stmt.setString(6, 매각지분);
			stmt.execute();
		} finally {
			DB.cleanup(stmt);
		}
	}

	/**
	 * Checks if is type match.
	 * 
	 * @param keys the keys
	 * 
	 * @return true, if is type match
	 */
	public boolean isTypeMatch(String[] keys) {
		for (String key: keys) {
			if (key.equals(getUse())){
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets the address.
	 * 
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Sets the area.
	 * 
	 * @param area the new area
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * Sets the use.
	 * 
	 * @param use the new use
	 */
	public void setUse(String use) {
		this.use = use;
	}

	/**
	 * Clear토지현황 for물건.
	 * 
	 * @param id the id
	 * 
	 * @throws Exception the exception
	 */
	public static void clear토지현황For물건(int id) throws Exception {
		Statement stmt = DB.createStatement();
		stmt.executeUpdate("DELETE FROM ac_land_statement WHERE goods_id=" + id + ";");
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
		ResultSet rs = stmt.executeQuery("SELECT * FROM ac_land_statement WHERE goods_id="+goods_id+";");
		boolean has = rs.next();
		DB.cleanup(rs, stmt);
		return has;
	}
	
}
