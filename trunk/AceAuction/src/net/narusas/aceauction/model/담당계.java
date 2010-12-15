package net.narusas.aceauction.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.narusas.aceauction.data.DB;

public class ���� {

	static Logger logger = Logger.getLogger("log");

	private ���� court;

	private long no;

	private List<���> sagun;

	private String time;

	private String �����̸�;
	private int �����ڵ�;

	private Date �Ű�����;

	private String �Ű����;

	private Date ����_������;

	private Date ����_���۳���;

	private String ���;

	public ����(���� court, int charge_code, String name, Date fixed_date, Date start_date, Date end_date,
			String location, String method, String time, long no) {
		this.court = court;
		�����ڵ� = charge_code;
		�����̸� = name;
		�Ű����� = fixed_date;
		����_���۳��� = start_date;
		����_������ = end_date;
		��� = location;
		�Ű���� = method;
		this.time = time;
		this.no = no;
	}

	public ����(���� court, String �Ű�����, String �����Ⱓ, String �����ڵ�, String name) {
		this.court = court;
		this.�Ű����� = DB.toDate(�Ű�����);
		if (�����Ⱓ != null) {
			����_���۳��� = DB.toDate(DB.token(�����Ⱓ, "~", 0));
			����_������ = DB.toDate(DB.token(�����Ⱓ, "~", 1));

		}
		this.�����ڵ� = DB.toInt(�����ڵ�);
		this.�����̸� = name;
		�Ű���� = ����_���۳��� == null ? "��������" : "�Ⱓ����";
	}

	public long getNo() {
		return no;
	}

	public List<���> getSagun() {
		return sagun;
	}

	public String get�����̸�() {
		return �����̸�;
	}

	public int get�����ڵ�() {
		return �����ڵ�;
	}

	public Date get�Ű�����() {
		return �Ű�����;
	}

	public String get�Ű����() {
		return �Ű����;
	}

	public String get�Ű��ð�() {
		return time;
	}

	public ���� get����() {
		return court;
	}

	public Date get����_������() {
		return ����_������;
	}

	public Date get����_���۳���() {
		return ����_���۳���;
	}

	public String get���() {
		return ���;
	}

	public void insert() throws Exception {
		logger.info("���ο� �㰭�踦 �Է��մϴ�. ����:" + this);
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = DB.prepareStatement("INSERT INTO ac_charge " + "(" + //
					"charge_code, " + //
					"court_code, " + //
					"name, " + //
					"fixed_date, " + //
					"start_date, " + //
					"end_date, " + //
					"location, " + //
					"method, " + //
					"time" + //
					") " + "VALUES (?,?,?,?,?,?,?,?,?);");
			stmt.setInt(1, this.get�����ڵ�());
			stmt.setInt(2, DB.toInt(this.get����().getCode()));
			stmt.setString(3, get�����̸�());
			stmt.setDate(4, get�Ű�����());
			stmt.setDate(5, get����_���۳���());
			stmt.setDate(6, get����_������());
			stmt.setString(7, get���());
			stmt.setString(8, get�Ű����());
			stmt.setString(9, get�Ű��ð�());
			stmt.execute();
			rs = stmt.getGeneratedKeys();
			rs.next();
			int no = rs.getInt(1);
			rs.close();
			this.setNo(no);
			Registry.add����(this);
		} finally {
			DB.cleanup(rs, stmt);
		}
	}

	public boolean isInScoop(java.util.Date startD, java.util.Date endD) {
		Date d = get�Ű�����();
		return d.compareTo(startD) >= 0 && d.compareTo(endD) <= 0;
	}

	public void setNo(long l) {
		this.no = l;
	}

	public void setSagun(List<���> sagun) {
		this.sagun = sagun;
	}

	public void set�Ű��ð�(String time) {
		this.time = time;
	}

	public void set���(List<���> sagun) {
		this.sagun = sagun;
	}

	public void set���(String ���) {
		this.��� = ���;
	}

	@Override
	public String toString() {
		if (����_���۳��� == null) {
			return �Ű����� + ":" + �����̸�;
		}
		return �Ű����� + ":" + �����̸� + "(" + ����_���۳��� + "~" + ����_������ + ")";

	}

	public void update() {
		try {
			DB.dbConnect();
			���� charge = ����.findByMemoryObject(this);
			if (updateNo() == false) {
				insert();
			} else {
				update����();
			}
		} catch (RuntimeException e) {
			logger.log(Level.INFO, "Reconnection failed", e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.log(Level.INFO, "Reconnection failed", e);
			e.printStackTrace();
		}
	}

	private boolean updateNo() throws Exception {
		���� ch = findByMemoryObject(this);
		if (ch != null) {
			this.setNo(ch.getNo());
			this.��� = ch.���;
			this.time = ch.time;
			Registry.add����(this);
			return true;
		}
		return false;
	}

	private void update����() throws Exception {
		logger.info("������ �ԷµǾ� �ִ� ���踦 �����մϴ�. ����:" + this);
		PreparedStatement stmt = null;
		try {
			stmt = DB.prepareStatement("UPDATE ac_charge " + //
					"SET  " //
					+ "name=?, " // 
					+ "start_date=?, "//
					+ "end_date=?, " //
					+ "location=?, " //
					+ "method=? "//
					+ "WHERE "//
					+ "charge_code=? AND  " //
					+ "court_code=? AND " //
					+ "fixed_date=?;");

			stmt.setString(1, get�����̸�());
			stmt.setDate(2, get����_���۳���());
			stmt.setDate(3, get����_������());
			stmt.setString(4, get���());
			stmt.setString(5, get�Ű����());
			stmt.setInt(6, get�����ڵ�());
			stmt.setString(7, get����().getCode());
			stmt.setDate(8, get�Ű�����());
			stmt.execute();
		} finally {
			DB.cleanup(stmt);
		}
	}

	public static ���� find(int charge_code, int court_code, Date date) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DB.prepareStatement("SELECT * " + //
					"FROM ac_charge " + //
					"WHERE charge_code=? AND  court_code=? AND fixed_date=?;");
			stmt.setInt(1, charge_code);
			stmt.setInt(2, court_code);
			stmt.setDate(3, date);
			stmt.execute();
			rs = stmt.getResultSet();
			return checkResultSet(rs);
		} finally {
			DB.cleanup(rs, stmt);
		}
	}

	public static ���� findByMemoryObject(���� charge) throws Exception {
		PreparedStatement stmt = DB
				.prepareStatement("SELECT * FROM ac_charge WHERE charge_code=? AND  court_code=? AND fixed_date=?;");
		stmt.setInt(1, charge.get�����ڵ�());
		stmt.setInt(2, DB.toInt(charge.get����().getCode()));
		stmt.setDate(3, charge.get�Ű�����());
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		if (rs.next()) {
			return ����.load(rs);
		}
		return null;
	}

	public static ���� findByNo(long no) throws Exception {
		���� charge = Registry.get����(no);
		if (charge != null) {
			return charge;
		}

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DB.prepareStatement("SELECT * " + //
					"FROM ac_charge " + //
					"WHERE no=?;");
			stmt.setLong(1, no);
			stmt.execute();
			rs = stmt.getResultSet();
			return checkResultSet(rs);
		} finally {
			DB.cleanup(rs, stmt);
		}
	}

	private static ���� checkResultSet(ResultSet rs) throws SQLException {
		if (rs.next()) {
			���� loaded = ����.load(rs);
			Registry.add����(loaded);
			return loaded;
		}
		return null;
	}

	private static ���� load(ResultSet rs) throws SQLException {
		int charge_code = rs.getInt("charge_code");
		int court_code = rs.getInt("court_code");
		String name = rs.getString("name");
		Date fixed_date = rs.getDate("fixed_date");
		Date start_date = rs.getDate("start_date");
		Date end_date = rs.getDate("end_date");
		String location = rs.getString("location");
		String method = rs.getString("method");
		String time = rs.getString("time");
		long no = rs.getLong("no");

		���� charge = new ����(����.findByCode(court_code), charge_code, name, fixed_date, start_date, end_date, location,
				method, time, no);

		return charge;
	}
}
