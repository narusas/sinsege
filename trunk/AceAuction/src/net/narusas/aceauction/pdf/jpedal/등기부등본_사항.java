package net.narusas.aceauction.pdf.jpedal;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ���ε_���� {
	class KeyValue {
		String key;

		String value;

		public KeyValue(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return key + "=" + value;
		}
	}

	static class PatternValue {
		Matcher m;

		Pattern pattern;

		String value;

		public PatternValue(Pattern p, String v) {
			pattern = p;
			value = v;
		}

		boolean isMatch(String text) {
			m = pattern.matcher(flat(text));
			return m.find();
		}
	}

	public static final int �Ҹ� = 2;

	public static int �Ҹ���� = 1;

	public static final int �μ� = 3;

	static Pattern becausePattern = Pattern.compile("(\\d+��\\s*\\d+��\\s*\\d+��\n)");

	static Pattern dateNoPattern = Pattern.compile("^(��\\d+ȣ)");

	static Pattern datePattern = Pattern.compile("(\\d+��\\s*\\d+��\\s*\\d+��)");

	static Pattern datePattern2 = Pattern.compile("(\\d+)��(\\d+)��(\\d+)��");

	static Pattern eventNoPattern = Pattern.compile("(\\d+Ÿ��\\d+)");

	static Pattern modifyPattern1 = Pattern.compile("(\\d+[-]*[\\d]*)��.*����$");

	static Pattern modifyPattern2 = Pattern.compile("(\\d+[-]*[\\d]*)��.*����$");

	static Pattern modifyPattern3 = Pattern.compile("(\\d+[-]*[\\d]*)��.*�����(�Ϻ�|����)*����$");

	static Pattern modifyPattern4 = Pattern.compile("(\\d+[-]*[\\d]*)��.*�������(�Ϻ�|����)*����$");

	static Pattern modifyPattern5 = Pattern.compile("(\\d+[-]*[\\d]*)��.*�����(�Ϻ�|����)*����$");

	static Pattern modifyPattern6 = Pattern.compile("(\\d+[-]*[\\d]*)��.*������(�Ϻ�|����)*����$");

	static String[] peopleKeys = { "���������", "�������", "������", "������", "������", "��Ź��", "�Ǹ���", "�������",
			"��������", "��������", "ä����", "��������", };

	static Pattern removePattern = Pattern.compile("(\\d+[-]*[\\d]*)��.*����$");

	static Pattern removePattern2 = Pattern.compile("(\\d+[-]*[\\d]*)��.*��������$");

	static List<PatternValue> replace = new LinkedList<PatternValue>();

	static String[] rightKeys = { "ä���ְ��", "���������", "�������", "û���ݾ�", "û����", "������", "������", "������",
			"��Ź��", "�Ǹ���", "�������", "��������", "ó��û", "����", "����", "����", "��������", "������", "���ӱⰣ", "ä����",
			"ä�Ǿ�", "��������" };

	private boolean doNotHandle = false;

	private int flag;

	private String newPurpose;

	private String newRight;

	private HashMap<String, Set<String>> peoples = new HashMap<String, Set<String>>();

	private �������� purpose;

	private List<KeyValue> rightValues = new LinkedList<KeyValue>();

	private boolean willDelete;
	LinkedList<��������> purposes = new LinkedList<��������>();

	Map<String, String> rightMap = new HashMap<String, String>();

	final TextPosition text;

	static {
		replace.add(new PatternValue(Pattern.compile("(.*)�����$"), "�����"));
		replace.add(new PatternValue(Pattern.compile("(.*û����)$"), "�����"));
		replace.add(new PatternValue(Pattern.compile("(.*)���з�$"), "���з�"));
		replace.add(new PatternValue(Pattern.compile("(.*)��ó��$"), "��ó��"));

		replace.add(new PatternValue(Pattern
				.compile("([\\d\\.]+����[\\d\\.]+.*[\\d\\.]+����[\\d\\.]+.*��������)"), "����������"));
		replace.add(new PatternValue(Pattern.compile("([\\d\\.]+�������ι�[\\d\\.]+�����Ϻ�����)"), "����������"));

		replace.add(new PatternValue(Pattern.compile("([\\d\\.]+�������ι�[\\d\\.]+�����Ϻ�����)"), "����������"));
		replace.add(new PatternValue(Pattern.compile("([\\d\\.]+�������ι�[\\d\\.]+�����Ϻ�����)"), "����������"));
		replace.add(new PatternValue(Pattern.compile("([\\d\\.]+�������ι�[\\d\\.]+�����Ϻ�����)"), "����������"));
		replace.add(new PatternValue(Pattern.compile("([\\d\\.]+�������ι�[\\d\\.]+�����Ϻ�����)"), "����������"));

		replace.add(new PatternValue(Pattern.compile("(������������������������)"), "����������"));
		replace.add(new PatternValue(Pattern.compile("(����������������������)"), "����������"));
		replace.add(new PatternValue(Pattern.compile("(����������������������û����)"), "����������"));
		replace.add(new PatternValue(Pattern.compile("(������������)"), "����������"));

		replace.add(new PatternValue(Pattern
				.compile("(.+\\s*����[\\d\\.]+����[\\d\\.]+��\\s+�Ϻ�\\([\\d\\.]+����[\\d\\.]+\\)����)"),
				"����������"));
		replace.add(new PatternValue(Pattern.compile("(.+\\s*������������)"), "����������"));
		replace.add(new PatternValue(Pattern
				.compile("(.+\\s*����[\\d\\.]+������[\\d\\.]+����[\\d\\.]+����)"), "����������"));

		replace.add(new PatternValue(Pattern
				.compile("(.+\\s*����[\\d\\.\\s]+����[\\d\\.\\s]+��[\\d\\.\\s]+����)"), "����������"));

		replace.add(new PatternValue(Pattern.compile("(.*�Ϻ�����)$"), "����������"));
		replace.add(new PatternValue(Pattern.compile("(.*��������)$"), "����������"));
		replace.add(new PatternValue(Pattern.compile("(.*�������μ���������)$"), "����������"));
		replace.add(new PatternValue(Pattern.compile("(.*������������)$"), "����������"));
		replace.add(new PatternValue(Pattern.compile("(.*������������û����)$"), "����������"));

		replace.add(new PatternValue(Pattern.compile("(.*)����$"), "����������"));

		replace.add(new PatternValue(Pattern.compile("(.*)�з�$"), "�з�"));

		replace.add(new PatternValue(Pattern.compile("(.*)������$"), "������"));

		replace.add(new PatternValue(Pattern.compile("(.*)���ǰ�Ű��ð���$"), "���ǰ��"));
		replace.add(new PatternValue(Pattern.compile("(.*)������Ű��ð���$"), "�������"));

		replace.add(new PatternValue(Pattern.compile("(.*��������)���ǰ�Ž�û$"), "���ǰ��"));

		replace.add(new PatternValue(Pattern.compile("(.*)������"), "������"));
		replace.add(new PatternValue(Pattern.compile("(.*)������Ǽ���"), "�������"));
		replace.add(new PatternValue(Pattern.compile("(.*)����Ǽ���"), "�����"));
		replace.add(new PatternValue(Pattern.compile("(.*)�����Ǽ���"), "������"));
		replace.add(new PatternValue(Pattern.compile("(.*)����Ǽ���"), "�����"));
		replace.add(new PatternValue(Pattern.compile("(.*)ȯ��Ư��"), "ȯ��Ư��"));

	}

	public ���ε_����(TextPosition text) {
		this.text = text;
	}

	public void addAcceptDate(TextPosition entity) {
		initPurpose();
		String text = entity.getText();
		Matcher m = dateNoPattern.matcher(text);
		if (m.find()) {
			if (purpose.acceptDate.size() > 0) {
				TextPosition t = purpose.acceptDate.remove(purpose.acceptDate.size() - 1);
				purpose.addAcceptDate(t.addVertical(entity));
			} else {
				purpose.addAcceptDate(entity);
			}

		} else {
			purpose.addAcceptDate(entity);
		}

	}

	public void addBecause(TextPosition entity) {
		initPurpose();
		purpose.addBecause(entity);

	}

	public void addPurpose(TextPosition entity) {
		purpose = new ��������(entity);
		purposes.add(purpose);

	}

	public void addRight(TextPosition entity) {
		initPurpose();
		purpose.addRightAndEtc(entity);
	}

	public List<KeyValue> collectRight() {
		List<KeyValue> res = new LinkedList<KeyValue>();
		String src = getRight();
		// System.out.println(src);
		for (String key : rightKeys) {
			String text = getRight(key, src);
			if ("".equals(text) || text == null) {
				continue;
			}
			res.add(new KeyValue(key, text));
		}
		return res;
	}

	public void doNotHandle(boolean b) {
		doNotHandle = b;
	}

	public String getAcceptDate() {
		String text = purposes.get(0).getAcceptDate();
		Matcher m = datePattern.matcher(text);
		if (m.find()) {
			return m.group(1);
		}
		return text;
	}

	public long getAcceptDateLong() {
		String date = getAcceptDate();
		Matcher m = datePattern2.matcher(date);
		if (m.find()) {
			Date d = new Date(toInt(m.group(1)), toInt(m.group(2)), toInt(m.group(3)));
			return d.getTime();
		}
		return 0;
	}

	public String getBecause() {
		String text = purposes.get(purposes.size() - 1).getBecause();
		Matcher m = becausePattern.matcher(text);
		if (m.find()) {
			String toRemove = m.group(1);
			return text.substring(toRemove.length());
		}
		return text;
	}

	public String getBigo() {
		String text = newPurpose == null ? getPurposeOriginal() : newPurpose;
		return parsePurposeBigo(flat(text));
	}

	public String getFlagString() {
		if (flag == �Ҹ����) {
			return "�Ҹ����";
		}
		if (flag == �Ҹ�) {
			return "�Ҹ�";
		}
		if (flag == �μ�) {
			return "�μ�";
		}
		return "NONE";
	}

	public String getPurpose() {
		String text = flat(newPurpose == null ? getPurposeOriginal() : newPurpose);
		return parsePurpose(text);
	}

	/**
	 * @return
	 */
	public String getPurposeOriginal() {
		return purposes.get(purposes.size() - 1).getPurpose();
	}

	public String getRight() {
		if (newRight == null) {
			if (purposes.size() > 0) {
				List<TextPosition> r = purposes.get(purposes.size() - 1).getRight();
				if (r.size() > 0) {
					return r.get(0).getText();
				}
				return "";
			} else {
				return "";
			}
		}
		return newRight;
	}

	public String getRight(String key) {
		if (rightMap.containsKey(key)) {

			return rightMap.get(key);
		}
		for (KeyValue pair : rightValues) {
			if (key.equals(pair.getKey())) {
				return pair.value;
			}
		}
		return "";
	}

	public Map<String, String> getRightItems() {
		Map<String, String> res = new HashMap<String, String>();
		for (String key : rightKeys) {
			String value = getRight(key, getRight());
			if (value == null || "".equals(value)) {
				continue;
			}
			res.put(key, value);
		}

		return res;
	}

	public Map<String, String> getRightMap() {
		return rightMap;
	}

	public String getRightPeople() {
		List<KeyValue> list = collectRight();
		for (int i = 0; i < list.size(); i++) {
			KeyValue entry = list.get(i);
			String key = entry.getKey();
			if (key.endsWith("��")) {

				if (list.size() > i + 1) {
					KeyValue temp = list.get(i + 1);
					if (temp.getKey().equals("ó��û")) {
						return temp.getValue();
					}
				}
				String src = entry.getValue();

				StringTokenizer lines = new StringTokenizer(src, "\r\n");
				String line = lines.nextToken();

				StringTokenizer tokens = new StringTokenizer(line, " \t");
				StringBuffer buf = new StringBuffer();
				while (tokens.hasMoreTokens()) {
					String token = tokens.nextToken();
					char ch = token.charAt(0);
					if (Character.isDigit(ch)) {
						break;
					}
					buf.append(token).append(" ");
				}
				return buf.toString().trim();

				// return ;oke

				// String[] tokens = entry.getValue().split(" ");
				// if ("�ֽ�ȸ��" .equals(tokens[0])){
				//					
				// return nextWord(tokens);
				// }
				// else if ("(��)" .equals(tokens[0])){
				// return nextWord(tokens);
				// }

				// return tokens[0];

				// StringBuffer buf = new StringBuffer();
				// for (String token : tokens) {
				// if (token == null || "".equals(token)){
				// continue;
				// }
				// char ch = token.charAt(0);
				// if (Character.isDigit(ch)) {
				// break;
				// }
				// buf.append(token).append(" ");
				// }
				// return buf.toString().trim();
			}
		}
		return "NONE";
	}

	public Set<String> getRightPeoples() {
		HashSet<String> res = new HashSet<String>();
		for (String peopleKey : peopleKeys) {
			for (KeyValue pair : rightValues) {
				if (rightMap.containsKey(peopleKey)) {
					String value = rightMap.get(peopleKey);
					String[] find = get�Ǹ���(value);
					java.util.Collections.addAll(res, find);

				} else if (peoples.containsKey(peopleKey)) {
					res.addAll(peoples.get(peopleKey));
				} else if (peopleKey.equals(pair.getKey())) {
					String[] find = get�Ǹ���(pair.value);
					java.util.Collections.addAll(res, find);
				}
			}
		}

		return res;
	}

	public String getRightPrice() {
		Set<Entry<String, String>> entries = rightMap.entrySet();
		for (Entry<String, String> entry : entries) {
			if (entry.getValue().endsWith("��") || entry.getValue().endsWith("����")) {
				return entry.getValue();
			}
		}

		List<KeyValue> list = collectRight();
		for (KeyValue entry : list) {
			if (entry.getValue().endsWith("��") || entry.getValue().endsWith("����")) {
				return entry.getValue();
			}
		}
		return "NONE";
	}

	public int getStage() {
		return text.getStage();
	}

	public String getText() {
		if (!text.getText().contains("(")) {
			return text.getText();
		} else {
			return text.getText().substring(0, text.getText().indexOf("(")).trim();
		}
	}

	public String get���������������ȣ() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern4.matcher(src);
		if (!m.find()) {
			return null;
		}
		return m.group(1);
	}

	public String get���ҹ�ȣ() {
		Matcher m = removePattern.matcher(getFlatPurpose());
		if (m.find()) {
			return m.group(1);
		}
		m = removePattern2.matcher(getFlatPurpose());
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}

	public String get�����ȣ() {
		Matcher m = modifyPattern1.matcher(flat(getPurpose()));
		if (m.find()) {
			return m.group(1);
		}
		m = modifyPattern2.matcher(flat(getPurpose()));
		m.find();
		return m.group(1);
	}

	public String get��ǹ�ȣIn����() {
		String text = getBecause();
		text = text.replaceAll(" ", "");
		text = text.replaceAll("\n", "");
		Matcher m = eventNoPattern.matcher(text);
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}

	public String get�������������ȣ() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern3.matcher(src);
		if (!m.find()) {
			return null;
		}
		return m.group(1);
	}

	public String get��������������ȣ() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern6.matcher(src);
		if (!m.find()) {
			return null;
		}
		return m.group(1);
	}

	public String get�������������ȣ() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern5.matcher(src);
		if (!m.find()) {
			return null;
		}
		return m.group(1);
	}

	public boolean is��������������() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern4.matcher(src);
		return m.find();
	}

	public boolean is���ҵ��() {
		return is���ҵ��(getFlatPurpose());
	}

	public boolean is������() {
		Matcher m = modifyPattern1.matcher(flat(getPurpose()));
		if (m.find()) {
			return true;
		}
		m = modifyPattern2.matcher(flat(getPurpose()));
		return m.find();
	}

	public boolean is������������() {
		String src = getFlatPurpose();
		Matcher m = modifyPattern3.matcher(src);
		return m.find();
	}

	public boolean is�������������() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern6.matcher(src);
		return m.find();
	}

	public boolean is������������() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern5.matcher(src);
		return m.find();
	}

	public ���ε_���� merge(���ε_���� s) {
		text.addVertical(s.text);
		purposes.addAll(s.purposes);
		���ε_���� newS = new ���ε_����(text);
		newS.purposes = this.purposes;
		return newS;
	}

	public void processRight() {
		rightValues = collectRight();
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public void setPurpose(String newPurpose) {
		this.newPurpose = newPurpose;
	}

	public void setRight(String newRight) {
		this.newRight = newRight;
	}

	@Override
	public String toString() {
		return text + "[" + willDelete + "] = " + purposes;
	}

	public void updateRight(Map<String, String> rightItems) {
		Iterator<String> keys = rightItems.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			String value = rightItems.get(key);
			if ("����".equals(key)) {
				this.newPurpose = value;
			} else {
				rightMap.put(key, value);
			}

		}
	}

	/**
	 * ���������, ������� �϶��� ���ȴ�.
	 * 
	 * @param key
	 * @param rightPeoples
	 */
	public void updateRightPeople(String key, Set<String> rightPeoples) {
		peoples.put(key, rightPeoples);
	}

	public boolean willDelete() {
		return willDelete;
	}

	public void willDelete(boolean willDelete) {
		this.willDelete = willDelete;
	}

	public boolean ����is���() {
		String text = getBecause();
		return text.contains("�������") || text.contains("���ǰ��");
	}

	/**
	 * @return
	 */
	private String getFlatPurpose() {
		return flat(getPurposeOriginal());
	}

	private void initPurpose() {
		if (purpose == null) {
			purpose = new ��������(new TextPosition("", 130, text.getY(), 130, text.getY2(), text
					.getPage(), text.getStage()));
			purposes.add(purpose);
		}
	}

	private String nextWord(String[] tokens) {
		for (int i = 1; i < tokens.length; i++) {
			String token = tokens[i];
			if (token == null || "".equals(token) || token.contains(" ")) {
				continue;
			}
			return token;
		}
		return tokens[1];
	}

	private int toInt(String value) {
		return Integer.parseInt(value);
	}

	public static String flat(String text2) {
		return text2.replaceAll("\n", "");
	}

	public static String getRight(String header, String text) {
		// System.out.println("###########");
		// System.out.println(header);
		// System.out.println("------------");
		// System.out.println(text);
		String[] lines = text.split("\n");

		for (int i = 0; i < lines.length;) {
			String res = "";

			String[] firstLineWords = lines[i].split(" ");
			String key = firstLineWords[0];
			for (int k = 1; k < firstLineWords.length; k++) {
				res += firstLineWords[k].trim() + " ";
			}
			res = res.trim();
			i++;
			for (; i < lines.length; i++) {
				if (lines[i].startsWith(" ")) {
					if ("".equals(res)) {
						res = lines[i];
					} else {
						res += "\n" + lines[i];
					}
				} else {
					break;
				}
			}
			if (key.equals(header)) {
				return res;
			}
		}

		return "";
	}

	public static String[] get�Ǹ���(String text) {
		String[] temp = parse�Ǹ���(text);
		String[] res = new String[temp.length];
		for (int i = 0; i < temp.length; i++) {
			res[i] = temp[i].replaceAll("�ֽ�ȸ��", "(��)");
		}

		return res;
	}

	public static boolean is���ҵ��(String src) {
		Matcher m = removePattern.matcher(src);
		if (m.find()) {
			return true;
		}
		m = removePattern2.matcher(src);
		return m.find();
	}

	public static String parsePurpose(String text) {

		for (int i = 0; i < replace.size(); i++) {
			PatternValue pv = replace.get(i);
			if (pv.isMatch(text)) {
				return pv.value;
			}
		}

		return text;
	}

	public static String parsePurposeBigo(String text) {
		for (int i = 0; i < replace.size(); i++) {
			PatternValue pv = replace.get(i);
			if (pv.isMatch(text)) {
				return pv.m.group(1);
			}
		}
		return text;
	}

	private static int countSpace(String line) {
		int count = 0;
		for (char ch : line.toCharArray()) {
			if (ch == ' ') {
				count++;
			} else {
				return count;
			}
		}
		return count;
	}

	/**
	 * @param text
	 * @return
	 */
	private static String[] parse�Ǹ���(String text) {
		// System.out.println("###"+text);
		String[] lines = text.split("\n");
		// System.out.println("#" + lines[0]);
		if (lines[0].startsWith(" ")) {
			int depth = countSpace(lines[0]);
			List<String> temp = new LinkedList<String>();
			String buf = lines[0];
			for (int i = 1; i < lines.length; i++) {
				String src = lines[i];
				if (countSpace(src) > depth) {
					buf += "\n" + src;
				} else {
					temp.add(buf);
					buf = src;
				}
			}
			temp.add(buf);
			List<String> res = new LinkedList<String>();

			for (int i = 0; i < temp.size(); i++) {
				String line = temp.get(i);
				String[] lines2 = line.split("\n");
				if (lines2[0].trim().startsWith("����")) {
					// System.out.println("## "+lines2[0]);
					i++;
					String[] lines3 = temp.get(i).split("\n");
					res.add(lines3[0].trim().split(" ")[0].trim());
				} else {
					res.add(lines2[0].trim().split(" ")[0].trim());
				}
			}
			return res.toArray(new String[] {});
		} else {
			if (lines[0].startsWith("����")) {
				return new String[] { lines[1].trim().split(" ")[0] };
			}
			String src = lines[0].trim().split(" ")[0];
			if ("��".equals(src)) {
				String[] lines4 = lines[1].trim().split(" ");
				if ("ó��û".equals(lines4[0])) {
					return new String[] { lines4[1] };
				}
				return new String[] { lines4[0] };
			}
			return new String[] { lines[0].trim().split(" ")[0] };

		}
	}
}
