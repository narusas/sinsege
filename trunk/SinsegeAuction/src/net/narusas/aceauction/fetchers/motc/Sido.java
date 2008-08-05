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

		sidos.add(new Sido("서울특별시", "11"));
		sidos.add(new Sido("부산광역시", "26"));
		sidos.add(new Sido("대구광역시", "27"));
		sidos.add(new Sido("인천광역시", "28"));
		sidos.add(new Sido("광주광역시", "29"));
		sidos.add(new Sido("대전광역시", "30"));
		sidos.add(new Sido("울산광역시", "31"));
		sidos.add(new Sido("경기도", "41"));
		sidos.add(new Sido("강원도", "41"));
		sidos.add(new Sido("충청북도", "43"));
		sidos.add(new Sido("충청남도", "44"));
		sidos.add(new Sido("전라북도", "45"));
		sidos.add(new Sido("전라남도", "46"));
		sidos.add(new Sido("경상북도", "47"));
		sidos.add(new Sido("경상남도", "48"));
		sidos.add(new Sido("제주특별자치도", "50"));

	}
}