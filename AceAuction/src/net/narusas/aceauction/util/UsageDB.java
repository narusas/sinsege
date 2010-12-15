package net.narusas.aceauction.util;

import java.sql.PreparedStatement;
import java.sql.Statement;

import net.narusas.aceauction.data.DB;

public class UsageDB extends DB {
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

	private void add(int code, String name, int parentCode) throws Exception {
		PreparedStatement stmt = prepareStatement("INSERT INTO ac_goods_usage (code, name, parent_code) VALUES (?,?,?);");
		stmt.setInt(1, code);
		stmt.setString(2, name);
		stmt.setInt(3, parentCode);
		stmt.execute();
	}

	private void addEntry(int code, String name, int parentCode) throws Exception {
		add(parentCode * 100 + code, name, parentCode);
	}

	private void addParent(String name, int parentCode) throws Exception {
		add(parentCode, name, 0);
	}

	private void insert(String[] src, int parentCode) throws Exception {
		addParent(src[0], parentCode);
		for (int i = 1; i < src.length; i++) {
			addEntry(i, src[i], parentCode);
		}

	}

	public static void main(String[] args) throws Exception {
		UsageDB db = new UsageDB();
		db.update();

	}

}
