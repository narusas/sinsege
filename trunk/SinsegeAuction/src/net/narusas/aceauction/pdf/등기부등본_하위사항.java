/*
 * 
 */
package net.narusas.aceauction.pdf;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class 등기부등본_하위사항.
 */
public class 등기부등본_하위사항 {

	/** The p. */
	static Pattern p = Pattern.compile("(\\d+년\\s*\\d+월\\s*\\d+일)");

	/** The accept date. */
	private String acceptDate = "";

	/** The because. */
	private String because = "";

	/** The last right and etc. */
	private Entity lastRightAndEtc;

	/** The purpose. */
	private String purpose = "";

	/** The right. */
	private String right = "";

	/** The rights. */
	List<String> rights = new LinkedList<String>();

	/**
	 * Instantiates a new 등기부등본_하위사항.
	 * 
	 * @param purpose the purpose
	 */
	public 등기부등본_하위사항(Entity purpose) {
		this(purpose.getText());
	}

	/**
	 * Instantiates a new 등기부등본_하위사항.
	 * 
	 * @param purpose the purpose
	 */
	public 등기부등본_하위사항(String purpose) {
		setPurpose(purpose);
	}

	/**
	 * Adds the accept date.
	 * 
	 * @param text the text
	 */
	public void addAcceptDate(String text) {
		acceptDate += text;
	}

	/**
	 * Adds the because.
	 * 
	 * @param text the text
	 */
	public void addBecause(String text) {
		// Matcher m = p.matcher(text);
		// if (m.find()) {
		// String s = m.group(1);
		// because += text.substring(text.indexOf(s) + s.length());
		// } else {
		because += text;
		// }

	}

	/**
	 * Adds the right and etc.
	 * 
	 * @param entity the entity
	 */
	public void addRightAndEtc(Entity entity) {
		this.lastRightAndEtc = entity;
		right += entity.getText() + "\n";
		rights.add(entity.getText());
	}

	/**
	 * Gets the accept date.
	 * 
	 * @return the accept date
	 */
	public String getAcceptDate() {
		return acceptDate.trim();
	}

	/**
	 * Gets the because.
	 * 
	 * @return the because
	 */
	public String getBecause() {
		return because.trim();
	}

	/**
	 * Gets the last right and etc.
	 * 
	 * @return the last right and etc
	 */
	public Entity getLastRightAndEtc() {
		return lastRightAndEtc;
	}

	/**
	 * Gets the purpose.
	 * 
	 * @return the purpose
	 */
	public String getPurpose() {
		return purpose.trim();
	}

	/**
	 * Gets the right.
	 * 
	 * @return the right
	 */
	public String getRight() {
		return right.trim();
	}

	/**
	 * Gets the rights.
	 * 
	 * @return the rights
	 */
	public List<String> getRights() {
		return rights;
	}

	/**
	 * Sets the purpose.
	 * 
	 * @param purpose the new purpose
	 */
	public void setPurpose(String purpose) {
		this.purpose = purpose;
		if (this.purpose.contains("(")) {
			this.purpose = this.purpose.substring(0, this.purpose.indexOf("("));
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "" + purpose + ", " + acceptDate + "," + because + "," + rights;
	}

}
