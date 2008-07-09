/*
 * 
 */
package net.narusas.aceauction.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.data.��Ȳ�ּ�Converter;

// TODO: Auto-generated Javadoc
/**
 * The Class ��������Ȳ.
 */
public class ��������Ȳ {

	/** The address. */
	private final String address;
	
	/** The items. */
	private List<������Item> items;

	/** The text. */
	private final String text;
	
	/** The logger. */
	Logger logger = Logger.getLogger("log");

	/**
	 * Instantiates a new ��������Ȳ.
	 * 
	 * @param text the text
	 * @param address the address
	 */
	public ��������Ȳ(String text, String address) {
		this.text = text;
		this.address = address;
		this.items = parse(text);
	}

	/**
	 * Gets the items.
	 * 
	 * @return the items
	 */
	public List<������Item> getItems() {
		return items;
	}

	/**
	 * Gets the ratio.
	 * 
	 * @return the ratio
	 */
	public double getRatio() {
		if (allSameRatio()) {
			return items.get(0).getRatio();
		}
		double result = 0;
		double a = 0;
		double b = 0;
		for (������Item item : items) {
			// result += item.getRatio();
			a += item.getRatioA();
			b += item.getRatioB();
		}
		result = b / a;
		return result;
	}

	/**
	 * Gets the ratio string.
	 * 
	 * @return the ratio string
	 */
	public String getRatioString() {
		if (allSameRatio()) {
			return items.get(0).getRatioA() + " ���� " + items.get(0).getRatioB();
		}
		double a = 0;
		double b = 0;

		for (������Item item : items) {
			a += item.getRatioA();
			b += item.getRatioB();
		}
		return toNumberString(a) + " ���� " + toNumberString(b);
	}

	/**
	 * Gets the ����.
	 * 
	 * @return the ����
	 */
	public double get����() {
		double result = 0;
		for (������Item item : items) {
			result += item.get����();
		}
		return result * getRatio();
	}

	/**
	 * Insert for����.
	 * 
	 * @param id the id
	 * @param �Ű�����comment the �Ű�����comment
	 * 
	 * @throws Exception the exception
	 */
	public void insertFor����(long id, String �Ű�����comment) throws Exception {
		logger.info("insert land right item address="
				+ ��Ȳ�ּ�Converter.convert(address) + ", ratio=" + getRatio()
				+ ",area=" + get����() + ",goods_id" + id);
		PreparedStatement stmt = null;
		try {
			stmt = DB.prepareStatement("INSERT INTO ac_land_right_statement " + //
					"(" // +
					+ "no," // 1
					+ "address," // 2
					+ "right_ratio," // 3
					+ "area," // 4
					+ "goods_id,"// 5
					+ "sell_comment"// 5
					+ ") " //
					+ "VALUES (?,?,?,?,?,?);");
			stmt.setInt(1, 0);
			stmt.setString(2, ��Ȳ�ּ�Converter.convert(address));
			stmt.setString(3, getRatioString());
			stmt.setString(4, toNumberString(get����()));
			stmt.setLong(5, id);
			stmt.setString(6, �Ű�����comment);
			stmt.execute();
		} finally {
			DB.cleanup(stmt);
		}

	}

	/**
	 * All same ratio.
	 * 
	 * @return true, if successful
	 */
	private boolean allSameRatio() {
		double result = items.get(0).getRatio();
		for (int i = 1; i < items.size(); i++) {
			if (result != items.get(i).getRatio()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Parses the.
	 * 
	 * @param text the text
	 * 
	 * @return the list<������ item>
	 */
	private List<������Item> parse(String text) {
		List<������Item> result = new LinkedList<������Item>();
		Map<Integer, ������Item> temp = new HashMap<Integer, ������Item>();
		String[] lines = text.split("\n");
		Pattern p = Pattern.compile("([\\d]+[,\\d\\s]*).(.*)$");
		int type = 0;
		for (int i = 1; i < lines.length; i++) {
			String line = lines[i];
			if (line.contains("���� �� ǥ�� :")) {
				type = 1;
				line = line.substring(line.indexOf("���� �� ǥ�� :")
						+ "���� �� ǥ�� :".length());
			} else if (line.contains("������������ :")) {
				type = 2;
				line = line.substring(line.indexOf("������������ :")
						+ "������������ :".length());
			} else if (line.contains("�������Ǻ��� :")) {
				type = 3;
				line = line.substring(line.indexOf("�������Ǻ��� :")
						+ "�������Ǻ��� :".length());
			}
			Matcher m = p.matcher(line);
			if (m.find()) {
				switch (type) {
				case 1: {
					String noString = m.group(1);
					String[] numbers = noString.split(",");
					for (String number : numbers) {
						int no = Integer.parseInt(number.trim());
						String address = m.group(2).trim();
						String area = lines[i + 1].trim();
						i++;
						������Item item = new ������Item(no, address, area);
						result.add(item);
						temp.put(no, item);
					}

				}
					break;
				case 2: {
					String noString = m.group(1);
					String[] numbers = noString.split(",");
					for (String number : numbers) {
						int no = Integer.parseInt(number.trim());
						String landType = m.group(2).trim();
						������Item item = temp.get(no);
						if(item != null){
							item.setType(landType);	
						}
						
					}

				}
					break;

				case 3: {
					String noString = m.group(1);
					String[] numbers = noString.split(",");
					for (String number : numbers) {
						int no = Integer.parseInt(number.trim());
						String ratio = m.group(2).trim();
						������Item item = temp.get(no);
						if (item!=null){
							item.setRatio(ratio);	
						}
						
						
					}

				}
					break;

				}
			}

		}

		return result;
	}

	/**
	 * To number string.
	 * 
	 * @param value the value
	 * 
	 * @return the string
	 */
	private String toNumberString(double value) {
		int v = (int) (value * 1000);
		double v2 = ((double) v) / 1000;
		return String.valueOf(v2);
	}

	/**
	 * Clear��������Ȳ for����.
	 * 
	 * @param id the id
	 * 
	 * @throws Exception the exception
	 */
	public static void clear��������ȲFor����(long id) throws Exception {
		Statement stmt = DB.createStatement();
		stmt
				.executeUpdate("DELETE FROM ac_land_right_statement WHERE goods_id="
						+ id + ";");
	}

	/**
	 * Has��Ȳ.
	 * 
	 * @param goods_id the goods_id
	 * 
	 * @return true, if successful
	 * 
	 * @throws Exception the exception
	 */
	public static boolean has��Ȳ(int goods_id) throws Exception {
		Statement stmt = DB.createStatement();
		ResultSet rs = stmt
				.executeQuery("SELECT * FROM ac_land_right_statement WHERE goods_id="
						+ goods_id + ";");
		boolean has = rs.next();
		DB.cleanup(rs, stmt);
		return has;
	}

}
