/*
 * 
 */
package net.narusas.aceauction.pdf;

// TODO: Auto-generated Javadoc
/**
 * The Class Entity.
 */
public class Entity {

	/** The page no. */
	private final int pageNo;

	/** The text. */
	private final String text;

	/** The width. */
	private final float width;

	/** The x. */
	private final float x;

	/** The y. */
	private final float y;

	/**
	 * Instantiates a new entity.
	 * 
	 * @param text the text
	 * @param pageNo the page no
	 * @param y the y
	 * @param x the x
	 * @param width the width
	 */
	public Entity(String text, int pageNo, float y, float x, float width) {
		this.text = text;
		this.pageNo = pageNo;
		this.x = x;
		this.width = width;
		this.y = pageNo * 1000 + y;
	}

	/**
	 * Gets the page no.
	 * 
	 * @return the page no
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Gets the width.
	 * 
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Gets the x.
	 * 
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * Gets the y.
	 * 
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return text + " P:" + pageNo + " Y:" + y;
	}

}
