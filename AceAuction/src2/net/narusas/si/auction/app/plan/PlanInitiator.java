package net.narusas.si.auction.app.plan;

import net.narusas.si.auction.app.Initializer;



public class PlanInitiator extends Initializer {
	public void init() {
		loadConfiguration();
		load법원목록();
	}
}
