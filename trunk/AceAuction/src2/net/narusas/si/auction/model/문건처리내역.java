package net.narusas.si.auction.model;

public class 문건처리내역 {
	Long id;
	String 접수일;
	String 접수내역;
	String 결과;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String get접수일() {
		return 접수일;
	}

	public void set접수일(String 접수일) {
		this.접수일 = 접수일;
	}

	public String get접수내역() {
		return 접수내역;
	}

	public void set접수내역(String 접수내역) {
		this.접수내역 = 접수내역;
	}

	public String get결과() {
		return 결과;
	}

	public void set결과(String 결과) {
		this.결과 = 결과;
	}

	@Override
	public String toString() {
		return "문건처리내역 [id=" + id + ", 접수일=" + 접수일 + ", 접수내역=" + 접수내역 + ", 결과=" + 결과 + "]";
	}
	

}
