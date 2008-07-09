/*
 * 
 */
package net.narusas.aceauction.model;

import java.util.ArrayList;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class 법원.
 */
public class 법원 {
	
	/** The TABL e_ b y_ code. */
	private static HashMap<String, 법원> TABLE_BY_CODE = new HashMap<String, 법원>();

	/** The TABL e_ b y_ name. */
	private static HashMap<String, 법원> TABLE_BY_NAME = new HashMap<String, 법원>();

	/** The list. */
	static ArrayList<법원> list = new ArrayList<법원>();

	/** The code. */
	private final String code;

	/** The gg auction code. */
	private final String ggAuctionCode;

	/** The infocare code. */
	private final String infocareCode;

	/** The name. */
	private final String name;

	/** The parent. */
	private 법원 parent;

	/** The speedy auction code. */
	private final String speedyAuctionCode;
	static {

		법원 c = new 법원("서울중앙지방법원", "000210", "A01", "101", "0101");
		list.add(c);
		setup(c, new 법원("서울동부지방법원", "000211", "A02", "102", "0102"));
		setup(c, new 법원("서울서부지방법원", "000215", "A03", "103", "0103"));
		setup(c, new 법원("서울남부지방법원", "000212", "A04", "104", "0104"));
		setup(c, new 법원("서울북부지방법원", "000213", "A05", "105", "0105"));

		c = new 법원("의정부지방법원", "000214", "D01", "106", "1401");
		list.add(c);
		setup(c, new 법원("고양지원", "214807", "D02", "107", "1402"));

		c = new 법원("인천지방법원", "000240", "C01", "201", "0201");
		list.add(c);
		setup(c, new 법원("부천지원", "000241", "C02", "202", "0202"));

		c = new 법원("수원지방법원", "000250", "E01", "301", "0301");
		list.add(c);
		setup(c, new 법원("성남지원", "000251", "E02", "302", "0302"));
		setup(c, new 법원("여주지원", "000252", "E03", "303", "0303"));
		setup(c, new 법원("평택지원", "000253", "E04", "304", "0304"));
		setup(c, new 법원("안산지원", "250826", "E05", "305", "0305"));

		c = new 법원("춘천지방법원", "000260", "F01", "501", "0601");
		list.add(c);
		setup(c, new 법원("강릉지원", "000261", "F02", "503", "0603"));
		setup(c, new 법원("원주지원", "000262", "F04", "502", "0602"));
		setup(c, new 법원("속초지원", "000263", "F03", "504", "0604"));
		setup(c, new 법원("영월지원", "000264", "F05", "505", "0605"));

		c = new 법원("청주지방법원", "000270", "G01", "407", "0501");
		list.add(c);
		setup(c, new 법원("충주지원", "000271", "G02", "408", "0502"));
		setup(c, new 법원("제천지원", "000272", "G03", "409", "0503"));
		setup(c, new 법원("영동지원", "000273", "G04", "410", "0504"));

		c = new 법원("대전지방법원", "000280", "H01", "401", "0401");
		list.add(c);
		setup(c, new 법원("홍성지원", "000281", "H05", "405", "0405"));
		setup(c, new 법원("논산지원", "000282", "H06", "406", "0406"));
		setup(c, new 법원("천안지원", "000283", "H02", "402", "0402"));
		setup(c, new 법원("공주지원", "000284", "H03", "403", "0403"));
		setup(c, new 법원("서산지원", "000285", "H04", "404", "0404"));

		c = new 법원("대구지방법원", "000310", "I01", "701", "1001");
		list.add(c);
		list.add(new 법원("서부지원", "000320", "I09", "", ""));
		setup(c, new 법원("안동지원", "000311", "I05", "707", "1007"));
		setup(c, new 법원("경주지원", "000312", "I02", "702", "1002"));
		setup(c, new 법원("김천지원", "000313", "I03", "703", "1003"));
		setup(c, new 법원("상주지원", "000314", "I04", "704", "'1004"));
		setup(c, new 법원("의성지원", "000315", "I07", "705", "1005"));
		setup(c, new 법원("영덕지원", "000316", "I06", "706", "1006"));
		setup(c, new 법원("포항지원", "000317", "I08", "708", "1008"));

		c = new 법원("부산지방법원", "000410", "B01", "601", "0701");
		list.add(c);
		setup(c, new 법원("동부지원", "000412", "B02", "602", "0702"));

		list.add(new 법원("울산지방법원", "000411", "N01", "603", "0801"));

		c = new 법원("창원지방법원", "000420", "J01", "604", "0901");
		list.add(c);
		setup(c, new 법원("진주지원", "000421", "J04", "608", "0905"));
		setup(c, new 법원("통영지원", "000422", "J05", "605", "0902"));
		setup(c, new 법원("밀양지원", "000423", "J03", "607", "0904"));
		setup(c, new 법원("거창지원", "000424", "J02", "606", "0903"));

		c = new 법원("광주지방법원", "000510", "L01", "801", "1101");
		list.add(c);
		setup(c, new 법원("목포지원", "000511", "L03", "802", "1102"));
		setup(c, new 법원("장흥지원", "000512", "L04", "805", "1105"));
		setup(c, new 법원("순천지원", "000513", "L02", "803", "1103"));
		setup(c, new 법원("해남지원", "000514", "L05", "804", "1104"));

		c = new 법원("전주지방법원", "000520", "K01", "806", "1201");
		list.add(c);
		setup(c, new 법원("군산지원", "000521", "K02", "808", "1203"));
		setup(c, new 법원("정읍지원", "000522", "K04", "809", "1204"));
		setup(c, new 법원("남원지원", "000523", "K03", "807", "1202"));

		list.add(new 법원("제주지방법원", "000530", "M01", "901", "1301"));

		for (법원 court : list) {
			TABLE_BY_NAME.put(court.name, court);
			TABLE_BY_CODE.put(court.code, court);
		}
	}

	/**
	 * Instantiates a new 법원.
	 * 
	 * @param name the name
	 * @param code the code
	 * @param speedyAuctionCode the speedy auction code
	 * @param ggAuctionCode the gg auction code
	 * @param infocareCode the infocare code
	 */
	public 법원(String name, String code, String speedyAuctionCode,
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
	public 법원 getParent() {
		return this.parent;
	}

	/**
	 * Gets the 스피드옥션 code.
	 * 
	 * @return the 스피드옥션 code
	 */
	public String get스피드옥션Code() {
		return speedyAuctionCode;
	}

	/**
	 * Gets the 인포케어 code.
	 * 
	 * @return the 인포케어 code
	 */
	public String get인포케어Code() {
		return infocareCode;
	}

	/**
	 * Gets the 지지옥션 code.
	 * 
	 * @return the 지지옥션 code
	 */
	public String get지지옥션Code() {
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
	private void setParent(법원 c) {
		this.parent = c;
	}

	/**
	 * Find by code.
	 * 
	 * @param court the court
	 * 
	 * @return the 법원
	 */
	public static 법원 findByCode(int court) {
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
	 * @return the 법원
	 */
	public static 법원 findByCode(String court) {
		return TABLE_BY_CODE.get(court);
	}

	/**
	 * Find by name.
	 * 
	 * @param code the code
	 * 
	 * @return the 법원
	 */
	public static 법원 findByName(String code) {
		return TABLE_BY_NAME.get(code);
	}

	/**
	 * Gets the.
	 * 
	 * @param index the index
	 * 
	 * @return the 법원
	 */
	public static 법원 get(int index) {
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
	private static void setup(법원 c, 법원 c2) {
		c2.setParent(c);
		list.add(c2);
	}
}
