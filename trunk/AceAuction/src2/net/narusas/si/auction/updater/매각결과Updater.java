package net.narusas.si.auction.updater;

import java.util.Date;

import net.narusas.si.auction.model.사건;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 매각결과Updater {
	private 사건 사건;

	public 매각결과Updater(사건 사건) {
		this.사건 = 사건;
	}

	final Logger logger = LoggerFactory.getLogger("auction");

	public void execute() {
		// TODO Auto-generated method stub
		
	}
	
}
