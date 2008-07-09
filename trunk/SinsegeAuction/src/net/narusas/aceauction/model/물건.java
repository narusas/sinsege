/*
 * 
 */
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

// TODO: Auto-generated Javadoc
/**
 * The Class ����.
 */
public class ���� {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The has����. */
	public boolean has����;

	/** The ����. */
	public String ����;

	/** The ������ȣ. */
	public String ������ȣ = "";

	/** The ���ǹ�ȣ. */
	public int ���ǹ�ȣ;

	/** The ��������. */
	public String ��������;

	/** The ������Ȳ. */
	public List<������Ȳ> ������Ȳ = new ArrayList<������Ȳ>();

	/** The ���. */
	public String ���;

	/** The ���. */
	public ��� ���;

	/** The ����. */
	public List<String> ���� = new ArrayList<String>();

	/** The ������. */
	public String ������;

	/** The detail. */
	private ���ǵ���ǹ��ǻ󼼳��� detail;

	/** The �ǹ����ε. */
	private String �ǹ����ε;

	/** The ���ϳ���. */
	private LinkedList<���ϳ���Item> ���ϳ��� = new LinkedList<���ϳ���Item>();

	/** The �Ű����Ǹ���html. */
	private String �Ű����Ǹ���html;

	/** The ����. */
	private ���� ����;

	/** The ���䱸������. */
	private String ���䱸������;

	/** The ���ÿܰǹ�s. */
	private LinkedList<���ÿܰǹ�> ���ÿܰǹ�s = new LinkedList<���ÿܰǹ�>();

	/** The �ּ�. */
	private String �ּ�;
	
	/** The �������ε. */
	private String �������ε;

	/**
	 * Instantiates a new ����.
	 */
	public ����() {

	}

	/**
	 * Instantiates a new ����.
	 * 
	 * @param ���ǹ�ȣ the ���ǹ�ȣ
	 * @param �������� the ��������
	 * @param has���� the has����
	 * @param ������ȣ the ������ȣ
	 * @param ��� the ���
	 * @param ���� the ����
	 * @param ������ the ������
	 */
	public ����(int ���ǹ�ȣ, String ��������, boolean has����, String ������ȣ, String ���, String ����, String ������) {
		this.���ǹ�ȣ = ���ǹ�ȣ;
		this.�������� = ��������;
		this.has���� = has����;
		this.������ȣ = ������ȣ;
		this.��� = ���;
		this.���� = ����;
		this.������ = ������;
	}

	/**
	 * Adds the.
	 * 
	 * @param bld the bld
	 */
	public void add(������Ȳ bld) {
		if (������Ȳ.size() == 0) {
			set�ּ�(bld.get�ּ�());
		}
		������Ȳ.add(bld);
	}

	/**
	 * Add���ϳ���.
	 * 
	 * @param record the record
	 */
	public void add���ϳ���(Row record) {
		���ϳ���Item item = new ���ϳ���Item(record);
		if (���ϳ���.contains(item)) {
			return;
		}
		���ϳ���.add(item);
	}

	/**
	 * Add����.
	 * 
	 * @param url the url
	 */
	public void add����(String url) {
		����.add(url);
	}

	/**
	 * Add���ÿܰǹ�.
	 * 
	 * @param e the e
	 */
	public void add���ÿܰǹ�(���ÿܰǹ� e) {
		���ÿܰǹ�s.add(e);
	}

	/**
	 * Clear���ÿܰǹ�.
	 */
	public void clear���ÿܰǹ�() {
		���ÿܰǹ�s.clear();
	}

	/**
	 * Gets the detail.
	 * 
	 * @return the detail
	 */
	public ���ǵ���ǹ��ǻ󼼳��� getDetail() {
		return detail;
	}

	/**
	 * Gets the has����.
	 * 
	 * @return the has����
	 */
	public boolean getHas����() {
		return has����;
	}

	/**
	 * Gets the ����.
	 * 
	 * @return the ����
	 */
	public String get����() {
		return ����;
	}

	/**
	 * Gets the �ǹ����ε.
	 * 
	 * @return the �ǹ����ε
	 */
	public String get�ǹ����ε() {
		return �ǹ����ε;
	}

	/**
	 * Gets the ���ϳ���.
	 * 
	 * @return the ���ϳ���
	 */
	public List<���ϳ���Item> get���ϳ���() {
		if (���ϳ���.size() == 0) {
			���.update���ϳ���(this);
		}
		return ���ϳ���;
	}

	/**
	 * Gets the ���ε file path.
	 * 
	 * @return the ���ε file path
	 */
	public String get���εFilePath() {
		return ���.getEventYear() + "/" + ���.court.getCode() + "/" + removeDots(���.charge.get�Ű�����().toString()) + "/"
				+ ���.charge.get�����ڵ�() + "/" + ���.get��ǹ�ȣ() + "/" + ���ǹ�ȣ + "/";
	}

	/**
	 * Gets the �Ű����Ǹ���html.
	 * 
	 * @return the �Ű����Ǹ���html
	 */
	public String get�Ű����Ǹ���html() {
		return �Ű����Ǹ���html;
	}

	/**
	 * Gets the ����.
	 * 
	 * @return the ����
	 */
	public ���� get����() {
		return ����;
	}

	/**
	 * Gets the ������ȣ.
	 * 
	 * @return the ������ȣ
	 */
	public String get������ȣ() {
		return ������ȣ;
	}

	/**
	 * Gets the ���� file path.
	 * 
	 * @return the ���� file path
	 */
	public String get����FilePath() {
		return ���.getEventYear() + "/" + ���.court.getCode() + "/" + removeDots(���.charge.get�Ű�����().toString()) + "/"
				+ ���.charge.get�����ڵ�() + "/" + ���.get��ǹ�ȣ() + "/" + ���ǹ�ȣ + "/";
	}

	/**
	 * Gets the ���ǹ�ȣ.
	 * 
	 * @return the ���ǹ�ȣ
	 */
	public int get���ǹ�ȣ() {
		return ���ǹ�ȣ;
	}

	/**
	 * Gets the ��������.
	 * 
	 * @return the ��������
	 */
	public String get��������() {
		return ��������;
	}

	/**
	 * Gets the ������Ȳ.
	 * 
	 * @return the ������Ȳ
	 */
	public List<������Ȳ> get������Ȳ() {
		return ������Ȳ;
	}

	/**
	 * Gets the ���䱸������.
	 * 
	 * @return the ���䱸������
	 */
	public String get���䱸������() {
		���.update���䱸(this);
		return ���䱸������;
	}

	/**
	 * Gets the ���.
	 * 
	 * @return the ���
	 */
	public String get���() {
		return ���;
	}

	/**
	 * Gets the ���.
	 * 
	 * @return the ���
	 */
	public ��� get���() {
		return ���;
	}

	/**
	 * Gets the ����.
	 * 
	 * @return the ����
	 */
	public List<String> get����() {
		return ����;
	}

	/**
	 * Gets the �󼼳���.
	 * 
	 * @param index the index
	 * 
	 * @return the �󼼳���
	 */
	public ������Ȳ get�󼼳���(int index) {
		return (������Ȳ) ������Ȳ.get(index);
	}

	/**
	 * Gets the ���ÿܰǹ�s.
	 * 
	 * @return the ���ÿܰǹ�s
	 */
	public LinkedList<���ÿܰǹ�> get���ÿܰǹ�s() {
		return ���ÿܰǹ�s;
	}

	/**
	 * Gets the �ּ�.
	 * 
	 * @return the �ּ�
	 */
	public String get�ּ�() {
		return �ּ�;
	}

	/**
	 * Gets the ������.
	 * 
	 * @return the ������
	 */
	public String get������() {
		return ������;
	}

	/**
	 * Gets the �������ε.
	 * 
	 * @return the �������ε
	 */
	public String get�������ε() {
		return �������ε;
	}

	/**
	 * Has����.
	 * 
	 * @return true, if successful
	 */
	public boolean has����() {
		return has����;
	}

	/**
	 * Merge.
	 * 
	 * @param m2 the m2
	 */
	public void merge(���� m2) {
		this.������Ȳ.addAll(m2.������Ȳ);

	}

	/**
	 * Sets the detail.
	 * 
	 * @param detail the new detail
	 */
	public void setDetail(���ǵ���ǹ��ǻ󼼳��� detail) {
		this.detail = detail;
	}

	/**
	 * Sets the has����.
	 * 
	 * @param has���� the new has����
	 */
	public void setHas����(boolean has����) {
		this.has���� = has����;
	}

	/**
	 * Sets the parent.
	 * 
	 * @param ��� the new parent
	 */
	public void setParent(��� ���) {
		this.��� = ���;
	}

	/**
	 * Sets the ����.
	 * 
	 * @param ���� the new ����
	 */
	public void set����(String ����) {
		this.���� = ����;
	}

	/**
	 * Sets the �ǹ����ε.
	 * 
	 * @param �ǹ����ε the new �ǹ����ε
	 */
	public void set�ǹ����ε(String �ǹ����ε) {
		this.�ǹ����ε = �ǹ����ε;

	}

	/**
	 * Sets the �Ű����Ǹ���html.
	 * 
	 * @param �Ű����Ǹ���html the new �Ű����Ǹ���html
	 */
	public void set�Ű����Ǹ���html(String �Ű����Ǹ���html) {
		this.�Ű����Ǹ���html = �Ű����Ǹ���html;
	}

	/**
	 * Sets the �Ű����Ǹ��� html.
	 * 
	 * @param �Ű����Ǹ���HTML the new �Ű����Ǹ��� html
	 */
	public void set�Ű����Ǹ���HTML(String �Ű����Ǹ���HTML) {
		�Ű����Ǹ���html = �Ű����Ǹ���HTML;

	}

	/**
	 * Sets the ����.
	 * 
	 * @param ���� the new ����
	 */
	public void set����(���� ����) {
		this.���� = ����;
	}

	/**
	 * Sets the ������ȣ.
	 * 
	 * @param ������ȣ the new ������ȣ
	 */
	public void set������ȣ(String ������ȣ) {
		this.������ȣ = ������ȣ;
	}

	/**
	 * Sets the ���ǹ�ȣ.
	 * 
	 * @param ���ǹ�ȣ the new ���ǹ�ȣ
	 */
	public void set���ǹ�ȣ(int ���ǹ�ȣ) {
		this.���ǹ�ȣ = ���ǹ�ȣ;
	}

	/**
	 * Sets the ��������.
	 * 
	 * @param �������� the new ��������
	 */
	public void set��������(String ��������) {
		this.�������� = ��������;
	}

	/**
	 * Sets the ���䱸������.
	 * 
	 * @param ���䱸������ the new ���䱸������
	 */
	public void set���䱸������(String ���䱸������) {
		this.���䱸������ = ���䱸������;
	}

	/**
	 * Sets the ���.
	 * 
	 * @param ��� the new ���
	 */
	public void set���(String ���) {
		this.��� = ���;
	}

	/**
	 * Sets the ���.
	 * 
	 * @param ��� the new ���
	 */
	public void set���(��� ���) {
		this.��� = ���;
	}

	/**
	 * Sets the ������.
	 * 
	 * @param ������ the new ������
	 */
	public void set������(String ������) {
		this.������ = ������;
	}

	/**
	 * Sets the �������ε.
	 * 
	 * @param �������ε the new �������ε
	 */
	public void set�������ε(String �������ε) {
		this.�������ε = �������ε;

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "����{��ȣ=" + ���ǹ�ȣ + "}";// ;,����=" + �������� + ",������������=" + has����;
		// + ", ������ȣ=" + ������ȣ + ", ����=[" + ���� + "], ������=[" + ������
		// + "],���=[" + ��� + "]}";
	}

	/**
	 * Removes the dots.
	 * 
	 * @param src the src
	 * 
	 * @return the string
	 */
	private String removeDots(String src) {
		return src.replaceAll(".", "").replace("-", "");
	}

	/**
	 * Sets the �ּ�.
	 * 
	 * @param �ּ� the new �ּ�
	 */
	private void set�ּ�(String �ּ�) {
		this.�ּ� = �ּ�;
	}

	/**
	 * Gets the special case source.
	 * 
	 * @param id the id
	 * 
	 * @return the special case source
	 */
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

	/**
	 * Update special case.
	 * 
	 * @param src the src
	 * @param id the id
	 * 
	 * @return the string
	 */
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

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
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
