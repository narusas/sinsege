/*
 * 
 */
package net.narusas.aceauction.pdf.jpedal;

import java.util.LinkedList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class 사항Fixer.
 */
public class 사항Fixer {

	/**
	 * Fix.
	 * 
	 * @param items the items
	 * 
	 * @return the list<등기부등본_사항>
	 */
	public static List<등기부등본_사항> fix(List<등기부등본_사항> items) {
		LinkedList<등기부등본_사항> res = new LinkedList<등기부등본_사항>();

		for (등기부등본_사항 s : items) {
			String text = s.getText();
			if ("".equals(text) || (text.contains(")") && text.contains("(") == false)
					|| (Character.isDigit(text.toCharArray()[0]) == false && text.contains(")"))) {

//				System.out.println("!!!!!!!!!!!!!");
//				for (사항 s2 : items) {
//
//					System.out.println("##" + s2);
//				}

//				System.out.println("##########\n" + s.getText() + " :" + s.getAcceptDate() + ":"
//						+ s.getRight());
//				System.out.println("##" + s.purposes.get(0).getPurpose());
//				System.out.println("##########");

				등기부등본_사항 ss = res.remove(res.size() - 1);
				등기부등본_사항 newS = ss.merge(s);
				res.add(newS);
			} else if (s.purposes.size() > 1) {
				if (isNothing(s.purposes.get(0).getAcceptDate())
						&& isNothing(s.purposes.get(1).getAcceptDate())) {
					하위사항 p1 = s.purposes.remove(0);
					하위사항 p2 = s.purposes.remove(0);
					하위사항 p3 = p1.merge(p2);
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
