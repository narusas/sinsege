package net.narusas.si.auction.model;

import java.util.Collection;

import net.narusas.si.auction.fetchers.위지상;
import net.narusas.si.auction.fetchers.전유부분;

public class 부동산표시 {
	int 목록번호;
	String 목록구분;
	String 상세내역;
	private 주소 주소;
	private 전유부분 전유부분;
	// private 대지권현황 대지권현황;
	private 위지상 위지상;
	private String 매각지분;

	boolean 전유부분건물표시여부;
	private String 대지면적;
	private String 구조;
	private boolean 지분;
	private boolean 대지권소유여부;
	private 대지권 대지권;
	private Collection<제시외건물> 제시외건물목록;
	private String 공시지가;

	public int get목록번호() {
		return 목록번호;
	}

	public void set목록번호(int 목록번호) {
		this.목록번호 = 목록번호;
	}

	public String get목록구분() {
		return 목록구분;
	}

	public void set목록구분(String 목록구분) {
		this.목록구분 = 목록구분;
	}

	public String get상세내역() {
		return 상세내역;
	}

	public void set상세내역(String 상세내역) {
		this.상세내역 = 상세내역;
	}

	public void set주소(주소 주소) {
		this.주소 = 주소;
	}

	public 주소 get주소() {
		return 주소;
	}

	public void set전유부분(전유부분 전유부분) {
		this.전유부분 = 전유부분;
	}

	public 전유부분 get전유부분() {
		return 전유부분;
	}

	public void set위지상(위지상 위지상) {
		this.위지상 = 위지상;
	}

	public 위지상 get위지상() {
		return 위지상;
	}

	public void set매각지분(String 매각지분) {
		this.매각지분 = 매각지분;
	}

	public String get매각지분() {
		return 매각지분;
	}

	public boolean is전유부분건물표시여부() {
		return 전유부분건물표시여부;
	}

	public void set전유부분건물표시여부(boolean 전유부분건물표시여부) {
		this.전유부분건물표시여부 = 전유부분건물표시여부;
	}

	public void set대지면적(String 대지면적) {
		this.대지면적 = 대지면적;
	}

	public String get대지면적() {
		return 대지면적;
	}

	public void set구조(String 구조) {
		this.구조 = 구조;
	}

	public String get구조() {
		return 구조;
	}

	public void set지분소유여부(boolean 지분) {
		this.지분 = 지분;
	}

	public boolean is지분소유여부() {
		return 지분;
	}

	public void set대지권소유여부(boolean 대지권소유여부) {
		this.대지권소유여부 = 대지권소유여부;
	}

	public boolean is대지권소유여부() {
		return 대지권소유여부;
	}

	public boolean is지분() {
		return 지분;
	}

	public void set지분(boolean 지분) {
		this.지분 = 지분;
	}

	public void set대지권(대지권 대지권) {
		this.대지권 = 대지권;
	}

	public 대지권 get대지권() {
		return 대지권;
	}

	public void set제시외건물(Collection<제시외건물> 제시외건물목록) {
		this.제시외건물목록 = 제시외건물목록;
	}

	public Collection<제시외건물> get제시외건물목록() {
		return 제시외건물목록;
	}

	public void set제시외건물목록(Collection<제시외건물> 제시외건물목록) {
		this.제시외건물목록 = 제시외건물목록;
	}

	public String get공시지가() {
		return 공시지가;
	}

	public void set공시지가(String 공시지가) {
		this.공시지가 = 공시지가;
	}
	

}
