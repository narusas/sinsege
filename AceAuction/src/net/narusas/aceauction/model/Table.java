package net.narusas.aceauction.model;

import java.util.LinkedList;
import java.util.List;

/**
 * 테이블을 표현하는 클래스이다. Header와 Row로 구성된다.
 * 
 * @author narusas
 * 
 */
public class Table {

	private final LinkedList<Row> rows = new LinkedList<Row>();

	final LinkedList<String> headers;

	public Table(LinkedList<String> heraders) {
		this.headers = heraders;
	}

	public void add(Row r) {
		rows.add(r);
		r.setTable(this);
	}

	public List<String> getHeaders() {
		return headers;
	}

	public List<Row> getRows() {
		return rows;
	}

	@Override
	public String toString() {
		return rows.toString();
	}
}
