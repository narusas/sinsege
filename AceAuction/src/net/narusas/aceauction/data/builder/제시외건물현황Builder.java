package net.narusas.aceauction.data.builder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.model.제시외건물;

public class 제시외건물현황Builder {

	private final int id;

	private final List<제시외건물> 제시외건물s;

	public 제시외건물현황Builder(int id, List<제시외건물> 제시외건물s) {
		this.id = id;
		this.제시외건물s = 제시외건물s;
	}

	public void update() throws Exception {
		if (제시외건물s == null || 제시외건물s.size() == 0) {
			return;
		}

		insert제시외건물s();
	}

	private boolean exist(int id2, int no) throws Exception {
		Statement stmt = DB.createStatement();
		ResultSet rs = stmt
				.executeQuery("SELECT * FROM ac_exclusion WHERE goods_id="
						+ id2 + " AND no=" + no + ";");
		return rs != null && rs.next();
	}

	private String handleAreaMessage(String area, String type) {
		if ("기계기구".equals(type)) {
			return area;
		}
		try {
			float f = Float.parseFloat(area);
			return area + "㎡";
		} catch (Throwable ex) {

		}
		return area;
	}

	private void insert제시외건물(int no, 제시외건물 제시외건물) throws Exception {
		// int no = 제시외건물.get물건번호();
		String address = 제시외건물.getAddress();
		String use = 제시외건물.get용도();
		String str = 제시외건물.get구조();
		String area = 제시외건물.get면적();
		String type = 제시외건물.get종류();
		String floor = 제시외건물.get층형();
		String contains = 제시외건물.get포함여부String();
		if (exist(id, no)) {
			return;
		}

		PreparedStatement stmt = DB
				.prepareStatement("INSERT INTO ac_exclusion " + //
						"(" // +
						+ "no," // 1
						+ "goods_id,"// 2
						+ "use_for," // 3
						+ "structure," // 4
						+ "area," // 5
						+ "type," // 6
						+ "floor," // 7
						+ "address," // 8
						+ "contains" // 9
						+ ") " //
						+ "VALUES (?,?,?,?,?,?,?,?,?);");
		stmt.setInt(1, no);
		stmt.setInt(2, id);
		stmt.setString(3, use);
		stmt.setString(4, str);
		stmt.setString(5, handleAreaMessage(area, type));
		stmt.setString(6, type);
		stmt.setString(7, floor);
		stmt.setString(8, address);
		stmt.setString(9, contains);
		stmt.execute();

	}

	private void insert제시외건물s() throws Exception {
		DB.reConnect();
		for (int i = 0; i < 제시외건물s.size(); i++) {
			insert제시외건물(i + 1, 제시외건물s.get(i));
		}
	}
}
