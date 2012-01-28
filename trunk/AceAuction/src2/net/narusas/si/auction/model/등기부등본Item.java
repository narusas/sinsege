package net.narusas.si.auction.model;

import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.pdf.attested.TextPosition;

public class 등기부등본Item {
	Long id;
	private 물건 물건;
	private 등기부등본 등기부등본;
	Integer 권리순위;
	String 순번, 권리종류, 접수일, 권리자, 금액, 소멸기준, 비고;
	private String 대상소유자;
	private Long 접수번호;

	public 등기부등본Item() {
	}

	public 등기부등본Item(물건 물건, 등기부등본 등기부등본, String right_type, String accept_date, String right_person,
			String right_price, String expiration, String comment, int no) {
		this.물건 = 물건;
		this.등기부등본 = 등기부등본;
		권리종류 = right_type;
		접수일 = accept_date;
		권리자 = right_person;
		금액 = right_price;
		소멸기준 = expiration;
		비고 = comment;
		권리순위 = no;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer get권리순위() {
		return 권리순위;
	}

	public void set권리순위(Integer 권리순위) {
		this.권리순위 = 권리순위;
	}

	public 물건 get물건() {
		return 물건;
	}

	public void set물건(물건 물건) {
		this.물건 = 물건;
	}

	public 등기부등본 get등기부등본() {
		return 등기부등본;
	}

	public void set등기부등본(등기부등본 등기부등본) {
		this.등기부등본 = 등기부등본;
	}

	public String get권리종류() {
		return 권리종류;
	}

	final static String[][] 권리종류정리 = new String[][] {//
	{ "강제경매개시결정", "강제경매" },//
			{ "근저당권설정", "근저당권" },//		
			{ "지상권설정", "지상권" },//		
			{ "전세권설정", "전세권" },//		
			{ "임의경매개시결정", "임의경매" },//		
			{ "소유권이전청구권가등기", "가등기" },//		
			{ "말소예고등기", "예고등기" },//		
	};

	public void set권리종류(String value) {
		value = value.trim();
		for (String[] pair : 권리종류정리) {
			if (pair[0].equals(value)) {
				this.권리종류 = pair[1];
				return;
			}
		}
		this.권리종류 = value;
	}

	public String get접수일() {
		return 접수일;
	}

	public void set접수일(String 접수일) {
		this.접수일 = 접수일;
	}

	public String get권리자() {
		return 권리자;
	}

	public void set권리자(String value) {
		value = value.replaceAll("\n", "").trim();
		value = value.replaceAll("\\(주\\)", "").trim();
		if (value.contains("(")) {
			String temp = value.substring(0, value.indexOf("("));
			this.권리자 = temp.replaceAll("\n", " ");//value.replaceAll("(\\([^\\)]+\\))", "");
			return;
		}
		this.권리자 = value;
	}

	public String get금액() {
		return 금액;
	}

	public void set금액(String 금액) {
		this.금액 = 금액;
	}

	public String get소멸기준() {
		return 소멸기준;
	}

	public void set소멸기준(String 소멸기준) {
		this.소멸기준 = 소멸기준;
	}

	public String get비고() {
		return 비고;
	}

	public void set비고(String 비고) {
		this.비고 = 비고;
	}

	// @Override
	// public String toString() {
	// return "[right=" + 권리종류 + ",접수일=" + 접수일 + ",권리자=" + 권리자 + ",금액=" + 금액 +
	// ",대상소유자=" + 대상소유자 + ",비고=" + 비고 + "]";
	// }

	public void set대상소유자(String 대상소유자) {
		String temp = 대상소유자.replace(" ", "").replace("\n", "").replace("\r", "");
		this.대상소유자 = temp;

	}

	@Override
	public String toString() {
		return "[접수일=" + 접수일 + ", 접수번호=" + 접수번호 + ", 순번=" + 순번 + ", 권리자=" + 권리자 + ", 권리종류=" + 권리종류 + ", 금액=" + 금액
				+ ", 대상소유자=" + 대상소유자 + ", 소멸기준=" + 소멸기준 + ", 비고=" + 비고 + "]";
	}

	public String get대상소유자() {
		return 대상소유자;
	}

	public String get순번() {
		return 순번;
	}

	public void set순번(String 순번) {
		this.순번 = 순번;
	}

	public boolean is부기등본() {
		return 순번.contains("-");
	}

	public boolean is변경등기() {
		return 권리종류.endsWith("이전") || 권리종류.endsWith("변경") || 권리종류.endsWith("경정");
	}

	public void updateFrom부기등기(등기부등본Item target) {
		if (target.금액 != null && !target.금액.equals("NONE")) {
			set금액(target.금액);
		}

		if (target.권리자 != null) {
			set권리자(target.권리자);
		}

	}

	boolean contains(String source, String[] targets) {
		for (String target : targets) {
			if (source.contains(target)) {
				return true;
			}
		}
		return false;
	}

	String[] 무시종류 = { "압류", "가압류", "일부이전", "질권", "가처분", "피보전권리", "목" };

	public boolean is무시종류(boolean 예고등기무시) {
		if (contains(권리종류, 무시종류)) {
			return true;
		}

		if (예고등기무시) {
			return 권리종류.contains("예고등기");
		}
		return false;

	}

	public void set접수번호(Long 접수번호) {
		this.접수번호 = 접수번호;

	}

	public Long get접수번호() {
		return 접수번호;
	}

	static Pattern datePattern = Pattern.compile("(\\d+)년\\s*(\\d+)월\\s*(\\d+)일");

	public Date get접수일AsDate() {
		if (접수일 == null) {
			return new Date();
		}
		Matcher m = datePattern.matcher(접수일);
		if (m.find() == false) {
			return new Date();
		}
		int year = Integer.parseInt(m.group(1));
		int month = Integer.parseInt(m.group(2));
		int day = Integer.parseInt(m.group(3));
		return new Date(year, month, day);
	}

	final static String[] 소멸기준종류 = new String[] { "근저당권", "저당권", "압류", "가압류", "강제경매", "임의경매" };

	public boolean is소멸기준종류() {
		return contains(권리종류, 소멸기준종류);
	}

	public boolean is예고등기() {
		return 권리종류.contains("예고등기");
	}
}
