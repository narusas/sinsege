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
 * The Class ������Ȳ.
 */
public class ������Ȳ {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The ����pattern. */
	static Pattern ����pattern = Pattern
			.compile("(\\d+\\s*��)", Pattern.MULTILINE);

	/** The ���� pattern. */
	static Pattern ����Pattern = Pattern.compile("\\(([^\\)]*����[^\\)]*)\\)");

	/** The detail. */
	private String detail;

	/** The goods id. */
	private long goodsId;

	/** The no. */
	private int no;

	/** The ���. */
	private String ���;
	
	/** The �ּ�. */
	private final String �ּ�;

	/**
	 * Instantiates a new ������Ȳ.
	 * 
	 * @param goodsId the goods id
	 * @param no the no
	 * @param address the address
	 * @param comment the comment
	 */
	public ������Ȳ(long goodsId, int no, String address, String comment) {
		this.goodsId = goodsId;
		this.no = no;
		�ּ� = address;
		��� = comment;
	}

	/**
	 * Instantiates a new ������Ȳ.
	 * 
	 * @param �ּ� the �ּ�
	 */
	public ������Ȳ(String �ּ�) {
		this.�ּ� = �ּ�.trim();
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
	 * Gets the �ǹ����.
	 * 
	 * @return the �ǹ����
	 */
	public String get�ǹ����() {
		return ���;
	}

	/**
	 * Gets the ����.
	 * 
	 * @return the ����
	 */
	public String get����() {
		return calc����();
	}

	/**
	 * Gets the �ּ�.
	 * 
	 * @return the �ּ�
	 */
	public String get�ּ�() {
		return �ּ�;
	}

	/**
	 * Insert.
	 * 
	 * @throws Exception the exception
	 */
	public void insert() throws Exception {
		logger.info("������Ȳ�� DB�� �߰��մϴ�. no=" + no + ",�ּ�=" + �ּ� + ",���=" + ���);
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
		stmt.setString(3, �ּ�);
		stmt.setString(4, ���);
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
	 * Sets the �ǹ����.
	 * 
	 * @param �ǹ���� the new �ǹ����
	 */
	public void set�ǹ����(String �ǹ����) {
		this.��� = �ǹ����;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "������Ȳ{�ּ�=[" + �ּ� + "], ��=[" + detail + "]}";
	}

	/**
	 * Calc����.
	 * 
	 * @return the string
	 */
	private String calc����() {
		Matcher m = ����pattern.matcher(detail);
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

		Matcher m = ����Pattern.matcher(detail);
		if (m.find()) {
			��� = m.group(1);
		}
	}

	/**
	 * ������ ��ȣ�� ������ ������Ȳ�� ��Ͽ� �ִ��� ���θ� ��ȯ�Ѵ�.
	 * 
	 * @param ��Ȳs �˻����
	 * @param targetNo ������ ��ȣ
	 * 
	 * @return ���� ����.
	 */
	public static boolean exitIn(List<������Ȳ> ��Ȳs, int targetNo) {
		for (������Ȳ status : ��Ȳs) {
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
	 * @return the list<������Ȳ>
	 * 
	 * @throws Exception the exception
	 */
	public static List<������Ȳ> findByGoodsId(long goodsId) throws Exception {
		LinkedList<������Ȳ> result = new LinkedList<������Ȳ>();
		Statement stmt = DB.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * " + //
				"FROM ac_goods_building " + //
				"WHERE goods_id=" + goodsId + ";");
		while (rs.next()) {
			result.add(������Ȳ.load(rs));
		}
		return result;
	}

	/**
	 * Load.
	 * 
	 * @param rs the rs
	 * 
	 * @return the ������Ȳ
	 * 
	 * @throws SQLException the SQL exception
	 */
	public static ������Ȳ load(ResultSet rs) throws SQLException {
		long goodsId = rs.getLong("goods_id");
		int no = rs.getInt("no");
		String address = rs.getString("address");
		String comment = rs.getString("comment");
		return new ������Ȳ(goodsId, no, address, comment);
	}
}
