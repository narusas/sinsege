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
 * The Class 등기부등본_사항.
 */
public class 등기부등본_사항 {
	
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

	/** The Constant 소멸. */
	public static final int 소멸 = 2;

	/** The 소멸기준. */
	public static int 소멸기준 = 1;

	/** The Constant 인수. */
	public static final int 인수 = 3;

	/** The because pattern. */
	static Pattern becausePattern = Pattern.compile("(\\d+년\\s*\\d+월\\s*\\d+일\n)");

	/** The date no pattern. */
	static Pattern dateNoPattern = Pattern.compile("^(제\\d+호)");

	/** The date pattern. */
	static Pattern datePattern = Pattern.compile("(\\d+년\\s*\\d+월\\s*\\d+일)");

	/** The date pattern2. */
	static Pattern datePattern2 = Pattern.compile("(\\d+)년(\\d+)월(\\d+)일");

	/** The event no pattern. */
	static Pattern eventNoPattern = Pattern.compile("(\\d+타경\\d+)");

	/** The modify pattern1. */
	static Pattern modifyPattern1 = Pattern.compile("(\\d+[-]*[\\d]*)번.*변경$");

	/** The modify pattern2. */
	static Pattern modifyPattern2 = Pattern.compile("(\\d+[-]*[\\d]*)번.*경정$");

	/** The modify pattern3. */
	static Pattern modifyPattern3 = Pattern.compile("(\\d+[-]*[\\d]*)번.*저당권(일부|전부)*이전$");

	/** The modify pattern4. */
	static Pattern modifyPattern4 = Pattern.compile("(\\d+[-]*[\\d]*)번.*근저당권(일부|전부)*이전$");

	/** The modify pattern5. */
	static Pattern modifyPattern5 = Pattern.compile("(\\d+[-]*[\\d]*)번.*지상권(일부|전부)*이전$");

	/** The modify pattern6. */
	static Pattern modifyPattern6 = Pattern.compile("(\\d+[-]*[\\d]*)번.*전세권(일부|전부)*이전$");

	/** The people keys. */
	static String[] peopleKeys = { "근저당권자", "저당권자", "소유자", "공유자", "합유자", "수탁자", "권리자", "지상권자",
			"임차권자", "전세권자", "채권자", "가등기권자", };

	/** The remove pattern. */
	static Pattern removePattern = Pattern.compile("(\\d+[-]*[\\d]*)번.*말소$");

	/** The remove pattern2. */
	static Pattern removePattern2 = Pattern.compile("(\\d+[-]*[\\d]*)번.*말소통지$");

	/** The replace. */
	static List<PatternValue> replace = new LinkedList<PatternValue>();

	/** The right keys. */
	static String[] rightKeys = { "채권최고액", "근저당권자", "저당권자", "청구금액", "청구액", "소유자", "공유자", "합유자",
			"수탁자", "권리자", "지상권자", "임차권자", "처분청", "면적", "목적", "범위", "전세권자", "전세금", "존속기간", "채권자",
			"채권액", "가등기권자" };

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
	private 하위사항 purpose;

	/** The right values. */
	private List<KeyValue> rightValues = new LinkedList<KeyValue>();

	/** The will delete. */
	private boolean willDelete;
	
	/** The purposes. */
	LinkedList<하위사항> purposes = new LinkedList<하위사항>();

	/** The right map. */
	Map<String, String> rightMap = new HashMap<String, String>();

	/** The text. */
	final TextPosition text;

	static {
		replace.add(new PatternValue(Pattern.compile("(.*)가등기$"), "가등기"));
		replace.add(new PatternValue(Pattern.compile("(.*청구권)$"), "가등기"));
		replace.add(new PatternValue(Pattern.compile("(.*)가압류$"), "가압류"));
		replace.add(new PatternValue(Pattern.compile("(.*)가처분$"), "가처분"));

		replace.add(new PatternValue(Pattern
				.compile("([\\d\\.]+분의[\\d\\.]+.*[\\d\\.]+분의[\\d\\.]+.*전부이전)"), "소유권이전"));
		replace.add(new PatternValue(Pattern.compile("([\\d\\.]+지분전부및[\\d\\.]+지분일부이전)"), "소유권이전"));

		replace.add(new PatternValue(Pattern.compile("([\\d\\.]+지분전부및[\\d\\.]+지분일부이전)"), "소유권이전"));
		replace.add(new PatternValue(Pattern.compile("([\\d\\.]+지분전부및[\\d\\.]+지분일부이전)"), "소유권이전"));
		replace.add(new PatternValue(Pattern.compile("([\\d\\.]+지분전부및[\\d\\.]+지분일부이전)"), "소유권이전"));
		replace.add(new PatternValue(Pattern.compile("([\\d\\.]+지분전부및[\\d\\.]+지분일부이전)"), "소유권이전"));

		replace.add(new PatternValue(Pattern.compile("(공유자전원의지분전부이전)"), "소유권이전"));
		replace.add(new PatternValue(Pattern.compile("(공유자전원지분전부이전)"), "소유권이전"));
		replace.add(new PatternValue(Pattern.compile("(공유자전원지분전부이전청구권)"), "소유권이전"));
		replace.add(new PatternValue(Pattern.compile("(공유지분이전)"), "소유권이전"));

		replace.add(new PatternValue(Pattern
				.compile("(.+\\s*지분[\\d\\.]+분의[\\d\\.]+중\\s+일부\\([\\d\\.]+분의[\\d\\.]+\\)이전)"),
				"소유권이전"));
		replace.add(new PatternValue(Pattern.compile("(.+\\s*지분전부이전)"), "소유권이전"));
		replace.add(new PatternValue(Pattern
				.compile("(.+\\s*지분[\\d\\.]+분지중[\\d\\.]+분지[\\d\\.]+이전)"), "소유권이전"));

		replace.add(new PatternValue(Pattern
				.compile("(.+\\s*지분[\\d\\.\\s]+분의[\\d\\.\\s]+중[\\d\\.\\s]+이전)"), "소유권이전"));

		replace.add(new PatternValue(Pattern.compile("(.*일부이전)$"), "소유권이전"));
		replace.add(new PatternValue(Pattern.compile("(.*지분이전)$"), "소유권이전"));
		replace.add(new PatternValue(Pattern.compile("(.*지분전부소유권이전)$"), "소유권이전"));
		replace.add(new PatternValue(Pattern.compile("(.*지분전부이전)$"), "소유권이전"));
		replace.add(new PatternValue(Pattern.compile("(.*지분전부이전청구권)$"), "소유권이전"));

		replace.add(new PatternValue(Pattern.compile("(.*)이전$"), "소유권이전"));

		replace.add(new PatternValue(Pattern.compile("(.*)압류$"), "압류"));

		replace.add(new PatternValue(Pattern.compile("(.*)예고등기$"), "예고등기"));

		replace.add(new PatternValue(Pattern.compile("(.*)임의경매개시결정$"), "임의경매"));
		replace.add(new PatternValue(Pattern.compile("(.*)강제경매개시결정$"), "강제경매"));

		replace.add(new PatternValue(Pattern.compile("(.*지분전부)임의경매신청$"), "임의경매"));

		replace.add(new PatternValue(Pattern.compile("(.*)임차권"), "임차권"));
		replace.add(new PatternValue(Pattern.compile("(.*)근저당권설정"), "근저당권"));
		replace.add(new PatternValue(Pattern.compile("(.*)저당권설정"), "저당권"));
		replace.add(new PatternValue(Pattern.compile("(.*)전세권설정"), "전세권"));
		replace.add(new PatternValue(Pattern.compile("(.*)지상권설정"), "지상권"));
		replace.add(new PatternValue(Pattern.compile("(.*)환매특약"), "환매특약"));

	}

	/**
	 * Instantiates a new 등기부등본_사항.
	 * 
	 * @param text the text
	 */
	public 등기부등본_사항(TextPosition text) {
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
		purpose = new 하위사항(entity);
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
		if (flag == 소멸기준) {
			return "소멸기준";
		}
		if (flag == 소멸) {
			return "소멸";
		}
		if (flag == 인수) {
			return "인수";
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
			if (key.endsWith("자")) {

				if (list.size() > i + 1) {
					KeyValue temp = list.get(i + 1);
					if (temp.getKey().equals("처분청")) {
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
				// if ("주식회사" .equals(tokens[0])){
				//					
				// return nextWord(tokens);
				// }
				// else if ("(주)" .equals(tokens[0])){
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
					String[] find = get권리자(value);
					java.util.Collections.addAll(res, find);

				} else if (peoples.containsKey(peopleKey)) {
					res.addAll(peoples.get(peopleKey));
				} else if (peopleKey.equals(pair.getKey())) {
					String[] find = get권리자(pair.value);
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
			if (entry.getValue().endsWith("원") || entry.getValue().endsWith("원정")) {
				return entry.getValue();
			}
		}

		List<KeyValue> list = collectRight();
		for (KeyValue entry : list) {
			if (entry.getValue().endsWith("원") || entry.getValue().endsWith("원정")) {
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
	 * Gets the 근저당권이전등기번호.
	 * 
	 * @return the 근저당권이전등기번호
	 */
	public String get근저당권이전등기번호() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern4.matcher(src);
		if (!m.find()) {
			return null;
		}
		return m.group(1);
	}

	/**
	 * Gets the 말소번호.
	 * 
	 * @return the 말소번호
	 */
	public String get말소번호() {
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
	 * Gets the 변경번호.
	 * 
	 * @return the 변경번호
	 */
	public String get변경번호() {
		Matcher m = modifyPattern1.matcher(flat(getPurpose()));
		if (m.find()) {
			return m.group(1);
		}
		m = modifyPattern2.matcher(flat(getPurpose()));
		m.find();
		return m.group(1);
	}

	/**
	 * Gets the 사건번호 in원인.
	 * 
	 * @return the 사건번호 in원인
	 */
	public String get사건번호In원인() {
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
	 * Gets the 저당권이전등기번호.
	 * 
	 * @return the 저당권이전등기번호
	 */
	public String get저당권이전등기번호() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern3.matcher(src);
		if (!m.find()) {
			return null;
		}
		return m.group(1);
	}

	/**
	 * Gets the 전세권이전등기번호.
	 * 
	 * @return the 전세권이전등기번호
	 */
	public String get전세권이전등기번호() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern6.matcher(src);
		if (!m.find()) {
			return null;
		}
		return m.group(1);
	}

	/**
	 * Gets the 지상권이전등기번호.
	 * 
	 * @return the 지상권이전등기번호
	 */
	public String get지상권이전등기번호() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern5.matcher(src);
		if (!m.find()) {
			return null;
		}
		return m.group(1);
	}

	/**
	 * Checks if is 근저당권이전등기.
	 * 
	 * @return true, if is 근저당권이전등기
	 */
	public boolean is근저당권이전등기() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern4.matcher(src);
		return m.find();
	}

	/**
	 * Checks if is 말소등기.
	 * 
	 * @return true, if is 말소등기
	 */
	public boolean is말소등기() {
		return is말소등기(getFlatPurpose());
	}

	/**
	 * Checks if is 변경등기.
	 * 
	 * @return true, if is 변경등기
	 */
	public boolean is변경등기() {
		Matcher m = modifyPattern1.matcher(flat(getPurpose()));
		if (m.find()) {
			return true;
		}
		m = modifyPattern2.matcher(flat(getPurpose()));
		return m.find();
	}

	/**
	 * Checks if is 저당권이전등기.
	 * 
	 * @return true, if is 저당권이전등기
	 */
	public boolean is저당권이전등기() {
		String src = getFlatPurpose();
		Matcher m = modifyPattern3.matcher(src);
		return m.find();
	}

	/**
	 * Checks if is 전세권이전등기.
	 * 
	 * @return true, if is 전세권이전등기
	 */
	public boolean is전세권이전등기() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern6.matcher(src);
		return m.find();
	}

	/**
	 * Checks if is 지상권이전등기.
	 * 
	 * @return true, if is 지상권이전등기
	 */
	public boolean is지상권이전등기() {
		String src = getFlatPurpose();

		Matcher m = modifyPattern5.matcher(src);
		return m.find();
	}

	/**
	 * Merge.
	 * 
	 * @param s the s
	 * 
	 * @return the 등기부등본_사항
	 */
	public 등기부등본_사항 merge(등기부등본_사항 s) {
		text.addVertical(s.text);
		purposes.addAll(s.purposes);
		등기부등본_사항 newS = new 등기부등본_사항(text);
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
			if ("목적".equals(key)) {
				this.newPurpose = value;
			} else {
				rightMap.put(key, value);
			}

		}
	}

	/**
	 * 근저당권자, 저당권자 일때만 사용된다.
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
	 * 원인is경매.
	 * 
	 * @return true, if successful
	 */
	public boolean 원인is경매() {
		String text = getBecause();
		return text.contains("강제경매") || text.contains("임의경매");
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
			purpose = new 하위사항(new TextPosition("", 130, text.getY(), 130, text.getY2(), text
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
	 * Gets the 권리자.
	 * 
	 * @param text the text
	 * 
	 * @return the 권리자
	 */
	public static String[] get권리자(String text) {
		String[] temp = parse권리자(text);
		String[] res = new String[temp.length];
		for (int i = 0; i < temp.length; i++) {
			res[i] = temp[i].replaceAll("주식회사", "(주)");
		}

		return res;
	}

	/**
	 * Checks if is 말소등기.
	 * 
	 * @param src the src
	 * 
	 * @return true, if is 말소등기
	 */
	public static boolean is말소등기(String src) {
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
	 * Parse권리자.
	 * 
	 * @param text the text
	 * 
	 * @return the string[]
	 */
	private static String[] parse권리자(String text) {
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
				if (lines2[0].trim().startsWith("지분")) {
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
			if (lines[0].startsWith("지분")) {
				return new String[] { lines[1].trim().split(" ")[0] };
			}
			String src = lines[0].trim().split(" ")[0];
			if ("국".equals(src)) {
				String[] lines4 = lines[1].trim().split(" ");
				if ("처분청".equals(lines4[0])) {
					return new String[] { lines4[1] };
				}
				return new String[] { lines4[0] };
			}
			return new String[] { lines[0].trim().split(" ")[0] };

		}
	}
}
