/*
 * 
 */
package net.narusas.si.auction.builder.present;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.fetchers.위지상;
import net.narusas.si.auction.fetchers.층형;
import net.narusas.si.auction.model.물건;
import net.narusas.util.lang.NFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class 부동산표시목록Builder.
 * @deprecated
 */
public class 부동산표시목록Builder {

	/** The bld match. */
	static List<Entry> bldMatch;

	/** The land match. */
	static List<Entry> landMatch;

	/** The lands. */
	static List<String> lands;

	/** The 전유부분 match. */
	static List<Entry> 전유부분Match;

	/** The goods_id. */
	private final int goods_id;

	/** The items. */
	private final List<부동산표시목록Item> items;

	/** The 물건. */
	private final 물건 물건;

	final Logger logger = LoggerFactory.getLogger("auction");

	static {
		loadLandWords();
		loadTypeMatches();
	}

	/**
	 * Instantiates a new 부동산표시목록 builder.
	 * 
	 * @param id
	 *            the id
	 * @param 물건
	 *            the 물건
	 * @param item
	 *            the item
	 */
	public 부동산표시목록Builder(int id, 물건 물건, List<부동산표시목록Item> item) {
		// super();
		this.goods_id = id;
		this.물건 = 물건;
		this.items = item;
	}

	/**
	 * Update.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void update() throws Exception {
		/**
		DB.dbConnect();

		boolean hasOldBuilding = 건물현황.has현황(goods_id);
		boolean hasOldLand = 토지현황.has현황(goods_id);
		boolean hadOldLandRight = 대지권현황.has현황(goods_id);

		// clearOld(goods_id);

		boolean hasBuilding = false;
		boolean hasLand = false;
		boolean hasBuildingRight = false;
		boolean hasLandRight = false;
		boolean has전유부분 = false;

		String garuantee_ratio = updateGuaranteeRatio();

		List<토지현황> lands = new LinkedList<토지현황>();
		List<건물현황> blds = new LinkedList<건물현황>();
		for (부동산표시목록Item item : items) {
			String detail = item.getDetail();
			// System.out.println("######################################");
			// System.out.println(detail);

			if (isStartLand(detail)) {
				hasLand = true;
				if (hasOldLand) {
					continue;
				}
				토지현황 land = addLand(item, detail);
				if (land != null) {
					lands.add(land);
				}
				if (detail.contains("지분")) {
					hasLandRight = true;
				}

			} else {
				if (hasOldBuilding) {
					continue;
				}
				List<건물현황> bld = addBuilding(detail, item);

				hasBuilding = true;
				if (bld != null || bld.size() == 0) {
					blds.addAll(bld);
				}
				if (has대지권(detail)) {
					addLangRight(item);
					hasLand = true;
				}
				if (detail.contains("지분")) {
					hasBuildingRight = true;
				}
				if (detail.contains("전유부분")) {
					has전유부분 = true;
				}
			}
		}

		try {
			updateGoods(hasBuilding, hasBuildingRight, hasLand, hasLandRight, has전유부분, 물건.get물건비고(), lands,
					blds, garuantee_ratio);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}

	/**
	 * Adds the building.
	 * 
	 * @param detail
	 *            the detail
	 * @param item
	 *            the item
	 * 
	 * @return the list<건물현황>
	 */
	private List<건물현황> addBuilding(String detail, 부동산표시목록Item item) {
		String floor = item.getAddress().addr4;
		String structure = null;
		String area = null;
		String 매각지분Comment = item.get매각지분();

		if (item.has전유부분건물표시()) {
			area = item.get전유부분().get면적();
			structure = item.get전유부분().get구조();
		} else {
			area = item.get대지면적();
			structure = item.get구조();
		}

		try {
			return insertBuilding(item, floor, structure, area, detail, 매각지분Comment);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Adds the land.
	 * 
	 * @param item
	 *            the item
	 * @param detail
	 *            the detail
	 * 
	 * @return the 토지현황
	 */
//	private 토지현황 addLand(부동산표시목록Item item, String detail) {
//		try {
//			String address = 현황주소Converter.convert(item.getAddress().toString());
//			String use = matchStartLand(detail);
//			String area = item.get대지면적();
//			String 매각지분 = item.get매각지분();
//			logger.info("Add Land address=" + address + ", use=" + use + ", area=" + area);
//			토지현황 st = new 토지현황(goods_id, address, use, area, 매각지분);
//			st.insert();
//			return st;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//
//	}

	/**
	 * Adds the lang right.
	 * 
	 * @param item
	 *            the item
	 * 
	 * @throws Exception
	 *             the exception
	 */
	private void addLangRight(부동산표시목록Item item) throws Exception {
		대지권현황 right = item.get대지권();
		logger.info("Add Land Right items size =" + right.getItems().size());
		right.insertFor물건(goods_id, item.get매각지분());
	}

	/**
	 * Calc presentation.
	 * 
	 * @param hasBuilding
	 *            the has building
	 * @param hasBuildingRight
	 *            the has building right
	 * @param hasLand
	 *            the has land
	 * @param hasLandRight
	 *            the has land right
	 * @param comment
	 *            the comment
	 * 
	 * @return the string
	 */
	private String calcPresentation(boolean hasBuilding, boolean hasBuildingRight, boolean hasLand,
			boolean hasLandRight, String comment) {
		String presentation = "";
		if (hasBuilding) {
			if (hasLand) {
				if (hasBuildingRight) {
					if (hasLandRight) {
						presentation = "토지 및 건물지분매각";
					} else {
						presentation = "토지전체  및 건물지분매각";
					}
				} else {
					if (hasLandRight) {
						presentation = "건물전체 및 토지지분매각";
					} else {
						presentation = "토지  및 건물일괄매각";
					}
				}
			} else {
				if (hasBuildingRight) {
					presentation = "토지제외 및 건물지분매각";
				} else {
					presentation = "토지제외 및 건물매각";
				}
			}
		} else {
			if (hasLandRight) {
				if (is건물제외비고(comment)) {
					presentation = "건물제외 및 토지지분매각";
				} else {
					presentation = "토지지분매각";
				}

			} else {
				if (is건물제외비고(comment)) {
					presentation = "건물제외 및 토지매각";
				} else {
					presentation = "토지매각";
				}

			}
		}
		return presentation;
	}

	/**
	 * Calc type.
	 * 
	 * @param hasBuilding
	 *            the has building
	 * @param has전유부분
	 *            the has전유부분
	 * @param lands
	 *            the lands
	 * @param blds
	 *            the blds
	 * @param type
	 *            the type
	 * 
	 * @return the int
	 */
//	private int calcType(boolean hasBuilding, boolean has전유부분, List<토지현황> lands, List<건물현황> blds, int type) {
//		List<Integer> types = new LinkedList<Integer>();
//		if (hasBuilding) {
//			if (has전유부분) {
//				for (건물현황 bld : blds) {
//					List<Entry> target = 전유부분Match;
//					type = iterateTypes(type, bld, target);
//					types.add(type);
//				}
//				return findHighestType(types, 전유부분Match);
//			} else {
//				for (건물현황 bld : blds) {
//					List<Entry> target = bldMatch;
//					type = iterateTypes(type, bld, target);
//					types.add(type);
//				}
//				return findHighestType(types, bldMatch);
//			}
//
//		} else {
//			for (토지현황 land : lands) {
//				Iterator<Entry> entries = landMatch.iterator();
//				while (entries.hasNext()) {
//					Entry entry = entries.next();
//					if (land.isTypeMatch(entry.getKey())) {
//						type = entry.getValue();
//						types.add(type);
//						break;
//					}
//				}
//			}
//			return findHighestType(types, landMatch);
//		}
//
//	}

	/**
	 * Clear old.
	 * 
	 * @param id
	 *            the id
	 */
//	private void clearOld(int id) {
//		try {
//			토지현황.clear토지현황For물건(id);
//			건물현황.clear건물현황For물건(id);
//			대지권현황.clear대지권현황For물건(id);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Find highest type.
	 * 
	 * @param types
	 *            the types
	 * @param matches
	 *            the matches
	 * 
	 * @return the int
	 */
	private int findHighestType(List<Integer> types, List<Entry> matches) {
		for (int i = 0; i < matches.size(); i++) {
			Entry e = matches.get(i);
			if (e.matchAnything(types)) {
				return e.getValue();
			}
		}
		return 410;
	}

	/**
	 * Find item by address.
	 * 
	 * @param 주소
	 *            the 주소
	 * 
	 * @return the 부동산표시목록 item
	 */
	private 부동산표시목록Item findItemByAddress(String 주소) {
		for (부동산표시목록Item item : items) {
			if (주소.contains(item.address.addr4)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * First word.
	 * 
	 * @param detail
	 *            the detail
	 * 
	 * @return the string
	 */
	private String firstWord(String detail) {
		String[] lines = detail.split("\n");
		String[] words = lines[0].split(" ");
		return words[0];
	}

	/**
	 * Has대지권.
	 * 
	 * @param detail
	 *            the detail
	 * 
	 * @return true, if successful
	 */
	private boolean has대지권(String detail) {
		return detail.contains("대지권의 목적인 토지의 표시") || detail.contains("대지권의 표시");
	}

	/**
	 * Insert building.
	 * 
	 * @param item
	 *            the item
	 * @param floor
	 *            the floor
	 * @param structure
	 *            the structure
	 * @param area
	 *            the area
	 * @param detail
	 *            the detail
	 * @param 매각지분comment
	 *            the 매각지분comment
	 * 
	 * @return the list<건물현황>
	 * 
	 * @throws Exception
	 *             the exception
	 * @throws SQLException
	 *             the SQL exception
	 */
	private List<건물현황> insertBuilding(부동산표시목록Item item, String floor, String structure, String area,
			String detail, String 매각지분comment) throws Exception, SQLException {
		List<건물현황> result = new LinkedList<건물현황>();
		if (item.get위지상() != null) {
			위지상 위지상 = item.get위지상();
			List<층형> floors = 위지상.getFloors();
			logger.info("추가할 위 지상 건물의 수는 " + floors.size());
			for (층형 f : floors) {
				logger.info("insert 위 지상 building floor=" + f.getText() + " str=" + structure + " area="
						+ f.getArea());
				건물현황 st = new 건물현황(goods_id,// 
						item.getAddress().toString(),// 
						f.getText(),// 
						"", //
						f.getArea(),// 
						"위지상", //
						detail, //
						매각지분comment);
				st.insert();
				result.add(st);
			}
		} else {
//			건물현황 st = new 건물현황(goods_id,// 
//					item.getAddress().toString(),//
//					건물현황_층형Converter.convert(floor),//
//					건물현황_구조Converter.convert(structure), //
//					현황주소Converter.convert(area),//
//					detail,// 
//					매각지분comment);
//			logger.info("insert building "// 
//					+ "address=" + item.getAddress().toString()//
//					+ "floor=" + 건물현황_층형Converter.convert(floor)//
//					+ " str=" + 건물현황_구조Converter.convert(structure)//
//					+ " area=" + 현황주소Converter.convert(area)//
//					+ " detail=" + detail//
//					+ " comment=" + 매각지분comment//
//			);
//
//			st.insert();
//			result.add(st);
		}

		return result;
	}

	/**
	 * Checks if is start land.
	 * 
	 * @param detail
	 *            the detail
	 * 
	 * @return true, if is start land
	 */
	private boolean isStartLand(String detail) {
		for (String land : lands) {
			if (land.equals(firstWord(detail))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if is 건물제외비고.
	 * 
	 * @param comment
	 *            the comment
	 * 
	 * @return true, if is 건물제외비고
	 */
	private boolean is건물제외비고(String comment) {
		// 매각에서 제외되는~,법정지상권,제시외건물,건물,지상에
		return comment != null
				&& (comment.contains("매각에서 제외되는") || comment.contains("법정지상권") || comment.contains("제시외건물")
						|| comment.contains("건물") || comment.contains("지상에"));
	}

	/**
	 * Iterate types.
	 * 
	 * @param type
	 *            the type
	 * @param bld
	 *            the bld
	 * @param target
	 *            the target
	 * 
	 * @return the int
	 */
	private int iterateTypes(int type, 건물현황 bld, List<Entry> target) {
		Iterator<Entry> entries = target.iterator();
		while (entries.hasNext()) {
			Entry entry = entries.next();
			if (bld.match(entry.getKey())) {
				type = entry.getValue();
				break;
			}

		}
		return type;
	}

	/**
	 * Match start land.
	 * 
	 * @param detail
	 *            the detail
	 * 
	 * @return the string
	 */
	private String matchStartLand(String detail) {
		for (String land : lands) {
			if (detail.startsWith(land)) {
				return land;
			}
		}
		return null;
	}

	/**
	 * Update goods.
	 * 
	 * @param hasBuilding
	 *            the has building
	 * @param hasBuildingRight
	 *            the has building right
	 * @param hasLand
	 *            the has land
	 * @param hasLandRight
	 *            the has land right
	 * @param has전유부분
	 *            the has전유부분
	 * @param comment
	 *            the comment
	 * @param lands
	 *            the lands
	 * @param blds
	 *            the blds
	 * @param garuantee_ratio
	 *            the garuantee_ratio
	 * 
	 * @throws Exception
	 *             the exception
	 * @throws SQLException
	 *             the SQL exception
	 */
	/**
	private void updateGoods(boolean hasBuilding, boolean hasBuildingRight, boolean hasLand,
			boolean hasLandRight, boolean has전유부분, String comment, List<토지현황> lands, List<건물현황> blds,
			String garuantee_ratio) throws Exception, SQLException {

		int type = 410;
		type = calcType(hasBuilding, has전유부분, lands, blds, type);

		String presentation = "";
		presentation = calcPresentation(hasBuilding, hasBuildingRight, hasLand, hasLandRight, comment);

		if (garuantee_ratio == null) {
			garuantee_ratio = "10%";
		}

		Statement queryStmt = DB.createStatement();
		ResultSet rs = queryStmt.executeQuery("SELECT type_code, sell_target FROM ac_goods WHERE id="
				+ goods_id + ";");
		rs.next();
		int oldTypeCode = rs.getInt("type_code");
		String oldSellTarget = rs.getString("sell_target");
		DB.cleanup(rs);

		if (oldTypeCode != 0) {
			logger.info("Goods have old type code. use old type code " + oldTypeCode + " instead of " + type);
			type = oldTypeCode;
		}

		if (oldSellTarget != null && "".equals(oldSellTarget) == false) {
			logger.info("Goods have old Sell target. use old sell target" + oldSellTarget + " instead of "
					+ presentation);
			presentation = oldSellTarget;
		}

		logger.info("update goods type_code =" + type + " comment=" + comment + " guarantee_ratio"
				+ garuantee_ratio);
		PreparedStatement stmt = DB
				.prepareStatement("UPDATE ac_goods SET comment=?, type_code=?, sell_target=?, guarantee_ratio=? WHERE id=?;");
		stmt.setString(1, comment);
		stmt.setInt(2, type);
		stmt.setString(3, presentation);
		stmt.setInt(4, Integer.parseInt(garuantee_ratio));
		stmt.setLong(5, goods_id);
		stmt.execute();
	}
*/
	/**
	 * Update guarantee ratio.
	 * 
	 * @return the string
	 */
	private String updateGuaranteeRatio() {
		String comment = 물건.get물건비고();
		String garuantee_ratio = "10";
		// 재매각, 보증금, 특별매각
		if (comment != null
				&& (comment.contains("재매각") || comment.contains("보증금") || comment.contains("특별매각") || comment.contains("매수신청보증액")) ) {
			Pattern p = Pattern.compile("(\\d+)%");
			Matcher m = p.matcher(comment);
			if (m.find()) {
				garuantee_ratio = m.group(1) + "";
			} else {
				p = Pattern.compile("(\\d+)할");
				m = p.matcher(comment);
				if (m.find()) {
					garuantee_ratio = m.group(1) + "0";
				}
			}
		}
		return garuantee_ratio;
	}

	/**
	 * Load land words.
	 */
	private static void loadLandWords() {
		lands = new LinkedList<String>();
		try {
			String text = NFile.getText(new File("cfg/land.txt"),"euc-kr");
			String[] values = text.split(",");
			for (String v : values) {
				if ("".equals(v.trim())) {
					continue;
				}
				lands.add(v.trim());
			}
		} catch (IOException e) {
			// logger.log(Level.WARNING, e.getMessage(), e);
		}
	}

	/**
	 * Load type matches.
	 */
	private static void loadTypeMatches() {
		landMatch = new LinkedList<Entry>();
		bldMatch = new LinkedList<Entry>();
		전유부분Match = new LinkedList<Entry>();
		File f = new File("cfg/type_match.txt");
		List<Entry> target = landMatch;
		try {
			String text = NFile.getText(f, "euc-kr");
			String[] lines = text.split("\n");
			for (String line : lines) {
				if (line.startsWith("----1")) {
					target = bldMatch;
					continue;
				}
				if (line.startsWith("----2")) {
					target = 전유부분Match;
					continue;
				}
				String[] tokens = line.split(",");
				if (tokens.length == 2) {
					target.add(new Entry(tokens[0], Integer.parseInt(tokens[1].trim())));
				} else {
					String[] key = new String[tokens.length - 1];
					System.arraycopy(tokens, 0, key, 0, tokens.length - 1);
					target.add(new Entry(key, Integer.parseInt(tokens[tokens.length - 1].trim())));
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class Entry {
	String[] text;

	int value;

	public Entry(String text, int value) {
		this.text = new String[] { text };
		this.value = value;
	}

	public Entry(String[] text, int value) {
		this.text = text;
		this.value = value;
	}

	public String[] getKey() {
		return text;
	}

	public int getValue() {
		return value;
	}

	public boolean isMatchKey(String t) {
		for (String key : text) {
			if (key.equals(t)) {
				return true;
			}
		}
		return false;
	}

	public boolean matchAnything(List<Integer> types) {
		for (Integer integer : types) {
			int v = integer.intValue();
			if (v == value) {
				return true;
			}
		}
		return false;
	}
}
