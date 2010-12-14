package net.narusas.si.auction.model;

public class 자동차 {
	long id;
	물건 물건;
	String 목록번호;
	String 감정가;
	String 차명;
	String 차종;
	String 등록번호;
	String 연식;
	String 제조사;
	String 연료종류;
	String 변속기;
	String 원동기형식;
	String 승인번호;
	String 차대번호;
	String 배기량;
	String 주행거리;
	String 상세내역;
	String 보관장소;

	public 자동차() {
	}

	public 자동차(물건 물건, String 목록번호, String 감정가, String 차명, String 차종, String 등록번호, String 연식, String 제조사, String 연료종류,
			String 변속기, String 원동기형식, String 승인번호, String 차대번호, String 배기량, String 주행거리, String 상세내역, String 보관장소) {
		super();
		this.물건 = 물건;
		this.목록번호 = 목록번호;
		this.감정가 = 감정가;
		this.차명 = 차명;
		this.차종 = 차종;
		this.등록번호 = 등록번호;
		this.연식 = 연식;
		this.제조사 = 제조사;
		this.연료종류 = 연료종류;
		this.변속기 = 변속기;
		this.원동기형식 = 원동기형식;
		this.승인번호 = 승인번호;
		this.차대번호 = 차대번호;
		this.배기량 = 배기량;
		this.주행거리 = 주행거리;
		this.상세내역 = 상세내역;
		this.보관장소 = 보관장소;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public 물건 get물건() {
		return 물건;
	}

	public void set물건(물건 물건) {
		this.물건 = 물건;
	}

	public String get목록번호() {
		return 목록번호;
	}

	public void set목록번호(String 목록번호) {
		this.목록번호 = 목록번호;
	}

	public String get감정가() {
		return 감정가;
	}

	public void set감정가(String 감정가) {
		this.감정가 = 감정가;
	}

	public String get차명() {
		return 차명;
	}

	public void set차명(String 차명) {
		this.차명 = 차명;
	}

	public String get차종() {
		return 차종;
	}

	public void set차종(String 차종) {
		this.차종 = 차종;
	}

	public String get등록번호() {
		return 등록번호;
	}

	public void set등록번호(String 등록번호) {
		this.등록번호 = 등록번호;
	}

	public String get연식() {
		return 연식;
	}

	public void set연식(String 연식) {
		this.연식 = 연식;
	}

	public String get제조사() {
		return 제조사;
	}

	public void set제조사(String 제조사) {
		this.제조사 = 제조사;
	}

	public String get연료종류() {
		return 연료종류;
	}

	public void set연료종류(String 연료종류) {
		this.연료종류 = 연료종류;
	}

	public String get변속기() {
		return 변속기;
	}

	public void set변속기(String 변속기) {
		this.변속기 = 변속기;
	}

	public String get원동기형식() {
		return 원동기형식;
	}

	public void set원동기형식(String 원동기형식) {
		this.원동기형식 = 원동기형식;
	}

	public String get승인번호() {
		return 승인번호;
	}

	public void set승인번호(String 승인번호) {
		this.승인번호 = 승인번호;
	}

	public String get차대번호() {
		return 차대번호;
	}

	public void set차대번호(String 차대번호) {
		this.차대번호 = 차대번호;
	}

	public String get배기량() {
		return 배기량;
	}

	public void set배기량(String 배기량) {
		this.배기량 = 배기량;
	}

	public String get주행거리() {
		return 주행거리;
	}

	public void set주행거리(String 주행거리) {
		this.주행거리 = 주행거리;
	}

	public String get상세내역() {
		return 상세내역;
	}

	public void set상세내역(String 상세내역) {
		this.상세내역 = 상세내역;
	}

	public String get보관장소() {
		return 보관장소;
	}

	public void set보관장소(String 보관장소) {
		this.보관장소 = 보관장소;
	}

}
