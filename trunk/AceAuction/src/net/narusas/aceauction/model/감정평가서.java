package net.narusas.aceauction.model;

/**
 * 대법원 사이트의 HTML 링크에 대한 정보를 가지고 있는 객체. 
 * @author narusas
 *
 */
public class 감정평가서 {

	private final String bub_cd;

	private final String gam_no;

	private final String ibub_nm;

	private final String mae_giil;

	private final String mo_sa_no;

	private final String relsaInfo;

	private final String sa_no;

	private final String saList_HasGam;

	private final String sano_HasGam;

	private final String userSaList_HasGam;

	private final String userSano_HasGam;

	public 감정평가서(String bub_cd, String sa_no, String mo_sa_no,
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

	public String getBub_cd() {
		return bub_cd;
	}

	public String getGam_no() {
		return gam_no;
	}

	public String getIbub_nm() {
		return ibub_nm;
	}

	public String getMae_giil() {
		return mae_giil;
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

	public String getSaList_HasGam() {
		return saList_HasGam;
	}

	public String getSano_HasGam() {
		return sano_HasGam;
	}

	public String getUserSaList_HasGam() {
		return userSaList_HasGam;
	}

	public String getUserSano_HasGam() {
		return userSano_HasGam;
	}

	public String toQueryString() {
		return "bub_cd=" + getBub_cd() + "&sa_no=" + getSa_no() + "&mae_giil="
				+ getMae_giil() + "&sano_HasGam=" + getSano_HasGam()
				+ "&gam_no=" + getGam_no();
	}

}
