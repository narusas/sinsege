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
import net.narusas.aceauction.fetchers.����������򰡼�Fetcher;
import net.narusas.aceauction.fetchers.������⺻����Fetcher;
import net.narusas.aceauction.fetchers.��������ϳ���Fetcher;
import net.narusas.aceauction.fetchers.���������ó������Fetcher;
import net.narusas.aceauction.fetchers.��������Fetcher;
import net.narusas.aceauction.fetchers.��������ÿܰǹ�Fetcher;
import net.narusas.aceauction.fetchers.�������Ȳ���缭Fetcher;
import net.narusas.aceauction.fetchers.����Collector;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.�ε���ǥ�ø��;
import net.narusas.aceauction.model.���;
import net.narusas.aceauction.pdf.gamjung.GamjungParser;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;
import org.htmlparser.util.ParserException;

// TODO: Auto-generated Javadoc
/**
 * The Class ���Builder.
 */
public class ���Builder {

	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/** The listener. */
	private final BuildProgressListener listener;

	/**
	 * Instantiates a new ��� builder.
	 * 
	 * @param listener
	 *            the listener
	 */
	public ���Builder(BuildProgressListener listener) {
		this.listener = listener;
	}

	/**
	 * Build���.
	 * 
	 * @param ����No
	 *            the ����No
	 * @param ���
	 *            the ���
	 * @param eventNos
	 *            the event nos
	 * 
	 * @throws Exception
	 *             the exception
	 */
	private void build���(long ����No, ��� ���, Long[] eventNos) throws Exception {
		if (eventNos != null && ���.isIn(eventNos) == false) {
			return;
		}
		DB.reConnect();

		���.retrieveFromDB();

		������⺻����Fetcher f = new ������⺻����Fetcher();
		logger.info("��������� �⺻������ ���ɴϴ�");
		f.update(���);
		logger.info("��������� ���ϳ����� ���ɴϴ�");
		��������ϳ���Fetcher f2 = new ��������ϳ���Fetcher();
		f2.update(���);

		try {
			���������ó������Fetcher temp = new ���������ó������Fetcher();
			temp.update(���);
		} catch (Exception e) {
		}

		logger.info("��������� ���ǳ���(���ÿܰǹ�)�� ���ɴϴ�");

		��������ÿܰǹ�Fetcher f3 = new ��������ÿܰǹ�Fetcher();
		f3.update(���);

		logger.info("DB�� �����մϴ�.");
		updateDB(����No, ���);
		String[] xml = uploadHTMLs(���);

		�ε���ǥ�ø�� item = null;
		if (xml != null && xml.length >= 2) {
			item = new �ε���ǥ�ø��(xml[1]);
		} else {
			item = new �ε���ǥ�ø��();
		}

		�����򰡼�Work work = new �����򰡼�Work(���);
		work.excute();

		update����(���, work.pictureUploadDone(), item, work.getInfo());

		FileUploader.getInstance().waitDone();
		listener.progress(BuildProgressListener.LEVEL_���);
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
	 * @param ����No
	 *            the ����No
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
	private void updateDB(long ����No, ��� sagun) throws SQLException,
			HttpException, IOException {
		try {
			logger.info("����� DB�� �Է��մϴ�. " + sagun.court.getName() + " "
					+ sagun.charge.get�����̸�() + " " + sagun.get��ǹ�ȣ());

			String name = sagun.get��Ǹ�();
			if (name.contains("�ڵ���") || name.contains("����")) {
				logger.info("����� �ε��� ����� �ƴϹǷ� �����մϴ�. (" + name + ")");
				return;
			}
			��� e = ���.find(sagun.court, sagun.get��ǹ�ȣ());
			if (e != null) {
				logger.info("DB�� �̹� �����ϴ� ���(id=" + e.getDbid()
						+ ")�Դϴ�. DB������ �����մϴ�.");
				sagun.setDbid(e.getDbid());
				sagun.update();
			} else {
				logger.info("���ο� ����Դϴ�. DB�� �߰��մϴ�. ");
				sagun.insert();
			}

			��Ǵ����Builder db = new ��Ǵ����Builder();
			db.update��Ǵ����DB(sagun, listener);

		} catch (Throwable ex) {
			logger.info(toString(ex));
			ex.printStackTrace();
		}

	}

	/**
	 * Update����.
	 * 
	 * @param ���
	 *            the ���
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
	private void update����(��� ���, boolean hasSagunPicture, �ε���ǥ�ø�� item,
			String[] gamjungInfo) throws Exception {
		����Builder mulDB = new ����Builder(listener);
		mulDB.update����DBs(���, hasSagunPicture, item, gamjungInfo);
	}

	/**
	 * Upload htm ls.
	 * 
	 * @param ���
	 *            the ���
	 * 
	 * @return the string[]
	 */
	private String[] uploadHTMLs(��� ���) {
		logger.info("HTML���ϵ��� ���ε� �մϴ�. ");

		try {
			�������Ȳ���缭Fetcher f = new �������Ȳ���缭Fetcher();
			String[] htmls = f.fetchAll(���, ���.get��Ȳ���缭());
			String ��Ȳ���������� = htmls[0];
			String �ε���ǥ�ø�� = htmls[1];
			File file1 = File.createTempFile("CurrentStatus", ".html");
			NFile.write(file1, ��Ȳ����������, "euc-kr");

			File file2 = File.createTempFile("LandMark", ".html");
			NFile.write(file2, �ε���ǥ�ø��, "euc-kr");

			FileUploader uploader = FileUploader.getInstance();
			uploader.upload(���.get���Path(), "CurrentStatus.html", file1);
			uploader.upload(���.get���Path(), "LandMark.html", file2);
			return new String[] { htmls[2], htmls[3] };
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
			return null;
		}
	}

	/**
	 * Build���s.
	 * 
	 * @param ����
	 *            the ����
	 * 
	 * @throws HttpException
	 *             the http exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ParserException
	 *             the parser exception
	 */
	void build���s(���� ����) throws HttpException, IOException, ParserException {
		logger.info("��� Fetcher�� �����մϴ�. ");
		��������Fetcher fetcher = new ��������Fetcher(����.get����(), ����);
		logger.info("����� ���� �ɴϴ�. ");
		List<���> sagun = fetcher.getSaguns();
		����.set���(sagun);
	}

	/**
	 * Update��� db.
	 * 
	 * @param ����No
	 *            the l
	 * @param sagun
	 *            the sagun
	 * @param eventNos
	 *            the event nos
	 */
	void update���DB(long ����No, List<���> sagun, Long[] eventNos) {
		listener.update���Size(sagun.size());
		while (sagun.size() > 0) {
			try {
				build���(����No, sagun.remove(0), eventNos);
			} catch (Exception e) {
				e.printStackTrace();
				logger.log(Level.FINE, "err", e);
			}
		}
	}

}
