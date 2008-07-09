/*
 * 
 */
package net.narusas.aceauction.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class 제시외건물.
 */
public class 제시외건물 {

	/** The Constant NO. */
	static final int NO = 0;

	/** The p. */
	static Pattern p = Pattern.compile("(\\d+[-,]*\\d*[\\S\\s]*)$");

	/** The Constant 제시외건물제외. */
	static final int 제시외건물제외 = 2;

	/** The Constant 제시외건물포함. */
	static final int 제시외건물포함 = 1;

	/** The address. */
	private final String address;

	/** The contains. */
	private final int contains;

	/** The type. */
	private String type;

	/** The 구조. */
	private final String 구조;

	/** The 면적. */
	private final String 면적;

	/** The 물건번호. */
	private final int 물건번호;

	/** The 용도. */
	private final String 용도;

	/** The 층형. */
	private String 층형;

	/**
	 * Instantiates a new 제시외건물.
	 * 
	 * @param 물건번호 the 물건번호
	 * @param 용도 the 용도
	 * @param 구조 the 구조
	 * @param 면적 the 면적
	 * @param address the address
	 * @param contains the contains
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		제시외건물 target = (제시외건물) arg0;
		return 용도.equals(target.용도) && 구조.equals(target.구조) && 면적.equals(target.면적)
				&& 물건번호 == target.물건번호;
	}

	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Gets the 구조.
	 * 
	 * @return the 구조
	 */
	public String get구조() {
		return 구조;
	}

	/**
	 * Gets the 면적.
	 * 
	 * @return the 면적
	 */
	public String get면적() {
		return 면적;
	}

	/**
	 * Gets the 물건번호.
	 * 
	 * @return the 물건번호
	 */
	public int get물건번호() {
		return 물건번호;
	}

	/**
	 * Gets the 용도.
	 * 
	 * @return the 용도
	 */
	public String get용도() {
		return 용도;
	}

	/**
	 * Gets the 종류.
	 * 
	 * @return the 종류
	 */
	public String get종류() {
		return type;
	}

	/**
	 * Gets the 층형.
	 * 
	 * @return the 층형
	 */
	public String get층형() {
		return 층형;
	}

	/**
	 * Gets the 포함여부.
	 * 
	 * @return the 포함여부
	 */
	public int get포함여부() {
		return contains;
	}

	/**
	 * Gets the 포함여부 string.
	 * 
	 * @return the 포함여부 string
	 */
	public String get포함여부String() {
		if (contains == 1) {
			return "포함";
		}
		if (contains == 2) {
			return "제외";
		}
		return "";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return type + "[" + 물건번호 + ",주소=" + address + ",용도=" + 용도 + ",구조=" + 구조 + ",면적=" + 면적 + "]";
	}

	/**
	 * Parses the address.
	 * 
	 * @param address the address
	 * 
	 * @return the string
	 */
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

	/**
	 * Parses the floor.
	 */
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

	/**
	 * Parses the type.
	 * 
	 * @return the string
	 */
	private String parseType() {
		if (구조.contains("지붕") || 구조.contains("층") || 구조.contains("조")) {
			return "제시외건물";
		}
		return "기계기구";
	}
}
