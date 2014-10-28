package net.narusas.si.auction.builder;

import net.narusas.si.auction.app.build.BuildApp;
import net.narusas.si.auction.model.사건;

public class 사건BatchFactory {

	public static ModeStrategy getBatch(사건 사건) {
		switch (BuildApp.mode) {
		case 신건:
			return new 신건ModeStrategy(사건);
		case 매각물건명세서:
			return new 매각물건명세서ModeStrategy(사건);
		case 등기부등본:
			return new 등기부등본ModeStrategy(사건);
		case 현장조사서이미지:
			return null;
		}
		return null;
	}
}
