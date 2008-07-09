/*
 * 
 */
package net.narusas.aceauction.data.updater;

import java.sql.Statement;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.법원;

// TODO: Auto-generated Javadoc
/**
 * The Class Undo.
 */
public class Undo extends DB {

	/** The charge. */
	private final 담당계 charge;

	/** The court. */
	private final 법원 court;

	/**
	 * Instantiates a new undo.
	 * 
	 * @param court the court
	 * @param charge the charge
	 */
	public Undo(법원 court, 담당계 charge) {
		this.court = court;
		this.charge = charge;
	}

	/**
	 * Start.
	 * 
	 * @throws Exception the exception
	 */
	public void start() throws Exception {
		reConnect();
		long no = getExisting담당계Id(charge);
		if (no == -1) {
			return;
		}
		Statement stmt = createStatement();
		stmt.executeUpdate("UPDATE ac_goods SET done=0 WHERE charge_id=" + no
				+ ";");

	}

	/**
	 * Gets the existing담당계 id.
	 * 
	 * @param charge the charge
	 * 
	 * @return the existing담당계 id
	 * 
	 * @throws Exception the exception
	 */
	private long getExisting담당계Id(담당계 charge) throws Exception {
		담당계 cc = 담당계.findByMemoryObject(charge);
		if (cc != null) {
			return cc.getNo();
		}
		return -1;
	}

}
