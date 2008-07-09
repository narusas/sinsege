/*
 * 
 */
package net.narusas.aceauction.pdf;

import java.util.LinkedList;

// TODO: Auto-generated Javadoc
/**
 * The Class 등기부등본_사항.
 */
public class 등기부등본_사항 {

	/** The purpose. */
	private 등기부등본_하위사항 purpose;

	/** The text. */
	private final String text;

	/** The will delete. */
	private boolean willDelete;

	/** The purposes. */
	LinkedList<등기부등본_하위사항> purposes = new LinkedList<등기부등본_하위사항>();

	/**
	 * Instantiates a new 등기부등본_사항.
	 * 
	 * @param text the text
	 */
	public 등기부등본_사항(String text) {
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
		purpose = new 등기부등본_하위사항(entity);
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
			purpose = new 등기부등본_하위사항("");
			purposes.add(purpose);
		}
	}
}
