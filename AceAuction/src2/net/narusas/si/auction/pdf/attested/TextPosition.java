package net.narusas.si.auction.pdf.attested;

public class TextPosition {
	int stage;

	int page;

	String text;

	float x, y, x2, y2;

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

	public TextPosition add(TextPosition tp2) {
		return new TextPosition(text + " " + tp2.text, x, y, tp2.x2, tp2.y2, page, stage);
	}

	public TextPosition addVertical(TextPosition tp2) {
		return addVertical(tp2, false);
	}

	public TextPosition addVertical(TextPosition tp2, boolean b) {
		if (b) {
			return new TextPosition(text + "\n" + tp2.text, x, y, Math.max(x2, tp2.x2), y2, page, stage);
		} else {
			return new TextPosition(text + "\n" + tp2.text, x, y, Math.max(x2, tp2.x2), Math.max(y2, tp2.y2), page,
					stage);
		}
	}

	public TextPosition addVertical들여쓰기(TextPosition tp2) {
		String space = "";
		int gap = (int) ((tp2.getX() - 504) / 10);
		for (int i = 0; i < gap; i++) {
			space += " ";
		}
		return new TextPosition(text + "\n" + space + tp2.text, x, y, Math.max(x2, tp2.x2), Math.max(y2, tp2.y2), page,
				stage);
	}

	public float getHeight() {
		return y2 - y;
	}

	public int getPage() {
		return page;
	}

	public int getStage() {
		return stage;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public float getWidth() {
		return x2 - x;
	}

	public boolean isBinding(TextPosition other, float gap) {
		return Math.abs(other.x - this.x2) < gap && Math.abs(other.y - this.y) < 1 && other.getPage() == getPage();
	}

	public boolean isBinding수직(TextPosition other) {
		return isBinding수직(other, 8);
	}

	public boolean isBinding수직(TextPosition other, float ygap) {
		return other.y - this.y2 <= ygap && other.y > this.y2 && other.getPage() == getPage();
	}

	public boolean isBinding수평(TextPosition other, float f) {
		return isBinding(other, f);
	}

	public void setX(float f) {
		this.x = f;
	}

	@Override
	public String toString() {
		return text + "=[X:" + x + " Y:" + y + " X2:" + x2 + " Y2:" + y2 + " P:" + page + " S:" + stage + ",ABSY="+getAbsoluteY()+"]";
	}

	public float getX() {
		return x;
	}

	public float getX2() {
		return x2;
	}

	public float getY() {
		return y;
	}

	public float getY2() {
		return y2;
	}

	public float getAbsoluteY() {
		return page * 1000 + y2;
	}

	public boolean isSameRow(TextPosition other, float tolerance) {
		return Math.abs(other.getAbsoluteY() - getAbsoluteY()) <= tolerance;
	}

	public boolean isSameRow(TextPosition other) {
		return isSameRow(other, 2f);
	}

	/**
	 * 왼쪽에 있는 크기는 공배이라고 가정하고 크기를 줄인다.
	 * 
	 * @param fontSize
	 * @return
	 */
	public TextPosition trim(float fontSize) {
		float textSize = 0;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (Character.isDigit(ch) || Character.isWhitespace(ch) || ',' == ch || '.' == ch
					|| (ch >= 'a' && ch <= 'Z')) {
				textSize += fontSize;
			} else {
				textSize += fontSize * 2;
			}
		}
		// float textSize = text.length() * fontSize;
		return new TextPosition(text, x2 - textSize, y, x2, y2, page, stage);

	}

	public void setStage(int stage2) {
		stage = stage2;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
}