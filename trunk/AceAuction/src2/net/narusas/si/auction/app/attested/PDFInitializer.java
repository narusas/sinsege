package net.narusas.si.auction.app.attested;

import net.narusas.si.auction.app.Initializer;

public class PDFInitializer extends Initializer {
	public void init() {
		loadConfiguration();
		load법원목록();
		update담당계();
	}
}
