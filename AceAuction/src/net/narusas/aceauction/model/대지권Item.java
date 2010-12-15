package net.narusas.aceauction.model;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 대지권Item {
	String address;
	String area;
	Logger logger = Logger.getLogger("log");
	int no;
	String ratio;
	String type;

	public 대지권Item(int no, String address, String area) {
		this.no = no;
		this.address = address;
		this.area = area;

	}

	public 대지권Item(int no, String address, String area, String type, String ratio) {
		super();
		this.no = no;
		this.address = address;
		this.area = area;
		this.type = type;
		this.ratio = ratio;
	}

	public String getAddress() {
		return address;
	}

	public String getArea() {
		return area;
	}

	public int getNo() {
		return no;
	}

	public double getRatio() {
		Pattern p = Pattern.compile("([\\d.]+)\\s*분의\\s*([\\d.]+)");
		Matcher m = p.matcher(ratio);
		m.find();
		double a = Double.parseDouble(m.group(1));
		double b = Double.parseDouble(m.group(2));
		return b / a;
	}

	public double getRatioA() {
		Pattern p = Pattern.compile("([\\d.]+)\\s*분의\\s*([\\d.]+)");
		Matcher m = p.matcher(ratio);
		m.find();
		double a = Double.parseDouble(m.group(1));
		return a;
	}

	public double getRatioB() {
		Pattern p = Pattern.compile("([\\d.]+)\\s*분의\\s*([\\d.]+)");
		Matcher m = p.matcher(ratio);
		m.find();
		double b = Double.parseDouble(m.group(2));
		return b;
	}

	public String getType() {
		return type;
	}

	public double get면적() {
		Pattern p = Pattern.compile("([\\d.]+)");
		Matcher m = p.matcher(area);
		m.find();
		return Double.parseDouble(m.group(1));
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return no + "," + address + "," + area + "," + type + "," + ratio;
	}
}