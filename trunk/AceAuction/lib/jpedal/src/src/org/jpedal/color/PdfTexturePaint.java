package org.jpedal.color;

import java.awt.TexturePaint;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class PdfTexturePaint extends TexturePaint implements PdfPaint {

	public PdfTexturePaint(BufferedImage txtr, Rectangle2D anchor) {
		super(txtr, anchor);
	}

	public void setScaling(double cropX,double cropH,float scaling){

	}

	public boolean isPattern() {
		return false;
	}

	public void setPattern(int dummy) {

	}

	public int getRGB() {
		return 0;
	}

}
