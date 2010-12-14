/*
 * 
 */
package net.narusas.si.auction.builder.present;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class 대지권Item.
 * @deprecated
 */
public class 대지권Item {
	
	/** The address. */
	String address;
	
	/** The area. */
	String area;
	
	/** The logger. */
	Logger logger = Logger.getLogger("log");
	
	/** The no. */
	int no;
	
	/** The ratio. */
	String ratio;
	
	/** The type. */
	String type;

	
	/**
	 * Instantiates a new 대지권 item.
	 * 
	 * @param no the no
	 * @param address the address
	 * @param area the area
	 */
	public 대지권Item(int no, String address, String area) {
		this.no = no;
		this.address = address;
		this.area = area;

	}

	/**
	 * Instantiates a new 대지권 item.
	 * 
	 * @param no the no
	 * @param address the address
	 * @param area the area
	 * @param type the type
	 * @param ratio the ratio
	 */
	public 대지권Item(int no, String address, String area, String type, String ratio) {
		super();
		this.no = no;
		this.address = address;
		this.area = area;
		this.type = type;
		this.ratio = ratio;
	}

	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	public String getAddress() {
		return address;
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
	 * Gets the no.
	 * 
	 * @return the no
	 */
	public int getNo() {
		return no;
	}

	/**
	 * Gets the ratio.
	 * 
	 * @return the ratio
	 */
	public double getRatio() {
		Pattern p = Pattern.compile("([\\d.]+)\\s*분의\\s*([\\d.]+)");
		Matcher m = p.matcher(ratio);
		m.find();
		double a = Double.parseDouble(m.group(1));
		double b = Double.parseDouble(m.group(2));
		return b / a;
	}

	/**
	 * Gets the ratio a.
	 * 
	 * @return the ratio a
	 */
	public double getRatioA() {
		Pattern p = Pattern.compile("([\\d.]+)\\s*분의\\s*([\\d.]+)");
		Matcher m = p.matcher(ratio);
		m.find();
		double a = Double.parseDouble(m.group(1));
		return a;
	}

	/**
	 * Gets the ratio b.
	 * 
	 * @return the ratio b
	 */
	public double getRatioB() {
		Pattern p = Pattern.compile("([\\d.]+)\\s*분의\\s*([\\d.]+)");
		Matcher m = p.matcher(ratio);
		m.find();
		double b = Double.parseDouble(m.group(2));
		return b;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gets the 면적.
	 * 
	 * @return the 면적
	 */
	public double get면적() {
		Pattern p = Pattern.compile("([\\d.]+)");
		Matcher m = p.matcher(area);
		m.find();
		return Double.parseDouble(m.group(1));
	}

	/**
	 * Sets the address.
	 * 
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Sets the area.
	 * 
	 * @param area the new area
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * Sets the no.
	 * 
	 * @param no the new no
	 */
	public void setNo(int no) {
		this.no = no;
	}

	/**
	 * Sets the ratio.
	 * 
	 * @param ratio the new ratio
	 */
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return no + "," + address + "," + area + "," + type + "," + ratio;
	}
}