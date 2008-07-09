/*
 * 
 */
package net.narusas.aceauction.pdf.jpedal;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class ��������.
 */
public class �������� {

	/** The p. */
	static Pattern p = Pattern.compile("(\\d+��\\s*\\d+��\\s*\\d+��)");

	/** The because. */
	private List<TextPosition> because = new LinkedList<TextPosition>();

	/** The last right and etc. */
	private TextPosition lastRightAndEtc;

	/** The purpose. */
	private TextPosition purpose;

	/** The right. */
	private List<TextPosition> right = new LinkedList<TextPosition>();

	/** The accept date. */
	List<TextPosition> acceptDate = new LinkedList<TextPosition>();

	/**
	 * Instantiates a new ��������.
	 * 
	 * @param purpose the purpose
	 */
	public ��������(TextPosition purpose) {
		this.purpose = purpose;
	}

	/**
	 * Adds the accept date.
	 * 
	 * @param text the text
	 */
	public void addAcceptDate(TextPosition text) {
		acceptDate.add(text);
	}

	/**
	 * Adds the because.
	 * 
	 * @param entity the entity
	 */
	public void addBecause(TextPosition entity) {
		because.add(entity);
	}

	/**
	 * Adds the right and etc.
	 * 
	 * @param entity the entity
	 */
	public void addRightAndEtc(TextPosition entity) {
		if (this.acceptDate.size() == 0 || right.size() == 0) {
			right.add(entity);
			return;
		}
		TextPosition accept = this.acceptDate.get(this.acceptDate.size() - 1);
		if (accept.getY() == entity.getY() && accept.getPage() == entity.getPage()) {
			right.add(entity);
			return;
		}

		// ������ ���� ������ ���̤��� �Ǹ��� �� �������� �ٸ��ٸ� �Ǹ��ڴ� ���� ���������� �̾����� ��������.
		if (accept.getPage() != entity.getPage()) {
			TextPosition b = this.right.remove(this.right.size() - 1);
			this.right.add(b.addVertical�鿩����(entity));
			return;
		}
		right.add(entity);
	}

	/**
	 * Gets the accept date.
	 * 
	 * @return the accept date
	 */
	public String getAcceptDate() {
		String temp = "";
		for (TextPosition a : acceptDate) {
			temp += a.getText() + "\n";
		}
		return temp.trim();
	}

	/**
	 * Gets the because.
	 * 
	 * @return the because
	 */
	public String getBecause() {
		String temp = "";
		for (TextPosition a : because) {
			temp += a.getText() + "\n";
		}
		return temp.trim();
	}

	/**
	 * Gets the last right and etc.
	 * 
	 * @return the last right and etc
	 */
	public TextPosition getLastRightAndEtc() {
		return lastRightAndEtc;
	}

	/**
	 * Gets the purpose.
	 * 
	 * @return the purpose
	 */
	public String getPurpose() {
		return purpose.getText();
	}

	/**
	 * Gets the right.
	 * 
	 * @return the right
	 */
	public List<TextPosition> getRight() {
		return right;
	}

	/**
	 * Merge.
	 * 
	 * @param p2 the p2
	 * 
	 * @return the ��������
	 */
	public �������� merge(�������� p2) {
		�������� newP = new ��������(this.purpose.add(p2.purpose));
		newP.acceptDate = merge(this.acceptDate, p2.acceptDate);
		newP.because = merge(this.because, p2.because);
		newP.right = merge(this.right, p2.right);

		return newP;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "" + purpose + ", " + acceptDate + "," + because + "," + right;
	}

	/**
	 * Merge.
	 * 
	 * @param list1 the list1
	 * @param list2 the list2
	 * 
	 * @return the list< text position>
	 */
	private List<TextPosition> merge(List<TextPosition> list1, List<TextPosition> list2) {
		List<TextPosition> res = new LinkedList<TextPosition>();
		res.addAll(list1);
		res.addAll(list2);
		return res;
	}

}
