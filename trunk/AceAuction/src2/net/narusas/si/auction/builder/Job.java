package net.narusas.si.auction.builder;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.fetchers.HTMLUtils;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.물건감정평가서;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.model.사건감정평가서;
import net.narusas.si.auction.model.dao.사건Dao;
import net.narusas.si.auction.pdf.gamjung.GamjungParser;
import net.narusas.si.auction.pdf.gamjung.GamjungParser.Group;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Job {
	final Logger logger = LoggerFactory.getLogger("auction");

	사건 사건;
	List<물건> new물건List;
	신건ModeStrategy strategy;
	public boolean haveOld;

	private void fill감정평가요항(사건 event, File rawFile, List<물건> 물건List) {
		logger.info("감정평가서를 분석합니다");
		try {
			GamjungParser parser = new GamjungParser();
			List<Group> res = parser.parse(rawFile);
			String 감정원 = parser.getSrc();
			if (감정원.length() > 20) {
				감정원 = "";
			}
			String 감정시점 = parser.getDate();
			if (res == null || res.size() == 0) {
				return;
			}
			Group g = res.get(0);
			사건감정평가서 report = new 사건감정평가서();
			if (event.get감정평가서() != null) {
				report = event.get감정평가서();
			}
			logger.info("감정원:" + 감정원);
			logger.info("감정시점:" + 감정시점);

			logger.info("도로관련:" + g.get도로관련());
			logger.info("이용상태:" + g.get이용상태());
			logger.info("위치및교통:" + g.get위치및교통());
			logger.info("토지:" + g.get토지());

			report.set도로(stableEncoding(g.get도로관련()));
			report.set용도(stableEncoding(g.get이용상태()));
			report.set장소(stableEncoding(g.get위치및교통()));
			report.set토지(stableEncoding(g.get토지()));
			event.set감정평가서(report);

			Date d = HTMLUtils.toDate(감정시점);

			List<물건> items = 물건List;
			for (물건 물건 : items) {
				물건감정평가서 물건감정평가서 = new 물건감정평가서();
				if (물건.get감정평가서() != null) {
					물건감정평가서 = 물건.get감정평가서();
				}
				물건감정평가서.set평가기관(감정원);
				물건감정평가서.set평가시점(d);
			}

		} catch (Throwable e) {
			// e.printStackTrace();
		}

	}

	private String stableEncoding(String text) {
		try {
			byte[] data = text.getBytes("euc-kr");
			return new String(data, "euc-kr");
		} catch (UnsupportedEncodingException e) {
			return text;
		}
	}

	void save() {
		사건Dao dao = (사건Dao) App.context.getBean("사건DAO");
		if (haveOld) {
			dao.update(사건);
		} else {
			dao.saveOrUpdate(사건);
		}
	}
}
