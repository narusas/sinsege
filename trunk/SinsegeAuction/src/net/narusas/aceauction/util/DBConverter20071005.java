/*
 * 
 */
package net.narusas.aceauction.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.data.DB;

// TODO: Auto-generated Javadoc
/**
 * 20071005에 있었던 DB테이블 정리 작업을 위한 클래스.
 * 
 * @author narusas
 */
public class DBConverter20071005 {
	
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// updateGuaranteeRatio();
		updateTypeCode();
		// updateArea();

	}

	/**
	 * Update area.
	 */
	private static void updateArea() {
		String[] tables = { "ac_bld_statement", "ac_land_statement",
				"ac_land_right_statement" };
		for (String table : tables) {
			updateAreaTable(table);
		}

	}

	/**
	 * Update area table.
	 * 
	 * @param table the table
	 */
	private static void updateAreaTable(String table) {
		try {
			DB.dbConnect();
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT id, area FROM " + table
					+ " WHERE id>0;");
			int count = 0;
			Pattern p = Pattern.compile("([\\d.]+㎡)");

			while (rs.next()) {
				long id = rs.getLong(1);
				String area = rs.getString(2);
				System.out.println("ID=" + id + ", area=" + area);
				if (area == null) {
					continue;
				}
				Matcher m = p.matcher(area);
				if (m.find()) {
					String newArea = m.group(1);
					System.out.println("Update to " + newArea);
					PreparedStatement pStmt = DB.prepareStatement("UPDATE "
							+ table + " SET area=? WHERE id=?;");
					pStmt.setString(1, newArea);
					pStmt.setLong(2, id);
					pStmt.executeUpdate();
					DB.cleanup(pStmt);
				}

			}

			// if (false) {
			// PreparedStatement pStmt = DB
			// .prepareStatement("UPDATE ac_goods SET type_code=? WHERE id=?;");
			// pStmt.setInt(1, usage_code);
			// pStmt.setLong(2, id);
			// pStmt.executeUpdate();
			// DB.cleanup(pStmt);
			// }

			rs.close();
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update guarantee ratio.
	 */
	private static void updateGuaranteeRatio() {
		Pattern p = Pattern.compile("(\\d+)%");
		try {
			DB.dbConnect();
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT id, guarantee_ratio, guarantee_price FROM ac_goods WHERE guarantee_ratio=0;");
			int count = 0;
			while (rs.next()) {
				long id = rs.getLong(1);
				int ratio = rs.getInt(2);
				String price = rs.getString(3);
				String calcedRatio = null;
				Matcher m = p.matcher(price);
				if (m.find()) {
					calcedRatio = m.group(1);
				}

				System.out.println(id + ":" + ratio + ":" + price + ":"
						+ calcedRatio);

				if (ratio == 0 && price != null && "".equals(price) == false
						&& calcedRatio != null) {
					PreparedStatement pStmt = DB
							.prepareStatement("UPDATE ac_goods SET guarantee_ratio=? WHERE id=?;");
					pStmt.setInt(1, Integer.parseInt(calcedRatio));
					pStmt.setLong(2, id);
					pStmt.executeUpdate();
					DB.cleanup(pStmt);
				}
				DB.cleanup(rs);
				stmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update type code.
	 */
	private static void updateTypeCode() {
		try {
			DB.dbConnect();
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT id, usage_code FROM ac_goods WHERE usage_code>0;");
			int count = 0;
			while (rs.next()) {
				long id = rs.getLong(1);
				int usage_code = rs.getInt(2);
				System.out.println("ID=" + id + ", usage_code=" + usage_code);

				if (usage_code != 0) {
					PreparedStatement pStmt = DB
							.prepareStatement("UPDATE ac_goods SET type_code=? WHERE id=?;");
					pStmt.setInt(1, usage_code);
					pStmt.setLong(2, id);
					pStmt.executeUpdate();
					DB.cleanup(pStmt);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
