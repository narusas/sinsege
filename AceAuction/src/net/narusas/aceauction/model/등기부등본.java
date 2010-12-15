package net.narusas.aceauction.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.narusas.aceauction.data.DB;

public class 등기부등본 {

	private long attested_id;

	private final long goodsId;

	private final String type;

	public 등기부등본(long parentID, String type) {
		this.goodsId = parentID;
		this.type = type;
	}

	public long getId() {
		return attested_id;
	}

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

	public static 등기부등본 findByGoodsId(long goodsId, String type)
			throws Exception {
		Statement st = DB.createStatement();
		ResultSet rs = st.executeQuery("SELECT * " + //
				"FROM ac_attested " + //
				"WHERE goods_id=" + goodsId + " AND TYPE=\"" + type + "\";");
		if (rs.next()) {
			return 등기부등본.load(rs);
		}
		return null;
	}

	private static 등기부등본 load(ResultSet rs) throws SQLException {
		long goodsId = rs.getLong("goods_id");
		String type = rs.getString("type");
		return new 등기부등본(goodsId, type);
	}
}
