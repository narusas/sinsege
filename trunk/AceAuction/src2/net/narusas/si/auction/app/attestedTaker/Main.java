package net.narusas.si.auction.app.attestedTaker;

public class Main {
	public static void main(String[] args) {
		AttestedTakerInitiator initiator = new AttestedTakerInitiator();
		initiator.init();
		AttestedTakerApp app = new AttestedTakerApp();
		app.initiator = initiator;
		
		app.setup();
		app.setSize(800,  640);
		app.setVisible(true);
	}
}
