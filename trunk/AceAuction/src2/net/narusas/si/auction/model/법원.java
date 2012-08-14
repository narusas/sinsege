package net.narusas.si.auction.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class 법원 implements Serializable {

	// 법원목록이 static인 이유는 각 법원이 담당계 workset을 메모리상에 유지해야 하기 때문이다.

	public static List<법원> 법원목록;

	private Integer 법원코드;

	private String 법원명;

	private String 스피드옥션_법원코드;

	private 법원 상위법원;

	지역 지역;

	private Integer order;

	// 실제 DB에 있는 모든 담당계를 포함하는게 아니고, 대법원 사이트에 현재 게재된 작업용 목ㄹㅗㄱ 이다.
	transient private List<담당계> workSet;

	public static 법원 findByName(String courtName) {
		if (법원목록 == null) {
			return null;
		}
		for (법원 court : 법원목록) {
			if (courtName.equals(court.get법원명())) {
				return court;
			}
		}
		return null;
	}

	public int get법원코드() {
		return 법원코드;
	}

	public void set법원코드(Integer 법원코드) {
		this.법원코드 = 법원코드;
	}

	public String get법원명() {
		return 법원명;
	}

	public void set법원명(String 법원명) {
		this.법원명 = 법원명;
	}

	public 법원 get상위법원() {
		return 상위법원;
	}

	public void set상위법원(법원 상위법원) {
		this.상위법원 = 상위법원;
	}

	public String get스피드옥션_법원코드() {
		return 스피드옥션_법원코드;
	}

	public void set스피드옥션_법원코드(String 스피드옥션_법원코드) {
		this.스피드옥션_법원코드 = 스피드옥션_법원코드;
	}

	public 지역 get지역() {
		return 지역;
	}

	public void set지역(지역 지역) {
		this.지역 = 지역;
	}

	@Override
	public String toString() {
		return 법원명;
	}

	public void add담당계목록(List<담당계> workSet) {
		getWorkSet().addAll(workSet);
	}

	public void add담당계(담당계 charge) {
		getWorkSet().add(charge);
	}

	public List<담당계> getWorkSet() {
		if (workSet == null) {
			workSet = new LinkedList<담당계>();
		}
		return workSet;
	}

	public void setWorkSet(List<담당계> workSet) {
		this.workSet = workSet;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

}
