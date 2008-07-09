package net.narusas.aceauction.fetchers.onara;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;

import net.narusas.aceauction.data.DB;

public class OnaraLandInfoRecursive {
	static Map<String, LandInfo> infoMap = new HashMap<String, LandInfo>();

	public static void main(String[] args) {
		OnaraLandFetcher f = new OnaraLandFetcher();
		try {
			List<LandInfo> infos = new ArrayList<LandInfo>();

			Connection conn = DB.dbConnect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM AC_LANDINFO");

			while (rs.next()) {
				LandInfo info = new LandInfo(rs.getString("code_cd"), rs
						.getString("code_nm"), rs.getString("code_pr"));
				infos.add(info);
				infoMap.put(info.getCode(), info);
			}

			DB.cleanup(rs, stmt);

			workSub(f, conn, infos);

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void workSub(OnaraLandFetcher f, Connection conn,
			List<LandInfo> alreadyExistInfos) throws HttpException,
			IOException, SQLException {
		for (int i = alreadyExistInfos.size() - 1; i >= 0; i--) {
			LandInfo info = alreadyExistInfos.get(i);
			List<LandInfo> sub = f.getSubLandInfos(info.getCode());

			for (LandInfo subInfo : sub) {
				if (infoMap.containsKey(subInfo.getCode())) {
					System.out.println("Skip :"+subInfo.getCode());
					continue;
				}

				// Statement stmt = conn.createStatement();
				// System.out.println("Select :" + subInfo.getCode());
				// ResultSet rs = stmt
				// .executeQuery("SELECT code_cd FROM AC_LANDINFO WHERE
				// code_cd="
				// + subInfo.getCode());
				// if (rs.next()) {
				// DB.cleanup(rs, stmt);
				// continue;
				// }

				// DB.cleanup(rs, stmt);

				System.out.println("Insert " + subInfo);
				PreparedStatement preparedStmt = conn
						.prepareStatement("INSERT INTO AC_LANDINFO (code_cd, code_nm, code_pr) VALUES (?,?,?)");
				preparedStmt.setString(1, subInfo.code);
				preparedStmt.setString(2, subInfo.name);
				preparedStmt.setString(3, info.getParent());
				preparedStmt.execute();
				DB.cleanup(preparedStmt);
			}

		}
	}
}
