/*
 * 
 */
package net.narusas.aceauction.model;

// TODO: Auto-generated Javadoc
/**
 * The Class �ε���ǥ�ø���ּ�.
 */
public class �ε���ǥ�ø���ּ� {

	/** The addr1. */
	public final String addr1;
	
	/** The addr2. */
	public final String addr2;
	
	/** The addr3. */
	public final String addr3;
	
	/** The addr4. */
	public final String addr4;

	/**
	 * Instantiates a new �ε���ǥ�ø���ּ�.
	 * 
	 * @param addr1 the addr1
	 * @param addr2 the addr2
	 * @param addr3 the addr3
	 * @param addr4 the addr4
	 */
	public �ε���ǥ�ø���ּ�(String addr1, String addr2, String addr3, String addr4) {
		this.addr1 = addr1;
		this.addr2 = addr2;
		this.addr3 = addr3;
		this.addr4 = addr4;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (addr3.endsWith("��") || addr3.endsWith("��")) {
			return words(addr4, 2);
		}
		return addr3 + " " + words(addr4, 1);
		// return (addr1 + " " + addr2 + " " + addr3 + " " + addr4).trim();
	}

	/**
	 * Clean word.
	 * 
	 * @param string the string
	 * 
	 * @return the string
	 */
	private String cleanWord(String string) {
		if (string.endsWith("��")) {
			return string.substring(0, string.length() - 1);
		}
		return string;
	}

	/**
	 * Words.
	 * 
	 * @param addr the addr
	 * @param count the count
	 * 
	 * @return the string
	 */
	private String words(String addr, int count) {
		String[] tokens = addr.split(" ");
		String temp = "";
		for (int i = 0; i < count; i++) {
			temp += cleanWord(tokens[i]) + " ";
		}
		return temp.trim();
	}
}
