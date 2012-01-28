package net.narusas.si.auction.model;

public class 건물 {

	private 물건 물건;
	private String 주소;
	private String 층형;
	private String 구조;
	private String 현황주소;
	private String 비고;
	String 매각지분비고;
	private String detail;
	Long id;
	Integer 목록번호;

	public 건물() {
	}

	public 건물(물건 물건, String 주소, String 층형, String string2, String 면적, String 비고, String detail, String 매각지분비고) {
		this.물건 = 물건;
		this.주소 = 주소;
		this.층형 = 층형;
		현황주소 = 면적;
		this.비고 = 비고;
		this.detail = detail;
		this.매각지분비고 = 매각지분비고;
		구조 = "";
	}

	public 건물(물건 물건, String 주소, String 층형, String 구조, String 현황주소, String detail, String comment) {
		this.물건 = 물건;
		목록번호 = 물건.get물건번호();
		this.주소 = 주소;
		this.층형 = 층형;
		this.구조 = 구조;
		this.현황주소 = 현황주소;
		this.detail = detail;
		this.비고 = "";
		this.매각지분비고 = comment;
	}

	@Override
	public String toString() {
		return "건물=[" + 층형 + "," + 구조 + "," + 현황주소 + "," + 비고 + "]";
	}

	public boolean match(String[] texts) {

		for (String text : texts) {
			boolean res = (구조 != null && 구조.contains(text)) || (층형 != null && 층형.contains(text))
					|| (detail != null && detail.contains(text));
			if (res == false) {
				return false;
			}
		}
		return true;
	}

	public 물건 get물건() {
		return 물건;
	}

	public void set물건(물건 물건) {
		this.물건 = 물건;
	}

	public String get주소() {
		return 주소;
	}

	public void set주소(String 주소) {
		this.주소 = 주소;
	}

	public String get층형() {
		return 층형;
	}

	public void set층형(String 층형) {
		this.층형 = 층형;
	}

	public String get구조() {
		return 구조;
	}

	public void set구조(String 구조) {
		this.구조 = 구조;
	}

	public String get현황주소() {
		if (현황주소 != null) {
			현황주소= 현황주소.replaceAll("㎡","");
		}
		return 현황주소;
	}

	public void set현황주소(String 현황주소) {
		this.현황주소 = 현황주소;
	}

	public String get비고() {
		return 비고;
	}

	public void set비고(String 비고) {
		this.비고 = 비고;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer get목록번호() {
		return 목록번호;
	}

	public void set목록번호(Integer 목록번호) {
		this.목록번호 = 목록번호;
	}

	public String get매각지분비고() {
		return 매각지분비고;
	}

	public void set매각지분비고(String 매각지분비고) {
		this.매각지분비고 = 매각지분비고;
	}

}
