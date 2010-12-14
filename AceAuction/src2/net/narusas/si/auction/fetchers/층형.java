/*
 * 
 */
package net.narusas.si.auction.fetchers;

// TODO: Auto-generated Javadoc
/**
 * The Class 층형.
 */
public class 층형 {

	/** The area. */
	private final String area;
	
	/** The text. */
	private final String text;

	public 층형(String text, String area) {
		this.text = text.replaceAll("\n", " ").replaceAll("\\s{2,}", " ")
				.trim();
		this.area = area;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		층형 target = (층형) obj;
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