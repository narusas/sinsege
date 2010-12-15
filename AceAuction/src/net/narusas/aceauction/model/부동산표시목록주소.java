package net.narusas.aceauction.model;

public class 부동산표시목록주소 {

	public final String addr1;
	public final String addr2;
	public final String addr3;
	public final String addr4;

	public 부동산표시목록주소(String addr1, String addr2, String addr3, String addr4) {
		this.addr1 = addr1;
		this.addr2 = addr2;
		this.addr3 = addr3;
		this.addr4 = addr4;
	}

	@Override
	public String toString() {
		if (addr3.endsWith("읍") || addr3.endsWith("면")) {
			return words(addr4, 2);
		}
		return addr3 + " " + words(addr4, 1);
		// return (addr1 + " " + addr2 + " " + addr3 + " " + addr4).trim();
	}

	private String cleanWord(String string) {
		if (string.endsWith("외")) {
			return string.substring(0, string.length() - 1);
		}
		return string;
	}

	private String words(String addr, int count) {
		String[] tokens = addr.split(" ");
		String temp = "";
		for (int i = 0; i < count; i++) {
			temp += cleanWord(tokens[i]) + " ";
		}
		return temp.trim();
	}
}
