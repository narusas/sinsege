/*
 * 
 */
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

// TODO: Auto-generated Javadoc
/**
 * The Class ���ε_����.
 */
public class ���ε_���� {
	
	/**
	 * The Class KeyValue.
	 */
	class KeyValue {
		
		/** The key. */
		String key;

		/** The value. */
		String value;

		/**
		 * Instantiates a new key value.
		 * 
		 * @param key the key
		 * @param value the value
		 */
		public KeyValue(String key, String value) {
			this.key = key;
			this.value = value;
		}

		/**
		 * Gets the key.
		 * 
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * Gets the value.
		 * 
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Sets the key.
		 * 
		 * @param key the new key
		 */
		public void setKey(String key) {
			this.key = key;
		}

		/**
		 * Sets the value.
		 * 
		 * @param value the new value
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return key + "=" + value;
		}
	}

	/**
	 * The Class PatternValue.
	 */
	static class PatternValue {
		
		/** The m. */
		Matcher m;

		/** The pattern. */
		Pattern pattern;

		/** The value. */
		String value;

		/**
		 * Instantiates a new pattern value.
		 * 
		 * @param p the p
		 * @param v the v
		 */
		public PatternValue(Pattern p, String v) {
			pattern = p;
			value = v;
		}

		/**
		 * Checks if is match.
		 * 
		 * @param text the text
		 * 
		 * @return true, if is match
		 */
		boolean isMatch(String text) {
			m = pattern.matcher(flat(text));
			return m.find();
		}
	}

	/** The Constant �Ҹ�. */
	public static final int �Ҹ� = 2;

	/** The �Ҹ����. */
	public static int �Ҹ���� = 1;

	/** The Constant �μ�. */
	public static final int �μ� = 3;

	/** The because pattern. */
	static Pattern becausePattern = Pattern.compile("(\\d+��\\s*\\d+��\\s*\\d+��\n)");

	/** The date no pattern. */
	static Pattern dateNoPattern = Pattern.compile("^(��\\d+ȣ)");

	/** The date pattern. */
	static Pattern datePattern = Pattern.compile("(\\d+��\\s*\\d+��\\s*\\d+��)");

	/** The date pattern2. */
	static Pattern datePattern2 = Pattern.compile("(\\d+)��(\\d+)��(\\d+)��");

	/** The event no pattern. */
	static Pattern eventNoPattern = Pattern.compile("(\\d+Ÿ��\\d+)");

	/** The modify pattern1. */
	static Pattern modifyPattern1 = Pattern.compile("(\\d+[-]*[\\d]*)��.*����$");

	/** The modify pattern2. */
	static Pattern modifyPattern2 = Pattern.compile("(\\d+[-]*[\\d]*)��.*����$");

	/** The modify pattern3. */
	static Pattern modifyPattern3 = Pattern.compile("(\\d+[-]*[\\d]*)��.*�����(�Ϻ�|����)*����$");

	/** The modify pattern4. */
	static Pattern modifyPattern4 = Pattern.compile("(\\d+[-]*[\\d]*)��.*�������(�Ϻ�|����)*����$");

	/** The modify pattern5. */
	static Pattern modifyPattern5 = Pattern.compile("(\\d+[-]*[\\d]*)��.*�����(�Ϻ�|����)*����$");

	/** The modify pattern6. */
	static Pattern modifyPattern6 = Pattern.compile("(\\d+[-]*[\\d]*)��.*������(�Ϻ�|����)*����$");

	/** The people keys. */
	static String[] peopleKeys = { "���������", "�������", "������", "������", "������", "��Ź��", "�Ǹ���", "�������",
			"��������", "��������", "ä����", "��������", };

	/** The remove pattern. */
	static Pattern removePattern = Pattern.compile("(\\d+[-]*[\\d]*)��.*����$");

	/** The remove pattern2. */
	static Pattern removePattern2 = Pattern.compile("(\\d+[-]*[\\d]*)��.*��������$");

	/** The replace. */
	static List<PatternValue> replace = new LinkedList<PatternValue>();

	/** The right keys. */
	static String[] rightKeys = { "ä���ְ��", "���������", "�������", "û���ݾ�", "û����", "������", "������", "������",
			"��Ź��", "�Ǹ���", "�������", "��������", "ó��û", "����", "����", "����", "��������", "������", "���ӱⰣ", "ä����",
			"ä�Ǿ�", "��������" };

	/** The do not handle. */
	private boolean doNotHandle = false;

	/** The flag. */
	private int flag;

	/** The new purpose. */
	private String newPurpose;

	/** The new right. */
	private String newRight;

	/** The peoples. */
	private HashMap<String, Set<String>> peoples = new HashMap<String, Set<String>>();

	/** The purpose. */
	private �������� purpose;

	/** The right values. */
	private List<KeyValue> rightValues = new LinkedList<KeyValue>();

	/** The will delete. */
	private boolean willDelete;
	
	/** The purposes. */
	LinkedList<��������> purposes = new LinkedList<��������>();

	/** The right map. */
	Map<String, String> rightMap = new HashMap<String, String>();

	/** The text. */
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

	/**
	 * Instantiates a new ���ε_����.
	 * 
	 * @param text the text
	 */
	public ���ε_����(TextPosition text) {
		this.text = text;
	}

	/**
	 * Adds the accept date.
	 * 
	 * @param entity the entity
	 */
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

	/**
	 * Adds the because.
	 * 
	 * @param entity the entity
	 */
	public void addBecause(TextPosition entity) {
		initPurpose();
		purpose.addBecause(entity);

	}

	/**
	 * Adds the purpose.
	 * 
	 * @param entity the entity
	 */
	public void addPurpose(TextPosition entity) {
		purpose = new ��������(entity);
		purposes.add(purpose);

	}

	/**
	 * Adds the right.
	 * 
	 * @param entity the entity
	 */
	public void addRight(TextPosition entity) {
		initPurpose();
		purpose.addRightAndEtc(entity);
	}

	/**
	 * Collect right.
	 * 
	 * @return the list< key value>
	 */
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

	/**
	 * Do not handle.
	 * 
	 * @param b the b
	 */
	public void doNotHandle(boolean b) {
		doNotHandle = b;
	}

	/**
	 * Gets the accept date.
	 * 
	 * @return the accept date
	 */
	public String getAcceptDate() {
		String text = purposes.get(0).getAcceptDate();
		Matcher m = datePattern.matcher(text);
		if (m.find()) {
			return m.group(1);
		}
		return text;
	}

	/**
	 * Gets the accept date long.
	 * 
	 * @return the accept date long
	 */
	public long getAcceptDateLong() {
		String date = getAcceptDate();
		Matcher m = datePattern2.matcher(date);
		if (m.find()) {
			Date d = new Date(toInt(m.group(1)), toInt(m.group(2)), toInt(m.group(3)));
			return d.getTime();
		}
		return 0;
	}

	/**
	 * Gets the because.
	 * 
	 * @return the because
	 */
	public String getBecause() {
		String text = purposes.get(purposes.size() - 1).getBecause();
		Matcher m = becausePattern.matcher(text);
		if (m.find()) {
			String toRemove = m.group(1);
			return text.substring(toRemove.length());
		}
		return text;
	}

	/**
	 * Gets the bigo.
	 * 
	 * @return the bigo
	 */
	public String getBigo() {
		String text = newPurpose == null ? getPurposeOriginal() : newPurpose;
		return parsePurposeBigo(flat(text));
	}

	/**
	 * Gets the flag string.
	 * 
	 * @return the flag string
	 */
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

	/**
	 * Gets the purpose.
	 * 
	 * @return the purpose
	 */
	public String getPurpose() {
		String text = flat(newPurpose == null ? getPurposeOriginal() : newPurpose);
		return parsePurpose(text);
	}

	/**
	 * Gets the purpose original.
	 * 
	 * @return the purpose original
	 */
	public String getPurposeOriginal() {
		return purposes.get(purposes.size() - 1).getPurpose();
	}

	/**
	 * Gets the right.
	 * 
	 * @return the right
	 */
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

	/**
	 * Gets the right.
	 * 
	 * @param key the key
	 * 
	 * @return the right
	 */
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

	/**
	 * Gets the right items.
	 * 
	 * @return the right items
	 */
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

	/**
	 * Gets the right map.
	 * 
	 * @return the right map
	 */
	public Map<String, String> getRightMap() {
		return rightMap;
	}

	/**
	 * Gets the right people.
	 * 
	 * @return the right people
	 */
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

	/**
	 * Gets the right peoples.
	 * 
	 * @return the right peoples
	 */
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

	/**
	 * Gets the right price.
	 * 
	 * @return the right price
	 */
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

	/**
	 * Gets the stage.
	 * 
	 * @return the stage
	 */
	public int getStage() {
		return text.getStage();
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		if (!text.getText().contains("(")) {
			return text.getText();
		} else {
			return text.getText().substring(0, text.getText().indexOf("(")).trim();
		}
	}

	/**
	 * Gets the ���������������ȣ.
	 * 
	 * @return the ���������������ȣ
	 */
	public String get���������������ȣ() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern4.matcher(src);
		if (!m.find()) {
			return null;
		}
		return m.group(1);
	}

	/**
	 * Gets the ���ҹ�ȣ.
	 * 
	 * @return the ���ҹ�ȣ
	 */
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

	/**
	 * Gets the �����ȣ.
	 * 
	 * @return the �����ȣ
	 */
	public String get�����ȣ() {
		Matcher m = modifyPattern1.matcher(flat(getPurpose()));
		if (m.find()) {
			return m.group(1);
		}
		m = modifyPattern2.matcher(flat(getPurpose()));
		m.find();
		return m.group(1);
	}

	/**
	 * Gets the ��ǹ�ȣ in����.
	 * 
	 * @return the ��ǹ�ȣ in����
	 */
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

	/**
	 * Gets the �������������ȣ.
	 * 
	 * @return the �������������ȣ
	 */
	public String get�������������ȣ() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern3.matcher(src);
		if (!m.find()) {
			return null;
		}
		return m.group(1);
	}

	/**
	 * Gets the ��������������ȣ.
	 * 
	 * @return the ��������������ȣ
	 */
	public String get��������������ȣ() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern6.matcher(src);
		if (!m.find()) {
			return null;
		}
		return m.group(1);
	}

	/**
	 * Gets the �������������ȣ.
	 * 
	 * @return the �������������ȣ
	 */
	public String get�������������ȣ() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern5.matcher(src);
		if (!m.find()) {
			return null;
		}
		return m.group(1);
	}

	/**
	 * Checks if is ��������������.
	 * 
	 * @return true, if is ��������������
	 */
	public boolean is��������������() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern4.matcher(src);
		return m.find();
	}

	/**
	 * Checks if is ���ҵ��.
	 * 
	 * @return true, if is ���ҵ��
	 */
	public boolean is���ҵ��() {
		return is���ҵ��(getFlatPurpose());
	}

	/**
	 * Checks if is ������.
	 * 
	 * @return true, if is ������
	 */
	public boolean is������() {
		Matcher m = modifyPattern1.matcher(flat(getPurpose()));
		if (m.find()) {
			return true;
		}
		m = modifyPattern2.matcher(flat(getPurpose()));
		return m.find();
	}

	/**
	 * Checks if is ������������.
	 * 
	 * @return true, if is ������������
	 */
	public boolean is������������() {
		String src = getFlatPurpose();
		Matcher m = modifyPattern3.matcher(src);
		return m.find();
	}

	/**
	 * Checks if is �������������.
	 * 
	 * @return true, if is �������������
	 */
	public boolean is�������������() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern6.matcher(src);
		return m.find();
	}

	/**
	 * Checks if is ������������.
	 * 
	 * @return true, if is ������������
	 */
	public boolean is������������() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern5.matcher(src);
		return m.find();
	}

	/**
	 * Merge.
	 * 
	 * @param s the s
	 * 
	 * @return the ���ε_����
	 */
	public ���ε_���� merge(���ε_���� s) {
		text.addVertical(s.text);
		purposes.addAll(s.purposes);
		���ε_���� newS = new ���ε_����(text);
		newS.purposes = this.purposes;
		return newS;
	}

	/**
	 * Process right.
	 */
	public void processRight() {
		rightValues = collectRight();
	}

	/**
	 * Sets the flag.
	 * 
	 * @param flag the new flag
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}

	/**
	 * Sets the purpose.
	 * 
	 * @param newPurpose the new purpose
	 */
	public void setPurpose(String newPurpose) {
		this.newPurpose = newPurpose;
	}

	/**
	 * Sets the right.
	 * 
	 * @param newRight the new right
	 */
	public void setRight(String newRight) {
		this.newRight = newRight;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return text + "[" + willDelete + "] = " + purposes;
	}

	/**
	 * Update right.
	 * 
	 * @param rightItems the right items
	 */
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
	 * @param key the key
	 * @param rightPeoples the right peoples
	 */
	public void updateRightPeople(String key, Set<String> rightPeoples) {
		peoples.put(key, rightPeoples);
	}

	/**
	 * Will delete.
	 * 
	 * @return true, if successful
	 */
	public boolean willDelete() {
		return willDelete;
	}

	/**
	 * Will delete.
	 * 
	 * @param willDelete the will delete
	 */
	public void willDelete(boolean willDelete) {
		this.willDelete = willDelete;
	}

	/**
	 * ����is���.
	 * 
	 * @return true, if successful
	 */
	public boolean ����is���() {
		String text = getBecause();
		return text.contains("�������") || text.contains("���ǰ��");
	}

	/**
	 * Gets the flat purpose.
	 * 
	 * @return the flat purpose
	 */
	private String getFlatPurpose() {
		return flat(getPurposeOriginal());
	}

	/**
	 * Inits the purpose.
	 */
	private void initPurpose() {
		if (purpose == null) {
			purpose = new ��������(new TextPosition("", 130, text.getY(), 130, text.getY2(), text
					.getPage(), text.getStage()));
			purposes.add(purpose);
		}
	}

	/**
	 * Next word.
	 * 
	 * @param tokens the tokens
	 * 
	 * @return the string
	 */
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

	/**
	 * To int.
	 * 
	 * @param value the value
	 * 
	 * @return the int
	 */
	private int toInt(String value) {
		return Integer.parseInt(value);
	}

	/**
	 * Flat.
	 * 
	 * @param text2 the text2
	 * 
	 * @return the string
	 */
	public static String flat(String text2) {
		return text2.replaceAll("\n", "");
	}

	/**
	 * Gets the right.
	 * 
	 * @param header the header
	 * @param text the text
	 * 
	 * @return the right
	 */
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

	/**
	 * Gets the �Ǹ���.
	 * 
	 * @param text the text
	 * 
	 * @return the �Ǹ���
	 */
	public static String[] get�Ǹ���(String text) {
		String[] temp = parse�Ǹ���(text);
		String[] res = new String[temp.length];
		for (int i = 0; i < temp.length; i++) {
			res[i] = temp[i].replaceAll("�ֽ�ȸ��", "(��)");
		}

		return res;
	}

	/**
	 * Checks if is ���ҵ��.
	 * 
	 * @param src the src
	 * 
	 * @return true, if is ���ҵ��
	 */
	public static boolean is���ҵ��(String src) {
		Matcher m = removePattern.matcher(src);
		if (m.find()) {
			return true;
		}
		m = removePattern2.matcher(src);
		return m.find();
	}

	/**
	 * Parses the purpose.
	 * 
	 * @param text the text
	 * 
	 * @return the string
	 */
	public static String parsePurpose(String text) {

		for (int i = 0; i < replace.size(); i++) {
			PatternValue pv = replace.get(i);
			if (pv.isMatch(text)) {
				return pv.value;
			}
		}

		return text;
	}

	/**
	 * Parses the purpose bigo.
	 * 
	 * @param text the text
	 * 
	 * @return the string
	 */
	public static String parsePurposeBigo(String text) {
		for (int i = 0; i < replace.size(); i++) {
			PatternValue pv = replace.get(i);
			if (pv.isMatch(text)) {
				return pv.m.group(1);
			}
		}
		return text;
	}

	/**
	 * Count space.
	 * 
	 * @param line the line
	 * 
	 * @return the int
	 */
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
	 * Parse�Ǹ���.
	 * 
	 * @param text the text
	 * 
	 * @return the string[]
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
