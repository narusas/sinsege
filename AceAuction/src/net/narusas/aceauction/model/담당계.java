package net.narusas.aceauction.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.narusas.aceauction.data.DB;

public class 담당계 {

	static Logger logger = Logger.getLogger("log");

	private 법원 court;

	private long no;

	private List<사건> sagun;

	private String time;

	private String 담당계이름;
	private int 담당계코드;

	private Date 매각기일;

	private String 매각방법;

	private Date 입찰_끝날자;

	private Date 입찰_시작날자;

	private String 장소;

	public 담당계(법원 court, int charge_code, String name, Date fixed_date, Date start_date, Date end_date,
			String location, String method, String time, long no) {
		this.court = court;
		담당계코드 = charge_code;
		담당계이름 = name;
		매각기일 = fixed_date;
		입찰_시작날자 = start_date;
		입찰_끝날자 = end_date;
		장소 = location;
		매각방법 = method;
		this.time = time;
		this.no = no;
	}

	public 담당계(법원 court, String 매각기일, String 입찰기간, String 담당계코드, String name) {
		this.court = court;
		this.매각기일 = DB.toDate(매각기일);
		if (입찰기간 != null) {
			입찰_시작날자 = DB.toDate(DB.token(입찰기간, "~", 0));
			입찰_끝날자 = DB.toDate(DB.token(입찰기간, "~", 1));

		}
		this.담당계코드 = DB.toInt(담당계코드);
		this.담당계이름 = name;
		매각방법 = 입찰_시작날자 == null ? "기일입찰" : "기간입찰";
	}

	public long getNo() {
		return no;
	}

	public List<사건> getSagun() {
		return sagun;
	}

	public String get담당계이름() {
		return 담당계이름;
	}

	public int get담당계코드() {
		return 담당계코드;
	}

	public Date get매각기일() {
		return 매각기일;
	}

	public String get매각방법() {
		return 매각방법;
	}

	public String get매각시간() {
		return time;
	}

	public 법원 get법원() {
		return court;
	}

	public Date get입찰_끝날자() {
		return 입찰_끝날자;
	}

	public Date get입찰_시작날자() {
		return 입찰_시작날자;
	}

	public String get장소() {
		return 장소;
	}

	public void insert() throws Exception {
		logger.info("새로운 담강계를 입력합니다. 담당계:" + this);
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
			stmt.setInt(1, this.get담당계코드());
			stmt.setInt(2, DB.toInt(this.get법원().getCode()));
			stmt.setString(3, get담당계이름());
			stmt.setDate(4, get매각기일());
			stmt.setDate(5, get입찰_시작날자());
			stmt.setDate(6, get입찰_끝날자());
			stmt.setString(7, get장소());
			stmt.setString(8, get매각방법());
			stmt.setString(9, get매각시간());
			stmt.execute();
			rs = stmt.getGeneratedKeys();
			rs.next();
			int no = rs.getInt(1);
			rs.close();
			this.setNo(no);
			Registry.add담당계(this);
		} finally {
			DB.cleanup(rs, stmt);
		}
	}

	public boolean isInScoop(java.util.Date startD, java.util.Date endD) {
		Date d = get매각기일();
		return d.compareTo(startD) >= 0 && d.compareTo(endD) <= 0;
	}

	public void setNo(long l) {
		this.no = l;
	}

	public void setSagun(List<사건> sagun) {
		this.sagun = sagun;
	}

	public void set매각시간(String time) {
		this.time = time;
	}

	public void set사건(List<사건> sagun) {
		this.sagun = sagun;
	}

	public void set장소(String 장소) {
		this.장소 = 장소;
	}

	@Override
	public String toString() {
		if (입찰_시작날자 == null) {
			return 매각기일 + ":" + 담당계이름;
		}
		return 매각기일 + ":" + 담당계이름 + "(" + 입찰_시작날자 + "~" + 입찰_끝날자 + ")";

	}

	public void update() {
		try {
			DB.dbConnect();
			담당계 charge = 담당계.findByMemoryObject(this);
			if (updateNo() == false) {
				insert();
			} else {
				update담당계();
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
		담당계 ch = findByMemoryObject(this);
		if (ch != null) {
			this.setNo(ch.getNo());
			this.장소 = ch.장소;
			this.time = ch.time;
			Registry.add담당계(this);
			return true;
		}
		return false;
	}

	private void update담당계() throws Exception {
		logger.info("기존에 입력되어 있는 담당계를 갱신합니다. 담당계:" + this);
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

			stmt.setString(1, get담당계이름());
			stmt.setDate(2, get입찰_시작날자());
			stmt.setDate(3, get입찰_끝날자());
			stmt.setString(4, get장소());
			stmt.setString(5, get매각방법());
			stmt.setInt(6, get담당계코드());
			stmt.setString(7, get법원().getCode());
			stmt.setDate(8, get매각기일());
			stmt.execute();
		} finally {
			DB.cleanup(stmt);
		}
	}

	public static 담당계 find(int charge_code, int court_code, Date date) throws Exception {
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

	public static 담당계 findByMemoryObject(담당계 charge) throws Exception {
		PreparedStatement stmt = DB
				.prepareStatement("SELECT * FROM ac_charge WHERE charge_code=? AND  court_code=? AND fixed_date=?;");
		stmt.setInt(1, charge.get담당계코드());
		stmt.setInt(2, DB.toInt(charge.get법원().getCode()));
		stmt.setDate(3, charge.get매각기일());
		stmt.execute();
		ResultSet rs = stmt.getResultSet();
		if (rs.next()) {
			return 담당계.load(rs);
		}
		return null;
	}

	public static 담당계 findByNo(long no) throws Exception {
		담당계 charge = Registry.get담당계(no);
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

	private static 담당계 checkResultSet(ResultSet rs) throws SQLException {
		if (rs.next()) {
			담당계 loaded = 담당계.load(rs);
			Registry.add담당계(loaded);
			return loaded;
		}
		return null;
	}

	private static 담당계 load(ResultSet rs) throws SQLException {
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

		담당계 charge = new 담당계(법원.findByCode(court_code), charge_code, name, fixed_date, start_date, end_date, location,
				method, time, no);

		return charge;
	}
}
