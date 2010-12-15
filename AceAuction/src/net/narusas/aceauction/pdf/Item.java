package net.narusas.aceauction.pdf;

import java.util.LinkedList;
import java.util.List;

public class Item {

	public List<Item> childs = new LinkedList<Item>();

	public Entity entity;

	public Item(Entity entity) {
		this.entity = entity;
	}

	public void addItem(Item item) {
		childs.add(item);
	}

	public void setItem(List<Item> childs) {
		this.childs = childs;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (Item e : childs) {
			buf.append("\n").append(e.toString());
		}
		return entity.getText() + "[" + buf.toString() + "]";
	}
}
