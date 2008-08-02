/*
 * 
 */
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
import net.narusas.aceauction.fetchers.대법원문건처리내역Fetcher;
import net.narusas.aceauction.fetchers.대법원사건Fetcher;
import net.narusas.aceauction.fetchers.대법원제시외건물Fetcher;
import net.narusas.aceauction.fetchers.대법원현황조사서Fetcher;
import net.narusas.aceauction.fetchers.사진Collector;
import net.narusas.aceauction.model.담당계;
import net.narusas.aceauction.model.부동산표시목록;
import net.narusas.aceauction.model.사건;
import net.narusas.aceauction.pdf.gamjung.GamjungParser;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;
import org.htmlparser.util.ParserException;

// TODO: Auto-generated Javadoc
/**
 * The Class 사건Builder.
 */
public class 사건Builder {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The listener. */
	private final BuildProgressListener listener;

	/**
	 * Instantiates a new 사건 builder.
	 * 
	 * @param listener
	 *            the listener
	 */
	public 사건Builder(BuildProgressListener listener) {
		this.listener = listener;
	}

	/**
	 * Build사건.
	 * 
	 * @param 담당계No
	 *            the 담당계No
	 * @param 사건
	 *            the 사건
	 * @param eventNos
	 *            the event nos
	 * 
	 * @throws Exception
	 *             the exception
	 */
	private void build사건(long 담당계No, 사건 사건, Long[] eventNos) throws Exception {
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

		try {
			대법원문건처리내역Fetcher temp = new 대법원문건처리내역Fetcher();
			temp.update(사건);
		} catch (Exception e) {
		}

		logger.info("대법원에서 물건내역(제시외건물)을 얻어옵니다");

		대법원제시외건물Fetcher f3 = new 대법원제시외건물Fetcher();
		f3.update(사건);

		logger.info("DB를 갱신합니다.");
		updateDB(담당계No, 사건);
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

	/**
	 * To string.
	 * 
	 * @param ex
	 *            the ex
	 * 
	 * @return the string
	 */
	private String toString(Throwable ex) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		return sw.getBuffer().toString();
	}

	/**
	 * Update db.
	 * 
	 * @param 담당계No
	 *            the 담당계No
	 * @param sagun
	 *            the sagun
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 * @throws HttpException
	 *             the http exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void updateDB(long 담당계No, 사건 sagun) throws SQLException,
			HttpException, IOException {
		try {
			logger.info("사건을 DB에 입력합니다. " + sagun.court.getName() + " "
					+ sagun.charge.get담당계이름() + " " + sagun.get사건번호());

			String name = sagun.get사건명();
			if (name.contains("자동차") || name.contains("선박")) {
				logger.info("사건이 부동산 사건이 아니므로 무시합니다. (" + name + ")");
				return;
			}
			사건 e = 사건.find(sagun.court, sagun.get사건번호());
			if (e != null) {
				logger.info("DB에 이미 존재하는 사건(id=" + e.getDbid()
						+ ")입니다. DB정보를 갱신합니다.");
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

	/**
	 * Update물건.
	 * 
	 * @param 사건
	 *            the 사건
	 * @param hasSagunPicture
	 *            the has sagun picture
	 * @param item
	 *            the item
	 * @param gamjungInfo
	 *            the gamjung info
	 * 
	 * @throws Exception
	 *             the exception
	 */
	private void update물건(사건 사건, boolean hasSagunPicture, 부동산표시목록 item,
			String[] gamjungInfo) throws Exception {
		물건Builder mulDB = new 물건Builder(listener);
		mulDB.update물건DBs(사건, hasSagunPicture, item, gamjungInfo);
	}

	/**
	 * Upload htm ls.
	 * 
	 * @param 사건
	 *            the 사건
	 * 
	 * @return the string[]
	 */
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
			uploader.upload(사건.get사건Path(), "CurrentStatus.html", file1);
			uploader.upload(사건.get사건Path(), "LandMark.html", file2);
			return new String[] { htmls[2], htmls[3] };
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
			return null;
		}
	}

	/**
	 * Build사건s.
	 * 
	 * @param 담당계
	 *            the 담당계
	 * 
	 * @throws HttpException
	 *             the http exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ParserException
	 *             the parser exception
	 */
	void build사건s(담당계 담당계) throws HttpException, IOException, ParserException {
		logger.info("사건 Fetcher를 생성합니다. ");
		대법원사건Fetcher fetcher = new 대법원사건Fetcher(담당계.get법원(), 담당계);
		logger.info("사건을 가져 옵니다. ");
		List<사건> sagun = fetcher.getSaguns();
		담당계.set사건(sagun);
	}

	/**
	 * Update사건 db.
	 * 
	 * @param 담당계No
	 *            the l
	 * @param sagun
	 *            the sagun
	 * @param eventNos
	 *            the event nos
	 */
	void update사건DB(long 담당계No, List<사건> sagun, Long[] eventNos) {
		listener.update사건Size(sagun.size());
		while (sagun.size() > 0) {
			try {
				build사건(담당계No, sagun.remove(0), eventNos);
			} catch (Exception e) {
				e.printStackTrace();
				logger.log(Level.FINE, "err", e);
			}
		}
	}

}
