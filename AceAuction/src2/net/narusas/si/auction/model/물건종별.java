package net.narusas.si.auction.model;

public class 물건종별 {
	Long id;
	String 이름;

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return 이름;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String get이름() {
		return 이름;
	}

	public void set이름(String 이름) {
		this.이름 = 이름;
	}

}
