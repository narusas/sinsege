/*
 * 
 */
package net.narusas.aceauction.pdf.jpedal;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class TextPositionFix.
 */
public class TextPositionFix {

	/**
	 * Fix x pos.
	 * 
	 * @param src the src
	 */
	public static void fixXPos(List<TextPosition> src) {
		for (TextPosition p : src) {
			String text = p.getText();
			float left = p.getX();
			float right = p.getX2();

			float len = 0;
			for (char ch : text.toCharArray()) {
				if (isDigit(ch) || isSymbol(ch)) {
					len += 5;
				} else {
					len += 10;
				}
			}
			p.setX(right - len);
		}

	}

	/**
	 * Checks if is digit.
	 * 
	 * @param ch the ch
	 * 
	 * @return true, if is digit
	 */
	private static boolean isDigit(char ch) {
		return ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5'
				|| ch == '6' || ch == '7' || ch == '8' || ch == '9';
	}

	/**
	 * Checks if is symbol.
	 * 
	 * @param ch the ch
	 * 
	 * @return true, if is symbol
	 */
	private static boolean isSymbol(char ch) {
		return ch == '-' || ch == '*' || ch == '(' || ch == ')' || ch == ' ';
	}

}
