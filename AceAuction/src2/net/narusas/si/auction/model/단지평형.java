package net.narusas.si.auction.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class 단지평형 {

	private HashMap<String, String> pairs;
	private String 전용면적;
	private String 세대수;
	private String 매매하한가;
	private String 매매상한가;
	private String 전세하한가;
	private String 전세상한가;
	private String code;
	List<단지평형시세추이> 추이목록;

	단지 단지;
	private String 전용면적상세;
	private String 현관;
	private String 대지면적;
	
	private String 방;
	private String 거실;
	private String 방겸거실;
	private String 욕실;
	private String 주방;
	

	public 단지평형() {
	}

	public 단지평형(단지 단지, HashMap<String, String> pairs) {
		this.단지 = 단지;
		this.pairs = pairs;
		code = 단지.getCode().longValue()+"_"+pairs.get("PYN_TYPE");
		전용면적 = pairs.get("SP_USEAREA");
		세대수 = pairs.get("SP_TOT_HOUSE");
		매매하한가 = pairs.get("SL_PRICE");
		매매상한가 = pairs.get("SU_PRICE");
		전세하한가 = pairs.get("RL_PRICE");
		전세상한가 = pairs.get("RU_PRICE");
	}
	
	public void update(HashMap<String, String> pairs){
/*
		dwr.engine.remote.handleCallback("0","0",{status:"OK",result:[
		{"SP_LIVING":1,"PYNIMG_YN":"Y","SP_TOT_HOUSE":84,"SP_LITCHEN":1,"SP_ROOMLIVING":0,"APTS_CODE":18371,PTYPE:null,"APT_TYPE_NM":"계단","SP_BATHROOM":2,"SP_USEAREA":134.29,"SP_FLD_AREA":0,"SP_ROOM":4,"PYN_TYPE":48},
		{"SP_LIVING":1,"PYNIMG_YN":"Y","SP_TOT_HOUSE":44,"SP_LITCHEN":1,"SP_ROOMLIVING":0,"APTS_CODE":18371,PTYPE:null,"APT_TYPE_NM":"계단","SP_BATHROOM":2,"SP_USEAREA":153.8,"SP_FLD_AREA":0,"SP_ROOM":4,"PYN_TYPE":55},
		{"SP_LIVING":1,"PYNIMG_YN":"Y","SP_TOT_HOUSE":84,"SP_LITCHEN":1,"SP_ROOMLIVING":0,"APTS_CODE":18371,PTYPE:"b","APT_TYPE_NM":"계단","SP_BATHROOM":2,"SP_USEAREA":170.32,"SP_FLD_AREA":0,"SP_ROOM":4,"PYN_TYPE":61}]});
*/	
		전용면적상세 = pairs.get("SP_USEAREA");
		현관  = pairs.get("APT_TYPE_NM");
		대지면적  = pairs.get("SP_FLD_AREA");
		방  = pairs.get("SP_ROOM");
		거실 = pairs.get("SP_LIVING");
		방겸거실 = pairs.get("SP_ROOMLIVING");
		욕실 = pairs.get("SP_BATHROOM");
		주방 = pairs.get("SP_LITCHEN");
		
	}
	
	public String getPYN_TYPE() {
		return pairs.get("PYN_TYPE");
	}

	public 단지 get단지() {
		return 단지;
	}

	public void set단지(단지 단지) {
		this.단지 = 단지;
	}

	@Override
	public String toString() {
		return code + "[" + pairs.toString() + "]";
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String get전용면적() {
		return 전용면적;
	}

	public void set전용면적(String 전용면적) {
		this.전용면적 = 전용면적;
	}

	public String get세대수() {
		return 세대수;
	}

	public void set세대수(String 세대수) {
		this.세대수 = 세대수;
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

	public List<단지평형시세추이> get추이목록() {
		return 추이목록;
	}

	public void set추이목록(List<단지평형시세추이> 추이목록) {
		this.추이목록 = 추이목록;
	}

	public void add시세추이(단지평형시세추이 단지평형시세추이) {
		if (추이목록 == null){
			추이목록 = new ArrayList<단지평형시세추이>();
		}
		추이목록.add(단지평형시세추이);
	}

	public String get전용면적상세() {
		return 전용면적상세;
	}

	public void set전용면적상세(String 전용면적상세) {
		this.전용면적상세 = 전용면적상세;
	}

	public String get현관() {
		return 현관;
	}

	public void set현관(String 현관) {
		this.현관 = 현관;
	}

	public String get대지면적() {
		return 대지면적;
	}

	public void set대지면적(String 대지면적) {
		this.대지면적 = 대지면적;
	}

	public String get방() {
		return 방;
	}

	public void set방(String 방) {
		this.방 = 방;
	}

	public String get거실() {
		return 거실;
	}

	public void set거실(String 거실) {
		this.거실 = 거실;
	}

	public String get방겸거실() {
		return 방겸거실;
	}

	public void set방겸거실(String 방겸거실) {
		this.방겸거실 = 방겸거실;
	}

	public String get욕실() {
		return 욕실;
	}

	public void set욕실(String 욕실) {
		this.욕실 = 욕실;
	}

	public String get주방() {
		return 주방;
	}

	public void set주방(String 주방) {
		this.주방 = 주방;
	}

}
