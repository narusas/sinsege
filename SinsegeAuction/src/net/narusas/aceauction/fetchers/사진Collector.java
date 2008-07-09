/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import net.narusas.aceauction.pdf.jpedal.ExtractImages;

import org.apache.commons.httpclient.HttpException;

// TODO: Auto-generated Javadoc
/**
 * ���� ������ ������ Ŭ����.
 * 
 * @author narusas
 */
public class ����Collector {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("log");

	/**
	 * Collect.
	 * 
	 * @param gamjung the gamjung
	 * @param bub_cd the bub_cd
	 * @param sa_no the sa_no
	 * 
	 * @return the list< file>
	 * 
	 * @throws HttpException the http exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<File> collect(File gamjung, String bub_cd, long sa_no)
			throws HttpException, IOException {
		HashSet<File> temp = new HashSet<File>();

		logger.info("���(����=" + bub_cd + ", ��ǹ�ȣ=" + sa_no + ")�� ������ �����մϴ�. ");

		String path = "images/" + bub_cd + "/" + sa_no + "/";
		if (gamjung != null) {

			logger.info("�����򰡼��� ������ ������ �����մϴ�. ");
			byte[] header4 = read4bytes(gamjung);
			if (isPDFHeader(header4)) {
				ExtractImages extractor = new ExtractImages(gamjung
						.getAbsolutePath());
				path = extractor.getOutputDir();
			} else if (isPrivateFormat(header4)) {
				JpegExtractor extractor = new JpegExtractor(gamjung);
				extractor.extractImages();
				path = extractor.getResultPath();
			}

		}

		File root = new File(path);
		if (root.exists() == false) {
			root.mkdirs();
		}
		logger.info("�����������  ������ ������ �����մϴ�. ");
		�����_���_����Fetcher fetcher = new �����_���_����Fetcher();
		List<File> files = fetcher.fetch(path, bub_cd, sa_no);

		temp.addAll(files);

		File[] tempFiles = root.listFiles();

		for (File f : tempFiles) {
			temp.add(f);
		}

		logger.info("������ ������ ���� " + temp.size() + "�Դϴ�");
		List<File> res = new LinkedList<File>();
		res.addAll(temp);
		return res;
	}

	/**
	 * Checks if is private format.
	 * 
	 * @param header4 the header4
	 * 
	 * @return true, if is private format
	 */
	public static  boolean isPrivateFormat(byte[] header4) {
		byte[] target = {0x5a};
		for (int i = 0; i < target.length; i++) {
			if (header4[i] != target[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Read4bytes.
	 * 
	 * @param gamjung the gamjung
	 * 
	 * @return the byte[]
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private byte[] read4bytes(File gamjung) throws IOException {
		FileInputStream in = new FileInputStream(gamjung);
		byte[] data = new byte[4];
		in.read(data, 0, 4);
		in.close();
		return data;
	}

	/**
	 * Checks if is pDF header.
	 * 
	 * @param header4 the header4
	 * 
	 * @return true, if is pDF header
	 */
	public static  boolean isPDFHeader(byte[] header4) {
		byte[] target = "%PDF".getBytes();
		for (int i = 0; i < 4; i++) {
			if (header4[i] != target[i]) {
				return false;
			}
		}
		return true;
	}

}
