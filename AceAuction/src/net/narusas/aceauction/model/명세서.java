package net.narusas.aceauction.model;


public class ���� {

	private final String ����;

	private final String �Ǹ�;

	private final String ���;

	private final String ����;

	private final Table ������;

	public ����(Table table, String ���, String �Ǹ�, String ����, String ����) {
		this.������ = table;
		this.��� = ���;
		this.�Ǹ� = �Ǹ�;
		this.���� = ����;
		this.���� = ����;

	}

	public String get����() {
		return ����;
	}

	public String get�Ǹ�() {
		return �Ǹ�;
	}

	public String get���() {
		return ���;
	}

	public String get����() {
		return ����;
	}

	public Table get������() {
		return ������;
	}

}
