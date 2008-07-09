/*
 * 
 */
package net.narusas.aceauction.util;

import java.sql.PreparedStatement;
import java.sql.Statement;

import net.narusas.aceauction.data.DB;

// TODO: Auto-generated Javadoc
/**
 * The Class UsageDB.
 */
public class UsageDB extends DB {
	
	/**
	 * Update.
	 * 
	 * @throws Exception the exception
	 */
	public void update() throws Exception {
		dbConnect();
		Statement stmt = createStatement();
		stmt.executeUpdate("DELETE FROM ac_goods_usage WHERE code>=0;");

		insert(new String[] { "주거용부동산", "아파트", "주택", "다세대(빌라)", "다가구(원룸등)", "근린주택", "오피스텔" }, 1);

		insert(new String[] { "상업 및 산업용부동산", "근린시설", "근린상가", "상가", "공장", "아파트형공장", "숙박시설", "주유소",
				"병원", "아파트상가", "창고", "목욕탕" }, 2);

		insert(new String[] { "토지", "대지", "임야", "전", "답", "과수원", "잡종지", "공장용지", "도로", "목장용지" }, 3);

		insert(new String[] { "기타", "축사", "콘도", "학교", "주차장", "묘지", "광업권", "어업권", "농가관련시설", "종교시설",
				"기타" }, 4);

	}

	/**
	 * Adds the.
	 * 
	 * @param code the code
	 * @param name the name
	 * @param parentCode the parent code
	 * 
	 * @throws Exception the exception
	 */
	private void add(int code, String name, int parentCode) throws Exception {
		PreparedStatement stmt = prepareStatement("INSERT INTO ac_goods_usage (code, name, parent_code) VALUES (?,?,?);");
		stmt.setInt(1, code);
		stmt.setString(2, name);
		stmt.setInt(3, parentCode);
		stmt.execute();
	}

	/**
	 * Adds the entry.
	 * 
	 * @param code the code
	 * @param name the name
	 * @param parentCode the parent code
	 * 
	 * @throws Exception the exception
	 */
	private void addEntry(int code, String name, int parentCode) throws Exception {
		add(parentCode * 100 + code, name, parentCode);
	}

	/**
	 * Adds the parent.
	 * 
	 * @param name the name
	 * @param parentCode the parent code
	 * 
	 * @throws Exception the exception
	 */
	private void addParent(String name, int parentCode) throws Exception {
		add(parentCode, name, 0);
	}

	/**
	 * Insert.
	 * 
	 * @param src the src
	 * @param parentCode the parent code
	 * 
	 * @throws Exception the exception
	 */
	private void insert(String[] src, int parentCode) throws Exception {
		addParent(src[0], parentCode);
		for (int i = 1; i < src.length; i++) {
			addEntry(i, src[i], parentCode);
		}

	}

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 * 
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		UsageDB db = new UsageDB();
		db.update();

	}

}
