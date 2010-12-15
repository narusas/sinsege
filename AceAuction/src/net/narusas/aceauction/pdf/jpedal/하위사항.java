package net.narusas.aceauction.pdf.jpedal;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class 하위사항 {

	static Pattern p = Pattern.compile("(\\d+년\\s*\\d+월\\s*\\d+일)");

	private List<TextPosition> because = new LinkedList<TextPosition>();

	private TextPosition lastRightAndEtc;

	private TextPosition purpose;

	private List<TextPosition> right = new LinkedList<TextPosition>();

	List<TextPosition> acceptDate = new LinkedList<TextPosition>();

	public 하위사항(TextPosition purpose) {
		this.purpose = purpose;
	}

	public void addAcceptDate(TextPosition text) {
		acceptDate.add(text);
	}

	public void addBecause(TextPosition entity) {
		because.add(entity);
	}

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

		// 마지막 접수 일자의 페이ㅈ와 권리자 의 페이지가 다르다면 권리자는 이전 페이지부터 이어지는 페이지임.
		if (accept.getPage() != entity.getPage()) {
			TextPosition b = this.right.remove(this.right.size() - 1);
			this.right.add(b.addVertical들여쓰기(entity));
			return;
		}
		right.add(entity);
	}

	public String getAcceptDate() {
		String temp = "";
		for (TextPosition a : acceptDate) {
			temp += a.getText() + "\n";
		}
		return temp.trim();
	}

	public String getBecause() {
		String temp = "";
		for (TextPosition a : because) {
			temp += a.getText() + "\n";
		}
		return temp.trim();
	}

	public TextPosition getLastRightAndEtc() {
		return lastRightAndEtc;
	}

	public String getPurpose() {
		return purpose.getText();
	}

	public List<TextPosition> getRight() {
		return right;
	}

	public 하위사항 merge(하위사항 p2) {
		하위사항 newP = new 하위사항(this.purpose.add(p2.purpose));
		newP.acceptDate = merge(this.acceptDate, p2.acceptDate);
		newP.because = merge(this.because, p2.because);
		newP.right = merge(this.right, p2.right);

		return newP;
	}

	@Override
	public String toString() {
		return "" + purpose + ", " + acceptDate + "," + because + "," + right;
	}

	private List<TextPosition> merge(List<TextPosition> list1, List<TextPosition> list2) {
		List<TextPosition> res = new LinkedList<TextPosition>();
		res.addAll(list1);
		res.addAll(list2);
		return res;
	}

}
