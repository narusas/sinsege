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
import net.narusas.aceauction.data.�ݾ�Converter;

// TODO: Auto-generated Javadoc
/**
 * The Class ���ϳ���Item.
 */
public class ���ϳ���Item {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The date. */
	private Date date;

	/** The goods_id. */
	private long goods_id;

	/** The ����. */
	private String ����;

	/** The ���ϰ��. */
	private String ���ϰ��;

	/** The �������. */
	private String �������;

	/** The ��������. */
	private String ��������;

	/** The ���ǹ�ȣ. */
	private String ���ǹ�ȣ;
	
	/** The �ð�. */
	private String �ð�;

	/** The �����Ű�����. */
	private String �����Ű�����;

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
		return "���ϳ���Item={Date=" + date + ", goods_id=" + goods_id + ",����=" + ���� + ",���ϰ��=" + ���ϰ�� + ",�������=" + �������
				+ ",��������=" + �������� + ",���ǹ�ȣ=" + ���ǹ�ȣ + ",�ð�=" + �ð� + ",�����Ű�����=" + �����Ű����� + ",��������=" + bid_start
				+ ", ��������=" + bid_end + ",index=" + index + "}";
	}

	/**
	 * Instantiates a new ���ϳ��� item.
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

	/**
	 * Instantiates a new ���ϳ��� item.
	 * 
	 * @param record the record
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		���ϳ���Item item = (���ϳ���Item) arg0;
		return ���ǹ�ȣ.equals(item.���ǹ�ȣ) && ����.equals(item.����);
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
	 * Gets the ����.
	 * 
	 * @return the ����
	 */
	public String get����() {
		return ���� != null ? ���� : DB.toString(date);
	}

	/**
	 * Gets the ���ϰ��.
	 * 
	 * @return the ���ϰ��
	 */
	public String get���ϰ��() {
		return ���ϰ��;
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
	public String get��������() {
		return ��������;
	}

	/**
	 * Gets the ���ǹ�ȣ.
	 * 
	 * @return the ���ǹ�ȣ
	 */
	public String get���ǹ�ȣ() {
		return ���ǹ�ȣ;
	}

	/**
	 * Gets the �ð�.
	 * 
	 * @return the �ð�
	 */
	public String get�ð�() {
		return �ð�;
	}

	/**
	 * Gets the �����Ű�����.
	 * 
	 * @return the �����Ű�����
	 */
	public String get�����Ű�����() {
		return �����Ű�����;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ���ǹ�ȣ.hashCode() + ����.hashCode();
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

	/**
	 * Gets the �Ⱓ����_����.
	 * 
	 * @return the �Ⱓ����_����
	 */
	public Date get�Ⱓ����_����() {
		return bid_start;
	}

	/**
	 * Gets the �Ⱓ����_����.
	 * 
	 * @return the �Ⱓ����_����
	 */
	public Date get�Ⱓ����_����() {
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
	 * Sets the ����.
	 * 
	 * @param ���� the new ����
	 */
	public void set����(String ����) {
		this.���� = ����;
	}

	/**
	 * Sets the ���ϰ��.
	 * 
	 * @param ���ϰ�� the new ���ϰ��
	 */
	public void set���ϰ��(String ���ϰ��) {
		this.���ϰ�� = ���ϰ��;
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
		this.�������� = ��������;
	}

	/**
	 * Sets the ���ǹ�ȣ.
	 * 
	 * @param ���ǹ�ȣ the new ���ǹ�ȣ
	 */
	public void set���ǹ�ȣ(String ���ǹ�ȣ) {
		this.���ǹ�ȣ = ���ǹ�ȣ;
	}

	/**
	 * Sets the �ð�.
	 * 
	 * @param �ð� the new �ð�
	 */
	public void set�ð�(String �ð�) {
		this.�ð� = �ð�;
	}

	/**
	 * Sets the �����Ű�����.
	 * 
	 * @param �����Ű����� the new �����Ű�����
	 */
	public void set�����Ű�����(String �����Ű�����) {
		this.�����Ű����� = �����Ű�����;
	}

	/**
	 * Find by goods id.
	 * 
	 * @param id the id
	 * 
	 * @return the list<���ϳ��� item>
	 * 
	 * @throws Exception the exception
	 */
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
	 * @return the ���ϳ��� item
	 * 
	 * @throws SQLException the SQL exception
	 */
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
