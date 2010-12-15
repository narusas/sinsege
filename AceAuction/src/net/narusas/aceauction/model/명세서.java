package net.narusas.aceauction.model;


public class 명세서 {

	private final String 개요;

	private final String 권리;

	private final String 비고;

	private final String 비고란;

	private final Table 점유자;

	public 명세서(Table table, String 비고, String 권리, String 개요, String 비고란) {
		this.점유자 = table;
		this.비고 = 비고;
		this.권리 = 권리;
		this.개요 = 개요;
		this.비고란 = 비고란;

	}

	public String get개요() {
		return 개요;
	}

	public String get권리() {
		return 권리;
	}

	public String get비고() {
		return 비고;
	}

	public String get비고란() {
		return 비고란;
	}

	public Table get점유자() {
		return 점유자;
	}

}
