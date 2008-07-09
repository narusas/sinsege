package net.narusas.aceauction.fetchers.onara;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.fetchers.PageFetcher;
import net.narusas.util.lang.NInputStream;

import org.apache.commons.httpclient.HttpException;

public class OnaraLandFetcher {
	public List<LandInfo> getSubLandInfos(String code) throws HttpException,
			IOException {
		List<LandInfo> result = new ArrayList<LandInfo>();
		PageFetcher fetcher = new PageFetcher(
				"http://www.onnara.go.kr/ep/map/datause/getSggList.jsp?code=");
		String page = fetcher.fetch(code);

		Pattern p = Pattern.compile("<data>([^>]*)</data>");
		Matcher m = p.matcher(page);
		while (m.find()) {
			String data = m.group(1).trim();
			String[] tokens = data.split(";");
			if (tokens.length == 0) {
				continue;
			}
			result.add(new LandInfo(tokens[0].trim(), tokens[1].trim()));
		}
		return result;
	}

	private static List<String> parseCode(String mapinfo) {
		Pattern p = Pattern.compile("\\\"(\\d+)");
		Matcher m = p.matcher(mapinfo);
		List<String> result = new ArrayList<String>();
		while (m.find()) {
			result.add(m.group(1).trim());
		}
		return result;
	}

	public static void main(String[] args) {
		OnaraLandFetcher f = new OnaraLandFetcher();
		try {
			String mapinfo = NInputStream.getText(OnaraLandFetcher.class
					.getResourceAsStream("onara_map.txt"));

			List<String> codes = parseCode(mapinfo);
			for (String code : codes) {

				System.out.println("#################### " + code);
				insertLandInfos(code, f.getSubLandInfos(code));
			}

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void insertLandInfos(String parentCode, List<LandInfo> infos)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, IOException {
		Connection conn = DB.dbConnect();

		for (LandInfo info : infos) {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM AC_LANDINFO WHERE code_cd="
							+ info.getCode());
			if (rs.next()) {
				DB.cleanup(rs);
				continue;
			}
			System.out.println("Insert " + info);
			PreparedStatement preparedStmt = conn
					.prepareStatement("INSERT INTO AC_LANDINFO (code_cd, code_nm, code_pr) VALUES (?,?,?)");
			preparedStmt.setString(1, info.code);
			preparedStmt.setString(2, info.name);
			preparedStmt.setString(3, parentCode);
			preparedStmt.execute();
			DB.cleanup(preparedStmt);
		}
	}

}
