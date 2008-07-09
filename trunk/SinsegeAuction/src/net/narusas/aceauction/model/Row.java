/*
 * 
 */
package net.narusas.aceauction.model;

import java.util.LinkedList;

// TODO: Auto-generated Javadoc
/**
 * 테이블의 각각의 한 줄을 표현한다.
 * 
 * @author narusas
 */
public class Row {

	/** The parent. */
	private Table parent;

	/** The values. */
	private LinkedList<String> values = new LinkedList<String>();

	/**
	 * Adds the.
	 * 
	 * @param value the value
	 */
	public void add(String value) {
		values.add(value);
	}

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public Table getParent() {
		return parent;
	}

	/**
	 * Gets the value.
	 * 
	 * @param index the index
	 * 
	 * @return the value
	 */
	public String getValue(int index) {
		return values.get(index);
	}

	/**
	 * Gets the values.
	 * 
	 * @return the values
	 */
	public LinkedList<String> getValues() {
		return values;
	}

	/**
	 * Sets the table.
	 * 
	 * @param parent the new table
	 */
	public void setTable(Table parent) {
		this.parent = parent;
	}

	/**
	 * Size.
	 * 
	 * @return the int
	 */
	public int size() {
		return values.size();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return values.toString();
	}
}
