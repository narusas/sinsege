package net.narusas.si.auction.fetchers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.converters.현황주소Converter;
import net.narusas.si.auction.model.대지권;
import net.narusas.si.auction.model.물건;

public class 대지권Parser {
	public static 대지권 parse(물건 물건, String chunk, String 주소, String 매각지분, String 상세내역, String 공시지가) {
		List<대지권Item> items = parse(chunk);
		String 현황주소 = 현황주소Converter.convert(주소);
		String 지분비율 = getRatioString(items);
		String 면적 = toNumberString(get면적(items));
		String 매각지분비고 = 매각지분;
		String 건물번호 = parse건물번호(상세내역);

		대지권 대지권 = new 대지권();
		대지권.set물건(물건);
		대지권.set주소(현황주소);
		대지권.set지분비율(지분비율);
		대지권.set면적(면적);
		대지권.set매각지분비고(매각지분비고);
		대지권.set건물번호(건물번호);
		대지권.set공시지가(공시지가);
		return 대지권;
	}

	private static String parse건물번호(String 상세내역) {
		Pattern p = Pattern.compile("건물의\\s*번호\\s*:\\s*([^\n]+)");
		Matcher m = p.matcher(상세내역);
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}

	static List<대지권Item> parse(String text) {
		List<대지권Item> result = new LinkedList<대지권Item>();
		Map<Integer, 대지권Item> temp = new HashMap<Integer, 대지권Item>();
		String[] lines = text.split("\n");
		Pattern p = Pattern.compile("([\\d]+[,\\d\\s]*).(.*)$");
		int type = 0;
		for (int i = 1; i < lines.length; i++) {
			String line = lines[i];
			String hubgryLine = line.replaceAll(" ", "");
			if (hubgryLine.contains("토지의표시:")) {
				type = 1;
				line = line.substring(line.indexOf(":") + 1);
			} else if (hubgryLine.contains("대지권의종류:")) {
				type = 2;
				line = line.substring(line.indexOf(":") + 1);
			} else if (hubgryLine.contains("대지권의비율:")) {
				type = 3;
				line = line.substring(line.indexOf(":") + 1);
			}
			Matcher m = p.matcher(line);
			if (m.find()) {
				switch (type) {
				case 1: {
					String noString = m.group(1);
					String[] numbers = noString.split(",");
					for (String number : numbers) {
						int no = Integer.parseInt(number.trim());
						String address = m.group(2).trim();
						String area = lines[i + 1].trim();
						i++;
						대지권Item item = new 대지권Item(no, address, area);
						result.add(item);
						temp.put(no, item);
					}

				}
					break;
				case 2: {
					String noString = m.group(1);
					String[] numbers = noString.split(",");
					for (String number : numbers) {
						int no = Integer.parseInt(number.trim());
						String landType = m.group(2).trim();
						대지권Item item = temp.get(no);
						if (item != null) {
							item.setType(landType);
						}

					}

				}
					break;

				case 3: {
					String noString = m.group(1);
					String[] numbers = noString.split(",");
					for (String number : numbers) {
						int no = Integer.parseInt(number.trim());
						String ratio = m.group(2).trim();
						대지권Item item = temp.get(no);
						if (item != null) {
							item.setRatio(ratio);
						}

					}

				}
					break;

				}
			}

		}

		return result;
	}

	public static double getRatio(List<대지권Item> items) {
		if (allSameRatio(items)) {
			return items.get(0).getRatio();
		}
		double result = 0;
		double a = 0;
		double b = 0;
		for (대지권Item item : items) {
			// result += item.getRatio();
			a += item.getRatioA();
			b += item.getRatioB();
		}
		result = b / a;
		return result;
	}

	/**
	 * Gets the ratio string.
	 * 
	 * @return the ratio string
	 */
	public static String getRatioString(List<대지권Item> items) {
		if (allSameRatio(items)) {
			return items.get(0).getRatioA() + " 분의 " + items.get(0).getRatioB();
		}
		double a = 0;
		double b = 0;

		for (대지권Item item : items) {
			a += item.getRatioA();
			b += item.getRatioB();
		}
		return toNumberString(a) + " 분의 " + toNumberString(b);
	}

	static String toNumberString(double value) {
		int v = (int) (value * 1000);
		double v2 = ((double) v) / 1000;
		return String.valueOf(v2);
	}

	private static boolean allSameRatio(List<대지권Item> items) {
		double result = items.get(0).getRatio();
		for (int i = 1; i < items.size(); i++) {
			if (result != items.get(i).getRatio()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the 면적.
	 * 
	 * @return the 면적
	 */
	public static double get면적(List<대지권Item> items) {
		double result = 0;
		for (대지권Item item : items) {
			result += item.get면적();
		}
		return result * getRatio(items);
	}

}

class 대지권Item {
	Logger logger = Logger.getLogger("auction");
	String address;

	String area;

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
		if (ratio == null || "".equals(ratio)) {
			return 0;
		}
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
		if (ratio == null || "".equals(ratio)) {
			return 0;
		}
		Pattern p = Pattern.compile("([\\d.]+)\\s*분의\\s*([\\d.]+)");
		Matcher m = p.matcher(ratio);
		m.find();
		double a = Double.parseDouble(m.group(1));
		return a;
	}

	public double getRatioB() {
		if (ratio == null || "".equals(ratio)) {
			return 0;
		}
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
