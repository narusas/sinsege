package net.narusas.si.auction.model;

import java.util.Collection;

public class 등기부등본 {
	Long id;
	Collection<등기부등본Item> items;
	private 물건 물건;
	private String type;

	public 등기부등본() {

	}

	public 등기부등본(물건 물건, String type) {
		this.물건 = 물건;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Collection<등기부등본Item> getItems() {
		return items;
	}

	public void setItems(Collection<등기부등본Item> items) {
		this.items = items;
		refreshItemsGoods();
	}

	private void refreshItemsGoods() {
		if (this.items == null){
			return;
		}
		for (등기부등본Item item : this.items) {
			item.set물건(물건);
			item.set등기부등본(this);
		}
	}

	public 물건 get물건() {
		return 물건;
	}

	public void set물건(물건 물건) {
		this.물건 = 물건;
		refreshItemsGoods();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
