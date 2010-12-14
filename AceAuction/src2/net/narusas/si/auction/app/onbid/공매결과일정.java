package net.narusas.si.auction.app.onbid;

public class 공매결과일정 {

	private String business_type;
	private String up_pageSize;
	private String up_pageIndex;
	private String up_orderfield;
	private String up_flag;
	private String strOpenState;
	private String 개찰일시;
	private String 자산구분;
	private String 부점명;
	private String auction_no;

	public void setLinkInof(String auction_no, String business_type, String up_pageSize, String up_pageIndex,
			String up_orderfield, String up_flag, String strOpenState) {
		this.auction_no = auction_no;
		this.business_type = business_type;
		this.up_pageSize = up_pageSize;
		this.up_pageIndex = up_pageIndex;
		this.up_orderfield = up_orderfield;
		this.up_flag = up_flag;
		this.strOpenState = strOpenState;

	}

	public String getBusiness_type() {
		return business_type;
	}

	public void setBusiness_type(String business_type) {
		this.business_type = business_type;
	}

	public String getUp_pageSize() {
		return up_pageSize;
	}

	public void setUp_pageSize(String up_pageSize) {
		this.up_pageSize = up_pageSize;
	}

	public String getUp_pageIndex() {
		return up_pageIndex;
	}

	public void setUp_pageIndex(String up_pageIndex) {
		this.up_pageIndex = up_pageIndex;
	}

	public String getUp_orderfield() {
		return up_orderfield;
	}

	public void setUp_orderfield(String up_orderfield) {
		this.up_orderfield = up_orderfield;
	}

	public String getUp_flag() {
		return up_flag;
	}

	public void setUp_flag(String up_flag) {
		this.up_flag = up_flag;
	}

	public String getStrOpenState() {
		return strOpenState;
	}

	public void setStrOpenState(String strOpenState) {
		this.strOpenState = strOpenState;
	}

	public void set개찰일시(String 개찰일시) {
		this.개찰일시 = 개찰일시;
	}

	public void set자산구분(String 자산구분) {
		this.자산구분 = 자산구분;
	}

	public void set부점명(String 부점명) {
		this.부점명 = 부점명;
	}

	public String get개찰일시() {
		return 개찰일시;
	}

	public String get자산구분() {
		return 자산구분;
	}

	public String get부점명() {
		return 부점명;
	}

	public String getLink() {
		return "/frontup/portal/result/control/result/getAuctionInfoKamco.do?AUCTION_NO=" + auction_no
				+ "&BUSINESS_TYPE=" + business_type + "&up_pageSize=" + up_pageSize + "&up_pageIndex=" + up_pageIndex
				+ "&up_orderfield=" + up_orderfield + "&up_flag=" + up_flag;
	}
	
	public String getLinke(int page){
		return "/frontup/portal/result/control/result/getAuctionInfoKamco.do?" +
				"pageSize=10" +
				"&pageIndex="+page+"" +
						"&ORDERFIELD=GOODS_NAME" +
						"&FLAG=ASC" +
						"&AUCTION_NO=" +auction_no+
						"&up_pageSize=10" +
						"&up_pageIndex=1" +
						"&up_orderfield=BID_OPEN_DATE" +
						"&up_flag=DESC" +
						"&AUCTION_STATUS_CODE=";
	}

	@Override
	public String toString() {
		return "공매결과일정 [부점명=" + 부점명 + ", 개찰일시=" + 개찰일시 + ", 자산구분=" + 자산구분 + ", link=" + getLink() + "]";
	}

}
