/*
 * 
 */
package net.narusas.aceauction.data.updater;

// TODO: Auto-generated Javadoc
/**
 * The Class 물건Item.
 */
public class 물건Item {

	/** The charge. */
	private final String charge;

	/** The court. */
	private final String court;

	/** The event_no. */
	private final String event_no;

	/** The id. */
	private final String id;

	/** The no. */
	private final String no;

	/**
	 * Instantiates a new 물건 item.
	 * 
	 * @param id the id
	 * @param court the court
	 * @param charge the charge
	 * @param event_no the event_no
	 * @param no the no
	 */
	public 물건Item(String id, String court, String charge, String event_no, String no) {
		this.id = id;
		this.court = court;
		this.charge = charge;
		this.event_no = event_no;
		this.no = no;
	}

	/**
	 * Gets the charge.
	 * 
	 * @return the charge
	 */
	public String getCharge() {
		return charge;
	}

	/**
	 * Gets the court.
	 * 
	 * @return the court
	 */
	public String getCourt() {
		return court;
	}

	/**
	 * Gets the event_no.
	 * 
	 * @return the event_no
	 */
	public String getEvent_no() {
		return event_no;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the no.
	 * 
	 * @return the no
	 */
	public String getNo() {
		return no;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "court=" + getCourt() + ",charge=" + getCharge() + ",event_no=" + getEvent_no()
				+ ",no=" + getNo() + ",id=" + id;
	}

}