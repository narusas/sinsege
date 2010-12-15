package net.narusas.aceauction.pdf;

import java.util.LinkedList;

public class ���ε_���� {

	private ���ε_�������� purpose;

	private final String text;

	private boolean willDelete;

	LinkedList<���ε_��������> purposes = new LinkedList<���ε_��������>();

	public ���ε_����(String text) {
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
		purpose = new ���ε_��������(entity);
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
			purpose = new ���ε_��������("");
			purposes.add(purpose);
		}
	}
}
