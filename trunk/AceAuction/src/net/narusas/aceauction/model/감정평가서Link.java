/*
 * 
 */
package net.narusas.aceauction.model;

// TODO: Auto-generated Javadoc
/**
 * 대법원 사이트의 HTML 링크에 대한 정보를 가지고 있는 객체.
 * 
 * @author narusas
 */
public class 감정평가서Link {

	/** The bub_cd. */
	private final String bub_cd;

	/** The gam_no. */
	private final String gam_no;

	/** The ibub_nm. */
	private final String ibub_nm;

	/** The mae_giil. */
	private final String mae_giil;

	/** The mo_sa_no. */
	private final String mo_sa_no;

	/** The relsa info. */
	private final String relsaInfo;

	/** The sa_no. */
	private final String sa_no;

	/** The sa list_ has gam. */
	private final String saList_HasGam;

	/** The sano_ has gam. */
	private final String sano_HasGam;

	/** The user sa list_ has gam. */
	private final String userSaList_HasGam;

	/** The user sano_ has gam. */
	private final String userSano_HasGam;

	/**
	 * Instantiates a new 감정평가서.
	 * 
	 * @param bub_cd the bub_cd
	 * @param sa_no the sa_no
	 * @param mo_sa_no the mo_sa_no
	 * @param relsaInfo the relsa info
	 * @param saList_HasGam the sa list_ has gam
	 * @param userSaList_HasGam the user sa list_ has gam
	 * @param sano_HasGam the sano_ has gam
	 * @param userSano_HasGam the user sano_ has gam
	 * @param ibub_nm the ibub_nm
	 * @param mae_giil the mae_giil
	 * @param gam_no the gam_no
	 */
	public 감정평가서Link(String bub_cd, String sa_no, String mo_sa_no,
			String relsaInfo, String saList_HasGam, String userSaList_HasGam,
			String sano_HasGam, String userSano_HasGam, String ibub_nm,
			String mae_giil, String gam_no) {
		this.bub_cd = bub_cd;
		this.sa_no = sa_no;
		this.mo_sa_no = mo_sa_no;
		this.relsaInfo = relsaInfo;
		this.saList_HasGam = saList_HasGam;
		this.userSaList_HasGam = userSaList_HasGam;
		this.sano_HasGam = sano_HasGam;
		this.userSano_HasGam = userSano_HasGam;
		this.ibub_nm = ibub_nm;
		this.mae_giil = mae_giil;
		this.gam_no = gam_no;
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
	 * Gets the gam_no.
	 * 
	 * @return the gam_no
	 */
	public String getGam_no() {
		return gam_no;
	}

	/**
	 * Gets the ibub_nm.
	 * 
	 * @return the ibub_nm
	 */
	public String getIbub_nm() {
		return ibub_nm;
	}

	/**
	 * Gets the mae_giil.
	 * 
	 * @return the mae_giil
	 */
	public String getMae_giil() {
		return mae_giil;
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
	 * Gets the sa list_ has gam.
	 * 
	 * @return the sa list_ has gam
	 */
	public String getSaList_HasGam() {
		return saList_HasGam;
	}

	/**
	 * Gets the sano_ has gam.
	 * 
	 * @return the sano_ has gam
	 */
	public String getSano_HasGam() {
		return sano_HasGam;
	}

	/**
	 * Gets the user sa list_ has gam.
	 * 
	 * @return the user sa list_ has gam
	 */
	public String getUserSaList_HasGam() {
		return userSaList_HasGam;
	}

	/**
	 * Gets the user sano_ has gam.
	 * 
	 * @return the user sano_ has gam
	 */
	public String getUserSano_HasGam() {
		return userSano_HasGam;
	}

	/**
	 * To query string.
	 * 
	 * @return the string
	 */
	public String toQueryString() {
		return "bub_cd=" + getBub_cd() + "&sa_no=" + getSa_no() + "&mae_giil="
				+ getMae_giil() + "&sano_HasGam=" + getSano_HasGam()
				+ "&gam_no=" + getGam_no();
	}

}
