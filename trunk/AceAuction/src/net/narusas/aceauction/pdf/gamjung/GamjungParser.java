package net.narusas.aceauction.pdf.gamjung;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.aceauction.pdf.PDFStripper;

import org.pdfbox.pdmodel.PDDocument;

public class GamjungParser {
	public class Group {
		public String name;
		List<Part> parts = new LinkedList<Part>();

		public boolean continueFrom(Group last) {
			if (last == null) {
				return false;
			}
			int firstNo = getFirstNo();
			int lastNo = last.getLastNo();//
			return firstNo == lastNo || firstNo == lastNo + 1;
		}

		public String get냉난방() {
			return find("냉난방설비|위생및냉난방설비|위생냉난방시설등|위생설비및냉난방설비등|냉난방설비등주요설비및기타설비|위생냉난방설비및기타설비|냉난방설비등");
		}

		public String get도로관련() {
			return find("인접도로상태|도로상태|도로상태및주차장시설등");
		}

		public String get위치및교통() {
			String res = "";
			List<String> buf = new LinkedList<String>();
			String[] targets = new String[] { "위치및교통", "위치및부근의상황", "위치교통및주위환경", "위치및주위환경", "주위환경", "교통상황", "위치", "교통" };
			for (int i = 0; i < targets.length; i++) {

				String temp = find(targets[i]);
				if (temp != null && "".equals(temp) == false) {

					if (isAlreadReaded(buf, temp) == false) {
						buf.add(temp);
					}
				}
			}

			for (int i = 0; i < buf.size(); i++) {
				res += buf.get(i) + "\n";
			}
			res = res.trim();
			return res;// ;find("위치및교통|위치및부근의상황|위치교통및주위환경|위치및주위환경|주위환경|교통상황|위치|교통");
		}

		private boolean isAlreadReaded(List<String> buf, String temp) {
			for (int i = 0; i < buf.size(); i++) {
				if (buf.get(i).equals(temp)) {
					return true;
				}
			}
			return false;
		}

		public String get이용상태() {
			return find("형상및이용상태|형태및이용상태|토지의상황|형상및이용상황|토지형태및이용상황|형태지세및이용상태|토지의형태및이용상태|형상·지세및이용상태|토지의형상및이용상태|형태|임지상황|토지의형태|토지상황|형태및이용상황|토지의형태및이용상태및인접도로");
		}

		public String get토지() {
			return find("토지이용계획관계및제한상태|토지이용계획및기타공법상제한상태|도시계획관계및기타공법상제한상태|도시계획관계및공법상제한상태|토지이용계획관계|토지이용계획및기타공법상관계|도시계획및기타공법상관계|도시계획및기타공법관계|토지이용계획관계및공법상제한상태|도시계획관계및공법상제한의상태|토지이용계획및공법상제한|도시계획및기타공법상의제한상태");
		}

		public void merge(Group group) {
			int lastNo = getLastNo();
			for (Part part : group.parts) {
				if (lastNo == part.getNumber()) {
					continue;
				}
				parts.add(part);
			}
		}

		private String find(String target) {
			Pattern p = Pattern.compile(target);
			for (Part part : parts) {
				Matcher m = p.matcher(part.title);
				if (m.find()) {
					return part.body;
				}
			}

			return "";
		}

		private int getFirstNo() {
			return parts.get(0).getNumber();
		}

		private int getLastNo() {
			return parts.get(parts.size() - 1).getNumber();
		}
	}

	class Part {
		String body = "";
		String title;

		public int getNumber() {
			Matcher m = numberP.matcher(title);
			m.find();
			return Integer.parseInt(m.group(1));
		}
	}

	static Pattern numberP = Pattern.compile("^(\\d+)\\.");
	static Pattern numberStartP = Pattern.compile("^\\d+\\.");

	static Pattern srcP = Pattern.compile("감정평가법인|감정평가사|감정원");
	static Pattern srcP2 = Pattern.compile("한국감정원은");

	String date;

	String src;

	public String getDate() {
		return date;
	}

	public String getSrc() {
		return src;
	}

	public List<Group> parse(File f) throws IOException {
		PDFStripper stripper = new PDFStripper();
		String text = stripper.getText(PDDocument.load(f));
		System.out.println(text);
		String[] lines = text.split("\r|\n");
		src = parseSource(lines);
		date = parseDate(text);

		// System.out.println("SRC:" + src);
		List<String> 요향표Text = explode요향표(lines, src);
		List<Group> groups = parseGroups(요향표Text);
		if (src == null) {
			src = "";
		}

		return sort(stripGroups(groups));
	}

	private List<String> explode요향표(String[] lines, String src) {
		if (src == null) {
			src = "한국감정원";
		}
		List<String> res = new ArrayList<String>();
		boolean isStart = false;
		for (String line : lines) {
			String trimedLine = trimSpace(line);
			if (trimedLine.contains("요항표")) {
				// System.out.println(trimedLine);
				res.add(trimedLine);
				isStart = true;

			}
			if (isStart) {
				if (trimedLine == null) {
					continue;
				}
				if ("".equals(trimedLine) || trimedLine.contains("열람용") || trimedLine.contains(src)
						|| trimedLine.contains("요항표") || trimedLine.startsWith("기호") || trimedLine.contains("개황도")) {
					continue;
				}
				// 위치도|사진용지|내부구조도
				if (trimedLine.contains("위치도") || trimedLine.contains("사진용지") || trimedLine.contains("내부구조도")) {
					break;
				}
				res.add(line.trim());
			}
		}
		return res;
	}

	private String parseDate(String text) {
		Pattern p = Pattern.compile("(\\d{4,4})[.-]+\\s*(\\d+)[.-]+\\s*(\\d+)");

		Matcher m = p.matcher(text);
		if (m.find()) {
			String res = m.group(1) + "." + m.group(2) + "." + m.group(3);
			if (res.startsWith("20") || res.startsWith("19")) {
				return res;
			}
		}
		return "";
	}

	private List<Group> parseGroups(List<String> lines) {
		List<Group> groups = new LinkedList<Group>();
		Group group = new Group();
		Part temp = new Part();
		for (String line : lines) {
			if (line.contains("요항표")) {
				group.parts.add(temp);
				group = new Group();
				groups.add(group);
				group.name = line;
				continue;
			}

			Matcher m = numberStartP.matcher(line);
			if (m.find()) {

				if (temp.title != null) {
					group.parts.add(temp);
					temp = new Part();
				}

				temp.title = trimSpace(line);
			} else {
				line = line.trim();
				if (temp.body.endsWith(".") == false) {
					temp.body += line;
				} else {
					temp.body += line + "\n";
				}

			}
		}
		group.parts.add(temp);
		return groups;
	}

	private List<Group> sort(List<Group> stripGroups) {
		List<Group> res = new LinkedList<Group>();
		Group last = null;
		for (Group group : stripGroups) {
			if (group.continueFrom(last)) {
				last.merge(group);
				continue;
			}
			last = group;
			res.add(last);
		}
		return res;
	}

	private List<Part> strip(List<Part> parts) {
		List<Part> res = new LinkedList<Part>();
		for (Part part : parts) {
			if ("".equals(part.body.trim())) {
				continue;
			}
			res.add(part);
		}
		return res;
	}

	private List<Group> stripGroups(List<Group> groups) {
		for (Group group : groups) {
			List<Part> parts = strip(group.parts);
			group.parts.clear();
			for (Part part : parts) {
				group.parts.add(part);
			}

		}
		return groups;
	}

	private String trimSpace(String line) {
		return line.replaceAll(" ", "");
	}

	void printParts(List<Group> groups) {
		for (Group group : groups) {
			System.out.println("############ " + group.name);

			for (Part part : group.parts) {
				System.out.println("###########");
				System.out.println(part.title);
				System.out.println(part.body);
			}

		}
	}

	public static void main(String[] args) throws IOException {
		GamjungParser parser = new GamjungParser();
		parser.printParts(parser.parse(new File("fixtures/감정평가서2861.pdf")));
	}

	public static String parseSource(String[] lines) {
		String[] targets = { "감정평가법인", "감정평가사", "감정원", "법인", "사무소", "감정원" };

		String src = null;
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i].replaceAll("\\s+", "");
			// System.out.println(i + ":" + line);
			for (String target : targets) {
				Pattern pattern = Pattern.compile(target);
				Matcher m = pattern.matcher(line);
				if (m.find()) {
					String splited = line.substring(0, m.start());
					Pattern p2 = Pattern.compile("([^\\(\\)]+)$");
					Matcher m2 = p2.matcher(splited);
					m2.find();

					return m2.group(1) + target;
				}
			}

			Matcher m = srcP.matcher(lines[i]);
			if (m.find()) {
				m = srcP2.matcher(lines[i]);
				src = lines[i];
				if (m.find()) {
					src = "한국감정원";
				}
				break;
			}
		}

		src = validateSrc(src, "(\\w+법인)");
		src = validateSrc(src, "(\\w+사무소)");
		src = validateSrc(src, "감정원");

		return src;
	}

	static String validateSrc(String src, String target) {
		if (src == null || target == null) {
			return src;
		}
		if (src.contains(target)) {
			src = src.substring(0, src.indexOf(target) + target.length());
		}
		return src;
	}
}
