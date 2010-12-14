package net.narusas.si.auction.builder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.narusas.si.auction.app.build.EventNotifier;
import net.narusas.si.auction.fetchers.사건목록Fetcher;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.사건;

public class 담당계Batch {

	private final 담당계 담당계;
	private final long 사건번호;
	final Logger logger = LoggerFactory.getLogger("auction");

	public 담당계Batch(담당계 담당계, long 사건번호) {
		this.담당계 = 담당계;
		this.사건번호 = 사건번호;
	}

	public void execute() {
		EventNotifier.set담당계Size(1);
		사건목록Fetcher fetcher = new 사건목록Fetcher();
		logger.info(담당계 + " 작업을 시작합니다");
		EventNotifier.progress담당계(0, 담당계);
		try {
			List<사건> 사건목록 = fetcher.fetchAll(담당계);
			System.out.println("############ " + 사건번호);
			for (사건 사건 : 사건목록) {
				System.out.println(사건.get사건번호());
				if (사건.get사건번호() == 사건번호) {
					사건BatchFactory.getBatch(사건).execute();
					// new 사건Batch(사건).execute();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		EventNotifier.end담당계();
	}

}
