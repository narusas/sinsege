package net.narusas.si.auction.model;

import java.util.HashMap;

public class 단지평형시세추이 {

	private HashMap<String, String> pairs;
	Long code;
	private String 매매하한가;
	private String 매매상한가;
	private String 전세하한가;
	private String 전세상한가;
	private String 조사일;
	private String 매매평균변동액;
	private String 전세평균변동액;
	단지평형 단지평형;

	public 단지평형시세추이(HashMap<String, String> parsePairs) {
		this.pairs = parsePairs;
		매매하한가 = pairs.get("SL_PRICE");
		매매상한가 = pairs.get("SU_PRICE");
		매매평균변동액 = pairs.get("SU_COLOR") + pairs.get("SU_CHANGE");
		전세하한가 = pairs.get("RL_PRICE");
		전세상한가 = pairs.get("RU_PRICE");
		전세평균변동액 = pairs.get("RU_COLOR") + pairs.get("RU_CHANGE");
		조사일 = pairs.get("APTP_DATE");

	}
	

	public Long getCode() {
		return code;
	}


	public void setCode(Long code) {
		this.code = code;
	}


	public 단지평형시세추이() {
	}

	public 단지평형 get단지평형() {
		return 단지평형;
	}

	public void set단지평형(단지평형 단지평형) {
		this.단지평형 = 단지평형;
	}

	public String get매매하한가() {
		return 매매하한가;
	}

	public void set매매하한가(String 매매하한가) {
		this.매매하한가 = 매매하한가;
	}

	public String get매매상한가() {
		return 매매상한가;
	}

	public void set매매상한가(String 매매상한가) {
		this.매매상한가 = 매매상한가;
	}

	public String get전세하한가() {
		return 전세하한가;
	}

	public void set전세하한가(String 전세하한가) {
		this.전세하한가 = 전세하한가;
	}

	public String get전세상한가() {
		return 전세상한가;
	}

	public void set전세상한가(String 전세상한가) {
		this.전세상한가 = 전세상한가;
	}

	public String get조사일() {
		return 조사일;
	}

	public void set조사일(String 조사일) {
		this.조사일 = 조사일;
	}

	public String get매매평균변동액() {
		return 매매평균변동액;
	}

	public void set매매평균변동액(String 매매평균변동액) {
		this.매매평균변동액 = 매매평균변동액;
	}

	public String get전세평균변동액() {
		return 전세평균변동액;
	}

	public void set전세평균변동액(String 전세평균변동액) {
		this.전세평균변동액 = 전세평균변동액;
	}

	@Override
	public String toString() {
		return "단지평형시세추이 [매매상한가=" + 매매상한가 + ", 매매평균변동액=" + 매매평균변동액 + ", 매매하한가=" + 매매하한가 + ", 전세상한가=" + 전세상한가
				+ ", 전세평균변동액=" + 전세평균변동액 + ", 전세하한가=" + 전세하한가 + ", 조사일=" + 조사일 + "]";
	}

}
