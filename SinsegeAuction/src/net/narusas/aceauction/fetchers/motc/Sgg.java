package net.narusas.aceauction.fetchers.motc;

public class Sgg {
	String name;

	String code;

	public Sgg(String code, String name) {
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
}