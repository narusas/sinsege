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

import net.narusas.aceauction.fetchers.지지옥션Fetcher;
import net.narusas.aceauction.model.물건;
import net.narusas.aceauction.model.법원;
import net.narusas.aceauction.model.사건;
import net.narusas.aceauction.pdf.itext.등기부등본날자변경;
import net.narusas.util.lang.NFile;

import com.lowagie.text.DocumentException;

public class 등본Task implements Runnable {

	static Logger logger = Logger.getLogger("log");

	private final Properties cfg;

	private 사건 s;
	private 물건 s2;

	public 등본Task(물건 s2, Properties cfg) {
		this.s2 = s2;
		this.cfg = cfg;
	}

	public 등본Task(사건 s, Properties cfg) {
		this.s = s;
		this.cfg = cfg;
	}

	public void run() {
		if (s != null) {
			fetch사건등기부등본();
		} else if (s2 != null) {
			fetch물건등기부등본();
		}

	}

	private void fetch물건등기부등본() {
		logger.info("사용할 PDF Reader:" + cfg.getProperty("pdfreader"));
		logger.info("건물등기부등본을 얻어옵니다");
		openURL(s2.get건물등기부등본());

		logger.info("토지등기부등본을 얻어옵니다");
		openURL(s2.get토지등기부등본());

		// Progress.getInstance().progress("등기부등본 얻어옵니다");
		// openURL(s2.get집합건물등기부등본());
	}

	private void fetch사건등기부등본() {
		try {

			logger.info("사용할 PDF Reader:" + cfg.getProperty("pdfreader"));
			logger.info("건물등기부등본을 얻어옵니다");
			지지옥션Fetcher fetcher = new 지지옥션Fetcher();
			법원 court = s.charge.get법원();
//			File f = File.createTempFile("건물등기부등본", ".pdf");
			File f = new File("건물등기부등본.pdf");
			if (f.exists()){
				f.delete();
			}
			byte[] data = fetcher.fetch건물등기부등본(court, s.getEventYear(), s
					.getEventNo());
			if (data != null && data.length != 0) {
				NFile.write(f, data);
				logger.info("건물등기부등본을 엽니다:" + f.getAbsolutePath());
				System.out.println(new String(data));
				Process ps = Runtime.getRuntime().exec(
						cfg.getProperty("pdfreader") + " "
								+ f.getAbsolutePath());
			}

			logger.info("토지등기부등본을 얻어옵니다");

			f = File.createTempFile("토지등기부등본", ".pdf");
			data = fetcher
					.fetch토지등기부등본(court, s.getEventYear(), s.getEventNo());
			if (data != null && data.length != 0) {
				logger.info("토지등기부등본을 엽니다:" + f.getAbsolutePath());

				NFile.write(f, data);
				Process ps = Runtime.getRuntime().exec(
						cfg.getProperty("pdfreader") + " "
								+ f.getAbsolutePath());
			}

			logger.info("등기부등본을 얻어옵니다:" + f.getAbsolutePath());

			f = File.createTempFile("등기부등본", ".pdf");
			data = fetcher.fetch등기부등본(court, s.getEventYear(), s.getEventNo());
			if (data != null && data.length != 0) {
				logger.info("등기부등본을 엽니다.");

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
			File f = File.createTempFile("등기부등본", ".pdf");
			byte[] data = net.narusas.util.lang.NInputStream.readBytes(u
					.openStream());
			if (data != null && data.length != 0) {
				logger.info("등기부등본을 엽니다:" + f.getAbsolutePath());

				NFile.write(f, data);

				File converted = 등기부등본날자변경.convert(f.getAbsolutePath());

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
