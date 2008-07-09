/*
 * Created on 17-Nov-2003
 *
 */
package org.jpedal.color;

import java.awt.RenderingHints;

/**
 * Static values for colorspaces
 */
public class ColorSpaces {
	
	public static final int UNKNOWN = -1;
	public static final int ICC = 7;
	public static final int CalGray = 4;
	public static final int DeviceGray = 1;
	public static final int DeviceN = 11;
	public static final int Separation = 10;
	public static final int Pattern = 9;
	public static final int Lab = 6;
	public static final int Indexed = 8;
	public static final int DeviceRGB = 2;
	public static final int CalRGB = 5;
	public static final int DeviceCMYK = 3;

	/**hint for conversion ops*/
	public static RenderingHints hints = null;
	protected static boolean useDeviceN=true;
	
	static {
		hints =
			new RenderingHints(
				RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		hints.put(
			RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY);
		hints.put(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(
			RenderingHints.KEY_DITHERING,
			RenderingHints.VALUE_DITHER_ENABLE);
		hints.put(
			RenderingHints.KEY_COLOR_RENDERING,
			RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		hints.put(
			RenderingHints.KEY_FRACTIONALMETRICS,
			RenderingHints.VALUE_FRACTIONALMETRICS_ON);

	}
	
	/**method to convert a name to an ID values*/
	final public  static int convertNameToID(String name){
		
		int id=-1;
		
		if ((name.indexOf("Separation") != -1))
			id=Separation;
		else if (name.indexOf("DeviceN") != -1)
			id=DeviceN;
		else if ((name.indexOf("Indexed") != -1))
			id=Indexed;	
		else if ((name.indexOf("DeviceCMYK") != -1)| (name.indexOf("CMYK") != -1))
			id=DeviceCMYK;
		else if (name.indexOf("CalGray") != -1)
			id=CalGray;
		else if (name.indexOf("CalRGB") != -1)
			id=CalRGB;
		else if (name.indexOf("Lab") != -1) 
			id=Lab;
		else if (name.indexOf("ICCBased") != -1) 
			id=ICC;
		else if (name.indexOf("Pattern") != -1)
			id=Pattern;
		else if ((name.indexOf("DeviceRGB") != -1)|(name.indexOf("RGB") != -1))
			id=DeviceRGB;
		else if ((name.indexOf("DeviceGray") != -1)|(name.indexOf("G") != -1))
			id=DeviceGray;			
		
		return id;
	}

}
