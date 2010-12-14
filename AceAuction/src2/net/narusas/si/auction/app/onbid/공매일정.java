package net.narusas.si.auction.app.onbid;

import java.util.List;

public class 공매일정 {
	Long id;
	private String 자산구분;
	private String 담당부점;
	private String 회차차수;
	private String 입찰기간;
	private String 개찰일시;
	private String announceNo;
	private String auctionSQ;
	private String auctionNo;
	private String auctionSubSQ;
	private String businessType;

	List<공매물건> 공매물건List;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void set자산구분(String 자산구분) {
		this.자산구분 = 자산구분;
	}

	public void set담당부점(String 담당부점) {
		this.담당부점 = 담당부점;
	}

	public void set회차차수(String 회차차수) {
		this.회차차수 = 회차차수;
	}

	public void set입찰기간(String 입찰기간) {
		this.입찰기간 = 입찰기간;
	}

	public void set개찰일시(String 개찰일시) {
		this.개찰일시 = 개찰일시;
	}

	public String get자산구분() {
		return 자산구분;
	}

	public String get담당부점() {
		return 담당부점;
	}

	public String get회차차수() {
		return 회차차수;
	}

	public String get입찰기간() {
		return 입찰기간;
	}

	public String get개찰일시() {
		return 개찰일시;
	}

	public void setLinkInfo(String ANNOUNCE_NO, String AUCTION_SQ, String AUCTION_NO, String AUCTION_SUB_SQ,
			String BUSINESS_TYPE) {

		announceNo = ANNOUNCE_NO;
		auctionSQ = AUCTION_SQ;
		auctionNo = AUCTION_NO;
		auctionSubSQ = AUCTION_SUB_SQ;
		businessType = BUSINESS_TYPE;

	}

	public String getUrl() {
		return "/frontup/portal/announce/control/announce/getAnnounceAuctionView.do?ANNOUNCE_NO=" + announceNo
				+ "&AUCTION_SQ=" + auctionSQ + "&C_CODE=" + auctionSQ + "&D_CODE=" + auctionSubSQ + "&AUCTION_SUB_SQ="
				+ auctionSubSQ + "&AUCTION_NO=" + auctionNo + "&BUSINESS_TYPE=" + businessType
				+ "&MENU_CODE=m0303000100";
	}

	@Override
	public String toString() {
		return "공매물건 [자산구분=" + 자산구분 + ", 담당부점=" + 담당부점 + ", 회차차수=" + 회차차수 + ", 입찰기간=" + 입찰기간 + ", 개찰일시=" + 개찰일시 + "]";
	}

	public void copyFromLast(공매일정 last) {
		if (last == null) {
			return;
		}

		if (자산구분 == null || "".equals(자산구분)) {
			자산구분 = last.자산구분;
		}
		if (담당부점 == null || "".equals(담당부점)) {
			담당부점 = last.담당부점;
		}
	}

	public List<공매물건> get공매물건List() {
		return 공매물건List;
	}

	public void set공매물건List(List<공매물건> 공매물건List) {
		this.공매물건List = 공매물건List;
	}

}
