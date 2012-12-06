package net.narusas.si.auction.builder;

import java.util.List;

import net.narusas.si.auction.app.build.EventNotifier;
import net.narusas.si.auction.model.사건;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 사건목록Batch implements ModeStrategy {

	private final List<사건> 사건목록;
	final Logger logger = LoggerFactory.getLogger("auction");
	private Long 여기부터사건번호;

	public 사건목록Batch(List<사건> 사건목록, Long 여기부터사건번호) {
		this(사건목록);
		this.여기부터사건번호 = 여기부터사건번호;
	}

	public 사건목록Batch(List<사건> 사건목록) {
		this.사건목록 = 사건목록;
	}

	public List<사건> get사건목록() {
		return 사건목록;
	}

	@Override
	public boolean execute() {
		boolean isFound = false;
		EventNotifier.set사건목록Size(사건목록.size());
		for (int i = 0; i < 사건목록.size(); i++) {
			EventNotifier.progress사건(i, 사건목록.get(i));
			사건 사건 = 사건목록.get(i);
			if (여기부터사건번호 == null) {
				logger.info("사건:" + 사건 + " 처리를 시작합니다.");
				사건BatchFactory.getBatch(사건).execute();
				logger.info("사건:" + 사건 + " 처리를 종료합니다.");
			} else if (여기부터사건번호 == 사건.get사건번호() || isFound) {
				isFound = true;
				logger.info("사건:" + 사건 + " 처리를 시작합니다.");
				사건BatchFactory.getBatch(사건).execute();
				logger.info("사건:" + 사건 + " 처리를 종료합니다.");
			}

			// new 사건Batch(사건목록.get(i)).execute();
		}
		EventNotifier.end사건();
		return isFound;
	}

}
