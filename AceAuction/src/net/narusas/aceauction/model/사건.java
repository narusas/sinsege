package net.narusas.aceauction.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.narusas.aceauction.data.DB;

public class ��� {
	private static final String Ÿ�� = "0130";

	static Logger logger = Logger.getLogger("log");

	public final ���� charge;

	public final ���� court;

	public ArrayList<����> items = new ArrayList<����>();

	public long ��ǹ�ȣ;

	public LinkedList<String> ������ = new LinkedList<String>();

	public LinkedList<String> ä���� = new LinkedList<String>();

	public LinkedList<String> ä���� = new LinkedList<String>();

	private long dbid;

	private int eventNo;

	private int eventYear;

	private String judgement_land;

	private String judgement_location;

	private String judgement_road;

	private String judgement_temp;

	private String judgement_use;

	private �����򰡼� �����򰡼�;

	private Date ���ð�������;

	private Table ���ϳ���;

	private Table ���䱸���⳻��;

	private String ����;

	private String ��Ǹ�;

	private String ����װ���������;
	private Date ��������;

	private List<���ÿܰǹ�> ���ÿܰǹ�;
	private String �������;

	private Date ��������;

	private long û���ݾ�;

	private ��Ȳ���缭 ��Ȳ���缭;

	String pic1;

	String pic2;

	public ���(���� charge, long id, int event_year, int event_no, String name, Date accept_date, long claim_price,
			String merged_to, String pic1, String pic2, String judgement_location, String judgement_use,
			String judgement_land, String judgement_road, String judgement_temp) {
		this.charge = charge;
		eventYear = event_year;
		eventNo = event_no;
		��Ǹ� = name;
		�������� = accept_date;
		û���ݾ� = claim_price;
		���� = merged_to;
		this.pic1 = pic1;
		this.pic2 = pic2;
		this.judgement_location = judgement_location;
		this.judgement_use = judgement_use;
		this.judgement_land = judgement_land;
		this.judgement_road = judgement_road;
		this.judgement_temp = judgement_temp;
		this.court = charge.get����();
		this.dbid = id;
	}

	public ���(���� court, ���� charge) {
		this.court = court;
		this.charge = charge;

	}

	public void add(���� item) {
		item.setParent(this);
		items.add(item);
		update���䱸(item);
		update���ϳ���(item);
	}

	public void add������(String ������) {
		this.������.add(������);

	}

	public void addä����(String ä����) {
		this.ä����.add(ä����);
	}

	public void addä����(String ä����) {
		this.ä����.add(ä����);
	}

	public void clear������() {
		������.clear();
	}

	public void clearä����() {
		ä����.clear();
	}

	public void clearä����() {
		ä����.clear();
	}

	public long getDbid() {
		return dbid;
	}

	public int getEventNo() {
		return eventNo;
	}

	public int getEventYear() {
		return eventYear;

	}

	public String getJudgement_land() {
		return judgement_land;
	}

	public String getJudgement_location() {
		return judgement_location;
	}

	public String getJudgement_road() {
		return judgement_road;
	}

	public String getJudgement_temp() {
		return judgement_temp;
	}

	public String getJudgement_use() {
		return judgement_use;
	}

	public String getPic1() {
		return pic1;
	}

	public String getPic2() {
		return pic2;
	}

	public �����򰡼� get�����򰡼�() {
		return �����򰡼�;
	}

	public Date get���ð�������() {
		return ���ð�������;
	}

	public ����[] get����s() {
		return items.toArray(new ����[items.size()]);
	}

	public Table get���䱸���⳻��() {
		return ���䱸���⳻��;
	}

	public String get����() {
		if (���� == null || "0".equals(����.trim()) || "O".equals(����.trim())) {
			return "";
		}

		return ����;
	}

	public String get���Path() {
		return getEventYear() + "/" + court.getCode() + "/" + removeDots(charge.get�Ű�����().toString()) + "/"
				+ charge.get�����ڵ�() + "/" + get��ǹ�ȣ() + "/";
	}

	public String get��Ǹ�() {
		return ��Ǹ�;
	}

	public long get��ǹ�ȣ() {
		return ��ǹ�ȣ;
	}

	public String get����װ���������() {
		return ����װ���������;
	}

	public String get������() {
		return ������.toString();
	}

	public Date get��������() {
		return ��������;
	}

	public List<���ÿܰǹ�> get���ÿܰǹ�() {
		return ���ÿܰǹ�;
	}

	public String get�������() {
		return �������;
	}

	public Date get��������() {
		return ��������;
	}

	public String getä����() {
		return ä����.toString();
	}

	public String getä����() {
		return ä����.toString();
	}

	public long getû���ݾ�() {
		return û���ݾ�;
	}

	public ��Ȳ���缭 get��Ȳ���缭() {
		return ��Ȳ���缭;
	}

	public void insert() throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		/*
		 * this.pic1 = pic1; this.pic2 = pic2; this.judgement_location =
		 * judgement_location; this.judgement_use = judgement_use;
		 * this.judgement_land = judgement_land; this.judgement_road =
		 * judgement_road; this.judgement_temp = judgement_temp;
		 */
		try {
			stmt = DB.prepareStatement("INSERT INTO ac_event (" + //
					"no, " + // 1
					"event_year, " + // 2
					"event_no, " + // 3
					"name, " + // 4
					"accept_date, " + // 5
					"claim_price, " + // 6
					"merged_to, " + // 7
					"court_code, " + // 8
					"charge_id," + // 9
					"pic_1," + // 10
					"pic_2," + // 11
					"judgement_location," + // 12
					"judgement_use," + // 13
					"judgement_land," + // 14
					"judgement_road," + // 15
					"judgement_temp" + // 16
					") " + "VALUES (?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?);");
			stmt.setLong(1, get��ǹ�ȣ());
			stmt.setInt(2, getEventYear());
			stmt.setInt(3, getEventNo());

			stmt.setString(4, get��Ǹ�());
			stmt.setDate(5, get��������());
			stmt.setLong(6, getû���ݾ�());
			stmt.setString(7, get����());

			stmt.setInt(8, DB.toInt(court.getCode()));
			stmt.setLong(9, charge.getNo());
			stmt.setString(10, pic1);
			stmt.setString(11, pic2);
			stmt.setString(12, judgement_location);
			stmt.setString(13, judgement_use);
			stmt.setString(14, judgement_land);
			stmt.setString(15, judgement_road);
			stmt.setString(16, judgement_temp);

			stmt.execute();
			rs = stmt.getGeneratedKeys();
			rs.next();
			setDBId(rs.getLong(1));
			Registry.add���(this);
		} finally {
			DB.cleanup(rs, stmt);
		}
	}

	public boolean isIn(Long[] eventNos) {
		for (Long no : eventNos) {
			if (no.equals(��ǹ�ȣ)) {
				return true;
			}
		}
		return false;
	}

	public void merge(��� s2) {
		logger.info(this.��ǹ�ȣ + "(" + items.size() + ")�� " + s2.��ǹ�ȣ + "(" + s2.items.size()
				+ ")�� �������� �и��Ǿ� �ֽ��ϴ�. ��Ĩ�ϴ�. ");

		try {
			ArrayList<����> allMulgun = new ArrayList<����>();
			allMulgun.addAll(this.items);
			allMulgun.addAll(s2.items);
			ArrayList<����> res = new ArrayList<����>();

			if (allMulgun.size() == 0) {
				return;
			}
			for (���� item : allMulgun) {
				item.set���(this);
			}
			res.add(allMulgun.get(0));
			for (int i = 1; i < allMulgun.size(); i++) {
				���� m1 = res.get(res.size() - 1);
				���� m2 = allMulgun.get(i);
				if (m1.get���ǹ�ȣ() == m2.get���ǹ�ȣ()) {
					m1.merge(m2);
				} else {
					res.add(m2);
				}
			}
			this.items = res;
			// logger.info("�������� ��ġ�� ���� ��������� ���ϳ����� ��� �ɴϴ�.");
			//
			// ��������ϳ���Fetcher f = new ��������ϳ���Fetcher();
			// f.update(this);
			// logger.info("���� ���� �������� ����� ������ �����մϴ�. ");

			for (int i = 0; i < items.size(); i++) {
				���� item = items.get(i);
				update���ϳ���(item);
				update���䱸(item);
				s2.update���ϳ���(item);
				s2.update���䱸(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.FINEST, e.getMessage(), e);
		}

	}

	public void setDbid(long dbid) {
		this.dbid = dbid;
	}

	public void setDBId(long no) {
		this.dbid = no;
	}

	public void setJudgement_land(String judgement_land) {
		this.judgement_land = judgement_land;
	}

	public void setJudgement_location(String judgement_location) {
		this.judgement_location = judgement_location;
	}

	public void setJudgement_road(String judgement_road) {
		this.judgement_road = judgement_road;
	}

	public void setJudgement_temp(String judgement_temp) {
		this.judgement_temp = judgement_temp;
	}

	public void setJudgement_use(String judgement_use) {
		this.judgement_use = judgement_use;
	}

	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}

	// public long getDBId() {
	// return dbid;
	// }

	public void setPic2(String pic2) {
		this.pic2 = pic2;
	}

	public void set�����򰡼�(�����򰡼� �����򰡼�) {
		this.�����򰡼� = �����򰡼�;
	}

	public void set���ð�������(String ���ð�������) {
		this.���ð������� = DB.toDate(���ð�������);
	}

	public void set���ϳ���(Table ���ϳ���) {
		this.���ϳ��� = ���ϳ���;
	}

	public void set���䱸���⳻��(Table ���䱸���⳻��) {
		// System.out.println(���䱸���⳻��);
		this.���䱸���⳻�� = ���䱸���⳻��;

	}

	public void set����(String str) {
		���� = str;
	}

	public void set��Ǹ�(String ��Ǹ�) {
		this.��Ǹ� = ��Ǹ�;
	}

	public void set��ǹ�ȣ(String ��ǹ�ȣ) {
		this.��ǹ�ȣ = DB.toLong(��ǹ�ȣ);
		this.eventNo = Integer.parseInt(parseEventNo(��ǹ�ȣ));
		this.eventYear = Integer.parseInt(��ǹ�ȣ.substring(0, ��ǹ�ȣ.indexOf(Ÿ��)));
	}

	public void set����װ���������(String ����װ���������) {
		this.����װ��������� = ����װ���������;
	}

	public void set��������(String ��������) {
		this.�������� = DB.toDate(��������);
	}

	public void set���ÿܰǹ�(List<���ÿܰǹ�> ���ÿܰǹ�) {
		this.���ÿܰǹ� = ���ÿܰǹ�;

		for (���� m : this.items) {
			m.clear���ÿܰǹ�();
			for (���ÿܰǹ� e : ���ÿܰǹ�) {
				if (e.get���ǹ�ȣ() == m.get���ǹ�ȣ()) {
					m.add���ÿܰǹ�(e);
				}
			}

		}

	}

	public void set�������(String �������) {
		this.������� = �������;
	}

	public void set��������(String ��������) {
		this.�������� = DB.toDate(��������);
	}

	public void setû���ݾ�(String û���ݾ�) {
		this.û���ݾ� = DB.toPrice(û���ݾ�);
	}

	public void set��Ȳ���缭(��Ȳ���缭 ��Ȳ���缭) {
		this.��Ȳ���缭 = ��Ȳ���缭;

	}

	@Override
	public String toString() {
		return getEventYear() + "Ÿ��" + getEventNo();
	}

	public void update() throws Exception {
		PreparedStatement stmt = null;

		try {
			stmt = DB.prepareStatement("UPDATE ac_event " + "SET " + "event_year=?, " // 1
					+ "event_no=?, " // 2
					+ "name=?, " // 3
					+ "accept_date=?, "// 4
					+ "claim_price=?, " // 5
					+ "merged_to=?, " // 6
					+ "charge_id=?," + // 7
					"pic_1=?," + // 8
					"pic_2=?," + // 9
					"judgement_location=?," + // 10
					"judgement_use=?," + // 11
					"judgement_land=?," + // 12
					"judgement_road=?," + // 13
					"judgement_temp=? " // 14
					+ "WHERE " + "id=?;"); // 15

			stmt.setInt(1, getEventYear());
			stmt.setInt(2, getEventNo());

			stmt.setString(3, get��Ǹ�());
			stmt.setDate(4, get��������());
			stmt.setLong(5, getû���ݾ�());
			stmt.setString(6, get����());
			stmt.setLong(7, charge.getNo());
			stmt.setString(8, pic1);
			stmt.setString(9, pic2);
			stmt.setString(10, judgement_location);
			stmt.setString(11, judgement_use);
			stmt.setString(12, judgement_land);
			stmt.setString(13, judgement_road);
			stmt.setString(14, judgement_temp);
			stmt.setLong(15, getDbid());

			stmt.execute();
		} finally {
			DB.cleanup(stmt);
		}
	}

	public void update���ϳ���(���� item) {
		if (���ϳ��� == null || item == null) {
			return;
		}
		for (Row record : ���ϳ���.getRows()) {
			String no = record.getValue(0);
			String target = "" + item.get���ǹ�ȣ();
			if (no.equals(target)) {
				item.add���ϳ���(record);
			}
		}
	}

	private String parseEventNo(String src) {
		int index = src.indexOf(Ÿ��);
		String temp = src.substring(index + Ÿ��.length());
		while (temp.startsWith("0")) {
			temp = temp.substring(1);
		}
		return temp;
	}

	private String removeDots(String src) {
		return src.replaceAll(".", "").replaceAll("-", "");
	}

	void update���䱸(���� item) {
		if (���䱸���⳻�� == null) {
			return;
		}
		for (Row record : ���䱸���⳻��.getRows()) {
			if (record.getValue(0).equals("" + item.get���ǹ�ȣ())) {
				item.set���䱸������(record.getValue(2));
			}
		}
	}

	public static ��� find(���� court, long ��ǹ�ȣ) throws Exception {
		logger.log(Level.INFO, "���� Code=" + court.getCode() + ", ��ǹ�ȣ=" + ��ǹ�ȣ + " �� ����� �˻��մϴ�. ");
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = DB.prepareStatement("SELECT * " + //
					"FROM ac_event " + //
					"WHERE no=? AND court_code=?;");
			stmt.setLong(1, ��ǹ�ȣ);
			stmt.setLong(2, DB.toLong(court.getCode()));
			stmt.execute();
			rs = stmt.getResultSet();
			if (rs.next()) {
				return ���.load(rs);
			}
			return null;
		} finally {
			DB.cleanup(rs, stmt);
		}
	}

	private static ��� load(ResultSet rs) throws Exception {
		long id = rs.getLong("id");
		int event_year = rs.getInt("event_year");
		int event_no = rs.getInt("event_no");
		String name = rs.getString("name");
		Date accept_date = rs.getDate("accept_date");
		long claim_price = rs.getLong("claim_price");
		String merged_to = rs.getString("merged_to");
		long charge_id = rs.getLong("charge_id");

		���� charge = ����.findByNo(charge_id);

		String pic1 = rs.getString("pic_1");
		String pic2 = rs.getString("pic_2");
		String judgement_location = rs.getString("judgement_location");
		String judgement_use = rs.getString("judgement_use");
		String judgement_land = rs.getString("judgement_land");
		String judgement_road = rs.getString("judgement_road");
		String judgement_temp = rs.getString("judgement_temp");

		return new ���(charge, id, event_year, event_no, name, accept_date, claim_price, merged_to, pic1, pic2,
				judgement_location, judgement_use, judgement_land, judgement_road, judgement_temp);
	}

	public static ��� findById(long id) throws Exception {
		��� s = Registry.get���(id);
		if (s != null) {
			return s;
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = DB.prepareStatement("SELECT * " + //
					"FROM ac_event " + //
					"WHERE id=?;");
			stmt.setLong(1, id);
			stmt.execute();
			rs = stmt.getResultSet();
			return checkResultSet(rs);
		} finally {
			DB.cleanup(rs, stmt);
		}
	}

	private static ��� checkResultSet(ResultSet rs) throws SQLException, Exception {
		if (rs.next()) {
			��� s2 = ���.load(rs);
			Registry.add���(s2);
			return s2;
		}
		return null;
	}

	public boolean hasPictures() {
		if ((pic1 == null || "".equals(pic1)) && (pic2 == null || "".equals(pic2))) {
			return false;
		}

		return true;
	}

	public void retrieveFromDB() throws Exception {
		��� s = ���.find(court, ��ǹ�ȣ);
		if (s == null) {
			return;
		}
		setDbid(s.getDbid());
		setPic1(s.getPic1());
		setPic2(s.getPic2());
		setJudgement_land(s.getJudgement_land());
		setJudgement_location(s.getJudgement_location());
		setJudgement_road(s.getJudgement_road());
		setJudgement_temp(s.getJudgement_temp());
		setJudgement_use(s.getJudgement_use());
		Registry.add���(this);
	}
}
