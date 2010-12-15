package net.narusas.aceauction.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import net.narusas.aceauction.data.DB;

public class ���� {

	static Logger logger = Logger.getLogger("log");

	public boolean has����;

	public String ����;

	public String ������ȣ = "";

	public int ���ǹ�ȣ;

	public String ��������;

	public List<������Ȳ> ������Ȳ = new ArrayList<������Ȳ>();

	public String ���;

	public ��� ���;

	public List<String> ���� = new ArrayList<String>();

	public String ������;

	private ���ǵ���ǹ��ǻ󼼳��� detail;

	private String �ǹ����ε;

	private LinkedList<���ϳ���Item> ���ϳ��� = new LinkedList<���ϳ���Item>();

	private String �Ű����Ǹ���html;

	private ���� ����;

	private String ���䱸������;

	private LinkedList<���ÿܰǹ�> ���ÿܰǹ�s = new LinkedList<���ÿܰǹ�>();

	private String �ּ�;
	private String �������ε;

	public ����() {

	}

	public ����(int ���ǹ�ȣ, String ��������, boolean has����, String ������ȣ, String ���, String ����, String ������) {
		this.���ǹ�ȣ = ���ǹ�ȣ;
		this.�������� = ��������;
		this.has���� = has����;
		this.������ȣ = ������ȣ;
		this.��� = ���;
		this.���� = ����;
		this.������ = ������;
	}

	public void add(������Ȳ bld) {
		if (������Ȳ.size() == 0) {
			set�ּ�(bld.get�ּ�());
		}
		������Ȳ.add(bld);
	}

	public void add���ϳ���(Row record) {
		���ϳ���Item item = new ���ϳ���Item(record);
		if (���ϳ���.contains(item)) {
			return;
		}
		���ϳ���.add(item);
	}

	public void add����(String url) {
		����.add(url);
	}

	public void add���ÿܰǹ�(���ÿܰǹ� e) {
		���ÿܰǹ�s.add(e);
	}

	public void clear���ÿܰǹ�() {
		���ÿܰǹ�s.clear();
	}

	public ���ǵ���ǹ��ǻ󼼳��� getDetail() {
		return detail;
	}

	public boolean getHas����() {
		return has����;
	}

	public String get����() {
		return ����;
	}

	public String get�ǹ����ε() {
		return �ǹ����ε;
	}

	public List<���ϳ���Item> get���ϳ���() {
		if (���ϳ���.size() == 0) {
			���.update���ϳ���(this);
		}
		return ���ϳ���;
	}

	public String get���εFilePath() {
		return ���.getEventYear() + "/" + ���.court.getCode() + "/" + removeDots(���.charge.get�Ű�����().toString()) + "/"
				+ ���.charge.get�����ڵ�() + "/" + ���.get��ǹ�ȣ() + "/" + ���ǹ�ȣ + "/";
	}

	public String get�Ű����Ǹ���html() {
		return �Ű����Ǹ���html;
	}

	public ���� get����() {
		return ����;
	}

	public String get������ȣ() {
		return ������ȣ;
	}

	public String get����FilePath() {
		return ���.getEventYear() + "/" + ���.court.getCode() + "/" + removeDots(���.charge.get�Ű�����().toString()) + "/"
				+ ���.charge.get�����ڵ�() + "/" + ���.get��ǹ�ȣ() + "/" + ���ǹ�ȣ + "/";
	}

	public int get���ǹ�ȣ() {
		return ���ǹ�ȣ;
	}

	public String get��������() {
		return ��������;
	}

	public List<������Ȳ> get������Ȳ() {
		return ������Ȳ;
	}

	public String get���䱸������() {
		���.update���䱸(this);
		return ���䱸������;
	}

	public String get���() {
		return ���;
	}

	public ��� get���() {
		return ���;
	}

	public List<String> get����() {
		return ����;
	}

	public ������Ȳ get�󼼳���(int index) {
		return (������Ȳ) ������Ȳ.get(index);
	}

	public LinkedList<���ÿܰǹ�> get���ÿܰǹ�s() {
		return ���ÿܰǹ�s;
	}

	public String get�ּ�() {
		return �ּ�;
	}

	public String get������() {
		return ������;
	}

	public String get�������ε() {
		return �������ε;
	}

	public boolean has����() {
		return has����;
	}

	public void merge(���� m2) {
		this.������Ȳ.addAll(m2.������Ȳ);

	}

	public void setDetail(���ǵ���ǹ��ǻ󼼳��� detail) {
		this.detail = detail;
	}

	public void setHas����(boolean has����) {
		this.has���� = has����;
	}

	public void setParent(��� ���) {
		this.��� = ���;
	}

	public void set����(String ����) {
		this.���� = ����;
	}

	public void set�ǹ����ε(String �ǹ����ε) {
		this.�ǹ����ε = �ǹ����ε;

	}

	public void set�Ű����Ǹ���html(String �Ű����Ǹ���html) {
		this.�Ű����Ǹ���html = �Ű����Ǹ���html;
	}

	public void set�Ű����Ǹ���HTML(String �Ű����Ǹ���HTML) {
		�Ű����Ǹ���html = �Ű����Ǹ���HTML;

	}

	public void set����(���� ����) {
		this.���� = ����;
	}

	public void set������ȣ(String ������ȣ) {
		this.������ȣ = ������ȣ;
	}

	public void set���ǹ�ȣ(int ���ǹ�ȣ) {
		this.���ǹ�ȣ = ���ǹ�ȣ;
	}

	public void set��������(String ��������) {
		this.�������� = ��������;
	}

	public void set���䱸������(String ���䱸������) {
		this.���䱸������ = ���䱸������;
	}

	public void set���(String ���) {
		this.��� = ���;
	}

	public void set���(��� ���) {
		this.��� = ���;
	}

	public void set������(String ������) {
		this.������ = ������;
	}

	public void set�������ε(String �������ε) {
		this.�������ε = �������ε;

	}

	@Override
	public String toString() {
		return "����{��ȣ=" + ���ǹ�ȣ + "}";// ;,����=" + �������� + ",������������=" + has����;
		// + ", ������ȣ=" + ������ȣ + ", ����=[" + ���� + "], ������=[" + ������
		// + "],���=[" + ��� + "]}";
	}

	private String removeDots(String src) {
		return src.replaceAll(".", "").replace("-", "");
	}

	private void set�ּ�(String �ּ�) {
		this.�ּ� = �ּ�;
	}

	public static String[] getSpecialCaseSource(long id) {
		Statement stmt = null;
		ResultSet rs = null;

		try {
			String[] result = new String[2];
			stmt = DB.createStatement();
			rs = stmt.executeQuery("SELECT goods_statememt_comment4,comment FROM ac_goods WHERE id=" + +id + ";");
			if (rs != null && rs.next()) {
				result[0] = rs.getString("goods_statememt_comment4");
				result[1] = rs.getString("comment");
				return result;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.cleanup(rs, stmt);
		}
		return null;
	}

	public static String updateSpecialCase(String[] src, long id) {
		if (src == null) {
			return null;
		}
		String res = "";
		if ((src[0] != null && src[0].contains("���������")) || (src[1] != null && src[1].contains("���������"))) {
			res += "���������,";
		}
		if ((src[0] != null && src[0].contains("�й�")) || (src[1] != null && src[1].contains("�й�"))) {
			res += "�й�,";
		}
		if ((src[0] != null && src[0].contains("��ġ��")) || (src[1] != null && src[1].contains("��ġ��"))) {
			res += "��ġ��,";
		}
		if ((src[0] != null && src[0].contains("�������")) || (src[1] != null && src[1].contains("�������"))) {
			res += "�������,";
		}
		if ((src[0] != null && src[0].contains("������ ")) || (src[1] != null && src[1].contains("������ "))) {
			res += "������,";
		}
		PreparedStatement stmt = null;

		try {
			stmt = DB.prepareStatement("UPDATE ac_goods SET special_case=? WHERE id=?;");
			stmt.setString(1, res);
			stmt.setLong(2, id);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.cleanup(stmt);
		}
		return res;
	}

	public static void main(String[] args) {
		try {
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT id FROM ac_goods;");
			while (rs.next()) {
				long id = rs.getLong("id");
				System.out.println("Try ID=" + id + ", res=" + ����.updateSpecialCase(����.getSpecialCaseSource(id), id));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
