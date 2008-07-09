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

		insert(new String[] { "�ְſ�ε���", "����Ʈ", "����", "�ټ���(����)", "�ٰ���(�����)", "�ٸ�����", "���ǽ���" }, 1);

		insert(new String[] { "��� �� �����ε���", "�ٸ��ü�", "�ٸ���", "��", "����", "����Ʈ������", "���ڽü�", "������",
				"����", "����Ʈ��", "â��", "�����" }, 2);

		insert(new String[] { "����", "����", "�Ӿ�", "��", "��", "������", "������", "�������", "����", "�������" }, 3);

		insert(new String[] { "��Ÿ", "���", "�ܵ�", "�б�", "������", "����", "������", "�����", "�󰡰��ýü�", "�����ü�",
				"��Ÿ" }, 4);

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
