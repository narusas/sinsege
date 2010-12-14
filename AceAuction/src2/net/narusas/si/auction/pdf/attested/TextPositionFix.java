package net.narusas.si.auction.pdf.attested;

import java.util.List;

public class TextPositionFix {

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

	private static boolean isDigit(char ch) {
		return ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5'
				|| ch == '6' || ch == '7' || ch == '8' || ch == '9';
	}

	private static boolean isSymbol(char ch) {
		return ch == '-' || ch == '*' || ch == '(' || ch == ')' || ch == ' ';
	}

}
