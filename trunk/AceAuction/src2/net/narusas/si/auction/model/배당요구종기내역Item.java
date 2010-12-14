package net.narusas.si.auction.model;

public class 배당요구종기내역Item {

	String 목록번호;
	String 소재지;
	String 배당요구종기일;

	public 배당요구종기내역Item(String 목록번호, String 소재지, String 배당요구종기일) {
		this.목록번호 = 목록번호;
		this.소재지 = 소재지;
		this.배당요구종기일 = 배당요구종기일;
	}

	public String get목록번호() {
		return 목록번호;
	}

	public void set목록번호(String 목록번호) {
		this.목록번호 = 목록번호;
	}

	public String get소재지() {
		return 소재지;
	}

	public void set소재지(String 소재지) {
		this.소재지 = 소재지;
	}

	public String get배당요구종기일() {
		return 배당요구종기일;
	}

	public void set배당요구종기일(String 배당요구종기일) {
		this.배당요구종기일 = 배당요구종기일;
	}

}
