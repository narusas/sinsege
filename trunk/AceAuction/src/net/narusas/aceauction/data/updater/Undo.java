package net.narusas.aceauction.data.updater;

import java.sql.Statement;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;

public class Undo extends DB {

	private final ���� charge;

	private final ���� court;

	public Undo(���� court, ���� charge) {
		this.court = court;
		this.charge = charge;
	}

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

	private long getExisting����Id(���� charge) throws Exception {
		���� cc = ����.findByMemoryObject(charge);
		if (cc != null) {
			return cc.getNo();
		}
		return -1;
	}

}
