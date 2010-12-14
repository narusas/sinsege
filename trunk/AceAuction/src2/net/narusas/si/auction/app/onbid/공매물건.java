package net.narusas.si.auction.app.onbid;

public class 공매물건 {
	Long id;
	private String 상위용도;
	private String 하위용도;
	private String 입찰번호;
	private String 소재지;
	private String 물건관리번호;
	private String 처분정보1;
	private String 처분정보2;
	private String 감정가;
	private String 최초예정가액;
	private String 최저입찰가;
	private String 물건상태;
	private String 유찰회수;
	private String 내역;
	공매일정 공매일정;
	private String url;
	private String 낙찰가;
	private String 개찰결과;

	public 공매일정 get공매일정() {
		return 공매일정;
	}

	public void set공매일정(공매일정 공매일정) {
		this.공매일정 = 공매일정;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String get소재지() {
		return 소재지;
	}

	public String get물건관리번호() {
		return 물건관리번호;
	}

	public String get처분정보1() {
		return 처분정보1;
	}

	public String get처분정보2() {
		return 처분정보2;
	}

	public String get감정가() {
		return 감정가;
	}

	public String get최초예정가액() {
		return 최초예정가액;
	}

	public String get최저입찰가() {
		return 최저입찰가;
	}

	public String get물건상태() {
		return 물건상태;
	}

	public String get유찰회수() {
		return 유찰회수;
	}

	public String get내역() {
		return 내역;
	}

	public void set상위용도(String 상위용도) {
		this.상위용도 = 상위용도;
	}

	public void set하위용도(String 하위용도) {
		this.하위용도 = 하위용도;

	}

	public void set입찰번호(String 입찰번호) {
		this.입찰번호 = 입찰번호;
	}

	public String get상위용도() {
		return 상위용도;
	}

	public String get하위용도() {
		return 하위용도;
	}

	public String get입찰번호() {
		return 입찰번호;
	}

	public void set소재지(String 소재지) {
		this.소재지 = 소재지;
	}

	public void set물건관리번호(String 물건관리번호) {
		this.물건관리번호 = 물건관리번호;
	}

	public void set처분정보1(String 처분정보1) {
		this.처분정보1 = 처분정보1;
	}

	public void set처분정보2(String 처분정보2) {
		this.처분정보2 = 처분정보2;
	}

	public void set감정가(String 감정가) {
		this.감정가 = 감정가;
	}

	public void set최초예정가액(String 최초예정가액) {
		this.최초예정가액 = 최초예정가액;
	}

	public void set최저입찰가(String 최저입찰가) {
		this.최저입찰가 = 최저입찰가;
	}

	public void set물건상태(String 물건상태) {
		this.물건상태 = 물건상태;
	}

	public void set유찰회수(String 유찰회수) {
		this.유찰회수 = 유찰회수;
	}

	public void set내역(String 내역) {
		this.내역 = 내역;
	}


	@Override
	public String toString() {
		return "공매물건 [상위용도=" + 상위용도 + ", 하위용도=" + 하위용도 + ", 입찰번호=" + 입찰번호 + ", 소재지=" + 소재지 + ", 물건관리번호=" + 물건관리번호
				+ ", 처분정보1=" + 처분정보1 + ", 처분정보2=" + 처분정보2 + ", 감정가=" + 감정가 + ", 최초예정가액=" + 최초예정가액 + ", 최저입찰가=" + 최저입찰가
				+ ", 물건상태=" + 물건상태 + ", 유찰회수=" + 유찰회수 + ", 내역=" + 내역 + ", 개찰결과=" + 개찰결과 + ", 낙찰가=" + 낙찰가 + "]";
	}

	public void merge(공매물건 item) {
		상위용도 = item.상위용도;
		하위용도 = item.하위용도;
		입찰번호 = item.입찰번호;
		소재지 = item.소재지;
		물건관리번호 = item.물건관리번호;
		처분정보1 = item.처분정보1;
		처분정보2 = item.처분정보2;
		감정가 = item.감정가;
		최초예정가액 = item.최초예정가액;
		최저입찰가 = item.최저입찰가;
		물건상태 = item.물건상태;
		유찰회수 = item.유찰회수;
		내역 = item.내역;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void set낙찰가(String 낙찰가) {
		this.낙찰가 = 낙찰가;

	}

	public String get낙찰가() {
		return 낙찰가;
	}

	public void set개찰결과(String 개찰결과) {
		this.개찰결과 = 개찰결과;
		
	}

	public String get개찰결과() {
		return 개찰결과;
	}

}
