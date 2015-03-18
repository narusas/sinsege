package net.narusas.si.auction.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class 주소 {
	지역 시도;
	지역 시군구;
	지역 시군구그룹;
	지역 읍면동;
	String 번지이하;
	private String 소재지;
	
	net.narusas.si.auction.fetchers.AddressBuilder.통합주소 통합주소;

	
	public net.narusas.si.auction.fetchers.AddressBuilder.통합주소 get통합주소() {
		return 통합주소;
	}

	public void set통합주소(net.narusas.si.auction.fetchers.AddressBuilder.통합주소 통합주소) {
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
	

	public 지역 get시군구그룹() {
		return 시군구그룹;
	}

	public void set시군구그룹(지역 시군구그룹) {
		this.시군구그룹 = 시군구그룹;
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
		if ( 통합주소.도로명 ==  null){
			return 통합주소.동주소();
		}else {
			return  통합주소.도로명주소();
		}
	}
	
	public String to토지주소() {
		if (통합주소.번지이하 != null){
			return 통합주소.동리+" "+통합주소.번지+" "+통합주소.번지이하;
		}
		
		return 통합주소.동리+" "+통합주소.번지;
//		
//		if (읍면동.toString().endsWith("읍") || 읍면동.toString().endsWith("면")){
//			return  번지이하;
//		}
//		return toString();
//		if ( 통합주소.동주소() ==  null){
//			return 통합주소.동주소();
//		}else {
		
//			return toString();
//		}
	}
	

}
