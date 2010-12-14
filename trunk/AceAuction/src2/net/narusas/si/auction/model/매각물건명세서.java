package net.narusas.si.auction.model;

public class 매각물건명세서 {
	Long id;
	물건 물건;
	String 점유인, 당사자구분, 점유부분, 점유기간, 보증금, 차임, 전입일자, 확정일자;
	private String 소재지;
	private String 물건번호;
	private String 정보출처;
	private String 배당요구;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public 물건 get물건() {
		return 물건;
	}

	public void set물건(물건 물건) {
		this.물건 = 물건;
	}

	public String get점유인() {
		return 점유인;
	}

	public void set점유인(String 점유인) {
		this.점유인 = 점유인;
	}

	public String get당사자구분() {
		return 당사자구분;
	}

	public void set당사자구분(String 당사자구분) {
		this.당사자구분 = 당사자구분;
	}

	public String get점유부분() {
		return 점유부분;
	}

	public void set점유부분(String 점유부분) {
		this.점유부분 = 점유부분;
	}

	public String get점유기간() {
		return 점유기간;
	}

	public void set점유기간(String 점유기간) {
		this.점유기간 = 점유기간;
	}

	public String get보증금() {
		return 보증금;
	}

	public void set보증금(String 보증금) {
		this.보증금 = 보증금;
	}

	public String get차임() {
		return 차임;
	}

	public void set차임(String 차임) {
		this.차임 = 차임;
	}

	public String get전입일자() {
		return 전입일자;
	}

	public void set전입일자(String 전입일자) {
		this.전입일자 = 전입일자;
	}

	public String get확정일자() {
		return 확정일자;
	}

	public void set확정일자(String 확정일자) {
		this.확정일자 = 확정일자;
	}

	public void set소재지(String 소재지) {
		this.소재지 = 소재지;

	}

	public String get소재지() {
		return 소재지;
	}

	public void set물건번호(String 물건번호) {
		this.물건번호 = 물건번호;

	}

	public String get물건번호() {
		return 물건번호;
	}

	@Override
	public String toString() {
		return "{점유인=" + 점유인 + ",당사자구분=" + 당사자구분 + ",점유부분=" + 점유부분 + ",점유기간=" + 점유기간 + ",보증금=" + 보증금 + ",차임="
				+ 차임 + ",전입일자=" + 전입일자 + ",확정일자=" + 확정일자 + ",배당요구=" + 배당요구 + ",정보출처=" + 정보출처 + "}";
	}

	public void set정보출처(String 정보출처) {
		this.정보출처 = 정보출처;
	}

	public String get정보출처() {
		return 정보출처;
	}

	public void set배당요구(String 배당요구) {
		this.배당요구 = 배당요구;
	}

	public String get배당요구() {
		return 배당요구;
	}

}
