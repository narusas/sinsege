package net.narusas.aceauction.pdf.jpedal;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filter {

	static String[] DELETE_FILTER = { "������", "�������", "���㺯��", "���׵��", "�����㺸����", "�����㺸����", "�Ҹ�",
			"�߰�", "����", "��ä�ǰ��з�", "�㺸", "��ä�Ǿз�", "��������Ϻ�����", "�������", "��Ź������", "��Ź��⺯��",
			"������\\d+����\\d+���", "������������Ϻ�����", "��������ǥ�ð���", "��������ǥ�ú���", "�Ϻκ���", "��Ź", "��Ź���",
			"��Ź��⺯��", "�����ǰ�����������", "�������\\d+�����Ϻ�����", "����$", "Ư��", "�Ļ�", "ȭ��", "�ΰ�", "���Ұ���", "��������",
			"ȸ�������������ð���", "�㺸ȸ��", "����", "����", "������", "����������Ǽ���", "����ó��" };

	static List<Pattern> DELETE_FILTER_PATTERN = new LinkedList<Pattern>();

	static Pattern removePattern = Pattern.compile("(\\d+[-]*[\\d]*)��");

	static String[] WORK_FILTER = { "����$", "ȸ��$", "����$", "����$", "��������$", };

	static List<Pattern> WORK_FILTER_PATTERN = new LinkedList<Pattern>();

	static {
		for (String pattern : DELETE_FILTER) {
			DELETE_FILTER_PATTERN.add(Pattern.compile(pattern));
		}

		for (String pattern : WORK_FILTER) {
			WORK_FILTER_PATTERN.add(Pattern.compile(pattern));
		}
	}

	public static void checkFlag(List<���ε_����> items) {
		boolean already�Ҹ���ػ��� = false;
		// System.out.println("### START");
		for (���ε_���� s : items) {
			if (s.willDelete()) {
				continue;
			}
			String text = s.getPurpose();
			// System.out.println("#" + s.getText() + " " + text);
			if ((text.contains("�����") || text.contains("�������") || text.contains("�з�") || text
					.contains("�������"))
					&& already�Ҹ���ػ��� == false) {
				// System.out.println("#~~~~~~~~1");
				already�Ҹ���ػ��� = true;
				s.setFlag(���ε_����.�Ҹ����);
			}

			else if ((text.contains("�����") || text.contains("�з�") || text.contains("���ǰ��")
					|| text.contains("����������") || text.contains("�����Ǻ���"))
					&& already�Ҹ���ػ��� == false) {
				// System.out.println("#~~~~~~~~3");
				s.setFlag(���ε_����.�Ҹ�);
			}

			else if (text.contains("������")) {
				// System.out.println("#~~~~~~~~4");
				s.setFlag(���ε_����.�μ�);
			} else if (text.contains("�����") || text.contains("������") || text.contains("�����")
					|| text.contains("��ó��") || text.contains("ȯ��Ư��") || text.contains("������")) {
				if (already�Ҹ���ػ��� == false) {
					// System.out.println("#~~~~~~~~5");
					s.setFlag(���ε_����.�μ�);
				} else {
					// System.out.println("#~~~~~~~~6");
					s.setFlag(���ε_����.�Ҹ�);
				}
			} else {
				// System.out.println("#~~~~~~~~7");
				s.setFlag(���ε_����.�Ҹ�);
			}
		}
		// System.out.println("### END");
	}

	public static void checkPeoples(List<���ε_����> items, String[] name) {
		if (name == null || name.length == 0) {
			return;
		}
		// if(true) return ;
		for (int i = 0; i < items.size(); i++) {
			���ε_���� s = items.get(i);
			if (s.willDelete()) {
				continue;
			}

			int stage = s.getStage();
			String no = s.getText();
			String purpose = s.getPurpose();
			String purposeOri = s.getPurposeOriginal();

			boolean b1 = purpose.endsWith("����");
			boolean b2 = ���ε_����.flat(purposeOri).contains("����");

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

	public static void checkWorkItem(List<���ε_����> items) {
		for (���ε_���� s : items) {
			for (�������� c : s.purposes) {
				if (isDeleteItem(���ε_����.flat(c.getPurpose()), WORK_FILTER_PATTERN)) {
					s.willDelete(true);
				}
			}
		}
	}

	public static boolean isDeleteItem(String purpose, List<Pattern> patterns) {
		// System.out.println(purpose);
		for (Pattern p : patterns) {
			Matcher m = p.matcher(���ε_����.flat(purpose));
			if (m.find()) {
				return true;
			}
		}
		if ("".equals(purpose) || purpose == null) {
			return true;
		}
		return false;
	}

	public static void sortByDate(List<���ε_����> items) {
		java.util.Collections.sort(items, new Comparator<���ε_����>() {
			public int compare(���ε_���� s1, ���ε_���� s2) {
				return s1.getAcceptDateLong() == s2.getAcceptDateLong() ? 0 : s1
						.getAcceptDateLong() > s2.getAcceptDateLong() ? 1 : -1;
			}
		});
	}
	public static void ����ó��(List<���ε_����> items, ���ε_���� origin, int i) {

		String purpose = ���ε_����.flat(origin.getPurposeOriginal());
		Matcher m = removePattern.matcher(purpose);
		while (m.find()) {
			String no = m.group(1);
			// System.out.println("#" + no);
			for (int k = i; k >= 0; k--) {
				���ε_���� s = items.get(k);
				if (no.equals(s.getText()) && origin.getStage() == s.getStage()) {
					s.willDelete(true);
				}
			}
		}

		origin.willDelete(true);
	}

	/**
	 * @param name
	 * @param s
	 * @param text
	 */
	private static void checkNames(String[] name, ���ε_���� s, String text) {
		for (String n : name) {
			if (text.contains(n)) {
				return;
			}
		}
		s.willDelete(true);
	}

	private static void modify(List<���ε_����> items, ���ε_���� modifier, int i) {
		String no = modifier.get�����ȣ();
		for (int k = i; k >= 0; k--) {
			���ε_���� s = items.get(k);
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

	private static void modify�Ǹ���(List<���ε_����> items, String ����ȣ, ���ε_���� ������, int i, String target) {
		for (int k = i; k >= 0; k--) {
			���ε_���� s = items.get(k);
			if (����ȣ == null && s.willDelete() == false && s.getRight(target) != null) {
				s.processRight();
				������.processRight();
				s.updateRightPeople(target, ������.getRightPeoples());
			}
			if (����ȣ.equals(s.getText())) {
				s.processRight();
				������.processRight();
				s.updateRightPeople(target, ������.getRightPeoples());
			}
		}
		������.willDelete(true);
	}

	private static void modify���������(List<���ε_����> items, String no, ���ε_���� ������, int i) {
		modify�Ǹ���(items, no, ������, i, "���������");
	}

	private static void modify�������(List<���ε_����> items, String no, ���ε_���� ������, int i) {
		modify�Ǹ���(items, no, ������, i, "�������");
	}

	private static void modify��������(List<���ε_����> items, String no, ���ε_���� ������, int i) {
		modify�Ǹ���(items, no, ������, i, "��������");
	}

	private static void modify�������(List<���ε_����> items, String no, ���ε_���� ������, int i) {
		modify�Ǹ���(items, no, ������, i, "�������");
	}

	static void filter(List<���ε_����> items, String[] ������) {

		for (int i = 0; i < items.size(); i++) {
			���ε_���� s = items.get(i);
			String purpose = ���ε_����.flat(s.getPurposeOriginal());
			if (isDeleteItem(purpose, DELETE_FILTER_PATTERN)) {
				s.willDelete(true);
				continue;
			}

			// System.out.println("##"+purpose);
			if (s.is���ҵ��()) {
				����ó��(items, s, i);
				continue;
			}

			if (s.is������()) {
				modify(items, s, i);
			}

			if (s.is��������������()) {
				modify���������(items, s.get���������������ȣ(), s, i);
			}
			if (s.is������������()) {
				modify�������(items, s.get�������������ȣ(), s, i);
			}

			if (s.is������������()) {
				modify�������(items, s.get�������������ȣ(), s, i);
			}

			if (s.is�������������()) {
				modify��������(items, s.get��������������ȣ(), s, i);
			}
		}
		checkWorkItem(items);

		Filter.sortByDate(items);

		Filter.checkFlag(items);

		Filter.checkPeoples(items, ������);
	}
}
