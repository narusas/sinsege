/*
 * 
 */
package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.narusas.util.lang.NFile;

// TODO: Auto-generated Javadoc
/**
 * The Class 위지상.
 */
public class 위지상 {

	/**
	 * The Class Line.
	 */
	static class Line {

		/** The area. */
		String area;

		/** The has area. */
		boolean hasArea;

		/** The head space. */
		int headSpace;

		/** The no. */
		int no;

		/** The serial. */
		boolean serial = false;

		/** The text. */
		String text;

		/**
		 * Instantiates a new line.
		 * 
		 * @param no
		 *            the no
		 * @param line
		 *            the line
		 */
		public Line(int no, String line) {
			text = line;
			setup(line);
		}

		/**
		 * Gets the title.
		 * 
		 * @return the title
		 */
		public String getTitle() {
			String text = this.text.trim();
			Matcher m = validHasAreaPattern.matcher(text);
			if (m.find()) {
				String temp = text.substring(0, m.start());

				int pos = temp.lastIndexOf("지붕");
				if (pos != -1) {
					Pattern p = Pattern.compile("((" + words + ")+)");
					m = p.matcher(temp);
					if (m.find()) {
						return (m.group(1) + " " + temp.substring(pos + 2)).trim();
					}
					return temp.substring(pos + 2).trim();
				}

				return temp;
			}

			return "";
		}

		/**
		 * Checks for multiple data.
		 * 
		 * @return true, if successful
		 */
		public boolean hasMultipleData() {
			return text.contains("및");
		}

		/**
		 * Checks if is no area단층.
		 * 
		 * @return true, if is no area단층
		 */
		public boolean isNoArea단층() {
			return text.contains("단층") && text.contains("㎡") == false;
		}

		/**
		 * Merge.
		 * 
		 * @param string
		 *            the string
		 */
		public void merge(String string) {
			this.text = this.text + " " + string.trim();
			setup(text);
		}

		/**
		 * Sets the up.
		 * 
		 * @param line
		 *            the new up
		 */
		private void setup(String line) {
			area = getValidArea(line);
			hasArea = area != null;
			headSpace = countSpace(line);
		}

		/**
		 * Checks if is only area.
		 * 
		 * @return true, if is only area
		 */
		boolean isOnlyArea() {
			Matcher m = onlyHasAreaPattern.matcher(text.trim());
			return m.find();
		}

		/**
		 * Checks if is only floor.
		 * 
		 * @return true, if is only floor
		 */
		boolean isOnlyFloor() {
			Pattern p = Pattern.compile("\\s*(" + words + ")\\s*[\\d.]*\\s*㎡");
			Matcher m = p.matcher(text);
			return m.find();
		}
	}

	/**
	 * The Class LineChunk.
	 */
	static class LineChunk {

		/** The is area serial. */
		boolean isAreaSerial = false;

		/** The serial. */
		List<Line> serial = new LinkedList<Line>();

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {

			String temp = "";
			for (Line line : serial) {
				temp += line.text + "\n";
			}
			return temp;
		}

		/**
		 * Checks if is all onlu floor.
		 * 
		 * @return true, if is all onlu floor
		 */
		boolean isAllOnluFloor() {
			for (Line line : serial) {
				if (line.isOnlyFloor() == false) {
					return false;
				}
			}
			return true;
		}

	}

	/** The area pattern. */
	static Pattern areaPattern = Pattern.compile("([\\d.]*\\s*㎡)");

	/** The only has area pattern. */
	static Pattern onlyHasAreaPattern = Pattern.compile("^[\\d.]+\\s*㎡");

	/** The valid has area pattern. */
	static Pattern validHasAreaPattern = Pattern.compile("([\\d.]+\\s*㎡)");

	/** The words. */
	static String words;

	/** The floors. */
	List<층형> floors = new LinkedList<층형>();

	/** The floors set. */
	Set<층형> floorsSet = new HashSet<층형>();

	static {
		try {
			words = NFile.getText(new File("cfg/위지상_words.txt"),"euc-kr");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Instantiates a new 위지상.
	 * 
	 * @param chunk
	 *            the chunk
	 * @param address
	 *            the address
	 */
	public 위지상(String chunk, String address) {
		parse(chunk);
	}

	/**
	 * Gets the floors.
	 * 
	 * @return the floors
	 */
	public List<층형> getFloors() {
		return floors;
	}

	/**
	 * Add층형.
	 * 
	 * @param f
	 *            the f
	 */
	private void add층형(층형 f) {
		floors.add(f);
	}

	/**
	 * Parses the.
	 * 
	 * @param chunk
	 *            the chunk
	 */
	private void parse(String chunk) {
		if (chunk.contains("내역")) {
			chunk = chunk.substring(0, chunk.indexOf("내역"));
		}
		String[] lines = splitLines(chunk);
		if (isStructures(lines)) {
			List<층형> result = parseStructure(lines);
			for (층형 floor : result) {
				add층형(floor);
			}

		}
	}

	/**
	 * Gets the valid area.
	 * 
	 * @param text
	 *            the text
	 * 
	 * @return the valid area
	 */
	public static String getValidArea(String text) {
		String trimedText = text.trim();
		Matcher m = validHasAreaPattern.matcher(trimedText);
		if (m.find() && trimedText.startsWith("(") == false && trimedText.endsWith(")") == false) {
			return m.group(1);
		}
		return null;
	}

	/**
	 * Count having area chunks.
	 * 
	 * @param lineChunkList
	 *            the line chunk list
	 * 
	 * @return the int
	 */
	private static int countHavingAreaChunks(List<LineChunk> lineChunkList) {
		int countHavingArea = 0;
		for (LineChunk lineChunk : lineChunkList) {
			if (lineChunk.isAreaSerial) {
				countHavingArea++;
			}
		}
		return countHavingArea;
	}

	/**
	 * Count space.
	 * 
	 * @param line
	 *            the line
	 * 
	 * @return the int
	 */
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

	/**
	 * Checks if is complete data.
	 * 
	 * @param string
	 *            the string
	 * 
	 * @return true, if is complete data
	 */
	private static boolean isCompleteData(String string) {
		string = string.trim();
		if (string.startsWith("및")) {
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

	/**
	 * Checks if is mergable state.
	 * 
	 * @param line
	 *            the line
	 * @param last
	 *            the last
	 * 
	 * @return true, if is mergable state
	 */
	private static boolean isMergableState(String line, Line last) {
		return (last.isNoArea단층() && line.contains("㎡") && last.hasMultipleData() == false)
		// || (last.text.endsWith("단층") && isCompleteData(line))
		// || (last.isNoArea단층() && line.trim().startsWith("및"))
		// || (last.isNoArea단층() && last.text.endsWith("및"))
		;
	}

	/**
	 * Trim brace.
	 * 
	 * @param line
	 *            the line
	 * 
	 * @return the string
	 */
	private static String trimBrace(String line) {
		Pattern p = Pattern.compile("(\\([^\\)]+\\))");
		Matcher m = p.matcher(line);
		if (m.find()) {
			String temp = line.substring(0, m.start()) + line.substring(m.end());
			if (temp.contains("(")) {
				return trimBrace(temp);
			}
			return temp;
		}
		return line;
	}

	/**
	 * Trim right space.
	 * 
	 * @param line
	 *            the line
	 * 
	 * @return the string
	 */
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

	/**
	 * Gets the contents.
	 * 
	 * @param lines
	 *            the lines
	 * 
	 * @return the contents
	 */
	static List<Line> getContents(String[] lines) {
		List<Line> lineList = new LinkedList<Line>();
		boolean content = false;
		List<Line> temp = new LinkedList<Line>();
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.equals("위 지상")) {
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
				if ("부속건물".equals(line.trim())) {
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

	/**
	 * Checks if is structures.
	 * 
	 * @param lines
	 *            the lines
	 * 
	 * @return true, if is structures
	 */
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

	/**
	 * Parses the serial line chunks.
	 * 
	 * @param lineList
	 *            the line list
	 * 
	 * @return the list< line chunk>
	 */
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

			if (last != null && line.hasArea && last.hasArea == false && last.isNoArea단층()
					&& line.hasMultipleData()) {
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

	/**
	 * Parses the structure.
	 * 
	 * @param lines
	 *            the lines
	 * 
	 * @return the list<층형>
	 */
	static List<층형> parseStructure(String[] lines) {

		List<층형> result = new LinkedList<층형>();

		List<Line> lineList = getContents(lines);

		List<LineChunk> lineChunkList = parseSerialLineChunks(lineList);

		int countHavingArea = countHavingAreaChunks(lineChunkList);

		// if (countHavingArea == 1) {
		for (LineChunk lineChunk : lineChunkList) {
			if (lineChunk.isAreaSerial) {
				List<Line> havingAreaLines = lineChunk.serial;
				for (Line line : havingAreaLines) {
					result.add(new 층형(line.getTitle(), line.area));
				}
			}
		}

		return result;
	}

	/**
	 * Split lines.
	 * 
	 * @param chunk
	 *            the chunk
	 * 
	 * @return the string[]
	 */
	static String[] splitLines(String chunk) {
		return chunk.split("\n");
	}

}
