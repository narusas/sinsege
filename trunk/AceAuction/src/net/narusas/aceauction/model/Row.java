package net.narusas.aceauction.model;

import java.util.LinkedList;

/**
 * ���̺��� ������ �� ���� ǥ���Ѵ�.
 * 
 * @author narusas
 * 
 */
public class Row {

	private Table parent;

	private LinkedList<String> values = new LinkedList<String>();

	public void add(String value) {
		values.add(value);
	}

	public Table getParent() {
		return parent;
	}

	public String getValue(int index) {
		return values.get(index);
	}

	public LinkedList<String> getValues() {
		return values;
	}

	public void setTable(Table parent) {
		this.parent = parent;
	}

	public int size() {
		return values.size();
	}

	@Override
	public String toString() {
		return values.toString();
	}
}
