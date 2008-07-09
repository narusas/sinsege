/*
 * 
 */
package net.narusas.aceauction.data.updater;

import java.sql.Statement;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;

// TODO: Auto-generated Javadoc
/**
 * The Class Undo.
 */
public class Undo extends DB {

	/** The charge. */
	private final ���� charge;

	/** The court. */
	private final ���� court;

	/**
	 * Instantiates a new undo.
	 * 
	 * @param court the court
	 * @param charge the charge
	 */
	public Undo(���� court, ���� charge) {
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
		long no = getExisting����Id(charge);
		if (no == -1) {
			return;
		}
		Statement stmt = createStatement();
		stmt.executeUpdate("UPDATE ac_goods SET done=0 WHERE charge_id=" + no
				+ ";");

	}

	/**
	 * Gets the existing���� id.
	 * 
	 * @param charge the charge
	 * 
	 * @return the existing���� id
	 * 
	 * @throws Exception the exception
	 */
	private long getExisting����Id(���� charge) throws Exception {
		���� cc = ����.findByMemoryObject(charge);
		if (cc != null) {
			return cc.getNo();
		}
		return -1;
	}

}
