package net.narusas.si.auction.builder;

import java.util.List;

import net.narusas.si.auction.app.build.EventNotifier;
import net.narusas.si.auction.fetchers.사건목록Fetcher;
import net.narusas.si.auction.model.담당계;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 담당계목록Batch {

	private final List<담당계> workset;
	final Logger logger = LoggerFactory.getLogger("auction");

	public 담당계목록Batch(List<담당계> workset) {
		this.workset = workset;
	}

	public void execute() {
		EventNotifier.set담당계Size(workset.size());
		사건목록Fetcher fetcher = new 사건목록Fetcher();
		for (int i = 0; i < workset.size(); i++) {
			담당계 charge = workset.get(i);
			logger.info(charge + " 작업을 시작합니다");
			EventNotifier.progress담당계(i, charge);
			try {
				new 사건목록Batch(fetcher.fetchAll(charge)).execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info(charge + " 작업을 완료했습니다.");
		}
		EventNotifier.end담당계();
	}
}
