package net.narusas.aceauction.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ���ÿܰǹ� {

	static final int NO = 0;

	static Pattern p = Pattern.compile("(\\d+[-,]*\\d*[\\S\\s]*)$");

	static final int ���ÿܰǹ����� = 2;

	static final int ���ÿܰǹ����� = 1;

	private final String address;

	private final int contains;

	private String type;

	private final String ����;

	private final String ����;

	private final int ���ǹ�ȣ;

	private final String �뵵;

	private String ����;

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

	@Override
	public boolean equals(Object arg0) {
		���ÿܰǹ� target = (���ÿܰǹ�) arg0;
		return �뵵.equals(target.�뵵) && ����.equals(target.����) && ����.equals(target.����)
				&& ���ǹ�ȣ == target.���ǹ�ȣ;
	}

	public String getAddress() {
		return address;
	}

	public String get����() {
		return ����;
	}

	public String get����() {
		return ����;
	}

	public int get���ǹ�ȣ() {
		return ���ǹ�ȣ;
	}

	public String get�뵵() {
		return �뵵;
	}

	public String get����() {
		return type;
	}

	public String get����() {
		return ����;
	}

	public int get���Կ���() {
		return contains;
	}

	public String get���Կ���String() {
		if (contains == 1) {
			return "����";
		}
		if (contains == 2) {
			return "����";
		}
		return "";
	}

	@Override
	public String toString() {
		return type + "[" + ���ǹ�ȣ + ",�ּ�=" + address + ",�뵵=" + �뵵 + ",����=" + ���� + ",����=" + ���� + "]";
	}

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

	private String parseType() {
		if (����.contains("����") || ����.contains("��") || ����.contains("��")) {
			return "���ÿܰǹ�";
		}
		return "���ⱸ";
	}
}
