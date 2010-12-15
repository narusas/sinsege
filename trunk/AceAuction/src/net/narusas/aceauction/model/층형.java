package net.narusas.aceauction.model;

public class ���� {

	private final String area;
	private final String text;

	public ����(String text, String area) {
		this.text = text.replaceAll("\n", " ").replaceAll("\\s{2,}", " ")
				.trim();
		this.area = area;
	}

	@Override
	public boolean equals(Object obj) {
		���� target = (����) obj;
		return text.equals(target.text) && area.equals(target.area);
	}

	public String getArea() {
		return area;
	}

	public String getText() {
		return text;
	}

	@Override
	public int hashCode() {
		return text.hashCode() + area.hashCode();
	}

	@Override
	public String toString() {
		return text + "," + area;
	}

}