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
import net.narusas.aceauction.data.�ݾ�Converter;

public class ���ϳ���Item {
	static Logger logger = Logger.getLogger("log");

	private Date date;

	private long goods_id;

	private String ����;

	private String ���ϰ��;

	private String �������;

	private String ��������;

	private String ���ǹ�ȣ;
	private String �ð�;

	private String �����Ű�����;

	int index;

	private Date bid_start;

	private Date bid_end;

	@Override
	public String toString() {
		return "���ϳ���Item={Date=" + date + ", goods_id=" + goods_id + ",����=" + ���� + ",���ϰ��=" + ���ϰ�� + ",�������=" + �������
				+ ",��������=" + �������� + ",���ǹ�ȣ=" + ���ǹ�ȣ + ",�ð�=" + �ð� + ",�����Ű�����=" + �����Ű����� + ",��������=" + bid_start
				+ ", ��������=" + bid_end + ",index=" + index + "}";
	}

	public ���ϳ���Item(long goods_id, int index, Date date, String type, String location, String lowest_price,
			String result, Date bid_start, Date bid_end) {
		this.goods_id = goods_id;
		this.index = index;
		this.date = date;
		�������� = type;
		������� = location;
		�����Ű����� = lowest_price;
		���ϰ�� = result;
		this.bid_start = bid_start;
		this.bid_end = bid_end;
	}

	public ���ϳ���Item(Row record) {
		���ǹ�ȣ = record.getValue(0);
		���� = record.getValue(1);
		�ð� = record.getValue(2);
		�������� = record.getValue(3);
		������� = record.getValue(4);
		�����Ű����� = record.getValue(5);
		���ϰ�� = record.getValue(6);

		if (���� != null) {
			Pattern p = Pattern.compile("\\((\\d+.\\d+.\\d+)~(\\d+.\\d+.\\d+)\\)");
			Matcher m = p.matcher(����);
			if (m.find()) {
				bid_start = DB.toDate(m.group(1));
				bid_end = DB.toDate(m.group(2));
			}
		}
	}

	@Override
	public boolean equals(Object arg0) {
		���ϳ���Item item = (���ϳ���Item) arg0;
		return ���ǹ�ȣ.equals(item.���ǹ�ȣ) && ����.equals(item.����);
	}

	public int getIndex() {
		return index;
	}

	public String get����() {
		return ���� != null ? ���� : DB.toString(date);
	}

	public String get���ϰ��() {
		return ���ϰ��;
	}

	public String get�������() {
		return �������;
	}

	public String get��������() {
		return ��������;
	}

	public String get���ǹ�ȣ() {
		return ���ǹ�ȣ;
	}

	public String get�ð�() {
		return �ð�;
	}

	public String get�����Ű�����() {
		return �����Ű�����;
	}

	@Override
	public int hashCode() {
		return ���ǹ�ȣ.hashCode() + ����.hashCode();
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

		logger.log(Level.INFO, "����(" + goods_id + ")�� �ش��ϴ� " + index + "��° ���ϳ����� DB�� �߰��մϴ�.  ");
		logger.log(Level.INFO, "����=" + DB.toDate(get����()) + ", ��������=" + get��������() + ", ���=" + get�������() + ", �ݾ�="
				+ �ݾ�Converter.convert(get�����Ű�����()) + ", ���=" + get���ϰ��());
		stmt.setInt(1, index);
		stmt.setDate(2, DB.toDate(get����()));
		stmt.setString(3, get��������());
		stmt.setString(4, get�������());
		stmt.setString(5, �ݾ�Converter.convert(get�����Ű�����()));
		stmt.setString(6, get���ϰ��());
		stmt.setLong(7, goods_id);
		stmt.setDate(8, get�Ⱓ����_����());
		stmt.setDate(9, get�Ⱓ����_����());
		stmt.execute();
	}

	public Date get�Ⱓ����_����() {
		return bid_start;
	}

	public Date get�Ⱓ����_����() {
		return bid_end;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void set����(String ����) {
		this.���� = ����;
	}

	public void set���ϰ��(String ���ϰ��) {
		this.���ϰ�� = ���ϰ��;
	}

	public void set�������(String �������) {
		this.������� = �������;
	}

	public void set��������(String ��������) {
		this.�������� = ��������;
	}

	public void set���ǹ�ȣ(String ���ǹ�ȣ) {
		this.���ǹ�ȣ = ���ǹ�ȣ;
	}

	public void set�ð�(String �ð�) {
		this.�ð� = �ð�;
	}

	public void set�����Ű�����(String �����Ű�����) {
		this.�����Ű����� = �����Ű�����;
	}

	public static List<���ϳ���Item> findByGoodsId(long id) throws Exception {
		List<���ϳ���Item> res = new LinkedList<���ϳ���Item>();
		Statement st = DB.createStatement();
		ResultSet rs = st.executeQuery("SELECT * " + //
				"FROM ac_appoint_statement " + //
				"WHERE goods_id=" + id + ";");

		while (rs.next()) {
			res.add(���ϳ���Item.load(rs));
		}

		return res;
	}

	public static void removeForGoodsId(long id) throws Exception {
		Statement stmt = DB.createStatement();
		stmt.executeUpdate("DELETE " + //
				"FROM ac_appoint_statement " + //
				"WHERE goods_id=" + id + ";");

	}

	static ���ϳ���Item load(ResultSet rs) throws SQLException {
		int index = rs.getInt("no");
		Date date = rs.getDate("fixed_date");
		Date bid_start = rs.getDate("bid_start");
		Date bid_end = rs.getDate("bid_end");

		String type = rs.getString("type");
		String location = rs.getString("location");
		String lowest_price = rs.getString("lowest_price");
		String result = rs.getString("result");
		long godos_id = rs.getLong("goods_id");

		return new ���ϳ���Item(godos_id, index, date, type, location, lowest_price, result, bid_start, bid_end);
	}

}
