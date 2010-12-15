package net.narusas.aceauction.data.builder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.data.FileUploader;
import net.narusas.aceauction.data.매각기일UpdaterDB;
import net.narusas.aceauction.data.물건종별Converter;
import net.narusas.aceauction.fetchers.대법원매각물건명세서Fetcher;
import net.narusas.aceauction.fetchers.스피드옥션_물건Updater;
import net.narusas.aceauction.model.물건;
import net.narusas.aceauction.model.부동산표시목록;
import net.narusas.aceauction.model.부동산표시목록Item;
import net.narusas.aceauction.model.사건;
import net.narusas.util.lang.NFile;
import net.narusas.util.lang.NInputStream;

public class 물건Builder {

	private int id;

	private final BuildProgressListener listener;

	private long sagunId;

	private 사건 사건;

	Logger logger = Logger.getLogger("log");

	public 물건Builder(BuildProgressListener listener) {
		this.listener = listener;
	}

	public void update물건DBs(사건 사건, boolean hasSagunPicture, 부동산표시목록 item, String[] gamjungInfo) throws Exception {
		try {
			DB.dbConnect();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		this.사건 = 사건;
		this.sagunId = 사건.getDbid();
		logger.info("물건을 DB에 입력합니다. " + 사건.court.getName() + " " + 사건.charge.get담당계이름() + " " + 사건.get사건번호() + " ");
		DB.reConnect();
		물건[] mul = 사건.get물건s();
		listener.update물건Size(mul.length);
		for (물건 물건 : mul) {
			try {
				update물건DB(물건, sagunId, hasSagunPicture, item, gamjungInfo);
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
			FileUploader.getInstance().waitDone();
		}
	}

	private int findMatchArea(int parentCode, String 주소, Statement stmt, int depth) throws SQLException {

		if (depth == 0) {
			return parentCode;
		}
		ResultSet rs = stmt.executeQuery("SELECT * FROM ac_area WHERE parent_code=" + parentCode + ";");
		while (rs.next()) {
			String name = rs.getString(2);
			if (주소.contains(name)) {
				return findMatchArea(rs.getInt(1), 주소, stmt, depth - 1);
			}
		}
		return parentCode;
	}

	private int getAreaCode(String 주소, int depth) throws Exception {
		Statement stmt = DB.createStatement();
		return findMatchArea(1, 주소, stmt, depth);
	}

	private int getExisting물건Id(물건 물건, long sagunId) throws Exception {
		PreparedStatement stmt = DB.prepareStatement("SELECT * FROM ac_goods WHERE event_no=? AND no=?;");
		stmt.setLong(1, sagunId);
		stmt.setInt(2, 물건.get물건번호());
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		if (rs.next()) {
			return rs.getInt("id");
		}
		return -1;
	}

	// 대법원 물건 종류
	private int getTypeCode(String 물건종류) throws Exception {
		PreparedStatement stmt = DB.prepareStatement("SELECT * FROM ac_goods_type WHERE name=?;");
		stmt.setString(1, 물건종류);
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		if (rs != null && rs.next()) {
			return rs.getInt(1);
		} else {
			PreparedStatement stmt2 = DB.prepareStatement("INSERT INTO ac_goods_type (name) VALUES (?)");
			stmt2.setString(1, 물건종류);
			stmt2.execute();
			ResultSet rs2 = stmt2.getGeneratedKeys();
			rs2.next();
			return rs2.getInt(1);
		}
	}

	// 스피드옥션 물건종별
	private int getUsageCode(String 물건종별) throws Exception {
		// System.out.println("물건종별:" + 물건종별);

		PreparedStatement stmt = DB.prepareStatement("SELECT * FROM ac_goods_usage WHERE name=?;");

		stmt.setString(1, 물건종별Converter.convert(물건종별.trim()));
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		if (rs != null && rs.next()) {
			return rs.getInt(1);
		} else {
			PreparedStatement stmt2 = DB
					.prepareStatement("SELECT (code) FROM ac_goods_usage WHERE name=? AND parent_code=4;");
			stmt2.setString(1, "기타");
			stmt2.execute();
			ResultSet rs2 = stmt.getResultSet();
			if (rs2.next()) {
				return rs2.getInt(1);
			}
			return 0;
		}
	}

	private int insertBasic(물건 물건, long sagunId) throws Exception {
		PreparedStatement stmt = DB.prepareStatement("INSERT INTO ac_goods "// 
				+ "("// 
				+ "no, "// 1
				// + "type_code,"// 2
				// + "sell_target,"// 3
				+ "sell_price,"// 2
				+ "lowest_price,"// 3
				+ "accept_date,"// 4
				+ "decision_date,"// 5
				+ "devidend_date,"// 6
				+ "comment,"// 7
				+ "court_code," // 8
				+ "charge_id," // 9
				+ "event_no," // 10
				+ "area_code," // 11
				+ "area_code_2," // 12
				+ "area_code_3," // 13
				+ "address" // 14
				+ ") " + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);");

		stmt.setInt(1, 물건.get물건번호());
		// stmt.setInt(2, 0);//getTypeCode(물건.get물건종류()));
		// stmt.setString(3,
		// "");//매각대상Converter.convert(물건.getDetail().get매각대상()));
		stmt.setLong(2, DB.toPrice(물건.get가격()));
		stmt.setLong(3, DB.toPrice(물건.get최저가()));
		stmt.setDate(4, 물건.사건.get접수일자());
		stmt.setDate(5, 물건.사건.get개시결정일자());
		stmt.setDate(6, DB.toDate(물건.get배당요구종기일()));
		stmt.setString(7, 물건.get비고());
		stmt.setInt(8, DB.toInt(물건.get사건().court.getCode()));
		stmt.setLong(9, 물건.get사건().charge.getNo());
		stmt.setLong(10, sagunId);
		stmt.setInt(11, getAreaCode(물건.get주소(), 1));
		stmt.setInt(12, getAreaCode(물건.get주소(), 2));
		stmt.setInt(13, getAreaCode(물건.get주소(), 3));
		stmt.setString(14, 물건.get주소());
		stmt.execute();
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		int no = rs.getInt(1);
		rs.close();
		return no;
	}

	private void insert물건(물건 물건, long sagunId, 부동산표시목록 item, String[] gamjungInfo) throws SQLException {
		// conn.setAutoCommit(false);
		try {
			id = insertBasic(물건, sagunId);
			update감정평가(id, 물건, gamjungInfo);
			기일내역Builder db = new 기일내역Builder(id, 물건);
			db.update();

			logger.log(Level.INFO, "부동산 표시목록db을 갱신합니다. ");
			List<부동산표시목록Item> target = item.getItem(물건);
			if (target != null && target.size() > 0) {
				부동산표시목록Builder budongsaDb = new 부동산표시목록Builder(id, 물건, target);
				budongsaDb.update();
			}

			// 건물현황DB 건물현황DB = new 건물현황DB(id, 물건, 물건.getDetail().get건물현황());
			// 건물현황DB.update();
			// 대지권현황DB 대지권현황DB = new 대지권현황DB(id, 물건, 물건.getDetail().get대지권현황());
			// 대지권현황DB.update();
			// new 토지현황DB(id, 물건.getDetail().get토지현황()).update();
			if (물건.has명세서) {
				대법원매각물건명세서Fetcher f = new 대법원매각물건명세서Fetcher();
				try {
					물건 = f.update(물건);
					new 매각물건명세서Builder(id, 물건.get명세서()).update();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			new 제시외건물현황Builder(id, 물건.get제시외건물s()).update();
			new 대법원물건현황Builder(id, 물건.get물건현황()).update();
			매각기일UpdaterDB appoint = new 매각기일UpdaterDB(null);
			appoint.update(id);

			updateSpecialCase(id);
			// conn.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.log(Level.INFO, "insert error", ex);
			// conn.rollback();
			throw new SQLException(ex);
		} finally {
			// conn.setAutoCommit(true);
		}
	}

	private void updateSpecialCase(int id) {
		String[] src = 물건.getSpecialCaseSource(id);
		물건.updateSpecialCase(src, id);
	}

	private void updateBasic(int id, 물건 물건) throws Exception {
		logger.info("물건(id=" + id + ")의 기본정보인  charge_id와 done을 각각 " + 물건.사건.charge.getNo()
				+ ",0 으로 갱신합니다. sell_price=" + DB.toPrice(물건.get가격()) + ", lowest_price=" + DB.toPrice(물건.get최저가())
				+ ", accept_date=" + 물건.사건.get접수일자() + ", decision_date=" + 물건.사건.get개시결정일자() + ", devidend_date="
				+ DB.toDate(물건.get배당요구종기일())

		);
		PreparedStatement stmt = DB.prepareStatement("UPDATE ac_goods SET " + "done=0, "// 
				+ "charge_id=?, " // 1
				+ "sell_price=?, "// 2
				+ "lowest_price=?, "// 3
				+ "accept_date=?,"// 4
				+ "decision_date=?,"// 5
				+ "devidend_date=? "// 6

				+ "WHERE id=?;");// 4
		stmt.setLong(1, 물건.사건.charge.getNo());
		stmt.setLong(2, DB.toPrice(물건.get가격()));
		stmt.setLong(3, DB.toPrice(물건.get최저가()));

		stmt.setDate(4, 물건.사건.get접수일자());
		stmt.setDate(5, 물건.사건.get개시결정일자());
		stmt.setDate(6, DB.toDate(물건.get배당요구종기일()));

		stmt.setInt(7, id);

		stmt.executeUpdate();
	}

	private void update감정평가(int id, 물건 물건, String[] gamjungInfo) throws Exception {

		// System.out.println("####" + gamjungInfo[0] + " " + gamjungInfo[1]);
		String src = null;
		Date date = null;
		if ("".equals(gamjungInfo[0]) || gamjungInfo[0] == null) {
			src = 물건.getDetail().get감정평가기관().trim();
		} else {
			src = gamjungInfo[0].trim();
		}
		if (src.endsWith("감정")) {
			src = src + "사무소";
		}

		Statement queryStmt = DB.createStatement();
		ResultSet rs = queryStmt.executeQuery("SELECT judgement_office,judgement_date FROM ac_goods WHERE id=" + id
				+ ";");
		rs.next();
		String oldOffice = rs.getString("judgement_office");
		int oldDateValue = rs.getInt("judgement_date");
		Date oldDate = null;
		if (oldDateValue != 0) {
			oldDate = rs.getDate("judgement_date");
		}

		if (oldOffice != null && "".equals(oldOffice) == false) {
			logger
					.info("Goods have old judgememt_office. use old judgememt_office " + oldOffice + " instead of "
							+ src);
			src = oldOffice;
		}
		if (oldDate != null) {
			logger.info("Goods have old judgement_date. use old judgement_date " + oldOffice + " instead of " + src);
			date = oldDate;
		}

		if (gamjungInfo[1] == null || "".equals(gamjungInfo[1])) {
			PreparedStatement stmt = DB.prepareStatement("UPDATE ac_goods "// 
					+ "SET " + "judgement_office=? " // 1
					+ "WHERE id=?;");

			stmt.setString(1, src);
			stmt.setInt(2, id);
			stmt.execute();
		} else {
			PreparedStatement stmt = DB.prepareStatement("UPDATE ac_goods "// 
					+ "SET " + "judgement_office=?," // 1
					+ "judgement_date=? " // 2
					+ "WHERE id=?;");

			if ("".equals(gamjungInfo[1]) || gamjungInfo[1] == null) {
				date = DB.toDate(물건.getDetail().get감정시점());
			} else {
				date = DB.toDate(gamjungInfo[1]);
			}

			stmt.setString(1, src);
			stmt.setDate(2, date);
			stmt.setInt(3, id);
			stmt.execute();
		}

	}

	private void update등기부등본(물건 물건, String url) throws Exception {
		if (url == null || "".equals(url) || url.startsWith("http") == false) {
			return;
		}
		등기부등본Builder db = new 등기부등본Builder(id, url, 물건.사건.소유자.toArray(new String[] {}), 물건.get등기부등본FilePath());
		try {
			db.execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void update등기부등본DB(물건 물건) throws Exception {
		logger.info("등기부등본 PDF파일을 업로드 합니다. ");
		update등기부등본(물건, 물건.get건물등기부등본());
		update등기부등본(물건, 물건.get토지등기부등본());
	}

	private void update물건(물건 물건, long sagunId, int id, 부동산표시목록 item, String[] gamjungInfo) throws SQLException {
		// setAutoCommit(false);
		try {
			// id = getExisting물건Id(물건, sagunId);
			updateBasic(id, 물건);
			update감정평가(id, 물건, gamjungInfo);
			// update감정평가(id, 물건);
			logger.info("기일내역을 갱신합니다. ");
			기일내역Builder db = new 기일내역Builder(id, 물건);
			db.update();

			logger.info("부동산표시목록을 갱신합니다. ");
			try {
				List<부동산표시목록Item> target = item.getItem(물건);
				if (target != null && target.size() > 0) {
					부동산표시목록Builder budongsaDb = new 부동산표시목록Builder(id, 물건, target);
					budongsaDb.update();
				}

			} catch (Throwable e) {
				logger.log(Level.WARNING, "Exception in 부동산표시목록", e);
				e.printStackTrace();
			}

			if (물건.has명세서) {
				logger.info("매각 물건 명세서 내역을 갱신합니다. ");
				대법원매각물건명세서Fetcher f = new 대법원매각물건명세서Fetcher();
				try {
					물건 = f.update(물건);
					new 매각물건명세서Builder(id, 물건.get명세서()).update();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				logger.info("매각물건 명세서 내역를 가지지 않는 물건입니다. ");
			}

			new 제시외건물현황Builder(id, 물건.get제시외건물s()).update();
			new 대법원물건현황Builder(id, 물건.get물건현황()).update();
			매각기일UpdaterDB appoint = new 매각기일UpdaterDB(null);
			appoint.update(id);
			// commit();

		} catch (Exception ex) {
			logger.log(Level.WARNING, "물건DB를 갱신하는 도중에 문제가 발생했습니다. 작업을 롤백합니다.", ex);
			ex.printStackTrace();
			// rollback();
			throw new SQLException(ex);
		} finally {
			// setAutoCommit(true);
		}

	}

	private void update물건DB(물건 물건, long sagunId, boolean hasSagunPicture, 부동산표시목록 item, String[] gamjungInfo)
			throws Exception {
		logger.info("물건을 DB에 입력합니다. " + 물건.사건.get사건번호() + ":" + 물건.get물건번호());

		스피드옥션_물건Updater.updateDetail(물건, gamjungInfo);
		id = getExisting물건Id(물건, sagunId);

		if (id != -1) {
			logger.info("DB에 존재하는 물건입니다. DB의 정보를 갱신합니다. ");
			update물건(물건, sagunId, id, item, gamjungInfo);
		} else {
			logger.info("새로운 물건입니다. DB에 정보를 추가합니다. ");
			insert물건(물건, sagunId, item, gamjungInfo);
		}

		upload매각물건명세서(물건);
		if (hasSagunPicture == false) {
			upload사진(물건);
		}

		update등기부등본DB(물건);
		listener.progress(BuildProgressListener.LEVEL_물건);
	}

	private void upload매각물건명세서(물건 물건) throws IOException {
		FileUploader uploader = FileUploader.getInstance();
		if (물건.get매각물건명세서html() == null) {
			return;
		}
		logger.info("매각물건명세서 HTML파일을 업로드 합니다. ");
		File f = File.createTempFile("Mulgun", ".html");
		NFile.write(f, 물건.get매각물건명세서html());
		uploader.upload(물건.get물건FilePath(), "Mulgun.html", f);
	}

	private void upload사진(물건 물건) throws IOException {
		logger.info("사진을 업로드 합니다. ");
		List<String> picUrlList = 물건.get사진();

		FileUploader uploader = FileUploader.getInstance();
		int count = 0;
		for (String url : picUrlList) {
			URL u = new URL(url);
			InputStream in = u.openStream();
			byte[] data = NInputStream.readBytes(in);
			File f = File.createTempFile("pic", ".jpg");
			NFile.write(f, data);
			uploader.upload(물건.get물건FilePath(), "pic_" + count + ".jpg", f);
			count++;
		}

	}
}
