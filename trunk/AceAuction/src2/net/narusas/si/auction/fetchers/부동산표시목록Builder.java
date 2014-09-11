package net.narusas.si.auction.fetchers;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.converters.건물현황_구조Converter;
import net.narusas.si.auction.converters.건물현황_층형Converter;
import net.narusas.si.auction.converters.현황주소Converter;
import net.narusas.si.auction.model.건물;
import net.narusas.si.auction.model.대지권;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.부동산표시;
import net.narusas.si.auction.model.사건종류;
import net.narusas.si.auction.model.주소;
import net.narusas.si.auction.model.토지;
import net.narusas.util.TextUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 부동산표시목록Builder {
	final static Logger logger = LoggerFactory.getLogger("auction");
	/** The Constant unitPattern1. */
	public final static Pattern unitPattern1 = Pattern.compile("(((\\d+평) *|(\\d+홉) *|(\\d+작) *)+)", Pattern.MULTILINE);

	/** The Constant unitPattern2. */
	public final static Pattern unitPattern2 = Pattern.compile("(((\\d+정)\\s*|(\\d+단)\\s*|(\\d+무)\\s*)+(\\d)*보)",
			Pattern.MULTILINE);

	/** The 단unit. */
	static Pattern 단unit = Pattern.compile("(\\d+)단", Pattern.MULTILINE);

	/** The 무unit. */
	static Pattern 무unit = Pattern.compile("(\\d+)무", Pattern.MULTILINE);

	/** The 보unit. */
	static Pattern 보unit = Pattern.compile("(\\d+)보", Pattern.MULTILINE);

	/** The 작unit. */
	static Pattern 작unit = Pattern.compile("(\\d+)작", Pattern.MULTILINE);

	/** The 정unit. */
	static Pattern 정unit = Pattern.compile("(\\d+)정", Pattern.MULTILINE);

	/** The 평unit. */
	static Pattern 평unit = Pattern.compile("(\\d+)평", Pattern.MULTILINE);

	/** The 홉unit. */
	static Pattern 홉unit = Pattern.compile("(\\d+)홉", Pattern.MULTILINE);

	boolean hasBuilding = false;
	boolean hasLand = false;
	boolean hasBuildingRight = false;
	boolean hasLandRight = false;
	boolean has전유부분 = false;

	public void build(물건 goods, int 목록번호, String 목록구분, String 상세내역, int index) {
		상세내역 = convertAreaUnit(상세내역);
		updateAddress2(goods,  상세내역, index);

		부동산표시 표시 = goods.get부동산표시(목록번호);
		if (goods.get사건().get종류() == 사건종류.부동산 && 표시 != null && isStartWithAddress(상세내역, 표시.get주소())) {
			상세내역 = removeStarting주소(상세내역, 표시.get주소());
		}

		// TODO 여기는 원래 null이 나오면 않됨
		if (표시 == null) {
			return;
		}
		if ("기타".equals(목록구분.trim())) {
			return;
		}
		표시.set목록구분(목록구분);
		표시.set상세내역(상세내역);

		표시.set전유부분건물표시여부(상세내역.contains("전유부분의"));
		표시.set대지면적(parse대지면적(상세내역));
		표시.set구조(parse구조(상세내역));
		표시.set대지권소유여부(has대지권(상세내역));

		List<String> chunks = parseStructure(상세내역);
		for (int i = 0; i < chunks.size(); i++) {
			String chunk = chunks.get(i);
			chunk = convertAreaUnit(chunk);
			if (chunk.startsWith("전유부분의 건물의 표시")) {
				전유부분 전유부분 = 전유부분Parser.parse(chunk);
				표시.set전유부분(전유부분);
			} else if (chunk.startsWith("대지권의 목적인 토지의 표시") || chunk.startsWith("대지권의 표시")) {
				handle대지권(goods, 목록번호, 상세내역, 표시, chunk);
			} else if (chunk.contains("위 지상")) {
				표시.set위지상(new 위지상(chunk, 표시.get주소().toSlimAddress()));
			} else if (chunk.contains("매각지분")) {
				표시.set매각지분(chunk);
			} else if (chunk.contains("제시외건물") || chunk.contains("제시외 건물")) {
				if (! chunk.matches("제시외\\s*건물\\s*매각\\s*제외")) {
					parse제시외(goods, 목록번호, 표시, chunk);
				}

			}
		}

		for (int i = 0; i < chunks.size(); i++) {
			String chunk = chunks.get(i);
			chunk = convertAreaUnit(chunk);
			if (chunk.startsWith("대지권의 목적인 토지의 표시") || chunk.startsWith("대지권의 표시")) {
				handle대지권(goods, 목록번호, 상세내역, 표시, chunk);
			}
		}

		String garuantee_ratio = updateGuaranteeRatio(goods);

		if (부동산표시TypeMatcher.isStart토지(상세내역.trim())) {
			hasLand = true;
			// if (goods.get토지목록() == null || goods.get토지목록().size() == 0) {
			토지 토지 = addLand(goods, 표시, 상세내역);
			토지.set목록번호(목록번호);
			goods.add토지(토지);
			표시.set지분소유여부(상세내역.contains("지분"));
			// }
			if (상세내역.contains("지분")) {
				hasLandRight = true;
			}
		} else {

			addBuilding(goods, 상세내역, 표시, 목록번호);
			hasBuilding = true;
			// 
			// if (has대지권(상세내역)) {
			if (표시.get대지권() != null) {
				goods.add대지권(표시.get대지권());
				hasLand = true;
				if (상세내역.contains("지분")) {
					hasLandRight = true;
				}
			}
			if (상세내역.contains("지분")) {
				hasBuildingRight = true;
			}
			if (상세내역.contains("전유부분")) {
				has전유부분 = true;
			}
		}

	}

	// 도로명 주소일때 구 주소가 있는지 여부를 확인해  ac_goods.address2 에 넣어 주는 기능
	private void updateAddress2(물건 goods, String 상세내역, int index) {
		if (index >0){
			return;
		}
		if (상세내역.contains("[도로명 주소]") == false){
			return;
		}
		String temp =  HTMLUtils.converHTMLSpecialChars(상세내역.substring(0,  상세내역.indexOf("[도로명 주소]"))).trim();
		String[] list = temp.split("\n");
		if (temp.contains(" 표시")){
			temp = list[1].trim();
		}
		else {
			temp = list[0].trim();
		}
		if (StringUtils.isEmpty(temp) || StringUtils.isNotEmpty(goods.getAddress2())){
			return;
		}
		logger.info("기존 주소:"+ temp);
		goods.setAddress2(temp);
	}


	private boolean isStartWithAddress(String 상세내역, 주소 주소) {
		String[] lines = 상세내역.split("\n");
		String startingLine = lines[0];
		if (주소.get시도() != null){
			if (startingLine.contains(주소.get시도().get지역명())){
				return true;
			}
		}
		if (주소.get시군구() != null){
			if (startingLine.contains(주소.get시군구().get지역명())){
				return true;
			}
		}
		
		if (주소.get읍면동()!=null) {
			if (startingLine.contains(주소.get읍면동().get지역명())){
				return true;
			}
		}
		return false;
	}

	private String removeStarting주소(String 상세내역, 주소 get주소) {
		String[] lines = 상세내역.split("\n");
		String[] res = new String[lines.length - 1];
		System.arraycopy(lines, 1, res, 0, res.length);

		return TextUtil.join(res, "\n").trim();
	}

	private void handle대지권(물건 goods, int 목록번호, String 상세내역, 부동산표시 표시, String chunk) {
		try {
			대지권 대지권 = 대지권Parser.parse(goods, chunk, 표시.get주소().toSlimAddress(), 표시.get매각지분(), 상세내역, 표시.get공시지가());
			대지권.set목록번호(목록번호);
			표시.set대지권(대지권);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void parse제시외(물건 goods, int 목록번호, 부동산표시 표시, String chunk) {
		
		if (chunk.indexOf("\n") != -1){
			chunk = chunk.substring(chunk.indexOf("\n")).trim();
		}
		제시외건물Parser.parse(goods, chunk, 목록번호, 표시.get주소());
	}


//	private int parse제시외(물건 goods, int 목록번호, 부동산표시 표시, List<String> chunks, int i) {
		
		
		
//		for (int k = i + 1; k < chunks.size(); k++) {
//			String temp = chunks.get(k);
//			if ("".equals(temp.trim())) {
//				i = k;
//				break;
//			}
//
//			제시외건물Parser.parse(goods, chunks.get(k), 목록번호, 표시.get주소());
//		}
//		return i;
//	}

	String calc매각대상(boolean 건물이있는가, boolean 건물지분이있는가, boolean 토지가있는가, boolean 토지지분이있는가, String comment) {
		String presentation = "";
		if (건물이있는가) {
			if (토지가있는가) {
				if (건물지분이있는가) {
					if (토지지분이있는가) {
						presentation = "토지 및 건물지분매각";
					} else {
						presentation = "토지전체  및 건물지분매각";
					}
				} else {
					if (토지지분이있는가) {
						presentation = "건물전체 및 토지지분매각";
					} else {
						presentation = "토지  및 건물일괄매각";
					}
				}
			} else {
				if (건물지분이있는가) {
					presentation = "토지제외 및 건물지분매각";
				} else {
					presentation = "토지제외 및 건물매각";
				}
			}
		} else {
			if (토지지분이있는가) {
				if (is건물제외비고(comment)) {
					presentation = "건물제외 및 토지지분매각";
				} else {
					presentation = "토지지분매각";
				}

			} else {
				if (is건물제외비고(comment)) {
					presentation = "건물제외 및 토지매각";
				} else {
					presentation = "토지매각";
				}

			}
		}
		return presentation;
	}

	boolean is건물제외비고(String comment) {
		// 매각에서 제외되는~,법정지상권,제시외건물,건물,지상에
		return comment != null
				&& (comment.contains("매각에서 제외되는") || comment.contains("법정지상권") || comment.contains("제시외건물")
						|| comment.contains("건물") || comment.contains("지상에"));
	}

	boolean has대지권(String detail) {
		return detail.contains("대지권의 목적인 토지의 표시") || detail.contains("대지권의 표시");
	}

	void addBuilding(물건 물건, String detail, 부동산표시 item, int 목록번호) {
		String floor = item.get주소().get통합주소().번지이하;
		String structure = null;
		String area = null;
		String 매각지분Comment = item.get매각지분();

		if (item.is전유부분건물표시여부()) {
			area = item.get전유부분().get면적();
			structure = item.get전유부분().get구조();
		} else {
			area = item.get대지면적();
			structure = item.get구조();
		}

		if (item.get위지상() != null) {
			위지상 위지상 = item.get위지상();
			List<층형> floors = 위지상.getFloors();
			logger.info("추가할 위 지상 건물의 수는 " + floors.size());
			for (층형 f : floors) {
				logger.info("insert 위 지상 building floor=" + f.getText() + " str=" + structure + " area=" + f.getArea());
				건물 st = new 건물(물건,// 
						item.get주소().toSlimAddress(),// 
						f.getText(),// 
						"", //
						f.getArea(),// 
						"위지상", //
						detail, //
						item.get매각지분());
				st.set목록번호(목록번호);
				물건.add건물(st);
			}
		} else {
			건물 st = new 건물(물건,// 
					item.get주소().toSlimAddress(),// 
					net.narusas.si.auction.converters.건물현황_층형Converter.convert(floor),//
					건물현황_구조Converter.convert(structure), //
					현황주소Converter.convert(area),//
					detail,// 
					매각지분Comment);
			logger.info("insert building "// 
					+ "address=" + item.get주소().toString()//
					+ "floor=" + 건물현황_층형Converter.convert(floor)//
					+ " str=" + 건물현황_구조Converter.convert(structure)//
					+ " area=" + 현황주소Converter.convert(area)//
					+ " detail=" + detail//
					+ " comment=" + item.get매각지분()//
			);
			st.set목록번호(목록번호);
			물건.add건물(st);
		}

	}

	private 토지 addLand(물건 goods, 부동산표시 표시, String 상세내역) {
		String address = 현황주소Converter.convert(표시.get주소().toSlimAddress());
		String use = 부동산표시TypeMatcher.matchStart토지(상세내역);
		String area = parse대지면적(상세내역);
		String 매각지분 = parse지분(표시.get매각지분());
		토지 st = new 토지(goods, address, use, area, 매각지분, 표시.get공시지가());

		return st;
	}

	private String parse지분(String src) {
		if (src == null) {
			return "";
		}
		src = src.replaceAll("[\r\n]", " ").trim();
		if (src.startsWith("매각지분")) {
			return src.substring(src.indexOf(":") + 1).trim();
		}
		return "";

		// Pattern p = Pattern.compile("([\\d\\.]+\\s*분의\\s*[\\d\\.]+)");
		// Matcher m = p.matcher(src);
		// if (m.find() == false) {
		// p = Pattern.compile("매각지분\\s*:\\s*(.*\\s*지분\\s*전부)");
		// m = p.matcher(src);
		// if (m.find()){
		// return m.group(1);
		// }
		// return "";
		// }
		// return m.group(1);
	}

	String updateGuaranteeRatio(물건 goods) {
		String comment = goods.get물건비고();
		String garuantee_ratio = "10";
		// 재매각, 보증금, 특별매각
		if (comment != null && (comment.contains("재매각") || comment.contains("보증금") || comment.contains("특별매각"))) {
			Pattern p = Pattern.compile("(\\d+)%");
			Matcher m = p.matcher(comment);
			if (m.find()) {
				garuantee_ratio = m.group(1) + "";
			} else {
				p = Pattern.compile("(\\d+)할");
				m = p.matcher(comment);
				if (m.find()) {
					garuantee_ratio = m.group(1) + "0";
				}
			}
		}
		return garuantee_ratio;
	}

	/**
	 * Convert area unit.
	 * 
	 * @param chunk
	 *            the chunk
	 * 
	 * @return the string
	 */
	public String convertAreaUnit(String chunk) {
		chunk = removeCommaInNumber(chunk);
		String temp = "";
		Matcher m = unitPattern1.matcher(chunk);
		int pos = 0;
		while (m.find()) {
			temp += chunk.substring(pos, m.start());
			temp += toNoString(convert평홉작ToMeterSquare(m.group(1))) + "㎡ ";
			pos = m.end();
		}
		temp += chunk.substring(pos);
		// if (pos == 0) {
		// temp = chunk;
		// }

		String chunk2 = temp.trim();
		m = unitPattern2.matcher(chunk2);
		pos = 0;
		String temp2 = "";
		while (m.find()) {
			temp2 += chunk2.substring(pos, m.start());
			temp2 += toNoString(convert정단무보ToMeterSquare(m.group(1))) + "㎡ ";
			pos = m.end();
		}

		temp2 += chunk2.substring(pos);
		// if (pos == 0) {
		// temp2 = chunk2;
		// }

		return temp2.trim();
	}
	
	public static String removeCommaInNumber(String src){
		Pattern p = Pattern.compile("([\\d,]+)");
		Matcher m = p.matcher(src);
		String res = src;
		while(m.find()){
			String token = m.group(1);
			res = res.replace(token, token.replaceAll(",", ""));
		}
		return res;
	}

	/**
	 * Convert정단무보 to meter square.
	 * 
	 * @param area
	 *            the area
	 * 
	 * @return the double
	 */
	public double convert정단무보ToMeterSquare(String area) {
		int 정 = getValue(area, 정unit);
		int 단 = getValue(area, 단unit);
		int 무 = getValue(area, 무unit);
		int 보 = getValue(area, 보unit);

		return 정 * 9917 + 단 * 991.7 + 무 * 99.17 + 보 * 3.3;
	}

	/**
	 * Convert평홉작 to meter square.
	 * 
	 * @param area
	 *            the area
	 * 
	 * @return the double
	 */
	public double convert평홉작ToMeterSquare(String area) {
		int pyung = getValue(area, 평unit);
		int hop = getValue(area, 홉unit);
		int jac = getValue(area, 작unit);
		return pyung * 3.3058 + hop * 0.33 + jac * 0.033;
	}

	/**
	 * Count head space.
	 * 
	 * @param line
	 *            the line
	 * 
	 * @return the int
	 */
	public static int countHeadSpace(String line) {
		char[] chars = line.toCharArray();
		int count = 0;
		for (char c : chars) {
			if (c == ' ') {
				count++;
				continue;
			}
			break;
		}
		return count;
	}

	/**
	 * Parses the structure.
	 * 
	 * @param text
	 *            the text
	 * 
	 * @return the list< string>
	 */
	public static List<String> parseStructure(String text) {
		List<String> result = new LinkedList<String>();

		String[] lines = text.split("[\n\r]");
		String temp = lines[0].trim();
		for (int i = 1; i < lines.length; i++) {
			String line = lines[i];
			if (temp != null && temp.startsWith("제시외")  && isStartWithNumber(line.trim())){
				temp += "\n" + line;
			}
			else 
				if (countHeadSpace(line) == 0 && !line.equals("") || isMainStart(line) ) {
				result.add(temp);
				temp = line;
			} else {
				temp += "\n" + line;
			}
		}
		result.add(temp);
		return result;
	}

	private static boolean isStartWithNumber(String line) {
		line = line.trim();
		if (line.length()==0){
			return false;
		}
		char ch =line.charAt(0);
		
		return Character.isDigit(ch);
	}

	private static boolean isMainStart(String line) {
		line = line.trim();
		return line.startsWith("1동의 건물의 표시") //
				|| line.startsWith("전유부분의 건물의 표시") || line.startsWith("대지권의 표시")

		;
	}

	/**
	 * Gets the value.
	 * 
	 * @param area
	 *            the area
	 * @param unit
	 *            the unit
	 * 
	 * @return the value
	 */
	private int getValue(String area, Pattern unit) {
		Matcher m = unit.matcher(area);
		if (m.find()) {
			return Integer.parseInt(m.group(1));
		}
		return 0;
	}

	/**
	 * To no string.
	 * 
	 * @param value
	 *            the value
	 * 
	 * @return the string
	 */
	private String toNoString(double value) {
		int v = (int) (value * 1000);
		double v2 = ((double) v) / 1000;
		return String.valueOf(v2);

	}

	public String parse구조(String detail) {
		if (has전유부분건물표시(detail)) {
			return parse전유부분구조(detail);
		}

		if (detail.startsWith("1동의 건물의 표시")) {
			String[] lines = detail.split("\n");
			return lines[2].trim();
		}
		Pattern p = Pattern.compile("1동건물의표시: (.*)$", Pattern.MULTILINE);
		Matcher m = p.matcher(detail);
		if (m.find()) {
			return m.group(1);
		}
		String temp = detail.replaceAll("\\s", "");

		p = Pattern.compile("1동건물의표시:(.*)$", Pattern.MULTILINE);
		m = p.matcher(temp);
		if (m.find()) {
			return m.group(1);
		}

		String[] lines = detail.split("\\n");
		return lines[0];
	}

	public boolean has전유부분건물표시(String detail) {
		return detail.contains("전유부분의");
	}

	public String parse전유부분구조(String detail) {
		String hungryStr = detail.replaceAll(" ", "");
		Pattern p = Pattern.compile("구조:([^\\d]*)\\s*[\\d.\\s㎡]*$", Pattern.MULTILINE);
		Matcher m = p.matcher(hungryStr);
		if (m.find()) {
			return m.group(1).trim();
		}
		return "";
	}

	public String parse대지면적(String detail) {
		Pattern p = Pattern.compile("([\\d.\\s]+㎡)");
		Matcher m = p.matcher(convertAreaUnit(detail));
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}

	public void update종합(물건 goods) {

		if (goods.get물건종별() == null) {
			int type = 410;
			type = 부동산표시TypeMatcher.calcType(hasBuilding, has전유부분, goods.get토지목록(), goods.get건물목록(), type);
			goods.set물건종별(type);
			logger.info("물건종별:" + type);
		}
		String 매각대상 = calc매각대상(hasBuilding, hasBuildingRight, hasLand, hasLandRight, goods.get비고());

		goods.set매각대상(매각대상);
		logger.info("매각대상:" + 매각대상);

	}
}

class Entry {
	String[] text;

	int value;

	public Entry(String text, int value) {
		this.text = new String[] { text };
		this.value = value;
	}

	public Entry(String[] text, int value) {
		this.text = text;
		this.value = value;
	}

	public String[] getKey() {
		return text;
	}

	public int getValue() {
		return value;
	}

	public boolean isMatchKey(String t) {
		for (String key : text) {
			if (key.equals(t)) {
				return true;
			}
		}
		return false;
	}

	public boolean matchAnything(List<Integer> types) {
		for (Integer integer : types) {
			int v = integer.intValue();
			if (v == value) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "[" + TextUtil.join(text, ",") + "]=" + value;
	}
}
