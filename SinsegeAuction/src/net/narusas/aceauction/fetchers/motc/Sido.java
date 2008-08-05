package net.narusas.aceauction.fetchers.motc;

import java.util.ArrayList;

public class Sido {
	static ArrayList<Sido> sidos = new ArrayList<Sido>();
	String name;

	String code;

	public Sido(String name, String code) {
		super();
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return name + "(" + code + ")";
	}

	static {

		sidos.add(new Sido("����Ư����", "11"));
		sidos.add(new Sido("�λ걤����", "26"));
		sidos.add(new Sido("�뱸������", "27"));
		sidos.add(new Sido("��õ������", "28"));
		sidos.add(new Sido("���ֱ�����", "29"));
		sidos.add(new Sido("����������", "30"));
		sidos.add(new Sido("��걤����", "31"));
		sidos.add(new Sido("��⵵", "41"));
		sidos.add(new Sido("������", "41"));
		sidos.add(new Sido("��û�ϵ�", "43"));
		sidos.add(new Sido("��û����", "44"));
		sidos.add(new Sido("����ϵ�", "45"));
		sidos.add(new Sido("���󳲵�", "46"));
		sidos.add(new Sido("���ϵ�", "47"));
		sidos.add(new Sido("��󳲵�", "48"));
		sidos.add(new Sido("����Ư����ġ��", "50"));

	}
}