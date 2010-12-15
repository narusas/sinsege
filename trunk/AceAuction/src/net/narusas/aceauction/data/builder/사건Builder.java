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
import net.narusas.aceauction.fetchers.��������Fetcher;
import net.narusas.aceauction.fetchers.��������ÿܰǹ�Fetcher;
import net.narusas.aceauction.fetchers.�������Ȳ���缭Fetcher;
import net.narusas.aceauction.fetchers.����Collector;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.�ε���ǥ�ø��;
import net.narusas.aceauction.model.���;
import net.narusas.aceauction.pdf.gamjung.GamjungParser;
import net.narusas.si.auction.model.�������;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;
import org.htmlparser.util.ParserException;

public class ���Builder {

	static Logger logger = Logger.getLogger("log");

	private final BuildProgressListener listener;

	public ���Builder(BuildProgressListener listener) {
		this.listener = listener;
	}

	private void build���(long l, ��� ���, Long[] eventNos) throws Exception {
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

		logger.info("��������� ���ǳ���(���ÿܰǹ�)�� ���ɴϴ�");

		��������ÿܰǹ�Fetcher f3 = new ��������ÿܰǹ�Fetcher();
		f3.update(���);

		logger.info("DB�� �����մϴ�.");
		updateDB(l, ���);
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

	private String toString(Throwable ex) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		return sw.getBuffer().toString();
	}

	private void updateDB(long l, ��� sagun) throws SQLException, HttpException, IOException {
		try {
			logger.info("����� DB�� �Է��մϴ�. " + sagun.court.getName() + " " + sagun.charge.get�����̸�() + " "
					+ sagun.get��ǹ�ȣ());

			String name = sagun.get��Ǹ�();
			������� type = �������.�ε���;

			if (name.contains("�ڵ���")) {
				type = �������.����;
			} else if (name.contains("����")) {
				type = �������.����;
			}

			else if (name.contains("��")) {
				type = �������.�߱�;
			}

			��� e = ���.find(sagun.court, sagun.get��ǹ�ȣ(),type);
			if (e != null) {
				logger.info("DB�� �̹� �����ϴ� ���(id=" + e.getDbid() + ")�Դϴ�. DB������ �����մϴ�.");
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

	private void update����(��� ���, boolean hasSagunPicture, �ε���ǥ�ø�� item, String[] gamjungInfo) throws Exception {
		����Builder mulDB = new ����Builder(listener);
		mulDB.update����DBs(���, hasSagunPicture, item, gamjungInfo);
	}

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
			uploader.upload(���.get���Path(), "CurrentStatus", file1);
			uploader.upload(���.get���Path(), "LandMark", file2);
			return new String[] { htmls[2], htmls[3] };
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
			return null;
		}
	}

	void build���s(���� ����) throws HttpException, IOException, ParserException {
		logger.info("��� Fetcher�� �����մϴ�. ");
		��������Fetcher fetcher = new ��������Fetcher(����.get����(), ����);
		logger.info("����� ���� �ɴϴ�. ");
		List<���> sagun = fetcher.getSaguns();
		����.set���(sagun);
	}

	void update���DB(long l, List<���> sagun, Long[] eventNos) {
		listener.update���Size(sagun.size());
		while (sagun.size() > 0) {
			try {
				build���(l, sagun.remove(0), eventNos);
			} catch (Exception e) {
				e.printStackTrace();
				logger.log(Level.FINE, "err", e);
			}
		}
	}

}
