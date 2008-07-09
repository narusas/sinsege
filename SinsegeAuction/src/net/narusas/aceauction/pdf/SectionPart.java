/*
 * 
 */
package net.narusas.aceauction.pdf;

import java.util.LinkedList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class SectionPart.
 */
public class SectionPart extends Item {

	/** The accept days. */
	List<Entity> acceptDays = new LinkedList<Entity>();

	/** The becauses. */
	List<Entity> becauses = new LinkedList<Entity>();

	/** The prioty nos. */
	List<Entity> priotyNos = new LinkedList<Entity>();

	/** The purposes. */
	List<Entity> purposes = new LinkedList<Entity>();

	/** The right and etc. */
	List<Entity> rightAndEtc = new LinkedList<Entity>();

	/**
	 * Instantiates a new section part.
	 */
	public SectionPart() {
		super(new Entity("", 0, 0, 0, 0));
	}

	/**
	 * Adds the accept days.
	 * 
	 * @param values the values
	 */
	public void addAcceptDays(List<Entity> values) {
		acceptDays.addAll(values);
	}

	/**
	 * Adds the becauses.
	 * 
	 * @param values the values
	 */
	public void addBecauses(List<Entity> values) {
		becauses.addAll(values);
	}

	/**
	 * Adds the prioty nos.
	 * 
	 * @param values the values
	 */
	public void addPriotyNos(List<Entity> values) {
		priotyNos.addAll(values);
	}

	/**
	 * Adds the purpose.
	 * 
	 * @param values the values
	 */
	public void addPurpose(List<Entity> values) {
		purposes.addAll(values);
	}

	/**
	 * Adds the right and etc.
	 * 
	 * @param values the values
	 */
	public void addRightAndEtc(List<Entity> values) {
		rightAndEtc.addAll(values);
	}

	/**
	 * Gets the.
	 * 
	 * @param i the i
	 * 
	 * @return the item
	 */
	public Item get(int i) {
		return childs.get(i);
	}

}
