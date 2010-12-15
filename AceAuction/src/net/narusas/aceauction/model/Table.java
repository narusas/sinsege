package net.narusas.aceauction.model;

import java.util.LinkedList;
import java.util.List;

/**
 * ���̺��� ǥ���ϴ� Ŭ�����̴�. Header�� Row�� �����ȴ�.
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
