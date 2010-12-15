package net.narusas.aceauction.pdf;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class ���ε_�������� {

	static Pattern p = Pattern.compile("(\\d+��\\s*\\d+��\\s*\\d+��)");

	private String acceptDate = "";

	private String because = "";

	private Entity lastRightAndEtc;

	private String purpose = "";

	private String right = "";

	List<String> rights = new LinkedList<String>();

	public ���ε_��������(Entity purpose) {
		this(purpose.getText());
	}

	public ���ε_��������(String purpose) {
		setPurpose(purpose);
	}

	public void addAcceptDate(String text) {
		acceptDate += text;
	}

	public void addBecause(String text) {
		// Matcher m = p.matcher(text);
		// if (m.find()) {
		// String s = m.group(1);
		// because += text.substring(text.indexOf(s) + s.length());
		// } else {
		because += text;
		// }

	}

	public void addRightAndEtc(Entity entity) {
		this.lastRightAndEtc = entity;
		right += entity.getText() + "\n";
		rights.add(entity.getText());
	}

	public String getAcceptDate() {
		return acceptDate.trim();
	}

	public String getBecause() {
		return because.trim();
	}

	public Entity getLastRightAndEtc() {
		return lastRightAndEtc;
	}

	public String getPurpose() {
		return purpose.trim();
	}

	public String getRight() {
		return right.trim();
	}

	public List<String> getRights() {
		return rights;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
		if (this.purpose.contains("(")) {
			this.purpose = this.purpose.substring(0, this.purpose.indexOf("("));
		}
	}

	@Override
	public String toString() {
		return "" + purpose + ", " + acceptDate + "," + because + "," + rights;
	}

}
