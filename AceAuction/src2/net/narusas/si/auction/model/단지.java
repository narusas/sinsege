package net.narusas.si.auction.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class 단지 {

	private HashMap<String, String> pairs;
	private String 이름;
	private String 단지규모;
	private String 사용승인월;
	private String 면적분포;
	private String 매매;
	private String 전세;
	private Long code;

	// Info
	private String 소재지;
	private String 시공사;
	private String 난방방식;
	private String 교통환경;
	private String 교육환경;
	private String 주거환경;
	private String 단지특성;
	private String 관리사무소;
	private HashMap<String, String> infoPairs;
	List<단지평형> 평형목록;

	지역 지역_도;
	지역 지역_시군구;
	지역 지역_동읍면;

	public 단지() {
	}

	public 단지(HashMap<String, String> pairs) {
		this.pairs = pairs;
		이름 = pairs.get("APTS_NAME");
		단지규모 = pairs.get("DANJI_AREA");
		사용승인월 = pairs.get("COMPLT_MONTH");
		면적분포 = pairs.get("SP_USEAREA_MIN") + "~" + pairs.get("SP_USEAREA_MAX");
		매매 = pairs.get("SA_PRICE_M2");
		전세 = pairs.get("RA_PRICE_M2");
		code = Long.parseLong(pairs.get("APTS_CODE"));
	}

	public 지역 get지역_도() {
		return 지역_도;
	}

	public void set지역_도(지역 지역_도) {
		this.지역_도 = 지역_도;
	}

	public 지역 get지역_시군구() {
		return 지역_시군구;
	}

	public void set지역_시군구(지역 지역_시군구) {
		this.지역_시군구 = 지역_시군구;
	}

	public 지역 get지역_동읍면() {
		return 지역_동읍면;
	}

	public void set지역_동읍면(지역 지역_동읍면) {
		this.지역_동읍면 = 지역_동읍면;
	}

	public List<단지평형> get평형목록() {
		return 평형목록;
	}

	public void set평형목록(List<단지평형> 평형목록) {
		this.평형목록 = 평형목록;
	}

	public void add종류(단지평형 종류) {
		if (평형목록 == null) {
			평형목록 = new ArrayList<단지평형>();
		}
		평형목록.add(종류);
	}

	public void updateInfo(HashMap<String, String> infoPairs) {
		this.infoPairs = infoPairs;
		소재지 = infoPairs.get("JUSO");
		시공사 = infoPairs.get("APTS_COMP");
		난방방식 = infoPairs.get("HEAT_CODE_NM")+"["+infoPairs.get("FUEL_CODE_NM")+"]";
		교통환경 = infoPairs.get("APTS_BUS");
		교육환경 = infoPairs.get("APTS_SCHOOL");
		주거환경 = infoPairs.get("APTS_NEAR");
		단지특성 = infoPairs.get("APT_CONT");
		관리사무소 = infoPairs.get("MOFF_TEL");
	}

	@Override
	public String toString() {
		return "" + get이름() + "," + get단지규모() + "," + get사용승인월() + "," + get면적분포() + "," + get매매() + "," + get전세();
	}

	public String get이름() {
		return 이름;
	}

	public String get단지규모() {
		return 단지규모;
	}

	public String get사용승인월() {
		return 사용승인월;
	}

	public String get면적분포() {
		return 면적분포;
	}

	public String get매매() {
		return 매매;
	}

	public String get전세() {
		return 전세;
	}

	public void set이름(String 이름) {
		this.이름 = 이름;
	}

	public void set단지규모(String 단지규모) {
		this.단지규모 = 단지규모;
	}

	public void set사용승인월(String 사용승인월) {
		this.사용승인월 = 사용승인월;
	}

	public void set면적분포(String 면적분포) {
		this.면적분포 = 면적분포;
	}

	public void set매매(String 매매) {
		this.매매 = 매매;
	}

	public void set전세(String 전세) {
		this.전세 = 전세;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	// //////

	public String get소재지() {
		return 소재지;
	}

	public void set소재지(String 소재지) {
		this.소재지 = 소재지;
	}

	public String get시공사() {
		return 시공사;
	}

	public void set시공사(String 시공사) {
		this.시공사 = 시공사;
	}

	public String get난방방식() {
		return 난방방식;
	}

	public void set난방방식(String 난방방식) {
		this.난방방식 = 난방방식;
	}

	public String get교통환경() {
		return 교통환경;
	}

	public void set교통환경(String 교통환경) {
		this.교통환경 = 교통환경;
	}

	public String get교육환경() {
		return 교육환경;
	}

	public void set교육환경(String 교육환경) {
		this.교육환경 = 교육환경;
	}

	public String get주거환경() {
		return 주거환경;
	}

	public void set주거환경(String 주거환경) {
		this.주거환경 = 주거환경;
	}

	public String get단지특성() {
		return 단지특성;
	}

	public void set단지특성(String 단지특성) {
		this.단지특성 = 단지특성;
	}

	public String get관리사무소() {
		return 관리사무소;
	}

	public void set관리사무소(String 관리사무소) {
		this.관리사무소 = 관리사무소;
	}

}
