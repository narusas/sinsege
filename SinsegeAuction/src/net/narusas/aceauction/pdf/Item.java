/*
 * 
 */
package net.narusas.aceauction.pdf;

import java.util.LinkedList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Item.
 */
public class Item {

	/** The childs. */
	public List<Item> childs = new LinkedList<Item>();

	/** The entity. */
	public Entity entity;

	/**
	 * Instantiates a new item.
	 * 
	 * @param entity the entity
	 */
	public Item(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Adds the item.
	 * 
	 * @param item the item
	 */
	public void addItem(Item item) {
		childs.add(item);
	}

	/**
	 * Sets the item.
	 * 
	 * @param childs the new item
	 */
	public void setItem(List<Item> childs) {
		this.childs = childs;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (Item e : childs) {
			buf.append("\n").append(e.toString());
		}
		return entity.getText() + "[" + buf.toString() + "]";
	}
}
