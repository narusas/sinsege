package net.narusas.si.auction.app.update;

import net.narusas.si.auction.app.Initializer;

public class UpdateInitializaer extends Initializer {
	public void init() {
		loadConfiguration();
		load법원목록();
	}
}
