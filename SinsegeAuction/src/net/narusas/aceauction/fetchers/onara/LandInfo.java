package net.narusas.aceauction.fetchers.onara;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.narusas.aceauction.data.DB;

public class LandInfo {
	String name;

	String code;

	String parent;

	public LandInfo(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public LandInfo(String code, String name, String parent) {
		this.code = code;
		this.name = name;
		this.parent = parent;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "LandInfo=[" + name + "," + code + "]";
	}

	public String getParent() {
		return parent;
	}

	public void insert() throws SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, IOException {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		try {
			conn = DB.dbConnect();
			preparedStmt = conn
					.prepareStatement("INSERT INTO AC_LANDINFO (code_cd, code_nm, code_pr) VALUES (?,?,?)");
			preparedStmt.setString(1, code);
			preparedStmt.setString(2, name);
			preparedStmt.setString(3, parent);
			preparedStmt.execute();
		} finally {
			DB.cleanup(preparedStmt);
		}
	}

	public static LandInfo find(String code) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException,
			IOException {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;
		try {
			conn = DB.dbConnect();
			preparedStmt = conn
					.prepareStatement("SELECT code_nm, code_pr FROM AC_LANDINFO WHERE code_cd=?");
			preparedStmt.setString(1, code);
			rs = preparedStmt.executeQuery();
			if (rs.next() == false) {
				return null;
			}
			String name = rs.getString(1);
			String parent = rs.getString(2);

			return new LandInfo(code, name, parent);
		} finally {
			DB.cleanup(rs, preparedStmt);
		}
	}
}