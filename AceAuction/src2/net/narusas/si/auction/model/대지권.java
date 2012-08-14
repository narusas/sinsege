package net.narusas.si.auction.model;

public class 대지권 {
	Long id;
	물건 물건;
	Integer 목록번호;
	String 주소, 지분비율, 면적, 매각지분비고, 건물번호;
	Long 가격;
	private String 공시지가;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public 물건 get물건() {
		return 물건;
	}

	public void set물건(물건 물건) {
		this.물건 = 물건;
	}

	public Integer get목록번호() {
		return 목록번호;
	}

	public void set목록번호(Integer 목록번호) {
		this.목록번호 = 목록번호;
	}

	public String get주소() {
		return 주소;
	}

	public void set주소(String 주소) {
		this.주소 = 주소;
	}

	public String get지분비율() {
		return 지분비율;
	}

	public void set지분비율(String 지분비율) {
		this.지분비율 = 지분비율;
	}

	public String get면적() {
		return 면적;
	}

	public void set면적(String 면적) {
		this.면적 = 면적;
	}

	public String get매각지분비고() {
		return 매각지분비고;
	}

	public void set매각지분비고(String 매각지분비고) {
		this.매각지분비고 = 매각지분비고;
	}

	public Long get가격() {
		return 가격;
	}

	public void set가격(Long 가격) {
		this.가격 = 가격;
	}

	public String get건물번호() {
		return 건물번호;
	}

	public void set건물번호(String 건물번호) {
		this.건물번호 = 건물번호;
	}

	public void set공시지가(String 공시지가) {
		this.공시지가 = 공시지가;
		
	}

	public String get공시지가() {
		return 공시지가;
	}

}
