/*
 * 
 */
package net.narusas.aceauction.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class ���ÿܰǹ�.
 */
public class ���ÿܰǹ� {

	/** The Constant NO. */
	static final int NO = 0;

	/** The p. */
	static Pattern p = Pattern.compile("(\\d+[-,]*\\d*[\\S\\s]*)$");

	/** The Constant ���ÿܰǹ�����. */
	static final int ���ÿܰǹ����� = 2;

	/** The Constant ���ÿܰǹ�����. */
	static final int ���ÿܰǹ����� = 1;

	/** The address. */
	private final String address;

	/** The contains. */
	private final int contains;

	/** The type. */
	private String type;

	/** The ����. */
	private final String ����;

	/** The ����. */
	private final String ����;

	/** The ���ǹ�ȣ. */
	private final int ���ǹ�ȣ;

	/** The �뵵. */
	private final String �뵵;

	/** The ����. */
	private String ����;

	/**
	 * Instantiates a new ���ÿܰǹ�.
	 * 
	 * @param ���ǹ�ȣ the ���ǹ�ȣ
	 * @param �뵵 the �뵵
	 * @param ���� the ����
	 * @param ���� the ����
	 * @param address the address
	 * @param contains the contains
	 */
	public ���ÿܰǹ�(int ���ǹ�ȣ, String �뵵, String ����, String ����, String address, int contains) {
		this.���ǹ�ȣ = ���ǹ�ȣ;
		this.�뵵 = �뵵;
		this.���� = ����;
		this.���� = ����;
		this.contains = contains;
		this.address = parseAddress(address);
		this.type = parseType();
		parseFloor();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		���ÿܰǹ� target = (���ÿܰǹ�) arg0;
		return �뵵.equals(target.�뵵) && ����.equals(target.����) && ����.equals(target.����)
				&& ���ǹ�ȣ == target.���ǹ�ȣ;
	}

	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	public String getAddress() {
		return address;
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
	 * Gets the ����.
	 * 
	 * @return the ����
	 */
	public String get����() {
		return ����;
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
	 * Gets the �뵵.
	 * 
	 * @return the �뵵
	 */
	public String get�뵵() {
		return �뵵;
	}

	/**
	 * Gets the ����.
	 * 
	 * @return the ����
	 */
	public String get����() {
		return type;
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
	 * Gets the ���Կ���.
	 * 
	 * @return the ���Կ���
	 */
	public int get���Կ���() {
		return contains;
	}

	/**
	 * Gets the ���Կ��� string.
	 * 
	 * @return the ���Կ��� string
	 */
	public String get���Կ���String() {
		if (contains == 1) {
			return "����";
		}
		if (contains == 2) {
			return "����";
		}
		return "";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return type + "[" + ���ǹ�ȣ + ",�ּ�=" + address + ",�뵵=" + �뵵 + ",����=" + ���� + ",����=" + ���� + "]";
	}

	/**
	 * Parses the address.
	 * 
	 * @param address the address
	 * 
	 * @return the string
	 */
	private String parseAddress(String address) {
		String[] buf = address.split(" ");
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			Matcher m = p.matcher(buf[i]);
			if (m.find()) {
				res.append(buf[i - 1]).append(" ").append(buf[i]);
				i++;
				for (; i < buf.length; i++) {
					res.append(" ").append(buf[i]);
				}
			}
		}
		return res.toString();
	}

	/**
	 * Parses the floor.
	 */
	private void parseFloor() {
		if (����.endsWith("��")) {
			���� = ����.substring(����.length() - 2);
		}
		if (����.endsWith("����")) {
			���� = "����";
		}
		if (����.endsWith("��ž")) {
			���� = "��ž";
		}
	}

	/**
	 * Parses the type.
	 * 
	 * @return the string
	 */
	private String parseType() {
		if (����.contains("����") || ����.contains("��") || ����.contains("��")) {
			return "���ÿܰǹ�";
		}
		return "���ⱸ";
	}
}
