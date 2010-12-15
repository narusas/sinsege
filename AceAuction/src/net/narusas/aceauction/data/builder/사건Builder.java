package net.narusas.aceauction.data.builder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.data.FileUploader;
import net.narusas.aceauction.data.FileUploaderBG;
import net.narusas.aceauction.fetchers.대법원감정평가서Fetcher;
import net.narusas.aceauction.fetchers.대법원기본내역Fetcher;
import net.narusas.aceauction.fetchers.대법원기일내역Fetcher;
import net.narusas.aceauction.fetchers.대법원사건Fetcher;
import net.narusas.aceauction.fetchers.대법원제시외건물Fetcher;
import net.narusas.aceauction.fetchers.대법원현황조사서Fetcher;
import net.narusas.aceauction.fetchers.사진Collector;
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.부동산표시목록;
import net.narusas.aceauction.model.사건;
import net.narusas.aceauction.pdf.gamjung.GamjungParser;
import net.narusas.si.auction.model.사건종류;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;
import org.htmlparser.util.ParserException;

public class 사건Builder {

	static Logger logger = Logger.getLogger("log");

	private final BuildProgressListener listener;

	public 사건Builder(BuildProgressListener listener) {
		this.listener = listener;
	}

	private void build사건(long l, 사건 사건, Long[] eventNos) throws Exception {
		if (eventNos != null && 사건.isIn(eventNos) == false) {
			return;
		}
		DB.reConnect();

		사건.retrieveFromDB();

		대법원기본내역Fetcher f = new 대법원기본내역Fetcher();
		logger.info("대법원에서 기본내역을 얻어옵니다");
		f.update(사건);
		logger.info("대법원에서 기일내역을 얻어옵니다");
		대법원기일내역Fetcher f2 = new 대법원기일내역Fetcher();
		f2.update(사건);

		logger.info("대법원에서 물건내역(제시외건물)을 얻어옵니다");

		대법원제시외건물Fetcher f3 = new 대법원제시외건물Fetcher();
		f3.update(사건);

		logger.info("DB를 갱신합니다.");
		updateDB(l, 사건);
		String[] xml = uploadHTMLs(사건);

		부동산표시목록 item = null;
		if (xml != null && xml.length >= 2) {
			item = new 부동산표시목록(xml[1]);
		} else {
			item = new 부동산표시목록();
		}

		감정평가서Work work = new 감정평가서Work(사건);
		work.excute();

		update물건(사건, work.pictureUploadDone(), item, work.getInfo());

		FileUploader.getInstance().waitDone();
		listener.progress(BuildProgressListener.LEVEL_사건);
	}

	private String toString(Throwable ex) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		return sw.getBuffer().toString();
	}

	private void updateDB(long l, 사건 sagun) throws SQLException, HttpException, IOException {
		try {
			logger.info("사건을 DB에 입력합니다. " + sagun.court.getName() + " " + sagun.charge.get담당계이름() + " "
					+ sagun.get사건번호());

			String name = sagun.get사건명();
			사건종류 type = 사건종류.부동산;

			if (name.contains("자동차")) {
				type = 사건종류.선박;
			} else if (name.contains("선박")) {
				type = 사건종류.선박;
			}

			else if (name.contains("중")) {
				type = 사건종류.중기;
			}

			사건 e = 사건.find(sagun.court, sagun.get사건번호(),type);
			if (e != null) {
				logger.info("DB에 이미 존재하는 사건(id=" + e.getDbid() + ")입니다. DB정보를 갱신합니다.");
				sagun.setDbid(e.getDbid());
				sagun.update();
			} else {
				logger.info("새로운 사건입니다. DB에 추가합니다. ");
				sagun.insert();
			}

			사건당사자Builder db = new 사건당사자Builder();
			db.update사건당사자DB(sagun, listener);

		} catch (Throwable ex) {
			logger.info(toString(ex));
			ex.printStackTrace();
		}

	}

	private void update물건(사건 사건, boolean hasSagunPicture, 부동산표시목록 item, String[] gamjungInfo) throws Exception {
		물건Builder mulDB = new 물건Builder(listener);
		mulDB.update물건DBs(사건, hasSagunPicture, item, gamjungInfo);
	}

	private String[] uploadHTMLs(사건 사건) {
		logger.info("HTML파일들을 업로드 합니다. ");

		try {
			대법원현황조사서Fetcher f = new 대법원현황조사서Fetcher();
			String[] htmls = f.fetchAll(사건, 사건.get현황조사서());
			String 현황및점유관계 = htmls[0];
			String 부동산표시목록 = htmls[1];
			File file1 = File.createTempFile("CurrentStatus", ".html");
			NFile.write(file1, 현황및점유관계, "euc-kr");

			File file2 = File.createTempFile("LandMark", ".html");
			NFile.write(file2, 부동산표시목록, "euc-kr");

			FileUploader uploader = FileUploader.getInstance();
			uploader.upload(사건.get사건Path(), "CurrentStatus", file1);
			uploader.upload(사건.get사건Path(), "LandMark", file2);
			return new String[] { htmls[2], htmls[3] };
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
			return null;
		}
	}

	void build사건s(담당계 담당계) throws HttpException, IOException, ParserException {
		logger.info("사건 Fetcher를 생성합니다. ");
		대법원사건Fetcher fetcher = new 대법원사건Fetcher(담당계.get법원(), 담당계);
		logger.info("사건을 가져 옵니다. ");
		List<사건> sagun = fetcher.getSaguns();
		담당계.set사건(sagun);
	}

	void update사건DB(long l, List<사건> sagun, Long[] eventNos) {
		listener.update사건Size(sagun.size());
		while (sagun.size() > 0) {
			try {
				build사건(l, sagun.remove(0), eventNos);
			} catch (Exception e) {
				e.printStackTrace();
				logger.log(Level.FINE, "err", e);
			}
		}
	}

}
