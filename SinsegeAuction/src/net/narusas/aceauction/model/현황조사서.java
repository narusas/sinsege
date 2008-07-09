/*
 * 
 */
package net.narusas.aceauction.model;

// TODO: Auto-generated Javadoc
/**
 * The Class 현황조사서.
 */
public class 현황조사서 {

	/** The bub_cd. */
	private final String bub_cd;

	/** The mo_sa_no. */
	private final String mo_sa_no;

	/** The relsa info. */
	private final String relsaInfo;

	/** The sa_no. */
	private final String sa_no;

	/** The sa list_ has hyunjo. */
	private final String saList_HasHyunjo;

	/** The sano_ has hyunjo. */
	private final String sano_HasHyunjo;

	/** The user sa list_ has hyunjo. */
	private final String userSaList_HasHyunjo;

	/**
	 * Instantiates a new 현황조사서.
	 * 
	 * @param bub_no the bub_no
	 * @param sa_no the sa_no
	 * @param sa_display the sa_display
	 * @param 중복_sa_display the 중복_sa_display
	 * @param saList_HasHyunjo the sa list_ has hyunjo
	 * @param userSaList_HasHyunjo the user sa list_ has hyunjo
	 * @param sano_HasHyunjo the sano_ has hyunjo
	 */
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

	/**
	 * Gets the bub_cd.
	 * 
	 * @return the bub_cd
	 */
	public String getBub_cd() {
		return bub_cd;
	}

	/**
	 * Gets the mo_sa_no.
	 * 
	 * @return the mo_sa_no
	 */
	public String getMo_sa_no() {
		return mo_sa_no;
	}

	/**
	 * Gets the relsa info.
	 * 
	 * @return the relsa info
	 */
	public String getRelsaInfo() {
		return relsaInfo;
	}

	/**
	 * Gets the sa_no.
	 * 
	 * @return the sa_no
	 */
	public String getSa_no() {
		return sa_no;
	}

	/**
	 * Gets the sa list_ has hyunjo.
	 * 
	 * @return the sa list_ has hyunjo
	 */
	public String getSaList_HasHyunjo() {
		return saList_HasHyunjo;
	}

	/**
	 * Gets the sano_ has hyunjo.
	 * 
	 * @return the sano_ has hyunjo
	 */
	public String getSano_HasHyunjo() {
		return sano_HasHyunjo;
	}

	/**
	 * Gets the user sa list_ has hyunjo.
	 * 
	 * @return the user sa list_ has hyunjo
	 */
	public String getUserSaList_HasHyunjo() {
		return userSaList_HasHyunjo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "현황조사서=" + bub_cd + "," + sa_no + "," + mo_sa_no + "," + relsaInfo + ","
				+ saList_HasHyunjo + "," + userSaList_HasHyunjo + "," + sano_HasHyunjo;
	}

}
