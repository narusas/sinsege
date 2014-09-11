package net.narusas.si.auction.app.attestedTaker;

import net.narusas.si.auction.app.Initializer;



public class AttestedTakerInitiator extends Initializer {
	public void init() {
		loadConfiguration();
		load법원목록();
	}
}
