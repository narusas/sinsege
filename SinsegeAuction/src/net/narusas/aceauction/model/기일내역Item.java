/*
 * 
 */
package net.narusas.aceauction.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.data.금액Converter;

// TODO: Auto-generated Javadoc
/**
 * The Class 기일내역Item.
 */
public class 기일내역Item {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The date. */
	private Date date;

	/** The goods_id. */
	private long goods_id;

	/** The 기일. */
	private String 기일;

	/** The 기일결과. */
	private String 기일결과;

	/** The 기일장소. */
	private String 기일장소;

	/** The 기일종류. */
	private String 기일종류;

	/** The 물건번호. */
	private String 물건번호;
	
	/** The 시간. */
	private String 시간;

	/** The 최저매각가격. */
	private String 최저매각가격;

	/** The index. */
	int index;

	/** The bid_start. */
	private Date bid_start;

	/** The bid_end. */
	private Date bid_end;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "기일내역Item={Date=" + date + ", goods_id=" + goods_id + ",기일=" + 기일 + ",기일결과=" + 기일결과 + ",기일장소=" + 기일장소
				+ ",기일종류=" + 기일종류 + ",물건번호=" + 물건번호 + ",시간=" + 시간 + ",최저매각가격=" + 최저매각가격 + ",입찰시작=" + bid_start
				+ ", 입찰종료=" + bid_end + ",index=" + index + "}";
	}

	/**
	 * Instantiates a new 기일내역 item.
	 * 
	 * @param goods_id the goods_id
	 * @param index the index
	 * @param date the date
	 * @param type the type
	 * @param location the location
	 * @param lowest_price the lowest_price
	 * @param result the result
	 * @param bid_start the bid_start
	 * @param bid_end the bid_end
	 */
	public 기일내역Item(long goods_id, int index, Date date, String type, String location, String lowest_price,
			String result, Date bid_start, Date bid_end) {
		this.goods_id = goods_id;
		this.index = index;
		this.date = date;
		기일종류 = type;
		기일장소 = location;
		최저매각가격 = lowest_price;
		기일결과 = result;
		this.bid_start = bid_start;
		this.bid_end = bid_end;
	}

	/**
	 * Instantiates a new 기일내역 item.
	 * 
	 * @param record the record
	 */
	public 기일내역Item(Row record) {
		물건번호 = record.getValue(0);
		기일 = record.getValue(1);
		시간 = record.getValue(2);
		기일종류 = record.getValue(3);
		기일장소 = record.getValue(4);
		최저매각가격 = record.getValue(5);
		기일결과 = record.getValue(6);

		if (기일 != null) {
			Pattern p = Pattern.compile("\\((\\d+.\\d+.\\d+)~(\\d+.\\d+.\\d+)\\)");
			Matcher m = p.matcher(기일);
			if (m.find()) {
				bid_start = DB.toDate(m.group(1));
				bid_end = DB.toDate(m.group(2));
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		기일내역Item item = (기일내역Item) arg0;
		return 물건번호.equals(item.물건번호) && 기일.equals(item.기일);
	}

	/**
	 * Gets the index.
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the 기일.
	 * 
	 * @return the 기일
	 */
	public String get기일() {
		return 기일 != null ? 기일 : DB.toString(date);
	}

	/**
	 * Gets the 기일결과.
	 * 
	 * @return the 기일결과
	 */
	public String get기일결과() {
		return 기일결과;
	}

	/**
	 * Gets the 기일장소.
	 * 
	 * @return the 기일장소
	 */
	public String get기일장소() {
		return 기일장소;
	}

	/**
	 * Gets the 기일종류.
	 * 
	 * @return the 기일종류
	 */
	public String get기일종류() {
		return 기일종류;
	}

	/**
	 * Gets the 물건번호.
	 * 
	 * @return the 물건번호
	 */
	public String get물건번호() {
		return 물건번호;
	}

	/**
	 * Gets the 시간.
	 * 
	 * @return the 시간
	 */
	public String get시간() {
		return 시간;
	}

	/**
	 * Gets the 최저매각가격.
	 * 
	 * @return the 최저매각가격
	 */
	public String get최저매각가격() {
		return 최저매각가격;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 물건번호.hashCode() + 기일.hashCode();
	}

	/**
	 * Insert.
	 * 
	 * @param goods_id the goods_id
	 * 
	 * @throws Exception the exception
	 */
	public void insert(long goods_id) throws Exception {
		PreparedStatement stmt = DB.prepareStatement("INSERT INTO ac_appoint_statement "//
				+ "(" //
				+ "no, "// 1
				+ "fixed_date, "// 2
				+ "type, "// 3
				+ "location, "// 4
				+ "lowest_price, "// 5
				+ "result, "// 6
				+ "goods_id,"// 7
				+ "bid_start,"// 8
				+ "bid_end"// 9
				+ ") " + "VALUES (?,?,?,?,?,?,?,?,?);");

		logger.log(Level.INFO, "물건(" + goods_id + ")에 해당하는 " + index + "번째 기일내역을 DB에 추가합니다.  ");
		logger.log(Level.INFO, "기일=" + DB.toDate(get기일()) + ", 기일종류=" + get기일종류() + ", 장소=" + get기일장소() + ", 금액="
				+ 금액Converter.convert(get최저매각가격()) + ", 결과=" + get기일결과());
		stmt.setInt(1, index);
		stmt.setDate(2, DB.toDate(get기일()));
		stmt.setString(3, get기일종류());
		stmt.setString(4, get기일장소());
		stmt.setString(5, 금액Converter.convert(get최저매각가격()));
		stmt.setString(6, get기일결과());
		stmt.setLong(7, goods_id);
		stmt.setDate(8, get기간입찰_시작());
		stmt.setDate(9, get기간입찰_종료());
		stmt.execute();
	}

	/**
	 * Gets the 기간입찰_시작.
	 * 
	 * @return the 기간입찰_시작
	 */
	public Date get기간입찰_시작() {
		return bid_start;
	}

	/**
	 * Gets the 기간입찰_종료.
	 * 
	 * @return the 기간입찰_종료
	 */
	public Date get기간입찰_종료() {
		return bid_end;
	}

	/**
	 * Sets the index.
	 * 
	 * @param index the new index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Sets the 기일.
	 * 
	 * @param 기일 the new 기일
	 */
	public void set기일(String 기일) {
		this.기일 = 기일;
	}

	/**
	 * Sets the 기일결과.
	 * 
	 * @param 기일결과 the new 기일결과
	 */
	public void set기일결과(String 기일결과) {
		this.기일결과 = 기일결과;
	}

	/**
	 * Sets the 기일장소.
	 * 
	 * @param 기일장소 the new 기일장소
	 */
	public void set기일장소(String 기일장소) {
		this.기일장소 = 기일장소;
	}

	/**
	 * Sets the 기일종류.
	 * 
	 * @param 기일종류 the new 기일종류
	 */
	public void set기일종류(String 기일종류) {
		this.기일종류 = 기일종류;
	}

	/**
	 * Sets the 물건번호.
	 * 
	 * @param 물건번호 the new 물건번호
	 */
	public void set물건번호(String 물건번호) {
		this.물건번호 = 물건번호;
	}

	/**
	 * Sets the 시간.
	 * 
	 * @param 시간 the new 시간
	 */
	public void set시간(String 시간) {
		this.시간 = 시간;
	}

	/**
	 * Sets the 최저매각가격.
	 * 
	 * @param 최저매각가격 the new 최저매각가격
	 */
	public void set최저매각가격(String 최저매각가격) {
		this.최저매각가격 = 최저매각가격;
	}

	/**
	 * Find by goods id.
	 * 
	 * @param id the id
	 * 
	 * @return the list<기일내역 item>
	 * 
	 * @throws Exception the exception
	 */
	public static List<기일내역Item> findByGoodsId(long id) throws Exception {
		List<기일내역Item> res = new LinkedList<기일내역Item>();
		Statement st = DB.createStatement();
		ResultSet rs = st.executeQuery("SELECT * " + //
				"FROM ac_appoint_statement " + //
				"WHERE goods_id=" + id + ";");

		while (rs.next()) {
			res.add(기일내역Item.load(rs));
		}

		return res;
	}

	/**
	 * Removes the for goods id.
	 * 
	 * @param id the id
	 * 
	 * @throws Exception the exception
	 */
	public static void removeForGoodsId(long id) throws Exception {
		Statement stmt = DB.createStatement();
		stmt.executeUpdate("DELETE " + //
				"FROM ac_appoint_statement " + //
				"WHERE goods_id=" + id + ";");

	}

	/**
	 * Load.
	 * 
	 * @param rs the rs
	 * 
	 * @return the 기일내역 item
	 * 
	 * @throws SQLException the SQL exception
	 */
	static 기일내역Item load(ResultSet rs) throws SQLException {
		int index = rs.getInt("no");
		Date date = rs.getDate("fixed_date");
		Date bid_start = rs.getDate("bid_start");
		Date bid_end = rs.getDate("bid_end");

		String type = rs.getString("type");
		String location = rs.getString("location");
		String lowest_price = rs.getString("lowest_price");
		String result = rs.getString("result");
		long godos_id = rs.getLong("goods_id");

		return new 기일내역Item(godos_id, index, date, type, location, lowest_price, result, bid_start, bid_end);
	}

}
