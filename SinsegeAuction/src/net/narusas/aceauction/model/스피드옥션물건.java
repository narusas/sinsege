/*
 * 
 */
package net.narusas.aceauction.model;

// TODO: Auto-generated Javadoc
/**
 * The Class ���ǵ���ǹ���.
 */
public class ���ǵ���ǹ��� {

	/** The charge. */
	final String charge;

	/** The court code. */
	final String courtCode;

	/** The no. */
	final String no;

	/** The year. */
	final String year;

	/** The ����no. */
	final String ����no;

	/** The ����. */
	final String ����;

	/**
	 * Instantiates a new ���ǵ���ǹ���.
	 * 
	 * @param courtCode the court code
	 * @param charge the charge
	 * @param year the year
	 * @param no the no
	 * @param ����no the ����no
	 * @param ���� the ����
	 */
	public ���ǵ���ǹ���(String courtCode, String charge, String year, String no, String ����no, String ����) {
		this.courtCode = courtCode;
		this.charge = charge;
		this.year = year;
		this.no = no;
		this.����no = ����no;
		this.���� = ����;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		���ǵ���ǹ��� t = (���ǵ���ǹ���) obj;
		return courtCode.equals(t.courtCode) && charge.equals(t.charge) && year.equals(t.year)
				&& no.equals(t.no) && ����no.equals(t.����no) && ����.equals(t.����);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ����no.hashCode() + ����.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "���ǵ���ǹ���[no=" + ����no + ",����=" + ���� + "]";
	}
}
