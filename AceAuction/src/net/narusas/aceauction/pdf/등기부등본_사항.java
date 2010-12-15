package net.narusas.aceauction.pdf;

import java.util.LinkedList;

public class 등기부등본_사항 {

	private 등기부등본_하위사항 purpose;

	private final String text;

	private boolean willDelete;

	LinkedList<등기부등본_하위사항> purposes = new LinkedList<등기부등본_하위사항>();

	public 등기부등본_사항(String text) {
		if (!text.contains("(")) {
			this.text = text;
		} else {
			this.text = text.substring(0, text.indexOf("(")).trim();
		}

	}

	public void addAcceptDate(Entity entity) {
		initPurpose();
		purpose.addAcceptDate(entity.getText());
	}

	public void addBecause(Entity entity) {
		initPurpose();
		purpose.addBecause(entity.getText());
	}

	public void addPurpose(Entity entity) {
		purpose = new 등기부등본_하위사항(entity);
		purposes.add(purpose);
	}

	public void addRightAndEtc(Entity entity) {
		initPurpose();
		purpose.addRightAndEtc(entity);
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return text + "[" + willDelete + "] = " + purposes;
	}

	public boolean willDelete() {
		return willDelete;
	}

	public void willDelete(boolean willDelete) {
		this.willDelete = willDelete;
	}

	private void initPurpose() {
		if (purpose == null) {
			purpose = new 등기부등본_하위사항("");
			purposes.add(purpose);
		}
	}
}
