/*
 * 
 */
package net.narusas.aceauction.data.builder;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.data.�ǹ���Ȳ_����Converter;
import net.narusas.aceauction.data.�ǹ���Ȳ_����Converter;
import net.narusas.aceauction.data.��Ȳ�ּ�Converter;
import net.narusas.aceauction.model.�ǹ���Ȳ;
import net.narusas.aceauction.model.��������Ȳ;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.�ε���ǥ�ø��Item;
import net.narusas.aceauction.model.������;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.������Ȳ;
import net.narusas.util.lang.NFile;

// TODO: Auto-generated Javadoc
/**
 * The Class �ε���ǥ�ø��Builder.
 */
public class �ε���ǥ�ø��Builder {

	/** The bld match. */
	static List<Entry> bldMatch;
	
	/** The land match. */
	static List<Entry> landMatch;
	
	/** The lands. */
	static List<String> lands;

	/** The �����κ� match. */
	static List<Entry> �����κ�Match;

	/** The goods_id. */
	private final int goods_id;
	
	/** The items. */
	private final List<�ε���ǥ�ø��Item> items;
	
	/** The ����. */
	private final ���� ����;
	
	/** The logger. */
	Logger logger = Logger.getLogger("log");

	static {
		loadLandWords();
		loadTypeMatches();
	}

	/**
	 * Instantiates a new �ε���ǥ�ø�� builder.
	 * 
	 * @param id the id
	 * @param ���� the ����
	 * @param item the item
	 */
	public �ε���ǥ�ø��Builder(int id, ���� ����, List<�ε���ǥ�ø��Item> item) {
		// super();
		this.goods_id = id;
		this.���� = ����;
		this.items = item;
	}

	/**
	 * Update.
	 * 
	 * @throws Exception the exception
	 */
	public void update() throws Exception {
		DB.dbConnect();

		logger.entering("�ε���ǥ�ø��DB", "update");
		logger.log(Level.INFO, "ID=" + goods_id);

		boolean hasOldBuilding = �ǹ���Ȳ.has��Ȳ(goods_id);
		boolean hasOldLand = ������Ȳ.has��Ȳ(goods_id);
		boolean hadOldLandRight = ��������Ȳ.has��Ȳ(goods_id);

		// clearOld(goods_id);

		boolean hasBuilding = false;
		boolean hasLand = false;
		boolean hasBuildingRight = false;
		boolean hasLandRight = false;
		boolean has�����κ� = false;

		String garuantee_ratio = updateGuaranteeRatio();

		List<������Ȳ> lands = new LinkedList<������Ȳ>();
		List<�ǹ���Ȳ> blds = new LinkedList<�ǹ���Ȳ>();
		for (�ε���ǥ�ø��Item item : items) {
			String detail = item.getDetail();
			// System.out.println("######################################");
			// System.out.println(detail);

			if (isStartLand(detail)) {
				hasLand = true;
				if (hasOldLand) {
					continue;
				}
				������Ȳ land = addLand(item, detail);
				if (land != null) {
					lands.add(land);
				}
				if (detail.contains("����")) {
					hasLandRight = true;
				}

			} else {
				if (hasOldBuilding) {
					continue;
				}
				List<�ǹ���Ȳ> bld = addBuilding(detail, item);

				hasBuilding = true;
				if (bld != null || bld.size() == 0) {
					blds.addAll(bld);
				}
				if (has������(detail)) {
					addLangRight(item);
					hasLand = true;
				}
				if (detail.contains("����")) {
					hasBuildingRight = true;
				}
				if (detail.contains("�����κ�")) {
					has�����κ� = true;
				}
			}
		}

		try {
			updateGoods(hasBuilding, hasBuildingRight, hasLand, hasLandRight, has�����κ�, ����.���, lands, blds,
					garuantee_ratio);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Exception in updateGoods", e);
			e.printStackTrace();
		}
	}

	/**
	 * Adds the building.
	 * 
	 * @param detail the detail
	 * @param item the item
	 * 
	 * @return the list<�ǹ���Ȳ>
	 */
	private List<�ǹ���Ȳ> addBuilding(String detail, �ε���ǥ�ø��Item item) {
		String floor = item.getAddress().addr4;
		String structure = null;
		String area = null;
		String �Ű�����Comment = item.get�Ű�����();

		if (item.has�����κаǹ�ǥ��()) {
			area = item.get�����κ�().get����();
			structure = item.get�����κ�().get����();
		} else {
			area = item.get��������();
			structure = item.get����();
		}

		try {
			return insertBuilding(item, floor, structure, area, detail, �Ű�����Comment);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Exception in insertBuilding", e);
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Adds the land.
	 * 
	 * @param item the item
	 * @param detail the detail
	 * 
	 * @return the ������Ȳ
	 */
	private ������Ȳ addLand(�ε���ǥ�ø��Item item, String detail) {
		try {
			String address = ��Ȳ�ּ�Converter.convert(item.getAddress().toString());
			String use = matchStartLand(detail);
			String area = item.get��������();
			String �Ű����� = item.get�Ű�����();
			logger.log(Level.INFO, "Add Land address=" + address + ", use=" + use + ", area=" + area);
			������Ȳ st = new ������Ȳ(goods_id, address, use, area, �Ű�����);
			st.insert();
			return st;
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.WARNING, "Exception in addLand", e);
			return null;
		}

	}

	/**
	 * Adds the lang right.
	 * 
	 * @param item the item
	 * 
	 * @throws Exception the exception
	 */
	private void addLangRight(�ε���ǥ�ø��Item item) throws Exception {
		��������Ȳ right = item.get������();
		logger.info("Add Land Right items size =" + right.getItems().size());
		right.insertFor����(goods_id, item.get�Ű�����());
	}

	/**
	 * Calc presentation.
	 * 
	 * @param hasBuilding the has building
	 * @param hasBuildingRight the has building right
	 * @param hasLand the has land
	 * @param hasLandRight the has land right
	 * @param comment the comment
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
						presentation = "���� �� �ǹ����иŰ�";
					} else {
						presentation = "������ü  �� �ǹ����иŰ�";
					}
				} else {
					if (hasLandRight) {
						presentation = "�ǹ���ü �� �������иŰ�";
					} else {
						presentation = "����  �� �ǹ��ϰ��Ű�";
					}
				}
			} else {
				if (hasBuildingRight) {
					presentation = "�������� �� �ǹ����иŰ�";
				} else {
					presentation = "�������� �� �ǹ��Ű�";
				}
			}
		} else {
			if (hasLandRight) {
				if (is�ǹ����ܺ��(comment)) {
					presentation = "�ǹ����� �� �������иŰ�";
				} else {
					presentation = "�������иŰ�";
				}

			} else {
				if (is�ǹ����ܺ��(comment)) {
					presentation = "�ǹ����� �� �����Ű�";
				} else {
					presentation = "�����Ű�";
				}

			}
		}
		return presentation;
	}

	/**
	 * Calc type.
	 * 
	 * @param hasBuilding the has building
	 * @param has�����κ� the has�����κ�
	 * @param lands the lands
	 * @param blds the blds
	 * @param type the type
	 * 
	 * @return the int
	 */
	private int calcType(boolean hasBuilding, boolean has�����κ�, List<������Ȳ> lands, List<�ǹ���Ȳ> blds, int type) {
		List<Integer> types = new LinkedList<Integer>();
		if (hasBuilding) {
			if (has�����κ�) {
				for (�ǹ���Ȳ bld : blds) {
					List<Entry> target = �����κ�Match;
					type = iterateTypes(type, bld, target);
					types.add(type);
				}
				return findHighestType(types, �����κ�Match);
			} else {
				for (�ǹ���Ȳ bld : blds) {
					List<Entry> target = bldMatch;
					type = iterateTypes(type, bld, target);
					types.add(type);
				}
				return findHighestType(types, bldMatch);
			}

		} else {
			for (������Ȳ land : lands) {
				Iterator<Entry> entries = landMatch.iterator();
				while (entries.hasNext()) {
					Entry entry = entries.next();
					if (land.isTypeMatch(entry.getKey())) {
						type = entry.getValue();
						types.add(type);
						break;
					}
				}
			}
			return findHighestType(types, landMatch);
		}

	}

	/**
	 * Clear old.
	 * 
	 * @param id the id
	 */
	private void clearOld(int id) {
		try {
			������Ȳ.clear������ȲFor����(id);
			�ǹ���Ȳ.clear�ǹ���ȲFor����(id);
			��������Ȳ.clear��������ȲFor����(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Find highest type.
	 * 
	 * @param types the types
	 * @param matches the matches
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
	 * @param �ּ� the �ּ�
	 * 
	 * @return the �ε���ǥ�ø�� item
	 */
	private �ε���ǥ�ø��Item findItemByAddress(String �ּ�) {
		for (�ε���ǥ�ø��Item item : items) {
			if (�ּ�.contains(item.address.addr4)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * First word.
	 * 
	 * @param detail the detail
	 * 
	 * @return the string
	 */
	private String firstWord(String detail) {
		String[] lines = detail.split("\n");
		String[] words = lines[0].split(" ");
		return words[0];
	}

	/**
	 * Has������.
	 * 
	 * @param detail the detail
	 * 
	 * @return true, if successful
	 */
	private boolean has������(String detail) {
		return detail.contains("�������� ������ ������ ǥ��") || detail.contains("�������� ǥ��");
	}

	/**
	 * Insert building.
	 * 
	 * @param item the item
	 * @param floor the floor
	 * @param structure the structure
	 * @param area the area
	 * @param detail the detail
	 * @param �Ű�����comment the �Ű�����comment
	 * 
	 * @return the list<�ǹ���Ȳ>
	 * 
	 * @throws Exception the exception
	 * @throws SQLException the SQL exception
	 */
	private List<�ǹ���Ȳ> insertBuilding(�ε���ǥ�ø��Item item, String floor, String structure, String area, String detail,
			String �Ű�����comment) throws Exception, SQLException {
		List<�ǹ���Ȳ> result = new LinkedList<�ǹ���Ȳ>();
		if (item.get������() != null) {
			������ ������ = item.get������();
			List<����> floors = ������.getFloors();
			logger.info("�߰��� �� ���� �ǹ��� ���� " + floors.size());
			for (���� f : floors) {
				logger.info("insert �� ���� building floor=" + f.getText() + " str=" + structure + " area=" + f.getArea());
				�ǹ���Ȳ st = new �ǹ���Ȳ(goods_id,// 
						item.getAddress().toString(),// 
						f.getText(),// 
						"", //
						f.getArea(),// 
						"������", //
						detail, //
						�Ű�����comment);
				st.insert();
				result.add(st);
			}
		} else {
			�ǹ���Ȳ st = new �ǹ���Ȳ(goods_id,// 
					item.getAddress().toString(),//
					�ǹ���Ȳ_����Converter.convert(floor),//
					�ǹ���Ȳ_����Converter.convert(structure), //
					��Ȳ�ּ�Converter.convert(area),//
					detail,// 
					�Ű�����comment);
			logger.info("insert building "// 
					+ "address=" + item.getAddress().toString()//
					+ "floor=" + �ǹ���Ȳ_����Converter.convert(floor)//
					+ " str=" + �ǹ���Ȳ_����Converter.convert(structure)//
					+ " area=" + ��Ȳ�ּ�Converter.convert(area)//
					+ " detail=" + detail//
					+ " comment=" + �Ű�����comment//
			);

			st.insert();
			result.add(st);
		}

		return result;
	}

	/**
	 * Checks if is start land.
	 * 
	 * @param detail the detail
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
	 * Checks if is �ǹ����ܺ��.
	 * 
	 * @param comment the comment
	 * 
	 * @return true, if is �ǹ����ܺ��
	 */
	private boolean is�ǹ����ܺ��(String comment) {
		// �Ű����� ���ܵǴ�~,���������,���ÿܰǹ�,�ǹ�,����
		return comment != null
				&& (comment.contains("�Ű����� ���ܵǴ�") || comment.contains("���������") || comment.contains("���ÿܰǹ�")
						|| comment.contains("�ǹ�") || comment.contains("����"));
	}

	/**
	 * Iterate types.
	 * 
	 * @param type the type
	 * @param bld the bld
	 * @param target the target
	 * 
	 * @return the int
	 */
	private int iterateTypes(int type, �ǹ���Ȳ bld, List<Entry> target) {
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
	 * @param detail the detail
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
	 * @param hasBuilding the has building
	 * @param hasBuildingRight the has building right
	 * @param hasLand the has land
	 * @param hasLandRight the has land right
	 * @param has�����κ� the has�����κ�
	 * @param comment the comment
	 * @param lands the lands
	 * @param blds the blds
	 * @param garuantee_ratio the garuantee_ratio
	 * 
	 * @throws Exception the exception
	 * @throws SQLException the SQL exception
	 */
	private void updateGoods(boolean hasBuilding, boolean hasBuildingRight, boolean hasLand, boolean hasLandRight,
			boolean has�����κ�, String comment, List<������Ȳ> lands, List<�ǹ���Ȳ> blds, String garuantee_ratio)
			throws Exception, SQLException {

		int type = 410;
		type = calcType(hasBuilding, has�����κ�, lands, blds, type);

		String presentation = "";
		presentation = calcPresentation(hasBuilding, hasBuildingRight, hasLand, hasLandRight, comment);

		if (garuantee_ratio == null) {
			garuantee_ratio = "10%";
		}

		Statement queryStmt = DB.createStatement();
		ResultSet rs = queryStmt.executeQuery("SELECT type_code, sell_target FROM ac_goods WHERE id=" + goods_id + ";");
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

		logger.info("update goods type_code =" + type + " comment=" + comment + " guarantee_ratio" + garuantee_ratio);
		PreparedStatement stmt = DB
				.prepareStatement("UPDATE ac_goods SET comment=?, type_code=?, sell_target=?, guarantee_ratio=? WHERE id=?;");
		stmt.setString(1, comment);
		stmt.setInt(2, type);
		stmt.setString(3, presentation);
		stmt.setInt(4, Integer.parseInt(garuantee_ratio));
		stmt.setLong(5, goods_id);
		stmt.execute();
	}

	/**
	 * Update guarantee ratio.
	 * 
	 * @return the string
	 */
	private String updateGuaranteeRatio() {
		String comment = ����.���;
		String garuantee_ratio = "10";
		// ��Ű�, ������, Ư���Ű�
		if (comment != null && (comment.contains("��Ű�") || comment.contains("������") || comment.contains("Ư���Ű�"))) {
			Pattern p = Pattern.compile("(\\d+)%");
			Matcher m = p.matcher(comment);
			if (m.find()) {
				garuantee_ratio = m.group(1) + "";
			} else {
				p = Pattern.compile("(\\d+)��");
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
			String text = NFile.getText(new File("land.txt"));
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
		�����κ�Match = new LinkedList<Entry>();
		File f = new File("type_match.txt");
		List<Entry> target = landMatch;
		try {
			String text = NFile.getText(f);
			String[] lines = text.split("\n");
			for (String line : lines) {
				if (line.startsWith("----1")) {
					target = bldMatch;
					continue;
				}
				if (line.startsWith("----2")) {
					target = �����κ�Match;
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
