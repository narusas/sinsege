package net.narusas.aceauction.model;

public class ���ǵ���ǹ��� {

	final String charge;

	final String courtCode;

	final String no;

	final String year;

	final String ����no;

	final String ����;

	public ���ǵ���ǹ���(String courtCode, String charge, String year, String no, String ����no, String ����) {
		this.courtCode = courtCode;
		this.charge = charge;
		this.year = year;
		this.no = no;
		this.����no = ����no;
		this.���� = ����;
	}

	@Override
	public boolean equals(Object obj) {
		���ǵ���ǹ��� t = (���ǵ���ǹ���) obj;
		return courtCode.equals(t.courtCode) && charge.equals(t.charge) && year.equals(t.year)
				&& no.equals(t.no) && ����no.equals(t.����no) && ����.equals(t.����);
	}

	@Override
	public int hashCode() {
		return ����no.hashCode() + ����.hashCode();
	}

	@Override
	public String toString() {
		return "���ǵ���ǹ���[no=" + ����no + ",����=" + ���� + "]";
	}
}
