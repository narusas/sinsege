package net.narusas.si.auction.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class 토지 {

	private 물건 물건;
	private String 주소;
	private String 목적;
	private String 목적2 = "";
	private String 면적;
	private String 매각지분;
	private String 매각지분계산결과;

	Long id;
	private Integer 목록번호;
	private String 공시지가;

	public 토지() {

	}

	public 토지(물건 물건, String 주소, String 목족, String 면적, String 매각지분, String 공시지가) {
		this.물건 = 물건;
		this.공시지가 = 공시지가;
		목록번호 = 물건.get물건번호();
		this.주소 = 주소;
		this.목적 = 목족;
		this.면적 = 면적;
		this.매각지분 = 매각지분;

		this.매각지분계산결과 = calc매각지분(매각지분);

	}

	String calc매각지분(String src) {
		if (StringUtils.isEmpty(매각지분)){
			return null;
		}
		
		{
			Pattern p = Pattern.compile("(\\d+)\\s*분의\\s*(\\d+)");
			Matcher m = p.matcher(src);
			if (m.find()) {
				return calc매각지분(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
			}
		}
		{
			Pattern p = Pattern.compile("(\\d+)\\s*/\\s*(\\d+)");
			Matcher m = p.matcher(src);
			if (m.find()) {
				return calc매각지분(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1)));
			}
		}
		
		{
			Pattern p = Pattern.compile("(\\d+)\\s*분지\\s*(\\d+)");
			Matcher m = p.matcher(src);
			if (m.find()) {
				return calc매각지분(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
			}
		}
		
		{
			Pattern p = Pattern.compile("(\\d+)\\s*분\\s*(\\d+)");
			Matcher m = p.matcher(src);
			if (m.find()) {
				return calc매각지분(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
			}
		}
		return null;
	}

	private String calc매각지분(int first, int second) {
		try {
			System.out.println("F:"+first+" S:"+second);
			float 면적F = Float.parseFloat(get면적());
			
			String formateed =  String.format("%.2f", (면적F/first*second));
			if (formateed.endsWith(".00")){
				return formateed.substring(0, formateed.length() -3);
			}
			return formateed;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public 물건 get물건() {
		return 물건;
	}

	public String get주소() {
		return 주소;
	}

	public String get목적() {
		return 목적;
	}

	public String get면적() {
		if (면적 != null) {
			면적 = 면적.replaceAll("㎡", "");
		}
		return 면적;
	}

	public String get매각지분() {
		return 매각지분;
	}

	public Integer get목록번호() {
		return 목록번호;
	}

	public void set목록번호(Integer 목록번호) {
		this.목록번호 = 목록번호;
	}

	public void set물건(물건 물건) {
		this.물건 = 물건;
	}

	public void set주소(String 주소) {
		this.주소 = 주소;
	}

	public void set목적(String 목적) {
		this.목적 = 목적;
	}

	public void set면적(String 면적) {
		this.면적 = 면적;
	}

	public void set매각지분(String 매각지분) {
		this.매각지분 = 매각지분;
		
	}

	public boolean isTypeMatch(String[] keys) {
		for (String key : keys) {
			if (key.equals(get목적())) {
				return true;
			}
		}
		return false;
	}

	public String get목적2() {
		return 목적2;
	}

	public void set목적2(String 목적2) {
		this.목적2 = 목적2;
	}

	public String get공시지가() {
		return 공시지가;
	}

	public void set공시지가(String 공시지가) {
		this.공시지가 = 공시지가;
	}

	public String get매각지분계산결과() {
		return 매각지분계산결과;
	}

	public void set매각지분계산결과(String 매각지분계산결과) {
		this.매각지분계산결과 = 매각지분계산결과;
	}

}
