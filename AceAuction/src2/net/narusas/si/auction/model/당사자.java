package net.narusas.si.auction.model;

public class 당사자 {
	long id;
	String 당사자구분;
	String 당사자명;
	사건 사건;

	public 당사자() {
	}

	public 당사자(사건 사건, String 당사자구분, String 당사자명) {
		this.사건 = 사건;
		this.당사자구분 = 당사자구분;
		this.당사자명 = 당사자명;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String get당사자구분() {
		return 당사자구분;
	}

	public void set당사자구분(String 당사자구분) {
		this.당사자구분 = 당사자구분;
	}

	public String get당사자명() {
		return 당사자명;
	}

	public void set당사자명(String 당사자명) {
		this.당사자명 = 당사자명;
	}

	public 사건 get사건() {
		return 사건;
	}

	public void set사건(사건 사건) {
		this.사건 = 사건;
	}

	@Override
	public String toString() {
		return 당사자구분 + ":" + 당사자명;
	}

}
