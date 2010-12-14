package net.narusas.si.auction.model;

import java.util.Collection;
import java.util.LinkedList;

public class 건물현황 {
	String 평가정리;
	Collection<건물> 건물목록;

	public String get평가정리() {
		return 평가정리;
	}

	public void set평가정리(String 평가정리) {
		this.평가정리 = 평가정리;
	}

	public Collection<건물> get건물목록() {
		return 건물목록;
	}

	public void set건물목록(Collection<건물> 건물목록) {
		this.건물목록 = 건물목록;
	}

	public int size() {
		return 건물목록 == null ? 0 : 건물목록.size();
	}

	public void add(건물 bld) {
		if (건물목록 == null) {
			건물목록 = new LinkedList<건물>();
		}
		건물목록.add(bld);
	}

}
