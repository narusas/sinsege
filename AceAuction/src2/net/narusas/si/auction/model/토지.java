package net.narusas.si.auction.model;

public class 토지 {

	private 물건 물건;
	private String 주소;
	private String 목적;
	private String 목적2 = "";
	private String 면적;
	private String 매각지분;

	Long id;
	private Integer 목록번호;

	public 토지() {
	}

	public 토지(물건 물건, String 주소, String 목족, String 면적, String 매각지분) {
		this.물건 = 물건;
		목록번호 = 물건.get물건번호();
		this.주소 = 주소;
		this.목적 = 목족;
		this.면적 = 면적;
		this.매각지분 = 매각지분;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public 물건 get물건() {
		return 물건;
	}

	public String get주소() {
		return 주소;
	}

	public String get목적() {
		return 목적;
	}

	public String get면적() {
		return 면적;
	}

	public String get매각지분() {
		return 매각지분;
	}

	public Integer get목록번호() {
		return 목록번호;
	}

	public void set목록번호(Integer 목록번호) {
		this.목록번호 = 목록번호;
	}

	public void set물건(물건 물건) {
		this.물건 = 물건;
	}

	public void set주소(String 주소) {
		this.주소 = 주소;
	}

	public void set목적(String 목적) {
		this.목적 = 목적;
	}

	public void set면적(String 면적) {
		this.면적 = 면적;
	}

	public void set매각지분(String 매각지분) {
		this.매각지분 = 매각지분;
	}

	public boolean isTypeMatch(String[] keys) {
		for (String key : keys) {
			if (key.equals(get목적())) {
				return true;
			}
		}
		return false;
	}

	public String get목적2() {
		return 목적2;
	}

	public void set목적2(String 목적2) {
		this.목적2 = 목적2;
	}

}
