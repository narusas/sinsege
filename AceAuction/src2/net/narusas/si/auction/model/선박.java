package net.narusas.si.auction.model;

public class 선박 {
	long id;
	물건 물건;
	String 목록번호;
	String 상세내역;

	public 선박() {
	}

	public 선박(물건 물건, String 목록번호, String 상세내역) {
		super();
		this.물건 = 물건;
		this.목록번호 = 목록번호;
		this.상세내역 = 상세내역;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public 물건 get물건() {
		return 물건;
	}

	public void set물건(물건 물건) {
		this.물건 = 물건;
	}

	public String get목록번호() {
		return 목록번호;
	}

	public void set목록번호(String 목록번호) {
		this.목록번호 = 목록번호;
	}

	public String get상세내역() {
		return 상세내역;
	}

	public void set상세내역(String 상세내역) {
		this.상세내역 = 상세내역;
	}

}
