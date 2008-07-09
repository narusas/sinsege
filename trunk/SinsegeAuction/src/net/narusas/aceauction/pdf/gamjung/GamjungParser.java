/*
 * 
 */
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

// TODO: Auto-generated Javadoc
/**
 * The Class GamjungParser.
 */
public class GamjungParser {
	
	/**
	 * The Class Group.
	 */
	public class Group {
		
		/** The name. */
		public String name;
		
		/** The parts. */
		List<Part> parts = new LinkedList<Part>();

		/**
		 * Continue from.
		 * 
		 * @param last the last
		 * 
		 * @return true, if successful
		 */
		public boolean continueFrom(Group last) {
			if (last == null) {
				return false;
			}
			int firstNo = getFirstNo();
			int lastNo = last.getLastNo();//
			return firstNo == lastNo || firstNo == lastNo + 1;
		}

		/**
		 * Gets the �ó���.
		 * 
		 * @return the �ó���
		 */
		public String get�ó���() {
			return find("�ó��漳��|�����׳ó��漳��|�����ó���ü���|��������׳ó��漳���|�ó��漳����ֿ伳��ױ�Ÿ����|�����ó��漳��ױ�Ÿ����|�ó��漳���");
		}

		/**
		 * Gets the ���ΰ���.
		 * 
		 * @return the ���ΰ���
		 */
		public String get���ΰ���() {
			return find("�������λ���|���λ���|���λ��¹�������ü���");
		}

		/**
		 * Gets the ��ġ�ױ���.
		 * 
		 * @return the ��ġ�ױ���
		 */
		public String get��ġ�ױ���() {
			String res = "";
			List<String> buf = new LinkedList<String>();
			String[] targets = new String[] { "��ġ�ױ���", "��ġ�׺α��ǻ�Ȳ", "��ġ���������ȯ��", "��ġ������ȯ��", "����ȯ��", "�����Ȳ", "��ġ", "����" };
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
			return res;// ;find("��ġ�ױ���|��ġ�׺α��ǻ�Ȳ|��ġ���������ȯ��|��ġ������ȯ��|����ȯ��|�����Ȳ|��ġ|����");
		}

		/**
		 * Checks if is alread readed.
		 * 
		 * @param buf the buf
		 * @param temp the temp
		 * 
		 * @return true, if is alread readed
		 */
		private boolean isAlreadReaded(List<String> buf, String temp) {
			for (int i = 0; i < buf.size(); i++) {
				if (buf.get(i).equals(temp)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Gets the �̿����.
		 * 
		 * @return the �̿����
		 */
		public String get�̿����() {
			return find("������̿����|���¹��̿����|�����ǻ�Ȳ|������̿��Ȳ|�������¹��̿��Ȳ|�����������̿����|���������¹��̿����|�����������̿����|������������̿����|����|������Ȳ|����������|������Ȳ|���¹��̿��Ȳ|���������¹��̿���¹���������");
		}

		/**
		 * Gets the ����.
		 * 
		 * @return the ����
		 */
		public String get����() {
			return find("�����̿��ȹ��������ѻ���|�����̿��ȹ�ױ�Ÿ���������ѻ���|���ð�ȹ����ױ�Ÿ���������ѻ���|���ð�ȹ����װ��������ѻ���|�����̿��ȹ����|�����̿��ȹ�ױ�Ÿ���������|���ð�ȹ�ױ�Ÿ���������|���ð�ȹ�ױ�Ÿ��������|�����̿��ȹ����װ��������ѻ���|���ð�ȹ����װ����������ǻ���|�����̿��ȹ�װ���������|���ð�ȹ�ױ�Ÿ�����������ѻ���");
		}

		/**
		 * Merge.
		 * 
		 * @param group the group
		 */
		public void merge(Group group) {
			int lastNo = getLastNo();
			for (Part part : group.parts) {
				if (lastNo == part.getNumber()) {
					continue;
				}
				parts.add(part);
			}
		}

		/**
		 * Find.
		 * 
		 * @param target the target
		 * 
		 * @return the string
		 */
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

		/**
		 * Gets the first no.
		 * 
		 * @return the first no
		 */
		private int getFirstNo() {
			return parts.get(0).getNumber();
		}

		/**
		 * Gets the last no.
		 * 
		 * @return the last no
		 */
		private int getLastNo() {
			return parts.get(parts.size() - 1).getNumber();
		}
	}

	/**
	 * The Class Part.
	 */
	class Part {
		
		/** The body. */
		String body = "";
		
		/** The title. */
		String title;

		/**
		 * Gets the number.
		 * 
		 * @return the number
		 */
		public int getNumber() {
			Matcher m = numberP.matcher(title);
			m.find();
			return Integer.parseInt(m.group(1));
		}
	}

	/** The number p. */
	static Pattern numberP = Pattern.compile("^(\\d+)\\.");
	
	/** The number start p. */
	static Pattern numberStartP = Pattern.compile("^\\d+\\.");

	/** The src p. */
	static Pattern srcP = Pattern.compile("�����򰡹���|�����򰡻�|������");
	
	/** The src p2. */
	static Pattern srcP2 = Pattern.compile("�ѱ���������");

	/** The date. */
	String date;

	/** The src. */
	String src;

	/**
	 * Gets the date.
	 * 
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Gets the src.
	 * 
	 * @return the src
	 */
	public String getSrc() {
		return src;
	}

	/**
	 * Parses the.
	 * 
	 * @param f the f
	 * 
	 * @return the list< group>
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Group> parse(File f) throws IOException {
		PDFStripper stripper = new PDFStripper();
		String text = stripper.getText(PDDocument.load(f));
		System.out.println(text);
		String[] lines = text.split("\r|\n");
		src = parseSource(lines);
		date = parseDate(text);

		// System.out.println("SRC:" + src);
		List<String> ����ǥText = explode����ǥ(lines, src);
		List<Group> groups = parseGroups(����ǥText);
		if (src == null) {
			src = "";
		}

		return sort(stripGroups(groups));
	}

	/**
	 * Explode����ǥ.
	 * 
	 * @param lines the lines
	 * @param src the src
	 * 
	 * @return the list< string>
	 */
	private List<String> explode����ǥ(String[] lines, String src) {
		if (src == null) {
			src = "�ѱ�������";
		}
		List<String> res = new ArrayList<String>();
		boolean isStart = false;
		for (String line : lines) {
			String trimedLine = trimSpace(line);
			if (trimedLine.contains("����ǥ")) {
				// System.out.println(trimedLine);
				res.add(trimedLine);
				isStart = true;

			}
			if (isStart) {
				if (trimedLine == null) {
					continue;
				}
				if ("".equals(trimedLine) || trimedLine.contains("������") || trimedLine.contains(src)
						|| trimedLine.contains("����ǥ") || trimedLine.startsWith("��ȣ") || trimedLine.contains("��Ȳ��")) {
					continue;
				}
				// ��ġ��|��������|���α�����
				if (trimedLine.contains("��ġ��") || trimedLine.contains("��������") || trimedLine.contains("���α�����")) {
					break;
				}
				res.add(line.trim());
			}
		}
		return res;
	}

	/**
	 * Parses the date.
	 * 
	 * @param text the text
	 * 
	 * @return the string
	 */
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

	/**
	 * Parses the groups.
	 * 
	 * @param lines the lines
	 * 
	 * @return the list< group>
	 */
	private List<Group> parseGroups(List<String> lines) {
		List<Group> groups = new LinkedList<Group>();
		Group group = new Group();
		Part temp = new Part();
		for (String line : lines) {
			if (line.contains("����ǥ")) {
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

	/**
	 * Sort.
	 * 
	 * @param stripGroups the strip groups
	 * 
	 * @return the list< group>
	 */
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

	/**
	 * Strip.
	 * 
	 * @param parts the parts
	 * 
	 * @return the list< part>
	 */
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

	/**
	 * Strip groups.
	 * 
	 * @param groups the groups
	 * 
	 * @return the list< group>
	 */
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

	/**
	 * Trim space.
	 * 
	 * @param line the line
	 * 
	 * @return the string
	 */
	private String trimSpace(String line) {
		return line.replaceAll(" ", "");
	}

	/**
	 * Prints the parts.
	 * 
	 * @param groups the groups
	 */
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

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		GamjungParser parser = new GamjungParser();
		parser.printParts(parser.parse(new File("fixtures/�����򰡼�2861.pdf")));
	}

	/**
	 * Parses the source.
	 * 
	 * @param lines the lines
	 * 
	 * @return the string
	 */
	public static String parseSource(String[] lines) {
		String[] targets = { "�����򰡹���", "�����򰡻�", "������", "����", "�繫��", "������" };

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
					src = "�ѱ�������";
				}
				break;
			}
		}

		src = validateSrc(src, "(\\w+����)");
		src = validateSrc(src, "(\\w+�繫��)");
		src = validateSrc(src, "������");

		return src;
	}

	/**
	 * Validate src.
	 * 
	 * @param src the src
	 * @param target the target
	 * 
	 * @return the string
	 */
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
