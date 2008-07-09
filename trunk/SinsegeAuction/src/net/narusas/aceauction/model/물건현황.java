/*
 * 
 */
package net.narusas.aceauction.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.data.DB;

// TODO: Auto-generated Javadoc
/**
 * The Class 물건현황.
 */
public class 물건현황 {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The 면적pattern. */
	static Pattern 면적pattern = Pattern
			.compile("(\\d+\\s*㎡)", Pattern.MULTILINE);

	/** The 지분 pattern. */
	static Pattern 지분Pattern = Pattern.compile("\\(([^\\)]*지분[^\\)]*)\\)");

	/** The detail. */
	private String detail;

	/** The goods id. */
	private long goodsId;

	/** The no. */
	private int no;

	/** The 비고. */
	private String 비고;
	
	/** The 주소. */
	private final String 주소;

	/**
	 * Instantiates a new 물건현황.
	 * 
	 * @param goodsId the goods id
	 * @param no the no
	 * @param address the address
	 * @param comment the comment
	 */
	public 물건현황(long goodsId, int no, String address, String comment) {
		this.goodsId = goodsId;
		this.no = no;
		주소 = address;
		비고 = comment;
	}

	/**
	 * Instantiates a new 물건현황.
	 * 
	 * @param 주소 the 주소
	 */
	public 물건현황(String 주소) {
		this.주소 = 주소.trim();
	}

	/**
	 * Gets the detail.
	 * 
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * Gets the 건물비고.
	 * 
	 * @return the 건물비고
	 */
	public String get건물비고() {
		return 비고;
	}

	/**
	 * Gets the 면적.
	 * 
	 * @return the 면적
	 */
	public String get면적() {
		return calc면적();
	}

	/**
	 * Gets the 주소.
	 * 
	 * @return the 주소
	 */
	public String get주소() {
		return 주소;
	}

	/**
	 * Insert.
	 * 
	 * @throws Exception the exception
	 */
	public void insert() throws Exception {
		logger.info("물건현황을 DB에 추가합니다. no=" + no + ",주소=" + 주소 + ",비고=" + 비고);
		PreparedStatement stmt = DB
				.prepareStatement("INSERT INTO ac_goods_building " + //
						"(" // +
						+ "goods_id," // 1
						+ "no," // 2
						+ "address,"// 3
						+ "comment" // 4
						+ ") " //
						+ "VALUES (?,?,?,?);");
		stmt.setLong(1, goodsId);
		stmt.setInt(2, no);
		stmt.setString(3, 주소);
		stmt.setString(4, 비고);
		stmt.execute();
	}

	/**
	 * Sets the detail.
	 * 
	 * @param detail the new detail
	 */
	public void setDetail(String detail) {
		parse(detail);
	}

	/**
	 * Sets the goods id.
	 * 
	 * @param id the new goods id
	 */
	public void setGoodsId(int id) {
		this.goodsId = id;
	}

	/**
	 * Sets the no.
	 * 
	 * @param no the new no
	 */
	public void setNo(int no) {
		this.no = no;
	}

	/**
	 * Sets the 건물비고.
	 * 
	 * @param 건물비고 the new 건물비고
	 */
	public void set건물비고(String 건물비고) {
		this.비고 = 건물비고;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "물건현황{주소=[" + 주소 + "], 상세=[" + detail + "]}";
	}

	/**
	 * Calc면적.
	 * 
	 * @return the string
	 */
	private String calc면적() {
		Matcher m = 면적pattern.matcher(detail);
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}

	/**
	 * Parses the.
	 * 
	 * @param detail the detail
	 */
	private void parse(String detail) {
		this.detail = detail.trim();

		Matcher m = 지분Pattern.matcher(detail);
		if (m.find()) {
			비고 = m.group(1);
		}
	}

	/**
	 * 지정된 번호를 가지는 물건현황이 목록에 있는지 여부를 반환한다.
	 * 
	 * @param 현황s 검색목록
	 * @param targetNo 지정된 번호
	 * 
	 * @return 포함 여부.
	 */
	public static boolean exitIn(List<물건현황> 현황s, int targetNo) {
		for (물건현황 status : 현황s) {
			if (status.no == targetNo) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Find by goods id.
	 * 
	 * @param goodsId the goods id
	 * 
	 * @return the list<물건현황>
	 * 
	 * @throws Exception the exception
	 */
	public static List<물건현황> findByGoodsId(long goodsId) throws Exception {
		LinkedList<물건현황> result = new LinkedList<물건현황>();
		Statement stmt = DB.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * " + //
				"FROM ac_goods_building " + //
				"WHERE goods_id=" + goodsId + ";");
		while (rs.next()) {
			result.add(물건현황.load(rs));
		}
		return result;
	}

	/**
	 * Load.
	 * 
	 * @param rs the rs
	 * 
	 * @return the 물건현황
	 * 
	 * @throws SQLException the SQL exception
	 */
	public static 물건현황 load(ResultSet rs) throws SQLException {
		long goodsId = rs.getLong("goods_id");
		int no = rs.getInt("no");
		String address = rs.getString("address");
		String comment = rs.getString("comment");
		return new 물건현황(goodsId, no, address, comment);
	}
}
