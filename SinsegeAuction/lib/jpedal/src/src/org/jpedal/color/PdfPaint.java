package org.jpedal.color;

import java.awt.Paint;

public interface PdfPaint extends Paint {

	public void setScaling(double cropX, double cropH, float scaling);
	
	public boolean isPattern();
	
	//constructor for pattern color
	public void setPattern(int dummy);
	
	public int getRGB();
}
