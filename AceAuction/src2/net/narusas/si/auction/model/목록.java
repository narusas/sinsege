package net.narusas.si.auction.model;

public class 목록 {

	private final String no;
	private final String address;
	private final String buNo;
	private final String type;
	private final String comment;
	private final 물건 goods;

	public 목록(물건 goods, String no, String address, String buNo, String type, String comment) {
		this.goods = goods;
		this.no = no;
		this.address = address;
		this.buNo = buNo;
		this.type = type;
		this.comment = comment;

	}

	public 물건 getGoods() {
		return goods;
	}

	public String getNo() {
		return no;
	}

	public String getAddress() {
		return address;
	}

	public String getBuNo() {
		return buNo;
	}

	public String getType() {
		return type;
	}

	public String getComment() {
		return comment;
	}

	@Override
	public String toString() {
		return ""+no+" ["+type+"  "+comment+" "+address+"]";
	}
}
