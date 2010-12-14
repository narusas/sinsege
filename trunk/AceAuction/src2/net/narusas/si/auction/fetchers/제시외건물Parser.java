package net.narusas.si.auction.fetchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.제시외건물;
import net.narusas.si.auction.model.주소;

public class 제시외건물Parser {
	public static Pattern usagePattern = Pattern.compile("\\(용도\\)([^\\(]+)");
	public static Pattern usagePattern2 = Pattern.compile("\\(용도\\)([^\\구]+)");
	public static Pattern structurePattern = Pattern.compile("\\(구조\\)([^\\(]+)");

	// static Pattern areaPattern = Pattern.compile("\\(면적\\)\\D*([\\d\\.]+)");
	
	public static Pattern containsRule1 = Pattern.compile(".*제시외[^\\.]*포함.*");
	public static Pattern containsRule2 = Pattern.compile(".*제시외[^\\.]*제외.*");
	public static Pattern containsRule3 = Pattern.compile(".*제시외[^\\.]*제외.*");

	public static void parse(물건 goods, String chunk, int 목록번호, 주소 주소) {
		String[] lines = chunk.split("\n");

		for (String line : lines) {
			String 용도 = regxGroup1(usagePattern, line);
			if (용도 ==  null || "".equals(용도.trim())){
				용도 = regxGroup1(usagePattern2, line);
				if (용도 !=  null && !"".equals(용도.trim())){
					용도 =  용도.replace('(',' ');
					용도 =  용도.replace(')',' ').trim();	
				}
				else {
					용도 = "";
				}
				
			}
			String 구조 = regxGroup1(structurePattern, line);

			String 면적 = parse면적(line);

			String 층형 = null;
			if (구조 != null) {
				층형 = parse제시외건물층형(구조);
			}

			String 포함여부 = null;
			String comment  = goods.get비고();
			
			
			if (goods.get비고().contains("제시외")) {
				if (comment.contains("매각대상아닌")){
					포함여부 = "제외";
				}
				else if (Pattern.matches(".*제시외[^\\.]*포함.*", comment)){
					포함여부 = "포함";
				}
				else if (Pattern.matches(".*제시외[^\\.]*제외.*", comment)){
					포함여부 = "제외";
				}
				else {
					포함여부 = "제외";
				}
			} else {
				포함여부 = "제외";
			}

			if (용도 == null && 구조 == null && 면적 == null) {
				continue;
			}

			goods.add제시외건물(new 제시외건물(goods, 용도, 구조, 면적, 층형, 포함여부, 목록번호, 주소.toSlimAddress()));

		}
	}

	public static String parse면적(String line) {
		if (line.indexOf("(면적)") == -1) {
			return null;
		}
		String temp = line.substring(line.indexOf("(면적)"));
		temp = temp.substring(temp.indexOf(")") + 1);
		temp = temp.replaceAll("\\)", "");
		temp = temp.replaceAll("\\(", "");
		return temp;
	}

	private static String parse제시외건물층형(String 구조) {
		if (구조.endsWith("층")) {
			return 구조.substring(구조.length() - 2);
		}
		if (구조.endsWith("지하")) {
			return "지하";
		}
		if (구조.endsWith("옥탑")) {
			return "옥탑";
		}
		return "";
	}

	public static String regxGroup1(Pattern p, String text) {
		Matcher m = p.matcher(text);
		if (m.find() == false) {
			return null;
		}
		return m.group(1);
	}
}
