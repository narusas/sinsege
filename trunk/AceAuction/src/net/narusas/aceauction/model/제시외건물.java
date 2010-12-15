package net.narusas.aceauction.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 제시외건물 {

	static final int NO = 0;

	static Pattern p = Pattern.compile("(\\d+[-,]*\\d*[\\S\\s]*)$");

	static final int 제시외건물제외 = 2;

	static final int 제시외건물포함 = 1;

	private final String address;

	private final int contains;

	private String type;

	private final String 구조;

	private final String 면적;

	private final int 물건번호;

	private final String 용도;

	private String 층형;

	public 제시외건물(int 물건번호, String 용도, String 구조, String 면적, String address, int contains) {
		this.물건번호 = 물건번호;
		this.용도 = 용도;
		this.구조 = 구조;
		this.면적 = 면적;
		this.contains = contains;
		this.address = parseAddress(address);
		this.type = parseType();
		parseFloor();
	}

	@Override
	public boolean equals(Object arg0) {
		제시외건물 target = (제시외건물) arg0;
		return 용도.equals(target.용도) && 구조.equals(target.구조) && 면적.equals(target.면적)
				&& 물건번호 == target.물건번호;
	}

	public String getAddress() {
		return address;
	}

	public String get구조() {
		return 구조;
	}

	public String get면적() {
		return 면적;
	}

	public int get물건번호() {
		return 물건번호;
	}

	public String get용도() {
		return 용도;
	}

	public String get종류() {
		return type;
	}

	public String get층형() {
		return 층형;
	}

	public int get포함여부() {
		return contains;
	}

	public String get포함여부String() {
		if (contains == 1) {
			return "포함";
		}
		if (contains == 2) {
			return "제외";
		}
		return "";
	}

	@Override
	public String toString() {
		return type + "[" + 물건번호 + ",주소=" + address + ",용도=" + 용도 + ",구조=" + 구조 + ",면적=" + 면적 + "]";
	}

	private String parseAddress(String address) {
		String[] buf = address.split(" ");
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			Matcher m = p.matcher(buf[i]);
			if (m.find()) {
				res.append(buf[i - 1]).append(" ").append(buf[i]);
				i++;
				for (; i < buf.length; i++) {
					res.append(" ").append(buf[i]);
				}
			}
		}
		return res.toString();
	}

	private void parseFloor() {
		if (구조.endsWith("층")) {
			층형 = 구조.substring(구조.length() - 2);
		}
		if (구조.endsWith("지하")) {
			층형 = "지하";
		}
		if (구조.endsWith("옥탑")) {
			층형 = "옥탑";
		}
	}

	private String parseType() {
		if (구조.contains("지붕") || 구조.contains("층") || 구조.contains("조")) {
			return "제시외건물";
		}
		return "기계기구";
	}
}
