package net.narusas.si.auction.app.attested;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import net.narusas.si.auction.app.App;
import net.narusas.si.auction.app.attested.parser.AtestedPDFParser;
import net.narusas.si.auction.builder.FileUploaderBG;
import net.narusas.si.auction.model.등기부등본;
import net.narusas.si.auction.model.등기부등본Item;
import net.narusas.si.auction.model.목록;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.dao.등기부등본Dao;
import net.narusas.si.auction.model.dao.물건Dao;
import net.narusas.util.lang.NFile;

import org.apache.commons.httpclient.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AtestedMonitor extends Thread {
	private Properties cfg;
	private JLabel label;
	private File workDir;
	private File storageDir;
	private 목록 target;
	private 등기부등본 atested;
	protected final Logger logger = LoggerFactory.getLogger("auction");

	public AtestedMonitor() {
		setDaemon(true);
		loadCFG();
		makeWorkDirs();
	}

	private void makeWorkDirs() {
		workDir = new File(cfg.getProperty("work"));
		mkdirs(workDir);

		storageDir = new File(cfg.getProperty("storage"));
		mkdirs(storageDir);
	}

	private void mkdirs(File f) {
		if (f.exists() == false) {
			f.mkdirs();
		}
	}

	private void loadCFG() {
		cfg = new Properties();
		try {
			Pattern p = Pattern.compile("([^=]+)=(.*)");
			Matcher m = p.matcher(NFile.getText(new File("cfg/atested.cfg")));
			while (m.find()) {
				String key = m.group(1).trim();
				String value = m.group(2).trim();
				System.out.println(key + " " + value);
				cfg.setProperty(key, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean alreadyDownloaded(목록 item) {
		String path = item.getGoods().getPath();

		String susffix = "land";
		if (item.getType().contains("건물")) {
			susffix = "building";
		}
		File dir = new File(storageDir, path);
		if (dir.exists() == false) {
			dir.mkdirs();
		}
		String pdfName = "PDF_" + susffix + ".pdf";
		File downloaded = new File(dir, pdfName);
		return downloaded.exists();
	}

	public void setStatus(JLabel label) {
		this.label = label;
	}

	public void setTarget(목록 target) {
		this.target = target;
		atested = new 등기부등본(target.getGoods(), target.getType());
	}

	@Override
	public void run() {
		if (target == null) {
			return;
		}
		while (true) {
			setStatus("...");

			if (checkFiles()) {
				break;
			}
			setStatus("감시중");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		setStatus("대기중");
		target = null;
	}

	private boolean checkFiles() {
		File[] list = workDir.listFiles();
		if (list == null || list.length == 0) {
			return false;
		}
		for (File file : list) {
			if (file.getName().toLowerCase().endsWith(".pdf")) {

				try {

					String path = target.getGoods().getPath();
					System.out.println("### Path:" + path);

					FileUploaderBG uploader = FileUploaderBG.getInstance();
					String susffix = "land";
					if (target.getType().contains("건물")) {
						susffix = "building";
					}
					File dir = new File(storageDir, path);
					if (dir.exists() == false) {
						dir.mkdirs();
					}
					String pdfName = "PDF_" + susffix + ".pdf";
					
					File downloaded = new File(dir, pdfName);
					logger.info("Target:"+downloaded.getAbsolutePath());
					System.out.println("Target:"+downloaded.getAbsolutePath());
					while(downloaded.exists()){
						logger.info("이미 파일이 존재합니다. 삭제를 시도합니다.");
						try {
							downloaded.delete();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						downloaded = new File(dir, pdfName);
						try {
							Thread.sleep(1000L);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						logger.info("파일을 옮깁니다. ");
						while(file.renameTo(downloaded)==false){
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					upload(path, pdfName, downloaded);
					try {
						
						물건 dummy = target.getGoods();
						
						물건Dao goodsDAO = (물건Dao) App.context.getBean("물건DAO");
						물건 goods = goodsDAO.find(dummy.get법원(), dummy.get담당계(), dummy.get사건(), dummy.get물건번호());
						atested.set물건(goods);
						List<등기부등본Item> items = new AtestedPDFParser().parse(downloaded);
						atested.setItems(items);
						등기부등본Dao dao = (등기부등본Dao) App.context.getBean("등기부등본DAO");
						dao.removeFor(goods, target.getType());
						dao.saveOrUpdate(atested);

					} catch (Exception e) {
						e.printStackTrace();
					}
//					upload(path, pdfName, downloaded);

					
				} catch (Exception e) {
					e.printStackTrace();
					logger.info(e.getMessage());
				}
				return true;
			}
		}
		return false;
	}

	private void upload(String path, String pdfName, File downloaded) throws HttpException, IOException {
		FileUploaderBG.getInstance().upload(path, pdfName, downloaded);
	}

	private void setStatus(final String string) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				label.setText(string);
			}
		});
	}

}
