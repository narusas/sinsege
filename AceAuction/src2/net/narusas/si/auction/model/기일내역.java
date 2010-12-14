package net.narusas.si.auction.model;

import java.util.Collection;
import java.util.LinkedList;

public class 기일내역 {
	String 평가정리;
	Collection<기일> 기일목록;

	public String get평가정리() {
		return 평가정리;
	}

	public void set평가정리(String 평가정리) {
		this.평가정리 = 평가정리;
	}

	public Collection<기일> get기일목록() {
		if (기일목록 == null){
			기일목록 = new LinkedList<기일>();
		}
		return 기일목록;
	}

	public void set기일목록(Collection<기일> 기일목록) {
		this.기일목록 = 기일목록;
	}

	public void add(기일 item) {
		if (기일목록 == null) {
			기일목록 = new LinkedList<기일>();
		}
		기일목록.add(item);
	}

	@Override
	public String toString() {
		return 기일목록.toString() + ":" + 평가정리;
	}
}
