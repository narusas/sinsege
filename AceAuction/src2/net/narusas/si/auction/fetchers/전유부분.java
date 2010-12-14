/*
 * 
 */
package net.narusas.si.auction.fetchers;

import net.narusas.si.auction.model.물건;

/**
 * The Class 전유부분.
 */
public class 전유부분 {

	String 구조;
	String 면적;
	물건 물건;

	/**
	 * Instantiates a new 전유부분.
	 * 
	 * @param text
	 *            the text
	 * @param string
	 */

	public 전유부분() {
	}

	public 전유부분(String get구조, String get면적) {
		구조 = get구조;
		면적 = get면적;
	}

	public String get구조() {
		return 구조;
	}

	public void set구조(String 구조) {
		this.구조 = 구조;
	}

	public String get면적() {
		return 면적;
	}

	public void set면적(String 면적) {
		this.면적 = 면적;
	}

	public 물건 get물건() {
		return 물건;
	}

	public void set물건(물건 물건) {
		this.물건 = 물건;
	}

	@Override
	public String toString() {
		return "전유부분={구조=" + get구조() + ",면적=" + get면적() + "}";
	}
}
