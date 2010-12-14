package net.narusas.si.auction.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class 담당계 {
	private long id;
	private 법원 소속법원;

	private String time;

	private String 담당계이름;

	private int 담당계코드;

	private Date 매각기일;

	private Date 입찰_끝날자;

	private Date 입찰_시작날자;

	private String 매각방법;

	private String 장소;

	public 담당계() {
	}

	public 담당계(법원 court, int chargeCode, String name, Date eventDay, String location, String method,
			String time2) {
		소속법원 = court;
		담당계코드 = chargeCode;
		담당계이름 = name;
		매각기일 = eventDay;
		장소 = location;
		매각방법 = method;
		time = time2;
	}

	public 담당계(법원 court, int chargeCode, String name, java.sql.Date eventDay, java.sql.Date startDay,
			java.sql.Date endDay, String location, String method, String time2) {
		소속법원 = court;
		담당계코드 = chargeCode;
		담당계이름 = name;
		매각기일 = eventDay;
		입찰_시작날자 = startDay;
		입찰_끝날자 = endDay;
		장소 = location;
		매각방법 = method;
		time = time2;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public 법원 get소속법원() {
		return 소속법원;
	}

	public void set소속법원(법원 소속법원) {
		this.소속법원 = 소속법원;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String get담당계이름() {
		return 담당계이름;
	}

	public void set담당계이름(String 담당계이름) {
		this.담당계이름 = 담당계이름;
	}

	public int get담당계코드() {
		return 담당계코드;
	}

	public void set담당계코드(int 담당계코드) {
		this.담당계코드 = 담당계코드;
	}

	public Date get매각기일() {
		return 매각기일;
	}

	public void set매각기일(Date 매각기일) {
		this.매각기일 = 매각기일;
	}

	public Date get입찰_끝날자() {
		return 입찰_끝날자;
	}

	public void set입찰_끝날자(Date 입찰_끝날자) {
		this.입찰_끝날자 = 입찰_끝날자;
	}

	public Date get입찰_시작날자() {
		return 입찰_시작날자;
	}

	public void set입찰_시작날자(Date 입찰_시작날자) {
		this.입찰_시작날자 = 입찰_시작날자;
	}

	public String get매각방법() {
		return 매각방법;
	}

	public void set매각방법(String 매각방법) {
		this.매각방법 = 매각방법;
	}

	public String get장소() {
		return 장소;
	}

	public void set장소(String 장소) {
		this.장소 = 장소;
	}

	@Override
	public String toString() {
		SimpleDateFormat f = new SimpleDateFormat("MM.dd");
		return 담당계이름 +" "+f.format(get매각기일());//+"("+담당계코드+")";// +" (" + 소속법원.get법원명() +")";
	}

}
