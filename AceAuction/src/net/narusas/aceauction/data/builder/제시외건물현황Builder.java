package net.narusas.aceauction.data.builder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.model.���ÿܰǹ�;

public class ���ÿܰǹ���ȲBuilder {

	private final int id;

	private final List<���ÿܰǹ�> ���ÿܰǹ�s;

	public ���ÿܰǹ���ȲBuilder(int id, List<���ÿܰǹ�> ���ÿܰǹ�s) {
		this.id = id;
		this.���ÿܰǹ�s = ���ÿܰǹ�s;
	}

	public void update() throws Exception {
		if (���ÿܰǹ�s == null || ���ÿܰǹ�s.size() == 0) {
			return;
		}

		insert���ÿܰǹ�s();
	}

	private boolean exist(int id2, int no) throws Exception {
		Statement stmt = DB.createStatement();
		ResultSet rs = stmt
				.executeQuery("SELECT * FROM ac_exclusion WHERE goods_id="
						+ id2 + " AND no=" + no + ";");
		return rs != null && rs.next();
	}

	private String handleAreaMessage(String area, String type) {
		if ("���ⱸ".equals(type)) {
			return area;
		}
		try {
			float f = Float.parseFloat(area);
			return area + "��";
		} catch (Throwable ex) {

		}
		return area;
	}

	private void insert���ÿܰǹ�(int no, ���ÿܰǹ� ���ÿܰǹ�) throws Exception {
		// int no = ���ÿܰǹ�.get���ǹ�ȣ();
		String address = ���ÿܰǹ�.getAddress();
		String use = ���ÿܰǹ�.get�뵵();
		String str = ���ÿܰǹ�.get����();
		String area = ���ÿܰǹ�.get����();
		String type = ���ÿܰǹ�.get����();
		String floor = ���ÿܰǹ�.get����();
		String contains = ���ÿܰǹ�.get���Կ���String();
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

	private void insert���ÿܰǹ�s() throws Exception {
		DB.reConnect();
		for (int i = 0; i < ���ÿܰǹ�s.size(); i++) {
			insert���ÿܰǹ�(i + 1, ���ÿܰǹ�s.get(i));
		}
	}
}
