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
 * The Class 사건.
 */
public class 사건 {
	
	/** The Constant 타경. */
	private static final String 타경 = "0130";

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The charge. */
	public final 담당계 charge;

	/** The court. */
	public final 법원 court;

	/** The items. */
	public ArrayList<물건> items = new ArrayList<물건>();

	/** The 사건번호. */
	public long 사건번호;

	/** The 소유자. */
	public LinkedList<String> 소유자 = new LinkedList<String>();

	/** The 채권자. */
	public LinkedList<String> 채권자 = new LinkedList<String>();

	/** The 채무자. */
	public LinkedList<String> 채무자 = new LinkedList<String>();

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

	/** The 감정평가서. */
	private 감정평가서 감정평가서;

	/** The 개시결정일자. */
	private Date 개시결정일자;

	/** The 기일내역. */
	private Table 기일내역;

	/** The 배당요구종기내역. */
	private Table 배당요구종기내역;

	/** The 병합. */
	private String 병합;

	/** The 사건명. */
	private String 사건명;

	/** The 사건항고정지여부. */
	private String 사건항고정지여부;
	
	/** The 접수일자. */
	private Date 접수일자;

	/** The 제시외건물. */
	private List<제시외건물> 제시외건물;
	
	/** The 종국결과. */
	private String 종국결과;

	/** The 종국일자. */
	private Date 종국일자;

	/** The 청구금액. */
	private long 청구금액;

	/** The 현황조사서. */
	private 현황조사서 현황조사서;

	/** The pic1. */
	String pic1;

	/** The pic2. */
	String pic2;

	/**
	 * Instantiates a new 사건.
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
	public 사건(담당계 charge, long id, int event_year, int event_no, String name, Date accept_date, long claim_price,
			String merged_to, String pic1, String pic2, String judgement_location, String judgement_use,
			String judgement_land, String judgement_road, String judgement_temp) {
		this.charge = charge;
		eventYear = event_year;
		eventNo = event_no;
		사건명 = name;
		접수일자 = accept_date;
		청구금액 = claim_price;
		병합 = merged_to;
		this.pic1 = pic1;
		this.pic2 = pic2;
		this.judgement_location = judgement_location;
		this.judgement_use = judgement_use;
		this.judgement_land = judgement_land;
		this.judgement_road = judgement_road;
		this.judgement_temp = judgement_temp;
		this.court = charge.get법원();
		this.dbid = id;
	}

	/**
	 * Instantiates a new 사건.
	 * 
	 * @param court the court
	 * @param charge the charge
	 */
	public 사건(법원 court, 담당계 charge) {
		this.court = court;
		this.charge = charge;

	}

	/**
	 * Adds the.
	 * 
	 * @param item the item
	 */
	public void add(물건 item) {
		item.setParent(this);
		items.add(item);
		update배당요구(item);
		update기일내역(item);
	}

	/**
	 * Add소유자.
	 * 
	 * @param 소유자 the 소유자
	 */
	public void add소유자(String 소유자) {
		this.소유자.add(소유자);

	}

	/**
	 * Add채권자.
	 * 
	 * @param 채권자 the 채권자
	 */
	public void add채권자(String 채권자) {
		this.채권자.add(채권자);
	}

	/**
	 * Add채무자.
	 * 
	 * @param 채무자 the 채무자
	 */
	public void add채무자(String 채무자) {
		this.채무자.add(채무자);
	}

	/**
	 * Clear소유자.
	 */
	public void clear소유자() {
		소유자.clear();
	}

	/**
	 * Clear채권자.
	 */
	public void clear채권자() {
		채권자.clear();
	}

	/**
	 * Clear채무자.
	 */
	public void clear채무자() {
		채무자.clear();
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
	 * Gets the 감정평가서.
	 * 
	 * @return the 감정평가서
	 */
	public 감정평가서 get감정평가서() {
		return 감정평가서;
	}

	/**
	 * Gets the 개시결정일자.
	 * 
	 * @return the 개시결정일자
	 */
	public Date get개시결정일자() {
		return 개시결정일자;
	}

	/**
	 * Gets the 물건s.
	 * 
	 * @return the 물건s
	 */
	public 물건[] get물건s() {
		return items.toArray(new 물건[items.size()]);
	}

	/**
	 * Gets the 배당요구종기내역.
	 * 
	 * @return the 배당요구종기내역
	 */
	public Table get배당요구종기내역() {
		return 배당요구종기내역;
	}

	/**
	 * Gets the 병합.
	 * 
	 * @return the 병합
	 */
	public String get병합() {
		if (병합 == null || "0".equals(병합.trim()) || "O".equals(병합.trim())) {
			return "";
		}

		return 병합;
	}

	/**
	 * Gets the 사건 path.
	 * 
	 * @return the 사건 path
	 */
	public String get사건Path() {
		return getEventYear() + "/" + court.getCode() + "/" + removeDots(charge.get매각기일().toString()) + "/"
				+ charge.get담당계코드() + "/" + get사건번호() + "/";
	}

	/**
	 * Gets the 사건명.
	 * 
	 * @return the 사건명
	 */
	public String get사건명() {
		return 사건명;
	}

	/**
	 * Gets the 사건번호.
	 * 
	 * @return the 사건번호
	 */
	public long get사건번호() {
		return 사건번호;
	}

	/**
	 * Gets the 사건항고정지여부.
	 * 
	 * @return the 사건항고정지여부
	 */
	public String get사건항고정지여부() {
		return 사건항고정지여부;
	}

	/**
	 * Gets the 소유자.
	 * 
	 * @return the 소유자
	 */
	public String get소유자() {
		return 소유자.toString();
	}

	/**
	 * Gets the 접수일자.
	 * 
	 * @return the 접수일자
	 */
	public Date get접수일자() {
		return 접수일자;
	}

	/**
	 * Gets the 제시외건물.
	 * 
	 * @return the 제시외건물
	 */
	public List<제시외건물> get제시외건물() {
		return 제시외건물;
	}

	/**
	 * Gets the 종국결과.
	 * 
	 * @return the 종국결과
	 */
	public String get종국결과() {
		return 종국결과;
	}

	/**
	 * Gets the 종국일자.
	 * 
	 * @return the 종국일자
	 */
	public Date get종국일자() {
		return 종국일자;
	}

	/**
	 * Gets the 채권자.
	 * 
	 * @return the 채권자
	 */
	public String get채권자() {
		return 채권자.toString();
	}

	/**
	 * Gets the 채무자.
	 * 
	 * @return the 채무자
	 */
	public String get채무자() {
		return 채무자.toString();
	}

	/**
	 * Gets the 청구금액.
	 * 
	 * @return the 청구금액
	 */
	public long get청구금액() {
		return 청구금액;
	}

	/**
	 * Gets the 현황조사서.
	 * 
	 * @return the 현황조사서
	 */
	public 현황조사서 get현황조사서() {
		return 현황조사서;
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
			stmt.setLong(1, get사건번호());
			stmt.setInt(2, getEventYear());
			stmt.setInt(3, getEventNo());

			stmt.setString(4, get사건명());
			stmt.setDate(5, get접수일자());
			stmt.setLong(6, get청구금액());
			stmt.setString(7, get병합());

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
			Registry.add사건(this);
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
			if (no.equals(사건번호)) {
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
	public void merge(사건 s2) {
		logger.info(this.사건번호 + "(" + items.size() + ")이 " + s2.사건번호 + "(" + s2.items.size()
				+ ")와 페이지가 분리되어 있습니다. 합칩니다. ");

		try {
			ArrayList<물건> allMulgun = new ArrayList<물건>();
			allMulgun.addAll(this.items);
			allMulgun.addAll(s2.items);
			ArrayList<물건> res = new ArrayList<물건>();

			if (allMulgun.size() == 0) {
				return;
			}
			for (물건 item : allMulgun) {
				item.set사건(this);
			}
			res.add(allMulgun.get(0));
			for (int i = 1; i < allMulgun.size(); i++) {
				물건 m1 = res.get(res.size() - 1);
				물건 m2 = allMulgun.get(i);
				if (m1.get물건번호() == m2.get물건번호()) {
					m1.merge(m2);
				} else {
					res.add(m2);
				}
			}
			this.items = res;
			// logger.info("페이지를 합치기 위해 대법원에서 기일내역을 얻어 옵니다.");
			//
			// 대법원기일내역Fetcher f = new 대법원기일내역Fetcher();
			// f.update(this);
			// logger.info("얻어온 기일 내역으로 사건의 정보를 갱신합니다. ");

			for (int i = 0; i < items.size(); i++) {
				물건 item = items.get(i);
				update기일내역(item);
				update배당요구(item);
				s2.update기일내역(item);
				s2.update배당요구(item);
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
	 * Sets the 감정평가서.
	 * 
	 * @param 감정평가서 the new 감정평가서
	 */
	public void set감정평가서(감정평가서 감정평가서) {
		this.감정평가서 = 감정평가서;
	}

	/**
	 * Sets the 개시결정일자.
	 * 
	 * @param 개시결정일자 the new 개시결정일자
	 */
	public void set개시결정일자(String 개시결정일자) {
		this.개시결정일자 = DB.toDate(개시결정일자);
	}

	/**
	 * Sets the 기일내역.
	 * 
	 * @param 기일내역 the new 기일내역
	 */
	public void set기일내역(Table 기일내역) {
		this.기일내역 = 기일내역;
	}

	/**
	 * Sets the 배당요구종기내역.
	 * 
	 * @param 배당요구종기내역 the new 배당요구종기내역
	 */
	public void set배당요구종기내역(Table 배당요구종기내역) {
		// System.out.println(배당요구종기내역);
		this.배당요구종기내역 = 배당요구종기내역;

	}

	/**
	 * Sets the 병합.
	 * 
	 * @param str the new 병합
	 */
	public void set병합(String str) {
		병합 = str;
	}

	/**
	 * Sets the 사건명.
	 * 
	 * @param 사건명 the new 사건명
	 */
	public void set사건명(String 사건명) {
		this.사건명 = 사건명;
	}

	/**
	 * Sets the 사건번호.
	 * 
	 * @param 사건번호 the new 사건번호
	 */
	public void set사건번호(String 사건번호) {
		this.사건번호 = DB.toLong(사건번호);
		this.eventNo = Integer.parseInt(parseEventNo(사건번호));
		this.eventYear = Integer.parseInt(사건번호.substring(0, 사건번호.indexOf(타경)));
	}

	/**
	 * Sets the 사건항고정지여부.
	 * 
	 * @param 사건항고정지여부 the new 사건항고정지여부
	 */
	public void set사건항고정지여부(String 사건항고정지여부) {
		this.사건항고정지여부 = 사건항고정지여부;
	}

	/**
	 * Sets the 접수일자.
	 * 
	 * @param 접수일자 the new 접수일자
	 */
	public void set접수일자(String 접수일자) {
		this.접수일자 = DB.toDate(접수일자);
	}

	/**
	 * Sets the 제시외건물.
	 * 
	 * @param 제시외건물 the new 제시외건물
	 */
	public void set제시외건물(List<제시외건물> 제시외건물) {
		this.제시외건물 = 제시외건물;

		for (물건 m : this.items) {
			m.clear제시외건물();
			for (제시외건물 e : 제시외건물) {
				if (e.get물건번호() == m.get물건번호()) {
					m.add제시외건물(e);
				}
			}

		}

	}

	/**
	 * Sets the 종국결과.
	 * 
	 * @param 종국결과 the new 종국결과
	 */
	public void set종국결과(String 종국결과) {
		this.종국결과 = 종국결과;
	}

	/**
	 * Sets the 종국일자.
	 * 
	 * @param 종국일자 the new 종국일자
	 */
	public void set종국일자(String 종국일자) {
		this.종국일자 = DB.toDate(종국일자);
	}

	/**
	 * Sets the 청구금액.
	 * 
	 * @param 청구금액 the new 청구금액
	 */
	public void set청구금액(String 청구금액) {
		this.청구금액 = DB.toPrice(청구금액);
	}

	/**
	 * Sets the 현황조사서.
	 * 
	 * @param 현황조사서 the new 현황조사서
	 */
	public void set현황조사서(현황조사서 현황조사서) {
		this.현황조사서 = 현황조사서;

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getEventYear() + "타경" + getEventNo();
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

			stmt.setString(3, get사건명());
			stmt.setDate(4, get접수일자());
			stmt.setLong(5, get청구금액());
			stmt.setString(6, get병합());
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
	 * Update기일내역.
	 * 
	 * @param item the item
	 */
	public void update기일내역(물건 item) {
		if (기일내역 == null || item == null) {
			return;
		}
		for (Row record : 기일내역.getRows()) {
			String no = record.getValue(0);
			String target = "" + item.get물건번호();
			if (no.equals(target)) {
				item.add기일내역(record);
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
		int index = src.indexOf(타경);
		String temp = src.substring(index + 타경.length());
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
	 * Update배당요구.
	 * 
	 * @param item the item
	 */
	void update배당요구(물건 item) {
		if (배당요구종기내역 == null) {
			return;
		}
		for (Row record : 배당요구종기내역.getRows()) {
			if (record.getValue(0).equals("" + item.get물건번호())) {
				item.set배당요구종기일(record.getValue(2));
			}
		}
	}

	/**
	 * Find.
	 * 
	 * @param court the court
	 * @param 사건번호 the 사건번호
	 * 
	 * @return the 사건
	 * 
	 * @throws Exception the exception
	 */
	public static 사건 find(법원 court, long 사건번호) throws Exception {
		logger.log(Level.INFO, "법원 Code=" + court.getCode() + ", 사건번호=" + 사건번호 + " 인 사건을 검색합니다. ");
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = DB.prepareStatement("SELECT * " + //
					"FROM ac_event " + //
					"WHERE no=? AND court_code=?;");
			stmt.setLong(1, 사건번호);
			stmt.setLong(2, DB.toLong(court.getCode()));
			stmt.execute();
			rs = stmt.getResultSet();
			if (rs.next()) {
				return 사건.load(rs);
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
	 * @return the 사건
	 * 
	 * @throws Exception the exception
	 */
	private static 사건 load(ResultSet rs) throws Exception {
		long id = rs.getLong("id");
		int event_year = rs.getInt("event_year");
		int event_no = rs.getInt("event_no");
		String name = rs.getString("name");
		Date accept_date = rs.getDate("accept_date");
		long claim_price = rs.getLong("claim_price");
		String merged_to = rs.getString("merged_to");
		long charge_id = rs.getLong("charge_id");

		담당계 charge = 담당계.findByNo(charge_id);

		String pic1 = rs.getString("pic_1");
		String pic2 = rs.getString("pic_2");
		String judgement_location = rs.getString("judgement_location");
		String judgement_use = rs.getString("judgement_use");
		String judgement_land = rs.getString("judgement_land");
		String judgement_road = rs.getString("judgement_road");
		String judgement_temp = rs.getString("judgement_temp");

		return new 사건(charge, id, event_year, event_no, name, accept_date, claim_price, merged_to, pic1, pic2,
				judgement_location, judgement_use, judgement_land, judgement_road, judgement_temp);
	}

	/**
	 * Find by id.
	 * 
	 * @param id the id
	 * 
	 * @return the 사건
	 * 
	 * @throws Exception the exception
	 */
	public static 사건 findById(long id) throws Exception {
		사건 s = Registry.get사건(id);
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
	 * @return the 사건
	 * 
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	private static 사건 checkResultSet(ResultSet rs) throws SQLException, Exception {
		if (rs.next()) {
			사건 s2 = 사건.load(rs);
			Registry.add사건(s2);
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
		사건 s = 사건.find(court, 사건번호);
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
		Registry.add사건(this);
	}
}
