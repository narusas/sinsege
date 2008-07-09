/*
 * 
 */
package net.narusas.aceauction.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.narusas.aceauction.data.DB;

// TODO: Auto-generated Javadoc
/**
 * The Class 매각물건명세서.
 */
public class 매각물건명세서 {

	/** The chaim. */
	private final String chaim;
	
	/** The conclusion_date. */
	private final String conclusion_date;
	
	/** The devidend_req_date. */
	private final String devidend_req_date;
	
	/** The goods_id. */
	private final long goods_id;
	
	/** The ground_claim. */
	private final String ground_claim;
	
	/** The guaranty_price. */
	private final String guaranty_price;
	
	/** The info_source. */
	private final String info_source;
	
	/** The name. */
	private final String name;
	
	/** The period. */
	private final String period;
	
	/** The possessory. */
	private final String possessory;
	
	/** The report_date. */
	private final String report_date;

	/**
	 * Instantiates a new 매각물건명세서.
	 * 
	 * @param goods_id the goods_id
	 * @param name the name
	 * @param possessory the possessory
	 * @param info_source the info_source
	 * @param ground_claim the ground_claim
	 * @param period the period
	 * @param guaranty_price the guaranty_price
	 * @param chaim the chaim
	 * @param report_date the report_date
	 * @param conclusion_date the conclusion_date
	 * @param devidend_req_date the devidend_req_date
	 */
	public 매각물건명세서(long goods_id, String name, String possessory,
			String info_source, String ground_claim, String period,
			String guaranty_price, String chaim, String report_date,
			String conclusion_date, String devidend_req_date) {
		this.goods_id = goods_id;
		this.name = name;
		this.possessory = possessory;
		this.info_source = info_source;
		this.ground_claim = ground_claim;
		this.period = period;
		this.guaranty_price = guaranty_price;
		this.chaim = chaim;
		this.report_date = report_date;
		this.conclusion_date = conclusion_date;
		this.devidend_req_date = devidend_req_date;
	}

	/**
	 * Insert.
	 * 
	 * @throws Exception the exception
	 */
	public void insert() throws Exception {
		PreparedStatement stmt = DB
				.prepareStatement("INSERT INTO ac_goods_statement "// 
						+ "("// 
						+ "name," // 1
						+ "possessory," // 2
						+ "info_source," // 3
						+ "ground_claim," // 4
						+ "period," // 5
						+ "guaranty_price," // 6
						+ "chaim," // 7
						+ "report_date," // 8
						+ "conclusion_date," // 9
						+ "devidend_req_date," // 10
						+ "goods_id"// 11
						+ ") "// 
						+ "VALUES (?,?,?,?,?,?,?,?,?,?,?);");
		stmt.setString(1, name);
		stmt.setString(2, possessory);
		stmt.setString(3, info_source);
		stmt.setString(4, ground_claim);
		stmt.setString(5, period);
		stmt.setString(6, guaranty_price);
		stmt.setString(7, chaim);
		stmt.setString(8, report_date);
		stmt.setString(9, conclusion_date);
		stmt.setString(10, devidend_req_date);
		stmt.setLong(11, goods_id);
		stmt.execute();
	}

	/**
	 * Find by goods id.
	 * 
	 * @param goodId the good id
	 * 
	 * @return the 매각물건명세서
	 * 
	 * @throws Exception the exception
	 */
	public static 매각물건명세서 findByGoodsId(long goodId) throws Exception {
		Statement stmt = DB.createStatement();
		ResultSet rs = stmt
				.executeQuery("SELECT * FROM  ac_goods_statement WHERE goods_id="
						+ goodId + ";");
		if (rs.next()) {
			return 매각물건명세서.load(rs);
		}
		return null;
	}

	/**
	 * Load.
	 * 
	 * @param rs the rs
	 * 
	 * @return the 매각물건명세서
	 * 
	 * @throws SQLException the SQL exception
	 */
	public static 매각물건명세서 load(ResultSet rs) throws SQLException {
		String name = rs.getString("name");
		String possessory = rs.getString("possessory");
		String info_source = rs.getString("info_source");
		String ground_claim = rs.getString("ground_claim");
		String period = rs.getString("period");
		String guaranty_price = rs.getString("guaranty_price");
		String chaim = rs.getString("chaim");
		String report_date = rs.getString("report_date");
		String conclusion_date = rs.getString("conclusion_date");
		String devidend_req_date = rs.getString("devidend_req_date");
		long goods_id = rs.getLong("goods_id");

		return new 매각물건명세서(goods_id, name, possessory, info_source,
				ground_claim, period, guaranty_price, chaim, report_date,
				conclusion_date, devidend_req_date);
	}

	/**
	 * Delete.
	 * 
	 * @throws Exception the exception
	 */
	public void delete() throws Exception {
		매각물건명세서.deleteByGoodsId(this.goods_id);
	}

	/**
	 * Delete by goods id.
	 * 
	 * @param id the id
	 * 
	 * @throws Exception the exception
	 */
	private static void deleteByGoodsId(long id) throws Exception {
		Statement stmt = DB.createStatement();
		stmt.executeUpdate("DELETE FROM ac_goods_statement WHERE goods_id=" + id+ ";");
	}
}
