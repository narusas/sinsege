package net.narusas.aceauction.data;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.util.lang.NFile;

public class DB {
	private static String cfg;

	private static Connection conn;

	static Pattern datePattern = Pattern.compile("(\\d+),(\\d+)$");

	static SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd");

	static Pattern intPattern = Pattern.compile("(\\d+)");

	static Pattern p1 = Pattern.compile("([\\d\\.]+)\\s*��");

	static Pattern p2 = Pattern.compile("(\\d+)��");

	static Pattern p3 = Pattern.compile("(\\d+)");

	static {
		try {
			cfg = NFile.getText(new File("db.cfg")).trim();
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void commit() throws SQLException {
		if (conn != null && conn.isClosed() == false) {
			conn.commit();
		}
	}

	public void rollback() throws SQLException {
		if (conn != null && conn.isClosed() == false) {
			conn.rollback();
		}
	}

	public void setAutoCommit(boolean b) throws SQLException {
		if (conn != null && conn.isClosed() == false) {
			conn.setAutoCommit(b);
		}
	}

	public String toArea(String area) {
		return area;
		// if (area == null || "".equals(area)) {
		// return 0;
		// }
		// Matcher m = p1.matcher(area);
		// if (m.find()) {
		// return toFloat(m.group(1));
		// }
		// try {
		// return toFloat(area);
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// return -1;
		// }
	}

	public float toFloat(String area) {
		if (area == null) {
			return 0;
		}
		return Float.parseFloat(area);
	}

	protected void resetConnect() throws Exception {
		if (conn != null && conn.isClosed() == false) {
			conn.close();
		}
		dbConnect();
	}

	public static void cleanup(PreparedStatement stmt) {
		if (stmt == null)
			return;
		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void cleanup(ResultSet rs) {
		if (rs == null)
			return;
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void cleanup(ResultSet rs, Statement stmt) {
		cleanup(rs);
		cleanup(stmt);
	}
	public static void cleanup(Statement stmt) {
		if (stmt == null)
			return;
		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Statement createStatement() throws Exception {
		reConnect();
		return conn.createStatement();
	}

	public static Connection dbConnect() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException,
			IOException {
		if (conn != null && conn.isClosed() == false) {
			conn.close();
		}
		conn = DriverManager.getConnection(cfg);
		return conn;
	}

	public static String intToken(String str) {
		Matcher m = intPattern.matcher(str);
		if (m.find()) {
			return m.group(1);
		}
		return "0";
	}

	public static boolean isNumber(String no) {
		try {
			Integer.parseInt(no.trim());
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static PreparedStatement prepareStatement(String sql)
			throws Exception {
		reConnect();
		return conn.prepareStatement(sql);
	}

	public static void reConnect() throws Exception {
		if (conn == null) {
			dbConnect();
			return;
		}
		if (conn != null && conn.isClosed()) {
			dbConnect();
		}

	}

	public static Date toDate(String ����) {
		// System.out.println(����);
		if (���� == null) {
			return null;
		}

		Matcher m = datePattern.matcher(����.trim());
		if (m.find()) {
			String monthStr = m.group(1);
			String yearStr = m.group(2);
			int year = Integer.parseInt(yearStr) - 1900;
			int month = Integer.parseInt(monthStr) - 1;
			int day = 1;
			return new Date(year, month, day);
		} else {
			String[] tokens = ����.split("[\\.-]");

			// for (String string : tokens) {
			// System.out.println(string);
			// }
			if (tokens == null || tokens.length < 3) {
				return null;
			}
			int yearTemp = Integer.parseInt(intToken(tokens[0]));
			if (yearTemp < 10) {
				yearTemp += 2000;
			} else if (yearTemp < 100) {
				yearTemp += 1900;
			}

			int year = yearTemp - 1900;
			int month = Integer.parseInt(intToken(tokens[1])) - 1;
			int day = Integer.parseInt(intToken(tokens[2]));

			return new Date(year, month, day);
		}

	}

	public static int toInt(String code) {
		if (code == null) {
			return 0;
		}
		return Integer.parseInt(code);
	}

	public static String token(String src, String sp, int i) {
		if (src == null) {
			return null;
		}
		String[] temp = src.split(sp);
		return temp[i].trim();
	}

	public static long toLong(String v) {
		if (v == null || "".equals(v)) {
			return 0;
		}
		return Long.parseLong(v);
	}

	public static long toPrice(String price) {

		price = price.replaceAll(",", "");
		Matcher m = p2.matcher(price);
		if (m.find()) {
			price = m.group(1);
		} else {
			m = p3.matcher(price);
			if (m.find()) {
				price = m.group(1);
			}
		}

		return toLong(price);
	}

	public static String toString(Date date) {
		int year = date.getYear();
		int month = date.getMonth();
		int day = date.getDate();

		return formater.format(date);
	}
}