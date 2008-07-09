/*
 * 
 */
package net.narusas.aceauction.model;

import java.util.ArrayList;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class ����.
 */
public class ���� {
	
	/** The TABL e_ b y_ code. */
	private static HashMap<String, ����> TABLE_BY_CODE = new HashMap<String, ����>();

	/** The TABL e_ b y_ name. */
	private static HashMap<String, ����> TABLE_BY_NAME = new HashMap<String, ����>();

	/** The list. */
	static ArrayList<����> list = new ArrayList<����>();

	/** The code. */
	private final String code;

	/** The gg auction code. */
	private final String ggAuctionCode;

	/** The infocare code. */
	private final String infocareCode;

	/** The name. */
	private final String name;

	/** The parent. */
	private ���� parent;

	/** The speedy auction code. */
	private final String speedyAuctionCode;
	static {

		���� c = new ����("�����߾��������", "000210", "A01", "101", "0101");
		list.add(c);
		setup(c, new ����("���ﵿ���������", "000211", "A02", "102", "0102"));
		setup(c, new ����("���Ｍ���������", "000215", "A03", "103", "0103"));
		setup(c, new ����("���ﳲ���������", "000212", "A04", "104", "0104"));
		setup(c, new ����("����Ϻ��������", "000213", "A05", "105", "0105"));

		c = new ����("�������������", "000214", "D01", "106", "1401");
		list.add(c);
		setup(c, new ����("�������", "214807", "D02", "107", "1402"));

		c = new ����("��õ�������", "000240", "C01", "201", "0201");
		list.add(c);
		setup(c, new ����("��õ����", "000241", "C02", "202", "0202"));

		c = new ����("�����������", "000250", "E01", "301", "0301");
		list.add(c);
		setup(c, new ����("��������", "000251", "E02", "302", "0302"));
		setup(c, new ����("��������", "000252", "E03", "303", "0303"));
		setup(c, new ����("��������", "000253", "E04", "304", "0304"));
		setup(c, new ����("�Ȼ�����", "250826", "E05", "305", "0305"));

		c = new ����("��õ�������", "000260", "F01", "501", "0601");
		list.add(c);
		setup(c, new ����("��������", "000261", "F02", "503", "0603"));
		setup(c, new ����("��������", "000262", "F04", "502", "0602"));
		setup(c, new ����("��������", "000263", "F03", "504", "0604"));
		setup(c, new ����("��������", "000264", "F05", "505", "0605"));

		c = new ����("û���������", "000270", "G01", "407", "0501");
		list.add(c);
		setup(c, new ����("��������", "000271", "G02", "408", "0502"));
		setup(c, new ����("��õ����", "000272", "G03", "409", "0503"));
		setup(c, new ����("��������", "000273", "G04", "410", "0504"));

		c = new ����("�����������", "000280", "H01", "401", "0401");
		list.add(c);
		setup(c, new ����("ȫ������", "000281", "H05", "405", "0405"));
		setup(c, new ����("�������", "000282", "H06", "406", "0406"));
		setup(c, new ����("õ������", "000283", "H02", "402", "0402"));
		setup(c, new ����("��������", "000284", "H03", "403", "0403"));
		setup(c, new ����("��������", "000285", "H04", "404", "0404"));

		c = new ����("�뱸�������", "000310", "I01", "701", "1001");
		list.add(c);
		list.add(new ����("��������", "000320", "I09", "", ""));
		setup(c, new ����("�ȵ�����", "000311", "I05", "707", "1007"));
		setup(c, new ����("��������", "000312", "I02", "702", "1002"));
		setup(c, new ����("��õ����", "000313", "I03", "703", "1003"));
		setup(c, new ����("��������", "000314", "I04", "704", "'1004"));
		setup(c, new ����("�Ǽ�����", "000315", "I07", "705", "1005"));
		setup(c, new ����("��������", "000316", "I06", "706", "1006"));
		setup(c, new ����("��������", "000317", "I08", "708", "1008"));

		c = new ����("�λ��������", "000410", "B01", "601", "0701");
		list.add(c);
		setup(c, new ����("��������", "000412", "B02", "602", "0702"));

		list.add(new ����("����������", "000411", "N01", "603", "0801"));

		c = new ����("â���������", "000420", "J01", "604", "0901");
		list.add(c);
		setup(c, new ����("��������", "000421", "J04", "608", "0905"));
		setup(c, new ����("�뿵����", "000422", "J05", "605", "0902"));
		setup(c, new ����("�о�����", "000423", "J03", "607", "0904"));
		setup(c, new ����("��â����", "000424", "J02", "606", "0903"));

		c = new ����("�����������", "000510", "L01", "801", "1101");
		list.add(c);
		setup(c, new ����("��������", "000511", "L03", "802", "1102"));
		setup(c, new ����("��������", "000512", "L04", "805", "1105"));
		setup(c, new ����("��õ����", "000513", "L02", "803", "1103"));
		setup(c, new ����("�س�����", "000514", "L05", "804", "1104"));

		c = new ����("�����������", "000520", "K01", "806", "1201");
		list.add(c);
		setup(c, new ����("��������", "000521", "K02", "808", "1203"));
		setup(c, new ����("��������", "000522", "K04", "809", "1204"));
		setup(c, new ����("��������", "000523", "K03", "807", "1202"));

		list.add(new ����("�����������", "000530", "M01", "901", "1301"));

		for (���� court : list) {
			TABLE_BY_NAME.put(court.name, court);
			TABLE_BY_CODE.put(court.code, court);
		}
	}

	/**
	 * Instantiates a new ����.
	 * 
	 * @param name the name
	 * @param code the code
	 * @param speedyAuctionCode the speedy auction code
	 * @param ggAuctionCode the gg auction code
	 * @param infocareCode the infocare code
	 */
	public ����(String name, String code, String speedyAuctionCode,
			String ggAuctionCode, String infocareCode) {
		this.name = name;
		this.code = code;
		this.speedyAuctionCode = speedyAuctionCode;
		this.ggAuctionCode = ggAuctionCode;
		this.infocareCode = infocareCode;
	}

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Gets the code as number.
	 * 
	 * @return the code as number
	 */
	public int getCodeAsNumber() {
		return Integer.parseInt(code);
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public ���� getParent() {
		return this.parent;
	}

	/**
	 * Gets the ���ǵ���� code.
	 * 
	 * @return the ���ǵ���� code
	 */
	public String get���ǵ����Code() {
		return speedyAuctionCode;
	}

	/**
	 * Gets the �����ɾ� code.
	 * 
	 * @return the �����ɾ� code
	 */
	public String get�����ɾ�Code() {
		return infocareCode;
	}

	/**
	 * Gets the �������� code.
	 * 
	 * @return the �������� code
	 */
	public String get��������Code() {
		return ggAuctionCode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * Sets the parent.
	 * 
	 * @param c the new parent
	 */
	private void setParent(���� c) {
		this.parent = c;
	}

	/**
	 * Find by code.
	 * 
	 * @param court the court
	 * 
	 * @return the ����
	 */
	public static ���� findByCode(int court) {
		String courtCode = String.valueOf(court);
		int len = courtCode.length();
		for (int i = len; i <= 6; i++) {
			courtCode = "0" + courtCode;
		}

		return TABLE_BY_CODE.get(courtCode);
	}

	/**
	 * Find by code.
	 * 
	 * @param court the court
	 * 
	 * @return the ����
	 */
	public static ���� findByCode(String court) {
		return TABLE_BY_CODE.get(court);
	}

	/**
	 * Find by name.
	 * 
	 * @param code the code
	 * 
	 * @return the ����
	 */
	public static ���� findByName(String code) {
		return TABLE_BY_NAME.get(code);
	}

	/**
	 * Gets the.
	 * 
	 * @param index the index
	 * 
	 * @return the ����
	 */
	public static ���� get(int index) {
		return list.get(index);
	}

	/**
	 * Size.
	 * 
	 * @return the int
	 */
	public static int size() {
		return list.size();
	}

	/**
	 * Setup.
	 * 
	 * @param c the c
	 * @param c2 the c2
	 */
	private static void setup(���� c, ���� c2) {
		c2.setParent(c);
		list.add(c2);
	}
}
