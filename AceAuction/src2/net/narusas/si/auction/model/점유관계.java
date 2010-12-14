package net.narusas.si.auction.model;

public class 점유관계 {
	Long id;
	String 소재지;
	String 점유관계;
	String 기타;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String get소재지() {
		return 소재지;
	}

	public void set소재지(String 소재지) {
		this.소재지 = 소재지;
	}

	public String get점유관계() {
		return 점유관계;
	}

	public void set점유관계(String 점유관계) {
		this.점유관계 = 점유관계;
	}

	public String get기타() {
		return 기타;
	}

	public void set기타(String 기타) {
		this.기타 = 기타;
	}

}
