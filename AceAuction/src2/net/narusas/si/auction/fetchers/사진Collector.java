package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.narusas.si.auction.builder.FileUploaderBG;
import net.narusas.si.auction.model.사건;
import net.narusas.si.auction.pdf.gamjung.ExtractImages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 사진Collector extends Thread {
	final Logger logger = LoggerFactory.getLogger("auction");
	private static 사진Collector instance;
	List<사진Collection> remains = new LinkedList<사진Collection>();

	private 사진Collector() {
		super("사진CollectorThread");
		setDaemon(true);
	}

	public static 사진Collector getInstance() {
		if (instance == null) {
			instance = new 사진Collector();
			instance.start();
		}
		return instance;
	}

	public void add(사건 사건, File 감정평가서File) {
		remains.add(new 사진Collection(사건, 감정평가서File));
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
			synchronized (remains) {

				if (remains.size() == 0) {
					continue;
				}

			}
			collect(remains.remove(0));
		}
	}

	private void collect(final 사진Collection item) {

		File gamjung = item.감정평가서파일;
		String path = null;
		try {
			byte[] header4 = read4bytes(gamjung);
			if (isPDFHeader(header4)) {
				logger.info("PDF 파일에서 사진을 추출합니다.");
				ExtractImages extractor = new ExtractImages(gamjung.getAbsolutePath()) {
					@Override
					protected String createPath(String name) {
						return "download/" + item.사건.getPath();
					}
				};
				path = extractor.getOutputDir();
				File root = new File(path);
				File[] files = root.listFiles();
				int count = 1;
				for (File file : files) {
					FileUploaderBG.getInstance().upload(item.사건.getPath(), "pic_" + count + ".jpg", file);
					count++;
				}
				logger.info("Path:" + path);
			}
			// else if (isPrivateFormat(header4)) {
			// JpegExtractor extractor = new JpegExtractor(gamjung);
			// extractor.extractImages();
			// path = extractor.getResultPath();
			// }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isPrivateFormat(byte[] header4) {
		byte[] target = { 0x5a };
		for (int i = 0; i < target.length; i++) {
			if (header4[i] != target[i]) {
				return false;
			}
		}
		return true;
	}

	private byte[] read4bytes(File gamjung) throws IOException {
		FileInputStream in = new FileInputStream(gamjung);
		byte[] data = new byte[4];
		in.read(data, 0, 4);
		in.close();
		return data;
	}

	public static boolean isPDFHeader(byte[] header4) {
		byte[] target = "%PDF".getBytes();
		for (int i = 0; i < 4; i++) {
			if (header4[i] != target[i]) {
				return false;
			}
		}
		return true;
	}
}

class 사진Collection {
	사건 사건;
	File 감정평가서파일;

	public int count = 1;

	public 사진Collection(사건 사건, File 감정평가서파일) {
		super();
		this.사건 = 사건;
		this.감정평가서파일 = 감정평가서파일;
	}

	public 사건 get사건() {
		return 사건;
	}

	public void set사건(사건 사건) {
		this.사건 = 사건;
	}

	public File get감정평가서파일() {
		return 감정평가서파일;
	}

	public void set감정평가서파일(File 감정평가서파일) {
		this.감정평가서파일 = 감정평가서파일;
	}

}
