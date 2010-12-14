package net.narusas.si.auction.model;

public class 매각물건명세서비고 {
	String 비고, 비소멸권리, 지상권개요, 비고란;

	public String get비고() {
		return 비고;
	}

	public void set비고(String 비고) {
		this.비고 = 비고;
	}

	public String get비소멸권리() {
		return 비소멸권리;
	}

	public void set비소멸권리(String 비소멸권리) {
		this.비소멸권리 = 비소멸권리;
	}

	public String get지상권개요() {
		return 지상권개요;
	}

	public void set지상권개요(String 지상권개요) {
		this.지상권개요 = 지상권개요;
	}

	public String get비고란() {
		return 비고란;
	}

	public void set비고란(String 비고란) {
		this.비고란 = 비고란;
	}

	public void merge(매각물건명세서비고 comment) {
		if (comment.get비고() != null && "".equals(comment.get비고().trim()) == false) {
			set비고(comment.get비고());
		}
		if (comment.get비소멸권리() != null && "".equals(comment.get비소멸권리().trim()) == false) {
			set비소멸권리(comment.get비소멸권리());
		}
		if (comment.get지상권개요() != null && "".equals(comment.get지상권개요().trim()) == false) {
			set지상권개요(comment.get지상권개요());
		}
		if (comment.get비고란() != null && "".equals(comment.get비고란().trim()) == false) {
			set비고란(comment.get비고란());
		}
	}
}
