/*
 * 
 */
package net.narusas.aceauction.model;

// TODO: Auto-generated Javadoc
/**
 * The Class 스피드옥션물건.
 */
public class 스피드옥션물건 {

	/** The charge. */
	final String charge;

	/** The court code. */
	final String courtCode;

	/** The no. */
	final String no;

	/** The year. */
	final String year;

	/** The 물건no. */
	final String 물건no;

	/** The 순서. */
	final String 순서;

	/**
	 * Instantiates a new 스피드옥션물건.
	 * 
	 * @param courtCode the court code
	 * @param charge the charge
	 * @param year the year
	 * @param no the no
	 * @param 물건no the 물건no
	 * @param 순서 the 순서
	 */
	public 스피드옥션물건(String courtCode, String charge, String year, String no, String 물건no, String 순서) {
		this.courtCode = courtCode;
		this.charge = charge;
		this.year = year;
		this.no = no;
		this.물건no = 물건no;
		this.순서 = 순서;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		스피드옥션물건 t = (스피드옥션물건) obj;
		return courtCode.equals(t.courtCode) && charge.equals(t.charge) && year.equals(t.year)
				&& no.equals(t.no) && 물건no.equals(t.물건no) && 순서.equals(t.순서);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 물건no.hashCode() + 순서.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "스피드옥션물건[no=" + 물건no + ",순서=" + 순서 + "]";
	}
}
