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
import net.narusas.aceauction.fetchers.����������򰡼�Fetcher;
import net.narusas.aceauction.fetchers.����Collector;
import net.narusas.aceauction.model.���;
import net.narusas.aceauction.pdf.gamjung.GamjungParser;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;

// TODO: Auto-generated Javadoc
/**
 * The Class �����򰡼�Work.
 */
public class �����򰡼�Work {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");
	
	/** The s. */
	private final ��� s;
	
	/** The picture upload done. */
	private boolean pictureUploadDone;
	
	/** The info. */
	private String[] info;

	/**
	 * Instantiates a new �����򰡼� work.
	 * 
	 * @param ��� the ���
	 */
	public �����򰡼�Work(��� ���) {
		this.s = ���;
	}

	/**
	 * Excute.
	 * 
	 * @throws Exception the exception
	 */
	public void excute() throws Exception {
		File f = upload�����򰡼�(s);
		pictureUploadDone = uploadPicture(f);
		info = parse�����򰡼�(s, f);
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
	 * Upload�����򰡼�.
	 * 
	 * @param s the s
	 * 
	 * @return the file
	 */
	private File upload�����򰡼�(��� s) {
		if (hasAlreadyGamjung(s.getDbid())) {
			logger.info("�̹� �����򰡼��� ���ε� �Ǿ� �ֽ��ϴ�. ���� ���ε� ������ �����մϴ�. ");
			return null;
		}
		����������򰡼�Fetcher f = new ����������򰡼�Fetcher();
		try {
			byte[] pdf = f.fetch(s.get�����򰡼�(), false);
			if (pdf == null) {
				logger.info("�����򰡼��� ���� ����Դϴ�");
				return null;
			}

			if (����Collector.isPDFHeader(pdf)) {
				File file = File.createTempFile("�����򰡼�", ".pdf");
				NFile.write(file, pdf);
				FileUploaderBG uploader = FileUploaderBG.getInstance();
				logger.info("�����򰡼��� ���ε� �մϴ�. ");
				uploader.upload(s.get���Path(), "PDF_Judgement.pdf", file);
				updateSagunHaveGamjung(s.getDbid());
				file.deleteOnExit();
				return file;
			} else if (����Collector.isPrivateFormat(pdf)) {
				File file = File.createTempFile("�����򰡼�", ".ddd");
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
		long ��ǹ�ȣ = s.get��ǹ�ȣ();

		if (s.hasPictures()) {
			logger.info("�̹� ������ ���ε� �Ǿ� �ֽ��ϴ�. ���� ���ε� ������ �����մϴ�. ");
			return true;
		}

		logger.info("����� ������ �����Ͽ� ���ε� �ϴ� �����Դϴ�. ");
		boolean hasSaungPicture = false;

		����Collector collector = new ����Collector();
		try {
			List<File> files = collector.collect(gamjungFile, code, ��ǹ�ȣ);
			if (files.size() > 0) {
				hasSaungPicture = true;
			}
			FileUploaderBG uploader = FileUploaderBG.getInstance();
			int count = 0;
			for (File file : files) {
				String ext = file.getName().substring(
						file.getName().indexOf(".") + 1);
				uploader
						.upload(s.get���Path(), "pic_" + count + "." + ext, file);
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
	 * Parse�����򰡼�.
	 * 
	 * @param ��� the ���
	 * @param file the file
	 * 
	 * @return the string[]
	 */
	private String[] parse�����򰡼�(��� ���, File file) {
		GamjungParser parser = new GamjungParser();

		try {
			parser.parse(file);
			String src = parser.getSrc();
			String date = parser.getDate();
			logger.info("��������:" + date + " �������:" + src);
			return new String[] { src, date };
		} catch (Exception e) {
			e.printStackTrace();

		}
		return new String[] { "", "" };

	}

}
