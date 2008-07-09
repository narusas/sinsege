/*
 * 
 */
package net.narusas.aceauction.pdf.jpedal;

// TODO: Auto-generated Javadoc
/**
 * The Class TextPosition.
 */
public class TextPosition {
	
	/** The stage. */
	private int stage;

	/** The page. */
	int page;

	/** The text. */
	String text;

	/** The y2. */
	float x, y, x2, y2;

	/**
	 * Instantiates a new text position.
	 * 
	 * @param text the text
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 * @param page the page
	 * @param stage the stage
	 */
	public TextPosition(String text, float x1, float y1, float x2, float y2, int page, int stage) {
		this.text = text;
		this.page = page;
		this.stage = stage;
		if (x1 <= x2) {
			x = x1;
			this.x2 = x2;
		} else {
			x = x2;
			this.x2 = x1;
		}

		if (y1 <= y2) {
			y = y1;
			this.y2 = y2;
		} else {
			y = y2;
			this.y2 = y1;
		}
	}

	/**
	 * Adds the.
	 * 
	 * @param tp2 the tp2
	 * 
	 * @return the text position
	 */
	public TextPosition add(TextPosition tp2) {
		return new TextPosition(text + " " + tp2.text, x, y, tp2.x2, tp2.y2, page, stage);
	}

	/**
	 * Adds the vertical.
	 * 
	 * @param tp2 the tp2
	 * 
	 * @return the text position
	 */
	public TextPosition addVertical(TextPosition tp2) {
		return addVertical(tp2, false);
	}

	/**
	 * Adds the vertical.
	 * 
	 * @param tp2 the tp2
	 * @param b the b
	 * 
	 * @return the text position
	 */
	public TextPosition addVertical(TextPosition tp2, boolean b) {
		if (b) {
			return new TextPosition(text + "\n" + tp2.text, x, y, Math.max(x2, tp2.x2), y2, page,
					stage);
		} else {
			return new TextPosition(text + "\n" + tp2.text, x, y, Math.max(x2, tp2.x2), Math.max(
					y2, tp2.y2), page, stage);
		}
	}

	/**
	 * Adds the vertical들여쓰기.
	 * 
	 * @param tp2 the tp2
	 * 
	 * @return the text position
	 */
	public TextPosition addVertical들여쓰기(TextPosition tp2) {
		String space = "";
		int gap = (int) ((tp2.getX() - 504) / 10);
		for (int i = 0; i < gap; i++) {
			space += " ";
		}
		return new TextPosition(text + "\n" + space + tp2.text, x, y, Math.max(x2, tp2.x2), Math
				.max(y2, tp2.y2), page, stage);
	}

	/**
	 * Gets the height.
	 * 
	 * @return the height
	 */
	public float getHeight() {
		return y2 - y;
	}

	/**
	 * Gets the page.
	 * 
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Gets the stage.
	 * 
	 * @return the stage
	 */
	public int getStage() {
		return stage;
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
		return x2 - x;
	}

	/**
	 * Checks if is binding.
	 * 
	 * @param other the other
	 * @param gap the gap
	 * 
	 * @return true, if is binding
	 */
	public boolean isBinding(TextPosition other, float gap) {
		return Math.abs(other.x - this.x2) < gap && Math.abs(other.y - this.y) < 1
				&& other.getPage() == getPage();
	}

	/**
	 * Checks if is binding수직.
	 * 
	 * @param other the other
	 * 
	 * @return true, if is binding수직
	 */
	public boolean isBinding수직(TextPosition other) {
		return isBinding수직(other, 8);
	}

	/**
	 * Checks if is binding수직.
	 * 
	 * @param other the other
	 * @param ygap the ygap
	 * 
	 * @return true, if is binding수직
	 */
	public boolean isBinding수직(TextPosition other, float ygap) {
		return other.y - this.y2 <= ygap && other.y > this.y2 && other.getPage() == getPage();
	}

	/**
	 * Checks if is binding수평.
	 * 
	 * @param other the other
	 * @param f the f
	 * 
	 * @return true, if is binding수평
	 */
	public boolean isBinding수평(TextPosition other, float f) {
		return isBinding(other, f);
	}

	/**
	 * Sets the x.
	 * 
	 * @param f the new x
	 */
	public void setX(float f) {
		this.x = f;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return text + "\t\t\tX:" + x + " Y:" + y + " X2:" + x2 + " Y2:" + y2 + " P:" + page + " S:"
				+ stage;
	}

	/**
	 * Gets the x.
	 * 
	 * @return the x
	 */
	float getX() {
		return x;
	}

	/**
	 * Gets the x2.
	 * 
	 * @return the x2
	 */
	float getX2() {
		return x2;
	}

	/**
	 * Gets the y.
	 * 
	 * @return the y
	 */
	float getY() {
		return y;
	}

	/**
	 * Gets the y2.
	 * 
	 * @return the y2
	 */
	float getY2() {
		return y2;
	}
}