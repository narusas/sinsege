/*
 * 
 */
package net.narusas.aceauction.data.builder;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.narusas.aceauction.data.DB;
import net.narusas.aceauction.data.FileUploaderBG;
import net.narusas.aceauction.fetchers.대법원감정평가서Fetcher;
import net.narusas.aceauction.fetchers.사진Collector;
import net.narusas.aceauction.model.사건;
import net.narusas.aceauction.pdf.gamjung.GamjungParser;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;

// TODO: Auto-generated Javadoc
/**
 * The Class 감정평가서Work.
 */
public class 감정평가서Work {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");
	
	/** The s. */
	private final 사건 s;
	
	/** The picture upload done. */
	private boolean pictureUploadDone;
	
	/** The info. */
	private String[] info;

	/**
	 * Instantiates a new 감정평가서 work.
	 * 
	 * @param 사건 the 사건
	 */
	public 감정평가서Work(사건 사건) {
		this.s = 사건;
	}

	/**
	 * Excute.
	 * 
	 * @throws Exception the exception
	 */
	public void excute() throws Exception {
		File f = upload감정평가서(s);
		pictureUploadDone = uploadPicture(f);
		info = parse감정평가서(s, f);
	}

	/**
	 * Picture upload done.
	 * 
	 * @return true, if successful
	 */
	public boolean pictureUploadDone() {
		return pictureUploadDone;
	}

	/**
	 * Gets the info.
	 * 
	 * @return the info
	 */
	public String[] getInfo() {
		return info;
	}

	/**
	 * Upload감정평가서.
	 * 
	 * @param s the s
	 * 
	 * @return the file
	 */
	private File upload감정평가서(사건 s) {
		if (hasAlreadyGamjung(s.getDbid())) {
			logger.info("이미 감정평가서가 업로드 되어 있습니다. 사진 업로드 과정을 중지합니다. ");
			return null;
		}
		대법원감정평가서Fetcher f = new 대법원감정평가서Fetcher();
		try {
			byte[] pdf = f.fetch(s.get감정평가서(), false);
			if (pdf == null) {
				logger.info("감정평가서가 없는 사건입니다");
				return null;
			}

			if (사진Collector.isPDFHeader(pdf)) {
				File file = File.createTempFile("감정평가서", ".pdf");
				NFile.write(file, pdf);
				FileUploaderBG uploader = FileUploaderBG.getInstance();
				logger.info("감정평가서를 업로드 합니다. ");
				uploader.upload(s.get사건Path(), "PDF_Judgement.pdf", file);
				updateSagunHaveGamjung(s.getDbid());
				file.deleteOnExit();
				return file;
			} else if (사진Collector.isPrivateFormat(pdf)) {
				File file = File.createTempFile("감정평가서", ".ddd");
				NFile.write(file, pdf);
				return file;
			}
			return null;

		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.log(Level.WARNING, "Fail to upload( )", e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Checks for already gamjung.
	 * 
	 * @param id the id
	 * 
	 * @return true, if successful
	 */
	boolean hasAlreadyGamjung(long id) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DB.createStatement();
			rs = stmt.executeQuery("SELECT has_gamjung FROM ac_event WHERE id="
					+ id + ";");
			rs.next();
			if (rs.getBoolean("has_gamjung") == true) {
				return true;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			DB.cleanup(rs, stmt);
		}
		return false;

	}

	/**
	 * Update sagun have gamjung.
	 * 
	 * @param id the id
	 */
	private void updateSagunHaveGamjung(long id) {
		PreparedStatement stmt = null;
		try {
			stmt = DB
					.prepareStatement("UPDATE ac_event SET has_gamjung=? WHERE id=?;");
			stmt.setBoolean(1, true);
			stmt.setLong(2, id);
			stmt.executeUpdate();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			DB.cleanup(stmt);
		}

	}

	/**
	 * Upload picture.
	 * 
	 * @param gamjungFile the gamjung file
	 * 
	 * @return true, if successful
	 * 
	 * @throws Exception the exception
	 */
	private boolean uploadPicture(File gamjungFile) throws Exception {

		String code = s.court.getCode();
		long 사건번호 = s.get사건번호();

		if (s.hasPictures()) {
			logger.info("이미 사진이 업로드 되어 있습니다. 사진 업로드 과정을 중지합니다. ");
			return true;
		}

		logger.info("사건의 사진을 수집하여 업로드 하는 과정입니다. ");
		boolean hasSaungPicture = false;

		사진Collector collector = new 사진Collector();
		try {
			List<File> files = collector.collect(gamjungFile, code, 사건번호);
			if (files.size() > 0) {
				hasSaungPicture = true;
			}
			FileUploaderBG uploader = FileUploaderBG.getInstance();
			int count = 0;
			for (File file : files) {
				String ext = file.getName().substring(
						file.getName().indexOf(".") + 1);
				uploader
						.upload(s.get사건Path(), "pic_" + count + "." + ext, file);
				count++;
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hasSaungPicture;
	}

	/**
	 * Parse감정평가서.
	 * 
	 * @param 사건 the 사건
	 * @param file the file
	 * 
	 * @return the string[]
	 */
	private String[] parse감정평가서(사건 사건, File file) {
		GamjungParser parser = new GamjungParser();

		try {
			parser.parse(file);
			String src = parser.getSrc();
			String date = parser.getDate();
			logger.info("감정시점:" + date + " 감정기관:" + src);
			return new String[] { src, date };
		} catch (Exception e) {
			e.printStackTrace();

		}
		return new String[] { "", "" };

	}

}
