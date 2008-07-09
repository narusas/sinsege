/*
 * 
 */
package net.narusas.aceauction.fetchers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.narusas.util.lang.NFile;

// TODO: Auto-generated Javadoc
/**
 * The Class JpegExtractor.
 */
public class JpegExtractor {

	/** The gamjung. */
	private final File gamjung;
	
	/** The path. */
	private File path;

	/**
	 * Instantiates a new jpeg extractor.
	 * 
	 * @param gamjung the gamjung
	 */
	public JpegExtractor(File gamjung) {
		this.gamjung = gamjung;
		path = new File("jpgextractor");
		path.mkdirs();
		File[] toDeletes = path.listFiles();
		for (File file : toDeletes) {
			file.delete();
		}
	}

	/**
	 * Extract images.
	 * 
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void extractImages() throws FileNotFoundException, IOException {
		byte[] data = NFile.readBytes(gamjung);

		int[] startTarget = { 0xFF, 0xD8 };
		int[] endTarget = { 0xFF, 0xD9 };

		int start = 0;
		int end = 0;
		int count = 0;
		for (int i = 0; i < data.length - 1; i++) {
			if ((data[i] & 0xFF) == startTarget[0]
					&& (data[i + 1] & 0xFF) == startTarget[1]) {
				System.out.println("Jpeg Start:" + i);
				start = i;
			}

			if ((data[i] & 0xFF) == endTarget[0]
					&& (data[i + 1] & 0xFF) == endTarget[1]) {
				System.out.println("JPeg End:" + i);
				end = i + 1;
				int len = end - start;
				byte[] buf = new byte[len];
				System.arraycopy(data, start, buf, 0, len);
				File img = new File(path, count + ".jpg");
				NFile.write(img, buf);
				count++;
			}
		}
	}

	/**
	 * Gets the result path.
	 * 
	 * @return the result path
	 */
	public String getResultPath() {
		return path.getAbsolutePath();
	}

}
