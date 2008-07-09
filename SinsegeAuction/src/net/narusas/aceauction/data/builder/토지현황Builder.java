/*
 * 
 */
package net.narusas.aceauction.data.builder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.data.현황주소Converter;
import net.narusas.aceauction.model.Row;
import net.narusas.aceauction.model.Table;

// TODO: Auto-generated Javadoc
/**
 * The Class 토지현황Builder.
 */
public class 토지현황Builder{

	/** The id. */
	private final int id;

	/** The 토지현황. */
	private final Table 토지현황;

	/**
	 * Instantiates a new 토지현황 builder.
	 * 
	 * @param id the id
	 * @param 토지현황 the 토지현황
	 */
	public 토지현황Builder(int id, Table 토지현황) {
		this.id = id;
		this.토지현황 = 토지현황;
	}

	/**
	 * Update.
	 * 
	 * @throws Exception the exception
	 */
	public void update() throws Exception {
		if (토지현황 == null) {
			return;
		}
		insert토지현황();
	}

	/**
	 * Exist.
	 * 
	 * @param id2 the id2
	 * 
	 * @return true, if successful
	 * 
	 * @throws Exception the exception
	 */
	private boolean exist(int id2) throws Exception {
		ResultSet rs = null;
		try {
			Statement stmt = DB.createStatement();
			rs = stmt.executeQuery("SELECT * FROM ac_land_statement WHERE goods_id=" + id2 + ";");
			return rs != null && rs.next();
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * Exist.
	 * 
	 * @param id2 the id2
	 * @param no the no
	 * 
	 * @return true, if successful
	 * 
	 * @throws Exception the exception
	 */
	private boolean exist(int id2, String no) throws Exception {
		ResultSet rs = null;
		try {
			Statement stmt = DB.createStatement();
			stmt.executeQuery("SELECT * FROM ac_land_statement WHERE goods_id=" + id2 + " AND no="
					+ no + ";");
			return rs != null && rs.next();
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * Insert토지현황.
	 * 
	 * @throws Exception the exception
	 */
	private void insert토지현황() throws Exception {
		DB.reConnect();
		if (exist(id)) {
			return;
		}
		for (int i = 0; i < 토지현황.getRows().size(); i++) {
			if (토지현황.getRows().get(i).size() == 2) {
				update물건토지현황Comment(토지현황.getRows().get(i));
				break;
			}
			insert토지현황(토지현황.getRows().get(i));
		}
	}

	/**
	 * Insert토지현황.
	 * 
	 * @param record the record
	 * 
	 * @throws Exception the exception
	 */
	private void insert토지현황(Row record) throws Exception {
//		System.out.println(record);
		String no = record.getValue(0);
		if (DB.isNumber(no) == false) {
			return;
		}
		String address = record.getValue(1);
//		System.out.println("ADDR:" + address);
		String use = record.getValue(2);
		String use2 = record.getValue(3);
		String std_price = record.getValue(4);
		String area = record.getValue(5);
		String each_price = record.getValue(6);
		String test_price = record.getValue(7);
		String comment = record.getValue(8);
		if (exist(id, no)) {
			return;
		}
		PreparedStatement stmt = DB.prepareStatement("INSERT INTO ac_land_statement " + //
				"(" // +
				+ "goods_id,"// 1
				+ "address," // 2
				+ "use_for," // 3
//				+ "use_for2," // 3
//				+ "std_price," // 4
				+ "area," // 4
//				+ "each_price," // 6
//				+ "test_price," // 7
//				+ "comment," // 8
				+ "no" // 5
				+ ") " //
				+ "VALUES (?,?,?,?,?);");
		stmt.setInt(1, id);
		stmt.setString(2, 현황주소Converter.convert(address));
		stmt.setString(3, use);
//		stmt.setString(4, use2);
//		stmt.setString(5, 금액Converter.convert(std_price));
		stmt.setString(4, area);
//		stmt.setString(7, 금액Converter.convert(each_price));
//		stmt.setString(8, 금액Converter.convert(test_price));
//		stmt.setString(9, comment);
		stmt.setInt(5,DB.toInt(no));
//		System.out.println(stmt.toString());
		stmt.execute();

	}

	/**
	 * Update물건토지현황 comment.
	 * 
	 * @param record the record
	 * 
	 * @throws Exception the exception
	 */
	private void update물건토지현황Comment(Row record) throws Exception {
		PreparedStatement stmt = DB.prepareStatement("UPDATE  ac_goods SET land_statement_comment=? WHERE id=?;");
		stmt.setString(1, record.getValue(1));
		stmt.setLong(2, id);
		stmt.execute();

	}
}
