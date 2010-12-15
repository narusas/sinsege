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
import net.narusas.aceauction.data.�Ű�����UpdaterDB;
import net.narusas.aceauction.data.��������Converter;
import net.narusas.aceauction.fetchers.������Ű����Ǹ���Fetcher;
import net.narusas.aceauction.fetchers.���ǵ����_����Updater;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.�ε���ǥ�ø��;
import net.narusas.aceauction.model.�ε���ǥ�ø��Item;
import net.narusas.aceauction.model.���;
import net.narusas.util.lang.NFile;
import net.narusas.util.lang.NInputStream;

public class ����Builder {

	private int id;

	private final BuildProgressListener listener;

	private long sagunId;

	private ��� ���;

	Logger logger = Logger.getLogger("log");

	public ����Builder(BuildProgressListener listener) {
		this.listener = listener;
	}

	public void update����DBs(��� ���, boolean hasSagunPicture, �ε���ǥ�ø�� item, String[] gamjungInfo) throws Exception {
		try {
			DB.dbConnect();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		this.��� = ���;
		this.sagunId = ���.getDbid();
		logger.info("������ DB�� �Է��մϴ�. " + ���.court.getName() + " " + ���.charge.get�����̸�() + " " + ���.get��ǹ�ȣ() + " ");
		DB.reConnect();
		����[] mul = ���.get����s();
		listener.update����Size(mul.length);
		for (���� ���� : mul) {
			try {
				update����DB(����, sagunId, hasSagunPicture, item, gamjungInfo);
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
			FileUploader.getInstance().waitDone();
		}
	}

	private int findMatchArea(int parentCode, String �ּ�, Statement stmt, int depth) throws SQLException {

		if (depth == 0) {
			return parentCode;
		}
		ResultSet rs = stmt.executeQuery("SELECT * FROM ac_area WHERE parent_code=" + parentCode + ";");
		while (rs.next()) {
			String name = rs.getString(2);
			if (�ּ�.contains(name)) {
				return findMatchArea(rs.getInt(1), �ּ�, stmt, depth - 1);
			}
		}
		return parentCode;
	}

	private int getAreaCode(String �ּ�, int depth) throws Exception {
		Statement stmt = DB.createStatement();
		return findMatchArea(1, �ּ�, stmt, depth);
	}

	private int getExisting����Id(���� ����, long sagunId) throws Exception {
		PreparedStatement stmt = DB.prepareStatement("SELECT * FROM ac_goods WHERE event_no=? AND no=?;");
		stmt.setLong(1, sagunId);
		stmt.setInt(2, ����.get���ǹ�ȣ());
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		if (rs.next()) {
			return rs.getInt("id");
		}
		return -1;
	}

	// ����� ���� ����
	private int getTypeCode(String ��������) throws Exception {
		PreparedStatement stmt = DB.prepareStatement("SELECT * FROM ac_goods_type WHERE name=?;");
		stmt.setString(1, ��������);
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		if (rs != null && rs.next()) {
			return rs.getInt(1);
		} else {
			PreparedStatement stmt2 = DB.prepareStatement("INSERT INTO ac_goods_type (name) VALUES (?)");
			stmt2.setString(1, ��������);
			stmt2.execute();
			ResultSet rs2 = stmt2.getGeneratedKeys();
			rs2.next();
			return rs2.getInt(1);
		}
	}

	// ���ǵ���� ��������
	private int getUsageCode(String ��������) throws Exception {
		// System.out.println("��������:" + ��������);

		PreparedStatement stmt = DB.prepareStatement("SELECT * FROM ac_goods_usage WHERE name=?;");

		stmt.setString(1, ��������Converter.convert(��������.trim()));
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		if (rs != null && rs.next()) {
			return rs.getInt(1);
		} else {
			PreparedStatement stmt2 = DB
					.prepareStatement("SELECT (code) FROM ac_goods_usage WHERE name=? AND parent_code=4;");
			stmt2.setString(1, "��Ÿ");
			stmt2.execute();
			ResultSet rs2 = stmt.getResultSet();
			if (rs2.next()) {
				return rs2.getInt(1);
			}
			return 0;
		}
	}

	private int insertBasic(���� ����, long sagunId) throws Exception {
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

		stmt.setInt(1, ����.get���ǹ�ȣ());
		// stmt.setInt(2, 0);//getTypeCode(����.get��������()));
		// stmt.setString(3,
		// "");//�Ű����Converter.convert(����.getDetail().get�Ű����()));
		stmt.setLong(2, DB.toPrice(����.get����()));
		stmt.setLong(3, DB.toPrice(����.get������()));
		stmt.setDate(4, ����.���.get��������());
		stmt.setDate(5, ����.���.get���ð�������());
		stmt.setDate(6, DB.toDate(����.get���䱸������()));
		stmt.setString(7, ����.get���());
		stmt.setInt(8, DB.toInt(����.get���().court.getCode()));
		stmt.setLong(9, ����.get���().charge.getNo());
		stmt.setLong(10, sagunId);
		stmt.setInt(11, getAreaCode(����.get�ּ�(), 1));
		stmt.setInt(12, getAreaCode(����.get�ּ�(), 2));
		stmt.setInt(13, getAreaCode(����.get�ּ�(), 3));
		stmt.setString(14, ����.get�ּ�());
		stmt.execute();
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		int no = rs.getInt(1);
		rs.close();
		return no;
	}

	private void insert����(���� ����, long sagunId, �ε���ǥ�ø�� item, String[] gamjungInfo) throws SQLException {
		// conn.setAutoCommit(false);
		try {
			id = insertBasic(����, sagunId);
			update������(id, ����, gamjungInfo);
			���ϳ���Builder db = new ���ϳ���Builder(id, ����);
			db.update();

			logger.log(Level.INFO, "�ε��� ǥ�ø��db�� �����մϴ�. ");
			List<�ε���ǥ�ø��Item> target = item.getItem(����);
			if (target != null && target.size() > 0) {
				�ε���ǥ�ø��Builder budongsaDb = new �ε���ǥ�ø��Builder(id, ����, target);
				budongsaDb.update();
			}

			// �ǹ���ȲDB �ǹ���ȲDB = new �ǹ���ȲDB(id, ����, ����.getDetail().get�ǹ���Ȳ());
			// �ǹ���ȲDB.update();
			// ��������ȲDB ��������ȲDB = new ��������ȲDB(id, ����, ����.getDetail().get��������Ȳ());
			// ��������ȲDB.update();
			// new ������ȲDB(id, ����.getDetail().get������Ȳ()).update();
			if (����.has����) {
				������Ű����Ǹ���Fetcher f = new ������Ű����Ǹ���Fetcher();
				try {
					���� = f.update(����);
					new �Ű����Ǹ���Builder(id, ����.get����()).update();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			new ���ÿܰǹ���ȲBuilder(id, ����.get���ÿܰǹ�s()).update();
			new �����������ȲBuilder(id, ����.get������Ȳ()).update();
			�Ű�����UpdaterDB appoint = new �Ű�����UpdaterDB(null);
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
		String[] src = ����.getSpecialCaseSource(id);
		����.updateSpecialCase(src, id);
	}

	private void updateBasic(int id, ���� ����) throws Exception {
		logger.info("����(id=" + id + ")�� �⺻������  charge_id�� done�� ���� " + ����.���.charge.getNo()
				+ ",0 ���� �����մϴ�. sell_price=" + DB.toPrice(����.get����()) + ", lowest_price=" + DB.toPrice(����.get������())
				+ ", accept_date=" + ����.���.get��������() + ", decision_date=" + ����.���.get���ð�������() + ", devidend_date="
				+ DB.toDate(����.get���䱸������())

		);
		PreparedStatement stmt = DB.prepareStatement("UPDATE ac_goods SET " + "done=0, "// 
				+ "charge_id=?, " // 1
				+ "sell_price=?, "// 2
				+ "lowest_price=?, "// 3
				+ "accept_date=?,"// 4
				+ "decision_date=?,"// 5
				+ "devidend_date=? "// 6

				+ "WHERE id=?;");// 4
		stmt.setLong(1, ����.���.charge.getNo());
		stmt.setLong(2, DB.toPrice(����.get����()));
		stmt.setLong(3, DB.toPrice(����.get������()));

		stmt.setDate(4, ����.���.get��������());
		stmt.setDate(5, ����.���.get���ð�������());
		stmt.setDate(6, DB.toDate(����.get���䱸������()));

		stmt.setInt(7, id);

		stmt.executeUpdate();
	}

	private void update������(int id, ���� ����, String[] gamjungInfo) throws Exception {

		// System.out.println("####" + gamjungInfo[0] + " " + gamjungInfo[1]);
		String src = null;
		Date date = null;
		if ("".equals(gamjungInfo[0]) || gamjungInfo[0] == null) {
			src = ����.getDetail().get�����򰡱��().trim();
		} else {
			src = gamjungInfo[0].trim();
		}
		if (src.endsWith("����")) {
			src = src + "�繫��";
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
				date = DB.toDate(����.getDetail().get��������());
			} else {
				date = DB.toDate(gamjungInfo[1]);
			}

			stmt.setString(1, src);
			stmt.setDate(2, date);
			stmt.setInt(3, id);
			stmt.execute();
		}

	}

	private void update���ε(���� ����, String url) throws Exception {
		if (url == null || "".equals(url) || url.startsWith("http") == false) {
			return;
		}
		���εBuilder db = new ���εBuilder(id, url, ����.���.������.toArray(new String[] {}), ����.get���εFilePath());
		try {
			db.execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void update���εDB(���� ����) throws Exception {
		logger.info("���ε PDF������ ���ε� �մϴ�. ");
		update���ε(����, ����.get�ǹ����ε());
		update���ε(����, ����.get�������ε());
	}

	private void update����(���� ����, long sagunId, int id, �ε���ǥ�ø�� item, String[] gamjungInfo) throws SQLException {
		// setAutoCommit(false);
		try {
			// id = getExisting����Id(����, sagunId);
			updateBasic(id, ����);
			update������(id, ����, gamjungInfo);
			// update������(id, ����);
			logger.info("���ϳ����� �����մϴ�. ");
			���ϳ���Builder db = new ���ϳ���Builder(id, ����);
			db.update();

			logger.info("�ε���ǥ�ø���� �����մϴ�. ");
			try {
				List<�ε���ǥ�ø��Item> target = item.getItem(����);
				if (target != null && target.size() > 0) {
					�ε���ǥ�ø��Builder budongsaDb = new �ε���ǥ�ø��Builder(id, ����, target);
					budongsaDb.update();
				}

			} catch (Throwable e) {
				logger.log(Level.WARNING, "Exception in �ε���ǥ�ø��", e);
				e.printStackTrace();
			}

			if (����.has����) {
				logger.info("�Ű� ���� ���� ������ �����մϴ�. ");
				������Ű����Ǹ���Fetcher f = new ������Ű����Ǹ���Fetcher();
				try {
					���� = f.update(����);
					new �Ű����Ǹ���Builder(id, ����.get����()).update();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				logger.info("�Ű����� ���� ������ ������ �ʴ� �����Դϴ�. ");
			}

			new ���ÿܰǹ���ȲBuilder(id, ����.get���ÿܰǹ�s()).update();
			new �����������ȲBuilder(id, ����.get������Ȳ()).update();
			�Ű�����UpdaterDB appoint = new �Ű�����UpdaterDB(null);
			appoint.update(id);
			// commit();

		} catch (Exception ex) {
			logger.log(Level.WARNING, "����DB�� �����ϴ� ���߿� ������ �߻��߽��ϴ�. �۾��� �ѹ��մϴ�.", ex);
			ex.printStackTrace();
			// rollback();
			throw new SQLException(ex);
		} finally {
			// setAutoCommit(true);
		}

	}

	private void update����DB(���� ����, long sagunId, boolean hasSagunPicture, �ε���ǥ�ø�� item, String[] gamjungInfo)
			throws Exception {
		logger.info("������ DB�� �Է��մϴ�. " + ����.���.get��ǹ�ȣ() + ":" + ����.get���ǹ�ȣ());

		���ǵ����_����Updater.updateDetail(����, gamjungInfo);
		id = getExisting����Id(����, sagunId);

		if (id != -1) {
			logger.info("DB�� �����ϴ� �����Դϴ�. DB�� ������ �����մϴ�. ");
			update����(����, sagunId, id, item, gamjungInfo);
		} else {
			logger.info("���ο� �����Դϴ�. DB�� ������ �߰��մϴ�. ");
			insert����(����, sagunId, item, gamjungInfo);
		}

		upload�Ű����Ǹ���(����);
		if (hasSagunPicture == false) {
			upload����(����);
		}

		update���εDB(����);
		listener.progress(BuildProgressListener.LEVEL_����);
	}

	private void upload�Ű����Ǹ���(���� ����) throws IOException {
		FileUploader uploader = FileUploader.getInstance();
		if (����.get�Ű����Ǹ���html() == null) {
			return;
		}
		logger.info("�Ű����Ǹ��� HTML������ ���ε� �մϴ�. ");
		File f = File.createTempFile("Mulgun", ".html");
		NFile.write(f, ����.get�Ű����Ǹ���html());
		uploader.upload(����.get����FilePath(), "Mulgun.html", f);
	}

	private void upload����(���� ����) throws IOException {
		logger.info("������ ���ε� �մϴ�. ");
		List<String> picUrlList = ����.get����();

		FileUploader uploader = FileUploader.getInstance();
		int count = 0;
		for (String url : picUrlList) {
			URL u = new URL(url);
			InputStream in = u.openStream();
			byte[] data = NInputStream.readBytes(in);
			File f = File.createTempFile("pic", ".jpg");
			NFile.write(f, data);
			uploader.upload(����.get����FilePath(), "pic_" + count + ".jpg", f);
			count++;
		}

	}
}
