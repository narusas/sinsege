package net.narusas.si.auction.model;

public class 송달내역 {
	Long id;
	String 송달일;
	String 송달내역;
	String 송달결과;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String get송달일() {
		return 송달일;
	}

	public void set송달일(String 송달일) {
		this.송달일 = 송달일;
	}

	public String get송달내역() {
		return 송달내역;
	}

	public void set송달내역(String 송달내역) {
		this.송달내역 = 송달내역;
	}

	public String get송달결과() {
		return 송달결과;
	}

	public void set송달결과(String 송달결과) {
		this.송달결과 = 송달결과;
	}

	@Override
	public String toString() {
		return "송달내역 [id=" + id + ", 송달일=" + 송달일 + ", 송달내역=" + 송달내역 + ", 송달결과=" + 송달결과 + "]";
	}

}
