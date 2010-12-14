package net.narusas.si.auction.model;

import java.util.Collection;
import java.util.LinkedList;

public class 대지권현황 {
	String 평가정리;
	Collection<net.narusas.si.auction.builder.present.대지권현황> 대지권목록;

	public String get평가정리() {
		return 평가정리;
	}

	public void set평가정리(String 평가정리) {
		this.평가정리 = 평가정리;
	}

	public int size() {
		return 대지권목록 == null ? 0 : 대지권목록.size();
	}

	public void add(net.narusas.si.auction.builder.present.대지권현황 item) {
		if (대지권목록 == null) {
			대지권목록 = new LinkedList<net.narusas.si.auction.builder.present.대지권현황>();
		}
		대지권목록.add(item);
	}

	public Collection<net.narusas.si.auction.builder.present.대지권현황> get대지권목록() {
		return 대지권목록;
	}

	public void set대지권목록(Collection<net.narusas.si.auction.builder.present.대지권현황> 대지권목록) {
		this.대지권목록 = 대지권목록;
	}

}
