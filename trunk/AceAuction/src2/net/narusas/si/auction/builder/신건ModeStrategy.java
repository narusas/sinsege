package net.narusas.si.auction.builder;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.fetchers.HTMLUtils;
import net.narusas.si.auction.fetchers.사건감정평가서Fetcher;
import net.narusas.si.auction.fetchers.사건내역Fetcher;
import net.narusas.si.auction.fetchers.사건문건송달내역Fetcher;
import net.narusas.si.auction.fetchers.사건부동산표시목록Fetcher;
import net.narusas.si.auction.fetchers.사건임대차관계조사서Parser;
import net.narusas.si.auction.fetchers.사건현황조사서Fetcher;
import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.매각물건명세서;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.물건감정평가서;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.model.사건감정평가서;
import net.narusas.si.auction.model.dao.당사자Dao;
import net.narusas.si.auction.model.dao.물건Dao;
import net.narusas.si.auction.model.dao.사건Dao;
import net.narusas.si.auction.pdf.gamjung.GamjungParser;
import net.narusas.si.auction.pdf.gamjung.GamjungParser.Group;
import net.narusas.util.lang.NFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 신건ModeStrategy implements ModeStrategy {
	final Logger logger = LoggerFactory.getLogger("auction");
	private 사건 사건;
	private boolean haveOld;
	boolean do감정평가분석 = true;

	public 신건ModeStrategy(사건 사건) {
		this.사건 = 사건;
		try {
			do감정평가분석 = Boolean.parseBoolean(NFile.getText(new File("cfg/do1.cfg")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static ArrayList<Job> list = new ArrayList<Job>();

	static class Que extends Thread {

		public void run() {
			while (true) {
				if (list.size() == 0) {
					try {
						sleep(1000);
						continue;
					} catch (InterruptedException e) {
					}
				}

				try {
					Job job = null;
					synchronized (list) {
						job = list.remove(0);
					}
					if (job == null) {
						continue;
					}
					List<File> 감정평가서RawFiles = new 사건감정평가서Fetcher().download(job.사건);
					if (감정평가서RawFiles != null || 감정평가서RawFiles.size()>0) {
						for (int i=0; i< 감정평가서RawFiles.size();i++) {
							File 감정평가서RawFile = 감정평가서RawFiles.get(i);
							FileUploaderBG.getInstance().upload(//
									job.사건.getPath(),// 
									감정평가서RawFile.getName(),// 
									감정평가서RawFile//
							);
							
						}
						
						// 사진Collector.getInstance().add(사건, 감정평가서RawFile);

						job.strategy.fill감정평가요항(job.사건, 감정평가서RawFiles.get(0), job.new물건List);
						job.save();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	static {
		new Que().start();
		new Que().start();
		// new Que().start();
		// new Que().start();
		// new Que().start();
		// new Que().start();

	}

	public boolean execute() {
		logger.info(사건.get사건번호() + " 작업을 시작합니다");
		사건내역Fetcher f1 = new 사건내역Fetcher();
		try {
			if (f1.update(사건) == false) { // 사건이 자동차, 선박이면 무시.
				return false;
			}
			사건 old = validate(사건);
			List<물건> new물건List = 사건.get물건목록();

			사건 = old;
			for (물건 물건 : new물건List) {
				물건.set사건(사건);
				물건.set담당계(사건.get담당계());
			}

			String 부동산표시목록RawText = new 사건부동산표시목록Fetcher().download(사건);
			String 현황조사서RawText = new 사건현황조사서Fetcher().download(사건);
			if (현황조사서RawText != null) {
				Map<Integer, Collection<매각물건명세서>> list = new 사건임대차관계조사서Parser().parse(현황조사서RawText);
				update매각물건명세(new물건List, list);
				사건.set부동산의현황(new 사건현황조사서Fetcher().parse부동산의현황(현황조사서RawText));
				사건.set부동산점유관계(new 사건현황조사서Fetcher().parse부동산점유관계(현황조사서RawText));
				사건.set임대차관계내역(new 사건현황조사서Fetcher().parse임대차관계내역(현황조사서RawText));

			}

			try {
				String[] 문건송달내역RawText = new 사건문건송달내역Fetcher().download(사건);

			} catch (Exception e) {
				logger.info("사건의 문건 송달 내역을 정상적으로 처리하지 못했습니다. 대법원 사이트를 확인해주십시요");
				e.printStackTrace();
			}
			Job job = new Job();
			job.haveOld = haveOld;
			job.사건 = 사건;
			job.strategy = this;
			job.new물건List = new물건List;

			// list.add(job);
			if (do감정평가분석) {
				List<File> 감정평가서RawFiles = new 사건감정평가서Fetcher().download(사건);
				if (감정평가서RawFiles != null && 감정평가서RawFiles.size()>0) {
					for (File 감정평가서RawFile : 감정평가서RawFiles) {
						FileUploaderBG.getInstance().upload(사건.getPath(), 감정평가서RawFile.getName(), 감정평가서RawFile);
						
					}
					// 사진Collector.getInstance().add(사건, 감정평가서RawFile);

					fill감정평가요항(사건, 감정평가서RawFiles.get(0), new물건List);
				}
			}

			save(사건);

			new 물건목록Batch(사건, new물건List).execute();
		} catch (IOException e) {
			e.printStackTrace();
			logger.warn("사건 내역을 얻어오는데 실패했습니다.");
		}

		// EventNotifier.end사건();
		return false;
	}

	private void update매각물건명세(List<물건> get물건목록, Map<Integer, Collection<매각물건명세서>> list) {
		for (물건 물건 : get물건목록) {

			Collection<매각물건명세서> res = list.get(물건.get물건번호());
			if (res != null) {
				물건.set매각물건명세목록(res);
			}
		}
	}

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

	private void save(사건 사건) {
		사건Dao dao = (사건Dao) App.context.getBean("사건DAO");
		if (haveOld) {
			dao.update(사건);
		} else {
			dao.saveOrUpdate(사건);
		}
	}

	private 사건 validate(사건 event) {
		담당계 charge = event.get담당계();
		사건Dao dao = (사건Dao) App.context.getBean("사건DAO");
		당사자Dao dao3 = (당사자Dao) App.context.getBean("당사자DAO");
		사건 oldEvent = dao.find(event.get법원(), event.get사건번호());
		if (oldEvent != null) {
			haveOld = true;
			oldEvent.get당사자목록().clear();
			oldEvent.get당사자목록().addAll(event.get당사자목록());
			dao.update(oldEvent);

			물건Dao dao2 = (물건Dao) App.context.getBean("물건DAO");
			oldEvent.set물건목록(dao2.get(oldEvent));

			if (oldEvent.get담당계() == null || oldEvent.get담당계().getId() != charge.getId()) {
				oldEvent.set담당계(charge);
				oldEvent.merge(event);

				dao.saveOrUpdate(oldEvent);
			}

			// temp.setWorkset(event.get물건목록());
			return oldEvent;
		}
		// dao.saveOrUpdate(event);
		return event;
	}
}
