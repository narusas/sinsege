/*
 * 
 */
package net.narusas.aceauction.model;

// TODO: Auto-generated Javadoc
/**
 * The Class 類⑽.
 */
public class 類⑽ {

	/** The area. */
	private final String area;
	
	/** The text. */
	private final String text;

	/**
	 * Instantiates a new 類⑽.
	 * 
	 * @param text the text
	 * @param area the area
	 */
	public 類⑽(String text, String area) {
		this.text = text.replaceAll("\n", " ").replaceAll("\\s{2,}", " ")
				.trim();
		this.area = area;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		類⑽ target = (類⑽) obj;
		return text.equals(target.text) && area.equals(target.area);
	}

	/**
	 * Gets the area.
	 * 
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return text.hashCode() + area.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return text + "," + area;
	}

}