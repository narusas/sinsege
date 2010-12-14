package net.narusas.si.auction.model;

import java.util.Date;

public class 매각기일결과 {
	String 매각가격, 매각결과;
	Date 매각결과결정일;
	String 매수인, 입찰수, 매각금액, 금액비율;

	public String get매수인() {
		return 매수인;
	}

	public void set매수인(String 매수인) {
		this.매수인 = 매수인;
	}

	public String get입찰수() {
		return 입찰수;
	}

	public void set입찰수(String 입찰수) {
		this.입찰수 = 입찰수;
	}

	public String get매각금액() {
		return 매각금액;
	}

	public void set매각금액(String 매각금액) {
		this.매각금액 = 매각금액;
	}

	public String get금액비율() {
		return 금액비율;
	}

	public void set금액비율(String 금액비율) {
		this.금액비율 = 금액비율;
	}

	public String get매각가격() {
		return 매각가격;
	}

	public void set매각가격(String 매각가격) {
		this.매각가격 = 매각가격;
	}

	public String get매각결과() {
		return 매각결과;
	}

	public void set매각결과(String 매각결과) {
		this.매각결과 = 매각결과;
	}

	public Date get매각결과결정일() {
		return 매각결과결정일;
	}

	public void set매각결과결정일(Date 매각결과결정일) {
		this.매각결과결정일 = 매각결과결정일;
	}

}
