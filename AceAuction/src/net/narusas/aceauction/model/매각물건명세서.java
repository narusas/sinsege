package net.narusas.aceauction.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.narusas.aceauction.data.DB;

public class 매각물건명세서 {

	private final String chaim;
	private final String conclusion_date;
	private final String devidend_req_date;
	private final long goods_id;
	private final String ground_claim;
	private final String guaranty_price;
	private final String info_source;
	private final String name;
	private final String period;
	private final String possessory;
	private final String report_date;

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

	public void delete() throws Exception {
		매각물건명세서.deleteByGoodsId(this.goods_id);
	}

	private static void deleteByGoodsId(long id) throws Exception {
		Statement stmt = DB.createStatement();
		stmt.executeUpdate("DELETE FROM ac_goods_statement WHERE goods_id=" + id+ ";");
	}
}
