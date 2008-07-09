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
 * The Class ���ε.
 */
public class ���ε {

	/** The attested_id. */
	private long attested_id;

	/** The goods id. */
	private final long goodsId;

	/** The type. */
	private final String type;

	/**
	 * Instantiates a new ���ε.
	 * 
	 * @param parentID the parent id
	 * @param type the type
	 */
	public ���ε(long parentID, String type) {
		this.goodsId = parentID;
		this.type = type;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return attested_id;
	}

	/**
	 * Insert.
	 * 
	 * @throws Exception the exception
	 */
	public void insert() throws Exception {
		PreparedStatement stmt = DB.prepareStatement("INSERT INTO ac_attested "
				+ "(goods_id, "// 1
				+ "type) "// 2
				+ "VALUES(?,?);"); // ;
		stmt.setLong(1, goodsId);
		stmt.setString(2, type);
		stmt.execute();
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		attested_id = rs.getLong(1);
		rs.close();
	}

	/**
	 * Find by goods id.
	 * 
	 * @param goodsId the goods id
	 * @param type the type
	 * 
	 * @return the ���ε
	 * 
	 * @throws Exception the exception
	 */
	public static ���ε findByGoodsId(long goodsId, String type)
			throws Exception {
		Statement st = DB.createStatement();
		ResultSet rs = st.executeQuery("SELECT * " + //
				"FROM ac_attested " + //
				"WHERE goods_id=" + goodsId + " AND TYPE=\"" + type + "\";");
		if (rs.next()) {
			return ���ε.load(rs);
		}
		return null;
	}

	/**
	 * Load.
	 * 
	 * @param rs the rs
	 * 
	 * @return the ���ε
	 * 
	 * @throws SQLException the SQL exception
	 */
	private static ���ε load(ResultSet rs) throws SQLException {
		long goodsId = rs.getLong("goods_id");
		String type = rs.getString("type");
		return new ���ε(goodsId, type);
	}
}
