package net.narusas.si.auction.model;

import java.util.Date;

public class 물건감정평가서 {
	String 평가기관;

	Date 평가시점;
	String 대지권가격, 건물가격, 제시외포함, 제시외미포함, 기계기구, 합계;

	@Override
	public String toString() {
		return 평가기관 + "," + 평가시점;
	}

	public String get평가기관() {
		return 평가기관;
	}

	public void set평가기관(String 평가기관) {
		this.평가기관 = 평가기관;
	}

	public Date get평가시점() {
		return 평가시점;
	}

	public void set평가시점(Date 평가시점) {
		this.평가시점 = 평가시점;
	}

	public String get대지권가격() {
		return 대지권가격;
	}

	public void set대지권가격(String 대지권가격) {
		this.대지권가격 = 대지권가격;
	}

	public String get건물가격() {
		return 건물가격;
	}

	public void set건물가격(String 건물가격) {
		this.건물가격 = 건물가격;
	}

	public String get제시외포함() {
		return 제시외포함;
	}

	public void set제시외포함(String 제시외포함) {
		this.제시외포함 = 제시외포함;
	}

	public String get제시외미포함() {
		return 제시외미포함;
	}

	public void set제시외미포함(String 제시외미포함) {
		this.제시외미포함 = 제시외미포함;
	}

	public String get기계기구() {
		return 기계기구;
	}

	public void set기계기구(String 기계기구) {
		this.기계기구 = 기계기구;
	}

	public String get합계() {
		return 합계;
	}

	public void set합계(String 합계) {
		this.합계 = 합계;
	}

}
