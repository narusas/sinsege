package net.narusas.si.auction.app.attested.parser;

import net.narusas.si.auction.app.attested.parser.AtestedPDFParser.Align;
import net.narusas.si.auction.pdf.attested.TextPosition;

public class Collumn {
	float x1;
	float x2;
	Align align;

	public Collumn(float x1, float x2, Align align) {
		super();
		this.x1 = x1;
		this.x2 = x2;
		this.align = align;
	}

	public float getX1() {
		return x1;
	}

	public float getX2() {
		return x2;
	}

	public float getWidth() {
		return Math.abs(x2 - x1);
	}

	public Align getAlign() {
		return align;
	}

	public boolean contains(TextPosition t) {
		switch (align) {
		case Left:
			if (t.getX() >= getX1() && t.getX() < getX2()) {
				return true;
			}
			break;
		case Right:
			if (t.getX2() >= getX1() && t.getX2() < getX2()) {
				return true;
			}
			break;
		}
		return false;
	}

}
