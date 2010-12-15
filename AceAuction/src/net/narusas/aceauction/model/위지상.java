package net.narusas.aceauction.model;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.util.lang.NFile;

public class À§Áö»ó {
	static class Line {
		String area;
		boolean hasArea;
		int headSpace;
		int no;
		boolean serial = false;
		String text;

		public Line(int no, String line) {
			text = line;
			setup(line);
		}

		public String getTitle() {
			String text = this.text.trim();
			Matcher m = validHasAreaPattern.matcher(text);
			if (m.find()) {
				String temp = text.substring(0, m.start());

				int pos = temp.lastIndexOf("ÁöºØ");
				if (pos != -1) {
					Pattern p = Pattern.compile("((" + words + ")+)");
					m = p.matcher(temp);
					if (m.find()) {
						return (m.group(1) + " " + temp.substring(pos + 2))
								.trim();
					}
					return temp.substring(pos + 2).trim();
				}

				return temp;
			}

			return "";
		}

		public boolean hasMultipleData() {
			return text.contains("¹×");
		}

		public boolean isNoArea´ÜÃþ() {
			return text.contains("´ÜÃþ") && text.contains("§³") == false;
		}

		public void merge(String string) {
			this.text = this.text + " " + string.trim();
			setup(text);
		}

		private void setup(String line) {
			area = getValidArea(line);
			hasArea = area != null;
			headSpace = countSpace(line);
		}

		boolean isOnlyArea() {
			Matcher m = onlyHasAreaPattern.matcher(text.trim());
			return m.find();
		}

		boolean isOnlyFloor() {
			Pattern p = Pattern.compile("\\s*(" + words + ")\\s*[\\d.]*\\s*§³");
			Matcher m = p.matcher(text);
			return m.find();
		}
	}
	static class LineChunk {
		boolean isAreaSerial = false;
		List<Line> serial = new LinkedList<Line>();

		@Override
		public String toString() {

			String temp = "";
			for (Line line : serial) {
				temp += line.text + "\n";
			}
			return temp;
		}

		boolean isAllOnluFloor() {
			for (Line line : serial) {
				if (line.isOnlyFloor() == false) {
					return false;
				}
			}
			return true;
		}

	}
	static Pattern areaPattern = Pattern.compile("([\\d.]*\\s*§³)");
	static Pattern onlyHasAreaPattern = Pattern.compile("^[\\d.]+\\s*§³");

	static Pattern validHasAreaPattern = Pattern.compile("([\\d.]+\\s*§³)");

	static String words;

	List<ÃþÇü> floors = new LinkedList<ÃþÇü>();

	Set<ÃþÇü> floorsSet = new HashSet<ÃþÇü>();

	static {
		try {
			words = NFile.getText(new File("À§Áö»ó_words.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public À§Áö»ó(String chunk, String address) {
		parse(chunk);
	}

	public List<ÃþÇü> getFloors() {
		return floors;
	}

	private void addÃþÇü(ÃþÇü f) {
		floors.add(f);
	}

	private void parse(String chunk) {
		if (chunk.contains("³»¿ª")) {
			chunk = chunk.substring(0, chunk.indexOf("³»¿ª"));
		}
		String[] lines = splitLines(chunk);
		if (isStructures(lines)) {
			List<ÃþÇü> result = parseStructure(lines);
			for (ÃþÇü floor : result) {
				addÃþÇü(floor);
			}

		}
	}

	public static String getValidArea(String text) {
		String trimedText = text.trim();
		Matcher m = validHasAreaPattern.matcher(trimedText);
		if (m.find() && trimedText.startsWith("(") == false
				&& trimedText.endsWith(")") == false) {
			return m.group(1);
		}
		return null;
	}

	private static int countHavingAreaChunks(List<LineChunk> lineChunkList) {
		int countHavingArea = 0;
		for (LineChunk lineChunk : lineChunkList) {
			if (lineChunk.isAreaSerial) {
				countHavingArea++;
			}
		}
		return countHavingArea;
	}

	private static int countSpace(String line) {
		char[] chs = line.toCharArray();
		int count = 0;
		for (char c : chs) {
			if (c == ' ') {
				count++;
				continue;
			}
			break;
		}
		return count;
	}

	private static boolean isCompleteData(String string) {
		string = string.trim();
		if (string.startsWith("¹×")) {
			return false;
		}

		Matcher m = areaPattern.matcher(string);
		if (m.find() == false) {
			return false;
		}
		String slice = string.substring(0, m.start()).trim();
		if ("".equals(slice)) {
			return false;
		}

		return true;
	}

	private static boolean isMergableState(String line, Line last) {
		return (last.isNoArea´ÜÃþ() && line.contains("§³") && last
				.hasMultipleData() == false)
		// || (last.text.endsWith("´ÜÃþ") && isCompleteData(line))
		// || (last.isNoArea´ÜÃþ() && line.trim().startsWith("¹×"))
		// || (last.isNoArea´ÜÃþ() && last.text.endsWith("¹×"))
		;
	}

	private static String trimBrace(String line) {
		Pattern p = Pattern.compile("(\\([^\\)]+\\))");
		Matcher m = p.matcher(line);
		if (m.find()) {
			String temp = line.substring(0, m.start())
					+ line.substring(m.end());
			if (temp.contains("(")) {
				return trimBrace(temp);
			}
			return temp;
		}
		return line;
	}

	private static String trimRightSpace(String line) {
		String temp = "";
		char[] chs = line.toCharArray();
		boolean space = true;
		;
		for (int i = chs.length - 1; i >= 0; i--) {
			if (space) {
				if (chs[i] != ' ') {
					space = false;
					temp = temp + chs[i];
				}
			} else {
				temp = temp + chs[i];
			}

		}
		return temp;
	}

	static List<Line> getContents(String[] lines) {
		List<Line> lineList = new LinkedList<Line>();
		boolean content = false;
		List<Line> temp = new LinkedList<Line>();
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.equals("À§ Áö»ó")) {
				content = true;
				continue;
			}
			if (content) {
				line = trimBrace(line);
				Matcher m = onlyHasAreaPattern.matcher(line.trim());
				if (m.find()) {
					Line last = lineList.get(lineList.size() - 1);
					last.merge(line);
					continue;
				}
				if ("ºÎ¼Ó°Ç¹°".equals(line.trim())) {
					continue;
				}

				if (lineList.size() > 0) {
					Line last = lineList.get(lineList.size() - 1);
					if (isMergableState(line, last)) {
						last.merge(line);
						continue;
					}

				}

				lineList.add(new Line(i, line));
			}
		}

		List<Line> result = new LinkedList<Line>();
		for (int i = 0; i < lineList.size(); i++) {
			Line line = lineList.get(i);
			if (result.size() > 0) {
				Line last = result.get(result.size() - 1);

				if (isMergableState(line.text, last)) {
					last.merge(line.text);
					continue;
				}

			}
			result.add(line);
		}

		return result;
	}
	static boolean isStructures(String[] lines) {
		List<Line> lineList = getContents(lines);

		Line last = null;

		for (int i = 0; i < lineList.size(); i++) {
			Line line = lineList.get(i);

			if (last == null) {
				last = line;
				continue;
			}
			last = line;
			if (last.headSpace == line.headSpace) {
				continue;
			}
			return false;

		}
		return true;
	}

	static List<LineChunk> parseSerialLineChunks(List<Line> lineList) {
		List<LineChunk> lineChunkList = new LinkedList<LineChunk>();
		Line last = null;
		LineChunk lineChunk = new LineChunk();
		lineChunkList.add(lineChunk);

		for (int i = 0; i < lineList.size(); i++) {
			Line line = lineList.get(i);

			if (last == null) {
				last = line;
				lineChunk.serial.add(line);
				lineChunk.isAreaSerial = line.hasArea;
				continue;
			}

			if (last != null && line.hasArea && last.hasArea == false
					&& last.isNoArea´ÜÃþ() && line.hasMultipleData()) {
				last.merge(line.text);
				lineChunk.isAreaSerial = line.hasArea;
				continue;
			}

			if (last.hasArea == line.hasArea) {
				lineChunk.serial.add(line);
			} else {
				lineChunk = new LineChunk();
				lineChunk.isAreaSerial = line.hasArea;
				lineChunk.serial.add(line);

				lineChunkList.add(lineChunk);
			}
			last = line;
		}
		return lineChunkList;
	}

	static List<ÃþÇü> parseStructure(String[] lines) {

		List<ÃþÇü> result = new LinkedList<ÃþÇü>();

		List<Line> lineList = getContents(lines);

		List<LineChunk> lineChunkList = parseSerialLineChunks(lineList);

		int countHavingArea = countHavingAreaChunks(lineChunkList);

		// if (countHavingArea == 1) {
		for (LineChunk lineChunk : lineChunkList) {
			if (lineChunk.isAreaSerial) {
				List<Line> havingAreaLines = lineChunk.serial;
				for (Line line : havingAreaLines) {
					result.add(new ÃþÇü(line.getTitle(), line.area));
				}
			}
		}

		return result;
	}

	static String[] splitLines(String chunk) {
		return chunk.split("\n");
	}

}
