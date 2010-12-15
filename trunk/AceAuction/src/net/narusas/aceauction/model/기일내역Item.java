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

public class 기일내역Item {
	static Logger logger = Logger.getLogger("log");

	private Date date;

	private long goods_id;

	private String 기일;

	private String 기일결과;

	private String 기일장소;

	private String 기일종류;

	private String 물건번호;
	private String 시간;

	private String 최저매각가격;

	int index;

	private Date bid_start;

	private Date bid_end;

	@Override
	public String toString() {
		return "기일내역Item={Date=" + date + ", goods_id=" + goods_id + ",기일=" + 기일 + ",기일결과=" + 기일결과 + ",기일장소=" + 기일장소
				+ ",기일종류=" + 기일종류 + ",물건번호=" + 물건번호 + ",시간=" + 시간 + ",최저매각가격=" + 최저매각가격 + ",입찰시작=" + bid_start
				+ ", 입찰종료=" + bid_end + ",index=" + index + "}";
	}

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

	@Override
	public boolean equals(Object arg0) {
		기일내역Item item = (기일내역Item) arg0;
		return 물건번호.equals(item.물건번호) && 기일.equals(item.기일);
	}

	public int getIndex() {
		return index;
	}

	public String get기일() {
		return 기일 != null ? 기일 : DB.toString(date);
	}

	public String get기일결과() {
		return 기일결과;
	}

	public String get기일장소() {
		return 기일장소;
	}

	public String get기일종류() {
		return 기일종류;
	}

	public String get물건번호() {
		return 물건번호;
	}

	public String get시간() {
		return 시간;
	}

	public String get최저매각가격() {
		return 최저매각가격;
	}

	@Override
	public int hashCode() {
		return 물건번호.hashCode() + 기일.hashCode();
	}

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

	public Date get기간입찰_시작() {
		return bid_start;
	}

	public Date get기간입찰_종료() {
		return bid_end;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void set기일(String 기일) {
		this.기일 = 기일;
	}

	public void set기일결과(String 기일결과) {
		this.기일결과 = 기일결과;
	}

	public void set기일장소(String 기일장소) {
		this.기일장소 = 기일장소;
	}

	public void set기일종류(String 기일종류) {
		this.기일종류 = 기일종류;
	}

	public void set물건번호(String 물건번호) {
		this.물건번호 = 물건번호;
	}

	public void set시간(String 시간) {
		this.시간 = 시간;
	}

	public void set최저매각가격(String 최저매각가격) {
		this.최저매각가격 = 최저매각가격;
	}

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

	public static void removeForGoodsId(long id) throws Exception {
		Statement stmt = DB.createStatement();
		stmt.executeUpdate("DELETE " + //
				"FROM ac_appoint_statement " + //
				"WHERE goods_id=" + id + ";");

	}

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
