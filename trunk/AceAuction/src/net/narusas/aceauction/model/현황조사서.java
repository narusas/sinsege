package net.narusas.aceauction.model;

public class 현황조사서 {

	private final String bub_cd;

	private final String mo_sa_no;

	private final String relsaInfo;

	private final String sa_no;

	private final String saList_HasHyunjo;

	private final String sano_HasHyunjo;

	private final String userSaList_HasHyunjo;

	public 현황조사서(String bub_no, String sa_no, String sa_display, String 중복_sa_display,
			String saList_HasHyunjo, String userSaList_HasHyunjo, String sano_HasHyunjo) {
		this.bub_cd = bub_no;
		this.sa_no = sa_no;
		this.mo_sa_no = sa_display;
		this.relsaInfo = 중복_sa_display;
		this.saList_HasHyunjo = saList_HasHyunjo;
		this.userSaList_HasHyunjo = userSaList_HasHyunjo;
		this.sano_HasHyunjo = sano_HasHyunjo;
	}

	public String getBub_cd() {
		return bub_cd;
	}

	public String getMo_sa_no() {
		return mo_sa_no;
	}

	public String getRelsaInfo() {
		return relsaInfo;
	}

	public String getSa_no() {
		return sa_no;
	}

	public String getSaList_HasHyunjo() {
		return saList_HasHyunjo;
	}

	public String getSano_HasHyunjo() {
		return sano_HasHyunjo;
	}

	public String getUserSaList_HasHyunjo() {
		return userSaList_HasHyunjo;
	}

	@Override
	public String toString() {
		return "현황조사서=" + bub_cd + "," + sa_no + "," + mo_sa_no + "," + relsaInfo + ","
				+ saList_HasHyunjo + "," + userSaList_HasHyunjo + "," + sano_HasHyunjo;
	}

}
