/*
 * 
 */
package net.narusas.aceauction.pdf;

import java.util.LinkedList;

// TODO: Auto-generated Javadoc
/**
 * The Class ���ε_����.
 */
public class ���ε_���� {

	/** The purpose. */
	private ���ε_�������� purpose;

	/** The text. */
	private final String text;

	/** The will delete. */
	private boolean willDelete;

	/** The purposes. */
	LinkedList<���ε_��������> purposes = new LinkedList<���ε_��������>();

	/**
	 * Instantiates a new ���ε_����.
	 * 
	 * @param text the text
	 */
	public ���ε_����(String text) {
		if (!text.contains("(")) {
			this.text = text;
		} else {
			this.text = text.substring(0, text.indexOf("(")).trim();
		}

	}

	/**
	 * Adds the accept date.
	 * 
	 * @param entity the entity
	 */
	public void addAcceptDate(Entity entity) {
		initPurpose();
		purpose.addAcceptDate(entity.getText());
	}

	/**
	 * Adds the because.
	 * 
	 * @param entity the entity
	 */
	public void addBecause(Entity entity) {
		initPurpose();
		purpose.addBecause(entity.getText());
	}

	/**
	 * Adds the purpose.
	 * 
	 * @param entity the entity
	 */
	public void addPurpose(Entity entity) {
		purpose = new ���ε_��������(entity);
		purposes.add(purpose);
	}

	/**
	 * Adds the right and etc.
	 * 
	 * @param entity the entity
	 */
	public void addRightAndEtc(Entity entity) {
		initPurpose();
		purpose.addRightAndEtc(entity);
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return text + "[" + willDelete + "] = " + purposes;
	}

	/**
	 * Will delete.
	 * 
	 * @return true, if successful
	 */
	public boolean willDelete() {
		return willDelete;
	}

	/**
	 * Will delete.
	 * 
	 * @param willDelete the will delete
	 */
	public void willDelete(boolean willDelete) {
		this.willDelete = willDelete;
	}

	/**
	 * Inits the purpose.
	 */
	private void initPurpose() {
		if (purpose == null) {
			purpose = new ���ε_��������("");
			purposes.add(purpose);
		}
	}
}
