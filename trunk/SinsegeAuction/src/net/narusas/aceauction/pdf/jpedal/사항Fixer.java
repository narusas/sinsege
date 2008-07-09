/*
 * 
 */
package net.narusas.aceauction.pdf.jpedal;

import java.util.LinkedList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class ����Fixer.
 */
public class ����Fixer {

	/**
	 * Fix.
	 * 
	 * @param items the items
	 * 
	 * @return the list<���ε_����>
	 */
	public static List<���ε_����> fix(List<���ε_����> items) {
		LinkedList<���ε_����> res = new LinkedList<���ε_����>();

		for (���ε_���� s : items) {
			String text = s.getText();
			if ("".equals(text) || (text.contains(")") && text.contains("(") == false)
					|| (Character.isDigit(text.toCharArray()[0]) == false && text.contains(")"))) {

//				System.out.println("!!!!!!!!!!!!!");
//				for (���� s2 : items) {
//
//					System.out.println("##" + s2);
//				}

//				System.out.println("##########\n" + s.getText() + " :" + s.getAcceptDate() + ":"
//						+ s.getRight());
//				System.out.println("##" + s.purposes.get(0).getPurpose());
//				System.out.println("##########");

				���ε_���� ss = res.remove(res.size() - 1);
				���ε_���� newS = ss.merge(s);
				res.add(newS);
			} else if (s.purposes.size() > 1) {
				if (isNothing(s.purposes.get(0).getAcceptDate())
						&& isNothing(s.purposes.get(1).getAcceptDate())) {
					�������� p1 = s.purposes.remove(0);
					�������� p2 = s.purposes.remove(0);
					�������� p3 = p1.merge(p2);
					s.purposes.add(0, p3);
				}
			}

			else {
				res.add(s);
			}
		}
		return res;
	}

	/**
	 * Checks if is nothing.
	 * 
	 * @param date the date
	 * 
	 * @return true, if is nothing
	 */
	private static boolean isNothing(String date) {
		return date == null || "".equals(date);
	}

}
