package net.narusas.aceauction.data.updater;

public class 물건Item {

	private final String charge;

	private final String court;

	private final String event_no;

	private final String id;

	private final String no;

	public 물건Item(String id, String court, String charge, String event_no, String no) {
		this.id = id;
		this.court = court;
		this.charge = charge;
		this.event_no = event_no;
		this.no = no;
	}

	public String getCharge() {
		return charge;
	}

	public String getCourt() {
		return court;
	}

	public String getEvent_no() {
		return event_no;
	}

	public String getId() {
		return id;
	}

	public String getNo() {
		return no;
	}

	@Override
	public String toString() {
		return "court=" + getCourt() + ",charge=" + getCharge() + ",event_no=" + getEvent_no()
				+ ",no=" + getNo() + ",id=" + id;
	}

}