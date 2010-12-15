package net.narusas.aceauction.model;

public class 스피드옥션물건 {

	final String charge;

	final String courtCode;

	final String no;

	final String year;

	final String 물건no;

	final String 순서;

	public 스피드옥션물건(String courtCode, String charge, String year, String no, String 물건no, String 순서) {
		this.courtCode = courtCode;
		this.charge = charge;
		this.year = year;
		this.no = no;
		this.물건no = 물건no;
		this.순서 = 순서;
	}

	@Override
	public boolean equals(Object obj) {
		스피드옥션물건 t = (스피드옥션물건) obj;
		return courtCode.equals(t.courtCode) && charge.equals(t.charge) && year.equals(t.year)
				&& no.equals(t.no) && 물건no.equals(t.물건no) && 순서.equals(t.순서);
	}

	@Override
	public int hashCode() {
		return 물건no.hashCode() + 순서.hashCode();
	}

	@Override
	public String toString() {
		return "스피드옥션물건[no=" + 물건no + ",순서=" + 순서 + "]";
	}
}
