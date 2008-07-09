/*
 * 
 */
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

// TODO: Auto-generated Javadoc
/**
 * The Class ���.
 */
public class ��� {
	
	/** The Constant Ÿ��. */
	private static final String Ÿ�� = "0130";

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The charge. */
	public final ���� charge;

	/** The court. */
	public final ���� court;

	/** The items. */
	public ArrayList<����> items = new ArrayList<����>();

	/** The ��ǹ�ȣ. */
	public long ��ǹ�ȣ;

	/** The ������. */
	public LinkedList<String> ������ = new LinkedList<String>();

	/** The ä����. */
	public LinkedList<String> ä���� = new LinkedList<String>();

	/** The ä����. */
	public LinkedList<String> ä���� = new LinkedList<String>();

	/** The dbid. */
	private long dbid;

	/** The event no. */
	private int eventNo;

	/** The event year. */
	private int eventYear;

	/** The judgement_land. */
	private String judgement_land;

	/** The judgement_location. */
	private String judgement_location;

	/** The judgement_road. */
	private String judgement_road;

	/** The judgement_temp. */
	private String judgement_temp;

	/** The judgement_use. */
	private String judgement_use;

	/** The �����򰡼�. */
	private �����򰡼� �����򰡼�;

	/** The ���ð�������. */
	private Date ���ð�������;

	/** The ���ϳ���. */
	private Table ���ϳ���;

	/** The ���䱸���⳻��. */
	private Table ���䱸���⳻��;

	/** The ����. */
	private String ����;

	/** The ��Ǹ�. */
	private String ��Ǹ�;

	/** The ����װ���������. */
	private String ����װ���������;
	
	/** The ��������. */
	private Date ��������;

	/** The ���ÿܰǹ�. */
	private List<���ÿܰǹ�> ���ÿܰǹ�;
	
	/** The �������. */
	private String �������;

	/** The ��������. */
	private Date ��������;

	/** The û���ݾ�. */
	private long û���ݾ�;

	/** The ��Ȳ���缭. */
	private ��Ȳ���缭 ��Ȳ���缭;

	/** The pic1. */
	String pic1;

	/** The pic2. */
	String pic2;

	/**
	 * Instantiates a new ���.
	 * 
	 * @param charge the charge
	 * @param id the id
	 * @param event_year the event_year
	 * @param event_no the event_no
	 * @param name the name
	 * @param accept_date the accept_date
	 * @param claim_price the claim_price
	 * @param merged_to the merged_to
	 * @param pic1 the pic1
	 * @param pic2 the pic2
	 * @param judgement_location the judgement_location
	 * @param judgement_use the judgement_use
	 * @param judgement_land the judgement_land
	 * @param judgement_road the judgement_road
	 * @param judgement_temp the judgement_temp
	 */
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

	/**
	 * Instantiates a new ���.
	 * 
	 * @param court the court
	 * @param charge the charge
	 */
	public ���(���� court, ���� charge) {
		this.court = court;
		this.charge = charge;

	}

	/**
	 * Adds the.
	 * 
	 * @param item the item
	 */
	public void add(���� item) {
		item.setParent(this);
		items.add(item);
		update���䱸(item);
		update���ϳ���(item);
	}

	/**
	 * Add������.
	 * 
	 * @param ������ the ������
	 */
	public void add������(String ������) {
		this.������.add(������);

	}

	/**
	 * Addä����.
	 * 
	 * @param ä���� the ä����
	 */
	public void addä����(String ä����) {
		this.ä����.add(ä����);
	}

	/**
	 * Addä����.
	 * 
	 * @param ä���� the ä����
	 */
	public void addä����(String ä����) {
		this.ä����.add(ä����);
	}

	/**
	 * Clear������.
	 */
	public void clear������() {
		������.clear();
	}

	/**
	 * Clearä����.
	 */
	public void clearä����() {
		ä����.clear();
	}

	/**
	 * Clearä����.
	 */
	public void clearä����() {
		ä����.clear();
	}

	/**
	 * Gets the dbid.
	 * 
	 * @return the dbid
	 */
	public long getDbid() {
		return dbid;
	}

	/**
	 * Gets the event no.
	 * 
	 * @return the event no
	 */
	public int getEventNo() {
		return eventNo;
	}

	/**
	 * Gets the event year.
	 * 
	 * @return the event year
	 */
	public int getEventYear() {
		return eventYear;

	}

	/**
	 * Gets the judgement_land.
	 * 
	 * @return the judgement_land
	 */
	public String getJudgement_land() {
		return judgement_land;
	}

	/**
	 * Gets the judgement_location.
	 * 
	 * @return the judgement_location
	 */
	public String getJudgement_location() {
		return judgement_location;
	}

	/**
	 * Gets the judgement_road.
	 * 
	 * @return the judgement_road
	 */
	public String getJudgement_road() {
		return judgement_road;
	}

	/**
	 * Gets the judgement_temp.
	 * 
	 * @return the judgement_temp
	 */
	public String getJudgement_temp() {
		return judgement_temp;
	}

	/**
	 * Gets the judgement_use.
	 * 
	 * @return the judgement_use
	 */
	public String getJudgement_use() {
		return judgement_use;
	}

	/**
	 * Gets the pic1.
	 * 
	 * @return the pic1
	 */
	public String getPic1() {
		return pic1;
	}

	/**
	 * Gets the pic2.
	 * 
	 * @return the pic2
	 */
	public String getPic2() {
		return pic2;
	}

	/**
	 * Gets the �����򰡼�.
	 * 
	 * @return the �����򰡼�
	 */
	public �����򰡼� get�����򰡼�() {
		return �����򰡼�;
	}

	/**
	 * Gets the ���ð�������.
	 * 
	 * @return the ���ð�������
	 */
	public Date get���ð�������() {
		return ���ð�������;
	}

	/**
	 * Gets the ����s.
	 * 
	 * @return the ����s
	 */
	public ����[] get����s() {
		return items.toArray(new ����[items.size()]);
	}

	/**
	 * Gets the ���䱸���⳻��.
	 * 
	 * @return the ���䱸���⳻��
	 */
	public Table get���䱸���⳻��() {
		return ���䱸���⳻��;
	}

	/**
	 * Gets the ����.
	 * 
	 * @return the ����
	 */
	public String get����() {
		if (���� == null || "0".equals(����.trim()) || "O".equals(����.trim())) {
			return "";
		}

		return ����;
	}

	/**
	 * Gets the ��� path.
	 * 
	 * @return the ��� path
	 */
	public String get���Path() {
		return getEventYear() + "/" + court.getCode() + "/" + removeDots(charge.get�Ű�����().toString()) + "/"
				+ charge.get�����ڵ�() + "/" + get��ǹ�ȣ() + "/";
	}

	/**
	 * Gets the ��Ǹ�.
	 * 
	 * @return the ��Ǹ�
	 */
	public String get��Ǹ�() {
		return ��Ǹ�;
	}

	/**
	 * Gets the ��ǹ�ȣ.
	 * 
	 * @return the ��ǹ�ȣ
	 */
	public long get��ǹ�ȣ() {
		return ��ǹ�ȣ;
	}

	/**
	 * Gets the ����װ���������.
	 * 
	 * @return the ����װ���������
	 */
	public String get����װ���������() {
		return ����װ���������;
	}

	/**
	 * Gets the ������.
	 * 
	 * @return the ������
	 */
	public String get������() {
		return ������.toString();
	}

	/**
	 * Gets the ��������.
	 * 
	 * @return the ��������
	 */
	public Date get��������() {
		return ��������;
	}

	/**
	 * Gets the ���ÿܰǹ�.
	 * 
	 * @return the ���ÿܰǹ�
	 */
	public List<���ÿܰǹ�> get���ÿܰǹ�() {
		return ���ÿܰǹ�;
	}

	/**
	 * Gets the �������.
	 * 
	 * @return the �������
	 */
	public String get�������() {
		return �������;
	}

	/**
	 * Gets the ��������.
	 * 
	 * @return the ��������
	 */
	public Date get��������() {
		return ��������;
	}

	/**
	 * Gets the ä����.
	 * 
	 * @return the ä����
	 */
	public String getä����() {
		return ä����.toString();
	}

	/**
	 * Gets the ä����.
	 * 
	 * @return the ä����
	 */
	public String getä����() {
		return ä����.toString();
	}

	/**
	 * Gets the û���ݾ�.
	 * 
	 * @return the û���ݾ�
	 */
	public long getû���ݾ�() {
		return û���ݾ�;
	}

	/**
	 * Gets the ��Ȳ���缭.
	 * 
	 * @return the ��Ȳ���缭
	 */
	public ��Ȳ���缭 get��Ȳ���缭() {
		return ��Ȳ���缭;
	}

	/**
	 * Insert.
	 * 
	 * @throws Exception the exception
	 */
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

	/**
	 * Checks if is in.
	 * 
	 * @param eventNos the event nos
	 * 
	 * @return true, if is in
	 */
	public boolean isIn(Long[] eventNos) {
		for (Long no : eventNos) {
			if (no.equals(��ǹ�ȣ)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Merge.
	 * 
	 * @param s2 the s2
	 */
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

	/**
	 * Sets the dbid.
	 * 
	 * @param dbid the new dbid
	 */
	public void setDbid(long dbid) {
		this.dbid = dbid;
	}

	/**
	 * Sets the dB id.
	 * 
	 * @param no the new dB id
	 */
	public void setDBId(long no) {
		this.dbid = no;
	}

	/**
	 * Sets the judgement_land.
	 * 
	 * @param judgement_land the new judgement_land
	 */
	public void setJudgement_land(String judgement_land) {
		this.judgement_land = judgement_land;
	}

	/**
	 * Sets the judgement_location.
	 * 
	 * @param judgement_location the new judgement_location
	 */
	public void setJudgement_location(String judgement_location) {
		this.judgement_location = judgement_location;
	}

	/**
	 * Sets the judgement_road.
	 * 
	 * @param judgement_road the new judgement_road
	 */
	public void setJudgement_road(String judgement_road) {
		this.judgement_road = judgement_road;
	}

	/**
	 * Sets the judgement_temp.
	 * 
	 * @param judgement_temp the new judgement_temp
	 */
	public void setJudgement_temp(String judgement_temp) {
		this.judgement_temp = judgement_temp;
	}

	/**
	 * Sets the judgement_use.
	 * 
	 * @param judgement_use the new judgement_use
	 */
	public void setJudgement_use(String judgement_use) {
		this.judgement_use = judgement_use;
	}

	/**
	 * Sets the pic1.
	 * 
	 * @param pic1 the new pic1
	 */
	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}

	// public long getDBId() {
	// return dbid;
	// }

	/**
	 * Sets the pic2.
	 * 
	 * @param pic2 the new pic2
	 */
	public void setPic2(String pic2) {
		this.pic2 = pic2;
	}

	/**
	 * Sets the �����򰡼�.
	 * 
	 * @param �����򰡼� the new �����򰡼�
	 */
	public void set�����򰡼�(�����򰡼� �����򰡼�) {
		this.�����򰡼� = �����򰡼�;
	}

	/**
	 * Sets the ���ð�������.
	 * 
	 * @param ���ð������� the new ���ð�������
	 */
	public void set���ð�������(String ���ð�������) {
		this.���ð������� = DB.toDate(���ð�������);
	}

	/**
	 * Sets the ���ϳ���.
	 * 
	 * @param ���ϳ��� the new ���ϳ���
	 */
	public void set���ϳ���(Table ���ϳ���) {
		this.���ϳ��� = ���ϳ���;
	}

	/**
	 * Sets the ���䱸���⳻��.
	 * 
	 * @param ���䱸���⳻�� the new ���䱸���⳻��
	 */
	public void set���䱸���⳻��(Table ���䱸���⳻��) {
		// System.out.println(���䱸���⳻��);
		this.���䱸���⳻�� = ���䱸���⳻��;

	}

	/**
	 * Sets the ����.
	 * 
	 * @param str the new ����
	 */
	public void set����(String str) {
		���� = str;
	}

	/**
	 * Sets the ��Ǹ�.
	 * 
	 * @param ��Ǹ� the new ��Ǹ�
	 */
	public void set��Ǹ�(String ��Ǹ�) {
		this.��Ǹ� = ��Ǹ�;
	}

	/**
	 * Sets the ��ǹ�ȣ.
	 * 
	 * @param ��ǹ�ȣ the new ��ǹ�ȣ
	 */
	public void set��ǹ�ȣ(String ��ǹ�ȣ) {
		this.��ǹ�ȣ = DB.toLong(��ǹ�ȣ);
		this.eventNo = Integer.parseInt(parseEventNo(��ǹ�ȣ));
		this.eventYear = Integer.parseInt(��ǹ�ȣ.substring(0, ��ǹ�ȣ.indexOf(Ÿ��)));
	}

	/**
	 * Sets the ����װ���������.
	 * 
	 * @param ����װ��������� the new ����װ���������
	 */
	public void set����װ���������(String ����װ���������) {
		this.����װ��������� = ����װ���������;
	}

	/**
	 * Sets the ��������.
	 * 
	 * @param �������� the new ��������
	 */
	public void set��������(String ��������) {
		this.�������� = DB.toDate(��������);
	}

	/**
	 * Sets the ���ÿܰǹ�.
	 * 
	 * @param ���ÿܰǹ� the new ���ÿܰǹ�
	 */
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

	/**
	 * Sets the �������.
	 * 
	 * @param ������� the new �������
	 */
	public void set�������(String �������) {
		this.������� = �������;
	}

	/**
	 * Sets the ��������.
	 * 
	 * @param �������� the new ��������
	 */
	public void set��������(String ��������) {
		this.�������� = DB.toDate(��������);
	}

	/**
	 * Sets the û���ݾ�.
	 * 
	 * @param û���ݾ� the new û���ݾ�
	 */
	public void setû���ݾ�(String û���ݾ�) {
		this.û���ݾ� = DB.toPrice(û���ݾ�);
	}

	/**
	 * Sets the ��Ȳ���缭.
	 * 
	 * @param ��Ȳ���缭 the new ��Ȳ���缭
	 */
	public void set��Ȳ���缭(��Ȳ���缭 ��Ȳ���缭) {
		this.��Ȳ���缭 = ��Ȳ���缭;

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getEventYear() + "Ÿ��" + getEventNo();
	}

	/**
	 * Update.
	 * 
	 * @throws Exception the exception
	 */
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

	/**
	 * Update���ϳ���.
	 * 
	 * @param item the item
	 */
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

	/**
	 * Parses the event no.
	 * 
	 * @param src the src
	 * 
	 * @return the string
	 */
	private String parseEventNo(String src) {
		int index = src.indexOf(Ÿ��);
		String temp = src.substring(index + Ÿ��.length());
		while (temp.startsWith("0")) {
			temp = temp.substring(1);
		}
		return temp;
	}

	/**
	 * Removes the dots.
	 * 
	 * @param src the src
	 * 
	 * @return the string
	 */
	private String removeDots(String src) {
		return src.replaceAll(".", "").replaceAll("-", "");
	}

	/**
	 * Update���䱸.
	 * 
	 * @param item the item
	 */
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

	/**
	 * Find.
	 * 
	 * @param court the court
	 * @param ��ǹ�ȣ the ��ǹ�ȣ
	 * 
	 * @return the ���
	 * 
	 * @throws Exception the exception
	 */
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

	/**
	 * Load.
	 * 
	 * @param rs the rs
	 * 
	 * @return the ���
	 * 
	 * @throws Exception the exception
	 */
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

	/**
	 * Find by id.
	 * 
	 * @param id the id
	 * 
	 * @return the ���
	 * 
	 * @throws Exception the exception
	 */
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

	/**
	 * Check result set.
	 * 
	 * @param rs the rs
	 * 
	 * @return the ���
	 * 
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	private static ��� checkResultSet(ResultSet rs) throws SQLException, Exception {
		if (rs.next()) {
			��� s2 = ���.load(rs);
			Registry.add���(s2);
			return s2;
		}
		return null;
	}

	/**
	 * Checks for pictures.
	 * 
	 * @return true, if successful
	 */
	public boolean hasPictures() {
		if ((pic1 == null || "".equals(pic1)) && (pic2 == null || "".equals(pic2))) {
			return false;
		}

		return true;
	}

	/**
	 * Retrieve from db.
	 * 
	 * @throws Exception the exception
	 */
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
