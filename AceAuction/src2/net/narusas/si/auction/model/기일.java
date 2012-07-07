package net.narusas.si.auction.model;


public class 기일 {
	Long id;
	String 기일;
	String 기일종류;
	String 기일장소;
	String 최저매각가격;
	String 기일결과;
	물건 물건;
	Integer 물건번호;
	private String 기간start;
	private String 기간end;

	public 기일() {
	}

	public 기일(String 기일2, String 기일종류2, String 기일장소2, String 최저매각가격2, String 기일결과2, String 기간start,
			String 기간end) {
		기일 = 기일2;
		기일종류 = 기일종류2;
		기일장소 = 기일장소2;
		최저매각가격 = 최저매각가격2;
		기일결과 = 기일결과2;
		this.기간start = 기간start;
		this.기간end = 기간end;
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

	public void set물건(물건 물건) {
		this.물건 = 물건;
	}

	public String get기일() {
		return 기일;
	}

	public void set기일(String 기일) {
		this.기일 = 기일;
	}

	public String get기일종류() {
		return 기일종류;
	}

	public void set기일종류(String 기일종류) {
		this.기일종류 = 기일종류;
	}

	public String get기일장소() {
		return 기일장소;
	}

	public void set기일장소(String 기일장소) {
		this.기일장소 = 기일장소;
	}

	public String get최저매각가격() {
		return 최저매각가격;
	}

	public void set최저매각가격(String 최저매각가격) {
		this.최저매각가격 = 최저매각가격;
	}

	public String get기일결과() {
		return 기일결과;
	}

	public void set기일결과(String 기일결과) {
		this.기일결과 = 기일결과;
	}

	public Integer get물건번호() {
		return 물건번호;
	}

	public void set물건번호(Integer 물건번호) {
		this.물건번호 = 물건번호;
	}

	public String get기간start() {
		return 기간start;
	}

	public void set기간start(String 기간start) {
		this.기간start = 기간start;
	}

	public String get기간end() {
		return 기간end;
	}

	public void set기간end(String 기간end) {
		this.기간end = 기간end;
	}

	@Override
	public String toString() {
		return "기일={" + 기일 + "," + 기일종류 + "," + 기일장소 + "," + 최저매각가격 + "," + 기일결과 + "}";
	}
}
