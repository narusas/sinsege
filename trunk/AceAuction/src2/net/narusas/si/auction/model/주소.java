package net.narusas.si.auction.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 주소 {
	지역 시도;
	지역 시군구;
	지역 읍면동;
	String 번지이하;
	private String 소재지;
	
	net.narusas.si.auction.fetchers.주소통합Builder.통합주소 통합주소;

	
	public net.narusas.si.auction.fetchers.주소통합Builder.통합주소 get통합주소() {
		return 통합주소;
	}

	public void set통합주소(net.narusas.si.auction.fetchers.주소통합Builder.통합주소 통합주소) {
		this.통합주소 = 통합주소;
	}

	public void set주소(String 소재지) {
		this.소재지 = 소재지;

	}

	public 지역 get시도() {
		return 시도;
	}

	public void set시도(지역 시도) {
		this.시도 = 시도;
	}

	public 지역 get시군구() {
		return 시군구;
	}

	public void set시군구(지역 시군구) {
		this.시군구 = 시군구;
	}

	public 지역 get읍면동() {
		return 읍면동;
	}

	public void set읍면동(지역 읍면동) {
		this.읍면동 = 읍면동;
	}

	public String get번지이하() {
		return 번지이하;
	}

	public void set번지이하(String 번지이하) {
		this.번지이하 = 번지이하;
	}

	public String get소재지() {
		return 소재지;
	}

	public void set소재지(String 소재지) {
		this.소재지 = 소재지;
	}

	@Override
	public String toString() {
		// return 시도 + " " + 시군구 + " " + 읍면동 + " " + 번지이하;
		return 읍면동 + " " + 번지이하;
	}

	public final static Pattern slimAddrPattern = Pattern.compile("(\\S+[동리가] \\s*[산]*\\s*[\\d-]*)");

	// ex) 굴포로 157
	public final static Pattern slimAddrPattern2 = Pattern.compile("(.*[길로] \\d+)");

	public String toSlimAddress() {

		Matcher m1 = slimAddrPattern2.matcher(toString());
		if (m1.find()) {
			return m1.group(1);
		}

		Matcher m = slimAddrPattern.matcher(toString());
		if (m.find()) {
			return m.group(1);
		}
		return toString();
	}

}
