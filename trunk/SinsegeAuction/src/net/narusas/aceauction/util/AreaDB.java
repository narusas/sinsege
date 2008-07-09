/*
 * 
 */
package net.narusas.aceauction.util;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.data.DB;
import net.narusas.util.lang.NFile;

// TODO: Auto-generated Javadoc
/**
 * The Class AreaDB.
 */
public class AreaDB extends DB {

	/**
	 * Clear.
	 * 
	 * @throws Exception the exception
	 */
	public void clear() throws Exception {
		dbConnect();
		Statement stmt = createStatement();
		stmt.executeQuery("DELETE FROM ac_area WHERE code>=0;");
	}

	/**
	 * Update.
	 * 
	 * @throws Exception the exception
	 */
	public void update() throws Exception {
		dbConnect();
		String src = NFile.getText(new File("fixtures/Áö¿ª.txt"), "euc-kr");
		Pattern p = Pattern.compile("_js_area\\[(\\d+)\\]\\[0\\] = '(\\d+)'\\s*"
				+ "_js_area\\[\\d+\\]\\[1\\] = '([^']+)'\\s*"
				+ "_js_area\\[\\d+\\]\\[2\\] = '(\\d+)'");
		Matcher m = p.matcher(src);
		while (m.find()) {
			// int code = toInt(m.group(1));
			int code = toInt(m.group(2));
			String name = m.group(3);
			int parent_code = toInt(m.group(4));
			PreparedStatement stmt = prepareStatement("INSERT INTO ac_area (code, name, parent_code) "
					+ "VALUES (?,?,?);");
			stmt.setInt(1, code);
			stmt.setString(2, name);
			stmt.setInt(3, parent_code);
			stmt.execute();
		}
	}

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 * 
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		AreaDB db = new AreaDB();
		db.update();
	}
}
