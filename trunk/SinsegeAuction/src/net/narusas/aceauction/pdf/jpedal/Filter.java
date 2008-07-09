/*
 * 
 */
package net.narusas.aceauction.pdf.jpedal;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class Filter.
 */
public class Filter {

	/** The DELET e_ filter. */
	static String[] DELETE_FILTER = { "대지권", "공담경정", "공담변경", "사항등기", "공동담보경정", "공동담보변경", "소멸",
			"추가", "질권", "부채권가압류", "담보", "부채권압류", "근저당권일부이전", "종결결정", "신탁등기경정", "신탁등기변경",
			"소유권\\d+분의\\d+등기", "근저당권지분일부이전", "등기명의인표시경정", "등기명의인표시변경", "일부변경", "신탁", "신탁등기",
			"신탁등기변경", "전세권공동목적변경", "근저당권\\d+지분일부이전", "사항$", "특약", "파산", "화의", "인가", "분할개시", "절차개시",
			"회사정리절차개시결정", "담보회복", "전사", "폐지", "지역권", "전세권저당권설정", "보전처리" };

	/** The DELET e_ filte r_ pattern. */
	static List<Pattern> DELETE_FILTER_PATTERN = new LinkedList<Pattern>();

	/** The remove pattern. */
	static Pattern removePattern = Pattern.compile("(\\d+[-]*[\\d]*)번");

	/** The WOR k_ filter. */
	static String[] WORK_FILTER = { "말소$", "회복$", "변경$", "경정$", "말소통지$", };

	/** The WOR k_ filte r_ pattern. */
	static List<Pattern> WORK_FILTER_PATTERN = new LinkedList<Pattern>();

	static {
		for (String pattern : DELETE_FILTER) {
			DELETE_FILTER_PATTERN.add(Pattern.compile(pattern));
		}

		for (String pattern : WORK_FILTER) {
			WORK_FILTER_PATTERN.add(Pattern.compile(pattern));
		}
	}

	/**
	 * Check flag.
	 * 
	 * @param items the items
	 */
	public static void checkFlag(List<등기부등본_사항> items) {
		boolean already소멸기준사항 = false;
		// System.out.println("### START");
		for (등기부등본_사항 s : items) {
			if (s.willDelete()) {
				continue;
			}
			String text = s.getPurpose();
			// System.out.println("#" + s.getText() + " " + text);
			if ((text.contains("저당권") || text.contains("근저당권") || text.contains("압류") || text
					.contains("강제경매"))
					&& already소멸기준사항 == false) {
				// System.out.println("#~~~~~~~~1");
				already소멸기준사항 = true;
				s.setFlag(등기부등본_사항.소멸기준);
			}

			else if ((text.contains("저당권") || text.contains("압류") || text.contains("임의경매")
					|| text.contains("소유권이전") || text.contains("소유권보존"))
					&& already소멸기준사항 == false) {
				// System.out.println("#~~~~~~~~3");
				s.setFlag(등기부등본_사항.소멸);
			}

			else if (text.contains("예고등기")) {
				// System.out.println("#~~~~~~~~4");
				s.setFlag(등기부등본_사항.인수);
			} else if (text.contains("지상권") || text.contains("전세권") || text.contains("가등기")
					|| text.contains("가처분") || text.contains("환매특약") || text.contains("임차권")) {
				if (already소멸기준사항 == false) {
					// System.out.println("#~~~~~~~~5");
					s.setFlag(등기부등본_사항.인수);
				} else {
					// System.out.println("#~~~~~~~~6");
					s.setFlag(등기부등본_사항.소멸);
				}
			} else {
				// System.out.println("#~~~~~~~~7");
				s.setFlag(등기부등본_사항.소멸);
			}
		}
		// System.out.println("### END");
	}

	/**
	 * Check peoples.
	 * 
	 * @param items the items
	 * @param name the name
	 */
	public static void checkPeoples(List<등기부등본_사항> items, String[] name) {
		if (name == null || name.length == 0) {
			return;
		}
		// if(true) return ;
		for (int i = 0; i < items.size(); i++) {
			등기부등본_사항 s = items.get(i);
			if (s.willDelete()) {
				continue;
			}

			int stage = s.getStage();
			String no = s.getText();
			String purpose = s.getPurpose();
			String purposeOri = s.getPurposeOriginal();

			boolean b1 = purpose.endsWith("이전");
			boolean b2 = 등기부등본_사항.flat(purposeOri).contains("지분");

			if (s.getStage() == 1 && b1) {
				s.processRight();
				checkNames(name, s, s.getRight());
			}

			else if (s.getStage() == 1 && b2 && b1 == false) {
				s.processRight();
				checkNames(name, s, purposeOri);
			}

			else if (s.getStage() == 2 && b2) {
				s.processRight();
				checkNames(name, s, purposeOri);
			}

			// if (b1) {
			// s.processRight();
			// checkNames(name, s, s.getRight());
			// }
			// if (b2 && b3 && ) {
			// s.processRight();
			// checkNames(name, s, purposeOri);
			// }
			// if ( b1 || b2) {
			// String no = s.getText();
			// s.processRight();
			// checkNames(name, s, purposeOri);
			// checkNames(name, s, s.getRight());
			//				
			// }
		}
	}

	/**
	 * Check work item.
	 * 
	 * @param items the items
	 */
	public static void checkWorkItem(List<등기부등본_사항> items) {
		for (등기부등본_사항 s : items) {
			for (하위사항 c : s.purposes) {
				if (isDeleteItem(등기부등본_사항.flat(c.getPurpose()), WORK_FILTER_PATTERN)) {
					s.willDelete(true);
				}
			}
		}
	}

	/**
	 * Checks if is delete item.
	 * 
	 * @param purpose the purpose
	 * @param patterns the patterns
	 * 
	 * @return true, if is delete item
	 */
	public static boolean isDeleteItem(String purpose, List<Pattern> patterns) {
		// System.out.println(purpose);
		for (Pattern p : patterns) {
			Matcher m = p.matcher(등기부등본_사항.flat(purpose));
			if (m.find()) {
				return true;
			}
		}
		if ("".equals(purpose) || purpose == null) {
			return true;
		}
		return false;
	}

	/**
	 * Sort by date.
	 * 
	 * @param items the items
	 */
	public static void sortByDate(List<등기부등본_사항> items) {
		java.util.Collections.sort(items, new Comparator<등기부등본_사항>() {
			public int compare(등기부등본_사항 s1, 등기부등본_사항 s2) {
				return s1.getAcceptDateLong() == s2.getAcceptDateLong() ? 0 : s1
						.getAcceptDateLong() > s2.getAcceptDateLong() ? 1 : -1;
			}
		});
	}
	
	/**
	 * 말소처리.
	 * 
	 * @param items the items
	 * @param origin the origin
	 * @param i the i
	 */
	public static void 말소처리(List<등기부등본_사항> items, 등기부등본_사항 origin, int i) {

		String purpose = 등기부등본_사항.flat(origin.getPurposeOriginal());
		Matcher m = removePattern.matcher(purpose);
		while (m.find()) {
			String no = m.group(1);
			// System.out.println("#" + no);
			for (int k = i; k >= 0; k--) {
				등기부등본_사항 s = items.get(k);
				if (no.equals(s.getText()) && origin.getStage() == s.getStage()) {
					s.willDelete(true);
				}
			}
		}

		origin.willDelete(true);
	}

	/**
	 * Check names.
	 * 
	 * @param name the name
	 * @param s the s
	 * @param text the text
	 */
	private static void checkNames(String[] name, 등기부등본_사항 s, String text) {
		for (String n : name) {
			if (text.contains(n)) {
				return;
			}
		}
		s.willDelete(true);
	}

	/**
	 * Modify.
	 * 
	 * @param items the items
	 * @param modifier the modifier
	 * @param i the i
	 */
	private static void modify(List<등기부등본_사항> items, 등기부등본_사항 modifier, int i) {
		String no = modifier.get변경번호();
		for (int k = i; k >= 0; k--) {
			등기부등본_사항 s = items.get(k);
			if (s.getStage() != modifier.getStage()) {
				continue;
			}
			if (no.equals(s.getText())) {
				s.processRight();
				modifier.processRight();
				s.updateRight(modifier.getRightItems());
			}
		}
	}

	/**
	 * Modify권리자.
	 * 
	 * @param items the items
	 * @param 등기번호 the 등기번호
	 * @param 등기사항 the 등기사항
	 * @param i the i
	 * @param target the target
	 */
	private static void modify권리자(List<등기부등본_사항> items, String 등기번호, 등기부등본_사항 등기사항, int i, String target) {
		for (int k = i; k >= 0; k--) {
			등기부등본_사항 s = items.get(k);
			if (등기번호 == null && s.willDelete() == false && s.getRight(target) != null) {
				s.processRight();
				등기사항.processRight();
				s.updateRightPeople(target, 등기사항.getRightPeoples());
			}
			if (등기번호.equals(s.getText())) {
				s.processRight();
				등기사항.processRight();
				s.updateRightPeople(target, 등기사항.getRightPeoples());
			}
		}
		등기사항.willDelete(true);
	}

	/**
	 * Modify근저당권자.
	 * 
	 * @param items the items
	 * @param no the no
	 * @param 등기사항 the 등기사항
	 * @param i the i
	 */
	private static void modify근저당권자(List<등기부등본_사항> items, String no, 등기부등본_사항 등기사항, int i) {
		modify권리자(items, no, 등기사항, i, "근저당권자");
	}

	/**
	 * Modify저당권자.
	 * 
	 * @param items the items
	 * @param no the no
	 * @param 등기사항 the 등기사항
	 * @param i the i
	 */
	private static void modify저당권자(List<등기부등본_사항> items, String no, 등기부등본_사항 등기사항, int i) {
		modify권리자(items, no, 등기사항, i, "저당권자");
	}

	/**
	 * Modify전세권자.
	 * 
	 * @param items the items
	 * @param no the no
	 * @param 등기사항 the 등기사항
	 * @param i the i
	 */
	private static void modify전세권자(List<등기부등본_사항> items, String no, 등기부등본_사항 등기사항, int i) {
		modify권리자(items, no, 등기사항, i, "전세권자");
	}

	/**
	 * Modify지상권자.
	 * 
	 * @param items the items
	 * @param no the no
	 * @param 등기사항 the 등기사항
	 * @param i the i
	 */
	private static void modify지상권자(List<등기부등본_사항> items, String no, 등기부등본_사항 등기사항, int i) {
		modify권리자(items, no, 등기사항, i, "지상권자");
	}

	/**
	 * Filter.
	 * 
	 * @param items the items
	 * @param 소유자 the 소유자
	 */
	static void filter(List<등기부등본_사항> items, String[] 소유자) {

		for (int i = 0; i < items.size(); i++) {
			등기부등본_사항 s = items.get(i);
			String purpose = 등기부등본_사항.flat(s.getPurposeOriginal());
			if (isDeleteItem(purpose, DELETE_FILTER_PATTERN)) {
				s.willDelete(true);
				continue;
			}

			// System.out.println("##"+purpose);
			if (s.is말소등기()) {
				말소처리(items, s, i);
				continue;
			}

			if (s.is변경등기()) {
				modify(items, s, i);
			}

			if (s.is근저당권이전등기()) {
				modify근저당권자(items, s.get근저당권이전등기번호(), s, i);
			}
			if (s.is저당권이전등기()) {
				modify저당권자(items, s.get저당권이전등기번호(), s, i);
			}

			if (s.is지상권이전등기()) {
				modify지상권자(items, s.get지상권이전등기번호(), s, i);
			}

			if (s.is전세권이전등기()) {
				modify전세권자(items, s.get전세권이전등기번호(), s, i);
			}
		}
		checkWorkItem(items);

		Filter.sortByDate(items);

		Filter.checkFlag(items);

		Filter.checkPeoples(items, 소유자);
	}
}
