/*
 * 
 */
package net.narusas.si.auction.builder.present;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class 대지권현황.
 * @deprecated
 */
public class 대지권현황 {

	/** The address. */
	private final String address;

	/** The items. */
	private List<대지권Item> items;

	/** The text. */
	private final String text;

	/** The logger. */
	Logger logger = Logger.getLogger("log");

	/**
	 * Instantiates a new 대지권현황.
	 * 
	 * @param text
	 *            the text
	 * @param address
	 *            the address
	 */
	public 대지권현황(String text, String address) {
		this.text = text;
		this.address = address;
		this.items = parse(text);
	}

	/**
	 * Gets the items.
	 * 
	 * @return the items
	 */
	public List<대지권Item> getItems() {
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
		for (대지권Item item : items) {
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
			return items.get(0).getRatioA() + " 분의 " + items.get(0).getRatioB();
		}
		double a = 0;
		double b = 0;

		for (대지권Item item : items) {
			a += item.getRatioA();
			b += item.getRatioB();
		}
		return toNumberString(a) + " 분의 " + toNumberString(b);
	}

	/**
	 * Gets the 면적.
	 * 
	 * @return the 면적
	 */
	public double get면적() {
		double result = 0;
		for (대지권Item item : items) {
			result += item.get면적();
		}
		return result * getRatio();
	}

	/**
	 * Insert for물건.
	 * 
	 * @param id
	 *            the id
	 * @param 매각지분comment
	 *            the 매각지분comment
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void insertFor물건(long id, String 매각지분comment) throws Exception {
//		logger.info("insert land right item address=" + 현황주소Converter.convert(address) + ", ratio="
//				+ getRatio() + ",area=" + get면적() + ",goods_id" + id);
//		PreparedStatement stmt = null;
//		try {
//			stmt = DB.prepareStatement("INSERT INTO ac_land_right_statement " + //
//					"(" // +
//					+ "no," // 1
//					+ "address," // 2
//					+ "right_ratio," // 3
//					+ "area," // 4
//					+ "goods_id,"// 5
//					+ "sell_comment"// 5
//					+ ") " //
//					+ "VALUES (?,?,?,?,?,?);");
//			stmt.setInt(1, 0);
//			stmt.setString(2, 현황주소Converter.convert(address));
//			stmt.setString(3, getRatioString());
//			stmt.setString(4, toNumberString(get면적()));
//			stmt.setLong(5, id);
//			stmt.setString(6, 매각지분comment);
//			stmt.execute();
//		} finally {
//			DB.cleanup(stmt);
//		}

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
	 * @param text
	 *            the text
	 * 
	 * @return the list<대지권 item>
	 */
	private List<대지권Item> parse(String text) {
		List<대지권Item> result = new LinkedList<대지권Item>();
		Map<Integer, 대지권Item> temp = new HashMap<Integer, 대지권Item>();
		String[] lines = text.split("\n");
		Pattern p = Pattern.compile("([\\d]+[,\\d\\s]*).(.*)$");
		int type = 0;
		for (int i = 1; i < lines.length; i++) {
			String line = lines[i];
			String hubgryLine = line.replaceAll(" ","");
			if (hubgryLine.contains("토지의표시:")) {
				type = 1;
				line = line.substring(line.indexOf(":") + 1);
			} else if (hubgryLine.contains("대지권의종류:")) {
				type = 2;
				line = line.substring(line.indexOf(":") + 1);
			} else if (hubgryLine.contains("대지권의비율:")) {
				type = 3;
				line = line.substring(line.indexOf(":") +1);
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
						대지권Item item = new 대지권Item(no, address, area);
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
						대지권Item item = temp.get(no);
						if (item != null) {
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
						대지권Item item = temp.get(no);
						if (item != null) {
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
	 * @param value
	 *            the value
	 * 
	 * @return the string
	 */
	private String toNumberString(double value) {
		int v = (int) (value * 1000);
		double v2 = ((double) v) / 1000;
		return String.valueOf(v2);
	}

	/**
	 * Clear대지권현황 for물건.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static void clear대지권현황For물건(long id) throws Exception {
//		Statement stmt = DB.createStatement();
//		stmt.executeUpdate("DELETE FROM ac_land_right_statement WHERE goods_id=" + id + ";");
	}

	/**
	 * Has현황.
	 * 
	 * @param goods_id
	 *            the goods_id
	 * 
	 * @return true, if successful
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static boolean has현황(int goods_id) throws Exception {
//		Statement stmt = DB.createStatement();
//		ResultSet rs = stmt.executeQuery("SELECT * FROM ac_land_right_statement WHERE goods_id=" + goods_id
//				+ ";");
//		boolean has = rs.next();
//		DB.cleanup(rs, stmt);
//		return has;
		return false;
	}

	@Override
	public String toString() {
		return "대지권현황=" + items;
	}

}
