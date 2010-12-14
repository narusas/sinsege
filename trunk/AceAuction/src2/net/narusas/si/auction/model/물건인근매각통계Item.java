package net.narusas.si.auction.model;

import net.narusas.si.auction.fetchers.Sheet;

public class 물건인근매각통계Item {
	물건 물건;
	Integer id;
	String 기간;
	String 매각건수;
	String 평균감정가;
	String 평균매각가;
	String 매각가율;
	String 평균유찰회수;

	public 물건인근매각통계Item() {
	}

	public 물건인근매각통계Item(물건 goods, Sheet sheet, int row) {
		물건 = goods;
		기간 = sheet.valueAt(row, 0);
		매각건수 = sheet.valueAt(row, 1);
		평균감정가 = sheet.valueAt(row, 2);
		평균매각가 = sheet.valueAt(row, 3);
		매각가율 = sheet.valueAt(row, 4);
		평균유찰회수 = sheet.valueAt(row, 5);

	}

	public 물건 get물건() {
		return 물건;
	}

	public void set물건(물건 물건) {
		this.물건 = 물건;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String get기간() {
		return 기간;
	}

	public void set기간(String 기간) {
		this.기간 = 기간;
	}

	public String get매각건수() {
		return 매각건수;
	}

	public void set매각건수(String 매각건수) {
		this.매각건수 = 매각건수;
	}

	public String get평균감정가() {
		return 평균감정가;
	}

	public void set평균감정가(String 평균감정가) {
		this.평균감정가 = 평균감정가;
	}

	public String get평균매각가() {
		return 평균매각가;
	}

	public void set평균매각가(String 평균매각가) {
		this.평균매각가 = 평균매각가;
	}

	public String get매각가율() {
		return 매각가율;
	}

	public void set매각가율(String 매각가율) {
		this.매각가율 = 매각가율;
	}

	public String get평균유찰회수() {
		return 평균유찰회수;
	}

	public void set평균유찰회수(String 평균유찰회수) {
		this.평균유찰회수 = 평균유찰회수;
	}

	public void merge(물건인근매각통계Item item) {
		this.매각가율 = item.매각가율;
		this.매각건수 = item.매각건수;
		this.평균감정가 = item.평균감정가;
		this.평균매각가 = item.평균매각가;
		this.평균유찰회수 = item.평균유찰회수;
	}

	@Override
	public String toString() {
		return "물건인근매각통계Item [id=" + id + ", 기간=" + 기간 + ", 매각가율=" + 매각가율 + ", 매각건수=" + 매각건수 + ", 물건=" + 물건
				+ ", 평균감정가=" + 평균감정가 + ", 평균매각가=" + 평균매각가 + ", 평균유찰회수=" + 평균유찰회수 + "]";
	}

}
