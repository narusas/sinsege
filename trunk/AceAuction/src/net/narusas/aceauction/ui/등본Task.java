package net.narusas.aceauction.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

import net.narusas.aceauction.fetchers.��������Fetcher;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.����;
import net.narusas.aceauction.model.���;
import net.narusas.aceauction.pdf.itext.���ε���ں���;
import net.narusas.util.lang.NFile;

import com.lowagie.text.DocumentException;

public class �Task implements Runnable {

	static Logger logger = Logger.getLogger("log");

	private final Properties cfg;

	private ��� s;
	private ���� s2;

	public �Task(���� s2, Properties cfg) {
		this.s2 = s2;
		this.cfg = cfg;
	}

	public �Task(��� s, Properties cfg) {
		this.s = s;
		this.cfg = cfg;
	}

	public void run() {
		if (s != null) {
			fetch��ǵ��ε();
		} else if (s2 != null) {
			fetch���ǵ��ε();
		}

	}

	private void fetch���ǵ��ε() {
		logger.info("����� PDF Reader:" + cfg.getProperty("pdfreader"));
		logger.info("�ǹ����ε�� ���ɴϴ�");
		openURL(s2.get�ǹ����ε());

		logger.info("�������ε�� ���ɴϴ�");
		openURL(s2.get�������ε());

		// Progress.getInstance().progress("���ε ���ɴϴ�");
		// openURL(s2.get���հǹ����ε());
	}

	private void fetch��ǵ��ε() {
		try {

			logger.info("����� PDF Reader:" + cfg.getProperty("pdfreader"));
			logger.info("�ǹ����ε�� ���ɴϴ�");
			��������Fetcher fetcher = new ��������Fetcher();
			���� court = s.charge.get����();
//			File f = File.createTempFile("�ǹ����ε", ".pdf");
			File f = new File("�ǹ����ε.pdf");
			if (f.exists()){
				f.delete();
			}
			byte[] data = fetcher.fetch�ǹ����ε(court, s.getEventYear(), s
					.getEventNo());
			if (data != null && data.length != 0) {
				NFile.write(f, data);
				logger.info("�ǹ����ε�� ���ϴ�:" + f.getAbsolutePath());
				System.out.println(new String(data));
				Process ps = Runtime.getRuntime().exec(
						cfg.getProperty("pdfreader") + " "
								+ f.getAbsolutePath());
			}

			logger.info("�������ε�� ���ɴϴ�");

			f = File.createTempFile("�������ε", ".pdf");
			data = fetcher
					.fetch�������ε(court, s.getEventYear(), s.getEventNo());
			if (data != null && data.length != 0) {
				logger.info("�������ε�� ���ϴ�:" + f.getAbsolutePath());

				NFile.write(f, data);
				Process ps = Runtime.getRuntime().exec(
						cfg.getProperty("pdfreader") + " "
								+ f.getAbsolutePath());
			}

			logger.info("���ε�� ���ɴϴ�:" + f.getAbsolutePath());

			f = File.createTempFile("���ε", ".pdf");
			data = fetcher.fetch���ε(court, s.getEventYear(), s.getEventNo());
			if (data != null && data.length != 0) {
				logger.info("���ε�� ���ϴ�.");

				NFile.write(f, data);
				Process ps = Runtime.getRuntime().exec(
						cfg.getProperty("pdfreader") + " "
								+ f.getAbsolutePath());
			}

		} catch (Throwable e) {
			try {

				FileOutputStream f = new FileOutputStream(new File("log.txt"));
				e.printStackTrace(new PrintStream(f));
				f.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e2) {
				e.printStackTrace();
			}

			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}

	private void openURL(String url) {
		if (url == null) {
			return;
		}
		try {
			URL u = new URL(url);
			File f = File.createTempFile("���ε", ".pdf");
			byte[] data = net.narusas.util.lang.NInputStream.readBytes(u
					.openStream());
			if (data != null && data.length != 0) {
				logger.info("���ε�� ���ϴ�:" + f.getAbsolutePath());

				NFile.write(f, data);

				File converted = ���ε���ں���.convert(f.getAbsolutePath());

				Process ps = Runtime.getRuntime().exec(
						cfg.getProperty("pdfreader") + " "
								+ converted.getAbsolutePath());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
