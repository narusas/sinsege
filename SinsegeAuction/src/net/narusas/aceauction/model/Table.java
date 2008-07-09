/*
 * 
 */
package net.narusas.aceauction.model;

import java.util.LinkedList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * 테이블을 표현하는 클래스이다. Header와 Row로 구성된다.
 * 
 * @author narusas
 */
public class Table {

	/** The rows. */
	private final LinkedList<Row> rows = new LinkedList<Row>();

	/** The headers. */
	final LinkedList<String> headers;

	/**
	 * Instantiates a new table.
	 * 
	 * @param heraders the heraders
	 */
	public Table(LinkedList<String> heraders) {
		this.headers = heraders;
	}

	/**
	 * Adds the.
	 * 
	 * @param r the r
	 */
	public void add(Row r) {
		rows.add(r);
		r.setTable(this);
	}

	/**
	 * Gets the headers.
	 * 
	 * @return the headers
	 */
	public List<String> getHeaders() {
		return headers;
	}

	/**
	 * Gets the rows.
	 * 
	 * @return the rows
	 */
	public List<Row> getRows() {
		return rows;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return rows.toString();
	}
}
