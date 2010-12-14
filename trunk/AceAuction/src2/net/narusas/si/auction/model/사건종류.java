package net.narusas.si.auction.model;

public enum 사건종류 {
	부동산(0), 선박(1), 자동차(2), 중장비(3);

	private final int id;

	private 사건종류(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static 사건종류 valueOf(int v) {
		switch (v) {
		case 0:
			return 부동산;

		case 1:
			return 선박;
		case 2:
			return 자동차;
		case 3:
			return 중장비;

		}
		return null;
	}

}
